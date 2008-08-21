dojo.provide("dojox.gfx");

dojo.require("dojox.gfx.matrix");
dojo.require("dojox.gfx._base");

dojo.loadInit(function(){
	//Since loaderInit can be fired before any dojo.provide/require calls,
	//make sure the dojox.gfx object exists and only run this logic if dojox.gfx.renderer
	//has not been defined yet.
	var gfx = dojo.getObject("dojox.gfx", true), sl, flag;
	if(!gfx.renderer){
		var renderers = (typeof dojo.config["gfxRenderer"] == "string" ?
			dojo.config["gfxRenderer"] : "svg,vml,silverlight,canvas").split(",");
		for(var i = 0; i < renderers.length; ++i){
			switch(renderers[i]){
				case "svg":
					//TODO: need more comprehensive test for SVG
					if(!dojo.isIE && (navigator.userAgent.indexOf("iPhone") < 0) && (navigator.userAgent.indexOf("iPod") < 0)){ dojox.gfx.renderer = "svg"; }
					break;
				case "vml":
					if(dojo.isIE != 0){ dojox.gfx.renderer = "vml"; }
					break;
				case "silverlight":
					try{
						if(dojo.isIE){
							sl = new ActiveXObject("AgControl.AgControl");
							if(sl && sl.IsVersionSupported("1.0")){
								flag = true;
							}
						}else{
							if(navigator.plugins["Silverlight Plug-In"]){
								flag = true;
							}
						}
					}catch(e){
						flag = false;
					}finally{
						sl = null;
					}
					if(flag){ dojox.gfx.renderer = "silverlight"; }
					break;
				case "canvas":
					//TODO: need more comprehensive test for Canvas
					if(dojo.isIE == 0){ dojox.gfx.renderer = "canvas"; }
					break;
			}
			if(dojox.gfx.renderer){ break; }
		}
		console.log("gfx renderer = " + dojox.gfx.renderer);
	}
});

// include a renderer conditionally
dojo.requireIf(dojox.gfx.renderer == "svg", "dojox.gfx.svg");
dojo.requireIf(dojox.gfx.renderer == "vml", "dojox.gfx.vml");
dojo.requireIf(dojox.gfx.renderer == "silverlight", "dojox.gfx.silverlight");
dojo.requireIf(dojox.gfx.renderer == "canvas", "dojox.gfx.canvas");
