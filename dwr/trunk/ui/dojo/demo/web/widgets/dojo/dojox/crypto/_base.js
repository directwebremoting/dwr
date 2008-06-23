/*
	Copyright (c) 2004-2008, The Dojo Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/


if(!dojo._hasResource["dojox.crypto._base"]){dojo._hasResource["dojox.crypto._base"]=true;dojo.provide("dojox.crypto._base");dojo.require("dojox.encoding.crypto._base");dojo.require("dojox.encoding.digests._base");dojo.deprecated("dojox.crypto._base","DojoX cryptography has been merged into DojoX Encoding. To use, include dojox.encoding.digests and/or dojox.encoding.crypto.","1.2");dojox.crypto._base=dojo.mixin(dojox.encoding.crypto._base,dojox.encoding.digests._base);}