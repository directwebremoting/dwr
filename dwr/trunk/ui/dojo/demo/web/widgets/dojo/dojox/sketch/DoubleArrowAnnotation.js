if(!dojo._hasResource["dojox.sketch.DoubleArrowAnnotation"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.sketch.DoubleArrowAnnotation"] = true;
dojo.provide("dojox.sketch.DoubleArrowAnnotation");

dojo.require("dojox.sketch.Annotation");
dojo.require("dojox.sketch.Anchor");

(function(){
	var ta=dojox.sketch;
	ta.DoubleArrowAnnotation=function(figure, id){
		ta.Annotation.call(this, figure, id);
		this.transform={ dx:0, dy:0 };
		this.start={x:0, y:0};
		this.control={x:100, y:-50};
		this.end={x:200, y:0};
		this.textPosition={ x:0, y:0 };
		this.textOffset=6;
		this.textYOffset=10;
		this.textAlign="middle";
		this.startRotation=0;
		this.endRotation=0;

//		this.property('label',this.id);
		this.labelShape=null;
		this.pathShape=null;
		this.startArrow=null;
		this.startArrowGroup=null;
		this.endArrow=null;
		this.endArrowGroup=null;

		this.anchors.start=new ta.Anchor(this, "start");
		this.anchors.control=new ta.Anchor(this, "control");
		this.anchors.end=new ta.Anchor(this, "end");
	};
	ta.DoubleArrowAnnotation.prototype=new ta.Annotation;
	var p=ta.DoubleArrowAnnotation.prototype;
	p.constructor=ta.DoubleArrowAnnotation;

	p.type=function(){ return 'DoubleArrow'; };
	p.getType=function(){ return ta.DoubleArrowAnnotation; };

	p._rot=function(){
		//	arrowhead rotation
		var opp=this.start.y-this.control.y;
		var adj=this.start.x-this.control.x;
		if(!adj) adj=1;
		this.startRotation=Math.atan(opp/adj);
		opp=this.control.y-this.end.y;
		adj=this.control.x-this.end.x;
		if(!adj) adj=1;
		this.endRotation=Math.atan(opp/adj);
	};
	p._pos=function(){
		//	text position
		var offset=this.textOffset;

		//	figure out the pull of the curve and place accordingly
		if(this.control.y<this.end.y) offset*=-1;
		else offset+=this.textYOffset;
		var ab={x:((this.control.x-this.start.x)*.5)+this.start.x, y:((this.control.y-this.start.y)*.5)+this.start.y};
		var bc={x:((this.end.x-this.control.x)*.5)+this.control.x, y:((this.end.y-this.control.y)*.5)+this.control.y};
		this.textPosition={x:((bc.x-ab.x)*.5)+ab.x, y:(((bc.y-ab.y)*.5)+ab.y)+offset};
	};
	
	p.apply=function(obj){
		if(!obj) return;
		if(obj.documentElement) obj=obj.documentElement;
		this.readCommonAttrs(obj);
		
		for(var i=0; i<obj.childNodes.length; i++){
			var c=obj.childNodes[i];
			if(c.localName=="text") this.property('label',c.childNodes.length?c.childNodes[0].nodeValue:'');
			else if(c.localName=="path"){
				//	the line
				var d=c.getAttribute('d').split(" ");
				var s=d[0].split(",");
				this.start.x=parseFloat(s[0].substr(1),10);
				this.start.y=parseFloat(s[1],10);
				s=d[1].split(",");
				this.control.x=parseFloat(s[0].substr(1),10);
				this.control.y=parseFloat(s[1],10);
				s=d[2].split(",");
				this.end.x=parseFloat(s[0],10);
				this.end.y=parseFloat(s[1],10);
			}
		}
	};
	p.initialize=function(obj){
		//	create, based on passed DOM node if available.
		var font=(ta.Annotation.labelFont)?ta.Annotation.labelFont:{family:"Times", size:"16px"};
		this.apply(obj);

		//	calculate the other positions
		this._rot();
		this._pos();

		//	rotation matrix
		var rot=this.startRotation;
		if(this.control.x<this.start.x) rot+=Math.PI;
		var startRot=dojox.gfx.matrix.rotate(rot);

		rot=this.endRotation;
		if(this.control.x>=this.end.x) rot+=Math.PI;
		var endRot=dojox.gfx.matrix.rotateAt(rot, this.end.x, this.end.y);

		//	draw the shapes
		this.shape=this.figure.group.createGroup();
		this.shape.getEventSource().setAttribute("id", this.id);
		if(this.transform.dx||this.transform.dy) this.shape.setTransform(this.transform);
		this.pathShape=this.shape.createPath(
				"M"+this.start.x+" "+this.start.y+"Q"+this.control.x+" "+this.control.y+" "+this.end.x+" "+this.end.y + " l0,0"
			).setStroke(this.property('stroke'));
		
		this.startArrowGroup=this.shape.createGroup().setTransform({ dx:this.start.x, dy:this.start.y });
		this.startArrowGroup.applyTransform(startRot);
//		console.log('startArrow',this.property('fill'));
		this.startArrow=this.startArrowGroup.createPath("M0,0 l20,-5 -3,5 3,5 Z").setFill(this.property('fill'));
		
		this.endArrowGroup=this.shape.createGroup().setTransform(endRot);
		this.endArrow=this.endArrowGroup.createPath(
			"M" + this.end.x + "," + this.end.y + " l-20,-5 3,5 -3,5 Z"
		).setFill(this.property('fill'));
		this.labelShape=this.shape.createText({
			x:this.textPosition.x, y:this.textPosition.y, text:this.property('label'), align:this.textAlign
		}).setFont(font).setFill(this.property('fill'));
	};
	p.destroy=function(){
		if(!this.shape) return;
		this.startArrowGroup.remove(this.startArrow);
		this.endArrowGroup.remove(this.endArrow);
		this.shape.remove(this.startArrowGroup);
		this.shape.remove(this.endArrowGroup);
		this.shape.remove(this.pathShape);
		this.shape.remove(this.labelShape);
		this.figure.group.remove(this.shape);
		this.shape=this.pathShape=this.labelShape=this.startArrowGroup=this.startArrow=this.endArrowGroup=this.endArrow=null;
	};
	p.draw=function(obj){
		this.apply(obj);
		this._rot();
		this._pos();
		//	rotation matrix
		var rot=this.startRotation;
		if(this.control.x<this.start.x) rot+=Math.PI;
		var startRot=dojox.gfx.matrix.rotate(rot);
		rot=this.endRotation;
		if(this.control.x>=this.end.x) rot+=Math.PI;
		var endRot=dojox.gfx.matrix.rotateAt(rot, this.end.x, this.end.y);

		this.shape.setTransform(this.transform);
		this.pathShape.setShape(
				"M"+this.start.x+" "+this.start.y+" Q"+this.control.x+" "+this.control.y+" "+this.end.x+" "+this.end.y + " l0,0"
			).setStroke(this.property('stroke'));
		this.startArrowGroup.setTransform({ dx:this.start.x, dy:this.start.y });
		this.startArrowGroup.applyTransform(startRot);
		this.startArrow.setFill(this.property('fill'));
		
		this.endArrowGroup.setTransform(endRot);
		this.endArrow.setShape(
			"M" + this.end.x + "," + this.end.y + " l-20,-5 3,5 -3,5 Z"
		).setFill(this.property('fill'));
		this.labelShape.setShape({x:this.textPosition.x, y:this.textPosition.y, text:this.property('label')}).setFill(this.property('fill'));
	};
	p.getBBox=function(){
		var x=Math.min(this.start.x, this.control.x, this.end.x);
		var y=Math.min(this.start.y, this.control.y, this.end.y);
		var w=Math.max(this.start.x, this.control.x, this.end.x)-x;
		var h=Math.max(this.start.y, this.control.y, this.end.y)-y;
		return { x:x, y:y, width:w, height:h };
	};
	p.serialize=function(){
		var s=this.property('stroke');
		return '<g '+this.writeCommonAttrs()+'>'
			+ '<path style="stroke:'+s.color+';stroke-width:'+s.width+';fill:none;" d="'
			+ "M"+this.start.x+","+this.start.y+" "
			+ "Q"+this.control.x+","+this.control.y+" "
			+ this.end.x+","+this.end.y
			+ '" />'
			+ '<g transform="translate(' + this.start.x + "," + this.start.y + ") "
			+ 'rotate(' + (Math.round((this.startRotation*(180/Math.PI))*Math.pow(10,4))/Math.pow(10,4)) + ')">'
			+ '<path style="fill:'+s.color+';" d="M0,0 l20,-5, -3,5, 3,5 Z" />'
			+ '</g>'
			+ '<g transform="rotate('
			+ (Math.round((this.endRotation*(180/Math.PI))*Math.pow(10,4))/Math.pow(10,4))
			+ ", "+this.end.x+", "+this.end.y
			+ ')">'
			+ '<path style="fill:'+s.color+';" d="M'+this.end.x+","+this.end.y+' l-20,-5, 3,5, -3,5 Z" />'
			+ '</g>'
			+ '<text style="fill:'+s.color+';text-anchor:'+this.textAlign+'" font-weight="bold" '
			+ 'x="' + this.textPosition.x + '" '
			+ 'y="' + this.textPosition.y + '">'
			+ this.property('label')
			+ '</text>'
			+ '</g>';
	};

	ta.Annotation.register("DoubleArrow");
})();

}
