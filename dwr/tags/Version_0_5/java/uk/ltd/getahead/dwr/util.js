
/**
 * Declare a constructor function to which we can add real functions.
 * @constructor
 */
function DWRUtil()
{
}

/**
 * Browser detection code.
 * This is eeevil, but the official way [if (window.someFunc) ...] does not
 * work when browsers differ in rendering ability rather than the use of someFunc()
 * For internal use only.
 */
DWRUtil._agent   = navigator.userAgent.toLowerCase();
DWRUtil._isGecko = (DWRUtil._agent.indexOf('gecko') != -1);
DWRUtil._isIE    = ((DWRUtil._agent.indexOf("msie") != -1) && (DWRUtil._agent.indexOf("opera") == -1));

/**
 * Set the CSS display style to 'block'
 */
DWRUtil.getElementById = function(id)
{
    if (document.getElementById)
    {
        return document.getElementById(id);
    }

    if (document.all)
    {
        return document.all[e];
    }

    throw "Can't use document.getElementById or document.all";
}

/**
 * Set the CSS display style to 'block'
 */
DWRUtil.showById = function(id)
{
    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("showById() can't find an element with id: " + id + ".");
        throw id;
    }

    ele.style.display = 'block';
}

/**
 * Set the CSS display style to 'none'
 */
DWRUtil.hideById = function(id)
{
    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("hideById() can't find an element with id: " + id + ".");
        throw id;
    }

    ele.style.display = 'none';
}

/**
 * Toggle an elements visibility
 */
DWRUtil.toggleDisplay = function(id)
{
    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("toggleDisplay() can't find an element with id: " + id + ".");
        throw id;
    }

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
 * Set the CSS class for an element
 */
DWRUtil.setCSSClass = function(id, cssclass)
{
    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("setCSSClass() can't find an element with id: " + id + ".");
        throw id;
    }

    if (ele)
    {
        ele.className = cssclass;
    }
}

/**
 * Remove all the children of a given node.
 * Most useful for dynamic tables where you clearChildNodes() on the tbody
 * element.
 */
DWRUtil.clearChildNodes = function(id)
{
    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("clearChildNodes() can't find an element with id: " + id + ".");
        throw id;
    }

    while (ele.childNodes.length > 0)
    {
        ele.removeChild(ele.firstChild);
    }
}

/**
 * Remove all the options from a select list (specified by id) and replace with
 * elements in an array of objects. The value and text of each option are taken
 * from the valueprop and textprop parameters.
 * If both are left empty then the object itself will be used.
 */
DWRUtil.fillList = function(id, data, valueprop, textprop)
{
    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("fillList() can't find an element with id: " + id + ".");
        throw id;
    }

    if (!DWRUtil.isHTMLSelectElement(ele))
    {
        alert("fillList() can only be used with select elements. Attempt to use: " + DWRUtil.detailedTypeOf(ele));
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
                text = DWRUtil.toDescriptiveString(data[i]);
                value = text;
            }
        }

        var opt = new Option(text, value);
        ele.options[ele.options.length] = opt;
    }
}

/**
 * TODO: This should probably be renamed fillTable()
 * Under the tbody (given by id) create a row for each element in the dataArray
 * and for that row create one cell for each function in the textFuncs array
 * by passing the rows object (from the dataArray) to the given function.
 * The return from the function is used to populate the cell.
 */
DWRUtil.drawTable = function(tbodyID, dataArray, textFuncs)
{
    // assure bug-free redraw in Geck engine by
    // letting window show cleared table
    if (navigator.product && navigator.product == "Gecko")
    {
        setTimeout(function() { DWRUtil.drawTableInner(tbodyID, dataArray, textFuncs); }, 0);
    }
    else
    {
        DWRUtil.drawTableInner(tbodyID, dataArray, textFuncs);
    }
}

/**
 * Internal function to help rendering tables
 */
DWRUtil.drawTableInner = function(tbodyID, dataArray, textFuncs)
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
                if (DWRUtil.isHTMLElement(reply))
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

    var tbody = DWRUtil.getElementById(tbodyID);
    tbody.appendChild(frag);
}

/**
 * Visually enable or diable an element.
 */
DWRUtil.setEnabled = function(id, state)
{
    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("setEnabled() can't find an element with id: " + id + ".");
        throw id;
    }

    // If we want to get funky and disable divs and spans by changing the font
    // colour or something then we might want to check the element type before
    // we make assumptions, but in the mean time ...
    // if (DWRUtil.isHTMLInputElement(ele)) { ... }

    ele.disabled = !state;
    ele.readonly = !state;
    if (DWRUtil._isIE)
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

/**
 * Is the given node an HTML element?
 */
