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
if (dwr == null) var dwr = {};
if (dwr.engine == null) dwr.engine = {};
if (DWREngine == null) var DWREngine = dwr.engine;

/**
 * Set an alternative error handler from the default alert box.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
dwr.engine.setErrorHandler = function(handler) {
  dwr.engine._errorHandler = handler;
};

/**
 * Set an alternative warning handler from the default alert box.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
dwr.engine.setWarningHandler = function(handler) {
  dwr.engine._warningHandler = handler;
};

/**
 * Setter for the text/html handler - what happens if a DWR request gets an HTML
 * reply rather than the expected Javascript. Often due to login timeout
 */
dwr.engine.setTextHtmlHandler = function(handler) {
  dwr.engine._textHtmlHandler = handler;
}

/**
 * Set a default timeout value for all calls. 0 (the default) turns timeouts off.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
dwr.engine.setTimeout = function(timeout) {
  dwr.engine._timeout = timeout;
};

/**
 * The Pre-Hook is called before any DWR remoting is done.
 * @see http://getahead.ltd.uk/dwr/browser/engine/hooks
 */
dwr.engine.setPreHook = function(handler) {
  dwr.engine._preHook = handler;
};

/**
 * The Post-Hook is called after any DWR remoting is done.
 * @see http://getahead.ltd.uk/dwr/browser/engine/hooks
 */
dwr.engine.setPostHook = function(handler) {
  dwr.engine._postHook = handler;
};

/**
 * Custom headers for all DWR calls
 * @see http://getahead.ltd.uk/dwr/????
 */
dwr.engine.setHeaders = function(headers) {
  dwr.engine._headers = headers;
};

/**
 * Custom parameters for all DWR calls
 * @see http://getahead.ltd.uk/dwr/????
 */
dwr.engine.setParameters = function(parameters) {
  dwr.engine._parameters = parameters;
};

/** XHR remoting type constant. See dwr.engine.set[Rpc|Poll]Type() */
dwr.engine.XMLHttpRequest = 1;

/** XHR remoting type constant. See dwr.engine.set[Rpc|Poll]Type() */
dwr.engine.IFrame = 2;

/** XHR remoting type constant. See dwr.engine.setRpcType() */
dwr.engine.ScriptTag = 3;

/**
 * Set the preferred remoting type.
 * @param newType One of dwr.engine.XMLHttpRequest or dwr.engine.IFrame or dwr.engine.ScriptTag
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
dwr.engine.setRpcType = function(newType) {
  if (newType != dwr.engine.XMLHttpRequest && newType != dwr.engine.IFrame && newType != dwr.engine.ScriptTag) {
    dwr.engine._handleError(null, { name:"dwr.engine.invalidRpcType", message:"RpcType must be one of dwr.engine.XMLHttpRequest or dwr.engine.IFrame or dwr.engine.ScriptTag" });
    return;
  }
  dwr.engine._rpcType = newType;
};

/**
 * Which HTTP method do we use to send results? Must be one of "GET" or "POST".
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
dwr.engine.setHttpMethod = function(httpMethod) {
  if (httpMethod != "GET" && httpMethod != "POST") {
    dwr.engine._handleError(null, { name:"dwr.engine.invalidHttpMethod", message:"Remoting method must be one of GET or POST" });
    return;
  }
  dwr.engine._httpMethod = httpMethod;
};

/**
 * Ensure that remote calls happen in the order in which they were sent? (Default: false)
 * @see http://getahead.ltd.uk/dwr/browser/engine/ordering
 */
dwr.engine.setOrdered = function(ordered) {
  dwr.engine._ordered = ordered;
};

