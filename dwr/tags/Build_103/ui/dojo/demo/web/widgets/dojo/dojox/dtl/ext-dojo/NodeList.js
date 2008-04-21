if(!dojo._hasResource["dojox.dtl.ext-dojo.NodeList"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.dtl.ext-dojo.NodeList"] = true;
dojo.provide("dojox.dtl.ext-dojo.NodeList");
dojo.require("dojox.dtl._base");

dojo.extend(dojo.NodeList, {
	dtl: function(template, context){
		// args: dojox.dtl.__StringArgs|String
		//		The template string or location
		// context: dojox.dtl.__ObjectArgs|Object
		//		The context object or location
		var d = dojox.dtl;

		var self = this;
		var render = function(data){
			var content = template.render(new d._Context(context));
			self.forEach(function(node){
				node.innerHTML = content;
			});
		}

		d.text._resolveTemplateArg(template).addCallback(function(templateString){
			template = new d.Template(templateString);
			d.text._resolveContextArg(context).addCallback(function(contextObject){
				render(contextObject);
			});
		});

		return this;
	}
});

}
