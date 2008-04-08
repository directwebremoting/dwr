/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Block","jsx3.gui.Form");jsx3.Class.defineClass("jsx3.gui.ColorPicker",jsx3.gui.Block,[jsx3.gui.Form],function(j,q){var
ub={k:'id="',O:"inline",ea:"_jsxoffsetx",_:"mousemove",p:"Lx",P:"100%",q:"GA",V:"px;",v:"px;background-color:",ha:"px",I:'" width="6" height="9"/>',a:"jsx:///images/colorpicker/",F:'px;">&#160;</span>',u:"px;height:",U:"1px solid #333333;",j:"0x",d:"hue-h.png",z:"px;background-color:#FFFFFF;",S:"1px solid #CCCCCC;",fa:"_jsxoffsety",D:"px;background-color:#000000;",w:';"></span>',K:"relativebox",R:"cursor:pointer;",aa:"RM",g:"brightness-v.png",B:"_2_v",b:"d1arrow.gif",Q:"cursor:pointer;overflow:hidden;",M:"span",f:"saturation-h.png",ga:"jsxchange",y:"_1_h",T:"overflow:hidden;",ba:"mouseup",x:"_1_v",e:"saturation-v.png",c:"hue-v.png",h:"string",W:"filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='",H:"_drag",G:'<img src="',i:"#",ca:"Df",A:'"></span>',t:' class="gradient" style="width:',N:"div",s:"<span",X:"', sizingMethod='scale');\"></span>",Z:'px;"/>',o:"mousedown",r:"",C:"_2_h",l:'" class="jsx30colorpicker" ',da:"q3",L:"box",m:'label="',Y:'"',J:"&#160;",n:'" ',E:"_3_v"};var
v=jsx3.gui.Interactive;var
jb=jsx3.gui.Block;var
Ma=jsx3.gui.Event;var
ia=jsx3.html.selectSingleElm;j.DEFAULT_WIDTH=324;j.DEFAULT_HEIGHT=300;j.HUE=1;j.SATURATION=2;j.BRIGHTNESS=3;j.jU=16;j.ct=8;j.UE=9;j.j2=9;var
hb=ub.a;j.bs={_drag:jsx3.resolveURI(hb+ub.b),_1_v:jsx3.resolveURI(hb+ub.c),_1_h:jsx3.resolveURI(hb+ub.d),_2_v:jsx3.resolveURI(hb+ub.e),_2_h:jsx3.resolveURI(hb+ub.f),_3_v:jsx3.resolveURI(hb+ub.g)};for(var P in j.bs)jsx3.html.loadImages(j.bs[P]);q.init=function(k,n,i,f,c){this.jsxsuper(k,n,i,f,c);this.jsxrgb=16711680;this.jsxaxis=1;};q.getValue=function(){return this.jsxrgb;};q.setValue=function(i){var
ua=parseInt(i);if(!isNaN(ua)){this.setRGB(ua);}else if(typeof i==ub.h){if(i.indexOf(ub.i)==0)i=i.substring(1);ua=parseInt(ub.j+i);if(!isNaN(ua))this.setRGB(ua);else this.setRGB(0);}else this.setRGB(0);};q.doValidate=function(){var
nb=1;this.setValidationState(nb);return nb;};q.getRGB=function(){return this.jsxrgb;};q.getRgbAsHex=function(){return ub.j+(16777216+(this.jsxrgb||Number(0))).toString(16).substring(1).toUpperCase();};q.setRGB=function(c){this.jsxrgb=Math.max(0,Math.min(c,16777215));this.dM(true,true);};q.getAxis=function(){return this.jsxaxis||1;};q.setAxis=function(b){this.jsxaxis=b;this.ce();return this;};q.setHSB=function(l,a,r){var
rb=j.HSBtoRGB(l,a,r);this.jsxrgb=(rb[0]<<16)+(rb[1]<<8)+rb[2];this.dM(true,true,[l,a,r]);};q.paint=function(){this.applyDynamicProperties();var
Ya=j.RGBtoHSB(this.jsxrgb);var
za=j.HSBtoRGB(Ya[0],1,1);var
da=ub.i+j.T7(za[0],za[1],za[2]);var
oa=this.renderAttributes(null,false);var
y=this.getAxis();var
u=this.Wl(true);u.setAttributes(ub.k+this.getId()+ub.l+ub.m+this.getName()+ub.n+this.Fo()+this.bn()+oa+jsx3.html.Kf);u.setStyles(this.zk()+this.bd()+this.bi());var
W=u.lg(0);var
Ab=W.lg(0);Ab.setAttributes(this.Gj(ub.o,ub.p,2)+jsx3.html.Kf);var
aa=W.lg(1);aa.setAttributes(this.Gj(ub.o,ub.q,2)+jsx3.html.Kf);var
Fa=ub.r,Q=ub.r;var
B=aa.mn(),Lb=aa.ec();var
na=Ab.mn(),ka=Ab.ec();var
va=ub.s+jsx3.html.Kf+ub.t;if(y==1){Fa=Fa+(va+na+ub.u+ka+ub.v+da+ub.w);Q=Q+this.k6(j.bs[ub.x],B,Lb);}else{Fa=Fa+this.k6(j.bs[ub.y],na,ka);Q=Q+(va+B+ub.u+Lb+ub.v+da+ub.w);}if(y==2){Fa=Fa+(va+na+ub.u+ka+ub.z+jsx3.html.getCSSOpacity(1-Ya[1])+ub.A);Q=Q+this.k6(j.bs[ub.B],B,Lb);}else{Fa=Fa+this.k6(j.bs[y==1?ub.C:ub.B],na,ka);Q=Q+(va+B+ub.u+Lb+ub.z+jsx3.html.getCSSOpacity(1-Ya[1])+ub.A);}if(y==3){Fa=Fa+(va+na+ub.u+ka+ub.D+jsx3.html.getCSSOpacity(1-Ya[2])+ub.A);Q=Q+this.k6(j.bs[ub.E],B,Lb);}else{Fa=Fa+this.k6(j.bs[ub.E],na,ka);Q=Q+(va+B+ub.u+Lb+ub.D+jsx3.html.getCSSOpacity(1-Ya[2])+ub.A);}Fa=Fa+(va+na+ub.u+ka+ub.F);Q=Q+(va+B+ub.u+Lb+ub.F);var
Na=Ab.lg(0);Fa=Fa+Na.paint().join(Na.lg(0).paint().join(ub.r));var
ra=W.lg(2);return u.paint().join(W.paint().join(Ab.paint().join(Fa)+aa.paint().join(Q)+ra.paint().join(ub.G+j.bs[ub.H]+ub.I)+ub.J));};q.Gc=function(d){var
da=jsx3.gui.Painted.Box;if(this.getParent()&&(d==null||isNaN(d.parentwidth)||isNaN(d.parentheight))){d=this.getParent().uf(this);}else if(d==null)d={};var
L=this.getRelativePosition()!=0;var
Nb=L?null:this.getLeft();var
Oa=L?null:this.getTop();var
ga=j.RGBtoHSB(this.jsxrgb);var
Sa=this.Kr(ga);if(!d.boxtype)d.boxtype=L?ub.K:ub.L;d.tagname=ub.M;if(d.left==null&&Nb!=null)d.left=Nb;if(d.top==null&&Oa!=null)d.top=Oa;d.width=this.getWidth()||j.DEFAULT_WIDTH;d.height=this.getHeight()||j.DEFAULT_HEIGHT;var
Lb=new
da(d);var
F=Lb.mn();var
R=Lb.ec();var
Ga=new
da({tagname:ub.N,boxtype:ub.O,height:R,width:F});Lb.Yf(Ga);var
Pa={tagname:ub.M,boxtype:ub.L,left:0,top:0,width:F-j.jU-j.ct,height:ub.P,border:this.getBorder(),parentwidth:F,parentheight:R};var
Ka=new
da(Pa);Ka.setStyles(ub.Q);Ga.Yf(Ka);Pa={tagname:ub.M,boxtype:ub.L,left:F-j.jU,top:0,width:j.jU,height:ub.P,border:this.getBorder(),parentwidth:F,parentheight:R};var
Db=new
da(Pa);Db.setStyles(ub.R);Ga.Yf(Db);var
oa=Math.round(Sa[1]*(Ka.mn()-1))-Math.floor(j.j2/2);var
va=Math.round(Sa[2]*(Ka.ec()-1))-Math.floor(j.j2/2);Pa={tagname:ub.M,boxtype:ub.L,left:oa,top:va,width:j.j2,height:j.j2,border:ub.S};var
fa=new
da(Pa);fa.setStyles(ub.T);var
G=new
da({tagname:ub.M,boxtype:ub.L,left:0,top:0,width:j.j2-2,height:j.j2-2,border:ub.U});G.setStyles(ub.T);fa.Yf(G);Ka.Yf(fa);va=Math.round(Sa[0]*(Db.ec()-1))-Math.floor(j.UE/2)+Db.gl();Pa={tagname:ub.M,boxtype:ub.L,left:F-j.jU-5,top:va,width:6,height:j.UE};var
t=new
da(Pa);Ga.Yf(t);return Lb;};q.Rc=function(g,d,k){this.Gi(g,d,k,1);};q.nU=function(o){switch(this.getAxis()){case 1:return [1-o[0],o[1],1-o[2]];case 2:return [1-o[1],1-o[0],1-o[2]];case 3:return [1-o[1],1-o[2],1-o[0]];default:throw new
jsx3.Exception();}};q.Kr=function(o){switch(this.getAxis()){case 1:return [1-o[0],o[1],1-o[2]];case 2:return [1-o[1],1-o[0],1-o[2]];case 3:return [1-o[2],1-o[0],1-o[1]];default:throw new
jsx3.Exception();}};q.k6=function(m,s,o){if(jsx3.CLASS_LOADER.IE6){return ub.s+jsx3.html.Kf+ub.t+s+ub.u+o+ub.V+ub.W+m+ub.X;}else return ub.G+m+ub.Y+jsx3.html.Kf+ub.t+s+ub.u+o+ub.Z;};q.GA=function(l,a){Ma.unsubscribe(ub._,this);Ma.subscribe(ub._,this,ub.aa);Ma.subscribe(ub.ba,this,ub.ca);var
_=jsx3.html.getRelativePosition(a,l.srcElement());this._jsxoffsety=l.getTrueY()-l.getOffsetY()-Math.max(0,_.T);this.Rr(l,(l.getOffsetY()+_.T)/(a.offsetHeight-1),null,null);};q.Lx=function(b,k){Ma.unsubscribe(ub._,this);Ma.subscribe(ub._,this,ub.da);Ma.subscribe(ub.ba,this,ub.ca);var
D=jsx3.html.getRelativePosition(k,b.srcElement());this._jsxoffsetx=b.getTrueX()-b.getOffsetX()-Math.max(0,D.L);this._jsxoffsety=b.getTrueY()-b.getOffsetY()-Math.max(0,D.T);this.Rr(b,null,(b.getOffsetX()+D.L)/(k.offsetWidth-1),(b.getOffsetY()+D.T)/(k.offsetHeight-1));};q.RM=function(l){l=l.event;var
V=l.getTrueY()-this._jsxoffsety;var
Ka=ia(this.getRendered(l),0,1);V=Math.max(0,Math.min(Ka.offsetHeight-1,V));this.Rr(l,V/(Ka.offsetHeight-1),null,null);};q.q3=function(n){n=n.event;var
zb=n.getTrueX()-this._jsxoffsetx;var
_=n.getTrueY()-this._jsxoffsety;var
w=ia(this.getRendered(n),0,0);zb=Math.max(0,Math.min(w.offsetWidth-1,zb));_=Math.max(0,Math.min(w.offsetHeight-1,_));this.Rr(n,null,zb/(w.offsetWidth-1),_/(w.offsetHeight-1));};q.Df=function(c){Ma.unsubscribe(ub._,this);Ma.unsubscribe(ub.ba,this);delete this[ub.ea];delete this[ub.fa];};q.Rr=function(c,i,h,g){var
C=this.Kr(j.RGBtoHSB(this.jsxrgb));if(i!=null)C[0]=i;if(h!=null)C[1]=h;if(g!=null)C[2]=g;var
yb=this.nU(C);var
_=j.HSBtoRGB(yb[0],yb[1],yb[2]);this.jsxrgb=(_[0]<<16)+(_[1]<<8)+_[2];this.dM(i!=null,h!=null||g!=null,yb);this.doEvent(ub.ga,{objEVENT:c,intRGB:this.jsxrgb});};q.dM=function(k,r,p){var
Fb=this.getRendered();if(Fb!=null){if(p==null)p=j.RGBtoHSB(this.jsxrgb);var
F=j.HSBtoRGB(p[0],1,1);var
wb=this.getAxis();var
Ia=this.Kr(p);if(k){switch(wb){case 1:ia(Fb,0,0,0).style.backgroundColor=ub.i+j.T7(F[0],F[1],F[2]);break;case 2:jsx3.html.updateCSSOpacity(ia(Fb,0,0,1),1-p[1]);break;case 3:jsx3.html.updateCSSOpacity(ia(Fb,0,0,2),1-p[2]);break;default:throw new
jsx3.Exception();}var
Ya=ia(Fb,0,2);var
ra=this.Wl(true).lg(0).lg(1);Ya.style.top=Math.round(Ia[0]*(ra.ec()-1))-Math.floor(j.UE/2)+ra.gl()+ub.ha;}if(r){switch(wb){case 1:jsx3.html.updateCSSOpacity(ia(Fb,0,1,1),1-p[1]);jsx3.html.updateCSSOpacity(ia(Fb,0,1,2),1-p[2]);break;case 2:ia(Fb,0,1,0).style.backgroundColor=ub.i+j.T7(F[0],F[1],F[2]);jsx3.html.updateCSSOpacity(ia(Fb,0,1,2),1-p[2]);break;case 3:ia(Fb,0,1,0).style.backgroundColor=ub.i+j.T7(F[0],F[1],F[2]);jsx3.html.updateCSSOpacity(ia(Fb,0,1,1),1-p[1]);break;default:throw new
jsx3.Exception();}var
ab=ia(Fb,0,0,4);var
ra=this.Wl(true).lg(0).lg(0);ab.style.left=Math.round(Ia[1]*(ra.mn()-1))-Math.floor(j.j2/2)+ub.ha;ab.style.top=Math.round(Ia[2]*(ra.ec()-1))-Math.floor(j.j2/2)+ub.ha;}}};j.HSBtoRGB=function(e,r,f){var
gb=0,ua=0,Ra=0,z;e=360*(e-Math.floor(e));var
oa=255*f;var
Ia=oa*r;var
kb=oa-Ia;if(e>=300||e<60){if(e>=300)e=e-360;gb=oa;z=e*Ia/60;if(z<0){ua=kb;Ra=ua-z;}else{Ra=kb;ua=Ra+z;}}else if(e>=60&&e<180){ua=oa;z=(e-120)*Ia/60;if(z<0){Ra=kb;gb=Ra-z;}else{gb=kb;Ra=gb+z;}}else if(e>=180&&e<300){Ra=oa;z=(e-240)*Ia/60;if(z<0){gb=kb;ua=gb-z;}else{ua=kb;gb=ua+z;}}return [Math.round(gb),Math.round(ua),Math.round(Ra)];};j.RGBtoHSB=function(d,o,a){if(arguments.length==1){a=d&255;o=(d&65280)>>8;d=(d&16711680)>>16;}var
L=0,mb=1,Ib=1;var
Kb=d>o?d:o;if(a>Kb)Kb=a;var
Na=d<o?d:o;if(a<Na)Na=a;Ib=Kb/255;if(Kb!=0)mb=(Kb-Na)/Kb;else mb=0;if(mb==0){L=0;}else{var
Jb=(Kb-d)/(Kb-Na);var
Hb=(Kb-o)/(Kb-Na);var
na=(Kb-a)/(Kb-Na);if(d==Kb)L=na-Hb;else if(o==Kb)L=2+Jb-na;else L=4+Hb-Jb;L=L/6;if(L<0)L=L+1;}return [L,mb,Ib];};j.T7=function(b,m,r){return (16777216+(b<<16)+(m<<8)+r).toString(16).substring(1);};});
