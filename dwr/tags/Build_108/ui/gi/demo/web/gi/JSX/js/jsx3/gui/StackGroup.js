/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.LayoutGrid","jsx3.gui.Stack");jsx3.Class.defineClass("jsx3.gui.StackGroup",jsx3.gui.LayoutGrid,null,function(c,p){var
ub={d:"100%",a:"27",h:"3.0.00",c:"box",f:",",g:"beforeEnd",b:"*",e:"auto"};var
jb=jsx3.gui.LayoutGrid;c.ORIENTATIONV=0;c.ORIENTATIONH=1;c.DEFAULTSTACKCOUNT=2;c.DEFAULTDIMENSIONS=[ub.a,ub.b];c.DEFAULTBARSIZE=27;p.init=function(q){this.jsxsuper(q);};p.getBarSize=function(){return this.jsxbarsize;};p.setBarSize=function(s){this.jsxbarsize=s;return this;};p.uf=function(g,m){if(m==null)m=g.getChildIndex();var
xb=this.bm(m);if(xb)return xb;var
Ha=this.getParent().uf(this);var
gb=this.getOrientation()==0;var
Aa=Ha.width?Ha.width:Ha.parentwidth;var
nb=Ha.height?Ha.height:Ha.parentheight;var
Ga=this.paintBarSize();var
Wa=gb?nb:Aa;var
Fb=m*Ga+(this.getSelectedIndex()<m?1:0)*(Wa-(this.getChildren().length-1)*Ga-Ga);var
N=0,Ba=Aa,ca=nb,C=0;if(gb){C=Fb;ca=g.isFront()?nb-Ga*(this.getChildren().length-1):Ga;}else{N=Fb;Ba=g.isFront()?Aa-Ga*(this.getChildren().length-1):Ga;}return this.Ck(m,{boxtype:ub.c,left:N,top:C,width:ub.d,height:ub.d,parentwidth:Ba,parentheight:ca});};p.Rc=function(m,j,e){var
Ia=this.Wl(true,m);if(j){Ia.recalculate(m,j,e);j.style.overflow=ub.e;}var
_a=0;var
fa=this.getChildren().length;for(var
Ba=0;Ba<fa;Ba++){var
F=this.getChild(Ba);var
Ya=this.uf(F,Ba);e.add(F,Ya,j!=null,true);}};p.paint=function(){var
Eb=this.getChild(this.getSelectedIndex());if(Eb==null){Eb=this.getChild(0);this.setSelectedIndex(0);}if(Eb!=null){var
Ma=this.getChildren().length;var
Sa=[];var
ka=this.paintBarSize();var
wb=this.getSelectedIndex();for(var
Ha=0;Ha<Ma;Ha++)if(wb!=Ha){Sa[Ha]=ka;}else Sa[Ha]=ub.b;if(this.getOrientation()==0)this.setRows(Sa.join(ub.f));else this.setCols(Sa.join(ub.f));}return this.jsxsuper();};p.paintBarSize=function(f){return this.getBarSize()?this.getBarSize():c.DEFAULTBARSIZE;};p.getSelectedIndex=function(){return this.jsxselectedindex==null?0:this.jsxselectedindex>this.getChildren().length-1?this.getChildren().length-1:this.jsxselectedindex;};p.setSelectedIndex=function(k){this.jsxselectedindex=k;};p.paintChild=function(d,k){var
gb=this.getRendered();if(gb!=null){jsx3.html.insertAdjacentHTML(gb,ub.g,d.paint());jsx3.gui.Painted.prototype.paintChild.call(this,d,k,gb,true);}if(!k)this.A0(true);};p.onSetChild=function(q){return q instanceof jsx3.gui.Stack;};p.onRemoveChild=function(g,m){this.jsxsuper(g,m);if(g instanceof Array){this.repaint();}else{var
ea=this.getChildren().length;var
Ya=m==this.jsxselectedindex;if(m<=this.jsxselectedindex&&(this.jsxselectedindex>0||ea==0))this.jsxselectedindex--;if(Ya&&this.jsxselectedindex>=0){this.A0();this.getChild(this.jsxselectedindex).doShow();}else this.A0(true);}};p.A0=function(g){var
fb=[];var
eb=this.paintBarSize();var
mb=this.getSelectedIndex();var
ua=this.getChildren().length;for(var
Kb=0;Kb<ua;Kb++)fb[Kb]=mb!=Kb?eb:ub.b;if(this.getOrientation()==0)this.setRows(fb.join(ub.f),g);else this.setCols(fb.join(ub.f),g);};p.getOrientation=function(){return this.jsxorientation==null?0:this.jsxorientation;};p.setOrientation=function(g){this.jsxorientation=g;return this;};c.getVersion=function(){return ub.h;};});jsx3.StackGroup=jsx3.gui.StackGroup;
