/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.CheckBox",jsx3.gui.Block,[jsx3.gui.Form],function(n,f){var
ub={k:"div",h:"",c:"visible",H:"<$1><xsl:if test=\"{0}='1'\"><xsl:attribute name=\"checked\">checked</xsl:attribute></xsl:if></input>",p:"background-color:#999999;",q:' id="',G:/<(input .*?)\/>/g,v:' type="checkbox" name="',i:"relativebox",A:"&#160;",a:"jsx30checkbox",F:"disabled",u:' class="jsx30checkbox_tristate" ',t:"overflow-x:hidden;",j:"box",s:' class="',z:";",d:"hidden",o:"0/0/0",D:"3.0.00",w:'" ',r:'"',C:" ",B:' checked="checked" ',l:"inline",g:"span",b:"jsxtoggle",m:"input[checkbox]",f:"keydown",y:"visibility:",n:"0 0 0 18",x:' class="jsx30checkbox_partial" ',E:"tM",e:"click"};n.UNCHECKED=0;n.CHECKED=1;n.PARTIAL=2;n.DEFAULTCLASSNAME=ub.a;f.init=function(g,r,m,e,b,q,d){this.jsxsuper(g,r,m,e,b,q);this.setDefaultChecked(d==null?1:d);this.jsxchecked=d;};f.getDefaultChecked=function(){return this.jsxdefaultchecked;};f.setDefaultChecked=function(c){this.jsxdefaultchecked=c;return this;};f.getChecked=function(){return this.jsxchecked!=null?this.jsxchecked:this.getDefaultChecked();};f.setChecked=function(l){if(this.jsxchecked!=l){this.jsxchecked=l;this.rI();if(this.isOldEventProtocol())this.doEvent(ub.b,{intCHECKED:l});}return this;};f.Tg=function(s,m){this.focus(m);if(!s.leftButton()&&s.isMouseEvent())return;if(this.getEnabled()==1){var
Va=this.getChecked()==1?0:1;this.jsxchecked=Va;this.rI(m);this.doEvent(ub.b,{objEVENT:s,intCHECKED:Va});}};f.rI=function(l){if(l==null)l=this.getRendered();if(l!=null){jsx3.html.selectSingleElm(l,0,0,0).checked=this.jsxchecked==1;jsx3.html.selectSingleElm(l,0,0,1).style.visibility=this.jsxchecked==2?ub.c:ub.d;}};f.oe=function(j,c){if(j.enterKey()){this.Tg(j,c);j.cancelAll();}};n.Tj={};n.Tj[ub.e]=true;n.Tj[ub.f]=true;f.Rc=function(b,r,p){this.Gi(b,r,p,3);};f.Gc=function(h){if(this.getParent()&&(h==null||isNaN(h.parentwidth)||isNaN(h.parentheight))){h=this.getParent().uf(this);}else if(h==null)h={};var
A=this.getRelativePosition()!=0&&(!this.getRelativePosition()||this.getRelativePosition()==1);var
ta,Cb,mb,La;if(h.tagname==null)h.tagname=ub.g;if((ta=this.getBorder())!=null&&ta!=ub.h)h.border=ta;if(A&&(Cb=this.getMargin())!=null&&Cb!=ub.h)h.margin=Cb;if(!h.boxtype)h.boxtype=A?ub.i:ub.j;if(h.left==null)h.left=!A&&!jsx3.util.strEmpty(this.getLeft())?this.getLeft():0;if(h.top==null)h.top=!A&&!jsx3.util.strEmpty(this.getTop())?this.getTop():0;if(h.height==null)h.height=(mb=this.getHeight())!=null?mb:18;if(h.width==null)if((La=this.getWidth())!=null)h.width=La;var
Mb=new
jsx3.gui.Painted.Box(h);var
Lb={};Lb.tagname=ub.k;Lb.boxtype=ub.l;var
db=new
jsx3.gui.Painted.Box(Lb);Mb.Yf(db);var
Lb={};Lb.tagname=ub.g;Lb.boxtype=ub.j;Lb.width=16;Lb.parentheight=Mb.ec();Lb.height=18;Lb.left=0;Lb.top=0;var
Fb=new
jsx3.gui.Painted.Box(Lb);db.Yf(Fb);var
Lb={};Lb.tagname=ub.m;Lb.empty=true;Lb.omitpos=true;var
Ya=new
jsx3.gui.Painted.Box(Lb);Fb.Yf(Ya);var
Lb={};Lb.tagname=ub.g;Lb.boxtype=ub.j;Lb.left=3;Lb.top=7;Lb.width=7;Lb.height=2;var
ra=new
jsx3.gui.Painted.Box(Lb);Fb.Yf(ra);var
Lb={};Lb.tagname=ub.g;Lb.boxtype=ub.l;Lb.top=2;Lb.parentheight=Mb.ec();Lb.height=18;Lb.padding=ub.n;var
_=new
jsx3.gui.Painted.Box(Lb);Mb.Yf(_);return Mb;};f.focus=function(k){if(!k)k=this.getRendered();if(k)jsx3.html.selectSingleElm(k,ub.o).focus();};f.paint=function(){this.applyDynamicProperties();var
pb=this.getEnabled()==1;var
pa=pb?this.jh(n.Tj,0):ub.h;var
Mb=this.renderAttributes(null,true);var
Va=this.getChecked()==2?ub.c:ub.d;var
U=pb?ub.h:ub.p;var
lb=this.Wl(true);lb.setAttributes(ub.q+this.getId()+ub.r+this.on()+ub.s+this.to()+ub.r+this.bn()+this.wn()+pa+Mb);lb.setStyles((lb.cm()?ub.t:ub.h)+this.Xe(true)+this.ad()+this.Yh()+this.Bf()+this.eh()+this.zf()+this.bd()+this.bi()+this.zk()+this.fh());var
Ca=lb.lg(0);var
Hb=Ca.lg(0);Hb.setAttributes(ub.u);var
C=Hb.lg(0);C.setAttributes(ub.v+this.getName()+ub.w+this.wn()+this.fx()+this.Fo());var
E=Hb.lg(1);E.setAttributes(ub.x);E.setStyles(ub.y+Va+ub.z+U);var
Xa=lb.lg(1);Xa.setAttributes(jsx3.html.Kf);return lb.paint().join(Ca.paint().join(Hb.paint().join(C.paint().join(ub.h)+E.paint().join(ub.A))+Xa.paint().join(this.wg())));};f.fx=function(){return this.getChecked()==1?ub.B:ub.h;};f.to=function(){var
J=this.getClassName();return n.DEFAULTCLASSNAME+(J?ub.C+J:ub.h);};f.doValidate=function(){this.setValidationState(this.getRequired()==0||this.getChecked()==1?1:0);return this.getValidationState();};n.getVersion=function(){return ub.D;};f.emGetType=function(){return jsx3.gui.Matrix.EditMask.FORMAT;};f.emInit=function(k){this.jsxsupermix(k);this.subscribe(ub.b,this,ub.E);};f.emSetValue=function(o){this.jsxchecked=Number(o)==1?1:0;};f.emGetValue=function(){var
Z=this.emGetSession();if(Z)return Z.column.getValueForRecord(Z.recordId);return null;};f.emBeginEdit=function(l,c,q,h,m,a,o){var
D=jsx3.html.selectSingleElm(o,0,0,0,0,0);if(D&&!D.getAttribute(ub.F)){this.jsxsupermix(l,c,q,h,m,a,o);D.focus();}else return false;};f.emPaintTemplate=function(){this.jsxchecked=0;this.setText(ub.h);this.setEnabled(0);var
B=this.paint();this.setEnabled(1);var
t=this.paint();var
ib=this.Tn(t,B);ib=ib.replace(ub.G,ub.H);return ib;};f.tM=function(o){var
Da=this.emGetSession();if(Da){var
va=o.context.intCHECKED;this.jsxchecked=va;Da.column.setValueForRecord(Da.recordId,va.toString());}};});jsx3.CheckBox=jsx3.gui.CheckBox;
