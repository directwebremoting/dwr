/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Slider",jsx3.gui.Block,[jsx3.gui.Form],function(l,r){var
ub={O:"P9",k:"%",h:"border:solid 1px #9898a5;border-right-color:#ffffff;border-top-color:#ffffff;",c:"jsx:///images/slider/left.gif",H:' class="handle',p:"div",P:"mouseup",q:"inline",G:"sx",v:"mousewheel",I:"_disabled",i:"px",A:"visibility:hidden;",a:"jsx:///images/slider/top.gif",F:"mousedown",u:"keydown",t:"jsxchange",j:"string",N:"mousemove",s:"0 ",S:";",z:'" ',d:"jsx:///images/slider/right.gif",o:"",D:' class="jsx30slider_track" ',R:"jsxincchange",K:"background-image:url(",w:'id="',r:" ",C:"Ut",B:"click",l:"relativebox",g:"border:solid 1px #9898a5;border-right-color:#ffffff;border-bottom-color:#ffffff;",Q:"v5",L:");",b:"jsx:///images/slider/bottom.gif",M:"&#160;",m:"box",f:"filter:progid:DXImageTransform.Microsoft.Gradient(GradientType=1, StartColorStr=#aaffffff, EndColorStr=#FF9090af);",J:'"',T:"3.0.00",y:'label="',n:"span",x:'" class="jsx30slider" ',E:'class="handle"',e:"filter:progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr=#aaffffff, EndColorStr=#FF9090af);"};var
zb=jsx3.gui.Event;l.HORIZONTAL=0;l.VERTICAL=1;l.O2=100;l.gT=15;l.Oo=7;l.TI=l.gT;l.KE=15;l.L5=100;l.r6=ub.a;l.d7=ub.b;l.HV=ub.c;l.AE=ub.d;l.oP=ub.e;l._6=ub.f;l.NR=ub.g;l.Qo=ub.h;r.jsxlength=100;r.jsxvalue=0;r.jsxpainttrack=1;r.jsxtrackclick=1;r.jsximg=null;r.init=function(b,d,i,j){this.jsxsuper(b,d,i);this.jsxlength=j;};r.getValue=function(){return this.jsxvalue!=null?this.jsxvalue:0;};r.setValue=function(h){this.jsxvalue=h==null?null:Math.max(0,Math.min(l.L5,Number(h)));this.Ku();this.ce();return this;};r.doValidate=function(){return this.setValidationState(1).getValidationState();};r.getLength=function(){return this.jsxlength!=null?this.jsxlength:l.O2;};r.setLength=function(k,o){this.jsxlength=k;this.ce();if(o)this.repaint();return this;};r.setWidth=function(f,j){if(this.getOrientation()==0)this.setLength(f,j);return this;};r.setHeight=function(h,e){if(this.getOrientation()==1)this.setLength(h,e);return this;};r.getOrientation=function(){return this.jsxorientation!=null?this.jsxorientation:0;};r.setOrientation=function(c){this.jsxorientation=c;this.ce();return this;};r.getPaintTrack=function(){return this.jsxpainttrack!=null?this.jsxpainttrack:1;};r.setPaintTrack=function(f){this.jsxpainttrack=f;return this;};r.getTrackClickable=function(){return this.jsxtrackclick!=null?this.jsxtrackclick:1;};r.setTrackClickable=function(b){this.jsxtrackclick=b;return this;};r.getHandleImage=function(){return this.jsximg;};r.setHandleImage=function(k){this.jsximg=k;return this;};r.Ku=function(){var
P=this.getRendered();if(P!=null){var
t=jsx3.html.selectSingleElm(P,0,0);var
lb=jsx3.html.selectSingleElm(P,0,1);if(this.getOrientation()==0){var
eb=parseInt(t.offsetWidth)-parseInt(lb.offsetWidth);lb.style.left=Math.round(this.getValue()*eb/l.L5)+ub.i;lb.style.top=Math.round((l.gT-l.TI)/2)+ub.i;}else{var
u=parseInt(t.offsetHeight)-parseInt(lb.offsetHeight);lb.style.left=Math.round((l.gT-l.TI)/2)+ub.i;lb.style.top=u-Math.round(this.getValue()*u/l.L5)+ub.i;}}};r.Rc=function(f,c,h){var
Ma=this.Wl(true,f);if(c){var
Z=f.width!=null&&Ma.implicit.width!=f.width||f.parentwidth!=Ma.implicit.parentwidth&&typeof Ma.implicit.width==ub.j&&Ma.implicit.width.indexOf(ub.k)>0||f.height!=null&&Ma.implicit.height!=f.height||f.parentheight!=Ma.implicit.parentheight&&typeof Ma.implicit.height==ub.j&&Ma.implicit.height.indexOf(ub.k)>0;var
B=Ma.recalculate(f,c,h);if(!B.w&&!B.h)return;var
Mb=Ma.lg(0);Mb.recalculate({width:Ma.mn(),height:Ma.ec()},c?c.childNodes[0]:null,h);var
va=Mb.lg(0);va.recalculate({width:Mb.mn(),height:Mb.ec()},c?c.childNodes[0].childNodes[0]:null,h);if(Z)this.Ku();}};r.Gc=function(g){if(this.getParent()&&(g==null||isNaN(g.parentwidth)||isNaN(g.parentheight))){g=this.getParent().uf(this);}else if(g==null)g={};var
I=this.getRelativePosition()!=0&&(!this.getRelativePosition()||this.getRelativePosition()==1);var
Q=I?null:this.getLeft();var
Fb=I?null:this.getTop();var
W=this.getOrientation()==0;if(!g.boxtype)g.boxtype=I?ub.l:ub.m;g.tagname=ub.n;if(g.left==null&&Q!=null)g.left=Q;if(g.top==null&&Fb!=null)g.top=Fb;if(W){g.height=l.gT;g.width=this.getLength();}else{g.height=this.getLength();g.width=l.gT;}var
V=this.getMargin();if(I&&V!=null&&V!=ub.o)g.margin=V;var
cb=new
jsx3.gui.Painted.Box(g);var
Ca={};Ca.tagname=ub.p;Ca.boxtype=ub.q;Ca.width=cb.mn();Ca.height=cb.ec();var
P=Math.round((l.gT-l.Oo)/2)+ub.r;var
C=ub.s;Ca.padding=W?P+C+P+C:C+P+C+P;var
Ka=new
jsx3.gui.Painted.Box(Ca);cb.Yf(Ka);Ca={};Ca.tagname=ub.p;Ca.boxtype=ub.q;if(W){Ca.height=l.Oo;Ca.width=Ka.mn();}else{Ca.height=Ka.ec();Ca.width=l.Oo;}Ca.border=this.getBorder()?this.getBorder():this.getOrientation()==0?l.NR:l.Qo;var
Va=new
jsx3.gui.Painted.Box(Ca);Ka.Yf(Va);var
ob=W?Math.round(this.getValue()/l.L5*(Ka.mn()-l.KE)):Math.floor((l.gT-l.TI)/2);var
ja=W?Math.floor((l.gT-l.TI)/2):Math.round(Ka.ec()-l.KE-this.getValue()/l.L5*(Ka.ec()-l.KE));var
Db=W?l.KE:l.TI;var
oa=W?l.TI:l.KE;Ca={};Ca.tagname=ub.p;Ca.boxtype=ub.m;Ca.left=ob;Ca.top=ja;Ca.width=Db;Ca.height=oa;var
Ya=new
jsx3.gui.Painted.Box(Ca);cb.Yf(Ya);Ca={};Ca.tagname=ub.p;Ca.boxtype=ub.q;Ca.width=Db;Ca.height=oa;var
wa=new
jsx3.gui.Painted.Box(Ca);Ya.Yf(wa);return cb;};r.oe=function(b,k){if(!b.hasModifier())if(b.isArrowKey()){if(b.upArrow()||b.rightArrow())this.G6(b,1);else if(b.downArrow()||b.leftArrow())this.G6(b,-1);b.cancelAll();}};r.lk=function(e,h){var
rb=e.getWheelDelta();if(rb!=0)this.G6(e,rb>0?1:-1);e.cancelBubble();};r.G6=function(f,h){var
Ab;if(h>0){if(this.jsxvalue>=l.L5)return;Ab=Math.floor(this.jsxvalue+h);while(Ab<l.L5){if(this.jsxvalue<this.constrainValue(Ab))break;Ab=Ab+h;}}else{if(this.jsxvalue<=0)return;Ab=Math.ceil(this.jsxvalue+h);while(Ab>0){if(this.jsxvalue>this.constrainValue(Ab))break;Ab=Ab+h;}}Ab=this.constrainValue(Ab);if(Ab!=this.jsxvalue){var
ra=this.doEvent(ub.t,{objEVENT:f,fpPREVIOUS:this.jsxvalue,fpVALUE:Ab});if(ra!==false)this.setValue(Ab);}};l.Tj={};l.Tj[ub.u]=true;l.Tj[ub.v]=true;r.paint=function(){this.applyDynamicProperties();var
na=this.getOrientation()==0;var
t=this.getEnabled()==1;var
Hb=this.jh(t?l.Tj:null,0);var
sb=this.renderAttributes(null,true);var
D=this.Wl(true);D.setAttributes(ub.w+this.getId()+ub.x+ub.y+this.getName()+ub.z+this.Fo()+this.bn()+Hb+sb);D.setStyles(this.dk()+this.zk()+this.bd()+this.bi());var
v=D.lg(0);var
Eb=!this.getPaintTrack()?ub.A:ub.o;var
F=this.getTrackClickable()&&t?this.Gj(ub.B,ub.C,2):ub.o;var
E=v.lg(0);E.setStyles(this.zf()+this.Of()+Eb);E.setAttributes(ub.D+F);var
ba=this.getHandleImage();if(ba==null)ba=na?l.r6:l.HV;var
_a=D.lg(1);_a.setAttributes(ub.E+(t?this.Gj(ub.F,ub.G,2):ub.o));var
Cb=_a.lg(0);Cb.setAttributes(ub.H+(t?ub.o:ub.I)+ub.J+jsx3.html.Kf);Cb.setStyles(ub.K+this.getUriResolver().resolveURI(ba)+ub.L);return D.paint().join(v.paint().join(E.paint().join(ub.M)+_a.paint().join(Cb.paint().join(ub.o))));};r.sx=function(k,o){if(!k.leftButton())return;k.cancelBubble();k.cancelReturn();var
Xa=this.getOrientation()==0;var
lb=this;jsx3.gui.Interactive.Bn(k,o,function(e,d,q){return lb.s8(q,e,d);});zb.subscribe(ub.N,this,ub.O);zb.subscribe(ub.P,this,ub.Q);};r.Ut=function(a,d){if(!a.leftButton())return;var
tb=this.getOrientation()==0;var
Pa=this.tS(tb?a.getOffsetX():a.getOffsetY());Pa=this.constrainValue(Pa);if(this.jsxvalue!=Pa){var
O=this.doEvent(ub.t,{objEVENT:a,fpPREVIOUS:this.jsxvalue,fpVALUE:Pa});if(O!==false)this.setValue(Pa);}};r.s8=function(i,q,p){var
E=this.getRendered(i);var
lb=this.getOrientation()==0;var
R=0,Xa=0;if(E!=null){var
yb=jsx3.html.selectSingleElm(E,0,0);var
_a=jsx3.html.selectSingleElm(E,0,1);if(lb){Xa=_a.offsetY;var
z=l.L5*q/(yb.offsetWidth-_a.offsetWidth);z=this.constrainValue(z);R=Math.round(z*(yb.offsetWidth-_a.offsetWidth)/l.L5);}else{R=_a.offsetX;var
Db=l.L5*p/(yb.offsetHeight-_a.offsetHeight);Db=this.constrainValue(Db);Xa=Math.round(Db*(yb.offsetHeight-_a.offsetHeight)/l.L5);}}return [R,Xa];};r.P9=function(e){var
cb=this.constrainValue(this.tS());this.doEvent(ub.R,{objEVENT:e.event,fpVALUE:cb});};r.v5=function(e){jsx3.EventHelp.reset();zb.unsubscribe(ub.N,this,ub.O);zb.unsubscribe(ub.P,this,ub.Q);var
za=this.constrainValue(this.tS());var
eb=this.doEvent(ub.t,{objEVENT:e.event,fpPREVIOUS:this.jsxvalue,fpVALUE:za});if(eb===false){this.setValue(this.jsxvalue);}else{this.jsxvalue=za;this.ce();}};r.tS=function(m){var
ka=this.getRendered();if(ka!=null){var
Qa=this.getOrientation()==0;var
nb=jsx3.html.selectSingleElm(ka,0,0);var
ra=jsx3.html.selectSingleElm(ka,0,1);return Qa?l.L5*(m!=null?m:ra.offsetLeft)/(nb.offsetWidth-ra.offsetWidth):l.L5*(1-(m!=null?m:ra.offsetTop)/(nb.offsetHeight-ra.offsetHeight));}else return 0;};r.constrainValue=function(s){return Math.max(0,Math.min(l.L5,s));};r.Of=function(){var
G=this.getBackground()||ub.o;return G||this.zf()?G+ub.S:this.getOrientation()==0?l.oP:l._6;};l.getVersion=function(){return ub.T;};});jsx3.Slider=jsx3.gui.Slider;
