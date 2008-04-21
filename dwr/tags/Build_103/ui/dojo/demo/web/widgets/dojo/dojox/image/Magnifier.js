if(!dojo._hasResource["dojox.image.Magnifier"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.image.Magnifier"] = true;
dojo.provide("dojox.image.Magnifier");

dojo.require("dojox.gfx");
dojo.require("dojox.image.MagnifierLite");

dojo.declare("dojox.image.Magnifier",
	[dojox.image.MagnifierLite],{
	// summary:	Adds magnification on a portion of an image element, using dojox.gfx
	//
	// description: An unobtrusive way to add an unstyled overlay
	// 		above the srcNode image element. The overlay/glass is a 
	//		scaled version of the src image (so larger images sized down
	//		are clearer).
	//
	//		over-ride the _createGlass method to create your custom surface,
	//		being sure to create an img node on that surface.

	_createGlass: function(){
		// summary: create the glassNode, and an img on a dojox.gfx surface

		// images are hard to make into workable templates, so just add outer overlay
		// and skip using dijit._Templated
		this.glassNode = dojo.doc.createElement('div');
		this.surfaceNode = this.glassNode.appendChild(dojo.doc.createElement('div'));

		dojo.addClass(this.glassNode,"glassNode");
		dojo.body().appendChild(this.glassNode);

		with(this.glassNode.style){
			height = this.glassSize + "px";
			width = this.glassSize + "px";
		}
		
		this.surface = dojox.gfx.createSurface(this.surfaceNode, this.glassSize, this.glassSize);
		this.img = this.surface.createImage({
		   src:this.domNode.src,
		   width:this._zoomSize.w,
		   height:this._zoomSize.h
		});

	},

	_placeGlass: function(e){
		// summary: position the overlay centered under the cursor
		var x = e.pageX - 2;
		var y = e.pageY - 2 ;
		var xMax = this.offset.x + this.offset.w + 2;
		var yMax = this.offset.y + this.offset.h + 2;
		
		// with svg, our mouseout connection to the image surface doesn't
		// fire, so we'r have to manually calculate offsets	
		if(x<this.offset.x || y<this.offset.y || x>xMax || y>yMax){
			this._hideGlass();
		}else{
			this.inherited(arguments);
		}
	},

	_setImage: function(e){
		// summary: set the image's offset in the clipping window relative to the mouse position

		var xOff = (e.pageX - this.offset.l) / this.offset.w;
		var yOff = (e.pageY - this.offset.t) / this.offset.h;
		var x = (this._zoomSize.w * xOff * -1)+(this.glassSize*xOff);
		var y = (this._zoomSize.h * yOff * -1)+(this.glassSize*yOff);
		// set the image offset
		this.img.setShape({ x: x, y:y });	

	}

});

}
