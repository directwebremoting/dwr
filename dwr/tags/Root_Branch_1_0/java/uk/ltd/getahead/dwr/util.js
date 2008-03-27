
// See: http://www.crockford.com/javascript/jslint.html
/*global DWREngine, Option, alert, document, setTimeout, window */

/**
 * Declare a constructor function to which we can add real functions.
 * @constructor
 */
function DWRUtil() { }

////////////////////////////////////////////////////////////////////////////////
// The following functions are described in script-compat.html

/**
 * Enables you to react to return being pressed in an input
 * For example:
 * <code>&lt;input type="text" onkeypressed="DWRUtil.onReturn(event, methodName)"/&gt;</code>
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
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
};

/**
 * Select a specific range in a text box.
 * This is useful for 'google suggest' type functionallity.
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
 * @param ele The id of the text input element or the HTML element itself
 * @param start The beginning index
 * @param end The end index 
 */
DWRUtil.selectRange = function(ele, start, end)
{
    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("selectRange() can't find an element with id: " + orig + ".");
        return;
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
};

////////////////////////////////////////////////////////////////////////////////
// The following functions are described in script-general.html

/**
 * Find the element in the current HTML document with the given id, or if more
 * than one parameter is passed, return an array containing the found elements.
 * Any non-string arguments are left as is in the reply.
 * This function is inspired by the prototype library however it probably works
 * on more browsers than the original.
 * Technically speaking this will not work on IE5.0 because it uses Array.push
 * however it is expected that this will only be used in conjunction with
 * engine.js (which makes up for this omission). If you are using this function
 * without engine.js and want IE5.0 compatibility then you should arrange for
 * a replacement for Array.push.
 * @see http://www.getahead.ltd.uk/dwr/script-general.html
 */
function $()
{
    var elements = new Array();

    for (var i = 0; i < arguments.length; i++)
    {
        var element = arguments[i];
        if (typeof element == 'string')
        {
            if (document.getElementById)
            {
                element = document.getElementById(element);
            }
            else if (document.all)
            {
                element = document.all[element];
            }
        }

        if (arguments.length == 1) 
        {
            return element;
        }

        elements.push(element);
    }

    return elements;
}

/**
 * A better toString than the default for an Object
 * @param data The object to describe
 * @param level 0 = Single line of debug, 1 = Multi-line debug that does not
 *              dig into child objects, 2 = Multi-line debug that digs into the
 *              2nd layer of child objects
 * @param depth How much do we indent this item?
 * @see http://www.getahead.ltd.uk/dwr/script-general.html
 */
