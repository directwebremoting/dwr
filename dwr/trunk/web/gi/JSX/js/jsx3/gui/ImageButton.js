/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.ImageButton",jsx3.gui.Block,[jsx3.gui.Form],function(p,n){var
ub={o:'<img src="',d:"mousedown",k:"jsx30imagebutton_disabled",w:"jsxexecute",r:"relativebox",c:"mouseout",h:'"',p:"/>",g:'id="',l:' width="',q:"span",b:"mouseover",v:"jsxtoggle",i:' class="',m:"",a:"click",u:"inline",f:"keydown",t:"div",j:"jsx30imagebutton",n:' height="',s:"box",e:"mouseup"};var
Va=jsx3.gui.Event;var
Za=jsx3.gui.Interactive;var
ga=jsx3.gui.Form;p.TYPE_NORMAL=0;p.TYPE_TOGGLE=1;p.STATE_OFF=0;p.STATE_ON=1;n.jsximage=null;n.jsxoverimage=null;n.jsxdownimage=null;n.jsxonimage=null;n.jsxdisabledimage=null;n.jsxprefetch=1;n.jsxtype=0;n.jsxstate=0;n._jsxRL=null;n.init=function(s,i,d,q,k){this.jsxsuper(s,i,d,q,k);};n.getImage=function(){return this.jsximage;};n.setImage=function(g){this.jsximage=g;return this;};n.getOverImage=function(){return this.jsxoverimage;};n.setOverImage=function(s){this.jsxoverimage=s;return this;};n.getDownImage=function(){return this.jsxdownimage;};n.setDownImage=function(q){this.jsxdownimage=q;return this;};n.getOnImage=function(){return this.jsxonimage;};n.setOnImage=function(m){this.jsxonimage=m;return this;};n.getDisabledImage=function(){return this.jsxdisabledimage;};n.setDisabledImage=function(c){this.jsxdisabledimage=c;return this;};n.getState=function(){return this.jsxstate;};n.setState=function(c){this.jsxstate=c;var
rb=this.getRendered();if(rb!=null)rb.src=this.BO(false,false);return this;};n.getType=function(){return this.jsxtype;};n.setType=function(k){this.jsxtype=k;return this;};n.isPreFetch=function(){return this.jsxprefetch;};n.setPreFetch=function(d){this.jsxprefetch=jsx3.Boolean.valueOf(d);return this;};n.setEnabled=function(c,e){if(this._jsxRL!=null)this._jsxRL.setEnabled(c==1);return this.jsxsupermix(c,e);};p.Tj={};p.Tj[ub.a]=true;p.Tj[ub.b]=true;p.Tj[ub.c]=true;p.Tj[ub.d]=true;p.Tj[ub.e]=true;p.Tj[ub.f]=true;n.paint=function(){this.applyDynamicProperties();var
Pa=this.getEnabled()==1;var
mb=this.getKeyBinding();if(mb){var
P=this;if(this._jsxRL!=null)this._jsxRL.destroy();this._jsxRL=this.doKeyBinding(function(i){P.Tg(i,P.getRendered());},mb);this._jsxRL.setEnabled(Pa);}var
Kb=this.jh(Pa?p.Tj:null,0);var
ca=this.renderAttributes(null,true);var
ka=this.Wl(true);ka.setAttributes(ub.g+this.getId()+ub.h+this.on()+this.Fo()+this.bn()+ub.i+(Pa?ub.j:ub.k)+ub.h+Kb+ca);ka.setStyles(this.Xe(true)+this.bd()+this.bi()+this.zk()+this.zf()+this.fh());var
Hb=ka.mn();Hb=Hb!=null?ub.l+Hb+ub.h:ub.m;var
na=ka.ec();na=na!=null?ub.n+na+ub.h:ub.m;var
jb=ub.o+this.BO(false,false)+ub.h+Hb+na+ub.p;if(!this._jsxDB&&this.isPreFetch()){var
nb=this.getUriResolver();var
eb=[this.getImage(),this.getOverImage(),this.getDownImage(),this.getOnImage(),this.getDisabledImage()].map(function(a){return a?nb.resolveURI(a):null;});jsx3.html.loadImages(eb);this._jsxDB=true;}var
S=ka.lg(0);return ka.paint().join(S.paint().join(jb));};n.Gc=function(){var
Qa=this.getRelativePosition()!=0;var
D={};if(!Qa&&!jsx3.util.strEmpty(this.getLeft()))D.left=this.getLeft();if(!Qa&&!jsx3.util.strEmpty(this.getTop()))D.top=this.getTop();if(!(this.getWidth()==null||isNaN(this.getWidth())))D.width=this.getWidth();if(!(this.getHeight()==null||isNaN(this.getHeight())))D.height=this.getHeight();D.tagname=ub.q;D.boxtype=Qa||D.left==null||D.top==null?ub.r:ub.s;if(this.getPadding()!=null)D.padding=this.getPadding();if(Qa&&this.getMargin()!=null)D.margin=this.getMargin();if(this.getBorder()!=null)D.border=this.getBorder();var
Fb=new
jsx3.gui.Painted.Box(D);D={tagname:ub.t,boxtype:ub.u,height:Fb.ec()};Fb.Yf(new
jsx3.gui.Painted.Box(D));return Fb;};n.Rc=function(e,b,m){this.Gi(e,b,m,1);};n.BO=function(o,i){var
Ib=null;if(this.getEnabled()==1){if(o)Ib=this.getOverImage();else if(i)Ib=this.getDownImage();if(this.getType()==1&&this.getState()==1)Ib=Ib||this.getOnImage();}else Ib=this.getDisabledImage();Ib=Ib||this.getImage();return Ib?this.getUriResolver().resolveURI(Ib):ub.m;};n.Tg=function(e,h){if(!e.leftButton()&&e.isMouseEvent())return;if(this.getType()==1){var
Fa=this.getState()==0?1:0;var
M=this.doEvent(ub.v,{objEVENT:e,intSTATE:Fa});if(M!==false){this.setState(Fa);h.childNodes[0].childNodes[0].src=this.BO(false,false);}}this.doEvent(ub.w,{objEVENT:e});};n.el=function(o,q){q.childNodes[0].childNodes[0].src=this.BO(true,false);};n.Qi=function(d,i){i.childNodes[0].childNodes[0].src=this.BO(false,false);};n.Dc=function(h,e){if(h.leftButton())e.childNodes[0].childNodes[0].src=this.BO(false,true);};n.Df=function(f,g){if(f.rightButton())this.jsxsupermix(f,g);else if(f.leftButton())g.childNodes[0].childNodes[0].src=this.BO(false,false);};n.oe=function(a,l){if(a.enterKey()||a.spaceKey()){this.Tg(a,l);a.cancelAll();}};n.doValidate=function(){var
Ua=null;if(this.getType()==p.NORMAL)Ua=1;else Ua=this.getState()==1||this.getRequired()==0?1:0;this.setValidationState(Ua);return Ua;};n.emGetType=function(){return jsx3.gui.Matrix.EditMask.FORMAT;};n.emInit=function(f){this.jsxsupermix(f);};n.emSetValue=function(a){};n.emGetValue=function(){return null;};n.emBeginEdit=function(m,b,d,r,l,c,s){var
yb=s.childNodes[0].childNodes[0];if(yb){this.jsxsupermix(m,b,d,r,l,c,s);yb.focus();}else return false;};n.emPaintTemplate=function(){this.setEnabled(0);var
Ja=this.paint();this.setEnabled(1);var
zb=this.paint();return this.Tn(zb,Ja);};});
