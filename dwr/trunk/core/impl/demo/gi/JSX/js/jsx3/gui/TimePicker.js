/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Form","jsx3.gui.Block","jsx3.util.NumberFormat");jsx3.Class.defineClass("jsx3.gui.TimePicker",jsx3.gui.Block,[jsx3.gui.Form],function(f,m){var
ub={oa:"/>",k:"box",O:' jsxfield="hour" size="2" maxlength="2" value="',ea:"time.sep.ampm",_:' jsxfield="milli" size="3" maxlength="3" value="',p:"input[text]",P:'"',ra:"&#160;",q:"1 1 0 0",V:"</div>",v:"zy",ha:"<img",I:"text-align:right;width:",a:"jsx:///images/jsxtimepicker/spin_up.gif",F:'" ',u:"keydown",U:"time.sep.hour-min",ua:"MS",j:"relativebox",ka:"click",d:"000",z:"P3",S:"left:",fa:"cursor:pointer;",D:'" class="jsx30timepicker" ',w:"blur",K:"px;position:absolute;",R:'<div style="',aa:"time.sep.sec-milli",g:"jsxfield",B:"wy",b:"jsx:///images/jsxtimepicker/spin_down.gif",Q:' jsxfield="minute" size="2" maxlength="2" value="',va:"jsxchange",M:"minute",qa:"c2",la:"V3",f:"input",ga:' class="spinner"',y:"focus",T:'px;">',ba:"time.ampm",x:"HW",e:"time.24hour",za:"native",ja:'" width="11" height="8" src="',c:"00",h:"",W:"second",H:";",wa:"_jsxj2",G:"background-color:",ma:"mousedown",i:"width",ca:' jsxfield="ampm" size="2" maxlength="2" value="',A:"mousewheel",t:"solid 0px;solid 0px;solid 0px;solid 1px #d8d8e5",pa:' style="top:8px;left:0px;position:absolute;',N:' class="fields"',s:"0",X:' jsxfield="second" size="2" maxlength="2" value="',Z:"milli",o:"inline",xa:"_jsxhI",sa:"ampm",r:"solid 0px;solid 0px;solid 0px;solid 0px",C:'id="',na:"YO",l:"span",ia:' style="top:0px;left:0px;position:absolute;',da:"position:absolute;left:",L:"hour",m:"solid 1px #9898a5;solid 1px #d8d8e5;solid 1px #d8d8e5;solid 1px #9898a5",Y:"time.sep.min-sec",J:"px;top:0px;height:",n:"div",ya:"int",ta:"y4",E:'label="'};var
db=jsx3.gui.Event;var
M=jsx3.gui.Interactive;f.h5=jsx3.resolveURI(ub.a);f.v9=jsx3.resolveURI(ub.b);jsx3.html.loadImages(f.h5,f.v9);f.aG=new
jsx3.util.NumberFormat(ub.c);f.XM=new
jsx3.util.NumberFormat(ub.d);m.jsxshowsecs=0;m.jsxshowmillis=0;m.jsx24hour=null;m.init=function(s,p,l,c){this.jsxsuper(s,p,l,0,c);this.jsxhours=0;this.jsxminutes=0;this.jsxseconds=0;this.jsxmillis=0;};m.getShowSeconds=function(){return this.jsxshowsecs!=null?this.jsxshowsecs:m.jsxshowsecs;};m.getShowMillis=function(){return this.jsxshowmillis!=null?this.jsxshowmillis:m.jsxshowmillis;};m.is24Hour=function(){return this.jsx24hour!=null?this.jsx24hour:this.wi(ub.e);};m.setFontSize=function(p){this.jsxsuper(p);this.ce();return this;};m.setShowSeconds=function(h){this.jsxshowsecs=jsx3.Boolean.valueOf(h);this.ce();return this;};m.setShowMillis=function(l){this.jsxshowmillis=jsx3.Boolean.valueOf(l);this.ce();return this;};m.set24Hour=function(g){this.jsx24hour=g!=null?jsx3.Boolean.valueOf(g):null;this.ce();return this;};m.getHours=function(){return this.jsxhours||Number(0);};m.setHours=function(r){this.jsxhours=Math.max(0,Math.min(23,r));this.LQ();};m.getMinutes=function(){return this.jsxminutes||Number(0);};m.setMinutes=function(r){this.jsxminutes=Math.max(0,Math.min(59,r));this.LQ();};m.getSeconds=function(){return this.jsxseconds||Number(0);};m.setSeconds=function(n){this.jsxseconds=Math.max(0,Math.min(59,n));this.LQ();};m.getMilliseconds=function(){return this.jsxmillis||Number(0);};m.setMilliseconds=function(i){this.jsxmillis=Math.max(0,Math.min(999,i));this.LQ();};m.getDate=function(h){if(this.jsxhours==null&&this.jsxminutes==null)return null;if(h==null)h=new
Date();h.setHours(this.jsxhours||Number(0));h.setMinutes(this.jsxminutes||Number(0));h.setSeconds(this.jsxseconds||Number(0));h.setMilliseconds(this.jsxmillis||Number(0));return h;};m.setDate=function(a){if(a==null){this.jsxhours=this.jsxminutes=this.jsxseconds=this.jsxmillis=null;}else{this.jsxhours=a.getHours();this.jsxminutes=a.getMinutes();this.jsxseconds=a.getSeconds();this.jsxmillis=a.getMilliseconds();}this.LQ();};m.LQ=function(){var
va=this.getRendered();if(va!=null){var
Ma=va.childNodes[0].childNodes;for(var
Ya=0;Ya<Ma.length;Ya++){var
B=Ma[Ya];if(B.tagName&&B.tagName.toLowerCase()==ub.f&&B.getAttribute(ub.g)){var
zb=f.xw[B.getAttribute(ub.g)];var
Ba=zb.o0(this);B.value=Ba!=null?zb.b6(this,Ba):ub.h;}}}};m.Rc=function(d,a,n){if(a){delete d[ub.i];var
jb=this.Wl(true,d);var
yb=jb.recalculate(d,a,n);if(!yb.w&&!yb.h)return;var
qb=jb.lg(0);var
pb=a.childNodes[0];qb.recalculate({height:jb.ec()},pb,n);var
Xa=qb.lg(0);Xa.recalculate({height:qb.ec()},pb.childNodes[0],n);var
ra=qb.lg(1);ra.recalculate({height:qb.ec()},pb.childNodes[2],n);var
T=2;if(this.jsxshowsecs){T=T+2;var
fb=qb.lg(2);fb.recalculate({height:qb.ec()},pb.childNodes[T],n);if(this.jsxshowsecs&&this.jsxshowmillis){T=T+2;var
Ya=qb.lg(3);Ya.recalculate({height:qb.ec()},pb.childNodes[T],n);}}if(!this.is24Hour()){T=T+2;var
t=qb.lg(4);t.recalculate({height:qb.ec()},pb.childNodes[T],n);}var
Ta=qb.lg(5);T++;Ta.recalculate({height:qb.ec()},pb.childNodes[T],n);}};m.Gc=function(g){if(this.getParent()&&(g==null||isNaN(g.parentwidth)||isNaN(g.parentheight))){g=this.getParent().uf(this);}else if(g==null)g={};var
Mb=Math.round((this.getFontSize()||jsx3.gui.Block.DEFAULTFONTSIZE)*3/4);var
B=Mb;var
ha=Mb*2;var
y=Math.round(Mb*2.2);var
R=Mb*3;var
qb=this.getRelativePosition()!=0&&(!this.getRelativePosition()||this.getRelativePosition()==1);var
Sa=qb?null:this.getLeft();var
Ka=qb?null:this.getTop();if(!g.boxtype)g.boxtype=qb?ub.j:ub.k;g.tagname=ub.l;if(qb&&this.getMargin())g.margin=this.getMargin();if(g.left==null&&Sa!=null)g.left=Sa;if(g.top==null&&Ka!=null)g.top=Ka;if(g.height==null&&this.getHeight())g.height=this.getHeight();var
gb;if((gb=this.getBorder())!=null&&gb!=ub.h){g.border=gb;}else g.border=ub.m;var
bb;if((bb=this.getPadding())!=null&&bb!=ub.h)g.padding=bb;var
V=new
jsx3.gui.Painted.Box(g);var
la={};la.tagname=ub.n;la.boxtype=ub.o;la.height=V.ec();var
Ta=new
jsx3.gui.Painted.Box(la);V.Yf(Ta);var
Sa=0;la={tagname:ub.p,empty:true,boxtype:ub.k,left:Sa,top:0,padding:ub.q,width:ha,height:Ta.ec(),border:ub.r};Ta.Yf(new
jsx3.gui.Painted.Box(la));Sa=Sa+(ha+B);la={tagname:ub.p,empty:true,boxtype:ub.k,left:Sa,top:0,padding:ub.q,width:ha,height:Ta.ec(),border:ub.r};Ta.Yf(new
jsx3.gui.Painted.Box(la));Sa=Sa+(ha+B);la={tagname:ub.p,empty:true,boxtype:ub.k,left:Sa,top:0,padding:ub.q,width:ha,height:Ta.ec(),border:ub.r};Ta.Yf(new
jsx3.gui.Painted.Box(la));if(this.jsxshowsecs)Sa=Sa+(ha+B);la={tagname:ub.p,empty:true,boxtype:ub.k,left:Sa,top:0,padding:ub.q,width:R,height:Ta.ec(),border:ub.r};Ta.Yf(new
jsx3.gui.Painted.Box(la));if(this.jsxshowsecs&&this.jsxshowmillis)Sa=Sa+(R+B);la={tagname:ub.p,empty:true,boxtype:ub.k,left:Sa,top:0,padding:ub.q,width:y,height:Ta.ec(),border:ub.r};Ta.Yf(new
jsx3.gui.Painted.Box(la));if(!this.is24Hour())Sa=Sa+y;la={tagname:ub.l,boxtype:ub.k,left:Sa,top:0,padding:ub.s,width:12,height:Ta.ec(),border:ub.t};Ta.Yf(new
jsx3.gui.Painted.Box(la));Sa=Sa+12;Ta.recalculate({width:Sa});V.recalculate({width:Sa+V.getBorderWidth()});return V;};m.paint=function(){this.applyDynamicProperties();var
Ka=this.getEnabled()==1?this.getBackgroundColor():this.getDisabledBackgroundColor();var
Lb=this.getEnabled()==1?this.Gj(ub.u,ub.v,2)+this.Gj(ub.w,ub.x,2)+this.Gj(ub.y,ub.z,2)+this.Gj(ub.A,ub.B,2):ub.h;Lb=Lb+(this.Fo()+this.wn());var
wa=this.Wl(true);wa.setAttributes(ub.C+this.getId()+ub.D+ub.E+this.getName()+ub.F+this.bn()+this.jh(null,0)+this.renderAttributes(null,true));wa.setStyles(this.zk()+this.bd()+this.bi()+this.fh()+(Ka!=null?ub.G+Ka+ub.H:ub.h));var
Fb=Math.round((this.getFontSize()||jsx3.gui.Block.DEFAULTFONTSIZE)*3/4);var
Bb=this.eh()+this.Bf()+this.ad()+this.Yh();var
Ba=this.eh()+this.Bf()+this.ad()+this.Yh()+ub.I+Fb+ub.J+wa.lg(0).lg(0).ec()+ub.K;var
mb=this.jsxhours!=null?f.xw[ub.L].b6(this,this.jsxhours):ub.h;var
V=this.jsxminutes!=null?f.xw[ub.M].b6(this,this.jsxminutes):ub.h;var
fb=wa.lg(0);fb.setAttributes(ub.N);var
bb=ub.h;var
Va=0;var
Ab=fb.lg(0);Ab.setAttributes(Lb+ub.O+mb+ub.P);Ab.setStyles(Bb);bb=bb+Ab.paint().join(ub.h);Va=Va+Ab.cm();var
C=fb.lg(1);C.setAttributes(Lb+ub.Q+V+ub.P);C.setStyles(Bb);bb=bb+(ub.R+Ba+ub.S+Va+ub.T+this.wi(ub.U)+ub.V);bb=bb+C.paint().join(ub.h);Va=Va+(C.cm()+Fb);if(this.jsxshowsecs){var
wb=this.jsxseconds!=null?f.xw[ub.W].b6(this,this.jsxseconds):ub.h;var
Sa=fb.lg(2);Sa.setAttributes(Lb+ub.X+wb+ub.P);Sa.setStyles(Bb);bb=bb+(ub.R+Ba+ub.S+Va+ub.T+this.wi(ub.Y)+ub.V);bb=bb+Sa.paint().join(ub.h);Va=Va+(Sa.cm()+Fb);if(this.jsxshowmillis){var
Q=this.jsxmillis!=null?f.xw[ub.Z].b6(this,this.jsxmillis):ub.h;var
ga=fb.lg(3);ga.setAttributes(Lb+ub._+Q+ub.P);ga.setStyles(Bb);bb=bb+(ub.R+Ba+ub.S+Va+ub.T+this.wi(ub.aa)+ub.V);bb=bb+ga.paint().join(ub.h);Va=Va+(ga.cm()+Fb);}}if(!this.is24Hour()){var
x=this.jsxhours!=null?this.wi(ub.ba)[this.jsxhours<12?0:1]:ub.h;var
Ea=fb.lg(4);Ea.setAttributes(Lb+ub.ca+x+ub.P);Ea.setStyles(Bb);bb=bb+(ub.R+Ba+ub.da+Va+ub.T+this.wi(ub.ea)+ub.V);bb=bb+Ea.paint().join(ub.h);}var
lb=fb.lg(5);var
J=this.getEnabled()==1?ub.fa:ub.h;lb.setAttributes(jsx3.html.Kf+ub.ga);var
Db=ub.ha+jsx3.html.Kf+ub.ia+J+ub.ja+f.h5+ub.P+this.Gj(ub.ka,ub.la,3)+this.Gj(ub.ma,ub.na,3)+ub.oa+ub.ha+jsx3.html.Kf+ub.pa+J+ub.ja+f.v9+ub.P+this.Gj(ub.ka,ub.qa,3)+this.Gj(ub.ma,ub.na,3)+ub.oa;return wa.paint().join(fb.paint().join(bb+lb.paint().join(Db)+ub.ra));};f.xw={hour:{y4:function(d,b){if(isNaN(b))b=d.is24Hour()?-1:0;b++;b=d.is24Hour()?b%24:(b-1)%12+1;return b;},MS:function(o,j){if(isNaN(j))j=o.is24Hour()?24:13;j--;j=o.is24Hour()?jsx3.util.numMod(j,24):jsx3.util.numMod(j-1,12)+1;return j;},b6:function(c,k){if(k==null)return ub.h;return c.is24Hour()?f.aG.format(k):(jsx3.util.numMod(k-1,12)+1).toString();},BN:function(i,p){if(p==null||p===ub.h){i.jsxhours=null;}else if(isNaN(p)){i.jsxhours=0;}else if(i.is24Hour()||p==null){i.jsxhours=Number(p);}else{var
Wa=i.wi(ub.ba);var
P=i.R1(ub.sa);if(P!=null&&P.value!=null&&P.value.toLowerCase()==Wa[1].toLowerCase())i.jsxhours=Number(p)+12;else i.jsxhours=Number(p);}},o0:function(b){if(b.is24Hour()||b.jsxhours==null){return b.jsxhours;}else return jsx3.util.numMod(b.jsxhours-1,12)+1;},CO:function(q){return q.R1(ub.M);},zE:function(n){return null;},IO:function(s,r,q,h){if(!(q>=48&&q<=57)||h)return true;var
Ha=r.value;jsx3.sleep(function(){if(s.getParent()==null)return;var
D=r.value;if(Ha==D)r.value=D=String.fromCharCode(q);var
Cb=Number(r.value);if(!isNaN(Cb)){if(Cb>(s.is24Hour()?23:12)){r.value=String.fromCharCode(q);Cb=Number(r.value);}if(Cb>(s.is24Hour()?2:1))this.CO(s).focus();}},null,this);}},minute:{y4:function(b,d){if(isNaN(d))d=0;d++;return d%60;},MS:function(h,q){if(isNaN(q))q=60;q--;return jsx3.util.numMod(q,60);},b6:function(a,e){if(e==null)return ub.h;return f.aG.format(e);},BN:function(b,d){if(isNaN(d)){b.jsxminutes=0;}else b.jsxminutes=Number(d);},o0:function(o){return o.jsxminutes;},CO:function(i){return i.R1(i.jsxshowsecs?ub.W:ub.sa);},zE:function(o){return o.R1(ub.L);},IO:function(q,a,o,j){if(!(o>=48&&o<=57)||j)return true;var
ta=a.value;jsx3.sleep(function(){if(q.getParent()==null)return;var
ia=a.value;if(ta==ia)a.value=ia=String.fromCharCode(o);var
Xa=Number(a.value);if(!isNaN(Xa)){if(Xa>=60){a.value=String.fromCharCode(o);Xa=Number(a.value);}if(Xa>=6){var
Ba=this.CO(q);if(Ba)Ba.focus();}}},null,this);}},second:{y4:function(j,o){if(isNaN(o))o=0;o++;return o%60;},MS:function(l,n){if(isNaN(n))n=60;n--;return jsx3.util.numMod(n,60);},b6:function(a,s){if(s==null)return ub.h;return f.aG.format(s);},BN:function(j,o){if(isNaN(o)){j.jsxseconds=0;}else j.jsxseconds=Number(o);},o0:function(h){return h.jsxseconds;},CO:function(e){return e.R1(e.jsxshowmillis?ub.Z:ub.sa);},zE:function(a){return a.R1(ub.M);},IO:function(n,d,r,e){return f.xw[ub.M].IO.call(this,n,d,r,e);}},milli:{y4:function(c,k){if(isNaN(k))k=0;k++;return k%1000;},MS:function(d,b){if(isNaN(b))b=1000;b--;return jsx3.util.numMod(b,1000);},b6:function(a,s){if(s==null)return ub.h;return f.XM.format(s);},BN:function(d,b){if(isNaN(b)){d.jsxmillis=0;}else d.jsxmillis=Number(b);},o0:function(n){return n.jsxmillis;},CO:function(s){return s.R1(ub.sa);},zE:function(e){return e.R1(ub.W);},IO:function(b,p,k,s){if(!(k>=48&&k<=57)||s)return true;var
x=p.value;jsx3.sleep(function(){if(b.getParent()==null)return;var
P=p.value;if(x==P)p.value=P=String.fromCharCode(k);var
Sa=Number(p.value);if(!isNaN(Sa))if(P.length==3){var
ha=this.CO(b);if(ha)ha.focus();}},null,this);}},ampm:{y4:function(o,j){var
Ia=o.wi(ub.ba);return j!=null&&j.toLowerCase()==Ia[0].toLowerCase()?Ia[1]:Ia[0];},MS:function(k,n){var
ob=k.wi(ub.ba);return n!=null&&n.toLowerCase()==ob[1].toLowerCase()?ob[0]:ob[1];},b6:function(g,r){return r;},BN:function(o,j){var
ta=o.wi(ub.ba);var
Pa=Number(o.R1(ub.L).value);if(!isNaN(Pa))if(j!=null&&j.toLowerCase()==ta[1].toLowerCase())o.jsxhours=Pa+12;else o.jsxhours=Pa;},o0:function(s){return s.wi(ub.ba)[s.jsxhours<12?0:1];},CO:function(e){return null;},zE:function(j){return j.R1(j.jsxshowsecs?j.jsxshowmillis?ub.Z:ub.W:ub.M);},IO:function(i,c,d,r){var
Y=String.fromCharCode(d);var
ga=i.wi(ub.ba);for(var
E=0;E<ga.length;E++)if(Y==ga[E].charAt(0).toUpperCase()){c.value=ga[E];break;}return true;}}};m.zy=function(a,l){if(a.hasModifier(true)||a.isModifierKey())return;var
gb=l.getAttribute(ub.g);var
Da=f.xw[gb];var
La=a.keyCode();if(La>=96&&La<=105)La=La+(48-96);if(La==38||La==40){var
oa=La==38?ub.ta:ub.ua;var
ka=Da[oa](this,l.value);if(this.doEvent(ub.va,{objEVENT:a,strFIELD:gb})!==false){l.value=Da.b6(this,ka);Da.BN(this,ka);}}else if(La==9||La==13){var
ua=a.shiftKey()?Da.zE(this):Da.CO(this);if(ua==null)return;ua.focus();}else if(La>=48&&La<=57||La>=65&&La<=90){var
O=Da.IO(this,l,La,a.shiftKey());if(!O)return;}else if(a.isArrowKey()||a.isFunctionKey()||a.escapeKey()||La==8||La==46){return;}else{}a.cancelAll();};m.HW=function(a,g){var
Ha=g.getAttribute(ub.g);var
qa=f.xw[Ha];var
ca=qa.o0(this);var
eb=jsx3.util.numIsNaN(g.value)?jsx3.util.strEmpty(g.value)?null:g.value:Number(g.value);if(ca!==eb){if(this.doEvent(ub.va,{objEVENT:a,strFIELD:Ha})!==false){qa.BN(this,eb);if(eb!=null)g.value=qa.b6(this,qa.o0(this));}else g.value=qa.b6(this,ca);}else g.value=qa.b6(this,ca);var
ua=this;this._jsxj2=window.setTimeout(function(){delete ua[ub.wa];delete ua[ub.xa];},0);};m.P3=function(e,s){if(this._jsxj2){window.clearTimeout(this._jsxj2);delete this[ub.wa];}this._jsxhI=s.getAttribute(ub.g);};m.wy=function(r,n){var
wa=r.getWheelDelta();if(wa!=0){var
lb=n.getAttribute(ub.g);var
za=f.xw[lb];var
_=wa>0?ub.ta:ub.ua;var
E=za[_](this,n.value);if(this.doEvent(ub.va,{objEVENT:r,strFIELD:lb})!==false){n.value=za.b6(this,E);za.BN(this,E);}r.cancelBubble();}};m.YO=function(e,s){db.publish(e);e.cancelAll();};m.V3=function(q,o){this.oB(q,o,ub.ta);};m.c2=function(n,r){this.oB(n,r,ub.ua);};m.oB=function(n,r,h){if(this.getEnabled()!=1)return;var
ua=this._jsxhI||ub.L;var
ib=this.R1(ua);var
da=f.xw[ua];var
u=da[h](this,ib.value);if(this.doEvent(ub.va,{objEVENT:n,strFIELD:ua})!==false){ib.value=da.b6(this,u);da.BN(this,u);if(this._jsxhI==null)ib.focus();}};m.R1=function(d){var
yb=this.getRendered();if(yb!=null){var
vb=yb.childNodes[0].childNodes;for(var
G=0;G<vb.length;G++){var
_=vb[G];if(_.getAttribute&&_.getAttribute(ub.g)==d)return _;}}return null;};m.emSetValue=function(o){var
E=this.emGetSession();var
ja=null;if(jsx3.util.strEmpty(o)){E.datetype=ub.ya;}else if(!isNaN(o)&&!isNaN(parseInt(o))){ja=new
Date();ja.setTime(parseInt(o));E.datetype=ub.ya;}else{ja=new
Date(o);if(isNaN(ja)){ja=null;}else E.datetype=ub.za;}E.olddate=ja;this.setDate(ja);};m.emGetValue=function(){var
z=this.emGetSession();var
Pa=this.getDate();var
Fb=this.emGetSession().datetype||ub.h;if(Pa==null)return null;switch(Fb){case ub.za:return Pa.toString();default:return Pa.getTime().toString();}};m.emFocus=function(){var
ta=this.getRendered();if(ta)ta.childNodes[0].childNodes[0].focus();};});
