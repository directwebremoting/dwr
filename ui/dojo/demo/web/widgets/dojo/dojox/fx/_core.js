/*
	Copyright (c) 2004-2008, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["dojox.fx._core"]){dojo._hasResource["dojox.fx._core"]=true;dojo.provide("dojox.fx._core");dojox.fx._Line=function(_1,_2){this.start=_1;this.end=_2;if(dojo.isArray(_1)){var _3=[];dojo.forEach(this.start,function(s,i){_3[i]=this.end[i]-s;},this);this.getValue=function(n){var _7=[];dojo.forEach(this.start,function(s,i){_7[i]=(_3[i]*n)+s;},this);return _7;};}else{var _3=_2-_1;this.getValue=function(n){return (_3*n)+this.start;};}};}