DWRUtil.toDescriptiveString = function(data, level, depth)
{
    var reply = "";
    var i = 0;
    var value;
    var obj;

    if (level == null)
    {
        level = 0;
    }

    if (depth == null)
    {
        depth = 0;
    }

    if (data == null)
    {
        return "null";
    }

    if (DWRUtil._isArray(data))
    {
        if (level != 0)
        {
            reply += "[\n";
        }
        else
        {
            reply = "[";
        }

        for (i = 0; i < data.length; i++)
        {
            try
            {
                obj = data[i];

                if (obj == null || typeof obj == "function")
                {
                    continue;
                }
                else if (typeof obj == "object")
                {
                    if (level > 0)
                    {
                        value = DWRUtil.toDescriptiveString(obj, level - 1, depth + 1);
                    }
                    else
                    {
                        value = DWRUtil._detailedTypeOf(obj);
                    }
                }
                else
                {
                    value = "" + obj;
                    value = value.replace(/\/n/g, "\\n");
                    value = value.replace(/\/t/g, "\\t");
                }
            }
            catch (ex)
            {
                value = "" + ex;
            }

            if (level != 0)
            {
                reply += DWRUtil._indent(level, depth + 2) + value + ", \n";
            }
            else
            {
                if (value.length > 13)
                {
                    value = value.substring(0, 10) + "...";
                }

                reply += value + ", ";

                if (i > 5)
                {
                    reply += "...";
                    break;
                }
            }
        }

        if (level != 0)
        {
            reply += DWRUtil._indent(level, depth) + "]";
        }
        else
        {
            reply += "]";
        }

        return reply;
    }

    if (typeof data == "string" || typeof data == "number" || DWRUtil._isDate(data))
    {
        return data.toString();
    }

    if (typeof data == "object")
    {
        var typename = DWRUtil._detailedTypeOf(data);
        if (typename != "Object")
        {
            reply = typename + " ";
        }

        if (level != 0)
        {
            reply += "{\n";
        }
        else
        {
            reply = "{";
        }

        var isHtml = DWRUtil._isHTMLElement(data);

        for (var prop in data)
        {
            if (isHtml)
            {
                if (prop.toUpperCase() == prop || prop == "title" ||
                    prop == "lang" || prop == "dir" || prop == "className" ||
                    prop == "form" || prop == "name" || prop == "prefix" ||
                    prop == "namespaceURI" || prop == "nodeType" ||
                    prop == "firstChild" || prop == "lastChild" ||
                    prop.match(/^offset/))
                {
                    // HTML nodes have far too much stuff. Chop out the constants
                    continue;
                }
            }

            value = "";

            try
            {
                obj = data[prop];

                if (obj == null || typeof obj == "function")
                {
                    continue;
                }
                else if (typeof obj == "object")
                {
                    if (level > 0)
                    {
                        value = "\n";
                        value += DWRUtil._indent(level, depth + 2);
                        value = DWRUtil.toDescriptiveString(obj, level - 1, depth + 1);
                    }
                    else
                    {
                        value = DWRUtil._detailedTypeOf(obj);
                    }
                }
                else
                {
                    value = "" + obj;
                    value = value.replace(/\/n/g, "\\n");
                    value = value.replace(/\/t/g, "\\t");
                }
            }
            catch (ex)
            {
                value = "" + ex;
            }

            if (level == 0 && value.length > 13)
            {
                value = value.substring(0, 10) + "...";
            }

            var propStr = prop;
            if (propStr.length > 30)
            {
                propStr = propStr.substring(0, 27) + "...";
            }

            if (level != 0)
            {
                reply += DWRUtil._indent(level, depth + 1);
            }

            reply += prop;
            reply += ":";
            reply += value;
            reply += ", ";

            if (level != 0)
            {
                reply += "\n";
            }

            i++;
            if (level == 0 && i > 5)
            {
                reply += "...";
                break;
            }
        }

        reply += DWRUtil._indent(level, depth);
        reply += "}";

        return reply;
    }

    return data.toString();
};

/**
 * Indenting for DWRUtil.toDescriptiveString
 * @private
 */
DWRUtil._indent = function(level, depth)
{
    var reply = "";
    if (level != 0)
    {
        for (var j = 0; j < depth; j++)
        {
            reply += "\u00A0\u00A0";
        }
        reply += " ";
    }
    return reply;
};

/**
 * Setup a GMail style loading message.
 * @see http://www.getahead.ltd.uk/dwr/script-general.html
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

    $('disabledZone').style.visibility = 'hidden';

    DWREngine.setPreHook(function() { $('disabledZone').style.visibility = 'visible'; });
    DWREngine.setPostHook(function() { $('disabledZone').style.visibility = 'hidden'; });
};

////////////////////////////////////////////////////////////////////////////////
// The following functions are described in script-simple.html

/**
 * Set the value for the given id to the specified val.
 * This method works for selects (where the option with a matching value and
 * not text is selected), input elements (including textareas) divs and spans.
 * @see http://www.getahead.ltd.uk/dwr/script-simple.html
 * @param ele The id of the element or the HTML element itself
 */
DWRUtil.setValue = function(ele, val)
{
    if (val == null)
    {
        val = "";
    }

    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("setValue() can't find an element with id: " + orig + ".");
        return;
    }

    if (DWRUtil._isHTMLElement(ele, "select"))
    {
        // We deal with select list elements by selecting the matching option
        // Begin by searching through the values
        var found  = false;
        var i;

        for (i = 0; i < ele.options.length; i++)
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

        for (i = 0; i < ele.options.length; i++)
        {
            if (ele.options[i].text == val)
            {
                ele.options[i].selected = true;
                break;
            }
        }

        return;
    }

    if (DWRUtil._isHTMLElement(ele, "input"))
    {
        switch (ele.type)
        {
        case "checkbox":
        case "check-box":
        case "radio":
            ele.checked = (val == true);
            return;

        default:
            ele.value = val;
            return;
        }
    }

    if (DWRUtil._isHTMLElement(ele, "textarea"))
    {
        ele.value = val;
        return;
    }

    // If the value to be set is a DOM object then we try importing the node
    // rather than serializing it out
    if (val.nodeType)
    {
        if (val.nodeType == 9 /*Node.DOCUMENT_NODE*/)
        {
            val = val.documentElement;
        }

        val = DWRUtil._importNode(ele.ownerDocument, val, true);
        ele.appendChild(val);
        return;
    }

    // Fall back to innerHTML
    ele.innerHTML = val;
};

