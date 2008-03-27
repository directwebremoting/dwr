/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Is the given node an HTML element (optionally of a given type)?
 * @see http://www.getahead.ltd.uk/dwr/util-compat.html
 * @param ele The element to test
 * @param nodeName eg input, textarea - optional extra check for node name
 * @deprecated DWR isn't a generic Javascript library
 */
DWRUtil.isHTMLElement = function(ele, nodeName)
{
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

/**
 * Like typeOf except that more information for an object is returned other
 * than "object"
 * @deprecated DWR isn't a generic Javascript library
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
};

/**
 * Array detector.
 * This is an attempt to work around the lack of support for instanceof in
 * some browsers.
 * @see http://www.getahead.ltd.uk/dwr/util-compat.html
 * @param data The object to test
 * @returns true iff <code>data</code> is an Array
 * @deprecated Not sure if DWR is the right place for this or if we support old browsers
 */
DWRUtil.isArray = function(data)
{
    return (data && data.join) ? true : false;
};

/**
 * Date detector.
 * This is an attempt to work around the lack of support for instanceof in
 * some browsers.
 * @see http://www.getahead.ltd.uk/dwr/util-compat.html
 * @param data The object to test
 * @returns true iff <code>data</code> is a Date
 * @deprecated Not sure if DWR is the right place for this or if we support old browsers
 */
DWRUtil.isDate = function(data)
{
    return (data && data.toUTCString) ? true : false;
};

/**
 * Is the given node an HTML input element?
 * @see http://www.getahead.ltd.uk/dwr/util-compat.html
 * @param ele The element to test
 * @deprecated See the documentation for alternatives
 */
DWRUtil.isHTMLInputElement = function(ele)
{
    return DWRUtil.isHTMLElement(ele, "input");
};

/**
 * Is the given node an HTML textarea element?
 * @see http://www.getahead.ltd.uk/dwr/util-compat.html
 * @param ele The element to test
 * @deprecated See the documentation for alternatives
 */
DWRUtil.isHTMLTextAreaElement = function(ele)
{
    return DWRUtil.isHTMLElement(ele, "textarea");
};

/**
 * Is the given node an HTML select element?
 * @see http://www.getahead.ltd.uk/dwr/util-compat.html
 * @param ele The element to test
 * @deprecated See the documentation for alternatives
 */
DWRUtil.isHTMLSelectElement = function(ele)
{
    return DWRUtil.isHTMLElement(ele, "select");
};

/**
 * Like document.getElementById() that works in more browsers.
 * @param id The id of the element
 * @deprecated Use $()
 */
DWRUtil.getElementById = function(id)
{
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

/**
 * Visually enable or diable an element.
 * @see http://www.getahead.ltd.uk/dwr/util-compat.html
 * @param ele The id of the element or the HTML element itself
 * @param state Boolean true/false to set if the element should be enabled
 * @deprecated DWR isn't a generic Javascript library
 */
DWRUtil.setEnabled = function(ele, state)
{
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

/**
 * Set the CSS display style to 'block'
 * @param ele The id of the element or the HTML element itself
 * @deprecated DWR isn't a generic Javascript library
 */
DWRUtil.showById = function(ele)
{
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

/**
 * Set the CSS display style to 'none'
 * @param ele The id of the element or the HTML element itself
 * @deprecated DWR isn't a generic Javascript library
 */
DWRUtil.hideById = function(ele)
{
    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("hideById() can't find an element with id: " + orig + ".");
        return;
    }

    ele.style.display = 'none';
};

/**
 * Toggle an elements visibility
 * @param ele The id of the element or the HTML element itself
 * @deprecated DWR isn't a generic Javascript library
 */
DWRUtil.toggleDisplay = function(ele)
{
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

/**
 * Alter an rows in a table that have a class of zebra to have classes of either
 * oddrow or evenrow alternately.
 * This is probably not the best place for this method, but I dont want to have
 * to fight with multiple onload functions.
 * @deprecated DWR isn't a generic Javascript library
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
};

/**
 * Set the CSS class for an element
 * @param ele The id of the element or the HTML element itself
 * @deprecated DWR isn't a generic Javascript library
 */
DWRUtil.setCSSClass = function(ele, cssclass)
{
    var orig = ele;
    ele = $(ele);
    if (ele == null)
    {
        alert("setCSSClass() can't find an element with id: " + orig + ".");
        return;
    }

    ele.className = cssclass;
};

/**
 * Ensure a function is called when the page is loaded
 * @param load The function to call when the page has been loaded
 * @deprecated DWR isn't a generic Javascript library
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
};

/**
 * Remove all the options from a select list (specified by id) and replace with
 * elements in an array of objects.
 * @deprecated Use DWRUtil.removeAllOptions(ele); DWRUtil.addOptions(ele, data, valueprop, textprop);
 */
DWRUtil.fillList = function(ele, data, valueprop, textprop)
{
    DWRUtil.removeAllOptions(ele);
    DWRUtil.addOptions(ele, data, valueprop, textprop);
};

/**
 * Add rows to a table
 * @deprecated Use DWRUtil.addRows()
 */
DWRUtil.drawTable = function(ele, data, cellFuncs)
{
    DWRUtil.addRows(ele, data, cellFuncs);
};

/**
 * Remove all the children of a given node.
 * Most useful for dynamic tables where you clearChildNodes() on the tbody
 * element.
 * @param id The id of the element
 * @deprecated Use DWRUtil.removeAllRows()
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
};