DWRUtil.isHTMLElement = function(ele)
{
    // There must be a better way
    if (DWRUtil._isGecko)
    {
        return typeof ele == "object" && ele instanceof HTMLElement;
    }
    else
    {
        // assume it is an html element if has an outerhtml property
        return typeof ele == "object" && ele.nodeName;
    }
}

/**
 * Is the given node an HTML input element?
 */
DWRUtil.isHTMLInputElement = function(ele)
{
    // There must be a better way
    if (DWRUtil._isGecko)
    {
        return typeof ele == "object" && ele instanceof HTMLInputElement;
    }
    else
    {
        return typeof ele == "object" && ele.nodeName && ele.nodeName.toLowerCase() == "input";
    }
}

/**
 * Is the given node an HTML textarea element?
 */
DWRUtil.isHTMLTextAreaElement = function(ele)
{
    // There must be a better way
    if (DWRUtil._isGecko)
    {
        return typeof ele == "object" && ele instanceof HTMLTextAreaElement;
    }
    else
    {
        return typeof ele == "object" && ele.nodeName && ele.nodeName.toLowerCase() == "textarea";
    }
}

/**
 * Is the given node an HTML select element?
 */
DWRUtil.isHTMLSelectElement = function(ele)
{
    // There must be a better way
    if (DWRUtil._isGecko)
    {
        return typeof ele == "object" && ele instanceof HTMLSelectElement;
    }
    else
    {
        return typeof ele == "object" && ele.nodeName && ele.nodeName.toLowerCase() == "select";
    }
}

/**
 * Set the value for the given id to the specified val.
 * This method works for selects (where the option with a matching value and
 * not text is selected), input elements (including textareas) divs and spans.
 */
DWRUtil.setValue = function(id, val)
{
    if (val == null)
    {
        val = "";
    }

    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("setValue() can't find an element with id: " + id + ".");
        throw id;
    }

    if (DWRUtil.isHTMLSelectElement(ele))
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

    if (DWRUtil.isHTMLInputElement(ele))
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

    if (DWRUtil.isHTMLTextAreaElement(ele))
    {
        ele.value = val;
        return;
    }

    if (DWRUtil.isHTMLElement(ele))
    {
        ele.innerHTML = val;
        return;
    }

    alert("Not sure how to setValue on a " + ele);
    ele.innerHTML = val;
}

/**
 * The counterpart to setValue() - read the current value for a given element.
 * This method works for selects (where the option with a matching value and
 * not text is selected), input elements (including textareas) divs and spans.
 */
DWRUtil.getValue = function(id)
{
    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("getValue() can't find an element with id: " + id + ".");
        throw id;
    }

    if (DWRUtil.isHTMLSelectElement(ele))
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

    if (DWRUtil.isHTMLInputElement(ele))
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
            alert("Not sure how to getValue on a input element of type " + ele.type);
            return ele.value;
        }
    }

    if (DWRUtil.isHTMLTextAreaElement(ele))
    {
        return ele.value;
    }

    if (DWRUtil.isHTMLElement(ele))
    {
        return ele.innerHTML;
    }

    alert("Not sure how to getValue from a " + ele);
    return ele.innerHTML;
}

/**
 * getText() is like getValue() with the except that it only works for selects
 * where it reads the text of an option and not it's value.
 */