/**
 * The counterpart to setValue() - read the current value for a given element.
 * This method works for selects (where the option with a matching value and
 * not text is selected), input elements (including textareas) divs and spans.
 * @see http://www.getahead.ltd.uk/dwr/script-simple.html
 * @param ele The id of the element or the HTML element itself
 */
DWRUtil.getValue = function(ele)
{
    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("getValue() can't find an element with id: " + orig + ".");
        return "";
    }

    if (DWRUtil._isHTMLElement(ele, "select"))
    {
        // This is a bit of a scam because it assumes single select
        // but I'm not sure how we should treat multi-select.
        var sel = ele.selectedIndex;
        if (sel != -1)
        {
            var reply = ele.options[sel].value;
            if (reply == null || reply == "")
            {
                reply = ele.options[sel].text;
            }

            return reply;
        }
        else
        {
            return "";
        }
    }

    if (DWRUtil._isHTMLElement(ele, "input"))
    {
        switch (ele.type)
        {
        case "checkbox":
        case "check-box":
        case "radio":
            return ele.checked;

        default:
            return ele.value;
        }
    }

    if (DWRUtil._isHTMLElement(ele, "textarea"))
    {
        return ele.value;
    }

    return ele.innerHTML;
};

/**
 * getText() is like getValue() with the except that it only works for selects
 * where it reads the text of an option and not it's value.
 * @see http://www.getahead.ltd.uk/dwr/script-simple.html
 * @param ele The id of the element or the HTML element itself
 */
