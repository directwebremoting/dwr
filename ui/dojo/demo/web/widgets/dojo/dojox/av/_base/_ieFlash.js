/*
	Copyright (c) 2004-2008, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


dojox.av.flash.place=function(_1,_2){_1=dojo.byId(_1);var o=dojox.av.flash.__ie_markup__(_2);if(o){_1.innerHTML=o.markup;return window[o.id];}return null;};if(dojo._initFired){dojox.av.flash.onInitialize();}else{dojo.addOnLoad(function(){dojox.av.flash.onInitialize();});}