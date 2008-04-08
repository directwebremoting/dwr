/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.xml.Cacheable","jsx3.gui.Form","jsx3.gui.Heavyweight","jsx3.gui.Block","jsx3.util.MessageFormat");jsx3.Class.defineClass("jsx3.gui.Select",jsx3.gui.Block,[jsx3.gui.Form,jsx3.xml.Cacheable,jsx3.xml.CDF],function(i,q){var
ub={Qa:' class="jsx30select_display" jsxtype="Display" ',_:"click",ea:"O4",q:"jsx3.gui.Select.defaultText",ra:"_",V:'padding:0px;margin:0px 0px -1px 0px;overflow:hidden;">&#160;</div>',v:"Select",U:"px;position:relative;left:0px;top:0px;",u:"Text",ka:"W",z:"url(",d:"JSX_SELECT_XSL",La:' id="',D:"&quot",fa:' jsxtype="Options" class="jsx30select_optionlist" style="',R:"noMatch",w:"focusPrevious",Ca:"inline",Q:"dataUnavailable",Na:'" jsxtype="Select" ',Pa:");background-repeat:no-repeat;background-position:right 0px;",la:"E",qa:"_jsxopening",Wa:"KW",y:"jsxselid",x:"focusNext",Za:"innerHTML",ja:"</div>",W:'<div tabindex="0" jsxselid="',Fa:"solid 0px;solid 1px #c8c8d5;solid 0px;solid 0px",Ha:"keyup",i:"<div tabIndex='0' class='jsx30select_option' onmousedown='var e = jsx3.gui.Event.wrap(event); jsx3.gui.Event.publish(e); e.cancelBubble();'>- data unavailable -</div>",Sa:'class="jsx30combo"',A:")",t:"Display",Ia:"eJ",s:"jsxid",X:'"',r:"jsxtype",sa:"Error focusing first object: ",C:/\"/g,Ua:'" jsxtype="Text" ',l:"jsx30curvisibleoptions",L:"jsxhomepath",ia:'">',Ka:"NQ",_a:"jsxbeforeselect",eb:" ",J:"null",Da:"input[text]",ta:"Qx",oa:"px",O:/\<div/i,k:'<div tabIndex="0" class="jsx30select_option jsx30select_none" onmousedown="var e=jsx3.gui.Event.wrap(event); jsx3.gui.Event.publish(e); e.cancelBubble();"><span style="left:0px;">{0}</span></div>',Ga:"jsxkeyup",p:"",P:"jsx3.gui.Select.",I:"JSX_GENERIC",ha:"px;",a:"#ffffff",Aa:"100%",F:'")]',Ra:' jsxtype="Text" class="jsx30select_text"',j:"<div tabIndex='0' class='jsx30select_option' onmousedown='var e = jsx3.gui.Event.wrap(event); jsx3.gui.Event.publish(e); e.cancelBubble();'>- no match found -</div>",ua:"span",S:"0px;1px;0px;1px",Ma:' class="',K:"jsxabspath",bb:"3.0.00",aa:"hO",B:'//record[@jsxtext="',g:"jsx:///images/select/selectover.gif",b:"- Select -",va:"solid 1px #a6a6af;solid 1px #e6e6e6;solid 1px #e6e6e6;solid 1px #a6a6af",M:"jsxtext",ab:"jsxselect",f:"jsx:///images/select/arrow.gif",T:'<div style="height:1px;width:',ga:"min-width:",ba:"mousedown",e:"JSX_COMBO_XSL",za:"0 19 0 0",h:"jsx:///images/select/selected.gif",c:"jsx:///xsl/jsxselect.xsl",H:"body",fb:"C0",wa:"relativebox",Ya:"value",G:"x",Ja:"focus",ma:"S",ca:"cw",pa:"div",N:"combo",o:";",Z:"gu",xa:"box",Ta:' class="jsx30combo_text" value="',na:"N",Ba:"2 0 0 3",da:"mouseover",Oa:"background-image:url(",m:"jsx30select_select",db:'" ',Y:"keydown",Ea:"0 0 0 4",n:"background-color:",Xa:"input",Va:"blur",ya:"0 0 0 0",cb:' maxlength="',E:'" or (not(@jsxtext) and @jsxid="'};var
ab=jsx3.util.Logger.getLogger(i.jsxclass.getName());var
na=jsx3.gui.Event;i.DEFAULTBACKGROUNDCOLOR=ub.a;i.DEFAULTTEXT=ub.b;i.DEFAULTXSLURL=jsx3.resolveURI(ub.c);i.SELECTXSLURL=i.DEFAULTXSLURL;i.SELECTXSLID=ub.d;i.COMBOXSLURL=i.DEFAULTXSLURL;i.COMBOXSLID=ub.e;i.ARROWICON=jsx3.resolveURI(ub.f);i.OVERIMAGE=jsx3.resolveURI(ub.g);i.SELECTEDIMAGE=jsx3.resolveURI(ub.h);jsx3.html.loadImages(i.ARROWICON,i.OVERIMAGE,i.SELECTEDIMAGE);i.NODATAMESSAGE=ub.i;i.NOMATCHMESSAGE=ub.j;i.bZ=new
jsx3.util.MessageFormat(ub.k);i.TYPESELECT=0;i.TYPECOMBO=1;i.TYPEAHEADDELAY=250;i.Tp=null;i.gX=null;i.wK=ub.l;i.DEFAULTCLASSNAME=ub.m;q.init=function(k,n,l,f,c,s){this.jsxsuper(k,n,l,f,c);if(s!=null)this.jsxvalue=s;};q.getXSL=function(){return this.qj(i.DEFAULTXSLURL);};q.doValidate=function(){var
Ja=jsx3.gui.Form;var
La=this.getRequired()==0;if(!La){var
_a=this.getValue();if(this.getType()==0){La=this.getRecordNode(_a)!=null;}else La=_a!=null&&_a.length>0;}this.setValidationState(La?1:0);return this.getValidationState();};q.zf=function(){var
Xa=this.getEnabled()!=0?this.getBackgroundColor():this.getDisabledBackgroundColor()||jsx3.gui.Form.DEFAULTDISABLEDBACKGROUNDCOLOR;return Xa?ub.n+Xa+ub.o:ub.p;};q.getType=function(){return this.jsxtype==null?0:this.jsxtype;};q.setType=function(f){this.jsxtype=f;this.ce();return this;};q.getDefaultText=function(){return this.jsxdefaulttext!=null&&this.jsxdefaulttext!=null?this.jsxdefaulttext:this.wi(ub.q);};q.setDefaultText=function(h){this.jsxdefaulttext=h;return this;};q.NQ=function(c,j){if(this.oe(c,j))return;var
Ga=c.srcElement();var
wb=Ga.getAttribute(ub.r);var
La=Ga.getAttribute(ub.s);var
L=c.hasModifier();if((c.spaceKey()||c.enterKey())&&La!=null){this.UF(c,Ga.getAttribute(ub.s));this.hide(true);}else if(wb==ub.t||wb==ub.u||wb==ub.v){if(c.downArrow()&&!L){this.VW();}else return;}else if(c.leftArrow()||c.escapeKey()){this.hide(true);}else if(c.downArrow()){if(L)return;if(Ga==j.lastChild||La==null){this.et(j.firstChild.nextSibling);}else this.et(Ga.nextSibling);}else if(c.upArrow()){if(L)return;if(Ga==j.firstChild.nextSibling||La==null){this.et(j.lastChild);}else this.et(Ga.previousSibling);}else if(c.tabKey()){if(c.hasModifier(true))return;this.UF(c,Ga.getAttribute(ub.s));jsx3.html[c.shiftKey()?ub.w:ub.x](this.getRendered(c),c);this.hide(false);return;}else return;c.cancelAll();};q.eJ=function(s,m){if(this.oe(s,m))return;var
ma=s.hasModifier();if(!ma&&(s.downArrow()||s.enterKey())){var
Va=jsx3.gui.Heavyweight.GO(i.wK);var
K=Va?Va.getRendered(s).childNodes[0].childNodes[0]:null;if(!s.enterKey()&&K&&K.getAttribute(ub.y)==this.getId()){this.et(K.childNodes[1]);}else{var
pb=s.enterKey()?ub.p:this.getText();this.VW(pb);}s.cancelAll();}else if(s.tabKey()&&s.shiftKey()&&!s.hasModifier(true)){jsx3.html.focusPrevious(this.getRendered(s),s);}else if(!ma&&(s.rightArrow()||s.leftArrow())){var
Ra=s.leftArrow();var
la=this.EG();var
Da=la.value;var
oa=jsx3.html.getSelection(la);if(Ra&&(oa.getStartIndex()>0||oa.getEndIndex()>0)||!Ra&&(oa.getStartIndex()<Da.length||oa.getEndIndex()<Da.length))s.cancelBubble();}else{var
ib=this.EG();var
Ab=ib.value;jsx3.sleep(function(){if(this.getParent()==null)return;var
Jb=ib.value;if(Ab!=Jb){this.jsxvalue=Jb;if(i.gX)window.clearTimeout(i.gX);var
Za=this;i.gX=window.setTimeout(function(){if(Za.getParent()==null)return;i.gX=null;Za.VW(Jb);},i.TYPEAHEADDELAY);}},null,this);}};q.gu=function(r,n){this.NQ(r,n);};q.EG=function(d){d=this.getRendered(d);return d?this.getType()==1?d.childNodes[0].childNodes[0].childNodes[0]:d.childNodes[0].childNodes[0]:null;};q.show=function(){var
ra=this.getRendered();if(ra)this.VW();};q.hO=function(k,b){var
eb=k.srcElement();while(eb!=null&&(!eb.getAttribute||eb.getAttribute(ub.s)==null)){eb=eb.parentNode;if(eb==b)eb=null;}if(eb!=null)this.UF(k,eb.getAttribute(ub.s));this.hide(true);};q.et=function(p){if(this._jsxsW){try{this._jsxsW.style.backgroundImage=ub.z+jsx3.gui.Block.SPACE+ub.A;}catch(Kb){}this._jsxsW=null;}if(p){p.focus();p.style.backgroundImage=ub.z+i.OVERIMAGE+ub.A;this._jsxsW=p;var
Hb=jsx3.gui.Heavyweight.GO(i.wK);Hb.scrollTo(p);}};q.KW=function(c,j){var
S=j.value;var
E=this.getXML().selectSingleNode(ub.B+S.replace(ub.C,ub.D)+ub.E+S.replace(ub.C,ub.D)+ub.F);if(E!=null){this.UF(c,E.getAttribute(ub.s));}else this.jsxvalue=S;};q.hide=function(g){if(i.Tp==this){var
A=jsx3.gui.Heavyweight.GO(i.wK);if(A)A.destroy();if(g)try{this.focus();}catch(Kb){}na.unsubscribeLoseFocus(this);i.Tp=null;}if(i.gX){i.gX=null;window.clearTimeout(i.gX);}};i.hideOptions=function(){if(i.Tp!=null)i.Tp.hide();};i.S4=function(){var
S=jsx3.html.getMode();if(S==2&&jsx3.CLASS_LOADER.IE7)S=S+ub.G;return S;};q.VW=function(f){if(this._jsxopening)return;var
_=this.getRendered();if(_!=null){var
Ha=this.getType();var
qa=_.ownerDocument;if(i.Tp!=null)i.Tp.hide(false);i.Tp=this;var
yb=this.getAbsolutePosition(qa.getElementsByTagName(ub.H)[0]);var
U=yb.W;var
Pa=this.getType()==1;var
ga={};ga.jsxtabindex=this.getIndex()?this.getIndex():0;ga.jsxselectedimage=i.SELECTEDIMAGE;ga.jsxtransparentimage=jsx3.gui.Block.SPACE;ga.jsxdragtype=ub.I;ga.jsxid=this.getId();ga.jsxselectedid=this.getValue()==null?ub.J:this.getValue();ga.jsxpath=jsx3.getEnv(ub.K);ga.jsxpathapps=jsx3.getEnv(ub.L);ga.jsxpathprefix=this.getUriResolver().getUriPrefix();ga.jsxmode=i.S4();if(Pa){ga.jsxsortpath=ub.M;ga.jsx_type=ub.N;}if(f!=null)ga.jsxtext=f;var
nb=this.getXSLParams();for(var y in nb)ga[y]=nb[y];var
wb=this.doTransform(ga);if(!jsx3.xml.Template.supports(1))wb=jsx3.html.removeOutputEscapingSpan(wb);wb=this.Al(wb);if(!wb.match(ub.O))wb=i.bZ.format(this.wi(ub.P+(Ha==0?ub.Q:ub.R)));var
zb=new
jsx3.gui.Painted.Box({width:U,height:1,border:ub.S});zb.calculate();var
xb=zb.mn();var
w=ub.T+xb+ub.U+ub.V;var
t=ub.W+this.getId()+ub.X+this.Gj(ub.Y,ub.Z)+this.Gj(ub._,ub.aa)+this.Gj(ub.ba,ub.ca)+this.Gj(ub.da,ub.ea)+ub.fa+this.zf()+ub.ga+xb+ub.ha+this.ad()+this.Yh()+this.Bf()+this.fn()+ub.ia+w+wb+ub.ja;var
Ta=new
jsx3.gui.Heavyweight(i.wK,this);Ta.setHTML(t);Ta.setScrolling(true);Ta.addXRule(_,ub.ka,ub.ka,0);Ta.addXRule(_,ub.la,ub.la,0);Ta.addYRule(_,ub.ma,ub.na,0);Ta.addYRule(_,ub.na,ub.ma,0);Ta.show();var
pa=Ta.getRendered();var
C=pa.childNodes[0].childNodes[0];var
x=Math.max(C.offsetWidth-2,C.clientWidth)+ub.oa;if(jsx3.CLASS_LOADER.IE)for(var
Xa=0;Xa<C.childNodes.length;Xa++){var
sa=C.childNodes[Xa];if(sa.nodeName&&sa.nodeName.toLowerCase()==ub.pa&&sa.getAttribute(ub.s))C.childNodes[Xa].style.width=x;}if(Ha==0||f==null){this._jsxopening=true;jsx3.sleep(function(){if(this.getParent()==null)return;delete this[ub.qa];var
rb=this.getRecordNode(this.getValue());var
qb=rb?this.getDocument().getElementById(this.getId()+ub.ra+this.getValue()):null;try{if(qb)this.et(qb);else C.focus();}catch(Kb){ab.info(ub.sa+jsx3.NativeError.wrap(Kb));}},null,this);}else this.EG().focus();na.subscribeLoseFocus(this,_,ub.ta);}};q.Qx=function(b){var
fa=b.event.srcElement();if(!this.containsHtmlElement(fa))this.hide(false);};q.Rc=function(l,e,f){var
Za=this.Wl(true,l);if(e){var
wb=Za.recalculate(l,e,f);if(!wb.w&&!wb.h)return;var
db=Za.lg(0);db.recalculate({parentwidth:Za.mn(),parentheight:Za.ec()},e?e.childNodes[0]:null,f);var
B=db.lg(0);if(this.getType()!=0){var
J=B.lg(0);J.recalculate({parentwidth:db.mn()-1,parentheight:db.ec()},e?jsx3.html.selectSingleElm(e,0,0,0):null,f);}else B.recalculate({parentwidth:db.mn(),parentheight:db.ec()},e?e.childNodes[0].childNodes[0]:null,f);}};q.Gc=function(k){if(this.getParent()&&(k==null||isNaN(k.parentwidth)||isNaN(k.parentheight))){k=this.getParent().uf(this);}else if(k==null)k={};var
Hb=this.getRelativePosition()!=0&&(!this.getRelativePosition()||this.getRelativePosition()==1);var
Ha,O,ua,Fa,B;k.tagname=ub.ua;k.border=(Ha=this.getBorder())!=null&&Ha!=ub.p?Ha:ub.va;k.margin=Hb&&(O=this.getMargin())!=null&&O!=ub.p?O:null;if(!k.boxtype)k.boxtype=Hb?ub.wa:ub.xa;if(k.left==null)k.left=!Hb&&!jsx3.util.strEmpty(this.getLeft())?this.getLeft():0;if(k.top==null)k.top=!Hb&&!jsx3.util.strEmpty(this.getTop())?this.getTop():0;if(k.width==null)k.width=(Fa=this.getWidth())!=null?Fa:100;if(k.height==null)k.height=(B=this.getHeight())!=null?B:18;k.padding=ub.ya;var
wa=new
jsx3.gui.Painted.Box(k);var
ka={};ka.tagname=ub.pa;ka.boxtype=ub.wa;if((ua=this.getPadding())!=null&&ua!=ub.p){ka.padding=ua;}else ka.padding=ub.za;ka.parentwidth=wa.mn();ka.parentheight=wa.ec();ka.left=0;ka.top=0;ka.width=ub.Aa;ka.height=ub.Aa;var
nb=new
jsx3.gui.Painted.Box(ka);wa.Yf(nb);if(this.getType()==0){ka={};ka.tagname=ub.pa;ka.boxtype=ub.wa;ka.padding=ub.Ba;ka.parentwidth=nb.mn();ka.parentheight=nb.ec();ka.width=ub.Aa;ka.height=ub.Aa;var
ca=new
jsx3.gui.Painted.Box(ka);nb.Yf(ca);}else{ka={};ka.tagname=ub.pa;ka.boxtype=ub.Ca;var
ca=new
jsx3.gui.Painted.Box(ka);nb.Yf(ca);ka={};ka.tagname=ub.Da;ka.boxtype=ub.wa;ka.parentwidth=nb.mn()-1;ka.parentheight=nb.ec();ka.width=ub.Aa;ka.height=ub.Aa;ka.padding=ub.Ea;ka.empty=true;ka.border=ub.Fa;var
pa=new
jsx3.gui.Painted.Box(ka);ca.Yf(pa);}return wa;};q.paint=function(){this.applyDynamicProperties();if(this.getXmlAsync())this.getXML();var
Ab=this.getId();var
P=this.getEnabled()==1;var
Bb={};if(P){Bb[ub.ba]=true;if(this.hasEvent(ub.Ga))Bb[ub.Ha]=true;if(this.getType()==1){Bb[ub.Y]=ub.Ia;Bb[ub.Ja]=true;}else Bb[ub.Y]=ub.Ka;}var
za=this.jh(Bb,0);var
Pa=this.renderAttributes(null,true);var
Ia=this.Wl(true);Ia.setAttributes(ub.La+Ab+ub.X+this.on()+ub.Ma+this.to()+ub.Na+Pa+za+this.Fo());Ia.setStyles(this.eh()+this.zf()+ub.Oa+i.ARROWICON+ub.Pa+this.zk()+this.bd()+this.bi()+this.fh());var
zb=Ia.lg(0);zb.setAttributes(ub.Qa+this.bn());zb.setStyles(ub.p);if(this.getType()==0){var
Qa=zb.lg(0);Qa.setAttributes(ub.Ra+jsx3.html.Kf);Qa.setStyles(this.ad()+this.Yh()+this.Bf()+this.eh()+this.fn());var
mb=Ia.paint().join(zb.paint().join(Qa.paint().join(this.wg())));}else{var
Qa=zb.lg(0);Qa.setAttributes(ub.Sa);var
x=Qa.lg(0);x.setAttributes(this.Fo()+this.paintMaxLength()+this.wn()+ub.Ta+this.wg().replace(ub.C,ub.D)+ub.Ua+this.Gj(ub.Va,ub.Wa,3));x.setStyles(this.ad()+this.Yh()+this.Bf()+this.eh()+this.fn()+this.zf());var
mb=Ia.paint().join(zb.paint().join(Qa.paint().join(x.paint().join(ub.p))));}return mb;};q.Dc=function(m,s){if(!m.leftButton())return;if(m.srcElement().tagName.toLowerCase()!=ub.Xa)this.VW();else this.et();};q.Vl=function(o,m){this.EG(m).focus();this.et();};q.cw=function(s,d){s.cancelBubble();};q.O4=function(o,m){var
H=i.rH(o,m);if(H){if(o.isFakeOver(H))return;this.et(H);}};i.rH=function(n,r){var
ja=n.srcElement();while(ja!=null&&ja.getAttribute(ub.s)==null){ja=ja.parentNode;if(ja==r||ja==r.ownerDocument)ja=null;}return ja;};q.setText=function(o){this.es(o);return this;};q.es=function(f){var
Db;if(Db=this.EG())Db[this.getType()==1?ub.Ya:ub.Za]=f;};q.focus=function(){var
W=this.getType()==1?this.EG():this.getRendered();if(W)W.focus();return W;};q.setValue=function(o){this.UF(this.isOldEventProtocol(),o);return this;};q.UF=function(s,g){if(g!=this.getValue()){var
ca=true;if(s instanceof na)ca=this.doEvent(ub._a,{objEVENT:s,strRECORDID:g});if(ca===false)return;this.jsxvalue=g;this.redrawRecord(g,2);if(s)this.doEvent(ub.ab,{objEVENT:s instanceof na?s:null,strRECORDID:g});}};q.getValue=function(){return this.jsxvalue;};q.getText=function(){var
lb=this.getRecordNode(this.getValue());if(lb!=null){var
Da=lb.getAttribute(ub.M);return Da!=null?Da:lb.getAttribute(ub.s);}else return this.getType()==1||this.getValue()!=null?this.getValue():this.getDefaultText();};q.redrawRecord=function(a,l){if(this.getValue()==a){var
ia=this.getRecordNode(a);if(ia!=null){var
ta=ia.getAttribute(ub.M);this.es(ta!=null?ta:ia.getAttribute(ub.s));}else this.es(this.getType()==0?this.getDefaultText():a!=null?a:ub.p);}return this;};i.getVersion=function(){return ub.bb;};q.containsHtmlElement=function(k){var
La=jsx3.gui.Heavyweight.GO(i.wK);return this.jsxsuper(k)||La!=null&&La.containsHtmlElement(k);};q.getMaxLength=function(){return this.jsxmaxlength!=null?this.jsxmaxlength:null;};q.setMaxLength=function(s){s=parseInt(s);this.jsxmaxlength=s>0?s:null;return this;};q.paintMaxLength=function(g){return this.getMaxLength()!=null?ub.cb+this.getMaxLength()+ub.db:ub.p;};q.to=function(){var
_a=this.getClassName();return i.DEFAULTCLASSNAME+(_a?ub.eb+_a:ub.p);};q.emInit=function(j){this.jsxsupermix(j);this.subscribe(ub.ab,this,ub.fb);};q.emCollapseEdit=function(h){this.hide(false);};q.C0=function(s){this.commitEditMask(s.context.objEVENT,true);};});jsx3.Select=jsx3.gui.Select;
