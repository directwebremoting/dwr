/*
	Copyright (c) 2004-2008, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["dojox.data.PersevereRestStore"]){dojo._hasResource["dojox.data.PersevereRestStore"]=true;dojo.provide("dojox.data.PersevereRestStore");dojo.require("dojox.data.JsonRestStore");dojo.require("dojox.json.ref");dojo.declare("dojox.data.PersevereRestStore",dojox.data.JsonRestStore,{getIdentity:function(_1){var _2=this.service.serviceName+"/";if(!_1._id){_1.id="../"+(_1._id="client/"+dojox.data.nextClientId++);}if(_1._id.substring(0,_2.length)!=_2){return "../"+_1._id;}return _1._id.substring(_2.length);}});dojox.data.nextClientId=0;}