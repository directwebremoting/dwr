// engine.js

// A function to be called before requests are marshalled. Can be null.
var dwrPreHook;

// A function to be called after replies are received. Can be null.
var dwrPostHook;

// A function to call if something fails.
var dwrErrorHandler;

// A map of all the known current calls
var dwrCalls = new Object();

// When we are dreaming up variable names, this is a number that we use
// as a basis to ensure uniqueness.
// WARNING: if it is possible for dwrExecute to be called again before
// marshalling has finished on the first then this will break, but I
// *think* that is not possible.
var dwrParamCount = 0;

// Warning: if you can see EL-like (${...}) expressions on the next line, don't be fooled into thinking it is EL.
// Check the source in DWRServlet for the hack. Maybe we will do something more EL like in the future.
// If you can't see the ${...} then you are probably looking at the processed source
var dwrUrlBase = '${request.contextPath}${request.servletPath}/exec';

// The error handler function
function dwrSetErrorHandler(handler)
{
    dwrErrorHandler = handler;
}

// A pre execute hook
function dwrSetPreHook(handler)
{
    dwrPreHook = handler;
}

// A post execute hook
function dwrSetPostHook(handler)
{
    dwrPostHook = handler;
}

// Called when the replies are received
function dwrHandleResponse(id, reply)
{
    var call = dwrCalls[id];
    if (call == null)
    {
        var known = "";
        for (call in dwrCalls)
        {
            known += call + "\n";
        }
        alert("Internal Error: Call with id='"+id+"' unknown.\nI do know about the following:\n"+known);
        return;
    }
    dwrCalls[id] = undefined;

    if (call.iframe != null)
    {
        call.iframe.parentNode.removeChild(call.iframe);
    }

    if (dwrPostHook != null)
    {
        dwrPostHook();
    }

    if (call.callback == null)
    {
        if (reply != null)
        {
            alert("Missing callback for reply "+reply);
        }
    }
    else
    {
        call.callback(reply);
    }
}

// Called when errors are received
function dwrHandleError(id, reason)
{
    var call = dwrCalls[id];
    if (call == null)
    {
        alert("Internal Error: Call with id="+id+" unknown.");
        return;
    }
    dwrCalls[id] = undefined;

    if (call.iframe != null)
    {
        call.iframe.parentNode.removeChild(call.iframe);
    }

    if (dwrPostHook != null)
    {
        dwrPostHook();
    }

    if (dwrErrorHandler != null)
    {
        dwrErrorHandler(reason);
    }
    else
    {
        alert(reason);
    }
}

// Get a unique ID for this call
function dwrGetID()
{
    var random = Math.floor(Math.random() * 10001);
    return (random + "_" + new Date().getTime()).toString();
}

// Send a request to the server
function dwrExecute(func, classname, methodname, vararg_params)
{
    var call = new Object();
    call.callback = func
    call.id = dwrGetID();
    dwrCalls[call.id] = call;

    if (dwrPreHook != null)
    {
        dwrPreHook();
    }

    if (func != null && typeof func != "function" && typeof func != "object")
    {
        alert("Supplied callback function is neither null nor a function: "+func);
        return;
    }

    call.callback = func;

    // Build a map containing all the values to pass to the server.
    dwrParamCount = 0;

    call.map = new Object();
    call.map.classname = classname;
    call.map.methodname = methodname;
    call.map.id = call.id;
    call.map.xml = true;
    for (var i=3; i<dwrExecute.arguments.length; i++)
    {
        dwrMarshall(call.map, new Array(), dwrExecute.arguments[i], "param" + (i-3));
    }

    // Get setup for XMLHttpRequest if possible
    if (window.XMLHttpRequest)
    {
        call.req = new XMLHttpRequest();
    }
    else if (window.ActiveXObject)
    {
        // I've seen code that asks for the following:
        // call.req = new ActiveXObject("Msxml2.XMLHTTP");
        call.req = new ActiveXObject("Microsoft.XMLHTTP");
        if (!call.req)
        {
            alert("Creation of Microsoft.XMLHTTP failed. Reverting to iframe method.");
        }
    }

    if (call.req)
    {
        // Proceed using XMLHttpRequest
        var query = "";
        for (var prop in call.map)
        {
            query += prop + "=" + call.map[prop] + "\n";
        }

        call.url = dwrUrlBase;

        call.req.onreadystatechange = function() { dwrStateChange(call); };
        call.req.open("POST", call.url, true);
        call.req.send(query);
    }
    else
    {
        // Proceed using iframe
        var query = "";
        for (var prop in call.map)
        {
            var lookup = call.map[prop];
            query += lookup.name + "=" + lookup.convert + "&";
        }
        query = query.substring(0, query.length - 1);

        call.url = dwrUrlBase + "?" + query;

        call.iframe = document.createElement('iframe');
        call.iframe.setAttribute('id', 'dwr-iframe');
        call.iframe.setAttribute('style', 'width:0px; height:0px; border:0px;');
        call.iframe.setAttribute('src', call.url);
        document.body.appendChild(call.iframe);
    }
}

