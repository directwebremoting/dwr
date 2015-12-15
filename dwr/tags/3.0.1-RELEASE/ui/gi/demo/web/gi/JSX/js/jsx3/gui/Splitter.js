/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Splitter",jsx3.gui.Block,null,function(m,f){var
ub={k:"box",h:"",c:"#2050df",H:");background-repeat:no-repeat;background-position:center center;",p:'" class="jsx30splitter"',q:' class="jsx30splitter_',G:"background-image:url(",v:"background-color:",I:"3.0.00",i:"JSXFRAG",A:"%",a:"jsx:///images/splitter/v.gif",F:/background-image\s*:\s*url\(/,u:"&#160;",t:'bar"',j:/%/g,s:"v",z:"doResizeEnd",d:"#c8c8d5",o:' id="',D:"number",w:";",r:"h",C:"50%",B:"jsxafterresize",l:"div",g:"100%",b:"jsx:///images/splitter/h.gif",m:"mousedown",f:"_",y:"mouseup",n:"doBeginMove",x:"jsxbeforeresize",E:"49.999%",e:"#ffffff"};var
W=jsx3.gui.Event;var
Q=jsx3.gui.Interactive;m.ORIENTATIONH=0;m.ORIENTATIONV=1;m.w7=ub.a;m.SK=ub.b;jsx3.html.loadImages(ub.a,ub.b);m._5=ub.c;m.rv=ub.d;m.t3=ub.e;f.init=function(i,s){this.jsxsuper(i);if(s!=null)this.setOrientation(s);for(var
X=1;X<=2;X++){var
wa=new
jsx3.gui.Block(i+ub.f+X,null,null,ub.g,ub.g,ub.h);wa.setRelativePosition(1);wa.setBackgroundColor(m.t3);this.setChild(wa,1,null,ub.i);}};f.paintChild=function(e,q){if(!q)this.repaint();};f.uf=function(a){var
qa=a.getChildIndex();var
N=this.bm(qa);if(N)return N;var
D=this.getParent().uf(this);var
Ib=D.width?D.width:D.parentwidth;var
Za=D.height?D.height:D.parentheight;var
Qa=Number(this.getSubcontainer1Pct().replace(ub.j,ub.h))/100;var
eb=this.getOrientation()==0;var
U=0,ya=0,Ia=Ib,J=Za;if(qa==0){if(eb){Ia=parseInt(Ib*Qa);}else J=parseInt(Za*Qa);}else if(eb){U=parseInt(Ib*Qa)+8;Ia=Ia-U;}else{ya=parseInt(Za*Qa)+8;J=J-ya;}return this.Ck(qa,{boxtype:ub.k,tagname:ub.l,left:U,top:ya,width:Ia,height:J,parentwidth:Ia,parentheight:J});};f.Rc=function(n,j,e){var
P=this.Wl(true,n);if(j){P.recalculate(n,j,e);var
Va=this.getChildren();var
ra=Va.length>2?2:Va.length;var
Cb=0;for(var
A=0;A<ra;A++){var
Oa=this.uf(Va[A]);if(A==1){var
ta=P.lg(0);if(this.getOrientation()==0){ta.recalculate({left:Cb,parentheight:N},j?j.childNodes[0]:null,e);}else ta.recalculate({top:Cb,parentwidth:C},j?j.childNodes[0]:null,e);}e.add(Va[A],Oa,j?j.childNodes[A+1]:null,true);var
C=Oa.width!=null?Oa.width:Oa.parentwidth;var
N=Oa.height!=null?Oa.height:Oa.parentheight;Cb=this.getOrientation()==0?C:N;}}};f.Gc=function(c){this.applyDynamicProperties();if(this.getParent()&&(c==null||!isNaN(c.parentwidth)&&!isNaN(c.parentheight)||!isNaN(c.width)&&!isNaN(c.height))){c=this.getParent().uf(this);}else if(c==null)c={};var
oa=this.getRelativePosition()!=0;if(c.left==null||!oa&&!jsx3.util.strEmpty(this.getLeft()))c.left=this.getLeft();if(c.top==null||!oa&&!jsx3.util.strEmpty(this.getTop()))c.top=this.getTop();if(c.width==null)c.width=ub.g;if(c.height==null)c.height=ub.g;c.tagname=ub.l;if(!c.boxtype)c.boxtype=ub.k;var
ja=new
jsx3.gui.Painted.Box(c);var
xb={};xb.tagname=ub.l;xb.boxtype=ub.k;var
z=Number(this.getSubcontainer1Pct().replace(ub.j,ub.h))/100;xb.parentwidth=ja.mn();xb.parentheight=ja.ec();var
kb=this.uf(this.getChild(0));var
V=this.getOrientation()==0?kb.width:kb.height;if(this.getOrientation()==0){xb.left=V;xb.top=0;xb.width=8;xb.height=ub.g;}else{xb.left=0;xb.top=V;xb.width=ub.g;xb.height=8;}var
nb=new
jsx3.gui.Painted.Box(xb);ja.Yf(nb);return ja;};f.paint=function(){this.applyDynamicProperties();var
ba=this.Gj(ub.m,ub.n,1);var
Fa=this.renderAttributes(null,true);var
U=this.Wl(true);U.setAttributes(this.Fo()+this.bn()+ub.o+this.getId()+ub.p+this.on()+Fa);U.setStyles(this.zf()+this.bd()+this.bi()+this.paintWrap()+this.fh());var
eb=U.lg(0);eb.setAttributes(ba+ub.q+(this.getOrientation()==0?ub.r:ub.s)+ub.t);eb.setStyles(this.zf()+(this.getOrientation()==0?this.rZ():this.JJ()));var
fa,ha;if((fa=this.getChild(0))!=null)fa.ac(this.uf(fa));if((ha=this.getChild(1))!=null)ha.ac(this.uf(ha));return U.paint().join(eb.paint().join(ub.u)+this.paintChildren());};f.zf=function(){return ub.v+(this.getBackgroundColor()?this.getBackgroundColor():m.rv)+ub.w;};f.paintWrap=function(){return ub.h;};f.doBeginMove=function(d,i){if(!d.leftButton())return;if(this.doEvent(ub.x,{objEVENT:d,objGUI:i})===false)return;i.style.backgroundColor=m._5;this._jsxmoving=true;if(this.getOrientation()==0){jsx3.EventHelp.constrainY=true;}else jsx3.EventHelp.constrainX=true;this.jsxsupermix(d,i);W.subscribe(ub.y,this,ub.z);};f.doResizeEnd=function(o){o=o.event;var
gb=this.getRendered(o).childNodes[0];W.unsubscribe(ub.y,this,ub.z);if(!this._jsxmoving)return;this._jsxmoving=false;o.releaseCapture(gb);gb.style.backgroundColor=this.getBackgroundColor()?this.getBackgroundColor():m.rv;if(this.getOrientation()==0){var
L=parseInt(gb.style.left);var
N=gb.parentNode.offsetWidth;if(L<this.getSubcontainer1Min())L=this.getSubcontainer1Min();if(L>N-8)L=N-8;var
oa=L/N*100;oa=parseInt(oa*100)/100;var
V=oa+ub.A;}else{var
L=parseInt(gb.style.top);var
N=gb.parentNode.offsetHeight;if(L<this.getSubcontainer1Min())L=this.getSubcontainer1Min();if(L>N-8)L=N-8;var
oa=L/N*100;oa=parseInt(oa*100)/100;var
V=oa+ub.A;}this.setSubcontainer1Pct(V,true);this.doEvent(ub.B,{objEVENT:o,objGUI:gb});};f.getSubcontainer1Pct=function(){return this.jsxsubcontainer1pct==null?ub.C:this.jsxsubcontainer1pct;};f.setSubcontainer1Pct=function(k,r){if(typeof k==ub.D)k=k+ub.A;this.jsxsubcontainer1pct=k;if(r){var
ra=this.getRendered();if(ra!=null)this.af({parentwidth:ra.offsetWidth,parentheight:ra.offsetHeight},ra);}return this;};f.getSubcontainer2Pct=function(){return this.jsxsubcontainer2pct==null?ub.E:this.jsxsubcontainer2pct;};f.setSubcontainer2Pct=function(d){this.jsxsubcontainer2pct=d;return this;};f.getSubcontainer1Min=function(){return this.jsxsubcontainer1min==null?1:this.jsxsubcontainer1min;};f.setSubcontainer1Min=function(g){this.jsxsubcontainer1min=g;return this;};f.getSubcontainer2Min=function(){return this.jsxsubcontainer2min==null?8:this.jsxsubcontainer2min;};f.setSubcontainer2Min=function(p){this.jsxsubcontainer2min=p;return this;};f.getOrientation=function(){return this.jsxorientation==null?0:this.jsxorientation;};f.setOrientation=function(d){this.jsxorientation=d;this.ce();return this;};f.getVSplitImage=function(){return this.jsxvsplitimage==null?m.w7:this.jsxvsplitimage;};f.setVSplitImage=function(o){this.jsxvsplitimage=o;return this;};f.getHSplitImage=function(){return this.jsxhsplitimage==null?m.SK:this.jsxhsplitimage;};f.setHSplitImage=function(b){this.jsxhsplitimage=b;return this;};f.JJ=function(){var
Va=this.getVSplitImage();return Va.search(ub.F)!=-1?Va:ub.G+jsx3.resolveURI(Va)+ub.H;};f.rZ=function(){var
Ib=this.getHSplitImage();return Ib.search(ub.F)!=-1?Ib:ub.G+jsx3.resolveURI(Ib)+ub.H;};f.onSetChild=function(n){return this.getChildren().length<2;};m.getVersion=function(){return ub.I;};});jsx3.Splitter=jsx3.gui.Splitter;