/**
 * Do we ask the XHR object to be asynchronous? (Default: true)
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
dwr.engine.setAsync = function(async) {
  dwr.engine._async = async;
};

/**
 * Does DWR poll the server for updates? (Default: false)
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
dwr.engine.setReverseAjax = function(reverseAjax) {
  dwr.engine._reverseAjax = reverseAjax;
  if (dwr.engine._reverseAjax) dwr.engine._poll();
};

/**
 * Does DWR us comet polling? (Default: true)
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
dwr.engine.setPollUsingComet = function(pollComet) {
  dwr.engine._pollComet = pollComet;
};

/**
 * Set the preferred polling type.
 * @param newPollType One of dwr.engine.XMLHttpRequest or dwr.engine.IFrame
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
dwr.engine.setPollType = function(newPollType) {
  if (newPollType != dwr.engine.XMLHttpRequest && newPollType != dwr.engine.IFrame) {
    dwr.engine._handleError(null, { name:"dwr.engine.invalidPollType", message:"PollType must be one of dwr.engine.XMLHttpRequest or dwr.engine.IFrame"  });
    return;
  }
  dwr.engine._pollType = newPollType;
};

/**
 * The default message handler.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
dwr.engine.defaultErrorHandler = function(message, ex) {
  dwr.engine._debug("Error: " + ex.name + ", " + ex.message);

  if (message == null || message == "") alert("A server error has occured. More information may be available in the console.");
  // Ignore NS_ERROR_NOT_AVAILABLE if Mozilla is being narky
  else if (message.indexOf("0x80040111") != -1) dwr.engine._debug(message);
  else alert(message);
};

/**
 * The default warning handler.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
dwr.engine.defaultWarningHandler = function(message, ex) {
  dwr.engine._debug(message);
};

/**
 * For reduced latency you can group several remote calls together using a batch.
 * @see http://getahead.ltd.uk/dwr/browser/engine/batch
 */
dwr.engine.beginBatch = function() {
  if (dwr.engine._batch) {
    dwr.engine._handleError(null, { name:"dwr.engine.batchBegun", message:"Batch already begun" });
    return;
  }
  dwr.engine._batch = dwr.engine._createBatch();
};

/**
 * Finished grouping a set of remote calls together. Go and execute them all.
 * @see http://getahead.ltd.uk/dwr/browser/engine/batch
 */
dwr.engine.endBatch = function(options) {
  var batch = dwr.engine._batch;
  if (batch == null) {
    dwr.engine._handleError(null, { name:"dwr.engine.batchNotBegun", message:"No batch in progress" });
    return;
  }
  dwr.engine._batch = null;
  if (batch.map.callCount == 0) return;

  // The hooks need to be merged carefully to preserve ordering
  if (options) dwr.engine._mergeBatch(batch, options);

  // In ordered mode, we don't send unless the list of sent items is empty
  if (dwr.engine._ordered && dwr.engine._batchesLength != 0) {
    dwr.engine._batchQueue[dwr.engine._batchQueue.length] = batch;
  }
  else {
    dwr.engine._sendData(batch);
  }
};

/** @deprecated */
dwr.engine.setPollMethod = function(type) { dwr.engine.setPollType(type); };
dwr.engine.setMethod = function(type) { dwr.engine.setRpcType(type); };
dwr.engine.setVerb = function(verb) { dwr.engine.setHttpMethod(verb); };

//==============================================================================
// Only private stuff below here
//==============================================================================

/** The original page id sent from the server */
dwr.engine._origScriptSessionId = "${scriptSessionId}";

/** The session cookie name */
dwr.engine._sessionCookieName = "${sessionCookieName}"; // JSESSIONID

/** The read page id that we calculate */
dwr.engine._scriptSessionId = null;

/** The function that we use to fetch/calculate a session id */
dwr.engine._getScriptSessionId = function() {
  if (dwr.engine._scriptSessionId == null) {
    dwr.engine._scriptSessionId = dwr.engine._origScriptSessionId + Math.floor(Math.random() * 1000);
  }
  return dwr.engine._scriptSessionId;
};

/** A function to call if something fails. */
dwr.engine._errorHandler = dwr.engine.defaultErrorHandler;

/** By default exceptions are handled by the errorHandler */
dwr.engine._exceptionHandler = dwr.engine.defaultErrorHandler;

/** For debugging when something unexplained happens. */
dwr.engine._warningHandler = dwr.engine.defaultWarningHandler;

/** A function to be called before requests are marshalled. Can be null. */
dwr.engine._preHook = null;

/** A function to be called after replies are received. Can be null. */
dwr.engine._postHook = null;

/** An map of the batches that we have sent and are awaiting a reply on. */
dwr.engine._batches = {};

/** A count of the number of outstanding batches. Should be == to _batches.length unless prototype has messed things up */
dwr.engine._batchesLength = 0;

/** In ordered mode, the array of batches waiting to be sent */
dwr.engine._batchQueue = [];

/** What is the default rpc type */
dwr.engine._rpcType = dwr.engine.XMLHttpRequest;

/** What is the default remoting method (ie GET or POST) */
dwr.engine._httpMethod = "POST";

/** Do we attempt to ensure that calls happen in the order in which they were sent? */
dwr.engine._ordered = false;

/** Do we make the calls async? */
dwr.engine._async = true;

/** The current batch (if we are in batch mode) */
dwr.engine._batch = null;

