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
 */
if (DWREngine == null) var DWREngine = {};

/**
 * Set an alternative error handler from the default alert box.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
DWREngine.setErrorHandler = function(handler) {
  DWREngine._errorHandler = handler;
};

/**
 * Set an alternative warning handler from the default alert box.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
DWREngine.setWarningHandler = function(handler) {
  DWREngine._warningHandler = handler;
};

/**
 * Setter for the text/html handler - what happens if a DWR request gets an HTML
 * reply rather than the expected Javascript. Often due to login timeout
 */
DWREngine.setTextHtmlHandler = function(handler) {
  DWREngine._textHtmlHandler = handler;
}

/**
 * Set a default timeout value for all calls. 0 (the default) turns timeouts off.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
DWREngine.setTimeout = function(timeout) {
  DWREngine._timeout = timeout;
};

/**
 * The Pre-Hook is called before any DWR remoting is done.
 * @see http://getahead.ltd.uk/dwr/browser/engine/hooks
 */
DWREngine.setPreHook = function(handler) {
  DWREngine._preHook = handler;
};

/**
 * The Post-Hook is called after any DWR remoting is done.
 * @see http://getahead.ltd.uk/dwr/browser/engine/hooks
 */
DWREngine.setPostHook = function(handler) {
  DWREngine._postHook = handler;
};

/** XHR remoting type constant. See DWREngine.set[Rpc|Poll]Type() */
DWREngine.XMLHttpRequest = 1;

/** XHR remoting type constant. See DWREngine.set[Rpc|Poll]Type() */
DWREngine.IFrame = 2;

/** XHR remoting type constant. See DWREngine.setRpcType() */
DWREngine.ScriptTag = 3;

/**
 * Set the preferred remoting type.
 * @param newType One of DWREngine.XMLHttpRequest or DWREngine.IFrame or DWREngine.ScriptTag
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
DWREngine.setRpcType = function(newType) {
  if (newType != DWREngine.XMLHttpRequest && newType != DWREngine.IFrame && newType != DWREngine.ScriptTag) {
    DWREngine._handleError(null, { name:"dwrengine.invalidRpcType", message:"RpcType must be one of DWREngine.XMLHttpRequest or DWREngine.IFrame or DWREngine.ScriptTag" });
    return;
  }
  DWREngine._rpcType = newType;
};

/**
 * Which HTTP method do we use to send results? Must be one of "GET" or "POST".
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
DWREngine.setHttpMethod = function(httpMethod) {
  if (httpMethod != "GET" && httpMethod != "POST") {
    DWREngine._handleError(null, { name:"dwrengine.invalidHttpMethod", message:"Remoting method must be one of GET or POST" });
    return;
  }
  DWREngine._httpMethod = httpMethod;
};

/**
 * Ensure that remote calls happen in the order in which they were sent? (Default: false)
 * @see http://getahead.ltd.uk/dwr/browser/engine/ordering
 */
DWREngine.setOrdered = function(ordered) {
  DWREngine._ordered = ordered;
};

/**
 * Do we ask the XHR object to be asynchronous? (Default: true)
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
DWREngine.setAsync = function(async) {
  DWREngine._async = async;
};

/**
 * Does DWR poll the server for updates? (Default: false)
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
DWREngine.setReverseAjax = function(reverseAjax) {
  DWREngine._reverseAjax = reverseAjax;
  if (DWREngine._reverseAjax) DWREngine._poll();
};

/**
 * Does DWR us comet polling? (Default: true)
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
DWREngine.setPollUsingComet = function(pollComet) {
  DWREngine._pollComet = pollComet;
};

/**
 * Set the preferred polling type.
 * @param newPollType One of DWREngine.XMLHttpRequest or DWREngine.IFrame
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
DWREngine.setPollType = function(newPollType) {
  if (newPollType != DWREngine.XMLHttpRequest && newPollType != DWREngine.IFrame) {
    DWREngine._handleError(null, { name:"dwrengine.invalidPollType", message:"PollType must be one of DWREngine.XMLHttpRequest or DWREngine.IFrame"  });
    return;
  }
  DWREngine._pollType = newPollType;
};

/**
 * The default message handler.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
DWREngine.defaultMessageHandler = function(ex) {
  if (typeof ex == "object" && ex.message) {
    alert(ex.message);
  }
  else {
    // Ignore NS_ERROR_NOT_AVAILABLE if Mozilla is being narky
    var desc = ex.toString();
    if (desc.indexOf("0x80040111") != -1) DWREngine._debug(desc);
    else alert(desc);
  }
};

/**
 * The default message handler.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
DWREngine.defaultWarningHandler = function(ex) {
  if (typeof ex == "object" && ex.message) {
    DWREngine._debug(ex.message);
  }
  else {
    DWREngine._debug(ex);
  }
};

/**
 * For reduced latency you can group several remote calls together using a batch.
 * @see http://getahead.ltd.uk/dwr/browser/engine/batch
 */
