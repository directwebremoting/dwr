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
if (DWREngine == null) {
  var DWREngine = {};
}

/**
 * The real session id should be filled in by the server
 */
DWREngine._httpSessionId = "${httpSessionId}";

/**
 * The real page id should be filled in by the server
 */
DWREngine._scriptSessionId = "${scriptSessionId}";

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

/** XHR remoting method constant. See DWREngine.setMethod() */
DWREngine.XMLHttpRequest = 1;

/** XHR remoting method constant. See DWREngine.setMethod() */
DWREngine.IFrame = 2;

/** XHR remoting method constant. See DWREngine.setMethod() */
DWREngine.ScriptTag = 3;

/**
 * Set the preferred remoting method.
 * @param newmethod One of DWREngine.XMLHttpRequest or DWREngine.IFrame
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
DWREngine.setMethod = function(newmethod) {
  if (newmethod != DWREngine.XMLHttpRequest && newmethod != DWREngine.IFrame && newmethod != DWREngine.ScriptTag) {
    DWREngine._handleError("Remoting method must be one of DWREngine.XMLHttpRequest or DWREngine.IFrame or DWREngine.ScriptTag");
    return;
  }
  DWREngine._method = newmethod;
};

/**
 * Which HTTP verb do we use to send results? Must be one of "GET" or "POST".
 * @see http://getahead.ltd.uk/dwr/browser/engine/options
 */
