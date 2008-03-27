/* WW-DWR */
var DWRActionUtil = {
  execute : function(action, values, callbackObjOrName, displayMessage) {
    var params= new Object();
    if (this.isElement(values)) {
      var element = this.getElement(values);
      var elementName= element.nodeName.toLowerCase();
      if (elementName == "input") {
        if(element.name != null && element.name != '') params[element.name] = element.value;
      }
      else if (elementName == 'form') {
          for (var i = 0; i < element.elements.length; i=i+1) {
              var e = element.elements[i];
              if (e.name != null && e.name != '') params[e.name] = e.value;
          }
      }
    }
    else {
      for(var prop in values) {
        params[prop]= values[prop];
      }
    }
    
    // prepare action invocation object
    var actionObj= {};
    if(typeof action == 'string') {
      var lastIdx= action.lastIndexOf('/');
      actionObj.executeResult= 'true';
      if(lastIdx != -1) {
        actionObj.namespace= action.substring(0, lastIdx);
        actionObj.action= action.substring(lastIdx + 1);
      }
      else {
        actionObj.namespace= '';
        actionObj.action= action;
      }
    }
    else {
      actionObj= action;
    }
    
    // prepare message if any
    var useMessage= false;
    if(displayMessage) {
      DWRUtil.useLoadingMessage(displayMessage);
      useMessage= true;
    }
    
    // prepare the DWR callback object
    var callbackObj = {};
    var originalCallback = {};
    var mustCall= false;
    if(typeof callbackObjOrName == 'string') {
      originalCallback.method = eval(callbackObjOrName);
      callbackObj.callback = function(dt) {
        try {
          if(dt.data) {
            originalCallback.method(dt.data);
          }
          else if(dt.text) {
            originalCallback.method(dt.text);
          }
          else {
            originalCallback.method(dt);
          }
        }
        finally {
          if(useMessage) {
            DWREngine.setPreHook(null);
            DWREngine.setPostHook(null);
          }
        }
      };
      mustCall= true;
    }
    else if(typeof callbackObjOrName == 'function') {
      originalCallback.method = callbackObjOrName;
      callbackObj.callback = function(dt) {
        try {
          if(dt.data) {
            originalCallback.method(dt.data);
          }
          else if(dt.text) {
            originalCallback.method(dt.text);
          }
          else {
            originalCallback.method(dt);
          }
        }
        finally {
          if(useMessage) {
            DWREngine.setPreHook(null);
            DWREngine.setPostHook(null);
          }
        }         
      };
      mustCall= true;
    } 
    else if(typeof callbackObjOrName == 'object' && typeof callbackObjOrName.callback == 'function') {
      for(var prop in callbackObjOrName) {
        callbackObj[prop]= callbackObjOrName[prop];
      }
      callbackObj.callback = function(dt) {
        try {
          if(dt.data) {
            callbackObjOrName.callback(dt.data);
          }
          else if(dt.text) {
            callbackObjOrName.callback(dt.text);
          }
          else {
            callbackObjOrName.callback(dt);
          }
        }
        finally {
          if(useMessage) {
            DWREngine.setPreHook(null);
            DWREngine.setPostHook(null);
          }
        }
      };
      mustCall= true;
    }
    if(mustCall) {
      DWRRewriteAction.execute(actionObj, params, callbackObj);
    }
  },
  
  isElement : function(elementOrId) {
    if (typeof elementOrId == "string") {
      return true;
    }
    if ( elementOrId.nodeName ) {
      var name= elementOrId.nodeName.toLowerCase();
      if(name == 'input' || name == 'form') {
        return true;
      }
    }
    
    return false;
  },
  
  getElement : function(elementOrId) {
    var elem;
    if (typeof elementOrId == "string") {
      elem = document.getElementById(elementOrId);
    }
    else {
      elem = elementOrId;
    }
    
    return elem;
  }
};