DWRUtil.getText = function(ele)
{
    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("getText() can't find an element with id: " + orig + ".");
        return "";
    }

    if (!DWRUtil._isHTMLElement(ele, "select"))
    {
        alert("getText() can only be used with select elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele) + " from  id: " + orig + ".");
        return "";
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
};

/**
 * Given a map, call setValue() for all the entries in the map using the key
 * of each entry as an id.
 * @see http://www.getahead.ltd.uk/dwr/script-simple.html
 * @param map The map of values to set to various elements
 */
DWRUtil.setValues = function(map)
{
    for (var property in map)
    {
        var ele = $(property);
        if (ele != null)
        {
            var value = map[property];
            DWRUtil.setValue(property, value);
        }
    }
};

/**
 * Given a map, call getValue() for all the entries in the map using the key
 * of each entry as an id.
 * @see http://www.getahead.ltd.uk/dwr/script-simple.html
 * @param map The map of values to set to various elements
 */
DWRUtil.getValues = function(map)
{
    for (var property in map)
    {
        var ele = $(property);
        if (ele != null)
        {
            map[property] = DWRUtil.getValue(property);
        }
    }
};

////////////////////////////////////////////////////////////////////////////////
// The following functions are described in script-list.html

/**
 * Add options to a list from an array or map.
 * DWRUtil.addOptions has 5 modes:
 * <p><b>Array</b><br/>
 * DWRUtil.addOptions(selectid, array) and a set of options are created with the
 * text and value set to the string version of each array element.
 * </p>
 * <p><b>Array of objects, using option text = option value</b><br/>
 * DWRUtil.addOptions(selectid, data, prop) creates an option for each array
 * element, with the value and text of the option set to the given property of
 * each object in the array.
 * </p>
 * <p><b>Array of objects, with differing option text and value</b><br/>
 * DWRUtil.addOptions(selectid, array, valueprop, textprop) creates an option
 * for each object in the array, with the value of the option set to the given
 * valueprop property of the object, and the option text set to the textprop
 * property.
 * </p>
 * <p><b>Map (object)</b><br/>
 * DWRUtil.addOptions(selectid, map, reverse) creates an option for each
 * property. The property names are used as option values, and the property
 * values are used as option text, which sounds wrong, but is actually normally
 * the right way around. If reverse evaluates to true then the property values
 * are used as option values.
 * <p><b>ol or ul list</b><br/>
 * DWRUtil.addOptions(ulid, array) and a set of li elements are created with the
 * innerHTML set to the string value of the array elements. This mode works
 * with ul and ol lists.
 * </p>
 * @see http://www.getahead.ltd.uk/dwr/script-list.html
 * @param ele The id of the list element or the HTML element itself
 * @param data An array or map of data items to populate the list
 * @param valuerev (optional) If data is an array of objects, an optional
 *        property name to use for option values. If the data is a map then this
 *        boolean property allows you to swap keys and values.
 * @param textprop (optional) Only for use with arrays of objects - an optional
 *        property name for use as the text of an option.
 */
DWRUtil.addOptions = function(ele, data, valuerev, textprop)
{
    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("addOptions() can't find an element with id: " + orig + ".");
        return;
    }

    var useOptions = DWRUtil._isHTMLElement(ele, "select");
    var useLi = DWRUtil._isHTMLElement(ele, ["ul", "ol"]);

    if (!useOptions && !useLi)
    {
        alert("fillList() can only be used with select elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
        return;
    }

    // Bail if we have no data
    if (data == null)
    {
        return;
    }

    var text;
    var value;
    var opt;

    if (DWRUtil._isArray(data))
    {
        // Loop through the data that we do have
        for (var i = 0; i < data.length; i++)
        {
            if (useOptions)
            {
                if (valuerev != null)
                {
                    if (textprop != null)
                    {
                        text = data[i][textprop];
                        value = data[i][valuerev];
                    }
                    else
                    {
                        value = data[i][valuerev];
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
                        text = "" + data[i];
                        value = text;
                    }
                }

                opt = new Option(text, value);
                ele.options[ele.options.length] = opt;
            }
            else
            {
                li = document.createElement("li");
                li.innerHTML = "" + data[i];
                ele.appendChild(li);
            }
        }
    }
    else
    {
        for (var prop in data)
        {
            if (!useOptions)
            {
                alert("DWRUtil.addOptions can only create select lists from objects.");
                return;
            }

            if (valuerev)
            {
                text = prop;
                value = data[prop];
            }
            else
            {
                text = data[prop];
                value = prop;
            }

            opt = new Option(text, value);
            ele.options[ele.options.length] = opt;
        }
    }
};

/**
 * Remove all the options from a select list (specified by id)
 * @see http://www.getahead.ltd.uk/dwr/script-list.html
 * @param ele The id of the list element or the HTML element itself
 */
DWRUtil.removeAllOptions = function(ele)
{
    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("removeAllOptions() can't find an element with id: " + orig + ".");
        return;
    }

    var useOptions = DWRUtil._isHTMLElement(ele, "select");
    var useLi = DWRUtil._isHTMLElement(ele, ["ul", "ol"]);

    if (!useOptions && !useLi)
    {
        alert("removeAllOptions() can only be used with select, ol and ul elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
        return;
    }

    if (useOptions)
    {
        // Empty the list
        ele.options.length = 0;
    }
    else
    {
        while (ele.childNodes.length > 0)
        {
            ele.removeChild(ele.firstChild);
        }
    }
};

////////////////////////////////////////////////////////////////////////////////
// The following functions are described in script-table.html

/**
 * Create rows inside a the table, tbody, thead or tfoot element (given by id).
 * The normal case would be to use tbody since that allows you to keep header
 * lines separate, but this function should work with and table element above
 * tr.
 * This function creates a row for each element in the <code>data</code> array
 * and for that row create one cell for each function in the
 * <code>cellFuncs</code> array by passing the element from the
 * <code>data</code> array to the given function.
 * The return from the function is used to populate the cell.
 * <p>The pseudo code looks something like this:
 * <pre>
 *   for each member of the data array
 *     for function in the cellFuncs array
 *       create cell from cellFunc(data[i])
 * </pre>
 * One slight modification to this is that any members of the cellFuncs array
 * that are strings instead of functions, the strings are used as cell contents
 * directly.
 * @see http://www.getahead.ltd.uk/dwr/script-table.html
 * @param ele The id of the tbody element
 * @param data Array containing one entry for each row in the updated table
 * @param cellFuncs An array of functions (one per column) for extracting cell
 *    data from the passed row data
 */
DWRUtil.addRows = function(ele, data, cellFuncs)
{
    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("addRows() can't find an element with id: " + orig + ".");
        return;
    }

    if (!DWRUtil._isHTMLElement(ele, ["table", "tbody", "thead", "tfoot"]))
    {
        alert("addRows() can only be used with table, tbody, thead and tfoot elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
        return;
    }

    // assure bug-free redraw in Gecko engine by
    // letting window show cleared table
    if (navigator.product && navigator.product == "Gecko")
    {
        setTimeout(function() { DWRUtil._addRowsInner(ele, data, cellFuncs); }, 0);
    }
    else
    {
        DWRUtil._addRowsInner(ele, data, cellFuncs);
    }
};

/**
 * Internal function to help rendering tables.
 * @see DWRUtil.addRows(ele, data, cellFuncs)
 * @private
 */
DWRUtil._addRowsInner = function(ele, data, cellFuncs)
{
    var frag = document.createDocumentFragment();

    if (DWRUtil._isArray(data))
    {
        // loop through data source
        for (var i = 0; i < data.length; i++)
        {
            DWRUtil._addRowInner(frag, data[i], cellFuncs);
        }
    }
    else if (typeof data == "object")
    {
        for (var row in data)
        {
            DWRUtil._addRowInner(frag, row, cellFuncs);
        }
    }

    ele.appendChild(frag);
};

/**
 * Iternal function to draw a single row of a table.
 * @private
 */
DWRUtil._addRowInner = function(frag, row, cellFuncs)
{
    var tr = document.createElement("tr");

    for (var j = 0; j < cellFuncs.length; j++)
    {
        var func = cellFuncs[j];
        var td;

        if (typeof func == "string")
        {
            td = document.createElement("td");
            var text = document.createTextNode(func);
            td.appendChild(text);
            tr.appendChild(td);
        }
        else
        {
            var reply = func(row);

            if (DWRUtil._isHTMLElement(reply, "td"))
            {
                td = reply;
            }
            else if (DWRUtil._isHTMLElement(reply))
            {
                td = document.createElement("td");
                td.appendChild(reply);
            }
            else
            {
                td = document.createElement("td");
                td.innerHTML = reply;
                //var text = document.createTextNode(reply);
                //td.appendChild(text);
            }

            tr.appendChild(td);
        }
    }

    frag.appendChild(tr);
};

/**
 * Remove all the children of a given node.
 * Most useful for dynamic tables where you clearChildNodes() on the tbody
 * element.
 * @see http://www.getahead.ltd.uk/dwr/script-table.html
 * @param ele The id of the element or the HTML element itself
 */
DWRUtil.removeAllRows = function(ele)
{
    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("removeAllRows() can't find an element with id: " + orig + ".");
        return;
    }

    if (!DWRUtil._isHTMLElement(ele, ["table", "tbody", "thead", "tfoot"]))
    {
        alert("removeAllRows() can only be used with table, tbody, thead and tfoot elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
        return;
    }

    while (ele.childNodes.length > 0)
    {
        ele.removeChild(ele.firstChild);
    }
};

////////////////////////////////////////////////////////////////////////////////
// Private functions only below here

/**
 * Browser detection code.
 * This is eeevil, but the official way [if (window.someFunc) ...] does not
 * work when browsers differ in rendering ability rather than the use of someFunc()
 * For internal use only.
 * @private
 */
DWRUtil._agent = navigator.userAgent.toLowerCase();

/**
 * @private
 */
DWRUtil._isIE = ((DWRUtil._agent.indexOf("msie") != -1) && (DWRUtil._agent.indexOf("opera") == -1));

/**
 * Is the given node an HTML element (optionally of a given type)?
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
 * @param ele The element to test
 * @param nodeName eg "input", "textarea" - optional extra check for node name
 *                 or an array of valid node names.
 * @private
 */
DWRUtil._isHTMLElement = function(ele, nodeName)
{
    if (ele == null || typeof ele != "object" || ele.nodeName == null)
    {
        return false;
    }

    if (nodeName != null)
    {
        var test = ele.nodeName.toLowerCase();

        if (typeof nodeName == "string")
        {
            return test == nodeName.toLowerCase();
        }

        if (DWRUtil._isArray(nodeName))
        {
            var match = false;
            for (var i = 0; i < nodeName.length && !match; i++)
            {
                if (test == nodeName[i].toLowerCase())
                {
                    match =  true;
                }
            }

            return match;
        }

        alert("DWRUtil._isHTMLElement was passed test node name that is neither a string or array of strings");
        return false;
    }

    return true;
};

/**
 * Like typeOf except that more information for an object is returned other
 * than "object"
 * @private
 */
DWRUtil._detailedTypeOf = function(x)
{
    var reply = typeof x;

    if (reply == "object")
    {
        reply = Object.prototype.toString.apply(x);  // Returns "[object class]"
        reply = reply.substring(8, reply.length-1);  // Just get the class bit
    }

    return reply;
};

/**
 * Array detector.
 * This is an attempt to work around the lack of support for instanceof in
 * some browsers.
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
 * @param data The object to test
 * @returns true iff <code>data</code> is an Array
 * @private
 */
DWRUtil._isArray = function(data)
{
    return (data && data.join) ? true : false;
};

/**
 * Date detector.
 * This is an attempt to work around the lack of support for instanceof in
 * some browsers.
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
 * @param data The object to test
 * @returns true iff <code>data</code> is a Date
 * @private
 */
DWRUtil._isDate = function(data)
{
    return (data && data.toUTCString) ? true : false;
};

/**
 * document.importNode is used by setValue.
 * This gets around the missing functionallity in IE.
 * @private
 */
DWRUtil._importNode = function(doc, importedNode, deep)
{
    var newNode;

    if (importedNode.nodeType == 1 /*Node.ELEMENT_NODE*/)
    {
        newNode = doc.createElement(importedNode.nodeName);

        for (var i = 0; i < importedNode.attributes.length; i++)
        {
            var attr = importedNode.attributes[i];
            if (attr.nodeValue != null && attr.nodeValue != '')
            {
                newNode.setAttribute(attr.name, attr.nodeValue);
            }
        }

        if (typeof importedNode.style != "undefined")
        {
            newNode.style.cssText = importedNode.style.cssText;
        }
    }
    else if (importedNode.nodeType == 3 /*Node.TEXT_NODE*/)
    {
        newNode = doc.createTextNode(importedNode.nodeValue);
    }

    if (deep && importedNode.hasChildNodes())
    {
        for (i = 0; i < importedNode.childNodes.length; i++)
        {
            newNode.appendChild(DWRUtil._importNode(doc, importedNode.childNodes[i], true));
        }
    }

    return newNode;
}
if (typeof document.importNode != "function")
{
    document.importNode = function(importedNode, deep)
    {
        DWRUtil._importNode(this, importedNode, deep);
    };
}

////////////////////////////////////////////////////////////////////////////////
// Deprecated functions only below here

/**
 * Is the given node an HTML element (optionally of a given type)?
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
 * @param ele The element to test
 * @param nodeName eg input, textarea - optional extra check for node name
 * @deprecated DWR isn't a generic Javascript library
 */
if (!DWRUtil.isHTMLElement)
{
DWRUtil.isHTMLElement = function(ele, nodeName)
{
    DWRUtil._deprecated("DWRUtil.isHTMLElement");

    if (nodeName == null)
    {
        // If I.E. worked properly we could use:
        //  return typeof ele == "object" && ele instanceof HTMLElement;
        return ele != null &&
               typeof ele == "object" &&
               ele.nodeName != null;
    }
    else
    {
        return ele != null &&
               typeof ele == "object" &&
               ele.nodeName != null &&
               ele.nodeName.toLowerCase() == nodeName.toLowerCase();
    }
};
}

/**
 * Like typeOf except that more information for an object is returned other
 * than "object"
 * @deprecated DWR isn't a generic Javascript library
 */
if (!DWRUtil.detailedTypeOf)
{
DWRUtil.detailedTypeOf = function(x)
{
    DWRUtil._deprecated("DWRUtil.detailedTypeOf");

    var reply = typeof x;

    if (reply == "object")
    {
        reply = Object.prototype.toString.apply(x);  // Returns "[object class]"
        reply = reply.substring(8, reply.length-1);  // Just get the class bit
    }

    return reply;
};
}

/**
 * Array detector.
 * This is an attempt to work around the lack of support for instanceof in
 * some browsers.
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
 * @param data The object to test
 * @returns true iff <code>data</code> is an Array
 * @deprecated Not sure if DWR is the right place for this or if we support old browsers
 */
if (!DWRUtil.isArray)
{
DWRUtil.isArray = function(data)
{
    DWRUtil._deprecated("DWRUtil.isArray", "(array.join != null)");
    return (data && data.join) ? true : false;
};
}

/**
 * Date detector.
 * This is an attempt to work around the lack of support for instanceof in
 * some browsers.
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
 * @param data The object to test
 * @returns true iff <code>data</code> is a Date
 * @deprecated Not sure if DWR is the right place for this or if we support old browsers
 */
if (!DWRUtil.isDate)
{
DWRUtil.isDate = function(data)
{
    return (data && data.toUTCString) ? true : false;
};
}

/**
 * Is the given node an HTML input element?
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
 * @param ele The element to test
 * @deprecated See the documentation for alternatives
 */
if (!DWRUtil.isHTMLInputElement)
{
DWRUtil.isHTMLInputElement = function(ele)
{
    DWRUtil._deprecated("DWRUtil.isHTMLInputElement");
    return DWRUtil.isHTMLElement(ele, "input");
};
}

/**
 * Is the given node an HTML textarea element?
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
 * @param ele The element to test
 * @deprecated See the documentation for alternatives
 */
if (!DWRUtil.isHTMLTextAreaElement)
{
DWRUtil.isHTMLTextAreaElement = function(ele)
{
    DWRUtil._deprecated("DWRUtil.isHTMLTextAreaElement");
    return DWRUtil.isHTMLElement(ele, "textarea");
};
}

/**
 * Is the given node an HTML select element?
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
 * @param ele The element to test
 * @deprecated See the documentation for alternatives
 */
if (!DWRUtil.isHTMLSelectElement)
{
DWRUtil.isHTMLSelectElement = function(ele)
{
    DWRUtil._deprecated("DWRUtil.isHTMLSelectElement");
    return DWRUtil.isHTMLElement(ele, "select");
};
}

/**
 * Like document.getElementById() that works in more browsers.
 * @param id The id of the element
 * @deprecated Use $()
 */
if (!DWRUtil.getElementById)
{
DWRUtil.getElementById = function(id)
{
    DWRUtil._deprecated("DWRUtil.getElementById", "$");

    if (document.getElementById)
    {
        return document.getElementById(id);
    }
    else if (document.all)
    {
        return document.all[id];
    }

    return null;
};
}

/**
 * Visually enable or diable an element.
 * @see http://www.getahead.ltd.uk/dwr/script-compat.html
 * @param ele The id of the element or the HTML element itself
 * @param state Boolean true/false to set if the element should be enabled
 * @deprecated DWR isn't a generic Javascript library
 */
if (!DWRUtil.setEnabled)
{
DWRUtil.setEnabled = function(ele, state)
{
    DWRUtil._deprecated("DWRUtil.setEnabled");

    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("setEnabled() can't find an element with id: " + orig + ".");
        return;
    }

    // If we want to get funky and disable divs and spans by changing the font
    // colour or something then we might want to check the element type before
    // we make assumptions, but in the mean time ...
    // if (DWRUtil.isHTMLElement(ele, "input")) { ... }

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
};
}