DWREngine.beginBatch = function() {
  if (DWREngine._batch) {
    DWREngine._handleError(null, { name:"dwrengine.batchBegun", message:"Batch already begun" });
    return;
  }
  DWREngine._batch = {
    headers:{}, map:{ callCount:0 }, paramCount:0,
    isPoll:false, batchIds:[], preHooks:[], postHooks:[]
  };
};

/**
 * Finished grouping a set of remote calls together. Go and execute them all.
 * @see http://getahead.ltd.uk/dwr/browser/engine/batch
 */
DWREngine.endBatch = function(options) {
  if (options == null) options = {};
  var batch = DWREngine._batch;
  if (batch == null) {
    DWREngine._handleError(null, { name:"dwrengine.batchNotBegun", message:"No batch in progress" });
    return;
  }
  // The hooks need to be merged carefully to preserve ordering
  if (options && options.preHook) batch.preHooks.unshift(options.preHook);
  if (options && options.postHook) batch.postHooks.push(options.postHook);
  if (DWREngine._preHook) batch.preHooks.unshift(DWREngine._preHook);
  if (DWREngine._postHook) batch.postHooks.push(DWREngine._postHook);
  // Other props. priority: batch, call, global - the order in which they were set
  var propnames = [ "rpcType", "httpMethod", "async", "timeout" ];
  var propname;
  for (var i = 0; i < propnames.length; i++) {
    propname = propnames[i];
    if (options[propname] != null) batch[propname] = options[propname];
    if (batch[propname] == null) batch[propname] = DWREngine["_" + propname];
  }

  batch.completed = false;

  // We are about to send so this batch should not be globally visible
  DWREngine._batch = null;

  if (batch.map.callCount == 0) return;

  // In ordered mode, we don't send unless the list of sent items is empty
  if (DWREngine._ordered && DWREngine._batches.length != 0) {
    DWREngine._batchQueue[DWREngine._batchQueue.length] = batch;
  }
  else {
    DWREngine._batches[DWREngine._batches.length] = batch;
    DWREngine._sendData(batch);
  }
};

/** @deprecated */
DWREngine.setPollMethod = function(type) { DWREngine.setPollType(type); };
DWREngine.setMethod = function(type) { DWREngine.setRpcType(type); };
DWREngine.setVerb = function(verb) { DWREngine.setHttpMethod(verb); };

//==============================================================================
// Only private stuff below here
//==============================================================================

/** The original page id sent from the server */
DWREngine._origScriptSessionId = "${scriptSessionId}";

/** The session cookie name */
DWREngine._sessionCookieName = "${sessionCookieName}"; // JSESSIONID

/** The read page id that we calculate */
DWREngine._scriptSessionId = null;

/** The function that we use to fetch/calculate a session id */
DWREngine._getScriptSessionId = function() {
  if (DWREngine._scriptSessionId == null) {
    DWREngine._scriptSessionId = DWREngine._origScriptSessionId + Math.floor(Math.random() * 1000);
  }
  return DWREngine._scriptSessionId;
};

/** A function to call if something fails. */
DWREngine._errorHandler = DWREngine.defaultMessageHandler;

/** By default exceptions are handled by the errorHandler */
DWREngine._exceptionHandler = function(message) {
  DWREngine._errorHandler(message);
};

/** For debugging when something unexplained happens. */
DWREngine._warningHandler = null;

/** A function to be called before requests are marshalled. Can be null. */
DWREngine._preHook = null;

/** A function to be called after replies are received. Can be null. */
DWREngine._postHook = null;

/** An array of the batches that we have sent and are awaiting a reply on. */
DWREngine._batches = [];

/** In ordered mode, the array of batches waiting to be sent */
DWREngine._batchQueue = [];

/** What is the default rpc type */
DWREngine._rpcType = DWREngine.XMLHttpRequest;

/** What is the default remoting method (ie GET or POST) */
DWREngine._httpMethod = "POST";

/** Do we attempt to ensure that calls happen in the order in which they were sent? */
DWREngine._ordered = false;

/** Do we make the calls async? */
DWREngine._async = true;

/** The current batch (if we are in batch mode) */
DWREngine._batch = null;

/** The global timeout */
DWREngine._timeout = 0;

/** ActiveX objects to use when we want to convert an xml string into a DOM object. */
DWREngine._DOMDocument = ["Msxml2.DOMDocument.6.0", "Msxml2.DOMDocument.5.0", "Msxml2.DOMDocument.4.0", "Msxml2.DOMDocument.3.0", "MSXML2.DOMDocument", "MSXML.DOMDocument", "Microsoft.XMLDOM"];

/** The ActiveX objects to use when we want to do an XMLHttpRequest call. */
DWREngine._XMLHTTP = ["Msxml2.XMLHTTP.6.0", "Msxml2.XMLHTTP.5.0", "Msxml2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP", "Microsoft.XMLHTTP"];

/** Are we doing reverse ajax? */
DWREngine._reverseAjax = false;

/** Is there a long term poll (comet) interraction in place? */
DWREngine._pollComet = true;

