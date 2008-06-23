/*
	Copyright (c) 2004-2008, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["dijit._base.window"]){dojo._hasResource["dijit._base.window"]=true;dojo.provide("dijit._base.window");dijit.getDocumentWindow=function(_1){if(dojo.isSafari&&!_1._parentWindow){var _2=function(_3){_3.document._parentWindow=_3;for(var i=0;i<_3.frames.length;i++){_2(_3.frames[i]);}};_2(window.top);}if(dojo.isIE&&window!==document.parentWindow&&!_1._parentWindow){_1.parentWindow.execScript("document._parentWindow = window;","Javascript");var _5=_1._parentWindow;_1._parentWindow=null;return _5;}return _1._parentWindow||_1.parentWindow||_1.defaultView;};}