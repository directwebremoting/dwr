/*
	Copyright (c) 2004-2008, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["dijit.form.SimpleTextarea"]){dojo._hasResource["dijit.form.SimpleTextarea"]=true;dojo.provide("dijit.form.SimpleTextarea");dojo.require("dijit.form._FormWidget");dojo.declare("dijit.form.SimpleTextarea",dijit.form._FormValueWidget,{baseClass:"dijitTextArea",attributeMap:dojo.mixin(dojo.clone(dijit.form._FormValueWidget.prototype.attributeMap),{rows:"focusNode",cols:"focusNode"}),rows:"",cols:"",templateString:"<textarea name='${name}' dojoAttachPoint='focusNode,containerNode'>",postMixInProperties:function(){if(this.srcNodeRef){this.value=this.srcNodeRef.value;}},setValue:function(_1){this.domNode.value=_1;this.inherited(arguments);},getValue:function(){return this.domNode.value.replace(/\r/g,"");}});}