DWREngine.setVerb = function(verb) {
  if (verb != "GET" && verb != "POST") {
    DWREngine._handleError("Remoting verb must be one of GET or POST");
    return;
  }
  DWREngine._verb = verb;
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
DWREngine.setPolling = function(polling) {
  DWREngine._pollServer = polling;
  if (DWREngine._pollServer) {
    DWREngine._triggerNextPoll(0);
  }
};

/**
 * Setter for the text/html handler - what happens if a DWR request gets an HTML
 * reply rather than the expected Javascript. Often due to login timeout
 */
DWREngine.setTextHtmlHandler = function(handler) {
  DWREngine._textHtmlHandler = handler;
}

/**
 * The default message handler.
 * @see http://getahead.ltd.uk/dwr/browser/engine/errors
 */
DWREngine.defaultMessageHandler = function(message) {
  if (typeof message == "object" && message.name == "Error" && message.description) {
    alert("Error: " + message.description);
  }
  else {
    // Ignore NS_ERROR_NOT_AVAILABLE
    if (message.toString().indexOf("0x80040111") == -1) {
      alert(message);
    }
  }
};

/**
 * For reduced latency you can group several remote calls together using a batch.
 * @see http://getahead.ltd.uk/dwr/browser/engine/batch
 */
DWREngine.beginBatch = function() {
  if (DWREngine._batch) {
    DWREngine._handleError("Batch already started.");
    return;
  }
  // Setup a batch
  DWREngine._batch = {
    map:{ callCount:0 },
    paramCount:0,
    ids:[],
    preHooks:[],
    postHooks:[]
  };
};

/**
 * Finished grouping a set of remote calls together. Go and execute them all.
 * @see http://getahead.ltd.uk/dwr/browser/engine/batch
 */
DWREngine.endBatch = function(options) {
  var batch = DWREngine._batch;
  if (batch == null) {
    DWREngine._handleError("No batch in progress.");
    return;
  }
  // Merge the global batch level properties into the batch meta data
  if (options && options.preHook) batch.preHooks.unshift(options.preHook);
  if (options && options.postHook) batch.postHooks.push(options.postHook);
  if (DWREngine._preHook) batch.preHooks.unshift(DWREngine._preHook);
  if (DWREngine._postHook) batch.postHooks.push(DWREngine._postHook);

  if (batch.method == null) batch.method = DWREngine._method;
  if (batch.verb == null) batch.verb = DWREngine._verb;
  if (batch.async == null) batch.async = DWREngine._async;
  if (batch.timeout == null) batch.timeout = DWREngine._timeout;

  batch.completed = false;

  // We are about to send so this batch should not be globally visible
  DWREngine._batch = null;

  // If we are in ordered mode, then we don't send unless the list of sent
  // items is empty
  if (!DWREngine._ordered) {
    DWREngine._sendData(batch);
    DWREngine._batches[DWREngine._batches.length] = batch;
  }
  else {
    if (DWREngine._batches.length == 0) {
      // We aren't waiting for anything, go now.
      DWREngine._sendData(batch);
      DWREngine._batches[DWREngine._batches.length] = batch;
    }
    else {
      // Push the batch onto the waiting queue
      DWREngine._batchQueue[DWREngine._batchQueue.length] = batch;
    }
  }
};

//==============================================================================
// Only private stuff below here
//==============================================================================

/** A function to call if something fails. */
DWREngine._errorHandler = DWREngine.defaultMessageHandler;

/** A function to call to alert the user to some breakage. */
DWREngine._warningHandler = DWREngine.defaultMessageHandler;

/** A function to be called before requests are marshalled. Can be null. */
DWREngine._preHook = null;

/** A function to be called after replies are received. Can be null. */
DWREngine._postHook = null;

/** An array of the batches that we have sent and are awaiting a reply on. */
DWREngine._batches = [];

/** In ordered mode, the array of batches waiting to be sent */
DWREngine._batchQueue = [];

/** A map of known ids to their handler objects */
DWREngine._handlersMap = {};

/** What is the default remoting method */
DWREngine._method = DWREngine.XMLHttpRequest;

/** What is the default remoting verb (ie GET or POST) */
DWREngine._verb = "POST";

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

/** Are we polling the server for updates? */
DWREngine._pollServer = false;

/** Is there a long term poll (comet) interraction in place? */
DWREngine._pollComet = true;

/** What is the default polling method */
DWREngine._pollMethod = DWREngine.XMLHttpRequest;
//DWREngine._pollMethod = DWREngine.IFrame;

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
      DWREngine._handleError("Can't batch requests to multiple DWR Servlets.");
      return;
    }
  }
  // From the other params, work out which is the function (or object with
  // call meta-data) and which is the call parameters
  var params;
  var callData;
  var firstArg = args[0];
  var lastArg = args[args.length - 1];

  if (typeof firstArg == "function") {
    callData = { callback:args.shift() };
    params = args;
  }
  else if (typeof lastArg == "function") {
    callData = { callback:args.pop() };
    params = args;
  }
  else if (typeof lastArg == "object" && lastArg.callback != null && typeof lastArg.callback == "function") {
    callData = args.pop();
    params = args;
  }
  else if (firstArg == null) {
    // This could be a null callback function, but if the last arg is also
    // null then we can't tell which is the function unless there are only
    // 2 args, in which case we don't care!
    if (lastArg == null && args.length > 2) {
      if (DWREngine._warningHandler) {
        DWREngine._warningHandler("Ambiguous nulls at start and end of parameter list. Which is the callback function?");
      }
    }
    callData = { callback:args.shift() };
    params = args;
  }
  else if (lastArg == null) {
    callData = { callback:args.pop() };
    params = args;
  }
  else {
    if (DWREngine._warningHandler) {
      DWREngine._warningHandler("Missing callback function or metadata object.");
    }
    return;
  }

  // Get a unique ID for this call
  var random = Math.floor(Math.random() * 10001);
  var id = (random + "_" + new Date().getTime()).toString();
  var prefix = "c" + DWREngine._batch.map.callCount + "-";
  DWREngine._batch.ids.push(id);

  // batchMetaData stuff the we allow in callMetaData for convenience
  if (callData.method != null) {
    DWREngine._batch.method = callData.method;
    delete callData.method;
  }
  if (callData.verb != null) {
    DWREngine._batch.verb = callData.verb;
    delete callData.verb;
  }
  if (callData.async != null) {
    DWREngine._batch.async = callData.async;
    delete callData.async;
  }
  if (callData.timeout != null) {
    DWREngine._batch.timeout = callData.timeout;
    delete callData.timeout;
  }

  // callMetaData stuff that we handle with the rest of the batchMetaData
  if (callData.preHook != null) {
    DWREngine._batch.preHooks.unshift(callData.preHook);
    delete callData.preHook;
  }
  if (callData.postHook != null) {
    DWREngine._batch.postHooks.push(callData.postHook);
    delete callData.postHook;
  }

  // ScriptTag method parameter - the scriptTagBase
  if (callData.scriptTagBase != null) {
    DWREngine._batch.scriptTagBase = callData.scriptTagBase;
    delete callData.scriptTagBase;
  }

  // Default the error and warning handlers
  if (callData.errorHandler == null) callData.errorHandler = DWREngine._errorHandler;
  if (callData.warningHandler == null) callData.warningHandler = DWREngine._warningHandler;

  // Save the callMetaData
  DWREngine._handlersMap[id] = callData;

  // Copy extra callData into the map
  var data, prop;
  for (prop in callData) {
    data = callData[prop];
    if (typeof data != "function") {
      DWREngine._batch.map[prop] = "" + data;
    }
  }

  // Add in the page and session ids
  DWREngine._batch.map.httpSessionId = DWREngine._httpSessionId;
  DWREngine._batch.map.scriptSessionId = DWREngine._scriptSessionId;
  DWREngine._batch.map.page = window.location.pathname;

  DWREngine._batch.map[prefix + "scriptName"] = scriptName;
  DWREngine._batch.map[prefix + "methodName"] = methodName;
  DWREngine._batch.map[prefix + "id"] = id;

  // Serialize the parameters into batch.map
  DWREngine._addSerializeFunctions();
  for (i = 0; i < params.length; i++) {
    DWREngine._serializeAll(DWREngine._batch, [], params[i], prefix + "param" + i);
  }
  DWREngine._removeSerializeFunctions();

  // Now we have finished remembering the call, we incr the call count
  DWREngine._batch.map.callCount++;
  if (singleShot) {
    DWREngine.endBatch();
  }
};

