/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Stack",jsx3.gui.Block,null,function(g,r){var
ub={k:"click",h:"JSXFRAG",c:"#ffffff",H:"3.0.00",p:"box",q:"div",G:"bold",v:' class="',i:"jsxexecute",A:"hidden",a:"#aaaafe",F:"5 5 5 5",u:'"',t:'id="',j:"jsxshow",s:"",z:"jsx30stack_texth",d:"solid 1px #ffffff;solid 1px #9898a5;solid 1px #9898a5;solid 1px #ffffff",o:"mouseout",D:"text-align:",w:"overflow:hidden;",r:"0 0 0 4",C:"left",B:"right",l:"XN",g:"100%",b:"#aaaacb",m:"keypress",f:"_content",y:"jsx30stack_textv",n:"mouseover",x:' class="jsx30stack_handle"',E:";",e:"solid 1px #c8c8d5;solid 1px #9898a5;solid 1px #9898a5;solid 1px #c8c8d5"};var
Ja=jsx3.gui;var
ua=Ja.Event;var
Y=Ja.Block;g.ORIENTATIONV=0;g.ORIENTATIONH=1;g.ACTIVECOLOR=ub.a;g.INACTIVECOLOR=ub.b;g.CHILDBGCOLOR=ub.c;g.BORDER=ub.d;g.CAPTION_BORDER=ub.e;g.BACKGROUND=jsx3.html.getCSSFade(false);g.BACKGROUNDH=jsx3.html.getCSSFade(true);r.init=function(j,f){this.jsxsuper(j,null,null,null,null,f);var
ea=new
Y(j+ub.f,0,0,ub.g,ub.g);ea.setOverflow(1);ea.setBorder(g.BORDER);ea.setBackgroundColor(g.CHILDBGCOLOR);this.setChild(ea,1,null,ub.h);};r.onSetParent=function(e){return Ja.StackGroup&&e instanceof Ja.StackGroup;};r.doShowStack=function(){this.XN(this.isOldEventProtocol());};r.doShow=function(){this.XN(false);};r.XN=function(k,b){var
Da=this.getParent();if(this==Da.getChild(Da.getSelectedIndex()))return;Da.setSelectedIndex(this.getChildIndex());if(k instanceof ua){var
D=jsx3.html.getJSXParent(k.srcElement());if(D!=null&&D!=this)return;}var
hb=Da.Wl();Da.af({left:0,top:0,parentwidth:hb._b(),parentheight:hb.lh()},true);if(k)this.doEvent(ub.i,{objEVENT:k instanceof ua?k:null});this.doEvent(ub.j);if(b)b.focus();};r.el=function(m,s){s.style.backgroundColor=this.getActiveColor();return;if(jsx3.EventHelp.isDragging())this.XN(m);var
lb=jsx3.html.getCSSOpacity(0.75);Ja.Painted.Si(s,lb);};r.Qi=function(q,o){o.style.backgroundColor=this.getInactiveColor();return;var
ib=jsx3.html.getCSSOpacity(1);Ja.Painted.Si(o,ib);};r.getContentChild=function(){var
Kb=this.getChildren().length;for(var
Wa=0;Wa<Kb;Wa++){var
E=this.getChild(Wa);if(!(Ja.Menu&&E instanceof Ja.Menu)&&!(Ja.ToolbarButton&&E instanceof Ja.ToolbarButton))return E;}return null;};r.oe=function(l,a){if(this.jsxsupermix(l,a))return;if(l.enterKey()||l.spaceKey()){this.XN(l);l.cancelAll();}};g.Tj={};g.Tj[ub.k]=ub.l;g.Tj[ub.m]=true;g.Tj[ub.n]=true;g.Tj[ub.o]=true;r.uf=function(n){var
Ca=this.bm(0);if(Ca)return Ca;var
tb={};if(this.getParent()){var
Lb=this.getParent().uf(this);var
eb=this.getParent().paintBarSize();var
qa=0,ba=0,z=Lb.parentwidth,Ta=Lb.parentheight;if(this.getOrientation()==0){ba=eb;Ta=Ta-eb;}else{qa=eb;z=z-eb;}tb={boxtype:ub.p,left:qa,top:ba,width:z,height:Ta,parentwidth:z,parentheight:Ta};}return this.Ck(0,tb);};r.Rc=function(h,e,j){var
Kb=this.Wl(true,h);if(e){var
V=Kb.recalculate(h,e,j);if(!V.w&&!V.h)return;var
eb=this.getParent().paintBarSize();var
Ta=Kb.mn();var
_=Kb.ec();var
hb={};if(this.getOrientation()==0){hb.height=eb;hb.parentwidth=h.parentwidth;}else{hb.width=eb;hb.parentheight=h.parentheight;}var
G=Kb.lg(0);G.recalculate(hb,e?e.childNodes[0]:null,j);var
t=this.getContentChild();if(t!=null){t=this.xv(t,this.isFront());if(this.isFront()){var
Mb=this.getOrientation()==0?{boxtype:ub.p,left:0,top:eb,width:Ta,height:_-eb,parentwidth:Ta,parentheight:_-eb}:{boxtype:ub.p,left:eb,top:0,width:Ta-eb,height:_,parentwidth:Ta-eb,parentheight:_};j.add(t,Mb,t.getRendered(e),true);}}}};r.Gc=function(k){if(this.getParent()&&(k==null||isNaN(k.parentwidth)||isNaN(k.parentheight))){k=this.getParent().uf(this);}else if(k==null)k={};k.width=ub.g;k.height=ub.g;k.tagname=ub.q;k.boxtype=ub.p;var
Q=new
Ja.Painted.Box(k);var
Ia=this.getParent().paintBarSize();var
_={};_.parentwidth=Q.mn();_.parentheight=Q.ec();if(this.getOrientation()==0){_.width=ub.g;_.height=Ia;}else{_.width=Ia;_.height=ub.g;}_.left=0;_.top=0;_.tagname=ub.q;_.boxtype=ub.p;_.border=g.CAPTION_BORDER;_.padding=ub.r;var
nb=new
Ja.Painted.Box(_);Q.Yf(nb);_={};_.tagname=ub.q;_.boxtype=ub.p;_.left=0;_.top=0;_.padding=this.getPadding();var
na=new
Ja.Painted.Box(_);nb.Yf(na);return Q;};r.paint=function(){if(!(this.getParent() instanceof Ja.StackGroup))return ub.s;this.applyDynamicProperties();var
Ca=this.Wl(true);Ca.setAttributes(ub.t+this.getId()+ub.u+this.on()+ub.v+this.to()+ub.u);Ca.setStyles(ub.w);var
Ea=this.jh(g.Tj,1);var
eb=this.renderAttributes(null,true);var
ca=Ca.lg(0);ca.setAttributes(this.Fo()+this.bn()+Ea+eb+ub.x);ca.setStyles(this.zf()+this.Of()+this.eh()+this.ad()+this.Yh()+this.Bf()+this.fn()+this.fh());var
M=this.getChildren();var
L=[],F=[];for(var
w=0;w<M.length;w++)if(Ja.Menu&&M[w] instanceof Ja.Menu||Ja.ToolbarButton&&M[w] instanceof Ja.ToolbarButton){F.push(M[w]);}else if(L.length==0){M[w]=this.xv(M[w],this.isFront());L.push(M[w]);}var
Gb=this.paintChildren(F);var
va=this.paintChildren(L);var
Nb=ca.lg(0);var
E=this.getOrientation()==0?ub.y:ub.z;Nb.setAttributes(ub.v+E+ub.u+jsx3.html.Kf);var
H=Nb.paint().join(this.wg());return Ca.paint().join(ca.paint().join(H+Gb)+va);};r.xv=function(c,j){var
Sa=c.Em(j);c=Sa||c;if(c instanceof Y)c.setVisibility(j?ub.s:ub.A,true);return c;};r.fn=function(){var
Aa;if(this.getTextAlign()){Aa=this.getTextAlign();}else if(this.getParent()&&this.getOrientation()==0){Aa=ub.B;}else Aa=ub.C;return ub.D+Aa+ub.E;};r.setText=function(n,s){this.jsxsuper(n,false);var
la;if(s&&(la=this.getRendered())!=null)la.firstChild.firstChild.innerHTML=n;return this;};r.getPadding=function(){var
X=this.jsxsuper();return X!=null&&X!=ub.s?X:ub.F;};r.getFontWeight=function(){return this.jsxsuper()||ub.G;};r.Of=function(){return (this.getBackground()?this.getBackground():this.getOrientation()==1?g.BACKGROUNDH:g.BACKGROUND)+ub.E;};r.Fo=function(){return this.jsxsuper(this.getIndex()||Number(0));};r.getActiveColor=function(){return this.jsxactivecolor==null?g.ACTIVECOLOR:this.jsxactivecolor;};r.setActiveColor=function(j){this.jsxactivecolor=j;return this;};r.getInactiveColor=function(){return this.jsxinactivecolor==null?g.INACTIVECOLOR:this.jsxinactivecolor;};r.setInactiveColor=function(e){this.jsxinactivecolor=e;return this;};r.getOrientation=function(){return this.getParent()!=null?this.getParent().getOrientation():0;};r.focus=function(){this.doShow();this.jsxsuper();};r.isFront=function(){var
bb=this.getChildIndex();return bb>=0&&this.getParent().getSelectedIndex()==bb;};r.getMaskProperties=function(){return Y.MASK_NO_EDIT;};g.getVersion=function(){return ub.H;};r.Zn=function(p){var
Ib=this.getContentChild();if(Ib)Ib.Zn(p);this.jsxsuper(p);};});jsx3.gui.Stack.prototype.getBackgroundColor=jsx3.gui.Stack.prototype.getInactiveColor;jsx3.gui.Stack.prototype.setBackgroundColor=jsx3.gui.Stack.prototype.setInactiveColor;jsx3.Stack=jsx3.gui.Stack;
