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
 * @see http://getahead.ltd.uk/dwr/browser/util/selectrange
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
 * Select a specific range in a text box. Useful for 'google suggest' type functions.
 * @see http://getahead.ltd.uk/dwr/browser/util/selectrange
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
 * Like toString but aimed at debugging
 * @see http://getahead.ltd.uk/dwr/browser/util/todescriptivestring
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
 * @private Indenting for DWRUtil.toDescriptiveString
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
 * @see http://getahead.ltd.uk/dwr/browser/util/useloadingmessage
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
 * Set the value an HTML element to the specified value.
 * @see http://getahead.ltd.uk/dwr/browser/util/setvalue
 */
DWRUtil.setValue = function(ele, val, options) {
  if (val == null) val = "";
  if (options != null) {
    if (options.escapeHtml) {
      val = val.replace(/&/, "&amp;");
      val = val.replace(/'/, "&apos;");
      val = val.replace(/</, "&lt;");
      val = val.replace(/>/, "&gt;");
    }
  }

  var orig = ele;
  var nodes, node, i;

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
    if (ele.type == "radio") {
      // Some browsers match names when looking for ids, so check names anyway.
      if (nodes == null) nodes = document.getElementsByName(orig);
      if (nodes != null && nodes.length > 1) {
        for (i = 0; i < nodes.length; i++) {
          node = nodes.item(i);
          if (node.type == "radio") {
            node.checked = (node.value == val);
          }
        }
      }
      else {
        ele.checked = (val == true);
      }
    }
    else if (ele.type == "checkbox") {
      ele.checked = val;
    }
    else {
      ele.value = val;
    }
    return;
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
 * @private Find multiple items in a select list and select them. Used by setValue()
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
 * @private Find an item in a select list and select it. Used by setValue()
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
    }
    else {
      ele.options[i].selected = false;
    }
  }
}

/**
 * Read the current value for a given HTML element.
 * @see http://getahead.ltd.uk/dwr/browser/util/getvalue
 */
DWRUtil.getValue = function(ele, options) {
  if (options == null) {
    options = {};
  }
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
    if (ele.type == "radio") {
      var node;
      for (i = 0; i < nodes.length; i++) {
        node = nodes.item(i);
        if (node.type == "radio") {
          if (node.checked) {
            if (nodes.length > 1) return node.value;
            else return true;
          }
        }
      }
    }
    switch (ele.type) {
    case "checkbox":
    case "check-box":
    case "radio":
      if (ele.checked && ele.value != "")
        return ele.value;
      else
        return ele.checked;
    default:
      return ele.value;
    }
  }

  if (DWRUtil._isHTMLElement(ele, "textarea")) {
    return ele.value;
  }

  if (options.textContent) {
    if (ele.textContent) return ele.textContent;
    else if (ele.innerText) return ele.innerText;
  }
  return ele.innerHTML;
};

/**
 * getText() is like getValue() except that it reads the text (and not the value) from select elements
 * @see http://getahead.ltd.uk/dwr/browser/util/gettext
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
 * Given a map, call setValue() for all the entries in the map using the entry key as an element id
 * @see http://getahead.ltd.uk/dwr/browser/util/setvalues
 */
DWRUtil.setValues = function(map) {
  for (var property in map) {
    // Are there any elements with that id or name
    if ($(property) != null || document.getElementsByName(property).length >= 1) {
      DWRUtil.setValue(property, map[property]);
    }
  }
};

/**
 * Given a map, call getValue() for all the entries in the map using the entry key as an element id.
 * Given a string or element that refers to a form, create an object from the elements of the form.
 * @see http://getahead.ltd.uk/dwr/browser/util/getvalues
 */
DWRUtil.getValues = function(data) {
  var ele;
  if (typeof data == "string") ele = $(data);
  if (DWRUtil._isHTMLElement(data)) ele = data;
  if (ele != null) {
    if (ele.elements == null) {
      alert("getValues() requires an object or reference to a form element.");
      return null;
    }
    var reply = {};
    var value;
    for (var i = 0; i < ele.elements.length; i++) {
      if (ele[i].id != null) value = ele[i].id;
      else if (ele[i].value != null) value = ele[i].value;
      else value = "element" + i;
      reply[value] = DWRUtil.getValue(ele[i]);
    }
    return reply;
  }
  else {
    for (var property in data) {
      // Are there any elements with that id or name
      if ($(property) != null || document.getElementsByName(property).length >= 1) {
        data[property] = DWRUtil.getValue(property);
      }
    }
    return data;
  }
};

/**
 * Add options to a list from an array or map.
 * @see http://getahead.ltd.uk/dwr/browser/lists
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
    DWRUtil.debug("addOptions() can only be used with select/ul/ol elements. Attempt to use: " + DWRUtil._detailedTypeOf(ele));
    return;
  }
  if (data == null) return;

  var text;
  var value;
  var opt;
  var li;
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
 * @private Get the data from an array function for DWRUtil.addOptions
 */
DWRUtil._getValueFrom = function(data, method) {
  if (method == null) return data;
  else if (typeof method == 'function') return method(data);
  else return data[method];
}

