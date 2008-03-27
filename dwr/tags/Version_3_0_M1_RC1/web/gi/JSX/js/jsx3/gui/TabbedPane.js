/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Block","jsx3.gui.Tab");jsx3.Class.defineClass("jsx3.gui.TabbedPane",jsx3.gui.Block,null,function(s,k){var
ub={k:"100%",h:"box",c:"beforeEnd",H:"jsxexecute",p:'id="',q:'" class="jsx30tabbedpane"',G:"_jsxxJ",v:'class="jsx30tabbedpane_autoscroller_l" jsxscrolltype="left" ',I:"jsxshow",i:"div",A:'class="jsx30tabbedpane_autoscroller_r" jsxscrolltype="right" ',a:"jsx:///images/tab/l.png",F:"-",u:"display:none;",t:'class="jsx30tabbedpane_controlbox"',j:"solid 1px #f6f6ff;solid 1px #a6a6af;solid 1px #a6a6af;solid 1px #f6f6ff",s:"visibility:hidden;",z:"D2",d:"",o:"mouseup",D:"left",w:"mouseover",r:"&#160;",C:"jsxscrolltype",B:"0px",l:"white-space:nowrap;",g:"jsxchange",b:"jsx:///images/tab/r.png",m:"jsxdrop",f:"jsxhide",J:"3.0.00",y:"mouseout",n:"jsxctrldrop",x:"aq",E:"px",e:"none"};var
V=jsx3.gui.Tab;var
pb=jsx3.gui.Event;var
Bb=jsx3.gui.Block;var
Mb=jsx3.gui.Interactive;s.AUTO_SCROLL_INTERVAL=50;s.LSCROLLER=jsx3.html.getCSSPNG(jsx3.resolveURI(ub.a));s.RSCROLLER=jsx3.html.getCSSPNG(jsx3.resolveURI(ub.b));s.DEFAULTTABHEIGHT=20;k.init=function(o){this.jsxsuper(o);};k.paintChild=function(c,l){var
Na=this.getRendered();if(Na!=null){if(this.getShowTabs())jsx3.html.insertAdjacentHTML(Na.childNodes[0].childNodes[0],ub.c,c.paint());var
da=c.getContentChild();var
Db=this.getChildren().length==1;da=this.xv(da,Db);jsx3.html.insertAdjacentHTML(Na,ub.c,c.paintChildren([da]));this.jsxsuper(da,l,Na,true);if(Db)this.rg(null,c);}this.kh();};k.onSetChild=function(g){if(!(g instanceof V))return false;var
Va=g.getContentChild();if(Va&&Va instanceof Bb)Va.setVisibility(ub.d);if(this.getChildren().length==0)this.jsxselectedindex=-1;return true;};k.xv=function(q,m){q=q.Em(m)||q;if(q instanceof Bb)q.setDisplay(m?ub.d:ub.e,true);return q;};k.onRemoveChild=function(g,m){this.jsxsuper(g,m);if(g instanceof Array){var
z=g[this.getSelectedIndex()];if(z)z.doEvent(ub.f);this.doEvent(ub.g);this.setSelectedIndex(-1);this.ce();this.repaint();}else{var
Va=this.getSelectedIndex();var
qb=Math.min(this.getChildren().length-1,Va);if(Va==m)g.doEvent(ub.f);this.rg(false,qb,true);this.kh();}};k.getSelectedIndex=function(){return this.jsxselectedindex==null?this.getChildren().length>0?0:-1:this.jsxselectedindex;};k.setSelectedIndex=function(g,e){if(e){this.rg(false,g);}else this.jsxselectedindex=g instanceof V?g.getChildIndex():g;return this;};k.Df=function(l,a){this.doDrop(l,a,jsx3.EventHelp.ONDROP);};k.uf=function(){var
H=this.bm(0);if(H)return H;var
sa={};if(this.getParent()){var
qa=this.getParent().uf(this);var
ka=qa.width?qa.width:qa.parentwidth;var
I=this.getShowTabs()?this.paintTabSize()+1:0;sa={parentwidth:ka,parentheight:I};}return this.Ck(0,sa);};k.gi=function(){if(this.getParent()){var
zb=this.getParent().uf(this);var
vb=zb.width!=null&&!isNaN(zb.width)?zb.width:zb.parentwidth;var
O=this.getShowTabs()?this.paintTabSize():0;var
ab=(zb.height!=null&&!isNaN(zb.height)?zb.height:zb.parentheight)-O;var
va={left:0,top:O,width:vb,height:ab,parentwidth:vb,parentheight:ab,boxtype:ub.h,tagname:ub.i};if(this.getShowTabs())va.border=ub.j;return va;}return {};};k.Rc=function(l,i,f){var
ea=this.Wl(true,l);if(i){ea.recalculate(l,i,f);var
Ab=ea.lg(0);Ab.recalculate({parentwidth:ea.mn(),height:this.paintTabSize()+1},i!=null?i.childNodes[0]:null,f);var
pa=Ab.lg(0);pa.recalculate({parentwidth:ea.mn(),height:this.paintTabSize()+1},i!=null?i.childNodes[0].childNodes[0]:null,f);var
va=Ab.lg(2);va.recalculate({left:Ab.mn()-22},i!=null?i.childNodes[0].childNodes[2]:null,f);var
ca=this.getChildren();var
Gb=this.uf();for(var
ra=0;ra<ca.length;ra++){var
y=ca[ra];f.add(y,this.uf(),i!=null,true);var
K=y.getContentChild();if(K!=null){var
eb=this.getSelectedIndex()==ra;K=this.xv(K,eb);if(eb)f.add(K,this.gi(),K.getRendered(i),true);}}this.kh();}};k.Gc=function(n){if(this.getParent()&&(n==null||!isNaN(n.parentwidth)&&!isNaN(n.parentheight)||!isNaN(n.width)&&!isNaN(n.height))){n=this.getParent().uf(this);}else if(n==null)n={};if(n.left==null)n.left=0;if(n.top==null)n.top=0;if(n.width==null)n.width=ub.k;if(n.height==null)n.height=ub.k;if(n.tagname==null)n.tagname=ub.i;if(!n.boxtype)n.boxtype=ub.h;var
D=this.getBorder();if(D!=null&&D!=ub.d)n.border=D;var
Fa=new
jsx3.gui.Painted.Box(n);var
y={};y.parentwidth=n.parentwidth;y.width=ub.k;y.height=this.paintTabSize()+1;y.left=0;y.top=0;y.tagname=ub.i;y.boxtype=ub.h;var
Xa=new
jsx3.gui.Painted.Box(y);Fa.Yf(Xa);y={};y.parentwidth=n.parentwidth;y.width=ub.k;y.height=this.paintTabSize()+1;y.left=0;y.top=0;y.tagname=ub.i;y.boxtype=ub.h;var
lb=new
jsx3.gui.Painted.Box(y);lb.setStyles(ub.l);Xa.Yf(lb);y={};y.width=14;y.height=14;y.left=2;y.top=2;y.tagname=ub.i;y.boxtype=ub.h;var
O=new
jsx3.gui.Painted.Box(y);Xa.Yf(O);y={};y.width=14;y.height=14;y.left=Xa.mn()-16;y.top=2;y.tagname=ub.i;y.boxtype=ub.h;var
F=new
jsx3.gui.Painted.Box(y);Xa.Yf(F);return Fa;};k.paint=function(){this.applyDynamicProperties();var
rb=this.getId();var
X=this.getShowTabs();var
ta=this.getSelectedIndex();if(ta<0||ta>=this.getChildren().length){ta=0;this.setSelectedIndex(ta);}var
Ja={};if(this.hasEvent(ub.m)||this.hasEvent(ub.n))Ja[ub.o]=true;var
Ba=this.jh(Ja,0);var
Ca=this.renderAttributes(null,true);var
I=this.Wl(true);I.setAttributes(ub.p+rb+ub.q+this.on()+this.Fo()+this.bn()+Ba+Ca);I.setStyles(this.zk()+this.zf()+this.Of()+this.bd()+this.bi()+this.fh());var
mb=I.lg(0);if(X){var
w=this.getChild(ta);w=w==null?this.getChild(0):w;if(w!=null)if(!w.getEnabled()){var
Ua=this.getChildren().length-1;for(var
L=0;L<=Ua;L++)if(this.getChild(L).getEnabled()){this.setSelectedIndex(L);break;}}var
_a=this.paintChildren();mb.setStyles(this.fn());}else{var
_a=ub.r;mb.setStyles(ub.s);}mb.setAttributes(ub.t);var
H=this.getChildren();var
E=[];for(var
L=0;L<H.length;L++){var
jb=this.getSelectedIndex()==L;var
bb=H[L].getContentChild();if(bb!=null){bb=this.xv(bb,jb);bb.ac(this.gi());E.push(bb);}}jsx3.sleep(function(){this.kh();},null,this);var
C=mb.lg(0);var
Fb=mb.lg(1);Fb.setStyles(ub.u+s.LSCROLLER);Fb.setAttributes(ub.v+this.Gj(ub.w,ub.x)+this.Gj(ub.y,ub.z));var
Lb=mb.lg(2);Lb.setStyles(ub.u+s.RSCROLLER);Lb.setAttributes(ub.A+this.Gj(ub.w,ub.x)+this.Gj(ub.y,ub.z));return I.paint().join(mb.paint().join(C.paint().join(_a)+Fb.paint().join(ub.r)+Lb.paint().join(ub.r))+this.paintChildren(E));};k.kh=function(){var
ka=this.QZ();var
xb=this.uf().parentwidth;var
Q=this.H8();if(Q)if(xb<ka){Q.nextSibling.style.display=parseInt(Q.style.left)<0?ub.d:ub.e;Q.nextSibling.nextSibling.style.display=ub.d;}else{Q.nextSibling.style.display=ub.e;Q.nextSibling.nextSibling.style.display=ub.e;Q.style.left=ub.B;}};k.QZ=function(){var
Va=this.H8();if(Va){var
B=Va.childNodes;var
Aa=0;for(var
La=0;La<B.length;La++)Aa=Aa+parseInt(B[La].offsetWidth);return Aa;}};k.aq=function(m,h){var
J=this;this._jsxxJ={direction:h.getAttribute(ub.C),totalwidth:this.QZ()-this.uf().parentwidth};this._jsxxJ.interval=window.setInterval(function(){J.VX();},s.AUTO_SCROLL_INTERVAL);};k.H8=function(){var
Oa=this.getRendered();return Oa?Oa.childNodes[0].childNodes[0]:null;};k.VX=function(){var
u=this.H8();var
Da=parseInt(u.style.left);if(this._jsxxJ.direction==ub.D){if(Da-5<0){u.style.left=Da+5+ub.E;u.nextSibling.nextSibling.style.display=ub.d;}else{u.style.left=ub.B;u.nextSibling.style.display=ub.e;this.D2();}}else if(Math.abs(Da)+5<=this._jsxxJ.totalwidth){u.style.left=Da-5+ub.E;u.nextSibling.style.display=ub.d;}else{u.style.left=ub.F+this._jsxxJ.totalwidth+ub.E;u.nextSibling.nextSibling.style.display=ub.e;this.D2();}};k.D2=function(l,a){if(this._jsxxJ){window.clearInterval(this._jsxxJ.interval);delete this[ub.G];}};k.Fo=function(){return this.jsxsuper(this.getIndex()||Number(0));};k.paintTabSize=function(){return this.getTabHeight()!=null&&!isNaN(this.getTabHeight())?this.getTabHeight():s.DEFAULTTABHEIGHT;};k.getTabHeight=function(){return this.jsxtabheight;};k.setTabHeight=function(d){this.jsxtabheight=d;this.ki(true);return this;};k.getShowTabs=function(){return this.jsxshowtabs==null||this.jsxshowtabs===ub.d?1:this.jsxshowtabs;};k.setShowTabs=function(g){this.jsxshowtabs=g;this.ki(true);return this;};k.rg=function(d,a,h,e){if(!(a instanceof V))a=this.getChild(a);if(a){var
ya=this.getShowTabs();var
za=a.getChildIndex();var
qb=this.getSelectedIndex();if(h||qb!=za){this.setSelectedIndex(za);var
bb=this.getChildren().length;for(var
Da=0;Da<bb;Da++){var
va=this.getChild(Da);var
tb=va.getContentChild();var
E=Da==za;if(tb)tb=this.xv(tb,E);if(ya)va.setBackgroundColor(E?va.Ad():va.vm(),true);if(E)tb.af(this.gi(),true);}}if(d)a.doEvent(ub.H,{objEVENT:d instanceof pb?d:null});if(!h){var
ga=this.getChild(qb);if(ga)ga.doEvent(ub.f);}if(e)a.focus();a.doEvent(ub.I);this.doEvent(ub.g);}};s.getVersion=function(){return ub.J;};});jsx3.TabbedPane=jsx3.gui.TabbedPane;
