function update() {
   var name = dojo.byId("demoName").value;
   dojo.xhrGet({
   // The following URL must match that used to test the server.
      url: "../../../dwr/jsonp/Demo/sayHello/" + name,
      handleAs: "json",
      load: function(responseObject, ioArgs) {
         // Now you can just use the object
         dojo.byId("demoReply").innerHTML = responseObject.reply;
      }
  });
}
