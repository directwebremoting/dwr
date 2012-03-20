
OpenAjax = {
	hub: {
        implementer: "http://openajax.org",
        implVersion: "0.6",
        specVersion: "0.6",
        implExtraData: {},

        subscribe: function(name, callback, scope, subscriberData, filter) {
            if (!scope) scope = window;
            var handle = name + "." + OpenAjax.hub._subIndex;
            var sub = { scope: scope, cb: callback, fcb: filter, data: subscriberData, sid: OpenAjax.hub._subIndex++, hdl: handle };
            var path = name.split(".");
            OpenAjax.hub._subscribe(OpenAjax.hub._subscriptions, path, 0, sub);
            return handle;
        },

        publish: function(name, message) {
            var path = name.split(".");
            OpenAjax.hub._pubDepth++;
            OpenAjax.hub._publish(OpenAjax.hub._subscriptions, path, 0, name, message);
            OpenAjax.hub._pubDepth--;
            if ((OpenAjax.hub._cleanup.length > 0) && (OpenAjax.hub._pubDepth == 0)) {
                for (var i = 0; i < OpenAjax.hub._cleanup.length; i++) 
                    OpenAjax.hub.unsubscribe(OpenAjax.hub._cleanup[i].hdl);
                delete(OpenAjax.hub._cleanup);
                OpenAjax.hub._cleanup = [];
            }
        },

        unsubscribe: function(sub) {
            var path = sub.split(".");
            var sid = path.pop();
            OpenAjax.hub._unsubscribe(OpenAjax.hub._subscriptions, path, 0, sid);
        },

        _subscribe: function(tree, path, index, sub) {
            var token = path[index];
            if(index == path.length)    
                tree.s.push(sub);
            else { 
                if(typeof tree.c == "undefined")
                     tree.c = {};
                if(typeof tree.c[token] == "undefined") {
                    tree.c[token] = { c: {}, s: [] }; 
                    OpenAjax.hub._subscribe(tree.c[token], path, index + 1, sub);
                }
                else 
                    OpenAjax.hub._subscribe( tree.c[token], path, index + 1, sub);
            }
        },

        _publish: function(tree, path, index, name, msg) {
            if(typeof tree != "undefined") {
                var node;
                if(index == path.length) {
                    node = tree;
                } else {
                    OpenAjax.hub._publish(tree.c[path[index]], path, index + 1, name, msg);
                    OpenAjax.hub._publish(tree.c["*"], path, index + 1, name, msg);         
                    node = tree.c["**"];
                }
                if(typeof node != "undefined") {
                    var callbacks = node.s;
                    var max = callbacks.length;
                    for(var i = 0; i < max; i++) {
                        if(callbacks[i].cb) {
                            var sc = callbacks[i].scope;
                            var cb = callbacks[i].cb;
                            var fcb = callbacks[i].fcb;
                            var d = callbacks[i].data;
                            if(typeof cb == "string"){
                                // get a function object
                                cb = sc[cb];
                            }
                            if(typeof fcb == "string"){
                                // get a function object
                                fcb = sc[fcb];
                            }
                            if((!fcb) || 
                               (fcb.call(sc, name, msg, d))) {
                                cb.call(sc, name, msg, d);
                            }
                        }
                    }
                }
            }
        },

        _unsubscribe: function(tree, path, index, sid) {
            if(typeof tree != "undefined") {
                if(index < path.length) {
                    var childNode = tree.c[path[index]];
                    OpenAjax.hub._unsubscribe(childNode, path, index + 1, sid);
                    if(childNode.s.length == 0) {
                        for(var x in childNode.c) 
                            return;     
                        delete tree.c[path[index]]; 
                    }
                    return;
                }
                else {
                    var callbacks = tree.s;
                    var max = callbacks.length;
                    for(var i = 0; i < max; i++) 
                        if(sid == callbacks[i].sid) {
                            if(OpenAjax.hub._pubDepth > 0) {
                                callbacks[i].cb = null; 
                                OpenAjax.hub._cleanup.push(callbacks[i]);                       
                            }
                            else
                                callbacks.splice(i, 1);
                            return;     
                        }
                }
            }
        },

        _subscriptions: { c:{}, s:[] },
        _cleanup: [],
        _subIndex: 0,
        _pubDepth: 0
    }
};
