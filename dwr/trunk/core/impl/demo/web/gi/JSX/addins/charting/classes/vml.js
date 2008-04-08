/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.Package.definePackage("jsx3.chart",function(n){var
ub={k:"gradient",h:"number",c:"right",p:"; f;",q:"error evaluating '",v:"function",i:/\s+/,a:".benchmark",u:" field '",t:"error evaluating ",j:" ",s:" is not of type ",z:"string",d:"bottom",o:"var f = ",w:/\S/,r:"', ",l:"_",g:"",b:"top",m:"_eval",f:/\s*[,;]\s*/,y:"doSpyOut",n:"object",x:"doSpyOver",e:"left"};n.si=n.ADDIN.getVersion();n.LOG=jsx3.util.Logger.getLogger(n.jsxpackage.getName());n.LOG_BENCH=jsx3.util.Logger.getLogger(n.jsxpackage.getName()+ub.a);n.X8={};n.QTOP=ub.b;n.QRIGHT=ub.c;n.QBOTTOM=ub.d;n.QLEFT=ub.e;n.splitBox=function(f,i,p,j,l,g,c){var
Eb=null,db=null;if(l==ub.b){Eb=[f,i,p,Math.min(c,j-1)];db=[f,i+Eb[3],p,j-Eb[3]];}else if(l==ub.c){var
Ua=Math.min(g,p-1);Eb=[f+p-Ua,i,Ua,j];db=[f,i,p-Ua,j];}else if(l==ub.d){var
R=Math.min(c,j-1);Eb=[f,i+j-R,p,R];db=[f,i,p,j-R];}else if(l==ub.e){Eb=[f,i,Math.min(g,p-1),j];db=[f+Eb[2],i,p-Eb[2],j];}return [Eb,db];};n.isValueAxis=function(k){return n.LinearAxis&&k instanceof n.LinearAxis||n.LogarithmicAxis&&k instanceof n.LogarithmicAxis;};n.isCategoryAxis=function(d){return n.CategoryAxis&&d instanceof n.CategoryAxis;};n.Ii=function(d){if(d==null)return null;var
Jb=d.split(ub.f);if(Jb[0]===ub.g)Jb.shift();if(Jb.length>0&&Jb[Jb.length-1]===ub.g)Jb.pop();return Jb;};n.asNumber=function(a){if(a==null)return null;if(typeof a==ub.h)return a;return new
Number(a);};n.parseGradient=function(h){if(!h)return null;var
db=h.split(ub.i);if(db[0]===ub.g)db.shift();if(db.length>0&&db[db.length-1]===ub.g)db.pop();if(db.length==0)return null;if(db.length>4)return [db[0],db[1],db[2],db.slice(3).join(ub.j)];return db;};n.addGradient=function(f,q){var
cb=n.parseGradient(q);if(f!=null&&cb!=null){var
P=new
jsx3.vector.Fill(f.getColor(),f.getAlpha());P.setType(ub.k);P.setColor2(cb[0]);P.setAngle(cb[1]);P.setAlpha2(cb[2]);P.setColors(cb[3]);return P;}else return f;};n.sj=function(d,g,m){d[g]=m;d[ub.l+g+ub.m]=null;};n.Sl=function(g,d,b){if(b==null)b=ub.n;var
db=ub.l+d+ub.m;if(!g[db]&&g[d])try{var
Va=g.eval(ub.o+g[d]+ub.p);g[db]=Va;if(typeof g[db]!=b){n.LOG.error(ub.q+d+ub.r+g[db]+ub.s+b);g[db]=n.X8;}}catch(Kb){Kb=jsx3.NativeError.wrap(Kb);g[db]=n.X8;n.LOG.error(ub.t+b+ub.u+d,Kb);}return g[db]!=n.X8?g[db]:null;};n.vc=function(l,r){return n.Sl(l,r,ub.v);};n.Ng=function(p,q){var
aa=p.getBackgroundColor();if(aa!=null&&aa.match(ub.w)){var
fb=q.getFirstChildOfType(jsx3.vector.Fill);if(fb==null){fb=new
jsx3.vector.Fill();q.setFill(fb);}fb.setColor(aa);if(typeof p.getAlpha==ub.v)fb.setAlpha(p.getAlpha());}else q.setFill(null);};n.Fd=function(j,d){var
da=j.getBorderColor();if(da!=null&&da.match(ub.w)){var
C=d.getFirstChildOfType(jsx3.vector.Stroke);if(C==null){C=new
jsx3.vector.Stroke();d.setStroke(C);}C.setColor(da);if(typeof j.getBorderAlpha==ub.v)C.setAlpha(j.getBorderAlpha());if(typeof j.getBorderWidth==ub.v)C.setWidth(j.getBorderWidth());}else d.setStroke(null);};n.tg=function(c,i){var
la=jsx3.gui.Interactive;var
D=jsx3.gui.Event;if(i==null)i=c.getCanvas();var
sa={};if(c.getMenu()!=null)sa[D.MOUSEUP]=true;if(c.hasEvent(la.SELECT))sa[D.CLICK]=true;if(c.hasEvent(la.EXECUTE))sa[D.DOUBLECLICK]=true;if(c.hasEvent(la.SPYGLASS)){sa[D.MOUSEOVER]=ub.x;sa[D.MOUSEOUT]=ub.y;}for(var v in sa){var
T=sa[v];if(typeof T!=ub.z)T=la.Sm[v];jsx3.vector.paintEventHandler(c,v,T,i);}};});if(jsx3.IDE){jsx3.ide.loadTemplateCatalog("prop","properties/catalog.xml",jsx3.chart.ADDIN);jsx3.ide.loadTemplateCatalog("event","events/catalog.xml",jsx3.chart.ADDIN);}jsx3.Package.definePackage("jsx3.vector",function(m){var
ub={o:"&quot;",d:"event",k:/[^\d\.]/,r:"jsx3.GO('",c:"v",h:/^\s*(.*?)\s*$/,p:/;\s*$/,g:"",l:" ",q:";",b:"dm",v:"');",i:"$1",m:"on",a:"px",u:",this,'",f:"#",t:"(",j:/[^\d\.]/g,n:/\"/g,s:"').",e:"number"};m.fe=ub.a;m.mw=ub.b;m.ne=ub.c;m.eT=ub.d;m.colorAsHtml=function(a){return typeof a==ub.e?ub.f+(a+16777216).toString(16).substring(1):ub.g+a;};m.Bo=function(a,j,i){if(a==null)a=0;if(j==null)j=ub.a;if(typeof a==ub.e){return a+ub.g+j;}else{a=a.toString();a=a.replace(ub.h,ub.i);if(i)a=a.replace(ub.j,ub.g);return a.match(ub.k)?a:a+ub.g+j;}};m.Rh=function(d,c){return m.Bo(d,ub.g,true)+ub.l+m.Bo(c,ub.g,true);};m.ah=function(p){return Math.max(0,Math.min(1,p));};m.degreesToRadians=function(s){return jsx3.util.numMod(2*Math.PI/360*(-1*s+90),2*Math.PI);};m.paintEventHandler=function(k,j,b,f){var
Qa=ub.m+j;var
V=ub.g;var
Ib=f.getProperty(Qa);if(Ib){V=Ib.replace(ub.n,ub.o);if(!Ib.match(ub.p))V=V+ub.q;}V=V+(ub.r+k.getId()+ub.s+m.mw+ub.t+m.eT+ub.u+b+ub.v);if(V.length>0)f.setProperty(Qa,V);};m.updateVector=function(p,h){h.outerHTML=p.paint();};});jsx3.Class.defineClass("jsx3.html.Tag",null,null,function(g,q){var
ub={k:"Error appending '",h:"id",c:" to ",p:"Error setting style '",q:"' to '",v:'$1="$2" ',i:"className",A:"#",a:":",u:/\b([_a-zA-Z]\w*)=([^\s\"]+) /g,t:"<",j:";",s:/^<(\w+(\:\w+)?)\b/,z:"/>",d:", already has parent ",o:"",w:"</",r:"': ",l:"' to 'cssText': ",g:".",b:"can't append ",m:"a2",f:" to parent ",y:"string",n:"r7",x:">",e:"Illegal to append child "};var
Ka=jsx3.Exception;g.k8=[];q.a8=null;q.Ct=null;if(jsx3.CLASS_LOADER.VML){q.init=function(e,m){this.a8=m;this.Ct=e;this.qd=document.createElement(e?e+ub.a+m:m);this.a2=null;this.r7=null;};q.appendChild=function(c){if(this.onAppendChild(c)){if(c instanceof g&&c.getParent()!=null)throw new
Ka(ub.b+c+ub.c+this+ub.d+c.a2);if(this.r7==null)this.r7=[];this.r7.push(c);c.a2=this;}else throw new
Ka(ub.e+c+ub.f+this+ub.g);};q.removeChild=function(d){if(this.r7){var
Pa=jsx3.util.arrIndexOf(this.r7,d);if(Pa>=0){this.r7[Pa].a2=null;this.r7.splice(Pa,1);}}};q.replaceChild=function(h,i){if(this.r7){var
I=jsx3.util.arrIndexOf(this.r7,i);if(I>=0){this.r7[I].a2=null;this.r7[I]=h;h.a2=this;}}};q.removeChildren=function(){if(this.r7)this.r7.splice(0,this.r7.length);};}else if(jsx3.CLASS_LOADER.SVG){q.init=function(f,n){this.a8=n;this.Ct=f;if(n)this.qd=f?document.createElementNS(f,n):document.createElement(n);this.a2=null;this.r7=null;};q.appendChild=function(n){if(this.onAppendChild(n)){if(n instanceof g&&n.getParent()!=null)throw new
Ka(ub.b+n+ub.c+this+ub.d+n.a2);if(this.r7==null)this.r7=[];this.r7.push(n);n.a2=this;this.qd.appendChild(n.qd);}else throw new
Ka(ub.e+n+ub.f+this+ub.g);};q.removeChild=function(d){if(this.r7){var
wa=jsx3.util.arrIndexOf(this.r7,d);if(wa>=0){this.r7[wa].a2=null;this.r7.splice(wa,1);}}this.qd.removeChild(d.qd);};q.replaceChild=function(j,e){if(this.r7){var
W=jsx3.util.arrIndexOf(this.r7,e);if(W>=0){this.r7[W].a2=null;this.r7[W]=j;j.a2=this;}}this.qd.replaceChild(j.qd,e.qd);};q.removeChildren=function(){if(this.r7)this.r7.splice(0,this.r7.length);var
hb=this.qd.childNodes;for(var
Kb=hb.length-1;Kb>=0;Kb--)this.qd.removeChild(hb[Kb]);};}q.getParent=function(){return this.a2;};q.getChildren=function(){return this.r7==null?g.k8:this.r7;};q.getId=function(){return this.qd.id;};q.setId=function(l){this.setProperty(ub.h,l);};q.getClassName=function(){return this.qd.className;};q.setClassName=function(n){this.setProperty(ub.i,n);};q.setExtraStyles=function(f){try{this.qd.style.cssText+=(ub.j+f);}catch(Kb){throw new
Ka(ub.k+f+ub.l+jsx3.NativeError.wrap(Kb));}};q.release=function(){delete this[ub.m];if(this.r7){for(var
gb=this.r7.length-1;gb>=0;gb--)if(this.r7[gb].release)this.r7[gb].release();delete this[ub.n];}};q.onAppendChild=function(d){return true;};q.onRemoveChild=function(b){return true;};q.setProperty=function(f,i){var
Gb=arguments;for(var
Nb=0;Nb<Gb.length;Nb=Nb+2){f=Gb[Nb];i=Gb[Nb+1];if(i!=null)this.qd.setAttribute(f,i);else this.qd.removeAttribute(f);}};if(jsx3.CLASS_LOADER.SVG)q.setPropertyNS=function(i,h,f){if(f!=null)this.qd.setAttributeNS(i,h,f);else this.qd.removeAttributeNS(i,h);};q.getProperty=function(e){return this.qd.getAttribute(e);};q.removeProperty=function(c){var
ca=arguments;for(var
pb=0;pb<ca.length;pb++)this.qd.removeAttribute(ca[pb]);};q.setStyle=function(h,f){var
Oa=arguments;for(var
Mb=0;Mb<Oa.length;Mb=Mb+2){h=Oa[Mb];f=Oa[Mb+1];try{this.qd.style[h]=f==null?ub.o:f;}catch(Kb){throw new
Ka(ub.p+h+ub.q+f+ub.r+jsx3.NativeError.wrap(Kb));}}};q.getStyle=function(p){return this.qd.style[p];};q.removeStyle=function(i){var
Ha=arguments;for(var
K=0;K<Ha.length;K++)this.qd.style[Ha[K]]=ub.o;};q.getTagName=function(){return this.a8;};q.getTagNS=function(){return this.Ct;};if(jsx3.CLASS_LOADER.VML){q.paint=function(){this.paintUpdate();var
Lb=[];var
Cb=this.hn(Lb,0);return Lb.slice(0,Cb).join(ub.o);};q.hn=function(b,i){var
Ca=this.r7;var
M=jsx3.html.getOuterHTML(this.qd);M=M.replace(ub.s,function(a,j){return ub.t+j.toLowerCase();});M=M.replace(ub.u,ub.v);var
fb=M.lastIndexOf(ub.w);if(fb>=0&&M.substring(fb).indexOf(this.qd.nodeName)!=2)fb=-1;if(Ca!=null&&Ca.length>0){var
Mb=null,qa=null;if(fb>=0){Mb=M.substring(0,fb);qa=M.substring(fb);}else{Mb=M;qa=ub.w+this.qd.nodeName.toLowerCase()+ub.x;}b[i++
]=Mb;for(var
R=0;R<Ca.length;R++){var
S=Ca[R];if(typeof S==ub.y)b[i++
]=S;else i=S.hn(b,i);}b[i++
]=qa;}else{if(fb>=0)b[i++
]=M.substring(0,fb-1);else b[i++
]=M.substring(0,M.length-1);b[i++
]=ub.z;}return i;};}else if(jsx3.CLASS_LOADER.SVG)q.paintDom=function(){this.paintUpdate();return this.qd;};q.paintUpdate=function(){var
y=this.r7;if(y)for(var
xa=0;xa<y.length;xa++)y[xa].paintUpdate();};q.toString=function(){return ub.t+this.getTagName()+ub.A+this.getId()+ub.z;};q.getFirstChildOfType=function(e){if(typeof e==ub.y)e=jsx3.Class.forName(e).getConstructor();if(this.r7){var
fb=this.r7;for(var
yb=0;yb<fb.length;yb++)if(fb[yb] instanceof e)return fb[yb];}return null;};});jsx3.Class.defineClass("jsx3.html.Text",jsx3.html.Tag,null,function(q,g){var
ub={a:"",c:'"]',b:'[jsx3.html.Text "'};if(jsx3.CLASS_LOADER.VML){g.init=function(s){this.oM=s;};g.hn=function(n,p){n[p]=this.oM;return p+1;};g.getText=function(){return this.oM;};g.setText=function(m){this.oM=m;};g.paint=function(){return this.oM;};}else if(jsx3.CLASS_LOADER.SVG){g.init=function(f){this.qd=document.createTextNode(f!=null?f:ub.a);};g.getText=function(){return this.qd.nodeValue;};g.setText=function(d){this.qd.nodeValue=d;};}g.onAppendChild=function(f){return false;};g.toString=function(){return ub.b+this.getText()+ub.c;};});jsx3.Class.defineClass("jsx3.html.BlockTag",jsx3.html.Tag,null,function(b,l){var
ub={o:"",d:" to ",k:"position",c:" of ",h:"height",p:/[^\d\-]+/,g:"width",l:"zIndex",b:"trying to set ",i:"margin",m:"backgroundColor",a:"left",f:"top",j:"padding",n:"number",e:"px"};l.init=function(e,o,d,g,p,h){this.jsxsuper(e,o);this.setDimensions(d,g,p,h);};l.getLeft=function(){var
W=this.getStyle(ub.a);return W!=null?parseInt(W):null;};l.c0=function(h,c){if(c==null){this.setStyle(h,null);}else{var
t=parseInt(c);if(isNaN(t))jsx3.chart.LOG.debug(ub.b+h+ub.c+this+ub.d+c);else this.setStyle(h,t+ub.e);}};l.setLeft=function(k){this.c0(ub.a,k);};l.getTop=function(){var
Ma=this.getStyle(ub.f);return Ma!=null?parseInt(Ma):null;};l.setTop=function(d){this.c0(ub.f,d);};l.getWidth=function(){var
J=this.getStyle(ub.g);return J!=null?parseInt(J):null;};l.setWidth=function(a){this.c0(ub.g,a);};l.getHeight=function(){var
Qa=this.getStyle(ub.h);return Qa!=null?parseInt(Qa):null;};l.setHeight=function(c){this.c0(ub.h,c);};l.getMargin=function(){return this.getStyle(ub.i);};l.setMargin=function(f){this.setStyle(ub.i,f);};l.getPadding=function(){return this.getStyle(ub.j);};l.setPadding=function(k){this.setStyle(ub.j,k);};l.getPosition=function(){return this.getStyle(ub.k);};l.setPosition=function(o){this.setStyle(ub.k,o);};l.getZIndex=function(){return this.getStyle(ub.l);};l.setZIndex=function(q){this.setStyle(ub.l,q);};l.getBackgroundColor=function(){return this.getStyle(ub.m);};l.setBackgroundColor=function(n){this.setStyle(ub.m,n);};l.getMarginDimensions=function(){return b.po(this.getMargin());};l.getPaddingDimensions=function(){return b.po(this.getPadding());};b.po=function(p){if(p)if(typeof p==ub.n){return [p,p,p,p];}else{var
Ma=(ub.o+p).split(ub.p);if(Ma[0]===ub.o)Ma.shift();if(Ma.length>0&&Ma[Ma.length]===ub.o)Ma.pop();if(Ma.length>=4){return [parseInt(Ma[0]),parseInt(Ma[1]),parseInt(Ma[2]),parseInt(Ma[3])];}else if(Ma.length>=1){var
xa=parseInt(Ma[0]);return [xa,xa,xa,xa];}}return [0,0,0,0];};l.getDimensions=function(){return [this.getLeft(),this.getTop(),this.getWidth(),this.getHeight()];};l.setDimensions=function(m,p,g,q){if(m instanceof Array){this.setLeft(m[0]);this.setTop(m[1]);this.setWidth(m[2]);this.setHeight(m[3]);}else{this.setLeft(m);this.setTop(p);this.setWidth(g);this.setHeight(q);}};});jsx3.Class.defineInterface("jsx3.html.FontTag",null,function(q,s){var
ub={d:"fontStyle",a:"fontFamily",h:"color",c:"px",f:"textAlign",g:"textDecoration",b:"fontSize",e:"fontWeight"};s.getFontFamily=function(){return this.getStyle(ub.a);};s.setFontFamily=function(a){this.setStyle(ub.a,a);};s.getFontSize=function(){return this.getStyle(ub.b);};s.setFontSize=function(h){this.setStyle(ub.b,isNaN(h)?h:h+ub.c);};s.getFontStyle=function(){return this.getStyle(ub.d);};s.setFontStyle=function(f){this.setStyle(ub.d,f);};s.getFontWeight=function(){return this.getStyle(ub.e);};s.setFontWeight=function(e){this.setStyle(ub.e,e);};s.getTextAlign=function(){return this.getStyle(ub.f);};s.setTextAlign=function(l){this.setStyle(ub.f,l);};s.getTextDecoration=function(){return this.getStyle(ub.g);};s.setTextDecoration=function(j){this.setStyle(ub.g,j);};s.getColor=function(){return this.getStyle(ub.h);};s.setColor=function(r){this.setStyle(ub.h,r);};});jsx3.Class.defineClass("jsx3.vector.Canvas",jsx3.html.BlockTag,null,function(s,a){var
ub={d:"coordsize",a:"group",c:"urn:schemas-microsoft-com:vml",b:"xmlns:v",e:"absolute"};a.init=function(m,p,g,q){this.jsxsuper(jsx3.vector.ne,ub.a,m,p,g,q);this.setProperty(ub.b,ub.c);};a.paintUpdate=function(){this.jsxsuper();if(this.getWidth()&&this.getHeight()){this.setProperty(ub.d,jsx3.vector.Rh(parseInt(this.getWidth()),parseInt(this.getHeight())));}else this.setProperty(ub.d,jsx3.vector.Rh(100,100));if(this.getPosition()!=ub.e){this.setLeft(null);this.setTop(null);}};});jsx3.Class.defineClass("jsx3.vector.Tag",jsx3.html.BlockTag,null,function(j,a){var
ub={a:"title",c:"coordsize",b:"rotation"};a.init=function(l,g,m,d,k){this.jsxsuper(jsx3.vector.ne,l,g,m,d,k);};a.getToolTip=function(){return this.getProperty(ub.a);};a.setToolTip=function(d){this.setProperty(ub.a,d);};a.getRotation=function(){return this.getStyle(ub.b);};a.setRotation=function(b){this.setStyle(ub.b,b);};a.paintUpdate=function(){this.jsxsuper();var
tb=this.getParent();if(this.getWidth()&&this.getHeight()){this.setProperty(ub.c,jsx3.vector.Rh(parseInt(this.getWidth()),parseInt(this.getHeight())));}else this.removeProperty(ub.c);};});jsx3.Class.defineClass("jsx3.vector.Stroke",jsx3.html.Tag,null,function(m,s){var
ub={d:"/>",k:" opacity='",c:" ",h:"'",g:" id='",l:" weight='",b:"<stroke ",i:" on='",m:/\s+/,a:"stroke",f:":",j:" color='",e:"<"};var
ga=jsx3.vector;m.di=ub.a;s.init=function(p,j,h){this.jsxsuper(ga.ne,m.di);this.os=null;this.tU=p!=null?p:0;this.qz=j!=null?j:1;this.ES=h!=null?ga.ah(h):1;};s.getColor=function(){return this.tU;};s.getColorHtml=function(){return ga.colorAsHtml(this.tU);};s.setColor=function(e){this.tU=e;};s.getWidth=function(){return this.qz;};s.setWidth=function(n){this.qz=n;};s.getAlpha=function(){return this.ES;};s.setAlpha=function(p){this.ES=p!=null?ga.ah(p):null;};s.onAppendChild=function(n){return false;};s.toString=function(){return ub.b+this.getColorHtml()+ub.c+this.qz+ub.c+this.ES+ub.d;};s.paint=function(){var
qa=ub.e+ga.ne+ub.f+this.getTagName();if(this.getId()!=null)qa=qa+(ub.g+this.getId()+ub.h);var
da=this.getColorHtml();if(this.os!=null)qa=qa+(ub.i+this.os+ub.h);if(da!=null)qa=qa+(ub.j+da+ub.h);if(this.ES!=null&&this.ES<1)qa=qa+(ub.k+this.ES+ub.h);if(this.qz!=null)qa=qa+(ub.l+ga.Bo(this.qz)+ub.h);qa=qa+ub.d;return qa;};s.hn=function(i,b){i[b]=this.paint();return b+1;};s.Kk=function(){return this.ES==1||this.ES==null;};m.valueOf=function(k){if(jsx3.util.strEmpty(k))return null;if(k instanceof m)return k;var
Da=k.toString().split(ub.m);return new
m(Da[0],Da[1],Da[2]);};});jsx3.Class.defineClass("jsx3.vector.Fill",jsx3.html.Tag,null,function(c,b){var
ub={o:" colors='",d:"/>",k:" opacity='",r:/\s+/,c:" ",h:"'",p:" angle='",g:" id='",l:"solid",q:" o:opacity2='",b:"<fill ",i:" on='",m:" type='",a:"fill",f:":",j:" color='",n:" color2='",e:"<"};var
ga=jsx3.vector;c.di=ub.a;b.init=function(e,m){this.jsxsuper(ga.ne,c.di);this.os=null;this.tU=e!=null?e:0;this.ES=m!=null?ga.ah(m):1;this.eI=null;this.B9=null;this.z3=null;this.j3=null;this.FR=null;};b.getColor=function(){return this.tU;};b.getColorHtml=function(){return ga.colorAsHtml(this.tU);};b.setColor=function(s){this.tU=s;};b.getAlpha=function(){return this.ES;};b.setAlpha=function(l){this.ES=l!=null?ga.ah(l):null;};b.getType=function(){return this.eI;};b.setType=function(g){this.eI=g;};b.getColor2=function(){return this.B9;};b.getColor2Html=function(){return ga.colorAsHtml(this.B9);};b.setColor2=function(n){this.B9=n;};b.getAlpha2=function(){return this.z3;};b.setAlpha2=function(m){this.z3=m;};b.getAngle=function(){return this.j3;};b.setAngle=function(l){this.j3=l;};b.getColors=function(){return this.FR;};b.setColors=function(l){this.FR=l;};b.toString=function(){return ub.b+this.getColorHtml()+ub.c+this.getAlpha()+ub.d;};b.paint=function(){var
O=ub.e+ga.ne+ub.f+this.getTagName();if(this.getId()!=null)O=O+(ub.g+this.getId()+ub.h);var
ma=this.getColorHtml();if(this.os!=null)O=O+(ub.i+this.os+ub.h);if(ma!=null)O=O+(ub.j+ma+ub.h);if(this.ES!=null&&this.ES<1)O=O+(ub.k+this.ES+ub.h);if(this.eI&&this.eI!=ub.l){O=O+(ub.m+this.eI+ub.h);var
ya=this.getColor2Html();if(ya!=null)O=O+(ub.n+ya+ub.h);if(this.FR!=null)O=O+(ub.o+this.FR+ub.h);if(this.j3!=null)O=O+(ub.p+this.j3+ub.h);if(this.z3!=null)O=O+(ub.q+this.z3+ub.h);}O=O+ub.d;return O;};b.hn=function(i,f){i[f]=this.paint();return f+1;};b.Kk=function(){return (this.ES==1||this.ES==null)&&(!this.eI||this.eI==ub.l);};c.valueOf=function(l){if(jsx3.util.strEmpty(l))return null;if(l instanceof c)return l;var
pb=l.toString().split(ub.r);return new
c(pb[0],pb[1]);};b.onAppendChild=function(o){return false;};});jsx3.Class.defineClass("jsx3.vector.Group",jsx3.vector.Tag,null,function(i,k){var
ub={a:"group"};k.init=function(p,s,d,a){this.jsxsuper(i.di,p,s,d,a);};i.di=ub.a;k.onAppendChild=function(a){return a instanceof i||a instanceof jsx3.vector.Shape;};});jsx3.Class.defineClass("jsx3.vector.Shape",jsx3.vector.Tag,null,function(l,g){var
ub={o:"true",d:" ",k:"string",r:"strokeweight",c:"m",h:"at",p:"fillcolor",g:"wa",l:"filled",q:"strokecolor",b:"t",i:"x",m:"false",a:"path",f:"l",j:"shape",n:"stroked",e:"r"};var
xb=jsx3.html.Tag;var
Ab=jsx3.vector;g.init=function(k,h,s,d,a){this.jsxsuper(k!=null?k:l.di,h,s,d,a);this.id=null;this.Wi=null;};g.getPath=function(){return this.getProperty(ub.a);};g.setPath=function(r){this.setProperty(ub.a,r);};g.pathMoveTo=function(h,o,r){this.uC((r?ub.b:ub.c)+ub.d+h+ub.d+o);return this;};g.pathLineTo=function(s,r,j){this.uC((j?ub.e:ub.f)+ub.d+s+ub.d+r);return this;};g.pathArcTo=function(m,s,d,c,j,q,b,i,e){this.uC((e?ub.g:ub.h)+ub.d+(m-d)+ub.d+(s-c)+ub.d+(m+d)+ub.d+(s+c)+ub.d+j+ub.d+q+ub.d+b+ub.d+i);return this;};g.pathClose=function(){this.uC(ub.i);return this;};g.uC=function(a){var
Na=this.getPath();if(!Na)this.setPath(a);else this.setPath(Na+ub.d+a);};l.di=ub.j;g.paintUpdate=function(){var
Fa=this.getChildren().concat();for(var
vb=0;vb<Fa.length;vb++)if(typeof Fa[vb]==ub.k)this.removeChild(Fa[vb]);this.jsxsuper();this.setProperty(ub.l,this.getFill()==null?ub.m:null,ub.n,this.getStroke()==null?ub.m:null);if(this.id!=null)if(this.id.Kk()){this.setProperty(ub.l,this.id.os!=null?this.id.os?ub.o:ub.m:null,ub.p,this.id.getColorHtml());}else{this.removeProperty(ub.l,ub.p);this.appendChild(this.id.paint());}if(this.Wi!=null)if(this.Wi.Kk()){var
Lb=this.Wi.getWidth();this.setProperty(ub.n,this.Wi.os!=null?this.Wi.os?ub.o:ub.m:null,ub.q,this.Wi.getColorHtml(),ub.r,Lb!=null?Ab.Bo(Lb):null);}else{this.removeProperty(ub.n,ub.q,ub.r);this.appendChild(this.Wi.paint());}};g.onAppendChild=function(a){return a instanceof Ab.TextLine||a instanceof Ab.Fill||a instanceof Ab.Stroke||typeof a==ub.k;};g.setFill=function(d){this.id=d;};g.setStroke=function(e){this.Wi=e;};g.getFill=function(){return this.id;};g.getStroke=function(){return this.Wi;};});jsx3.Class.defineClass("jsx3.vector.Line",jsx3.vector.Shape,null,function(k,n){var
ub={d:"to",i:"}/>",a:"line",h:"} {",c:"from",f:" {",g:",",b:"coordsize",e:"<line "};n.init=function(g,j,f,m,e,l){var
Lb=Math.max(g,Math.max(f,e))-Math.min(g,Math.min(f,e));var
Hb=Math.max(j,Math.max(m,l))-Math.min(j,Math.min(m,l));this.jsxsuper(ub.a,g,j,Math.max(Lb,16),Math.max(Hb,16));this.iF=f;this.nv=m;this.VN=e;this.rw=l;};n.setPoints=function(c,j,b,i){this.iF=c;this.nv=j;this.VN=b;this.rw=i;var
U=this.getLeft();var
ib=this.getTop();var
Lb=Math.max(U,Math.max(c,b))-Math.min(U,Math.min(c,b));var
sb=Math.max(ib,Math.max(j,i))-Math.min(ib,Math.min(j,i));this.setWidth(Lb);this.setHeight(sb);};n.getX1=function(){return this.iF;};n.setX1=function(s){this.iF=s;};n.getY1=function(){return this.nv;};n.setY1=function(d){this.nv=d;};n.getX2=function(){return this.VN;};n.setX2=function(c){this.VN=c;};n.getY2=function(){return this.rw;};n.setY2=function(i){this.rw=i;};n.paintUpdate=function(){this.jsxsuper();this.removeProperty(ub.b);this.setProperty(ub.c,jsx3.vector.Rh(this.iF,this.nv),ub.d,jsx3.vector.Rh(this.VN,this.rw));};n.toString=function(){return ub.e+this.getId()+ub.f+this.getX1()+ub.g+this.getY1()+ub.h+this.getX2()+ub.g+this.getY2()+ub.i;};});jsx3.Class.defineClass("jsx3.vector.Rectangle",jsx3.vector.Shape,null,function(r,i){var
ub={a:"rect",b:"coordsize"};i.init=function(p,s,d,a){this.jsxsuper(ub.a,p,s,d,a);};i.clipToBox=function(p){this.clipTo(p.getLeft(),p.getTop(),p.getWidth(),p.getHeight());};i.clipTo=function(h,g,e,n){var
db=Math.max(this.getLeft(),h);var
t=Math.max(this.getTop(),g);var
gb=Math.min(this.getWidth()-(db-this.getLeft()),h+e-db);var
wb=Math.min(this.getHeight()-(t-this.getTop()),g+n-t);this.setDimensions(db,t,gb,wb);};i.paintUpdate=function(){this.jsxsuper();this.removeProperty(ub.b);};});jsx3.Class.defineClass("jsx3.vector.Oval",jsx3.vector.Shape,null,function(a,b){var
ub={a:"oval",b:"coordsize"};b.init=function(p,s,d,k){this.jsxsuper(a.di,p,s,d,k);};a.di=ub.a;b.paintUpdate=function(){this.jsxsuper();this.removeProperty(ub.b);};});jsx3.Class.defineClass("jsx3.vector.Polygon",jsx3.vector.Shape,null,function(r,o){var
ub={a:"polyline",c:"points",b:" "};o.init=function(k,n,q){this.jsxsuper(ub.a,k,n);this.z0=null;this.n9=q;this.kr=q!=null?q.join(ub.b):null;};o.setPoints=function(i){this.z0=i;this.n9=null;this.kr=i!=null?i.join(ub.b):null;};o.setPointsAsNumberArray=function(b){this.z0=null;this.n9=b;this.kr=b!=null?b.join(ub.b):null;};o.setPointsAsString=function(q){this.z0=null;this.n9=null;this.kr=q;};o.paintUpdate=function(){this.jsxsuper();this.setProperty(ub.c,this.kr);};});jsx3.Class.defineClass("jsx3.vector.TextLine",jsx3.vector.Shape,[jsx3.html.FontTag],function(g,b){var
ub={k:"return jsx3.html.FontTag.prototype.",h:"stroke",c:" ",p:"setExtraStyles",q:"return jsx3.html.Tag.prototype.",v:".",i:"false",a:"path",u:"string",t:"v-text-align",j:"Ur",s:"color",z:"stroked",d:"textpath",o:"setClassName",w:"#000000",r:"",l:".apply(this.",g:"textpathok",b:"m 0 0 l ",m:", arguments);",f:"true",y:"filled",n:"getClassName",x:"textAlign",e:"on"};var
bb=jsx3.html.Tag;var
Aa=jsx3.app.Browser;var
G=jsx3.vector;b.init=function(r,f,q,e,a){var
Jb=Math.max(1,Math.max(r,q)-Math.min(r,q));var
v=Math.max(1,Math.max(f,e)-Math.min(f,e));this.jsxsuper(null,r,f,Jb,v);this.setProperty(ub.a,ub.b+(q-r)+ub.c+(e-f));this.Ur=new
bb(G.ne,ub.d);this.Ur.setProperty(ub.e,ub.f);this.ks=new
bb(G.ne,ub.a);this.ks.setProperty(ub.g,ub.f);this.YI=new
bb(G.ne,ub.h);this.YI.setProperty(ub.e,ub.i);this.setText(a);};var
ab=ub.j;var
pa=jsx3.html.FontTag.jsxclass.getInstanceMethods();for(var
Sa=0;Sa<pa.length;Sa++){var
Ia=pa[Sa];b[Ia.getName()]=new
Function(ub.k+Ia.getName()+ub.l+ab+ub.m);}var
B=[ub.n,ub.o,ub.p,ub.p];for(var
Sa=0;Sa<B.length;Sa++)b[B[Sa]]=new
Function(ub.q+B[Sa]+ub.l+ab+ub.m);b.setColor=function(j){if(j!=null&&j!=ub.r)this.setFill(new
G.Fill(j));this.Ur.setStyle(ub.s,j);};b.getTextAlign=function(){return this.Ur.getStyle(ub.t);};b.setTextAlign=function(k){this.Ur.setStyle(ub.t,k);};b.getText=function(){return this.Ur.getProperty(ub.u);};b.setText=function(s){this.Ur.setProperty(ub.u,s);};b.onAppendChild=function(o){var
J=o.getTagName();return this.jsxsuper(o)||J==ub.a||J==ub.d||J==ub.h;};b.paintUpdate=function(){var
Ua=null;if(this.getFill()==null){var
C=this.getColor();if(!C){if(Ua==null)Ua=Aa.getStyleClass(ub.v+this.getClassName())||Number(0);if(Ua)C=Ua.color;}this.setFill(new
G.Fill(C||ub.w));}if(!this.getTextAlign()){var
Ca=this.Ur.getStyle(ub.x);if(Ca==null){if(Ua==null)Ua=Aa.getStyleClass(ub.v+this.getClassName())||Number(0);if(Ua)Ca=Ua.textAlign;}if(Ca)this.setTextAlign(Ca);}this.jsxsuper();this.setProperty(ub.y,this.getFill()==null?ub.i:ub.f,ub.z,this.getStroke()==null?null:ub.f);if(this.Ur.getParent()==null)this.appendChild(this.Ur);if(this.ks.getParent()==null)this.appendChild(this.ks);if(this.getStroke()==null){if(this.YI.getParent()==null)this.appendChild(this.YI);}else this.removeChild(this.YI);};});jsx3.Class.defineClass("jsx3.vector.LineGroup",jsx3.vector.Shape,null,function(n,m){var
ub={a:""};m.init=function(p,s,d,a){this.jsxsuper(null,p,s,d,a);};m.addLine=function(h,o,g,b){this.pathMoveTo(h,o).pathLineTo(g,b);};m.addRelativeLine=function(o,c,d,l){this.pathMoveTo(o,c).pathLineTo(d,l,true);};m.clearLines=function(){this.setPath(ub.a);};});jsx3.Class.defineClass("jsx3.vector.RectangleGroup",jsx3.vector.Shape,null,function(h,r){var
ub={a:""};r.init=function(p,s,d,a){this.jsxsuper(null,p,s,d,a);};r.addRectangle=function(b,o,g,n){this.pathMoveTo(b,o).pathLineTo(g,o).pathLineTo(g,n).pathLineTo(b,n).pathClose();};r.addRelativeRectangle=function(o,c,q,m){this.pathMoveTo(o,c).pathLineTo(q,0,true).pathLineTo(0,m,true).pathLineTo(-1*q,0,true).pathClose();};r.clearRectangles=function(){this.setPath(ub.a);};});jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.vector.Block",jsx3.gui.Block,null,function(s,r){var
ub={d:"box",a:"relative",c:"relativebox",b:"absolute",e:"span"};r.getCanvas=function(){if(this.vK==null)this.createVector();return this.vK;};r.createVector=function(){this.applyDynamicProperties();var
T=this.vK;var
zb=this.createCanvas();zb.setId(this.getId());zb.setZIndex(this.getZIndex());zb.setPosition(this.getRelativePosition()?ub.a:ub.b);this.eP(zb);var
Gb=this.getAttributes();for(var Pa in Gb)zb.setProperty(Pa,Gb[Pa]);if(T!=null)T.release();this.vK=zb;return zb;};r.updateVector=function(o){this.applyDynamicProperties();this.eP(o);return true;};r.eP=function(k){var
T=this.Wl(true);k.setLeft(T.bc());k.setTop(T.mh());k.setWidth(T.cm());k.setHeight(T.Zm());};r.createCanvas=function(){return new
jsx3.vector.Canvas();};r.paint=function(){if(this.vK==null)this.createVector();return this.vK.paint();};r.repaint=function(){if(!this.vK||!this.updateVector(this.vK))this.createVector();return this.jsxsuper();};r.paintEventHandler=function(m,e,i){if(i==null)i=this.getCanvas();jsx3.vector.paintEventHandler(this,m,e,i);};r.Gc=function(d){if(this.getParent()&&(d==null||isNaN(d.parentwidth)||isNaN(d.parentheight))){d=this.getParent().uf(this);}else if(d==null)d={};var
ha=this.getRelativePosition()!=jsx3.gui.Block.ABSOLUTE;var
La=ha?null:this.getLeft();var
hb=ha?null:this.getTop();if(!d.boxtype)d.boxtype=ha?ub.c:ub.d;d.tagname=ub.e;if(d.left==null&&La!=null)d.left=La;if(d.top==null&&hb!=null)d.top=hb;d.width=this.getWidth();d.height=this.getHeight();return new
jsx3.gui.Painted.Box(d);};r.Rc=function(n,k,d){if(k){var
cb=this.Wl(true,n);var
E=cb.recalculate(n,k,d);if(E.w||E.h)this.repaint();}};r.doClone=function(k){this.vK=null;return this.jsxsuper(k);};r.getCanSpy=function(){return true;};});jsx3.Class.defineInterface("jsx3.chart.PointRenderer",null,function(i,b){var
ub={d:"y2",a:"x1",c:"x2",f:"stroke",g:"area",b:"y1",e:"fill"};var
L=jsx3.vector;b.render=jsx3.Method.newAbstract(ub.a,ub.b,ub.c,ub.d,ub.e,ub.f);b.areaToRadius=jsx3.Method.newAbstract(ub.g);i.CIRCLE=i.jsxclass.newInnerClass();i.CIRCLE.areaToRadius=function(g){return Math.sqrt(g/Math.PI);};i.CIRCLE.render=function(k,r,j,q,l,h){var
P=new
L.Oval(k,r,j-k,q-r);P.setFill(l);P.setStroke(h);return P;};i.CROSS=i.jsxclass.newInnerClass();i.CROSS.SI=0.6;i.CROSS.areaToRadius=function(p){return Math.sqrt(p/(1-this.SI/Math.SQRT2))/2;};i.CROSS.render=function(k,r,j,q,l,h){var
rb=j-k;var
mb=this.SI;var
T=Math.round(rb*(1-mb)/2);var
ja=Math.round(rb*mb/2);var
vb=Math.round(rb-rb*(1-mb)/2);var
ia=Math.round(rb/2);var
Nb=new
L.Polygon(0,0,[k,r,k+T,r,k+ia,r+ja,k+vb,r,j,r,j,r+T,j-ja,r+ia,j,r+vb,j,q,j-T,q,j-ia,q-ja,j-vb,q,k,q,k,q-T,k+ja,q-ia,k,q-vb,k,r]);Nb.setFill(l);Nb.setStroke(h);return Nb;};i.DIAMOND=i.jsxclass.newInnerClass();i.DIAMOND.KF=1.2;i.DIAMOND.areaToRadius=function(n){return Math.sqrt(n)/2;};i.DIAMOND.render=function(a,h,s,g,r,o){var
t=(a+s)/2;var
Fa=(h+g)/2;var
Xa=(s-a)/this.KF;var
Pa=(g-h)/this.KF;var
gb=new
L.Rectangle(Math.round(t-Xa/2),Math.round(Fa-Pa/2),Math.round(Xa),Math.round(Pa));gb.setRotation(45);gb.setFill(r);gb.setStroke(o);return gb;};i.BOX=i.jsxclass.newInnerClass();i.BOX.areaToRadius=function(f){return Math.sqrt(i.DIAMOND.KF*i.DIAMOND.KF*f)/2;};i.BOX.render=function(l,s,k,r,m,j){var
J=new
L.Rectangle(l,s,k-l,r-s);J.setFill(m);J.setStroke(j);return J;};i.TRIANGLE=i.jsxclass.newInnerClass();i.TRIANGLE.areaToRadius=function(l){return Math.sqrt(2*l)/2;};i.TRIANGLE.render=function(d,k,a,h,c,s){var
Ka=Math.round((d+a)/2);var
E=new
L.Polygon(0,0,[Ka,k,a,h,d,h,Ka,k]);E.setFill(c);E.setStroke(s);return E;};});jsx3.chart.Renderers=jsx3.chart.PointRenderer;jsx3.chart.Renderers.Circle=jsx3.chart.PointRenderer.CIRCLE;jsx3.chart.Renderers.Cross=jsx3.chart.PointRenderer.CROSS;jsx3.chart.Renderers.Diamond=jsx3.chart.PointRenderer.DIAMOND;jsx3.chart.Renderers.Box=jsx3.chart.PointRenderer.BOX;jsx3.chart.Renderers.Triangle=jsx3.chart.PointRenderer.TRIANGLE;jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.chart.ChartComponent",jsx3.gui.Block,null,function(s,i){var
ub={a:"relative",b:"absolute"};var
Pa=jsx3.gui.Event;var
ya=jsx3.gui.Interactive;var
Fb=jsx3.vector;var
C=jsx3.chart;s.MASK_PROPS_NOEDIT={NN:false,SS:false,EE:false,WW:false,MM:false};i.init=function(o){this.jsxsuper(o);this.E5=null;};i.getChart=function(){return this.findAncestor(function(q){return C.Chart&&q instanceof C.Chart;},true);};i.doClone=function(c){this.E5=null;return this.jsxsuper(c);};i.updateView=function(){this.applyDynamicProperties();var
u=null,Ma=this._canvas;if(Ma!=null)u=Ma.getParent();var
Mb=new
Fb.Group();Mb.setId(this.getId());Mb.setDimensions(this.getDimensions());Mb.setZIndex(this.getZIndex());Mb.setPosition(this.getRelativePosition()?ub.a:ub.b);var
gb=this.getAttributes();for(var Aa in gb)Mb.setProperty(Aa,gb[Aa]);if(Ma!=null)Ma.release();if(u!=null)u.replaceChild(Mb,Ma);this._canvas=Mb;};i.getCanvas=function(){if(this._canvas==null)this.updateView();return this._canvas;};i.tg=function(n){jsx3.chart.tg(this,n);};if(jsx3.CLASS_LOADER.VML){i.paint=function(){if(this._canvas==null)this.updateView();return this._canvas.paint();};}else if(jsx3.CLASS_LOADER.SVG){i.isDomPaint=function(){return true;};i.paint=function(){throw new
jsx3.Exception();};i.paintDom=function(){if(this._canvas==null)this.updateView();return this._canvas.paintDom();};}i.repaint=function(){this.updateView();return this.jsxsuper();};i.Ho=function(d,o){if(this.E5==null)this.E5={};this.E5[d]=o;};i.pj=function(c){return this.E5!=null?this.E5[c]:null;};i.Ll=function(a){if(this.E5!=null)delete this.E5[a];};i.getMaskProperties=function(){return s.MASK_PROPS_NOEDIT;};i.getPaddingDimensions=function(){return jsx3.html.BlockTag.po(this.getPadding());};i.getCanSpy=function(){return true;};s.getVersion=function(){return C.si;};});jsx3.require("jsx3.chart.ChartComponent");jsx3.Class.defineClass("jsx3.chart.ChartLabel",jsx3.chart.ChartComponent,null,function(j,p){var
mb=jsx3.vector;j.DEFAULT_WIDTH=100;j.ROTATION_NORMAL=0;j.ROTATION_CW=90;j.ROTATION_CCW=270;p.init=function(h,m){this.jsxsuper(h);this.jsxtext=m;this.alpha=null;this.borderStroke=null;this.preferredWidth=null;this.preferredHeight=null;this.labelRotation=0;};p.getText=function(){return this.jsxtext;};p.setText=function(e){this.jsxtext=e;};p.getPreferredWidth=function(){if(this.preferredWidth!=null){return this.preferredWidth;}else if(this.isRotated()){return this.YT();}else{var
P=this.getPaddingDimensions();return j.DEFAULT_WIDTH+P[0]+P[2];}};p.setPreferredWidth=function(l){this.preferredWidth=l;};p.getPreferredHeight=function(){if(this.preferredHeight!=null){return this.preferredHeight;}else if(this.isRotated()){var
ja=this.getPaddingDimensions();return j.DEFAULT_WIDTH+ja[1]+ja[3];}else return this.YT();};p.setPreferredHeight=function(d){this.preferredHeight=d;};p.YT=function(){var
ta=this.getPaddingDimensions();var
Za=this.getFontSize()!=null?this.getFontSize():10;return Math.round(Za*1.5)+(this.isRotated()?ta[1]+ta[3]:ta[0]+ta[2]);};p.getAlpha=function(){return this.alpha;};p.setAlpha=function(r){this.alpha=r!=null?mb.ah(r):null;};p.getBorderStroke=function(){return this.borderStroke;};p.setBorderStroke=function(f){this.borderStroke=f;};p.getLabelRotation=function(){return this.labelRotation;};p.setLabelRotation=function(m){this.labelRotation=m;};p.isRotated=function(){return this.labelRotation==90||this.labelRotation==270;};p.updateView=function(){this.jsxsuper();var
_=this.getCanvas();var
oa=this.getWidth();var
ta=this.getHeight();var
I=this.getPaddingDimensions();this.tg();var
W=new
mb.Rectangle(0,0,oa,ta);_.appendChild(W);jsx3.chart.Ng(this,W);var
hb=W.getFill();var
Gb=mb.Stroke.valueOf(this.borderStroke);if(Gb!=null){W.setStroke(Gb);}else if(hb!=null&&(this.alpha==null||this.alpha==1))W.setStroke(new
mb.Stroke(hb.getColor()));var
Sa=0,eb=0,T=0,z=0;if(this.isRotated()){T=(z=Math.round(I[3]+(oa-I[1]-I[3])/2));if(this.labelRotation==90){eb=ta;}else Sa=ta;}else{Sa=(eb=Math.round(ta/2));T=0;z=oa;}var
M=new
mb.TextLine(T,Sa,z,eb,this.jsxtext);M.setColor(this.getColor());M.setClassName(this.getClassName());M.setFontFamily(this.jsxfontname);M.setFontWeight(this.jsxfontweight);M.setFontSize(this.jsxfontsize);M.setTextAlign(this.jsxtextalign);_.appendChild(M);};p.onSetChild=function(){return false;};p.onSetParent=function(a){return a instanceof jsx3.chart.ChartComponent||a instanceof jsx3.chart.Chart;};j.getVersion=function(){return jsx3.chart.si;};});jsx3.require("jsx3.chart.ChartComponent");jsx3.Class.defineClass("jsx3.chart.Axis",jsx3.chart.ChartComponent,null,function(k,q){var
ub={o:"index",d:"none",k:"-",r:"tickPlacement",c:"cross",h:"%",p:"labelFunction",g:"axis",l:"",q:"labelPlacement",b:"outside",i:"0",m:"e",a:"inside",u:"bad placement value: ",f:"low",t:/\S/,j:".",n:"#000000",s:"minorTickPlacement",e:"high"};var
db=jsx3.vector;var
sb=db.Stroke;var
ha=jsx3.chart;k.TICK_INSIDE=ub.a;k.TICK_OUTSIDE=ub.b;k.TICK_CROSS=ub.c;k.TICK_NONE=ub.d;k.LABEL_HIGH=ub.e;k.LABEL_LOW=ub.f;k.LABEL_AXIS=ub.g;k.oQ={inside:1,outside:1,cross:1};k.LP={axis:1,high:1,low:1};k.BX=1;k.mD=2;k.YZ=4;k.PJ=3;k.Wp=7;k.Mq=8;k.gA=6;k.F8=5;k.f0=[k.PJ,k.YZ,k.mD,k.BX,k.mD,k.BX,k.PJ,k.YZ,k.Mq,k.gA,k.Wp,k.F8];k.o4=10;k.rE=50;k.dP=12;k.percent=function(s){return s+ub.h;};k.scientific=function(h,f){if(h==0)return ub.i;if(f==null)f=2;var
ua=h<0;h=Math.abs(h);var
Ja=Math.floor(Math.log(h)/Math.LN10);var
Db=Ja!=0?h/Math.pow(10,Ja):h;Db=Db.toString();var
sa=Db.indexOf(ub.j);if(sa>=0)if(Db.length-sa-1>f)Db=Db.substring(0,sa+1+f);return (ua?ub.k:ub.l)+Db+ub.m+Ja;};q.init=function(c,e,p){this.jsxsuper(c);this.horizontal=e!=null?jsx3.Boolean.valueOf(e):null;this.primary=p!=null?jsx3.Boolean.valueOf(p):null;this.length=100;this.showAxis=jsx3.Boolean.TRUE;this.axisStroke=ub.n;this.showLabels=jsx3.Boolean.TRUE;this.labelGap=3;this.labelRotation=0;this.labelPlacement=ub.g;this.tickLength=3;this.tickPlacement=ub.b;this.tickStroke=ub.n;this.minorTickDivisions=4;this.minorTickLength=3;this.minorTickPlacement=ub.d;this.minorTickStroke=null;this.labelFunction=null;this.labelClass=null;this.labelStyle=null;this.labelColor=null;this.displayWidth=null;};q.Xj=jsx3.Method.newAbstract(ub.o);q.Hf=jsx3.Method.newAbstract();q.mo=jsx3.Method.newAbstract();q.fl=jsx3.Method.newAbstract();q.getHorizontal=function(){return this.horizontal;};q.setHorizontal=function(c){this.horizontal=c;};q.ui=function(){return this.primary;};q.Ke=function(o){this.primary=o;};q.getLength=function(){return this.length;};q.setLength=function(j){this.length=j;};q.getShowAxis=function(){return this.showAxis;};q.setShowAxis=function(l){this.showAxis=l;};q.getLabelFunction=function(){return ha.vc(this,ub.p);};q.setLabelFunction=function(n){ha.sj(this,ub.p,n);};q.getAxisStroke=function(){return this.axisStroke;};q.setAxisStroke=function(l){this.axisStroke=l;};q.getShowLabels=function(){return this.showLabels;};q.setShowLabels=function(f){this.showLabels=f;};q.getLabelGap=function(){return this.labelGap;};q.setLabelGap=function(f){this.labelGap=f;};q.getLabelRotation=function(){return this.labelRotation;};q.setLabelRotation=function(o){this.labelRotation=o;};q.getLabelPlacement=function(){return this.labelPlacement;};q.setLabelPlacement=function(p){if(k.LP[p]){this.labelPlacement=p;}else throw new
jsx3.IllegalArgumentException(ub.q,p);};q.getTickLength=function(){return this.tickLength;};q.setTickLength=function(h){this.tickLength=h;};q.getTickPlacement=function(){return this.tickPlacement;};q.setTickPlacement=function(j){if(k.oQ[j]||j==ub.d){this.tickPlacement=j;}else throw new
jsx3.IllegalArgumentException(ub.r,j);};q.getTickStroke=function(){return this.tickStroke;};q.setTickStroke=function(s){this.tickStroke=s;};q.getMinorTickDivisions=function(){return this.minorTickDivisions;};q.setMinorTickDivisions=function(f){this.minorTickDivisions=f;};q.getMinorTickLength=function(){return this.minorTickLength;};q.setMinorTickLength=function(s){this.minorTickLength=s;};q.getMinorTickPlacement=function(){return this.minorTickPlacement;};q.setMinorTickPlacement=function(c){if(k.oQ[c]||c==ub.d){this.minorTickPlacement=c;}else throw new
jsx3.IllegalArgumentException(ub.s,c);};q.getMinorTickStroke=function(){return this.minorTickStroke;};q.setMinorTickStroke=function(j){this.minorTickStroke=j;};q.getLabelClass=function(){return this.labelClass;};q.setLabelClass=function(g){this.labelClass=g;};q.getLabelStyle=function(){return this.labelStyle;};q.setLabelStyle=function(a){this.labelStyle=a;};q.getLabelColor=function(){return this.labelColor;};q.setLabelColor=function(r){this.labelColor=r;};q.getDisplayWidth=function(){if(this.displayWidth!=null){return this.displayWidth;}else return this.horizontal?k.dP:k.rE;};q.setDisplayWidth=function(l){this.displayWidth=l;};q.updateView=function(){this.jsxsuper();var
x=this.getCanvas();var
Hb=this.getWidth();var
vb=this.getHeight();var
Va=this.getOpposingAxis();if(Va==null)return;var
Mb=this.Hs(Va);this.tg();if(this.showAxis){var
Bb=new
db.Line(0,0,0,0,0,0);x.appendChild(Bb);var
T=sb.valueOf(this.axisStroke);if(T==null)T=new
sb();Bb.setStroke(T);if(this.horizontal)Bb.setPoints(0,Mb,this.length,Mb);else Bb.setPoints(Mb,0,Mb,this.length);}var
Fa=this.Hf();if(k.oQ[this.tickPlacement]&&this.tickLength>0){var
D=new
db.LineGroup(0,0,Hb,vb);x.appendChild(D);var
T=sb.valueOf(this.tickStroke);D.setStroke(T);var
B=this.hq(this.tickPlacement,this.tickLength);var
O=B[0];var
J=Mb+B[1];this.MU(D,Fa,J,O);}if(k.oQ[this.minorTickPlacement]&&this.minorTickLength>0){var
wb=new
db.LineGroup(0,0,Hb,vb);x.appendChild(wb);var
T=sb.valueOf(this.minorTickStroke);wb.setStroke(T);var
B=this.hq(this.minorTickPlacement,this.minorTickLength);var
O=B[0];var
J=Mb+B[1];var
X=0;for(var
M=0;M<Fa.length;M++){var
cb=this.Gl(Fa,M);this.MU(wb,cb,J,O);X=Fa[M];}if(X<this.length){var
cb=this.Gl(Fa,Fa.length);this.MU(wb,cb,J,O);}}var
ga=this.m7(Mb);var
lb=this.getAxisTitle();if(lb!=null&&lb.getDisplay()!=jsx3.gui.Block.DISPLAYNONE){var
ka=ga[5];var
Ia=this.horizontal&&this.primary||!this.horizontal&&!this.primary?0:-1;if(this.horizontal){var
Gb=ka+Ia*lb.getPreferredHeight();lb.setDimensions(0,Gb,this.length,lb.getPreferredHeight());}else{var
L=ka+Ia*lb.getPreferredWidth();lb.setDimensions(L,0,lb.getPreferredWidth(),this.length);}lb.updateView();x.appendChild(lb.getCanvas());}if(this.showLabels){var
xb=this.se();var
ta=new
db.Group(0,0,Hb,vb);x.appendChild(ta);var
R=ga[0];var
ua=ga[1];var
Ia=ga[2];this._jsxMX=null;for(var
M=0;M<xb.length;M++){var
Oa=xb[M];var
Lb=null;if(this.horizontal){var
jb=M>0?(xb[M-1]+xb[M])/2:null;var
ba=M<xb.length-1?(xb[M+1]+xb[M])/2:null;if(ba==null&&jb!=null)ba=2*xb[M]-jb;else if(jb==null&&ba!=null)jb=2*xb[M]-ba;if(ba==null){jb=xb[M]-50;ba=xb[M]+50;}Lb=Math.round(ba-jb);}else Lb=this.getDisplayWidth()-this._H()-this.labelGap;if(this.horizontal){var
Wa=Math.round(Oa-Lb/2);var
Ja=ua+Ia*Math.round(k.o4/2);this.vU(ta,Wa,Ja,Wa+Lb,Ja,this.Yu(M));}else{var
Wa=Ia==1?ua:ua-Lb;this.vU(ta,Wa,Oa,Wa+Lb,Oa,this.Yu(M));}}}};q.vU=function(s,a,h,p,g,d){if(!(d&&d.toString().match(ub.t)))return;var
yb=new
db.TextLine(a,h,p,g,d);yb.setClassName(this.labelClass);yb.setExtraStyles(this.labelStyle);yb.setColor(this.labelColor);s.appendChild(yb);};q.Yu=function(p){var
Za=this.Xj(p);var
J=this.getLabelFunction();return J!=null?J.call(null,Za):Za!=null?Za.toString():ub.l;};q._H=function(){var
kb=this.tickPlacement==ub.b||this.tickPlacement==ub.c?this.tickLength:0;var
sa=this.minorTickPlacement==ub.b||this.minorTickPlacement==ub.c?this.minorTickLength:0;return Math.max(kb,sa);};q.Hs=function(d){if(d==null){d=this.getOpposingAxis();if(d==null)return 0;}if(d.mo())return d.getCoordinateFor(0);else if(this.primary)return this.horizontal?d.getLength():0;else return this.horizontal?0:d.getLength();};q.hq=function(l,i){var
ma=0;if(l==ub.c){ma=-1*i;i=i*2;}else{var
v=0;if(this.horizontal)v++;if(this.primary)v++;if(l==ub.a)v++;if(v%2==1)ma=-1*i;}return [i,ma];};q.m7=function(b){var
I=this.getOpposingAxis();if(b==null)b=this.Hs(I);var
ta=0;if(this.horizontal)ta=ta|1;if(this.primary)ta=ta|2;if(this.labelPlacement==ub.f)ta=ta|4;else if(this.labelPlacement==ub.g)ta=ta|8;var
wa=k.f0[ta];var
Za=0;var
H=0;if(this.tickPlacement==ub.b||this.tickPlacement==ub.c)Za=this.tickLength;if(this.tickPlacement==ub.a||this.tickPlacement==ub.c)H=this.tickLength;if(this.minorTickPlacement==ub.b||this.minorTickPlacement==ub.c)Za=Math.max(Za,this.minorTickLength);if(this.minorTickPlacement==ub.a||this.minorTickPlacement==ub.c)H=Math.max(H,this.minorTickLength);var
fa=null,S=null,Jb=null;switch(wa){case k.BX:case k.PJ:S=-1;fa=-this.labelGap;fa=fa-Math.max(0,Za-b);break;case k.mD:case k.YZ:S=1;fa=I.getLength()+this.labelGap;fa=fa+Math.max(0,Za+b-I.getLength());break;case k.Wp:case k.gA:S=-1;fa=b-this.labelGap-Za;break;case k.Mq:case k.F8:S=1;fa=b+this.labelGap+Za;break;default:ha.LOG.error(ub.u+wa);}if(this.showLabels){if(this.horizontal)Jb=fa+S*k.o4;else Jb=fa+S*this.getDisplayWidth();}else Jb=fa;if(this.horizontal&&this.primary||!this.horizontal&&!this.primary){Jb=Math.max(Jb,I.getLength());}else Jb=Math.min(Jb,0);return [wa,fa,S,Za,H,Jb];};q.Ol=function(){var
A=0,za=0;var
ja=this.getOpposingAxis();if(ja==null)return [0,0];var
eb=this.Hs(ja);var
ob=this.m7(eb);var
Nb=this.getAxisTitle();var
ra=ob[1];var
xa=ob[2];var
kb=ob[3];var
V=ob[4];if(this.showLabels)if(this.horizontal)ra=ra+xa*k.o4;else ra=ra+xa*this.getDisplayWidth();if(ra<0){A=-ra;}else if(ra>ja.getLength())za=ra-ja.getLength();if(V>this.length-eb)za=Math.max(za,V+this.length-eb);if(kb>-eb)A=Math.max(A,kb-eb);if(Nb!=null&&Nb.getDisplay()!=jsx3.gui.Block.DISPLAYNONE)if(this.horizontal)za=za+Nb.getPreferredHeight();else A=A+Nb.getPreferredWidth();return [A,za];};q.MU=function(l,o,c,s){if(this.horizontal){for(var
Eb=0;Eb<o.length;Eb++)l.addRelativeLine(o[Eb],c,0,s);}else for(var
Eb=0;Eb<o.length;Eb++)l.addRelativeLine(c,o[Eb],s,0);};q.se=function(){return this.Hf();};q.Gl=function(f,j){var
x=[];if(j==0){return [];}else if(j==f.length){return [];}else{var
t=f[j-1];var
kb=f[j];for(var
fb=1;fb<this.minorTickDivisions;fb++)x.push(Math.round(t+fb/this.minorTickDivisions*(kb-t)));}return x;};q.getAxisTitle=function(){return ha.ChartLabel?this.getFirstChildOfType(ha.ChartLabel):null;};q.getOpposingAxis=function(){var
F=this.getChart();if(F==null)return null;if(this.horizontal){if(this.primary){return F.getPrimaryYAxis();}else return F.getSecondaryYAxis();}else if(this.primary){return F.getPrimaryXAxis();}else return F.getSecondaryXAxis();};q.onSetChild=function(f){if((ha.ChartLabel&&f instanceof ha.ChartLabel)&&this.getAxisTitle()==null){f.setLabelRotation(this.horizontal?0:270);return true;}return false;};q.onSetParent=function(b){return ha.Chart&&b instanceof ha.Chart;};k.getVersion=function(){return ha.si;};});jsx3.require("jsx3.chart.ChartComponent","jsx3.chart.PointRenderer");jsx3.Class.defineClass("jsx3.chart.Legend",jsx3.chart.ChartComponent,null,function(n,q){var
ub={d:"",a:"10 10 10 4",h:"jsxid",c:"_b",f:"seriesId",g:"recordIndex",b:"4 4 0 4",e:"left"};var
L=jsx3.vector;var
Sa=jsx3.chart;n.DEFAULT_WIDTH=100;n.DEFAULT_HEIGHT=100;n.LE=1;n.nH=3;n.qx=2;n.SHOW_SERIES=1;n.SHOW_CATEGORIES=2;n.ZB=8;n.xJ=6;q.init=function(k){this.jsxsuper(k);this.boxHeight=10;this.lineHeight=22;this.labelClass=null;this.labelStyle=null;this.backgroundFill=null;this.backgroundStroke=null;this.preferredWidth=null;this.preferredHeight=null;this.setMargin(ub.a);this.setPadding(ub.b);};q.getBoxHeight=function(){return this.boxHeight;};q.setBoxHeight=function(h){this.boxHeight=h;};q.getLineHeight=function(){return this.lineHeight;};q.setLineHeight=function(h){this.lineHeight=h;};q.getLabelClass=function(){return this.labelClass;};q.setLabelClass=function(j){this.labelClass=j;};q.getLabelStyle=function(){return this.labelStyle;};q.setLabelStyle=function(f){this.labelStyle=f;};q.getBackgroundFill=function(){return this.backgroundFill;};q.setBackgroundFill=function(g){this.backgroundFill=g;};q.getBackgroundStroke=function(){return this.backgroundStroke;};q.setBackgroundStroke=function(k){this.backgroundStroke=k;};q.getPreferredWidth=function(){return this.preferredWidth!=null?this.preferredWidth:n.DEFAULT_WIDTH;};q.setPreferredWidth=function(m){this.preferredWidth=m;};q.getPreferredHeight=function(){return this.preferredHeight!=null?this.preferredHeight:n.DEFAULT_HEIGHT;};q.setPreferredHeight=function(h){this.preferredHeight=h;};q.updateView=function(){this.jsxsuper();var
Nb=this.getCanvas();this.tg();var
Ba=this.getChart();var
Da=Ba.getLegendEntryType();var
nb=0;if(Da==1){nb=Ba.Cl().length;}else if(Da==2){var
Xa=Ba.bh();if(Xa!=null)nb=Xa.length;}var
wa=this.getLegendTitle();var
Ha=wa!=null&&wa.getDisplay()!=jsx3.gui.Block.DISPLAYNONE?wa.getPreferredHeight()+n.ZB:0;var
Va=jsx3.html.BlockTag.po(this.getMargin());var
y=jsx3.html.BlockTag.po(this.getPadding());var
ka=this.getWidth()-Va[1]-Va[3];var
aa=Math.min(this.getHeight()-Va[0]-Va[2],Ha+this.lineHeight*nb+y[0]+y[2]);var
A=Va[3];var
U=Math.max(Va[0],Math.round((this.getHeight()-aa)/2));var
Ua=new
L.Group(A,U,ka,aa);Nb.appendChild(Ua);Ua.setZIndex(n.qx);if(this.backgroundFill||this.backgroundStroke)if(nb>0||wa!=null&&wa.getDisplay()!=jsx3.gui.Block.DISPLAYNONE){var
ya=new
L.Rectangle(A,U,ka,aa);ya.setZIndex(n.LE);Nb.appendChild(ya);var
ab=L.Fill.valueOf(this.backgroundFill);var
Lb=L.Stroke.valueOf(this.backgroundStroke);ya.setFill(ab);ya.setStroke(Lb);}var
ia=U+y[0];var
da=ka-y[1]-y[3];if(wa!=null&&wa.getDisplay()!=jsx3.gui.Block.DISPLAYNONE){wa.setDimensions(A+y[3],ia,da,wa.getPreferredHeight());wa.setZIndex(n.nH);wa.updateView();Nb.appendChild(wa.getCanvas());ia=ia+Ha;}ia=ia-U;var
O=A+y[3]+this.boxHeight+n.xJ;var
ra=da-this.boxHeight-n.xJ;if(Da==1&&nb>0){var
ob=Ba.Cl();for(var
Ib=0;Ib<ob.length;Ib++){var
ea=ob[Ib].getLegendRenderer();var
E=A+y[3];var
ab=ob[Ib].Rj();var
Lb=ob[Ib].ef(ab);var
Z=ea.render(E,ia,E+this.boxHeight,ia+this.boxHeight,ab,Lb);Z.setId(this.getId()+ub.c+Ib);Ua.appendChild(Z);var
hb=this.ZM(Ua,ob[Ib].getSeriesName(),this.labelClass,this.labelStyle,O,Math.round(ia+this.boxHeight/2),ra);ia=ia+this.lineHeight;this.tg(Z,ob[Ib],null);this.tg(hb,ob[Ib],null);}}else if(Da==2&&nb>0){var
Xa=Ba.bh();var
ea=Sa.PointRenderer.BOX;var
Lb=L.Stroke.valueOf(Ba.getSeriesStroke());for(var
Ib=0;Ib<Xa.length;Ib++){var
E=A+y[3];var
ab=Ba.vn(Xa[Ib],Ib);var
ja=Lb==null&&ab.Kk()?new
L.Stroke(ab.getColor()):Lb;var
Z=ea.render(E,ia,E+this.boxHeight,ia+this.boxHeight,ab,ja);Z.setId(this.getId()+ub.c+Ib);Ua.appendChild(Z);var
x=Ba.getCategoryField();var
Ra=x?Xa[Ib].getAttribute(x):ub.d;var
hb=this.ZM(Ua,Ra,this.labelClass,this.labelStyle,O,Math.round(ia+this.boxHeight/2),ra);ia=ia+this.lineHeight;this.tg(Z,null,Ib);this.tg(hb,null,Ib);}}};q.ZM=function(d,r,i,m,e,a,c){var
ob=new
L.TextLine(e,a,c,a,r);ob.setClassName(i);ob.setExtraStyles(m);if(!ob.getTextAlign())ob.setTextAlign(ub.e);d.appendChild(ob);return ob;};q.getLegendTitle=function(){return Sa.ChartLabel?this.getFirstChildOfType(Sa.ChartLabel):null;};q.onSetChild=function(d){return (Sa.ChartLabel&&d instanceof Sa.ChartLabel)&&this.getLegendTitle()==null;};q.onSetParent=function(k){return Sa.Chart&&k instanceof Sa.Chart;};n.getVersion=function(){return Sa.si;};q.tg=function(d,h,f){if(d==null)d=this.getCanvas();if(h!=null)d.setProperty(ub.f,h.getId());if(f!=null)d.setProperty(ub.g,f);this.jsxsuper(d);};q.Tg=function(f,g){var
Z=g.getAttribute(ub.f);var
T=g.getAttribute(ub.g);this.doEvent(jsx3.gui.Interactive.SELECT,this.v4(f,Z,T));};q.Oh=function(o,m){var
S=m.getAttribute(ub.f);var
Bb=m.getAttribute(ub.g);this.doEvent(jsx3.gui.Interactive.EXECUTE,this.v4(o,S,Bb));};q.doSpyOver=function(b,k){var
xb=k.getAttribute(ub.f);var
kb=k.getAttribute(ub.g);this.jsxsupermix(b,k,this.v4(b,xb,kb));};q.Df=function(i,d){var
t=d.getAttribute(ub.f);var
pb=d.getAttribute(ub.g);var
ya;if(i.rightButton()&&(ya=this.getMenu())!=null){var
Aa=this.getServer().getJSXByName(ya);if(Aa!=null){var
xb=this.v4(i,t,pb);xb.objMENU=Aa;var
Y=this.doEvent(jsx3.gui.Interactive.MENU,xb);if(Y!==false){if(Y instanceof Object&&Y.objMENU instanceof jsx3.gui.Menu)Aa=Y.objMENU;Aa.showContextMenu(i,this);}}}};q.v4=function(m,l,o){var
D={objEVENT:m};D.objSERIES=l!=null?this.getServer().getJSXById(l):null;if(o!=null){D.intINDEX=o;var
Xa=this.getChart().bh()[o];D.strRECORDID=Xa?Xa.getAttribute(ub.h):null;}else D.intINDEX=D.strRECORDID=null;return D;};});jsx3.require("jsx3.chart.ChartComponent");jsx3.Class.defineClass("jsx3.chart.Series",jsx3.chart.ChartComponent,null,function(m,p){var
ub={d:"recordIndex",a:"tooltipFunction",c:"strRecordId",b:"colorFunction"};var
da=jsx3.gui.Interactive;var
Ab=jsx3.vector;var
T=jsx3.chart;p.init=function(r,n){this.jsxsuper(r);this.seriesName=n;this.usePrimaryX=jsx3.Boolean.TRUE;this.usePrimaryY=jsx3.Boolean.TRUE;this.stroke=null;this.fill=null;this.fillGradient=null;this.tooltipFunction=null;};p.getSeriesName=function(){return this.seriesName;};p.setSeriesName=function(b){this.seriesName=b;};p.getUsePrimaryX=function(){return this.usePrimaryX;};p.setUsePrimaryX=function(l){this.usePrimaryX=l;};p.getUsePrimaryY=function(){return this.usePrimaryY;};p.setUsePrimaryY=function(d){this.usePrimaryY=d;};p.setTooltipFunction=function(b){T.sj(this,ub.a,b);};p.getTooltipFunction=function(){return T.vc(this,ub.a);};p.getIndex=function(){var
na=this.getChart();return na!=null?na.getSeriesIndex(this):-1;};p.getStroke=function(){return this.stroke;};p.setStroke=function(r){this.stroke=r;};p.getFill=function(){return this.fill;};p.setFill=function(g){this.fill=g;};p.getFillGradient=function(){return this.fillGradient;};p.setFillGradient=function(q){this.fillGradient=q;};p.getXAxis=function(){var
hb=this.getChart();if(hb!=null)return this.usePrimaryX?hb.getPrimaryXAxis():hb.getSecondaryXAxis();return null;};p.getYAxis=function(){var
cb=this.getChart();if(cb!=null)return this.usePrimaryY?cb.getPrimaryYAxis():cb.getSecondaryYAxis();return null;};p.Zf=function(){var
sa=Math.max(this.getIndex(),0)%T.Chart.DEFAULT_FILLS.length;return T.Chart.DEFAULT_FILLS[sa];};p.yi=function(){var
ya=Math.max(this.getIndex(),0)%T.Chart.DEFAULT_FILLS.length;if(T.Chart.DEFAULT_STROKES[ya]==null){var
Cb=this.Zf();T.Chart.DEFAULT_STROKES[ya]=new
Ab.Stroke(Cb.getColor(),1,Cb.getAlpha());}return T.Chart.DEFAULT_STROKES[ya];};p.Rj=function(){var
_=this.fill?Ab.Fill.valueOf(this.fill):this.Zf();if(_!=null)_=T.addGradient(_,this.fillGradient);return _;};p.ef=function(h){if(this.stroke){return Ab.Stroke.valueOf(this.stroke);}else if(this.getColorFunction()!=null){return null;}else if(h!=null&&h.Kk()){return new
Ab.Stroke(h.getColor());}else if(!this.fill)return this.yi(h);else return null;};p.getColorFunction=function(){return T.vc(this,ub.b);};p.setColorFunction=function(k){T.sj(this,ub.b,k);};p.getLegendRenderer=function(){return T.PointRenderer.BOX;};p.getLabel=function(){return T.ChartLabel?this.getFirstChildOfType(T.ChartLabel):null;};p.onSetChild=function(f){return (T.ChartLabel&&f instanceof T.ChartLabel)&&this.getLabel()==null;};p.onSetParent=function(c){return T.Chart&&c instanceof T.Chart;};p.tg=function(k,r,h){if(k==null)k=this.getCanvas();if(h!=null)k.setProperty(ub.c,h);if(r!=null)k.setProperty(ub.d,r);this.jsxsuper(k);};p.Tg=function(l,a){var
Sa=a.getAttribute(ub.d);var
u=a.getAttribute(ub.c);this.doEvent(jsx3.gui.Interactive.SELECT,{objEVENT:l,intINDEX:Sa,strRECORDID:u});};p.Oh=function(e,h){var
tb=h.getAttribute(ub.d);var
E=h.getAttribute(ub.c);this.doEvent(jsx3.gui.Interactive.EXECUTE,{objEVENT:e,intINDEX:tb,strRECORDID:E});};p.doSpyOver=function(j,c){var
y=c.getAttribute(ub.d);var
V=c.getAttribute(ub.c);this.jsxsupermix(j,c,{objEVENT:j,intINDEX:y,strRECORDID:V});};p.Df=function(h,e){var
ka=e.getAttribute(ub.d);var
sb=e.getAttribute(ub.c);var
na;if(h.rightButton()&&(na=this.getMenu())!=null){var
Wa=this.getServer().getJSXByName(na);if(Wa!=null){var
U={objEVENT:h,objMENU:Wa,intINDEX:ka,strRECORDID:sb};var
Ea=this.doEvent(da.MENU,U);if(Ea!==false){if(Ea instanceof Object&&Ea.objMENU instanceof jsx3.gui.Menu)Wa=Ea.objMENU;Wa.showContextMenu(h,this,ka);}}}};m.getVersion=function(){return T.si;};});jsx3.require("jsx3.vector.Block","jsx3.xml.Cacheable","jsx3.chart.Series");jsx3.Class.defineClass("jsx3.chart.Chart",jsx3.vector.Block,[jsx3.xml.Cacheable,jsx3.xml.CDF],function(d,f){var
ub={o:" because it isn't of valid type for ",d:"titlePlacement",k:" because chart already has a legend",c:"#999999",h:"series",g:"_jsxdp",l:"can't add title ",b:"right",i:"jsx3.chart.Legend",m:" because chart already has a title",a:"top",f:"/data/record",j:"can't add legend ",n:"can't add series ",e:"legendPlacement"};var
_=jsx3.vector;var
lb=_.Fill;var
M=jsx3.chart;d.Yp=1;d.N3=2;d.ZINDEX_DATA=10;d.YF=990;d.hC=1000;d.Dy=20;d.DEFAULT_FILLS=[new
lb(3381708,1),new
lb(16763904,1),new
lb(10079334,1),new
lb(13408563,1),new
lb(13421772,1),new
lb(13382502,1),new
lb(16751103,1),new
lb(6710886,1)];d.DEFAULT_STROKES=[];d.PART_LEGEND=1<<0;d.RQ={top:1,right:1,bottom:1,left:1};f.init=function(a,e,h,o,i){this.jsxsuper(a);this.setDimensions(e,h,o,i);this.titlePlacement=ub.a;this.legendPlacement=ub.b;this.dataPadding=10;this.borderColor=ub.c;this.borderWidth=1;this.borderAlpha=1;this.alpha=1;this.setRelativePosition(jsx3.gui.Block.RELATIVE);};f.getTitlePlacement=function(){return this.titlePlacement;};f.setTitlePlacement=function(r){if(d.RQ[r]){this.titlePlacement=r;}else throw new
jsx3.IllegalArgumentException(ub.d,r);};f.getLegendPlacement=function(){return this.legendPlacement;};f.setLegendPlacement=function(l){if(d.RQ[l]){this.legendPlacement=l;}else throw new
jsx3.IllegalArgumentException(ub.e,l);};f.getDataPadding=function(){return this.dataPadding;};f.setDataPadding=function(a){this.dataPadding=a;};f.getBorderColor=function(){return this.borderColor;};f.setBorderColor=function(l){this.borderColor=l;};f.getBorderWidth=function(){return this.borderWidth;};f.setBorderWidth=function(h){this.borderWidth=h;};f.getBorderAlpha=function(){return this.borderAlpha;};f.setBorderAlpha=function(m){this.borderAlpha=m;};f.getAlpha=function(){return this.alpha;};f.setAlpha=function(o){this.alpha=o!=null?_.ah(o):null;};f.wf=function(){return this._jsxdc;};f.bh=function(){return this._jsxdp;};f.Pw=function(e){if(e!=null){this._jsxdp=e.selectNodes(ub.f).toArray();}else delete this[ub.g];};f.getSeries=function(){return this.getDescendantsOfType(M.Series);};f.Cl=function(){return this.findDescendants(function(s){return s instanceof M.Series&&s.getDisplay()!=jsx3.gui.Block.DISPLAYNONE;},false,true,false,false);};f.getSeriesIndex=function(o){var
Ca=this.getSeries();for(var
Cb=0;Cb<Ca.length;Cb++)if(o==Ca[Cb])return Cb;return -1;};f.getChartTitle=function(){return M.ChartLabel?this.getFirstChildOfType(M.ChartLabel):null;};f.getLegend=function(){return M.Legend?this.getFirstChildOfType(M.Legend):null;};f.kg=function(){return false;};f.rl=jsx3.Method.newAbstract(ub.h);f.getLegendEntryType=function(){jsx3.require(ub.i);return 1;};f.bg=function(q,h){var
bb=this.bh();if(bb==null)return null;var
Ka=new
Array(bb.length);for(var
ga=0;ga<bb.length;ga++){Ka[ga]=0;for(var
fb=0;fb<q.length;fb++){var
oa=q[fb][h](bb[ga]);if(oa!=null)Ka[ga]+=Math.abs(oa);}}return Ka;};f.ak=function(o,j,h){var
P=this.bh();if(P==null)return null;var
E=new
Array(o.length);for(var
za=0;za<o.length;za++){E[za]=0;for(var
hb=0;hb<P.length;hb++){var
ob=o[za][j](P[hb]);if(ob!=null&&(ob>=0||!h))E[za]+=Math.abs(ob);}}return E;};f.createCanvas=function(){return new
jsx3.vector.Canvas();};f.createVector=function(){var
ua=this.jsxsuper();var
pb=ua.getLeft();var
sb=ua.getTop();var
Ma=ua.getWidth();var
zb=ua.getHeight();var
W=this.getXML();this.Pw(W);var
Mb=new
_.Rectangle(0,0,Ma,zb);ua.appendChild(Mb);Mb.setZIndex(d.Yp);M.Ng(this,Mb);M.Fd(this,Mb);var
Ab=jsx3.html.BlockTag.po(this.getPadding());var
Pa=this.borderWidth!=null?this.borderWidth:1;Ma=Ma-Ab[1]-Ab[3]-2*Pa;zb=zb-Ab[0]-Ab[2]-2*Pa;var
ra=new
_.Group(Ab[3]+Pa,Ab[0]+Pa,Ma,zb);ua.appendChild(ra);ra.setZIndex(d.N3);var
L=this.getChartTitle();if(L!=null&&L.getDisplay()!=jsx3.gui.Block.DISPLAYNONE){var
vb=M.splitBox(0,0,Ma,zb,this.titlePlacement,L.getPreferredWidth(),L.getPreferredHeight());L.setDimensions(vb[0][0],vb[0][1],vb[0][2],vb[0][3]);L.setZIndex(d.hC);L.updateView();ra.appendChild(L.getCanvas());sb=vb[1][0];pb=vb[1][1];Ma=vb[1][2];zb=vb[1][3];}else{sb=0;pb=0;}var
G=new
_.Group();this._jsxdc=G;ra.appendChild(G);var
U=this.getLegend();if(U!=null&&U.getDisplay()!=jsx3.gui.Block.DISPLAYNONE){var
vb=M.splitBox(sb,pb,Ma,zb,this.legendPlacement,U.getPreferredWidth(),U.getPreferredHeight());U.setDimensions(vb[0][0],vb[0][1],vb[0][2],vb[0][3]);U.setZIndex(d.YF);U.updateView();ra.appendChild(U.getCanvas());G.setDimensions(vb[1][0],vb[1][1],vb[1][2],vb[1][3]);}else G.setDimensions(sb,pb,Ma,zb);var
mb=jsx3.html.BlockTag.po(this.dataPadding);var
Ra=G.getDimensions();G.setDimensions(Ra[0]+mb[3],Ra[1]+mb[0],Ra[2]-mb[1]-mb[3],Ra[3]-mb[0]-mb[2]);M.tg(this);var
u=this.Cl();for(var
za=0;za<u.length;za++){var
Ba=this.kg()?u.length-za:za;u[za].setZIndex(d.Dy+Ba);}return ua;};f.updateVector=function(q){return false;};f.repaintParts=function(a){if(a&d.PART_LEGEND){var
xb=this.getLegend();if(xb!=null)xb.repaint();}};f.onSetChild=function(n){if(M.Legend&&n instanceof M.Legend){if(this.getLegend()!=null){M.LOG.info(ub.j+n+ub.k);return false;}}else if(M.ChartLabel&&n instanceof M.ChartLabel){if(this.getChartTitle()!=null){M.LOG.info(ub.l+n+ub.m);return false;}}else if(M.Series&&n instanceof M.Series){if(!this.rl(n)){M.LOG.info(ub.n+n+ub.o+this);return false;}}else return false;return true;};f.redrawRecord=function(){this.repaint();};f.onXmlBinding=function(j){this.jsxsupermix(j);this.repaint();};d.getVersion=function(){return M.si;};});jsx3.require("jsx3.chart.Chart","jsx3.chart.Axis","jsx3.chart.GridLines");jsx3.Class.defineClass("jsx3.chart.CartesianChart",jsx3.chart.Chart,null,function(g,b){var
ub={a:"series",b:"no data provider for chart: "};var
va=jsx3.chart;var
Za=va.Chart;var
Ta=va.GridLines;var
qa=va.Axis;g.oz=function(k){return k instanceof qa&&k.ui()&&k.getHorizontal();};g.a4=function(e){return e instanceof qa&&!e.ui()&&e.getHorizontal();};g.hR=function(l){return l instanceof qa&&l.ui()&&!l.getHorizontal();};g.Jz=function(q){return q instanceof qa&&!q.ui()&&!q.getHorizontal();};g.Rz=Za.ZINDEX_DATA+1;g.pU=Za.ZINDEX_DATA+90;g.h0=Za.ZINDEX_DATA+100;g.PART_GRIDLINES=1<<8;b.init=function(l,p,s,d,a){this.jsxsuper(l,p,s,d,a);};b.getGridLines=function(){return Ta?this.getDescendantsOfType(Ta):[];};b.pe=function(h,q){var
v=[];var
gb=q?this.Cl():this.getSeries();for(var
F=0;F<gb.length;F++)if(h.getHorizontal()&&h.ui()==gb[F].getUsePrimaryX()||!h.getHorizontal()&&h.ui()==gb[F].getUsePrimaryY())v.push(gb[F]);return v;};b.getPrimaryXAxis=function(){return this.findDescendants(g.oz,false,false,true);};b.getSecondaryXAxis=function(){return this.findDescendants(g.a4,false,false,true);};b.getPrimaryYAxis=function(){return this.findDescendants(g.hR,false,false,true);};b.getSecondaryYAxis=function(){return this.findDescendants(g.Jz,false,false,true);};b.getRangeForAxis=function(c){var
Ka=this.pe(c,true);return c.getHorizontal()?this.getXRange(Ka):this.getYRange(Ka);};b.getXRange=jsx3.Method.newAbstract(ub.a);b.getYRange=jsx3.Method.newAbstract(ub.a);b.getRangeForField=function(a,r){var
Hb=this.bh();if(Hb==null){va.LOG.debug(ub.b+this);return null;}var
db=Number.NEGATIVE_INFINITY;var
wa=Number.POSITIVE_INFINITY;for(var
aa=0;aa<Hb.length;aa++){var
u=Hb[aa];for(var
Ya=0;Ya<a.length;Ya++){var
w=a[Ya];var
Oa=w[r](u);if(Oa!=null){wa=Math.min(wa,Oa);db=Math.max(db,Oa);}}}if(db==Number.NEGATIVE_INFINITY||wa==Number.POSITIVE_INFINITY)return null;return [wa,db];};b.getStackedRangeForField=function(s,f){var
V=this.bh();if(V==null){va.LOG.debug(ub.b+this);return null;}var
E=Number.NEGATIVE_INFINITY;var
D=Number.POSITIVE_INFINITY;for(var
Z=0;Z<V.length;Z++){var
Eb=V[Z];var
ab=0,La=0;for(var
ra=0;ra<s.length;ra++){var
ya=s[ra];var
B=ya[f](Eb);if(B==null)continue;if(B>=0)ab=ab+B;else La=La+B;}D=Math.min(D,La);E=Math.max(E,ab);}if(E==Number.NEGATIVE_INFINITY||D==Number.POSITIVE_INFINITY)return null;return [D,E];};b.getStacked100RangeForField=function(q,h){var
za=this.bh();if(za==null){va.LOG.debug(ub.b+this);return null;}var
Pa=Number.NEGATIVE_INFINITY;var
Ba=Number.POSITIVE_INFINITY;for(var
jb=0;jb<za.length;jb++){var
ta=za[jb];var
xa=0,ha=0,Ua=0;for(var
B=0;B<q.length;B++){var
sa=q[B];var
La=sa[h](ta);if(La==null)continue;Ua=Ua+Math.abs(La);if(La>=0)xa=xa+La;else ha=ha+La;}var
ab=Ua==0?0:100*ha/Ua;var
yb=Ua==0?0:100*xa/Ua;Ba=Math.min(Ba,ab);Pa=Math.max(Pa,yb);}if(Pa==Number.NEGATIVE_INFINITY||Ba==Number.POSITIVE_INFINITY)return null;return [Ba,Pa];};b.getCombinedRange=function(h){var
eb=Number.NEGATIVE_INFINITY;var
ob=Number.POSITIVE_INFINITY;for(var
Ja=0;Ja<h.length;Ja++)if(h[Ja]!=null){ob=Math.min(ob,h[Ja][0]);eb=Math.max(eb,h[Ja][1]);}if(eb==Number.NEGATIVE_INFINITY||ob==Number.POSITIVE_INFINITY)return null;return [ob,eb];};b.createVector=function(){this.jsxsuper();var
Nb=this.wf();var
X=this.getPrimaryXAxis();var
Ya=this.getPrimaryYAxis();var
V=this.getSecondaryXAxis();var
F=this.getSecondaryYAxis();var
Na=Nb.getWidth();var
Oa=Nb.getHeight();var
S=Nb.getPaddingDimensions();var
D=null;for(var
ja=1;ja<=2;ja++){var
Mb=null;if(ja==1){Mb=[V!=null?V.getDisplayWidth():0,F!=null?F.getDisplayWidth():0,X!=null?X.getDisplayWidth():0,Ya!=null?Ya.getDisplayWidth():0];}else Mb=this.ZS(V,F,X,Ya);D=[S[3]+Mb[3],S[0]+Mb[0],Na-(S[3]+Mb[3]+S[1]+Mb[1]),Oa-(S[0]+Mb[0]+S[2]+Mb[2])];this.bR(X,D[2]);this.bR(Ya,D[3]);this.bR(V,D[2]);this.bR(F,D[3]);}var
ba=this.getGridLines();for(var
ja=0;ja<ba.length;ja++){var
Hb=ba[ja];if(Hb.getDisplay()==jsx3.gui.Block.DISPLAYNONE)continue;Hb.setDimensions(D);Hb.setZIndex(Hb.getInForeground()?g.pU:g.Rz);Hb.updateView();Nb.appendChild(Hb.getCanvas());}this.Y2(X,D[0],D[1]);this.Y2(Ya,D[0],D[1]);this.Y2(V,D[0],D[1]);this.Y2(F,D[0],D[1]);var
E=this.Cl();for(var
ja=0;ja<E.length;ja++)E[ja].setDimensions(D);};b.bR=function(l,k){if(l!=null){l.setLength(k);l.fl();}};b.Y2=function(q,i,j){var
kb=this.wf();if(q!=null){q.setDimensions(i,j,kb.getWidth(),kb.getHeight());q.setZIndex(g.h0);q.updateView();kb.appendChild(q.getCanvas());}};b.ZS=function(e,q,k,r){var
da=0,bb=0,ca=0,za=0;if(e!=null){var
oa=e.Ol();bb=oa[1];za=oa[0];}if(q!=null){var
oa=q.Ol();ca=ca+oa[0];da=da+oa[1];}if(k!=null){var
oa=k.Ol();za=Math.max(za,oa[1]);bb=Math.max(bb,oa[0]);}if(r!=null){var
oa=r.Ol();da=Math.max(da,oa[0]);ca=Math.max(ca,oa[1]);}return [bb,ca,za,da];};b.repaintParts=function(m){if(m&g.PART_GRIDLINES){var
Ja=this.getGridLines();for(var
pa=0;pa<Ja.length;pa++){var
S=Ja[pa];if(S.getDisplay()==jsx3.gui.Block.DISPLAYNONE)continue;S.setZIndex(S.getInForeground()?g.pU:g.Rz);S.repaint();}}this.jsxsuper(m);};b.onSetChild=function(a){if(Ta&&a instanceof Ta){return true;}else if(qa&&a instanceof qa){return true;}else return this.jsxsuper(a);};g.getVersion=function(){return va.si;};});