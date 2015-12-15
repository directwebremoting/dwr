/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.xml.Cacheable","jsx3.gui.Form","jsx3.gui.Block","jsx3.gui.Column");jsx3.Class.defineClass("jsx3.gui.List",jsx3.gui.Block,[jsx3.gui.Form,jsx3.xml.Cacheable,jsx3.xml.CDF],function(m,o){var
ub={Wb:"text",Qa:'style="',xb:"']",Cb:"A new row could not be appended to ",_:"JSXDragId",ea:"JSX_GENERIC",yb:"jsxbeforeappend",Pb:"relative",q:"//xsl:comment[.='JSXUNCONFIGURED']",ra:"//record[@jsxid='",V:/BACKGROUND/g,v:"none",rb:'" cellspacing="0" cellpadding="3" border="0" style="table-layout:fixed;',Ub:/&/g,U:"tempid=",u:": ",Nb:"style",ka:"cellIndex",z:"mouseup",d:"/jsx30list.xsl",La:' id="',D:"",fa:"jsxadopt",qb:'<table jsxid="',R:"jsxaftersort",w:"px",Ca:"jsxspy",Ab:"TD",kb:"height:",Lb:/(on(?:mousedown|click|focus|blur|mouseup|scroll|keydown|keypress))/gi,Q:"hidden",Na:';" class="jsx30list_colresize"></span>',Pa:'" class="jsx30list" ',la:"tr",qa:"1",Wa:'_jsxhead" class="jsx30list_headspan" style="',y:"visible",lb:"px;",x:"jsxbeforeresize",Za:'_jsxghost"',Hb:/&amp;/g,ja:"td",W:"bg",Fa:"mouseout",Ha:"keydown",Kb:"A row could not be updated, due to the following reasons(s): ",i:"jsx:///images/list/header.gif",Fb:"<",Sa:">",A:"Nk",t:" of ",Ia:"mousedown",s:"Error loading XSL for column #",X:/class=/g,Db:" because of an XML error: ",tb:"@",r:"JSXCONFIGURED",sa:"' and @",C:"_jsxhead",Ua:'<tr><td height="',l:"#c8cfd8",jb:";",L:" ",ia:"jsxmenu",Ka:"zn",sb:';">',_a:' class="jsx30list_ghost">&#160;</div>',eb:' style="',J:"100%",Da:"_curSpyRow",ta:"jsxselected",Rb:"/>",oa:"jsxexecute",O:"click",k:"#F3F2F4",gb:'" class="jsx30list_bodyspan">',Ga:"dblclick",p:"jsx30list_r1",P:"jsxbeforesort",I:"div",ha:"jsxdrop",a:"jsx:///xsl/",Aa:"jsxid",F:"jsxafterresize",Ra:'"',hb:"</table>",j:");",ua:"='1']",ib:"background-color:",Sb:/&nbsp;/g,S:"<span id='JSX' style='font-family:Verdana;font-size:10px;padding:0px;height:22px;width:200px;overflow:hidden;text-overflow:ellipsis;filter:progid:DXImageTransform.Microsoft.Gradient(GradientType=1, StartColorStr=#eedfdfe8, EndColorStr=#00ffffff);'><table style='font-family:verdana;font-size:10px;color:#a8a8a8;' cellpadding='3' cellspacing='0'>",Ma:'_jsxcolresize" style="background-color:',K:"solid 1px #ffffff;solid 1px #9898a5;solid 1px #9898a5;solid 1px #ffffff",bb:" onscroll=\"this.parentNode.parentNode.parentNode.childNodes[0].childNodes[0].childNodes[0].childNodes[0].style.left = -this.scrollLeft + 'px';\" ",aa:"normal",B:"-10px",g:")",Ib:"&",Gb:/&gt;/g,b:"ie",va:"jsxselect",M:"padding",ab:"</td></tr>",f:"jsx:///images/list/select.gif",Jb:"list.update.1",vb:"jsxgroupname",Eb:/&lt;/g,T:/id=/g,ga:"jsxctrldrop",ba:"No list with id ",mb:'<table cellspacing="0" cellpadding="3" border="0" style="position:absolute;left:0px;top:0px;table-layout:fixed;',e:"url(",Mb:"class",pb:"</tr></table>",za:"//record[@",h:"background-image:url(",c:"fx",H:"box",fb:";height:",wa:"jsxchange",ob:'" ',Ya:"</div>",G:"_jsxghost",Ja:"<span",ma:"_jsxbody",Xb:"3.0.00",ca:".",pa:"jsxunselectable",N:"Yk",zb:"TR",o:"#2050df",Z:"</table></span>",xa:"_jsxV6",Vb:"&amp;",ub:"jsxafteredit",Ta:'<table class="jsx30list_table" border="0" cellpadding="0" cellspacing="0" style="">',na:"tbody",Ba:"strRecordId",da:"bold",Bb:"jsxafterappend",Tb:"&#160;",nb:'<tr style="',Oa:'<div id="',wb:"//record[@jsxgroupname='",m:"ascending",Qb:/(<(?:img|input)[^>]*)(>)/gi,Ob:/(on(?:mousedown|click|focus|blur|mouseup|scroll|mouseup|keydown|keypress))/gi,db:'_jsxbody"',Y:"jsxc=",Ea:"mouseover",n:"descending",Xa:'">',Va:'" style="position:relative;overflow:hidden;">',ya:"_",cb:'" valign="top" style="position:relative;height:',E:"%"};var
ta=jsx3.gui.Event;var
eb=jsx3.gui.Interactive;var
vb=jsx3.xml.CDF;var
wb=jsx3.util.Logger.getLogger(m.jsxclass.getName());m.DEFAULTXSLURL=jsx3.resolveURI(ub.a+(jsx3.CLASS_LOADER.IE?ub.b:ub.c)+ub.d);m.SELECTBGIMAGE=ub.e+jsx3.resolveURI(ub.f)+ub.g;m.DEFAULTBACKGROUNDHEAD=ub.h+jsx3.resolveURI(ub.i)+ub.j;jsx3.html.loadImages(ub.f,ub.i);m.DEFAULTBACKGROUNDCOLOR=ub.k;m.DEFAULTBACKGROUNDCOLORHEAD=ub.l;m.SORTASCENDING=ub.m;m.SORTDESCENDING=ub.n;m.DEFAULTHEADERHEIGHT=20;m.MULTI=1;m.SINGLE=0;m.NOTSELECTABLE=2;m.RESIZEBARBGCOLOR=ub.o;m.DEFAULTROWCLASS=ub.p;o.init=function(a){this.jsxsuper(a);this._jsxFh=null;this._jsxV6=null;this._jsxS9=null;this._jsxrS=null;};o.onRemoveChild=function(a,g){this.jsxsuper(a,g);this.resetXslCacheData();this.repaint();};o.paintChild=function(i,s){if(!s){this.resetXslCacheData();this.repaint();}};o.qj=function(e){var
J=this.jsxsupermix(e);if(J.getSourceURL()==e)J=J.cloneDocument();this._configXSL(J);return J;};o.getXSL=function(){return this.qj(m.DEFAULTXSLURL);};o._configXSL=function(c){var
t=c.selectSingleNode(ub.q);if(t!=null){t.setValue(ub.r);var
ca=this.Ld();var
Z=ca.length;if(Z>0)for(var
qb=0;qb<=Z;qb++){var
aa=new
jsx3.xml.Document();var
rb=qb==Z?ca[Z-1].paintXSLString(true):ca[qb].paintXSLString();aa.loadXML(rb);if(aa.hasError()){wb.error(ub.s+qb+ub.t+this+ub.u+aa.getError());}else t.getParent().insertBefore(aa.getRootNode(),t);}}};o.clearXSL=function(){this.resetXslCacheData();return this;};m.WU=function(g){return g&&g.getDisplay()!=ub.v;};o.Ld=function(){return this.getChildren().filter(m.WU);};o.doValidate=function(){var
C=this.getSelectedNodes().size()>0||this.getRequired()==0;this.setValidationState(C?1:0);return this.getValidationState();};o.getResizable=function(){return this.jsxresize==null?1:this.jsxresize;};o.setResizable=function(f){this.jsxresize=f;return this;};o.zn=function(n,r){if(!n.leftButton())return;ta.publish(n);if(this.getCanResize()!=0){var
Bb=r;var
Sa=this.getRendered().childNodes[1];var
Q=parseInt(Bb.parentNode.parentNode.parentNode.parentNode.parentNode.style.left);var
wa=Bb.parentNode.parentNode.offsetLeft+Q;Sa.style.left=wa+ub.w;this._jsxS9=wa;this._jsxrS=Bb.parentNode.parentNode.cellIndex;var
C=this._jsxrS-1;var
qb=this.doEvent(ub.x,{objEVENT:n,intINDEX:C,intCOLUMNINDEX:C})===false;if(!qb){Sa.style.visibility=ub.y;jsx3.gui.Event.subscribe(ub.z,this,ub.A);jsx3.gui.Interactive.Un(n,Sa,false,true);}else Sa.style.left=ub.B;}n.cancelReturn();n.cancelBubble();};o.Nk=function(e,h){var
e=e.event;jsx3.gui.Event.unsubscribe(ub.z,this,ub.A);if(h==null)h=this.getRendered().childNodes[1];e.releaseCapture(h);var
Ka=parseInt(h.style.left);var
Sa=Ka-this._jsxS9;var
na=this._jsxrS-1;var
S=this.Ld();var
ea=this.getDocument();var
qb=ea.getElementById(this.getId()+ub.C);var
Ib=qb.childNodes[0].childNodes[0].childNodes[0].childNodes[na];var
kb=Ib.offsetWidth;var
qa=S[na].getWidth()+ub.D;var
jb;if(jb=qa.indexOf(ub.E)>-1){var
ua=this.getAbsolutePosition().W;var
Db=kb+Sa;Db=parseInt(Db/ua*1000)/10;if(Db<2)Db=2;Db=Db+ub.E;}else{var
Db=kb+Sa;if(Db<4)Db=4;}h.style.left=ub.B;var
ib={objEVENT:e,intDIFF:Sa,intINDEX:na,intOLDWIDTH:kb,vntWIDTH:Db,intCOLUMNINDEX:na};var
H=this.doEvent(ub.F,ib);if(!(H===false)){var
M=H instanceof Object&&H.vntWIDTH?H.vntWIDTH:Db;S[na].setWidth(M);this.resetXslCacheData();this.repaintBody();this.repaintHead();}};o.uh=function(b,k){if(!b.leftButton())return;ta.publish(b);var
V=this.getDocument().getElementById(this.getId()+ub.G);V.innerHTML=ub.D;var
v=parseInt(k.parentNode.parentNode.parentNode.style.left);var
P={};P.boxtype=ub.H;P.tagname=ub.I;P.left=k.offsetLeft+v;P.top=0;P.parentheight=k.offsetHeight;P.parentwidth=parseInt(k.offsetWidth);P.width=ub.J;P.height=ub.J;P.border=ub.K;P.padding=parseInt(k.childNodes[0].offsetTop)+ub.L+(k.style.paddingRight?parseInt(k.style.paddingRight):0)+ub.L+(k.style.paddingBottom?parseInt(k.style.paddingBottom):0)+ub.L+(k.style.paddingLeft?parseInt(k.style.paddingLeft):0);var
I=new
jsx3.gui.Painted.Box(P);V.style.left=I.bc()+ub.w;V.style.top=I.mh()+ub.w;V.style.width=I.cm()+ub.w;V.style.height=I.Zm()+ub.w;V.style.fontName=k.style.fontName;V.style.fontSize=k.style.fontSize;V.style.textAlign=k.style.textAlign;V.style.fontWeight=k.style.fontWeight;V.style.visibility=ub.y;jsx3.gui.Painted.Si(V,I.hm(),ub.M);V.innerHTML=jsx3.html.getOuterHTML(k.childNodes[0]);this._jsxS9=k.offsetLeft+v;this._jsxrS=k.cellIndex;jsx3.gui.Event.subscribe(ub.z,this,ub.N);jsx3.gui.Interactive.Un(b,V,false,true);};o.dc=function(q,c){var
Ta=q.getType()==ub.O?c.cellIndex:this._jsxrS;var
Mb=this.Ld()[Ta];var
_=this.getChildren().indexOf(Mb);if(this.getCanSort()!=0&&Mb!=null&&Mb.getCanSort()!=0){var
sb={objEVENT:q,intCOLUMNINDEX:_};var
Db=this.doEvent(ub.P,sb);if(Db!==false){var
U=Db instanceof Object&&Db.intCOLUMNINDEX!=null?Db.intCOLUMNINDEX:_;this.RB(q,U);}}};o.Yk=function(i,d){var
i=i.event;jsx3.gui.Event.unsubscribe(ub.z,this,ub.N);var
J=this.getDocument().getElementById(this.getId()+ub.G);if(d==null)var
d=J;i.releaseCapture(J);J.style.visibility=ub.Q;if(this._jsxS9==parseInt(J.style.left)){this.dc(i,d);}else if(this.getCanReorder()!=0){var
pb=parseInt(J.style.left);var
yb=this.Ld();var
ia=this.getChildren().length;var
va=yb[0].getRendered().parentNode;var
Ma=this.getChildren().indexOf(yb[this._jsxrS]);var
A=0;for(var
Jb=0;Jb<ia;Jb++)if(m.WU(this.getChild(Jb))){var
ra=va.childNodes[A].offsetLeft;if(pb<ra){if(Ma!=Jb)this.ye(Ma,Jb-1);jsx3.EventHelp.reset();return;}A++;}if(Ma!=ia-1)this.ye(Ma,ia-1);}};o.setSortColumn=function(n){this.RB(this.isOldEventProtocol(),n);};o.RB=function(i,g){this.jsxsortcolumn=g;this.doSort();if(i)this.doEvent(ub.R,{objEVENT:i instanceof jsx3.gui.Event?i:null,intCOLUMNINDEX:g});return this;};o.getSortColumn=function(){return this.jsxsortcolumn;};o.Sh=function(){var
Aa=this.jsxsortcolumn!=null?this.getChild(this.jsxsortcolumn)==null?null:this.getChild(this.jsxsortcolumn).getSortPath():this.getSortPath();return Aa?Aa.substring(1):ub.D;};o.ye=function(i,h){var
da=this.getChildren();var
A=da.length;var
ya=this.getChild(i);if(h<i){for(var
ua=i;ua>h;ua--)if(ua>0)da[ua]=da[ua-1];da[h+1]=ya;}else{for(var
ua=i;ua<=h;ua++)if(ua<=A-2)da[ua]=da[ua+1];da[h]=ya;}this.resetXslCacheData();this.repaint();this.getServer().getDOM().onChange(0,this.getId(),this.getChild(0).getId());};o.doSort=function(k){if(k!=null){this.setSortDirection(k);}else this.setSortDirection(this.getSortDirection()==ub.m?ub.n:ub.m);this.repaintBody();this.repaintHead();};o.getSortPath=function(){return this.jsxsortpath==null?ub.D:this.jsxsortpath;};o.setSortPath=function(n){this.jsxsortpath=n;return this;};o.getSortDirection=function(){return this.jsxsortdirection==null?ub.m:this.jsxsortdirection;};o.setSortDirection=function(i){this.jsxsortdirection=i;return this;};o.getMultiSelect=function(){return this.jsxmultiselect==null?1:this.jsxmultiselect;};o.setMultiSelect=function(b){this.jsxmultiselect=b;if(b==2)this.deselectAllRecords();return this;};o.getCanReorder=function(){return this.jsxreorder==null?1:this.jsxreorder;};o.setCanReorder=function(g){this.jsxreorder=g;return this;};o.getCanSort=function(){return this.jsxsort==null?1:this.jsxsort;};o.setCanSort=function(h){this.jsxsort=h;return this;};o.getBackgroundColorHead=function(){return this.jsxbgcolorhead;};o.setBackgroundColorHead=function(b){this.jsxbgcolorhead=b;return this;};o.getBackgroundHead=function(){return this.jsxbghead;};o.setBackgroundHead=function(p){this.jsxbghead=p;return this;};o.getHeaderHeight=function(){return this.jsxheaderheight;};o.setHeaderHeight=function(a){this.jsxheaderheight=a;return this;};m.AF=function(p,j,q,e){return ub.S+jsx3.html.getOuterHTML(p).replace(ub.T,ub.U).replace(ub.V,ub.W).replace(ub.X,ub.Y)+ub.Z;};m.doBlurItem=function(h){var
Fa=h.getAttribute(ub._);var
Ha=h.id.substring(0,h.id.length-(Fa.length+1));var
ga=jsx3.GO(Ha);h.style.fontWeight=ub.aa;if(ga!=null)ga._jsxFh=null;};m.doFocusItem=function(q){var
Ib=q.getAttribute(ub._);var
t=q.id.substring(0,q.id.length-(Ib.length+1));var
Za=jsx3.GO(t);if(Za==null){wb.warn(ub.ba+t+ub.ca);return;}if(!Za.Pk(Ib))return;q.focus();q.style.fontWeight=ub.da;Za._jsxFh=q;};o.Dc=function(k,b){if(this.getCanDrag()==1&&k.leftButton()){var
Ib=k.srcElement();var
Aa=this.OI(Ib);if(Aa[0]!=null){if(!this.Pk(Aa[0]))return;ta.publish(k);this.doDrag(k,Aa[2],m.AF,{strRECORDID:Aa[0],intCOLUMNINDEX:Aa[1]});}}};o.Df=function(q,k){var
bb=this.OI(q.srcElement());if(this.getCanDrop()==1&&jsx3.EventHelp.isDragging()&&jsx3.EventHelp.JSXID!=this){if(jsx3.EventHelp.DRAGTYPE==ub.ea){var
Wa=jsx3.EventHelp.JSXID.getId();var
F=jsx3.EventHelp.DRAGID;var
W=jsx3.GO(Wa);if(W==null)return;var
Z=jsx3.gui.isMouseEventModKey(q);var
rb=W.doEvent(ub.fa,{objEVENT:q,strRECORDID:F,strRECORDIDS:[F],objTARGET:this,bCONTROL:Z});var
J={objEVENT:q,objSOURCE:W,strDRAGID:F,strDRAGTYPE:jsx3.EventHelp.DRAGTYPE,strRECORDID:bb[0],intCOLUMNINDEX:bb[1],bALLOWADOPT:rb!==false};var
Ga=this.doEvent(Z?ub.ga:ub.ha,J);if(rb!==false&&Ga!==false&&W.instanceOf(jsx3.xml.CDF))this.adoptRecord(W,F);}}else if(q.rightButton()){var
U;if((U=this.getMenu())!=null){var
la=this.getServer().getJSX(U);if(la!=null){var
Cb={objEVENT:q,objMENU:la,strRECORDID:bb[0],intCOLUMNINDEX:bb[1]};var
u=this.doEvent(ub.ia,Cb);if(u!==false){if(u instanceof Object&&u.objMENU instanceof jsx3.gui.Menu)la=u.objMENU;la.showContextMenu(q,this,bb[0]);}}}}};o.OI=function(l){var
Eb=null;var
fb=null;var
qb=null;while(l!=null&&Eb==null){if(l.tagName&&l.tagName.toLowerCase()==ub.ja)fb=l.getAttribute(ub.ka);else if(l.tagName&&l.tagName.toLowerCase()==ub.la){Eb=l.getAttribute(ub._);qb=l;}l=l.parentNode;}return [Eb,Eb?fb:null,qb];};o.nM=function(e,d,p){if(!this.Pk(d.getAttribute(ub._)))return;if(!this.Pk(p.getAttribute(ub._)))return;if(this.getMultiSelect()==2)return;var
yb=0;var
T=this.getDocument().getElementById(this.getId()+ub.ma).childNodes[0].childNodes[0];var
pb=[],na=[];for(var
ab=T.childNodes.length-1;ab>=0;ab--){if(T.childNodes[ab]==p)yb++;if(T.childNodes[ab]==d)yb++;if(yb>=1&&yb<=2){var
ba=T.childNodes[ab];pb.push(ba.getAttribute(ub._),ba);}if(yb==2)break;}this.Iu(e,pb,na,false);m.doFocusItem(d);};o.oe=function(b,k){if(this.jsxsupermix(b,k))return;if(this._jsxFh==null)return;var
zb=this.getId().length;var
Ra=b.keyCode();var
pa=this.getMultiSelect()==1;var
B=false;var
La=jsx3.gui.isMouseEventModKey(b);if(Ra==38){if(this._jsxFh==this._jsxFh.parentNode.firstChild)return;if(pa&&La){m.doFocusItem(this._jsxFh.previousSibling);}else if(pa&&b.shiftKey()){this.nM(b,this._jsxFh.previousSibling,this._jsxV6);}else{var
Ka=this._jsxFh.previousSibling;this.LX(b,Ka.getAttribute(ub._),Ka,false);}B=true;}else if(Ra==40){if(this._jsxFh==this._jsxFh.parentNode.lastChild)return;if(pa&&La){m.doFocusItem(this._jsxFh.nextSibling);}else if(pa&&b.shiftKey()){this.nM(b,this._jsxFh.nextSibling,this._jsxV6);}else{var
Ka=this._jsxFh.nextSibling;this.LX(b,Ka.getAttribute(ub._),Ka,false);}B=true;}else if(Ra==13){this.u3(b);B=true;}else if(Ra==32){if(pa&&La){var
Fa=this._jsxFh.getAttribute(ub._);if(this.isSelected(Fa)){this.nz(b,Fa,this._jsxFh);}else this.LX(b,Fa,this._jsxFh,true);}else if(pa&&b.shiftKey()){this.nM(b,b.srcElement(),this._jsxV6);}else{var
Ka=this._jsxFh;this.LX(b,Ka.getAttribute(ub._),Ka,false);}B=true;}else if(Ra==9&&b.shiftKey()){this.focus();B=true;}else if(Ra==9){this.getRendered().lastChild.focus();B=true;}if(B)b.cancelAll();};o.Tg=function(p,i){var
ua=false;var
Ma=p.srcElement();if(Ma.tagName&&Ma.tagName.toLowerCase()==ub.na||Ma==i){this.WV(p,this.getSelectedIds(),[]);return;}var
xb=this.getRendered();while(jsx3.util.strEmpty(Ma.getAttribute(ub._))&&Ma!=xb)Ma=Ma.parentNode;if(!Ma||!Ma.getAttribute(ub._)){this.WV(p,this.getSelectedIds(),[]);return;}var
Cb=this.getMultiSelect()==1;var
ib=jsx3.gui.isMouseEventModKey(p);if(Cb&&p.shiftKey()&&this._jsxV6!=null){this.nM(p,Ma,this._jsxV6);ua=true;}else if(Cb&&ib){var
cb=Ma.getAttribute(ub._);if(this.isSelected(cb)){this.nz(p,cb,Ma);}else{this.LX(p,cb,Ma,true);m.doFocusItem(Ma);}ua=true;}else{if(this.isSelected(Ma.getAttribute(ub._))){if(ib||p.shiftKey())this.nz(p,Ma.getAttribute(ub._),Ma);}else this.LX(p,Ma.getAttribute(ub._),Ma,false);ua=true;}if(ua){p.cancelBubble();p.cancelReturn();}};o.Oh=function(p,i){this.u3(p);};o.executeRecord=function(h){var
Q=this.getRecordNode(h);if(Q!=null)this.eval(Q.getAttribute(ub.oa),{strRECORDID:h});};o.doExecute=function(j){this.u3(this.isOldEventProtocol(),j!=null?[j]:null);};o.u3=function(k,c){if(c==null)c=this.getSelectedIds();for(var
D=0;D<c.length;D++){var
sb=c[D];var
K=this.getRecordNode(sb);if(K.getAttribute(ub.pa)==ub.qa)continue;this.eval(K.getAttribute(ub.oa),{strRECORDID:sb});}if(c.length>0&&k)this.doEvent(ub.oa,{objEVENT:k instanceof ta?k:null,strRECORDID:c[0],strRECORDIDS:c});};o.isSelected=function(b){return this.getXML().selectSingleNode(ub.ra+b+ub.sa+ub.ta+ub.ua)!=null;};o.doSelect=function(l,n,i,h){this.LX(!i&&this.isOldEventProtocol(),l,null,true);if(l&&h)this.revealRecord(l);return this;};o.selectRecord=function(f){if(!this.Pk(f))return;if(this.getMultiSelect()==2)return;this.LX(false,f,null,true);return this;};o.deselectRecord=function(g){this.nz(false,g,null);return this;};o.deselectAllRecords=function(){this.WV(false,this.getSelectedIds(),[]);return this;};o.LX=function(h,r,q,i){var
Qa=this.getRecordNode(r);if(!Qa||Qa.getAttribute(ub.ta)==ub.qa||Qa.getAttribute(ub.pa)==ub.qa||this.getMultiSelect()==2)return false;var
Za=i&&this.getMultiSelect()==1;if(!Za)this.deselectAllRecords();Qa.setAttribute(ub.ta,ub.qa);q=q||this.K7(r);if(q!=null){if(!Za){this._jsxV6=q;m.doFocusItem(q);}q.style.backgroundImage=m.SELECTBGIMAGE;}if(h){this.doEvent(ub.va,{objEVENT:h instanceof ta?h:null,strRECORDID:r,strRECORDIDS:[r]});this.doEvent(ub.wa,{objEVENT:h instanceof ta?h:null});}return true;};o.Iu=function(j,d,f,k){if(!k)this.deselectAllRecords();for(var
la=0;la<d.length;la++){var
Z=this.LX(false,d[la],f[la],true);if(!Z){d.splice(la,1);f.splice(la,1);la--;}}if(j&&d.length>0){this.doEvent(ub.va,{objEVENT:j,strRECORDID:d[0],strRECORDIDS:d});this.doEvent(ub.wa,{objEVENT:j});}};o.nz=function(s,g,i){var
Ja=this.getRecordNode(g);if(!Ja||Ja.getAttribute(ub.ta)!=ub.qa)return false;Ja.removeAttribute(ub.ta);i=i||this.K7(g);if(i!=null){if(this._jsxV6==i){delete this[ub.xa];m.doBlurItem(i);}i.style.backgroundImage=ub.D;}if(s){this.doEvent(ub.va,{objEVENT:s instanceof ta?s:null,strRECORDID:null,strRECORDIDS:[]});this.doEvent(ub.wa,{objEVENT:s instanceof ta?s:null});}return true;};o.WV=function(c,k,n){for(var
Ib=0;Ib<k.length;Ib++){var
T=this.nz(false,k[Ib],n[Ib]);if(!T){k.splice(Ib,1);n.splice(Ib,1);Ib--;}}if(c&&k.length>0){this.doEvent(ub.va,{objEVENT:c,strRECORDID:k[0],strRECORDIDS:k});this.doEvent(ub.wa,{objEVENT:c});}};o.focusRecord=function(d){var
yb=this.K7(d);if(yb!=null)yb.focus();return this;};o.doDeselect=function(r,h){this.nz(this.isOldEventProtocol(),r,null);return this;};o.getActiveRow=function(){return this._jsxV6;};o.revealRecord=function(g,h){var
Kb=this.K7(g);if(Kb){var
ra=h?h.getRendered():this.getRendered();if(ra)jsx3.html.scrollIntoView(Kb,ra,0,10);}};o.K7=function(e){var
x=this.getDocument();return x!=null?x.getElementById(this.getId()+ub.ya+e):null;};o.redrawRecord=function(j,b){if(b==1){this.appendRow(this.getRecord(j),j);}else if(j!=null&&b==0){var
Wa;if((Wa=this.K7(j))!=null)jsx3.html.removeNode(Wa);}else if(j!=null&&b==2)this.updateRow(j);};o.getSelectedNodes=function(){return this.getXML().selectNodes(ub.za+ub.ta+ub.ua);};o.getSelectedIds=function(){return this.getSelectedNodes().map(function(a){return a.getAttribute(ub.Aa);}).toArray(true);};o.getValue=function(){var
U=this.getSelectedIds();return this.getMultiSelect()==1?U:U[0];};o.setValue=function(a){if(a instanceof Array){if(this.getMultiSelect()!=1)throw new
jsx3.IllegalArgumentException(ub.Ba,a);}else a=a!=null?[a]:[];this.Iu(false,a,[],false);return this;};o.el=function(p,i){if(this.getCanSpy()==1&&this.getEvent(ub.Ca)){var
kb=this.OI(p.srcElement());if(kb[0]){m._curSpyRow=kb[2];this.applySpyStyle(kb[2]);var
Y=p.clientX()+jsx3.EventHelp.DEFAULTSPYLEFTOFFSET;var
E=p.clientY()+jsx3.EventHelp.DEFAULTSPYTOPOFFSET;var
ia=this;p.sf();m.fY=window.setTimeout(function(){if(!this.getParent())return;var
Ca={objEVENT:p,strRECORDID:kb[0],intCOLUMNINDEX:kb[1]};var
fa=ia.doEvent(ub.Ca,Ca);if(fa){jsx3.gui.Interactive.hideSpy();ia.showSpy(fa,p);}},jsx3.EventHelp.SPYDELAY);}}};o.Qi=function(){if(m._curSpyRow){this.removeSpyStyle(m._curSpyRow);delete m[ub.Da];}window.clearTimeout(m.fY);jsx3.gui.Interactive.hideSpy();};m.Tj={};m.Tj[ub.Ea]=true;m.Tj[ub.Fa]=true;m.Tj[ub.O]=true;m.Tj[ub.Ga]=true;m.Tj[ub.Ha]=true;m.Tj[ub.Ia]=true;m.Tj[ub.z]=true;o.Rc=function(c,s,g){var
Ea=this.getDocument();if(Ea!=null){var
Bb=Ea.getElementById(this.getId()+ub.ma);if(Bb!=null){var
Fb=this.getHeaderHeight()!=null?this.getHeaderHeight():m.DEFAULTHEADERHEIGHT;Bb.style.height=Math.max(0,c.parentheight-Fb)+ub.w;}}};o.paint=function(){this.applyDynamicProperties();this._jsxV6=null;var
va=this.getParent().uf(this);var
D=this.getId();var
oa=ub.D;var
Ga=ub.Ja+this.Gj(ub.Ia,ub.Ka,1)+ub.La+D+ub.Ma+m.RESIZEBARBGCOLOR+ub.Na;var
Ra=this.getHeaderHeight()!=null?this.getHeaderHeight():m.DEFAULTHEADERHEIGHT;var
ea=ub.Oa+D+ub.Pa+ub.Qa+ub.D+this.bi()+this.bd()+this.dk()+this.zf()+this.Of()+ub.Ra+this.on()+this.renderAttributes()+ub.Sa;ea=ea+ub.Ta;if(Ra>0){ea=ea+(ub.Ua+Ra+ub.Va);ea=ea+(ub.Oa+D+ub.Wa+this.Zi()+this._e()+ub.Xa);ea=ea+this.gh();ea=ea+ub.Ya;ea=ea+(ub.Oa+D+ub.Za+ub._a);ea=ea+ub.ab;oa=ub.bb;}var
qa=this.jh(m.Tj,5);var
aa=va.parentheight-Ra;ea=ea+(ub.Ua+(Ra==0?ub.J:aa)+ub.cb+aa+ub.w+ub.Xa);ea=ea+(ub.Oa+D+ub.db+qa+oa+ub.eb+this.zf()+this.getBorder()+ub.fb+aa+ub.w+ub.gb);ea=ea+this.Wm();ea=ea+this.bk();ea=ea+ub.Ya;ea=ea+ub.ab;ea=ea+ub.hb;ea=ea+Ga;ea=ea+ub.Ya;return ea;};o.zf=function(){return ub.ib+(this.getBackgroundColor()?this.getBackgroundColor():m.DEFAULTBACKGROUNDCOLOR)+ub.jb;};o.Of=function(){return this.getBackground()?this.getBackground()+ub.jb:ub.D;};o.Zi=function(){return ub.ib+(this.getBackgroundColorHead()?this.getBackgroundColorHead():m.DEFAULTBACKGROUNDCOLORHEAD)+ub.jb;};o._e=function(){return this.getBackgroundHead()?this.getBackgroundHead()+ub.jb:m.DEFAULTBACKGROUNDHEAD;};o.to=function(){return this.getClassName()?this.getClassName():m.DEFAULTROWCLASS;};o.zl=function(){return ub.kb+(this.getHeaderHeight()?this.getHeaderHeight():m.DEFAULTHEADERHEIGHT)+ub.lb;};o.bk=function(){return ub.D;};o.gh=function(){var
_=ub.mb+this.zl()+ub.Xa;_=_+(ub.nb+this.zl()+ub.ob+this.bn()+ub.Sa);var
Na=this.Ld();var
xb=this.getChildren();var
sa=xb.length;var
Ra=0;for(var
Fb=0;Ra<=Na.length;Fb++)if(xb[Fb]==null||m.WU(xb[Fb])){if(Ra==Na.length){if(Na.length>0)_=_+Na[Na.length-1].paint(true);}else if(Fb<xb.length){var
Oa=Fb==this.getSortColumn()?this.getSortDirection():null;_=_+xb[Fb].paint(false,Oa);}Ra++;}_=_+ub.pb;return _;};o.Wm=function(){var
va=ub.qb+this.getId()+ub.rb+this.Of()+this.Yh()+this.ad()+this.Bf()+ub.sb;va=va+this.doTransform();va=va+ub.hb;return va;};o.repaintBody=function(){var
jb=this.getDocument();if(jb!=null){var
oa=jb.getElementById(this.getId()+ub.ma);if(oa!=null)jsx3.html.setOuterHTML(oa.childNodes[0],this.Wm());}};o.repaintHead=function(){var
Ca=this.getDocument();if(Ca!=null){var
F=Ca.getElementById(this.getId()+ub.C);if(F!=null){jsx3.html.setOuterHTML(F.childNodes[0],this.gh());this.scrollHead();}}};o.scrollHead=function(){var
nb=this.getDocument();var
C=nb.getElementById(this.getId()+ub.ma);if(C&&C.scrollLeft!=0){var
fa=nb.getElementById(this.getId()+ub.C);if(fa!=null)fa.childNodes[0].style.left=-C.scrollLeft+ub.w;}};o.doTransform=function(q){var
Wa={};if(q)Wa.jsxrowid=q;Wa.jsxtabindex=isNaN(this.getIndex())?0:this.getIndex();Wa.jsxselectionbg=m.SELECTBGIMAGE;Wa.jsxtransparentimage=jsx3.gui.Block.SPACE;Wa.jsxid=this.getId();Wa.jsxsortpath=this.Sh();Wa.jsxsortdirection=this.getSortDirection();Wa.jsxrowclass=this.to();Wa.jsxsorttype=this.getSortType();var
L=this.getXSLParams();for(var Y in L)Wa[Y]=L[Y];var
G=this.jsxsupermix(Wa);G=this.Al(G);return G;};m.onDelete=function(c,g){var
qb=jsx3.html.getJSXParent(g);if(qb instanceof m)qb.deleteRecord(c);};m.onCheck=function(n,q,j,i,r){var
H=jsx3.gui.Event.getCurrent();if(q.substring(0,1)==ub.tb)q=q.substring(1);var
ua=jsx3.html.getJSXParent(j);if(i)j.checked=!j.checked;var
fa=j.checked;var
u=fa?r[0]:r[1];ua.insertRecordProperty(n,q,u,false);ua.doEvent(ub.ub,{objEVENT:H,strATTRIBUTENAME:q,strATTRIBUTEVALUE:u,strRECORDID:n,objGUI:j,objMASK:null});if(H)H.cancelReturn();};m.onRadio=function(k,b,n,p,c){if(b.substring(0,1)==ub.tb)b=b.substring(1);if(p)n.checked=true;m.onCheck(k,b,n,false,c);if(p){var
x=jsx3.html.getJSXParent(n);var
Ma=x.getRecordNode(k);var
N;if(Ma!=null&&(N=Ma.getAttribute(ub.vb))!=null){var
ja=x.getXML().selectNodes(ub.wb+N+ub.xb);for(var
u=0;u<ja.getLength();u++){var
wa=ja.getItem(u);if(wa.getAttribute(ub.Aa)!=k)ja.getItem(u).setAttribute(b,c[1]);}}}};o.appendRow=function(f,q){var
Db=this.getDocument();if(Db!=null){var
ba=this.getId();var
z=Db.getElementById(ba+ub.ma).childNodes[0].childNodes[0];var
tb=true;if(q==null){q=jsx3.xml.CDF.getKey();tb=false;}if(f==null)f={jsxid:q};this.doEvent(ub.yb,{objMASTERRECORD:f});if(tb==false)this.insertRecord(f,null,false);var
x=m.yJ(this.doTransform(q));if(x!=ub.D){var
Ra=new
jsx3.xml.Document();Ra.loadXML(x);if(!Ra.hasError()){var
Ma=Db.createElement(ub.zb);var
ea=Ra.getRootNode();m.UI(ea,Ma);var
Gb=ea.selectNodes(ub.ja);for(var
M=0;M<Gb.getLength();M++){ea=Gb.getItem(M);var
Ab=Db.createElement(ub.Ab);m.UI(ea,Ab);Ma.appendChild(Ab);var
ra=ea.getChildNodes(true);var
ib=ub.D;for(var
Pa=0;Pa<ra.getLength();Pa++)ib=ib+ra.getItem(Pa).toString();Ab.innerHTML=ib;}z.appendChild(Ma);this.doEvent(ub.Bb,{objMASTERRECORD:f,objTR:Ma});}else wb.warn(ub.Cb+this+ub.Db+Ra.getError());}}};o.updateRow=function(j){var
va;if(this.getRecordNode(j)!=null&&(va=this.K7(j))!=null){var
L=m.yJ(this.doTransform(j));if(L!=ub.D){var
Ea=new
jsx3.xml.Document();Ea.loadXML(L);if(!Ea.hasError()){var
M=Ea.getRootNode();m.UI(M,va);var
Ha=M.selectNodes(ub.ja);for(var
u=0;u<Ha.getLength();u++){M=Ha.getItem(u);var
db=va.childNodes.item(u);m.UI(M,db);var
lb=M.getChildNodes(true);var
gb=ub.D;for(var
rb=0;rb<lb.getLength();rb++)gb=gb+lb.getItem(rb).toString().replace(ub.Eb,ub.Fb).replace(ub.Gb,ub.Sa).replace(ub.Hb,ub.Ib);db.innerHTML=gb;}}else jsx3.util.Logger.doLog(ub.Jb,ub.Kb+Ea.getError(),3,false);}}};m.UI=function(q,a){var
y=q.getAttributes();for(var
Bb=0;Bb<y.getLength();Bb++){var
mb=y.getItem(Bb);var
da=mb.getNodeName();var
ea=ub.Lb;var
qb=mb.getValue();if(da.match(ea)){a[da.toLowerCase()]=new
Function(qb);}else if(da==ub.Mb){a.className=qb;}else if(da==ub.Nb){jsx3.gui.Painted.Si(a,qb);}else a.setAttribute(da,qb);}};m.jr=function(e,n){var
J=e.getAttributes();for(var
va=0;va<J.getLength();va++){var
fb=J.getItem(va);var
ra=fb.getNodeName();var
E=ub.Ob;var
da=fb.getValue();if(ra.match(E)){n[ra]=new
Function(da);}else if(ra!=ub.Mb){n.setAttribute(ra,da);}else n.className=da;}if(!n.tagName||n.tagName.toLowerCase()!=ub.la)n.style.position=ub.Pb;};m.yJ=function(s){var
O=ub.Qb;s=s.replace(O,function(a,f,r){return f+ub.Rb;});s=s.replace(ub.Sb,ub.Tb).replace(ub.Ub,ub.Vb);return s;};o.getGrowBy=function(){return this.jsxgrowby;};o.setGrowBy=function(a){this.jsxgrowby=a;return this;};o.getAutoExpand=function(){return jsx3.Boolean.valueOf(this.getGrowBy());};o.setAutoExpand=function(l){return this.setGrowBy(l?1:0);};o.getSortType=function(){if(this.jsxsorttype==null){if(this.jsxsortcolumn!=null)return this.getChild(this.jsxsortcolumn)==null?ub.Wb:this.getChild(this.jsxsortcolumn).getDataType();var
Ha=this.getSortPath();for(var
C=this.getChildren().length-1;C>=0;C--){var
E=this.getChild(C);if(E instanceof jsx3.gui.Column&&E.getSortPath()==Ha)return this.getChild(C).getDataType();}return ub.Wb;}else return this.jsxsorttype;};o.setSortType=function(a){this.jsxsorttype=a;return this;};o.getMaskProperties=function(){return jsx3.gui.Block.MASK_NO_EDIT;};o.getWrap=function(){return this.jsxwrap==null?1:this.jsxwrap;};o.setWrap=function(a){this.resetXslCacheData();this.jsxwrap=a;return this;};o.onSetChild=function(n){return n instanceof jsx3.gui.Column;};o.Pk=function(d){var
t=this.getRecord(d);return t!=null&&(t[ub.pa]==null||t[ub.pa]!=ub.qa);};m.getVersion=function(){return ub.Xb;};});jsx3.gui.List.prototype.getResizeable=jsx3.gui.List.prototype.getResizable;jsx3.gui.List.prototype.setResizeable=jsx3.gui.List.prototype.setResizable;jsx3.gui.List.prototype.getCanResize=jsx3.gui.List.prototype.getResizable;jsx3.gui.List.prototype.setCanResize=jsx3.gui.List.prototype.setResizable;jsx3.gui.List.prototype.doClearSelections=jsx3.gui.List.prototype.deselectAllRecords;jsx3.gui.List.prototype.deselectRecords=jsx3.gui.List.prototype.deselectAllRecords;jsx3.List=jsx3.gui.List;