/**
 * Set the CSS display style to 'block'
 * @param ele The id of the element or the HTML element itself
 * @deprecated DWR isn't a generic Javascript library
 */
if (!DWRUtil.showById)
{
DWRUtil.showById = function(ele)
{
    DWRUtil._deprecated("DWRUtil.showById");

    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("showById() can't find an element with id: " + orig + ".");
        return;
    }

    // Apparently this works better that display = 'block'; ???
    ele.style.display = '';
};
}

/**
 * Set the CSS display style to 'none'
 * @param ele The id of the element or the HTML element itself
 * @deprecated DWR isn't a generic Javascript library
 */
if (!DWRUtil.hideById)
{
DWRUtil.hideById = function(ele)
{
    DWRUtil._deprecated("DWRUtil.hideById");

    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("hideById() can't find an element with id: " + orig + ".");
        return;
    }

    ele.style.display = 'none';
};
}

/**
 * Toggle an elements visibility
 * @param ele The id of the element or the HTML element itself
 * @deprecated DWR isn't a generic Javascript library
 */
if (!DWRUtil.toggleDisplay)
{
DWRUtil.toggleDisplay = function(ele)
{
    DWRUtil._deprecated("DWRUtil.toggleDisplay");

    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("toggleDisplay() can't find an element with id: " + orig + ".");
        return;
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
};
}

