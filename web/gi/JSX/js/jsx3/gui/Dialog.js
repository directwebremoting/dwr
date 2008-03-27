/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Alerts","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Dialog",jsx3.gui.Block,[jsx3.gui.Alerts],function(b,r){var
ub={Qa:" ",_:"display:none;",ea:"height:",q:"@Min Icon",ra:"<span ",V:"box",v:"@Close Icon",U:"div",u:"this.getParent().getParent().doClose();",ka:' style="width:100%;height:100%;position:absolute;left:0px;top:0px;z-index:',z:"display",d:"jsx:///images/dialog/min.gif",La:"'); d.doToggleState(d.isFront() ? jsx3.gui.Dialog.MINIMIZED : jsx3.gui.Dialog.MAXIMIZED); false;",D:"BeforeMove model event error for the control, ",fa:"px;",R:"tG",w:"@Close Tip",Ca:'class="jsx30dialog_mask"',Q:"jsxbeforeresize",Na:"overflow:hidden;",Pa:"jsx30dialog",la:';"',qa:">&#160;</div>",y:"none",x:"",ja:"jX",W:"2 2 2 2",Fa:");",Ha:"overflow:hidden;z-index:1;background-image:url(",i:"jsx3.gui.WindowBar",Sa:"3.0.00",A:"focus",t:"_btn_close",Ia:"&#160;",s:"@Min Tip",X:"1px pseudo",r:"jsxtip",sa:' style="position:absolute;left:-1px;top:0px;width:1px;height:1px;overflow:hidden;"></span></div>',C:"doEndMove",l:"JSXFRAG",L:"doEndMove: ",ia:"keydown",Ka:"var d = jsx3.GO('",J:"AfterMove model event error for the control, ",Da:"overflow:hidden;visibility:hidden;",ta:'label="',oa:"xY",O:"@Restore Tip",k:"_cbar",Ga:"yF",p:"jsximage",P:"number",I:"body",ha:'" ',a:"#e8e8f5",Aa:"<span style='display:none;'>&#160;</span>",F:"hidden",Ra:"#FFFFFF",j:"jsx3.gui.ToolbarButton",ua:'"',S:"_jsxEz",Ma:"repaint",K:"jsxaftermove",aa:"jsxkeypress",B:"mouseup",g:"JSX/images/dialog/restore.gif",b:"jsx:///images/dialog/window.gif",va:' class="',M:"_jsxAW",f:"JSX/images/dialog/max.gif",T:"jsxafterresize",ga:'<div id="',ba:"keypress",e:"jsx:///images/dialog/resize.gif",za:";",h:"JSXTBB_",c:"jsx:///images/dialog/close.gif",H:"px",wa:"z-index:1;",G:"-1px",Ja:"jsxmodal",ma:">",ca:"mousedown",pa:"Qq",N:"@Max Tip",o:"jsxexecute",Z:"0",xa:'id="',na:'<div class="jsx30dialog_modal"',Ba:'class="jsx30dialog_content"',da:"wV",Oa:"overflow:auto;",m:"_btn_min",Y:"100%",Ea:"overflow:hidden;cursor:se-resize;z-index:12;background-image:url(",n:"this.getParent().getParent().doToggleState();",ya:"z-index:",E:".\nDescription:\n"};var
Ea=jsx3.gui.Event;var
Ja=jsx3.gui.Interactive;var
sb=jsx3.util.Logger.getLogger(b.jsxclass.getName());b.MINIMIZED=0;b.MAXIMIZED=1;b.DEFAULTBACKGROUNDCOLOR=ub.a;b.FIXED=0;b.RESIZEABLE=1;b.RESIZABLE=1;b._G=jsx3.resolveURI(ub.b);b.wv=jsx3.resolveURI(ub.c);b.Jv=jsx3.resolveURI(ub.d);b.lX=jsx3.resolveURI(ub.e);b.MODAL=1;b.NONMODAL=0;b.GV=ub.f;b.sR=ub.g;b.cR=[null,32,32,32];b.IR=[10,10,10,10];b.aN=10;b.AD=ub.h;r.init=function(q,o,l,m){this.jsxsuper(q,null,null,o,l);jsx3.require(ub.i,ub.j);var
Fa=new
jsx3.gui.WindowBar(q+ub.k);if(m!=null)Fa.setText(m);this.setChild(Fa,1,null,ub.l);var
J=new
jsx3.gui.ToolbarButton(this.getName()+ub.m,null);J.setEvent(ub.n,ub.o);J.setDynamicProperty(ub.p,ub.q);J.setDynamicProperty(ub.r,ub.s);Fa.setChild(J,1,null,ub.l);J=new
jsx3.gui.ToolbarButton(this.getName()+ub.t,null);J.setEvent(ub.u,ub.o);J.setDynamicProperty(ub.p,ub.v);J.setDynamicProperty(ub.r,ub.w);Fa.setChild(J,1,null,ub.l);};r.onAfterAssemble=function(m,o){if(this.getWindowState()==1)this.setZIndex(o.getNextZIndex(jsx3.app.Server.Z_DIALOG));};r.getMaskProperties=function(){return this.getModal()==0?jsx3.gui.Block.MASK_ALL_EDIT:jsx3.gui.Block.MASK_NO_EDIT;};r.doToggleState=function(n){var
Fb=n!=null?n:this.getWindowState()==1?0:1;var
M;if((M=this.getTaskButton())!=null){this.setWindowState(Fb);var
sa=null;if(Fb==1){this.setDisplay(ub.x,true);this.setZIndex(this.getServer().getNextZIndex(jsx3.app.Server.Z_DIALOG)*this.getZMultiplier(),true);sa=this;}else{this.setDisplay(ub.y,true);sa=M;}M.setState(Fb==1?1:0);sa.focus();}else{var
Gb=this.NK();if(Fb==1){Gb.childNodes[1].style.display=ub.x;var
ya=this.Wl(true);ya.recalculate({height:this.getHeight()},Gb);var
eb=ya.lg(1);eb.recalculate({height:this.getHeight()},Gb.childNodes[2]);if(this.getResize()){Gb.childNodes[3].style.display=ub.x;Gb.childNodes[4].style.display=ub.x;}this.setZIndex(this.getServer().getNextZIndex(jsx3.app.Server.Z_DIALOG)*this.getZMultiplier(),true);this.setWindowState(1);}else{Gb.childNodes[1].style.display=ub.y;var
ya=this.Wl(true);ya.recalculate({height:32},Gb);var
eb=ya.lg(1);eb.recalculate({height:32},Gb.childNodes[2]);if(this.getResize()){Gb.childNodes[3].style.display=ub.y;Gb.childNodes[4].style.display=ub.y;}this.setWindowState(0);}}};r.setDisplay=function(d,f){this.jsxdisplay=d;if(f)this.updateGUI(ub.z,d);return this;};r.updateGUI=function(a,e){var
Ia=this.NK();if(Ia!=null)try{Ia.style[a]=e;}catch(Kb){}};r.focus=function(k){if(!k){jsx3.sleep(function(){this.focus(true);},ub.A+this.getId(),this);return;}var
u=this.NK();if(!u)return;if(this.getWindowState()==0){if(this.getTaskButton())this.doToggleState(1);else this.NK().focus();}else{var
Ka=jsx3.app.Browser.isTableMoveBroken();if(Ka)u.childNodes[1].style.display=ub.y;this.setZIndex(this.getServer().getNextZIndex(jsx3.app.Server.Z_DIALOG),true);var
Hb=this.getCaptionBar();if(Hb!=null)Hb.focus();else this.NK().focus();if(Ka)u.childNodes[1].style.display=ub.x;}};r.isFront=function(){if(this.getWindowState()==0)return false;var
lb=this.getParent();var
rb=lb.getDescendantsOfType(b,true);for(var
mb=0;mb<rb.length;mb++)if(rb[mb]!=this&&rb[mb].getWindowState()==1&&rb[mb].getZIndex()>this.getZIndex())return false;return true;};r.wV=function(l,a){if(!this.isFront()){var
D=this.Ky();var
Ya=D.style.display;var
z=jsx3.app.Browser.isTableMoveBroken();if(Ya!=ub.y&&z)D.style.display=ub.y;this.setZIndex(this.getServer().getNextZIndex(jsx3.app.Server.Z_DIALOG),true);if(Ya!=ub.y&&z)D.style.display=Ya;}};r.Ky=function(){var
Za=this.NK();return Za!=null?Za.childNodes[1]:null;};r.getTaskButton=function(c){if(c==null){var
ab=this.getServer();if(ab!=null)c=ab.getTaskBar();}if(c!=null)return c.getChild(b.AD+this.getId());return null;};r.paintChild=function(m,o){var
fa=this.NK();if(fa!=null)if(this.getCaptionBar()==m){this.repaint();}else this.jsxsuper(m,o,fa.childNodes[1]);};r.doClose=function(){this.getParent().removeChild(this);};r.onSetParent=function(d){var
fa=this.getServer();if(fa!=null&&fa!=d.getServer())this.RJ(fa);return true;};r.onSetChild=function(e){this.jsxsuper(e);if(!this.getCaptionBar()&&b.vP(e))this.ki(true);return true;};r.RJ=function(n){if(n==null)n=this.getServer();var
Pa=this.getTaskButton(n.getTaskBar());if(Pa!=null)Pa.getParent().removeChild(Pa);};r.onDestroy=function(i){this.RJ(i.getServer());this.jsxsuper(i);};r.getWindowState=function(){return this.jsxwindowstate!=null?this.jsxwindowstate:1;};r.setWindowState=function(s){this.jsxwindowstate=s;return this;};r.getZMultiplier=function(){return this.jsxzmultiplier!=null?this.jsxzmultiplier:1;};r.setZMultiplier=function(i){this.jsxzmultiplier=i;return this;};r.getModal=function(){return this.jsxmodal!=null?this.jsxmodal:0;};r.setModal=function(f){this.jsxmodal=f;return this;};r.doBeginMove=function(l,a){if(l.leftButton()){this._jsxvy=true;var
cb=this.NK();var
Oa=cb.childNodes[2];Oa.style.visibility=ub.x;cb.childNodes[0].focus();try{this.jsxsupermix(l,Oa);Ea.subscribe(ub.B,this,ub.C);this.setZIndex(this.getServer().getNextZIndex(jsx3.app.Server.Z_DIALOG),true);}catch(Kb){var
Ua=jsx3.lang.NativeError.wrap(Kb);sb.error(ub.D+this.getName()+ub.E+Ua);this.g3(Oa);}}};r.g3=function(e){e.style.visibility=ub.F;e.style.left=ub.G;e.style.top=ub.G;this._jsxvy=false;Ea.unsubscribe(ub.B,this,ub.C);};r.doEndMove=function(o){if(this._jsxvy){var
Ba=this.NK();var
va=Ba.childNodes[2];Ba.style.left=parseInt(parseInt(Ba.style.left)+parseInt(va.style.left)+1)+ub.H;Ba.style.top=parseInt(parseInt(Ba.style.top)+parseInt(va.style.top)+1)+ub.H;this.g3(va);o=o.event;if(Ba.ownerDocument.getElementsByTagName(ub.I)[0]){try{this.jsxsupermix(o,Ba);}catch(Kb){var
Ra=jsx3.lang.NativeError.wrap(Kb);sb.error(ub.J+this.getName()+ub.E+Ra);}if(!this.getEvent(ub.K))this.constrainPosition();}else sb.error(ub.L+jsx3.html.getOuterHTML(o.srcElement()));}this.focus(true);};r.NK=function(){var
G=this.getRendered();if(G!=null&&this.jsxmodal){return G.childNodes[1];}else return G;};r.getAbsolutePosition=function(o){return this.jsxsuper(o,this.NK());};r.setLeft=function(s,o){this.jsxleft=s;if(o){this.A4({left:s});}else this.ce();return this;};r.setTop=function(d,g){this.jsxtop=d;if(g){this.A4({top:d});}else this.ce();return this;};r.setWidth=function(l,a){this.jsxwidth=l;if(a){this.A4({width:this.jsxwidth});}else this.ce();return this;};r.setHeight=function(a,p){this.jsxheight=a;if(p){this.A4({height:this.jsxheight});}else this.ce();return this;};r.doMaximize=function(e){if(this.getWindowState()==0)this.doToggleState(1);if(this._jsxAW!=null){var
ea=this.BK(this._jsxAW.jsxwidth,this._jsxAW.jsxheight);this.jsxwidth=ea[0];this.jsxheight=ea[1];this.jsxleft=this._jsxAW.jsxleft;this.jsxtop=this._jsxAW.jsxtop;delete this[ub.M];var
Na={left:this.getLeft(),top:this.getTop(),width:this.getWidth(),height:this.getHeight()};this.A4(Na,true);if(e)e.setDynamicProperty(ub.r,ub.N).setImage(b.GV).repaint();}else{this._jsxAW={};this._jsxAW.jsxwidth=this.getWidth();this._jsxAW.jsxheight=this.getHeight();this._jsxAW.jsxtop=this.getTop();this._jsxAW.jsxleft=this.getLeft();var
B=this.getParent().getAbsolutePosition();var
Nb=this.NK();var
ea=this.BK(B.W-b.IR[1]-b.IR[3],B.H-b.IR[0]-b.IR[2]);this.jsxwidth=ea[0];this.jsxheight=ea[1];this.jsxleft=Math.min(parseInt(Nb.style.left),B.W-this.jsxwidth-b.IR[1]);this.jsxtop=Math.min(parseInt(Nb.style.top),B.H-this.jsxheight-b.IR[2]);var
Na={left:this.getLeft(),top:this.getTop(),width:this.getWidth(),height:this.getHeight()};this.A4(Na,true);if(e)e.setDynamicProperty(ub.r,ub.O).setImage(b.sR).repaint();}};r.getResize=function(){return this.jsxresize==null?1:this.jsxresize;};r.setResize=function(m){this.jsxresize=m;this.ce();return this;};r.setResizeParameters=function(n,l,m,f,g,i){this.jsxresize=n;this.jsxminx=l;this.jsxminy=m;this.jsxmaxx=f;this.jsxmaxy=g;};r.BK=function(q,s){q=Math.max(q,this.uu());s=Math.max(s,this.qO());if(typeof this.jsxmaxx==ub.P)q=Math.min(q,this.jsxmaxx);if(typeof this.jsxmaxy==ub.P)s=Math.min(s,this.jsxmaxy);return [q,s];};r.getCaptionBar=function(){return this.findDescendants(b.vP,false,false,true);};b.vP=function(o){return jsx3.gui.WindowBar&&o instanceof jsx3.gui.WindowBar&&o.getType()==0;};r.yF=function(p,i){if(!p.leftButton())return;var
Ia=this.doEvent(ub.Q,{objEVENT:p});if(Ia!==false){b._jsxEz=i.parentNode.childNodes[2];b._jsxEz.style.visibility=ub.x;b._jsxEz.style.zIndex=11;i.style.zIndex=12;var
pb=this.Wl(true).lg(1);b._jsxoffx=b.aN+2;b._jsxoffy=b.aN+2;var
Sa=this;jsx3.gui.Interactive.Bn(p,i,function(g,f){return Sa.UH(g,f);});Ea.subscribe(ub.B,this,ub.R);}};r.UH=function(j,i){if(b._jsxEz){this._jsxG3=j+b._jsxoffx;this._jsxj1=i+b._jsxoffy;this._jsxG3=Math.max(this._jsxG3,this.uu());this._jsxj1=Math.max(this._jsxj1,this.qO());if(typeof this.jsxmaxx==ub.P)this._jsxG3=Math.min(this._jsxG3,this.jsxmaxx);if(typeof this.jsxmaxy==ub.P)this._jsxj1=Math.min(this._jsxj1,this.jsxmaxy);var
A=this.Wl().lg(1);A.recalculate({width:this._jsxG3,height:this._jsxj1},b._jsxEz);return [this._jsxG3-b._jsxoffx,this._jsxj1-b._jsxoffy];}return [j,i];};r.uu=function(){var
Ab=Number(this.jsxminx)||-1;return Math.max(25,Ab);};r.qO=function(){var
K=Number(this.jsxminy)||-1;var
v=15;if(this.getCaptionBar()!=null)v=v+30;return Math.max(v,K);};r.tG=function(s){s=s.event;Ea.unsubscribe(ub.B,this,ub.R);Ea.publish(s);if(!b._jsxEz)return;var
ib=this.Wl().lg(1);var
Nb=ib._b();var
y=ib.lh();b._jsxEz.style.visibility=ub.F;b._jsxEz.style.zIndex=0;delete b[ub.S];this.jsxwidth=Nb;this.jsxheight=y;this.A4({width:Nb,height:y});this.doEvent(ub.T,{objEVENT:s});};r.recalcBox=function(){this.jsxsuper();this.A4();};r.uf=function(m){var
ab=m==this.getCaptionBar()?this.Wl(true):this.Wl(true).lg(0);return {parentwidth:ab.mn(),parentheight:ab.ec()};};r.A4=function(s,d){if(d&&(s.left==null||s.top==null)){var
cb=this.getParent().getAbsolutePosition();if(s.left==null)s.left=Math.max(0,parseInt((cb.W-s.width)/2));if(s.top==null)s.top=Math.max(0,parseInt((cb.H-s.height)/2));}this.af(s,true);};r.Gc=function(k){var
qa=this.getWindowState()==0&&this.getServer().getTaskBar()==null;var
ia=this.getParent();if(ia==null){ia={H:this.getHeight(),W:this.getWidth()};}else{ia=ia.getAbsolutePosition();if(ia==null||ia.W==0)ia={H:this.getHeight(),W:this.getWidth()};}var
va=jsx3.util.strEmpty(this.getTop())?Math.max(0,parseInt((ia.H-this.getHeight())/2)):this.getTop();var
la=jsx3.util.strEmpty(this.getLeft())?Math.max(0,parseInt((ia.W-this.getWidth())/2)):this.getLeft();if(k==null)k={};if(k.left==null)k.left=la;if(k.top==null)k.top=va;if(k.width==null)k.width=this.getWidth();if(k.height==null)k.height=qa?30:this.getHeight();k.tagname=ub.U;k.boxtype=ub.V;k.padding=ub.W;k.border=ub.X;var
Ib=new
jsx3.gui.Painted.Box(k);var
Xa=this.getCaptionBar();var
Ka={};Ka.parentwidth=Ib.mn();Ka.parentheight=Ib.ec();Ka.width=ub.Y;Ka.height=this.getHeight()-(Xa!=null?Xa.getHeight()+8:6);Ka.top=Xa!=null?Xa.getHeight()+4:2;Ka.left=2;Ka.tagname=ub.U;Ka.boxtype=ub.V;Ka.border=ub.X;var
Ha=new
jsx3.gui.Painted.Box(Ka);Ib.Yf(Ha);Ka={};Ka.left=-1;Ka.top=-1;Ka.width=this.getWidth();Ka.height=qa?30:this.getHeight();Ka.tagname=ub.U;Ka.boxtype=ub.V;Ka.padding=ub.Z;Ka.border=ub.X;var
S=new
jsx3.gui.Painted.Box(Ka);Ib.Yf(S);Ka={};Ka.left=this.getWidth()-(b.aN+2);Ka.top=this.getHeight()-(b.aN+2);Ka.width=b.aN+1;Ka.height=b.aN+1;Ka.tagname=ub.U;Ka.boxtype=ub.V;var
J=new
jsx3.gui.Painted.Box(Ka);Ib.Yf(J);Ka={};Ka.left=this.getWidth()-(b.aN+2);Ka.top=this.getHeight()-(b.aN+2);Ka.width=b.aN;Ka.height=b.aN;Ka.tagname=ub.U;Ka.boxtype=ub.V;var
zb=new
jsx3.gui.Painted.Box(Ka);Ib.Yf(zb);return Ib;};r.Rc=function(c,s,o){var
bb=this.Wl(true,c);if(s!=null)s=this.NK();if(s!=null){var
qb=this.getCaptionBar();var
ba=bb.recalculate(c,s,o);if(!ba.w&&!ba.h)return;var
La=bb.lg(0);var
u=this.getHeight()-(qb!=null?qb.getHeight()+8:6);La.recalculate({parentwidth:bb.mn(),parentheight:bb.ec(),width:ub.Y,height:u},s.childNodes[1],o);var
Pa=bb.lg(1);Pa.recalculate({width:this.getWidth(),height:this.getHeight()},s.childNodes[2],o);if(this.getResize()==1){var
mb=bb.lg(2);mb.recalculate({top:this.getHeight()-(b.aN+2),left:this.getWidth()-(b.aN+2)},s.childNodes[3],o);var
db=bb.lg(3);db.recalculate({top:this.getHeight()-(b.aN+2),left:this.getWidth()-(b.aN+2)},s.childNodes[4],o);}if(qb)o.add(qb,{width:ub.Y,parentwidth:bb.mn(),height:qb.getHeight()},s.childNodes[0],true);var
qa=this.getChildren();var
Qa=0;var
Wa=s.childNodes[qb?1:0];if(Wa){var
Kb=Wa.childNodes;for(var
t=0;t<qa.length;t++)if(qa[t]!=qb){o.add(qa[t],{parentwidth:La.mn(),parentheight:La.ec()},true,true);}else Qa=1;}}};r.paint=function(){this.applyDynamicProperties();var
Q=this.getId();var
ib=this.getWindowState()==0&&this.getServer().getTaskBar()==null;var
G=ib?ub._:ub.x;var
N=this.getServer()!=null?this.getServer().getNextZIndex(jsx3.app.Server.Z_DIALOG)*this.getZMultiplier():5000;var
Sa={};if(this.getEvent(ub.aa)!=null)Sa[ub.ba]=true;if(this.getModal()!=1)Sa[ub.ca]=ub.da;var
ta=this.getModal()==1;var
ob=this.jh(Sa,ta?1:0);var
ga=this.renderAttributes(null,true);this.Mj();var
Ra=this.Wl(true);if(ib){var
Ta=Ra.lh()-Ra.Zm();var
I=ub.ea+(32-Ta)+ub.fa;}else var
I=ub.x;if(ta){var
ra=[ub.ga+Q+ub.ha+this.Fo()+this.Gj(ub.ia,ub.ja,0)+ub.ka+N+this.bd()+ub.la+ub.ma+ub.na+this.Gj(ub.ca,ub.oa,1)+this.Gj(ub.B,ub.pa,1)+ub.qa,ub.ra+this.Fo()+this.Gj(ub.ia,ub.ja,1)+ub.sa];Ra.setAttributes(ub.ta+this.getName()+ub.ua+ob+this.Fo()+ub.va+this.to()+ub.ha+ga);Ra.setStyles(ub.wa+this.zf()+this.Of()+this.bi()+this.fh()+I);}else{var
ra=[ub.x,ub.x];Ra.setAttributes(ub.xa+Q+ub.ua+this.on()+ob+this.Fo()+ub.va+this.to()+ub.ha+ga);Ra.setStyles(ub.ya+N+ub.za+this.zf()+this.Of()+this.bd()+this.bi()+this.fh()+I);}var
Bb=this.getCaptionBar();var
C=Bb!=null?Bb.paint():ub.Aa;var
xa=Ra.lg(0);xa.setAttributes(ub.Ba);xa.setStyles(this.fn()+this.dk()+G);var
bb=this.getChildren().filter(function(g){return g!=Bb;});var
Ha=this.paintChildren(bb);var
cb=Ra.lg(1);cb.setAttributes(ub.Ca);cb.setStyles(ub.Da+I);if(this.getResize()==1){var
rb=Ra.lg(2);rb.setStyles(ub.Ea+jsx3.gui.Block.SPACE+ub.Fa+G);rb.setAttributes(this.Gj(ub.ca,ub.Ga,ta?2:1));var
db=Ra.lg(3);db.setStyles(ub.Ha+b.lX+ub.Fa+G);var
ia=rb.paint().join(ub.x)+db.paint().join(ub.x);}else var
ia=ub.x;return ra[0]+Ra.paint().join(C+xa.paint().join(Ha)+cb.paint().join(ub.Ia)+ia)+ra[1];};r.jX=function(p,n){if(p.srcElement()==n&&p.tabKey()){p.cancelReturn();(this.getCaptionBar()||this).focus();}else p.setAttribute(ub.Ja,1);};r.xY=function(j,c){this.beep().focus();j.cancelBubble();};r.Qq=function(f,g){this.focus();f.cancelBubble();};r.Mj=function(){var
V=this.getId();var
Gb;if(this.getServer()!=null&&(Gb=this.getServer().getTaskBar())!=null&&this.getModal()!=1){var
eb=this.getCaptionBar();var
na=this.getTaskButton();if(eb!=null){if(na==null){eb.applyDynamicProperties();jsx3.require(ub.j);var
Ga=new
jsx3.gui.ToolbarButton(b.AD+V,1,eb.getText());Gb.setChild(Ga);Ga.setEvent(ub.Ka+V+ub.La,ub.o);Ga.setState(this.getWindowState()==1?1:0);Ga.setText(jsx3.util.strTruncate(eb.getText()||ub.x,20));Ga.setImage(b._G);if(Gb.getRendered()==null){jsx3.sleep(Gb.repaint,ub.Ma+Gb.getId(),Gb);}else Gb.paintChild(Ga);}else na.setText(jsx3.util.strTruncate(eb.getText(),20)).repaint();}else if(na!=null)na.getParent().removeChild(na);}};r.Fo=function(){return this.jsxsuper(this.getIndex()||Number(0));};r.dk=function(){if(this.getOverflow()==2){return ub.Na;}else return ub.Oa;};r.to=function(){var
mb=this.getClassName();return ub.Pa+(mb?ub.Qa+mb:ub.x);};r.getAlertsParent=function(){return this;};r.constrainPosition=function(e){var
N=this.getRendered();var
La=N!=null;var
Za=null;if(N){Za=N.parentNode;}else if(this.getParent())Za=this.getParent().getRendered();if(Za==null)return;var
ib=parseInt(Za.style.width);var
U=parseInt(Za.style.height);if(e===true){var
db=ib-b.IR[1]-b.IR[3];var
w=U-b.IR[0]-b.IR[2];if(this.getWidth()>db)this.setWidth(db,La);if(this.getHeight()>w)this.setHeight(w,La);var
za=ib-this.getWidth()-b.IR[1];var
D=U-this.getHeight()-b.IR[2];if(this.getLeft()<b.IR[3])this.setLeft(b.IR[3],La);else if(this.getLeft()>za)this.setLeft(za,La);if(this.getTop()<b.IR[0])this.setTop(b.IR[0],La);else if(this.getTop()>D)this.setTop(D,La);}else{var
ca=this.getDimensions();if(!e)e=b.cR;e=e.concat();if(e[0]==null)e[0]=ca[3];else if(e[0]<0)e[0]=ca[3]+e[0];if(e[1]==null)e[1]=ca[2];else if(e[1]<0)e[1]=ca[2]+e[1];if(e[2]==null)e[2]=ca[3];else if(e[2]<0)e[2]=ca[3]+e[2];if(e[3]==null)e[3]=ca[2];else if(e[3]<0)e[3]=ca[2]+e[3];if(ca[0]<e[3]-ca[2])this.setLeft(e[3]-ca[2],La);else if(ca[0]>ib-e[1])this.setLeft(ib-e[1],La);if(ca[1]<e[0]-ca[3])this.setTop(e[0]-ca[3],La);else if(ca[1]>U-e[2])this.setTop(U-e[2],La);}};r.onRemoveChild=function(q,d){this.jsxsuper(q,d);if(q instanceof Array){this.ce();this.repaint();}else if(b.vP(q)){this.ce();this.repaint();}};r.beep=function(){var
R=this.getCaptionBar();if(R!=null){R.beep();}else{var
Oa=this.NK();jsx3.gui.Yl(Oa,{backgroundColor:ub.Ra});}return this;};r.getAlwaysCheckHotKeys=function(){return true;};b.getVersion=function(){return ub.Sa;};});jsx3.Dialog=jsx3.gui.Dialog;
