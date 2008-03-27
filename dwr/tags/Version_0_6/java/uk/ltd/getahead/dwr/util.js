
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
DWRUtil._agent = navigator.userAgent.toLowerCase();
DWRUtil._isIE  = ((DWRUtil._agent.indexOf("msie") != -1) && (DWRUtil._agent.indexOf("opera") == -1));

/**
 * Set the CSS display style to 'block'
 * @param id The id of the element
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
 * @param id The id of the element
 */
DWRUtil.showById = function(id)
{
    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("showById() can't find an element with id: " + id + ".");
        throw id;
    }

    // Apparently this works better that display = 'block'; ???
    ele.style.display = '';
}

/**
 * Set the CSS display style to 'none'
 * @param id The id of the element
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
 * @param id The id of the element
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
        // Apparently this works better that display = 'block'; ???
        ele.style.display = '';
    }
    else
    {
        ele.style.display = 'none';
    }
}

/**
 * Enables you to react to return being pressed in an input
 * For example <input type="text" onkeypressed="DWRUtil.onReturn(event, methodName)"/>
 * @param event The event object for Netscape browsers
 * @param action Method name to execute when return is pressed
 */
DWRUtil.onReturn = function(event, action)
{
    if (!event)
    {
        event = window.event;
    }

    if (event && event.keyCode && event.keyCode == 13)
    {
        action();
    }
}

/**
 * Set the CSS class for an element
 * @param id The id of the element
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
 * @param id The id of the element
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
 * elements in an array of objects.
 * If valueprop and textprop are null then each member of the data array will
 * be converted to a string and used as both the value and the text.
 * (The text is what is displayed in an option and the value what is passed to
 * the server and used in scripts)
 * So without valueprop or textprop the pseudo code goes:
 * <pre>
 *   for (var i = 0; i < data.length; i++)
 *     list.addOption( text = value = DWRUtil.toDescriptiveString(data[i]) );
 * </pre>
 * If only valueprop is specified then both the text and value will be set to
 * the specified property. i.e.:
 * <pre>
 *   for (var i = 0; i < data.length; i++)
 *     list.addOption( text = value = data[i][valueprop] );
 * </pre>
 * If both are specified, then value prop used used to set the value and
 * textprop (unsurprisingly) is used to set the text.
 * @param id A string containing the id of the list element
 * @param data An array of data items to populate the list with
 * @param valueprop (optional) If property name as an index into each data item
 *     for use as an option value
 * @param textprop (optional) If property name as an index into each data item
 *     for use as the option text
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
 * Under the tbody (given by id) create a row for each element in the data
 * and for that row create one cell for each function in the cellFuncs array
 * by passing the rows object (from the data) to the given function.
 * The return from the function is used to populate the cell.
 * <p>The pseudo code looks something like this:
 * <pre>
 *   for (var i = 0; i < data.length; i++)
 *     for (var j = 0; j < cellFuncs.length; j++)
 *       create cell from cellFuncs[j](data[i])
 * </pre>
 * One slight modification to this is that any members of the cellFuncs array
 * that are strings instead of functions, the strings are used as cell contents
 * directly.
 * <p>Note that this function uses instanceof to detect strings which may break
 * on IE5 Mac. So we might need to rely on typeof == "string" alone. I'm not
 * yet sure if this might be an issue.
 * @param tbodyID The id of the tbody element
 * @param data Array containing one entry for each row in the updated table
 * @param cellFuncs An array of functions (one per column) for extracting cell
 *    data from the passed row data
 */
DWRUtil.drawTable = function(tbodyID, data, cellFuncs)
{
    // assure bug-free redraw in Gecko engine by
    // letting window show cleared table
    if (navigator.product && navigator.product == "Gecko")
    {
        setTimeout(function() { DWRUtil._drawTableInner(tbodyID, data, cellFuncs); }, 0);
    }
    else
    {
        DWRUtil._drawTableInner(tbodyID, data, cellFuncs);
    }
}

Array.prototype.isArray = true;
String.prototype.isString = true;

/**
 * Internal function to help rendering tables.
 * @see DWRUtil.drawTable(tbodyID, data, cellFuncs)
 * @private
 */
DWRUtil._drawTableInner = function(tbodyID, data, cellFuncs)
{
    var frag = document.createDocumentFragment();

    if (data.isArray)
    {
        // loop through data source
        for (var i = 0; i < data.length; i++)
        {
            DWRUtil._drawRowInner(frag, data[i], cellFuncs);
        }
    }
    else if (typeof data == "object")
    {
        for (var row in data)
        {
            DWRUtil._drawRowInner(frag, row, cellFuncs);
        }
    }

    var tbody = DWRUtil.getElementById(tbodyID);
    tbody.appendChild(frag);
}

/**
 *
 */
