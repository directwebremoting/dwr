// engine.js

/**
 * Declare a constructor function to which we can add real functions.
 * @constructor
 */
function DWREngine()
{
}

/**
 * The constants to pick the remoting method
 */
DWREngine.XMLHttpRequest = 1;
DWREngine.IFrame = 2;

/**
 * A function to be called before requests are marshalled. Can be null.
 * @private
 */
DWREngine._preHook = null;

/**
 * A function to be called after replies are received. Can be null.
 * @private
 */
DWREngine._postHook = null;

/**
 * A map of all the known current calls
 * @private
 */
DWREngine._calls = new Object();

/**
 * What is the default remoting method
 * @private
 */
DWREngine._method = DWREngine.XMLHttpRequest;

/**
 * When we are dreaming up variable names, this is a number that we use
 * as a basis to ensure uniqueness.
 * WARNING: if it is possible for execute to be called again before
 * marshalling has finished on the first then this will break, but I
 * think that is not possible.
 * @private
 */
DWREngine._paramCount = 0;

/**
 * The base URL that we use to contact the server. This is dependent on the
 * server on which DWR is deployed so it is dynamic.
 * Warning: if you can see EL-like (${...}) expressions on the next line, don't
 * be fooled into thinking it is EL. Check the DWRServlet source for the hack.
 * Maybe we will do something more EL like in the future.
 * If you can't see the ${...} then you are probably looking at the processed
 * source
 * @private
 */
DWREngine._urlBase = '${request.contextPath}${request.servletPath}/exec';

/**
 * The error handler function
 * @param handler A function to call with single an error parameter on failure
 */
DWREngine.setErrorHandler = function(handler)
{
    DWREngine._errorHandler = handler;
}

/**
 * The Pre-Hook is called before any DWR remoting is done.
 * Pre hooks can be useful for displaying "please wait" messages.
 * @param handler A function to call with no params before remoting
 * @see DWREngine.setPostHook
 */
DWREngine.setPreHook = function(handler)
{
    DWREngine._preHook = handler;
}

/**
 * The Post-Hook is called after any DWR remoting is done.
 * Pre hooks can be useful for removing "please wait" messages.
 * @param handler A function to call with no params after remoting
 * @see DWREngine.setPreHook
 */
DWREngine.setPostHook = function(handler)
{
    DWREngine._postHook = handler;
}

/**
 * Set the preferred remoting method.
 * setMethod does not guarantee that the selected method will be used, just that
 * we will try that method first.
 * @param newmethod One of DWREngine.XMLHttpRequest or DWREngine.IFrame
 */
DWREngine.setMethod = function(newmethod)
{
    if (newmethod != DWREngine.XMLHttpRequest && newmethod != DWREngine.IFrame)
    {
        alert("Remoting method must be one of DWREngine.XMLHttpRequest or DWREngine.IFrame");
        throw newmethod;
    }

    DWREngine._method = newmethod;
}

/**
 * A function to call if something fails.
 * @private
 */
DWREngine._errorHandler = function(data)
{
    if (typeof data == "object" && data.name == "Error" && data.description)
    {
        alert("Error: " + data.description);
    }
    else
    {
        alert(data);
    }
}

/**
 * Called when the replies are received.
 * This method is called by Javascript that is emitted by server
 * @private
 */
DWREngine.handleResponse = function(id, reply)
{
    var call = DWREngine._calls[id];
    if (call == null)
    {
        var known = "";
        for (call in DWREngine._calls)
        {
            known += call + "\n";
        }
        alert("Internal Error: Call with id='"+id+"' unknown.\nI do know about the following:\n"+known);
        return;
    }
    DWREngine._calls[id] = undefined;

    if (call.iframe != null)
    {
        call.iframe.parentNode.removeChild(call.iframe);
    }

    if (DWREngine._postHook != null)
    {
        DWREngine._postHook();
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
        // Error handlers inside here indicate an error that is nothing to do
        // with DWR so we handle them differently.
        try
        {
            call.callback(reply);
        }
        catch (ex)
        {
            DWREngine._errorHandler(ex);
        }
    }
}

/**
 * Called when errors are received.
 * This method is called by Javascript that is emitted by server
 * @private
 */
DWREngine.handleError = function(id, reason)
{
    var call = DWREngine._calls[id];
    if (call != null)
    {
        DWREngine._calls[id] = undefined;

        if (call.iframe != null)
        {
            call.iframe.parentNode.removeChild(call.iframe);
        }
    }
    else
    {
        // Things are going wrong so alerting probably does not make sense
        // alert("Internal Error: Call with id="+id+" unknown.");
    }

    if (DWREngine._postHook != null)
    {
        DWREngine._postHook();
    }

    DWREngine._errorHandler(reason);
}