/**
 * @private Poll the server to see if there is any data waiting
 */
DWREngine._poll = function(overridePath) {
  if (!DWREngine._pollServer) {
    return;
  }
  // Get a unique ID for this call
  var random = Math.floor(Math.random() * 10001);
  var id = (random + "_" + new Date().getTime()).toString();
  // Create a batch object that describes how we are to call the server
  var batch = {
    completed:false,
    map:{
      callCount:1,
      'c0-scriptName':'DWRSystem',
      'c0-methodName':'poll',
      'c0-id':id,
      httpSessionId:DWREngine._httpSessionId,
      scriptSessionId:DWREngine._scriptSessionId,
      page:window.location.pathname
    },
    method:DWREngine._pollMethod,
    isPoll:true,
    verb:"POST",
    async:true,
    paramCount:0,
    ids:[ id ],
    path:(overridePath) ? overridePath : DWREngine._defaultPath,
    preHooks:(DWREngine._preHook) ? [ DWREngine._preHook ] : [],
    postHooks:(DWREngine._postHook) ? [ DWREngine._postHook ] : [],
    timeout:0
  };
  // IE can't handle XHR polling, so we must back off to iframe
  if (!window.XMLHttpRequest && DWREngine._pollMethod == DWREngine.XMLHttpRequest) {
    batch.method = DWREngine.IFrame;
  }
  // Create an entry in the handlers map for what happens when the reply arrives
  DWREngine._handlersMap[id] = {
    errorHandler:DWREngine._errorHandler,
    warningHandler:DWREngine._warningHandler,
    callback:DWREngine._triggerNextPoll
  };
  // Send the data
  DWREngine._sendData(batch);
  DWREngine._batches[DWREngine._batches.length] = batch;
  DWREngine._checkCometPoll();
};

/**
 * Setup an interval to kick off the next poll
 */
DWREngine._triggerNextPoll = function(pause) {
  setTimeout("DWREngine._poll()", pause);
}

/**
 * @private Check for reverse Ajax activity
 */
DWREngine._checkCometPoll = function() {
  if (DWREngine._pollComet) {
    if (DWREngine._pollFrame) {
      setTimeout("DWREngine._checkCometPoll()", DWREngine._pollCometInterval);
      var text = DWREngine._getTextFromCometIFrame();
      DWREngine._processCometResponse(text);
    }
    else if (DWREngine._pollReq) {
      setTimeout("DWREngine._checkCometPoll()", DWREngine._pollCometInterval);
      try {
        var xhrtext = DWREngine._pollReq.responseText;
        DWREngine._processCometResponse(xhrtext);
      }
      catch (ex) {
        // IE complains for no good reason. Ignore
      }
    }
  }
}

/**
 * @private Extract the whole (executed an all) text from the current iframe
 */