/**
 * Remove all the options from a select list (specified by id)
 * @see http://getahead.ltd.uk/dwr/browser/lists
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
 * @see http://getahead.ltd.uk/dwr/browser/tables
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
  var tr, rowNum;
  if (DWRUtil._isArray(data)) {
    for (rowNum = 0; rowNum < data.length; rowNum++) {
      options.rowData = data[rowNum];
      options.rowIndex = rowNum;
      options.rowNum = rowNum;
      options.data = null;
      options.cellNum = -1;
      tr = DWRUtil._addRowInner(cellFuncs, options);
      if (tr != null) ele.appendChild(tr);
    }
  }
  else if (typeof data == "object") {
    rowNum = 0;
    for (var rowIndex in data) {
      options.rowData = data[rowIndex];
      options.rowIndex = rowIndex;
      options.rowNum = rowNum;
      options.data = null;
      options.cellNum = -1;
      tr = DWRUtil._addRowInner(cellFuncs, options);
      if (tr != null) ele.appendChild(tr);
      rowNum++;
    }
  }
};

/**
 * @private Internal function to draw a single row of a table.
 */
DWRUtil._addRowInner = function(cellFuncs, options) {
  var tr = options.rowCreator(options);
  if (tr == null) return null;
  for (var cellNum = 0; cellNum < cellFuncs.length; cellNum++) {
    var func = cellFuncs[cellNum];
    var reply = func(options.rowData, options);
    options.data = reply;
    options.cellNum = cellNum;
    var td = options.cellCreator(options);
    if (td != null) {
      if (reply != null) {
        if (DWRUtil._isHTMLElement(reply)) td.appendChild(reply);
        else td.innerHTML = reply;
      }
      tr.appendChild(td);
    }
  }
  return tr;
};

/**
 * @private Default row creation function
 */
DWRUtil._defaultRowCreator = function(options) {
  return document.createElement("tr");
};

/**
 * @private Default cell creation function
 */
DWRUtil._defaultCellCreator = function(options) {
  return document.createElement("td");
};

/**
 * Remove all the children of a given node.
 * @see http://getahead.ltd.uk/dwr/browser/tables
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
 * Clone a node and insert it into the document just above the 'template' node
 * @see http://getahead.ltd.uk/dwr/???
 */
DWRUtil.cloneNode = function(ele, options) {
  if (options == null) options = {};
  var orig = ele;
  ele = $(ele);
  if (ele == null) {
    DWRUtil.debug("cloneNode2() can't find an element with id: " + orig + ".");
    return null;
  }
  var clone = ele.cloneNode(true);
  if (options.idPrefix || options.idSuffix) {
    DWRUtil._updateIds(clone, options);
  }
  else {
    DWRUtil._removeIds(clone);
  }
  ele.parentNode.insertBefore(clone, ele);
  return clone;
}

/**
 * @private Update all of the ids in an element tree
 */
DWRUtil._updateIds = function(ele, options) {
  if (options == null) options = {};
  var orig = ele;
  ele = $(ele);
  if (ele == null) {
    DWRUtil.debug("_updateIds() can't find an element with id: " + orig + ".");
    return;
  }
  if (ele.id) {
    ele.setAttribute("id", (options.idPrefix || "") + ele.id + (options.idSuffix || ""));
  }
  var children = ele.childNodes;
  for (var i = 0; i < children.length; i++) {
    var child = children.item(i);
    if (child.nodeType == 1 /*Node.ELEMENT_NODE*/) {
      DWRUtil._updateIds(child, options);
    }
  }
}

/**
 * @private Remove all the Ids from an element
 */
DWRUtil._removeIds = function(ele) {
  var orig = ele;
  ele = $(ele);
  if (ele == null) {
    DWRUtil.debug("_removeIds() can't find an element with id: " + orig + ".");
    return;
  }
  if (ele.id) ele.removeAttribute("id");
  var children = ele.childNodes;
  for (var i = 0; i < children.length; i++) {
    var child = children.item(i);
    if (child.nodeType == 1 /*Node.ELEMENT_NODE*/) {
      DWRUtil._removeIds(child);
    }
  }
}

/**
 * @private Is the given node an HTML element (optionally of a given type)?
 * @param ele The element to test
 * @param nodeName eg "input", "textarea" - check for node name (optional)
 *         if nodeName is an array then check all for a match.
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
 * @private Like typeOf except that more information for an object is returned other than "object"
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
 * @private Array detector. Work around the lack of instanceof in some browsers.
 */
DWRUtil._isArray = function(data) {
  return (data && data.join) ? true : false;
};

/**
 * @private Date detector. Work around the lack of instanceof in some browsers.
 */
DWRUtil._isDate = function(data) {
  return (data && data.toUTCString) ? true : false;
};

/**
 * @private Used by setValue. Gets around the missing functionallity in IE.
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

/** The array of current debug items */
DWRUtil._debugDisplay = [];

/** What is the maximum length of the debug items array */
DWRUtil._debugMaxLength = 50;

/**
 * Used internally when some message needs to get to the programmer
 */
DWRUtil.debug = function(message) {
  var debug = $("dwr-debug");
  if (debug) {
    while (DWRUtil._debugDisplay.length >= DWRUtil._debugMaxLength) {
      DWRUtil._debugDisplay.shift();
    }
    DWRUtil._debugDisplay.push(message);
    var contents = "";
    for (var i = 0; i < DWRUtil._debugDisplay.length; i++) {
      contents += DWRUtil._debugDisplay[i] + "<br/>";
    }
    DWRUtil.setValue("dwr-debug", contents);
  }
  else if (window.console) window.console.log(message);
  else if (window.opera && window.opera.postError) window.opera.postError(message);
  else if (window.navigator.product == "Gecko") window.dump(message + "\n");
  //alert(message);
}