/** What is the default polling type */
DWREngine._pollType = DWREngine.XMLHttpRequest;
//DWREngine._pollType = DWREngine.IFrame;

/** The iframe that we are using to poll */
DWREngine._pollFrame = null;

/** The xhr object that we are using to poll */
DWREngine._pollReq = null;

/** How much data has been received into a reverse ajax document */
DWREngine._pollCometSize = 0;

/** How many milliseconds between internal comet polls */
DWREngine._pollCometInterval = 200;

/** Do we do a document.reload if we get a text/html reply? */
DWREngine._textHtmlHandler = null;

/** Undocumented interceptors - do not use */
DWREngine._postSeperator = "\n";
DWREngine._defaultInterceptor = function(data) {return data;}
DWREngine._urlRewriteHandler = DWREngine._defaultInterceptor;
DWREngine._contentRewriteHandler = DWREngine._defaultInterceptor;
DWREngine._replyRewriteHandler = DWREngine._defaultInterceptor;

/** Batch ids allow us to know which question the server is answering */
DWREngine._lastBatchId = 0;

/**
 * @private Send a request. Called by the Javascript interface stub
 * @param path part of URL after the host and before the exec bit without leading or trailing /s
 * @param scriptName The class to execute
 * @param methodName The method on said class to execute
 * @param func The callback function to which any returned data should be passed
 *       if this is null, any returned data will be ignored
 * @param vararg_params The parameters to pass to the above class
 */
DWREngine._execute = function(path, scriptName, methodName, vararg_params) {
  var singleShot = false;
  if (DWREngine._batch == null) {
    DWREngine.beginBatch();
    singleShot = true;
  }
  // To make them easy to manipulate we copy the arguments into an args array
  var args = [];
  for (var i = 0; i < arguments.length - 3; i++) {
    args[i] = arguments[i + 3];
  }
  // All the paths MUST be to the same servlet
  if (DWREngine._batch.path == null) {
    DWREngine._batch.path = path;
  }
  else {
    if (DWREngine._batch.path != path) {
      DWREngine._handleError(null, { name:"dwrengine.multipleServlets", message:"Can't batch requests to multiple DWR Servlets." });
      return;
    }
  }
  // From the other params, work out which is the function (or object with
  // call meta-data) and which is the call parameters
  var callData;
  var lastArg = args[args.length - 1];
  if (typeof lastArg == "function" || lastArg == null) callData = { callback:args.pop() };
  else callData = args.pop();

  // Get a unique ID for this call
  DWREngine._lastBatchId++;
  var batchId = DWREngine._lastBatchId;

  // Merge from the callData into the batch
  if (callData.rpcType != null) DWREngine._batch.rpcType = callData.rpcType;
  if (callData.httpMethod != null) DWREngine._batch.httpMethod = callData.httpMethod;
  if (callData.async != null) DWREngine._batch.async = callData.async;
  if (callData.timeout != null) DWREngine._batch.timeout = callData.timeout;
  if (callData.preHook != null) DWREngine._batch.preHooks.unshift(callData.preHook);
  if (callData.postHook != null) DWREngine._batch.postHooks.push(callData.postHook);
  DWREngine._batch.batchIds[batchId] = {
    exceptionHandler:callData.exceptionHandler,
    callback:callData.callback
  };
  var data, prop;
  if (callData.headers) {
    for (prop in callData.headers) {
      data = callData[prop];
      if (typeof data != "function") DWREngine._batch.headers[prop] = "" + data;
    }
  }

  // Copy to the map the things that need serializing
  DWREngine._batch.map.httpSessionId = DWREngine._getJSessionId();
  DWREngine._batch.map.scriptSessionId = DWREngine._getScriptSessionId();
  DWREngine._batch.map.page = window.location.pathname;
  var prefix = "c" + DWREngine._batch.map.callCount + "-";
  DWREngine._batch.map[prefix + "scriptName"] = scriptName;
  DWREngine._batch.map[prefix + "methodName"] = methodName;
  DWREngine._batch.map[prefix + "id"] = batchId;
  if (callData.parameters) {
    for (prop in callData.parameters) {
      data = callData[prop];
      if (typeof data != "function") DWREngine._batch.map[prop] = "" + data;
    }
  }
  for (i = 0; i < args.length; i++) {
    DWREngine._serializeAll(DWREngine._batch, [], args[i], prefix + "param" + i);
  }

  // Now we have finished remembering the call, we incr the call count
  DWREngine._batch.map.callCount++;
  if (singleShot) DWREngine.endBatch();
};

