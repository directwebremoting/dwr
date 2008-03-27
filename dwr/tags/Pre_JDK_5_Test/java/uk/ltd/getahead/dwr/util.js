/**
 *
 */
function clearChildNodes(id)
{
    var elem = document.getElementById(id);
    while (elem.childNodes.length > 0)
    {
        elem.removeChild(elem.firstChild);
    }
}

/**
 *
 */
function drawTable(tbodyID, dataArray, textFuncs)
{
    // assure bug-free redraw in Geck engine by
    // letting window show cleared table
    if (navigator.product && navigator.product == "Gecko")
    {
        setTimeout(function() { drawTableInner(tbodyID, dataArray, textFuncs); }, 0);
    }
    else
    {
        drawTableInner(tbodyID, dataArray, textFuncs);
    }
}

/**
 *
 */
function showById(id)
{
    document.getElementById(id).style.display = 'block';
}

/**
 *
 */
function hideById(id)
{
    document.getElementById(id).style.display = 'none';
}

/**
 * Toggle an elements visibility
 */
function toggleDisplay(id)
{
    var ele = document.getElementById(id);
    if (ele.style.display == 'none')
    {
        ele.style.display = 'block';
    }
    else
    {
        ele.style.display = 'none';
    }
}

/**
 *
 */
function setCSSClass(id, cssclass)
{
    var element = document.getElementById(id);
    if (element)
    {
        element.className = cssclass;
    }
}

/**
 *
 */
function drawTableInner(tbodyID, dataArray, textFuncs)
{
    var frag = document.createDocumentFragment();

    // loop through data source
    for (var i = 0; i < dataArray.length; i++)
    {
        var tr = document.createElement("tr");

        for (var j = 0; j < textFuncs.length; j++)
        {
            var td = document.createElement("td");
            tr.appendChild(td);

            var func = textFuncs[j];
            if (typeof func == "string" || func instanceof String)
            {
                var text = document.createTextNode(func);
                td.appendChild(text);
            }
            else
            {
                var reply = func(dataArray[i]);
                if (isHTMLElement(reply))
                {
                    td.appendChild(reply);
                }
                else
                {
                    td.innerHTML = reply;
                    //var text = document.createTextNode(reply);
                    //td.appendChild(text);
                }
            }
        }

        frag.appendChild(tr);
    }

    var tbody = document.getElementById(tbodyID);
    tbody.appendChild(frag);
}

/**
 *
 */
function setEnabled(id, state)
{
    var ele = document.getElementById(id);
    if (ele == null)
    {
        alert("Element id: "+id+" not found.");
        throw id;
    }

    // If we want to get funky and disable divs and spans by changing the font
    // colour or something then we might want to check the element type before
    // we make assumptions, but in the mean time ...
    // if (isHTMLInputElement(ele)) { ...

    ele.disabled = !state;
    ele.readonly = !state;
    if (is_ie)
    {
        if (state)
        {
            ele.style.backgroundColor = "White";
        }
        else
        {
            // This is WRONG but the hack will do for now.
            ele.style.backgroundColor = "Scrollbar";
        }
    }
}

var agt = navigator.userAgent.toLowerCase();
var is_major = parseInt(navigator.appVersion);
var is_minor = parseFloat(navigator.appVersion);

var is_nav = ((agt.indexOf('mozilla') != -1) && (agt.indexOf('spoofer') == -1)
             && (agt.indexOf('compatible') == -1) && (agt.indexOf('opera') == -1)
             && (agt.indexOf('webtv') == -1) && (agt.indexOf('hotjava') == -1));

var is_nav4up = (is_nav && (is_major >= 4));
var is_nav6up = (is_nav && (is_major >= 5));
var is_gecko = (agt.indexOf('gecko') != -1);

