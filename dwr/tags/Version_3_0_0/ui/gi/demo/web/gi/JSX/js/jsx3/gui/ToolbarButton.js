/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.ToolbarButton",jsx3.gui.Block,[jsx3.gui.Form],function(r,d){var
ub={O:"class='jsx30toolbarbutton_lbl'",k:"jsxexecute",h:")",c:"jsx:///images/tbb/over.gif",H:"background-image:url(",p:"BT",P:"&#160;",q:"keydown",V:"pt",G:"5 4 5 0",v:"blur",I:");",i:"jsxmenu",A:"span",a:"jsx:///images/tbb/down.gif",F:"0px;0px;0px;solid 1px ",U:"3.0.00",u:"mouseover",t:"mouseout",j:"#808080",N:"class='jsx30toolbarbutton_img'",s:"mouseup",S:"background-color:",z:"1 4 1 0",d:"jsx:///images/tbb/default.gif",o:"click",D:"box",R:"overflow:hidden;",K:"' ",w:"Qi",r:"mousedown",C:"div",B:"relativebox",l:"border",g:"url(",Q:"class='jsx30toolbarbutton_cap'",L:"label='",b:"jsx:///images/tbb/on.gif",M:" class='jsx30toolbarbutton'",m:"padding",f:"",J:"id='",T:";",y:"el",n:"jsxchange",x:"focus",E:"0 0 0 4",e:"#9B9BB7"};var
Oa=jsx3.gui.Form;var
z=jsx3.gui.Interactive;var
nb=jsx3.gui.Event;r.TYPENORMAL=0;r.TYPECHECK=1;r.TYPERADIO=2;r.STATEOFF=0;r.STATEON=1;r.IMAGEDOWN=jsx3.resolveURI(ub.a);r.IMAGEON=jsx3.resolveURI(ub.b);r.IMAGEOVER=jsx3.resolveURI(ub.c);r.DEFAULTIMAGE=jsx3.resolveURI(ub.d);r.BORDERCOLOR=ub.e;jsx3.html.loadImages(r.IMAGEDOWN,r.IMAGEON,r.IMAGEOVER,r.DEFAULTIMAGE);d.init=function(i,s,l){this.jsxsuper(i,null,null,null,null);if(s!=null)this.setType(s);if(l!=null)this.setTip(l);};d.getDisabledImage=function(){return this.jsxdisabledimage!=null&&jsx3.util.strTrim(this.jsxdisabledimage)!=ub.f?this.jsxdisabledimage:this.getImage();};d.setDisabledImage=function(f){this.jsxdisabledimage=f;return this;};d.doValidate=function(){var
T=this.getType==0||this.getRequired()==0||this.getState()==1;return this.setValidationState(T?1:0).getValidationState();};d.getImage=function(){return this.jsximage!=null&&jsx3.util.strTrim(this.jsximage)!=ub.f?this.jsximage:null;};d.setImage=function(i){this.jsximage=i;return this;};d.getType=function(){return this.jsxtype==null?0:this.jsxtype;};d.setType=function(h){this.jsxtype=h;return this;};d.oe=function(i,m){if(i.spaceKey()||i.enterKey()){this.BT(i,m);i.cancelAll();}};d.getMaskProperties=function(){return jsx3.gui.Block.MASK_NO_EDIT;};d.Dc=function(e,h){if(!e.leftButton())return;h.style.backgroundImage=ub.g+r.IMAGEDOWN+ub.h;h.childNodes[3].style.backgroundColor=r.BORDERCOLOR;if(this.getEvent(ub.i)!=null)h.childNodes[2].style.backgroundImage=ub.g+r.IMAGEDOWNMENU+ub.h;};d.Df=function(p,i){if(p.leftButton()){i.style.backgroundImage=ub.f;i.childNodes[3].style.backgroundColor=ub.f;}else if(p.rightButton())this.jsxsupermix(p,i);};d.el=function(p,n){if(this.getState()==0){n.style.backgroundImage=ub.g+r.IMAGEOVER+ub.h;n.childNodes[3].style.backgroundColor=ub.j;}};d.Qi=function(f,g){if(this.getState()==0){g.style.backgroundImage=ub.f;g.childNodes[3].style.backgroundColor=ub.f;}};d.doExecute=function(n){if(n==null)n=true;this.BT(n,this.getRendered(n instanceof nb?n:null));};d.doClick=function(){this.BT(true,this.getRendered());};d.BT=function(b,k){var
Hb=this.doEvent(ub.k,{objEVENT:b instanceof nb?b:null});if(Hb!==false)if(this.getType()==2){this.UJ(1,b,k);}else if(this.getType()==1)this.UJ(this.getState()==1?0:1,b,k);};d.getGroupName=function(){return this.jsxgroupname!=null&&this.getType()==2?this.jsxgroupname:null;};d.setGroupName=function(a){if(this.getType()==2)this.jsxgroupname=a;return this;};d.getDivider=function(){return this.jsxdivider!=null?this.jsxdivider:0;};d.setDivider=function(f,i){this.jsxdivider=f;if(i)this.recalcBox([ub.l,ub.m]);else this.ce();return this;};d.getState=function(){return this.getType()==0?0:this.jsxstate==null?0:this.jsxstate;};d.setState=function(o){var
Mb=null;Mb=this.isOldEventProtocol();this.UJ(o,Mb,this.getRendered());return this;};d.UJ=function(l,c,j){var
cb=false;if(this.getType()==2&&l==1){var
wb=this.getGroupName();var
ua=this.getParent().findDescendants(function(f){return f instanceof r&&f.getGroupName()==wb;},false,true,true);for(var
Va=ua.length-1;Va>=0;Va--)if(ua[Va]!=this&&ua[Va].getType()==2){ua[Va].jsxstate=0;var
Wa=ua[Va].getRendered(j);if(Wa!=null){Wa.style.backgroundImage=ub.f;Wa.childNodes[3].style.backgroundColor=ub.f;if(ua[Va].getEvent(ub.i)!=null)Wa.childNodes[2].style.backgroundImage=ub.g+r.IMAGEOFFMENU+ub.h;}}else if(ua[Va]==this)if(j!=null){j.style.backgroundImage=ub.g+r.IMAGEON+ub.h;j.childNodes[3].style.backgroundColor=r.BORDERCOLOR;if(this.getEvent(ub.i)!=null)j.childNodes[2].style.backgroundImage=ub.g+r.IMAGEONMENU+ub.h;}cb=true;}else if(this.getType()==2){if(j!=null){j.style.backgroundImage=ub.f;j.childNodes[3].style.backgroundColor=ub.f;if(this.getEvent(ub.i)!=null)j.childNodes[2].style.backgroundImage=ub.g+r.IMAGEOFFMENU+ub.h;}cb=true;}else if(this.getType()==1){if(j!=null)if(l==1){j.style.backgroundImage=ub.g+r.IMAGEON+ub.h;j.childNodes[3].style.backgroundColor=r.BORDERCOLOR;if(this.getEvent(ub.i)!=null)j.childNodes[2].style.backgroundImage=ub.g+r.IMAGEONMENU+ub.h;}else{j.style.backgroundImage=ub.f;j.childNodes[3].style.backgroundColor=ub.f;if(this.getEvent(ub.i)!=null)j.childNodes[2].style.backgroundImage=ub.g+r.IMAGEOFFMENU+ub.h;}cb=true;}this.jsxstate=l;if(cb&&c){var
Jb=null;if(c instanceof nb)Jb={objEVENT:c};this.doEvent(ub.n,Jb);}return this;};d.setEnabled=function(f,p){if(this._jsxhotkey!=null)this._jsxhotkey.setEnabled(f==1);return this.jsxsupermix(f,p);};r.Tj={};r.Tj[ub.o]=ub.p;r.Tj[ub.q]=true;r.Tj[ub.r]=true;r.Tj[ub.s]=true;r.Tj[ub.t]=true;r.Tj[ub.u]=true;r.Tj[ub.v]=ub.w;r.Tj[ub.x]=ub.y;d.Rc=function(b,e,p){this.Gi(b,e,p,3);};d.Gc=function(){var
pb=this.getRelativePosition()!=0;var
vb,mb,Ab;var
sa={};sa.height=22;if(pb){sa.margin=(vb=this.getMargin())!=null&&vb!=ub.f?vb:ub.z;sa.tagname=ub.A;sa.boxtype=ub.B;}else{sa.left=(mb=this.getLeft())!=null&&mb!=ub.f?mb:0;sa.top=(Ab=this.getTop())!=null&&Ab!=ub.f?Ab:0;sa.tagname=ub.C;sa.boxtype=ub.D;}if(this.getDivider()==1){sa.padding=ub.E;sa.border=ub.F+r.BORDERCOLOR;}var
Y=new
jsx3.gui.Painted.Box(sa);sa={};sa.width=this.getImage()!=null&&this.getImage()!=ub.f?22:3;sa.height=22;sa.tagname=ub.A;sa.boxtype=ub.B;var
Cb=new
jsx3.gui.Painted.Box(sa);Y.Yf(Cb);sa={};if(jsx3.util.strEmpty(this.getText())){sa.width=1;}else sa.padding=ub.G;sa.height=22;sa.tagname=ub.A;sa.boxtype=ub.B;var
L=new
jsx3.gui.Painted.Box(sa);Y.Yf(L);sa={};sa.width=1;sa.height=22;sa.tagname=ub.A;sa.boxtype=ub.B;var
ob=new
jsx3.gui.Painted.Box(sa);Y.Yf(ob);sa={};sa.width=1;sa.height=22;sa.tagname=ub.A;sa.boxtype=ub.B;var
Jb=new
jsx3.gui.Painted.Box(sa);Y.Yf(Jb);return Y;};d.paint=function(){this.applyDynamicProperties();var
Ka;if((Ka=this.getKeyBinding())!=null){var
Pa=this;if(this._jsxhotkey!=null)this._jsxhotkey.destroy();this._jsxhotkey=this.doKeyBinding(function(i){Pa.BT(i,Pa.getRendered());},Ka);this._jsxhotkey.setEnabled(this.getEnabled()!=0);}var
Ma=this.getState()==1?ub.H+r.IMAGEON+ub.I:ub.f;var
V=this.bd();var
la=this.bi();var
yb=null,Q=null,Hb=null;if(this.getEnabled()==1){yb=this.jh(r.Tj,0);Q=this.getUriResolver().resolveURI(this.getImage());Hb=ub.f;}else{yb=ub.f;Q=this.getUriResolver().resolveURI(this.getDisabledImage());Hb=jsx3.html.getCSSOpacity(0.4);}var
mb=this.renderAttributes(null,true);var
Ab=this.Wl(true);Ab.setAttributes(ub.J+this.getId()+ub.K+ub.L+this.getName()+ub.K+this.Fo()+this.bn()+yb+ub.M+mb);Ab.setStyles(this.Xe(true)+Ma+V+la+Hb+this.zk()+this.fh());var
O=Ab.lg(0);O.setStyles(Q!=null?ub.H+Q+ub.I:ub.f);O.setAttributes(ub.N+jsx3.html.Kf);var
aa=Ab.lg(1);aa.setAttributes(ub.O+jsx3.html.Kf);var
G;if((G=this.getText())!=null&&G!=ub.f){aa.setStyles(this.eh()+this.ad()+this.Yh()+this.Bf());}else{G=ub.P;aa.setStyles(jsx3.html.Fc);}var
Kb=Ab.lg(2);Kb.setAttributes(ub.Q);var
qb=Ab.lg(3);qb.setAttributes(ub.Q);qb.setStyles(ub.R+(this.getState()==1?ub.S+r.BORDERCOLOR+ub.T:ub.f));return Ab.paint().join(O.paint().join(ub.P)+aa.paint().join(G)+Kb.paint().join(ub.P)+qb.paint().join(ub.P));};r.getVersion=function(){return ub.U;};d.emGetType=function(){return jsx3.gui.Matrix.EditMask.FORMAT;};d.emInit=function(o){this.jsxsupermix(o);if(this.getType()==2)this.setType(0);this.subscribe(ub.k,this,ub.V);};d.emSetValue=function(b){};d.emGetValue=function(){return null;};d.emBeginEdit=function(f,i,s,k,l,n,g){var
T=g.childNodes[0].childNodes[0];if(T){this.jsxsupermix(f,i,s,k,l,n,g);T.focus();}else return false;};d.emPaintTemplate=function(){this.setEnabled(0);var
_a=this.paint();this.setEnabled(1);var
jb=this.paint();return this.Tn(jb,_a);};d.pt=function(l){var
N=this.emGetSession();if(N){}};});jsx3.ToolbarButton=jsx3.gui.ToolbarButton;