DWRUtil._drawRowInner = function(frag, row, cellFuncs)
{
    var tr = document.createElement("tr");

    for (var j = 0; j < cellFuncs.length; j++)
    {
        var td = document.createElement("td");
        tr.appendChild(td);

        var func = cellFuncs[j];
        if (typeof func == "string" || func.isString)
        {
            var text = document.createTextNode(func);
            td.appendChild(text);
        }
        else
        {
            var reply = func(row);
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

/**
 * Visually enable or diable an element.
 * @param id The id of the element
 * @param state Boolean true/false to set if the element should be enabled
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
 * Select a specific range in a text box.
 * This is useful for 'google suggest' type functionallity.
 * @param id The id of the text input area to select a portion of
 * @param start The beginning index
 * @param end The end index 
 */
DWRUtil.selectRange = function(id, start, end)
{
    var ele = DWRUtil.getElementById(id);
    if (ele == null)
    {
        alert("selectRange() can't find an element with id: " + id + ".");
        throw id;
    }

    if (ele.setSelectionRange)
    {
        ele.setSelectionRange(start, end);
    }
    else if (ele.createTextRange)
    {
        var range = ele.createTextRange();
        range.moveStart("character", start);
        range.moveEnd("character", end - ele.value.length);
        range.select();
    }

    ele.focus();
}

/**
 * Is the given node an HTML element?
 */
DWRUtil.isHTMLElement = function(ele)
{
    // If I.E. worked properly we could use:
    //  return typeof ele == "object" && ele instanceof HTMLElement;
    return typeof ele == "object" && ele.nodeName;
}

/**
 * Is the given node an HTML input element?
 */
DWRUtil.isHTMLInputElement = function(ele)
{
    // If I.E. worked properly we could use:
    //   return typeof ele == "object" && ele instanceof HTMLInputElement;
    return typeof ele == "object" && ele.nodeName && ele.nodeName.toLowerCase() == "input";
}

/**
 * Is the given node an HTML textarea element?
 */
DWRUtil.isHTMLTextAreaElement = function(ele)
{
    // If I.E. worked properly we could use:
    //   return typeof ele == "object" && ele instanceof HTMLTextAreaElement;
    return typeof ele == "object" && ele.nodeName && ele.nodeName.toLowerCase() == "textarea";
}

/**
 * Is the given node an HTML select element?
 */
DWRUtil.isHTMLSelectElement = function(ele)
{
    // If I.E. worked properly we could use:
    //   return typeof ele == "object" && ele instanceof HTMLSelectElement;
    return typeof ele == "object" && ele.nodeName && ele.nodeName.toLowerCase() == "select";
}

/**
 * Set the value for the given id to the specified val.
 * This method works for selects (where the option with a matching value and
 * not text is selected), input elements (including textareas) divs and spans.
 * @param id The id of the element
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
        case "checkbox":
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
 * @param id The id of the element
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
        case "checkbox":
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
 * @param id The id of the element
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
 * @deprecated
 */
function showById(id)
{
    deprecated("showById");
    DWRUtil.showById(id);
}

/**
 * @deprecated
 */
function hideById(id)
{
    deprecated("hideById");
    DWRUtil.hideById(id);
}

/**
 * @deprecated
 */
function toggleDisplay(id)
{
    deprecated("toggleDisplay");
    DWRUtil.toggleDisplay(id);
}

/**
 * @deprecated
 */
function setCSSClass(id, cssclass)
{
    deprecated("setCSSClass");
    DWRUtil.setCSSClass(id, cssclass);
}

/**
 * @deprecated
 */
function clearChildNodes(id)
{
    deprecated("clearChildNodes");
    DWRUtil.clearChildNodes(id);
}

/**
 * @deprecated
 */
function fillList(id, data, valueprop, textprop)
{
    deprecated("fillList");
    DWRUtil.fillList(id, data, valueprop, textprop);
}

/**
 * @deprecated
 */
function drawTable(tbodyID, data, cellFuncs)
{
    deprecated("drawTable");
    DWRUtil.drawTable(tbodyID, data, cellFuncs);
}

/**
 * @deprecated
 */
function setEnabled(id, state)
{
    deprecated("setEnabled");
    DWRUtil.setEnabled(id, state);
}

/**
 * @deprecated
 */
function isHTMLElement(ele)
{
    deprecated("isHTMLElement");
    return DWRUtil.isHTMLElement(ele);
}

/**
 * @deprecated
 */
function isHTMLInputElement(ele)
{
    deprecated("isHTMLInputElement");
    return DWRUtil.isHTMLInputElement(ele);
}

/**
 * @deprecated
 */
function isHTMLTextAreaElement(ele)
{
    deprecated("isHTMLTextAreaElement");
    return DWRUtil.isHTMLTextAreaElement(ele);
}

/**
 * @deprecated
 */
function isHTMLSelectElement(ele)
{
    deprecated("isHTMLSelectElement");
    return DWRUtil.isHTMLSelectElement(ele);
}

/**
 * @deprecated
 */
function setValue(id, val)
{
    deprecated("setValue");
    DWRUtil.setValue(id, val);
}

/**
 * @deprecated
 */
function getValue(id)
{
    deprecated("getValue");
    return DWRUtil.getValue(id);
}

/**
 * @deprecated
 */
function getText(id)
{
    deprecated("getText");
    return DWRUtil.getText(id);
}

/**
 * @deprecated
 */
function setValues(map)
{
    deprecated("setValues");
    DWRUtil.setValues(map);
}

/**
 * @deprecated
 */
function callOnLoad(load)
{
    deprecated("callOnLoad");
    DWRUtil.callOnLoad(load);
}

/**
 * @deprecated
 */
function alternateRowColors()
{
    deprecated("alternateRowColors");
    DWRUtil.alternateRowColors();
}

/**
 * @deprecated
 */
function toDescriptiveString(object)
{
    deprecated("toDescriptiveString");
    return DWRUtil.toDescriptiveString(object);
}

/**
 * @deprecated
 */
function detailedTypeOf(x)
{
    deprecated("detailedTypeOf");
    return DWRUtil.detailedTypeOf(x);
}

/**
 * @deprecated
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
