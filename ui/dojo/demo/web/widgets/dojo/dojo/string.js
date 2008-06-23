/*
	Copyright (c) 2004-2008, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["dojo.string"]){dojo._hasResource["dojo.string"]=true;dojo.provide("dojo.string");dojo.string.pad=function(_1,_2,ch,_4){var _5=String(_1);if(!ch){ch="0";}while(_5.length<_2){if(_4){_5+=ch;}else{_5=ch+_5;}}return _5;};dojo.string.substitute=function(_6,_7,_8,_9){return _6.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g,function(_a,_b,_c){var _d=dojo.getObject(_b,false,_7);if(_c){_d=dojo.getObject(_c,false,_9)(_d);}if(_8){_d=_8(_d,_b);}return _d.toString();});};dojo.string.trim=function(_e){_e=_e.replace(/^\s+/,"");for(var i=_e.length-1;i>0;i--){if(/\S/.test(_e.charAt(i))){_e=_e.substring(0,i+1);break;}}return _e;};}