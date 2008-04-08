/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.Class.defineClass("jsx3.net.Form",null,[jsx3.util.EventDispatcher],function(q,d){var
ub={k:'<html><body class="jsx30form">',O:"Text",p:"_frame' style='width:100%;height:100%;'></iframe></span>",P:"string",q:"_B",V:"-50px",v:"method",I:"htfrm.sec",a:"get",F:"htfrm.prompt",u:"htfrm.bad_frag",U:"px",j:'<form method="get" action=""><input type="submit"/></form>',d:"multipart/form-data",z:"<div>",S:"<html/>",D:": <input type='file' name='",w:"action",K:"loaded",R:"htfrm.bad_dt",g:"error",B:"'/></div>",b:"post",Q:"htfrm.bad_xml",M:"XML",f:"response",y:"object",T:"htfrm.destr",x:"encoding",e:"file",c:"application/x-www-form-urlencoded",h:"timeout",W:"0px",H:"",G:"htfrm.no_file",i:"jsx_httpform_",A:": <input type='text' name='",t:"form",N:"HTML",s:"htfrm.no_ifr",X:"10px",o:"_span' style='border:2px solid black;position:absolute;left:-50px;top:0px;width:10px;height:10px;overflow:hidden;'><iframe id='",r:"_frame",C:" ",l:"</body></html>",L:"unknown",m:"beforeEnd",J:"complete",n:"<span id='",E:"htfrm.dup"};var
Lb=jsx3.util.Logger.getLogger(q.jsxclass.getName());q.lp=0;q.oS=250;q.t6=30000;q.METHOD_GET=ub.a;q.METHOD_POST=ub.b;q.SJ=ub.c;q.NH=ub.d;q.EVENT_FILE_SELECTED=ub.e;q.EVENT_ON_RESPONSE=ub.f;q.EVENT_ON_ERROR=ub.g;q.EVENT_ON_TIMEOUT=ub.h;d.init=function(g,s,m){this.ST=ub.i+q.lp++;if(g==-1){this.XW(s);}else{this.XW();this.setMethod(g!=null?g:ub.a);this.setAction(s);this.setMultipart(m);}};q.newFromFragment=function(k){return new
q(-1,k);};d.XW=function(l){var
qa=l;if(!qa)qa=ub.j;var
ab=ub.k+qa+ub.l;jsx3.html.insertAdjacentHTML(document.body,ub.m,ub.n+this.ST+ub.o+this.ST+ub.p);this._B=document.getElementById(this.ST+ub.q);this.LR=this.eval(this.ST+ub.r);var
t=this.LR.document||this.LR.contentDocument;if(t==null)throw new
jsx3.Exception(jsx3._msg(ub.s,this));t.open();t.write(ab);t.close();this._form=t.getElementsByTagName(ub.t)[0];if(l){if(this._form==null)throw new
jsx3.Exception(jsx3._msg(ub.u,l));this.s6=this._form.action;this.P1=this._form.method;this.X3=this._form.encoding&&this._form.encoding.toLowerCase()==q.NH;}};d.getMethod=function(){return this.P1;};d.setMethod=function(m){m=m!=null?m.toLowerCase():ub.a;this.P1=m;this.t7().setAttribute(ub.v,m);};d.getAction=function(){return this.s6;};d.setAction=function(b){if(!jsx3.CLASS_LOADER.IE)b=jsx3.app.Browser.getLocation().resolve(b).toString();this.s6=b;this.t7().setAttribute(ub.w,b);};d.getMultipart=function(){return this.X3;};d.setMultipart=function(r){this.X3=r;this.t7().setAttribute(ub.x,r?q.NH:q.SJ);};d.M6=function(){return this._B;};d.mN=function(){return this.LR;};d.t7=function(){return this._form;};d.getField=function(k){var
Z=this.t7().elements[k];return Z!=null&&typeof Z==ub.y?Z.value:null;};d.getFields=function(){var
Cb=[];var
ab=this.t7().elements;for(var ea in ab)Cb[ea]=ab[ea].name;return Cb;};d.setField=function(i,f,m){var
Ja=this.t7();var
_=Ja.elements[i];if(_==null||typeof _!=ub.y){jsx3.html.insertAdjacentHTML(Ja,ub.m,ub.z+i+ub.A+i+ub.B);_=Ja.elements[i];}if(m&&_.value){_.value=_.value+ub.C+f;}else _.value=f;};d.removeField=function(f){var
db=this.t7();var
w=db.elements[f];if(w!=null&&w.parentNode)jsx3.html.removeNode(w.parentNode);};d.addFileUploadField=function(a){var
S=this.t7();var
Ta=S.elements[a];if(Ta==null||typeof Ta!=ub.y){jsx3.html.insertAdjacentHTML(S,ub.m,ub.z+a+ub.D+a+ub.B);Ta=S.elements[a];var
J=this;Ta.onchange=function(){J.wW(a,Ta.value);};}else throw new
jsx3.Exception(jsx3._msg(ub.E,this,a));};d.promptForFile=function(o){var
va=this.t7();var
Wa=va.elements[o];if(Wa!=null&&Wa.type==ub.e){if(jsx3.CLASS_LOADER.IE){Wa.click();}else{Lb.warn(jsx3._msg(ub.F));Wa.click();}}else throw new
jsx3.Exception(jsx3._msg(ub.G,this,o));};d.wW=function(i,g){this.publish({subject:ub.e,field:i,value:g});};d.Yr=function(){this._form=null;this.publish({subject:ub.f});};d.dF=function(f){this._form=null;this.publish({subject:ub.g,message:f});};d.abort=function(){window.clearInterval(this.rN);};if(jsx3.CLASS_LOADER.IE){d.send=function(p,j){if(p==null)p=q.oS;if(j==null)j=q.t6;this._form.submit();var
bb=0;var
_a=j<=0?Number.MAX_VALUE:Math.ceil(j/p);var
J=this;this.rN=window.setInterval(function(){J.e0(++bb<_a);},p);};d.e0=function(k){try{this.LR.document.readyState==ub.H;}catch(Kb){window.clearInterval(this.rN);this.rN=null;this.dF(jsx3._msg(ub.I,this,jsx3.NativeError.wrap(Kb)));return;}if(this.LR.document.readyState==ub.J||this.LR.document.readyState==ub.K){window.clearInterval(this.rN);this.rN=null;this.Yr();}else if(!k){window.clearInterval(this.rN);this.rN=null;this.destroy();this.publish({subject:ub.h});}};d.getResponseText=function(){var
pa=this.LR.document;if(typeof pa.mimeType!=ub.L)if(pa.mimeType.indexOf(ub.M)==0){return pa.XMLDocument.xml;}else if(pa.mimeType.indexOf(ub.N)==0){return jsx3.html.getOuterHTML(this.LR.document.childNodes[0]);}else if(pa.mimeType.indexOf(ub.O)==0)return this.LR.document.childNodes[0].innerText;return this.LR.document.body.innerHTML;};d.getResponseXML=function(){var
da=this.LR.document;if(typeof da.mimeType==ub.P&&da.mimeType.indexOf(ub.M)==0||da.XMLDocument&&da.XMLDocument.documentElement){var
H=new
jsx3.xml.Document();H.loadXML(da.XMLDocument.documentElement.xml);return H;}else{var
vb=null;if(da.mimeType.indexOf(ub.N)==0){vb=jsx3.html.getOuterHTML(this.LR.document.childNodes[0]);}else if(da.mimeType.indexOf(ub.O)==0){vb=this.LR.document.childNodes[0].innerText;}else vb=this.LR.document.body.innerHTML;var
xb=new
jsx3.xml.Document();xb.loadXML(vb);if(xb.hasError()){Lb.error(jsx3._msg(ub.Q,xb.getError()));return null;}return xb;}};}else{d.send=function(i,c){if(i==null)i=q.oS;if(c==null)c=q.t6;var
D=this;this.LR.onload=function(){D.Ap();};try{this._form.submit();}catch(Kb){this.dF(jsx3.NativeError.wrap(Kb).toString());return;}this.rN=window.setTimeout(function(){D.pV();},i*c);};d.Ap=function(){this.LR.onload=null;if(this.rN){window.clearTimeout(this.rN);this.rN=null;}try{var
D=ub.C+this.LR.contentDocument.childNodes[0].innerHTML;}catch(Kb){this.publish({subject:ub.g,message:jsx3.NativeError.wrap(Kb).toString()});return;}this.Yr();};d.pV=function(){this.LR.onload=null;this.rN=null;this.destroy();this.publish({subject:ub.h});};d.getResponseText=function(){var
ja=this.LR.contentDocument;if(ja instanceof XMLDocument){return (new
XMLSerializer()).serializeToString(ja);}else if(ja instanceof HTMLDocument){return ja.childNodes[0].innerHTML;}else{Lb.warn(jsx3._msg(ub.R,ja));return ub.H;}};d.getResponseXML=function(){var
Ka=this.LR.contentDocument;if(Ka instanceof XMLDocument){var
Ya=(new
XMLSerializer()).serializeToString(Ka);return (new
jsx3.xml.Document()).loadXML(Ya);}else if(Ka instanceof HTMLDocument){var
Ya=Ka.childNodes[0].innerHTML;return (new
jsx3.xml.Document()).loadXML(Ya);}else{Lb.warn(jsx3._msg(ub.R,Ka));return (new
jsx3.xml.Document()).loadXML(ub.S);}};}d.destroy=function(){var
na=this.M6();if(na!=null){jsx3.html.removeNode(na);this._B=null;this.LR=null;this._form=null;}else Lb.warn(jsx3._msg(ub.T,this));};d.reveal=function(r,j,g,c){if(r==null)r=10;if(j==null)j=10;if(g==null)g=Math.round(this._B.parentNode.offsetWidth/3);if(c==null)c=Math.round(this._B.parentNode.offsetHeight/3);var
mb=this._B.style;mb.left=r+ub.U;mb.top=j+ub.U;mb.width=g+ub.U;mb.height=c+ub.U;mb.zIndex=30000;};d.conceal=function(){var
la=this._B.style;la.left=ub.V;la.top=ub.W;la.width=ub.X;la.height=ub.X;la.zIndex=0;};});jsx3.HttpForm=jsx3.net.Form;