DWREngine._getTextFromCometIFrame = function() {
  var frameDocument;
  if (DWREngine._pollFrame.contentDocument) {
    frameDocument = DWREngine._pollFrame.contentDocument.defaultView.document;
  }
  else if (DWREngine._pollFrame.contentWindow) {
    frameDocument = DWREngine._pollFrame.contentWindow.document
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
}

/**
 * @private Some more text might have come in, test and execute the new stuff
 */
DWREngine._processCometResponse = function(response) {
  if (DWREngine._pollCometSize != response.length) {
    var firstStartTag = response.indexOf("//@DWR-START@", DWREngine._pollCometSize);
    if (firstStartTag == -1) {
      // There is no start tag so we ignore the rest
      // DWRUtil.debug("Missing //@DWR-START@ Skipping: " + response.substring(DWREngine._pollCometSize));
      DWREngine._pollCometSize = response.length;
    }
    else {
      // if (firstStartTag != DWREngine._pollCometSize) {
      //   DWRUtil.debug("//@DWR-START@ not at start skipping: " + response.substring(DWREngine._pollCometSize, firstStartTag));
      // }
      var lastEndTag = response.lastIndexOf("//@DWR-END@");
      if (lastEndTag != -1) {
        var executeString = response.substring(firstStartTag + 13, lastEndTag);
        // DWRUtil.debug(executeString);
        eval(executeString);
        // Skip the end tag too for next time
        DWREngine._pollCometSize = lastEndTag + 11;
      }
      // else {
      //   DWRUtil.debug("Missing //@DWR-END@ Postponing: " + executeString);
      // }
    }
  }
}

/**
 * @private Actually send the block of data in the batch object.
 */
DWREngine._sendData = function(batch) {
  // If the batch is empty, don't send anything
  if (batch.map.callCount == 0) return;
  // Call any pre-hooks
  for (var i = 0; i < batch.preHooks.length; i++) {
    batch.preHooks[i]();
  }
  batch.preHooks = null;
  // Set a timeout
  if (batch.timeout && batch.timeout != 0) {
    batch.interval = setInterval(function() {
      clearInterval(batch.interval);
      DWREngine._abortRequest(batch);
    }, batch.timeout);
  }
  // A quick string to help people that use web log analysers
  var statsInfo;
  if (batch.map.callCount == 1) {
    statsInfo = batch.map["c0-scriptName"] + "." + batch.map["c0-methodName"] + ".dwr";
  }
  else {
    statsInfo = "Multiple." + batch.map.callCount + ".dwr";
  }

  // Get setup for XMLHttpRequest if possible
  if (batch.method == DWREngine.XMLHttpRequest) {
    if (window.XMLHttpRequest) {
      batch.req = new XMLHttpRequest();
    }
    // IE5 for the mac claims to support window.ActiveXObject, but throws an error when it's used
    else if (window.ActiveXObject && !(navigator.userAgent.indexOf('Mac') >= 0 && navigator.userAgent.indexOf("MSIE") >= 0)) {
      batch.req = DWREngine._newActiveXObject(DWREngine._XMLHTTP);
    }
  }

  var query = "";
  var prop;

  // This equates to (batch.method = XHR && browser supports XHR)
  if (batch.req) {
    // Proceed using XMLHttpRequest
    if (batch.async) {
      batch.req.onreadystatechange = function() {
        DWREngine._stateChange(batch);
      };
    }
    // If we're polling, record this for monitoring
    if (batch.isPoll) DWREngine._pollReq = batch.req;
    // Workaround for Safari 1.x POST bug
    var indexSafari = navigator.userAgent.indexOf('Safari/');
    if (indexSafari >= 0) {
      // So this is Safari, are we on 1.x? POST is broken
      var version = navigator.userAgent.substring(indexSafari + 7);
      var verNum = parseInt(version, 10);
      if (verNum < 400) {
        batch.verb == "GET";
      }
    }
    if (batch.verb == "GET") {
      // Some browsers (Opera/Safari2) seem to fail to convert the value
      // of batch.map.callCount to a string in the loop below so we do it
      // manually here.
      batch.map.callCount = "" + batch.map.callCount;

      for (prop in batch.map) {
        var qkey = encodeURIComponent(prop);
        var qval = encodeURIComponent(batch.map[prop]);
        if (qval == "") {
          if (DWREngine._warningHandler) {
            DWREngine._warningHandler("Found empty qval for qkey=" + qkey);
          }
        }
        query += qkey + "=" + qval + "&";
      }
      query += "jsessionid=" + DWREngine._httpSessionId;

      try {
        batch.req.open("GET", batch.path + "/plainjs/" + statsInfo + "?" + query, batch.async);
        batch.req.send(null);
        if (!batch.async) {
          DWREngine._stateChange(batch);
        }
      }
      catch (ex) {
        DWREngine._handleMetaDataError(null, ex);
      }
    }
    else {
      for (prop in batch.map) {
        if (typeof batch.map[prop] != "function") {
          query += prop + "=" + batch.map[prop] + "\n";
        }
      }

      try {
        // This might include Safari too, but it shouldn't do any harm
        //   if (navigator.userAgent.indexOf('Gecko') >= 0) {
        //     batch.req.setRequestHeader('Connection', 'close');
        //   }
        batch.req.open("POST", batch.path + "/plainjs/" + statsInfo, batch.async);
        batch.req.setRequestHeader('Content-Type', 'text/plain');
        batch.req.send(query);
        if (!batch.async) {
          DWREngine._stateChange(batch);
        }
      }
      catch (ex) {
        DWREngine._handleMetaDataError(null, ex);
      }
    }
  }
  else if (batch.method != DWREngine.ScriptTag) {
    var idname = "dwr-if-" + batch.map["c0-id"];
    // Proceed using iframe
    batch.div = document.createElement('div');
    batch.div.innerHTML = "<iframe src='javascript:void(0)' frameborder='0' width='0' height='0' id='" + idname + "' name='" + idname + "'></iframe>";
    document.body.appendChild(batch.div);
    batch.iframe = document.getElementById(idname);
    batch.iframe.setAttribute('style', 'width:0px; height:0px; border:0px;');
    // Settings that vary if we are polling
    var baseUrl;
    if (batch.isPoll) {
      DWREngine._pollFrame = batch.iframe;
      DWREngine._pollCometSize = 0;
      baseUrl = batch.path + "/plainjs/" + statsInfo;
    }
    else {
      baseUrl = batch.path + "/htmljs/" + statsInfo;
    }
    if (batch.verb == "GET") {
      for (prop in batch.map) {
        if (typeof batch.map[prop] != "function") {
          query += encodeURIComponent(prop) + "=" + encodeURIComponent(batch.map[prop]) + "&";
        }
      }
      query = query.substring(0, query.length - 1);

      batch.iframe.setAttribute('src', baseUrl + "?" + query);
      document.body.appendChild(batch.iframe);
    }
    else {
      batch.form = document.createElement('form');
      batch.form.setAttribute('id', 'dwr-form');
      batch.form.setAttribute('action', baseUrl);
      batch.form.setAttribute('target', idname);
      batch.form.target = idname;
      batch.form.setAttribute('method', 'post');
      for (prop in batch.map) {
        var formInput = document.createElement('input');
        formInput.setAttribute('type', 'hidden');
        formInput.setAttribute('name', prop);
        formInput.setAttribute('value', batch.map[prop]);
        batch.form.appendChild(formInput);
      }
      document.body.appendChild(batch.form);
      batch.form.submit();
	}
  }
  else {
    if (!batch.scriptTagBase) {
      DWREngine._handleError("Please specify the scriptTagBase property within the call data.");
      return;
    }
    for (prop in batch.map) {
      if (typeof batch.map[prop] != "function") {
        query += encodeURIComponent(prop) + "=" + encodeURIComponent(batch.map[prop]) + "&";
      }
    }
    query = query.substring(0, query.length - 1);
    batch.script = document.createElement('script');
    batch.script.id = "dwr-st-" + batch.map["c0-id"];
    batch.script.src = batch.scriptTagBase + batch.path + "/plainjs/" + statsInfo + "?" + query;
    document.body.appendChild(batch.script);
  }
};

/**
 * @private Called by XMLHttpRequest to indicate that something has happened
 */
DWREngine._stateChange = function(batch) {
  if (!batch.completed && batch.req.readyState == 4) {
    try {
      var reply = batch.req.responseText;
      var status = batch.req.status;

      if (reply == null || reply == "") {
        DWREngine._handleMetaDataError(null, "No data received from server");
        return;
      }

// DWR doesn't use response codes for failure conditions, so we should be able
// to delete this. If no one complains, we will.
//      if (status != 200) {
//        if (reply == null) reply = "Unknown error occured";
//        DWREngine._handleMetaDataError(null, reply);
//        return;
//      }

      var contentType = batch.req.getResponseHeader('Content-Type');
      if (DWREngine._textHtmlHandler && contentType.match(/^text\/html/)) {
        DWREngine._textHtmlHandler();
        return;
      }

      // Comet replies might have already partially executed
      if (batch.req == DWREngine._pollReq) {
        DWREngine._processCometResponse(reply);
      }
      else {
        // This should get us out of 404s etc.
        if (reply.search("DWREngine._handle") == -1) {
          DWREngine._handleMetaDataError(null, "Invalid reply from server");
          return;
        }
        eval(reply);
      }

      // We're done. Clear up
      DWREngine._clearUp(batch);
    }
    catch (ex) {
      if (ex == null) ex = "Unknown error occured";
      DWREngine._handleMetaDataError(null, ex);
    }
    finally {
      // If there is anything on the queue waiting to go out, then send it.
      // We don't need to check for ordered mode, here because when ordered mode
      // gets turned off, we still process *waiting* batches in an ordered way.
      if (DWREngine._batchQueue.length != 0) {
        var sendbatch = DWREngine._batchQueue.shift();
        DWREngine._sendData(sendbatch);
        DWREngine._batches[DWREngine._batches.length] = sendbatch;
      }
    }
  }
};

/**
 * @private Called by reply scripts generated as a result of remote requests
 * @param id The identifier of the call that we are handling a response for
 * @param reply The data to pass to the callback function
 */
DWREngine._handleResponse = function(id, reply) {
  // Clear this callback out of the list - we don't need it any more
  var handlers = DWREngine._handlersMap[id];
  DWREngine._handlersMap[id] = null;
  // TODO: How can we do this - delete DWREngine._handlersMap.id
  if (handlers) {
    // Error handlers inside here indicate an error that is nothing to do
    // with DWR so we handle them differently.
    try {
      if (handlers.callback) handlers.callback(reply);
    }
    catch (ex) {
      DWREngine._handleMetaDataError(handlers, ex);
    }
  }

  // Finalize the call for IFrame transport 
  var responseBatch = DWREngine._batches[DWREngine._batches.length-1];
  if (responseBatch.method == DWREngine.IFrame) {
    // Only finalize after the last call has been handled
    if (responseBatch.map["c"+(responseBatch.map.callCount-1)+"-id"] == id) {
      DWREngine._clearUp(responseBatch);
    }
  }
};

/**
 * @private This method is called by Javascript that is emitted by server
 */
DWREngine._handleServerError = function(id, error) {
  // Clear this callback out of the list - we don't need it any more
  var handlers = DWREngine._handlersMap[id];
  DWREngine._handlersMap[id] = null;
  if (error.message) {
    DWREngine._handleMetaDataError(handlers, error.message, error);
  }
  else {
    DWREngine._handleMetaDataError(handlers, error);
  }
};

/**
 * @private This is a hack to make the context be this window
 */
DWREngine._eval = function(script) {
  return eval(script);
}

/**
 * @private Called as a result of a request timeout
 */ 
DWREngine._abortRequest = function(batch) {
  if (batch && batch.metadata != null && !batch.completed) {
    DWREngine._clearUp(batch);
    if (batch.req) batch.req.abort();
    // Call all the timeout errorHandlers
    var handlers;
    var id;
    for (var i = 0; i < batch.ids.length; i++) {
      id = batch.ids[i];
      handlers = DWREngine._handlersMap[id];
      DWREngine._handleMetaDataError(handlers, "Timeout");
    }
  }
};

/**
 * @private A call has finished by whatever means and we need to shut it all down.
 */
DWREngine._clearUp = function(batch) {
  if (batch.completed) {
    DWREngine._handleWarning("double complete");
    return;
  }

  // Irame tidyup
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
};

/**
 * @private Generic error handling routing to save having null checks everywhere.
 */
DWREngine._handleError = function(reason, ex) {
  if (DWREngine._errorHandler) {
    DWREngine._errorHandler(reason, ex);
  }
};

/**
 * @private Generic error handling routing to save having null checks everywhere.
 */
DWREngine._handleWarning = function(reason, ex) {
  if (DWREngine._warningHandler) {
    DWREngine._warningHandler(reason, ex);
  }
};

/**
 * @private Generic error handling routing to save having null checks everywhere.
 */
DWREngine._handleMetaDataError = function(handlers, reason, ex) {
  if (handlers && typeof handlers.errorHandler == "function") {
    handlers.errorHandler(reason, ex);
  }
  else {
    DWREngine._handleError(reason, ex);
  }
};

/**
 * @private Hack a polymorphic dwrSerialize() function on all basic types. Yeulch
 */
DWREngine._addSerializeFunctions = function() {
  Object.prototype.dwrSerialize = DWREngine._serializeObject;
  Array.prototype.dwrSerialize = DWREngine._serializeArray;
  Boolean.prototype.dwrSerialize = DWREngine._serializeBoolean;
  Number.prototype.dwrSerialize = DWREngine._serializeNumber;
  String.prototype.dwrSerialize = DWREngine._serializeString;
  Date.prototype.dwrSerialize = DWREngine._serializeDate;
};

/**
 * @private Remove the hacked polymorphic dwrSerialize() function on all basic types.
 */
DWREngine._removeSerializeFunctions = function() {
  delete Object.prototype.dwrSerialize;
  delete Array.prototype.dwrSerialize;
  delete Boolean.prototype.dwrSerialize;
  delete Number.prototype.dwrSerialize;
  delete String.prototype.dwrSerialize;
  delete Date.prototype.dwrSerialize;
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
    if (data.dwrSerialize) {
      batch.map[name] = data.dwrSerialize(batch, referto, data, name);
    }
    else if (data.nodeName) {
      batch.map[name] = DWREngine._serializeXml(batch, referto, data, name);
    }
    else {
      if (DWREngine._warningHandler) {
        DWREngine._warningHandler("Object without dwrSerialize: " + typeof data + ", attempting default converter.");
      }
      batch.map[name] = "default:" + data;
    }
    break;

  case "function":
    // We just ignore functions.
    break;

  default:
    if (DWREngine._warningHandler) {
      DWREngine._warningHandler("Unexpected type: " + typeof data + ", attempting default converter.");
    }
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
  if (lookup) {
    return "reference:" + lookup.name;
  }
  referto.push({ data:data, name:name });
  return null;
};

/** @private Marshall an object */
DWREngine._serializeObject = function(batch, referto, data, name) {
  var ref = DWREngine._lookup(referto, this, name);
  if (ref) return ref;

  if (data.nodeName) {
    return DWREngine._serializeXml(batch, referto, data, name);
  }

  // treat objects as an associative arrays
  var reply = "Object:{";
  var element;
  for (element in this)  {
    if (element != "dwrSerialize") {
      batch.paramCount++;
      var childName = "c" + DWREngine._batch.map.callCount + "-e" + batch.paramCount;
      DWREngine._serializeAll(batch, referto, this[element], childName);

      reply += encodeURIComponent(element);
      reply += ":reference:";
      reply += childName;
      reply += ", ";
    }
  }

  if (reply.substring(reply.length - 2) == ", ") {
    reply = reply.substring(0, reply.length - 2);
  }
  reply += "}";

  return reply;
};

/** @private Marshall an object */
DWREngine._serializeXml = function(batch, referto, data, name) {
  var ref = DWREngine._lookup(referto, this, name);
  if (ref) {
    return ref;
  }
  var output;
  if (window.XMLSerializer) {
    var serializer = new XMLSerializer();
    output = serializer.serializeToString(data);
  }
  else {
    output = data.toXml;
  }
  return "XML:" + encodeURIComponent(output);
};

/** @private Marshall an array */
DWREngine._serializeArray = function(batch, referto, data, name) {
  var ref = DWREngine._lookup(referto, this, name);
  if (ref) return ref;

  var reply = "Array:[";
  for (var i = 0; i < this.length; i++) {
    if (i != 0) {
      reply += ",";
    }

    batch.paramCount++;
    var childName = "c" + DWREngine._batch.map.callCount + "-e" + batch.paramCount;
    DWREngine._serializeAll(batch, referto, this[i], childName);
    reply += "reference:";
    reply += childName;
  }
  reply += "]";

  return reply;
};

/** @private Marshall a Boolean */
DWREngine._serializeBoolean = function(batch, referto, data, name) {
  return "Boolean:" + this;
};

/** @private Marshall a Number */
DWREngine._serializeNumber = function(batch, referto, data, name) {
  return "Number:" + this;
};

/** @private Marshall a String */
DWREngine._serializeString = function(batch, referto, data, name) {
  return "String:" + encodeURIComponent(this);
};

/** @private Marshall a Date */
DWREngine._serializeDate = function(batch, referto, data, name) {
  return "Date:" + this.getTime();
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
    dom.loadXML(xml);
    // What happens on parse fail with IE?
    return dom;
  }
  //else if (window.XMLHttpRequest) {
  //  // Hack with XHR to get at Safari's parser
  //  var req = new XMLHttpRequest;
  //  var url = "data:application/xml;charset=utf-8," + encodeURIComponent(xml);
  //  req.open("GET", url, false);
  //  req.send(null);
  //  return req.responseXML;
  //}
  else {
    var div = document.createElement('div');
    div.innerHTML = xml;
    return div;
  }
};

