if(!dojo._hasResource["dojox.sketch.Toolbar"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojox.sketch.Toolbar"] = true;
dojo.provide("dojox.sketch.Toolbar");

dojo.require("dojox.sketch.Annotation");
dojo.require("dijit.Toolbar");
dojo.require("dijit.form.Button");
dojo.require("dijit.form.Slider");

dojo.declare("dojox.sketch.ButtonGroup", null, {
	constructor: function(){
		this._childMaps={};
		this._children=[];
	},
	add: function(/*_Plugin*/ plugin){
		this._childMaps[plugin]=plugin.connect(plugin,'onActivate',dojo.hitch(this,'_resetGroup',plugin));
		this._children.push(plugin);
	},
//	remove: function(/*_Plugin*/ plugin){
//		widget.disconnect(this._childMaps[widget.id]);
//		delete this._childMaps[widget.id];
//		this._children.splice(this._children.indexOf(widget.id),1);
//	},
	_resetGroup: function(p){
		var cs=this._children;
		dojo.forEach(cs,function(c){
			if(p!=c && c['attr']){
				c.attr('checked',false);
			}
		});
	}
});

dojo.declare("dojox.sketch.Toolbar", dijit.Toolbar, {
	figure: null,
	plugins: null,
	postCreate: function(){
		this.inherited(arguments);
		this.shapeGroup=new dojox.sketch.ButtonGroup;

		this.connect(this.figure,'onLoad','reset');
		if(!this.plugins){
			this.plugins=['Slider','Lead','SingleArrow','DoubleArrow','Underline','Preexisting'];
		}
		this._plugins=[];

		dojo.forEach(this.plugins,function(obj){
			var name=dojo.isString(obj)?obj:obj.name;
			var p=new dojox.sketch.tools[name](obj.args||{});
			this._plugins.push(p);
			p.setFigure(this.figure);
			p.setToolbar(this);
			if(!this._defaultTool && p.button){
				this._defaultTool=p;
			}
		},this);
	},
	destroy: function(){
		dojo.forEach(this._plugins,function(p){
			p.destroy();
		});
		this.inherited(arguments);
		delete this._defaultTool;
		delete this._plugins;
	},
	addGroupItem: function(/*_Plugin*/item,group){
		if(group!='toolsGroup'){
			console.error('not supported group '+group);
			return;
		}

		this.shapeGroup.add(item);
	},
	reset: function(){
		this._defaultTool.activate();
	},
	_setShape: function(s){
		if(!this.figure.surface) return;
		//	now do the action.
		if(this.figure.hasSelections()){
			for(var i=0; i<this.figure.selected.length; i++){
				var before=this.figure.selected[i].serialize();
				this.figure.convert(this.figure.selected[i], s);
				this.figure.history.add(ta.CommandTypes.Convert, this.figure.selected[i], before);
			}
		}
	}
});

dojox.sketch.makeToolbar=function(node,figure){
	var toolbar=new dojox.sketch.Toolbar({"figure":figure});
	node.appendChild(toolbar.domNode);
	return toolbar;
};

}
