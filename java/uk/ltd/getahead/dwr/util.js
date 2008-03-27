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
 * Declare a constructor function to which we can add real functions.
 * @constructor
 */
function DWRUtil() { }

/**
 * Enables you to react to return being pressed in an input
 * For example:
 * <code>&lt;input type="text" onkeypressed="DWRUtil.onReturn(event, methodName)"/&gt;</code>
 * @param event The event object for Netscape browsers
 * @param action Method name to execute when return is pressed
 */
DWRUtil.onReturn = function(event, action) {
  if (!event) {
    event = window.event;
  }
  if (event && event.keyCode && event.keyCode == 13) {
    action();
  }
};

/**
 * Select a specific range in a text box.
 * This is useful for 'google suggest' type functionallity.
 * @param ele The id of the text input element or the HTML element itself
 * @param start The beginning index
 * @param end The end index 
 */
DWRUtil.selectRange = function(ele, start, end) {
  var orig = ele;
  ele = $(ele);
  if (ele == null) {
    DWRUtil.debug("selectRange() can't find an element with id: " + orig + ".");
    return;
  }
  if (ele.setSelectionRange) {
    ele.setSelectionRange(start, end);
  }
  else if (ele.createTextRange) {
    var range = ele.createTextRange();
    range.moveStart("character", start);
    range.moveEnd("character", end - ele.value.length);
    range.select();
  }
  ele.focus();
};

/**
 * Find the element in the current HTML document with the given id or ids
 * @see http://getahead.ltd.uk/dwr/browser/util/$
 */
var $;
if (!$ && document.getElementById) {
  $ = function() {
    var elements = new Array();
    for (var i = 0; i < arguments.length; i++) {
      var element = arguments[i];
      if (typeof element == 'string') {
        element = document.getElementById(element);
      }
      if (arguments.length == 1) {
        return element;
      }
      elements.push(element);
    }
    return elements;
  }
}
else if (!$ && document.all) {
  $ = function() {
    var elements = new Array();
    for (var i = 0; i < arguments.length; i++) {
      var element = arguments[i];
      if (typeof element == 'string') {
        element = document.all[element];
      }
      if (arguments.length == 1) {
        return element;
      }
      elements.push(element);
    }
    return elements;
  }
}

/**
 * A better toString than the default for an Object
 * @param data The object to describe
 * @param level 0 = Single line of debug, 1 = Multi-line debug that does not
 *        dig into child objects, 2 = Multi-line debug that digs into the
 *        2nd layer of child objects
 * @param depth How much do we indent this item?
 */