/** The global timeout */
dwr.engine._timeout = 0;

/** ActiveX objects to use when we want to convert an xml string into a DOM object. */
dwr.engine._DOMDocument = ["Msxml2.DOMDocument.6.0", "Msxml2.DOMDocument.5.0", "Msxml2.DOMDocument.4.0", "Msxml2.DOMDocument.3.0", "MSXML2.DOMDocument", "MSXML.DOMDocument", "Microsoft.XMLDOM"];

/** The ActiveX objects to use when we want to do an XMLHttpRequest call. */
dwr.engine._XMLHTTP = ["Msxml2.XMLHTTP.6.0", "Msxml2.XMLHTTP.5.0", "Msxml2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP", "Microsoft.XMLHTTP"];

/** Are we doing reverse ajax? */
dwr.engine._reverseAjax = false;

/** Is there a long term poll (comet) interraction in place? */
dwr.engine._pollComet = true;

/** What is the default polling type */
dwr.engine._pollType = dwr.engine.XMLHttpRequest;
//dwr.engine._pollType = dwr.engine.IFrame;

/** The iframe that we are using to poll */
dwr.engine._pollFrame = null;

/** The xhr object that we are using to poll */
dwr.engine._pollReq = null;

/** How much data has been received into a reverse ajax document */
dwr.engine._pollCometSize = 0;

/** How many milliseconds between internal comet polls */
dwr.engine._pollCometInterval = 200;

/** Do we do a document.reload if we get a text/html reply? */
dwr.engine._textHtmlHandler = null;

/** If you wish to send custom headers with every request */
dwr.engine._headers = null;

/** If you wish to send extra custom request parameters with each request */
dwr.engine._parameters = null;

/** Undocumented interceptors - do not use */
dwr.engine._postSeperator = "\n";
dwr.engine._defaultInterceptor = function(data) {return data;}
dwr.engine._urlRewriteHandler = dwr.engine._defaultInterceptor;
dwr.engine._contentRewriteHandler = dwr.engine._defaultInterceptor;
dwr.engine._replyRewriteHandler = dwr.engine._defaultInterceptor;

/** Batch ids allow us to know which batch the server is answering */
dwr.engine._nextBatchId = 0;

/** A list of the properties that need merging from calls to a batch */
dwr.engine._propnames = [ "rpcType", "httpMethod", "async", "timeout", "errorHandler", "warningHandler", "textHtmlHandler" ];

/**
 * @private Send a request. Called by the Javascript interface stub
 * @param path part of URL after the host and before the exec bit without leading or trailing /s
 * @param scriptName The class to execute
 * @param methodName The method on said class to execute
 * @param func The callback function to which any returned data should be passed
 *       if this is null, any returned data will be ignored
 * @param vararg_params The parameters to pass to the above class
 */
dwr.engine._execute = function(path, scriptName, methodName, vararg_params) {
  var singleShot = false;
  if (dwr.engine._batch == null) {
    dwr.engine.beginBatch();
    singleShot = true;
  }
  var batch = dwr.engine._batch;
  // To make them easy to manipulate we copy the arguments into an args array
  var args = [];
  for (var i = 0; i < arguments.length - 3; i++) {
    args[i] = arguments[i + 3];
  }
  // All the paths MUST be to the same servlet
  if (batch.path == null) {
    batch.path = path;
  }
  else {
    if (batch.path != path) {
      dwr.engine._handleError(batch, { name:"dwr.engine.multipleServlets", message:"Can't batch requests to multiple DWR Servlets." });
      return;
    }
  }
  // From the other params, work out which is the function (or object with
  // call meta-data) and which is the call parameters
  var callData;
  var lastArg = args[args.length - 1];
  if (typeof lastArg == "function" || lastArg == null) callData = { callback:args.pop() };
  else callData = args.pop();

  // Merge from the callData into the batch
  dwr.engine._mergeBatch(batch, callData);
  batch.handlers[batch.map.callCount] = {
    exceptionHandler:callData.exceptionHandler,
    callback:callData.callback
  };

  // Copy to the map the things that need serializing
  var prefix = "c" + batch.map.callCount + "-";
  batch.map[prefix + "scriptName"] = scriptName;
  batch.map[prefix + "methodName"] = methodName;
  batch.map[prefix + "id"] = batch.map.callCount;
  for (i = 0; i < args.length; i++) {
    dwr.engine._serializeAll(batch, [], args[i], prefix + "param" + i);
  }

  // Now we have finished remembering the call, we incr the call count
  batch.map.callCount++;
  if (singleShot) dwr.engine.endBatch();
};

