/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Tab",jsx3.gui.Block,null,function(p,d){var
ub={k:"url(",h:"_content",c:"#d8d8e5",H:"background-color:",p:"mouseout",q:"span",G:"cursor:pointer;",v:"div",I:";",i:"100%",A:'id="',a:"jsx:///images/tab/bevel.gif",F:");background-repeat:repeat-x;background-position:top left;",u:"0px pseudo;2px pseudo;0px pseudo;1px pseudo",t:"3 4 1 4",j:"JSXFRAG",s:"",z:"', could not be painted on-screen, because it does not belong to a jsx3.gui.TabbedPane parent.",d:"#f6f6ff",o:"mouseover",D:' class="jsx30tab_text"',w:"inline",r:"relativebox",C:' class="jsx30tab" ',B:'"',l:")",g:"jsx:///images/tab/off.gif",b:"#e8e8f5",m:"click",f:"jsx:///images/tab/on.gif",J:"3.0.00",y:"The jsx3.gui.Tab instance with the id, '",n:"keydown",x:"t21",E:"background-image:url(",e:"#a6a6af"};var
Kb=jsx3.gui.Event;var
Sa=jsx3.gui.Interactive;p.DEFAULTBEVELIMAGE=jsx3.resolveURI(ub.a);p.DEFAULTACTIVECOLOR=ub.b;p.DEFAULTINACTIVECOLOR=ub.c;p.DEFAULTHIGHLIGHT=ub.d;p.DEFAULTSHADOW=ub.e;p.ACTIVEBEVEL=jsx3.resolveURI(ub.f);p.INACTIVEBEVEL=jsx3.resolveURI(ub.g);jsx3.html.loadImages(p.DEFAULTBEVELIMAGE,p.ACTIVEBEVEL,p.INACTIVEBEVEL);p.CHILDBGCOLOR=ub.b;p.STATEDISABLED=0;p.STATEENABLED=1;d.init=function(c,q,a,g,f){this.jsxsuper(c,null,null,a,null,q);if(g!=null)this.setActiveColor(g);if(f!=null)this.setInactiveColor(f);var
yb=new
jsx3.gui.Block(c+ub.h,null,null,ub.i,ub.i);yb.setOverflow(1);yb.setRelativePosition(0);yb.setBackgroundColor(g==null?p.CHILDBGCOLOR:g);this.setChild(yb,1,null,ub.j);};d.onSetParent=function(k){return jsx3.gui.TabbedPane&&k instanceof jsx3.gui.TabbedPane;};d.getBevel=function(){return this.jsxbevel;};d.setBevel=function(b){this.jsxbevel=b;return this;};d.getMaskProperties=function(){return jsx3.gui.Block.MASK_EAST_ONLY;};d.el=function(n,r){r.style.backgroundImage=ub.k+p.ACTIVEBEVEL+ub.l;if(jsx3.EventHelp.isDragging())this.Pv(n,false);};d.Qi=function(h,e){e.style.backgroundImage=ub.k+p.INACTIVEBEVEL+ub.l;};d.Tg=function(e,h){h.focus();if(e.leftButton())this.Pv(e);};d.doClickTab=function(k,c){this.Pv(this.isOldEventProtocol(),c);};d.doShow=function(){this.Pv(false);};d.Pv=function(m,f){this.getParent().rg(m,this);};d.getActiveColor=function(){return this.jsxactivecolor;};d.setActiveColor=function(l){this.jsxactivecolor=l;return this;};d.getInactiveColor=function(){return this.jsxinactivecolor;};d.setInactiveColor=function(f){this.jsxinactivecolor=f;return this;};d.getEnabled=function(){return this.jsxenabled==null?1:this.jsxenabled;};d.setEnabled=function(j){this.jsxenabled=j;return this;};d.oe=function(c,j){if(this.jsxsupermix(c,j))return;var
_a=c.keyCode();var
S=this.getChildIndex();var
Za=this.getParent();if(_a>=37&&_a<=40){if(c.leftArrow()||c.upArrow()){var
Q=S>0?S-1:Za.getChildren().length-1;}else if(c.rightArrow()||c.downArrow())var
Q=S<Za.getChildren().length-1?S+1:0;Za.rg(c,Q,null,true);c.cancelAll();}else if(_a==9&&!c.shiftKey()){this.getContentChild().focus();c.cancelAll();}else if(_a==9)this.getParent().focus();};p.Tj={};p.Tj[ub.m]=true;p.Tj[ub.n]=true;p.Tj[ub.o]=true;p.Tj[ub.p]=true;d.uf=function(l){return this.bm(0)||this.Ck(0,this.getParent()?this.getParent().gi(this):{});};d.Rc=function(b,r,m){this.Gi(b,r,m,3);};d.Gc=function(e){if(e==null||isNaN(e.parentwidth)||isNaN(e.parentheight)){e=this.getParent().uf(this);}else if(e==null)e={};var
xb=this.getPadding();if(this.getWidth()!=null&&!isNaN(parseInt(this.getWidth())))e.width=this.getWidth();e.height=ub.i;e.tagname=ub.q;e.boxtype=ub.r;e.padding=xb!=null&&xb!=ub.s?xb:ub.t;e.border=ub.u;var
ha=new
jsx3.gui.Painted.Box(e);var
Gb={};Gb.parentwidth=ha.mn();Gb.parentheight=ha.ec();Gb.height=ub.i;if(!(this.getWidth()==null||isNaN(this.getWidth()))){Gb.width=ub.i;Gb.tagname=ub.v;Gb.boxtype=ub.w;}else{Gb.tagname=ub.q;Gb.boxtype=ub.r;}var
I=new
jsx3.gui.Painted.Box(Gb);ha.Yf(I);return ha;};d.paint=function(){if(!(this.getParent() instanceof jsx3.gui.TabbedPane)){jsx3.util.Logger.doLog(ub.x,ub.y+this.getId()+ub.z);return ub.s;}this.applyDynamicProperties();var
Gb=this.getEnabled()==1?this.jh(p.Tj,0):ub.s;var
F=this.renderAttributes(null,true);var
R=this.Wl(true);R.setAttributes(ub.A+this.getId()+ub.B+this.on()+this.Fo()+this.bn()+Gb+ub.C+F);R.setStyles(this.Ec()+this.Xe()+this.zf()+this.eh()+this.ad()+this.Yh()+this.Bf()+this.fn()+this.fh());var
pa=R.lg(0);pa.setAttributes(ub.D+jsx3.html.Kf);pa.setStyles(this.fn());return R.paint().join(pa.paint().join(this.wg()));};d.setWidth=function(o,a){this.jsxsuper(o);if(a)this.repaint();};d.setText=function(s,m){this.jsxsuper(s,m);if(m&&this.getParent())this.getParent().kh();};d.Ec=function(){return ub.E+p.INACTIVEBEVEL+ub.F;};d.Xe=function(){return this.getEnabled()==1?ub.G:ub.s;};d.Fo=function(){return this.jsxsuper(this.getIndex()||Number(0));};d.Ad=function(){return this.getActiveColor()?this.getActiveColor():p.DEFAULTACTIVECOLOR;};d.vm=function(){return this.getInactiveColor()?this.getInactiveColor():p.DEFAULTINACTIVECOLOR;};d.zf=function(){var
oa=this.getChildIndex()!=this.getParent().getSelectedIndex()?this.vm():this.Ad();return oa?ub.H+oa+ub.I:ub.s;};p.getVersion=function(){return ub.J;};d.getContentChild=function(){return this.getChild(0);};d.Zn=function(e){var
Bb=this.getContentChild();if(Bb)Bb.Zn(e);this.jsxsuper(e);};});jsx3.Tab=jsx3.gui.Tab;
