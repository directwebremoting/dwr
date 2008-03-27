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
 * The DWR object is defined by dwr.util etc.
 */
if (typeof this['dwr'] == 'undefined') {
  dwr = { };
}

(function() {
  dwr.engine = { };

  /**
    * Set an alternative error handler from the default alert box.
    * @param {Function} handler The function to call when an error happens
    * @see http://getahead.org/dwr/browser/engine/errors
    */
  dwr.engine.setErrorHandler = function(handler) {
    dwr.engine._errorHandler = handler;
  };

  /**
    * Set an alternative warning handler from the default alert box.
    * @param {Function} handler The function to call when a warning happens
    * @see http://getahead.org/dwr/browser/engine/errors
    */
  dwr.engine.setWarningHandler = function(handler) {
    dwr.engine._warningHandler = handler;
  };

  /**
    * Setter for the text/html handler - what happens if a DWR request gets an HTML
    * reply rather than the expected Javascript. Often due to login timeout
    * @param {Function} handler The function to call on an unexpected text/html content type
    */
  dwr.engine.setTextHtmlHandler = function(handler) {
    dwr.engine._textHtmlHandler = handler;
  };

  /**
    * Set a default timeout value for all calls. 0 (the default) turns timeouts off.
    * @param {Function} handler The function to call when we get bored of waiting for a call
    * @see getahead.org/dwr/browser/engine/errors
    */
  dwr.engine.setTimeout = function(timeout) {
    dwr.engine._timeout = timeout;
  };

  /**
    * The Pre-Hook is called before any DWR remoting is done.
    * @param {Function} handler The function to call before any remote calls
    * @see getahead.org/dwr/browser/engine/hooks
    */
  dwr.engine.setPreHook = function(handler) {
    dwr.engine._preHook = handler;
  };

  /**
   * The Post-Hook is called after any DWR remoting is done.
   * @param {Function} handler The function to call after any remote calls
   * @see getahead.org/dwr/browser/engine/hooks
   */
  dwr.engine.setPostHook = function(handler) {
    dwr.engine._postHook = handler;
  };

  /**
   * Custom headers for all DWR calls
   * @param {Object} headers Object containing name/value pairs for extra headers
   * @see getahead.org/dwr/????
   */
  dwr.engine.setHeaders = function(headers) {
    dwr.engine._headers = headers;
  };

  /**
   * Custom parameters for all DWR calls
   * @param {Object} parameters Object containing name/value pairs for extra request parameters
   * @see getahead.org/dwr/????
   */
  dwr.engine.setParameters = function(parameters) {
    dwr.engine._parameters = parameters;
  };

  /**
   * Ensure that remote calls happen in the order in which they were sent? (Default: false)
   * @param {boolean} ordered true to enable ordered processing
   * @see getahead.org/dwr/browser/engine/ordering
   */
  dwr.engine.setOrdered = function(ordered) {
    dwr.engine._ordered = ordered;
  };

  /**
   * Do we ask the XHR object to be asynchronous? (Default: true)
   * Warning: it is <strong>highly</strong> advised to use the default ofasync
   * processing, especially when dealing with Internet based requests.
   * @param {boolean} async false to enable sync processing for XHR queries
   * @see getahead.org/dwr/browser/engine/options
   */
  dwr.engine.setAsync = function(async) {
    dwr.engine._async = async;
  };

  /**
   * Does the client actively check the server for updates? (Default: false)
   * @param {boolean} async true to enable low latency reverse ajax
   * @see getahead.org/dwr/browser/engine/options
   */
  dwr.engine.setActiveReverseAjax = function(activeReverseAjax) {
    if (activeReverseAjax) {
      // Bail if we are already started
      if (dwr.engine._activeReverseAjax) return;
      dwr.engine._activeReverseAjax = true;
      dwr.engine._poll();
    }
    else {
      // Can we cancel an existing request?
      if (dwr.engine._activeReverseAjax && dwr.engine._pollReq) dwr.engine._pollReq.abort();
      dwr.engine._activeReverseAjax = false;
    }
    // TODO: in iframe mode, if we start, stop, start then the second start may
    // well kick off a second iframe while the first is still about to return
    // we should cope with this but we don't
  };

  /**
   * Turn server notification of page unload on and off
   * @param {boolean} notify true or false depending on if we want to turn unload on or off
   * @see getahead.org/dwr/browser/engine/options
   */
  dwr.engine.setNotifyServerOnPageUnload = function(notify) {
    if (notify == dwr.engine._isNotifyServerOnPageUnload) return;
    if (notify) {
      if (window.addEventListener) window.addEventListener('unload', dwr.engine._unloader, false);
      else if (window.attachEvent) window.attachEvent('onunload', dwr.engine._unloader);
    }
    else {
      if (window.removeEventListener) window.removeEventListener('unload', dwr.engine._unloader, false);
      else if (window.detachEvent) window.detachEvent('onunload', dwr.engine._unloader);
    }
    dwr.engine._isNotifyServerOnPageUnload = notify;
  };

  /**
   * The default message handler.
   * @param {String} message The text of the error message
   * @param {Object} ex An error object containing at least a name and message
   * @see getahead.org/dwr/browser/engine/errors
   */
  dwr.engine.defaultErrorHandler = function(message, ex) {
    dwr.engine._debug("Error: " + ex.name + ", " + ex.message, true);
    if (message == null || message == "") alert("A server error has occurred.");
    // Ignore NS_ERROR_NOT_AVAILABLE if Mozilla is being narky
    else if (message.indexOf("0x80040111") != -1) dwr.engine._debug(message);
    else alert(message);
  };

  /**
   * The default warning handler.
   * @param {String} message The text of the error message
   * @param {Object} ex An error object containing at least a name and message
   * @see getahead.org/dwr/browser/engine/errors
   */
  dwr.engine.defaultWarningHandler = function(message, ex) {
    dwr.engine._debug(message);
  };

  /**
   * For reduced latency you can group several remote calls together using a batch.
   * @see getahead.org/dwr/browser/engine/batch
   */
  dwr.engine.beginBatch = function() {
    if (dwr.engine._batch) {
      dwr.engine._handleError(null, { name:"dwr.engine.batchBegun", message:"Batch already begun" });
      return;
    }
    dwr.engine._batch = dwr.engine.batch.create();
  };

  /**
   * Finished grouping a set of remote calls together. Go and execute them all.
   * @param {Object} options A options object to customize processing
   * @see getahead.org/dwr/browser/engine/batch
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
    if (options) dwr.engine.batch.merge(batch, options);

    // In ordered mode, we don't send unless the list of sent items is empty
    if (dwr.engine._ordered && dwr.engine._batchesLength != 0) {
      dwr.engine._batchQueue[dwr.engine._batchQueue.length] = batch;
    }
    else {
      dwr.engine.transport.send(batch);
    }
  };

  //==============================================================================
  // Only private stuff below here
  //==============================================================================

  /** The session cookie name */
  dwr.engine._sessionCookieName = "${sessionCookieName}"; // JSESSIONID

  /** Is GET enabled for the benefit of Safari? */
  dwr.engine._allowGetForSafariButMakeForgeryEasier = "${allowGetForSafariButMakeForgeryEasier}";

  /** The script prefix to strip in the case of scriptTagProtection. */
  dwr.engine._scriptTagProtection = "${scriptTagProtection}";

  /** The default path to the DWR servlet */
  dwr.engine._defaultPath = "${defaultPath}";

  /** Do we use XHR for reverse ajax because we are not streaming? */
  dwr.engine._pollWithXhr = "${pollWithXhr}";

  dwr.engine._ModePlainCall = "/call/plaincall/";

  dwr.engine._ModeHtmlCall = "/call/htmlcall/";

  dwr.engine._ModePlainPoll = "/call/plainpoll/";

  dwr.engine._ModeHtmlPoll = "/call/htmlpoll/";

  /** The page id */
  dwr.engine._scriptSessionId = null;

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

  /** Do we attempt to ensure that calls happen in the order in which they were
  sent? This starts true until we have fetched the ids, when it is to false */
  dwr.engine._ordered = true;

  /** Do we make the calls async? */
  dwr.engine._async = true;

  /** The current batch (if we are in batch mode) */
  dwr.engine._batch = null;

  /** The global timeout */
  dwr.engine._timeout = 0;

  /** Are we doing comet or polling? */
  dwr.engine._activeReverseAjax = false;

  /** The xhr object that we are using to poll */
  dwr.engine._pollReq = null;

  /** How many milliseconds between internal comet polls */
  dwr.engine._pollCometInterval = 200;

  /** How many times have we re-tried to poll? */
  dwr.engine._pollRetries = 0;
  dwr.engine._maxPollRetries = 10;

  /** The intervals between successive retries in seconds */
  dwr.engine._retryIntervals = [ 2, 5, 10, 60, 300 ];

  /** Do we do a document.reload if we get a text/html reply? */
  dwr.engine._textHtmlHandler = null;

  /** If you wish to send custom headers with every request */
  dwr.engine._headers = null;

  /** If you wish to send extra custom request parameters with each request */
  dwr.engine._parameters = null;

  /** Batch ids allow us to know which batch the server is answering */
  dwr.engine._nextBatchId = 0;

  /** A list of the properties that need merging from calls to a batch */
  dwr.engine._propnames = [ "async", "timeout", "errorHandler", "warningHandler", "textHtmlHandler" ];

  /** Do we stream, or can be hacked to do so? */
  dwr.engine._partialResponseNo = 0;
  dwr.engine._partialResponseYes = 1;
  dwr.engine._partialResponseFlush = 2;

  /** Are we doing page unloading? */
  dwr.engine._isNotifyServerOnPageUnload = false;

  /**
   * Find the HTTP session id sent by the web server
   * @private
   */
  dwr.engine._getHttpSessionId = function() {
    // try to find the httpSessionId
    var cookies = document.cookie.split(';');
    for (var i = 0; i < cookies.length; i++) {
      var cookie = cookies[i];
      while (cookie.charAt(0) == ' ') cookie = cookie.substring(1, cookie.length);
      if (cookie.indexOf(dwr.engine._sessionCookieName + "=") == 0) {
        return cookie.substring(dwr.engine._sessionCookieName.length + 1, cookie.length);
      }
    }
    return "";
  };

  /** A function to call if something fails. */
  dwr.engine._errorHandler = dwr.engine.defaultErrorHandler;

  /** For debugging when something unexplained happens. */
  dwr.engine._warningHandler = dwr.engine.defaultWarningHandler;

  /** Undocumented interceptors - do not use */
  dwr.engine._postSeperator = "\n";
  dwr.engine._defaultInterceptor = function(data) { return data; };
  dwr.engine._urlRewriteHandler = dwr.engine._defaultInterceptor;
  dwr.engine._contentRewriteHandler = dwr.engine._defaultInterceptor;
  dwr.engine._replyRewriteHandler = dwr.engine._defaultInterceptor;

  /** @private If we have used reverse ajax then we try to tell the server we are gone */
  dwr.engine._unloader = function() {
    dwr.engine._debug("calling unloader for: " + dwr.engine._scriptSessionId);
    var batch = {
      map:{
        callCount:1,
        'c0-scriptName':'__System',
        'c0-methodName':'pageUnloaded',
        'c0-id':0
      },
      paramCount:0, isPoll:false, async:true,
      headers:{}, preHooks:[], postHooks:[],
      timeout:dwr.engine._timeout,
      errorHandler:null, warningHandler:null, textHtmlHandler:null,
      path:dwr.engine._defaultPath,
      handlers:[{ exceptionHandler:null, callback:null }]
    };
    dwr.engine.transport.send(batch);
    dwr.engine.setNotifyServerOnPageUnload(false);
  };

  /**
   * Send a request. Called by the Javascript interface stub
   * @private
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

    // To make them easy to manipulate we copy the arguments into an args array
    var args = [];
    for (var i = 0; i < arguments.length - 3; i++) {
      args[i] = arguments[i + 3];
    }

    var batch = dwr.engine._batch;
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

    dwr.engine.batch.addCall(batch, scriptName, methodName, args);

    // Now we have finished remembering the call, we increment the call count
    batch.map.callCount++;
    if (singleShot) dwr.engine.endBatch();
  };

  /**
   * Poll the server to see if there is any data waiting
   * @private
   * @param {Object} overridePath
   */
  dwr.engine._poll = function(overridePath) {
    if (!dwr.engine._activeReverseAjax) {
      return;
    }
    var batch = dwr.engine.batch.createPoll();
    dwr.engine.transport.send(batch);
  };

  /**
   * Try to recover from polling errors
   * @param {Object} msg
   * @param {Object} ex
   */
  dwr.engine._pollErrorHandler = function(msg, ex) {
    if (dwr.engine._pollRetries > dwr.engine._maxPollRetries) {
      dwr.engine._activeReverseAjax = false;
      dwr.engine._debug("Reverse Ajax poll failed (retries=" + dwr.engine._pollRetries + "). Giving Up: " + ex.name + " : " + ex.message);
      dwr.engine._debug("Giving up.");
      return;
    }

    var index = dwr.engine._pollRetries;
    if (index >= dwr.engine._retryIntervals.length) {
      index = dwr.engine._retryIntervals.length - 1;
    }

    dwr.engine._debug("Reverse Ajax poll failed (retries=" + dwr.engine._pollRetries + "). Trying again in " + dwr.engine._retryIntervals[index] + "s: " + ex.name + " : " + ex.message);
    setTimeout("dwr.engine._poll()", 1000 * dwr.engine._retryIntervals[index]);

    dwr.engine._pollRetries++;
  };

  /** @private This is a hack to make the context be this window */
  dwr.engine._eval = function(script) {
    if (script == null) {
      return null;
    }
    if (script == "") {
      dwr.engine._debug("Warning: blank script", true);
      return null;
    }
    // dwr.engine._debug("Exec: [" + script + "]", true);
    return eval(script);
  };

  /** @private call all the post hooks for a batch */
  dwr.engine._callPostHooks = function(batch) {
    if (batch.postHooks) {
      for (var i = 0; i < batch.postHooks.length; i++) {
        batch.postHooks[i]();
      }
      batch.postHooks = null;
    }
  };

  /**
   * Generic error handling routing to save having null checks everywhere
   * @private
   * @param {Object} batch
   * @param {Object} ex
   */
  dwr.engine._handleError = function(batch, ex) {
    if (typeof ex == "string") ex = { name:"unknown", message:ex };
    if (ex.message == null) ex.message = "";
    if (ex.name == null) ex.name = "unknown";
    if (batch && typeof batch.errorHandler == "function") batch.errorHandler(ex.message, ex);
    else if (dwr.engine._errorHandler) dwr.engine._errorHandler(ex.message, ex);
    if (batch) dwr.engine.batch.remove(batch);
  };

  /**
   * Generic error handling routing to save having null checks everywhere
   * @private
   * @param {Object} batch
   * @param {Object} ex
   */
  dwr.engine._handleWarning = function(batch, ex) {
    if (typeof ex == "string") ex = { name:"unknown", message:ex };
    if (ex.message == null) ex.message = "";
    if (ex.name == null) ex.name = "unknown";
    if (batch && typeof batch.warningHandler == "function") batch.warningHandler(ex.message, ex);
    else if (dwr.engine._warningHandler) dwr.engine._warningHandler(ex.message, ex);
    if (batch) dwr.engine.batch.remove(batch);
  };

  /**
   * Used internally when some message needs to get to the programmer
   * @private
   * @param {String} message
   * @param {Object} stacktrace
   */
  dwr.engine._debug = function(message, stacktrace) {
    var written = false;
    try {
      if (window.console) {
        if (stacktrace && window.console.trace) window.console.trace();
        window.console.log(message);
        written = true;
      }
      else if (window.opera && window.opera.postError) {
        window.opera.postError(message);
        written = true;
      }
    }
    catch (ex) { /* ignore */ }

    if (!written) {
      var debug = document.getElementById("dwr-debug");
      if (debug) {
        var contents = message + "<br/>" + debug.innerHTML;
        if (contents.length > 2048) contents = contents.substring(0, 2048);
        debug.innerHTML = contents;
      }
    }
  };

  /**
   * Functions called by the server
   */
  dwr.engine.remote = {
    /**
     * Execute a callback
     * @private
     * @param {int} batchId The ID of the batch that we are replying to
     * @param {int} callId The call ID that the script relates to
     * @param {String} reply The script to execute
     */
    handleCallback:function(batchId, callId, reply) {
      var batch = dwr.engine._batches[batchId];
      if (batch == null) {
        dwr.engine._debug("Warning: batch == null in remoteHandleCallback for batchId=" + batchId, true);
        return;
      }
      // Error handlers inside here indicate an error that is nothing to do
      // with DWR so we handle them differently.
      try {
        var handlers = batch.handlers[callId];
        if (!handlers) {
          dwr.engine._debug("Warning: Missing handlers. callId=" + callId, true);
        }
        else {
          batch.handlers[callId] = null;
          if (typeof handlers.callback == "function") {
            handlers.callback.call(handlers.callbackScope, reply, handlers.callbackArgs);
          }
        }
      }
      catch (ex) {
        dwr.engine._handleError(batch, ex);
      }
    },

    /**
     * Called by the server: Handle an exception for a call
     * @private
     * @param {int} batchId The ID of the batch that we are replying to
     * @param {int} callId The call ID that the script relates to
     * @param {String} reply The script to execute
     */
    handleException:function(batchId, callId, ex) {
      var batch = dwr.engine._batches[batchId];
      if (batch == null) {
        dwr.engine._debug("Warning: null batch in remoteHandleException", true);
        return;
      }

      var handlers = batch.handlers[callId];
      batch.handlers[callId] = null;
      if (handlers == null) {
        dwr.engine._debug("Warning: null handlers in remoteHandleException", true);
        return;
      }

      if (ex.message == undefined) {
        ex.message = ""; 
      }

      if (typeof handlers.exceptionHandler == "function") {
        handlers.exceptionHandler.call(handlers.exceptionScope, ex.message, ex, handlers.exceptionArgs);
      }
      else if (typeof batch.errorHandler == "function") {
        batch.errorHandler(ex.message, ex);
      }
    },

    /**
     * Called by the server: The whole batch is broken
     * @private
     * @param {Object} ex The data about what broke
     * @param {int} batchId The ID of the batch that we are replying to
     */
    handleBatchException:function(ex, batchId) {
      var searchBatch = (dwr.engine._receivedBatch == null && batchId != null);
      if (searchBatch) {
        dwr.engine._receivedBatch = dwr.engine._batches[batchId];
      }
      if (ex.message == undefined) ex.message = "";
      dwr.engine._handleError(dwr.engine._receivedBatch, ex);
      if (searchBatch) {
        dwr.engine._receivedBatch = null;
        dwr.engine.batch.remove(dwr.engine._batches[batchId]);
      }
    },

    /**
     * Called by the server when we need to set a new script session id
     * @param {Object} newSessionId The new script session id to be used from now
     */
    handleNewScriptSession:function(newSessionId) {
      if (dwr.engine._scriptSessionId != null) {
        dwr.engine._debug("Server side script session id timed out. New session automatically created");
      }
      dwr.engine._scriptSessionId = newSessionId;
    },

    /**
     * Called by the server: Reverse ajax should not be used
     * @private
     * @param {Object} ex
     * @param {int} batchId
     */
    pollCometDisabled:function(ex, batchId){
      dwr.engine.setActiveReverseAjax(false);
      var searchBatch = (dwr.engine._receivedBatch == null && batchId != null);
      if (searchBatch) {
        dwr.engine._receivedBatch = dwr.engine._batches[batchId];
      }
      if (ex.message == undefined) {
        ex.message = "";
      }
      dwr.engine._handleError(dwr.engine._receivedBatch, ex);
      if (searchBatch) {
        dwr.engine._receivedBatch = null;
        dwr.engine.batch.remove(dwr.engine._batches[batchId]);
      }
    }
  };

  /**
   * Functions to serialize a data set into a list of parameters
   */
  dwr.engine.serialize = {
    /**
     * ActiveX objects to use when we want to convert an xml string into a DOM object
     */
    domDocument:[
      "Msxml2.DOMDocument.6.0",
      "Msxml2.DOMDocument.5.0",
      "Msxml2.DOMDocument.4.0",
      "Msxml2.DOMDocument.3.0",
      "MSXML2.DOMDocument",
      "MSXML.DOMDocument",
      "Microsoft.XMLDOM"
    ],

    /**
     * Convert a text representation of XML into a DOM tree
     * @param {String} xml An xml string
     */
    toDom:function(xml) {
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
        dom = dwr.engine.util.newActiveXObject(dwr.engine.serialize.domDocument);
        dom.loadXML(xml); // What happens on parse fail with IE?
        return dom;
      }
      else {
        var div = document.createElement("div");
        div.innerHTML = xml;
        return div;
      }
    },

    /**
     * Marshall a data item
     * @private
     * @param batch A map of variables to how they have been marshalled
     * @param referto An array of already marshalled variables to prevent recurrsion
     * @param data The data to be marshalled
     * @param name The name of the data being marshalled
     */
    convert:function(batch, referto, data, name) {
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
        var ref = dwr.engine.serialize.lookup(referto, data, name);
        if (ref) {
          batch.map[name] = ref;
        }
        else if (data instanceof String) batch.map[name] = "String:" + encodeURIComponent(data);
        else if (data instanceof Boolean) batch.map[name] = "Boolean:" + data;
        else if (data instanceof Number) batch.map[name] = "Number:" + data;
        else if (data instanceof Date) batch.map[name] = "Date:" + data.getTime();
        else if (data && data.join) batch.map[name] = dwr.engine.serialize.convertArray(batch, referto, data, name);
        else if (data && data.tagName && data.tagName.toLowerCase() == "input" && data.type && data.type.toLowerCase() == "file") {
          batch.fileUpload = true;
          batch.map[name] = data;
        }
        else {
          // This check for an HTML is not complete, but is there a better way?
          // Maybe we should add: data.hasChildNodes typeof "function" == true
          if (data.nodeName && data.nodeType) {
            batch.map[name] = dwr.engine.serialize.convertXml(batch, referto, data, name);
          }
          else {
            batch.map[name] = dwr.engine.serialize.convertObject(batch, referto, data, name);
          }
        }
        break;
      case "function":
        // We just ignore functions.
        break;
      default:
        dwr.engine._handleWarning(null, { name:"dwr.engine.unexpectedType", message:"Unexpected type: " + typeof data + ", attempting default converter." });
        batch.map[name] = "default:" + data;
        break;
      }
    },

    /**
     * Marshall an array
     * @private
     * @see dwr.engine.serialize.convert() for parameter details
     */
    convertArray:function(batch, referto, data, name) {
      var reply = "Array:[";
      for (var i = 0; i < data.length; i++) {
        if (i != 0) reply += ",";
        batch.paramCount++;
        var childName = "c" + dwr.engine._batch.map.callCount + "-e" + batch.paramCount;
        dwr.engine.serialize.convert(batch, referto, data[i], childName);
        reply += "reference:";
        reply += childName;
      }
      reply += "]";

      return reply;
    },

    /**
     * Marshall an object
     * @private
     * @see dwr.engine.serialize.convert() for parameter details
     */
    convertObject:function(batch, referto, data, name) {
      // treat objects as an associative arrays
      var reply = "Object_" + dwr.engine.serialize.getObjectClassName(data) + ":{";
      var element;
      for (element in data) {
        if (typeof data[element] != "function") {
          batch.paramCount++;
          var childName = "c" + dwr.engine._batch.map.callCount + "-e" + batch.paramCount;
          dwr.engine.serialize.convert(batch, referto, data[element], childName);
          reply += encodeURIComponent(element) + ":reference:" + childName + ", ";
        }
      }

      if (reply.substring(reply.length - 2) == ", ") {
        reply = reply.substring(0, reply.length - 2);
      }
      reply += "}";

      return reply;
    },

    /**
     * Marshall an object
     * @private
     * @see dwr.engine.serialize.convert() for parameter details
     */
    convertXml:function(batch, referto, data, name) {
      var output;
      if (window.XMLSerializer) output = new XMLSerializer().serializeToString(data);
      else if (data.toXml) output = data.toXml;
      else output = data.innerHTML;

      return "XML:" + encodeURIComponent(output);
    },

    /**
     * Have we already converted this object?
     * @private
     * @see dwr.engine.serialize.convert() for parameter details
     */
    lookup:function(referto, data, name) {
      var lookup;
      // Can't use a map: getahead.org/ajax/javascript-gotchas
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
    },

    /**
     * The names of classes that need special treatment
     */
    errorClasses:{
      "Error":Error,
      "EvalError":EvalError,
      "RangeError":RangeError,
      "ReferenceError":ReferenceError,
      "SyntaxError":SyntaxError,
      "TypeError":TypeError,
      "URIError":URIError
    },

    /**
     * Returns the classname of supplied argument obj. Similar to typeof, but
     * which returns the name of the constructor that created the object rather
     * than 'object'
     * @private
     * @param {Object} obj The object to detect the type of
     * @return The name of the object
     */
    getObjectClassName:function(obj) {
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
        for (var errorname in dwr.engine.serialize.errorClasses) {
          if (obj.constructor == dwr.engine.serialize.errorClasses[errorname]) return errorname;
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
    }
  };

  /**
   * Functions to handle the various remoting transport
   */
  dwr.engine.transport = {
    /**
     * Actually send the block of data in the batch object.
     * @private
     * @param {Object} batch
     */
    send:function(batch) {
      dwr.engine.batch.prepareToSend(batch);

      if (batch.fileUpload) {
        batch.transport = dwr.engine.transport.iframe;
      }
      else if (dwr.engine.isCrossDomain) {
        batch.transport = dwr.engine.transport.scriptTag;
      }
      // else if (batch.isPoll && dwr.engine.isIE) {
      //   batch.transport = dwr.engine.transport.htmlfile;
      // }
      else {
        batch.transport = dwr.engine.transport.xhr;
      }

      batch.transport.send(batch);
    },

    /**
     * A generic function to remove all remoting artifacts
     * @param {Object} batch The batch that has completed
     */
    remove:function(batch) {
      dwr.engine.transport.iframe.remove(batch);
      dwr.engine.transport.xhr.remove(batch);
    },

    /**
     * Called as a result of a request timeout
     * @private
     * @param {Object} batch The batch that is aborting
     */
    abort:function(batch) {
      if (batch && !batch.completed) {
        clearInterval(batch.interval);
        dwr.engine.batch.remove(batch);

        if (batch.req) {
          batch.req.abort();
        }

        dwr.engine.transport.remove(batch);
        dwr.engine._handleError(batch, { name:"dwr.engine.timeout", message:"Timeout" });
      }
    },

    /**
     * Remoting through XHR
     */
    xhr:{
      /**
       * The default HTTP method to use
       */
      httpMethod:"POST",

      /**
       * The ActiveX objects to use when we want to do an XMLHttpRequest call.
       * TODO: We arrived at this by trial and error. Other toolkits use
       * different strings, maybe there is an officially correct version?
       */
      XMLHTTP:["Msxml2.XMLHTTP.6.0", "Msxml2.XMLHTTP.5.0", "Msxml2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP", "Microsoft.XMLHTTP"],

      /**
       * Setup a batch for transfer through XHR
       * @param {Object} batch The batch to alter for XHR transmit
       */
      send:function(batch) {

        if (batch.isPoll) {
          batch.map.partialResponse = dwr.engine._partialResponseYes;
        }

        // Do proxies or IE force us to use early closing mode?
        if (batch.isPoll && dwr.engine._pollWithXhr == "true") {
          batch.map.partialResponse = dwr.engine._partialResponseNo;
        }
        if (batch.isPoll && dwr.engine.isIE) {
          batch.map.partialResponse = dwr.engine._partialResponseNo;
        }

        if (window.XMLHttpRequest) {
          batch.req = new XMLHttpRequest();
        }
        else if (window.ActiveXObject) {
          batch.req = dwr.engine.util.newActiveXObject(dwr.engine.transport.xhr.XMLHTTP);
        }

        // Proceed using XMLHttpRequest
        if (batch.async) {
          batch.req.onreadystatechange = function() {
            if (typeof dwr != 'undefined') dwr.engine.transport.xhr.stateChange(batch);
          };
        }
        // If we're polling, record this for monitoring
        if (batch.isPoll) {
          dwr.engine._pollReq = batch.req;
          // In IE XHR is an ActiveX control so you can't augment it like this
          if (!document.all) batch.req.batch = batch;
        }

        httpMethod = dwr.engine.transport.xhr.httpMethod;
        // Workaround for Safari 1.x POST bug
        var indexSafari = navigator.userAgent.indexOf("Safari/");
        if (indexSafari >= 0) {
          var version = navigator.userAgent.substring(indexSafari + 7);
          if (parseInt(version, 10) < 400) {
            if (dwr.engine._allowGetForSafariButMakeForgeryEasier == "true") {
              httpMethod = "GET";
            }
            else {
              dwr.engine._handleWarning(batch, {
                name: "dwr.engine.oldSafari",
                message: "Safari GET support disabled. See getahead.org/dwr/server/servlet and allowGetForSafariButMakeForgeryEasier."
              });
            }
          }
        }

        batch.mode = batch.isPoll ? dwr.engine._ModePlainPoll : dwr.engine._ModePlainCall;
        var request = dwr.engine.batch.constructRequest(batch, httpMethod);
        try {
          batch.req.open(httpMethod, request.url, batch.async);
          try {
            for (var prop in batch.headers) {
              var value = batch.headers[prop];
              if (typeof value == "string") batch.req.setRequestHeader(prop, value);
            }
            if (!batch.headers["Content-Type"]) batch.req.setRequestHeader("Content-Type", "text/plain");
          }
          catch (ex) {
            dwr.engine._handleWarning(batch, ex);
          }
          batch.req.send(request.body);
          if (!batch.async) dwr.engine.transport.xhr.stateChange(batch);
        }
        catch (ex) {
          dwr.engine._handleError(batch, ex);
        }

        if (batch.isPoll && batch.map.partialResponse == dwr.engine._partialResponseYes) {
          dwr.engine.transport.xhr.checkCometPoll();
        }
      },

      /**
       * Called by XMLHttpRequest to indicate that something has happened
       * @private
       * @param {Object} batch The current remote operation
       */
      stateChange:function(batch) {
        var toEval;

        if (batch.completed) {
          dwr.engine._debug("Error: _stateChange() with batch.completed");
          return;
        }

        var req = batch.req;
        try {
          if (req.readyState != 4) return;
        }
        catch (ex) {
          dwr.engine._handleWarning(batch, ex);
          // It's broken - clear up and forget this call
          dwr.engine.batch.remove(batch);
          return;
        }

        try {
          var reply = req.responseText;
          reply = dwr.engine._replyRewriteHandler(reply);
          var status = req.status; // causes Mozilla to except on page moves

          if (reply == null || reply == "") {
            dwr.engine._handleWarning(batch, { name:"dwr.engine.missingData", message:"No data received from server" });
          }
          else if (status != 200) {
            dwr.engine._handleError(batch, { name:"dwr.engine.http." + status, message:req.statusText });
          }
          else {
            var contentType = req.getResponseHeader("Content-Type");
            if (!contentType.match(/^text\/plain/) && !contentType.match(/^text\/javascript/)) {
              if (contentType.match(/^text\/html/) && typeof batch.textHtmlHandler == "function") {
                batch.textHtmlHandler({ status:status, responseText:reply, contentType:contentType });
              }
              else {
                dwr.engine._handleWarning(batch, { name:"dwr.engine.invalidMimeType", message:"Invalid content type: '" + contentType + "'" });
              }
            }
            else {
              // Comet replies might have already partially executed
             if (batch.isPoll && batch.map.partialResponse == dwr.engine._partialResponseYes) {
                dwr.engine.transport.xhr.processCometResponse(reply, batch);
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

        dwr.engine._callPostHooks(batch);

        // Outside of the try/catch so errors propagate normally:
        dwr.engine._receivedBatch = batch;
        if (toEval != null) toEval = toEval.replace(dwr.engine._scriptTagProtection, "");
        dwr.engine._eval(toEval);
        dwr.engine._receivedBatch = null;
        dwr.engine.batch.validate(batch);
        dwr.engine.batch.remove(batch);
      },

      /**
       * Check for reverse Ajax activity
       * @private
       */
      checkCometPoll:function() {
        if (dwr.engine._pollReq) {
          var req = dwr.engine._pollReq;
          var text = req.responseText;
          if (text != null) {
              dwr.engine.transport.xhr.processCometResponse(text, req.batch);
          }
        }

        // If the poll resources are still there, come back again
        if (dwr.engine._pollReq) {
          setTimeout("dwr.engine.transport.xhr.checkCometPoll()", dwr.engine._pollCometInterval);
        }
      },

      /**
       * Some more text might have come in, test and execute the new stuff.
       * This method could also be called by the iframe transport
       * @private
       * @param {Object} response from xhr.responseText
       * @param {Object} batch The batch that the XHR object pertains to
       */
      processCometResponse:function(response, batch) {
        if (batch.charsProcessed == response.length) return;
        if (response.length == 0) {
          batch.charsProcessed = 0;
          return;
        }

        var firstStartTag = response.indexOf("//#DWR-START#", batch.charsProcessed);
        if (firstStartTag == -1) {
          // dwr.engine._debug("No start tag (search from " + batch.charsProcessed + "). skipping '" + response.substring(batch.charsProcessed) + "'");
          batch.charsProcessed = response.length;
          return;
        }
        // if (firstStartTag > 0) {
        //   dwr.engine._debug("Start tag not at start (search from " + batch.charsProcessed + "). skipping '" + response.substring(batch.charsProcessed, firstStartTag) + "'");
        // }

        var lastEndTag = response.lastIndexOf("//#DWR-END#");
        if (lastEndTag == -1) {
          // dwr.engine._debug("No end tag. unchanged charsProcessed=" + batch.charsProcessed);
          return;
        }

        // Skip the end tag too for next time, remembering CR and LF
        if (response.charCodeAt(lastEndTag + 11) == 13 && response.charCodeAt(lastEndTag + 12) == 10) {
         batch.charsProcessed = lastEndTag + 13;
        }
        else {
          batch.charsProcessed = lastEndTag + 11;
        }

        var exec = response.substring(firstStartTag + 13, lastEndTag);

        try {
          dwr.engine._receivedBatch = batch;
          dwr.engine._eval(exec);
          dwr.engine._receivedBatch = null;
        }
        catch (ex) {
          dwr.engine._handleError(batch, ex);
        }
      },

      /**
       * Tidy-up when an XHR call is done
       * @param {Object} batch
       */
      remove:function(batch) {
        // XHR tidyup: avoid IE handles increase
        if (batch.req) {
          // If this is a poll frame then stop comet polling
          if (batch.req == dwr.engine._pollReq) dwr.engine._pollReq = null;
          delete batch.req;
        }
      }
    },

    /**
     * Functions for remoting through IFrame
     */
    iframe:{
      /**
       * Setup a batch for transfer through IFrame
       * @param {Object} batch The batch to alter for IFrame transmit
       */
      send:function(batch) {
        if (batch.fileUpload) {
          batch.httpMethod = "POST";
          batch.encType = "multipart/form-data";
        }
        var idname = dwr.engine.transport.iframe.getId(batch);
        batch.div = document.createElement("div");
        // Add the div to the document first, otherwise IE 6 will ignore onload handler.
        document.body.appendChild(batch.div);
        batch.div.innerHTML = "<iframe src='javascript:void(0)' frameborder='0' style='width:0px;height:0px;border:0;' id='" + idname + "' name='" + idname + "' onload='dwr.engine.transport.iframe.loadingComplete(" + batch.map.batchId + ");'></iframe>";
        batch.document = document;
        dwr.engine.transport.iframe.beginLoader(batch, idname);
      },

      /**
       * Create a unique ID so multiple iframes can fire at the same time
       * @param {Object} batch A source of a unique number for the batch
       * @return {String} a name prefix for created elements
       */
      getId:function(batch) {
        return batch.isPoll ? "dwr-if-poll-" + batch.map.batchId : "dwr-if-" + batch.map["c0-id"];
      },

      /**
       * Setup a form or construct a src attribute to use the iframe.
       * This is abstracted from send() because the same logic will do for htmlfile
       * @param {Object} batch
       */
      beginLoader:function(batch, idname) {
        batch.iframe = batch.document.getElementById(idname);
        batch.iframe.batch = batch;
        batch.mode = batch.isPoll ? dwr.engine._ModeHtmlPoll : dwr.engine._ModeHtmlCall;
        if (batch.isPoll) dwr.engine._outstandingIFrames.push(batch.iframe);
        var request = dwr.engine.batch.constructRequest(batch, batch.httpMethod);
        if (batch.httpMethod == "GET") {
          batch.iframe.setAttribute("src", request.url);
        }
        else {
          // setting enctype via the DOM does not work in IE, create the form using innerHTML instead
          var formHtml = "<form id='dwr-form' action='" + request.url + "' target='" + idname + "' style='display:none;' method='" + batch.httpMethod + "'";
          if (batch.encType) formHtml += " enctype='" + batch.encType + "'";
          formHtml += "></form>";
          var div = batch.document.createElement("div");
          div.innerHTML = formHtml;
          batch.form = div.firstChild;
          for (var prop in batch.map) {
            var value = batch.map[prop];
            if (typeof value != "function") {
              if (value.tagName && value.tagName.toLowerCase() == "input" && value.type && value.type.toLowerCase() == "file") {
                // Since we can not set the value of a file object, we must post the actual file object
                // that the user clicked browse on. We will put a clone in it's place.
                var clone = value.cloneNode(true);
                value.removeAttribute("id", prop);
                value.setAttribute("name", prop);
                value.parentNode.insertBefore(clone, value);
                value.parentNode.removeChild(value);
                batch.form.appendChild(value);
              } else {
                var formInput = batch.document.createElement("input");
                formInput.setAttribute("type", "hidden");
                formInput.setAttribute("name", prop);
                formInput.setAttribute("value", value);
                batch.form.appendChild(formInput);
              }
            }
          }
          batch.document.body.appendChild(batch.form);
          batch.form.submit();
        }
      },

      /**
       * Called from iframe onload, check batch using batch-id
       * @private
       * @param {int} batchId The id of the batch that has loaded
       */
      loadingComplete:function(batchId) {
        var batch = dwr.engine._batches[batchId];
        if (batch) dwr.engine.batch.validate(batch);
      },

      /**
       * Functions designed to be called by the server
       */
      remote:{
        /**
         * Called by the server: An IFrame reply is about to start
         * @private
         * @param {Object} iframe
         * @param {int} batchId
         */
        beginIFrameResponse:function(iframe, batchId) {
          if (iframe != null) dwr.engine._receivedBatch = iframe.batch;
          dwr.engine._callPostHooks(dwr.engine._receivedBatch);
        },

        /**
         * Called by the server: An IFrame reply is just completing
         * @private
         * @param {int} batchId
         */
        endIFrameResponse:function(batchId) {
          dwr.engine.batch.remove(dwr.engine._receivedBatch);
          dwr.engine._receivedBatch = null;
        }
      },

      remove:function(batch) {
        // TODO: make it so that we don't need these if statements
        if (batch.div) {
            batch.div.parentNode.removeChild(batch.div);
        }
        if (batch.iframe) {
          batch.iframe.parentNode.removeChild(batch.iframe);
        }
        if (batch.form) {
          batch.form.parentNode.removeChild(batch.form);
        }
      }

      /*
      // If we have an iframe comet solution where we need to read data streamed
      // into an iframe then we need code like this to slurp the data out.
      // Compare this with xhr.checkCometPoll()
      outstandingIFrames:[],

      checkCometPoll:function() {
        for (var i = 0; i < dwr.engine.transport.iframe.outstandingIFrames.length; i++) {
          var text = "";
          var iframe = dwr.engine.transport.iframe.outstandingIFrames[i];
          try {
            text = dwr.engine.transport.iframe.getTextFromCometIFrame(iframe);
          }
          catch (ex) {
            dwr.engine._handleWarning(iframe.batch, ex);
          }
          if (text != "") dwr.engine.transport.xhr.processCometResponse(text, iframe.batch);
        }

        if (dwr.engine.transport.iframe.outstandingIFrames.length > 0) {
          setTimeout("dwr.engine.transport.iframe.checkCometPoll()", dwr.engine._pollCometInterval);
        }
      }

      // We probably also need to update dwr.engine.remote.beginIFrameResponse()
      // to call checkCometPoll.

      // Extract the whole (executed and all) text from the current iframe
      getTextFromCometIFrame:function(frameEle) {
        var body = frameEle.contentWindow.document.body;
        if (body == null) return "";
        var text = body.innerHTML;
        // We need to prevent IE from stripping line feeds
        if (text.indexOf("<PRE>") == 0 || text.indexOf("<pre>") == 0) {
          text = text.substring(5, text.length - 7);
        }
        return text;
      };

      // And an addition to iframe.remove():
      {
        if (batch.iframe) {
          // If this is a poll frame then stop comet polling
          for (var i = 0; i < dwr.engine.transport.iframe.outstandingIFrames.length; i++) {
            if (dwr.engine.transport.iframe.outstandingIFrames[i] == batch.iframe) {
              dwr.engine.transport.iframe.outstandingIFrames.splice(i, 1);
            }
          }
        }
      }
      */
    },

    /**
     * Functions for remoting through Script Tags
     */
    scriptTag:{
      /**
       * Setup a batch for transfer through a script tag
       * @param {Object} batch The batch to alter for script tag transmit
       */
      send:function(batch) {
        batch.mode = batch.isPoll ? dwr.engine._ModePlainPoll : dwr.engine._ModePlainCall;
        var request = dwr.engine.batch.constructRequest(batch, "GET");
        batch.script = document.createElement("script");
        batch.script.id = "dwr-st-" + batch.map["c0-id"];
        batch.script.src = request.url;
        document.body.appendChild(batch.script);
      }
    },

    /**
     * Remoting through IE's htmlfile ActiveX control
     */
    htmlfile:{
      /**
       * Setup a batch for transfer through htmlfile
       * @param {Object} batch The batch to alter for htmlfile transmit
       */
      send:function(batch) {
        var idname = dwr.engine.transport.iframe.getId(batch);
        batch.htmlfile = new window.ActiveXObject("htmlfile");
        batch.htmlfile.open();
        batch.htmlfile.write("<" + "html>");
        //batch.htmlfile.write("<script>document.domain='" + document.domain + "';</script>");
        batch.htmlfile.write("<div><iframe className='wibble' src='javascript:void(0)' id='" + idname + "' name='" + idname + "' onload='dwr.engine.transport.iframe.loadingComplete(" + batch.map.batchId + ");'></iframe></div>");
        batch.htmlfile.write("</" + "html>");
        batch.htmlfile.close();
        batch.htmlfile.parentWindow.dwr = dwr;
        batch.document = batch.htmlfile;

        dwr.engine.transport.iframe.beginLoader(batch, idname);
      }
    }
  };

  /**
   * Functions to manipulate batches
   * @private
   */
  dwr.engine.batch = {
    /**
     * Generate a new standard batch
     * @private
     */
    create:function() {
      var batch = {
        map:{ callCount:0 },
        charsProcessed:0,
        paramCount:0,
        parameters:{},
        headers:{},
        isPoll:false,
        handlers:{},
        preHooks:[],
        postHooks:[],
        async:dwr.engine._async,
        timeout:dwr.engine._timeout,
        errorHandler:dwr.engine._errorHandler,
        warningHandler:dwr.engine._warningHandler,
        textHtmlHandler:dwr.engine._textHtmlHandler
      };

      if (dwr.engine._preHook) {
        batch.preHooks.push(dwr.engine._preHook);
      }
      if (dwr.engine._postHook) {
        batch.postHooks.push(dwr.engine._postHook);
      }

      dwr.engine.batch.populateHeadersAndParameters(batch);
      return batch;
    },

    /**
     * Generate a new batch for polling
     * @private
     * @see dwr.engine.batch.create()
     */
    createPoll:function() {
      var batch = {
        map:{ callCount:1, id:0 /* TODO: do we need id? */ },
        async:true,
        charsProcessed:0,
        paramCount:0,
        parameters:{},
        headers:{},
        isPoll:true,
        handlers:[{
          callback:function(pause) {
            dwr.engine._pollRetries = 0;
            setTimeout("dwr.engine._poll()", pause);
          }
        }],
        preHooks:[],
        postHooks:[],
        timeout:0,
        path:dwr.engine._defaultPath,
        errorHandler:dwr.engine._pollErrorHandler,
        warningHandler:dwr.engine._pollErrorHandler,
        textHtmlHandler:dwr.engine._textHtmlHandler
      };

      dwr.engine.batch.populateHeadersAndParameters(batch);
      return batch;
    },

    /**
     * Copy the global headers and parameters into this batch object
     * @private
     * @param {Object} batch The destination
     */
    populateHeadersAndParameters:function(batch) {
      var propname, data;
      if (dwr.engine._headers) {
        for (propname in dwr.engine._headers) {
          data = dwr.engine._headers[propname];
          if (typeof data != "function") batch.headers[propname] = data;
        }
      }
      if (dwr.engine._parameters) {
        for (propname in dwr.engine._parameters) {
          data = dwr.engine._parameters[propname];
          if (typeof data != "function") batch.parameters[propname] = data;
        }
      }
    },

    /**
     * Augment this batch with a new call
     * @private
     * @param {Object} batch
     * @param {String} scriptName
     * @param {String} methodName
     * @param {Object} args
     */
    addCall:function(batch, scriptName, methodName, args) {
      // From the other params, work out which is the function (or object with
      // call meta-data) and which is the call parameters
      var callData;
      var lastArg = args[args.length - 1];
      if (typeof lastArg == "function" || lastArg == null) callData = { callback:args.pop() };
      else callData = args.pop();

      // Merge from the callData into the batch
      dwr.engine.batch.merge(batch, callData);
      batch.handlers[batch.map.callCount] = {
        exceptionHandler:callData.exceptionHandler,
        exceptionArgs:callData.exceptionArgs || callData.args || null,
        exceptionScope:callData.exceptionScope || callData.scope || window,
        callback:callData.callbackHandler || callData.callback,
        callbackArgs:callData.callbackArgs || callData.args || null,      
        callbackScope:callData.callbackScope || callData.scope || window          
      };

      // Copy to the map the things that need serializing
      var prefix = "c" + batch.map.callCount + "-";
      batch.map[prefix + "scriptName"] = scriptName;
      batch.map[prefix + "methodName"] = methodName;
      batch.map[prefix + "id"] = batch.map.callCount;
      var converted = [];
      for (i = 0; i < args.length; i++) {
        dwr.engine.serialize.convert(batch, converted, args[i], prefix + "param" + i);
      }
    },

    /**
     * Take further options and merge them into a batch
     * @private
     * @param {Object} batch The batch that we are altering
     * @param {Object} overrides The object containing properties to copy into batch
     */
    merge:function(batch, overrides) {
      var propname, data;
      for (var i = 0; i < dwr.engine._propnames.length; i++) {
        propname = dwr.engine._propnames[i];
        if (overrides[propname] != null) batch[propname] = overrides[propname];
      }
      if (overrides.preHook != null) batch.preHooks.unshift(overrides.preHook);
      if (overrides.postHook != null) batch.postHooks.push(overrides.postHook);
      if (overrides.headers) {
        for (propname in overrides.headers) {
          data = overrides.headers[propname];
          if (typeof data != "function") batch.headers[propname] = data;
        }
      }
      if (overrides.parameters) {
        for (propname in overrides.parameters) {
          data = overrides.parameters[propname];
          if (typeof data != "function") batch.map["p-" + propname] = "" + data;
        }
      }
    },

    /**
     * Executed just before a transport sends the batch
     * @private
     * @param {Object} batch The batch to prepare for sending
     */
    prepareToSend:function(batch) {
      batch.map.batchId = dwr.engine._nextBatchId;
      dwr.engine._nextBatchId++;
      dwr.engine._batches[batch.map.batchId] = batch;
      dwr.engine._batchesLength++;
      batch.completed = false;

      // security details are filled in late so previous batches have completed
      batch.map.page = window.location.pathname + window.location.search;
      batch.map.httpSessionId = dwr.engine._getHttpSessionId();
      batch.map.scriptSessionId = dwr.engine._scriptSessionId;

      for (var i = 0; i < batch.preHooks.length; i++) {
        batch.preHooks[i]();
      }
      batch.preHooks = null;
      // Set a timeout
      if (batch.timeout && batch.timeout != 0) {
        batch.interval = setInterval(function() { dwr.engine.transport.abort(batch); }, batch.timeout);
      }
    },

    /**
     * Work out what the URL should look like
     * @private
     * @param {Object} batch the data that we are sending
     * @param {Object} httpMethod Are we using GET/POST etc?
     */
    constructRequest:function(batch, httpMethod) {
      // A quick string to help people that use web log analysers
      var urlBuffer = [];
      urlBuffer.push(batch.path);
      urlBuffer.push(batch.mode);
      if (batch.isPoll == true) {
        urlBuffer.push("ReverseAjax.dwr");
      }
      else if (batch.map.callCount == 1) {
        urlBuffer.push(batch.map["c0-scriptName"]);
        urlBuffer.push(".");
        urlBuffer.push(batch.map["c0-methodName"]);
        urlBuffer.push(".dwr");
      }
      else {
        urlBuffer.push("Multiple.");
        urlBuffer.push(batch.map.callCount);
        urlBuffer.push(".dwr");
      }
      // Play nice with url re-writing
      var sessionMatch = location.href.match(/jsessionid=([^?]+)/);
      if (sessionMatch != null) {
        urlBuffer.push(";jsessionid=");
        urlBuffer.push(sessionMatch[1]);
      }

      var request = {};
      var prop;
      if (httpMethod == "GET") {
        // Some browsers (Opera/Safari2) seem to fail to convert the callCount value
        // to a string in the loop below so we do it manually here.
        batch.map.callCount = "" + batch.map.callCount;
        urlBuffer.push("?");
        for (prop in batch.map) {
          if (typeof batch.map[prop] != "function") {
            urlBuffer.push(encodeURIComponent(prop));
            urlBuffer.push("=");
            urlBuffer.push(encodeURIComponent(batch.map[prop]));
            urlBuffer.push("&");
          }
        }
        urlBuffer.pop(); // remove the trailing &
        request.body = null;
      }
      else {
        // PERFORMANCE: for iframe mode this is thrown away.
        var bodyBuffer = [];
        for (prop in batch.map) {
          if (typeof batch.map[prop] != "function") {
            bodyBuffer.push(prop);
            bodyBuffer.push("=");
            bodyBuffer.push(batch.map[prop]);
            bodyBuffer.push(dwr.engine._postSeperator);
          }
        }
        request.body = dwr.engine._contentRewriteHandler(bodyBuffer.join(""));
      }
      request.url = dwr.engine._urlRewriteHandler(urlBuffer.join(""));
      return request;
    },

    /**
     * @private This function is invoked when a batch reply is received.
     * It checks that there is a response for every call in the batch. Otherwise,
     * an error will be signaled (a call without a response indicates that the
     * server failed to send complete batch response).
     */
    validate:function(batch) {
      // If some call left unreplied, report an error.
      if (!batch.completed) {
        for (var i = 0; i < batch.map.callCount; i++) {
          if (batch.handlers[i] != null) {
            dwr.engine._handleWarning(batch, { name:"dwr.engine.incompleteReply", message:"Incomplete reply from server" });
            break;
          }
        }
      }
    },

    /**
     * A call has finished by whatever means and we need to shut it all down.
     * @private
     * @param {Object} batch The batch that we are altering
     */
    remove:function(batch) {
      if (!batch) {
        dwr.engine._debug("Warning: null batch in dwr.engine.batch.remove()", true);
        return;
      }

      if (batch.completed == "true") {
        dwr.engine._debug("Warning: Double complete", true);
        return;
      }
      batch.completed = true;

      // Transport tidyup
      dwr.engine.transport.remove(batch);

      // TODO: co-locate all the functions that work on a set of batches
      if (batch.map && (batch.map.batchId || batch.map.batchId == 0)) {
        delete dwr.engine._batches[batch.map.batchId];
        dwr.engine._batchesLength--;
      }

      // If there is anything on the queue waiting to go out, then send it.
      // We don't need to check for ordered mode, here because when ordered mode
      // gets turned off, we still process *waiting* batches in an ordered way.
      if (dwr.engine._batchQueue.length != 0) {
        var sendbatch = dwr.engine._batchQueue.shift();
        dwr.engine.transport.send(sendbatch);
      }
    }
  };

  /**
   * Various utility functions
   * @private
   */
  dwr.engine.util = {
    /**
     * Create one of a number of ActiveX strings
     * @param {Object} axarray An array of strings to attempt to create ActiveX objects from
     */
    newActiveXObject:function(axarray) {
      var returnValue;
      for (var i = 0; i < axarray.length; i++) {
        try {
          returnValue = new ActiveXObject(axarray[i]);
          break;
        }
        catch (ex) { /* ignore */ }
      }
      return returnValue;
    }
  };

  /**
    * Work out what type of browser we are working on
    */
  var userAgent = navigator.userAgent;
  var versionString = navigator.appVersion;
  var version = parseFloat(versionString);

  dwr.engine.isOpera = (userAgent.indexOf("Opera") >= 0) ? version : 0;
  dwr.engine.isKhtml = (versionString.indexOf("Konqueror") >= 0) || (versionString.indexOf("Safari") >= 0) ? version : 0;
  dwr.engine.isSafari = (versionString.indexOf("Safari") >= 0) ? version : 0;

  var geckoPos = userAgent.indexOf("Gecko");
  dwr.engine.isMozilla = ((geckoPos >= 0) && (!dwr.engine.isKhtml)) ? version : 0;

  dwr.engine.isFF = 0;
  dwr.engine.isIE = 0;
  try {
    if (dwr.engine.isMozilla) {
      dwr.engine.isFF = parseFloat(userAgent.split("Firefox/")[1].split(" ")[0]);
    }
    if ((document.all) && (!dwr.engine.isOpera)) {
      dwr.engine.isIE = parseFloat(versionString.split("MSIE ")[1].split(";")[0]);
    }
  }
  catch(ex) { }

  // Fetch the scriptSessionId from the server
  dwr.engine._execute(dwr.engine._defaultPath, '__System', 'pageLoaded', function() {
    dwr.engine._ordered = false;
  });

  /**
   * Routines for the DWR pubsub hub
   */
  dwr.hub = {
    /**
      * Publish some data to a given topic
      * @param {Object} topicName The topic to publish to
      * @param {Object} data The data to publish
      */
    publish:function(topicName, data) {
      dwr.engine._execute(dwr.engine._defaultPath, '__System', 'publish', topicName, data, {});
    },

    /**
      * Subscribe to get notifications of publish events to a given topic
      * @param {String} topicName The topic to subscribe to
      * @param {Function} callback The function to call when a publish happens
      * @param {Object} scope The 'this' object on which the callback executes (optional)
      * @param {Object} subscriberData Data that the subscriber wishes to remember (optional)
      * @return An opaque type for use with unsubscribe
      */
    subscribe:function(topicName, callback, scope, subscriberData) {
      var subscription = "" + dwr.hub._subscriptionId++;
      dwr.hub._subscriptions[subscription] = {
        callback:callback,
        scope:scope,
        subscriberData:subscriberData
      };
      dwr.engine._execute(dwr.engine._defaultPath, '__System', 'subscribe', topicName, subscription, {});
      return subscription;
    },

    /**
      * Called by the server: A publish event has happened that we care about
      * @private
      * @param {Object} subscriptionId
      * @param {Object} publishData
      */
    _remotePublish:function(subscriptionId, publishData) {
      var subscriptionData = dwr.hub._subscriptions[subscriptionId];
      if (!subscriptionData) return;
      subscriptionData.callback.call(subscriptionData.scope, publishData, subscriptionData.subscriberData);
    },

    /**
      * Subscribe to get notifications of publish events to a given topic
      * @param {String} topicName The topic to subscribe to
      * @param {Function} callback The function to call when a publish happens
      * @param {Object} scope The 'this' object on which the callback executes (optional)
      * @param {Object} subscriberData Data that the subscriber wishes to remember (optional)
      * @return An opaque type for use with unsubscribe
      */
    subscribe:function(topicName, callback, scope, subscriberData) {
      var subscription = "" + dwr.hub._subscriptionId++;
      dwr.hub._subscriptions[subscription] = {
        callback:callback,
        scope:scope,
        subscriberData:subscriberData
      };
      dwr.engine._execute(dwr.engine._defaultPath, '__System', 'subscribe', topicName, subscription, {});
      return subscription;
    },

    /**
      * Each time we subscribe to something, we use a unique number
      */
    _subscriptionId:0,

    /**
      * We need to remember what we are subscribed to so we can recall the callback
      */
    _subscriptions:{}
  };
})();