/**
 * Alter an rows in a table that have a class of zebra to have classes of either
 * oddrow or evenrow alternately.
 * This is probably not the best place for this method, but I dont want to have
 * to fight with multiple onload functions.
 * @deprecated DWR isn't a generic Javascript library
 */
if (!DWRUtil.alternateRowColors)
{
DWRUtil.alternateRowColors = function()
{
    DWRUtil._deprecated("DWRUtil.alternateRowColors");

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
};
}

/**
 * Set the CSS class for an element
 * @param ele The id of the element or the HTML element itself
 * @deprecated DWR isn't a generic Javascript library
 */
if (!DWRUtil.setCSSClass)
{
DWRUtil.setCSSClass = function(ele, cssclass)
{
    DWRUtil._deprecated("DWRUtil.setCSSClass");

    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("setCSSClass() can't find an element with id: " + orig + ".");
        return;
    }

    ele.className = cssclass;
};
}

/**
 * Ensure a function is called when the page is loaded
 * @param load The function to call when the page has been loaded
 * @deprecated DWR isn't a generic Javascript library
 */
if (!DWRUtil.callOnLoad)
{
DWRUtil.callOnLoad = function(load)
{
    DWRUtil._deprecated("DWRUtil.callOnLoad", "window.addEventListener or window.onload");

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
};
}

