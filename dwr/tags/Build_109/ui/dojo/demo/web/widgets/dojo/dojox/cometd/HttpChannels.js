dojo.provide("dojox.cometd.HttpChannels");
 
dojo.require("dojox.rpc.Client");
dojo.require("dojox.io.httpParse");
if(dojox.data && dojox.data.JsonRestStore){
	dojo.require("dojox.data.restListener");
}
// Note that cometd _base is _not_ required, this can run standalone, but ifyou want 
// cometd functionality, you must explicitly load/require it elsewhere, and cometd._base
// MUST be loaded prior to HttpChannels ifyou use it.

// summary:
// 		HTTP Channels - An HTTP Based approach to Comet transport with full HTTP messaging 
// 		semantics including REST
// 		HTTP Channels is a efficient, reliable duplex transport for Comet

// description:
// 		This can be used:
// 		1. As a cometd transport
// 		2. As an enhancement for the REST RPC service, to enable "live" data (real-time updates directly alter the data in indexes)
// 		2a. With the JsonRestStore (which is driven by the REST RPC service), so this dojo.data has real-time data. Updates can be heard through the dojo.data notification API.
// 		3. As a standalone transport. To use it as a standalone transport looks like this:
// 	|		dojox.cometd.HttpChannels.open();
// 	|		dojox.cometd.HttpChannels.get("/myResource",{callback:function(){
// 	|			// this is called when the resource is first retrieved and any time the 
// 	|			// resource is changed in the future. This provides a means for retrieving a
// 	|			// resource and subscribing to it in a single request
// 	|		});
// 	|	dojox.cometd.HttpChannels.subscribe("/anotherResource",{callback:function(){
// 	|		// this is called when the resource is changed in the future
// 	|	});
// 		Channels HTTP can be configured to a different delays:
// 	|	dojox.cometd.HttpChannels.autoReconnectTime = 60000; // reconnect after one minute
//

