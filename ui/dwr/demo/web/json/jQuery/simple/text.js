
 function update() {
	  var name = dwr.util.getValue("demoName");
	  var postParam = $.param({param0: name});
	  $.post("../../../dwr/jsonp/Demo/sayHello/", postParam,
			  function(data) {
		        dwr.util.setValue("demoReply", data.reply);
      }, "jsonp"); 
  }