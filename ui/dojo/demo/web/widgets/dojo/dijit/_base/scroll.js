/*
	Copyright (c) 2004-2008, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["dijit._base.scroll"]){dojo._hasResource["dijit._base.scroll"]=true;dojo.provide("dijit._base.scroll");dijit.scrollIntoView=function(_1){var _2=_1.parentNode;var _3=_2.scrollTop+dojo.marginBox(_2).h;var _4=_1.offsetTop+dojo.marginBox(_1).h;if(_3<_4){_2.scrollTop+=(_4-_3);}else{if(_2.scrollTop>_1.offsetTop){_2.scrollTop-=(_2.scrollTop-_1.offsetTop);}}};}