// Marshall a data item
function dwrMarshall(output, referto, data, name)
{
    if (data == null)
    {
        output[name] = "null:null";
        return;
    }

    switch (typeof data)
    {
    case "boolean":
        output[name] = "boolean:" + data;
        break;

    case "number":
        output[name] = "number:" + data;
        break;

    case "string":
        output[name] = "string:" + encodeURIComponent(data);
        break;

    case "object":
        if (data instanceof Boolean)
        {
            output[name] = "Boolean:" + data;
        }
        else if (data instanceof Number)
        {
            output[name] = "Number:" + data;
        }
        else if (data instanceof String)
        {
            output[name] = "String:" + encodeURIComponent(data);
        }
        else if (data instanceof Date)
        {
            output[name] = "Date:[ " + data.getUTCFullYear() + ", " + data.getUTCMonth() + ", " + data.getUTCDate() + ", " + data.getUTCHours() + ", " + data.getUTCMinutes() + ", " + data.getUTCSeconds() + ", " + data.getUTCMilliseconds() + "]";
        }
        else
        {
            // This is the beginning of the types that can recurse so we need to
            // check that we've not marshalled this object before.
            // We'd like to do:
            //   var lookup = referto[data];
            // However hashmaps in javascript appear to use the hash values of
            // the *string* versions of the objects used as keys so all objects
            // count as the same thing.
            // So we need to have referto as an array and go through it
            // sequentially checking for equality with data
            var lookup;
            for (var i = 0; i < referto.length; i++)
            {
                if (referto[i].data === data)
                {
                    lookup = referto[i];
                    break;
                }
            }

            if (lookup != null)
            {
                output[name] = "reference:" + lookup.name;
                return;
            }

            referto.push({ data:data, name:name });

            if (data instanceof Array)
            {
                var reply = "Array:["
                for (var i = 0; i < data.length; i++)
                {
                    if (i != 0)
                    {
                        reply += ",";
                    }

                    dwrParamCount++;
                    var childName = "c"+dwrParamCount;
                    dwrMarshall(output, referto, data[i], childName);
                    reply += "reference:";
                    reply += childName;
                }
                reply += "]";

                output[name] = reply;
            }
            else
            {
                 // treat anything else as an associative array (map)
                var reply = "Object:{"
                for (element in data)
                {
                    dwrParamCount++;
                    var childName = "c"+dwrParamCount;
                    dwrMarshall(output, referto, data[element], childName);

                    reply += element;
                    reply += ":reference:";
                    reply += childName;
                    reply += ", ";
                }
                reply = reply.substring(0, reply.length - 2);
                reply += "}";

                output[name] = reply;
            }
            break;
        }

    default:
        alert("Unexpected type: " + typeof data + ", attempting default converter");
        output[name] = "default:" + data;
    }
}

// Called by XMLHttpRequest to indicate that something has happened
function dwrStateChange(call)
{
    if (call.req.readyState == 4)
    {
        //try
        {
            if (call.req.status == 200)
            {
                eval(call.req.responseText);
            }
            else
            {
                dwrHandleError(call.id, call.req.responseText);
            }
        }
        //catch (ex)
        {
        //    dwrHandleError(call.id, ex);
        }
    }
}
