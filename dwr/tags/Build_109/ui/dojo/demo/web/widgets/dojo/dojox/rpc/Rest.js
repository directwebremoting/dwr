dojo.provide("dojox.rpc.Rest"); // Note: This doesn't require dojox.rpc.Service, if you want it you must require it yourself
// summary:
// 		This provides a HTTP REST service with full range REST verbs include PUT,POST, and DELETE.
// description:
// 		A normal GET query is done by using the service directly:
// 		| var restService = dojox.rpc.Rest("Project");
// 		| restService("4");
//		This will do a GET for the URL "/Project/4".
//		| restService.put("4","new content");
//		This will do a PUT to the URL "/Project/4" with the content of "new content".
//		You can also use the SMD service to generate a REST service:
// 		| var services = dojox.rpc.Service({services: {myRestService: {transport: "REST",...
// 		| services.myRestService("parameters");
//
// 		The modifying methods can be called as sub-methods of the rest service method like:
//  	| services.myRestService.put("parameters","data to put in resource");
//  	| services.myRestService.post("parameters","data to post to the resource");
//  	| services.myRestService['delete']("parameters");
(function(){
	if(dojox.rpc && dojox.rpc.transportRegistry){
		// register it as an RPC service if the registry is available
		dojox.rpc.transportRegistry.register(
			"REST",
			function(str){return str == "REST";},
			{
				getExecutor : function(func,method,svc){
					return new dojox.rpc.Rest(
						method.name,
						(method.contentType||svc._smd.contentType||"").match(/json|javascript/), // isJson
						null,
						function(id, args){
							var request = svc._getRequest(method,[id, args]);
							request.url= request.target + (request.data ? '?'+  request.data : '');
							return request;
						}
					);
				}
			}
		);
	}
	var drr;

	function index(deferred, service, range, id){
		deferred.addCallback(function(result){
			if(range){
				// try to record the total number of items from the range header
				range = deferred.ioArgs.xhr && deferred.ioArgs.xhr.getResponseHeader("Content-Range");
				deferred.fullLength = range && (range=range.match(/\/(.*)/)) && parseInt(range[1]);
			}
			return result;
		});
		return deferred;
	}
	drr = dojox.rpc.Rest = function(/*String*/path, /*Boolean?*/isJson, /*Object?*/schema, /*Function?*/getRequest){
		// summary:
		//		Creates a REST service using the provided path.
		var service;
		// it should be in the form /Table/
		path = path.match(/\/$/) ? path : (path + '/');
		service = function(id, args){
			return drr._get(service, id, args);
		};
		service.isJson = isJson;
		service._schema = schema;
		// cache:
		//		This is an object that provides indexing service
		// 		This can be overriden to take advantage of more complex referencing/indexing
		// 		schemes
		service.cache = {
			serialize: isJson ? ((dojox.json && dojox.json.ref) || dojo).toJson : function(result){
				return result;
			}
		};
		// the default XHR args creator:
		service._getRequest = getRequest || function(id, args){
			return {
				url: path + (dojo.isObject(id) ? '?' + dojo.objectToQuery(id) : id == null ? "" : id), 
				handleAs: isJson?'json':'text', 
				contentType: isJson?'application/json':'text/plain',
				sync: dojox.rpc._sync,
				headers: {
					Range: args && (args.start >= 0 || args.count >= 0) ?  "items=" + (args.start || '') + '-' + ((args.count && (args.count + (args.start || 0) - 1)) || '') : undefined
				}
			};
		};
		// each calls the event handler
		function makeRest(name){
			service[name] = function(id,content){
				return drr._change(name,service,id,content); // the last parameter is to let the OfflineRest know where to store the item
			};
		}
		makeRest('put');
		makeRest('post');
		makeRest('delete');
		// record the REST services for later lookup
		dojox.rpc.services = dojox.rpc.services || {};
		dojox.rpc.services[path] = service;
		service.servicePath = path;
		return service;
	};
	function restMethod(name){
		// summary:
		// 		create a REST method for the given name
		drr[name] = function(target,content){
			// parse the id to find the service and the id to use
			var parts = target.__id.match(/^(.*\/)([^\/]*)$/);
			// find the service and call it
			var service = dojox.rpc.services[parts[1]] || new dojox.rpc.Rest(parts[1]); // use an existing or create one
			// // TODO: could go directly to the event handlers
			return name == 'get' ? service(parts[2],content) : service[name](parts[2],content);
		};
	}
	restMethod("get");
	restMethod("put");
	restMethod("post");
	restMethod("delete");
	drr._index={};// the map of all indexed objects that have gone through REST processing
	// these do the actual requests
	drr._change = function(method,service,id,content){
		// this is called to actually do the put, post, and delete
		var request = service._getRequest(id);
		request[method+"Data"] = content;
		return index(dojo.xhr(method.toUpperCase(),request,true),service);
	};

	drr._get= function(service,id, args){
		args = args || {};
		// this is called to actually do the get
		return index(dojo.xhrGet(service._getRequest(id, args)), service, (args.start >= 0 || args.count >= 0), id);
	};
})();