/** @private Poll the server to see if there is any data waiting */
DWREngine._poll = function(overridePath) {
  if (!DWREngine._reverseAjax) return;
  // Get a unique ID for this call
  DWREngine._lastBatchId++;
  var batchId = DWREngine._lastBatchId;
  // Create a batch object that describes how we are to call the server
  var batch = {
    map:{
      id:batchId, callCount:1,
      httpSessionId:DWREngine._getJSessionId(),
      scriptSessionId:DWREngine._getScriptSessionId(),
      page:window.location.pathname
    },
    rpcType:DWREngine._pollType, completed:false, isPoll:true, httpMethod:"POST",
    async:true, headers:{}, batchIds:[ batchId ], paramCount:0,
    path:(overridePath) ? overridePath : DWREngine._defaultPath,
    preHooks:[], postHooks:[], timeout:0
  };
  if (window.XMLHttpRequest != null) {
    // Mozilla case where XHR.responseText filled as call proceeds
    batch.map.partialResponse = "true";
  }
  else {
    // The IE case where XHR.responseText is not filled until call is closed
    batch.map.partialResponse = "false";
  }
  // Create an entry in the handlers map for what happens when the reply arrives
  batch.batchIds[batchId] = {
    callback:function(pause) { setTimeout("DWREngine._poll()", pause); }
  };
  // Send the data
  DWREngine._sendData(batch);
  DWREngine._batches[DWREngine._batches.length] = batch;
  if (batch.map.partialResponse == "true") {
    DWREngine._checkCometPoll();
  }
};

/** @private What is our session id? */
DWREngine._getJSessionId =  function() {
  var cookies = document.cookie.split(';');
  for (var i = 0; i < cookies.length; i++) {
    var cookie = cookies[i];
    while (cookie.charAt(0) == ' ') cookie = cookie.substring(1, cookie.length);
    if (cookie.indexOf(DWREngine._sessionCookieName + "=") == 0) {
      return cookie.substring(11, cookie.length);
    }
  }
  return "";
}

/** @private Check for reverse Ajax activity */
DWREngine._checkCometPoll = function() {
  if (DWREngine._pollComet) {
    // If the poll resources are still there, come back again
    if (DWREngine._pollFrame || DWREngine._pollReq) {
      setTimeout("DWREngine._checkCometPoll()", DWREngine._pollCometInterval);
    }
    try {
      if (DWREngine._pollFrame) {
        var text = DWREngine._getTextFromCometIFrame();
        DWREngine._processCometResponse(text);
      }
      else if (DWREngine._pollReq) {
        var xhrtext = DWREngine._pollReq.responseText;
        DWREngine._processCometResponse(xhrtext);
      }
    }
    catch (ex) {
      // IE complains for no good reason for both options above. Ignore.
    }
  }
};

/** @private Extract the whole (executed an all) text from the current iframe */
DWREngine._getTextFromCometIFrame = function() {
  var frameDocument;
  if (DWREngine._pollFrame.contentDocument) {
    frameDocument = DWREngine._pollFrame.contentDocument.defaultView.document;
  }
  else if (DWREngine._pollFrame.contentWindow) {
    frameDocument = DWREngine._pollFrame.contentWindow.document;
  }
  else {
    return "";
  }
  var bodyNodes = frameDocument.getElementsByTagName("body");
  if (bodyNodes == null || bodyNodes.length == 0) return "";
  if (bodyNodes[0] == null) return "";
  var text = bodyNodes[0].innerHTML.toString();
  // IE plays silly-pants and adds <PRE>...</PRE> for some unknown reason
  if (text.indexOf("<PRE>") == 0) text = text.substring(5, text.length - 7);
  return text;
};

/** @private Some more text might have come in, test and execute the new stuff */
DWREngine._processCometResponse = function(response) {
  // See CVS history for extra debug lines to help sort problems with this code
  if (DWREngine._pollCometSize != response.length) {
    var firstStartTag = response.indexOf("//#DWR-START#", DWREngine._pollCometSize);
    if (firstStartTag == -1) {
      // There is no start tag so we ignore the rest
      DWREngine._pollCometSize = response.length;
    }
    else {
      var lastEndTag = response.lastIndexOf("//#DWR-END#");
      if (lastEndTag != -1) {
        var executeString = response.substring(firstStartTag + 13, lastEndTag);
        eval(executeString);
        // Skip the end tag too for next time
        DWREngine._pollCometSize = lastEndTag + 11;
      }
    }
  }
};