/**
 * @private Helper to find an ActiveX object that works.
 * @param axarray An array of strings to attempt to create ActiveX objects from
 */
DWREngine._newActiveXObject = function(axarray) {
  var returnValue;  
  for (var i = 0; i < axarray.length; i++) {
    try {
      returnValue = new ActiveXObject(axarray[i]);
      break;
    }
    catch (ex) {
    }
  }
  return returnValue;
};

/** @private To make up for the lack of encodeURIComponent() on IE5.0 */
if (typeof window.encodeURIComponent === 'undefined') {
  DWREngine._utf8 = function(wide) {
    wide = "" + wide; // Make sure it is a string
    var c;
    var s;
    var enc = "";
    var i = 0;
    while (i < wide.length) {
      c = wide.charCodeAt(i++);
      // handle UTF-16 surrogates
      if (c >= 0xDC00 && c < 0xE000) continue;
      if (c >= 0xD800 && c < 0xDC00) {
        if (i >= wide.length) continue;
        s = wide.charCodeAt(i++);
        if (s < 0xDC00 || c >= 0xDE00) continue;
        c = ((c - 0xD800) << 10) + (s - 0xDC00) + 0x10000;
      }
      // output value
      if (c < 0x80) {
        enc += String.fromCharCode(c);
      }
      else if (c < 0x800) {
        enc += String.fromCharCode(0xC0 + (c >> 6), 0x80 + (c & 0x3F));
      }
      else if (c < 0x10000) {
        enc += String.fromCharCode(0xE0 + (c >> 12), 0x80 + (c >> 6 & 0x3F), 0x80 + (c & 0x3F));
      }
      else {
        enc += String.fromCharCode(0xF0 + (c >> 18), 0x80 + (c >> 12 & 0x3F), 0x80 + (c >> 6 & 0x3F), 0x80 + (c & 0x3F));
      }
    }
    return enc;
  }

  DWREngine._hexchars = "0123456789ABCDEF";

  DWREngine._toHex = function(n) {
    return DWREngine._hexchars.charAt(n >> 4) + DWREngine._hexchars.charAt(n & 0xF);
  }

  DWREngine._okURIchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-";

  window.encodeURIComponent = function(s)  {
    s = DWREngine._utf8(s);
    var c;
    var enc = "";
    for (var i= 0; i<s.length; i++) {
      if (DWREngine._okURIchars.indexOf(s.charAt(i)) == -1) {
        enc += "%" + DWREngine._toHex(s.charCodeAt(i));
      }
      else {
        enc += s.charAt(i);
      }
    }
    return enc;
  }
}