(function(){
	dojo.declare("dojox.cometd.HttpChannels", null, {
		constructor: function(options){
			// summary:
			//		Initiates the HTTP Channels protocol
			//	options:
			//		Keyword arguments:
			//	The *autoSubscribeRoot* parameter:
			//		When this is set, all REST service requests that have this
			// 		prefix will be auto-subscribed. The default is '/' (all REST requests).
			//  The *url* parameter:
			//		This is the url to connect to for server-sent messages. The default
			//		is "/channels".
			//	The *autoReconnectTime* parameter:
			// 		This is amount time to wait to reconnect with a connection is broken	
			dojo.mixin(this,options);
			// If we have a Rest service available and we are auto subscribing, we will augment the Rest service 
			if(dojox.rpc.Rest && this.autoSubscribeRoot){
				// override the default Rest handler so we can add subscription requests
				var defaultGet = dojox.rpc.Rest._get;
				var self = this;
				dojox.rpc.Rest._get = function(service, id){
					// when there is a REST get, we will intercept and add our own xhr handler
					var defaultXhrGet = dojo.xhrGet;
					dojo.xhrGet = function(r){
						var autoSubscribeRoot = self.autoSubscribeRoot;
						return (autoSubscribeRoot && r.url.substring(0, autoSubscribeRoot.length) == autoSubscribeRoot) ?
							self.get(r.url,r) : // auto-subscribe 
							defaultXhrGet(r); // plain XHR request
					};
		
					var result = defaultGet.apply(this,arguments);
					dojo.xhrGet = defaultXhrGet;
					return result;
				};
			}
			if(dojox.data && dojox.data.restListener){
				this.receive = dojox.data.restListener;
			}
		},
		absoluteUrl: function(baseUrl,relativeUrl){
			return new dojo._Url(baseUrl,relativeUrl)+'';
		},
		acceptType: "x-application/http+json,application/http;q=0.9,*/*;q=0.7",
		subscriptions: {},
		subCallbacks: {},
		autoReconnectTime: 30000,
		sendAsJson: false,
		url: '/channels',
		autoSubscribeRoot: '/',
		open: function(){
			// summary:
			// 		Startup the transport (connect to the "channels" resource to receive updates from the server).
			//
			// description:
			//		Note that if there is no connection open, this is automatically called when you do a subscription,
			// 		it is often not necessary to call this
			//
			if(!this.connected){
				this.connectionId = dojox._clientId;
				var clientIdHeader = this.started ? 'X-Client-Id' : 'X-Create-Client-Id';

				var headers = {Accept:this.acceptType};
				headers[clientIdHeader] = this.connectionId;
				var dfd = dojo.xhrPost({headers:headers, url: this.url, noStatus: true});
		  		var self = this;
		  		this.lastIndex = 0; 
				var onerror, onprogress = function(data){ // get all the possible event handlers
					if(typeof dojo == 'undefined'){
						return null;// this can be called after dojo is unloaded, just do nothing in that case
					}
					data = data.substring(self.lastIndex);
					var contentType = xhr && (xhr.contentType || xhr.getResponseHeader("Content-Type"));
					self.started = true;
					var error = self.onprogress(xhr,data,contentType);
					if(error){
						onerror();
						return new Error(error);
					}
					if(!xhr || xhr.readyState==4){
						xhr = null;
						if(self.connected){
							self.connected = false;
							self.open();
						}
					}
					return data;
				};
				onerror = function(error){
					if(self.started){ // this means we need to reconnect
						self.started = false;
						self.connected = false;
						var subscriptions = self.subscriptions;
						self.subscriptions = {};
						for(var i in subscriptions){
							self.subscribe(i,{since:subscriptions[i]});
						}
					}else{
						self.disconnected();
					}
					return error;
			  	};
			  	dfd.addCallbacks(onprogress,onerror);
			  	var xhr = dfd.ioArgs.xhr; // this may not exist if we are not using XHR, but an alternate XHR plugin
			  	if(xhr){
			  		// if we are doing a monitorable XHR, we want to listen to streaming events
	  				xhr.onreadystatechange = function(){
	  					var responseText;
						try{
							if(xhr.readyState == 3){// only for progress, the deferred object will handle the finished responses
								self.readyState = 3;
								responseText = xhr.responseText;
							}
						} catch(e){
						}
	  					if(typeof data=='string'){
	  						onprogress(responseText);
	  					}
	  				} 
			  	}
			  	
	  			 
				if(window.attachEvent){// IE needs a little help with cleanup
					attachEvent("onunload",function(){
						self.connected= false;
						if(xhr){
							xhr.abort();
						}
					});
				}
				
				this.connected = true;
			}
		},
		_send: function(method,args,data){
			// fire an XHR with appropriate modification for JSON handling
			if(this.sendAsJson){
				// send use JSON Messaging
				args.postBody = dojo.toJson({
					target:args.url,
					method:method,
					content: data,
					params:args.content,
					subscribe:headers["X-Subscribe"]
				});
				args.url = this.url;
				method = "POST";
			}else{
				args.postData = dojo.toJson(data);
			}			
			return dojo.xhr(method,args,args.postBody);
		}, 
		subscribe: function(/*String*/channel, /*dojo.__XhrArgs?*/args){
			// summary:
			// 		Subscribes to a channel/uri, and returns a dojo.Deferred object for the response from 
			// 		the subscription request
			//
			// channel: 
			// 		the uri for the resource you want to monitor
			// 
			// args: 
			// 		See dojo.xhr
			// 
			// headers:
			// 		These are the headers to be applied to the channel subscription request
			//
			// callback:
			// 		This will be called when a event occurs for the channel
			// 		The callback will be called with a single argument:
			// 	|	callback(message)
			// 		where message is an object that follows the XHR API:
			// 		status : Http status
			// 		statusText : Http status text
			// 		getAllResponseHeaders() : The response headers
			// 		getResponseHeaders(headerName) : Retrieve a header by name
			// 		responseText : The response body as text
			// 			with the following additional Bayeux properties 
			// 		data : The response body as JSON
			// 		channel : The channel/url of the response
			args = args || {};
			args.url = this.absoluteUrl(this.url, channel);
			if(args.headers){ 
				// FIXME: combining Ranges with notifications is very complicated, we will save that for a future version
				delete args.headers.Range;
			}
			var oldSince = this.subscriptions[channel];
			var method = args.method || "HEAD"; // HEAD is the default for a subscription
			var since = args.since;
			var callback = args.callback;
			var headers = args.headers || (args.headers = {});
			this.subscriptions[channel] = since || oldSince || 0;
			var oldCallback = this.subCallbacks[channel];
			if(callback){
				this.subCallbacks[channel] = oldCallback ? function(m){
					oldCallback(m);
					callback(m);
				} : callback;
			} 
			if(!this.connected){
				this.open();
			}
			if(oldSince === undefined || oldSince != since){
				headers["Cache-Control"] = "max-age=0";
				since = typeof since == 'number' ? new Date(since).toUTCString() : since;
				if(since){
					headers["X-Subscribe-Since"] = since;
				}
				headers["X-Subscribe"] = args.unsubscribe ? 'none' : '*';
				var dfd = this._send(method,args);
				
				var self = this;
				dfd.addBoth(function(result){					
					var xhr = dfd.ioArgs.xhr;
					if(!(result instanceof Error)){
						if(args.confirmation){
							args.confirmation();
						}
					}
					if(xhr && xhr.getResponseHeader("X-Subscribed")  == "OK"){
						var lastMod = xhr.getResponseHeader('Last-Modified');
						
						if(xhr.responseText){ 
							self.subscriptions[channel] = lastMod || new Date().toUTCString();
						}else{
							return null; // don't process the response, the response will be received in the main channels response
						}
					}else if(xhr){ // ifit is not a 202 response, that means it is did not accept the subscription
						delete self.subscriptions[channel];
					}
					if(!(result instanceof Error)){
						var message = {
							responseText:xhr && xhr.responseText,
							channel:channel,
							getResponseHeader:function(name){
								return xhr.getResponseHeader(name);
							},
							getAllResponseHeaders:function(){
								return xhr.getAllResponseHeaders();
							}
						};
						try{
							message.data = result;
						}
						catch (e){}
						if(self.subCallbacks[channel]){
							self.subCallbacks[channel](message); // call with the fake xhr object
						}
					}else{
						if(self.subCallbacks[channel]){
							self.subCallbacks[channel](xhr); // call with the actual xhr object
						}
					}
					return result;
				});
				return dfd;
			}
			return null;
		},
		publish: function(channel,data){
			// summary:
			//		Publish an event.
			// description:
			// 		This does a simple POST operation to the provided URL,
			// 		POST is the semantic equivalent of publishing a message within REST/Channels
			// channel:
			// 		Channel/resource path to publish to
			// data:
			//		data to publish
			return this._send("POST",{url:channel,contentType : 'application/json'},data);
		},
		_processMessage: function(message){
			message.event = message.event || message.getResponseHeader('X-Event');
			if(message.event=="connection-conflict"){
				return "conflict"; // indicate an error
			}
			try{
				message.data = message.content || dojo.fromJson(message.responseText);
			}
			catch(e){}
			var self = this;	
			var loc = message.channel = new dojo._Url(this.url, message.target || message.getResponseHeader('Content-Location'))+'';//for cometd
			if(loc in this.subscriptions){
				this.subscriptions[loc] = message.getResponseHeader('Last-Modified'); 
			}
			if(this.subCallbacks[loc]){
				setTimeout(function(){ //give it it's own stack 
					self.subCallbacks[loc](message);
				},0);
			}
			this.receive(message);
			return null;		
		},
		onprogress: function(xhr,data,contentType){
			// internal XHR progress handler
			if(!contentType || contentType.match(/application\/http\+json/)){
				var size = data.length;
				data = data.replace(/^\s*[,\[]?/,'['). // must start with a opening bracket
					replace(/[,\]]?\s*$/,']'); // and end with a closing bracket
				try{
					// if this fails, it probably means we have an incomplete JSON object
					var xhrs = dojo.fromJson(data);
					this.lastIndex += size;
				}
				catch(e){
				}
			}
			else if(contentType.match(/application\/http/)){
				// do HTTP tunnel parsing
				var topHeaders = '';
				if(xhr && xhr.getAllResponseHeaders){
					// mixin/inherit headers from the container response
					topHeaders = xhr.getAllResponseHeaders();
				}
				xhrs = dojox.io.httpParse(data,topHeaders,xhr.readyState != 4);
			}
			if(xhrs){
				for(var i = 0;i < xhrs.length;i++){
					if(this._processMessage(xhrs[i])){
						return "conflict";
					}
				}
				return null;
			}
			if(!xhr){
				//no streaming and we didn't get any message, must be an error
				return "error";
			}
			if(xhr.readyState != 4){ // we only want finished responses here if we are not streaming 
				return null;
			}
			if(xhr.__proto__){// firefox uses this property, so we create an instance to shadow this property
				xhr = {channel:"channel",__proto__:xhr};
			}			
			return this._processMessage(xhr);
		
		},
		
		get: function(/*String*/channel, /*dojo.__XhrArgs?*/args){
			// summary:
			// 		GET the initial value of the resource and subscribe to it  
			//		See subscribe for parameter values
			(args = args || {}).method = "GET"; 
			return this.subscribe(channel,args);
		},
		receive: function(message){
			// summary:
			//		Called when a message is received from the server
			//	message:
			//		A cometd/XHR message
		},
		disconnected: function(){
			// summary:
			// 		called when our channel gets disconnected
			var self = this;
			if(this.connected){ // ifwe are connected, we shall tryto reconnect 
				setTimeout(function(){ // auto reconnect
					self.open();
				},this.autoReconnectTime);
			}
			this.connected = false;
		},
		unsubscribe: function(/*String*/channel, /*dojo.__XhrArgs?*/args){
			// summary:
			// 		unsubscribes from the resource  
			//		See subscribe for parameter values 
			
			args = args || {};
			args.unsubscribe = true;
			this.subscribe(channel,args); // change the time frame to after 5000AD 
		},
		disconnect: function(){
			// summary:
			// 		disconnect from the server  
			this.connected = false;
			this.xhr.abort();
		}
	});
	var Channels = dojox.cometd.HttpChannels.defaultInstance = new dojox.cometd.HttpChannels();
	if(dojox.cometd.connectionTypes){ 
		// register as a dojox.cometd transport and wire everything for cometd handling
		// below are the necessary adaptions for cometd
		Channels.startup = function(data){ // must be able to handle objects or strings
			Channels.open();
			this._cometd._deliver({channel:"/meta/connect",successful:true}); // tell cometd we are connected so it can proceed to send subscriptions, even though we aren't yet 

		};
		Channels.check = function(types, version, xdomain){
			for(var i = 0; i< types.length; i++){
				if(types[i] == "http-channels"){
					return !xdomain;
				}
			}
			return false;
		};
		Channels.deliver = function(message){ 
			// nothing to do
		};
		dojo.connect(this,"receive",null,function(message){
			this._cometd._deliver(message);
		});
		Channels.sendMessages = function(messages){
			for(var i = 0; i < messages.length; i++){
				var message = messages[i];
				var channel = message.channel;
				var cometd = this._cometd;
				var args = {
					confirmation: function(){ // send a confirmation back to cometd
						cometd._deliver({channel:channel,successful:true});
					}
				};
				if(channel == '/meta/subscribe'){
					this.subscribe(message.subscription,args);
				}else if(channel == '/meta/unsubscribe'){
					this.unsubscribe(message.subscription,args);
				}else if(channel == '/meta/connect'){
					args.confirmation();
				}else if(channel == '/meta/disconnect'){
					Channels.disconnect();
					args.confirmation();
				}else if(channel.substring(0,6) != '/meta/'){
					this.publish(channel,message.data);
				}
			}
		};
		dojox.cometd.connectionTypes.register("http-channels", Channels.check, Channels,false,true);
	}
})();