/** @private Actually send the block of data in the batch object. */
DWREngine._sendData = function(batch) {
  for (var i = 0; i < batch.preHooks.length; i++) {
    batch.preHooks[i]();
  }
  batch.preHooks = null;
  // Set a timeout
  if (batch.timeout && batch.timeout != 0) {
    batch.interval = setInterval(function() { DWREngine._abortRequest(batch); }, batch.timeout);
  }
  // Get setup for XMLHttpRequest if possible
  if (batch.rpcType == DWREngine.XMLHttpRequest) {
    if (window.XMLHttpRequest) {
      batch.req = new XMLHttpRequest();
    }
    // IE5 for the mac claims to support window.ActiveXObject, but throws an error when it's used
    else if (window.ActiveXObject && !(navigator.userAgent.indexOf("Mac") >= 0 && navigator.userAgent.indexOf("MSIE") >= 0)) {
      batch.req = DWREngine._newActiveXObject(DWREngine._XMLHTTP);
    }
  }

  var prop, request;
  if (batch.req) {
    // Proceed using XMLHttpRequest
    if (batch.async) {
      batch.req.onreadystatechange = function() { DWREngine._stateChange(batch); };
    }
    // If we're polling, record this for monitoring
    if (batch.isPoll) DWREngine._pollReq = batch.req;
    // Workaround for Safari 1.x POST bug
    var indexSafari = navigator.userAgent.indexOf("Safari/");
    if (indexSafari >= 0) {
      var version = navigator.userAgent.substring(indexSafari + 7);
      if (parseInt(version, 10) < 400) batch.httpMethod = "GET";
    }
    batch.mode = batch.isPoll ? DWREngine._ModePlainPoll : DWREngine._ModePlainCall;
    request = DWREngine._constructRequest(batch);
    try {
      batch.req.open(batch.httpMethod, request.url, batch.async);
      try {
        for (prop in batch.headers) {
          var value = batch.headers[prop];
          if (typeof value == "string") batch.req.setRequestHeader(prop, value);
        }
        if (!batch.headers["Content-Type"]) batch.req.setRequestHeader("Content-Type", "text/plain");
      }
      catch (ex) {
        DWREngine._handleWarning(null, ex);
      }
      batch.req.send(request.body);
      if (!batch.async) DWREngine._stateChange(batch);
    }
    catch (ex) {
      DWREngine._handleError(batch, ex);
    }
  }
  else if (batch.rpcType != DWREngine.ScriptTag) {
    var idname = "dwr-if-" + batch.map["c0-id"];
    // Proceed using iframe
    batch.div = document.createElement("div");
    batch.div.innerHTML = "<iframe src='javascript:void(0)' frameborder='0' width='0' height='0' id='" + idname + "' name='" + idname + "'></iframe>";
    document.body.appendChild(batch.div);
    batch.iframe = document.getElementById(idname);
    batch.iframe.setAttribute("style", "width:0px; height:0px; border:0px;");
    batch.mode = batch.isPoll ? DWREngine._ModeHtmlPoll : DWREngine._ModeHtmlCall;
    if (batch.isPoll) {
      // Settings that vary if we are polling
      DWREngine._pollFrame = batch.iframe;
      DWREngine._pollCometSize = 0;
    }
    request = DWREngine._constructRequest(batch);
    if (batch.httpMethod == "GET") {
      batch.iframe.setAttribute("src", request.url);
      document.body.appendChild(batch.iframe);
    }
    else {
      batch.form = document.createElement("form");
      batch.form.setAttribute("id", "dwr-form");
      batch.form.setAttribute("action", request.url);
      batch.form.setAttribute("target", idname);
      batch.form.target = idname;
      batch.form.setAttribute("method", batch.httpMethod);
      for (prop in batch.map) {
        var value = batch.map[prop];
        if (typeof value != "function") {
          var formInput = document.createElement("input");
          formInput.setAttribute("type", "hidden");
          formInput.setAttribute("name", prop);
          formInput.setAttribute("value", value);
          batch.form.appendChild(formInput);
        }
      }
      document.body.appendChild(batch.form);
      batch.form.submit();
    }
  }
  else {
    batch.httpMethod = "GET"; // There's no such thing as ScriptTag using POST
    batch.mode = batch.isPoll ? DWREngine._ModePlainPoll : DWREngine._ModePlainCall;
    request = DWREngine._constructRequest(batch);
    batch.script = document.createElement("script");
    batch.script.id = "dwr-st-" + batch.map["c0-id"];
    batch.script.src = request.url;
    document.body.appendChild(batch.script);
  }
};

DWREngine._ModePlainCall = "/plaincall/";
DWREngine._ModeHtmlCall = "/htmlcall/";
DWREngine._ModePlainPoll = "/plainpoll/";
DWREngine._ModeHtmlPoll = "/htmlpoll/";

/** @private Work out what the URL should look like */
DWREngine._constructRequest = function(batch) {
  // A quick string to help people that use web log analysers
  var request = { url:batch.path + batch.mode, body:null };
  if (batch.isPoll == true) {
    request.url += "ReverseAjax.dwr";
  }
  else if (batch.map.callCount == 1) {
    request.url += batch.map["c0-scriptName"] + "." + batch.map["c0-methodName"] + ".dwr";
  }
  else {
    request.url += "Multiple." + batch.map.callCount + ".dwr";
  }
  // Play nice with url re-writing
  var sessionMatch = location.href.match(/jsessionid=(\w+)/);
  if (sessionMatch != null) {
    request.url += ";jsessionid=" + sessionMatch[1];
  }

  var prop;
  if (batch.httpMethod == "GET") {
    // Some browsers (Opera/Safari2) seem to fail to convert the callCount value
    // to a string in the loop below so we do it manually here.
    batch.map.callCount = "" + batch.map.callCount;
    request.url += "?";
    for (prop in batch.map) {
      if (typeof batch.map[prop] != "function") {
        request.url += encodeURIComponent(prop) + "=" + encodeURIComponent(batch.map[prop]) + "&";
      }
    }
    request.url = request.url.substring(0, request.url.length - 1);
  }
  else {
    // PERFORMANCE: for iframe mode this is thrown away.
    request.body = "";
    for (prop in batch.map) {
      if (typeof batch.map[prop] != "function") {
        request.body += prop + "=" + batch.map[prop] + DWREngine._postSeperator;
      }
    }
    request.body = DWREngine._contentRewriteHandler(request.body);
  }
  request.url = DWREngine._urlRewriteHandler(request.url);
  return request;
};

