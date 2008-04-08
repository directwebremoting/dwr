/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.xml.Cacheable","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.BlockX",jsx3.gui.Block,[jsx3.xml.Cacheable],function(a,g){var
ub={d:"jsx3.gui.BlockX.",a:"loading",c:"xml.err_load_xsl",f:"xE",g:"3.00.00",b:"error",e:"jsxdisableescape"};g.init=function(n,k,f,l,i){this.jsxsuper(n,k,f,l,i);};g.paint=function(){this.applyDynamicProperties();if(this.jsxxslasync){var
Ta=this.getXSL();if(!Ta.hasError()&&Ta.getNamespaceURI()==jsx3.app.Cache.XSDNS){var
Da=Ta.getNodeName();if(Da==ub.a)this.FI(true);else if(Da==ub.b)jsx3.util.Logger.getLogger(a.jsxclass.getName()).error(jsx3._msg(ub.c,this,Ta.getAttribute(ub.b)));return this.jsxsuper(this.wi(ub.d+Ta.getNodeName()));}}return this.jsxsuper(this.doTransform());};g.onAfterPaint=function(n){var
Ia=jsx3.xml.Template;if(!Ia.supports(1)&&this.getXSLParams(ub.e))jsx3.html.removeOutputEscaping(n);};g.getXSLString=function(){return this.jsxxsl;};g.setXSLString=function(r){this.jsxxsl=r;return this;};g.getXSLURL=function(){return this.jsxxslurl;};g.setXSLURL=function(e){this.jsxxslurl=e;return this;};g.getXslAsync=function(){return this.jsxxslasync;};g.setXslAsync=function(n){this.jsxxslasync=jsx3.Boolean.valueOf(n);return this;};g.onXmlBinding=function(k){this.jsxsupermix(k);this.repaint();};g.FI=function(m){if(Boolean(this._jsxxslbound)!=m){var
C=this.getServer();if(C){var
A=C.getCache();var
v=this.getXSLId();if(m)A.subscribe(v,this,ub.f);else A.unsubscribe(v,this);this._jsxxslbound=m;}}};g.xE=function(s){this.FI(false);this.repaint();};g.getXSL=function(){var
y=jsx3.xml.XslDocument;var
L=this.getXSLId();var
Jb=this.getServer().getCache();var
X=Jb.getDocument(L);if(X==null){if(this.getXSLString()!=null){X=(new
y()).loadXML(this.getXSLString());}else if(this.getXSLURL()!=null){var
Bb=this.getUriResolver().resolveURI(this.getXSLURL());if(this.jsxxslasync){X=Jb.getOrOpenAsync(Bb,L);}else X=(new
y()).load(Bb);}else X=jsx3.getSharedCache().getOrOpenDocument(jsx3.xml.Cacheable.DEFAULTSTYLESHEET,null,y.jsxclass);if(X.hasError()){jsx3.util.Logger.getLogger(a.jsxclass.getName()).error(jsx3._msg(ub.c,this,X.getError()));return X;}if(!this.jsxxslasync)Jb.setDocument(L,X);}return X;};a.getVersion=function(){return ub.g;};});jsx3.BlockX=jsx3.gui.BlockX;
