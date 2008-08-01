/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.xml.Cacheable","jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Tree",jsx3.gui.Block,[jsx3.gui.Form,jsx3.xml.Cacheable,jsx3.xml.CDF],function(h,d){var
ub={oa:"mouseover",k:")",O:"object",Ga:"jsxabspath",ea:"px",_:"jsxdrop",p:"1",P:"jsxtoggle",ra:"mousemove",q:"",V:"dropverb",v:"jsxselect",ha:"append",I:"<div id='JSX' class='jsx30tree_dragicon' style='",Aa:' style="position:absolute;left:0px;top:0px;width:1px;height:1px;" ',a:"JSX_TREE_XSL",F:"plusminus",u:"strRecordId",U:"QA",ua:"box",j:"jsx:///images/tree/over.gif",ka:"record",d:"jsx:///images/tree/plus.gif",z:"nX",S:"JSXDragId",fa:"7px",D:"_",w:"jsxchange",K:"</div>",R:"none",Ca:' id="',aa:"jsxmenu",g:"jsx:///images/matrix/insert_before.gif",B:"//record[@jsxselected='1']",b:"jsx:///xsl/jsxtree.xsl",Q:"block",va:"div",M:"jsxopen",qa:"mousedown",la:"jsxexecute",f:"jsx:///images/tree/select.gif",ga:"12px",y:"jsxid",T:"mouseup",ba:"jsxspy",x:"string",e:"jsx:///images/tree/file.gif",za:"Vl",ja:"_jsxspy",c:"jsx:///images/tree/minus.gif",h:"jsx:///images/matrix/append.gif",W:"insertbefore",H:"icon",wa:"100%",Ha:"jsxhomepath",Fa:"JSX_GENERIC",G:"text",ma:"click",i:"url(",ca:"aZ",A:"solid 1px ",t:";",pa:"mouseout",N:"jsxdata",Ia:"3.0.00",s:"background-color:",X:"rowcontext",Z:"jsxctrldrop",o:"jsxselected",xa:'<img src="',sa:"keydown",r:"null",C:"jsxtext",Ba:"/>",na:"dblclick",l:"#8CAEDF",ia:"jsxcanceldrop",da:"jsxbeforedrop",L:"jsxlazy",m:"#ffffff",Da:' class="jsx30tree" ',Y:"jsxadopt",J:"'>",Ea:'<div class="jsx30tree_drop_icon">&#160;</div>',n:"&#160;",ya:'"',ta:"focus",E:"jsxtype"};var
Oa=jsx3.gui.Event;var
Ia=jsx3.gui.Interactive;h.DEFAULTXSLID=ub.a;h.DEFAULTXSLURL=jsx3.resolveURI(ub.b);h.ICONMINUS=ub.c;h.ICONPLUS=ub.d;h.ICON=ub.e;h.SELECTEDIMAGE=jsx3.resolveURI(ub.f);h.INSERT_BEFORE_IMG=jsx3.resolveURI(ub.g);h.APPEND_IMG=jsx3.resolveURI(ub.h);h.ONDROPBGIMAGE=ub.i+jsx3.resolveURI(ub.j)+ub.k;jsx3.html.loadImages(h.ICONMINUS,h.ICONPLUS,h.ICON,h.SELECTEDIMAGE,ub.j,h.INSERT_BEFORE_IMG,h.APPEND_IMG);h.BORDERCOLOR=ub.l;h.DEFAULTBACKGROUNDCOLOR=ub.m;h.DEFAULTNODATAMSG=ub.n;h.aZ=null;h.q7=500;h.DB=null;h.oy=250;h.CT=null;h.MULTI=1;h.SINGLE=0;d.init=function(m,i){this.jsxsuper(m);if(i!=null)this.insertRecordProperty(i,ub.o,ub.p,false);};d.onAfterAttach=function(){if(this.jsxvalue!=null&&this.jsxvalue!=ub.q&&this.jsxvalue!=ub.r)this.setValue(this.jsxvalue);this.jsxsuper();};d.getXSL=function(){return this.qj(h.DEFAULTXSLURL);};d.doValidate=function(){var
ab=this.H4();var
N=ab.length>0||this.getRequired()==0;this.setValidationState(N?1:0);return this.getValidationState();};d.zf=function(){return ub.s+(this.getBackgroundColor()?this.getBackgroundColor():h.DEFAULTBACKGROUNDCOLOR)+ub.t;};d.setValue=function(n,j){var
ia=this.getValue();var
wb=this.getMultiSelect()==1;var
lb=false;lb=this.isOldEventProtocol();if(n instanceof Array){if(!wb)throw new
jsx3.IllegalArgumentException(ub.u,n);}else if(wb)n=[n];if(wb){this.hJ();for(var
ea=0;ea<n.length;ea++){var
la=n[ea];if(la!=null&&!this.Pk(la))continue;this.ND(la);}if(j&&n.length>0)this.revealRecord(n[0]);if(lb)this.doEvent(ub.v,{strRECORDID:n[0],strRECORDIDS:n});}else{if(n!=null&&!this.Pk(n))return this;this.hJ();if(n!=null){this.ND(n);if(j)this.revealRecord(n);}if(lb)this.doEvent(ub.v,{strRECORDID:n,strRECORDIDS:[n]});}if(lb)this.doEvent(ub.w,{objEVENT:null,preVALUE:ia});return this;};d.D9=function(j,p,k,n,s){var
cb=this.getValue();var
zb=this.getMultiSelect()==1;if(zb&&k){if(p!=null&&!this.Pk(p))return;var
z=false;if(this.Dp(p)){if(n)this.PE(p);}else{this.ND(p);z=true;}if(z&&!s)this.doEvent(ub.v,{objEVENT:j,strRECORDID:p,strRECORDIDS:[p]});}else{var
jb=this.getValue()==p;if(!n&&jb&&!k)return;this.hJ();if(p!=null&&!this.Pk(p))p=null;if(p!=null)if(jb)this.PE(p);else this.ND(p);if(!s){var
pa=jb?null:p;var
X=jb?[]:[pa];this.doEvent(ub.v,{objEVENT:j,strRECORDID:pa,strRECORDIDS:X});}}if(!s)this.doEvent(ub.w,{objEVENT:j,preVALUE:cb});};d.Pu=function(m){var
mb=typeof m==ub.x?this.fy(m):m;if(mb!=null&&mb.getAttribute){m=mb.getAttribute(ub.y);if(m)try{mb.childNodes[0].childNodes[2].focus();this.PU(mb.getAttribute(ub.y));}catch(Kb){}}else this.PU(null);};d.PU=function(e){if(e!=null){if(this._jsxf2==null)if(this.getMultiSelect()==1)Oa.subscribeLoseFocus(this,this.getRendered(),ub.z);this._jsxf2=e;}else{if(this._jsxf2!=null)Oa.unsubscribeLoseFocus(this);this._jsxf2=null;}};d.nX=function(i){Oa.unsubscribeLoseFocus(this);this._jsxf2=null;};d.qX=function(n){var
ea=this.fy(n);if(ea!=null){var
ga=jsx3.html.selectSingleElm(ea,0,2);ga.style.backgroundImage=ub.i+h.SELECTEDIMAGE+ub.k;ga.style.borderRight=ub.A+h.BORDERCOLOR;}};d.JZ=function(c){var
ua=this.fy(c);if(ua!=null){var
Sa=jsx3.html.selectSingleElm(ua,0,2);Sa.style.backgroundImage=ub.q;Sa.style.borderRight=ub.q;}};d.hJ=function(){for(var
Gb=this.rA();Gb.hasNext();){var
H=Gb.next();H.removeAttribute(ub.o);this.JZ(H.getAttribute(ub.y));}};d.PE=function(p){this.deleteRecordProperty(p,ub.o,false);this.JZ(p);};d.ND=function(e){this.insertRecordProperty(e,ub.o,ub.p,false);this.qX(e);};d.rA=function(){return this.getXML().selectNodeIterator(ub.B);};d.H4=function(){var
tb=this.rA();var
Kb=[];while(tb.hasNext()){var
gb=tb.next();Kb[Kb.length]=gb.getAttribute(ub.y);}return Kb;};d.revealRecord=function(i,j){var
Ra=this.getRecordNode(i);var
H=Ra?Ra.getParent():null;while(H!=null){this.toggleItem(H.getAttribute(ub.y),true);H=H.getParent();}var
v=this.fy(i);if(v){var
zb=j?j.getRendered(v):this.getRendered(v);if(zb)jsx3.html.scrollIntoView(v,zb,0,10);}};d.getValue=function(){return this.getMultiSelect()==0?this.H4()[0]:this.H4();};d.getKeyListener=function(){return this.jsxkeylistener==null?1:this.jsxkeylistener;};d.setKeyListener=function(s){this.jsxkeylistener=s;return this;};d.getText=function(){var
X=this.rA().next();return X!=null?X.getAttribute(ub.C):null;};d.getMultiSelect=function(){return this.jsxmultiselect==null?0:this.jsxmultiselect;};d.setMultiSelect=function(r){this.jsxmultiselect=r;return this;};d.redrawRecord=function(p,i){var
U=this.fy(p);if(i==0){if(U)if(U.parentNode.childNodes.length>1){jsx3.html.removeNode(U);}else{var
lb=U.parentNode.parentNode;var
z=lb.getAttribute(ub.y);jsx3.html.setOuterHTML(lb,this.doTransform(z));}return this;}if(U==null){var
Sa=this.getRecordNode(p);if(Sa!=null)if(this.getParent()!=null){Sa=Sa.getParent();var
z=Sa.getAttribute(ub.y);var
lb=this.fy(z);if(lb!=null)jsx3.html.setOuterHTML(lb,this.doTransform(z));}}else jsx3.html.setOuterHTML(U,this.doTransform(p));return this;};d.getRoot=function(){return this.jsxuseroot!=null?this.jsxuseroot:1;};d.setRoot=function(g){this.jsxuseroot=g;return this;};d.getIcon=function(){return this.jsxicon!=null?this.jsxicon:h.ICON;};d.setIcon=function(p){this.jsxicon=p;return this;};d.getIconMinus=function(){return this.jsxiconminus!=null?this.jsxiconminus:h.ICONMINUS;};d.setIconMinus=function(f){this.jsxiconminus=f;return this;};d.getIconPlus=function(){return this.jsxiconplus!=null?this.jsxiconplus:h.ICONPLUS;};d.setIconPlus=function(o){this.jsxiconplus=o;return this;};d.fy=function(f){var
qb=this.getDocument();return qb!=null?qb.getElementById(this.getId()+ub.D+f):null;};d.Vl=function(n,r){if(r!=n.srcElement())return;var
N=this.H4()[0];if(N){this.Pu(N);}else{var
ka=this.getRendered(r).childNodes[0];if(ka!=null)this.Pu(ka);}};d.Tg=function(f,g){if(!f.leftButton())return;g=f.srcElement();var
ea=this.getRendered(g);while(jsx3.util.strEmpty(g.getAttribute(ub.E))&&g!=ea)g=g.parentNode;if(g.getAttribute(ub.E)!=null){if(g.getAttribute(ub.E)==ub.F){this.A3(f,g.parentNode.parentNode.getAttribute(ub.y));}else if(g.getAttribute(ub.E)==ub.G||g.getAttribute(ub.E)==ub.H){var
ob=g.parentNode.parentNode.getAttribute(ub.y);var
Kb=this.yH();this.Pu(g.parentNode.parentNode);if(!g.parentNode)g=this.fy(ob).childNodes[0].childNodes[2];if(this.Pk(ob))if(f.shiftKey()&&this.getMultiSelect()==1){if(Kb){this.revealRecord(Kb);this.ZV(f,Kb,ob);}else this.D9(f,ob,false,true);}else{var
Ga=jsx3.gui.isMouseEventModKey(f);if(Ga||!this.Dp(ob))this.D9(f,ob,Ga,Ga);}}else this.Pu(this.H4()[0]);}else this.Pu(this.H4()[0]);};d.yH=function(){if(this._jsxf2!=null)return this._jsxf2;var
sb=this.H4();if(sb.length==1)return sb[0];return null;};d.ZV=function(r,j,o){var
Na=this.getValue();var
eb=false;var
Ba=!jsx3.gui.isMouseEventModKey(r)||!this.Dp(o);var
tb=[j];var
V=j;while((V=this.cA(V))!=null){tb.push(V);if(V==o){eb=true;break;}}if(!eb){tb=[j];V=j;while((V=this.Yz(V))!=null){tb.push(V);if(V==o){eb=true;break;}}if(!eb)return;}var
ca=[];for(var
pa=0;pa<tb.length;pa++){var
vb=this.Dp(tb[pa]);if(!vb)ca.push(tb[pa]);}if(!jsx3.gui.isMouseEventModKey(r))this.hJ();for(var
pa=0;pa<tb.length;pa++){var
na=tb[pa];var
vb=this.Dp(na);if(Ba||vb)this.D9(null,na,true,!Ba&&vb,true);}if(Ba)this.doEvent(ub.v,{objEVENT:r,strRECORDID:ca[0],strRECORDIDS:ca});this.doEvent(ub.w,{objEVENT:r,preVALUE:Na});};d.Pk=function(o){var
ma=o instanceof jsx3.xml.Entity?o:this.getRecord(o);return ma!=null&&ma.jsxunselectable!=ub.p;};d.Dp=function(j){return this.getRecordNode(j).getAttribute(ub.o)==ub.p;};h.getDragIcon=function(g,s,n,o){return ub.I+jsx3.html.getCSSOpacity(0.75)+ub.J+jsx3.html.getOuterHTML(g.parentNode.childNodes[1])+jsx3.html.getOuterHTML(g)+ub.K;};d.toggleItem=function(p,q,c){var
Mb=this.getRecordNode(p);var
Ua=this.fy(p);if(Ua!=null)this.B8(Mb,Ua,q,c);return this;};d.A3=function(b,e,i,l){var
H=this.getRecordNode(e);var
Ba=this.fy(e);if(Ba!=null){var
Na=null;if(H.getAttribute(ub.L)==ub.p&&H.getAttribute(ub.M)!=ub.p&&(i==null||i===true)){jsx3.html.updateCSSOpacity(Ba.childNodes[0].childNodes[0],0.5);jsx3.sleep(function(){if(this.getParent()==null)return;var
Ra=this.doEvent(ub.N,{objXML:this.getXML(),objNODE:H});if(Ra&&typeof Ra==ub.O){if(Ra.bCLEAR)H.removeAttribute(ub.L);if(Ra.arrNODES!=null){H.removeChildren();for(var
O=0;O<Ra.arrNODES.length;O++)H.appendChild(Ra.arrNODES[O]);}}else H.removeAttribute(ub.L);this.redrawRecord(e,2);if(l!=null)l();},null,this);H.setAttribute(ub.M,ub.p);Na=true;}else Na=this.B8(H,Ba,i,false);this.doEvent(ub.P,{objEVENT:b,strRECORDID:e,objRECORD:H,bOPEN:Na});}};d.B8=function(s,c,q,r){var
na=s.getAttribute(ub.M)==ub.p;if(q==null)q=!na;if(na==q)return q;if(q){c.childNodes[0].childNodes[0].src=this.getUriResolver().resolveURI(this.getIconMinus());c.childNodes[1].style.display=ub.Q;s.setAttribute(ub.M,ub.p);}else{c.childNodes[0].childNodes[0].src=this.getUriResolver().resolveURI(this.getIconPlus());c.childNodes[1].style.display=ub.R;s.removeAttribute(ub.M);}if(r)c.childNodes[0].childNodes[0].setAttribute(ub.E,ub.F);return q;};d.Dc=function(q,o){if(this.getCanDrag()==1&&!q.rightButton()){var
F=q.srcElement();if(F==null)return;var
O=false;if(jsx3.util.strEmpty(F.getAttribute(ub.E)))F=F.parentNode;if(F.getAttribute(ub.E)==ub.H){O=true;F=F.parentNode.childNodes[2];}if(F.getAttribute(ub.E)==ub.G){if(!this.Pk(F.getAttribute(ub.S)))return;var
V=this;q.sf();h.CT=window.setTimeout(function(){h.CT=null;Oa.unsubscribe(ub.T,V,ub.U);if(V.getParent()!=null){V.Tg(q,o);V.doDrag(q,F,h.getDragIcon,{strDRAGIDS:V.H4()});}},h.oy);Oa.subscribe(ub.T,this,ub.U);if(O){Oa.publish(q);q.cancelAll();}}}};d.QA=function(p){Oa.unsubscribe(ub.T,this,ub.U);if(h.CT)window.clearTimeout(h.CT);};d.Df=function(b,k){var
Bb=b.srcElement();if(Bb==null)return;var
kb=Bb.getAttribute(ub.E);if(jsx3.util.strEmpty(kb))Bb=Bb.parentNode;kb=Bb.getAttribute(ub.E);if(this.getCanDrop()==1&&jsx3.EventHelp.isDragging()){var
Z=this.yR(b);var
sb=Z.getAttribute(ub.V)==ub.W;var
ca=Z.getAttribute(ub.X);if(ca!=null){var
vb=jsx3.EventHelp.JSXID;var
la=jsx3.EventHelp.getDragId();var
x=jsx3.EventHelp.getDragIds();var
Sa=jsx3.EventHelp.DRAGTYPE;var
Cb=jsx3.gui.isMouseEventModKey(b);if(vb==null)return;var
Ua=vb.doEvent(ub.Y,{objEVENT:b,strRECORDID:la,strRECORDIDS:x,objTARGET:this,bCONTROL:Cb});var
D={objEVENT:b,strRECORDID:ca,objSOURCE:vb,strDRAGID:la,strDRAGIDS:x,strDRAGTYPE:Sa,bINSERTBEFORE:sb,bALLOWADOPT:Ua!==false};var
cb=this.doEvent(Cb?ub.Z:ub._,D);if(Ua!=false&&cb!==false&&vb.instanceOf(jsx3.xml.CDF)){for(var
v=0;v<x.length;v++)if(sb)this.adoptRecordBefore(vb,x[v],ca);else this.adoptRecord(vb,x[v],ca);this.revealRecord(x[0]);}}}else if((kb==ub.G||kb==ub.H)&&b.rightButton()&&this.getMenu()){var
ca=Bb.parentNode.parentNode.getAttribute(ub.y);var
ka=this.getServer().getJSX(this.getMenu());if(ka!=null&&this.Pk(ca)){var
ta=this.doEvent(ub.aa,{objEVENT:b,objMENU:ka,strRECORDID:ca});if(ta!==false){if(ta instanceof Object&&ta.objMENU instanceof jsx3.gui.Menu)ka=ta.objMENU;var
N=b.shiftKey()||jsx3.gui.isMouseEventModKey(b);if(this.Dp(ca))this.Pu(ca);else this.D9(b,ca,N,N);ka.showContextMenu(b,this,ca);}}}this.XF(b);};d.el=function(j,s){var
M=j.toElement();if(M==null)return;var
jb=M.getAttribute(ub.E);if(jb==ub.G&&this.hasEvent(ub.ba)){var
wb=M;while(wb.getAttribute(ub.y)==null&&wb!=s)wb=wb.parentNode;if(wb==s)return;var
_=wb.getAttribute(ub.y);this.applySpyStyle(M);var
ma=j.clientX()+jsx3.EventHelp.DEFAULTSPYLEFTOFFSET;var
wa=j.clientY()+jsx3.EventHelp.DEFAULTSPYTOPOFFSET;j.sf();var
sa=this;if(h.DB)window.clearTimeout(h.DB);h.DB=window.setTimeout(function(){h.DB=null;if(sa.getParent()!=null)sa.tF(j,_,M,wb);},jsx3.EventHelp.SPYDELAY);}};d._ebMouseMove=function(l,m){if(jsx3.EventHelp.isDragging()&&this.getCanDrop()==1){var
Ja=l.srcElement();if(Ja==null)return;var
lb=Ja.getAttribute(ub.E);var
ob=Ja;while(ob.getAttribute(ub.y)==null&&ob!=m)ob=ob.parentNode;if(ob==m)return;var
y=ob.getAttribute(ub.y);if(lb==ub.F){var
la=this.getRecordNode(y).getAttribute(ub.M)==ub.p;if(!la&&!h.aZ){var
xa=this;l.sf();h.aZ=window.setTimeout(function(){delete h[ub.ca];if(xa.getParent()!=null)xa.A3(l,y);},h.q7);}}else if(lb==ub.G){var
Hb=jsx3.EventHelp.getDragSource();var
hb=jsx3.EventHelp.getDragType();var
Ka=this.doEvent(ub.da,{objEVENT:l,strRECORDID:y,objSOURCE:Hb,strDRAGID:jsx3.EventHelp.getDragId(),strDRAGIDS:jsx3.EventHelp.getDragIds(),strDRAGTYPE:hb,objGUI:ob});if(Ka===false)return;var
J=this.getAbsolutePosition(m,ob);var
E=this.getAbsolutePosition(m,ob.childNodes[0]);var
T=this.yR(m);var
F=T.style;var
va=J.L;if(E.H/3>l.getOffsetY()){var
za=this.getAbsolutePosition(m,m);F.top=E.T-4+ub.ea;F.width=Math.max(0,za.W-va-8)+ub.ea;F.height=ub.fa;F.backgroundImage=ub.i+h.INSERT_BEFORE_IMG+ub.k;T.setAttribute(ub.V,ub.W);}else{va=va+26;F.width=ub.ga;F.height=ub.ga;F.top=E.T-10+E.H+ub.ea;F.backgroundImage=ub.i+h.APPEND_IMG+ub.k;T.setAttribute(ub.V,ub.ha);}F.left=va+ub.ea;T.setAttribute(ub.X,y);F.display=ub.Q;}}};d.yR=function(r){return this.getRendered(r).lastChild;};d.XF=function(o){var
Na=this.yR(o);Na.style.display=ub.R;Na.removeAttribute(ub.V);Na.removeAttribute(ub.X);};d.tF=function(m,f,o,e){this.removeSpyStyle(o);var
kb=this.doEvent(ub.ba,{objEVENT:m,strRECORDID:f});if(kb)this.showSpy(kb,m);};d.Qi=function(q,g){var
Fa=q.isFakeOut(g);var
hb=q.fromElement();if(hb==null)return;var
Ra=hb.getAttribute(ub.E);if(!Fa&&jsx3.EventHelp.isDragging()&&this.getCanDrop()==1){this.XF(q);window.clearTimeout(h.aZ);var
ca=jsx3.EventHelp.JSXID;var
xb=jsx3.EventHelp.DRAGTYPE;var
va=hb.parentNode.parentNode.getAttribute(ub.y);var
ja=hb.parentNode.parentNode;var
T=this.doEvent(ub.ia,{objEVENT:q,strRECORDID:va,objSOURCE:ca,strDRAGID:jsx3.EventHelp.getDragId(),strDRAGIDS:jsx3.EventHelp.getDragIds(),strDRAGTYPE:xb,objGUI:ja});}else if(Ra==ub.G&&this.hasEvent(ub.ba)){var
fa=q.toElement();if(!fa||fa.id!=ub.ja){jsx3.sleep(jsx3.gui.Interactive.hideSpy);this.removeSpyStyle(hb);if(h.DB)window.clearTimeout(h.DB);}}};d.oe=function(m,q){if(this.jsxsupermix(m,q))return;var
aa=m.keyCode();var
K=this.getXML();var
fb=m.srcElement().parentNode.parentNode;if(fb!=null&&fb.getAttribute(ub.y)!=null){var
A=fb.getAttribute(ub.y);if(aa>=37&&aa<=40){var
cb=this.getRecordNode(A);var
kb=cb.getAttribute(ub.L)==ub.p;if(kb||cb.selectSingleNode(ub.ka)!=null){var
ga=true;var
Ib=cb.getAttribute(ub.M)==ub.p;}else var
ga=false;if(aa==37){if(ga&&Ib){this.A3(m,A,false);}else this.sI(cb);}else if(aa==38){this.sI(cb);}else if(aa==39){if(kb&&!Ib){var
qb=this;this.A3(m,A,true,function(){qb.Pu(A);});}else if(ga&&!Ib){this.A3(m,A,true);}else this.uW(cb);}else if(aa==40)this.uW(cb);m.cancelAll();}else if(aa==9){if(m.shiftKey()){jsx3.html.focusPrevious(this.getRendered(q),m);}else jsx3.html.focusNext(this.getRendered(q),m);}else if(m.spaceKey()||m.enterKey()){var
la=this.Dp(A);if(la&&m.enterKey()){this.kp(m);}else this.D9(m,A,jsx3.gui.isMouseEventModKey(m)||m.shiftKey(),true);m.cancelAll();}}};d.sI=function(b){var
z=this.Yz(b.getAttribute(ub.y));if(z!=null)this.Pu(z);};d.uW=function(n){var
xa=this.cA(n.getAttribute(ub.y));if(xa!=null)this.Pu(xa);};d.Yz=function(r){var
ka=this.fy(r);if(ka!=null){var
jb=ka.previousSibling;if(jb!=null){while(jb.childNodes[1].style.display==ub.Q){var
Ma=jb.childNodes[1].lastChild;if(Ma==null)break;jb=Ma;}return jb.getAttribute(ub.y);}else return ka.parentNode.parentNode.getAttribute(ub.y);}return null;};d.cA=function(f){var
lb=this.fy(f);if(lb!=null){if(lb.childNodes[1].style.display==ub.Q){var
wa=lb.childNodes[1].firstChild;if(wa!=null)return wa.getAttribute(ub.y);}var
ga=lb.nextSibling;if(ga!=null){return ga.getAttribute(ub.y);}else{var
Hb=this.getId();var
Ab=lb.parentNode.parentNode;while(Ab!=null&&Ab.id&&Ab.id.indexOf(Hb)==0){if(Ab.nextSibling!=null)return Ab.nextSibling.getAttribute(ub.y);Ab=Ab.parentNode.parentNode;}}}return null;};d.executeRecord=function(l){var
Ab=null;Ab=this.isOldEventProtocol();this.kp(Ab,l);};d.kp=function(k,o){var
S=null;if(o==null)S=this.H4();else if(!(o instanceof Array))S=[o];else S=o;for(var
C=0;C<S.length;C++){var
R=S[C];if(R==null||!this.Pk(R))continue;var
w=this.getRecordNode(R);var
za=w.getAttribute(ub.la);if(za){var
qa={strRECORDID:R};qa.objRECORD=w;if(k instanceof jsx3.gui.Event)qa.objEVENT=k;this.eval(za,qa);}}if(k)this.doEvent(ub.la,{objEVENT:k,objRECORD:this.getRecordNode(S[0]),strRECORDIDS:S,strRECORDID:S[0]});};d.Oh=function(q,o){var
ab=null;var
Ha=q.srcElement();if(Ha!=null&&jsx3.util.strEmpty(Ha.getAttribute(ub.E)))Ha=Ha.parentNode;if(Ha!=null&&Ha.getAttribute(ub.E)!=null&&(Ha.getAttribute(ub.E)==ub.G||Ha.getAttribute(ub.E)==ub.H))ab=Ha.parentNode.parentNode.getAttribute(ub.y);if(ab)this.kp(q);};h.Tj={};h.Tj[ub.ma]=true;h.Tj[ub.na]=true;h.Tj[ub.oa]=true;h.Tj[ub.pa]=true;h.Tj[ub.T]=true;h.Tj[ub.qa]=true;h.Tj[ub.ra]=true;h.Tj[ub.sa]=true;h.Tj[ub.ta]=true;d.Rc=function(g,m,k){this.Gi(g,m,k,3);};d.Gc=function(r){this.applyDynamicProperties();if(this.getParent()&&(r==null||isNaN(r.parentwidth)||isNaN(r.parentheight))){r=this.getParent().uf(this);}else if(r==null)r={};r.boxtype=ub.ua;r.tagname=ub.va;if(r.left==null)r.left=0;if(r.top==null)r.top=0;if(r.width==null)r.width=ub.wa;if(r.height==null)r.height=ub.wa;var
Ab,Ta;if((Ab=this.getBorder())!=null&&Ab!=ub.q)r.border=Ab;if((Ta=this.getPadding())!=null&&Ta!=ub.q)r.padding=Ta;return new
jsx3.gui.Painted.Box(r);};d.paint=function(){this.applyDynamicProperties();var
qa=this.getId();var
sa=this.doTransform();if(!sa)sa=this.getNoDataMessage();sa=sa+(ub.xa+jsx3.gui.Block.SPACE+ub.ya+this.Gj(ub.ta,ub.za)+ub.Aa+this.Fo()+ub.Ba);var
P=ub.q;if(this.getEnabled()==1)P=this.jh(h.Tj,0);var
H=this.renderAttributes(null,true);var
_=this.Wl(true);_.setAttributes(this.bn()+P+ub.Ca+qa+ub.ya+this.on()+ub.Da+H);_.setStyles(this.Yh()+this.zf()+this.Of()+this.eh()+this.dk()+this.ad()+this.Bf()+this.Xe()+this.bd()+this.bi()+this.fh());return _.paint().join(sa+ub.Ea);};d.doTransform=function(b){var
pa={};var
ja=false;if(b==null){var
Ya=this.getXML().getRootNode();if(Ya){var
Q=Ya.getChildIterator();if(Q.hasNext())b=Q.next().getAttribute(ub.y);}}else ja=true;var
za=this.getUriResolver();var
wa=this.getIcon(),ga=this.getIconMinus(),U=this.getIconPlus();if(b!=null)pa.jsxrootid=b;pa.jsxtabindex=this.getIndex()==null?0:this.getIndex();pa.jsxselectedimage=h.SELECTEDIMAGE;pa.jsxbordercolor=h.BORDERCOLOR;pa.jsxicon=wa?za.resolveURI(wa):ub.q;pa.jsxiconminus=ga?za.resolveURI(ga):ub.q;pa.jsxiconplus=U?za.resolveURI(U):ub.q;pa.jsxtransparentimage=jsx3.gui.Block.SPACE;pa.jsxdragtype=ub.Fa;pa.jsxid=this.getId();pa.jsxuseroot=ja?1:this.getRoot();pa.jsxfragment=ja?1:0;pa.jsxpath=jsx3.getEnv(ub.Ga);pa.jsxpathapps=jsx3.getEnv(ub.Ha);pa.jsxpathprefix=this.getUriResolver().getUriPrefix();var
Lb=this.getXSLParams();for(var Jb in Lb)pa[Jb]=Lb[Jb];return this.Al(this.jsxsupermix(pa));};d.onXmlBinding=function(p){this.jsxsupermix(p);this.repaint();};d.getNoDataMessage=function(){return this.jsxnodata==null?h.DEFAULTNODATAMSG:this.jsxnodata;};d.onSetChild=function(k){return false;};h.getVersion=function(){return ub.Ia;};});jsx3.Tree=jsx3.gui.Tree;
