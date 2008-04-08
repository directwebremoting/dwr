/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.xml.Cacheable","jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Table",jsx3.gui.Block,[jsx3.gui.Form,jsx3.xml.Cacheable,jsx3.xml.CDF],function(h,a){var
ub={Qa:"xsl",_:' id="',ea:"nE",q:"jsxid",ra:'<div jsxindex="',V:"mousedown",v:"jsxunselectable",U:"mouseup",u:"='1']",ka:'<div class="jsx30table_head_pane ',z:"",d:"jsx:///images/table/select.gif",La:";{$myselectionbg}{$jsxcellstyle}",D:"]",fa:">",R:"dblclick",w:"1",Ca:"white-space:nowrap;",Q:"click",Na:'xmlns:xsl="http://www.w3.org/1999/XSL/Transform"',Pa:"'",la:' style="',qa:"jsxtext",y:")",x:"url(",ja:'px;">',W:"syncheadtobody",Fa:"The column profile document has errors. A new, empty CDF Document will be used instead. (Description: ",Ha:"id",i:"<td id=\"{$jsxid}_{@jsxid}jsx#ATTNAME#\" class=\"jsx30table {$jsxcellclass}\" unselectable=\"on\" \n  style=\"width:#WIDTH#;{$myselectionbg} {$jsxcellstyle} \">\n  <xsl:apply-templates select=\".\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n    <xsl:with-param name=\"attname\" select=\"'#ATTNAME#'\"/>\n  </xsl:apply-templates>\n</td>",Sa:"_jsxZM",A:"jsxposition",t:"jsxselected",Ia:"{$jsxid}_{@jsxid}jsx",s:"//record[@",X:"box",r:"_jsxUY",sa:'" ',C:"//record[",l:"_",L:/^td/i,ia:"px;width:",Ka:"width:",J:"jsxaftersort",Da:"jsx3.gui.Table.noData",ta:"nc",oa:"background-image:url(",O:"_jsxspy",k:"strId",Ga:"jsxwidth",p:/^tr/i,P:"jsxexecute",I:"object",ha:'<div class="jsx30table_head_port" style="height:',a:"jsx:///xsl/jsxtable.xsl",Aa:"jsxabspath",F:"jsxpath",Ra:"<xsl:template",j:"<xsl:template match=\"record\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n <xsl:param name=\"attname\"/>\n <div unselectable=\"on\" class=\"{@jsxclass}\" style=\"{@jsxstyle};{$jsxcellwrap}\">\n   <xsl:choose>\n     <xsl:when test=\"$attname = 'jsximg' and @jsximg\">\n       <xsl:variable name=\"jsximg_resolved\">\n         <xsl:apply-templates select=\"attribute::*[name()=$attname]\" mode=\"uri-resolver\"/>\n       </xsl:variable>\n       <img unselectable=\"on\" src=\"{$jsximg_resolved}\"/>\n     </xsl:when>\n     <xsl:otherwise>\n       <xsl:value-of select=\"attribute::*[name()=$attname]\"/>\n       <xsl:if test=\"not(attribute::*[name()=$attname])\">&#160;</xsl:if>\n     </xsl:otherwise>\n   </xsl:choose>\n </div>\n</xsl:template>",ua:' class="jsx30table_header_cell" style="width:100px;height:',S:"mouseover",Ma:"//xsl:with-param",K:"//record",aa:'"',B:"jsxchange",g:"jsx:///images/table/sort_desc.gif",b:"text",va:"px;",M:"jsxmenu",f:"descending",T:"mouseout",ga:"</div>",ba:' class="jsx30table" ',e:"ascending",za:"jsxroot",h:"jsx:///images/table/sort_asc.gif",c:"number",H:"jsxbeforesort",wa:"</div></div>",G:"jsxpathtype",Ja:"style",ma:";",ca:'<div class="jsx30table_body" ',pa:");",N:"jsxspy",o:"px",Z:"100%",xa:"beforeEnd",na:'">',Ba:"jsxhomepath",da:"scroll",Oa:"select",m:"jsx",Y:"div",Ea:"//xsl:template/tr",n:"-",ya:"repaint data",E:"jsxindex"};var
qb=jsx3.util.Logger.getLogger(h.jsxclass.getName());var
Mb=jsx3.gui.Event;var
V=jsx3.gui.Interactive;var
cb=jsx3.xml.CDF;h.DEFAULTXSLURL=jsx3.resolveURI(ub.a);h.TYPE_TEXT=ub.b;h.TYPE_NUMBER=ub.c;h.DB=null;h.SELECTION_BG=ub.d;h.SELECTION_UNSELECTABLE=0;h.SELECTION_ROW=1;h.SELECTION_MULTI_ROW=2;h.SORT_ASCENDING=ub.e;h.SORT_DESCENDING=ub.f;h.SORT_DESCENDING_IMG=jsx3.resolveURI(ub.g);h.SORT_ASCENDING_IMG=jsx3.resolveURI(ub.h);h.DEFAULT_HEADER_HEIGHT=20;h.sy=(new
jsx3.xml.Document()).loadXML(ub.i);h.DEFAULT_CELL_VALUE_TEMPLATE=ub.j;a.init=function(r){this.jsxsuper(r);};a.doValidate=function(){var
gb=this.getSelectedIds();var
La=gb.length>0||this.getRequired()==0;this.setValidationState(La?1:0);return this.getValidationState();};a.getValue=function(){var
Ia=this.getSelectionModel();if(Ia==2){return this.getSelectedIds();}else return this.getSelectedIds()[0];};a.setValue=function(b){this.deselectAllRecords();if(b){if(b instanceof Array){if(this.getSelectionModel()!=2&&b.length>1)throw new
jsx3.IllegalArgumentException(ub.k,b);}else b=[b];for(var
La=0;La<b.length;La++)this.selectRecord(b[La]);}return this;};a.TV=function(k){var
Lb=this.getDocument();return Lb.getElementById(this.getId()+ub.l+k);};a.Fq=function(j,o){var
ba=this.getDocument();return ba.getElementById(this.getId()+ub.l+j+ub.m+o);};a.getContentElement=function(g,q){var
ea=this.Fq(g,q);if(ea)return ea;};a.focusRowById=function(b){var
Lb=this.TV(b);if(Lb)Lb.focus();};a.nE=function(b,k){if(k.parentNode.childNodes[1]){var
ha=k.scrollLeft;k.parentNode.childNodes[1].childNodes[0].style.left=ub.n+ha+ub.o;}};a.getSelectionModel=function(i){return this.jsxselectionmodel!=null?this.jsxselectionmodel>2?0:this.jsxselectionmodel:i!=null?i:null;};a.setSelectionModel=function(o){this.jsxselectionmodel=o;};a.Tg=function(m,s){var
Ja=m.srcElement();if(Ja==null)return;var
Na=this.getRendered(s);while(Ja&&Ja!=Na&&Ja.tagName.search(ub.p)==-1)Ja=Ja.parentNode;if(Ja==Na)return;var
A=Ja.getAttribute(ub.q);if(jsx3.gui.isMouseEventModKey(m)){this.T2(A);if(this.isRecordSelected(A)){this.nz(m,A,null);}else this.LX(m,A,null,true);}else if(m.shiftKey()){var
O=this.uD();if(O){var
Ca=this.TV(O);if(Ca)this.NN(m,Ca,Ja);}else{this.T2(A);this.LX(m,A,null,false);}}else{this.T2(A);if(!this.isRecordSelected(A))this.LX(m,A,null,false);}};a.hF=function(){if(!this._jsxUY)this._jsxUY={bg:this.getServer().resolveURI(this.getSelectionBG(h.SELECTION_BG))};return this._jsxUY.bg;};a.getSelectionBG=function(j){return this.jsxselectionbg?this.jsxselectionbg:j?j:null;};a.setSelectionBG=function(g){delete this[ub.r];this.jsxselectionbg=g;};a.T2=function(l){this._jsxi0=l;};a.uD=function(){return this._jsxi0;};a.IM=function(){return this.getDocument().getElementById(this.uD());};a.getSelectedNodes=function(){return this.getXML().selectNodes(ub.s+ub.t+ub.u);};a.getSelectedIds=function(){var
xa=[];var
Ya=this.getXML().selectNodeIterator(ub.s+ub.t+ub.u);while(Ya.hasNext()){var
Sa=Ya.next();xa[xa.length]=Sa.getAttribute(ub.q);}return xa;};a.Pk=function(q){var
W=this.getRecord(q);return W&&(W[ub.v]==null||W[ub.v]!=ub.w);};a.isRecordSelected=function(q){var
Oa=this.getRecord(q);return Oa!=null&&Oa[ub.t]==ub.w;};a.selectRecord=function(o){if(this.getSelectionModel()==0)return;if(!this.Pk(o))return;this.LX(false,o,null,this.getSelectionModel()==2);};a.deselectRecord=function(k){this.nz(false,k,null);};a.deselectAllRecords=function(){var
ya=this.getSelectedIds();var
X=ya.length;for(var
ia=0;ia<X;ia++)this.nz(false,ya[ia]);};a.LX=function(n,l,d,o){var
Ka=this.getSelectionModel(1);var
z=this.getRecordNode(l);var
va=o||n&&this.getCanDrag()==1;if(Ka==0||!z||z.getAttribute(ub.t)==ub.w&&va||z.getAttribute(ub.v)==ub.w)return false;var
Za=o&&Ka==2;if(!Za)this.deselectAllRecords();z.setAttribute(ub.t,ub.w);d=d||this.TV(l);if(d!=null){var
Lb=ub.x+this.hF()+ub.y;for(var
Ab=0;Ab<d.childNodes.length;Ab++)d.childNodes[Ab].style.backgroundImage=Lb;}this.uH(n,l);return true;};a.nz=function(o,k,e){var
za=this.getRecordNode(k);if(!za||za.getAttribute(ub.t)!=ub.w)return false;za.removeAttribute(ub.t);e=e||this.TV(k);if(e!=null&&e.childNodes){e.style.backgroundImage=ub.z;for(var
Ab=0;Ab<e.childNodes.length;Ab++)e.childNodes[Ab].style.backgroundImage=ub.z;}this.uH(o);return true;};a.NN=function(k,b,s){if(!b||!s)return;var
G=b.getAttribute(ub.q);var
sb=s.getAttribute(ub.q);if(!this.Pk(G)||!this.Pk(sb))return;var
db=b.getAttribute(ub.A);var
y=s.getAttribute(ub.A);var
P=Math.min(db,y);var
Ca=Math.max(db,y);var
Ya=this.getSelectedIds();var
ab=Ya.length;var
Va={};for(var
Sa=0;Sa<ab;Sa++){var
L=this.TV(Ya[Sa]);if(L.getAttribute(ub.A)<P||L.getAttribute(ub.A)>Ca){this.nz(false,Ya[Sa],L);}else Va[Ya[Sa]]=1;}var
ib=s.parentNode;for(var
Sa=P;Sa<=Ca;Sa++){var
sa=ib.childNodes[Sa].getAttribute(ub.q);if(!Va[sa])this.LX(false,sa,ib.childNodes[Sa],true);}this.uH(k,sb);};a.uH=function(l,d){if(!this._jsxJX)this._jsxJX=[];if(l&&l instanceof Mb){var
Db=this.getValue();this.doEvent(ub.B,{objEVENT:l,strRECORDID:d,strRECORDIDS:Db,preVALUE:this._jsxJX});this._jsxJX=Db;}};a.iS=function(m){return this.getColumnProfileDocument().selectSingleNode(ub.C+(m+1)+ub.D);};a.nc=function(f,g){if(this.getCanSort()!=0){var
Xa=Number(g.getAttribute(ub.E));var
lb=this.iS(Xa);var
Z=lb.getAttribute(ub.F);var
la=lb.getAttribute(ub.G)||ub.b;this.setSortPath(Z);this.setSortType(la);var
T=this.doEvent(ub.H,{objEVENT:f,intCOLUMNINDEX:Xa,strSORTPATH:Z,strSORTTYPE:la});if(T!==false){if(T!=null&&typeof T==ub.I){if(T.strSORTPATH)this.setSortPath(T.strSORTPATH);if(T.strSORTTYPE)this.setSortType(T.strSORTTYPE);}this.doSort();this.doEvent(ub.J,{objEVENT:f,intCOLUMNINDEX:Xa,strSORTPATH:this.getSortPath(),strSORTTYPE:this.getSortType()});}}};a.doSort=function(c){if(c){this.setSortDirection(c);}else this.setSortDirection(this.getSortDirection()==ub.e?ub.f:ub.e);if(this.getHeaderHeight()>0){var
xa=this.getSortPath();var
y=this.getColumnProfileDocument().selectNodeIterator(ub.K);var
G=0;var
na=this.getRendered().childNodes[1].childNodes[0];while(y.hasNext())this._0(na,G++
,y.next().getAttribute(ub.F)==xa);}this.repaintData();};a._0=function(l,j,o){l.childNodes[j].style.backgroundImage=o?ub.x+(this.getSortDirection()==ub.e?h.SORT_ASCENDING_IMG:h.SORT_DESCENDING_IMG)+ub.y:ub.z;};a.getSortPath=function(){return this.jsxsortpath==null?ub.z:this.jsxsortpath;};a.setSortPath=function(e){this.jsxsortpath=e;};a.getSortType=function(){return this.jsxsorttype==null?ub.b:this.jsxsorttype;};a.setSortType=function(k){this.jsxsorttype=k;};a.getSortDirection=function(){return this.jsxsortdirection==null?ub.e:this.jsxsortdirection;};a.setSortDirection=function(g){this.jsxsortdirection=g;};a.getCanSort=function(){return this.jsxsort;};a.setCanSort=function(o){this.jsxsort=o;};a.redrawRecord=function(m,l){this.repaint();return this;};a.Df=function(l,n){if(l.rightButton()&&this.getMenu()){var
t=l.srcElement();if(t==null)return;var
ia=this.getRendered(n);while(t&&t!=ia&&t.tagName.search(ub.L)==-1)t=t.parentNode;if(t==ia)return;var
pa=t.parentNode.getAttribute(ub.q);var
E=this.getServer().getJSX(this.getMenu());if(E!=null&&this.Pk(pa)){var
H=this.doEvent(ub.M,{objEVENT:l,objMENU:E,strRECORDID:pa,intCOLUMNINDEX:t.cellIndex});if(H!==false){if(H instanceof Object&&H.objMENU instanceof jsx3.gui.Menu)E=H.objMENU;E.showContextMenu(l,this,pa);}}}};a.el=function(m,l){var
Kb=m.srcElement();if(Kb==null)return;var
U=this.getRendered(l);while(Kb&&Kb!=U&&Kb.tagName.search(ub.L)==-1)Kb=Kb.parentNode;if(Kb==U)return;if(this.hasEvent(ub.N)){var
Y=Kb.parentNode;var
bb=Y.getAttribute(ub.q);var
ab=m.clientX()+jsx3.EventHelp.DEFAULTSPYLEFTOFFSET;var
gb=m.clientY()+jsx3.EventHelp.DEFAULTSPYTOPOFFSET;m.sf();var
Da=this;if(h.DB)window.clearTimeout(h.DB);h.DB=window.setTimeout(function(){h.DB=null;if(Da.getParent()!=null)Da.tF(m,bb,Y,Kb.cellIndex);},jsx3.EventHelp.SPYDELAY);}};a.tF=function(m,i,l,k){this.removeSpyStyle(l);var
t=this.doEvent(ub.N,{objEVENT:m,strRECORDID:i,intCOLUMNINDEX:k});if(t)this.showSpy(t,m);};a.Qi=function(m,l){var
Ib=m.isFakeOut(l);var
sa=m.fromElement();if(sa==null)return;if(this.hasEvent(ub.N)){var
Nb=m.toElement();if(!Nb||Nb.id!=ub.O){jsx3.sleep(jsx3.gui.Interactive.hideSpy);if(h.DB)window.clearTimeout(h.DB);}}};a.oe=function(e,o){if(this.jsxsupermix(e,o))return;};a.kp=function(i,f){var
sa=null;if(f==null)sa=this.getSelectedIds();else if(!(f instanceof Array))sa=[f];else sa=f;for(var
Nb=0;Nb<sa.length;Nb++){var
Wa=sa[Nb];if(Wa==null||!this.Pk(Wa))continue;var
kb=this.getRecordNode(Wa);var
qa=kb.getAttribute(ub.P);if(qa){var
fb={strRECORDID:Wa};if(i instanceof Mb)fb.objEVENT=i;this.eval(qa,fb);}}if(i)this.doEvent(ub.P,{objEVENT:i,strRECORDIDS:this.getSelectedIds(),strRECORDID:sa[0]});};a.Oh=function(i,l){var
w=i.srcElement();if(w==null)return;var
u=this.getRendered(l);while(w&&w!=u&&w.tagName.search(ub.p)==-1)w=w.parentNode;if(w==u)return;var
va=w.getAttribute(ub.q);if(va)this.kp(i,va);};h.Tj={};h.Tj[ub.Q]=true;h.Tj[ub.R]=true;h.Tj[ub.S]=true;h.Tj[ub.T]=true;h.Tj[ub.U]=true;h.Tj[ub.V]=true;a.Rc=function(d,n,g){this.Gi(d,n,g,1);jsx3.sleep(function(){this.Rq(n);},ub.W,this);};a.Gc=function(s){this.applyDynamicProperties();if(this.getParent()&&(s==null||isNaN(s.parentwidth)||isNaN(s.parentheight))){s=this.getParent().uf(this);}else if(s==null)s={};s.boxtype=ub.X;s.tagname=ub.Y;if(s.left==null)s.left=0;if(s.top==null)s.top=0;if(s.width==null)s.width=ub.Z;if(s.height==null)s.height=ub.Z;var
y;if((y=this.getBorder())!=null&&y!=ub.z)s.border=y;return new
jsx3.gui.Painted.Box(s);};a.paint=function(){this.applyDynamicProperties();var
ma=this.getId();var
D=this.doTransform();if(!D)D=this.getNoDataMessage();var
P=ub.z;if(this.getEnabled()==1)P=this.jh(h.Tj,0);var
la=this.renderAttributes(null,true);var
Aa=this.Wl(true);Aa.setAttributes(this.bn()+P+ub._+ma+ub.aa+this.on()+ub.ba+la);Aa.setStyles(this.Yh()+this.zf()+this.eh()+this.ad()+this.Bf()+this.Xe()+this.bd()+this.bi()+this.fh());return Aa.paint().join(ub.ca+this.Gj(ub.da,ub.ea)+ub.fa+D+ub.ga);};a.onAfterPaint=function(e){this.h3(e);};a.h3=function(o){var
Y=this.getId();var
mb=this.getHeaderHeight();if(mb){var
vb=o.childNodes[0];if(vb!=null){do
vb=vb.childNodes[0];while(vb&&vb.tagName.search(ub.p)==-1);if(vb){var
ka=[];ka.push(ub.ha+mb+ub.ia+o.childNodes[0].offsetWidth+ub.ja+ub.ka+this.getHeaderClass(ub.z)+ub.aa+ub.la+this.Of()+ub.ma+this.getHeaderStyle(ub.z)+ub.na);var
ma=this.getSortPath();var
Hb=ub.oa+(this.getSortDirection()==ub.e?h.SORT_ASCENDING_IMG:h.SORT_DESCENDING_IMG)+ub.pa;var
hb=this.getColumnProfileDocument().selectNodeIterator(ub.K);var
ab=0;while(hb.hasNext()){var
fa=hb.next();var
Ta=ma&&fa.getAttribute(ub.F)==ma?Hb:ub.z;var
u=fa.getAttribute(ub.qa)||ub.z;ka.push(ub.ra+ab+++ub.sa+this.Gj(ub.Q,ub.ta)+ub.ua+mb+ub.va+Ta+ub.na+u+ub.ga);}ka.push(ub.wa);if(o.childNodes.length==2){jsx3.html.setOuterHTML(o.childNodes[1],ka.join(ub.z));}else jsx3.html.insertAdjacentHTML(o,ub.xa,ka.join(ub.z));this.Rq(o);}}}};a.Rq=function(k){if(this.getHeaderHeight()>0){var
qa=k.childNodes[0];if(qa!=null){do
qa=qa.childNodes[0];while(qa&&qa.tagName.search(ub.p)==-1);if(qa){var
Ca=k.childNodes[1].childNodes[0];if(!Ca)return;k.childNodes[1].style.width=k.childNodes[0].clientWidth+ub.o;var
Ab=0;for(var
ra=0;ra<qa.childNodes.length;ra++){var
pb=qa.childNodes[ra].offsetWidth;var
Y=Ca.childNodes[ra].offsetWidth;var
Gb=pb-Y+window.parseInt(Ca.childNodes[ra].style.width);Ab=Ab+Gb;Ca.childNodes[ra].style.width=Gb+ub.o;}Ca.style.width=Ab+ub.o;}}}};a.repaintHead=function(){this.h3(this.getRendered());};a.repaintData=function(){var
ea=new
jsx3.util.Timer(h.jsxclass,this);var
_a=this.getRendered();if(_a)_a.childNodes[0].innerHTML=this.doTransform();ea.log(ub.ya);};a.doTransform=function(){var
Fa={};Fa.jsxshallowfrom=this.getRenderingContext(ub.za);Fa.jsxtabindex=this.getIndex()==null?0:this.getIndex();Fa.jsxid=this.getId();Fa.jsxsortpath=this.getSortPath();Fa.jsxsortdirection=this.getSortDirection();Fa.jsxsorttype=this.getSortType();Fa.jsxpath=jsx3.getEnv(ub.Aa);Fa.jsxpathapps=jsx3.getEnv(ub.Ba);Fa.jsxpathprefix=this.getUriResolver().getUriPrefix();Fa.jsxselectionbgurl=this.hF();Fa.jsxheaderheight=this.getHeaderHeight(h.DEFAULT_HEADER_HEIGHT);Fa.jsxcellstyle=this.getCellStyle();Fa.jsxcellclass=this.getCellClass();Fa.jsxrowstyle1=this.getRowStyle();Fa.jsxrowclass1=this.getRowClass();Fa.jsxrowstyle2=this.getAlternateRowStyle(Fa.jsx_rowstyle1);Fa.jsxrowclass2=this.getAlternateRowClass(Fa.jsx_rowclass1);Fa.jsxtablestyles=this.Yh()+this.eh()+this.ad()+this.Bf();Fa.jsxcellwrap=this.getWrap(0)?ub.z:ub.Ca;var
qa=this.getXSLParams();for(var tb in qa)Fa[tb]=qa[tb];return this.Al(this.jsxsupermix(Fa));};a.getHeaderStyle=function(e){return this.jsxheaderstyle?this.jsxheaderstyle:e?e:null;};a.setHeaderStyle=function(k){this.jsxheaderstyle=k;};a.getHeaderClass=function(s){return this.jsxheaderclass?this.jsxheaderclass:s?s:null;};a.setHeaderClass=function(c){this.jsxheaderclass=c;};a.getRowStyle=function(){return this.jsxrowstyle;};a.setRowStyle=function(k){this.jsxrowstyle=k;};a.getAlternateRowStyle=function(n){return this.jsxaltrowstyle?this.jsxaltrowstyle:n?n:null;};a.setAlternateRowStyle=function(n){this.jsxaltrowstyle=n;};a.getCellStyle=function(){return this.jsxcellstyle;};a.setCellStyle=function(d){this.jsxcellstyle=d;};a.getRowClass=function(){return this.jsxrowclass;};a.setRowClass=function(m){this.jsxrowclass=m;};a.getAlternateRowClass=function(i){return this.jsxaltrowclass?this.jsxaltrowclass:i?i:null;};a.setAlternateRowClass=function(g){this.jsxaltrowclass=g;};a.getCellClass=function(){return this.jsxcellclass;};a.setCellClass=function(o){this.jsxcellclass=o;};a.getWrap=function(b){return this.jsxwrap!=null?this.jsxwrap:b!=null?b:null;};a.setWrap=function(r){this.jsxwrap=r;};a.onXmlBinding=function(m){this.jsxsupermix(m);this.repaint();};a.getNoDataMessage=function(){return this.jsxnodata==null?this.wi(ub.Da):this.jsxnodata;};a.onSetChild=function(f){return false;};a.getXSL=function(){return this.yk();};a.yk=function(){var
bb=new
jsx3.util.Timer(h.jsxclass,this);var
Cb=jsx3.getSharedCache().getOrOpenDocument(h.DEFAULTXSLURL,null,jsx3.xml.XslDocument.jsxclass);var
zb=this.getServer().getCache();var
ea=zb.getDocument(this.getXSLId());if(ea==null){ea=Cb.cloneDocument();zb.setDocument(this.getXSLId(),ea);var
x=ea.selectSingleNode(ub.Ea);var
mb=this.getValueTemplate(h.DEFAULT_CELL_VALUE_TEMPLATE);var
wb=new
jsx3.xml.Document();wb.loadXML(mb);if(!wb.hasError()){ea.appendChild(wb);}else{qb.error(ub.Fa+wb.getError().description+ub.y);return;}var
nb=this.getColumnProfileDocument().selectNodeIterator(ub.K);while(nb.hasNext()){var
kb=nb.next();var
ja={jsxpath:kb.getAttribute(ub.F),jsxwidth:kb.getAttribute(ub.Ga)};if(jsx3.util.strEmpty(ja.jsxwidth)){ja.jsxwidth=ub.z;}else if(!isNaN(ja.jsxwidth))ja.jsxwidth+=ub.o;wb=h.sy.cloneDocument();wb.setAttribute(ub.Ha,ub.Ia+ja.jsxpath);wb.setAttribute(ub.Ja,ub.Ka+ja.jsxwidth+ub.La);wb.selectSingleNode(ub.Ma,ub.Na).setAttribute(ub.Oa,ub.Pa+ja.jsxpath+ub.Pa);x.appendChild(wb);}}bb.log(ub.Qa);return ea;};a.getValueTemplate=function(n){if(this.jsxvaluetemplate!=null&&jsx3.util.strTrim(this.jsxvaluetemplate).indexOf(ub.Ra)==0)return this.jsxvaluetemplate;if(n!=null)return n;};a.setValueTemplate=function(f){this.jsxvaluetemplate=f;this.nj();};a.nj=function(e){this.resetXslCacheData();};a.getColumnProfileDocument=function(){if(!this._jsxZM){this._jsxZM=cb.Document.newDocument();if(!jsx3.util.strEmpty(this.jsxcolumnprofile)){this._jsxZM.loadXML(this.jsxcolumnprofile);if(this._jsxZM.hasError()){qb.error(ub.Fa+this._jsxZM.getError().description+ub.y);this._jsxZM=cb.Document.newDocument();}}}return this._jsxZM.cloneDocument();};a.getColumnProfile=function(){return this.jsxcolumnprofile;};a.setColumnProfile=function(e){this.jsxcolumnprofile=e+ub.z;delete this[ub.Sa];this.nj();};a.getRenderingContext=function(r){return this.jsxrenderingcontext!=null&&this.jsxrenderingcontext!=ub.z?this.jsxrenderingcontext:r!=null?r:null;};a.setRenderingContext=function(m,r){this.jsxrenderingcontext=m;this.nj(true);if(!r)this.repaint();};a.getHeaderHeight=function(n){return this.jsxheaderheight!=null?Number(this.jsxheaderheight):n?n:null;};a.setHeaderHeight=function(n,s){this.jsxheaderheight=n;if(!s)this.repaint();};});
