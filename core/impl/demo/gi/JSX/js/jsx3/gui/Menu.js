/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.xml.Cacheable","jsx3.gui.Form","jsx3.gui.Heavyweight","jsx3.gui.Block","jsx3.gui.ToolbarButton","jsx3.util.MessageFormat");jsx3.Class.defineClass("jsx3.gui.Menu",jsx3.gui.Block,[jsx3.xml.Cacheable,jsx3.xml.CDF,jsx3.gui.Form],function(e,i){var
ub={Qa:"class='jsx30toolbarbutton_img'",_:"mouseup",ea:"</div>",q:"jsx:///images/tbb/on.gif",ra:"jsxabspath",V:"gu",v:"1",U:"keydown",u:"jsxdisabled",ka:"div",z:"jsxid",d:"#ffffff",La:");",D:")",fa:"E",R:'"',w:"jsxgroupname",Ca:"focus",Q:'<div tabindex="0" jsxindex="',Na:"' ",Pa:" class='jsx30toolbarbutton'",la:"px",qa:"JSX_GENERIC",Wa:";",y:"']",x:"//record[@jsxgroupname='",Za:"#808080",ja:"S",W:"click",Fa:"span",Ha:"box",i:"jsx:///images/menu/submenuarrow.gif",Sa:"&#160;",A:"jsxselected",t:"",Ia:"0 0 0 4",s:"jsx30curvisiblemenu_",X:"hO",r:"#9B9BB7",sa:"jsxhomepath",C:"url(",Ua:"overflow:hidden;",l:"jsx:///images/menu/down_menu.gif",L:"jsxhide",ia:"Y",Ka:"5 4 5 0",_a:"3.0.00",J:"jsxspy",Da:"el",ta:"jsx3.gui.Menu.noData",oa:"Ru",O:"jsx30menu_",k:"jsx:///images/menu/selected.gif",Ga:"relativebox",p:"jsx:///images/tbb/down.gif",P:"_list",I:"none",ha:"N",a:"background-image:url(",Aa:"blur",F:"Book",Ra:"class='jsx30toolbarbutton_lbl'",j:"jsx:///images/menu/selectover.gif",ua:":",S:"mouseover",Ma:"id='",K:"jsx3.gui.Menu.loseFocus",bb:"border",aa:"yz",B:"jsxindex",g:"<div tabIndex ='0' class='jsx30menu_0_list' onmousedown='var e = jsx3.gui.Event.wrap(event); jsx3.gui.Event.publish(e); e.cancelBubble(); e.cancelReturn();'>- no data -</div>",b:"jsx:///images/menu/bg.gif",va:"|",M:"jsxmenu",ab:"pt",f:"JSX_MENU_XSL",T:"O4",ga:"W",ba:' class="',e:"jsx:///xsl/jsxmenu.xsl",za:"mouseout",h:'<div tabIndex="0" class="{0}" onmousedown="var e = jsx3.gui.Event.wrap(event); jsx3.gui.Event.publish(e); e.cancelBubble(); e.cancelReturn();">{1}</div>',c:");background-repeat:repeat-y;",H:"focusNext",wa:"//record[@jsxkeycode]",Ya:"jsxexecute",G:"focusPrevious",Ja:"0px;0px;0px;solid 1px ",ma:"Divider",ca:'" style="',pa:"x",N:"Menu.showTopMenu",o:"jsx:///images/menu/over_menu.gif",Z:"cw",xa:"jsxkeycode",Ta:"class='jsx30toolbarbutton_mnu'",na:"key",Ba:"Qi",da:'">',Oa:"label='",m:"jsx:///images/menu/off_menu.gif",Y:"mousedown",Ea:"1 4 1 0",n:"jsx:///images/menu/on_menu.gif",Xa:"class='jsx30toolbarbutton_cap'",Va:"background-color:",ya:"record",cb:"padding",E:"jsxtype"};var
Ka=jsx3.util.Logger.getLogger(e.jsxclass.getName());var
ia=jsx3.gui.Event;var
Qa=jsx3.gui.Interactive;e.DEFAULTBACKGROUND=ub.a+jsx3.resolveURI(ub.b)+ub.c;e.DEFAULTBACKGROUNDCOLOR=ub.d;e.DEFAULTXSLURL=jsx3.resolveURI(ub.e);e.DEFAULTXSLCACHEID=ub.f;e.DEFAULTCONTEXTLEFTOFFSET=10;e.NODATAMESSAGE=ub.g;e.eK=new
jsx3.util.MessageFormat(ub.h);e.JB=0;e.jJ=1;e.rX=12;e.oT=jsx3.resolveURI(ub.i);e.jW=jsx3.resolveURI(ub.j);e.TN=jsx3.resolveURI(ub.k);e.l4=jsx3.resolveURI(ub.l);e.K0=jsx3.resolveURI(ub.m);e.iQ=jsx3.resolveURI(ub.n);e.yt=jsx3.resolveURI(ub.o);e.aA=jsx3.resolveURI(ub.p);e.JY=jsx3.resolveURI(ub.q);jsx3.html.loadImages(ub.b,e.l4,e.K0,e.iQ,e.yt,e.TN,e.jW,e.oT,e.aA,e.JY);e.YG=ub.r;e.Bu=null;e._A=[];e.WI=[];e.YQ=null;e.Lq=null;e.UL=null;e.eG=250;e.pY=150;e.KA=ub.s;e.HF=1000;i.init=function(k,f){this.jsxsuper(k);if(f!=null)this.setText(f);};i.getImage=function(){return this.jsximage!=null&&jsx3.util.strTrim(this.jsximage)!=ub.t?this.jsximage:null;};i.setImage=function(l){this.jsximage=l;return this;};i.getXSL=function(){return this.qj(e.DEFAULTXSLURL);};i.disableItem=function(m){return this.enableItem(m,false);};i.enableItem=function(p,l){if(l||l==null)this.deleteRecordProperty(p,ub.u,false);else this.insertRecordProperty(p,ub.u,ub.v,false);return this;};i.isItemEnabled=function(k){var
mb=this.getRecordNode(k);return mb!=null&&mb.getAttribute(ub.u)!=ub.v;};i.selectItem=function(o,p){if(p||p==null){var
_a=this.getRecordNode(o);if(_a!=null){var
yb=_a.getAttribute(ub.w);if(yb)for(var
hb=this.getXML().selectNodeIterator(ub.x+yb+ub.y);hb.hasNext();){var
G=hb.next();if(G.getAttribute(ub.z)!=o)G.removeAttribute(ub.A);}}this.insertRecordProperty(o,ub.A,1,false);}else this.deleteRecordProperty(o,ub.A,false);return this;};i.deselectItem=function(n){return this.selectItem(n,false);};i.isItemSelected=function(k){var
D=this.getRecordNode(k);return D!=null&&D.getAttribute(ub.A)==ub.v;};i.et=function(s,d,q){if(q==null)q=parseInt(d.parentNode.parentNode.getAttribute(ub.B));var
ua=e.WI[q];if(ua&&ua!=d){try{ua.style.backgroundImage=ub.C+jsx3.gui.Block.SPACE+ub.D;}catch(Kb){}e.WI[q]=null;}var
cb=false;if(d.getAttribute(ub.u)!=ub.v){var
ta=d.getAttribute(ub.z);cb=this.T0(ta);d.style.backgroundImage=ub.C+e.jW+ub.D;e.WI[q]=d;d.focus();var
ha=e._A[q-1];if(ha)ha.scrollTo(d);}else d.parentNode.parentNode.focus();this.sO(cb?q+1:q);};i.sO=function(n,d){if(e._A.length>n&&(e.mM==null||e.mM>n)){if(e.UL)window.clearTimeout(e.UL);this.N6(n,null);e.mM=n;var
Ma=this;e.UL=window.setTimeout(function(){if(Ma.getParent()==null)return;e.UL=null;e.mM=null;Ma.NL(n+1,true);},d?0:e.pY);}};i.T0=function(g){if(this._jsxP4)return jsx3.util.arrIndexOf(this._jsxP4,g)>=0;return false;};i.N6=function(g,q){if(g<1)return;if(this._jsxP4==null)this._jsxP4=[];if(q==null){this._jsxP4.splice(g-1,this._jsxP4.length);}else this._jsxP4.splice(g-1,this._jsxP4.length,q);};i.oe=function(b,k){if(this.jsxsupermix(b,k))return;var
ba=b.keyCode();if((ba==40||ba==13||ba==32)&&!b.hasModifier()){this.showMenu(b,k,1);b.cancelAll();}else if(ba==39){this.Ww(true);}else if(ba==37)this.Ww(false);};i.Dc=function(r,n){if(!r.leftButton())return;this.mv(r,n);n.childNodes[2].style.backgroundImage=ub.C+e.l4+ub.D;if(this.getState()==e.jJ){if(!this._jsxX1)this.NL();}else this.showMenu(r,n,1);};i.el=function(l,a){if(l.isFakeOver(a))return;if(e.Lq)window.clearTimeout(e.Lq);this.A5(l,a);if(this.getState()==e.JB)a.childNodes[2].style.backgroundImage=ub.C+e.yt+ub.D;if(e.Bu!=null&&this!=e.Bu&&e.Bu.getParent()==this.getParent()||jsx3.EventHelp.FLAG==1&&this.getCanDrop()==1)this.showMenu(l,a,1);};i.Qi=function(n,h){if(n.isFakeOut(h))return;if(e.Lq)window.clearTimeout(e.Lq);if(this.getState()==e.JB||h!=this._jsxB9[0]){this.NM(n,h);h.childNodes[2].style.backgroundImage=ub.C+e.K0+ub.D;}};i.gu=function(o,q){if(o.hasModifier(true))return;var
ha=e.rH(o,q);var
Ha=parseInt(q.getAttribute(ub.B));var
I=ha?ha.getAttribute(ub.z):null;var
rb=ha&&ha.getAttribute(ub.u)==ub.v;var
jb=o.keyCode();if((jb==32||jb==13)&&!rb){this.b2(o,I);this.NL();}else if(jb==39){if(ha&&ha.getAttribute(ub.E)==ub.F&&!rb){this.showMenu(o,ha,Ha+1,I);}else this.Ww(true);}else if(jb==37){if(Ha>1)this.NL(Ha);else this.Ww(false);}else if(jb==27){this.NL();}else if(jb==40){if(!ha)ha=q.lastChild;for(var
t=ha;true;){if(t==t.parentNode.lastChild){t=t.parentNode.firstChild;}else t=t.nextSibling;if(t==ha)break;if(t.getAttribute(ub.u)!=ub.v){this.et(o,t,Ha);break;}}}else if(jb==38){if(!ha)ha=q.firstChild;for(var
t=ha;true;){if(t==t.parentNode.firstChild){t=t.parentNode.lastChild;}else t=t.previousSibling;if(t==ha)break;if(t.getAttribute(ub.u)!=ub.v){this.et(o,t,Ha);break;}}}else if(jb==9){jsx3.html[o.shiftKey()?ub.G:ub.H](this.getRendered(o),o);return;}else return;o.cancelAll();};i.Ww=function(a){var
ja=this.getParent().getChildren();var
va=this.getChildIndex();var
Jb=a?1:-1;for(var
ma=va;true;){ma=jsx3.util.numMod(ma+Jb,ja.length);if(ma==va)break;var
xa=ja[ma];if(xa instanceof e&&xa.getDisplay()!=ub.I){try{xa.focus();}catch(Kb){}break;}}};i.cw=function(f,g){if(!f.leftButton()){f.cancelBubble();return;}var
nb=parseInt(g.getAttribute(ub.B));var
Z=e.rH(f,g);if(Z&&Z.getAttribute(ub.E)==ub.F)this.showMenu(f,Z,nb+1,Z.getAttribute(ub.z));f.cancelBubble();};i.yz=function(j,c){if(!j.leftButton())j.cancelBubble();};i.O4=function(h,s){var
pa=e.rH(h,s);if(pa&&h.isFakeOver(pa))return;if(e.Lq)window.clearTimeout(e.Lq);e.NP();var
da=parseInt(s.getAttribute(ub.B));if(pa){var
T=pa.getAttribute(ub.E);var
C=pa.getAttribute(ub.u)==ub.v;var
ea=pa.getAttribute(ub.z);this.et(h,pa,da);if(T==ub.F)if(!this.T0(ea)){var
J=this;h.sf();e.Lq=window.setTimeout(function(){if(J.getParent()==null)return;e.Lq=null;J.showMenu(h,pa,da+1,ea);},e.eG);}if(ea!=null)this.sp(h,ea,T==ub.F);}};i.sp=function(k,o,r){if(this.hasEvent(ub.J)){k.sf();var
N=this;e.fY=window.setTimeout(function(){if(N.getParent()==null)return;e.fY=null;var
J=N.doEvent(ub.J,{objEVENT:k,strRECORDID:o});if(J){jsx3.gui.Interactive.hideSpy();N.showSpy(J,k);}},r?e.HF:jsx3.EventHelp.HF);}};e.NP=function(){if(e.fY){window.clearTimeout(e.fY);e.fY=null;}jsx3.gui.Interactive.hideSpy();};e.rH=function(m,s){var
Ha=m.srcElement();while(Ha!=null&&Ha.getAttribute(ub.E)==null){Ha=Ha.parentNode;if(Ha==s||Ha==s.ownerDocument)Ha=null;}return Ha;};i.hO=function(r,n){if(!r.leftButton()){r.cancelBubble();return;}var
E=e.rH(r,n);if(E){var
C=E.getAttribute(ub.E);var
ca=E.getAttribute(ub.u);if(C!=ub.F&&!ca){this.b2(r,E.getAttribute(ub.z));this.NL();}}};i.Ru=function(j){jsx3.sleep(function(){this.NL(1,true);},ub.K,this);};i.NL=function(g,o){if(this!=e.Bu)return;if(g==null)g=1;if(e.UL)window.clearTimeout(e.UL);e.NP();this.N6(g-1,null);for(var
Mb=e._A.length-1;Mb>=g-1;Mb--){var
K=e._A[Mb];if(K)K.destroy();}e._A.splice(g-1,e._A.length);e.WI.splice(g,e.WI.length);if(g==1){this.UJ(e.JB);this.doEvent(ub.L);e.Bu=null;e.x1=false;ia.unsubscribeLoseFocus(this,this);}if(!o)try{if(this._jsxB9[g-1])this._jsxB9[g-1].focus();else if(g==1)this.focus();}catch(Kb){}this._jsxB9.splice(g-1,this._jsxB9.length);};i.showContextMenu=function(r,p,h,f){e.x1=true;this._jsxPI=p;this._jsxSt=h;this.showMenu(r,null,1,null,f);};i.getContextParent=function(){return this._jsxPI;};i.getContextRecordId=function(){return this._jsxSt;};i.showMenu=function(k,d,b,g,a){var
hb=this.doEvent(ub.M,{objEVENT:k,objANCHOR:d,intINDEX:b,strPARENTID:g});if(hb===false)return;if(d==null&&g!=null)d=this.getRendered(k);if(b==1){this._jsxX1=true;if(e.Bu!=null&&e.Bu!=this)e.Bu.NL(1,true);e.Bu=this;}k.sf();jsx3.sleep(function(){this.bT(k,d,b,g,a);},ub.N,this,true);if(this._jsxB9==null)this._jsxB9=[];};i.bT=function(r,q,g,s,h){if(g>1&&!e._A[g-2])return;this._jsxB9[g-1]=q;this.N6(g,s);var
Cb=e.KA+g;var
kb=this.aY(s,g);var
u=ub.O+e.S4()+ub.P;var
Ib=ub.Q+g+ub.R+this.Gj(ub.S,ub.T)+this.Gj(ub.U,ub.V)+this.Gj(ub.W,ub.X)+this.Gj(ub.Y,ub.Z)+this.Gj(ub._,ub.aa)+ub.ba+u+ub.ca+this.zf()+this.Of()+ub.da+kb+ub.ea;var
B=e._A[g-1];if(B)B.destroy();var
Eb=e._A[g-1]=new
jsx3.gui.Heavyweight(Cb,this);Eb.setHTML(Ib);Eb.setScrolling(true);if(q&&s!=null){Eb.addXRule(q,ub.fa,ub.ga,-4);Eb.addXRule(q,ub.ga,ub.fa,8);Eb.addYRule(q,ub.ha,ub.ha,0);Eb.addRule(null,ub.ha,0,ub.ia);}else if(q){Eb.addXRule(q,ub.ga,ub.ga,0);Eb.addXRule(q,ub.fa,ub.fa,0);Eb.addYRule(q,ub.ja,ub.ha,0);Eb.addYRule(q,ub.ha,ub.ja,0);}else{Eb.addXRule(h?h.L:r,null,ub.ga,e.DEFAULTCONTEXTLEFTOFFSET);Eb.addXRule(h?h.L:r,null,ub.fa,-e.DEFAULTCONTEXTLEFTOFFSET);Eb.addYRule(h?h.T:r,null,ub.ha,e.rX);Eb.addYRule(h?h.T:r,null,ub.ja,-e.rX);Eb.addRule(null,ub.ha,0,ub.ia);}if(g==1&&!e.x1)this.UJ(e.jJ,q);Eb.show();var
Oa=Eb.getRendered(q);var
Wa=Oa.childNodes[0].childNodes[0];if(jsx3.CLASS_LOADER.IE){var
Va=Math.max(Wa.offsetWidth-2,Wa.clientWidth);for(var
ca=0;ca<Wa.childNodes.length;ca++){var
qb=Wa.childNodes[ca];if(qb.nodeName&&qb.nodeName.toLowerCase()==ub.ka)if(qb.getAttribute(ub.z))Wa.childNodes[ca].style.width=Va+ub.la;else if(qb.getAttribute(ub.E)==ub.ma&&qb.offsetWidth<30)Wa.childNodes[ca].style.width=Math.max(0,Va-(qb.offsetWidth-1))+ub.la;}}if(g>1&&r.getType().indexOf(ub.na)==0){this.et(r,Wa.childNodes[0],g);}else Wa.focus();if(g==1){ia.subscribeLoseFocus(this,this,ub.oa);jsx3.sleep(function(){this._jsxX1=false;},null,this);}};i.repaint=function(){this.NL();return this.jsxsuper();};e.S4=function(){var
U=jsx3.html.getMode();if(U==2&&jsx3.CLASS_LOADER.IE7)U=U+ub.pa;return U;};i.aY=function(d,b){var
Fb={jsxtabindex:this.getIndex()?this.getIndex():0,jsxselectedimage:e.TN,jsxtransparentimage:jsx3.gui.Block.SPACE,jsxdragtype:ub.qa,jsxid:this.getId(),jsxsubmenuimage:e.oT,jsxmode:e.S4(),jsxpath:jsx3.getEnv(ub.ra),jsxpathapps:jsx3.getEnv(ub.sa),jsxpathprefix:this.getUriResolver().getUriPrefix(),jsxkeycodes:this.oA(d)};if(d!=null)Fb.jsxrootid=d;var
ja=this.getXSLParams();for(var tb in ja)Fb[tb]=ja[tb];var
zb=this.doTransform(Fb);if(!jsx3.xml.Template.supports(1))zb=jsx3.html.removeOutputEscapingSpan(zb);zb=this.Al(zb);if(jsx3.util.strTrim(zb)==ub.t){var
Q=ub.O+e.S4()+ub.P;zb=e.eK.format(Q,this.wi(ub.ta));}return zb;};i.oA=function(h){if(this._jsxVY==null)return ub.t;var
db=[];for(var qa in this._jsxVY)db[db.length]=qa+ub.ua+this._jsxVY[qa].getFormatted();db[db.length]=ub.t;return db.join(ub.va);};i.clearCachedContent=function(){};i.getState=function(){return this._jsxBA!=null?this._jsxBA:e.JB;};i.UJ=function(a,b){if(b==null)b=this.getRendered(b);if(b!=null)if(a==e.jJ){if(e.YQ==this)return this;if(e.YQ!=null)e.YQ.UJ(e.JB);b.style.backgroundImage=ub.C+e.JY+ub.D;b.childNodes[3].style.backgroundColor=e.YG;b.childNodes[2].style.backgroundImage=ub.C+e.iQ+ub.D;e.YQ=this;}else{b.style.backgroundImage=ub.t;b.childNodes[3].style.backgroundColor=ub.t;b.childNodes[2].style.backgroundImage=ub.C+e.K0+ub.D;if(e.YQ==this)e.YQ=null;}this._jsxBA=a;return this;};i.U8=function(){if(this._jsxVY!=null)for(var Ma in this._jsxVY)this._jsxVY[Ma].destroy();this._jsxVY={};var
bb=this.getId();var
ha=this.getXML();if(ha!=null)for(var
pa=ha.selectNodeIterator(ub.wa);pa.hasNext();){var
Mb=pa.next();var
ka=Mb.getAttribute(ub.xa).toLowerCase();var
K=Mb.getAttribute(ub.z);var
ta=jsx3.makeCallback(function(c,m){this.lx(m[0],c);},this,K);this._jsxVY[K]=this.doKeyBinding(ta,ka);}};i.lx=function(o,k){var
fa=this.getRecordNode(k);if(fa==null)return;var
pa=fa.getParent();var
Z=pa!=null&&pa.getNodeName()==ub.ya?pa.getAttribute(ub.z):null;var
Pa=this.doEvent(ub.M,{objEVENT:o,objANCHOR:null,intINDEX:null,strPARENTID:Z});if(Pa===false)return;if(this.isItemEnabled(k))this.b2(o,k);};e.Tj={};e.Tj[ub.U]=true;e.Tj[ub.Y]=true;e.Tj[ub.za]=true;e.Tj[ub.S]=true;e.Tj[ub.Aa]=ub.Ba;e.Tj[ub.Ca]=ub.Da;i.Rc=function(g,d,k){this.Gi(g,d,k,3);};i.Gc=function(){this.applyDynamicProperties();var
L=this.getRelativePosition()!=0;var
Db={height:22};if(L){var
wa=this.getMargin();Db.margin=wa!=null&&wa!=ub.t?wa:ub.Ea;Db.tagname=ub.Fa;Db.boxtype=ub.Ga;}else{var
Da=this.getLeft();var
A=this.getTop();Db.left=Da!=null&&Da!=ub.t?Da:0;Db.top=A!=null&&A!=ub.t?A:0;Db.tagname=ub.ka;Db.boxtype=ub.Ha;}if(this.getDivider()==1){Db.padding=ub.Ia;Db.border=ub.Ja+e.YG;}var
ib=new
jsx3.gui.Painted.Box(Db);Db={height:22,tagname:ub.Fa,boxtype:ub.Ga};Db.width=this.getImage()!=null&&this.getImage()!=ub.t?22:3;var
nb=new
jsx3.gui.Painted.Box(Db);ib.Yf(nb);Db={height:22,tagname:ub.Fa,boxtype:ub.Ga};if(jsx3.util.strEmpty(this.getText())){Db.width=1;}else Db.padding=ub.Ka;var
O=new
jsx3.gui.Painted.Box(Db);ib.Yf(O);var
J=new
jsx3.gui.Painted.Box({width:11,height:22,tagname:ub.Fa,boxtype:ub.Ga});ib.Yf(J);var
aa=new
jsx3.gui.Painted.Box({width:1,height:22,tagname:ub.Fa,boxtype:ub.Ga});ib.Yf(aa);return ib;};i.paint=function(){this.applyDynamicProperties();if(this.getXmlAsync())this.getXML();this.U8();var
K=this;var
zb=this.getState()==e.jJ?ub.a+e.JY+ub.La:ub.t;var
Lb=this.bd();var
y=this.bi();var
oa=null,Z=null,ga=null;if(this.getEnabled()==1){oa=this.jh(e.Tj,0);ga=ub.t;}else{oa=ub.t;ga=jsx3.html.getCSSOpacity(0.4);}if(this.getImage()!=null)Z=this.getUriResolver().resolveURI(this.getImage());var
qb=this.renderAttributes(null,true);var
B=this.Wl(true);B.setAttributes(ub.Ma+this.getId()+ub.Na+ub.Oa+this.getName()+ub.Na+this.Fo()+this.bn()+oa+ub.Pa+qb);B.setStyles(this.Xe(true)+zb+Lb+y+ga+this.zk()+this.fh());var
D=B.lg(0);D.setStyles(Z!=null?ub.a+Z+ub.La:ub.t);D.setAttributes(ub.Qa+jsx3.html.Kf);var
aa=B.lg(1);aa.setAttributes(ub.Ra+jsx3.html.Kf);var
tb=this.getText();if(tb!=null&&tb!=ub.t){aa.setStyles(this.eh()+this.ad()+this.Yh()+this.Bf());}else{tb=ub.Sa;aa.setStyles(jsx3.html.Fc);}var
ja=B.lg(2);ja.setStyles(ub.a+(this.getState()==e.jJ?e.iQ:e.K0)+ub.La);ja.setAttributes(ub.Ta);var
Va=B.lg(3);Va.setStyles(ub.Ua+(this.getState()==e.jJ?ub.Va+e.YG+ub.Wa:ub.t));Va.setAttributes(ub.Xa);return B.paint().join(D.paint().join(ub.Sa)+aa.paint().join(tb)+ja.paint().join(ub.Sa)+Va.paint().join(ub.Sa));};i.zf=function(){return ub.Va+(this.getBackgroundColor()?this.getBackgroundColor():e.DEFAULTBACKGROUNDCOLOR)+ub.Wa;};i.Of=function(){return this.getBackground()?this.getBackground()+ub.Wa:e.DEFAULTBACKGROUND;};i.executeRecord=function(h){this.b2(this.isOldEventProtocol(),h);return this;};i.b2=function(b,k){var
N=null;if((N=this.getRecordNode(k))!=null){this._jsxvalue=k;var
L=N.getAttribute(ub.Ya);var
Ua=true;var
Hb={strRECORDID:k,objRECORD:N};if(b instanceof jsx3.gui.Event)Hb.objEVENT=b;Ua=jsx3.util.strEmpty(L)?true:this.eval(L,Hb);if(Ua!==false&&b)this.doEvent(ub.Ya,Hb);}};i.redrawRecord=function(){if(this==e.Bu)this.NL();return this;};i.getValue=function(){return this._jsxvalue;};i.doValidate=function(){return this.setValidationState(1).getValidationState();};i.beep=function(){var
Ya=this.getRendered();jsx3.gui.Yl(Ya.childNodes[2],{backgroundImage:ub.C+e.l4+ub.D});jsx3.gui.Yl(Ya,{backgroundImage:ub.C+e.aA+ub.D});jsx3.gui.Yl(Ya.childNodes[3],{backgroundColor:ub.Za});return this;};e.getVersion=function(){return ub._a;};i.emGetType=function(){return jsx3.gui.Matrix.EditMask.FORMAT;};i.emInit=function(g){this.jsxsupermix(g);this.subscribe(ub.Ya,this,ub.ab);};i.emSetValue=function(a){};i.emGetValue=function(){return null;};i.emBeginEdit=function(d,k,g,l,b,o,f){var
v=f.childNodes[0].childNodes[0];if(v){this.jsxsupermix(d,k,g,l,b,o,f);v.focus();}else return false;};i.emPaintTemplate=function(){this.setEnabled(0);var
W=this.paint();this.setEnabled(1);var
Oa=this.paint();return this.Tn(Oa,W);};i.pt=function(f){var
db=this.emGetSession();if(db){db.td.focus();this.NL(1,true);}};i.containsHtmlElement=function(l){var
Jb=this.jsxsuper(l);if(!Jb&&this==e.Bu)for(var
G=0;G<e._A.length&&!Jb;G++){var
Mb=e._A[G];if(Mb)Jb=Mb.containsHtmlElement(l);}return Jb;};i.getDivider=function(){return this.jsxdivider!=null?this.jsxdivider:0;};i.setDivider=function(f,m){this.jsxdivider=f;if(m)this.recalcBox([ub.bb,ub.cb]);else this.ce();return this;};i.emCollapseEdit=function(n){this.NL(1,true);};});jsx3.gui.Menu.prototype.mv=jsx3.gui.ToolbarButton.prototype.Dc;jsx3.gui.Menu.prototype.A5=jsx3.gui.ToolbarButton.prototype.el;jsx3.gui.Menu.prototype.NM=jsx3.gui.ToolbarButton.prototype.Qi;jsx3.Menu=jsx3.gui.Menu;