var is_ie     = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));
var is_ie3    = (is_ie && (is_major < 4));
var is_ie4    = (is_ie && (is_major == 4) && (agt.indexOf("msie 4")!=-1) );
var is_ie4up  = (is_ie && (is_major >= 4));
var is_ie5    = (is_ie && (is_major == 4) && (agt.indexOf("msie 5.0")!=-1) );
var is_ie5_5  = (is_ie && (is_major == 4) && (agt.indexOf("msie 5.5") !=-1));
var is_ie5up  = (is_ie && !is_ie3 && !is_ie4);
var is_ie5_5up =(is_ie && !is_ie3 && !is_ie4 && !is_ie5);
var is_ie6    = (is_ie && (is_major == 4) && (agt.indexOf("msie 6.")!=-1) );
var is_ie6up  = (is_ie && !is_ie3 && !is_ie4 && !is_ie5 && !is_ie5_5);

/**
 *
 */
function isHTMLElement(ele)
{
    // There must be a better way
    if (is_gecko)
    {
        return typeof ele == "object" && ele instanceof HTMLElement;
    }
    else
    {
        // assume it is an html element if has an outerhtml property
        return typeof ele == "object" && ele.nodeName != null;
    }
}

/**
 *
 */
function isHTMLInputElement(ele)
{
    // There must be a better way
    if (is_gecko)
    {
        return typeof ele == "object" && ele instanceof HTMLInputElement;
    }
    else
    {
        return typeof ele == "object" && ele.nodeName.toLowerCase() == "input";
    }
}

/**
 *
 */
function isHTMLTextAreaElement(ele)
{
    // There must be a better way
    if (is_gecko)
    {
        return typeof ele == "object" && ele instanceof HTMLTextAreaElement;
    }
    else
    {
        return typeof ele == "object" && ele.nodeName.toLowerCase() == "textarea";
    }
}

/**
 *
 */
function isHTMLSelectElement(ele)
{
    // There must be a better way
    if (is_gecko)
    {
        return typeof ele == "object" && ele instanceof HTMLSelectElement;
    }
    else
    {
        return typeof ele == "object" && ele.nodeName.toLowerCase() == "select";
    }
}

/**
 *
 */
function setValue(id, val)
{
    if (val == null)
    {
        val = "";
    }

    var ele = document.getElementById(id);
    if (ele == null)
    {
        alert("Element id: "+id+" not found.");
        throw id;
    }

    if (isHTMLSelectElement(ele))
    {
        // search through the values
        var found  = false;
        for (var i = 0; i < ele.options.length; i++)
        {
            if (ele.options[i].value == val)
            {
                ele.options[i].selected = true;
                found = true;
            }
            else
            {
                ele.options[i].selected = false;
            }
        }

        // If that fails then try searching through the visible text
        if (found)
        {
            return;
        }

        for (var i = 0; i < ele.options.length; i++)
        {
            if (ele.options[i].text == val)
            {
                ele.options[i].selected = true;
                break;
            }
        }

        return;
    }

    if (isHTMLInputElement(ele))
    {
        switch (ele.type)
        {
        case "check-box":
        case "radio":
            ele.checked = (val == true);
            return;

        case "hidden":
        case "text":
            ele.value = val;
            return;

        default:
            alert("Not sure how to setValue on a input element of type " + ele.type);
            ele.value = val;
            return;
        }
    }

    if (isHTMLTextAreaElement(ele))
    {
        ele.value = val;
        return;
    }

    if (isHTMLElement(ele))
    {
        ele.innerHTML = val;
        return;
    }

    alert("Not sure how to setValue on a " + ele);
    ele.innerHTML = val;
}

/**
 *
 */
function getValue(id)
{
    var ele = document.getElementById(id);

    if (ele == null)
    {
        alert("Element id: "+id+" not found.");
        throw id;
    }

    if (isHTMLSelectElement(ele))
    {
        // This is a bit of a scam because it assumes single select
        // but I'm not sure how we should treat multi-select.
        var sel = ele.selectedIndex;
        if (sel != -1)
        {
            return ele.options[sel].value;
        }
        else
        {
            return "";
        }
    }

    if (isHTMLInputElement(ele))
    {
        switch (ele.type)
        {
        case "check-box":
        case "radio":
            return ele.checked;

        case "hidden":
        case "text":
            return ele.value;

        default:
            alert("Not sure how to setValue on a input element of type " + ele.type);
            return ele.value;
        }
    }

    if (isHTMLTextAreaElement(ele))
    {
        return ele.value;
    }

    if (isHTMLElement(ele))
    {
        return ele.innerHTML;
    }

    alert("Not sure how to getValue from a " + ele);
    return ele.innerHTML;
}

