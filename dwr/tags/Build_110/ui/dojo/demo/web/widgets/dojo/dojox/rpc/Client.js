dojo.provide("dojox.rpc.Client");
// Provide extra headers for robust client and server communication
(function() {
	dojo._defaultXhr = dojo.xhr;
	dojo.xhr = function(method,args){
		var headers = args.headers = args.headers || {};
		// set the client id, this can be used by servers to maintain state information with the
		// a specific client. Many servers rely on sessions for this, but sessions are shared
		// between tabs/windows, so this is not appropriate for application state, it
		// really only useful for storing user authentication
		headers["X-Client-Id"] = dojox._clientId;
		// set the sequence id. HTTP is non-deterministic, message can arrive at the server
		// out of order. In complex Ajax applications, it may be more to ensure that messages
		// can be properly sequenced deterministically. This applies a sequency id to each
		// XHR request so that the server can order them.
		headers["X-Seq-Id"] = dojox._reqSeqId = (dojox._reqSeqId||0)+1;
		return dojo._defaultXhr.apply(dojo,arguments);
	}
})();
// initiate the client id to a good random number
dojox._clientId = (Math.random() + '').substring(2,14) + (Math.random() + '').substring(2,14);
