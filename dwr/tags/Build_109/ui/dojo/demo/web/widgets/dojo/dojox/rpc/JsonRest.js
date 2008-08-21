dojo.provide("dojox.rpc.JsonRest");

dojo.require("dojox.json.ref"); // this provides json indexing
dojo.require("dojox.rpc.Rest");
// summary:
// 		Provides JSON/REST utility functions
(function(){
	var dirtyObjects = [];
	var Rest = dojox.rpc.Rest;
	var jr = dojox.rpc.JsonRest={
		commit: function(kwArgs){
			// summary:
			//		Saves the dirty data using REST Ajax methods
			var data = [];

			var left; // this is how many changes are remaining to be received from the server
			kwArgs = kwArgs || {};
			function finishOne(value){
				
				if(!(--left)){
					dirtyObjects.splice(0,numDirty); // remove all the objects that were committed
					if(kwArgs.onComplete){
						kwArgs.onComplete.call(kwArgs.scope);
					}
				}
				return value;
			}
			var actions = [];
			var numDirty = dirtyObjects.length;
			var alreadyRecorded = {};
			
			for(var i = 0; i < numDirty; i++){
				var dirty = dirtyObjects[i];
				var object = dirty.object;
				var append = false;
				if(object && !dirty.old){
					// new object
					actions.push({method:"post",target:{__id:jr.getServiceAndId(object.__id).service.servicePath},
											content:object});
				}else if(!object && dirty.old){
					// deleted object
					actions.push({method:"delete",target:dirty.old});
				}else{
					// changed object
					while(!(dojox.json && dojox.json.ref && dojox.json.ref.useRefs) && object.__id.match(/[\[\.]/)){ // it is a path reference
						// this means it is a sub object and the server doesn't support directly putting to
						// this object by path, we must go to the parent object and save it
						var parentId = object.__id.match(/^[^\[\.]*/)[0];
						if(parentId in alreadyRecorded){// if it has already been saved, we don't want to repeat it
							continue;
						}
						// record that we are saving
						object = alreadyRecorded[parentId] = Rest._index[parentId];
					}
					actions.push({method:"put",target:object,content:object});
				}

				data.push(object);
			}
			var xhrSendId;
			var plainXhr = dojo.xhr;
			left = actions.length;
			if(left){
				dojo.xhr = function(method,args){
					// keep the transaction open as we send requests
					args.headers = args.headers || {};
					args.headers['X-Transaction'] = "open";
					return plainXhr.apply(dojo,arguments);
				};
			}
			for(i =0; i < actions.length;i++){ // iterate through the actions to execute
				var action = actions[i];
				if(actions.length - 1 == i){
					// the last one should disconnect, so no transaction header is sent and thus commit the transaction
					dojo.xhr = plainXhr;
				}
				dojox.rpc.JsonRest._contentId = action.content && action.content.__id; // this is used by LocalStorageRest
				var dfd = Rest[action.method](
									action.target,
									dojox.json.ref.toJson(action.content,false,jr.getServiceAndId(action.target.__id).service.servicePath)
								);
				dfd.addCallback(finishOne);
				dfd.addErrback(function(value){
					
					// on an error we want to revert, first we want to separate any changes that were made since the commit
					left = -1; // first make sure that success isn't called
					var postCommitDirtyObjects = dirtyObjects.splice(numDirty,dirtyObjects.length - numDirty);
					numDirty = 0; // make sure this does't do anything if it is called again
					jr.revert(); // revert if there was an error
					dirtyObjects = postCommitDirtyObjects;
					if(kwArgs.onError){
						kwArgs.onError();
					}
				});
			}
			
			return actions;
		},
		getDirtyObjects: function(){
			return dirtyObjects;
		},
		revert: function(){
			// summary:
			//		Reverts all the changes made to JSON/REST data
			while (dirtyObjects.length>0){
				var d = dirtyObjects.pop();
				if(d.object && d.old){
					// changed
					for(var i in d.old){
						if(d.old.hasOwnProperty(i)){
							d.object[i] = d.old[i];
						}
					}
					for(i in d.object){
						if(!d.old.hasOwnProperty(i)){
							delete d.object[i];
						}
					}
				}
			}
		},
		changing: function(object,_deleting){
			// summary:
			//		adds an object to the list of dirty objects.  This object
			//		contains a reference to the object itself as well as a
			//		cloned and trimmed version of old object for use with
			//		revert.
			if(!object.__id){
				return;
			}
			//if an object is already in the list of dirty objects, don't add it again
			//or it will overwrite the premodification data set.
			for(var i=0; i<dirtyObjects.length; i++){
				if(object==dirtyObjects[i].object){
					return;
				}
			}
			var old = object instanceof Array ? [] : {};
			for(i in object){
				if(object.hasOwnProperty(i)){
					old[i] = object[i];
				}
			}
			dirtyObjects.push({object: !_deleting && object, old: old});
		},
		deleteObject: function(object){
			// summary:
			//		deletes an object 
			//	object:
			//  	object to delete
			//

			this.changing(object,true);
		},
		getConstructor: function(/*Function|String*/service){
			// summary:
			// 		Creates or gets a constructor for objects from this service
			if(typeof service == 'string'){
				service = new dojox.rpc.Rest(service,true);
			}
			if(service._constructor){
				return service._constructor;
			}
			service._constructor = function(data){
				// summary:
				//		creates a new object for this table
				//
				//	data:
				//		object to mixed in
				if(data){
					dojo.mixin(this,data);
				}
				Rest._index[this.__id = service.servicePath + (data[jr.getIdAttribute(service)] = Math.random().toString(16).substring(2,14)+Math.random().toString(16).substring(2,14))] = this;
				dirtyObjects.push({object:this});
	//			this._getParent(parentInfo).push(data); // append to this list

			};
			return dojo.mixin(service._constructor, service._schema, {load:service});
		},
		fetch: function(absoluteId){
			// summary:
			//		Fetches a resource by an absolute path/id and returns a dojo.Deferred.
			var serviceAndId = jr.getServiceAndId(absoluteId);
			return this.get(serviceAndId.service,serviceAndId.id);
		},
		getIdAttribute: function(service){
			// summary:
			//		Return the ids attribute used by this service (based on it's schema).
			//		Defaults to "id", if not other id is defined
			var schema = service._schema;
			var idAttr;
			if(schema){
				if(!(idAttr = schema._idAttr)){
					for(var i in schema.properties){
						if(schema.properties[i].unique){
							schema._idAttr = idAttr = i;
						}
					}
				}
			}
			return idAttr || 'id';
		},
		getServiceAndId: function(/*String*/absoluteId){
			// summary:
			//		Returns the REST service and the local id for the given absolute id. The result 
			// 		is returned as an object with a service property and an id property
			//	absoluteId:
			//		This is the absolute id of the object
			var parts = absoluteId.match(/^(.*\/)([^\/]*)$/);
			var svc = jr.services[parts[1]] || new dojox.rpc.Rest(parts[1]); // use an existing or create one
			return { service: svc, id:parts[2] };
		},
		services:{},
		schemas:{},
		registerService: function(/*Function*/ service, /*String*/ servicePath, /*Object?*/ schema){
			//	summary:
			//		Registers a service for as a JsonRest service, mapping it to a path and schema
			
			servicePath = servicePath || service.servicePath;
			servicePath = service.servicePath = servicePath.match(/\/$/) ? servicePath : (servicePath + '/'); // add a trailing / if needed
			jr.schemas[servicePath] = schema || service._schema || {};
			jr.services[servicePath] = service;
		},
		get: function(service, id, args){
			// if caching is allowed, we look in the cache for the result
			var deferred, result = args && args.useIndexCache && Rest._index[(service.servicePath || '') + id];
			if(result && !result._loadObject){// cache hit
				deferred = new dojo.Deferred();
				deferred.callback(result);
				return deferred;
			}
			
			deferred = service(id, args);
			deferred.addCallback(function(result){
				if(result.nodeType && result.cloneNode){
					// return immediately if it is an XML document
					return result;
				}
				return dojox.json.ref.resolveJson(result, {
					defaultId: typeof id != 'string' || (args && (args.start || args.count)) ? undefined: id, 
					index: Rest._index,
					idPrefix: service.servicePath,
					idAttribute: jr.getIdAttribute(service),
					schemas: jr.schemas,
					loader:	jr._loader
				});
			});
			return deferred;			
		},
		_loader: function(callback){
			// load a lazy object
			var serviceAndId = jr.getServiceAndId(this.__id);
			var self = this;
			jr.get(serviceAndId.service, serviceAndId.id).addBoth(function(result){
				// if they are the same this means an object was loaded, otherwise it 
				// might be a primitive that was loaded or maybe an error
				if(result == self){
					// we can clear the flag, so it is a loaded object
					delete result.$ref;
					delete result._loadObject;
				}else{
					// it is probably a primitive value, we can't change the identity of an object to
					//	the loaded value, so we will keep it lazy, but define the lazy loader to always
					//	return the loaded value
					self._loadObject = function(callback){
						callback(result);
					};
				}
				callback(result);
			});
		},
		isDirty: function(item){
			// summary
			//		returns true if the item is marked as dirty.
			for(var i = 0, l = dirtyObjects.length; i < l; i++){
				if(dirtyObjects[i]==item){return true;}
			}
			return false;
		}
	};
})();


