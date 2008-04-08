/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Image",jsx3.gui.Block,[],function(s,h){var
ub={d:' height="',a:' width="',c:"",f:' class="jsx30image" src="',g:"/>",b:'"',e:"<img"};h.paint=function(){this.applyDynamicProperties();var
V=this.Wl(true);var
Db=this.getUriResolver().resolveURI(this.jsxsrc);var
Na=this.getWidth()!=null?ub.a+V.mn()+ub.b:ub.c;var
F=this.getHeight()!=null?ub.d+V.ec()+ub.b:ub.c;return this.jsxsuper(ub.e+jsx3.html.Kf+ub.f+Db+ub.b+Na+F+ub.g);};h.onSetChild=function(p){return false;};h.Rc=function(p,m,b){this.Gi(p,m,b,1);};h.getRenderedWidth=function(){var
qb=this.getRendered();return qb&&qb.childNodes[0]?qb.childNodes[0].width:null;};h.getRenderedHeight=function(){var
Ea=this.getRendered();return Ea&&Ea.childNodes[0]?Ea.childNodes[0].height:null;};h.getSrc=function(){return this.jsxsrc;};h.setSrc=function(b){this.jsxsrc=b;return this;};});
