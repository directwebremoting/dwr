/**
 * Declare an object to which we can add real functions.
 * @author Alexandru Popescu
 */
if (dwr == null) var dwr = {};
if (dwr.webwork == null) dwr.webwork = {};
if (DWRActionUtil == null) var DWRActionUtil = dwr.webwork;

/** Execute a remote request using DWR */
dwr.webwork.execute = function(action, values, callbackObjOrName) {
  var params = {};
  if (dwr.webwork.isElement(values)) {
    var element = $(values);
    var elementName= element.nodeName.toLowerCase();
    if (elementName == 'form') {
      for (var i = 0; i < element.elements.length; i=i+1) {
        var e = element.elements[i];
        if (e.name != null && e.name != '') params[e.name] = dwr.util.getValue(e);
      }
    }
    else {
      params[element.name] = dwr.util.getValue(element);
    }
  }
  else {
    for (var prop in values) {
      params[prop]= values[prop];
    }
  }

  // prepare action invocation object
  var actionObj = {};
  if (typeof action == 'string') {
    var lastIdx= action.lastIndexOf('/');
    actionObj.executeResult = 'true';
    if (lastIdx != -1) {
      actionObj.namespace = action.substring(0, lastIdx);
      actionObj.action = action.substring(lastIdx + 1);
    }
    else {
      actionObj.namespace= '';
      actionObj.action = action;
    }
  }
  else {
    actionObj= action;
  }

  // prepare the DWR callback object
  var callbackObj = {};
  var mustCall= false;
  if (typeof callbackObjOrName == 'string') {
    callbackObj.callback = function(dt) { dwr.webwork.callback(dt, eval(callbackObjOrName)); };
    mustCall= true;
  }
  else if (typeof callbackObjOrName == 'function') {
    callbackObj.callback = function(dt) { dwr.webwork.callback(dt, callbackObjOrName); };
    mustCall= true;
  } 
  else if (typeof callbackObjOrName == 'object' && typeof callbackObjOrName.callback == 'function') {
    for (var prop in callbackObjOrName) {
      callbackObj[prop] = callbackObjOrName[prop];
    }
    callbackObj.callback = function(dt) { dwr.webwork.callback(dt, callbackObjOrName.callback); };
    mustCall= true;
  }
  if (mustCall) {
    dwr.webwork.execute(actionObj, params, callbackObj);
  }
};

/** Execute a remote request using DWR */
dwr.webwork.callback = function(dt, originalCallback) {
  if (dt.data) originalCallback(dt.data);
  else if (dt.text) originalCallback(dt.text);
  else originalCallback(dt);
};

/** Utility to check to see if the passed object is an input element / element id */
dwr.webwork.isElement = function(elementOrId) {
  if (typeof elementOrId == "string") {
    return true;
  }
  if (elementOrId.nodeName) {
    var name= elementOrId.nodeName.toLowerCase();
    if (name == 'input' || name == 'form') {
      return true;
    }
  }

  return false;
};
