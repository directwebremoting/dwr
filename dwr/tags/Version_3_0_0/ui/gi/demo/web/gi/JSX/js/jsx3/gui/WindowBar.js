/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.WindowBar",jsx3.gui.Block,null,function(a,f){var
ub={k:"div",h:"100%",c:"solid 1px #e8e8f5;solid 0px;solid 1px #a8a8b5;solid 0px;",p:"cursor:default;",q:"",v:";",i:"inline",A:"right",a:"#c8c8d5",u:"background-color:",t:' class="jsx30windowbar"',j:"box",s:'"',z:"text-align:",d:"solid 1px #9898a5",o:"cursor:move;",D:"#FFFFFF",w:"font-weight:",r:'id="',C:"none",B:"left",l:"0 0 0 4",g:"doBeginMove",b:"#aaaacb",m:"span",f:"mousedown",y:"px;",n:'class="jsx30windowbar_lbl"',x:"font-size:",E:"2.4.00",e:"bold"};var
u=jsx3.gui.Block;a.DEFAULTHEIGHT=26;a.DEFAULTBACKGROUND=jsx3.html.getCSSFade(false);a.DEFAULTBG=ub.a;a.DEFAULTBGCAPTION=ub.b;a.DEFAULTBORDER=ub.c;a.DEFAULTBORDERCAPTION=ub.d;a.DEFAULTFONTWEIGHT=ub.e;a.DEFAULTFONTSIZE=11;a.TYPECAPTION=0;a.TYPETOOL=1;a.TYPEMENU=2;a.TYPETASK=3;f.init=function(m,r){this.jsxsuper(m);this.setRelativePosition(1);if(r!=null)this.setType(r);};f.getMaskProperties=function(){return this.getRelativePosition()==0?u.MASK_MOVE_ONLY:u.MASK_NO_EDIT;};f.getType=function(){return this.jsxbartype==null?0:this.jsxbartype;};f.setType=function(j){this.jsxbartype=j;this.ce();return this;};f.setText=function(j,c){this.jsxsuper(j,c);if(c)if(this.getType()==0){var
ga=this.getParent();if(ga instanceof jsx3.gui.Dialog&&ga.getRendered()!=null)ga.Mj();}return this;};f.doBeginMove=function(p,i){if(jsx3.html.getJSXParent(p.srcElement())==this)this.getParent().doBeginMove(p,i);};a.Tj={};a.Tj[ub.f]=ub.g;f.Rc=function(k,h,g){this.Gi(k,h,g,2);};f.Gc=function(n){this.applyDynamicProperties();if(this.getParent()&&(n==null||!isNaN(n.parentwidth)&&!isNaN(n.parentheight))){n=this.getParent().uf(this);}else if(n==null)n={};var
vb=this.getBorder();var
pa=this.getRelativePosition()!==0;if(n.left==null&&!pa&&!jsx3.util.strEmpty(this.getLeft()))n.left=this.getLeft();if(n.top==null&&!pa&&!jsx3.util.strEmpty(this.getTop()))n.top=this.getTop();if(n.width==null)n.width=ub.h;if(n.height==null)n.height=a.DEFAULTHEIGHT;if(!n.boxtype)n.boxtype=pa?ub.i:ub.j;n.tagname=ub.k;n.padding=ub.l;n.border=vb!=null?vb:this.getType()==0?a.DEFAULTBORDERCAPTION:a.DEFAULTBORDER;var
Pa=new
jsx3.gui.Painted.Box(n);if(this.getType()==0){var
Qa={};Qa.left=6;Qa.top=6;Qa.tagname=ub.m;Qa.boxtype=ub.j;var
S=new
jsx3.gui.Painted.Box(Qa);Pa.Yf(S);}return Pa;};f.paint=function(){this.applyDynamicProperties();var
X=this.Wl(true);var
xb=null,x=null,sb=null;if(this.getType()==0){var
Oa=X.lg(0);Oa.setAttributes(ub.n);Oa.setStyles(this.eh()+this.Bf()+this.ad()+this.Yh());sb=Oa.paint().join(this.wg());xb=ub.o;x=this.jh(a.Tj,0);}else{xb=ub.p;x=ub.q;sb=ub.q;}var
lb=this.renderAttributes(null,true);X.setAttributes(ub.r+this.getId()+ub.s+this.on()+this.bn()+this.Fo()+x+ub.t+lb);X.setStyles(xb+this.fn()+this.zf()+this.zk()+this.Of()+this.fh());return X.paint().join(sb+this.paintChildren());};f.Fo=function(){return this.jsxsuper(this.getIndex()||Number(0));};f.zf=function(){var
la=this.getBackgroundColor();return la==null||la!=ub.q?ub.u+(la?la:this.getType()==0?a.DEFAULTBGCAPTION:a.DEFAULTBG)+ub.v:ub.q;};f.Of=function(){if(this.getType()==2)return ub.q;var
Db=this.getBackground();return Db==null?a.DEFAULTBACKGROUND:Db;};f.getHeight=function(){var
La=this.jsxsuper();if(La==null||isNaN(La))La=a.DEFAULTHEIGHT;return La;};f.Bf=function(){return this.getFontWeight()?ub.w+this.getFontWeight()+ub.v:ub.w+ub.e+ub.v;};f.Yh=function(){return ub.x+(this.getFontSize()?this.getFontSize():a.DEFAULTFONTSIZE)+ub.y;};f.fn=function(){return ub.z+(this.getTextAlign()?this.getTextAlign():this.getType()==0?ub.A:ub.B)+ub.v;};f.wg=function(){return this.getText()?this.getText():ub.q;};f.beep=function(){jsx3.gui.Yl(this.getRendered(),{filter:ub.C,backgroundColor:ub.D,backgroundImage:ub.q});};a.getVersion=function(){return ub.E;};});jsx3.WindowBar=jsx3.gui.WindowBar;
