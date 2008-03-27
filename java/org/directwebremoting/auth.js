/*
 * Copyright 2005 Andreas Schmidt
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
if (dwr.auth == null) dwr.auth = {};
if (DWRAuthentication == null) var DWRAuthentication = dwr.auth;

//
// Application-wide stuff
//

// enabled-status flag
dwr.auth._enabled = false;

// stores the original MetaDataWarningHandler of DWREnging
dwr.auth._dwrHandleBatchExeption = null;

// give dwr.auth the control
dwr.auth.enable = function () {
  if (dwr.auth._enabled) {
    alert("dwr.auth already enabled");
    return;
  }
  dwr.auth._enabled = true;
  dwr.auth._dwrHandleBatchExeption = dwr.engine._handleError;
  dwr.engine._handleError = dwr.auth.authWarningHandler;
}

// resume dwr.auth
dwr.auth.disable = function() {
  if (!dwr.auth._enabled) {
    alert("dwr.auth not enabled");
    return;
  }
  dwr.engine._handleError = dwr.auth._dwrHandleBatchExeption;
  dwr.auth._dwrHandleBatchExeption = null;
  dwr.auth._enabled = false;
}

// define the url that is protected by servlet-security
dwr.auth._protectedURL = null;
dwr.auth.setProtectedURL = function(url) {
  dwr.auth._protectedURL = url;
}

//
// setters for the various authentication-callback
// and their default-implementataions.
// callback-functions have to return true, if they want, that
// dwr resends the resumed original request.
// 

// authentication required: a dwr request has some authorizations-rules,
// but there's no user attached to the current session
dwr.auth.defaultAuthenticationRequiredHandler = function(batch,ex) {
  alert(ex.message);
  return false;
}
dwr.auth._authRequiredHandler = dwr.auth.defaultAuthenticationRequiredHandler;
dwr.auth.setAuthenticationRequiredHandler = function(handler) {
  dwr.auth._authRequiredHandler = handler;
}

// authentication failed: the server didn't accept the given credentials
dwr.auth.defaultAuthenticationFailedHandler = function(login_form) {
  alert("Login failed");
  return false;
}
dwr.auth._authFailedHandler = dwr.auth.defaultAuthenticationFailedHandler;
dwr.auth.setAuthenticationFailedHandler = function(handler) {
  dwr.auth._authFailedHandler = handler;
}

// access denied: the current session's user is not privileged to do
// the remote call
dwr.auth.defaultAccessDeniedHandler = function(batch,ex) {
  alert(ex.message);
  return false;
}
dwr.auth._accessDeniedHandler = dwr.auth.defaultAccessDeniedHandler;
dwr.auth.setAccessDeniedHandler = function(handler) {
  dwr.auth._accessDeniedHandler = handler;
}

// authenficiation success: the user was successful authenticated
dwr.auth.defaultAuthenticationSuccessHandler = function (msg) {
  return true;
}
dwr.auth._successHandler = dwr.auth.defaultAuthenticationSuccessHandler;
dwr.auth.setAuthenticationSuccessHandler = function(handler) {
  dwr.auth._successHandler = handler;
}

// stores the last dwr-request-batch that dwr didn't process because of
// authenfication/authorization-issues
dwr.auth._batch = null;

// makes a deep-copy of a given javascript-object
dwr.auth._deepCopy = function(source) {
  var destination = {};
  for (property in source) {
    var value = source[property];
    if (typeof value != 'object') {
      //alert("simple property:"+property);
      destination[property] = value;
    }
    else if ( value instanceof Array) {
      //alert("array property:"+property+"("+value.length+")");
      // since the batch-arrays never get changed after 
      // execution, we don't have to do a deepcopy for reexecution
      // otherwise we would have to iterate by value.length, which
      // could take quite long for sparsely populated batchIds-array
      destination[property] = value;
    }
    else {
      //alert("object property:"+property);
      destination[property] = dwr.auth._deepCopy(value);
    }
  }
  return destination;
}

// make a copy of the batch that we can replay later
dwr.auth._cloneBatch = function(batch) {
  var req = batch.req;
  var div = batch.div;
  var form = batch.form;
  var iframe = batch.iframe;
  var script = batch.script;
  delete batch.req;
  delete batch.div;
  delete batch.form;
  delete batch.iframe;
  delete batch.script;
  var clone = dwr.auth._deepCopy(batch);
  batch.req = req;
  batch.div = div;
  batch.form = form;
  batch.iframe = iframe;
  batch.script = script;
  
  clone.completed = false;
  clone.map.httpSessionId = dwr.engine._getJSessionId();
  clone.map.scriptSessionId = dwr.engine._getScriptSessionId();
  return clone;
}

dwr.auth._exceptionPackage = "org.directwebremoting.extend.";
// replacement for dwr's MetaDataWarningHandler
dwr.auth.authWarningHandler = function(batch, ex) {
  if (batch == null || typeof ex != "object" || ex.type == null
    || ex.type.indexOf(dwr.auth._exceptionPackage) != 0) {
    dwr.auth._dwrHandleBatchExeption(batch, ex);
    return;
  }

  var errorType = ex.type.substring(dwr.auth._exceptionPackage.length);
  //alert("errorCode="+errorType);
  switch (errorType) {
    case "LoginRequiredException":
      dwr.auth._batch = dwr.auth._cloneBatch(batch);
      if (dwr.auth._authRequiredHandler(batch,ex)) {
        dwr.auth._replayBatch();
      }
      break;
    case "AccessDeniedException":
      dwr.auth._batch = dwr.auth._cloneBatch(batch);
      if (dwr.auth._accessDeniedHandler(batch,ex)) {
        dwr.auth._replayBatch();
      }
      break;
    default:
      dwr.auth._dwrHandleBatchExeption(batch, ex);
  }
}

// resend a rejected request with dwr.engine
dwr.auth._replayBatch = function() {
  if (dwr.auth._batch == null) {
    alert("no batch to replay!");
    return;
  }
  else {
    //alert("replay batch "+dwr.auth._batch);
  }
  var caller = function() {
    var batch = dwr.auth._batch;
    dwr.auth._batch = null;
    dwr.engine._batches[dwr.engine._batches.length] = batch;
    dwr.engine._sendData(batch);
  };
  // give dwr some time to finish the old batch processing
  setTimeout( caller, 200);
}

// use some minimal protection with a private class
// to prevent acess to credentials from other javascript code
dwr.auth.ServletLoginProcessor = function() {
  var login = null;
  var password = null;
  this.setLogin = function(aLogin) {
    login = aLogin;
  }
  this.getLogin = function() {
    return login;
  }
  this.setPassword = function(aPassword) {
    password = aPassword;
  }
  this.login = function(login_form) {
    login_form.j_username.value = login;
    login_form.j_password.value = password;
    login_form.submit();
    // just because i'm paranoid: clear password
    password = null;
  }
}

dwr.auth._loginProcessor = new dwr.auth.ServletLoginProcessor();

dwr.auth.authenticate = function(login, password) {
  var processor = dwr.auth._loginProcessor;
  processor.setLogin(login);
  processor.setPassword(password);
  // call login-test url in iframe
  var div = document.createElement("div");
  div.innerHTML = "<iframe src='"+dwr.auth._protectedURL+"' frameborder='0' width='0' height='0' id='login_frame' name='login_frame' style='width:0px; height:0px; border:0px;'></iframe>";
  document.body.appendChild(div);
}

dwr.auth._loginCallback = function(login_form) {
  dwr.auth._loginProcessor.login(login_form);
}

dwr.auth._loginFailedCallback = function(login_form) {
  dwr.auth._authFailedHandler(login_form);
}

dwr.auth._loginSucceededCallback = function(msg) {
  if (dwr.auth._successHandler(msg)) {
    dwr.auth._replayBatch();
  }
}