/** @private Poll the server to see if there is any data waiting */
dwr.engine._poll = function(overridePath) {
  if (!dwr.engine._reverseAjax) return;

  var batch = dwr.engine._createBatch();
  batch.map.id = 0; // TODO: Do we need this??
  batch.map.callCount = 1;
  batch.map.partialResponse = (document.all) ? "false" : "true"; // (window.XMLHttpRequest) ? "true" : "false";
  batch.isPoll = true;
  batch.rpcType = dwr.engine._pollType;
  batch.httpMethod = "POST";
  batch.async = true;
  batch.timeout = 0;
  batch.path = (overridePath) ? overridePath : dwr.engine._defaultPath;
  batch.preHooks = [];
  batch.postHooks = [];
  batch.handlers[0] = {
    callback:function(pause) {
      dwr.engine._cometBatch = null;
      setTimeout("dwr.engine._poll()", pause);
    }
  };

  // Send the data
  dwr.engine._sendData(batch);
  if (batch.map.partialResponse == "true") {
    dwr.engine._cometBatch = batch;
    dwr.engine._checkCometPoll();
  }
};

/** @private Generate a new standard batch */
dwr.engine._createBatch = function() {
  var batch = {
    map:{
      callCount:0,
      page:window.location.pathname,
      httpSessionId:dwr.engine._getJSessionId(),
      scriptSessionId:dwr.engine._getScriptSessionId()
    },
    paramCount:0, // TODO: What's this for?
    isPoll:false, headers:{}, handlers:{}, preHooks:[], postHooks:[],
    rpcType:dwr.engine._rpcType,
    httpMethod:dwr.engine._httpMethod,
    async:dwr.engine._async,
    timeout:dwr.engine._timeout,
    errorHandler:dwr.engine._errorHandler,
    warningHandler:dwr.engine._warningHandler,
    textHtmlHandler:dwr.engine._textHtmlHandler
  };
  if (dwr.engine._preHook) batch.preHooks.push(dwr.engine._preHook);
  if (dwr.engine._postHook) batch.postHooks.push(dwr.engine._postHook);
  var propname, data;
  if (dwr.engine._headers) {
    for (propname in dwr.engine._headers) {
      data = dwr.engine._headers[propname];
      if (typeof data != "function") batch.headers[propname] = "" + data;
    }
  }
  if (dwr.engine._parameters) {
    for (propname in dwr.engine._parameters) {
      data = dwr.engine._parameters[propname];
      if (typeof data != "function") batch.parameters[propname] = "" + data;
    }
  }
  return batch;
}

/** @private Take further options and merge them into */
dwr.engine._mergeBatch = function(batch, overrides) {
  var propname, data;
  for (var i = 0; i < dwr.engine._propnames.length; i++) {
    propname = dwr.engine._propnames[i];
    if (overrides[propname] != null) batch[propname] = overrides[propname];
  }
  if (overrides.preHook != null) batch.preHooks.unshift(overrides.preHook);
  if (overrides.postHook != null) batch.postHooks.push(overrides.postHook);
  if (overrides.headers) {
    for (propname in overrides.headers) {
      data = overrides[propname];
      if (typeof data != "function") batch.headers[propname] = "" + data;
    }
  }
  if (overrides.parameters) {
    for (propname in overrides.parameters) {
      data = overrides[propname];
      if (typeof data != "function") batch.map[propname] = "" + data;
    }
  }
};

/** @private What is our session id? */
dwr.engine._getJSessionId =  function() {
  var cookies = document.cookie.split(';');
  for (var i = 0; i < cookies.length; i++) {
    var cookie = cookies[i];
    while (cookie.charAt(0) == ' ') cookie = cookie.substring(1, cookie.length);
    if (cookie.indexOf(dwr.engine._sessionCookieName + "=") == 0) {
      return cookie.substring(11, cookie.length);
    }
  }
  return "";
}

/** @private Check for reverse Ajax activity */
dwr.engine._checkCometPoll = function() {
  if (dwr.engine._pollComet) {
    // If the poll resources are still there, come back again
    if (dwr.engine._pollFrame || dwr.engine._pollReq) {
      setTimeout("dwr.engine._checkCometPoll()", dwr.engine._pollCometInterval);
    }
    try {
      dwr.engine._receivedBatch = dwr.engine._cometBatch;
      if (dwr.engine._pollFrame) {
        var text = dwr.engine._getTextFromCometIFrame();
        dwr.engine._processCometResponse(text);
      }
      else if (dwr.engine._pollReq) {
        var xhrtext = dwr.engine._pollReq.responseText;
        dwr.engine._processCometResponse(xhrtext);
      }
      dwr.engine._receivedBatch = null;
    }
    catch (ex) {
      // IE complains for no good reason for both options above. Ignore.
    }
  }
};

