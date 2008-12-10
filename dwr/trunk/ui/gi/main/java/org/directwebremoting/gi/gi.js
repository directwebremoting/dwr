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
 * Declare an object to which we can add real functions.
 */
if (typeof this['dwr'] == 'undefined') {
  this.dwr = {};
}
if (typeof dwr['gi'] == 'undefined') {
  dwr.gi = {};
}

/**
 * Convert a javascript object into a CDF document with the given jsxid
 * @param {Object} data The data that we want to publish in a CDF document
 * @param {String} jsxid The jsxid of the returned document
 */
dwr.gi.toCdfDocument = function(data, jsxid) {
  var cdfstr = dwr.gi._toCdfDocumentString(data, jsxid);
  var cdf = new jsx3.xml.Document();
  cdf.loadXML(cdfstr);
  return cdf;
};

/**
 * Convert an XML String into a JSX3 Document.
 * This method just calls jsx3.xml.Document.loadXML(), however since this method
 * is static it can be called as a one-off without setup, which is useful when
 * using pseudo-json.
 * @private
 * @param {String} xmlString The string serialization of the XML
 * @return {jsx3.xml.Document} A JSX3 in-memory model of the XML
 */
dwr.gi._loadXml = function(xmlString) {
  var doc = new jsx3.xml.Document();
  doc.loadXML(xmlString);
  return doc;
};

/**
 * Convert a javascript object into a CDF RecordSet
 * @param data The data that we want to publish in a CDF record set
 */
dwr.gi.toRecordSet = function(data) {
  if (data == null) return "";
  if (data instanceof Array) {
    return dwr.gi._convertArray(data, "", 0);
  }
  if (data instanceof Object) {
    return dwr.gi._convertObject(data, "", 0);
  }
  return dwr.gi._convertOther(data, "root", 0);
};

/** @private Convert any Javascript object to a CDF Document string */
dwr.gi._toCdfDocumentString = function(data, jsxid) {
  return "<data jsxid='" + jsxid + "'>\n" + dwr.gi.toRecordSet(data) + "</data>\n";
};

/** @private Convert an array to a JSX string */
dwr.gi._convertArray = function(data, name, depth) {
  var reply = "";
  for (var i = 0; i < data.length; i++) {
    reply += dwr.gi._convertAttributes(data[i], name + i, depth);
  }
  for (var i = 0; i < data.length; i++) {
    reply += dwr.gi._convertElements(data[i], name + i, depth);
  }
  return reply;
};

/** @private Convert an object to a JSX string */
dwr.gi._convertObject = function(data, name, depth) {
  var reply = "";
  for (var element in data) {
    reply += dwr.gi._convertAttributes(data[element], element, depth);
  }
  for (var element in data) {
    reply += dwr.gi._convertElements(data[element], element, depth);
  }
  return reply;
};

/** @private The characters that we need to escape from an JS perspective */
dwr.gi._specials = [ '\\', '\'', '"' ];

/** @private The Regex that works on the characters needing escaping */
dwr.gi._escapeRegexp = new RegExp('(\\' + dwr.gi._specials.join('|\\') + ')', 'g');

/** @private Escape a string so it is valid */
dwr.gi._escape = function(text) {
  return text.replace(dwr.gi._escapeRegexp, '\\$1');
};

/** @private Convert an object of unknown type to a JSX string */
dwr.gi._convertAttributes = function(data, name, depth) {
  if (typeof data == "function") {
    return ""; // ignore functions
  }
  if (typeof data == "string" || data instanceof String) {
    // TODO: escape the string
    return " " + name + "='" + data + "'";
  }
  if (typeof data == "object") {
    if (data instanceof Date) {
      return " " + name + "='" + data.toUTCString() + "'";
    }
    return "";
  }
  return " " + name + "='" + dwr.gi._escape(data) + "'";
};

/** @private Convert an object of unknown type to a JSX string */
dwr.gi._convertElements = function(data, name, depth) {
  var reply;
  if (data == null) {
    return ""; // don't create an empty element for null objects
  }
  if (typeof data == "function") {
    return ""; // ignore functions
  }
  if (typeof data == "object") {
    if (data instanceof Date) {
      return "";
    }
    if (data instanceof Array) {
      reply = dwr.gi._indent(depth) + "<record index='0' jsxid='" + name + "' jsxtext='" + name + "' >\n";
      reply += dwr.gi._convertArray(data, name + ".", depth + 1);
      reply += dwr.gi._indent(depth) + "</record>\n";
      return reply;
    }
    reply = dwr.gi._indent(depth) + "<record";
    for (var element in data) {
      reply += dwr.gi._convertAttributes(data[element], element, depth + 1);
    }
    reply += ">\n";
    for (var element in data) {
      reply += dwr.gi._convertElements(data[element], element, depth + 1);
    }
    reply += dwr.gi._indent(depth) + "</record>\n";
    return reply;
  }
  return "";
};

/** @private Do we try to keep the generated source looking good? */
dwr.gi._prettyPrint = false;

/** @private Pretty printing */
dwr.gi._indent = function(depth) {
  if (!dwr.gi._prettyPrint) return "";
  for (var i = 0; i < depth; i++) {
      reply += "  ";
  }
  return reply;
};
