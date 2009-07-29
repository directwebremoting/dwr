
 function update() {
	  var name = dwr.util.getValue("demoName");
	  $.post("../../../dwr/jsonp/Demo/sayHello/" + name, { },
			  function(data) {
		        dwr.util.setValue("demoReply", data.reply);
      }, "jsonp"); 
  }