/** @private Extract the whole (executed an all) text from the current iframe */
dwr.engine._getTextFromCometIFrame = function() {
  var frameDocument;
  if (dwr.engine._pollFrame.contentDocument) {
    frameDocument = dwr.engine._pollFrame.contentDocument.defaultView.document;
  }
  else if (dwr.engine._pollFrame.contentWindow) {
    frameDocument = dwr.engine._pollFrame.contentWindow.document;
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
dwr.engine._processCometResponse = function(response) {
  // See CVS history for extra debug lines to help sort problems with this code
  if (dwr.engine._pollCometSize != response.length) {
    var firstStartTag = response.indexOf("//#DWR-START#", dwr.engine._pollCometSize);
    if (firstStartTag == -1) {
      //dwr.engine._debug("No start tag. '" + response + "'. Searching next time from: " + response.length);
      dwr.engine._pollCometSize = response.length;
    }
    else {
      var lastEndTag = response.lastIndexOf("//#DWR-END#");
      if (lastEndTag != -1) {
        dwr.engine._remoteEval(response.substring(firstStartTag + 13, lastEndTag));
        // Skip the end tag too for next time
        dwr.engine._pollCometSize = lastEndTag + 11;
      }
      else {
        //dwr.engine._debug("No end tag. (yet) '" + response + "'");
      }
    }
  }
};

/** @private Actually send the block of data in the batch object. */
dwr.engine._sendData = function(batch) {
  batch.map.batchId = dwr.engine._nextBatchId++;
  dwr.engine._batches[batch.map.batchId] = batch;
  dwr.engine._batchesLength++;
  batch.completed = false;
  dwr.engine._debug("Sending batchId=" + batch.map.batchId + " handlers=" + dwr.util.toDescriptiveString(batch.handlers));

  for (var i = 0; i < batch.preHooks.length; i++) {
    batch.preHooks[i]();
  }
  batch.preHooks = null;
  // Set a timeout
  if (batch.timeout && batch.timeout != 0) {
    batch.interval = setInterval(function() { dwr.engine._abortRequest(batch); }, batch.timeout);
  }
  // Get setup for XMLHttpRequest if possible
  if (batch.rpcType == dwr.engine.XMLHttpRequest) {
    if (window.XMLHttpRequest) {
      batch.req = new XMLHttpRequest();
    }
    // IE5 for the mac claims to support window.ActiveXObject, but throws an error when it's used
    else if (window.ActiveXObject && !(navigator.userAgent.indexOf("Mac") >= 0 && navigator.userAgent.indexOf("MSIE") >= 0)) {
      batch.req = dwr.engine._newActiveXObject(dwr.engine._XMLHTTP);
    }
  }

  var prop, request;
  if (batch.req) {
    // Proceed using XMLHttpRequest
    if (batch.async) {
      batch.req.onreadystatechange = function() { dwr.engine._stateChange(batch); };
    }
    // If we're polling, record this for monitoring
    if (batch.isPoll) dwr.engine._pollReq = batch.req;
    // Workaround for Safari 1.x POST bug
    var indexSafari = navigator.userAgent.indexOf("Safari/");
    if (indexSafari >= 0) {
      var version = navigator.userAgent.substring(indexSafari + 7);
      if (parseInt(version, 10) < 400) batch.httpMethod = "GET";
    }
    batch.mode = batch.isPoll ? dwr.engine._ModePlainPoll : dwr.engine._ModePlainCall;
    request = dwr.engine._constructRequest(batch);
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
        dwr.engine._handleWarning(batch, ex);
      }
      batch.req.send(request.body);
      if (!batch.async) dwr.engine._stateChange(batch);
    }
    catch (ex) {
      dwr.engine._handleError(batch, ex);
    }
  }
  else if (batch.rpcType != dwr.engine.ScriptTag) {
    var idname = "dwr-if-" + batch.map["c0-id"];
    // Proceed using iframe
    batch.div = document.createElement("div");
    batch.div.innerHTML = "<iframe src='javascript:void(0)' frameborder='0' width='0' height='0' id='" + idname + "' name='" + idname + "'></iframe>";
    document.body.appendChild(batch.div);
    batch.iframe = document.getElementById(idname);
    batch.iframe.setAttribute("style", "width:0px; height:0px; border:0px;");
    batch.iframe.batch = batch;
    batch.mode = batch.isPoll ? dwr.engine._ModeHtmlPoll : dwr.engine._ModeHtmlCall;
    if (batch.isPoll) {
      // Settings that vary if we are polling
      dwr.engine._pollFrame = batch.iframe;
      dwr.engine._pollCometSize = 0;
    }
    request = dwr.engine._constructRequest(batch);
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
    batch.mode = batch.isPoll ? dwr.engine._ModePlainPoll : dwr.engine._ModePlainCall;
    request = dwr.engine._constructRequest(batch);
    batch.script = document.createElement("script");
    batch.script.id = "dwr-st-" + batch.map["c0-id"];
    batch.script.src = request.url;
    document.body.appendChild(batch.script);
  }
};

