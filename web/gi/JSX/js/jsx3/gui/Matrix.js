/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.xml.Cacheable","jsx3.gui.Form","jsx3.gui.Block","jsx3.gui.Matrix.Column","jsx3.util.MessageFormat");jsx3.Class.defineClass("jsx3.gui.Matrix",jsx3.gui.Block,[jsx3.gui.Form,jsx3.xml.Cacheable,jsx3.xml.CDF],function(m,q){var
ub={Td:"_jsxBF",Od:"xsl",yc:"dq",Jd:"default",Pc:") exceeded by: ",_:"mouseover",yb:"undefined",pd:"focus",Pb:"position:relative;",V:"If the header border and body border do not share the same pixel width, the columns in the matrix may not align as expected (",Jc:"jsxbeforeresize",U:"tr",wc:"E",Tc:"tbody",Cd:"_value",d:"font-weight:bold",fa:"mouseup",qb:"//record[@",R:"box",Xd:"]",sd:"W3",pc:"jsxadopt",Q:"100%",Xc:"horizontal",de:"id",Pa:"_masks",qa:"gN",la:'_body" class="jsx30matrix_body" ',ke:/^(on(?:mousedown|click|focus|blur|mouseup|mouseover|mouseout|dblclick|scroll|keydown|keypress))/i,cd:"contextnodes",nd:"//xsl:template[@name='row_template']//tr/xsl:choose/xsl:when",yd:"$jsx_cell_value_template_id='",lb:"1",nc:"jsx30matrix_drop_icon",Za:"Cw",Hb:"jsxaftercommit",ja:"selectstart",W:")\nHeader Border (",Bc:'<div class="jsx30matrix_resize_anchor" jsxindex="',Zb:"<tr class='",gd:"formatters",Ha:'<div class="jsx30matrix_resize_bar">&#160;</div>',Fa:"&#160;",ce:"JSXDRAGID",Kb:"paged",ge:"\\b(",Fb:"_jsxOM",Kc:"Xy",_b:"</tr></table></div>",Ia:'<div class="jsx30matrix_drop_icon" ',kd:"Fetching records: ",X:") != Body Border (",Db:"jsxafterappend",Fd:"Error with column ",sa:"au",l:'<xsl:call-template xmlns:xsl="http://www.w3.org/1999/XSL/Transform" name="{0}">\n  <xsl:with-param name="jsx_is_first_panel_row" select="$jsx_is_first_panel_row"/>\n  <xsl:with-param name="jsx_row_number" select="$jsx_row_number"/>\n  <xsl:with-param name="jsx_rowbg" select="$jsx_rowbg"/>\n  <xsl:with-param name="jsx_cdfkey" select="$jsx_cdfkey"/>\n  <xsl:with-param name="jsx_descendant_index" select="$jsx_descendant_index"/>\n</xsl:call-template>',Ka:"Pp",jb:":",sb:"='1']",_a:"jsxmenu",sc:"N",Da:"overflow:hidden;background-color:#e8e8f5;z-index:11;",J:"*",Fc:"px;background-image:url(",Bd:"name",_c:"vertical",_d:"Getting Record Count: ",O:"Matrix Width Recalc, Pass 3 (",gb:"_jsxX6",ie:"BeforeBegin",gc:"rowcontext",P:"Matrix Width Recalc, Pass 4 (",id:"autorow",tc:"S",td:' colspan="{$jsx_colspan}" jsxcolspan="{$jsx_colspan}" ',Aa:"xD",a:"jsx:///images/matrix/select.gif",Zd:/(\d+)/,ua:' class="jsx30matrix_scrollv"',ib:";",ec:"12px",S:"div",Ma:'_masks" class="jsx30matrix_masks">',ee:"redrawCell can not resolve the on-screen HTML element ot update",bb:"jsxaftersort",Dc:"px;",g:"jsx:///images/matrix/file.gif",b:"jsx:///images/matrix/insert_before.gif",Dd:"ui_controller",ic:"_jsxMK",M:"Matrix Width Recalc, Pass 2a (",bc:"7px",Rc:"structure",f:"jsx:///images/matrix/plus.gif",Jb:"plusminus",ga:'id="',T:"inline",ed:"Matrix2pass",ba:"dblclick",e:"jsx:///images/matrix/minus.gif",Hc:'px;" ',pb:"_jsxUY",Mb:"./record",za:"FS",c:"jsx:///images/matrix/append.gif",h:"ascending",wa:"z-index:10;overflow:scroll;",Rd:"jsxhomepath",bd:"fetching panel: ",Ya:"BeforeEnd",Ja:">&#160;</div>",Md:/<\/xsl:template>\s*$/,Hd:/width:\d*px;/,ud:' jsxtype="cell" class="jsx30matrixcolumn_cell" id="{$jsx_id}_jsx_{$jsx_cdfkey}_jsx_',Xb:"' style='",pa:"aV",N:"Matrix Width Recalc, Pass 2b (",qc:"jsxctrldrop",zb:"PK",dd:"jsxrownumber",wd:'class="jsx30matrixcolumn_cell_value"',Vb:"<table class='",ub:"jsxselect",Mc:"jsxafterresize",Ta:"_head",na:"lk",Vc:"-",da:"mousedown",uc:"W",Oa:"paint masks",nb:"jsxid",Tb:"<div id='JSX' class='jsx30matrix_dragicon' style='",Vd:/<ids>([\s\S]*)\s*,\s*<\/ids>/,wb:"jsxautorow",db:"text",Ob:"Fetch the content belonging to: ",qd:"tu",dc:"insertbefore",Oc:"Panel pool max (",Va:'_ghost" cellspacing="0" cellpadding="0" class="jsx30matrix_ghost" style="width:',Xa:"</tr></table>",Pd:"jsx_use_categories",Zc:"Matrix_timeout",Wb:"class",rc:"jsxdrop",Qa:"repaint data",vc:"jsxlazy",xb:"jsxbeforeedit",ea:"click",Cb:"jsxroot",Qc:"reaping panel: ",ra:"mousemove",q:"jsxpaintpage",Yd:"count",v:"_jsxb6",Uc:"_jsx_",Cc:'" style="left:',rb:"jsxselected",Ub:"'>",u:"jsx:///xsl/jsxmatrix.xsl",Ud:"sort",Nb:"jsxopen",ka:"HZ",Ac:"visibility:hidden;",le:"strId",La:'<div id="',z:"Race condition with view...",D:"jsxindent",Nc:"-6px",Wc:"jsxscroll",w:"scroll",Ca:"z-index:10;overflow:auto;",Qd:"jsxabspath",kb:"jsxunselectable",Ab:"_jsxyP",Lb:"record",Nd:"</xsl:template>",Kd:/\{0\}/g,kc:"jsxcanceldrop",od:"//xsl:choose/xsl:when/xsl:choose",Yc:"jsx3.gui.Matrix.seek",Na:"</div>",y:"block",Wa:'px;"><tr>',Gd:": ",Yb:"style",rd:"blur",x:"none",hd:"position:absolute;left:0px;top:",i:"descending",Ad:"//xsl:call-template",jd:"panel",A:"px",Sa:'cellspacing="0" cellpadding="0" class="jsx30matrix_head_table"',t:"hierarchical",jc:"jsx30spyglassbuffer",s:"shallow",hc:"jsxspy",tb:"url(",r:"deep",be:".//record",C:"jsxcolspan",Ua:'<table id="',he:")\\b",ia:"_onMouseUp",L:"): ",fd:"queue formatters",fc:"append",eb:"jsxafterreorder",ta:"Jt",ae:"The paging model was overridden (disabled) because the rendering mode is hierarchical and stepped paging was not explicitly set.",Sc:"jsx_",Rb:"jsxtoggle",oa:"RI",k:"jsx:///images/matrix/sort_asc.gif",ad:"X1",Ga:'<div class="jsx30matrix_scroll_info"><span class="jsx30matrix_scroll_info">&#160;</span></div>',Ed:"//xsl:template[@name='ui_controller']//xsl:call-template",p:"Viewing rows {0} to {1} of {2}",ac:"jsxbeforedrop",ha:'" class="jsx30matrix" ',I:/\d*%/,F:"0",Ra:'_head" class="jsx30matrix_head"',j:"jsx:///images/matrix/sort_desc.gif",hb:/(-\S)/gi,Sb:"pagedfocusdelay",Ec:"width:",K:"Matrix Width Recalc, Pass 1 (",aa:"mouseout",mc:"hierachical",ld:" - to - ",B:"table",Gb:"jsxafteredit",Ib:"jsxtype",je:"td",va:"display:none;",ab:"jsxbeforesort",Gc:");height:",vb:"jsxchange",Eb:"true",fe:"cellvalue",mb:"jsxexecute",zd:"_value'",Ic:"jsxindex",lc:"body",H:"",fb:"focusdelay",Wd:"[",ob:"_jsxhU",G:"object",ma:"mousewheel",oc:"JSX_GENERIC",Lc:"...still need logic here to find the widest cell and make the column exactly as wide, ",ca:"keydown",Z:"img",o:'<xsl:call-template name="{0}">\n  <xsl:with-param name="jsx_cell_width" select="$jsx_true_width"/>\n  <xsl:with-param name="jsx_row_number" select="$jsx_row_number"/>\n  <xsl:with-param name="jsx_descendant_index" select="$jsx_descendant_index"/>\n</xsl:call-template>',xa:'src="',md:"//xsl:template[@name='row_template']//tr",cc:"dropverb",Ba:' class="jsx30matrix_scrollh"',zc:"vG",vd:"{$jsx_selection_bg}{$jsx_rowbg}",Bb:"jsxbeforeappend",xc:"colgroup",m:"<xsl:when xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" test=\"$jsx_cell_value_template_id=''{0}''\">\n  <xsl:for-each select=\"//*[@jsxid=$jsx_record_context]\">\n    <xsl:call-template name=\"{0}\">\n    </xsl:call-template>\n  </xsl:for-each>\n</xsl:when>\n",Qb:"jsxcontextindex",Sd:"<tr",Y:")",Ld:"@",Ea:"_ebMouseOutDropIcon",xd:"test",n:"<xsl:template xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" name=\"{0}\">\n  <xsl:param name=\"jsx_is_first_panel_row\"/>\n  <xsl:param name=\"jsx_row_number\"/>\n  <xsl:param name=\"jsx_rowbg\"/>\n  <xsl:param name=\"jsx_cdfkey\"/>\n  <xsl:param name=\"jsx_descendant_index\"/>\n  <xsl:param name=\"jsx_selection_bg\"><xsl:choose>\n     <xsl:when test=\"@jsxselected=1\">background-image:url(<xsl:value-of select=\"$jsx_selection_bg_url\"/>);</xsl:when>\n   </xsl:choose></xsl:param>\n  <xsl:param name=\"jsx_cell_width\" select=\"''{2}''\"/>\n  <xsl:param name=\"jsx_true_width\">\n    <xsl:choose><xsl:when test=\"$jsx_use_categories!=''0'' and not(@jsxcategory=''0'') and (@jsxcategory or record)\">{3}</xsl:when><xsl:otherwise><xsl:value-of select=\"$jsx_cell_width\"/></xsl:otherwise></xsl:choose>\n  </xsl:param>\n  <xsl:param name=\"jsx_first_row_width_style\">\n    <xsl:choose><xsl:when test=\"$jsx_is_first_panel_row\">width:<xsl:value-of select=\"$jsx_true_width\"/>px;</xsl:when></xsl:choose>\n  </xsl:param>\n  <xsl:param name=\"jsx_colspan\">\n    <xsl:choose><xsl:when test=\"$jsx_use_categories!=''0'' and not(@jsxcategory=''0'') and (@jsxcategory or record)\"><xsl:value-of select=\"$jsx_column_count\"/></xsl:when><xsl:otherwise>1</xsl:otherwise></xsl:choose>\n  </xsl:param>\n  {1}\n</xsl:template>",ya:'"',Id:"{$jsx_first_row_width_style}",E:"0+",cb:"_jsxDY"};var
Qa=jsx3.util.Logger.getLogger(m.jsxclass.getName());var
gb=jsx3.gui.Event;var
La=jsx3.gui.Interactive;var
A=jsx3.xml.CDF;var
Ya=jsx3.gui.Block;var
yb=jsx3.gui.Painted.Box;m.NE=500;m._2=1;m.w5=150;m.AUTO_SCROLL_INTERVAL=50;m.SELECTION_BG=ub.a;m.INSERT_BEFORE_IMG=jsx3.resolveURI(ub.b);m.APPEND_IMG=jsx3.resolveURI(ub.c);m.FOCUS_STYLE=ub.d;m.ICON_MINUS=ub.e;m.ICON_PLUS=ub.f;m.ICON=ub.g;m.SORT_ASCENDING=ub.h;m.SORT_DESCENDING=ub.i;m.SORT_DESCENDING_IMG=jsx3.resolveURI(ub.j);m.SORT_ASCENDING_IMG=jsx3.resolveURI(ub.k);m.MINIMUM_COLUMN_WIDTH=8;m.DEFAULT_HEADER_HEIGHT=20;m.AUTOROW_NONE=0;m.AUTOROW_LAST_ROW=1;m.AUTOROW_FIRST_ROW=2;m.WZ=(new
jsx3.xml.Document()).loadXML(ub.l);m.UB=(new
jsx3.xml.Document()).loadXML(ub.m);m.sy=new
jsx3.util.MessageFormat(ub.n);m.yO=new
jsx3.util.MessageFormat(ub.o);m.DEFAULT_INFO_LABEL=ub.p;m.ON_PAINT_PAGE=ub.q;m.PAGING_OFF=0;m.PAGING_2PASS=1;m.PAGING_CHUNKED=2;m.PAGING_PAGED=3;m.PAGING_STEPPED=4;m.SELECTION_UNSELECTABLE=0;m.SELECTION_ROW=1;m.SELECTION_MULTI_ROW=2;m.REND_DEEP=ub.r;m.REND_SHALLOW=ub.s;m.REND_HIER=ub.t;m.DEFAULT_ROW_HEIGHT=20;m.DEFAULT_PANEL_POOL_COUNT=5;m.DEFAULT_ROWS_PER_PANEL=50;m.DEFAULT_REAPER_INTERVAL=250;m.DEFAULT_PANEL_QUEUE_SIZE=3;m.DEFAULTXSLURL=jsx3.resolveURI(ub.u);m.DEFAULT_XSL_URL=m.DEFAULTXSLURL;m.SCROLL_INC=12;q._jsxhH=[];q.init=function(l){this.jsxsuper(l);};q.Rc=function(s,n,a){var
ba=this.Wl(true,s);delete this[ub.v];var
P=yb.getScrollSize()+1;var
B=this.getScaleWidth()==1?s.parentwidth-P:this.x8();var
X=this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT);ba.recalculate(s,n,a);var
ka=ba.lg(0);ka.recalculate({parentwidth:this.nW(),parentheight:X},n?n.childNodes[0]:null,a);var
Db=ka.lg(0).lg(0);Db.recalculate({parentwidth:B,parentheight:X},n&&n.childNodes[0]?jsx3.html.selectSingleElm(n,0,0,0):null,a);var
u=ba.ec()-X;var
wb=ba.lg(1);wb.recalculate({parentwidth:this.nW(),parentheight:u},n?n.childNodes[1]:null,a);var
Sa=wb.gl()+wb.ec();u=ba.ec()-this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT);var
I={};I.left=wb._b()-1;I.top=0;I.height=Sa+this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT)-(P-yb.getScrollSizeOffset(ub.w));var
Oa=ba.lg(2);Oa.recalculate(I,n?n.childNodes[2]:null,a);var
Jb={};Jb.top=Sa+this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT)-P;Jb.width=wb.mn();var
bb=ba.lg(3);bb.recalculate(Jb,n?n.childNodes[3]:null,a);var
E=bb.lg(0);var
Ia=this.getScaleWidth()||B-P<=ba.mn()?0:B;E.recalculate({width:Ia},n&&n.childNodes[3]?n.childNodes[3].childNodes[0]:null,a);if(n&&n.childNodes[3])if(this.getSuppressHScroller(0)==1||this.getScaleWidth()==1||E.mn()<=bb.mn()){n.childNodes[3].style.display=ub.x;this.setScrollLeft(0);}else n.childNodes[3].style.display=ub.y;var
cb={};cb.left=I.left;cb.top=Sa+this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT)-P;cb.height=ba.ec()-cb.top;var
_a=ba.lg(4);_a.recalculate(cb,n&&n.childNodes[4]?n.childNodes[4]:null,a);var
Ga=this.Xi();var
Ra=this.oC(wb.mn());var
la=false;for(var
Z=0;Z<Ga.length;Z++){var
Y=Ga[Z].Rc({parentwidth:Ra[Z],parentheight:X},n?Ga[Z].getRendered():null,a);la=la||Y==null||Y.w;}if(!la){}else if(n&&n.childNodes[0]){var
O=[];for(var
Z=0;Z<Ga.length;Z++)O.push(Ga[Z].Wl(true).cm());this.AX(n.childNodes[0].childNodes[0],O);Ra=[];for(var
Z=0;Z<Ga.length;Z++)Ra.push(Ga[Z].Wl(true)._b());this.MF(n.childNodes[0].childNodes[0],Ra);O=[];for(var
Z=0;Z<Ga.length;Z++)O.push(Ga[Z].Wl(true).lg(1).cm());if(this.getRenderingModel()==m.REND_HIER){var
Ha=this._K({contextnodes:n.childNodes[1].childNodes[0].childNodes});this.ut(Ha,O);}else this.AX(n.childNodes[1].childNodes[0],O);this.Tw();}else{Qa.trace(ub.z);jsx3.sleep(function(){if(this.getParent())this.af(this.getParent().uf(this),true);},null,this);}};q.MF=function(g,s){var
Fb=0;for(var
ta=1;ta<g.childNodes.length;ta++){var
rb=g.childNodes[ta];Fb=Fb+s[ta-1];rb.style.left=Fb-4+ub.A;}};q.AX=function(f,l){var
ya=this.x8();for(var
Hb=0;Hb<f.childNodes.length;Hb++){var
Lb=f.childNodes[Hb];if(Lb.tagName.toLowerCase()==ub.B){Lb.style.width=ya+ub.A;var
Za=this.mI(Lb);if(Za)for(var
jb=0;jb<Za.childNodes.length;jb++)Za.childNodes[jb].style.width=l[jb]+ub.A;}}};q.ut=function(p,e){var
Ha=this.x8();var
ib=Ha-this.oC()[0]+e[0];for(var
ja=0;ja<p.length;ja++){var
Mb=p[ja];if(!(Mb&&Mb.childNodes))continue;var
la=Mb.parentNode;if(la.tagName.toLowerCase()!=ub.B)la=la.parentNode;var
aa=Ha-parseInt(la.style.width);la.style.width=Ha+ub.A;for(var
Aa=0;Aa<Mb.childNodes.length;Aa++){var
I=Aa==0&&Mb.childNodes[0].getAttribute(ub.C)>1?ib:e[Aa];Mb.childNodes[Aa].style.width=I+ub.A;if(Aa==0&&this.getRenderNavigators(1)!=0){la=Mb.childNodes[Aa].childNodes[0].childNodes[0];var
S=I-la.getAttribute(ub.D);la.style.width=Math.max(0,S)+ub.A;}}}};q.x8=function(i){if(!i)i=this.oC();return eval(i.join(ub.E)+ub.F)/10;};q.oC=function(g){if(typeof this._jsxb6==ub.G){return this._jsxb6.truewidths;}else{if(!g){g=this.nW();var
Aa={width:1000,height:10};var
va=this.getBodyBorder();if(va!=null&&va!=ub.H)Aa.border=va;g=g-(Aa.width-(new
yb(Aa)).mn());}var
F=[];var
N=0;var
ca;var
_=0;var
Ia=this.Xi();var
S={percent:[],wildcard:[],pixel:[]};for(var
u=0;u<Ia.length;u++){var
wa=Ia[u].getWidth();if(jsx3.util.strTrim(String(wa)).search(ub.I)==0){S.percent.unshift(u);ca=parseInt(parseInt(wa)/100*g);}else if(!isNaN(wa)){S.pixel.unshift(u);ca=Number(wa);}else{S.wildcard.unshift(u);if(this.getScaleWidth()){_++;ca=ub.J;}else ca=m.Column.DEFAULT_WIDTH;}if(!isNaN(ca))N=N+ca;F.push(ca);}Qa.trace(ub.K+this.getName()+ub.L+F);if(this.getScaleWidth()){var
C=g-N;var
la;if(_&&C>=0&&parseInt(C/_)>8){var
Cb=_;var
Kb=C/_;if(Kb>parseInt(Kb)){Kb=parseInt(Kb);la=C-(_-1)*Kb;}else la=Kb;for(var
u=0;u<F.length;u++)if(F[u]==ub.J){F[u]=_==1?la:Kb;_--;}_=Cb;}else if(_)for(var
u=0;u<F.length;u++)if(F[u]==ub.J)F[u]=8;Qa.trace(ub.M+this.getName()+ub.L+F);var
Va=this.x8(F);var
zb=Va-g;if(zb>0){var
Ra=zb;if(S.wildcard.length)Ra=this.Xu(F,S.wildcard,zb);if(S.percent.length&&Ra>0)Ra=this.Xu(F,S.percent,Ra);if(S.pixel.length&&Ra>0)Ra=this.Xu(F,S.pixel,Ra);}Qa.trace(ub.N+this.getName()+ub.L+F);}for(var
u=0;u<F.length;u++)if(F[u]<8)F[u]=8;Qa.trace(ub.O+this.getName()+ub.L+F);var
Va=this.x8(F);var
C=g-Va;if(C>0)F[F.length-1]+=C;Qa.trace(ub.P+this.getName()+ub.L+F);this._jsxb6={truewidths:F};return F;}};q.Xu=function(d,h,n){var
ya=0;var
Ca=parseInt(n/h.length);for(var
Ra=0;Ra<h.length;Ra++){var
Q=h[Ra];if(Ra==h.length-1)Ca=n-(h.length-1)*Ca;if(d[Q]-Ca<8){ya=ya+(8-(d[Q]-Ca));d[Q]=8;}else d[Q]-=Ca;}return ya;};q.nW=function(){var
E=this.getParent().uf(this).parentwidth;return this.getSuppressVScroller(0)==1?E:E-yb.getScrollSize();};q.uf=function(d){var
Ra=d.getDisplayIndex();return {parentwidth:Ra!=null?this.oC()[Ra]:null,parentheight:this.Wl(true).lg(0).ec()};};q.Gc=function(k){this.applyDynamicProperties();if(this.getParent()&&(k==null||isNaN(k.parentwidth)||isNaN(k.parentheight))){k=this.getParent().uf(this);this.nj();}else if(k==null)k={};var
C=yb.getScrollSize()+1;k.left=0;k.top=0;k.width=ub.Q;k.height=ub.Q;k.boxtype=ub.R;k.tagname=ub.S;var
Y=new
yb(k);var
ca=this.getScaleWidth()==1?this.nW():this.x8();var
I={};I.left=0;I.top=0;I.height=this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT);I.width=ub.Q;I.parentwidth=this.nW();I.boxtype=ub.R;I.tagname=ub.S;var
ob;if((ob=this.getHeaderBorder())!=null&&ob!=ub.H)I.border=ob;var
F=new
yb(I);Y.Yf(F);var
Hb={};Hb.tagname=ub.S;Hb.boxtype=ub.R;Hb.top=0;Hb.left=0;var
jb=new
yb(Hb);F.Yf(jb);var
Gb={};Gb.left=0;Gb.top=0;Gb.width=ub.Q;Gb.parentwidth=F.mn();Gb.boxtype=ub.R;Gb.tagname=ub.B;var
Pa=new
yb(Gb);jb.Yf(Pa);var
ta={};ta.boxtype=ub.T;ta.tagname=ub.U;var
N=new
yb(ta);Pa.Yf(N);var
Cb=Y.ec()-this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT);var
zb={};zb.left=0;zb.top=this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT);zb.parentwidth=this.nW();zb.width=ub.Q;zb.parentheight=Cb;zb.height=ub.Q;zb.boxtype=ub.R;zb.tagname=ub.S;if((ob=this.getBodyBorder())!=null&&ob!=ub.H)zb.border=ob;var
G=new
yb(zb);Y.Yf(G);if(this.getHeaderHeight()!=0&&F.getBorderWidth()!=G.getBorderWidth())Qa.warn(ub.V+this.getName()+ub.W+this.getHeaderBorder()+ub.X+this.getBodyBorder()+ub.Y);var
qb={};qb.tagname=ub.S;qb.boxtype=ub.R;qb.top=0;qb.left=0;var
hb=new
yb(qb);G.Yf(hb);var
H=G.gl()+G.ec();var
B={};B.boxtype=ub.R;B.tagname=ub.S;B.left=G._b()-1;B.top=0;B.width=C;B.height=H+this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT)-(C-yb.getScrollSizeOffset(ub.w));var
ha=new
yb(B);Y.Yf(ha);var
S={};S.boxtype=ub.T;S.tagname=ub.Z;S.empty=true;S.left=0;S.top=0;S.width=1;S.height=this.getPagingModel()!=3?0:this.Xt()+this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT)+C;var
ra=new
yb(S);ha.Yf(ra);var
Va={};Va.boxtype=ub.R;Va.tagname=ub.S;Va.left=G.xm();Va.top=H+this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT)-C;Va.height=C;Va.width=G.mn();var
nb=new
yb(Va);Y.Yf(nb);var
u={};u.boxtype=ub.T;u.tagname=ub.Z;u.empty=true;u.left=0;u.top=0;u.width=ca-C<=Y.mn()?0:ca;u.height=1;var
Na=new
yb(u);nb.Yf(Na);var
Aa={};Aa.boxtype=ub.R;Aa.tagname=ub.S;Aa.left=B.left;Aa.top=H+this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT)-C;Aa.width=C;Aa.height=Y.ec()-Aa.top;var
va=new
yb(Aa);Y.Yf(va);return Y;};m.Tj={};m.Tj[ub._]=true;m.Tj[ub.aa]=true;m.Tj[ub.ba]=true;m.Tj[ub.ca]=true;m.Tj[ub.da]=true;m.Tj[ub.ea]=true;m.Tj[ub.fa]=true;q.paint=function(){this.applyDynamicProperties();var
tb=this.getId();var
ab=this.Wl(true);ab.setAttributes(ub.ga+tb+ub.ha+this.Gj(ub.fa,ub.ia));var
cb=this.jh(m.Tj,1);if(jsx3.CLASS_LOADER.IE)cb=cb+this.Gj(ub.ja,ub.ka,1);var
Jb=ab.lg(1);Jb.setAttributes(ub.ga+tb+ub.la+cb);var
ca=Jb.lg(0);ca.setStyles(this.zf()+this.Of());ca.setAttributes(this.Gj(ub.ma,ub.na,2));var
pb=ab.lg(2);pb.setAttributes(this.Gj(ub.w,ub.oa)+this.Gj(ub._,ub.pa)+this.Gj(ub.aa,ub.qa)+this.Gj(ub.ra,ub.sa)+this.Gj(ub.da,ub.ta)+jsx3.html.Kf+ub.ua);var
Bb=this.getSuppressVScroller(0)==1?ub.va:ub.H;pb.setStyles(ub.wa+Bb);var
wa=pb.lg(0);wa.setAttributes(ub.xa+Ya.SPACE+ub.ya);var
Gb=ab.lg(3);Gb.setAttributes(this.Gj(ub.w,ub.za)+this.Gj(ub.da,ub.Aa)+jsx3.html.Kf+ub.Ba);Bb=this.getSuppressHScroller(0)==1||this.getScaleWidth()==1||Gb.lg(0).mn()<=Gb.mn()?ub.va:ub.H;Gb.setStyles(ub.Ca+Bb);var
B=Gb.lg(0);B.setAttributes(ub.xa+Ya.SPACE+ub.ya);var
Pa=ab.lg(4);Pa.setStyles(ub.Da);var
va=ub.H;var
Kb=this.getPagingModel(0);if(Kb==0||Kb==4){if(this.MQ()==2)va=va+this.Q8(-1);va=va+this.Q8(0);if(this.MQ()==1)va=va+this.Q8(-1);}var
ga=this.Vv(true);var
hb=this.Gj(ub.aa,ub.Ea);var
S=ab.paint().join(this.h3()+Jb.paint().join(ca.paint().join(va+ga))+pb.paint().join(wa.paint().join(ub.H))+Gb.paint().join(B.paint().join(ub.H))+Pa.paint().join(ub.Fa)+ub.Ga+ub.Ha+ub.Ia+hb+ub.Ja);return S;};q.onAfterPaint=function(a){var
Mb=this.getPagingModel(0);if(Mb==0||Mb==4){this.Pp();}else jsx3.sleep(this.Pp,ub.Ka+this.getId(),this);};q.Vv=function(k){var
la=this.getPagingModel(0);var
Wa=la==0||la==4;if(k&&Wa||!k&&!Wa){var
z=new
jsx3.util.Timer(m.jsxclass,this);var
Aa=this.Xi();var
ra=[];for(var
Bb=0;Bb<Aa.length;Bb++){var
mb=Aa[Bb];var
Ca=mb.getEditMasks();for(var
P=0;P<Ca.length;P++){var
X=Ca[P];if(m.ZJ(X,mb))if(X.emGetType()==m.EditMask.NORMAL||X.emGetType()==m.EditMask.DIALOG)ra.push(X);}}var
I=ub.La+this.getId()+ub.Ma+this.paintChildren(ra)+ub.Na;z.log(ub.Oa);return I;}return ub.H;};q.resetMask=function(){this.endEditSession();};q.repaintData=function(){var
v=new
jsx3.util.Timer(m.jsxclass,this);var
u=this.vD();if(u){var
S=this.getScrollTop();var
tb=ub.H;var
ya=this.getPagingModel(0);if(ya==0||ya==4){this.dR();if(this.MQ()==2)tb=tb+this.Q8(-1);tb=tb+this.Q8(0);if(this.MQ()==1)tb=tb+this.Q8(-1);}var
Ma=this.getDocument(u);var
_=Ma.getElementById(this.getId()+ub.Pa);if(_){_.style.display=ub.x;u.parentNode.appendChild(_);}u.innerHTML=tb;this.Pp(false,true);if(_){_.style.display=ub.H;u.appendChild(_);}jsx3.sleep(function(){this.i6(S);},null,this);}v.log(ub.Qa);};q.i6=function(g){var
F=this.vD();if(g==null)g=this.getScrollTop();var
pb=F?F.offsetHeight:0;if(pb<g){this.setScrollTop(pb);}else this.setScrollTop(g);this.Tw(F);};q.h3=function(){var
t=this.getId();var
ca=this.Wl(true);var
fb=this.Wl(true).lg(0).ec();var
X=ca.lg(0);X.setAttributes(ub.ga+t+ub.Ra);var
pa=X.lg(0);var
I=pa.lg(0);I.setAttributes(ub.Sa);var
lb=I.lg(0);var
Ga=[];var
Cb=this.oC();var
Ca=this.Xi();for(var
wb=0;wb<Ca.length;wb++){Ca[wb].ac({parentwidth:Cb[wb],parentheight:fb},null);Ga.push(Ca[wb].paint());}var
Ua=X.paint().join(pa.paint().join(I.paint().join(lb.paint().join(Ga.join(ub.H)))+this.PV(Ca)));return Ua;};q.repaintHead=function(){var
Eb=this.getDocument();var
Wa=Eb.getElementById(this.getId()+ub.Ta);if(Wa){var
Ib=this.h3();jsx3.html.setOuterHTML(Wa,Ib);}};q.Se=function(d,i){var
Kb=this.getServer().getJSXById(i.id);var
Bb=Kb.getDisplayIndex();this.st(Bb);if(d.leftButton()&&Bb>=this.getFixedColumnIndex(0)&&this.getCanReorder()!=0){gb.publish(d);var
U=ub.Ua+this.getId()+ub.Va+parseInt(i.offsetWidth)+ub.Wa+jsx3.html.getOuterHTML(i)+ub.Xa;var
eb=this.getRendered(d).childNodes[0].childNodes[0];jsx3.html.insertAdjacentHTML(eb,ub.Ya,U);var
Ga=eb.lastChild;var
V=this.oC();var
Ea=0;for(var
ab=0;ab<this.LK();ab++)Ea=Ea+V[ab];Ga.style.left=Ea+ub.A;this._jsxUB=Ea;La.Bn(d,Ga,function(b,a){return [b,0];});gb.subscribe(ub.fa,this,ub.Za);}else if(d.rightButton()){var
mb=Kb.getMenu();if(mb){var
Ua=Kb.getServer().getJSX(mb);if(Ua!=null){var
Pa={objEVENT:d,objMENU:Ua};var
va=Kb.doEvent(ub._a,Pa);if(va!==false){if(va instanceof Object&&va.objMENU instanceof jsx3.gui.Menu)Ua=va.objMENU;Ua.showContextMenu(d,Kb);}}}}};q.Cw=function(a){jsx3.EventHelp.reset();gb.unsubscribe(ub.fa,this,ub.Za);var
Z=this.getRendered(a.event).childNodes[0].childNodes[0].lastChild;var
ja=parseInt(Z.style.left);jsx3.html.removeNode(Z);var
ha=this.getChildren();var
ob=this.Xi();var
ba=ob[this.LK()];var
la=jsx3.util.arrIndexOf(ha,ba);if(ja==this._jsxUB){if(this.getCanSort()!=0)this.nc(a);}else if(this.getCanReorder()!=0){var
Fa=this.oC();var
hb=0;for(var
_=0;_<ob.length;_++){if(hb>=ja){var
ya=ob[_];var
nb=jsx3.util.arrIndexOf(ha,ya);this.Bt(a,ba,ya);return;}hb=hb+Fa[_];}this.Bt(a,ba,ob[ob.length-1]);}};q.TO=function(e){return e.getSortPath();};q.Ix=function(c){return c.getSortDataType();};q.nc=function(j){var
aa=this.Xi();var
fa=aa[this.LK()];if(fa&&fa.getCanSort()!=0&&this.getCanSort()!=0){this.ls();var
y=jsx3.util.arrIndexOf(this.getChildren(),fa);var
t=this.TO(fa);var
Ia=this.Ix(fa);var
Q=this.doEvent(ub.ab,{objEVENT:j,objCOLUMN:fa,strSORTPATH:t,strSORTTYPE:Ia});if(Q!==false){if(Q!=null&&typeof Q==ub.G)if(Q.objCOLUMN!=null){fa=Q.objCOLUMN;t=this.TO(fa);Ia=this.Ix(fa);}this.setSortPath(t);this.setSortType(Ia);this.doSort();this.doEvent(ub.bb,{objEVENT:j,objCOLUMN:fa,strSORTPATH:t,strSORTTYPE:Ia});}}};q.doSort=function(f){if(f){this.setSortDirection(f);}else this.setSortDirection(this.getSortDirection()==ub.h?ub.i:ub.h);var
Oa=this.getSortPath();var
I=this.Xi();for(var
xb=0;xb<I.length;xb++)I[xb]._applySortIcon(I[xb].getSortPath()==Oa);delete this[ub.cb];this.repaintData();};q.getSortPath=function(){return this.jsxsortpath==null?ub.H:this.jsxsortpath;};q.setSortPath=function(o){this.jsxsortpath=o;};q.getSortType=function(){return this.jsxsorttype==null?ub.db:this.jsxsorttype;};q.setSortType=function(h){this.jsxsorttype=h;};q.getSortDirection=function(){return this.jsxsortdirection==null?ub.h:this.jsxsortdirection;};q.setSortDirection=function(g){this.jsxsortdirection=g;};q.getCanSort=function(){return this.jsxsort;};q.setCanSort=function(c){this.jsxsort=c;};q.Bt=function(l,n,b){var
H=this.Xi();var
ob=jsx3.util.arrIndexOf(H,n);var
pb=jsx3.util.arrIndexOf(H,b);var
na=this.getFixedColumnIndex(0);if(ob<=na)return;if(pb<na)b=this.Xi()[na];if(!b)return;var
Z=this.insertBefore(n,b,true);if(Z){var
Fb=n.getChildIndex();this.doEvent(ub.eb,{objEVENT:l,intOLDINDEX:ob,intNEWINDEX:Fb});}};q.adoptChild=function(e,a,b){this.jsxsuper(e,false,b);this.nj();if(a!==false)this.repaint();};q.insertBefore=function(c,b,i){var
fa=this.jsxsuper(c,b,false);if(fa){this.nj();if(i!=false)this.repaint();}return fa;};q.getCanReorder=function(){return this.jsxreorder;};q.setCanReorder=function(f){this.jsxreorder=f;};q.focusRowById=function(p){this.focusCellByIndex(p,0);};q.focusCellById=function(e,o){if(this.getRenderingModel()==m.REND_HIER)this.revealRecord(e);var
wa=this.Fq(e,o);if(wa){wa.focus();}else if(this.DJ(e))jsx3.sleep(function(){jsx3.sleep(function(){this.focusCellById(e,o);},ub.fb,this);},ub.fb,this);};q.focusCellByIndex=function(h,a){if(this.getRenderingModel()==m.REND_HIER)this.revealRecord(h);var
jb=this.jO(h,a);if(jb){jb.focus();}else if(this.DJ(h))jsx3.sleep(function(){jsx3.sleep(function(){this.focusCellByIndex(h,a);},ub.fb,this);},ub.fb,this);};q.DJ=function(p){if(this.getPagingModel()==3){var
F=this.getSortedIds();var
J=F.length;for(var
Gb=0;Gb<J;Gb++)if(F[Gb]==p){this.setScrollTop(this.getRowHeight(m.DEFAULT_ROW_HEIGHT)*Gb);this._jsxDY=this.getRowHeight(m.DEFAULT_ROW_HEIGHT)*Gb;this.jB().unshift({index:this.KO()});this.X1();return true;}}return false;};q.tu=function(n,r){this.N9(n,r);this._scrollIntoView(r);};q.W3=function(o,k){};q.getFocusStyle=function(l){return this.jsxfocusstyle?this.jsxfocusstyle:l?l:null;};q.setFocusStyle=function(g){delete this[ub.gb];this.jsxfocusstyle=g;};q.yq=function(b,r){if(typeof this._jsxX6!=ub.G)this._jsxX6=this.ws(this.getFocusStyle(m.FOCUS_STYLE));this.UC(b,this._jsxX6,r);};q.ws=function(b){var
ob=ub.hb;var
M={};var
ta=b.split(ub.ib);for(var
la=0;la<ta.length;la++){var
Na=ta[la]+ub.H;var
Ba=Na.split(ub.jb);if(Ba&&Ba.length==2){var
Oa=Ba[0].replace(ob,function(p,o){return o.substring(1).toUpperCase();});M[Oa]=Ba[1];}}return M;};q.UC=function(j,p,f){if(f){for(var rb in p)j.style[rb]=p[rb];}else for(var rb in p)j.style[rb]=ub.H;};q.dV=function(){return this._jsxcU;};q.CD=function(f){if(this._jsxcU!=f){var
ea=this.hL();if(ea)this.yq(ea,false);}this._jsxcU=f;};q.resetFocusContext=function(i){this.CD();};q.hL=function(){return this.getDocument().getElementById(this.dV());};q.N9=function(f,g){this.oL(f);var
Ba=true;var
na=this.Xi();var
W=na[g.cellIndex];var
ca=W.getEditMasks();for(var
oa=0;oa<ca.length;oa++){var
kb=ca[oa];if(m.ZJ(kb,W))if(kb.emGetType()!=m.EditMask.NORMAL){Ba=false;break;}}if(Ba)g.focus();this.CD(g.id);this.yq(g,true);this.Y7(f,g);};q.Oh=function(i,d){this.u3(i);};q.u3=function(n,s){if(s==null)s=this.getSelectedIds();for(var
aa=0;aa<s.length;aa++){var
sa=s[aa];var
Xa=this.getRecordNode(sa);if(Xa.getAttribute(ub.kb)==ub.lb)continue;this.eval(Xa.getAttribute(ub.mb),{strRECORDID:sa});}if(s.length)this.doEvent(ub.mb,{objEVENT:n,strRECORDID:s[0],strRECORDIDS:s});};q.executeRecord=function(n){var
H=this.getRecordNode(n);if(H)this.eval(H.getAttribute(ub.mb),{strRECORDID:n});};q.Y7=function(f,g){var
Nb=this.getSelectionModel(1);var
Lb=this.sE(g);var
X=Lb.cell;var
ta=X?X.cellIndex:null;var
wa=Lb.row;var
jb=wa.getAttribute(ub.nb);var
Ba=this.MQ()&&this.CM(wa);if(!Ba)if(this.I3().ctrl){this.T2(jb);if(this.isRecordSelected(jb)){this.nz(f,jb,null);}else this.LX(f,jb,wa,true,ta);}else if(this.I3().shift){var
Aa=this.uD();if(Aa){this.NN(f,wa,ta);}else{this.T2(jb);this.LX(f,jb,null,false,ta);}}else{this.T2(jb);if(!this.isRecordSelected(jb))this.LX(f,jb,null,false,ta);}this.A2(f,X,this.Xi()[ta],Ba);delete this[ub.ob];};q.hF=function(){if(!this._jsxUY)this._jsxUY={bg:this.getServer().resolveURI(this.getSelectionBG(m.SELECTION_BG))};return this._jsxUY.bg;};q.getSelectionBG=function(l){return this.jsxselectionbg?this.jsxselectionbg:l?l:null;};q.setSelectionBG=function(i){delete this[ub.pb];this.jsxselectionbg=i;};q.T2=function(g){this._jsxi0=g;};q.uD=function(){return this._jsxi0;};q.IM=function(){return this.getDocument().getElementById(this.uD());};q.cy=function(k,f,o){o.push(k);var
Ga=k.getParent();return !Ga.equals(f)?this.cy(Ga,f,o):o;};q.getSelectedNodes=function(){return this.getXML().selectNodes(ub.qb+ub.rb+ub.sb);};q.getSelectedIds=function(){var
db=[];var
N=this.getXML().selectNodeIterator(ub.qb+ub.rb+ub.sb);while(N.hasNext()){var
mb=N.next();db[db.length]=mb.getAttribute(ub.nb);}return db;};q.Pk=function(p){var
Y=this.getRecord(p);return Y&&(Y[ub.kb]==null||Y[ub.kb]!=ub.lb);};q.isRecordSelected=function(b){var
ba=this.getRecord(b);return ba!=null&&ba[ub.rb]==ub.lb;};q.selectRecord=function(r){if(this.getSelectionModel()==0)return;if(!this.Pk(r))return;this.LX(false,r,null,this.getSelectionModel()==2);};q.deselectRecord=function(g){this.nz(false,g,null);};q.deselectAllRecords=function(){var
oa=this.getSelectedIds();var
Wa=oa.length;for(var
xb=0;xb<Wa;xb++)this.nz(false,oa[xb]);};q.LX=function(o,k,e,p,r){var
Lb=this.getSelectionModel(1);var
sb=this.getRecordNode(k);var
ta=p||o&&this.getCanDrag()==1;if(Lb==0||!sb||sb.getAttribute(ub.rb)==ub.lb&&ta||sb.getAttribute(ub.kb)==ub.lb)return false;var
xa=p&&Lb==2;if(!xa)this.deselectAllRecords();sb.setAttribute(ub.rb,ub.lb);e=e||this.TV(k);if(e!=null){var
bb=ub.tb+this.hF()+ub.Y;for(var
W=0;W<e.childNodes.length;W++)e.childNodes[W].style.backgroundImage=bb;}this.uH(o,k,r);return true;};q.nz=function(e,b,c){var
va=this.getRecordNode(b);if(!va||va.getAttribute(ub.rb)!=ub.lb)return false;va.removeAttribute(ub.rb);c=c||this.TV(b);if(c!=null&&c.childNodes){c.style.backgroundImage=ub.H;for(var
Z=0;Z<c.childNodes.length;Z++)c.childNodes[Z].style.backgroundImage=ub.H;}this.uH(e);return true;};q.NN=function(f,k,g){if(!k)return;var
hb=this.uD();var
wb=k.getAttribute(ub.nb);if(!this.Pk(hb)||!this.Pk(wb))return;var
u=this.getSelectedIds();var
na=u.length;for(var
Ta=0;Ta<na;Ta++)this.nz(false,u[Ta],this.TV(u[Ta]));u=this.getSortedIds();var
W=new
jsx3.util.List(u);var
sa=W.indexOf(hb);var
ka=W.indexOf(wb);var
Ka=Math.min(sa,ka);var
t=Math.max(sa,ka);var
za=u.length;var
T=0;for(var
Ta=Ka;Ta<=t;Ta++)this.LX(false,u[Ta],this.TV(u[Ta]),true,g);this.uH(f,wb,g);};q.uH=function(o,a,p){if(o&&o instanceof gb){this.doEvent(ub.ub,{objEVENT:o,strRECORDID:a,strRECORDIDS:this.getSelectedIds(),objCOLUMN:p!=null?this.Xi()[p]:null});this.doEvent(ub.vb,{objEVENT:o});}};q.A2=function(d,r,k,n){var
Za=k.getEditMask();if(Za!=null){var
Mb,ha;if(n){var
ca=this.a3();ha=ca[k.getPath()];Mb=ub.wb;}else{Mb=r.parentNode.getAttribute(ub.nb);ha=k.getValueForRecord(Mb);}var
ka=this.getRendered(r);var
C=this.vD();var
O=Za.emGetType()==m.EditMask.NORMAL||Za.emGetType()==m.EditMask.DIALOG;if(O){var
Y=this.doEvent(ub.xb,{objEVENT:d,strRECORDID:Mb,objCOLUMN:k});if(Y===false)return;if(Y!=null&&typeof Y==ub.G)if(typeof Y.objMASK!=ub.yb)Za=Y.objMASK;}var
Q=jsx3.html.getRelativePosition(C,r);var
Nb=jsx3.html.getRelativePosition(ka,ka);Nb.W-=parseInt(C.style.left);Nb.H-=parseInt(C.style.top);var
Db=this._jsxOM;if(Db&&Db.mask&&Db.mask.emGetSession())this.endEditSession();if(Za.cB(ha,Q,Nb,this,k,Mb,r)){this._jsxOM={mask:Za,column:k,recordId:Mb,value:ha};gb.subscribeLoseFocus(this,this.getRendered(r).childNodes[1],ub.zb);}}};q.getAutoRowSession=function(){return this.a3();};q.a3=function(){if(!this._jsxyP){this._jsxyP={jsxid:jsx3.xml.CDF.getKey()};this._jsxb8={jsxid:this._jsxyP.jsxid};}return this._jsxyP;};q.DV=function(){if(this._jsxyP)for(var ha in this._jsxyP)if(this._jsxb8[ha]!=this._jsxyP[ha]&&!(jsx3.util.strEmpty(this._jsxyP[ha])&&jsx3.util.strEmpty(this._jsxb8[ha])))return true;return false;};q.WX=function(r){var
Gb=this.a3();Gb[r.column.getPath()]=r.newvalue;};q.Uq=function(k,a){if(a!==false)this.oL(k);var
ja=this.a3();if(ja!=null&&!jsx3.util.strEmpty(ja.jsxid)){delete this[ub.Ab];var
ca;if(k)ca=this.doEvent(ub.Bb,{objEVENT:k,objRECORD:ja});if(ca!==false){var
Wa=this.insertRecord(ja,this.getRenderingContext(ub.Cb),true);if(k)this.doEvent(ub.Db,{objEVENT:k,objRECORDNODE:Wa});}var
Lb={jsxid:ub.wb};this.insertRecord(Lb,null,false);this.redrawRecord(ub.wb,2);this.deleteRecord(ub.wb,false);}};q.commitAutoRowSession=function(i,j){this.Uq(i,false);if(!isNaN(j)){var
Ua=this.TV(ub.wb);if(Ua&&Ua.childNodes[+j])Ua.childNodes[+j].focus();}};q.n0=function(){delete this[ub.Ab];};q.CM=function(g){if(!g)return false;if(g.getAttribute(ub.nb)!=ub.wb)return false;var
ma=g.parentNode;if(ma.tagName.toLowerCase()!=ub.B)ma=ma.parentNode;return ma.getAttribute(ub.wb)==ub.Eb;};q.oL=function(c,n){var
Ja=this._jsxOM;if(Ja!=null){if(!n){delete this[ub.Fb];gb.unsubscribeLoseFocus(this);}var
ua=n?Ja.mask.emGetValue():Ja.mask.ZZ();var
ka=Ja.recordId==ub.wb&&this.MQ()&&this.CM(this.TV(ub.wb));var
ma=true;if(c&&ka){var
Va=this.TV(ub.wb);ma=jsx3.html.findElementUp(c.srcElement(),function(h){return h==Va;},true)==null;}if(!(ma&&ka&&this.DV()))if(Ja.value===ua)return;var
wb=Ja.mask.emGetType()==m.EditMask.NORMAL||Ja.mask.emGetType()==m.EditMask.DIALOG;var
Eb=true;if(wb){if(c!=null)Eb=this.doEvent(ub.Gb,{objEVENT:c,strRECORDID:Ja.recordId,objCOLUMN:Ja.column,strNEWVALUE:ua});if(Eb!=null&&typeof Eb==ub.G)if(typeof Eb.strNEWVALUE!=ub.yb)ua=Eb.strNEWVALUE;var
Ma=Ja.column.getPath()==ub.nb;if(Ma&&this.getRecordNode(ua))Eb=false;if(Eb!==false){if(n)Ja.value=ua;if(ka){Ja.newvalue=ua;this.WX(Ja);var
Ha=this.a3();var
z={};for(var Ra in Ha)z[Ra]=Ha[Ra];z.jsxid=ub.wb;this.insertRecord(z,null,false);this.redrawCell(ub.wb,Ja.column);this.deleteRecord(ub.wb,false);if(ma&&!n&&this.DV())this.Uq(c,false);}else if(Ma){this.insertRecordProperty(Ja.recordId,ub.nb,ua,false);this.redrawCell(ua,Ja.column);}else{Ja.column.setValueForRecord(Ja.recordId,ua);this.redrawCell(Ja.recordId,Ja.column);}}}if(c!=null&&Eb!==false)this.doEvent(ub.Hb,{objEVENT:c,strRECORDID:Ja.recordId,objCOLUMN:Ja.column,strVALUE:ua});}};q.endEditSession=function(a){this.oL(a);};q.collapseEditSession=function(i,d){var
Fa=this._jsxOM;if(Fa!=null){Fa.mask.emCollapseEdit(i);this.endEditSession(i);d.focus();}};q.PK=function(n){var
V=n.event.srcElement();var
ob=this._jsxOM;if(ob&&!ob.f1&&!ob.mask.containsHtmlElement(V))this.oL(n.event);};m.ZJ=function(r,b){if(r._jsxkO)return true;if(r.instanceOf(jsx3.gui.Form)){m.EditMask.jsxclass.mixin(r,true);}else if(jsx3.gui.Dialog&&r instanceof jsx3.gui.Dialog){m.DialogMask.jsxclass.mixin(r,true);m.BlockMask.jsxclass.mixin(r,true);m.EditMask.jsxclass.mixin(r,true);}else if(r instanceof Ya){m.BlockMask.jsxclass.mixin(r,true);m.EditMask.jsxclass.mixin(r,true);}else return false;r.emInit(b);r._jsxkO=true;return true;};q.Tg=function(a,l){a.cancelBubble();jsx3.gui.Event.publish(a);var
Mb=a.srcElement();var
sb=this.sE(Mb);var
J=Mb.getAttribute(ub.Ib);if(J==ub.Jb||J==ub.Kb){this.Qt(a,Mb);}else while(Mb&&Mb!=l){if(Mb.getAttribute(ub.Ib)==ub.Lb)if(!jsx3.gui.isMouseEventModKey(a)&&!a.shiftKey()){var
wb=sb.row.getAttribute(ub.nb);var
ia=this.getSelectedIds();var
qa=ia.length==1&&wb==ia[0]?false:a;this.deselectAllRecords();this.LX(qa,wb,sb.row,false,sb.cell?sb.cell.cellIndex:null);return;}Mb=Mb.parentNode;}};q.Qt=function(c,j,i){var
Hb=this.DC(j);var
eb=this.mI(Hb.previousSibling).getAttribute(ub.nb);var
Gb=this.getRecordNode(eb);if(!Gb.selectSingleNode(ub.Mb))return;if(i==null)i=false;if(j.nodeType==3)j=j.parentNode;var
Mb=j.getAttribute(ub.Ib);if(Hb.style.display==ub.x||i){i=true;Gb.setAttribute(ub.Nb,ub.lb);Hb.style.display=ub.H;if(this.getRenderNavigators(1)!=0)j.style.backgroundImage=ub.tb+this.getUriResolver().resolveURI(this.getIconMinus(m.ICON_MINUS))+ub.Y;if(this.GZ(Hb)){Qa.trace(ub.Ob+eb);var
Oa={};Oa.jsx_panel_css=ub.Pb;Oa.jsx_column_widths=this.x8();Oa.jsx_rendering_context=eb;Oa.jsx_context_index=Hb.getAttribute(ub.Qb);Hb.innerHTML=this.doTransform(Oa);if(this.getRenderNavigators(1)!=0)j.setAttribute(ub.Ib,ub.Jb);var
ua={painted:1,token:m.getToken(),contextnodes:Hb.childNodes};this.Sv()[0]=ua;this.TA(ua);}}else{Gb.removeAttribute(ub.Nb);Hb.style.display=ub.x;if(this.getRenderNavigators(1)!=0)j.style.backgroundImage=ub.tb+this.getUriResolver().resolveURI(this.getIconMinus(m.ICON_PLUS))+ub.Y;}this.Tw();if(c)this.doEvent(ub.Rb,{objEVENT:c,strRECORDID:eb,objRECORD:Gb,bOPEN:i});};q.toggleItem=function(c,k){var
z=this.jO(c,0);if(z!=null){while(z&&z.getAttribute&&z.getAttribute(ub.Ib)!=ub.Jb&&z.getAttribute(ub.Ib)!=ub.Kb)z=z.childNodes[0];this.Qt(false,z,k);}};q.revealRecord=function(e){var
Ka=this.getRecordNode(e);if(Ka){if(this.getRenderingModel()==m.REND_HIER){var
Ia=[];do
Ia.push(Ka.getAttribute(ub.nb));while((Ka=Ka.getParent())!=null&&Ka.getNodeName()==ub.Lb);for(var
x=Ia.length-1;x>=0;x--)this.toggleItem(Ia[x],true);}this.synchronizeVScroller();var
Gb=this.jO(e,0);if(Gb){this._scrollIntoView(Gb);}else if(this.DJ(e))jsx3.sleep(function(){jsx3.sleep(function(){var
Gb=this.jO(e,0);if(Gb)this._scrollIntoView(Gb);},ub.Sb,this);},ub.Sb,this);}};q.DC=function(o){while(!o.tagName||o&&o.tagName&&o.tagName.toLowerCase()!=ub.B||o.id==ub.H)o=o.parentNode;return o.nextSibling;};q.getDragIcon=function(o,a,i,f){var
Z=jsx3.EventHelp.DRAGIDS;var
Oa=ub.H;var
ca=o.id;var
Sa=0.4;var
mb=o.getAttribute(ub.nb);if(a.Pk(mb)&&jsx3.util.arrIndexOf(Z,mb)==-1)Z.push(mb);for(var
Q=0;Q<Z.length&&Q<4;Q++){var
B=a.TV(Z[Q]);if(B)Oa=Oa+a.TU(B,Sa);Sa=Sa-0.1;}return Oa;};q.TU=function(c,b){var
Ib=c;while(Ib.tagName.toLowerCase()!=ub.B)Ib=Ib.parentNode;return ub.Tb+jsx3.html.getCSSOpacity(b)+ub.Ub+ub.Vb+Ib.getAttribute(ub.Wb)+ub.Xb+Ib.getAttribute(ub.Yb)+ub.Ub+ub.Zb+c.getAttribute(ub.Wb)+ub.Xb+c.getAttribute(ub.Yb)+ub.Ub+c.innerHTML+ub._b;};if(jsx3.CLASS_LOADER.IE)q.HZ=function(h,e){var
jb=this.getSelectionModel()!=0;if(jb)h.cancelAll();};q.Dc=function(e,s){var
S=true;this.wF(e);if(e.leftButton()){var
Ga=e.srcElement();var
nb=this.sE(Ga);if(nb==null)return;if(nb){s=nb.cell;if(this.dV()!=s.id){s.focus();}else{this.U5();this.N9(e,s);}if(this.getCanDrag()==1&&this.getSelectionModel(1)>0){var
Pa=this.getSelectedIds();var
jb=nb.row.getAttribute(ub.nb);var
Ab=jsx3.util.List.wrap(Pa);if(Ab.indexOf(jb)==-1)Pa=[jb];if(this.Pk(jb)&&jsx3.util.arrIndexOf(Pa,jb)==-1)Pa.push(jb);this.doDrag(e,nb.row,this.getDragIcon,{strDRAGIDS:Pa});S=true;}else S=this._jsxOM!=null;}}if(S){gb.publish(e);e.cancelAll();}};q.el=function(o,k){var
Eb=o.toElement();if(!Eb)return;var
Hb=Eb.getAttribute(ub.Ib);var
Ea=this.sE(Eb);if(!Ea)return;var
Ab=Ea.row.getAttribute(ub.nb);k=this.Jr(Ea.row);if(jsx3.EventHelp.isDragging()&&this.getCanDrop()==1&&jsx3.EventHelp.getDragIds()[0]!=null){if(Hb==ub.Jb&&this.getRecordNode(Ab).getAttribute(ub.Nb)!=ub.lb||Hb==ub.Kb){var
Ja=this;o.sf();m.TOGGLETIMEOUT=window.setTimeout(function(){if(Ja.getParent()!=null)Ja.Qt(o,Eb);},m.NE);}var
cb=this.doEvent(ub.ac,{objEVENT:o,strRECORDID:Ab,objSOURCE:jsx3.EventHelp.getDragSource(),strDRAGIDS:jsx3.EventHelp.getDragIds(),strDRAGTYPE:jsx3.EventHelp.getDragType(),objGUI:k});if(!(cb===false)){var
Ia=this.getRendered(o);var
U=this.getAbsolutePosition(Ia,Ea.row);var
Z=this.yR(Ia);var
xb=this.getRenderingModel()==m.REND_HIER&&this.getRenderNavigators(1)!=0?parseInt(Ea.row.childNodes[0].childNodes[0].childNodes[0].getAttribute(ub.D)):4;if(this.getRenderingModel()!=m.REND_HIER||U.H/3>o.getOffsetY()){Z.style.top=U.T-4+ub.A;Z.style.width=this.nW()-xb-8+ub.A;Z.style.height=ub.bc;Z.style.backgroundImage=ub.tb+m.INSERT_BEFORE_IMG+ub.Y;Z.setAttribute(ub.cc,ub.dc);}else{xb=xb+26;Z.style.width=ub.ec;Z.style.height=ub.ec;Z.style.top=U.T-10+U.H+ub.A;Z.style.backgroundImage=ub.tb+m.APPEND_IMG+ub.Y;Z.setAttribute(ub.cc,ub.fc);}Z.style.left=xb+ub.A;Z.setAttribute(ub.gc,Ab);Z.style.display=ub.y;}}else if(this.getEvent(ub.hc)){this.applySpyStyle(Eb);var
Lb=o.clientX()+jsx3.EventHelp.DEFAULTSPYLEFTOFFSET;var
ba=o.clientY()+jsx3.EventHelp.DEFAULTSPYTOPOFFSET;o.sf();var
Ja=this;var
L=this.Xi()[Ea.cell.cellIndex];if(m.SPYTIMEOUT)window.clearTimeout(m.SPYTIMEOUT);m.SPYTIMEOUT=window.setTimeout(function(){m.SPYTIMEOUT=null;if(Ja.getParent()!=null)Ja.tF(o,Ab,L,Eb);},jsx3.EventHelp.SPYDELAY);}};q.tF=function(f,a,h,c){this.removeSpyStyle(c);var
V=this.doEvent(ub.hc,{objEVENT:f,objCOLUMN:h,strRECORDID:a});if(V)this.showSpy(V,f);};q.aV=function(j,o){this.XF(o.parentNode);if(jsx3.EventHelp.isDragging()&&this.getCanDrop()==1&&jsx3.EventHelp.getDragIds()[0]!=null){var
za=this;this._jsxMK={offsety:j.getOffsetY(),offsetheight:o.offsetHeight,scrollheight:o.scrollHeight};this._jsxMK.interval=window.setInterval(function(){za.VX();},m.AUTO_SCROLL_INTERVAL);}};q.VX=function(){if(this._jsxMK.offsety<this._jsxMK.offsetheight/2){if(this.getScrollTop()>0)this.setScrollTop(this.getScrollTop()-20);}else if(this.getScrollTop()<this._jsxMK.scrollheight)this.setScrollTop(this.getScrollTop()+20);};q.gN=function(j,c){if(this._jsxMK){window.clearInterval(this._jsxMK.interval);delete this[ub.ic];}};q.au=function(r,n){if(jsx3.EventHelp.isDragging()&&this.getCanDrop()==1&&jsx3.EventHelp.getDragIds()[0]!=null&&this._jsxMK){this._jsxMK.offsety=r.getOffsetY();this._jsxMK.offsetheight=n.offsetHeight;this._jsxMK.scrollheight=n.scrollHeight;}};q._ebMouseOutDropIcon=function(c,j){if(!c.isFakeOut(j.parentNode.childNodes[1]))this.XF(j.parentNode);};q.Qi=function(d,i){var
Hb=d.fromElement();if(d.isFakeOut(i))this.XF(i.parentNode);if(!jsx3.EventHelp.isDragging()&&this.getEvent(ub.hc)){var
S=d.toElement();var
ja=false;try{ja=!S||S.className!=ub.jc;}catch(Kb){ja=true;}if(ja){jsx3.sleep(La.hideSpy);this.removeSpyStyle(Hb);if(m.SPYTIMEOUT)window.clearTimeout(m.SPYTIMEOUT);}}if(Hb==null||d.isFakeOut(i.parentNode)&&Hb.getAttribute(ub.Ib)!=ub.Jb)return;var
db=Hb.getAttribute(ub.Ib);var
Db=this.sE(Hb);if(!Db)return;var
na=Db.row.getAttribute(ub.nb);i=this.Jr(Db.row);if(jsx3.EventHelp.isDragging()&&this.getCanDrop()==1){if(db==ub.Jb)window.clearTimeout(m.TOGGLETIMEOUT);var
O=this.doEvent(ub.kc,{objEVENT:d,strRECORDID:na,objSOURCE:jsx3.EventHelp.getDragSource(),strDRAGIDS:jsx3.EventHelp.getDragIds(),strDRAGTYPE:jsx3.EventHelp.getDragType(),objGUI:i});this.XF(this.getRendered(d));}};q.sE=function(e){var
Aa=e;var
xa=null;while(Aa.getAttribute(ub.Ib)!=ub.Lb){xa=Aa;Aa=Aa.parentNode;if(!Aa.tagName||Aa.tagName.toLowerCase()==ub.lc||Aa.id==this.getId())return null;}return {row:Aa,cell:xa};};q.Jr=function(j){if(this.getRenderingModel()==ub.mc){j=this.DC(j).parentNode;}else if(this.getPagingModel(0)!=3)while(j.tagName.toLowerCase()!=ub.B)j=j.parentNode;return j;};q.Df=function(b,k){};q.lk=function(b,k){var
ua=b.getWheelDelta();var
H=this.vD();var
K=this.getScrollTop();K=Math.max(0,Math.min(K-ua*m.SCROLL_INC,H.offsetHeight));this.collapseEditSession(b,k);this.setScrollTop(K,k);};q._isDescendantOrSelf=function(g,f){while(g&&f){if(g.equals(f))return true;g=g.getParent();}return false;};q._onMouseUp=function(p,i){var
Sa=p.srcElement()&&p.srcElement().className==ub.nc?this.TV(p.srcElement().getAttribute(ub.gc)).childNodes[0]:p.srcElement();var
Mb=this.sE(Sa);if(this.getCanDrop()==1&&jsx3.EventHelp.isDragging()){if(jsx3.EventHelp.getDragType()==ub.oc){jsx3.sleep(function(){this.XF();},null,this);var
oa=jsx3.EventHelp.getDragSource();if(oa&&oa.instanceOf(jsx3.xml.CDF)){var
Ea=jsx3.gui.isMouseEventModKey(p);var
Ma=oa.doEvent(ub.pc,{objEVENT:p,strRECORDID:jsx3.EventHelp.getDragId(),strRECORDIDS:jsx3.EventHelp.getDragIds(),objTARGET:this,bCONTROL:Ea});var
x=this.yR(this.getRendered(p));var
V=x.getAttribute(ub.cc)==ub.dc;var
Nb=Mb?Mb.row.getAttribute(ub.nb):null;var
sb={objEVENT:p,objSOURCE:oa,strDRAGIDS:jsx3.EventHelp.getDragIds(),strDRAGTYPE:jsx3.EventHelp.getDragType(),strDRAGID:jsx3.EventHelp.getDragId(),strRECORDID:Nb,bINSERTBEFORE:V,objCOLUMN:Mb!=null?this.Xi()[Mb.cell.cellIndex]:null,bALLOWADOPT:Ma!==false};var
Ja=this.doEvent(Ea?ub.qc:ub.rc,sb);if(Ma!==false&&Ja!==false){var
hb=jsx3.EventHelp.getDragIds();for(var
u=0;u<hb.length;u++)if(!(this==oa&&Mb&&this._isDescendantOrSelf(this.getRecordNode(Mb.row.getAttribute(ub.nb)),this.getRecordNode(hb[u])))){oa.deleteRecordProperty(hb[u],ub.rb,false);var
P;if(V){P=true;var
ma=this.adoptRecordBefore(oa,hb[u],Nb);}else{P=Mb!=null;var
ma=this.adoptRecord(oa,hb[u],this.getRenderingModel()==m.REND_HIER&&Mb?Mb.row.getAttribute(ub.nb):null,Mb!=null);}}if(!P&&!Mb)this.repaint();}}}jsx3.EventHelp.reset();}else if(p.rightButton()){var
da=this.getMenu();if(da){var
Ga=this.getServer().getJSX(da);if(Ga!=null){var
vb,U;if(Mb){vb=this.Xi()[Mb.cell.cellIndex];U=Mb.row.getAttribute(ub.nb);}var
Cb={objEVENT:p,objMENU:Ga,strRECORDID:U,objCOLUMN:vb};var
K=this.doEvent(ub._a,Cb);if(K!==false){if(K instanceof Object&&K.objMENU instanceof jsx3.gui.Menu)Ga=K.objMENU;Ga.showContextMenu(p,this,U);}}}}};q.wF=function(h){this._jsxhU={ctrl:jsx3.gui.isMouseEventModKey(h),shift:h.shiftKey(),alt:h.altKey()};};q.I3=function(){return this._jsxhU!=null?this._jsxhU:{};};q.oe=function(s,j){if(this.jsxsupermix(s,j))return;var
G=s.keyCode();var
bb=s.hasModifier(true);var
Ab=G==9&&!bb;var
Cb=this.getSelectionModel(1);this.wF(s);if(this.dV()==null){var
wa=this.qt();if(wa){this.CD(wa.id);}else return;}var
Bb=this.hL();var
V=this.MQ()&&Bb&&this.CM(Bb.parentNode);var
Ia=false;if(V&&(G==13||G==40||G==38||Bb.parentNode.lastChild==Bb&&(Ab&&!s.shiftKey()||G==39)||Bb.parentNode.firstChild==Bb&&(Ab&&s.shiftKey()||G==37))){var
U=Bb.cellIndex;if(G==13){this.oL(s);var
Xa=Bb.parentNode;if(!Xa)Xa=this.TV(ub.wb);if(Xa&&Xa.childNodes[U])Xa.childNodes[U].focus();}else if(this.MQ()==2&&G==40){var
ob=this.nI();if(ob){var
qa=this.mI(ob);if(qa)qa.childNodes[U].focus();}}else if(this.MQ()==1&&G==38){var
ob=this.gS();if(ob){var
qa=this.uL(ob);if(qa)qa.childNodes[U].focus();}}else if(Bb.parentNode.lastChild==Bb&&(Ab&&!s.shiftKey()||G==39)){Bb.parentNode.firstChild.focus();}else if(Bb.parentNode.firstChild==Bb&&(Ab&&s.shiftKey()||G==37))Bb.parentNode.lastChild.focus();Ia=true;}else if(Bb){var
U=Bb.cellIndex;var
L=Bb.parentNode.getAttribute(ub.nb);if(G==38||G==13&&s.shiftKey()){var
za=this.P2(ub.sc,Bb,true,U);Ia=this.OB(za);}else if(G==40||G==13&&!s.shiftKey()&&Cb==0){var
za=this.P2(ub.tc,Bb,true,U);Ia=this.OB(za);}else if(G==37||Ab&&s.shiftKey()){if(this.getRenderingModel()==m.REND_HIER&&U==0&&this.getSuppressVScroller()!=1){var
fa=Bb.parentNode.getAttribute(ub.nb);var
S=this.getRecordNode(fa);var
ha=S.getAttribute(ub.Nb);if(ha==1&&S.selectSingleNode(ub.Lb)){var
qb=this.tX(Bb);this.Qt(s,qb,false);Ia=true;}else{var
za=this.P2(ub.uc,Bb,true,U);Ia=this.OB(za);}}if(!Ia){var
za=this.P2(ub.uc,Bb,true,U);Ia=this.OB(za);}}else if(G==39||Ab&&!s.shiftKey()){if(this.getRenderingModel()==m.REND_HIER&&U==0&&this.getSuppressVScroller()!=1){var
fa=Bb.parentNode.getAttribute(ub.nb);var
S=this.getRecordNode(fa);var
ha=S.getAttribute(ub.Nb);if(ha!=1&&(S.getAttribute(ub.vc)==ub.lb||S.selectSingleNode(ub.Lb))){var
qb=this.tX(Bb);this.Qt(s,qb,true);Ia=true;}else{var
za=this.P2(ub.wc,Bb,true,U);Ia=this.OB(za);}}if(!Ia){var
za=this.P2(ub.wc,Bb,true,U);Ia=this.OB(za);}}else if(G==13){this.u3(s);Ia=true;}else if(Ab&&s.shiftKey()){this.focus();Ia=true;}else if(Ab){this.getRendered(s).lastChild.focus();Ia=true;}}if(Ia)s.cancelAll();};q.OB=function(a){if(a){jsx3.sleep(function(){try{a.focus();}catch(Kb){}});return true;}};q.tX=function(n){var
J=n.childNodes[0].childNodes[0];var
Ta=this.mI(J);if(Ta)return Ta.childNodes[0];};q.iC=function(){return this._jsxP9;};q.U5=function(i){this._jsxP9=i;};q.P2=function(j,s,c,d){var
ga=this.getSelectionModel(1);if(j==ub.wc){this.U5();if(s.parentNode.lastChild!=s){return s.nextSibling;}else if(s.parentNode.lastChild==s&&ga>0){return s.parentNode.firstChild;}else{j=ub.tc;s=s.parentNode.firstChild;d=0;}}else if(j==ub.uc){this.U5();if(s.parentNode.firstChild!=s){return s.previousSibling;}else if(s.parentNode.firstChild==s&&ga>0){return s.parentNode.lastChild;}else{j=ub.sc;s=s.parentNode.lastChild;d=s.cellIndex;}}var
Ra=this.AL(j,s.parentNode,c);if(Ra){if(this.iC()&&(j==ub.sc||j==ub.tc)&&Ra.childNodes.length>1){d=this.iC();this.U5();}var
Kb=Ra.childNodes[d];if(Kb){return Kb;}else{this.U5(d);return Ra.childNodes[0];}}else return null;};q.AL=function(h,b,a){if(h==ub.uc)h=ub.sc;else if(h==ub.wc)h=ub.tc;if(this.getRenderingModel()==m.REND_HIER)return this.XZ(h,b);if(h==ub.sc&&b.previousSibling&&b.previousSibling.tagName.toLowerCase()!=ub.xc){return b.previousSibling;}else if(h==ub.tc&&b.nextSibling){return b.nextSibling;}else{var
Ha=this.F7(this.Gz(b));var
F;var
ia=this.getRenderingModel()==m.REND_HIER?this.vR():this.Sv().length;if(h==ub.sc&&Ha==0||h==ub.tc&&Ha==ia-1){if(h==ub.sc&&this.MQ()==2||h==ub.tc&&this.MQ()==1)return this.mI(this.IT());else return null;}else if(h==ub.sc&&(F=this.HA(Ha-1))!=null){var
N=this.getRenderingModel()==m.REND_HIER?1:this.getRowsPerPanel(m.DEFAULT_ROWS_PER_PANEL);return this.uL(F);}else if(h==ub.tc&&(F=this.HA(Ha+1))!=null)return this.mI(F);}return null;};q.XZ=function(r,k){var
Ca=k;if(r==ub.sc){while(Ca.tagName.toLowerCase()!=ub.B)Ca=Ca.parentNode;var
za=this.zu(Ca.parentNode.previousSibling);if(za)return za;var
P=Ca.parentNode.parentNode.previousSibling;return this.mI(P);}else if(r==ub.tc){while(Ca.tagName.toLowerCase()!=ub.B)Ca=Ca.parentNode;var
Wa=Ca.nextSibling;if(Wa&&Wa.style.display.toLowerCase()!=ub.x){var
va=Wa.childNodes[0].childNodes[0];return this.mI(va);}Wa=Ca.parentNode.nextSibling;if(Wa){var
va=Wa.childNodes[0];return this.mI(va);}return this.f4(Ca.parentNode);}return null;};q.zu=function(c){if(c){var
ua=c.childNodes[1];if(ua&&ua.style.display.toLowerCase()!=ub.x&&ua.childNodes.length){var
Ma=ua.lastChild;ua=Ma.childNodes[1];if(ua&&ua.style.display.toLowerCase()!=ub.x&&ua.childNodes.length){return this.zu(Ma);}else return this.mI(Ma.childNodes[0]);}return this.mI(c.childNodes[0]);}return null;};q.f4=function(d){var
Pa=d.parentNode.parentNode.nextSibling;if(Pa){return this.mI(Pa.childNodes[0]);}else{var
Ga=d.parentNode.parentNode;if(Ga)return this.f4(Ga);}return null;};q._scrollIntoView=function(p){var
ia=this.getRendered(p);if(ia){var
db=jsx3.html.getRelativePosition(this.vD(ia),p);var
Ga=this.getScrollTop();var
ab=db.T;var
D=yb.getScrollSize();var
ba=ia.childNodes[3].style.display==ub.x?0:D;var
Ra=parseInt(this.Wl(true).lg(1).ec()-ba+1);if(!(ab>Ga&&ab+db.H<Ga+Ra)){var
tb=Math.abs(ab-Ga);var
vb=Math.abs(ab-(Ga+Ra)+db.H+1);if(vb<tb){if(vb==0)vb=db.H;this.setScrollTop(Ga+vb);}else this.setScrollTop(ab-(D+1));}if(this.getScaleWidth()!=1){var
Za=this.getScrollLeft();var
F=db.L;var
Wa=parseInt(this.Wl(true).lg(1).mn()-D+1);if(!(F>Za&&F+db.W<Za+(Wa-(D+1)))){var
nb=Math.abs(F-Za);var
Bb=Math.abs(F-(Za+Wa));if(Bb<nb){this.setScrollLeft(F);}else this.setScrollLeft(F-(D+1));}}}};q.PV=function(p){var
W=[];var
Fb=0;var
ma=this.Wl().lg(0);var
Va=ma.ec();var
Da=this.oC();for(var
zb=0;zb<p.length;zb++){var
ba=p[zb].Wl();Fb=Fb+ba._b();var
ja=this.getResizable()!=0&&zb<p.length-1&&p[zb].getResizable()!=0;if(ja){var
y=this.Gj(ub.da,ub.yc,3)+this.Gj(ub.ba,ub.zc,3);var
Pa=ub.H;}else{var
y=ub.H;var
Pa=ub.Ac;}W.push(ub.Bc+zb+ub.Cc+(Fb-4)+ub.Dc+Pa+ub.Ec+4+ub.Fc+Ya.SPACE+ub.Gc+Va+ub.Hc+y+ub.Ja);}return W.join(ub.H);};q.dq=function(n,b){if(!n.leftButton())return;gb.publish(n);this.endEditSession();var
na=jsx3.util.arrIndexOf(this.getChildren(),this.Xi()[Number(b.getAttribute(ub.Ic))]);this.st(na);if(typeof this._jsxZ2==ub.G&&(new
Date()).valueOf()-this._jsxZ2.timestamp<200)return;var
va=this.doEvent(ub.Jc,{objEVENT:n,intCOLUMNINDEX:na});if(!(va===false)){var
O=this.WQ();var
I=parseInt(b.style.left)-this.getScrollLeft();this._jsxresizeorigin={origin:I};O.style.left=I+ub.A;La.Bn(n,O,function(i,p){return [i,0];});gb.subscribe(ub.fa,this,ub.Kc);}this._jsxZ2={timestamp:(new
Date()).valueOf()};n.cancelAll();};q.vG=function(j,r){if(!j.leftButton())return;gb.publish(j);var
ea=this.LK(ea);Qa.trace(ub.Lc+ea);j.cancelAll();};q.LK=function(){return this._jsxp4;};q.st=function(i){this._jsxp4=i;};q.WQ=function(){return this.getRendered().childNodes[6];};q.yR=function(c){if(!c)c=this.getRendered();return c.childNodes[7];};q.XF=function(p){var
W=this.yR(p);W.style.display=ub.x;W.removeAttribute(ub.cc);W.removeAttribute(ub.gc);};q.Xy=function(p){jsx3.EventHelp.reset();gb.unsubscribe(ub.fa,this,ub.Kc);if(parseInt(this.WQ().style.left)!=this._jsxresizeorigin.origin){var
ca=this.vw();var
Oa=jsx3.util.arrIndexOf(this.getChildren(),this.Xi()[this.LK()]);var
E=this.doEvent(ub.Mc,{objEVENT:p,vntWIDTH:ca,intCOLUMNINDEX:Oa});if(!(E===false))this.getChild(this.LK()).setWidth(ca,true);this.i6();}this.WQ().style.left=ub.Nc;};q.vw=function(){var
z=this.WQ();var
wa=parseInt(z.style.left);var
Fb=this.Xi();var
pa=this.oC();var
lb=this.getChild(this.LK()).getDisplayIndex();for(var
C=0;C<lb;C++)wa=wa-pa[C];wa=wa+this.getScrollLeft();return wa<8?8:wa;};q.getResizable=function(){return this.jsxresize;};q.setResizable=function(j){this.jsxresize=j;};q.dR=function(){this.ls();var
ka=Math.max(1,Math.ceil(this.vR(true)/this.getRowsPerPanel(m.DEFAULT_ROWS_PER_PANEL)));this.aT(new
Array(ka));};q.Pp=function(j,h){if(this.getParent()==null)return;this.n0();this.endEditSession();this.U5();this.T2();this.CD();this.dR();var
T=this.Sv().length;var
aa=this.getPagingModel(0);if(aa==0||aa==4){var
_={painted:1,token:m.getToken(),index:0};if(this.getRenderingModel()==m.REND_HIER)_.contextnodes=this.vD().childNodes;this.Sv()[0]=_;this.TA(_,true);if(this.MQ()){var
zb={painted:1,token:m.getToken(),index:-1};this.TA(zb,true);}}else{this.vD().innerHTML=ub.H;if(aa==3){if(this.xL(this.Sv().length-1))this.jB().unshift({index:T-1});if(this.xL(0))this.jB().unshift({index:0});this.X1();}else if(aa==2){for(var
tb=0;tb<T;tb++)this.jB().push({index:tb});if(this.MQ()==2){this.jB().unshift({index:-1});}else if(this.MQ()==1)this.jB().push({index:-1});this.X1();}else if(aa==1){this.jB().unshift({index:0});if(this.MQ()==2){this.jB().unshift({index:-1});}else if(this.MQ()==1)this.jB().push({index:-1});this.X1();}if(!h){var
la=this.Vv(false);if(la){var
L=this.vD();if(L.lastChild)jsx3.html.insertAdjacentHTML(L.lastChild,ub.Ya,la);else L.innerHTML=la;}}}if(j!==false)this.i6();};q.MQ=function(){return this.getPagingModel()==3||this.getRenderingModel()==m.REND_HIER?0:this.getAutoRow();};q.getAutoRow=function(){return this.jsxautorow;};q.setAutoRow=function(f){this.jsxautorow=f;};q.zr=function(){if(this.getPagingModel()==3){var
sb=this.Sv();var
Ra=this.vD().childNodes.length;var
xb=this.getPanelPoolSize(m.DEFAULT_PANEL_POOL_COUNT);var
lb=Ra-xb;if(lb>0){Qa.trace(ub.Oc+xb+ub.Pc+lb);var
I=this.KO();var
Aa=sb.length;if(Aa/2>I){lb=this.ju(sb,Aa-1,I+1,lb,-1);if(lb<=0)return;lb=this.ju(sb,0,I-1,lb,1);if(lb<=0)return;}else{lb=this.ju(sb,0,I-1,lb,1);if(lb<=0)return;lb=this.ju(sb,Aa-1,I+1,lb,-1);if(lb<=0)return;}}}};q.ju=function(r,f,l,g,e){for(var
Aa=f;e==-1&&Aa>l||e==1&&Aa<l;Aa=Aa+e){if(r[Aa]!=null){r[Aa]=null;g--;Qa.trace(ub.Qc+Aa);var
z=this.HA(Aa);if(z)jsx3.html.removeNode(z);}if(g<=0)return 0;}return g;};q.nI=function(){var
I=this.vD();var
u=I.childNodes;for(var
Sa=0;Sa<u.length;Sa++)if(u[Sa].tagName.toLowerCase()==ub.B&&u[Sa].getAttribute(ub.wb)!=ub.Eb||this.getRenderingModel()==m.REND_HIER&&u[Sa].getAttribute(ub.Ib)==ub.Rc)return this.getRenderingModel()==m.REND_HIER?u[Sa].firstChild:u[Sa];};q.gS=function(){var
rb=this.vD();var
t=rb.childNodes;for(var
P=t.length-1;P>=0;P--)if(t[P].tagName.toLowerCase()==ub.B&&t[P].getAttribute(ub.wb)!=ub.Eb||this.getRenderingModel()==m.REND_HIER&&t[P].getAttribute(ub.Ib)==ub.Rc)return this.getRenderingModel()==m.REND_HIER?t[P].firstChild:t[P];};q.IT=function(){return this.HA(-1);};q.HA=function(f){var
Lb=this.getDocument();return Lb.getElementById(this.getId()+ub.Sc+f);};q.F7=function(g){return parseInt((g.id+ub.H).replace(this.getId()+ub.Sc,ub.H));};q.Gz=function(g){if(g.parentNode.tagName.toLowerCase()==ub.B)return g.parentNode;return g.parentNode.parentNode;};q.AP=function(o,c){if(!isNaN(o))o=this.HA(o);if(o){var
db=0;for(var
wb=0;wb<o.childNodes.length;wb++)if(o.childNodes[wb].tagName.toLowerCase()==ub.Tc){return o.childNodes[wb].childNodes[c];}else if(o.childNodes[wb].tagName.toLowerCase()==ub.U){return o.childNodes[c+db];}else db++;}return null;};q.TV=function(h){var
Ba=this.getId()+ub.Uc+h;var
L=this.getDocument();return L.getElementById(Ba);};q.WT=function(p){var
O=this.TV(p);return O?this.Gz(this.TV(p)).parentNode:null;};q.Fq=function(b,l){var
Fb=this.Xi();for(var
F=0;F<Fb.length;F++)if(Fb[F].getPath()==l){var
Ra=this.getId()+ub.Uc+b+ub.Uc+F;var
R=this.getDocument();return R.getElementById(Ra);}return null;};q.jO=function(f,c){var
ha=this.TV(f);return ha?ha.childNodes[c]:null;};q.qt=function(){var
Da=this.AP(0,0);return Da?Da.childNodes[0]:null;};q.mI=function(r){return this.AP(r,0);};q.uL=function(n){var
la=this.AP(n,0);return la?la.parentNode.lastChild:null;};q.xD=function(e,s){this.collapseEditSession(e,s);};q.Jt=function(s,j){this.collapseEditSession(s,j);};q.FS=function(h,e){var
z=e.parentNode;var
F=z.childNodes[0].childNodes[0];var
M=z.childNodes[1].childNodes[0];var
t=e.scrollLeft;z.childNodes[1].scrollLeft=0;F.style.left=ub.Vc+t+ub.A;M.style.left=ub.Vc+t+ub.A;if(h)this.doEvent(ub.Wc,{objEVENT:h,strDIRECTION:ub.Xc,intPOSITION:t});};q.RI=function(d,i){var
ra=this.vD(i.parentNode);ra.parentNode.scrollTop=0;this._jsxDY=i.scrollTop;ra.style.top=ub.Vc+this._jsxDY+ub.A;var
ta=this.KO();if(this.getPagingModel(0)==3){var
v=this.getScrollInfoLabel(this.wi(ub.Yc));if(v!=ub.H){this.B3(i.parentNode).style.display=ub.y;window.clearTimeout(this._jsxo4);var
Ja=this;this._jsxo4=window.setTimeout(function(){if(i&&i.parentNode)Ja.B3(i.parentNode).style.display=ub.x;},1000);jsx3.sleep(function(){if(this.getParent()==null)return;if(i&&i.parentNode){var
x=this.getRowHeight(m.DEFAULT_ROW_HEIGHT);var
Kb=parseInt(this._jsxDY/x)+1;var
Na=this.Wl(true).ec();var
oa=this.vR();var
Ba=Kb+parseInt(Na/x)-1;if(Ba>oa)Ba=oa;var
P=new
jsx3.util.MessageFormat(v);this.B3(i.parentNode).childNodes[0].innerHTML=P.format(Kb,Ba,oa);}},ub.Zc+this.getId(),this);}var
Xa;var
Ha=this.getPanelQueueSize(m.DEFAULT_PANEL_QUEUE_SIZE);var
D=parseInt(Ha/2);var
M=Ha-D;for(var
Bb=ta+M;Bb>=ta-D;Bb--)if(this.xL(Bb)!=null){this.jB().unshift({index:Bb});if(this.jB().length>Ha)var
qb=this.jB().pop();Xa=true;}if(Xa)this.X1(ra);}this.doEvent(ub.Wc,{objEVENT:d,strDIRECTION:ub._c,intPOSITION:this._jsxDY});};q.B3=function(j){return j.childNodes[5];};q.KO=function(){return parseInt(this._jsxDY/(this.getRowsPerPanel(m.DEFAULT_ROWS_PER_PANEL)*this.getRowHeight(m.DEFAULT_ROW_HEIGHT)));};q.X1=function(p){jsx3.sleep(function(){if(this.getParent()==null)return;if(this.jB().length){var
aa=this.jB().shift();if(this.xL(aa.index))this.RG(this.Q8(aa.index),p,aa.index);if(this.jB().length)this.X1(p);}},ub.ad+this.getId(),this);};q.jB=function(){return this._jsxhH;};q.ls=function(){this._jsxhH=[];};q.RG=function(n,h,p){if(!h)h=this.vD();if(h){var
C={index:p,painted:1,token:m.getToken()};this.Sv()[p]=C;Qa.trace(ub.bd+p);jsx3.html.insertAdjacentHTML(h,ub.Ya,n);this.TA(C);this.Tw(h);var
Ja=this;window.setTimeout(function(){if(Ja.getParent()==null)return;Ja.zr();},this.getReaperInterval(m.DEFAULT_REAPER_INTERVAL));}};q.getIterableRows=function(){var
O,M;var
Ba=[];M=this.getRendered();if(M)if(this.getRenderingModel()==m.REND_HIER){var
Jb=this.getRenderingContext(ub.Cb);var
t=this.getRecordNode(Jb);var
Ga=[];for(var
V=t.selectNodeIterator(ub.Mb);V.hasNext();){var
eb=V.next();var
pb=eb.getAttribute(ub.nb);Ga.push(this.WT(pb));}Ba=this._K({contextnodes:Ga});}else{var
ra=M.childNodes[1].childNodes[0].childNodes;var
sa;for(var
V=0;V<ra.length;V++){sa=ra[V];sa=this.mI(sa);if(sa){sa=sa.parentNode;var
Pa=sa.childNodes.length;for(var
T=0;T<Pa;T++){var
za=sa.childNodes[T];if(za.tagName.toLowerCase()==ub.U)Ba.push(za);}}}}return Ba;};q._K=function(r){var
H=[];if(r.contextnodes){for(var
qa=0;qa<r.contextnodes.length;qa++)if(r.contextnodes[qa].getAttribute(ub.Ib)==ub.Rc)H.push.apply(H,this.JA(r.contextnodes[qa]));}else{var
t=r.index;var
xb=this.HA(t);if(xb){xb=this.mI(xb);if(xb){xb=xb.parentNode;var
ea=xb.childNodes.length;for(var
Hb=0;Hb<ea;Hb++){var
R=xb.childNodes[Hb];if(R.tagName.toLowerCase()==ub.U)H.push(R);}}}}return H;};q.JA=function(r,a){if(a==null)a=[];a.push(this.mI(r.firstChild));if(r.lastChild){var
Z=r.lastChild.childNodes;for(var
Gb=0;Gb<Z.length;Gb++)if(Z[Gb].tagName)this.JA(Z[Gb],a);}return a;};m.RN=0;m.getToken=function(){m.RN+=1;return m.RN;};q.getContentElement=function(i,s){var
Ea=this.Fq(i,s);if(Ea)if(Ea.cellIndex==0&&this.getRenderingModel()==m.REND_HIER&&this.getRenderNavigators(1)!=0){var
pa=Ea.childNodes[0].childNodes[0];while(pa&&pa.tagName.toLowerCase()!=ub.U)pa=pa.childNodes[0];if(pa)return pa.lastChild.firstChild;}else return Ea.childNodes[0];};q.TA=function(d,f){var
P=new
jsx3.util.Timer(m.jsxclass,this);this._jsxF9=[];var
Cb=this._jsxF9;if(this.vR()==0&&!this.MQ())return;var
eb=this.getServer();var
ha=this.Xi();var
za=new
Array(ha.length);var
Fa=false;for(var
jb=0;jb<ha.length;jb++){var
ab=ha[jb].Rm();if(ab){za[jb]=ab;Fa=true;}}if(!Fa)return;var
tb=this._K(d);if(d.contextnodes){d.index=true;delete d[ub.cd];}var
pb=tb.length;var
U=this.getRenderingModel()==m.REND_HIER&&this.getRenderNavigators(1)!=0;var
D=[];for(var
jb=0;jb<ha.length;jb++){var
ab=za[jb];if(ab)D.push([jb,ab,ha[jb]]);}for(var
W=0;W<pb;W++){var
cb=tb[W];var
Fb=cb.getAttribute(ub.nb);var
Y=cb.getAttribute(ub.dd);for(var
ia=0;ia<D.length;ia++){var
J=null;var
jb=D[ia][0];var
ab=D[ia][1];var
X=D[ia][2];if(U&&jb==0){var
ra=cb.childNodes[0].childNodes[0].childNodes[0];while(ra&&ra.tagName.toLowerCase()!=ub.U)ra=ra.childNodes[0];if(ra)J=ra.lastChild.firstChild;}else if(cb.childNodes[jb])J=cb.childNodes[jb].childNodes[0];if(J)if(f){ab.format(J,Fb,this,X,Y,eb);}else Cb[Cb.length]=[ab,J,Fb,X,Y,d];}}if(D.length>0&&tb.length>0)jsx3.sleep(this.JF,ub.ed+this.getId(),this);P.log(ub.fd);};q.JF=function(){if(this.getParent()==null){this._jsxF9=[];return;}var
N=new
jsx3.util.Timer(m.jsxclass,this);var
Ra=this.getServer();var
ya=(new
Date()).getTime();var
Ib=ya;while(this._jsxF9.length>0&&Ib-ya<m.w5){var
cb=this._jsxF9.shift();var
Pa=cb[5];var
qb=Pa.index;if(!qb){var
M=this.Sv()[Pa.index];qb=M!=null&&M.token==Pa.token;}if(qb){cb[0].format(cb[1],cb[2],this,cb[3],cb[4],Ra);Ib=(new
Date()).getTime();}}if(this._jsxF9.length>0)jsx3.sleep(this.JF,ub.ed+this.getId(),this);N.log(ub.gd);};q.vD=function(e){if(!e)e=this.getRendered();return e?e.childNodes[1].childNodes[0]:null;};q.Xt=function(){var
Ha=this.getPagingModel(0);var
Aa=null;if(Ha==3){Aa=this.vR()*this.getRowHeight(m.DEFAULT_ROW_HEIGHT);}else{var
eb=this.vD();Aa=eb?parseInt(eb.offsetHeight):0;}return Aa;};q.xL=function(f){if(f==-1||f>=0&&f<this.Sv().length&&this.Sv()[f]==null){var
Lb=this.getDocument();var
Q=Lb.getElementById(this.getId()+ub.Sc+f);return !Q;}return false;};q.Q8=function(e){var
Ib=this.Wl(true);var
B=this.getRowsPerPanel(m.DEFAULT_ROWS_PER_PANEL);var
x=this.getPagingModel(0);if(x==3){var
Va=this.getRowHeight(m.DEFAULT_ROW_HEIGHT);var
za=ub.hd+B*Va*e+ub.Dc;var
K=B*e;var
Ba=K+B+1;}else{var
za=ub.Pb;var
fb=ub.H;if(x==2){var
K=B*e;var
Ba=K+B+1;}else{var
Ha=this.vR();var
K=0;var
Ba=Ha+1;}}var
va={};va.jsx_min_exclusive=K;va.jsx_max_exclusive=Ba;va.jsx_panel_index=e;va.jsx_panel_css=za;va.jsx_column_widths=this.x8();va.jsx_rendering_context=this.getRenderingContext(ub.Cb);va.jsx_mode=e==-1?ub.id:ub.jd;Qa.trace(ub.kd+K+ub.ld+Ba);return this.doTransform(va);};q.resetXmlCacheData=function(c){this.nj(true);this.jsxsupermix(c);};q.setXMLId=function(p){this.nj(true);return this.jsxsupermix(p);};q.repaint=function(){this.nj(true);return this.jsxsuper();};q.setXMLString=function(d){this.nj(true);return this.jsxsupermix(d);};q.setXMLURL=function(p){this.nj(true);return this.jsxsupermix(p);};q.getXSL=function(){return this.yk();};q.yk=function(g){var
K=new
jsx3.util.Timer(m.jsxclass,this);var
Ib=jsx3.getSharedCache().getOrOpenDocument(m.DEFAULTXSLURL,null,jsx3.xml.XslDocument.jsxclass);if(g)return Ib;var
ra=this.getServer().getCache();var
pa=ra.getDocument(this.getXSLId());if(pa==null){pa=Ib.cloneDocument();ra.setDocument(this.getXSLId(),pa);var
N=this.getRenderingModel(m.REND_DEEP);var
Ea=this.Xi();var
ha=pa.selectSingleNode(ub.md);var
M=pa.selectSingleNode(ub.nd);var
I=pa.selectSingleNode(ub.od);var
qa=this.x8();var
T=this.Fo()+this.Gj(ub.pd,ub.qd)+this.Gj(ub.rd,ub.sd);for(var
Ia=0;Ia<Ea.length;Ia++){var
Hb=Ea[Ia];var
z=Hb.getId();var
ab=Hb.Wl(true).lg(1);var
w=ab.lg(0);var
Ma=Ia==0&&N==m.REND_HIER?ub.td:ub.H;ab.setAttributes(T+Ma+ub.ud+Ia+ub.ya);ab.setStyles(Hb.Sd()+Hb.ag()+Hb.Mc()+Hb.Jk()+Hb.sd()+Hb.Uc()+Hb.eo()+ub.vd);w.setAttributes(ub.wd);w.setStyles(Hb.yo()+Hb.tf());var
Wa=m.UB.cloneDocument();Wa.setAttribute(ub.xd,ub.yd+z+ub.zd);Wa.selectSingleNode(ub.Ad).setAttribute(ub.Bd,z+ub.Cd);I.appendChild(Wa);if(N==m.REND_HIER&&Ia==0&&this.getRenderNavigators(1)!=0){var
Aa=m.yO.format(ub.Dd);var
Lb=pa.selectSingleNode(ub.Ed);Lb.setAttribute(ub.Bd,z+ub.Cd);var
u=w.paint().join(ub.H);Wa.loadXML(u);if(!Wa.hasError()){Lb.getParent().appendChild(Wa);Wa.appendChild(Lb);}else Qa.error(ub.Fd+Hb+ub.Gd+Wa.getError());}else var
Aa=m.yO.format(z+ub.Cd);var
Aa=ab.paint().join(w.paint().join(Aa)).replace(ub.Hd,ub.Id);var
Na=ab.cm();var
rb=Ia==0?qa-this.oC()[0]+Na:Na;var
lb=m.sy.format(z,Aa,String(Na),String(rb));Wa.loadXML(lb);if(!Wa.hasError()){pa.appendChild(Wa);}else Qa.error(ub.Fd+Hb+ub.Gd+Wa.getError());var
B=Hb.getValueTemplate(m.Column.TEMPLATES[ub.Jd]).replace(ub.Kd,ub.Ld+Hb.getPath());var
Z=Hb.getEditMask();if(Z!=null&&m.ZJ(Z)&&Z.emGetType()==m.EditMask.FORMAT){var
Ba=new
jsx3.xml.Document();B=B.replace(ub.Md,Z.emPaintTemplate().replace(ub.Kd,ub.Ld+Hb.getPath())+ub.Nd);}Wa.loadXML(B);if(!Wa.hasError()){Wa.setAttribute(ub.Bd,z+ub.Cd);pa.appendChild(Wa);}Wa=m.WZ.cloneNode(true);Wa.setAttribute(ub.Bd,z);if(Ia==0)ha.insertBefore(Wa,M.getParent());else M.appendChild(Wa);}}K.log(ub.Od);return pa;};m.WU=function(e){return e&&e.getDisplay()!=ub.x;};q.Xi=function(){return this.getChildren().filter(m.WU);};q.doTransform=function(f){if(!f)f={};f.jsx_id=this.getId();f.jsx_rendering_model=this.getRenderingModel(m.REND_DEEP);f.jsx_paging_model=this.getPagingModel(0);var
x=this.getUriResolver();if(f.jsx_rendering_model==ub.t){var
wa=this.getIcon(m.ICON),Ab=this.getIconMinus(m.ICON_MINUS),ra=this.getIconPlus(m.ICON_PLUS);if(f.jsx_icon==null)f.jsx_icon=wa?x.resolveURI(wa):ub.H;if(f.jsx_icon_minus==null)f.jsx_icon_minus=Ab?x.resolveURI(Ab):ub.H;if(f.jsx_icon_plus==null)f.jsx_icon_plus=ra?x.resolveURI(ra):ub.H;f.jsx_transparent_image=Ya.SPACE;}f.jsx_sort_path=this.getSortPath();f.jsx_sort_direction=this.getSortDirection();f.jsx_sort_type=this.getSortType();f.jsx_selection_model=this.getSelectionModel(1);f.jsx_selection_bg_url=this.hF();var
X=this.getXSLParams();for(var wb in X)f[wb]=X[wb];if(f.jsx_use_categories&&this.getRenderingModel()!=m.REND_HIER)delete f[ub.Pd];f.jsx_column_count=this.Xi().length;f.jsxpath=jsx3.getEnv(ub.Qd);f.jsxpathapps=jsx3.getEnv(ub.Rd);f.jsxpathprefix=this.getUriResolver().getUriPrefix();var
D=this.jsxsupermix(f);D=this.Al(D);return !f.jsx_return_at_all_costs&&D.indexOf(ub.Sd)==-1?ub.H:D;};q.onXmlBinding=function(i){this.jsxsupermix(i);this.repaintData();};q.nj=function(d){if(!this.getServer())return;delete this[ub.Td];if(!d){this.resetXslCacheData();this.ki(true);delete this[ub.v];}};q.getSortedIds=function(){var
_=this.doTransform({jsx_mode:ub.Ud,jsx_return_at_all_costs:true});return _.search(ub.Vd)>-1?window.eval(ub.Wd+RegExp.$1+ub.Xd):[];};q.vR=function(d){if(d)delete this[ub.Td];if(this._jsxBF){return this._jsxBF.maxlen;}else{var
J={};J.jsx_mode=ub.Yd;J.jsx_rendering_model=this.getRenderingModel(m.REND_DEEP);J.jsx_rendering_context=this.getRenderingContext(ub.Cb);var
S=this.yk(true);S.reset();S.setParams(J);var
kb=S.transform(this.getXML());var
Ja=kb.search(ub.Zd)>-1?parseInt(RegExp.$1):0;Qa.trace(ub._d+Ja);this._jsxBF={maxlen:Ja};if(this.getPagingModel()==3){var
xb=this.Wl();var
ya=this.getRendered();if(xb&&ya){xb=xb.lg(2).lg(0);var
u=this.getRowHeight(m.DEFAULT_ROW_HEIGHT)*Ja;xb.recalculate({height:u+this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT)},ya.childNodes[2].childNodes[0],null);}}return Ja;}};q.Sv=function(){return this._jsxcN;};q.aT=function(k){this._jsxcN=k;};q.getSelectionModel=function(n){return this.jsxselectionmodel!=null?this.jsxselectionmodel>2?0:this.jsxselectionmodel:n!=null?n:null;};q.setSelectionModel=function(k){this.jsxselectionmodel=k;};q.getRowHeight=function(s){return this.jsxrowheight!=null?this.jsxrowheight:s!=null?s:null;};q.setRowHeight=function(o,r){this.jsxrowheight=o;this.nj();if(!r)this.repaint();};q.getRowsPerPanel=function(e){return this.jsxrowsperpanel?this.jsxrowsperpanel:e?e:null;};q.setRowsPerPanel=function(n,k){this.jsxrowsperpanel=n;this.nj(true);if(!k)this.repaint();};q.getPanelQueueSize=function(n){return this.jsxpaintqueuesize?this.jsxpaintqueuesize:n?n:null;};q.setPanelQueueSize=function(p){this.jsxpaintqueuesize=p;};q.getReaperInterval=function(l){return this.jsxreaperinterval?this.jsxreaperinterval:l?l:null;};q.setReaperInterval=function(f){this.jsxreaperinterval=f;};q.getPanelPoolSize=function(l){return this.jsxpanelpoolsize?this.jsxpanelpoolsize:l?l:null;};q.setPanelPoolSize=function(d){this.jsxpanelpoolsize=d;this.nj(true);};q.getPagingModel=function(k){if(this.getRenderingModel()==m.REND_HIER&&this.jsxpagingmodel!=4){Qa.trace(ub.ae);return 0;}return !isNaN(this.jsxpagingmodel)?this.jsxpagingmodel:!isNaN(k)?k:null;};q.setPagingModel=function(j){this.jsxpagingmodel=j;this.nj();};q.getHeaderHeight=function(i){return this.jsxheaderheight!=null?Number(this.jsxheaderheight):i?i:null;};q.setHeaderHeight=function(g,a){this.jsxheaderheight=g;this.ki(true);if(!a)this.repaint();};q.getScrollInfoLabel=function(f){return this.jsxscrollinfolabel!=null?this.jsxscrollinfolabel:f?f:null;};q.setScrollInfoLabel=function(f){this.jsxscrollinfolabel=f;};q.getScrollLeft=function(){var
T=this.getRendered();return T?T.childNodes[3].scrollLeft:0;};q.setScrollLeft=function(b){var
va=this.getRendered();if(va&&va.childNodes[3]){va.childNodes[3].scrollLeft=b;if(va.childNodes[3].style.display==ub.x)this.FS(false,va.childNodes[3]);}};q.getScrollTop=function(){return this._jsxDY?this._jsxDY:0;};q.kt=function(){var
Za=this.getRendered();return Za&&Za.childNodes[2]?Za.childNodes[2].scrollTop:this.getScrollTop();};q.setScrollTop=function(h,s){if(h<0)h=0;s=this.getRendered(s);if(s&&s.childNodes[2]){if(s.childNodes[2].scrollTop==0&&h==0)s.childNodes[2].scrollTop=1;s.childNodes[2].scrollTop=h;}};q.synchronizeVScroller=function(){this.Tw();};q.Tw=function(g){if(!g)g=this.vD();if(!g)return;if(this.getPagingModel()!=3){var
W=this.Wl().lg(2).lg(0);W.recalculate({height:g.offsetHeight+this.getHeaderHeight(m.DEFAULT_HEADER_HEIGHT)},g.parentNode.parentNode.childNodes[2].childNodes[0],null);var
wb=this.kt();if(this.getScrollTop()!=wb){this.setScrollTop(wb);g.style.top=ub.Vc+wb+ub.A;}}if(g.parentNode.parentNode.childNodes[3].style.display==ub.x)this.setScrollLeft(0);this.dt();};q.dt=function(){var
Va=this.getRendered();if(Va)Va.childNodes[4].style.display=this.getSuppressVScroller(0)==1?ub.x:ub.H;};q.getScaleWidth=function(){return this.jsxscalewidth;};q.setScaleWidth=function(a){this.jsxscalewidth=a;this.ce();return this;};q.onSetChild=function(r){if(r instanceof m.Column){this.nj();return true;}return false;};q.paintChild=function(g,b){if(!b)this.repaint();};q.onRemoveChild=function(d,s){this.jsxsuper(d,s);this.nj();this.repaint();};q.getHeaderBorder=function(){return this.jsxheaderborder;};q.setHeaderBorder=function(j){this.jsxheaderborder=j;this.ki(true);this.repaintHead();};q.getBodyBorder=function(){return this.jsxbodyborder;};q.setBodyBorder=function(r,s){this.jsxbodyborder=r;this.nj();if(!s)this.repaint();};q.getValue=function(){var
y=this.getSelectionModel();if(y==2){return this.getSelectedIds();}else return this.getSelectedIds()[0];};q.doValidate=function(){var
Ma=this.getSelectedNodes().size()>0||this.getRequired()==0;this.setValidationState(Ma?1:0);return this.getValidationState();};q.getRenderingModel=function(f){return this.jsxrenderingmodel?this.jsxrenderingmodel:f?f:null;};q.setRenderingModel=function(c,h){this.jsxrenderingmodel=c;this.nj();if(!h)this.repaint();};q.getRenderingContext=function(r){return this.jsxrenderingcontext!=null&&this.jsxrenderingcontext!=ub.H?this.jsxrenderingcontext:r!=null?r:null;};q.setRenderingContext=function(n,h){this.jsxrenderingcontext=n;this.nj(true);if(!h)this.repaint();};q.getSuppressHScroller=function(e){return this.jsxsuppresshscroll!=null?this.jsxsuppresshscroll:e!=null?e:null;};q.setSuppressHScroller=function(a){this.jsxsuppresshscroll=a;var
ua=this.getRendered();if(ua&&ua.childNodes[3]){ua.childNodes[3].style.display=a!=1?ub.y:ub.x;this.dt();}};q.getSuppressVScroller=function(r){return this.jsxsuppressvscroll!=null?this.jsxsuppressvscroll:r!=null?r:null;};q.setSuppressVScroller=function(i,e){this.jsxsuppressvscroll=i;this.nj();if(e){var
D=this.getRendered();if(D&&D.childNodes[2]){D.childNodes[2].style.display=i!=1?ub.y:ub.x;this.dt();}}else this.repaint();};q.getFixedColumnIndex=function(k){return this.jsxfixedcolumnindex!=null?this.jsxfixedcolumnindex:k!=null?k:null;};q.setFixedColumnIndex=function(d){this.jsxfixedcolumnindex=d;};q.getRenderNavigators=function(o){return this.jsxrendernavigators!=null?this.jsxrendernavigators:o!=null?o:null;};q.setRenderNavigators=function(h,f){this.jsxrendernavigators=h;this.nj();if(!f)this.repaint();};q.getIcon=function(k){return this.jsxicon!=null&&this.jsxicon!=ub.H?this.jsxicon:k?k:null;};q.setIcon=function(o){this.jsxicon=o;};q.getIconMinus=function(d){return this.jsxiconminus!=null&&this.jsxiconminus!=ub.H?this.jsxiconminus:d?d:null;};q.setIconMinus=function(h){this.jsxiconminus=h;};q.getIconPlus=function(j){return this.jsxiconplus!=null&&this.jsxiconplus!=ub.H?this.jsxiconplus:j?j:null;};q.setIconPlus=function(i){this.jsxiconplus=i;};q.deleteRecord=function(r,h){var
Pa=this.getXML();var
F=Pa.selectSingleNode(this.Ee(r));if(F!=null){F=F.getParent().removeChild(F);if(h!==false){this.redrawRecord(r,0);if(this.getRenderingModel()!=m.REND_HIER){var
sa=F.selectNodes(ub.be);for(var
Bb=sa.size()-1;Bb>=0;Bb--){var
Ka=sa.get(Bb);this.redrawRecord(Ka.getAttribute(ub.nb),0);}}}return F;}return null;};q.insertRecordProperty=function(b,o,f,p){if(o==ub.nb){var
Fb=this.jsxsupermix(b,o,f,false);var
db=this.TV(b);if(db){db.setAttribute(ub.nb,f);db.setAttribute(ub.ce,f);var
aa=this.getId()+ub.Uc+f;db.setAttribute(ub.de,aa);var
Za=db.childNodes;aa=aa+ub.Uc;for(var
t=0;t<Za.length;t++)Za[t].setAttribute(ub.de,aa+t);if(p!=false)this.redrawRecord(f,2);return Fb;}}else return this.jsxsupermix(b,o,f,p);};q.redrawCell=function(a,h,i){var
jb=this.jO(a,h.getDisplayIndex());if(jb){var
ya=jb.childNodes[0];if(this.getRenderingModel()==m.REND_HIER&&h.getChildIndex()==0&&this.getRenderNavigators(1)!=0){while(ya&&ya.tagName.toLowerCase()!=ub.U)ya=ya.childNodes[0];if(ya){ya=ya.lastChild.firstChild;}else{Qa.error(ub.ee);return;}}var
Nb={};Nb.jsx_return_at_all_costs=true;Nb.jsx_mode=ub.fe;Nb.jsx_record_context=a;Nb.jsx_cell_value_template_id=h.getId()+ub.Cd;var
Va=this.doTransform(Nb);ya.innerHTML=Va;var
Cb=h.Rm();if(Cb){var
qb=jb.parentNode;Cb.format(ya,qb.getAttribute(ub.nb),this,h,qb.getAttribute(ub.dd),this.getServer());}if(!i){var
Bb=this.Xi();var
Ga=new
RegExp(ub.ge+h.getPath()+ub.he);for(var
S=0;S<Bb.length;S++){var
Db=Bb[S].getTriggers()+ub.H;if(Bb[S]!=h&&(Bb[S].getPath()==h.getPath()||Db.search(Ga)>=0))this.redrawCell(a,Bb[S],true);}}}};q.redrawMappedCells=function(f,i){var
L=this.Xi();for(var
va=0;va<L.length;va++)if(L[va].getPath()==i){this.redrawCell(f,L[va],false);return;}};q.uz=function(g,a){return this.doTransform({jsx_mode:ub.Lb,jsx_panel_css:ub.Pb,jsx_column_widths:this.x8(),jsx_context_index:a?a:1,jsx_rendering_context:this.getRecordNode(g).getParent().getAttribute(ub.nb),jsx_rendering_context_child:g});};q.redrawRecord=function(i,a,p){var
Ja=this.vD();if(i!=null&&a==2){if(this.getRenderingModel()==m.REND_HIER){var
X=this.WT(i);var
na=X.parentNode;var
Lb=this.uz(i,na.getAttribute(ub.Qb));jsx3.html.setOuterHTML(X,Lb);X=this.WT(i);var
vb={painted:1,token:m.getToken(),contextnodes:[X]};this.Sv()[0]=vb;this.TA(vb);}else{var
Fa=this.Xi();for(var
ob=0;ob<Fa.length;ob++)this.redrawCell(i,Fa[ob],true);}}else if(i!=null&&a==0){if(this.getRenderingModel()==m.REND_HIER){var
Mb=this.WT(i);if(Mb){var
E=Mb.parentNode;jsx3.html.removeNode(Mb);if(E.childNodes.length==0){var
Eb=E.previousSibling;var
qb=this.mI(Eb);if(qb){var
Q=qb.getAttribute(ub.nb);this.redrawRecord(Q,2);}}this.Tw();}}else if(this.getPagingModel(0)!=3){var
Mb=this.TV(i);if(Mb){var
Fb=Mb.parentNode;if(Fb.childNodes.length==1){if(Fb.tagName.toLowerCase()!=ub.B)Fb=Fb.parentNode;jsx3.html.removeNode(Fb);}else{var
ab=Mb.nextSibling;if(Mb.parentNode.firstChild==Mb&&ab)for(var
ob=0;ob<Mb.childNodes.length;ob++)ab.childNodes[ob].style.width=Mb.childNodes[ob].style.width;jsx3.html.removeNode(Mb);this.Tw();}}}else this.repaint();}else if(i!=null&&a==3){if(this.getPagingModel(0)!=3){var
u=this.getRecordNode(i);var
R=u.getNextSibling();var
U=R.getAttribute(ub.nb);if(this.getRenderingModel()==m.REND_HIER){var
zb=this.WT(U);var
Lb=this.uz(i,zb.parentNode.getAttribute(ub.Qb));jsx3.html.insertAdjacentHTML(zb,ub.ie,Lb);var
vb={painted:1,token:m.getToken(),contextnodes:[zb.previousSibling]};this.Sv()[0]=vb;this.TA(vb);this.Tw(Ja);}else{var
ia=this.TV(U);var
ka=ia.parentNode;var
nb=this.TY(ka,i);ka.insertBefore(nb,ia);if(ka.firstChild==nb)for(var
ob=0;ob<nb.childNodes.length;ob++){nb.childNodes[ob].style.width=ia.childNodes[ob].style.width;ia.childNodes[ob].style.width=ub.H;}this.KN(nb,i);this.Tw(Ja);if(p!==false)this.Tx(u);}}else this.repaint();}else if(i!=null&&a==1)if(this.getPagingModel(0)!=3){if(this.getRenderingModel()==m.REND_HIER){var
xb=this.getRecordNode(i).getParent().getAttribute(ub.nb);var
X=this.WT(xb);var
na=X.lastChild;if(this.GZ(na)){this.toggleItem(xb,true);}else{var
Lb=this.uz(i,na.getAttribute(ub.Qb));jsx3.html.insertAdjacentHTML(na,ub.Ya,Lb);var
vb={painted:1,token:m.getToken(),contextnodes:[na.lastChild]};this.Sv()[0]=vb;this.TA(vb);this.Tw(Ja);}}else{var
ka=this.gS();if(ka==null||ka.firstChild==null){this.repaintData();}else{if(ka.firstChild.tagName.toLowerCase()==ub.Tc)ka=ka.firstChild;var
nb=this.TY(ka,i);ka.appendChild(nb);this.KN(nb,i);this.Tw(Ja);if(p!==false)this.Tx(this.getRecordNode(i));}}}else this.repaint();};q.Tx=function(s){if(this.getRenderingModel(m.REND_DEEP)==m.REND_DEEP){var
x=s.selectNodeIterator(ub.be);while(x.hasNext()){s=x.next();this.redrawRecord(s.getAttribute(ub.nb),1,false);}}};q.GZ=function(p){return p.childNodes.length==0||p.childNodes.length==1&&p.childNodes[0].nodeType!=1;};q.KN=function(h,j){var
V=this.Xi();for(var
zb=0;zb<V.length;zb++){var
Jb=V[zb];var
Ca=Jb.Rm();if(Ca){var
Xa=h.childNodes[zb].childNodes[0];Ca.format(Xa,j,this,Jb,h.getAttribute(ub.dd),this.getServer());}}};q.TY=function(d,c){var
V=this.getDocument();var
Kb={};Kb.jsx_column_widths=this.x8();Kb.jsx_rendering_context=this.getRecordNode(c).getParent().getAttribute(ub.nb);Kb.jsx_rendering_context_child=c;Kb.jsx_mode=ub.Lb;var
Sa=this.doTransform(Kb);var
y=new
jsx3.xml.Document();y.loadXML(Sa);var
Ab=y.getRootNode();var
P=V.createElement(ub.U);m.UI(Ab,P);for(var
Da=Ab.getChildIterator();Da.hasNext();){Ab=Da.next();var
wa=V.createElement(ub.je);m.UI(Ab,wa);P.appendChild(wa);wa.innerHTML=Ab.getFirstChild().getXML();}return P;};m.UI=function(a,h){var
qb=a.getAttributeNames();for(var
Ka=0;Ka<qb.length;Ka++){var
Db=qb[Ka];var
Nb=a.getAttribute(Db);var
Ua=ub.ke;if(Db.match(Ua)){jsx3.html.addEventListener(h,Db.toLowerCase(),Nb);}else if(Db==ub.Wb){h.className=Nb;}else if(Db==ub.Yb){jsx3.gui.Painted.Si(h,Nb);}else h.setAttribute(Db,Nb);}};q.setValue=function(k){this.deselectAllRecords();if(k){if(k instanceof Array){if(this.getSelectionModel()!=2&&k.length>1)throw new
jsx3.IllegalArgumentException(ub.le,k);}else k=[k];for(var
R=0;R<k.length;R++)this.selectRecord(k[R]);this.revealRecord(k[0]);}else{this.synchronizeVScroller();this.setScrollTop(0);}return this;};q.getMaskProperties=function(){return Ya.MASK_NO_EDIT;};});jsx3.Class.defineClass("jsx3.gui.Matrix.ColumnFormat",null,null,function(g,p){var
ub={k:"objDiv",h:"{0,",c:"oZ",p:"objServer",q:/&lt;/g,v:'"',i:"}",A:"",a:"short",u:/&quot;/g,t:">",j:"@message",s:/&gt;/g,z:"jsxtext",d:"jE",o:"intRowNumber",w:/&amp;/g,r:"<",l:"strCDFKey",g:/^@(datetime|date|time|number)\b/,b:"long",m:"objMatrix",f:"@",y:"function",n:"objMatrixColumn",x:"&",e:"JE"};var
F=jsx3.gui.Matrix;g.LF={medium:2,full:4};g.LF[ub.a]=1;g.LF[ub.b]=3;g.Ax={integer:1,percent:1,currency:1};g.hz={unescape:ub.c,unescape_all:ub.d,lookup:ub.e};g.getInstance=function(l,e){var
Y=null;var
Ja=null;if(l.charAt(0)==ub.f&&(Ja=g.hz[l.substring(1)])!=null){Y=new
g();Y.format=g[Ja];}else if(l.match(ub.g)){Y=new
F.MessageFormat(e,ub.h+l.substring(1)+ub.i);}else if(l.indexOf(ub.j)==0)Y=new
F.MessageFormat(e,l.substring(9));return Y;};p.init=function(){};p.validate=function(){return true;};p.format=jsx3.Method.newAbstract(ub.k,ub.l,ub.m,ub.n,ub.o,ub.p);g.oZ=function(c,k,r,e,q,f){g.jE(c,k,r,e,q,f,jsx3.xml.Template.supports(1));};g.jE=function(m,a,i,n,j,d,l){if(!l)m.innerHTML=m.innerHTML.replace(ub.q,ub.r).replace(ub.s,ub.t).replace(ub.u,ub.v).replace(ub.w,ub.x);};g.JE=function(l,b,h,o,f,q){var
N=o.getEditMask();if(N&&N.OK)N=N.OK();if(N!=null&&typeof N.getRecordNode==ub.y){var
cb=h.getRecordNode(b);if(cb){var
la=o.getValueForRecord(b);var
Db=N.getRecordNode(la);l.innerHTML=Db!=null?Db.getAttribute(ub.z):la!=null?jsx3.util.strEscapeHTML(la):ub.A;}}};});jsx3.Class.defineClass("jsx3.gui.Matrix.MessageFormat",jsx3.gui.Matrix.ColumnFormat,null,function(h,m){m.init=function(d,c){this.b6=new
jsx3.util.MessageFormat(c,d.getServer().getLocale());};m.format=function(o,r,k,l,i,n){var
gb=l.getValueForRecord(r);if(gb)try{o.innerHTML=this.b6.format(gb);}catch(Kb){jsx3.util.Logger.GLOBAL.error(this.b6,jsx3.NativeError.wrap(Kb));}};});jsx3.Class.defineInterface("jsx3.gui.Matrix.EditMask",null,function(d,s){var
ub={a:"_jsxTy"};var
fb=jsx3.gui.Matrix;d.NORMAL=1;d.FORMAT=2;d.DIALOG=3;s.emInit=function(h){};s.emGetType=function(){return d.NORMAL;};s.emPaintTemplate=function(){return this.paint();};s.cB=function(a,n,h,f,e,i,l){this._jsxTy={matrix:f,column:e,recordId:i,td:l,value:a};return this.emBeginEdit(a,n,h,f,e,i,l)!==false;};s.emBeginEdit=function(r,p,o,a,g,q,n){};s.ZZ=function(){var
V=this.emEndEdit();delete this[ub.a];return V;};s.emEndEdit=function(){return this.emGetValue();};s.emGetValue=function(){return null;};s.suspendEditSession=function(){this.getAncestorOfType(fb)._jsxOM.f1=true;};s.resumeEditSession=function(){this.getAncestorOfType(fb)._jsxOM.f1=false;};s.emGetSession=function(){return this._jsxTy;};s.commitEditMask=function(a,p){if(this._jsxTy)this._jsxTy.matrix.oL(a,p);};s.emCollapseEdit=function(c){};});jsx3.Class.defineInterface("jsx3.gui.Matrix.BlockMask",jsx3.gui.Matrix.EditMask,function(k,f){var
ub={a:"none",b:""};var
Eb=jsx3.gui.Matrix;var
_a=jsx3.gui.Block;f.emInit=function(d){this.setDisplay(ub.a,true);this.setDimensions(0,0,null,null,true);this.setRelativePosition(0,true);this._jsxo1=this.getWidth();this._jsxGN=this.getHeight();var
qa=this.OK();if(qa){var
Za=qa.getRelativePosition();var
pa=qa.getDisplay();Eb.ZJ(qa,d);qa.setRelativePosition(Za,true);qa.setDisplay(pa,true);}};f.emGetType=function(){return Eb.EditMask.NORMAL;};f.emBeginEdit=function(b,m,n,g,d,j,r){var
wa=isNaN(this._jsxo1)?m.W:parseInt(this._jsxo1);var
Ga=isNaN(this._jsxGN)?m.H:parseInt(this._jsxGN);this.setMaskValue(b);this.setDimensions(m.L,m.T,wa,Ga,true);this.setDisplay(ub.b,true);var
u=this.getMaskFirstResponder()||this;u.focus();};f.emGetValue=function(){return this.getMaskValue();};f.getMaskFirstResponder=function(){return this.OK();};f.getMaskValue=function(){var
ca=this.OK();return ca!=null?ca.getValue():null;};f.setMaskValue=function(q){var
Jb=this.OK();if(Jb!=null)Jb.setValue(q);};f.OK=function(){return this.getDescendantsOfType(jsx3.gui.Form)[0];};f.emEndEdit=function(){var
yb=this.getMaskValue();this.setDisplay(ub.a,true);return yb;};});jsx3.Class.defineInterface("jsx3.gui.Matrix.DialogMask",jsx3.gui.Matrix.BlockMask,function(g,f){var
ub={a:"",b:"none"};var
za=jsx3.gui.Matrix;f.emInit=function(k){za.BlockMask.prototype.emInit.call(this,k);var
rb=this.getParent();while(rb!=null){if(jsx3.gui.Window&&rb instanceof jsx3.gui.Window){rb=rb.getRootBlock();break;}else if(jsx3.gui.Dialog&&rb instanceof jsx3.gui.Dialog)break;rb=rb.getParent();}if(rb==null)rb=this.getServer().getRootBlock();rb.paintChild(this);};f.emGetType=function(){return za.EditMask.DIALOG;};f.emBeginEdit=function(c,l,a,h,e,k,j){this._jsxTy={matrix:h,column:e,recordId:k,td:j};var
da=this.getRendered(j).parentNode.parentNode;var
E=jsx3.html.getRelativePosition(da,da);var
Mb=jsx3.html.getRelativePosition(da,j);var
Z=this._jsxo1;var
y=this._jsxGN;var
Xa=[E.W-Mb.L-l.W,Mb.L,l.W];var
ia=-1;for(var
jb=0;jb<Xa.length&&ia<0;jb++)if(Z<=Xa[jb])ia=jb;if(ia<0)L1:for(var
jb=0;jb<Xa.length&&ia<0;jb++){for(var
yb=0;yb<Xa.length;yb++)if(Xa[jb]<Xa[yb])continue L1;ia=jb;}var
Ea=ia==2;var
eb=[E.H-Mb.T-(Ea?l.H:0),Mb.T+(Ea?0:l.H)];var
fb=-1;for(var
jb=0;jb<eb.length&&fb<0;jb++)if(y<=eb[jb])fb=jb;if(fb<0)L2:for(var
jb=0;jb<eb.length&&fb<0;jb++){for(var
yb=0;yb<eb.length;yb++)if(eb[jb]<eb[yb])continue L2;fb=jb;}Z=Math.min(Z,Xa[ia]);y=Math.min(y,eb[fb]);var
gb=[Mb.L+l.W,Mb.L-Z,Mb.L][ia];var
I=[Mb.T+(Ea?l.H:0),Mb.T-y+(Ea?0:l.H)][fb];this.setMaskValue(c);this.setDimensions(gb,I,Z,y,true);this.setDisplay(ub.a,true);jsx3.html.updateCSSOpacity(this.getRendered(),0.9);var
J=this.getMaskFirstResponder()||this;J.focus();};f.OK=function(){var
Q=this.getChild(0)==this.getCaptionBar()?this.getChild(1):this.getChild(0);return Q!=null?Q.getDescendantsOfType(jsx3.gui.Form)[0]:null;};f.emCollapseEdit=function(d){this.setDisplay(ub.b,true);};});