/** @private Called by XMLHttpRequest to indicate that something has happened */
DWREngine._stateChange = function(batch) {
  var toEval;

  try {
    if (batch.completed || batch.req.readyState != 4) return;
  }
  catch (ex) {
    DWREngine._handleWarning(null, ex);
    // It's broken - clear up and forget this call
    DWREngine._clearUp(batch);
    return;
  }

  try {
    var reply = batch.req.responseText;
    reply = DWREngine._replyRewriteHandler(reply);
    var status = batch.req.status; // causes Mozilla to except on page moves

    if (reply == null || reply == "") {
      DWREngine._handleWarning(null, { name:"dwrengine.missingData", message:"No data received from server" });
    }
    else if (status == 501) {
      DWREngine._handleWarning(null, { name:"dwrengine.http.501", message:reply });
    }
    else {
      var contentType = batch.req.getResponseHeader("Content-Type");
      if (!contentType.match(/^text\/plain/) && !contentType.match(/^text\/javascript/)) {
        if (DWREngine._textHtmlHandler && contentType.match(/^text\/html/)) {
          DWREngine._textHtmlHandler();
        }
        else {
          DWREngine._handleWarning(null, { name:"dwrengine.invalidMimeType", message:"Invalid content type: '" + contentType + "'" });
        }
      }
      else {
        // Skip checking the xhr.status because the above will do for most errors
        // and because it causes Mozilla to error

        // Comet replies might have already partially executed
        if (batch.req == DWREngine._pollReq && batch.map.partialResponse == "true") {
          DWREngine._receivedBatch = batch;
          DWREngine._processCometResponse(reply);
          DWREngine._receivedBatch = null;
        }
        else {
          if (reply.search("//#DWR") == -1) {
            DWREngine._handleWarning(null, { name:"dwrengine.invalidReply", message:"Invalid reply from server" });
          }
          else {
            toEval = reply;
          }
        }
      }
    }
  }
  catch (ex) {
    DWREngine._handleWarning(null, ex);
  }

  // Outside of the try/catch so errors propogate normally:
  DWREngine._receivedBatch = batch;
  if (toEval != null) eval(toEval);
  DWREngine._receivedBatch = null;

  DWREngine._clearUp(batch);
};

/** @private Called by reply scripts generated as a result of remote requests */
DWREngine._remoteHandleCallback = function(batchId, reply) {
  // Error handlers inside here indicate an error that is nothing to do
  // with DWR so we handle them differently.
  try {
    var handlers = DWREngine._receivedBatch.batchIds[batchId];
    if (handlers && typeof handlers.callback == "function") handlers.callback(reply);
  }
  catch (ex) {
    DWREngine._handleError(DWREngine._receivedBatch, ex);
  }
  DWREngine._maybeClearUpIFrame();
};

/** @private This method is called by Javascript that is emitted by server */
DWREngine._remoteHandleException = function(batchId, ex) {
  var handlers = DWREngine._receivedBatch.batchIds[batchId];
  if (ex.message == undefined) ex.message = "";
  if (handlers && typeof handlers.exceptionHandler == "function") handlers.exceptionHandler(error.message, ex);
  else if (DWREngine._exceptionHandler) DWREngine._exceptionHandler(error.message, ex);
  DWREngine._maybeClearUpIFrame();
};

/** @private This method is called by Javascript that is emitted by server */
DWREngine._remoteHandleServerException = function(ex) {
  if (ex.message == undefined) ex.message = "";
  DWREngine._handleError(DWREngine._receivedBatch, ex);
  DWREngine._maybeClearUpIFrame();
};

/** @private Finalize the call for IFrame transport */
DWREngine._maybeClearUpIFrame = function() {
  // TODO: Is this a bug? What happens if the iframes arrive unordered?
  var responseBatch = DWREngine._batches[DWREngine._batches.length - 1];
  if (responseBatch.rpcType == DWREngine.IFrame) {
    // Only finalize after the last call has been handled
    // TODO: This must be a bug id is undefined!
    if (responseBatch.map["c" + (responseBatch.map.callCount - 1) + "-id"] == id) {
      DWREngine._clearUp(responseBatch);
    }
  }
};

/** @private This is a hack to make the context be this window */
DWREngine._eval = function(script) {
  return eval(script);
};