/**
 *
 */
function getText(id)
{
    var ele = document.getElementById(id);

    if (ele == null)
    {
        alert("Element id: "+id+" not found.");
        throw id;
    }

    if (!isHTMLSelectElement(ele))
    {
        alert("getText() can only be used with select elements. Attempt to use: " + detailedTypeOf(ele));
        throw ele;
    }

    // This is a bit of a scam because it assumes single select
    // but I'm not sure how we should treat multi-select.
    var sel = ele.selectedIndex;
    if (sel != -1)
    {
        return ele.options[sel].text;
    }
    else
    {
        return "";
    }
}

/**
 *
 */
function setValues(map)
{
    for (var property in map)
    {
        var value = map[property];
        setValue(property, value);
    }
}

/**
 *
 */
function fillList(id, data, valueprop, textprop)
{
    var ele = document.getElementById(id);

    if (ele == null)
    {
        alert("Element id: "+id+" not found.");
        throw id;
    }

    if (!isHTMLSelectElement(ele))
    {
        alert("fillList() can only be used with select elements. Attempt to use: " + detailedTypeOf(ele));
        throw ele;
    }

    // Empty the list
    ele.options.length = 0;

    // Bail if we have no data
    if (data == null)
    {
        return;
    }

    // Loop through the data that we do have
    for (var i = 0; i < data.length; i++)
    {
        var text;
        var value;

        if (valueprop != null)
        {
            if (textprop != null)
            {
                text = data[i][textprop];
                value = data[i][valueprop];
            }
            else
            {
                value = data[i][valueprop];
                text = value;
            }
        }
        else
        {
            if (textprop != null)
            {
                text = data[i][textprop];
                value = text;
            }
            else
            {
                text = toDescriptiveString(data[i]);
                value = text;
            }
        }

        var opt = new Option(text, value);
        ele.options[ele.options.length] = opt;
    }
}

/**
 * Ensure a function is called when the page is loaded
 */
function callOnLoad(load)
{
    if (window.addEventListener)
    {
        window.addEventListener("load", load, false);
    }
    else if (window.attachEvent)
    {
        window.attachEvent("onload", load);
    }
    else
    {
        window.onload = load;
    }
}

/**
 * Alter an rows in a table that have a class of zebra to have classes of either
 * oddrow or evenrow alternately.
 * This is probably not the best place for this method, but I dont want to have
 * to fight with multiple onload functions.
 */
function alternateRowColors()
{
    var tables = document.getElementsByTagName("table");
    var rowCount = 0;

    for (var i = 0; i < tables.length; i++)
    {
        var table = tables.item(i);
        var rows = table.getElementsByTagName("tr");

        for (var j = 0; j < rows.length; j++)
        {
            var row = rows.item(j);
            if (row.className == "zebra")
            {
                if (rowCount % 2)
                {
                    row.className = 'oddrow';
                }
                else
                {
                    row.className = 'evenrow';
                }

                rowCount++;
            }
        }

        rowCount = 0;
    }
}

/**
 * A better toString that the default for an Object
 */
function toDescriptiveString(object)
{
    if (typeof object != "object")
    {
        return object.toString();
    }

    /*
    if (data instanceof Boolean || data instanceof Number ||
        data instanceof String || data instanceof Date)
    {
        return object.toString();
    }
    */

    if (object.toString != Object.prototype.toString)
    {
        return object.toString();
    }

    var reply = "" + detailedTypeOf(object) + " {";
    var i = 0;
    for (var prop in object)
    {
        var value = "" + object[prop];
        if (value.length > 13)
        {
            value = value.substring(0, 10) + "...";
        }

        reply += prop;
        reply += ":";
        reply += value;
        reply += ", ";

        i++;
        if (i > 5)
        {
            reply += "...";
            break;
        }
    }
    reply += "}";

    return reply;
}

function detailedTypeOf(x)
{
    var reply = typeof x;

    if (reply == "object")
    {
        reply = Object.prototype.toString.apply(x);  // Returns "[object class]"
        reply = reply.substring(8, reply.length-1);  // Just get the class bit
    }

    return reply;
} 
