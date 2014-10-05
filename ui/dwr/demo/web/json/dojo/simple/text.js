
dojo.provide("custom.jsonrpcDemo");

dojo.require("dojox.rpc.Service");
dojo.require("dojox.rpc.JsonRPC");

function update() {
	var services = new dojox.rpc.Service({
	    target:"../../../dwr/jsonrpc",
	    transport:"POST",
	    envelope:"JSON-RPC-1.0",
	    contentType:"application/json",
	    services:{
	        "Demo.sayHello":{
	            returns:{"type":"string"},
	            parameters:[{"type":"string"}]
	        }
	    }
	});	
	var name = dojo.byId("demoName").value;
    var deferred = services.Demo.sayHello(name);
    deferred.addCallback(function(result) {
        dojo.byId("demoReply").innerHTML = result;
    });
}