/** @private Called as a result of a request timeout */
DWREngine._abortRequest = function(batch) {
  if (batch && !batch.completed) {
    clearInterval(batch.interval);
    DWREngine._clearUp(batch);
    if (batch.req) batch.req.abort();
    DWREngine._handleError(batch, { name:"dwrengine.timeout", message:"Timeout" });
  }
};

/** @private A call has finished by whatever means and we need to shut it all down. */
DWREngine._clearUp = function(batch) {
  if (batch.completed) {
    DWREngine._handleError(null, { name:"dwrengine.doubleComplete", message:"Double complete" });
    return;
  }

  // IFrame tidyup
  if (batch.div) batch.div.parentNode.removeChild(batch.div);
  if (batch.iframe) {
    // If this is a poll frame then stop comet polling
    if (batch.iframe == DWREngine._pollFrame) DWREngine._pollFrame = null;
    batch.iframe.parentNode.removeChild(batch.iframe);
  }
  if (batch.form) batch.form.parentNode.removeChild(batch.form);

  // XHR tidyup: avoid IE handles increase
  if (batch.req) {
    // If this is a poll frame then stop comet polling
    if (batch.req == DWREngine._pollReq) DWREngine._pollReq = null;
    delete batch.req;
  }

  for (var i = 0; i < batch.postHooks.length; i++) {
    batch.postHooks[i]();
  }
  batch.postHooks = null;

  // TODO: There must be a better way???
  for (var i = 0; i < DWREngine._batches.length; i++) {
    if (DWREngine._batches[i] == batch) {
      DWREngine._batches.splice(i, 1);
      break;
    }
  }

  batch.completed = true;

  // If there is anything on the queue waiting to go out, then send it.
  // We don't need to check for ordered mode, here because when ordered mode
  // gets turned off, we still process *waiting* batches in an ordered way.
  if (DWREngine._batchQueue.length != 0) {
    var sendbatch = DWREngine._batchQueue.shift();
    DWREngine._batches[DWREngine._batches.length] = sendbatch;
    DWREngine._sendData(sendbatch);
  }
};

/** @private Generic error handling routing to save having null checks everywhere */
DWREngine._handleError = function(batch, ex) {
  if (typeof ex == "string") ex = { name:"unknown", message:ex };
  if (ex.message == null) ex.message = "";
  if (ex.name == null) ex.name = "unknown";
  if (batch && typeof batch.errorHandler == "function") batch.errorHandler(ex.message, ex);
  else if (DWREngine._errorHandler) DWREngine._errorHandler(ex.message, ex);
};

/** @private Generic error handling routing to save having null checks everywhere */
DWREngine._handleWarning = function(batch, ex) {
  if (typeof ex == "string") ex = { name:"unknown", message:ex };
  if (ex.message == null) ex.message = "";
  if (ex.name == null) ex.name = "unknown";
  if (batch && typeof batch.warningHandler == "function") handlers.warningHandler(ex.message, ex);
  else if (DWREngine._warningHandler) DWREngine._warningHandler(ex.message, ex);
};

/**
 * @private Marshall a data item
 * @param batch A map of variables to how they have been marshalled
 * @param referto An array of already marshalled variables to prevent recurrsion
 * @param data The data to be marshalled
 * @param name The name of the data being marshalled
 */
DWREngine._serializeAll = function(batch, referto, data, name) {
  if (data == null) {
    batch.map[name] = "null:null";
    return;
  }

  switch (typeof data) {
  case "boolean":
    batch.map[name] = "boolean:" + data;
    break;
  case "number":
    batch.map[name] = "number:" + data;
    break;
  case "string":
    batch.map[name] = "string:" + encodeURIComponent(data);
    break;
  case "object":
    if (data instanceof String) batch.map[name] = "String:" + encodeURIComponent(data);
    else if (data instanceof Boolean) batch.map[name] = "Boolean:" + data;
    else if (data instanceof Number) batch.map[name] = "Number:" + data;
    else if (data instanceof Date) batch.map[name] = "Date:" + data.getTime();
    else if (data instanceof Array) batch.map[name] = DWREngine._serializeArray(batch, referto, data, name);
    else batch.map[name] = DWREngine._serializeObject(batch, referto, data, name);
    break;
  case "function":
    // We just ignore functions.
    break;
  default:
    DWREngine._handleWarning(null, { name:"dwrengine.unexpectedType", message:"Unexpected type: " + typeof data + ", attempting default converter." });
    batch.map[name] = "default:" + data;
    break;
  }
};

/** @private Have we already converted this object? */
DWREngine._lookup = function(referto, data, name) {
  var lookup;
  // Can't use a map: http://getahead.ltd.uk/ajax/javascript-gotchas
  for (var i = 0; i < referto.length; i++) {
    if (referto[i].data == data) {
      lookup = referto[i];
      break;
    }
  }
  if (lookup) return "reference:" + lookup.name;
  referto.push({ data:data, name:name });
  return null;
};