dwr.engine._ModePlainCall = "/call/plaincall/";
dwr.engine._ModeHtmlCall = "/call/htmlcall/";
dwr.engine._ModePlainPoll = "/call/plainpoll/";
dwr.engine._ModeHtmlPoll = "/call/htmlpoll/";

/** @private Work out what the URL should look like */
dwr.engine._constructRequest = function(batch) {
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
        request.body += prop + "=" + batch.map[prop] + dwr.engine._postSeperator;
      }
    }
    request.body = dwr.engine._contentRewriteHandler(request.body);
  }
  request.url = dwr.engine._urlRewriteHandler(request.url);
  return request;
};

/** @private Called by XMLHttpRequest to indicate that something has happened */
dwr.engine._stateChange = function(batch) {
  var toEval;

  if (batch.completed) {
    dwr.engine._debug("Error: _stateChange() with batch.completed");
    return;
  }

  try {
    if (batch.req.readyState != 4) return;
  }
  catch (ex) {
    dwr.engine._handleWarning(batch, ex);
    // It's broken - clear up and forget this call
    dwr.engine._clearUp(batch);
    return;
  }

  try {
    var reply = batch.req.responseText;
    reply = dwr.engine._replyRewriteHandler(reply);
    var status = batch.req.status; // causes Mozilla to except on page moves

    if (reply == null || reply == "") {
      dwr.engine._handleWarning(batch, { name:"dwr.engine.missingData", message:"No data received from server" });
    }
    else if (status != 200) {
      dwr.engine._handleWarning(batch, { name:"dwr.engine.http." + status, message:reply });
    }
    else {
      var contentType = batch.req.getResponseHeader("Content-Type");
      if (!contentType.match(/^text\/plain/) && !contentType.match(/^text\/javascript/)) {
        if (dwr.engine._textHtmlHandler && contentType.match(/^text\/html/)) {
          dwr.engine._textHtmlHandler();
        }
        else {
          dwr.engine._handleWarning(batch, { name:"dwr.engine.invalidMimeType", message:"Invalid content type: '" + contentType + "'" });
        }
      }
      else {
        // Comet replies might have already partially executed
        if (batch.req == dwr.engine._pollReq && batch.map.partialResponse == "true") {
          dwr.engine._receivedBatch = batch;
          dwr.engine._processCometResponse(reply);
          dwr.engine._receivedBatch = null;
        }
        else {
          if (reply.search("//#DWR") == -1) {
            dwr.engine._handleWarning(batch, { name:"dwr.engine.invalidReply", message:"Invalid reply from server" });
          }
          else {
            toEval = reply;
          }
        }
      }
    }
  }
  catch (ex) {
    dwr.engine._handleWarning(batch, ex);
  }

  // Outside of the try/catch so errors propogate normally:
  dwr.engine._receivedBatch = batch;
  if (toEval != null) dwr.engine._remoteEval(toEval);
  else dwr.engine._debug("Nothing to eval");
  dwr.engine._receivedBatch = null;

  dwr.engine._clearUp(batch);
};

/** @private Called by reply scripts generated as a result of remote requests */
dwr.engine._remoteHandleCallback = function(batchId, callId, reply) {
  var batch = dwr.engine._batches[batchId];
  if (batch == null) {
    dwr.engine._debug("batch == null in dwr.engine._remoteHandleCallback for batchId=" + batchId);
    return;
  }
  // Error handlers inside here indicate an error that is nothing to do
  // with DWR so we handle them differently.
  try {
    var handlers = batch.handlers[callId];
    if (!handlers) {
      dwr.engine._debug("Missing handlers. callId=" + callId + ", handlers=" + dwr.util.toDescriptiveString(batch.handlers));
    }
    else if (typeof handlers.callback == "function") handlers.callback(reply);
  }
  catch (ex) {
    dwr.engine._handleError(batch, ex);
  }
};

