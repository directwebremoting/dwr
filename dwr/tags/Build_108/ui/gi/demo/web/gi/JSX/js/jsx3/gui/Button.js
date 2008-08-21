/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Button",jsx3.gui.Block,[jsx3.gui.Form],function(r,b){var
ub={k:"mouseup",h:"BT",c:"#a6a6af",p:"2",q:";solid 1px ",v:'"',i:"keydown",A:" ",a:"#e8e8f5",u:'id="',t:"inline",j:"mousedown",s:"div",z:";",d:"jsx30button",o:"span",D:"pt",w:' class="',r:"100%",C:"3.0.00",B:"",l:"mouseout",g:"click",b:"#f6f6ff",m:"relativebox",f:"solid 1px ",y:"background-color:",n:"box",x:' class="jsx30button_text"',e:"jsxexecute"};var
zb=jsx3.gui.Event;var
Gb=jsx3.gui.Form;r.DEFAULTBACKGROUNDCOLOR=ub.a;r.DEFAULTHIGHLIGHT=ub.b;r.DEFAULTSHADOW=ub.c;r.DEFAULTHEIGHT=17;b._jsxRL=null;r.DEFAULTCLASSNAME=ub.d;b.init=function(k,h,d,f,c){this.jsxsuper(k,h,d,f);this.setText(c);};b.doValidate=function(){return this.setValidationState(1).getValidationState();};b.oe=function(i,d){if(i.spaceKey()||i.enterKey()){this.BT(i,d);i.cancelAll();}};b.doClick=function(){var
ka=zb.getCurrent();this.BT(ka);};b.focus=function(){var
J=this.getRendered();if(J){J=J.childNodes[0];J.focus();return J;}};b.doExecute=function(a){this.BT(a);};b.BT=function(q,o){if(q==null||q.leftButton()||!q.isMouseEvent())this.doEvent(ub.e,{objEVENT:q});};b.Dc=function(s,m){if(!s.leftButton())return;if(!this.getBorder()){m.style.border=ub.f+r.DEFAULTSHADOW;m.style.borderRightColor=r.DEFAULTHIGHLIGHT;m.style.borderBottomColor=r.DEFAULTHIGHLIGHT;}m.childNodes[0].focus();};b.Df=function(e,h){if(e.leftButton())if(!this.getBorder()){h.style.border=ub.f+r.DEFAULTHIGHLIGHT;h.style.borderRightColor=r.DEFAULTSHADOW;h.style.borderBottomColor=r.DEFAULTSHADOW;}this.jsxsupermix(e,h);};b.Qi=function(q,o){if(!this.getBorder()){o.style.border=ub.f+r.DEFAULTHIGHLIGHT;o.style.borderRightColor=r.DEFAULTSHADOW;o.style.borderBottomColor=r.DEFAULTSHADOW;}};b.getValue=function(){return this.getText();};b.setEnabled=function(l,o){if(this._jsxRL!=null)this._jsxRL.setEnabled(l==1);return this.jsxsupermix(l,o);};r.Tj={};r.Tj[ub.g]=ub.h;r.Tj[ub.i]=true;r.Tj[ub.j]=true;r.Tj[ub.k]=true;r.Tj[ub.l]=true;b.Rc=function(f,c,l){var
Mb=this.Wl(true,f);if(c){var
Fb=Mb.recalculate(f,c,l);if(Fb.w||Fb.h){var
Ka=Mb.lg(0);Ka.recalculate({parentwidth:Mb.mn(),parentheight:Mb.ec()},c.childNodes[0],l);}}};b.Gc=function(g){this.applyDynamicProperties();if(this.getParent()&&(g==null||isNaN(g.parentwidth)||isNaN(g.parentheight))){g=this.getParent().uf(this);}else if(g==null)g={};var
ka=this.getRelativePosition()!=0;if(!g.boxtype)g.boxtype=ka?ub.m:ub.n;g.tagname=ub.o;if(g.width==null&&!jsx3.util.strEmpty(this.getWidth()))g.width=this.getWidth();g.height=this.getHeight()==null?r.DEFAULTHEIGHT:this.getHeight();if(ka){if(!jsx3.util.strEmpty(this.getMargin()))g.margin=this.getMargin();}else{g.left=!jsx3.util.strEmpty(this.getLeft())?this.getLeft():0;g.top=!jsx3.util.strEmpty(this.getTop())?this.getTop():0;}g.padding=!jsx3.util.strEmpty(this.getPadding())?this.getPadding():ub.p;g.border=this.getBorder()||ub.f+r.DEFAULTHIGHLIGHT+ub.q+r.DEFAULTSHADOW+ub.q+r.DEFAULTSHADOW+ub.q+r.DEFAULTHIGHLIGHT;var
pb=new
jsx3.gui.Painted.Box(g);var
Bb={};Bb.parentwidth=pb.mn();Bb.parentheight=pb.ec();Bb.height=ub.r;if(g.width){Bb.width=ub.r;Bb.tagname=ub.s;Bb.boxtype=ub.t;}else{Bb.tagname=ub.o;Bb.boxtype=ub.m;}var
Lb=new
jsx3.gui.Painted.Box(Bb);pb.Yf(Lb);return pb;};b.paint=function(){this.applyDynamicProperties();var
va;if((va=this.getKeyBinding())!=null){var
ob=this;if(this._jsxRL!=null)this._jsxRL.destroy();this._jsxRL=this.doKeyBinding(function(q){ob.BT(q);},va);this._jsxRL.setEnabled(this.getEnabled()!=0);}var
Ia=this.jh(this.getEnabled()==1?r.Tj:null,0);var
Ua=this.renderAttributes(null,true);var
Ra=this.Wl(true);Ra.setAttributes(ub.u+this.getId()+ub.v+this.on()+this.bn()+Ia+ub.w+this.to()+ub.v+jsx3.html.Kf+Ua);Ra.setStyles(this.bd()+this.bi()+this.Xe(true)+this.Yh()+this.ad()+this.Bf()+this.eh()+this.zf()+this.zk()+this.fn()+this.fh());var
ca=Ra.lg(0);ca.setAttributes(this.Fo()+ub.x+jsx3.html.Kf);ca.setStyles(this.fn());return Ra.paint().join(ca.paint().join(this.wg())+this.paintChildren());};b.zf=function(){var
xb=this.getEnabled()!=0?this.getBackgroundColor()||r.DEFAULTBACKGROUNDCOLOR:this.getDisabledBackgroundColor()||Gb.DEFAULTDISABLEDBACKGROUNDCOLOR;return ub.y+xb+ub.z;};b.to=function(){var
tb=this.getClassName();return r.DEFAULTCLASSNAME+(tb?ub.A+tb:ub.B);};r.getVersion=function(){return ub.C;};b.emGetType=function(){return jsx3.gui.Matrix.EditMask.FORMAT;};b.emInit=function(j){this.jsxsupermix(j);this.subscribe(ub.e,this,ub.D);};b.emSetValue=function(n){};b.emGetValue=function(){return null;};b.emBeginEdit=function(n,a,j,s,k,c,g){var
jb=jsx3.html.selectSingleElm(g,0,0,0);if(jb){this.jsxsupermix(n,a,j,s,k,c,g);jb.focus();}else return false;};b.emPaintTemplate=function(){this.setEnabled(0);var
Pa=this.paint();this.setEnabled(1);var
K=this.paint();return this.Tn(K,Pa);};b.pt=function(j){var
A=this.emGetSession();if(A){}};});jsx3.Button=jsx3.gui.Button;