/** @private Marshall an object */
DWREngine._serializeObject = function(batch, referto, data, name) {
  var ref = DWREngine._lookup(referto, data, name);
  if (ref) return ref;

  // This check for an HTML is not complete, but is there a better way?
  // Maybe we should add: data.hasChildNodes typeof "function" == true
  if (data.nodeName && data.nodeType) {
    return DWREngine._serializeXml(batch, referto, data, name);
  }

  // treat objects as an associative arrays
  var reply = "Object_" + DWREngine._getObjectClassName(data) + ":{";
  var element;
  for (element in data) {
    batch.paramCount++;
    var childName = "c" + DWREngine._batch.map.callCount + "-e" + batch.paramCount;
    DWREngine._serializeAll(batch, referto, data[element], childName);

    reply += encodeURIComponent(element) + ":reference:" + childName + ", ";
  }

  if (reply.substring(reply.length - 2) == ", ") {
    reply = reply.substring(0, reply.length - 2);
  }
  reply += "}";

  return reply;
};

/** @private Returns the classname of supplied argument obj */
DWREngine._errorClasses = { "Error":Error, "EvalError":EvalError, "RangeError":RangeError, "ReferenceError":ReferenceError, "SyntaxError":SyntaxError, "TypeError":TypeError, "URIError":URIError };
DWREngine._getObjectClassName = function(obj) {
  // Try to find the classname by stringifying the object's constructor
  // and extract <class> from "function <class>".
  if (obj && obj.constructor && obj.constructor.toString)
  {
    var str = obj.constructor.toString();
    var regexpmatch = str.match(/function\s+(\w+)/);
    if (regexpmatch && regexpmatch.length == 2) {
      return regexpmatch[1];
    }
  }

  // Now manually test against the core Error classes, as these in some 
  // browsers successfully match to the wrong class in the 
  // Object.toString() test we will do later
  if (obj && obj.constructor) {
	for (var errorname in DWREngine._errorClasses) {
      if (obj.constructor == DWREngine._errorClasses[errorname]) return errorname;
    }
  }

  // Try to find the classname by calling Object.toString() on the object
  // and extracting <class> from "[object <class>]"
  if (obj) {
    var str = Object.prototype.toString.call(obj);
    var regexpmatch = str.match(/\[object\s+(\w+)/);
    if (regexpmatch && regexpmatch.length==2) {
      return regexpmatch[1];
    }
  }

  // Supplied argument was probably not an object, but what is better?
  return "Object";
};

/** @private Marshall an object */
DWREngine._serializeXml = function(batch, referto, data, name) {
  var ref = DWREngine._lookup(referto, data, name);
  if (ref) return ref;

  var output;
  if (window.XMLSerializer) output = new XMLSerializer().serializeToString(data);
  else if (data.toXml) output = data.toXml;
  else output = data.innerHTML;

  return "XML:" + encodeURIComponent(output);
};

/** @private Marshall an array */
DWREngine._serializeArray = function(batch, referto, data, name) {
  var ref = DWREngine._lookup(referto, data, name);
  if (ref) return ref;

  var reply = "Array:[";
  for (var i = 0; i < data.length; i++) {
    if (i != 0) reply += ",";
    batch.paramCount++;
    var childName = "c" + DWREngine._batch.map.callCount + "-e" + batch.paramCount;
    DWREngine._serializeAll(batch, referto, data[i], childName);
    reply += "reference:";
    reply += childName;
  }
  reply += "]";

  return reply;
};

/** @private Convert an XML string into a DOM object. */
DWREngine._unserializeDocument = function(xml) {
  var dom;
  if (window.DOMParser) {
    var parser = new DOMParser();
    dom = parser.parseFromString(xml, "text/xml");
    if (!dom.documentElement || dom.documentElement.tagName == "parsererror") {
      var message = dom.documentElement.firstChild.data;
      message += "\n" + dom.documentElement.firstChild.nextSibling.firstChild.data;
      throw message;
    }
    return dom;
  }
  else if (window.ActiveXObject) {
    dom = DWREngine._newActiveXObject(DWREngine._DOMDocument);
    dom.loadXML(xml); // What happens on parse fail with IE?
    return dom;
  }
  else {
    var div = document.createElement("div");
    div.innerHTML = xml;
    return div;
  }
};

/** @param axarray An array of strings to attempt to create ActiveX objects from */
DWREngine._newActiveXObject = function(axarray) {
  var returnValue;  
  for (var i = 0; i < axarray.length; i++) {
    try {
      returnValue = new ActiveXObject(axarray[i]);
      break;
    }
    catch (ex) { /* ignore */ }
  }
  return returnValue;
};

/** See if there is anywhere we can write a debug message */
DWREngine._debug = function(message) {
  if (window.console) { window.console.trace(); window.console.log(message); }
  else if (window.opera && window.opera.postError) window.opera.postError(message);
  //else if (window.navigator.product == "Gecko") window.dump(message + "\n");
  else {
    var debug = document.getElementById("dwr-debug");
    if (debug) {
      var contents = message + "<br/>" + debug.innerHTML;
      if (contents.length > 1024) contents = contents.substring(0, 1024);
      debug.innerHtml = contents;
    }
  }
};