/**
 * Send a request to the server
 * This method is called by Javascript that is emitted by server
 * @private
 */
DWREngine.execute = function(func, classname, methodname, vararg_params)
{
    if (func != null && typeof func != "function" && typeof func != "object")
    {
        alert("Supplied callback function is neither null nor a function: "+func);
        throw func;
    }

    var call = new Object();
    call.callback = func

    // Get a unique ID for this call
    var random = Math.floor(Math.random() * 10001);
    call.id = (random + "_" + new Date().getTime()).toString();

    DWREngine._calls[call.id] = call;

    if (DWREngine._preHook != null)
    {
        DWREngine._preHook();
    }

    call.callback = func;

    // Build a map containing all the values to pass to the server.
    DWREngine._paramCount = 0;

    call.map = new Object();
    call.map.classname = classname;
    call.map.methodname = methodname;
    call.map.id = call.id;
    for (var i=3; i<arguments.length; i++)
    {
        DWREngine._marshall(call.map, new Array(), arguments[i], "param" + (i-3));
    }

    // Get setup for XMLHttpRequest if possible
    if (DWREngine._method == DWREngine.XMLHttpRequest)
    {
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
    }

    if (call.req)
    {
        call.map.xml = true;

        // Proceed using XMLHttpRequest
        var query = "";
        for (var prop in call.map)
        {
            query += prop + "=" + call.map[prop] + "\n";
        }

        call.url = DWREngine._urlBase;

        call.req.onreadystatechange = function() { DWREngine._stateChange(call); };
        call.req.open("POST", call.url, true);
        call.req.send(query);
    }
    else
    {
        call.map.xml = false;

        // Proceed using iframe
        var query = "";
        for (var prop in call.map)
        {
            var lookup = call.map[prop];
            query += lookup.name + "=" + lookup.convert + "&";
        }
        query = query.substring(0, query.length - 1);

        call.url = DWREngine._urlBase + "?" + query;

        call.iframe = document.createElement('iframe');
        call.iframe.setAttribute('id', 'dwr-iframe');
        call.iframe.setAttribute('style', 'width:0px; height:0px; border:0px;');
        call.iframe.setAttribute('src', call.url);
        document.body.appendChild(call.iframe);
    }
}

/**
 * Marshall a data item
 * @private
 */
DWREngine._marshall = function(output, referto, data, name)
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

                    DWREngine._paramCount++;
                    var childName = "c" + DWREngine._paramCount;
                    DWREngine._marshall(output, referto, data[i], childName);
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
                for (var element in data)
                {
                    DWREngine._paramCount++;
                    var childName = "c" + DWREngine._paramCount;
                    DWREngine._marshall(output, referto, data[element], childName);

                    reply += element;
                    reply += ":reference:";
                    reply += childName;
                    reply += ", ";
                }
                reply = reply.substring(0, reply.length - 2);
                reply += "}";

                output[name] = reply;
            }
        }
        break;

    default:
        alert("Unexpected type: " + typeof data + ", attempting default converter");
        output[name] = "default:" + data;
    }
}

/**
 * Called by XMLHttpRequest to indicate that something has happened
 * @private
 */
DWREngine._stateChange = function(call)
{
    if (call.req.readyState == 4)
    {
        if (call.req.status)
        {
            try
            {
                if (call.req.status == 200)
                {
                    eval(call.req.responseText);
                }
                else
                {
                    DWREngine.handleError(call.id, call.req.responseText);
                }
            }
            catch (ex)
            {
                DWREngine.handleError(call.id, ex);
            }
        }
        else
        {
            DWREngine.handleError(call.id, "No response from remote server.");
        }
    }
}

/**
 * @deprecated
 * The error handler function
 */
function dwrSetErrorHandler(handler)
{
    dwrDeprecated();
    DWREngine.setErrorHandler(handler);
}

/**
 * @deprecated
 * A pre execute hook
 */
function dwrSetPreHook(handler)
{
    dwrDeprecated();
    DWREngine.setPreHook(handler);
}

/**
 * @deprecated
 * A post execute hook
 */
function dwrSetPostHook(handler)
{
    dwrDeprecated();
    DWREngine.setPostHook(handler);
}

/**
 * Inform the users that the function they just called is deprecated.
 * @private
 */
function dwrDeprecated()
{
    alert("dwrXxx() functions are deprecated. Please convert to DWREngine.xxx()");
}
