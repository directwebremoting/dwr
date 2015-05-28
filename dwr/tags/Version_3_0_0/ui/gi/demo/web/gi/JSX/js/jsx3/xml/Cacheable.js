/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.xml.Template","jsx3.xml.CDF");jsx3.Class.defineInterface("jsx3.xml.Cacheable",null,function(p,s){var
ub={k:"data",h:/\s*<\/?JSX_FF_WELLFORMED_WRAPPER( [^>]*)?\/?>\s*/g,c:"",p:"onChangeServer",q:"h7",v:"lV",i:"jsxxslparams",A:/\s*,\s*/g,a:"jsx:///xsl/xml.xsl",u:"onDestroy",t:"_jsxbG",j:"xml.err_load",s:"TC",z:"_XSL",d:"jsxasyncmessage",o:"xmlbind",D:"xml.trans_err",w:"loading",r:"onAfterAttach",C:"xml.trans_bad",B:",",l:"jsxassignids",g:"xml.err_trans",b:"JSX_XML_XSL",m:"1",f:"jsx3.xml.Cacheable.data",y:"xml.err_load_xsl",n:"_XML",x:"onXmlBinding",E:"3.00.00",e:"jsx3.xml.Cacheable."};var
t=jsx3.xml.Document;var
x=jsx3.xml.CDF;var
xb=jsx3.util.Logger.getLogger(p.jsxclass.getName());p.DEFAULTSTYLESHEET=jsx3.resolveURI(ub.a);p.DEFAULTXSLCACHEID=ub.b;p.CLEANUPRESOURCES=0;p.SHARERESOURCES=1;s.doTransform=function(h){var
ca=this.getXML();if(this.getNodeSet())ca=this.getNodeSet();this.yN(0,ca);var
N=this.getXSL();if(ca.hasError()||N.hasError())return ub.c;if(h==null)h=this.jsxxslparams;var
la=ub.c;var
K=null;if(N instanceof jsx3.xml.XslDocument){K=N;K.reset();}else K=new
jsx3.xml.Template(N);if(!K.hasError()){if(h)K.setParams(h);if(ca.getNamespaceURI()==jsx3.app.Cache.XSDNS){var
da=this.getServer();K.setParam(ub.d,da.getDynamicProperty(ub.e+ca.getNodeName(),h&&h.jsxtitle||da.getDynamicProperty(ub.f)));}la=K.transform(ca);if(K.hasError()){xb.error(jsx3._msg(ub.g,this,K.getError()));la=ub.c;}}else xb.error(jsx3._msg(ub.g,this,K.getError()));return la;};s.Al=function(q){return q.replace(ub.h,ub.c);};s.getXSLParams=function(){if(this.jsxxslparams==null)this.jsxxslparams={};return this.jsxxslparams;};s.setXSLParam=function(i,f){if(f!=null)this.getXSLParams()[i]=f;else delete this.getXSLParams()[i];return this;};s.removeXSLParam=function(h){delete this.getXSLParams()[h];return this;};s.removeXSLParams=function(){delete this[ub.i];return this;};s.getNodeSet=function(){return this._jsxbG;};s.setNodeSet=function(c){this._jsxbG=c;};s.resetCacheData=function(e){var
lb=(e||this.getServer()).getCache();lb.clearById(this.getXSLId());lb.clearById(this.getXMLId());};s.resetXmlCacheData=function(m){var
na=(m||this.getServer()).getCache();na.clearById(this.getXMLId());};s.resetXslCacheData=function(m){var
Cb=(m||this.getServer()).getCache();Cb.clearById(this.getXSLId());};s.clearXmlData=function(){this.getServer().getCache().setDocument(this.getXMLId(),x.newDocument());};s.getShareResources=function(){return this.jsxshare==null?0:this.jsxshare;};s.setShareResources=function(j){this.jsxshare=j;return this;};s.getXML=function(){var
zb=this.getServer();if(zb==null)return x.newDocument();var
bb=zb.getCache();var
sa=this.getXMLId();var
ga=bb.getDocument(sa);if(ga==null){var
Ua=this.getXMLString();if(!jsx3.util.strEmpty(Ua)){ga=new
t();ga.loadXML(Ua);}else{var
V=this.getXMLURL();if(!jsx3.util.strEmpty(V)){V=this.getUriResolver().resolveURI(V);if(this.jsxxmlasync){ga=bb.getOrOpenAsync(V,sa);}else ga=(new
t()).load(V);}else ga=x.newDocument();}if(ga.hasError()){xb.error(jsx3._msg(ub.j,this,ga.getError()));return ga;}ga=this.setSourceXML(ga,bb);}return ga;};s.setSourceXML=function(d,a){d=this._J(d);if(!a)a=this.getServer().getCache();var
M=this.getXMLId();if(a.getDocument(M)!=d)a.setDocument(this.getXMLId(),d);if(this.instanceOf(x)){if(d.getNodeName()==ub.k&&d.getAttribute(ub.l)==ub.m)this.assignIds();this.convertProperties(this.getServer().getProperties());}return d;};s.getXMLId=function(){return this.jsxxmlid||this.getId()+ub.n;};s.setXMLId=function(h){this.jsxxmlid=h;return this;};s.getXMLString=function(){return this.jsxxml;};s.setXMLString=function(o){this.jsxxml=o;return this;};s.getXMLURL=function(){return this.jsxxmlurl;};s.setXMLURL=function(h){this.jsxxmlurl=h;return this;};s.getXmlAsync=function(){return this.jsxxmlasync;};s.setXmlAsync=function(h){this.jsxxmlasync=jsx3.Boolean.valueOf(h);return this;};s.getXmlBind=function(){return this.jsxxmlbind;};s.setXmlBind=function(o){this.jsxxmlbind=jsx3.Boolean.valueOf(o);this.yN(this.jsxxmlbind);return this;};s.onXmlBinding=function(h){var
T=h.target.getDocument(h.subject);this.yN(0,T);if(this.publish)this.publish({subject:ub.o,xml:T});};s.h7=function(c,n){var
qb=n.getCache(),A=c.getCache();var
yb=this.getXMLId(),pb=this.getXSLId();var
Y=qb.getDocument(yb);var
mb=qb.getDocument(pb);if(this.getShareResources()!=1)this.resetCacheData(n);if(Y)A.setDocument(yb,Y);if(mb)A.setDocument(pb,mb);this.yN(false,0,n);this.yN(this.jsxxmlbind,0,c);};jsx3.app.Model.jsxclass.addMethodMixin(ub.p,p.jsxclass,ub.q);s.TC=function(){this.yN(this.jsxxmlbind);};jsx3.app.Model.jsxclass.addMethodMixin(ub.r,p.jsxclass,ub.s);s.lV=function(j){var
La=j.getServer();this.yN(false,0,La);if(this.getShareResources()==0)this.resetCacheData(La);delete this[ub.t];};jsx3.app.Model.jsxclass.addMethodMixin(ub.u,p.jsxclass,ub.v);s.yN=function(b,r,q){if(!q)q=this.getServer();if(q){var
v=q.getCache();var
U=this.getXMLId();var
Da=null;if(r){if(!this.jsxxmlbind)Da=!r.hasError()&&r.getNamespaceURI()==jsx3.app.Cache.XSDNS&&r.getNodeName()==ub.w;}else Da=b;if(Da!=null&&Boolean(this._jsxqs)!=Da){if(Da){v.subscribe(U,this,ub.x);}else v.unsubscribe(U,this);this._jsxqs=Da;}}};s.getXSL=function(){return this.qj(p.DEFAULTSTYLESHEET);};s.qj=function(i){var
Nb=jsx3.xml.XslDocument;var
X=this.getXSLId();var
Ka=this.getServer().getCache();var
ea=Ka.getDocument(X);if(ea==null){if(this.getXSLString()!=null){ea=(new
Nb()).loadXML(this.getXSLString());}else if(this.getXSLURL()!=null){ea=(new
Nb()).load(this.getUriResolver().resolveURI(this.getXSLURL()));}else ea=jsx3.getSharedCache().getOrOpenDocument(i,null,Nb.jsxclass);if(ea.hasError()){xb.error(jsx3._msg(ub.y,this,ea.getError()));return ea;}Ka.setDocument(X,ea);}return ea;};s.getXSLId=function(){var
Nb=null;Nb=this.jsxxslid;return Nb||this.getId()+ub.z;};s.setXSLId=function(j){this.jsxxslid=j;return this;};s.getXSLString=function(){return this.jsxxsl;};s.setXSLString=function(m){this.jsxxsl=m;return this;};s.getXSLURL=function(){return this.jsxxslurl;};s.setXSLURL=function(r){this.jsxxslurl=r;return this;};s.getXMLTransformers=function(){return this.jsxxmltrans!=null?this.jsxxmltrans.split(ub.A):[];};s.setXMLTransformers=function(c){this.jsxxmltrans=c!=null?c instanceof Array?c.join(ub.B):c:null;};s._J=function(m){var
da=this.getXMLTransformers();if(da.length>0){var
_=this.getServer();var
O=_.getCache();var
Ga=m;for(var
La=0;La<da.length;La++){var
Fa=da[La];var
u=O.getDocument(Fa);if(u==null){Fa=_.resolveURI(Fa);u=O.openDocument(Fa,Fa);}if(u==null||u.hasError()){xb.warn(jsx3._msg(ub.C,Fa,this,u.getError()));O.clearById(Fa);continue;}var
fb=new
jsx3.xml.Template(u);fb.setParams(this.getXSLParams());Ga=fb.transformToObject(Ga);if(fb.hasError())throw new
jsx3.Exception(jsx3._msg(ub.D,Fa,this,fb.getError()));if(Ga.hasError())throw new
jsx3.Exception(jsx3._msg(ub.D,Fa,this,Ga.getError()));}return Ga;}else return m;};p.getVersion=function(){return ub.E;};});jsx3.xml.Cacheable.prototype.resetData=jsx3.xml.Cacheable.prototype.clearXmlData;