/** @private This method is called by Javascript that is emitted by server */
dwr.engine._remoteHandleException = function(batchId, callId, ex) {
  var batch = dwr.engine._batches[batchId];
  if (batch == null) {
    dwr.engine._debug("batch == null in dwr.engine._remoteHandleException for batchId=" + batchId);
    return;
  }
  var handlers = batch.handlers[callId];
  if (ex.message == undefined) ex.message = "";
  if (handlers && typeof handlers.exceptionHandler == "function") handlers.exceptionHandler(ex.message, ex);
  else if (dwr.engine._exceptionHandler) dwr.engine._exceptionHandler(ex.message, ex);
};

/** @private This method is called by Javascript that is emitted by server */
dwr.engine._remoteHandleBatchException = function(ex) {
  if (ex.message == undefined) ex.message = "";
  dwr.engine._handleError(dwr.engine._receivedBatch, ex);
};

/** @private An IFrame reply is about to start */
dwr.engine._remoteBeginIFrameResponse = function(element, batchId) {
  dwr.engine._receivedBatch = element.batch;
  element.batch = null;
};

/** @private An IFrame reply is just completing */
dwr.engine._remoteEndIFrameResponse = function(batchId) {
  dwr.engine._clearUp(dwr.engine._receivedBatch);
  dwr.engine._receivedBatch = null;
};

/** @private This is a hack to make the context be this window */
dwr.engine._remoteEval = function(script) {
  if (script == null) return null;
  dwr.engine._debug(script);
  return eval(script);
};

/** @private Called as a result of a request timeout */
dwr.engine._abortRequest = function(batch) {
  if (batch && !batch.completed) {
    clearInterval(batch.interval);
    dwr.engine._clearUp(batch);
    if (batch.req) batch.req.abort();
    dwr.engine._handleError(batch, { name:"dwr.engine.timeout", message:"Timeout" });
  }
};

/** @private A call has finished by whatever means and we need to shut it all down. */
dwr.engine._clearUp = function(batch) {
  if (!batch) { dwr.engine._debug("null batch in dwr.engine._clearUp()", true); return; }
  if (batch.completed == "true") { dwr.engine._debug("Double complete", true); return; }

  // IFrame tidyup
  if (batch.div) batch.div.parentNode.removeChild(batch.div);
  if (batch.iframe) {
    // If this is a poll frame then stop comet polling
    if (batch.iframe == dwr.engine._pollFrame) dwr.engine._pollFrame = null;
    batch.iframe.parentNode.removeChild(batch.iframe);
  }
  if (batch.form) batch.form.parentNode.removeChild(batch.form);

  // XHR tidyup: avoid IE handles increase
  if (batch.req) {
    // If this is a poll frame then stop comet polling
    if (batch.req == dwr.engine._pollReq) dwr.engine._pollReq = null;
    delete batch.req;
  }

  for (var i = 0; i < batch.postHooks.length; i++) {
    batch.postHooks[i]();
  }
  batch.postHooks = null;

  delete dwr.engine._batches[batch.map.batchId];
  dwr.engine._batchesLength--;
  batch.completed = true;

  // If there is anything on the queue waiting to go out, then send it.
  // We don't need to check for ordered mode, here because when ordered mode
  // gets turned off, we still process *waiting* batches in an ordered way.
  if (dwr.engine._batchQueue.length != 0) {
    var sendbatch = dwr.engine._batchQueue.shift();
    dwr.engine._sendData(sendbatch);
  }
};

/** @private Generic error handling routing to save having null checks everywhere */
dwr.engine._handleError = function(batch, ex) {
  if (typeof ex == "string") ex = { name:"unknown", message:ex };
  if (ex.message == null) ex.message = "";
  if (ex.name == null) ex.name = "unknown";
  if (batch && typeof batch.errorHandler == "function") batch.errorHandler(ex.message, ex);
  else if (dwr.engine._errorHandler) dwr.engine._errorHandler(ex.message, ex);
  dwr.engine._clearUp(batch);
};

/** @private Generic error handling routing to save having null checks everywhere */
dwr.engine._handleWarning = function(batch, ex) {
  if (typeof ex == "string") ex = { name:"unknown", message:ex };
  if (ex.message == null) ex.message = "";
  if (ex.name == null) ex.name = "unknown";
  if (batch && typeof batch.warningHandler == "function") batch.warningHandler(ex.message, ex);
  else if (dwr.engine._warningHandler) dwr.engine._warningHandler(ex.message, ex);
  dwr.engine._clearUp(batch);
};

