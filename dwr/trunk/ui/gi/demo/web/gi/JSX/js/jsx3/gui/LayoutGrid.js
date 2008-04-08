/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.LayoutGrid",jsx3.gui.Block,null,function(g,j){var
ub={o:"true",d:"50%,50%",k:"box",c:"*",h:"_jsxcachedcols",p:"",g:"_jsxcachedrows",l:"100%",q:"3.0.00",b:"34%",i:"string",m:"relativebox",a:"33%",f:",",j:"%",n:"div",e:/\s*,\s*/g};g.ORIENTATIONCOL=0;g.ORIENTATIONROW=1;g.DEFAULTORIENTATION=0;g.DEFAULTREPEAT=3;g.DEFAULTDIMENSIONS=[ub.a,ub.a,ub.b];g.ABSOLUTE=0;g.ADAPTIVE=1;j.init=function(c){this.jsxsuper(c);this.setCols(ub.c);this.setRows(ub.d);};j.onAfterAttach=function(){if(this.jsxsizearray&&!(this.jsxrows||this.jsxcols))this.setDimensionArray(this.jsxsizearray,false);this.jsxsuper();};j.onRemoveChild=function(a,n){this.jsxsuper(a,n);this.e2();};j.paintChild=function(i,l){if(!l){this.recalcBox();this.repaint();}};j.getOrientation=function(){return this.jsxorientation==null?g.DEFAULTORIENTATION:this.jsxorientation;};j.setOrientation=function(l){this.jsxorientation=l;return this;};j.getBestGuess=function(){return this.jsxbestguess;};j.setBestGuess=function(k){this.jsxbestguess=k;return this;};j.getRepeat=function(){return this.jsxrepeat;};j.setRepeat=function(o){this.jsxrepeat=o;return this;};j.getDimensionArray=function(){var
gb=this.getOrientation()==g.ORIENTATIONCOL?this.getRows():this.getCols();return gb!=null?gb.split(ub.e):[];};j.setDimensionArray=function(l,b){return this.getOrientation()==g.ORIENTATIONCOL?this.setRows(l.join(ub.f),b):this.setCols(l.join(ub.f),b);};j.getCols=function(){return this.jsxcols||ub.c;};j.setCols=function(p,q){this.jsxcols=p;if(q)this.e2();return this;};j.getRows=function(){return this.jsxrows||ub.c;};j.setRows=function(i,h){this.jsxrows=i;if(h)this.e2();return this;};j.e2=function(){this.V8();var
_a=this.getRendered();if(_a!=null)this.af({},_a);};j.uK=function(m,r){var
Ab=m?ub.g:ub.h;var
N=this.Wl(true);if(N[Ab] instanceof Array)return N[Ab];if(r==null||isNaN(r))r=m?N.ec():N.mn();if(isNaN(r))return [];var
R=0;var
_a=0;var
ib=m?this.getRows():this.getCols();if(!(ib instanceof Array))ib=ib!=null?ib.split(ub.e):[];var
y=new
Array(ib.length);for(var
x=0;x<ib.length;x++){var
Na=ib[x];if(Na==ub.c){y[x]=ub.c;}else if(typeof Na==ub.i&&Na.indexOf(ub.j)>=0){var
Ba=parseInt(Na);y[x]=isNaN(Ba)?ub.c:Ba/100*r;}else{var
Ba=parseInt(Na);y[x]=isNaN(Ba)?ub.c:Ba;}if(y[x]==ub.c)_a++;else R=R+y[x];}if(_a>0){var
nb=Math.max(0,r-R)/_a;for(var
x=0;x<y.length;x++)if(y[x]==ub.c)y[x]=nb;}var
T=0;for(var
x=0;x<y.length;x++){T=T+y[x]%1;y[x]=Math.floor(y[x]);if(T>=1||x==y.length-1&&T>0.5){y[x]++;T--;}}N[Ab]=y;return y;};j.uf=function(m,s){if(s==null)s=m.getChildIndex();var
t=this.bm(s);if(!t)this.qH();return this.bm(s)||{boxtype:ub.k,left:0,top:0,width:0,height:0,parentwidth:0,parentheight:0};};j.qH=function(){var
hb=this.getParent().uf(this);var
Ba=hb.width?hb.width:hb.parentwidth;var
Na=hb.height?hb.height:hb.parentheight;var
sa=this.uK(true,Na);var
qa=this.uK(false,Ba);var
U=sa.length*qa.length;var
Sa=this.getChildren().length;var
Va=Math.min(U,Sa);var
Fb=0;var
Ca=0;for(var
oa=0;oa<sa.length;oa++){var
u=0;for(var
Cb=0;Cb<qa.length&&Fb<Va;Cb++){this.Ck(Fb++
,{boxtype:ub.k,left:u,top:Ca,width:ub.l,height:ub.l,parentwidth:qa[Cb],parentheight:sa[oa]});u=u+qa[Cb];}Ca=Ca+sa[oa];}};j.Rc=function(s,p,r){if(!p){this.ki(true);return;}if(this.getParent()&&(!isNaN(s.parentwidth)&&!isNaN(s.parentheight)||s.width==null&&s.height==null)){s=this.getParent().uf(this);}else if(s==null)s={};var
Q=this.Wl(true,s);var
nb=Q._jsxcachedrows!=null&&Q._jsxcachedcols!=null;nb=nb&&!Q.recalculate(s,p,r);if(nb)return;this.V8(Q);var
D=this.getChildren();for(var
sa=0;sa<D.length;sa++)r.add(D[sa],this.uf(D[sa],sa),p?p.childNodes[sa]:null,true);};j.V8=function(q){if(!q)q=this.Wl(false);if(q){delete q[ub.g];delete q[ub.h];}};j.Gc=function(o){if(this.getParent()&&(o==null||!isNaN(o.parentwidth)&&!isNaN(o.parentheight)||!isNaN(o.width)&&!isNaN(o.height))){o=this.getParent().uf(this);}else if(o==null)o={};var
sb=this.getRelativePosition()!=0&&(!this.getRelativePosition()||this.getRelativePosition()==1);if(!o.boxtype)o.boxtype=sb||o.left==null||o.top==null?ub.m:ub.k;if(o.boxtype==ub.m){if(o.left==null)o.left=0;if(o.top==null)o.top=0;}if(o.width==null)o.width=ub.l;if(o.height==null)o.height=ub.l;o.tagname=ub.n;o.container=ub.o;var
V;if((V=this.getBorder())!=null&&V!=ub.p)o.border=V;return new
jsx3.gui.Painted.Box(o);};j.paint=function(){this.applyDynamicProperties();this.setOverflow(2);var
ba=this.getChildren();for(var
Ra=0;Ra<ba.length;Ra++)ba[Ra].ac(this.uf(ba[Ra],Ra));return this.jsxsuper();};g.getVersion=function(){return ub.q;};});jsx3.LayoutGrid=jsx3.gui.LayoutGrid;
