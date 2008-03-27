if (DWRAuthentication == null) var DWRAuthentication = {};

//
// Application-wide stuff
//

// enabled-status flag
DWRAuthentication._enabled = false;

// stores the original MetaDataWarningHandler of DWREnging
DWRAuthentication._dwrHandleBatchExeption = null;

// give DWRAuthentication the control
DWRAuthentication.enable = function () { 
	if( DWRAuthentication._enabled ) {
		alert("DWRAuthentication already enabled");
		return;
	}
	DWRAuthentication._enabled = true;
	DWRAuthentication._dwrHandleBatchExeption = DWREngine._handleError;
	DWREngine._handleError = DWRAuthentication.authWarningHandler;

}

// resume DWRAuthentication
DWRAuthentication.disable = function() {
	if( !DWRAuthentication._enabled ) {
		alert("DWRAuthentication not enabled");
		return;
	}
	DWREngine._handleError = DWRAuthentication._dwrHandleBatchExeption;
	DWRAuthentication._dwrHandleBatchExeption = null;
	DWRAuthentication._enabled = false;
}

// define the url that is protected by servlet-security
DWRAuthentication._protectedURL = null;
DWRAuthentication.setProtectedURL = function(url) {
	DWRAuthentication._protectedURL = url;
}

//
// setters for the various authentication-callback
// and their default-implementataions.
// callback-functions have to return true, if they want, that
// dwr resends the resumed original request.
// 

// authentication required: a dwr request has some authorizations-rules,
// but there's no user attached to the current session
DWRAuthentication.defaultAuthenticationRequiredHandler = function(batch,ex) {
       alert(ex.message);
	return false;
}
DWRAuthentication._authRequiredHandler = DWRAuthentication.defaultAuthenticationRequiredHandler;
DWRAuthentication.setAuthenticationRequiredHandler = function(handler) {
	DWRAuthentication._authRequiredHandler = handler;
}

// authentication failed: the server didn't accept the given credentials
DWRAuthentication.defaultAuthenticationFailedHandler = function(login_form) {
	alert("Login failed");
	return false;
}
DWRAuthentication._authFailedHandler = DWRAuthentication.defaultAuthenticationFailedHandler;
DWRAuthentication.setAuthenticationFailedHandler = function(handler) {
	DWRAuthentication._authFailedHandler = handler;
}

// access denied: the current session's user is not privileged to do
// the remote call
DWRAuthentication.defaultAccessDeniedHandler = function(batch,ex) {
       alert(ex.message);
	return false;
}
DWRAuthentication._accessDeniedHandler = DWRAuthentication.defaultAccessDeniedHandler;
DWRAuthentication.setAccessDeniedHandler = function(handler) {
	DWRAuthentication._accessDeniedHandler = handler;
}

// authenficiation success: the user was successful authenticated
DWRAuthentication.defaultAuthenticationSuccessHandler = function (msg) {
	return true;
}
DWRAuthentication._successHandler = DWRAuthentication.defaultAuthenticationSuccessHandler;
DWRAuthentication.setAuthenticationSuccessHandler = function(handler) {
	DWRAuthentication._successHandler = handler;
}

// stores the last dwr-request-batch that dwr didn't process because of
// authenfication/authorization-issues
DWRAuthentication._batch = null;

// makes a deep-copy of a given javascript-object
DWRAuthentication._deepCopy = function(source) {
  var destination = {};
  for (property in source) {
  	var value = source[property];
  	if( typeof value != 'object' ) {
	  	//alert("simple property:"+property);
	    destination[property] = value;
	}
	else if ( value instanceof Array ) {
	  	//alert("array property:"+property+"("+value.length+")");
	  	// since the batch-arrays never get changed after 
	  	// execution, we don't have to do a deepcopy for reexecution
	  	// otherwise we would have to iterate by value.length, which
	  	// could take quite long for sparsely populated batchIds-array
		destination[property] = value;
		
	}
	else {
	  	//alert("object property:"+property);
		destination[property] = DWRAuthentication._deepCopy(value);
	}
  }
  return destination;
}

// make a copy of the batch that we can replay later
DWRAuthentication._cloneBatch = function(batch) {
	var req = batch.req;
	batch.req = null;
	var clone = DWRAuthentication._deepCopy(batch);
	batch.req = req;
	clone.completed = false;
  	clone.map.httpSessionId = DWREngine._getJSessionId();
    clone.map.scriptSessionId = DWREngine._getScriptSessionId();
	return clone;
}

DWRAuthentication._exceptionPackage = "org.directwebremoting.extend.";
// replacement for dwr's MetaDataWarningHandler
DWRAuthentication.authWarningHandler = function(batch, ex) {
	if( batch == null 
		|| typeof ex != "object" 
		|| ex.type == null 
               || ex.type.indexOf(DWRAuthentication._exceptionPackage) != 0
	) {
		DWRAuthentication._dwrHandleBatchExeption(batch, ex);
		return;
	}
	
       var errorType = ex.type.substring(DWRAuthentication._exceptionPackage.length);
	//alert("errorCode="+errorType);
	switch( errorType ) {
               case "LoginRequiredException":
                       DWRAuthentication._batch = DWRAuthentication._cloneBatch(batch);
			if( DWRAuthentication._authRequiredHandler(batch,ex) ) {
				DWRAuthentication._replayBatch();
			}
			break;
		case "AccessDeniedException":
                       DWRAuthentication._batch = DWRAuthentication._cloneBatch(batch);
			if( DWRAuthentication._accessDeniedHandler(batch,ex) ) {
				DWRAuthentication._replayBatch();
			}
			break;
		default:
                       DWRAuthentication._dwrHandleBatchExeption(batch, ex);
	}
}

// resend a rejected request with DWREngine
DWRAuthentication._replayBatch = function() {
	if( DWRAuthentication._batch == null ) {
		alert("no batch to replay!");
		return;
	}
	else {
		//alert("replay batch "+DWRAuthentication._batch);
	}
	var caller = function() {
		var batch = DWRAuthentication._batch;
		DWRAuthentication._batch = null;
	    DWREngine._batches[DWREngine._batches.length] = batch;
		DWREngine._sendData(batch);
	};
	// give dwr some time to finish the old batch processing
	setTimeout( caller, 200 );
}

// use some minimal protection with a private class
// to prevent acess to credentials from other javascript code
DWRAuthentication.ServletLoginProcessor = function() {

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

DWRAuthentication._loginProcessor = new DWRAuthentication.ServletLoginProcessor();

DWRAuthentication.authenticate = function(login, password) {
	
	var processor = DWRAuthentication._loginProcessor;
	processor.setLogin(login);
	processor.setPassword(password);
	// call login-test url in iframe
    var div = document.createElement("div");
    div.innerHTML = "<iframe src='"+DWRAuthentication._protectedURL+"' frameborder='0' width='0' height='0' id='login_frame' name='login_frame' style='width:0px; height:0px; border:0px;'></iframe>";
    document.body.appendChild(div);
	
}

DWRAuthentication._loginCallback = function(login_form) {
	DWRAuthentication._loginProcessor.login(login_form);
}

DWRAuthentication._loginFailedCallback = function(login_form) {
	DWRAuthentication._authFailedHandler(login_form);
}

DWRAuthentication._loginSucceededCallback = function(msg) {
	if( DWRAuthentication._successHandler(msg) ) {
		DWRAuthentication._replayBatch();
	}
}