/** @private To make up for the lack of Array.splice() on IE5.0 */
if (typeof Array.prototype.splice === 'undefined') {
  Array.prototype.splice = function(ind, cnt)
  {
    if (arguments.length == 0) {
      return ind;
    }
    if (typeof ind != "number") {
      ind = 0;
    }
    if (ind < 0) {
      ind = Math.max(0,this.length + ind);
    }
    if (ind > this.length) {
      if (arguments.length > 2) {
        ind = this.length;
      }
      else {
        return [];
      }
    }
    if (arguments.length < 2) {
      cnt = this.length-ind;
    }

    cnt = (typeof cnt == "number") ? Math.max(0, cnt) : 0;
    removeArray = this.slice(ind, ind + cnt);
    endArray = this.slice(ind + cnt);
    this.length = ind;

    for (var i = 2; i < arguments.length; i++) {
      this[this.length] = arguments[i];
    }
    for (i = 0; i < endArray.length; i++) {
      this[this.length] = endArray[i];
    }

    return removeArray;
  }
}

/** @private To make up for the lack of Array.shift() on IE5.0 */
if (typeof Array.prototype.shift === 'undefined') {
  Array.prototype.shift = function(str) {
    var val = this[0];
    for (var i = 1; i < this.length; ++i) {
      this[i - 1] = this[i];
    }
    this.length--;
    return val;
  }
}

/** @private To make up for the lack of Array.unshift() on IE5.0 */
if (typeof Array.prototype.unshift === 'undefined') {
  Array.prototype.unshift = function() {
    var i = unshift.arguments.length;
    for (var j = this.length - 1; j >= 0; --j) {
      this[j + i] = this[j];
    }
    for (j = 0; j < i; ++j) {
      this[j] = unshift.arguments[j];
    }
  }
}

/** @private To make up for the lack of Array.push() on IE5.0 */
if (typeof Array.prototype.push === 'undefined') {
  Array.prototype.push = function() {
    var sub = this.length;
    for (var i = 0; i < push.arguments.length; ++i) {
      this[sub] = push.arguments[i];
      sub++;
    }
  }
}

/** @private To make up for the lack of Array.pop() on IE5.0 */
if (typeof Array.prototype.pop === 'undefined') {
  Array.prototype.pop = function() {
    var lastElement = this[this.length - 1];
    this.length--;
    return lastElement;
  }
}