/**
 * Remove all the options from a select list (specified by id) and replace with
 * elements in an array of objects.
 * @deprecated Use DWRUtil.removeAllOptions(ele); DWRUtil.addOptions(ele, data, valueprop, textprop);
 */
if (!DWRUtil.fillList)
{
DWRUtil.fillList = function(ele, data, valueprop, textprop)
{
    DWRUtil._deprecated("DWRUtil.fillList", "DWRUtil.addOptions");
    DWRUtil.removeAllOptions(ele);
    DWRUtil.addOptions(ele, data, valueprop, textprop);
};
}

/**
 * Add rows to a table
 * @deprecated Use DWRUtil.addRows()
 */
if (!DWRUtil.drawTable)
{
DWRUtil.drawTable = function(ele, data, cellFuncs)
{
    DWRUtil._deprecated("DWRUtil.drawTable", "DWRUtil.addRows");
    DWRUtil.addRows(ele, data, cellFuncs);
};
}

/**
 * Remove all the children of a given node.
 * Most useful for dynamic tables where you clearChildNodes() on the tbody
 * element.
 * @param id The id of the element
 * @deprecated Use DWRUtil.removeAllRows()
 */
if (!DWRUtil.clearChildNodes)
{
DWRUtil.clearChildNodes = function(id)
{
    DWRUtil._deprecated("DWRUtil.clearChildNodes", "DWRUtil.removeAllRows");

    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("clearChildNodes() can't find an element with id: " + orig + ".");
        return;
    }

    while (ele.childNodes.length > 0)
    {
        ele.removeChild(ele.firstChild);
    }
};
}

/**
 * Do we alert on deprecation warnings
 * @private
 */
DWRUtil._showDeprecated = true;

/**
 * We can use this function to deprecate things.
 * @deprecated
 * @private
 */
DWRUtil._deprecated = function(fname, altfunc)
{
    if (DWRUtil._showDeprecated)
    {
        var warning;
        var alternative;

        if (fname == null)
        {
            warning = "You have used a deprecated function which could be removed in the future.";
            alternative = "";
        }
        else
        {
            warning = "Utility functions like '" + fname + "' are deprecated and could be removed in the future.";

            if (altfunc == null)
            {
                alternative = "\nSee the documentation for alternatives.";
            }
            else
            {
                alternative = "\nFor an alternative see: " + altfunc;
            }
        }

        var further = "\nImport deprecated.js to get rid of this warning.\nDo you wish to ignore further deprecation warnings on this page?";

        DWRUtil._showDeprecated = !confirm(warning + alternative + further);
    }
};