DWRUtil.toDescriptiveString = function(data, level, depth) {
  var reply = "";
  var i = 0;
  var value;
  var obj;
  if (level == null) level = 0;
  if (depth == null) depth = 0;
  if (data == null) return "null";
  if (DWRUtil._isArray(data)) {
    if (data.length == 0) reply += "[]";
    else {
      if (level != 0) reply += "[\n";
      else reply = "[";
      for (i = 0; i < data.length; i++) {
        try {
          obj = data[i];
          if (obj == null || typeof obj == "function") {
            continue;
          }
          else if (typeof obj == "object") {
            if (level > 0) value = DWRUtil.toDescriptiveString(obj, level - 1, depth + 1);
            else value = DWRUtil._detailedTypeOf(obj);
          }
          else {
            value = "" + obj;
            value = value.replace(/\/n/g, "\\n");
            value = value.replace(/\/t/g, "\\t");
          }
        }
        catch (ex) {
          value = "" + ex;
        }
       if (level != 0)  {
          reply += DWRUtil._indent(level, depth + 2) + value + ", \n";
       }
        else {
          if (value.length > 13) value = value.substring(0, 10) + "...";
          reply += value + ", ";
          if (i > 5) {
            reply += "...";
            break;
          }
        }
      }
      if (level != 0) reply += DWRUtil._indent(level, depth) + "]";
      else reply += "]";
    }
    return reply;
  }
  if (typeof data == "string" || typeof data == "number" || DWRUtil._isDate(data)) {
    return data.toString();
  }
  if (typeof data == "object") {
    var typename = DWRUtil._detailedTypeOf(data);
    if (typename != "Object")  reply = typename + " ";
    if (level != 0) reply += "{\n";
    else reply = "{";
    var isHtml = DWRUtil._isHTMLElement(data);
    for (var prop in data) {
      if (isHtml) {
        // HTML nodes have far too much stuff. Chop out the constants
        if (prop.toUpperCase() == prop || prop == "title" ||
          prop == "lang" || prop == "dir" || prop == "className" ||
          prop == "form" || prop == "name" || prop == "prefix" ||
          prop == "namespaceURI" || prop == "nodeType" ||
          prop == "firstChild" || prop == "lastChild" ||
          prop.match(/^offset/)) {
          continue;
        }
      }
      value = "";
      try {
        obj = data[prop];
        if (obj == null || typeof obj == "function") {
          continue;
        }
        else if (typeof obj == "object") {
          if (level > 0) {
            value = "\n";
            value += DWRUtil._indent(level, depth + 2);
            value = DWRUtil.toDescriptiveString(obj, level - 1, depth + 1);
          }
          else {
            value = DWRUtil._detailedTypeOf(obj);
          }
        }
        else {
          value = "" + obj;
          value = value.replace(/\/n/g, "\\n");
          value = value.replace(/\/t/g, "\\t");
        }
      }
      catch (ex) {
        value = "" + ex;
      }
      if (level == 0 && value.length > 13) value = value.substring(0, 10) + "...";
      var propStr = prop;
      if (propStr.length > 30) propStr = propStr.substring(0, 27) + "...";
      if (level != 0) reply += DWRUtil._indent(level, depth + 1);
      reply += prop + ":" + value + ", ";
      if (level != 0) reply += "\n";
      i++;
      if (level == 0 && i > 5) {
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
DWRUtil._indent = function(level, depth) {
  var reply = "";
  if (level != 0) {
    for (var j = 0; j < depth; j++) {
      reply += "\u00A0\u00A0";
    }
    reply += " ";
  }
  return reply;
};

/**
 * Setup a GMail style loading message.
 */
DWRUtil.useLoadingMessage = function(message) {
  var loadingMessage;
  if (message) loadingMessage = message;
  else loadingMessage = "Loading";
  DWREngine.setPreHook(function() {
    var disabledZone = $('disabledZone');
    if (!disabledZone) {
      disabledZone = document.createElement('div');
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
      var text = document.createTextNode(loadingMessage);
      messageZone.appendChild(text);
    }
    else {
      $('messageZone').innerHTML = loadingMessage;
      disabledZone.style.visibility = 'visible';
    }
  });
  DWREngine.setPostHook(function() {
    $('disabledZone').style.visibility = 'hidden';
  });
}

/**
 * Set the value for the given id to the specified val.
 * This method works for selects (where the option with a matching value and
 * not text is selected), input elements (including textareas) divs and spans.
 * @param ele The id of the element or the HTML element itself
 */
DWRUtil.setValue = function(ele, val) {
  if (val == null) val = "";

  var orig = ele;
  var nodes, i;

  ele = $(ele);
  // We can work with names and need to sometimes for radio buttons
  if (ele == null) {
    nodes = document.getElementsByName(orig);
    if (nodes.length >= 1) {
      ele = nodes.item(0);
    }
  }
  if (ele == null) {
    DWRUtil.debug("setValue() can't find an element with id/name: " + orig + ".");
    return;
  }

  if (DWRUtil._isHTMLElement(ele, "select")) {
    if (ele.type == "select-multiple" && DWRUtil._isArray(val)) {
      DWRUtil._selectListItems(ele, val);
      }
    else {
      DWRUtil._selectListItem(ele, val);
    }
    return;
  }

  if (DWRUtil._isHTMLElement(ele, "input")) {
    if (nodes && ele.type == "radio") {
      for (i = 0; i < nodes.length; i++) {
        if (nodes.item(i).type == "radio") {
          nodes.item(i).checked = (nodes.item(i).value == val);
        }
      }
    }
    else {
      switch (ele.type) {
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
  }

  if (DWRUtil._isHTMLElement(ele, "textarea")) {
    ele.value = val;
    return;
  }

  // If the value to be set is a DOM object then we try importing the node
  // rather than serializing it out
  if (val.nodeType) {
    if (val.nodeType == 9 /*Node.DOCUMENT_NODE*/) {
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
 * Find multiple items in a select list and make them selected
 * @param ele The select list item
 * @param val The array of values to select
 */
DWRUtil._selectListItems = function(ele, val) {
  // We deal with select list elements by selecting the matching option
  // Begin by searching through the values
  var found  = false;
  var i;
  var j;
  for (i = 0; i < ele.options.length; i++) {
    ele.options[i].selected = false;
    for (j = 0; j < val.length; j++) {
      if (ele.options[i].value == val[j]) {
        ele.options[i].selected = true;
      }
    }
  }
  // If that fails then try searching through the visible text
  if (found) return;

  for (i = 0; i < ele.options.length; i++) {
    for (j = 0; j < val.length; j++) {
      if (ele.options[i].text == val[j]) {
        ele.options[i].selected = true;
      }
    }
  }
};

/**
 * Find an item in a select list and make it selected
 * @param ele The select list item
 * @param val The value to select
 */
DWRUtil._selectListItem = function(ele, val) {
  // We deal with select list elements by selecting the matching option
  // Begin by searching through the values
  var found  = false;
  var i;
  for (i = 0; i < ele.options.length; i++) {
    if (ele.options[i].value == val) {
      ele.options[i].selected = true;
      found = true;
    }
    else {
      ele.options[i].selected = false;
    }
  }

  // If that fails then try searching through the visible text
  if (found) return;

  for (i = 0; i < ele.options.length; i++) {
    if (ele.options[i].text == val) {
      ele.options[i].selected = true;
      break;
    }
  }
}

/**
 * The counterpart to setValue() - read the current value for a given element.
 * This method works for selects (where the option with a matching value and
 * not text is selected), input elements (including textareas) divs and spans.
 * @param ele The id of the element or the HTML element itself
 */
DWRUtil.getValue = function(ele) {
  var orig = ele;
  ele = $(ele);
  // We can work with names and need to sometimes for radio buttons, and IE has
  // an annoying bug where
  var nodes = document.getElementsByName(orig);
  if (ele == null && nodes.length >= 1) {
    ele = nodes.item(0);
  }
  if (ele == null) {
    DWRUtil.debug("getValue() can't find an element with id/name: " + orig + ".");
    return "";
  }

  if (DWRUtil._isHTMLElement(ele, "select")) {
    // This is a bit of a scam because it assumes single select
    // but I'm not sure how we should treat multi-select.
    var sel = ele.selectedIndex;
    if (sel != -1) {
      var reply = ele.options[sel].value;
      if (reply == null || reply == "") {
        reply = ele.options[sel].text;
      }

      return reply;
    }
    else {
      return "";
    }
  }

  if (DWRUtil._isHTMLElement(ele, "input")) {
    if (nodes && ele.type == "radio") {
      for (i = 0; i < nodes.length; i++) {
        if (nodes.item(i).type == "radio") {
          if (nodes.item(i).checked) {
            return nodes.item(i).value;
          }
        }
      }
    }
    switch (ele.type) {
    case "checkbox":
    case "check-box":
    case "radio":
      return ele.checked;
    default:
      return ele.value;
    }
  }

  if (DWRUtil._isHTMLElement(ele, "textarea")) {
    return ele.value;
  }

  return ele.innerHTML;
};

/**
 * getText() is like getValue() with the except that it only works for selects
 * where it reads the text of an option and not it's value.
 * @param ele The id of the element or the HTML element itself
 */
DWRUtil.getText = function(ele) {
  var orig = ele;
  ele = $(ele);
  if (ele == null) {
    DWRUtil.debug("getText() can't find an element with id: " + orig + ".");
    return "";
  }

  if (!DWRUtil._isHTMLElement(ele, "select")) {
    DWRUtil.debug("getText() can only be used with select elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele) + " from  id: " + orig + ".");
    return "";
  }

  // This is a bit of a scam because it assumes single select
  // but I'm not sure how we should treat multi-select.
  var sel = ele.selectedIndex;
  if (sel != -1) {
    return ele.options[sel].text;
  }
  else {
    return "";
  }
};

/**
 * Given a map, call setValue() for all the entries in the map using the key
 * of each entry as an id.
 * @param map The map of values to set to various elements
 */
DWRUtil.setValues = function(map) {
  for (var property in map) {
    var ele = $(property);
    if (ele != null) {
      var value = map[property];
      DWRUtil.setValue(property, value);
    }
  }
};

/**
 * Given a map, call getValue() for all the entries in the map using the key
 * of each entry as an id.
 * @param map The map of values to set to various elements
 */
DWRUtil.getValues = function(map) {
  for (var property in map) {
    var ele = $(property);
    if (ele != null) {
      map[property] = DWRUtil.getValue(property);
    }
  }
};

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
 * <p><b>Map of objects</b><br/>
 * DWRUtil.addOptions(selectid, map, valueprop, textprop) creates an option
 * for each object in the map, with the value of the option set to the given
 * valueprop property of the object, and the option text set to the textprop
 * property.
 * </p>
 * <p><b>ol or ul list</b><br/>
 * DWRUtil.addOptions(ulid, array) and a set of li elements are created with the
 * innerHTML set to the string value of the array elements. This mode works
 * with ul and ol lists.
 * </p>
 * @param ele The id of the list element or the HTML element itself
 * @param data An array or map of data items to populate the list
 * @param valuerev (optional) If data is an array of objects, an optional
 *    property name to use for option values. If the data is a map then this
 *    boolean property allows you to swap keys and values.
 * @param textprop (optional) Only for use with arrays of objects - an optional
 *    property name for use as the text of an option.
 */
DWRUtil.addOptions = function(ele, data) {
  var orig = ele;
  ele = $(ele);
  if (ele == null) {
    DWRUtil.debug("addOptions() can't find an element with id: " + orig + ".");
    return;
  }
  var useOptions = DWRUtil._isHTMLElement(ele, "select");
  var useLi = DWRUtil._isHTMLElement(ele, ["ul", "ol"]);
  if (!useOptions && !useLi) {
    DWRUtil.debug("addOptions() can only be used with select elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
    return;
  }
  if (data == null) return;

  var text;
  var value;
  var opt;
  if (DWRUtil._isArray(data)) {
    // Loop through the data that we do have
    for (var i = 0; i < data.length; i++) {
      if (useOptions) {
        if (arguments[2] != null) {
          if (arguments[3] != null) {
            text = DWRUtil._getValueFrom(data[i], arguments[3]);
            value = DWRUtil._getValueFrom(data[i], arguments[2]);
          }
          else {
            value = DWRUtil._getValueFrom(data[i], arguments[2]);
            text = value;
          }
        }
        else
        {
          text = DWRUtil._getValueFrom(data[i], arguments[3]);
          value = text;
        }
        if (text || value) {
          opt = new Option(text, value);
          ele.options[ele.options.length] = opt;
        }
      }
      else {
        li = document.createElement("li");
        value = DWRUtil._getValueFrom(data[i], arguments[2]);
        if (value != null) {
          li.innerHTML = value;
          ele.appendChild(li);
        }
      }
    }
  }
  else if (arguments[3] != null) {
    for (var prop in data) {
      if (!useOptions) {
        alert("DWRUtil.addOptions can only create select lists from objects.");
        return;
      }
      value = DWRUtil._getValueFrom(data[prop], arguments[2]);
      text = DWRUtil._getValueFrom(data[prop], arguments[3]);
      if (text || value) {
        opt = new Option(text, value);
        ele.options[ele.options.length] = opt;
      }
    }
  }
  else {
    for (var prop in data) {
      if (!useOptions) {
        DWRUtil.debug("DWRUtil.addOptions can only create select lists from objects.");
        return;
      }
      if (typeof data[prop] == "function") {
        // Skip this one it's a function.
        text = null;
        value = null;
      }
      else if (arguments[2]) {
        text = prop;
        value = data[prop];
      }
      else {
        text = data[prop];
        value = prop;
      }
      if (text || value) {
        opt = new Option(text, value);
        ele.options[ele.options.length] = opt;
      }
    }
  }
};

/**
 * Get the data from an array function for DWRUtil.addOptions
 * @private
 */
DWRUtil._getValueFrom = function(data, method) {
  if (method == null) return data;
  else if (typeof method == 'function') return method(data);
  else return data[method];
}

/**
 * Remove all the options from a select list (specified by id)
 * @param ele The id of the list element or the HTML element itself
 */
DWRUtil.removeAllOptions = function(ele) {
  var orig = ele;
  ele = $(ele);
  if (ele == null) {
    DWRUtil.debug("removeAllOptions() can't find an element with id: " + orig + ".");
    return;
  }
  var useOptions = DWRUtil._isHTMLElement(ele, "select");
  var useLi = DWRUtil._isHTMLElement(ele, ["ul", "ol"]);
  if (!useOptions && !useLi) {
    DWRUtil.debug("removeAllOptions() can only be used with select, ol and ul elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
    return;
  }
  if (useOptions) {
    ele.options.length = 0;
  }
  else {
    while (ele.childNodes.length > 0) {
      ele.removeChild(ele.firstChild);
    }
  }
};

/**
 * Create rows inside a the table, tbody, thead or tfoot element (given by id).
 * The normal case would be to use tbody since that allows you to keep header
 * lines separate, but this function should work with and table element above
 * tr.
 * <p>This function creates a row for each element in the <code>data</code>
 * array and for that row create one cell for each function in the
 * <code>cellFuncs</code> array by passing the element from the
 * <code>data</code> array to the given function.
 * <p>The return from the function is used to populate the cell.
 * <p>The pseudo code looks something like this:
 * <pre>
 *   for each member of the data array
 *   for function in the cellFuncs array
 *     create cell from cellFunc(data[i])
 * </pre>
 * <p>One slight modification to this is that any members of the cellFuncs array
 * that are strings instead of functions, the strings are used as cell contents
 * directly.
 * <p>There are 2 current options:<ul>
 * <li>rowCreator: a function that will create a row for you (e.g. you wish to
 *   add css to the tr). The default returns document.createElement("tr")</li>
 * <li>cellCreator: a function to create a cell, (e.g. to use a th in place of a
 *   td). The default returns document.createElement("td")</li>
 * </ul>
 * @param ele The id of the tbody element
 * @param data Array containing one entry for each row in the updated table
 * @param cellFuncs An array of functions (one per column) for extracting cell
 *  data from the passed row data
 * @param options An object containing various options. See above for options.
 */
DWRUtil.addRows = function(ele, data, cellFuncs, options) {
  var orig = ele;
  ele = $(ele);
  if (ele == null) {
    DWRUtil.debug("addRows() can't find an element with id: " + orig + ".");
    return;
  }
  if (!DWRUtil._isHTMLElement(ele, ["table", "tbody", "thead", "tfoot"])) {
    DWRUtil.debug("addRows() can only be used with table, tbody, thead and tfoot elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
    return;
  }
  if (!options) options = {};
  if (!options.rowCreator) options.rowCreator = DWRUtil._defaultRowCreator;
  if (!options.cellCreator) options.cellCreator = DWRUtil._defaultCellCreator;
  // TODO: remove the frag if it does not cause bugs: var frag = document.createDocumentFragment();
  var tr, i;
  if (DWRUtil._isArray(data)) {
    for (i = 0; i < data.length; i++) {
      tr = DWRUtil._addRowInner(data[i], cellFuncs, options);
      if (tr != null) ele.appendChild(tr);
    }
  }
  else if (typeof data == "object") {
    i = 0;
    for (var row in data) {
      tr = DWRUtil._addRowInner(row, cellFuncs, options, i);
      if (tr != null) ele.appendChild(tr);
      i++;
    }
  }
  //ele.appendChild(frag);
};

/**
 * Internal function to draw a single row of a table.
 * @private
 */
DWRUtil._addRowInner = function(row, cellFuncs, options, i) {
  var tr = options.rowCreator(row, i);
  if (tr == null) return null;
  for (var j = 0; j < cellFuncs.length; j++) {
    var func = cellFuncs[j];
    var td;
    if (typeof func == "string") {
      td = options.cellCreator();
      td.appendChild(document.createTextNode(func));
    }
    else {
      var reply = func(row);
      td = options.cellCreator(reply, j);
      if (DWRUtil._isHTMLElement(reply, "td")) td = reply;
      else if (DWRUtil._isHTMLElement(reply)) td.appendChild(reply);
      else td.innerHTML = reply;
    }
    tr.appendChild(td);
  }
  return tr;
};

/**
 * Default row creation function
 * @private
 */
DWRUtil._defaultRowCreator = function(row, i) {
  return document.createElement("tr");
};

/**
 * Default cell creation function
 * @private
 */
DWRUtil._defaultCellCreator = function(data, j) {
  return document.createElement("td");
};

/**
 * Remove all the children of a given node.
 * Most useful for dynamic tables where you clearChildNodes() on the tbody
 * element.
 * @param ele The id of the element or the HTML element itself
 */
DWRUtil.removeAllRows = function(ele) {
  var orig = ele;
  ele = $(ele);
  if (ele == null) {
    DWRUtil.debug("removeAllRows() can't find an element with id: " + orig + ".");
    return;
  }
  if (!DWRUtil._isHTMLElement(ele, ["table", "tbody", "thead", "tfoot"])) {
    DWRUtil.debug("removeAllRows() can only be used with table, tbody, thead and tfoot elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
    return;
  }
  while (ele.childNodes.length > 0) {
    ele.removeChild(ele.firstChild);
  }
};

/**
 * Is the given node an HTML element (optionally of a given type)?
 * @param ele The element to test
 * @param nodeName eg "input", "textarea" - check for node name (optional)
 *         if nodeName is an array then check of a match.
 * @private
 */
DWRUtil._isHTMLElement = function(ele, nodeName) {
  if (ele == null || typeof ele != "object" || ele.nodeName == null) {
    return false;
  }

  if (nodeName != null) {
    var test = ele.nodeName.toLowerCase();

    if (typeof nodeName == "string") {
      return test == nodeName.toLowerCase();
    }

    if (DWRUtil._isArray(nodeName)) {
      var match = false;
      for (var i = 0; i < nodeName.length && !match; i++) {
        if (test == nodeName[i].toLowerCase()) {
          match =  true;
        }
      }
      return match;
    }

    DWRUtil.debug("DWRUtil._isHTMLElement was passed test node name that is neither a string or array of strings");
    return false;
  }

  return true;
};

/**
 * Like typeOf except that more information for an object is returned other
 * than "object"
 * @private
 */
DWRUtil._detailedTypeOf = function(x) {
  var reply = typeof x;
  if (reply == "object") {
    reply = Object.prototype.toString.apply(x);  // Returns "[object class]"
    reply = reply.substring(8, reply.length-1);  // Just get the class bit
  }
  return reply;
};

/**
 * Array detector.
 * This is an attempt to work around the lack of support for instanceof in
 * some browsers.
 * @param data The object to test
 * @returns true iff <code>data</code> is an Array
 * @private
 */
DWRUtil._isArray = function(data) {
  return (data && data.join) ? true : false;
};

/**
 * Date detector.
 * This is an attempt to work around the lack of support for instanceof in
 * some browsers.
 * @param data The object to test
 * @returns true iff <code>data</code> is a Date
 * @private
 */
DWRUtil._isDate = function(data) {
  return (data && data.toUTCString) ? true : false;
};

/**
 * document.importNode is used by setValue.
 * This gets around the missing functionallity in IE.
 * @private
 */
DWRUtil._importNode = function(doc, importedNode, deep) {
  var newNode;

  if (importedNode.nodeType == 1 /*Node.ELEMENT_NODE*/) {
    newNode = doc.createElement(importedNode.nodeName);

    for (var i = 0; i < importedNode.attributes.length; i++) {
      var attr = importedNode.attributes[i];
      if (attr.nodeValue != null && attr.nodeValue != '') {
        newNode.setAttribute(attr.name, attr.nodeValue);
      }
    }

    if (typeof importedNode.style != "undefined") {
      newNode.style.cssText = importedNode.style.cssText;
    }
  }
  else if (importedNode.nodeType == 3 /*Node.TEXT_NODE*/) {
    newNode = doc.createTextNode(importedNode.nodeValue);
  }

  if (deep && importedNode.hasChildNodes()) {
    for (i = 0; i < importedNode.childNodes.length; i++) {
      newNode.appendChild(DWRUtil._importNode(doc, importedNode.childNodes[i], true));
    }
  }

  return newNode;
}

/**
 * Used internally when some message needs to get to the programmer
 */
DWRUtil.debug = function(message) {
  alert(message);
}