DWRUtil.getText = function(id)
{
    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("getText() can't find an element with id: " + id + ".");
        throw id;
    }

    if (!DWRUtil.isHTMLSelectElement(ele))
    {
        alert("getText() can only be used with select elements. Attempt to use: " + DWRUtil.detailedTypeOf(ele));
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
 * Given a map, call setValue() for all the entries in the map using the key
 * of each entry as an id.
 */
DWRUtil.setValues = function(map)
{
    for (var property in map)
    {
        // This is done by setValue, but we can provide better debug by doing
        // it here.
        var ele = DWRUtil.getElementById(property);
        if (ele == null)
        {
            alert("setValues() can't find an element with id: " + property + ".");
            throw id;
        }

        var value = map[property];
        DWRUtil.setValue(property, value);
    }
}

/**
 * Ensure a function is called when the page is loaded
 */
DWRUtil.callOnLoad = function(load)
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
DWRUtil.alternateRowColors = function()
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
DWRUtil.toDescriptiveString = function(object)
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

    var reply = "" + DWRUtil.detailedTypeOf(object) + " {";
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

/**
 * Like typeOf except that more information for an object is returned other
 * than "object"
 */
DWRUtil.detailedTypeOf = function(x)
{
    var reply = typeof x;

    if (reply == "object")
    {
        reply = Object.prototype.toString.apply(x);  // Returns "[object class]"
        reply = reply.substring(8, reply.length-1);  // Just get the class bit
    }

    return reply;
}

/**
 * Setup a GMail style loading message
 */
DWRUtil.useLoadingMessage = function()
{
    var disabledZone = document.createElement('div');
    disabledZone.setAttribute('id', 'disabledZone');
    disabledZone.style.position = "absolute";
    disabledZone.style.zIndex = "1000";
    disabledZone.style.left = "0px";
    disabledZone.style.top = "0px";
    disabledZone.style.width = "100%";
    disabledZone.style.height = "100%";
    document.body.appendChild(disabledZone);

    var messageZone = document.createElement('div');
    messageZone.setAttribute('id', 'messageZone');
    messageZone.style.position = "absolute";
    messageZone.style.top = "0px";
    messageZone.style.right = "0px";
    messageZone.style.background = "red";
    messageZone.style.color = "white";
    messageZone.style.fontFamily = "Arial,Helvetica,sans-serif";
    messageZone.style.padding = "4px";
    disabledZone.appendChild(messageZone);

    var text = document.createTextNode('Loading');
    messageZone.appendChild(text);

    hidePleaseWait();

    DWREngine.setPreHook(showPleaseWait);
    DWREngine.setPostHook(hidePleaseWait)
}

/**
 * Call back function used to show the "please wait" message
 */
var showPleaseWait = function()
{
    DWRUtil.getElementById('disabledZone').style.visibility = 'visible';
}

/**
 * Call back function used to remove the "please wait" message
 */
var hidePleaseWait = function()
{
    DWRUtil.getElementById('disabledZone').style.visibility = 'hidden';
}


/**
 * Deprecated
 */
function showById(id)
{
    deprecated("showById");
    DWRUtil.showById(id);
}

/**
 * Deprecated
 */
function hideById(id)
{
    deprecated("hideById");
    DWRUtil.hideById(id);
}

/**
 * Deprecated
 */
function toggleDisplay(id)
{
    deprecated("toggleDisplay");
    DWRUtil.toggleDisplay(id);
}

/**
 * Deprecated
 */
function setCSSClass(id, cssclass)
{
    deprecated("setCSSClass");
    DWRUtil.setCSSClass(id, cssclass);
}

/**
 * Deprecated
 */
function clearChildNodes(id)
{
    deprecated("clearChildNodes");
    DWRUtil.clearChildNodes(id);
}

/**
 * Deprecated
 */
function fillList(id, data, valueprop, textprop)
{
    deprecated("fillList");
    DWRUtil.fillList(id, data, valueprop, textprop);
}

/**
 * Deprecated
 */
function drawTable(tbodyID, dataArray, textFuncs)
{
    deprecated("drawTable");
    DWRUtil.drawTable(tbodyID, dataArray, textFuncs);
}

/**
 * Deprecated
 */
function setEnabled(id, state)
{
    deprecated("setEnabled");
    DWRUtil.setEnabled(id, state);
}

/**
 * Deprecated
 */
function isHTMLElement(ele)
{
    deprecated("isHTMLElement");
    return DWRUtil.isHTMLElement(ele);
}

/**
 * Deprecated
 */
function isHTMLInputElement(ele)
{
    deprecated("isHTMLInputElement");
    return DWRUtil.isHTMLInputElement(ele);
}

/**
 * Deprecated
 */
function isHTMLTextAreaElement(ele)
{
    deprecated("isHTMLTextAreaElement");
    return DWRUtil.isHTMLTextAreaElement(ele);
}

/**
 * Deprecated
 */
function isHTMLSelectElement(ele)
{
    deprecated("isHTMLSelectElement");
    return DWRUtil.isHTMLSelectElement(ele);
}

/**
 * Deprecated
 */
function setValue(id, val)
{
    deprecated("setValue");
    DWRUtil.setValue(id, val);
}

/**
 * Deprecated
 */
function getValue(id)
{
    deprecated("getValue");
    return DWRUtil.getValue(id);
}

/**
 * Deprecated
 */
function getText(id)
{
    deprecated("getText");
    return DWRUtil.getText(id);
}

/**
 * Deprecated
 */
function setValues(map)
{
    deprecated("setValues");
    DWRUtil.setValues(map);
}

/**
 * Deprecated
 */
function callOnLoad(load)
{
    deprecated("callOnLoad");
    DWRUtil.callOnLoad(load);
}

/**
 * Deprecated
 */
function alternateRowColors()
{
    deprecated("alternateRowColors");
    DWRUtil.alternateRowColors();
}

/**
 * Deprecated
 */
function toDescriptiveString(object)
{
    deprecated("toDescriptiveString");
    return DWRUtil.toDescriptiveString(object);
}

/**
 * Deprecated
 */
function detailedTypeOf(x)
{
    deprecated("detailedTypeOf");
    return DWRUtil.detailedTypeOf(x);
}

/**
 * Deprecated
 */
function useLoadingMessage()
{
    deprecated("useLoadingMessage");
    DWRUtil.useLoadingMessage();
}

function deprecated(fname)
{
    alert("Utility functions like " + fname + "() are deprecated. Please convert to the DWRUtil.xxx() versions");
}