/**
 * @private Marshall a data item
 * @param batch A map of variables to how they have been marshalled
 * @param referto An array of already marshalled variables to prevent recurrsion
 * @param data The data to be marshalled
 * @param name The name of the data being marshalled
 */
dwr.engine._serializeAll = function(batch, referto, data, name) {
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
    else if (data instanceof Array) batch.map[name] = dwr.engine._serializeArray(batch, referto, data, name);
    else batch.map[name] = dwr.engine._serializeObject(batch, referto, data, name);
    break;
  case "function":
    // We just ignore functions.
    break;
  default:
    dwr.engine._handleWarning(null, { name:"dwr.engine.unexpectedType", message:"Unexpected type: " + typeof data + ", attempting default converter." });
    batch.map[name] = "default:" + data;
    break;
  }
};

/** @private Have we already converted this object? */
dwr.engine._lookup = function(referto, data, name) {
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
dwr.engine._serializeObject = function(batch, referto, data, name) {
  var ref = dwr.engine._lookup(referto, data, name);
  if (ref) return ref;

  // This check for an HTML is not complete, but is there a better way?
  // Maybe we should add: data.hasChildNodes typeof "function" == true
  if (data.nodeName && data.nodeType) {
    return dwr.engine._serializeXml(batch, referto, data, name);
  }

  // treat objects as an associative arrays
  var reply = "Object_" + dwr.engine._getObjectClassName(data) + ":{";
  var element;
  for (element in data) {
    batch.paramCount++;
    var childName = "c" + dwr.engine._batch.map.callCount + "-e" + batch.paramCount;
    dwr.engine._serializeAll(batch, referto, data[element], childName);

    reply += encodeURIComponent(element) + ":reference:" + childName + ", ";
  }

  if (reply.substring(reply.length - 2) == ", ") {
    reply = reply.substring(0, reply.length - 2);
  }
  reply += "}";

  return reply;
};

/** @private Returns the classname of supplied argument obj */
dwr.engine._errorClasses = { "Error":Error, "EvalError":EvalError, "RangeError":RangeError, "ReferenceError":ReferenceError, "SyntaxError":SyntaxError, "TypeError":TypeError, "URIError":URIError };
dwr.engine._getObjectClassName = function(obj) {
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
	for (var errorname in dwr.engine._errorClasses) {
      if (obj.constructor == dwr.engine._errorClasses[errorname]) return errorname;
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
dwr.engine._serializeXml = function(batch, referto, data, name) {
  var ref = dwr.engine._lookup(referto, data, name);
  if (ref) return ref;

  var output;
  if (window.XMLSerializer) output = new XMLSerializer().serializeToString(data);
  else if (data.toXml) output = data.toXml;
  else output = data.innerHTML;

  return "XML:" + encodeURIComponent(output);
};

/** @private Marshall an array */
dwr.engine._serializeArray = function(batch, referto, data, name) {
  var ref = dwr.engine._lookup(referto, data, name);
  if (ref) return ref;

  var reply = "Array:[";
  for (var i = 0; i < data.length; i++) {
    if (i != 0) reply += ",";
    batch.paramCount++;
    var childName = "c" + dwr.engine._batch.map.callCount + "-e" + batch.paramCount;
    dwr.engine._serializeAll(batch, referto, data[i], childName);
    reply += "reference:";
    reply += childName;
  }
  reply += "]";

  return reply;
};

/** @private Convert an XML string into a DOM object. */
dwr.engine._unserializeDocument = function(xml) {
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
    dom = dwr.engine._newActiveXObject(dwr.engine._DOMDocument);
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
dwr.engine._newActiveXObject = function(axarray) {
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


/** Used internally when some message needs to get to the programmer */
dwr.engine._debug = function(message, stacktrace) {
  var debug = document.getElementById("dwr-debug");
  if (debug) {
    var contents = message + "<br/>" + debug.innerHTML;
    if (contents.length > 1024) contents = contents.substring(0, 1024);
    debug.innerHTML = contents;
  }
  else {
    if (window.console) {
      if (stacktrace && window.console.trace) window.console.trace();
      window.console.log(message);
    }
    else if (window.opera && window.opera.postError) window.opera.postError(message);
    //else if (window.navigator.product == "Gecko") window.dump(message + "\n");
  }
};
