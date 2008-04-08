/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
Array.prototype.indexOf=function(j,g){if(g==null)g=0;for(var
Ka=g;Ka<this.length;Ka++)if(this[Ka]==j)return Ka;return -1;};Array.prototype.lastIndexOf=function(f,n){if(n==null)n=this.length-1;for(var
rb=n;rb>=0;rb--)if(this[rb]==f)return rb;return -1;};Array.prototype.contains=function(o){return this.indexOf(o)>=0;};Array.prototype.remove=function(l){var
ba=this.indexOf(l);if(ba>=0)return this.splice(ba,1)[0];return null;};Array.prototype.clone=function(){return this.concat();};Array.prototype.pushAll=function(s){if(typeof s=="array"&&s.length>0){for(var
Oa=0;Oa<s.length;Oa++)this.push(s[Oa]);}else if(typeof s=="object"&&typeof s.length=="number"&&s.length>0)for(var
Oa=0;Oa<s.length;Oa++)this.push(s[Oa]);};Array.prototype.contentsEqual=function(l){if(l==null)return false;if(this.length!=l.length)return false;for(var
U=0;U<l.length;U++)if(this[U]!=l[U])return false;return true;};Array.prototype.filter=function(r){var
Sa=[];for(var
T=0;T<this.length;T++)if(r(this[T]))Sa.push(this[T]);return Sa;};Array.prototype.map=function(i,f,g){var
Mb=null;if(f){if(g){Mb={};for(var
db=0;db<this.length;db++){var
ma=i(this[db]);for(var
xa=0;xa<ma.length;xa=xa+2)Mb[ma[db]]=ma[db+1];}}else{Mb=[];for(var
db=0;db<this.length;db++){var
vb=i(this[db]);if(vb instanceof Array)Mb.pushAll(vb);else Mb.push(vb);}}}else if(g){Mb={};for(var
db=0;db<this.length;db++){var
W=i(this[db]);Mb[W[0]]=W[1];}}else{Mb=new
Array(this.length);for(var
db=0;db<this.length;db++)Mb[db]=i(this[db]);}return Mb;};Math.modpos=function(k,f){return jsx3.util.numMod(k,f);};Math.isNaN=function(q){return jsx3.util.numIsNaN(q);};Number.prototype.roundTo=function(n){return jsx3.util.numRound(this,n);};Number.prototype.zeroPad=function(e){var
ra=""+this;while(ra.length<e)ra="0"+ra;return ra;};Date.prototype.getLastDateOfMonth=function(){var
Aa=this.getMonth();return Date.AH[Aa]||((new
Date(this.getYear(),Aa,29)).getMonth()==Aa?29:28);};Date.prototype.equals=function(g){return g!=null&&g instanceof Date&&g.valueOf()==this.valueOf();};Date.prototype.compareTo=function(d){var
Ma=this.valueOf();var
ha=d.valueOf();return Ma==ha?0:Ma>ha?1:-1;};Date.AH=[31,null,31,30,31,30,31,31,30,31,30,31];String.prototype.trim=function(){return jsx3.util.strTrim(this);};String.prototype.doReplace=function(r,o){var
L=new
RegExp(r,["g"]);return this.replace(L,o);};String.prototype.escapeHTML=function(){return jsx3.util.strEscapeHTML(this);};String.prototype.doTruncate=function(r){return jsx3.util.strTruncate(this,r,"...",1);};String.prototype.toAbsolute=function(){var
Jb;if(this.substring(0,1)=="/"||this.substring(0,7).toUpperCase()=="HTTP://"||this.substring(0,8).toUpperCase()=="HTTPS://"){Jb=this.toString();}else if(this.substring(0,4)=="JSX/"){Jb=jsx3.getEnv("jsxabspath")+this;}else Jb=jsx3.getEnv("jsxhomepath")+this;return Jb;};String.prototype.urlTo=function(e){var
oa=null;var
Ma=this.lastIndexOf("/");if(Ma==this.length-1)oa=this+e;else if(Ma<0)oa=e;else oa=this.substring(0,Ma+1)+e;oa=oa.replace(/\\/g,"/");var
rb=oa.split("/");for(var
U=0;U<rb.length;U++){var
hb=rb[U];if(hb=="."){rb.splice(U--
,1);}else if(hb=="..")if(U>0&&rb[U-1]!=".."){rb.splice(U--
,1);rb.splice(U--
,1);}}return rb.join("/");};String.prototype.endsWith=function(r){return jsx3.util.strEndsWith(this,r);};String.prototype.constrainLength=function(p,e){return jsx3.util.strTruncate(this,p,e,0.6666666666666666);};String.prototype.toBase64=function(){return jsx3.util.strEncodeBase64(this);};String.prototype.fromBase64=function(){return jsx3.util.strDecodeBase64(this);};if(jsx3.lang==null)jsx3.lang={};jsx3.lang.STACK_MAX=50;jsx3.lang.getCaller=function(r){var
Mb=(r!=null?r:0)+1;for(var
Gb=arguments.callee;Gb!=null;Gb=Gb.caller){if(--Mb>=0)continue;return Gb.caller;}return null;};jsx3.lang.getStack=function(j){var
Ua=[];var
Xa=(j!=null?j:0)+1;for(var
kb=arguments.callee;kb!=null&&Ua.length<jsx3.lang.STACK_MAX;kb=kb.caller){if(--Xa>=0)continue;Ua[Ua.length]=kb.caller;}return Ua;};if(window["jsx3"]==null)window["jsx3"]={};if(jsx3.lang==null)jsx3.lang={};jsx3.lang.Uj=new
window.Object();jsx3.lang.Jl=new
window.Object();jsx3.lang.qg=function(){var
ub={a:"obj.no_inst"};return function(){if(arguments[0]!=jsx3.lang.Uj){var
Bb=this.getClass?this.getClass():null;throw new
jsx3.Exception(jsx3._msg(ub.a,Bb||this));}};};jsx3.lang.jm=function(){return function(){if(arguments[0]!==jsx3.lang.Jl)this.init.apply(this,arguments);};};jsx3.lang.Object=function(){this.init();};window._jsxDX=function(n,e){var
ub={o:/jsx3/g,d:"@",k:"obj.supmx_none",w:"}\r\nelse",r:"",c:"objClass",h:"undefined",p:"jsx3.gui",g:"obj.super_none",l:"obj.mix_funct",q:"SUPERS",b:"string",v:/\}\s*else/g,i:"obj.supmx_funct",m:"obj.mix_static",a:"function",u:"The JS debugger is not defined. It is probably not supported on this platform.",f:"obj.super_static",t:"jsx3.ide.Debugger",j:"obj.supmx_static",n:"INTERFACES",s:".",e:"obj.super_funct"};e.init=function(){};e.getClass=function(){return this.__jsxclass__.jsxclass;};e.equals=function(s){return this===s;};e.clone=function(){return this.getClass().bless(this);};e.instanceOf=function(m){if(m instanceof jsx3.lang.Class){return m.isInstance(this);}else if(typeof m==ub.a&&m.prototype!=null){return this instanceof m||m.jsxclass!=null&&m.jsxclass.isInstance(this);}else if(typeof m==ub.b){m=jsx3.lang.Class.forName(m);if(m!=null)return m.isInstance(this);}throw new
jsx3.IllegalArgumentException(ub.c,m);};e.toString=function(){return ub.d+this.getClass().getName();};e.jsxsuper=function(d){var
kb=jsx3.Exception;var
H=jsx3.lang.getCaller();var
w=H!=null?H.jsxmethod:null;if(w==null||!(w instanceof jsx3.lang.Method))throw new
kb(jsx3._msg(ub.e,H));if(w.isStatic())throw new
kb(jsx3._msg(ub.f,w));var
fb=w.getDeclaringClass();var
nb=fb.yf(w);if(nb==null)throw new
kb(jsx3._msg(ub.g,w));var
Ha=nb.apply(this,arguments);if(typeof Ha!=ub.h)return Ha;};e.jsxsupermix=function(p){var
pb=jsx3.Exception;var
sa=jsx3.lang.getCaller();var
yb=sa!=null?sa.jsxmethod:null;if(yb==null||!(yb instanceof jsx3.lang.Method))throw new
pb(jsx3._msg(ub.i,sa));if(yb.isStatic())throw new
pb(jsx3._msg(ub.j,yb));var
X=yb.getDeclaringClass();var
kb=X.Cn(yb);if(kb==null)throw new
pb(jsx3._msg(ub.k,yb));var
Pa=kb.apply(this,arguments);if(typeof Pa!=ub.h)return Pa;};e.jsxmix=function(o){var
N=jsx3.Exception;var
pa=jsx3.lang.getCaller();var
ib=pa!=null?pa.jsxmethod:null;if(ib==null||!(ib instanceof jsx3.lang.Method))throw new
N(jsx3._msg(ub.l,pa));if(ib.isStatic())throw new
N(jsx3._msg(ub.m,ib));pa.jsxmethod.gc(this,arguments);};e.isInstanceOf=function(d,q,h){if(this.getClass()&&(typeof d!=ub.b||jsx3.lang.Class.forName(d)!=null))return this.instanceOf(d);var
ka=jsx3.getClass(this.getInstanceOf());if(ka==null)ka=this.constructor;if(typeof ka==ub.a){var
ob=ka[q?q:ub.n];var
Ga=ob?ob[d]:null;if(Ga==1){return true;}else if(h){return false;}else return this.isInstanceOf(d.replace(ub.o,ub.p),q,true);}return false;};e.isSubclassOf=function(o){return this.isInstanceOf(o,ub.q);};e.getInstanceOf=function(){if(this.getClass())return this.getClass().getName();return this.jsxinstanceof?this.jsxinstanceof:this.constructor.className;};e.setInstanceOf=function(q){this.jsxinstanceof=q;return this;};e.getInstanceOfPackage=function(){if(this.getClass())return this.getClass().getPackageName();var
pb=this.getInstanceOf();if(pb==null)return ub.r;var
Y=pb.lastIndexOf(ub.s);if(Y>=0)return pb.substring(0,Y);else return ub.r;};e.getInstanceOfClass=function(){if(this.getClass()){var
P=this.getClass().getName();return P.substring(P.lastIndexOf(ub.s)+1);}var
xa=this.getInstanceOf();if(xa==null)return ub.r;var
ab=xa.lastIndexOf(ub.s);if(ab>=0)return xa.substring(ab+1);else return xa;};e.debug=function(){jsx3.require(ub.t);if(!jsx3.ide.doDebug)throw new
jsx3.Exception(ub.u);jsx3.ide.DEBUG_ARGS=arguments.callee.caller.arguments;var
v=jsx3.ide.createExpressionObject(arguments.callee.caller.toString().replace(ub.v,ub.w));var
J=jsx3._l(this.getInstanceOf());J.jsxMethod=jsx3.ide.doDebug;return this.jsxMethod(v);};e.eval=function(d,l){return jsx3.eval.call(this,d,l);};};window._jsxDX(jsx3.lang.Object,jsx3.lang.Object.prototype);window._jsxDX=null;jsx3.lang.Object.prototype.__noSuchMethod__=function(r,c){throw new
jsx3.Exception(jsx3._msg("class.nsm",this.getClass().getName()+"#"+r+"()"));};window.inheritance=jsx3.lang.Object;if(window["jsx3"]==null)window["jsx3"]={};if(jsx3.lang==null)jsx3.lang={};jsx3.lang.Method=jsx3.lang.qg();jsx3.lang.Method.prototype=new
jsx3.lang.Object();jsx3.lang.Method.prototype.constructor=jsx3.lang.Method;window._jsxDX=function(d,f){var
ub={o:".apply(this.",d:".",k:'var method = arguments.callee.jsxmethod;if (method instanceof jsx3.lang.Method) {  throw new jsx3.Exception("method " + method.getName() + " in class " + method.getDeclaringClass() +    " is abstract and may not be invoked");} else {  throw new jsx3.Exception("invoked abstract method improperly initialized");}',c:"method.call",h:"]",p:", arguments);",g:"paramNames[",l:"new Function(",b:/\s*,\s*/,i:"'",m:"');",a:/^\s*function(\s+\w+)?\s*\(\s*([^\)]*?)\s*\)/,f:/^[a-zA-Z_]\w*$/,j:"', ",n:"return this.",e:""};d.C2=ub.a;f.dB=function(){if(d.C2.exec(this.getFunction().toString())){var
Z=RegExp.$2;this.cQ=Z?Z.split(ub.b):[];}else this.cQ=[];};f.getName=function(){return this.zo;};f.getArity=function(){if(this.cQ==null)this.dB();return this.cQ.length;};f.getParameterNames=function(){if(this.cQ==null)this.dB();return this.cQ.concat();};f.getParameterName=function(h){if(this.cQ==null)this.dB();return this.cQ[h];};f.getDeclaringClass=function(){return this.no;};f.isPackageMethod=function(){return this.no instanceof jsx3.lang.Package;};f.isStatic=function(){return this.mg;};f.isAbstract=function(){return this.co;};f.getFunction=function(){if(this._function!=null)return this._function;if(this.isPackageMethod()){return this.no.getNamespace()[this.zo];}else if(this.mg){return this.no.getConstructor()[this.zo];}else return this.no.getConstructor().prototype[this.zo];};f.apply=function(j,i){return this.getFunction().apply(j,i);};f.call=function(n){var
fa=arguments;if(fa.length>11)throw new
jsx3.Exception(jsx3._msg(ub.c,+fa.length));return this.getFunction().call(fa[0],fa[1],fa[2],fa[3],fa[4],fa[5],fa[6],fa[7],fa[8],fa[9],fa[10]);};f.Ob=function(i,r){if(!this.LT)this.LT=[];this.LT.push([i,r]);};f.gc=function(r,q){if(this.LT)for(var
ha=0;ha<this.LT.length;ha++){var
Q=this.LT[ha];if(Q[0].isInstance(r))r[Q[1]].apply(r,q);}};f.toString=function(){return this.no.getName()+ub.d+this.zo;};d.newAbstract=function(c){var
Aa=ub.e;for(var
Y=0;Y<arguments.length;Y++){if(!arguments[Y].match(ub.f))throw new
jsx3.IllegalArgumentException(ub.g+Y+ub.h,arguments[Y]);Aa=Aa+(ub.i+arguments[Y]+ub.j);}var
H=ub.k;var
V=jsx3.eval(ub.l+Aa+ub.i+H+ub.m);V.co=true;return V;};d.newDelegate=function(c,s){var
la=ub.n+s+ub.d+c+ub.o+s+ub.p;return new
Function(la);};d.argsAsArray=function(p,g,n){if(g==null)g=0;if(n==null)n=p.length;else n=Math.min(n,p.length);var
I=n-g;if(I<=0)return [];var
Hb=new
Array(I);for(var
hb=0;hb<I;hb++)Hb[hb]=p[hb+g];return Hb;};};window._jsxDX(jsx3.lang.Method,jsx3.lang.Method.prototype);window._jsxDX=null;if(window["jsx3"]==null)window["jsx3"]=new
window.Object();if(jsx3.lang==null)jsx3.lang=new
window.Object();jsx3.lang.Class=jsx3.lang.qg();jsx3.lang.Class.prototype=new
jsx3.lang.Object();jsx3.lang.Class.prototype.__jsxclass__=jsx3.lang.Class;window._jsxDX=function(a,e){var
ub={o:"class.no_init",d:"jsx3.lang.Class",k:"object",r:"class.class_imp_class",c:"jsx3.util.Logger.Manager",h:"class.bad_super",p:"loaded ",g:"function",l:"jsx3.lang",q:"class.bad_int",b:"valueOf",i:"class.int_ext_class",m:"class.redefine",a:"toString",f:"jsx3.lang.Object",j:"class.class_ext_int",n:"class.def_error",s:"class.redef_method",e:"."};a.K5={"jsx3.lang.Object":1,"jsx3.lang.Method":1,"jsx3.lang.ClassLoader":1,"jsx3.lang.Class":2};a.W2=[ub.a,ub.b];a.y9={prototype:1,constructor:1,jsxclass:1,__jsxclass__:1};a.aM=null;a.defineClass=function(c,l,q,o){this.E8(c,l,q,o,false);};a.defineInterface=function(p,f,b){this.E8(p,f,null,b,true);};a.E8=function(b,k,r,p,h){if(a.aM==null&&a.forName&&a.forName(ub.c)&&jsx3.util.Logger.Manager.getManager())a.aM=jsx3.util.Logger.getLogger(ub.d);var
xb=b.split(ub.e);var
Ra=xb.pop();var
_=this.Lj(xb);var
ha=this.K5[b]!=null;var
Bb=null;if(k==null){Bb=h||b==ub.f?window.Object:jsx3.lang.Object;}else if(k instanceof a){Bb=k.getConstructor();}else if(typeof k==ub.g&&k.prototype!=null){Bb=k;}else a.R2(jsx3._msg(ub.h,k));var
V=false;if(!ha){if(Bb.jsxclass!=null){if(h&&!Bb.jsxclass.isInterface())a.R2(jsx3._msg(ub.i,b,Bb.jsxclass));if(!h&&Bb.jsxclass.isInterface())a.R2(jsx3._msg(ub.j,b,Bb.jsxclass));}if(typeof _[Ra]==ub.g){V=true;}else if(h){_[Ra]=jsx3.lang.qg();}else if(typeof _[Ra]==ub.k){var
Nb=_[Ra];_[Ra]=jsx3.lang.jm();for(var Na in Nb)_[Ra][Na]=Nb[Na];}else _[Ra]=jsx3.lang.jm();_[Ra].prototype=this.xM(Bb,h);}_[Ra].prototype.__jsxclass__=_[Ra];var
t=_[Ra];if(xb.join(ub.e)==ub.l)jsx3[Ra]=t;var
fb=a.Le(a);fb.zo=b;fb.is=t;if(Bb!=null)fb.Gq=Bb.jsxclass;fb.CJ=h;fb.OD=[];fb.HL=[];fb.jG=[];var
qb=fb.TH=[];var
Lb=fb.yI=[];fb.Iv={};fb.zV={};if(t.jsxclass!=null)a.R2(jsx3._msg(ub.m,b,t.jsxclass));t.jsxclass=fb;try{p(t,t.prototype);}catch(Kb){var
za=jsx3.NativeError?jsx3.NativeError.wrap(Kb):null;a.R2(jsx3._msg(ub.n,b,za||Kb.description),za);}for(var Na in t){if(a.y9[Na])continue;if(typeof t[Na]==ub.g){this.lB(t[Na],fb,Na,true);}else qb[qb.length]=Na;}for(var
R=0;R<a.W2.length;R++){var
ua=a.W2[R];if(t[ua]!=null&&t[ua]!=window.Function.prototype[ua]&&t[ua].jsxmethod==null)this.lB(t[ua],fb,ua,true);}for(var Na in t.prototype){if(a.y9[Na])continue;var
ib=t.prototype[Na];if(typeof ib==ub.g){if(Bb==null||ib!=Bb.prototype[Na])this.lB(ib,fb,Na,false);}else Lb[Lb.length]=Na;}for(var
R=0;R<a.W2.length;R++){var
ua=a.W2[R];if(t.prototype[ua]!=null&&t.prototype[ua]!=window.Object.prototype[ua]&&t.prototype[ua].jsxmethod==null)this.lB(t.prototype[ua],fb,ua,false);}if(!V&&!h&&!(typeof t.prototype.init==ub.g))a.R2(jsx3._msg(ub.o,b));if(r instanceof Array)for(var
R=r.length-1;R>=0;R--)a.iV(fb,t,r[R]);if(a.aM)a.aM.trace(ub.p+b);jsx3.CLASS_LOADER.so(fb);};a.iV=function(c,r,p){if(typeof p==ub.g&&p.jsxclass!=null)p=p.jsxclass;else if(!(p instanceof a))a.R2(jsx3._msg(ub.q,c,p));if(!p.isInterface())a.R2(jsx3._msg(ub.r,c,p));var
Xa=p.getConstructor().prototype;for(var ta in Xa){var
B=Xa[ta];var
W=typeof B==ub.g?B.jsxmethod:null;if(W==null)continue;var
mb=r.prototype[ta];if(mb==null){r.prototype[ta]=B;}else if(!mb.jsxmethod.getDeclaringClass().equals(c))r.prototype[ta]=B;}c.OD.unshift(p);};a.Lj=function(p){var
ma=window;for(var
R=0;R<p.length;R++){var
kb=p[R];if(ma[kb]==null)ma[kb]=new
window.Object();ma=ma[kb];}return ma;};a.lB=function(l,s,j,o){if(l.jsxmethod instanceof jsx3.lang.Method)if(l.jsxmethod.getDeclaringClass().equals(s))a.R2(jsx3._msg(ub.s,l.jsxmethod,s+ub.e+j));else return;var
M=a.Le(jsx3.lang.Method);M._function=l;M.no=s;M.zo=j;M.mg=o;M.co=Boolean(l.co);l.jsxmethod=M;var
wb=o?s.HL:s.jG;wb[wb.length]=M;};a.xM=function(b,k){if(b==Object)return {};return new
b(k?jsx3.lang.Uj:jsx3.lang.Jl);};a.Le=function(d){return new
d(jsx3.lang.Uj);};a.R2=function(i,s){if(a.aM){a.aM.fatal(i,s);}else if(jsx3.Exception){var
Da=new
jsx3.Exception(i,s);window.alert(Da.printStackTrace());}else window.alert(i);};};window._jsxDX(jsx3.lang.Class,jsx3.lang.Class.prototype);window._jsxDX=null;jsx3.lang.Class.defineClass("jsx3.lang.Class",null,null,function(n,k){var
ub={o:"class.mmix_bad",d:".",k:"class.int_imp_int",c:"function",h:"get",p:"m:",g:"class.bless_int",l:"class.class_imp_class",q:"undefined",b:"object",i:"is",m:"class.already_imp",a:/\./g,f:"class.new_inst",j:"set",n:"xV",e:""};n.forName=function(q){var
Wa=q.split(ub.a);var
tb=window;var
B;for(var
bb=0;bb<Wa.length;bb++){var
ka=Wa[bb];if(!ka)return null;B=typeof tb;if(B==ub.b||B==ub.c){tb=tb[ka];}else return null;}B=typeof tb;return B==ub.b||B==ub.c?tb.jsxclass:null;};k.getName=function(){return this.zo;};k.getPackage=function(){var
lb=this.zo;while(true){var
Ua=lb.lastIndexOf(ub.d);if(Ua<0)break;lb=lb.substring(0,Ua);var
nb=jsx3.lang.Package.forName(lb);if(nb!=null)return nb;if(n.forName(lb)==null)break;}return null;};k.getPackageName=function(){var
ya=this.getPackage();if(ya){return ya.getName();}else{var
Xa=this.zo.lastIndexOf(ub.d)+1;return Xa>=0?this.zo.substring(0,Xa-1):ub.e;}};k.getConstructor=function(){if(this.is!=null)return this.is;try{return jsx3.eval(this.zo);}catch(Kb){return null;}};k.getSuperClass=function(){return this.Gq;};k.isInterface=function(){return this.CJ;};k.toString=function(){return this.zo;};k.newInstance=function(l){if(arguments.length>10)throw new
jsx3.Exception(jsx3._msg(ub.f));var
z=arguments;var
la=this.getConstructor();return new
la(z[0],z[1],z[2],z[3],z[4],z[5],z[6],z[7],z[8],z[9]);};k.isInstance=function(i){var
D=i.__jsxclass__?i.__jsxclass__.jsxclass:null;return D!=null&&this.isAssignableFrom(D);};k.isAssignableFrom=function(p){if(this.equals(p))return true;if(p.xV==null)p.G0();return p.xV[this.getName()]==true;};k.G0=function(){this.xV={};for(var
db=0;db<this.OD.length;db++){var
rb=this.OD[db];this.xV[rb.getName()]=true;if(rb.xV==null)rb.G0();for(var F in rb.xV)this.xV[F]=true;}if(this.Gq!=null){this.xV[this.Gq.getName()]=true;if(this.Gq.xV==null)this.Gq.G0();for(var F in this.Gq.xV)this.xV[F]=true;}};k.mixin=function(p,l,s){if(s){for(var
qb=0;qb<s.length;qb++){var
gb=this.getInstanceMethod(s[qb]);if(gb&&p[gb.getName()]==null||!l)p[gb.getName()]=gb.getFunction();}}else for(var
qb=0;qb<this.jG.length;qb++){var
gb=this.jG[qb];if(p[gb.getName()]==null||!l)p[gb.getName()]=gb.getFunction();}};k.bless=function(b){if(this.isInterface())throw new
jsx3.Exception(jsx3._msg(ub.g,this));var
Cb=n.xM(this.getConstructor());if(b!=null)for(var Ib in b)if(!(typeof b[Ib]==ub.c))Cb[Ib]=b[Ib];return Cb;};k.newInnerClass=function(j){if(this.isInterface()){return n.Le(this.getConstructor());}else return this.newInstance.apply(this,arguments);};k.getStaticMethods=function(){return this.HL.concat();};k.getInstanceMethods=function(){return this.jG.concat();};k.getStaticMethod=function(h){for(var
jb=0;jb<this.HL.length;jb++)if(h==this.HL[jb].getName())return this.HL[jb];return null;};k.getInstanceMethod=function(l){for(var
Bb=0;Bb<this.jG.length;Bb++)if(l==this.jG[Bb].getName())return this.jG[Bb];return null;};k.getGetter=function(i){i=i.charAt(0).toUpperCase()+i.substring(1);return this.II(ub.h+i)||this.II(ub.i+i);};k.getSetter=function(i){i=i.charAt(0).toUpperCase()+i.substring(1);return this.II(ub.j+i);};k.getStaticFieldNames=function(){return this.TH.concat();};k.getInstanceFieldNames=function(){return this.yI.concat();};k.getInterfaces=function(){return this.OD.concat();};k.addInterface=function(g){var
wa=null;if(this.isInterface())wa=ub.k;else if(!g.isInterface())wa=ub.l;else if(g.isAssignableFrom(this))wa=ub.m;if(wa)throw new
jsx3.Exception(jsx3._msg(wa,this,g));n.iV(this,this.getConstructor(),g);delete this[ub.n];};k.getInheritance=function(){var
ib=this.OD.concat();if(this.Gq!=null){ib[ib.length]=this.Gq;ib.push.apply(ib,this.Gq.getInheritance());}return ib;};k.getClasses=function(){var
H=this.getConstructor();var
Da=[];for(var lb in H)if(typeof H[lb]==ub.c&&H[lb].jsxclass instanceof n){Da[Da.length]=H[lb].jsxclass;Da.push.apply(Da,H[lb].jsxclass.getClasses());}return Da;};k.addMethodMixin=function(d,q,p){var
da=this.getInstanceMethod(d);if(!da)n.R2(jsx3._msg(ub.o,this,d));da.Ob(q,p);};k.II=function(o,r){var
xb=null;if(!r)xb=this.getInstanceMethod(o);var
Za=this.getInheritance();for(var
Ab=0;xb==null&&Ab<Za.length;Ab++)xb=Za[Ab].getInstanceMethod(o);return xb;};k.jM=function(g,j){var
jb=null;if(!j)jb=this.getInstanceMethod(g);if(jb==null&&this.Gq!=null)jb=this.Gq.jM(g);return jb;};k.hE=function(c){var
Ya=null;for(var
Fb=0;Fb<this.OD.length&&Ya==null;Fb++)Ya=this.OD[Fb].getInstanceMethod(c);if(Ya==null&&this.Gq!=null)Ya=this.Gq.hE(c);return Ya;};k.yf=function(f){var
ba=f.getName();var
x=this.Iv[ub.p+ba];if(typeof x==ub.q)this.Iv[ub.p+ba]=x=this.jM(ba,true);return x;};k.Cn=function(f){var
Ua=f.getName();var
Gb=this.zV[ub.p+Ua];if(typeof Gb==ub.q)this.zV[ub.p+Ua]=Gb=this.hE(Ua);return Gb;};});jsx3.lang.Class.defineClass("jsx3.lang.Object",null,null,function(){});jsx3.lang.Class.defineClass("jsx3.lang.Method",null,null,function(){});jsx3.lang.Class.jsxclass.Gq=jsx3.lang.Object.jsxclass;jsx3.Class.defineClass("jsx3.lang.Exception",null,null,function(n,o){var
ub={o:/\s*,\s*/,d:"",k:"anonymous",r:" { ",c:"__noSuchMethod__",h:".",p:", ",g:"#",l:/\s+/g,q:")",b:"jsxsupermix",i:"()",m:" ",a:"jsxsuper",u:"\nCaused By:\n",f:"    at ",t:": ",j:"^function(\\s+\\w+)?\\s*\\(([^\\)]*)\\)\\s*{",n:"(",s:"anonymous()",e:"\n"};var
xb=jsx3.lang.Method;n.N7=false;n.Lh=null;o.init=function(f,l){n.Lh=this;this.Ro=f;this.JK=l;this.M4=[];this.k4();if(n.N7&&window.onerror==null)window.alert(f+this.printStackTrace());};o.toString=function(){return this.Ro;};o.getMessage=function(){return this.Ro;};o.getCause=function(){return this.JK;};o.getStack=function(){return this.M4;};n.sJ=[jsx3.Object.jsxclass.getInstanceMethod(ub.a),jsx3.Object.jsxclass.getInstanceMethod(ub.b),jsx3.Object.jsxclass.getInstanceMethod(ub.c)];n.formatStack=function(k){var
Ib=ub.d;if(!jsx3.util||!jsx3.util.jsxpackage)return Ib;for(var
ob=0;ob<k.length;ob++){var
ya=k[ob];if(ya==null)continue;if(ya.jsxmethod instanceof xb){var
wa=k[ob+1];if(wa!=null&&jsx3.util.arrIndexOf(n.sJ,wa.jsxmethod)>=0)if(ya==xb.prototype.apply)continue;if(jsx3.util.arrIndexOf(n.sJ,ya.jsxmethod)>=0)continue;if(Ib.length>0)Ib=Ib+ub.e;Ib=Ib+ub.f;Ib=Ib+ya.jsxmethod.getDeclaringClass().getName();Ib=Ib+(ya.jsxmethod.isStatic()?ub.g:ub.h);Ib=Ib+(ya.jsxmethod.getName()+ub.i);}else{if(Ib.length>0)Ib=Ib+ub.e;Ib=Ib+ub.f;if(ya.jsxclass instanceof jsx3.lang.Class){Ib=Ib+(ya.jsxclass.getName()+ub.i);}else{var
Y=ya.toString();if(Y.match(new
RegExp(ub.j))){var
ra=RegExp.$1||ub.k;var
Ka=RegExp.$2;var
V=RegExp.rightContext;V=jsx3.util.strTruncate(jsx3.util.strTrim(V).replace(ub.l,ub.m),70);Ib=Ib+(jsx3.util.strTrim(ra)+ub.n+jsx3.util.strTrim(Ka).split(ub.o).join(ub.p)+ub.q+(V?ub.r+V:ub.d));}else Ib=Ib+ub.s;}}}return Ib;};o.printStackTrace=function(){var
X=this.getClass().getName()+ub.t+this+ub.e+n.formatStack(this.M4);if(this.JK!=null)X=X+(ub.u+this.JK.printStackTrace());return X;};o.k4=function(){var
M=jsx3.lang.getStack(1);var
_a=-1;for(var
sa=0;sa<M.length;sa++)if(M[sa].jsxclass!=null){_a=sa;break;}if(_a>=0)M.splice(0,_a+1);this.M4=M;};});jsx3.Class.defineClass("jsx3.lang.IllegalArgumentException",jsx3.lang.Exception,null,function(k,c){var
ub={a:"exc.ill_arg"};c.init=function(o,f){this.jsxsuper(jsx3._msg(ub.a,o,f));};});jsx3.Class.defineClass("jsx3.lang.NativeError",jsx3.lang.Exception,null,function(r,b){var
ub={o:"Error",d:"error.trap",k:"RangeError",r:"line:",c:"jsx3.util.Logger",h:"objError",p:" (type:",g:"error.trap_err",l:"ReferenceError",q:", ",b:"uncaught exception:",i:/\s*$/,m:"SyntaxError",a:"",f:"\n",t:")",j:"EvalError",n:"TypeError",s:"file:",e:"error.uncaught"};var
bb=jsx3.Exception;r.NT=false;r.fT=true;r.wrap=function(j){if(j instanceof Error)return new
r(j);else if(j instanceof bb)return j;else return new
bb(ub.a+j);};r.initErrorCapture=function(e){window.onerror=arguments.length>0?e:r.d3;};r.stopErrorCapture=function(q){window.onerror=null;};r.yA=ub.b;r.d3=function(f,a,k){try{if(!r.NT&&jsx3.Class.forName(ub.c)!=null&&jsx3.util.Logger.GLOBAL!=null){if(f.indexOf(r.yA)>=0)if(bb.Lh!=null){var
y=jsx3.lang.getStack(0);if(y.length<2||y.contentsEqual(bb.Lh.getStack())){if(y.length<2)jsx3.util.Logger.GLOBAL.logStack(2,jsx3._msg(ub.d,f,r.Zz(k),a),1);jsx3.util.Logger.GLOBAL.error(jsx3._msg(ub.e),bb.Lh);bb.Lh=true;return true;}}jsx3.util.Logger.GLOBAL.logStack(2,jsx3._msg(ub.d,f,r.Zz(k),a),1);return true;}else if(r.fT){if(f.indexOf(r.yA)>=0)if(bb.Lh!=null){var
y=jsx3.lang.getStack(0);if(y.contentsEqual(bb.Lh.getStack())){window.alert(jsx3._msg(ub.e)+ub.f+bb.Lh.printStackTrace());return true;}}var
y=jsx3.lang.getStack(0);window.alert(jsx3._msg(ub.d,f,r.Zz(k),a)+ub.f+bb.formatStack(y));return true;}else return false;}catch(Kb){window.alert(jsx3._msg(ub.g,r.wrap(Kb),f,r.Zz(k),a));}};b.ex=null;b.init=function(j){if(!(j instanceof Error))throw new
jsx3.IllegalArgumentException(ub.h,j);this.jsxsuper();this.ex=j;};b.getMessage=function(){return this.ex.message!=null?this.ex.message.replace(ub.i,ub.a):ub.a;};b.getFileName=function(){return this.ex.fileName;};b.getLineNumber=function(){return r.Zz(this.ex.lineNumber);};r.Zz=function(p){if(jsx3.util.numIsNaN(p))return null;return p;};b.getName=function(){return this.ex.name;};b.isStructural=function(){return false;};b.getType=function(){if(this.ex instanceof EvalError)return ub.j;if(this.ex instanceof RangeError)return ub.k;if(this.ex instanceof ReferenceError)return ub.l;if(this.ex instanceof SyntaxError)return ub.m;if(this.ex instanceof TypeError)return ub.n;return ub.o;};b.toString=function(){var
ka=this.getLineNumber();var
xb=this.getFileName();var
tb=this.getMessage();if(ka||xb){tb=tb+(ub.p+this.getType()+ub.q);if(ka)tb=tb+(ub.r+ka);if(xb){if(ka)tb=tb+ub.q;tb=tb+(ub.s+xb);}tb=tb+ub.t;}return tb;};});jsx3.lang.Package=jsx3.lang.qg();jsx3.lang.Class.defineClass("jsx3.lang.Package",null,null,function(s,k){var
ub={d:"pkg.redefine",i:"class.nsm",k:"()",a:".",h:"object",c:"jsx3.lang",f:"jsxpackage",j:"#",l:"class.redef_method",g:"function",b:"jsx3.util.Logger",e:"pkg.def_error"};var
hb=jsx3.lang.Class;var
ea=jsx3.lang.Method;s.R8=[];s.definePackage=function(l,f){var
t=hb.Lj(l.split(ub.a));var
aa=false;var
rb=null;if(t.jsxpackage!=null){if(jsx3.Class.forName(ub.b))jsx3.util.Logger.getLogger(ub.c).info(jsx3._msg(ub.d,l));rb=t.jsxpackage;aa=true;rb.TH=[];rb.HL=[];}else{rb=hb.Le(s);rb.bO=l;rb.TH=[];rb.HL=[];t.jsxpackage=rb;}try{f(t);}catch(Kb){var
D=jsx3.NativeError.wrap(Kb);throw new
jsx3.Exception(jsx3._msg(ub.e,l,D),D);}for(var lb in t){if(lb==ub.f)continue;if(typeof t[lb]==ub.g){if(t[lb].jsxclass==null)this.lB(t[lb],rb,lb);}else if(t[lb]==null||typeof t[lb]!=ub.h||t[lb].jsxpackage==null)rb.TH.push(lb);}if(t.__noSuchMethod__==null)t.__noSuchMethod__=function(a,e){throw new
jsx3.Exception(jsx3._msg(ub.i,l+ub.j+a+ub.k));};s.R8.push(rb);jsx3.CLASS_LOADER.Vm(rb);};s.lB=function(d,a,p){if(d.jsxmethod instanceof ea)if(d.jsxmethod.getDeclaringClass().equals(a)&&d.jsxmethod.getName()!=p){throw new
jsx3.Exception(jsx3._msg(ub.l,d.jsxmethod,a+ub.a+p));}else{if(d.jsxmethod.getDeclaringClass().equals(a)&&jsx3.util.arrIndexOf(a.HL,d.jsxmethod)<0)a.HL.push(d.jsxmethod);return;}var
t=hb.Le(ea);t.no=a;t.zo=p;t.mg=true;d.jsxmethod=t;a.HL.push(t);};s.forName=function(l){try{var
lb=jsx3.eval(l);if(typeof lb==ub.h)return lb.jsxpackage;}catch(Kb){}return null;};s.getPackages=function(){return s.R8.concat();};k.bO=null;k.Ev=null;k.HL=null;k.TH=null;k.getName=function(){return this.bO;};k.getNamespace=function(){if(this.Ev!=null)return this.Ev;try{return jsx3.eval(this.bO);}catch(Kb){return null;}};k.getClasses=function(){var
xa=[];var
Nb=this.getNamespace();for(var R in Nb)if(typeof Nb[R]==ub.g&&Nb[R].jsxclass instanceof hb)if(Nb[R].jsxclass.getPackage()==this&&this.getName()+ub.a+R==Nb[R].jsxclass.getName()){xa[xa.length]=Nb[R].jsxclass;xa.push.apply(xa,Nb[R].jsxclass.getClasses());}return xa;};k.getStaticMethods=function(){return this.HL.concat();};k.getStaticMethod=function(m){for(var
B=0;B<this.HL.length;B++)if(m==this.HL[B].getName())return this.HL[B];return null;};k.getStaticFieldNames=function(){return this.TH.concat();};k.toString=function(){return this.bO;};});jsx3.Package.definePackage("jsx3",function(){var
ub={k:"string",h:"var ",c:"Msxml2.XSLTemplate.3.0",H:"GP",p:"gi",q:"http://www.tibco.com/gi",G:"jsx3.util.Logger",v:"script",i:" = _ec.",A:"\n",a:"JSX/addins/",F:"1",u:/\s+/,t:"tibco",j:";",s:"jsx",z:")",d:"Msxml2.XMLHTTP.3.0",o:"queueDone",D:"' of '",w:"function",r:"jsx3",C:"Super class '",B:"INHR01",l:"object",g:"",b:"Msxml2.FreeThreadedDOMDocument.3.0",m:"jsx3.gui.MatrixColumn",f:"JSX30INITIALIZE",y:".",n:"jsx3.gui.Matrix.Column",x:".prototype",E:"' not properly defined",e:"JSX30DESERIALIZE"};jsx3.ADDINSPATH=ub.a;jsx3.XMLREGKEY=ub.b;jsx3.XSLREGKEY=ub.c;jsx3.HTTPREGKEY=ub.d;jsx3.getXmlVersion=function(){return 4;};jsx3.DESERIALIZE=ub.e;jsx3.INITIALIZE=ub.f;jsx3.STARTUP_EVENT=null;jsx3.CACHE=null;jsx3.EVENT=null;jsx3.getSharedCache=function(){if(jsx3.CACHE==null)jsx3.CACHE=new
jsx3.app.Cache();return jsx3.CACHE;};jsx3.getSystemCache=function(){if(jsx3.kA==null)jsx3.kA=new
jsx3.app.Cache();return jsx3.kA;};jsx3.eval=function(s,g){if(s!=null&&s!==ub.g){var
nb=ub.g;if(g){var
_ec=g;var
eb=[];for(var da in _ec)eb[eb.length]=ub.h+da+ub.i+da+ub.j;nb=eb.join(ub.g);}return eval(nb+s);}};jsx3.resolveURI=function(f){return jsx3.net.URIResolver.DEFAULT.resolveURI(f).toString();};jsx3.makeCallback=function(l,i,r){var
da=[];for(var
B=2;B<arguments.length;B++)da[da.length]=arguments[B];if(typeof l==ub.k){return function(){var
Hb=da.concat();for(var
B=0;B<arguments.length;B++)Hb[Hb.length]=arguments[B];i[l].apply(i,Hb);};}else return function(){var
Ka=da.concat();for(var
B=0;B<arguments.length;B++)Ka[Ka.length]=arguments[B];l.apply(i,Ka);};};jsx3.clone=function(h){if(typeof h!=ub.l)return h;var
_={};for(var Ha in h)_[Ha]=h[Ha];return _;};jsx3.i2={};jsx3.i2[ub.m]=ub.n;jsx3.require=function(b){for(var
R=0;R<arguments.length;R++){var
Ma=arguments[R];Ma=jsx3.i2[Ma]||Ma;if(jsx3.Class.forName(Ma)==null)jsx3.CLASS_LOADER.loadClass(Ma);}};jsx3.jv=[];jsx3._P=[];jsx3.Uo=null;jsx3.sleep=function(p,n,b,o){if(n&&jsx3._P[n])if(o)jsx3._P[n][0]=null;else return;var
mb=[p,n,b];var
zb=jsx3.jv;zb[zb.length]=mb;jsx3._P[n]=mb;if(jsx3.Uo==null)jsx3.Uo=window.setTimeout(jsx3.m1,0);};jsx3.QUEUE_DONE=ub.o;jsx3.m1=function(){if(jsx3.lang.getCaller()!=null){jsx3.Uo=window.setTimeout(jsx3.m1,0);return;}jsx3.util.WeakMap.expire();var
pb=null;while(jsx3.jv.length>0&&(pb==null||pb[0]==null))pb=jsx3.jv.shift();if(pb){jsx3.Uo=window.setTimeout(jsx3.m1,0);var
Za=pb[0];var
rb=pb[1];var
fa=pb[2];delete jsx3._P[rb];Za.apply(fa);}else{jsx3.Uo=null;jsx3.publish({subject:jsx3.QUEUE_DONE});}};jsx3.startup=function(){};jsx3.destroy=function(){if(jsx3.app&&jsx3.app.Server){var
u=jsx3.app.Server.allServers();for(var
Z=0;Z<u.length;Z++)try{u[Z].destroy();}catch(Kb){}}if(jsx3.gui&&jsx3.gui.Event){var
pa="BEFOREUNLOAD BLUR CHANGE CLICK DOUBLECLICK ERROR FOCUS KEYDOWN KEYPRESS KEYUP LOAD MOUSEDOWN MOUSEMOVE MOUSEOUT MOUSEOVER MOUSEUP MOUSEWHEEL UNLOAD RESIZE".split(ub.u);for(var
Z=0;Z<pa.length;Z++)jsx3.gui.Event.unsubscribeAll(jsx3.gui.Event[pa[Z]]);}jsx3.NativeError.stopErrorCapture();var
qb=document.getElementsByTagName(ub.v);for(var
Z=0;Z<qb.length;Z++){var
ua=qb.item(Z);ua.parentNode.removeChild(ua);}jsx3.CLASS_LOADER.destroy();window.jsx3=null;};jsx3.getClass=function(j){try{var
D=eval(j);return typeof D==ub.w?D:null;}catch(Kb){return null;}};jsx3._l=function(q){try{var
sa=eval(q+ub.x);return typeof sa==ub.l&&typeof sa.constructor==ub.w?sa:null;}catch(Kb){return null;}};jsx3.getClassConstants=function(r){var
H=jsx3.getClass(r);if(H!=null){var
T=[];for(var bb in H)if(bb.toUpperCase()==bb)T[T.length]=r+ub.y+bb;return T;}};jsx3.getInstanceMethods=function(i){var
Fb=jsx3._l(i);var
Ca=[];for(var ab in Fb)if(typeof Fb[ab]==ub.w){var
Wa=Fb[ab].toString();Ca[Ca.length]=Wa.substring(9,Wa.indexOf(ub.z)+1);}return Ca;};jsx3.getClassMethods=function(l){var
Sa=jsx3.getClass(l);var
Bb=ub.g;for(var Q in Sa)if(typeof Sa[Q]==ub.w){var
qb=Sa[Q].toString();Bb=Bb+(Q+qb.substring(8,qb.indexOf(ub.z)+1)+ub.A);}return Bb;};jsx3.doInherit=function(o,d,m){var
Kb=jsx3.getClass(o);var
z=jsx3.getClass(d);if(z==null){jsx3.util.Logger.doLog(ub.B,ub.C+d+ub.D+o+ub.E,1,false);return;}if(Kb.isInherited==null||m!=null&&m){if(!m){Kb.SUPER=d;Kb.SUPERS={};for(var Ga in z.SUPERS){Kb.SUPERS[Ga]=z.SUPERS[Ga];Kb.SUPERS[o]=1;}}Kb.className=o;if(Kb.INHERITANCE==null)Kb.INHERITANCE=[o];if(z.INHERITANCE)for(var
fa=z.INHERITANCE.length-1;fa>=0;fa--)Kb.INHERITANCE.splice(1,0,z.INHERITANCE[fa]);else Kb.INHERITANCE.splice(1,0,d);if(Kb.INTERFACES==null)Kb.INTERFACES={};if(z.INTERFACES){for(var Ga in z.INTERFACES)Kb.INTERFACES[Ga]=z.INTERFACES[Ga];}else Kb.INTERFACES[d]=ub.F;Kb.INTERFACES[o]=ub.F;if(!(m!=null&&m))Kb.isInherited=true;var
Ea=jsx3._l(d);var
K=jsx3._l(o);for(var Ga in Ea)if(typeof K[Ga]!=ub.w)K[Ga]=Ea[Ga];}};jsx3.doImplement=function(m,b){jsx3.doInherit(m,b,true);};jsx3.doMixin=function(e,m){var
v=jsx3._l(m);for(var kb in v)if(typeof v[kb]==ub.w)e[kb]=v[kb];};jsx3.doDefine=function(l,o){var
tb=jsx3.getClass(l);if(tb.isDefined==null){tb.isDefined=true;o();}};jsx3.out=function(s,a,l,m){if(jsx3.Class.forName(ub.G))jsx3.util.Logger.doLog(s,a,l,m);};window.doInherit=jsx3.doInherit;window.doImplement=jsx3.doImplement;window.doMixin=jsx3.doMixin;window.doDefine=jsx3.doDefine;jsx3.log=function(n){if(jsx3.Class.forName(ub.G)&&jsx3.util.Logger.GLOBAL){if(jsx3.GP){for(var
L=0;L<jsx3.GP.length;L++)jsx3.util.Logger.GLOBAL.info(jsx3.GP[L]);delete jsx3[ub.H];}jsx3.util.Logger.GLOBAL.info(n);}else{var
Ka=jsx3.GP;if(!Ka)jsx3.GP=Ka=[];Ka[Ka.length]=n;}};});jsx3.Package.definePackage("jsx3.app",function(){});jsx3.Boolean={};jsx3.Boolean.TRUE=1;jsx3.Boolean.FALSE=0;jsx3.Boolean.valueOf=function(s){return s?1:0;};jsx3.Package.definePackage("jsx3.gui",function(q){var
ub={a:"className"};q.Yl=function(j,f){var
x=[];for(var
Jb=0;Jb<arguments.length-1;Jb=Jb+2){j=arguments[Jb];if(j._jsxRr)continue;j._jsxRr=true;f=arguments[Jb+1];var
Ib={};for(var K in f)Ib[K]=K==ub.a?j.className:j.style[K];x.push(j,Ib,f);}q.uF(x,0,6);};q.uF=function(g,i,l){if(i==l){for(var
cb=0;cb<g.length-2;cb=cb+3){var
H=g[cb];H._jsxRr=null;}return;}for(var
cb=0;cb<g.length-2;cb=cb+3){var
H=g[cb];var
xa=i%2==0?g[cb+2]:g[cb+1];for(var La in xa)if(La==ub.a)H.className=xa[La];else H.style[La]=xa[La];}window.setTimeout(function(){q.uF(g,i+1,l);},75);};q.isMouseEventModKey=function(b){if(jsx3.app.Browser.macosx)return b.metaKey();else return b.ctrlKey();};});jsx3.Package.definePackage("jsx3.lang",function(k){});jsx3.Package.definePackage("jsx3.net",function(r){});jsx3.Class.defineInterface("jsx3.net.URIResolver",null,function(a,r){var
ub={o:/!/g,d:"jsx",k:"/",c:"jsxscriptapppath",h:"!",p:"strURI",g:"jsxaddin",l:"JSXAPPS",q:"bRel",b:"jsxhomepath",i:":",m:"GI_Builder/",a:"jsxabspath",f:"jsxapp",j:"jsxuser",n:"..",e:"JSX"};a.VA=function(){if(this.V0==null)this.V0=new
jsx3.net.URI(jsx3.getEnv(ub.a));return this.V0;};a.Ds=function(){var
z=jsx3.getEnv(ub.b);if(z==null)return new
jsx3.net.URI(jsx3.getEnv(ub.c));if(this.vr==null)this.vr=new
jsx3.net.URI(z);return this.vr;};a.DEFAULT=a.jsxclass.newInnerClass();a.DEFAULT.resolveURI=function(i){var
va=jsx3.net.URI.valueOf(i);var
Gb=va.getScheme();var
hb=va.getPath();var
S=null;if(Gb){var
y=jsx3.net.URI.fromParts(null,null,null,null,hb,va.getQuery(),va.getFragment());if(Gb==ub.d){S=a.VA().resolve(ub.e+y);}else if(Gb==ub.f){var
wb=a.getResolver(va);S=wb.resolveURI(y.toString().substring(1));}else if(Gb==ub.g){var
da=va.getHost();var
Ha=da.split(ub.h,2).join(ub.i);var
lb=jsx3.System.getLoadedAddinByKey(Ha);S=lb!=null?lb.resolveURI(y.toString().substring(1)):va;}else if(Gb==ub.j){S=a.Ds().resolve(y.toString().substring(1));}else S=va;}else if(hb.indexOf(ub.e+ub.k)==0){S=a.VA().resolve(va);}else if(hb.indexOf(ub.l+ub.k)==0){S=a.Ds().resolve(va);}else if(hb.indexOf(ub.k)==0){S=va;}else if(hb.indexOf(ub.m)==0){S=a.VA().resolve(va);}else if(hb.indexOf(ub.n)>=0){var
Q=jsx3.app.Browser.getLocation();S=Q.relativize(Q.resolve(va));}else S=va;return S;};a.DEFAULT.getUriPrefix=function(){return a.VA().toString();};a.DEFAULT.relativizeURI=function(l,s){return jsx3.net.URI.valueOf(l);};a.JSX=a.jsxclass.newInnerClass();a.JSX.getURI=function(){if(this._uri==null)this._uri=a.VA().resolve(ub.e+ub.k);return this._uri;};a.JSX.resolveURI=function(l){var
da=jsx3.net.URI.valueOf(l);if(!a.isAbsoluteURI(da)){return a.DEFAULT.resolveURI(this.getURI().resolve(da.toString()));}else return a.DEFAULT.resolveURI(da);};a.JSX.getUriPrefix=function(){return a.VA()+ub.e+ub.k;};a.JSX.relativizeURI=function(p,d){var
Fb=this.getURI().relativize(p);if(Fb.isAbsolute()||d)return Fb;else return jsx3.net.URI.fromParts(ub.d,null,null,null,ub.k+Fb.getPath(),Fb.getQuery(),Fb.getFragment());};a.USER=a.jsxclass.newInnerClass();a.USER.resolveURI=function(n){var
qa=jsx3.net.URI.valueOf(n);if(!a.isAbsoluteURI(qa)){return a.DEFAULT.resolveURI(a.Ds().resolve(qa));}else return a.DEFAULT.resolveURI(qa);};a.USER.getUriPrefix=function(){return a.Ds().toString();};a.USER.relativizeURI=function(d,k){var
I=a.Ds().relativize(d);if(I.isAbsolute()||k)return I;else return jsx3.net.URI.fromParts(ub.j,null,null,null,ub.k+I.getPath(),I.getQuery(),I.getFragment());};a.isAbsoluteURI=function(s){var
lb=jsx3.net.URI.valueOf(s);if(lb.getScheme()!=null)return true;var
I=lb.getPath();return I.indexOf(ub.k)==0||I.indexOf(ub.e+ub.k)==0||I.indexOf(ub.l+ub.k)==0||I.indexOf(ub.m)==0;};a.getResolver=function(m){var
Ua=jsx3.net.URI.valueOf(m);var
fa=Ua.getScheme();if(fa)if(fa==ub.d){return a.JSX;}else if(fa==ub.f){var
I=Ua.getHost().replace(ub.o,ub.k);var
ka=jsx3.System.getLoadedAppByPath(I);return ka!=null?ka:new
jsx3.net.ServerURIResolver(I);}else if(fa==ub.g){var
ba=Ua.getHost();var
K=ba.split(ub.h,2).join(ub.i);return jsx3.System.getLoadedAddinByKey(K);}else if(fa==ub.j)return a.USER;return a.DEFAULT;};r.resolveURI=jsx3.Method.newAbstract(ub.p);r.getUriPrefix=jsx3.Method.newAbstract();r.relativizeURI=jsx3.Method.newAbstract(ub.p,ub.q);});jsx3.Class.defineClass("jsx3.net.ServerURIResolver",null,[jsx3.net.URIResolver],function(c,a){var
ub={d:/!/g,a:"jsxhomepath",c:"/",b:"JSXAPPS",e:"jsxapp"};var
F=jsx3.net.URIResolver;a.init=function(i){this._host=i;this._uri=new
jsx3.net.URI(jsx3.getEnv(ub.a)+ub.b+ub.c+i.replace(ub.d,ub.c)+ub.c);};a.resolveURI=function(f){var
D=jsx3.net.URI.valueOf(f);if(!F.isAbsoluteURI(D)){return F.DEFAULT.resolveURI(this._uri.resolve(D.toString()));}else return F.DEFAULT.resolveURI(D);};a.getUriPrefix=function(){return this._uri.toString();};a.relativizeURI=function(m,s){var
Nb=this._uri.relativize(m);if(Nb.isAbsolute()||s)return Nb;else return jsx3.net.URI.fromParts(ub.e,null,this._host,null,ub.c+Nb.getPath(),Nb.getQuery(),Nb.getFragment());};a.toString=function(){return this._uri.toString();};});jsx3.Package.definePackage("jsx3.xml",function(f){});jsx3.Package.definePackage("jsx3.util",function(c){var
ub={o:"...",d:/(^\s*)|(\s*$)/g,k:/\"/g,r:"bench.",c:"",h:"&lt;",p:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/",g:/</g,l:"&quot;",q:"=",b:/\./,v:":",i:/>/g,m:/[^\x09\x0A\x0D\x20-\x7F]/g,a:/^(\d+)?([a-zA-Z_]\w*)?$/,u:" ",f:"&amp;",t:"ms",j:"&gt;",n:"\\u",s:" : ",e:/&/g};c.compareVersions=function(k,j){var
Jb=ub.a;var
S=k.split(ub.b);var
t=j.split(ub.b);var
lb=Math.max(S.length,t.length);var
K,wb,Na,w;for(var
wa=0;wa<lb;wa++){if(S.length>wa&&Jb.test(S[wa])){K=parseInt(RegExp.$1)||Number(0);wb=RegExp.$2;}else{K=0;wb=ub.c;}if(t.length>wa&&Jb.test(t[wa])){Na=parseInt(RegExp.$1)||Number(0);w=RegExp.$2;}else{Na=0;w=ub.c;}if(K>Na)return 1;if(K<Na)return -1;if(wb>w)return 1;if(wb<w)return -1;}return 0;};c.numMod=function(f,a){return f<0?f%a+a:f%a;};c.numIsNaN=function(g){return g==null||g===ub.c||isNaN(g);};c.numRound=function(e,a){return Math.round(e/a)*a;};c.strEmpty=function(a){return a==null||a===ub.c;};c.arrIndexOf=function(d,h){for(var
t=0;t<d.length;t++)if(d[t]===h)return t;return -1;};c.pO=ub.d;c.strTrim=function(n){return n.replace(c.pO,ub.c);};c.strEscapeHTML=function(g){return g.replace(ub.e,ub.f).replace(ub.g,ub.h).replace(ub.i,ub.j).replace(ub.k,ub.l).replace(ub.m,function(b){var
Q=b.charCodeAt(0);if(Q<32||Q>55295&&Q<57344||Q>65533&&Q<65536||Q>1114111)return ub.n+Q.toString(16);else return b;});};c.strTruncate=function(e,b,o,s){if(o==null)o=ub.o;if(s==null)s=1;if(e.length>b&&o.length<b){var
N=b-o.length;var
eb=Math.round(N*s);var
zb=e.substring(0,eb);var
S=e.substring(e.length-(N-eb));return zb+o+S;}else return e;};c.strEndsWith=function(p,r){var
Ta=p.lastIndexOf(r);return Ta>=0&&Ta==p.length-r.length;};c.J5=ub.p;c.strEncodeBase64=function(q){var
ib=c.J5;var
Ib=new
Array(Math.ceil(q.length*4/3));var
N=0,lb=0,V=q.length;for(;N<=V-3;N=N+3){var
ia=(q.charCodeAt(N)&255)<<16|(q.charCodeAt(N+1)&255)<<8|q.charCodeAt(N+2)&255;Ib[lb++
]=ib.charAt((ia&16515072)>>18);Ib[lb++
]=ib.charAt((ia&258048)>>12);Ib[lb++
]=ib.charAt((ia&4032)>>6);Ib[lb++
]=ib.charAt(ia&63);}if(N<V){var
S=N<V-1;var
ia=(q.charCodeAt(N)&255)<<16;if(S)ia=ia|(q.charCodeAt(N+1)&255)<<8;Ib[lb++
]=ib.charAt((ia&16515072)>>18);Ib[lb++
]=ib.charAt((ia&258048)>>12);if(S)Ib[lb++
]=ib.charAt((ia&4032)>>6);else Ib[lb++
]=ub.q;Ib[lb++
]=ub.q;}return Ib.join(ub.c);};c.strDecodeBase64=function(a){var
D=c.J5;var
Ya=new
Array(Math.ceil(a.length/4));var
Cb=0,Ia=0,ma=a.length;for(;Cb<ma;Cb=Cb+4){var
Bb=(D.indexOf(a.charAt(Cb))&255)<<18|(D.indexOf(a.charAt(Cb+1))&255)<<12|(D.indexOf(a.charAt(Cb+2))&255)<<6|D.indexOf(a.charAt(Cb+3))&255;Ya[Ia++
]=String.fromCharCode((Bb&16711680)>>16,(Bb&65280)>>8,Bb&255);}if(a.charCodeAt(Cb-2)==61)Ya[Ia-1]=Ya[Ia-1].substring(0,1);else if(a.charCodeAt(Cb-1)==61)Ya[Ia-1]=Ya[Ia-1].substring(0,2);return Ya.join(ub.c);};c.Timer=function(e,h,d){this._t1=(new
Date()).getTime();this._topic=e.toString();this._message=h;this._level=d||4;};c.Timer.prototype.log=function(g,o){var
fb=(new
Date()).getTime();if(!this._logger)if(jsx3.util.Logger)this._logger=jsx3.util.Logger.getLogger(ub.r+this._topic);if(this._logger&&this._logger.isLoggable(this._level))this._logger.log(this._level,this._message+(g?ub.s+g:ub.c)+ub.s+(fb-this._t1)+ub.t);if(o)this._t1=fb;};c.CatTimer=function(m,r,s){this._topic=m.toString();this._name=r;this._level=s||4;this.reset();};c.CatTimer.prototype.start=function(n){if(!this._current){this._current=n;this._lasttime=(new
Date()).getTime();}};c.CatTimer.prototype.pause=function(r){if(this._current==r){var
Ea=(new
Date()).getTime()-this._lasttime;this._current=null;if(this._cats[r]==null)this._cats[r]=0;this._cats[r]+=Ea;}};c.CatTimer.prototype.log=function(){var
va=this._name;for(var Fb in this._cats)va=va+(ub.u+Fb+ub.v+this._cats[Fb]+ub.t);if(jsx3.util.Logger)jsx3.util.Logger.getLogger(ub.r+this._topic).log(this._level,va);};c.CatTimer.prototype.reset=function(){this._cats={};this._current=null;this._lasttime=null;};});jsx3.Class.defineClass("jsx3.util.WeakMap",null,null,function(k,e){var
ub={a:"lv"};k.cN=0;k.lZ={};k.Q2=null;k.expire=function(){window.clearTimeout(k.Q2);k.Q2=null;for(var qb in k.lZ){var
J=k.lZ[qb];if(J.vJ)J.lv={};}};e.init=function(){this.rS=k.cN++;this.lv={};this.vJ=false;k.lZ[this.rS]=this;};e.get=function(l){return this.lv[l];};e.set=function(b,j){this.lv[b]=j;this.vJ=true;if(!k.Q2)k.Q2=window.setTimeout(k.expire,0);};e.destroy=function(r,g){delete this[ub.a];delete k.lZ[this.rS];};});jsx3.Class.defineClass("jsx3.util.List",null,null,function(m,o){var
ub={d:"]",a:"a",c:"[",b:"number"};m.wrap=function(g){if(g instanceof m){return g;}else if(g instanceof Array){return new
m(g,true);}else throw new
jsx3.IllegalArgumentException(ub.a,g);};o.init=function(l,h){if(typeof l==ub.b){this.rf=new
Array(l);}else if(l instanceof m){this.rf=l.rf.concat();}else{l=m.vM(l);if(l instanceof Array){this.rf=h?l:l.concat();}else this.rf=[];}this.Y6=-1;};o.size=function(){return this.rf.length;};o.get=function(k){return this.rf[k];};o.set=function(i,k){this.rf[i]=k;};o.iterator=function(){return new
m.Iterator(this);};o.clear=function(){this.rf.splice(0,this.rf.length);};o.indexOf=function(p,r){if(r==null)r=0;var
Ta=this.size();for(var
sa=r;sa<Ta;sa++)if(this.get(sa)===p)return sa;return -1;};o.lastIndexOf=function(d,f){if(f==null)f=this.size()-1;for(var
ea=f;ea>=0;ea--)if(this.get(ea)===d)return ea;return -1;};o.contains=function(h){return this.indexOf(h)>=0;};o.remove=function(f){var
va=this.indexOf(f);if(va>=0)return this.rf.splice(va,1)[0];return null;};o.removeAt=function(n,b){if(arguments.length==2){return m.wrap(this.rf.splice(n,b-n));}else return this.rf.splice(n,1)[0];};o.clone=function(){return new
m(this);};o.add=function(q,n){var
bb=this.rf;if(n==null)bb[bb.length]=q;else bb.splice(n,0,q);};o.addAll=function(l,q){if(l instanceof m)l=l.toArray(true);else l=m.vM(l);if(l instanceof Array){if(q==null)this.rf.push.apply(this.rf,l);else this.rf=this.rf.slice(0,q).concat(l,this.rf.slice(q));}else throw new
jsx3.IllegalArgumentException(ub.a,l);};m.vM=function(s){if(s==null||s instanceof Array){return s;}else if(typeof s.length==ub.b){var
H=new
Array(s.length);for(var
Z=0;Z<s.length;Z++)H[Z]=s[Z];return H;}else return s;};o.slice=function(i,a){return m.wrap(arguments.length>1?this.rf.slice(i,a):this.rf.slice(i));};o.sort=function(e){if(e)this.rf.sort(e);else this.rf.sort();};o.toArray=function(q){return q?this.rf:this.rf.concat();};o.equals=function(d){if(this===d)return true;if(!(d instanceof m))return false;var
mb=this.size();if(mb!=d.size())return false;for(var
ob=0;ob<mb;ob++)if(this.get(ob)!==d.get(ob))return false;return true;};o.filter=function(r){var
ua=[];var
wb=this.size();for(var
Fa=0;Fa<wb;Fa++){var
Ra=this.get(Fa);if(r(Ra))ua[ua.length]=Ra;}return m.wrap(ua);};o.map=function(e,j,c){var
xa=this.size();if(j){if(c){var
ca={};for(var
Za=0;Za<xa;Za++){var
Na=e(this.get(Za));for(var
B=0;B<Na.length;B=B+2)ca[Na[B]]=Na[B+1];}return ca;}else{var
ca=[];for(var
Za=0;Za<xa;Za++){var
Ia=e(this.get(Za));if(Ia instanceof Array)ca.push.apply(ca,Ia);else ca[ca.length]=Ia;}return m.wrap(ca);}}else if(c){var
ca={};for(var
Za=0;Za<xa;Za++){var
jb=e(this.get(Za));ca[jb[0]]=jb[1];}return ca;}else{var
ca=new
Array(xa);for(var
Za=0;Za<xa;Za++)ca[Za]=e(this.get(Za));return m.wrap(ca);}};o.toString=function(){return ub.c+this.rf+ub.d;};o.reset=function(){this.Y6=-1;return this;};o.next=function(){return this.get(
++this.Y6);};o.move=function(k){this.Y6=k;return this;};o.hasNext=function(){return this.Y6<this.size()-1;};o.getIndex=function(){return this.Y6;};o.getItem=function(i){return this.get(i);};o.getLength=function(){return this.size();};});jsx3.Class.defineInterface("jsx3.util.Iterator",null,function(n,r){r.next=jsx3.Method.newAbstract();r.hasNext=jsx3.Method.newAbstract();r.remove=jsx3.Method.newAbstract();});jsx3.Class.defineClass("jsx3.util.List.Iterator",null,[jsx3.util.Iterator],function(n,r){r.init=function(b){this.eg=b;this.wm=b?b.size():0;this.Fx=0;};r.next=function(){return this.eg.get(this.Fx++);};r.hasNext=function(){return this.Fx<this.wm;};r.remove=function(){if(this.Fx>0){this.eg.removeAt(
--this.Fx);this.wm--;}};});jsx3.Class.defineClass("jsx3.app.AddIn",null,[jsx3.net.URIResolver],function(d,j){var
ub={d:"description",i:"jsxaddin",k:"!",a:"prototypes/",h:"3.2",c:"name",f:"jsxversion",j:/:/,l:"/",g:"3.1",b:"id",e:"version"};var
C=jsx3.net.URIResolver;d.PROTOTYPES_DIR=ub.a;j.init=function(m,r){var
Ea=jsx3.System.addinKeyToPath(m);this.Gm=m;this.ks=Ea;this.io=new
jsx3.net.URI(Ea);this.Co=jsx3.app.Browser.getLocation().resolve(this.io);this.p7=r;};j.getId=function(){return this.getSettings().get(ub.b);};j.getName=function(){return this.getSettings().get(ub.c);};j.getDescription=function(){return this.getSettings().get(ub.d);};j.getVersion=function(){return this.getSettings().get(ub.e);};j.getJsxVersion=function(){return this.getSettings().get(ub.f)||ub.g;};j.getKey=function(){return this.Gm;};j.getPath=function(){return this.ks;};j.getSettings=function(){if(this.p7==null)this.p7=new
jsx3.app.Settings(3,this);return this.p7;};j.resolveURI=function(i){var
fb=jsx3.net.URI.valueOf(i);if(jsx3.util.compareVersions(this.getJsxVersion(),ub.h)>=0&&!C.isAbsoluteURI(fb)){return C.DEFAULT.resolveURI(this.io.resolve(fb));}else return C.DEFAULT.resolveURI(fb);};j.getUriPrefix=function(){return this.io.toString();};j.relativizeURI=function(e,l){var
t=jsx3.app.Browser.getLocation();var
ma=this.Co.relativize(t.resolve(e));if(ma.isAbsolute()||l)return ma;else return jsx3.net.URI.fromParts(ub.i,null,this.getKey().replace(ub.j,ub.k),null,ub.l+ma.getPath(),ma.getQuery(),ma.getFragment());};j.toString=function(){return this.Gm;};});jsx3.Class.defineInterface("jsx3.util.EventDispatcher",null,function(c,m){var
ub={d:"string",i:"objEvent",a:"*",h:"}",c:"function",f:"{",g:"}, {",b:"object",e:"objHandler, objFunction"};c.i4=1;c.VS=2;c.Ly=3;c.lS=4;c.dw=5;m._jsxVH=null;c.SUBJECT_ALL=ub.a;m.subscribe=function(j,s,f){var
G=typeof s;var
vb=typeof f;var
w=null;if(G==ub.b){if(vb==ub.c){w=[c.i4,s,f];}else if(vb==ub.d)w=[c.VS,s,f];}else if(G==ub.d){if(vb==ub.c){w=[c.Ly,s,f];}else if(vb==ub.d)w=[c.lS,s,f];}else if(G==ub.c)w=[c.dw,s];if(w==null&&G==ub.b&&s.call&&s.apply)w=[c.dw,s];if(w==null)throw new
jsx3.IllegalArgumentException(ub.e,ub.f+typeof s+ub.g+typeof f+ub.h);if(!(j instanceof Array))j=[j];for(var
ba=0;ba<j.length;ba++){var
cb=this.b0();var
C=cb[j[ba]];if(C==null)C=cb[j[ba]]=new
jsx3.util.List();C.add(w);}};m.unsubscribe=function(h,b){if(!(h instanceof Array))h=[h];for(var
Hb=0;Hb<h.length;Hb++){var
Ra=this.b0()[h[Hb]];if(Ra!=null)for(var
ea=Ra.iterator();ea.hasNext();)if(ea.next()[1]===b)ea.remove();}};m.unsubscribeAll=function(h){delete this.b0()[h];};m.unsubscribeAllFromAll=function(){this._jsxVH={};};m.publish=function(a){if(a.target==null)a.target=this;var
rb=a.subject;if(rb==null)throw new
jsx3.IllegalArgumentException(ub.i,a);var
ja=this.b0()[rb];var
ib=this.b0()[ub.a];if((ja==null||ja.size()==0)&&(ib==null||ib.size()==0))return 0;var
F=new
jsx3.util.List();if(ja!=null)F.addAll(ja);if(ib!=null)F.addAll(ib);for(var
Mb=F.iterator();Mb.hasNext();){var
ua=Mb.next();var
Aa=ua[0];var
ma=ua[1];var
wa=ua[2];if(Aa==c.i4){wa.call(ma,a);}else if(Aa==c.VS){ma[wa](a);}else if(Aa==c.Ly){var
Bb=this.getServer().getJSX(ma);if(Bb)wa.call(Bb,a);}else if(Aa==c.lS){var
Bb=this.getServer().getJSX(ma);if(Bb)Bb[wa](a);}else if(Aa==c.dw){ma.call(null,a);}else{}}return F.size();};m.getSubscriberCount=function(k){var
sb=this.b0()[k];return sb!=null?sb.size():0;};m.b0=function(){if(this._jsxVH==null)this._jsxVH={};return this._jsxVH;};});jsx3.util.EventDispatcher.jsxclass.mixin(jsx3);jsx3.EventDispatcher=jsx3.util.EventDispatcher;jsx3.Class.defineClass("jsx3.net.URI",null,null,function(s,f){var
ub={k:"@",h:"][\\-\\.\\+",c:"_-!.~'()*",p:"?",q:"#",v:"&",i:"]*\\:",A:"%0",a:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",u:"..",t:".",j:"arguments",s:"/",z:"]*$",d:",;:$&+=",o:"//",w:"=",r:"string",B:/[^a-fA-F0-9]/,l:":",g:"^[",b:"0123456789",m:"",f:"%",y:"\\$1",n:"/@",x:/(\W)/g,e:"?/[]@"};s.o1=ub.a;s.TZ=ub.b;s.ou=s.o1+s.TZ;s.B4=s.ou+ub.c;s.FQ=ub.d;s.nB=s.FQ+ub.e;s.FJ=ub.f;s._o=new
RegExp(ub.g+s.o1+ub.h+s.o1+ub.i);f.v3=null;f.RR=null;f.GH=null;f.fs=null;f.Bq=null;f.ks=null;f.dp=null;f.Fy=null;f.K4=null;f.Sw=null;s.fromParts=function(k,p,e,m,o,r,l){var
sb=s.jsxclass.bless();var
wa=arguments;var
B=null,N=null;if(wa.length==3){sb.RR=wa[0];N=s.encode(wa[1],s.B4+s.nB+s.FJ);sb.GH=s.decode(wa[1]);sb.fs=s.decode(wa[2]);}else if(wa.length==7){sb.RR=wa[0];sb.Fy=s.decode(wa[1]);sb.K4=wa[2];sb.Sw=wa[3];sb.ks=s.decode(wa[4]);sb.dp=s.decode(wa[5]);sb.fs=s.decode(wa[6]);}else throw new
jsx3.IllegalArgumentException(ub.j,jsx3.Method.argsAsArray(wa));if(sb.Bq==null&&sb.K4!=null){sb.Bq=B=sb.K4;if(sb.Fy){sb.Bq=sb.Fy+ub.k+sb.Bq;B=s.encode(sb.Fy,s.B4+s.FQ+s.FJ)+ub.k+B;}if(sb.Sw){sb.Bq=sb.Bq+ub.l+sb.Sw;B=B+ub.l+sb.Sw;}}if(sb.GH==null){sb.GH=N=ub.m;if(sb.ks){sb.GH=sb.ks;N=s.encode(sb.ks,s.B4+s.FQ+s.FJ+ub.n);}if(sb.Bq!=null){sb.GH=ub.o+sb.Bq+sb.GH;N=ub.o+B+N;}if(sb.dp){sb.GH=sb.GH+ub.p+sb.dp;N=N+ub.p+s.encode(sb.dp,s.B4+s.FQ+s.FJ);}}if(sb.v3==null){sb.v3=N;if(sb.RR)sb.v3=sb.RR+ub.l+sb.v3;if(sb.fs!=null)sb.v3=sb.v3+ub.q+s.encode(sb.fs,s.B4+s.nB+s.FJ);}return sb;};f.init=function(j){if(j==null)j=ub.m;if(typeof j!=ub.r)j=j.toString();this.v3=j;var
Ja=j;var
Ua;if(s._o.test(Ja)){var
Bb=RegExp.lastMatch;this.RR=Ja.substring(0,Bb.length-1);Ja=Ja.substring(Bb.length);}if((Ua=Ja.indexOf(ub.q))>=0){this.fs=s.decode(Ja.substring(Ua+1));Ja=Ja.substring(0,Ua);}this.GH=Ja;var
Z=this.RR!=null;var
Ea=Z&&Ja.indexOf(ub.s)!=0;if(!Ea){if(Ja.indexOf(ub.o)==0){Ua=Ja.indexOf(ub.s,2);this.Bq=Ja.substring(2,Ua>=0?Ua:Ja.length);Ja=Ua>=0?Ja.substring(Ua):ub.m;}if((Ua=Ja.indexOf(ub.p))>=0){this.dp=s.decode(Ja.substring(Ua+1));Ja=Ja.substring(0,Ua);}this.ks=s.decode(Ja);var
Ma=this.Bq;if(Ma){if((Ua=Ma.indexOf(ub.k))>=0){this.Fy=s.decode(Ma.substring(0,Ua));Ma=Ma.substring(Ua+1);}if((Ua=Ma.indexOf(ub.l))>=0){this.Sw=parseInt(Ma.substring(Ua+1));Ma=Ma.substring(0,Ua);}}this.K4=Ma;}};f.normalize=function(){if(jsx3.util.strEmpty(this.ks))return this;var
La=this.ks.split(ub.s);s.N0(La);var
u=La.join(ub.s);return u==this.ks?this:s.fromParts(this.RR,this.Fy,this.K4,this.Sw,u,this.dp,this.fs);};s.N0=function(g){var
Na=g[0]!==ub.m;for(var
Lb=g.length-1;Lb>=0;Lb--)if(g[Lb]==ub.t)g.splice(Lb,1);for(var
Lb=0;Lb<g.length;Lb++)if(Lb>0&&g[Lb]==ub.u&&g[Lb-1]!=ub.u){g.splice(Lb-1,2);Lb=Lb-2;}if(Na&&g[0]!=null&&g[0].indexOf(ub.l)>=0)g.unshift(ub.t);};f.resolve=function(e){e=s.valueOf(e);if(this.v3==ub.m)return e;if(e.isAbsolute()||this.isOpaque())return e;if(e.fs&&!e.ks&&!e.RR&&!e.Bq&&!e.dp)return s.fromParts(this.RR,this.Fy,this.K4,this.Sw,this.ks,this.dp,e.fs);var
Va=this.RR;var
wb=e.dp;var
ob=e.fs;var
sa=null,Mb=null,ya=null,gb=null;if(e.Bq!=null){sa=e.Fy;Mb=e.K4;ya=e.Sw;gb=e.ks;}else{sa=this.Fy;Mb=this.K4;ya=this.Sw;if(e.ks.indexOf(ub.s)==0){gb=e.ks;}else{var
U=this.ks.split(ub.s);U.pop();U.push.apply(U,e.ks.split(ub.s));s.N0(U);gb=U.join(ub.s);}}return s.fromParts(Va,sa,Mb,ya,gb,wb,ob);};f.relativize=function(b){b=s.valueOf(b);if(this.v3==ub.m)return b;if(this.isOpaque()||b.isOpaque())return b;if(this.RR!=b.RR)return b;var
Hb=this.Bq!=null?this.Bq:ub.m;var
Sa=b.Bq!=null?b.Bq:ub.m;if(Hb!=Sa)return b;var
D=this.ks||ub.m;var
wa=b.ks||ub.m;var
cb=D.split(ub.s);cb.pop();var
C=wa.split(ub.s);var
wb=[];var
X=0;while(X<cb.length&&X<C.length){if(cb[X]!=C[X])break;X++;}var
ea=null;if(X<2&&D.indexOf(ub.s)==0){ea=wa;}else{for(var
Db=X;Db<cb.length;Db++)wb[wb.length]=ub.u;for(var
Db=X;Db<C.length;Db++)wb[wb.length]=C[Db];ea=wb.join(ub.s);}return s.fromParts(null,null,null,null,ea,b.dp,b.fs);};f.getAuthority=function(){return this.Bq;};f.getFragment=function(){return this.fs;};f.getHost=function(){return this.K4;};f.getPath=function(){return this.ks;};f.getPort=function(){return this.Sw;};f.getQuery=function(){return this.dp;};f.getQueryParam=function(g){var
u=this.dp;if(u){var
T=0;var
ba=g.length;var
mb=null;while((mb=u.indexOf(g,T))>=0){if(mb==0||u.charAt(mb-1)==ub.v){var
fa=u.charAt(mb+ba);if(fa==ub.v||jsx3.util.strEmpty(fa)){return true;}else if(fa==ub.w){var
Fb=u.indexOf(ub.v,mb+ba+1);return Fb>=0?u.substring(mb+ba+1,Fb):u.substring(mb+ba+1);}}T=mb+ba;}}return null;};f.getQueryParams=function(){var
da={};if(this.dp){var
Ma=this.dp.split(ub.v);for(var
K=0;K<Ma.length;K++){var
u=Ma[K];var
La=u.indexOf(ub.w);if(La>=0){da[u.substring(0,La)]=u.substring(La+1);}else da[u]=true;}}return da;};f.getScheme=function(){return this.RR;};f.getSchemeSpecificPart=function(){return this.GH;};f.getUserInfo=function(){return this.Fy;};f.isAbsolute=function(){return this.RR!=null;};f.isOpaque=function(){return this.RR!=null&&this.GH.indexOf(ub.s)!=0;};f.equals=function(p){if(this===p)return true;if(!(p instanceof jsx3.net.URI))return false;var
Wa=this.RR;var
ga=p.RR;if(Wa==null&&ga!=null||Wa!=null&&ga==null)return false;if(Wa!=null&&Wa.toLowerCase()!=ga.toLowerCase())return false;if(this.fs!=p.fs)return false;if(this.isOpaque()){if(!p.isOpaque())return false;return this.GH==p.GH;}else return this.ks==p.ks&&this.dp==p.dp&&this.Bq==p.Bq;};f.toString=function(){return this.v3;};s.encode=function(q,j){if(q==null)return null;if(j==null)j=s.B4;var
sa=new
RegExp(ub.g+j.replace(ub.x,ub.y)+ub.z);if(q.match(sa))return q;var
z=q.length;var
P=new
Array(z);for(var
C=0;C<z;C++){var
nb=q.charAt(C);if(j.indexOf(nb)<0){var
Ca=nb.charCodeAt(0);if(Ca<16){P[C]=ub.A+Ca.toString(16).toUpperCase();}else if(Ca<256){P[C]=ub.f+Ca.toString(16).toUpperCase();}else P[C]=nb;}else P[C]=nb;}return P.join(ub.m);};s.decode=function(k){if(k==null)return null;if(k.indexOf(ub.f)<0)return k;var
K=k.length;var
Oa=new
Array(K);var
P=0;for(var
ga=0;ga<k.length;ga++){var
aa=k.charAt(ga);if(aa==ub.f){var
Ua=k.substring(ga+1,ga+3);if(Ua.match(ub.B)){Oa[P++
]=aa;}else{Oa[P++
]=String.fromCharCode(parseInt(Ua,16));ga=ga+2;}}else Oa[P++
]=aa;}return Oa.join(ub.m);};s.valueOf=function(b){if(jsx3.util.strEmpty(b))return s.K8;return b instanceof s?b:new
s(b);};s.K8=new
s();});jsx3.Class.defineClass("jsx3.gui.Event",null,null,function(k,a){var
ub={k:"load",O:"subscribeLoseFocus ",ea:"KeyEvent",_:"",p:"mouseup",P:"... adding event listener to ",q:"mousewheel",V:"Handling event: ",v:"deregistering window ",ha:"@jsx3.gui.Event <empty>",I:" on ",a:"beforeunload",F:".",u:"registering window ",U:"Publishing event: ",j:"keyup",d:"click",z:"Subscribing to event ",S:"Unsubscribing from event ",fa:"DOMMouseScroll",D:"attaching event listener ",w:"Window ",K:"_focusHandler  lost focus:",R:"... removing event listener from ",aa:"mouse",g:"focus",B:/\s+/g,b:"blur",Q:"unsubscribeLoseFocus ",M:")",f:"error",ga:"onmousewheel",y:"on",T:"Unsubscribing all from event ",ba:"key",x:" not registered.",e:"dblclick",ja:":",c:"change",h:"keydown",W:"detaching event listener ",H:"setting event handler ",G:"adding event listener ",i:"keypress",ca:"KeyEvents",A:": ",t:"jsx3.util.Logger",N:"function",s:"resize",X:" from ",Z:"unsetting event handler ",o:"mouseover",r:"unload",C:" ",l:"mousedown",ia:"@jsx3.gui.Event ",da:"MouseEvent",L:" (",m:"mousemove",Y:"removing event listener ",J:"error subscribing to event ",n:"mouseout",E:" to "};k.BEFOREUNLOAD=ub.a;k.BLUR=ub.b;k.CHANGE=ub.c;k.CLICK=ub.d;k.DOUBLECLICK=ub.e;k.ERROR=ub.f;k.FOCUS=ub.g;k.KEYDOWN=ub.h;k.KEYPRESS=ub.i;k.KEYUP=ub.j;k.LOAD=ub.k;k.MOUSEDOWN=ub.l;k.MOUSEMOVE=ub.m;k.MOUSEOUT=ub.n;k.MOUSEOVER=ub.o;k.MOUSEUP=ub.p;k.MOUSEWHEEL=ub.q;k.UNLOAD=ub.r;k.RESIZE=ub.s;k.KEY_ALT=18;k.KEY_ARROW_DOWN=40;k.KEY_ARROW_LEFT=37;k.KEY_ARROW_RIGHT=39;k.KEY_ARROW_UP=38;k.KEY_BACKSPACE=8;k.KEY_CONTROL=17;k.KEY_DELETE=46;k.KEY_END=35;k.KEY_ENTER=13;k.KEY_ESCAPE=27;k.KEY_HOME=36;k.KEY_INSERT=45;k.KEY_META=-1;k.KEY_PAGE_DOWN=34;k.KEY_PAGE_UP=33;k.KEY_SHIFT=16;k.KEY_SPACE=32;k.KEY_TAB=9;k.KEY_0=48;k.KEY_9=57;k.KEY_A=65;k.KEY_Z=90;k.KEY_NP0=96;k.KEY_NP9=105;k.KEY_NPDIV=111;k.KEY_NPMUL=106;k.KEY_NPSUB=109;k.KEY_NPADD=107;k.KEY_NPDEC=110;k.KEY_F1=112;k.KEY_F15=126;k.cx=[];k.NC=[];k.wP=jsx3.util.EventDispatcher.jsxclass.newInnerClass();k.LS=[];var
y=null;k.tZ=function(){if(k._LOG==null)if(jsx3.Class.forName(ub.t)!=null){y=jsx3.util.Logger;k._LOG=y.getLogger(k.jsxclass.getName());}return k._LOG;};k._registerWindow=function(h){var
Eb=k.tZ();if(Eb!=null&&Eb.isLoggable(5))Eb.debug(ub.u+h.name);k.cx.push(h);k.NC.push({});k.LS.push({});};k._deregisterWindow=function(d){var
J=jsx3.util.arrIndexOf(k.cx,d);if(J>=0){var
ua=k.tZ();if(ua!=null&&ua.isLoggable(5))ua.debug(ub.v+d.name);k.cx.splice(J,1);k.NC.splice(J,1);k.LS.splice(J,1);}else throw new
jsx3.Exception(ub.w+d+ub.x);};k._registerWindow(window);k.subscribe=function(h,b,s){k.wP.subscribe(h,b,s);var
L=ub.y+h;var
La=k.tZ();if(La!=null&&La.isLoggable(5))La.debug(ub.z+h+ub.A+b.toString().substring(0,50).replace(ub.B,ub.C));for(var
Ya=0;Ya<k.cx.length;Ya++)try{var
x=k.cx[Ya];var
ob=k.gG(x,h);if(ob.attachEvent&&k.cW(h)){var
Oa=k.LS[Ya];if(!Oa[h]){if(La!=null&&La.isLoggable(5))La.debug(ub.D+h+ub.E+ob+ub.F);ob.attachEvent(L,k.VR);Oa[h]=true;}}else if(ob.addEventListener&&k.l1(h)){var
Oa=k.LS[Ya];if(!Oa[h]){if(La!=null&&La.isLoggable(5))La.debug(ub.G+h+ub.E+ob+ub.F);ob.addEventListener(h,k.VR,false);Oa[h]=true;}}else{var
ab=k.NC[Ya];if(ob[L]!=k.VR){if(La!=null&&La.isLoggable(5))La.debug(ub.H+L+ub.I+ob+ub.F);if(ob[L]!=null)ab[L]=ob[L];ob[L]=k.VR;}}}catch(Kb){if(La!=null&&La.isLoggable(5))La.debug(ub.J+h,jsx3.NativeError.wrap(Kb));k._deregisterWindow(x);Ya--;}};k.Er=[];k.SY=function(r){var
Eb=k.wrap(r);var
ta=Eb.srcElement();var
cb=k.Er.concat();var
L=k.tZ();L4:for(var
t=0;t<cb.length;t++){var
da=cb[t];var
ea=da[0];var
kb=da[1];var
sb=da[2];var
Ab=ta;if(kb.containsHtmlElement){if(kb.containsHtmlElement(ta))continue;}else while(Ab!=null){if(Ab==kb)continue L4;Ab=Ab.parentNode;}if(L!=null&&L.isLoggable(5))L.debug(ub.K+kb+ub.L+ea+ub.M);var
Qa={target:k,event:Eb};if(typeof sb==ub.N)sb.call(ea,Qa);else ea[sb](Qa);}};k.subscribeLoseFocus=function(g,b,m){var
Na=k.tZ();if(Na!=null&&Na.isLoggable(5))Na.debug(ub.O+g+ub.C+b);k.Er.push([g,b,m]);if(k.Er.length==1){var
K=b.ownerDocument||b.getDocument();if(Na!=null&&Na.isLoggable(5))Na.debug(ub.P+K);K.addEventListener(ub.g,k.SY,true);}};k.unsubscribeLoseFocus=function(g){var
V=k.tZ();if(V!=null&&V.isLoggable(5))V.debug(ub.Q+g);var
B=null;for(var
cb=0;cb<k.Er.length;cb++)if(k.Er[cb][0]==g){var
Xa=k.Er[cb][1];B=Xa.ownerDocument||Xa.getDocument();k.Er.splice(cb--
,1);}if(k.Er.length==0&&B!=null){if(V!=null&&V.isLoggable(5))V.debug(ub.R+B);B.removeEventListener(ub.g,k.SY,true);}};k.preventSelection=function(f){};k.unsubscribe=function(e,l){var
lb=k.tZ();if(lb!=null&&lb.isLoggable(5))lb.debug(ub.S+e+ub.A+l.toString().substring(0,50).replace(ub.B,ub.C));k.wP.unsubscribe(e,l);if(k.wP.getSubscriberCount(e)==0)k.ku(e);};k.unsubscribeAll=function(l){var
_a=k.tZ();if(_a!=null&&_a.isLoggable(5))_a.debug(ub.T+l+ub.F);k.wP.unsubscribeAll(l);k.ku(l);};k.publish=function(f){var
Db={subject:f.getType(),target:k,event:f};k.bw(Db);};k.bw=function(o){var
t=ub.y+o.subject.toLowerCase();var
fa=k.tZ();if(fa!=null&&fa.isLoggable(6))fa.trace(ub.U+t+ub.F);var
xa=k.NC[0];if(xa[t]!=null)xa[t]();k.wP.publish(o);};k.VR=function(c){var
Fa=new
k(c!=null?c:window.event);var
H=k.tZ();if(H!=null&&H.isLoggable(6))H.trace(ub.V+Fa.getType()+ub.F);var
N={subject:Fa.getType(),target:k,event:Fa};k.bw(N);if(N.returnValue)return N.returnValue;};k.ku=function(c){var
R=ub.y+c;var
A=k.tZ();for(var
fa=0;fa<k.cx.length;fa++)try{var
w=k.cx[fa];var
Ja=k.gG(w,c);if(Ja.attachEvent&&k.cW(c)){var
Za=k.LS[fa];if(Za[c]){if(A!=null&&A.isLoggable(5))A.debug(ub.W+c+ub.X+Ja);Ja.detachEvent(R,k.VR);Za[c]=false;}}else if(Ja.removeEventListener&&k.l1(c)){var
Za=k.LS[fa];if(Za[c]){if(A!=null&&A.isLoggable(5))A.debug(ub.Y+c+ub.X+Ja+ub.F);Ja.removeEventListener(c,k.VR,false);Za[c]=false;}}else{var
za=k.NC[fa];if(A!=null&&A.isLoggable(5))A.debug(ub.Z+R+ub.I+Ja+ub.F);if(za[R]!=null){Ja[R]=za[R];delete za[R];}else Ja[R]=null;}}catch(Kb){if(A!=null&&A.isLoggable(5))A.debug(ub.J+c,jsx3.NativeError.wrap(Kb));k._deregisterWindow(w);fa--;}};k.gG=function(n,m){return m==ub.a||m==ub.r||m==ub.s?n:n.document;};k.cW=function(j){return j!=ub.a;};k.l1=function(s){return s!=ub.a;};a.init=function(o,i){this.KP=o;if(i)this._clone=jsx3.clone(o);};a.sf=function(){if(this._clone==null)this._clone=jsx3.clone(this.KP);this._clone._jsxh1=true;};k.wrap=function(s,r){return s instanceof k?s:new
k(s,r);};k.getCurrent=function(b){return window.event?new
k(window.event,b):null;};a.EH=function(){try{if(this.KP==null)return this._clone;else if(this.KP.currentTarget!=null)return this.KP;}catch(Kb){}this.KP=null;return this._clone;};a.event=function(){return this.EH();};a.getType=function(){return this.EH().type;};a.srcElement=function(){var
K=this.EH();return K.target||K.srcElement;};a.toElement=function(){var
Xa=this.EH();return Xa.type==ub.n?Xa.relatedTarget:Xa.target;};a.fromElement=function(){var
xb=this.EH();return xb.type==ub.o?xb.relatedTarget:xb.target;};a.isMouseEvent=function(){var
na=this.getType()||ub._;return na.indexOf(ub.aa)==0||na==ub.d||na==ub.e;};a.isKeyEvent=function(){return (this.getType()||ub._).indexOf(ub.ba)==0;};a.setCapture=function(e){};a.releaseCapture=function(g){};a.keyCode=function(){var
Cb=this.EH();return Cb.keyCode;};a.clientX=function(){var
lb=this.EH();return lb?lb.clientX:Number.NaN;};a.clientY=function(){var
ma=this.EH();return ma?ma.clientY:Number.NaN;};a.getOffsetX=function(){var
Za=this.EH().target;var
ea=this.EH().clientX;return ea-jsx3.html.getRelativePosition(Za.ownerDocument.body,Za).L;};a.getOffsetY=function(){var
Mb=this.EH().target;var
Gb=this.EH().clientY;return Gb-jsx3.html.getRelativePosition(Mb.ownerDocument.body,Mb).T;};a.getScreenX=function(){return this.EH().screenX;};a.getScreenY=function(){return this.EH().screenY;};a.getTrueX=function(){return this.EH().clientX;};a.getTrueY=function(){return this.EH().clientY;};a.getWheelDelta=function(){var
nb=-1*this.EH().detail;return nb;};a.shiftKey=function(){var
J=this.EH();return J.shiftKey;};a.ctrlKey=function(){var
Aa=this.EH();return Aa.ctrlKey;};a.altKey=function(){var
Ja=this.EH();return Ja.altKey;};a.metaKey=function(){var
eb=this.EH();return eb.metaKey;};a.enterKey=function(){return this.EH().keyCode==13;};a.spaceKey=function(){return this.EH().keyCode==32;};a.tabKey=function(){return this.EH().keyCode==9;};a.rightArrow=function(){return this.EH().keyCode==39;};a.leftArrow=function(){return this.EH().keyCode==37;};a.upArrow=function(){return this.EH().keyCode==38;};a.downArrow=function(){return this.EH().keyCode==40;};a.deleteKey=function(){return this.EH().keyCode==46;};a.backspaceKey=function(){return this.EH().keyCode==8;};a.insertKey=function(){return this.EH().keyCode==45;};a.homeKey=function(){return this.EH().keyCode==36;};a.endKey=function(){return this.EH().keyCode==35;};a.pageUpKey=function(){return this.EH().keyCode==33;};a.pageDownKey=function(){return this.EH().keyCode==34;};a.escapeKey=function(){return this.EH().keyCode==27;};a.exists=function(){return this.EH()!=null;};a.cancelBubble=function(){var
P=this.EH();P._jsxRZ=true;if(!P._jsxh1)P.stopPropagation();};a.cancelReturn=function(){this.EH().returnValue=false;};a.cancelKey=function(){var
La=this.EH();if(!La._jsxh1){La.stopPropagation();La.preventDefault();}};a.cancelAll=function(){this.cancelBubble();this.cancelKey();this.cancelReturn();};a.leftButton=function(){var
Ka=this.EH();if(jsx3.app.Browser.macosx&&Ka.ctrlKey)return false;var
x=this.getType();if(x==ub.l||x==ub.p){return Ka.button==0;}else if(x==ub.d||x==ub.e)return Ka.button==0;return false;};a.rightButton=function(){var
na=this.EH();var
vb=this.getType();if(vb==ub.l||vb==ub.p)return na.button==2||jsx3.app.Browser.macosx&&na.ctrlKey;else return false;};a.button=function(){var
Ba=this.EH();return Ba.button;};a.setReturn=function(o){this.EH().returnValue=o;};a.setKeyCode=function(q){var
ua=this.EH();if(ua.charCode==13){var
Qa=this.getDocument().createEvent(ub.ca);Qa.initKeyEvent(ub.i,true,true,this.getDocument().defaultView,ua.ctrlKey(),ua.altKey(),ua.shiftKey(),false,0,q);ua.preventDefault();ua.target.dispatchEvent(Qa);}};a.isModifierKey=function(){var
K=this.EH();return K.keyCode==16||K.keyCode==17||K.keyCode==18||K.keyCode==-1;};a.hasModifier=function(d){return !d&&this.shiftKey()||this.ctrlKey()||this.altKey()||this.metaKey();};a.isArrowKey=function(){var
Qa=this.keyCode();return Qa>=37&&Qa<=40;};a.isFunctionKey=function(){var
t=this.keyCode();return t>=112&&t<=126;};a.getAttribute=function(q){return this.EH()[q];};a.setAttribute=function(r,p){this.EH()[r]=p;};a.removeAttribute=function(b){this.EH()[b]=null;};k.dispatchMouseEvent=function(e,h,b){var
jb=document.createEvent(ub.da);jb.initMouseEvent(h,true,true,window,0,0,0,0,0,false,false,false,false,0,e);if(b)for(var ab in b)jb[ab]=b[ab];e.dispatchEvent(jb);};k.dispatchKeyEvent=function(j,g,i,f,q,c,n){var
Mb=document.createEvent(ub.ea);Mb.initMouseEvent(g,true,true,window,0,0,0,0,0,false,q,c,f,0,j);Mb.keyCode=i;if(n)for(var Db in n)Mb[Db]=n[Db];j.dispatchEvent(Mb);};window.addEventListener(ub.fa,function(o){var
tb=o.target||o.srcElement;while(tb!=null&&!o._jsxRZ){if(tb.getAttribute){var
V=tb.getAttribute(ub.ga);if(V)jsx3.eval.call(tb,V,{event:o});}tb=tb.parentNode;}},false);a.isFakeOut=function(m){if(this.getType()==ub.n){var
Ka=this.toElement();try{while(Ka!=null){if(Ka==m)return true;Ka=Ka.parentNode;}}catch(Kb){return false;}}return false;};a.isFakeOver=function(l){if(this.getType()==ub.o){var
Jb=this.fromElement();try{while(Jb!=null){if(Jb==l)return true;Jb=Jb.parentNode;}}catch(Kb){return false;}}return false;};a.toString=function(){var
Ib=this.EH();if(Ib==null)return ub.ha;var
Aa=[];for(var P in Ib)if(typeof Ib[P]!=ub.N)Aa[Aa.length]=P;Aa.sort();var
ab=[ub.ia];for(var
qa=0;qa<Aa.length;qa++)ab[ab.length]=Aa[qa]+ub.ja+Ib[Aa[qa]]+ub.C;return ab.join(ub._);};});jsx3.gui.Event.subscribe("unload",jsx3.destroy);jsx3.Class.defineClass("jsx3.app.Browser",null,null,function(a,s){var
ub={d:"other",i:"Mac OS X",a:"win32",h:"Linux",c:"macosx",f:"_",g:"Win",b:"linux",e:/[_-]/};a.WIN32=ub.a;a.LINUX=ub.b;a.MACOSX=ub.c;a.OTHER=ub.d;a.getLocaleString=function(){var
Eb=window.navigator.language;var
Aa=Eb.split(ub.e);if(Aa.length>0){Aa[0]=Aa[0].toLowerCase();if(Aa.length>1)Aa[1]=Aa[1].toUpperCase();}return Aa.join(ub.f);};a.isTableMoveBroken=function(h){return true;};a.tP=null;a.getStyleClass=function(q){if(a.tP==null){a.tP={};for(var
Ga=0;Ga<document.styleSheets.length;Ga++){var
sa=document.styleSheets[Ga];for(var
Ab=0;Ab<sa.cssRules.length;Ab++){var
S=sa.cssRules[Ab];a.tP[S.selectorText]=S.style;}}}return a.tP[q];};a.getLocation=function(){if(a.VF==null)a.VF=new
jsx3.net.URI(window.location.href);return a.VF;};a.getSystem=function(){if(a.Rx==null){var
v=navigator.platform;var
E=navigator.userAgent;var
Oa=ub.d;if(v.indexOf(ub.g)==0)Oa=ub.a;else if(v.indexOf(ub.h)==0)Oa=ub.b;else if(E.indexOf(ub.i)!=-1)Oa=ub.c;a.Rx=Oa;}return a.Rx;};a[a.getSystem()]=true;});jsx3.Class.defineClass("jsx3.app.Settings",null,null,function(i,s){var
ub={k:"undefined",h:"sets.error",c:"JSX_SETTINGS",p:"/record[@jsxid='",q:"']",v:"number",i:'<data jsxid="jsxroot"/>',A:"map",a:"JSX/settings.xml",u:"jsxid",t:"type",j:"sets.no_url",s:"./record",z:"data",d:"object",o:"/data",w:"boolean",r:"_cache",l:"./record[@type='map' and record[@jsxid='",g:"intDomain",b:"/config.xml",m:"' and .='",f:"_addin_",y:"null",n:"']]",x:"true",e:"string"};i.DOMAIN_SYSTEM=1;i.DOMAIN_PROJECT=2;i.DOMAIN_ADDIN=3;i.uI=ub.a;i.PATH_PROJECT=ub.b;i.qu=ub.b;i.P5=ub.c;s.init=function(g,d){var
Ha=null;var
ha=null;var
Ma=null;if(g instanceof jsx3.xml.Document){this.Qg(null,null,g.getSourceURL(),g);return;}else if(g==1){Ha=jsx3.getSystemCache();ha=i.P5;Ma=i.uI;}else if(g==2){if(typeof d==ub.d){Ha=d.getCache();ha=i.P5;Ma=d.getAppPath()+i.PATH_PROJECT;}else if(typeof d==ub.e)Ma=d+i.PATH_PROJECT;}else if(g==3){Ha=jsx3.getSystemCache();var
Pa=d instanceof jsx3.app.AddIn?d.getPath():jsx3.System.addinKeyToPath(d);ha=i.P5+ub.f+Pa;Ma=Pa+i.qu;}else throw new
jsx3.IllegalArgumentException(ub.g,g);this.Qg(Ha,ha,Ma);};s.Qg=function(r,d,q,f){if(r!=null)f=r.getDocument(d,true);if(f==null){f=new
jsx3.xml.Document();if(q!=null){this.ii(f,q);if(f.hasError()){jsx3.util.Logger.GLOBAL.warn(jsx3._msg(ub.h,q,f.getError()));f=(new
jsx3.xml.Document()).loadXML(ub.i);}}else{jsx3.util.Logger.GLOBAL.warn(jsx3._msg(ub.j));f=(new
jsx3.xml.Document()).loadXML(ub.i);}if(r!=null)r.setDocument(d,f);}this.ks=q;this.gg=f;this.Hg=f.getRootNode();this.vg=r;this.rc=d;if(r!=null)this.Di=r.getTimestamp(d);};s.ii=function(b,h){b.load(h);};s.get=function(a){var
w=this.m8(arguments);if(typeof w==ub.k){var
yb=this.getNode.apply(this,arguments);if(yb==null)return null;w=i.az(yb);this.Et(w,arguments);}return w;};s.getMapInArrayByField=function(a,k,r){var
rb=[];for(var
_=0;_<arguments.length-2;_++)rb.push(arguments[0]);k=arguments[arguments.length-2];r=arguments[arguments.length-1];var
ba=this.getNode.apply(this,rb);if(ba){var
Ja=ba.selectSingleNode(ub.l+k+ub.m+r+ub.n);if(Ja!=null)return i.az(Ja);}return null;};s.getMapsInArrayByField=function(j,a,r){var
A=[];for(var
x=0;x<arguments.length-2;x++)A.push(arguments[0]);a=arguments[arguments.length-2];r=arguments[arguments.length-1];var
z=this.getNode.apply(this,A);if(z){var
qa=z.selectNodes(ub.l+a+ub.m+r+ub.n);return qa.map(function(c){return i.az(c);}).toArray(true);}return [];};s.getNode=function(f){var
ja=this.Hg;var
K=ub.o;for(var
S=0;ja!=null&&S<arguments.length;S++)K=K+(ub.p+arguments[S]+ub.q);return ja.selectSingleNode(K);};s.Et=function(m,k){if(k.length==0){this._cache=m;}else{if(this._cache==null)this._cache={_jsxQO:true};var
Ea=this._cache;for(var
Ia=0;Ia<k.length-1;Ia++){var
L=k[Ia];if(Ea[L]==null)Ea[L]={_jsxQO:true};Ea=Ea[L];}Ea[k[k.length-1]]=m;}};s.m8=function(g){if(this.vg){var
sa=this.vg.getTimestamp(this.rc);if(sa>this.Di){this.Th();this.Di=sa;return;}}var
fa=this._cache;for(var
ab=0;fa!=null&&ab<g.length;ab++)fa=fa[g[ab]];return fa!=null&&fa._jsxQO?i.UNDEF:fa;};s.Th=function(){delete this[ub.r];};i.I1={array:function(m){var
B=m.selectNodeIterator(ub.s);var
u=[];while(B.hasNext()){var
Ta=B.next();var
Cb=i.I1[Ta.getAttribute(ub.t)];u[u.length]=Cb?Cb(Ta):Ta.getValue();}return u;},map:function(q){var
E=q.selectNodeIterator(ub.s);var
Na={};while(E.hasNext()){var
Aa=E.next();var
G=i.I1[Aa.getAttribute(ub.t)];Na[Aa.getAttribute(ub.u)]=G?G(Aa):Aa.getValue();}return Na;}};i.I1[ub.v]=function(d){return Number(d.getValue());};i.I1[ub.w]=function(o){return o.getValue()===ub.x;};i.I1[ub.y]=function(h){return null;};i.I1[ub.e]=function(f){return f.getValue();};i.az=function(q){var
P=q.getNodeName()==ub.z?ub.A:q.getAttribute(ub.t);var
Z=i.I1[P];return Z!=null?Z(q):q.getValue();};s.toString=function(){return this.jsxsuper()+this.ks;};});jsx3.Settings=jsx3.app.Settings;jsx3.Class.defineClass("jsx3.xml.Entity",null,null,function(m,a){var
ub={o:"jsx3.xml.Template",d:"",k:"object",r:"[",c:"xml.err_append",h:'"',p:"transformToObject",g:'="',l:"<foo ",q:"transform",b:"xml.clone_tp",v:"3.0.0",i:"xml.str_err",m:"/>",a:"xml.wrap_type",u:" ",f:"@",t:"undefined",j:"xml.str_empty",n:"text/xml",s:"]",e:":"};m.TYPEELEMENT=1;m.TYPEATTRIBUTE=2;m.TYPETEXT=3;m.TYPECDATA=4;m.TYPECOMMENT=8;m.eD={1:true,2:true,3:true,4:true,7:true,8:true};a.init=function(p){this.bW=p;this.fA=p.nodeType;if(!m.eD[this.fA]){this.pk(300,jsx3._msg(ub.a,this.fA));}else if(this.ex)this.pk(0);};a.createNode=function(d,k,n){var
qb=this.Kt();var
C=null;if(n==null)n=null;if(d==2){C=qb.createAttributeNS(n,k);}else if(d==3){C=qb.createTextNode(k);}else if(d==4){C=qb.createCDATASection(k);}else if(d==8){C=qb.createComment(k);}else C=qb.createElementNS(n,k);return new
m(C);};a.cloneNode=function(p){if(this.fA==1){var
K=this.bW.cloneNode(p);return new
m(K);}else this.pk(301,jsx3._msg(ub.b,this.fA));};a.appendChild=function(l){var
Ga=l.bW;if(this.bW!=null&&Ga!=null&&this.fA==1){this.bW.appendChild(Ga);}else this.pk(302,jsx3._msg(ub.c,l));return this;};a.insertBefore=function(k,s){if(s==null){if(this.fA==1){this.appendChild(k);return k;}}else{var
ja=k.bW;if(this.fA==1&&s.fA!=2&&k.fA!=2)if(s.getParent()!=null&&s.getParent().equals(this)){var
pb=new
m(this.bW.insertBefore(ja,s.bW));return pb;}return null;}};a.replaceNode=function(h,c){var
w=h.bW;if(this.fA==1&&c.fA==1&&h.fA==1)if(c.getParent()!=null&&c.getParent().equals(this)&&c.getParent()!=null&&c.getParent().equals(this)){var
Oa=new
m(this.bW.replaceChild(w,c.bW));return Oa;}return null;};a.setAttribute=function(n,h){if(h!=null)this.bW.setAttribute(n,String(h));else this.removeAttribute(n);return this;};a.getAttribute=function(j){return this.bW?this.bW.getAttribute(j):null;};a.getAttributeNode=function(j){if(this.bW!=null&&this.fA==1){var
S=this.bW.getAttributeNode(j);if(S!=null)return new
m(S);}};a.setAttributeNode=function(k){var
C=k.bW;this.bW.setAttributeNodeNS(C);return this;};a.getAttributes=function(){if(this.bW!=null&&this.fA==1)return new
m.List(this.bW.attributes);else return null;};a.getAttributeNames=function(){var
Ya=this.bW.attributes;var
rb=new
Array(Ya.length);for(var
ca=0;ca<rb.length;ca++)rb[ca]=Ya[ca].nodeName;return rb;};a.getRootNode=function(){return this.bW?new
m(this.Kt(1)):null;};a.getParent=function(){return this.bW!=this.Kt(1)?new
m(this.bW.parentNode):null;};a.getChildIterator=function(q){return new
m.ChildIterator(this.bW?this.bW.childNodes:[],q);};a.getChildNodes=function(g){if(!this.bW)return new
m.List([]);var
U=this.bW.childNodes;var
ta=[];for(var
sb=0;sb<U.length;sb++){var
Za=U[sb];if(Za.nodeType==1||g&&(Za.nodeType==3||Za.nodeType==4))ta[ta.length]=Za;}return new
m.List(ta);};a.removeChild=function(f){var
I=f.getParent();return I!=null&&I.equals(this)?new
m(this.bW.removeChild(f.bW)):null;};a.removeChildren=function(){var
Mb=this.bW.childNodes;for(var
Oa=Mb.length-1;Oa>=0;Oa--)this.bW.removeChild(Mb[Oa]);};a.removeAttribute=function(e){if(this.fA==1)this.bW.removeAttribute(e);};a.removeAttributeNode=function(l){this.bW.removeAttributeNode(l.bW);return this;};a.equals=function(f){return f.bW==this.bW;};a.getNodeType=function(){return this.fA;};a.getNodeName=function(){return this.bW.nodeName;};a.getNamespaceURI=function(){var
Xa=this.bW.namespaceURI;if(Xa==null)Xa=ub.d;return Xa;};a.selectSingleNode=function(c,i){if(!this.bW)return null;return this.rt(c,i,0);};a.selectNodes=function(o,p){if(!this.bW)return new
m.List([]);return this.rt(o,p,1);};a.selectNodeIterator=function(e,r){if(!this.bW)return new
m.SelectIterator();return this.rt(e,r,2);};a.getBaseName=function(){var
Na=this.getNodeName();var
Ua=Na.indexOf(ub.e);return Ua>=0?Na.substring(Ua+1):Na;};a.getPrefix=function(){var
db=this.getNodeName();var
H=db.indexOf(ub.e);return H>=0?db.substring(0,H):ub.d;};a.getXML=function(){return this.toString();};a.toString=function(){var
Db=ub.f+this.getClass().getName();if(this.bW!=null&&!this.hasError()){if(this.getNodeType()==2){return this.getNodeName()+ub.g+this.getValue()+ub.h;}else return (new
XMLSerializer()).serializeToString(this.bW);}else return this.hasError()?jsx3._msg(ub.i,Db,this.getError()):jsx3._msg(ub.j,Db);};a.getValue=function(){if(this.fA==1){var
_=new
Array(this.bW.childNodes.length);for(var
pb=0;pb<this.bW.childNodes.length;pb++){var
jb=this.bW.childNodes[pb];if(jb.nodeType==3||jb.nodeType==4)_[pb]=jb.nodeValue;else _[pb]=jb.textContent;}return _.join(ub.d);}else return this.bW.nodeValue;};a.setValue=function(e){if(e==null)e=ub.d;if(this.fA==1){this.removeChildren();this.appendChild(this.createNode(3,e));}else this.bW.nodeValue=e;return this;};m.HX=new
XPathEvaluator();m.ZC=[XPathResult.FIRST_ORDERED_NODE_TYPE,XPathResult.ORDERED_NODE_ITERATOR_TYPE,XPathResult.ORDERED_NODE_SNAPSHOT_TYPE];a.rt=function(n,q,g){if(typeof q==ub.k)q=jsx3.xml.Document.Ym(q);var
Ta=this.bW.ownerDocument;var
tb=Ta.documentElement;var
wa=q?m.Ug(q,tb):m.g4(Ta)||m.HX.createNSResolver(tb);var
Aa=null;try{Aa=m.HX.evaluate(n,this.bW,wa,m.ZC[g],null);}catch(Kb){}if(g==1){var
D=null;var
Ea=[];if(Aa)while(D=Aa.iterateNext())Ea[Ea.length]=D;return new
m.List(Ea);}else if(g==2){return new
m.SelectIterator(Aa);}else return Aa&&Aa.singleNodeValue?new
m(Aa.singleNodeValue):null;};m.g4=function(e){if(!e._jsxBn)if(e._jsxie)e._jsxBn=m.Ug(e._jsxie);return e._jsxBn;};m.Ug=function(g){var
ca=(new
DOMParser()).parseFromString(ub.l+g+ub.m,ub.n);return m.HX.createNSResolver(ca.documentElement);};a.Kt=function(g){if(g==null){return this.bW.ownerDocument;}else if(g==1){var
wa=this.bW.ownerDocument;return wa!=null?wa.documentElement:null;}else if(g==2)return this.bW.documentElement;};a.getNative=function(){return this.bW;};a.getFirstChild=function(){if(this.fA==1){var
w=this.bW.firstChild;while(w!=null&&w.nodeType!=1)w=w.nextSibling;if(w!=null)return new
m(w);}return null;};a.getLastChild=function(){if(this.fA==1){var
I=this.bW.lastChild;while(I!=null&&I.nodeType!=1)I=I.previousSibling;if(I!=null)return new
m(I);}return null;};a.getPreviousSibling=function(){if(this.fA==1){var
Ib=this.bW.previousSibling;while(Ib!=null&&Ib.nodeType!=1)Ib=Ib.previousSibling;if(Ib!=null)return new
m(Ib);}return null;};a.getNextSibling=function(){if(this.fA==1){var
yb=this.bW.nextSibling;while(yb!=null&&yb.nodeType!=1)yb=yb.nextSibling;if(yb!=null)return new
m(yb);}return null;};a.transformNode=function(b,r,p){jsx3.require(ub.o);var
N=new
jsx3.xml.Template(b);if(r)N.setParams(r);return N[p?ub.p:ub.q](this);};m.QF=function(){};m.QF.prototype.toString=function(){return ub.r+this.code+ub.s+(typeof this.description!=ub.t?ub.u+this.description:ub.d);};a.pk=function(p,o){if(this.ex==null)this.ex=new
m.QF();this.ex.code=p;this.ex.description=o;};a.getError=function(){if(!this.ex)this.pk(0);return this.ex;};a.hasError=function(){return this.ex!=null&&this.ex.code!=0;};a.getOwnerDocument=function(){return this.bW?new
jsx3.xml.Document(this.Kt()):null;};m.getVersion=function(){return ub.v;};});jsx3.Entity=jsx3.xml.Entity;jsx3.Class.defineClass("jsx3.xml.Entity.List",jsx3.util.List,null,function(m,o){var
ub={a:"Not implemented",c:"]",b:"["};var
Ja=jsx3.Exception;o.init=function(p){this.jsxsuper(null,true);this.rf=p;};o.get=function(j){var
_=this.rf[j];return _!=null?new
jsx3.xml.Entity(_):_;};var
sb=ub.a;o.add=function(){throw new
Ja(sb);};o.addAll=function(){throw new
Ja(sb);};o.set=function(){throw new
Ja(sb);};o.remove=function(){throw new
Ja(sb);};o.removeAt=function(){throw new
Ja(sb);};o.sort=function(){throw new
Ja(sb);};o.slice=function(r,f){return new
m(arguments.length>1?this.rf.slice(r,f):this.rf.slice(r));};o.toString=function(){return ub.b+this.toArray()+ub.c;};o.clone=function(){return new
m(this.rf.concat());};o.toArray=function(){var
tb=this.size();var
Ka=new
Array(tb);for(var
xa=0;xa<tb;xa++)Ka[xa]=this.get(xa);return Ka;};});jsx3.Class.defineClass("jsx3.xml.Entity.ChildIterator",null,[jsx3.util.Iterator],function(m,q){q.init=function(r,j){this.vt=r;this.Fx=0;this.oM=j;this.S0();this.bW=null;};q.next=function(){if(!this._next)return null;if(this.bW){this.bW.init(this._next);}else this.bW=new
jsx3.xml.Entity(this._next);this.S0();return this.bW;};q.hasNext=function(){return this._next!=null;};q.S0=function(){this._next=null;var
jb=this.vt;var
Z=jb.length;while(this._next==null&&this.Fx<Z){var
kb=jb[this.Fx];if(kb.nodeType==1||this.oM&&(kb.nodeType==3||kb.nodeType==4))this._next=kb;this.Fx++;}};});jsx3.Class.defineClass("jsx3.xml.Entity.SelectIterator",null,[jsx3.util.Iterator],function(e,i){i.init=function(g){this.mP=g;this.dX=0;this.bW=null;};i.next=function(){var
v=this.mP.snapshotItem(this.dX++);if(!v)return null;if(this.bW){this.bW.init(v);}else this.bW=new
jsx3.xml.Entity(v);return this.bW;};i.hasNext=function(){return this.mP&&this.dX<this.mP.snapshotLength;};});jsx3.Collection=jsx3.xml.Entity.List;jsx3.util.Collection=jsx3.Collection;jsx3.Class.defineClass("jsx3.xml.Document",jsx3.xml.Entity,[jsx3.util.EventDispatcher],function(e,m){var
ub={k:"xml.doc_load",O:"xml",_:"undefined",p:"text/xml",P:"<!-- ",q:"_jsxclonedoc",V:"'",v:"xml.unknown",I:'"',a:"response",F:"1.0",u:"mA",U:"='",j:"UniversalBrowserRead",d:"http://xsd.tns.tibco.com/gi/cxf/2006",z:"xml.err_fmt",S:"XPath",D:/[\n\r]/g,w:"xml.timeout",K:' standalone="',R:"\n",aa:"jsx",g:"",B:"parsererror",b:"error",Q:" -->",M:"no",f:/xmlns:([^=]*)=['\"]([^\"^']*)['\"]/g,y:"0",T:"xmlns:",ba:"attribute::* | child::*",x:"GET",e:"jsx_xmlns",c:"timeout",h:"xml.parser",W:"object",H:' version="',G:"<?xml",i:"MV",ca:"3.0.0",A:"xml.doc_bad_ex",t:"xml.doc_status",N:"?>\n",s:"_jsxqt",X:"([^=]*)=['\"]([^\"^']*)['\"]",Z:":",o:"load sync",r:"xml.doc_bad",C:/^http:\/\/www\.mozilla\.org\/(.+\/)?parsererror.xml/i,l:"file",L:"yes",m:"load async",Y:"g",J:' encoding="',n:"load",E:" "};var
Ma=jsx3.xml.Entity;e.ON_RESPONSE=ub.a;e.ON_ERROR=ub.b;e.ON_TIMEOUT=ub.c;e.SEARCHABLE_NAMESPACE=ub.d;e.SEARCHABLE_PREFIX=ub.e;e.DU=ub.f;e.VY=(new
jsx3.net.URI(window.location.href)).getHost()!=document.domain;m.init=function(j){if(j==null){try{this.MV=window.document.implementation.createDocument(ub.g,ub.g,null);}catch(Kb){this.pk(101,jsx3._msg(ub.h,jsx3.NativeError.wrap(Kb)));delete this[ub.i];}}else{this.MV=j;this.jsxsuper(j.documentElement);}};m.load=function(s,g){this.SN=s.toString();var
la=jsx3.net.URIResolver.DEFAULT.resolveURI(s);this.pk(0);var
xa=this.getAsync()?true:false;if(e.VY){this.rB(la,g,xa);return this;}this.MV.async=xa;if(this.getAsync()){jsx3.sleep(function(){if(g>0)this._jsxvX=window.setTimeout(jsx3.makeCallback(function(){this.Bs();},this),g);this.rC(la,g,true);if(this.hasError()){this.JS();this.publish({subject:ub.b});}},null,this);}else this.rC(la,null,false);return this;};m.ap=function(c){Ma.prototype.init.call(this,c);};e.nA=netscape.security.PrivilegeManager.enablePrivilege;e.VH=ub.j;m.rC=function(g,p,b){e.R3(6,jsx3._msg(ub.k,g));try{if(g.getScheme()&&g.getScheme()!=ub.l){this._jsxclonedoc=true;e.nA(e.VH);}var
Ka=new
jsx3.util.Timer(e.jsxclass,g);if(b){this._jsxqt=jsx3.makeCallback(function(){Ka.log(ub.m);this.g1();},this);this.MV.addEventListener(ub.n,this._jsxqt,false);}this.MV.load(g.toString());if(!b){this.Nt();Ka.log(ub.o);if(!this.B5(this.MV))this.ap(this.MV.documentElement);}}catch(Kb){this.B5(this.MV,jsx3.NativeError.wrap(Kb));}};m.loadXML=function(i){this.SN=null;this.pk(0);try{this.MV=(new
DOMParser()).parseFromString(i,ub.p);if(!this.B5(this.MV))this.ap(this.MV.documentElement);}catch(Kb){this.B5(this.MV,jsx3.NativeError.wrap(Kb));}return this;};m.Nt=function(){if(this._jsxclonedoc){delete this[ub.q];try{e.nA(e.VH);}catch(Kb){}var
pa=(new
XMLSerializer()).serializeToString(this.MV);this.MV=(new
DOMParser()).parseFromString(pa,ub.p);}};m.g1=function(){this.JS();this.Nt();if(this.B5(this.MV)){this.publish({subject:ub.b});}else if(this.MV.documentElement==null){this.pk(105,jsx3._msg(ub.r));this.publish({subject:ub.b});}else{this.ap(this.MV.documentElement);this.publish({subject:ub.a});}};m.JS=function(){window.clearTimeout(this._jsxvX);this._jsxvX=null;try{e.nA(e.VH);}catch(Kb){}try{this.MV.removeEventListener(ub.n,this._jsxqt,false);}catch(Kb){}delete this[ub.s];};m.W5=function(){if(this.mA.readyState==4){var
Lb=this.mA;this.MW();if(!(Lb.status==0||Lb.status>=200&&Lb.status<300)){this.pk(102,jsx3._msg(ub.t,this.getSourceURL(),Lb.status));this.publish({subject:ub.b});}else{var
ab=Lb.responseText;try{this.MV=(new
DOMParser()).parseFromString(ab,ub.p);if(!this.B5(this.MV))this.ap(this.MV.documentElement);jsx3.sleep(function(){this.publish({subject:ub.a});},null,this);}catch(Kb){this.B5(this.MV,Kb);this.publish({subject:ub.b});}}}};m.MW=function(){try{window.clearTimeout(this._jsxvX);this._jsxvX=null;this.mA.onreadystatechange=new
Function();delete this[ub.u];}catch(ab){e.R3(6,jsx3._msg(ub.v,jsx3.NativeError.wrap(ab)));}};m.q1=function(){this.MW();this.pk(111,jsx3._msg(ub.w));this.publish({subject:ub.c});};m.rB=function(j,k,g){e.R3(6,jsx3._msg(ub.k,j));try{if(k>0&&g)this._jsxvX=window.setTimeout(jsx3.makeCallback(function(){this.q1();},this),k);var
H=new
XMLHttpRequest();this.mA=H;H.open(ub.x,j.toString(),g);H.send(null);if(g){if(this.mA.readyState==4){this.MV=(new
DOMParser()).parseFromString(ib,ub.p);if(!this.B5(this.MV))this.ap(this.MV.documentElement);jsx3.sleep(function(){this.publish({subject:ub.a});},null,this);}else{var
w=this;H.onreadystatechange=function(){w.W5();};}}else if(!(H.status==0||H.status>=200&&H.status<300)){this.pk(102,jsx3._msg(ub.t,j,H.status));}else{var
ib=H.responseText;try{this.MV=(new
DOMParser()).parseFromString(ib,ub.p);if(!this.B5(this.MV))this.ap(this.MV.documentElement);}catch(Kb){this.B5(this.MV,Kb);}}}catch(Kb){this.B5(this.MV,Kb);}};m.getSourceURL=function(){return this.SN;};m.B5=function(s,j){if(s!=null){var
S=s.parseError;if(S!=null&&S.errorCode!=ub.y){var
wb=jsx3._msg(ub.z,S.errorCode,S.line,S.linepos,jsx3.util.strTrim(String(S.reason)),jsx3.util.strTruncate(S.srcText),S.url);this.pk(S.errorCode,wb);return true;}var
Xa=s.documentElement;if(Xa==null){if(j)this.pk(158,jsx3._msg(ub.A,j));else this.pk(108,jsx3._msg(ub.r));return true;}else if(Xa.tagName==ub.B&&Xa.namespaceURI&&Xa.namespaceURI.match(ub.C)){this.pk(109,Xa.textContent.replace(ub.D,ub.E));return true;}}if(j!=null){this.pk(110,jsx3._msg(ub.v,jsx3.NativeError.wrap(j)));return true;}return false;};m.getValidateOnParse=function(){return this.MV.validateOnParse;};m.setValidateOnParse=function(j){this.MV.validateOnParse=j;};m.getResolveExternals=function(){return this.MV.resolveExternals;};m.setResolveExternals=function(k){this.MV.resolveExternals=k;};m.cloneDocument=function(){try{e.nA(e.VH);}catch(Kb){}try{var
eb=new
e(this.MV.cloneNode(true));}catch(Kb){var
eb=new
e();eb.loadXML(this.getXML());}return eb;};m.Bs=function(){this.JS();this.pk(112,jsx3._msg(ub.w));this.publish({subject:ub.c});};m.toString=function(){if(this.MV!=null&&!this.hasError()){return (new
XMLSerializer()).serializeToString(this.MV);}else return this.jsxsuper();};m.getXmlVersion=function(){return this.MV.xmlVersion;};m.getXmlEncoding=function(){return this.MV.xmlEncoding;};m.getXmlStandalone=function(){return this.MV.xmlStandalone;};m.serialize=function(k,f,o){if(k===true)k=this.getXmlVersion()||ub.F;if(f===true)f=this.getXmlEncoding();var
Nb=k||f||o;if(Nb){var
Ea=ub.G;if(k)Ea=Ea+(ub.H+k+ub.I);if(f)Ea=Ea+(ub.J+f+ub.I);if(o!=null)Ea=Ea+(ub.K+(o?ub.L:ub.M)+ub.I);Ea=Ea+ub.N;var
C=new
Array(this.MV.childNodes.length+1);C[0]=Ea;for(var
Z=0;Z<this.MV.childNodes.length;Z++){var
Da=this.MV.childNodes[Z];if(Da.nodeType!=7||Da.nodeName!=ub.O){var
Wa=new
Ma(Da);C[Z+1]=Wa.hasError()?ub.P+Wa+ub.Q:Wa.toString();}}}else{var
C=new
Array(this.MV.childNodes.length);for(var
Z=0;Z<this.MV.childNodes.length;Z++){var
Da=this.MV.childNodes[Z];var
Wa=new
Ma(Da);C[Z]=Wa.hasError()?ub.P+Wa+ub.Q:Wa.toString();if(Da.nodeType==7||Da.nodeName==ub.O)C[Z]+=ub.R;}}return C.join(ub.g);};m.createDocumentElement=function(p,k){if(k==null)k=null;var
Nb=this.MV.createElementNS(k,p);if(this.MV.documentElement!=null)this.MV.replaceChild(Nb,this.MV.documentElement);else this.MV.appendChild(Nb);this.ap(this.MV.documentElement);return new
jsx3.xml.Entity(Nb);};m.createProcessingInstruction=function(s,l){var
ja=this.MV.createProcessingInstruction(s,l);this.MV.appendChild(ja);};m.setAsync=function(q){this._jsxX2=q;return this;};m.getAsync=function(j){return this._jsxX2==null?false:this._jsxX2;};m.setSelectionLanguage=function(l){return this;};m.getSelectionLanguage=function(){return ub.S;};e.Ym=function(l){var
I=[];for(var Wa in l)I[I.length]=ub.T+l[Wa]+ub.U+Wa+ub.V;return I.join(ub.E);};m.setSelectionNamespaces=function(o){if(typeof o==ub.W)o=e.Ym(o);this.MV._jsxie=o;this.MV._jsxBn=null;return this;};m.getSelectionNamespaces=function(i){return this.MV._jsxie?this.MV._jsxie:ub.g;};m.createNamespaceAxis=function(){var
rb=this.getRootNode();var
db=rb.toString();var
fb=ub.g;do{var
Fa=new
RegExp(ub.T+ub.e+fb+ub.X,ub.Y);var
qa=db.search(Fa);if(qa>=0)fb=fb==ub.g?0:fb+1;}while(qa>=0);this.mC(rb,ub.e+fb);this.loadXML(this.getXML());return ub.e+fb;};m.mC=function(f,k){var
Ba=f.cloneNode(false).getXML();var
Z;while(Z=e.DU.exec(Ba))if(RegExp.$1!=k){var
Qa=f.createNode(2,k+ub.Z+RegExp.$1,e.SEARCHABLE_NAMESPACE);Qa.setValue(RegExp.$2);f.setAttributeNode(Qa);}for(var
gb=f.getChildIterator();gb.hasNext();)this.mC(gb.next(),k);};m.getDeclaredNamespaces=function(o){this._jsxEs={};var
ka=this.getRootNode();if(ka)this._getDeclaredNamespaces(ka,{index:0},o);return this._jsxEs;};m._getDeclaredNamespaces=function(r,c,b){var
ga=r.getNamespaceURI();if(ga!=ub.g){var
R;if(!this._jsxEs[ga]||b&&(R=r.getPrefix())!=ub.g&&typeof b[R]!=ub._)if(R){this._jsxEs[ga]=R;}else{c.index+=1;this._jsxEs[ga]=ub.aa+c.index;}}if(r.getNodeType()==1)for(var
tb=r.selectNodeIterator(ub.ba);tb.hasNext();){var
Bb=tb.next();if(Bb.getNodeType()==1||Bb.getNodeType()==2)this._getDeclaredNamespaces(Bb,c,b);}};m.getNativeDocument=function(){return this.MV;};e.R3=function(p,s){if(e.aM==null)if(jsx3.util.Logger){e.aM=jsx3.util.Logger.getLogger(e.jsxclass.getName());if(e.aM==null)return;}else return;e.aM.log(p,s);};e.getVersion=function(){return ub.ca;};});jsx3.Document=jsx3.xml.Document;jsx3.Class.defineClass("jsx3.xml.Template",null,null,function(p,s){var
ub={d:"temp.root_elm",i:"temp.temp_err",k:"temp.err",a:"temp.init_err",h:"transform",c:"temp.nat_err",f:" => ",j:"temp.doc_err",g:"temp.empty",b:"stylesheet",e:""};p.DISABLE_OUTPUT_ESCAPING=1;p.b8={};p.supports=function(n){return p.b8[n]||Boolean(0);};s.init=function(e){if(e.hasError())throw new
jsx3.Exception(jsx3._msg(ub.a,e.getError()));if(e.getBaseName()==ub.b){try{this.WP=new
XSLTProcessor();this.WP.importStylesheet(e.getNativeDocument());}catch(Kb){this.pk(202,jsx3._msg(ub.c,jsx3.NativeError.wrap(Kb)));}}else this.pk(201,jsx3._msg(ub.d));this._src=e.getSourceURL();};s.setParam=function(j,n){if(!this.cQ)this.cQ=new
jsx3.util.List();if(this.cQ.indexOf(j)<0)this.cQ.add(j);this.WP.setParameter(ub.e,j,n!=null?n.toString():ub.e);};s.fR=function(h,d){var
lb=new
jsx3.util.Timer(p.jsxclass,(h instanceof jsx3.xml.Document?h.getSourceURL():ub.e)+ub.f+this);var
H=h instanceof jsx3.xml.Document?h.getNativeDocument():h.getNative();var
ia=this.WP.transformToDocument(H);var
zb=null;if(ia){zb=d?new
jsx3.xml.Document(ia):(new
XMLSerializer()).serializeToString(ia);}else this.pk(203,jsx3._msg(ub.g));lb.log(ub.h);return zb;};s.reset=function(){if(this.cQ){for(var
rb=this.cQ.iterator();rb.hasNext();)this.WP.removeParameter(ub.e,rb.next());this.cQ.clear();}};s.setParams=function(e){for(var va in e)this.setParam(va,e[va]);};s.transform=function(c,i){if(this.hasError())throw new
jsx3.Exception(jsx3._msg(ub.i,this.getError()));if(c.hasError())throw new
jsx3.Exception(jsx3._msg(ub.j,c.getError()));try{return this.fR(c,i);}catch(Kb){this.pk(204,jsx3._msg(ub.k,jsx3.NativeError.wrap(Kb)));return null;}};s.transformToObject=function(r){return this.transform(r,true);};s.toString=function(){return this._src;};});jsx3.xml.Entity.jsxclass.mixin(jsx3.xml.Template.prototype,true,["getError","hasError","pk"]);jsx3.Class.defineClass("jsx3.xml.XslDocument",jsx3.xml.Document,null,function(f,s){var
ub={a:"_M"};s.setParam=function(o,q){this.fZ().setParam(o,q);};s.setParams=function(b){this.fZ().setParams(b);};s.reset=function(){if(this._M)this._M.reset();};s.transform=function(d){return this.fZ().transform(d);};s.transformToObject=function(p){return this.fZ().transformToObject(p);};s.fZ=function(){if(this._M==null)this._M=new
jsx3.xml.Template(this);return this._M;};s.load=function(q){delete this[ub.a];return this.jsxsuper(q);};s.loadXML=function(r){delete this[ub.a];return this.jsxsuper(r);};s.hasError=function(){return this.jsxsuper()||this._M!=null&&this._M.hasError();};s.getError=function(){var
Ga=null;if(this._M)Ga=this._M.getError();if(!Ga)Ga=this.jsxsuper();return Ga;};f.wrap=function(r){return new
f(r.getNativeDocument());};s.cloneDocument=function(){return f.wrap(this.jsxsuper());};s.isMutable=function(){return true;};});jsx3.Class.defineClass("jsx3.xml.Processor",null,null,function(q,n){var
la=jsx3.xml.Template;q.DISABLE_OUTPUT_ESCAPING=1;q.supports=function(h){return la.supports(h);};n.init=function(k,r,s){this.ur=k;this.n4=r;this.cQ=s;};n.setXML=function(a){this.ur=a;return this;};n.setXSL=function(i){this.n4=i;return this;};n.setParams=function(a){this.cQ=a;return this;};n.transformToObject=function(){return this.transform(true);};n.transform=function(m){var
w=new
la(this.n4);if(!w.hasError()){if(this.cQ)w.setParams(this.cQ);var
ra=w.transform(this.ur,m);if(!w.hasError())return ra;}var
V=w.getError();this.pk(V.code,V.description);return null;};});jsx3.xml.Entity.jsxclass.mixin(jsx3.xml.Processor.prototype,true,["getError","hasError","pk"]);jsx3.Class.defineClass("jsx3.net.Request",null,[jsx3.util.EventDispatcher],function(q,p){var
ub={o:" ",d:"_timeoutto",k:"hS",c:"req_inst",h:"req.open",p:"3.00.00",g:"req.netsc",l:"req.err_send",b:"timeout",i:"req.err_open",m:"load async",a:"response",f:"UniversalBrowserRead",j:"req.err_state",n:"load sync",e:"req.bad_xml"};q.STATUS_OK=200;q.STATUS_ERROR=400;q.EVENT_ON_RESPONSE=ub.a;q.EVENT_ON_TIMEOUT=ub.b;q._X={};p.init=function(m){if(m!=null)q._X[m]=this;try{this._request=new
XMLHttpRequest();}catch(Kb){throw new
jsx3.Exception(jsx3._msg(ub.c),jsx3.NativeError.wrap(Kb));}};p.abort=function(){if(this._timeoutto){window.clearTimeout(this._timeoutto);delete this[ub.d];}this._request.onreadystatechange=new
Function();this._request.abort();return this;};p.getAllResponseHeaders=function(){return this._request.getAllResponseHeaders();};p.getResponseHeader=function(f){return this._request.getResponseHeader(f);};p.getStatusText=function(){return this._request.statusText;};p.getStatus=function(){var
qb=this.hS!=null?this.hS:this._request.status;return qb==0?200:qb;};p.getResponseText=function(){return this._request.responseText;};p.getResponseXML=function(){var
Bb=this.getResponseText();var
Ba=new
jsx3.xml.Document();Ba.loadXML(Bb);if(!Ba.hasError())return Ba;else q.R3(2,jsx3._msg(ub.e,this.SN,Ba.getError()));return null;};p.setRequestHeader=function(f,i){this._request.setRequestHeader(f,String(i));return this;};p.getReadyState=function(){return this._request.readyState;};p.open=function(a,r,l,m,h){this.hS=0;r=r.toString();this.wE=Boolean(l);this.P1=a;this.SN=r;try{if(window.netscape&&netscape.security)netscape.security.PrivilegeManager.enablePrivilege(ub.f);}catch(Kb){q.R3(5,jsx3._msg(ub.g,jsx3.NativeError.wrap(Kb)));}try{q.R3(6,jsx3._msg(ub.h,r));this._request.open(a,r,this.wE,m,h);}catch(Kb){this.hS=400;q.R3(2,jsx3._msg(ub.i,r,jsx3.NativeError.wrap(Kb)));}return this;};q.cancelRequest=function(c){var
pa=q._X[c];if(pa)pa.abort();};q.getRequest=function(f){return q._X[f];};p.getURL=function(){return this.SN;};p.setTimeouts=function(c,k,f,n){return this;};p.send=function(i,r){if(this.hS==400)throw new
jsx3.Exception(jsx3._msg(ub.j));var
mb=new
jsx3.util.Timer(q.jsxclass,this.SN);var
w=false;try{this._request.send(i);if(this.wE)this.hS=0;else delete this[ub.k];}catch(Kb){this.hS=400;q.R3(2,jsx3._msg(ub.l,this.SN,jsx3.NativeError.wrap(Kb)));w=this;}if(this.wE){if(w||this._request.readyState==4){jsx3.sleep(function(){mb.log(ub.m);this.publish({subject:ub.a});},null,this);}else{var
Mb=this;this._request.onreadystatechange=function(){if(Mb._request.readyState==4){mb.log(ub.m);Mb.zL();}};if(!isNaN(r)&&r>0)this._timeoutto=window.setTimeout(function(){mb.log(ub.b);Mb.hW();},r);}}else mb.log(ub.n);return this;};p.hW=function(){delete this[ub.d];this.abort();this.hS=408;this.publish({subject:ub.b});};p.zL=function(){delete this[ub.k];if(this._timeoutto){window.clearTimeout(this._timeoutto);delete this[ub.d];}this._request.onreadystatechange=new
Function();this.publish({subject:ub.a});};p.toString=function(){return this.jsxsuper()+ub.o+this.P1+ub.o+this.getStatus()+ub.o+this.SN;};q.R3=function(k,e){if(q.aM==null)if(jsx3.util.Logger){q.aM=jsx3.util.Logger.getLogger(q.jsxclass.getName());if(q.aM==null)return;}else return;q.aM.log(k,e);};q.getVersion=function(){return ub.p;};});jsx3.HttpRequest=jsx3.net.Request;jsx3.Class.defineClass("jsx3.util.Logger",null,null,function(h,m){var
ub={o:"\n",d:"INFO",k:") ",c:"WARN",h:"logr.err_hand",p:"CODE: ",g:"",l:":",q:"DESC: ",b:"ERROR",i:" ",m:"ERRO01",a:"FATAL",f:"TRACE",j:"(",n:"TIME: ",e:"DEBUG"};var
qa=jsx3.Method;var
Ua=jsx3.Exception;h.OFF=0;h.FATAL=1;h.ERROR=2;h.WARN=3;h.INFO=4;h.DEBUG=5;h.TRACE=6;h.qR=1;h.d0=6;h.GLOBAL=null;h.getLogger=function(n){var
Jb=h.Manager.getManager();if(Jb==null)return null;var
fb=Jb.getLogger(n);if(fb==null){fb=new
h(n);Jb.addLogger(fb);}return fb;};h.FL=[null,ub.a,ub.b,ub.c,ub.d,ub.e,ub.f];h.levelAsString=function(r){return h.FL[r]||ub.g;};m.zo=null;m.XJ=null;m.y5=null;m.D5=4;m.a2=null;m.fz=true;m.r7=null;m.init=function(k){this.zo=k;};m.getName=function(){return this.zo;};m.addHandler=function(n){if(this.XJ==null)this.XJ=new
jsx3.util.List();this.XJ.add(n);};m.removeHandler=function(b){if(this.XJ!=null)this.XJ.remove(b);};m.getLevel=function(){return this.y5;};m.getEffectiveLevel=function(){return this.D5;};m.setLevel=function(c){c=Math.max(0,Math.min(h.d0,c));this.y5=c;this.hG();};m.hG=function(){var
Eb=null;if(this.y5!=null){Eb=this.y5;}else if(this.a2!=null){Eb=this.a2.D5;}else Eb=m.D5;if(Eb!=this.D5){this.D5=Eb;if(this.r7!=null)for(var
Mb=this.r7.iterator();Mb.hasNext();)Mb.next().hG();}};m.getParent=function(){return this.a2;};m.setParent=function(o){if(this.a2!=null)this.a2.r7.remove(this);this.a2=o;if(this.a2!=null){if(this.a2.r7==null)this.a2.r7=new
jsx3.util.List();this.a2.r7.add(this);}this.hG();};m.getUseParentHandlers=function(){return this.fz;};m.setUseParentHandlers=function(i){this.fz=i;};m.GM=function(p){var
Db=this;var
yb=p.getLevel();while(Db!=null){if(Db.XJ!=null)for(var
C=Db.XJ.iterator();C.hasNext();){var
O=C.next();if(O.isLoggable(yb))try{O.handle(p);}catch(Kb){Kb=jsx3.NativeError.wrap(Kb);h.getLogger(h.jsxclass.getName()).error(jsx3._msg(ub.h,O.getName(),Kb),Kb);}}if(!Db.getUseParentHandlers())break;Db=Db.getParent();}};m.log=function(g,i,o){g=Math.max(g,h.qR);if(this.D5<g)return;var
Y=o instanceof Array?o:qa.argsAsArray(arguments,2);var
T=new
h.Record(i,Y,g,this.getName(),jsx3.lang.getCaller(1),null);this.GM(T);};m.logError=function(k,e,r){k=Math.max(k,h.qR);if(this.D5<k)return;var
Ca=new
h.Record(e,null,k,this.getName(),jsx3.lang.getCaller(1),r);this.GM(Ca);};m.logStack=function(b,n,q){b=Math.max(b,h.qR);if(this.D5<b)return;var
v=new
h.Record(n,null,b,this.getName(),jsx3.lang.getStack(q!=null?q:0),null);this.GM(v);};m.isLoggable=function(l){l=Math.max(l,h.qR);return this.D5>=l;};m.fatal=function(i,o){if(o==null||o instanceof Array)this.log(1,i,o);else if(o instanceof Ua)this.logError(1,i,o);else if(this.D5>=1)this.log(1,i,qa.argsAsArray(arguments,1));};m.error=function(i,o){if(o==null||o instanceof Array)this.log(2,i,o);else if(o instanceof Ua)this.logError(2,i,o);else if(this.D5>=2)this.log(2,i,qa.argsAsArray(arguments,1));};m.warn=function(a,d){if(d==null||d instanceof Array)this.log(3,a,d);else if(d instanceof Ua)this.logError(3,a,d);else if(this.D5>=3)this.log(3,a,qa.argsAsArray(arguments,1));};m.info=function(b,c){if(c==null||c instanceof Array)this.log(4,b,c);else if(c instanceof Ua)this.logError(4,b,c);else if(this.D5>=4)this.log(4,b,qa.argsAsArray(arguments,1));};m.debug=function(q,g){if(g==null||g instanceof Array)this.log(5,q,g);else if(g instanceof Ua)this.logError(5,q,g);else if(this.D5>=5)this.log(5,q,qa.argsAsArray(arguments,1));};m.trace=function(s,e){if(e==null||e instanceof Array)this.log(6,s,e);else if(e instanceof Ua)this.logError(6,s,e);else if(this.D5>=6)this.log(6,s,qa.argsAsArray(arguments,1));};m.toString=function(){return this.jsxsuper()+ub.i+this.getName();};h.reset=function(){};h.doLog=function(s,a,l,f){if(l==null)l=4;else if(l<4)l=4;else l=5;if(h.GLOBAL){var
bb=a!=null?ub.j+s+ub.k+a:s;if(f||f==null)h.GLOBAL.logStack(l,bb,1);else h.GLOBAL.log(l,bb);}};h.logError=function(k,e){var
I=ub.g;for(var M in k){if(I)I=I+ub.i;I=I+(M+ub.l+k[M]);}h.doLog(ub.m,I,e,false);};h.getMinPriority=function(){return 3;};h.getLog=function(){return [];};h.errorToString=function(c){var
eb=ub.g;eb=eb+(ub.n+new
Date(c.timestamp)+ub.o);eb=eb+(ub.p+c.code+ub.o);eb=eb+(ub.q+c.description+ub.o);return eb;};h.toString=function(){return ub.g;};});jsx3.Class.defineClass("jsx3.util.Logger.Manager",null,null,function(f,b){var
ub={k:"",h:"/configuration/logger[handler-ref/@name='",c:"jsx_logger_config",p:"level",q:"number",G:".",v:"[@require='true']",i:"name",A:"logr.bn_setr",a:"jsx:/../logger.xml",F:"logr.no_hand",u:"zs",t:"jsx.js",j:"/configuration/handler",s:"logger.require",z:"logr.bn_eval",d:"logr.err_conf",o:"require",D:"false",w:"./property",r:"logr.no_class",C:"useParent",B:"/configuration/logger[@name='",l:"class",g:"']",b:"global",m:"lazy",f:"[@lazy='true' and @class='",y:"eval",n:"true",x:"value",E:"./handler-ref",e:"<configuration/>"};var
Wa=jsx3.Exception;var
Za=jsx3.util.Logger;f.SB=ub.a;f.BL=ub.b;f.fw=null;f.uv=-1;b.initialize=function(p){if(!p){p=new
jsx3.xml.Document();p.load(jsx3.getEnv(ub.c)||f.SB);}if(p.hasError()){window.alert(jsx3._msg(ub.d,p.getError(),jsx3.resolveURI(p.getSourceURL())));p.loadXML(ub.e);}this.fD=p;this._W();for(var Na in this.Nx)this.addLogger(this.Nx[Na]);};f.getManager=function(){if(f.fw==null){f.fw=new
f();Za.GLOBAL=new
Za(f.BL);f.fw.addLogger(Za.GLOBAL);}return f.fw;};b.fD=null;b.Nx=null;b.XJ=null;b.init=function(l){this.Nx={};this.XJ={};};b.kS=function(d){var
ba=this._W(ub.f+d.getName()+ub.g);this.E2(ba);};b.E2=function(j){for(var
da=0;da<j.length;da++){var
tb=j[da];var
Nb=this.getHandler(tb);var
nb=this.fD.selectNodeIterator(ub.h+tb+ub.g);while(nb.hasNext()){var
fb=nb.next();var
T=fb.getAttribute(ub.i);var
ja=this.getLogger(T);if(ja!=null)ja.addHandler(Nb);}}};b._W=function(g){var
Mb=[];if(!this.fD)return Mb;var
C=this.fD.selectNodeIterator(ub.j+(g!=null?g:ub.k));var
wa=this.zs==null;while(C.hasNext()){var
u=C.next();var
yb=u.getAttribute(ub.i);if(this.getHandler(yb)!=null)continue;var
Xa=u.getAttribute(ub.l);var
ta=u.getAttribute(ub.m)==ub.n;var
Cb=u.getAttribute(ub.o)==ub.n;var
Ga=jsx3.Class.forName(Xa);if(Ga==null&&Cb){if(this.zs==null)this.zs=[];this.zs.push(Xa);this.XJ[yb]=f.uv;continue;}if(Ga){var
ba=Ga.newInstance(yb);this.u9(ba,u);ba.onAfterInit();var
Eb=u.getAttribute(ub.p);if(Eb&&typeof Za[Eb]==ub.q)ba.setLevel(Za[Eb]);this.addHandler(ba);Mb[Mb.length]=yb;}else if(!ta&&!Cb){window.alert(jsx3._msg(ub.r,Xa));}else this.XJ[yb]=f.uv;}if(wa&&this.zs!=null){var
_a=this;var
I=new
jsx3.util.Job(ub.s);I.run=function(){jsx3.sleep(function(){this.Z4();},null,_a);};jsx3.CLASS_LOADER.addJob(I,ub.t);}return Mb;};b.Z4=function(q){for(var
wa=0;wa<this.zs.length;wa++)jsx3.require(this.zs[wa]);delete this[ub.u];var
Eb=this._W(ub.v);this.E2(Eb);};b.u9=function(i,m){var
xa=i.getClass();for(var
M=m.selectNodeIterator(ub.w);M.hasNext();){var
Ba=M.next();var
Ua=Ba.getAttribute(ub.i);var
ob=Ba.getAttribute(ub.x);var
qa=Ba.getAttribute(ub.y)==ub.n;var
J=xa.getSetter(Ua);if(J!=null){if(qa)try{ob=isNaN(ob)?jsx3.eval(ob):Number(ob);}catch(Kb){throw new
Wa(jsx3._msg(ub.z,Ua,ob,i),jsx3.NativeError.wrap(Kb));}J.apply(i,[ob]);}else throw new
Wa(jsx3._msg(ub.A,Ua,xa));}};b.addLogger=function(d){var
_a=d.getName();this.Nx[_a]=d;if(this.fD){var
cb=this.fD.selectSingleNode(ub.B+_a+ub.g);if(cb!=null){var
qb=cb.getAttribute(ub.p);if(qb&&typeof Za[qb]==ub.q)d.setLevel(Za[qb]);var
la=cb.getAttribute(ub.C)!=ub.D;d.setUseParentHandlers(la);var
db=cb.selectNodeIterator(ub.E);while(db.hasNext()){var
Aa=db.next();var
nb=Aa.getAttribute(ub.i);var
Da=this.getHandler(nb);if(Da!=null){d.addHandler(Da);}else if(this.XJ[nb]!=f.uv)throw new
Wa(jsx3._msg(ub.F,_a,nb));}this.u9(d,cb);}}if(_a!=f.BL){var
Mb=_a.lastIndexOf(ub.G);var
ga=Mb>=0?_a.substring(0,Mb):f.BL;d.setParent(Za.getLogger(ga));}};b.addHandler=function(h){this.XJ[h.getName()]=h;};b.getLogger=function(n){return this.Nx[n];};b.getHandler=function(k){var
Ya=this.XJ[k];return Ya==f.uv?null:Ya;};b.getHandlerNames=function(){var
Qa=[];for(var Fb in this.XJ)Qa[Qa.length]=Fb;return Qa;};});jsx3.Class.defineClass("jsx3.util.Logger.Record",null,null,function(a,i){var
ub={a:"function"};a.lp=1;i.rS=null;i.xx=null;i.Ro=null;i.cQ=null;i.D5=null;i.vV=null;i.M4=null;i.ex=null;i.init=function(o,c,s,e,h,g){this.rS=a.lp++;this.xx=new
Date();this.Ro=o;this.cQ=c;this.D5=s;this.vV=e;this.M4=h;this.ex=g;};i.getSerial=function(){return this.rS;};i.getDate=function(){return this.xx;};i.getMessage=function(){return this.Ro;};i.getParameters=function(){return this.cQ;};i.getLevel=function(){return this.D5;};i.getLoggerName=function(){return this.vV;};i.getFunction=function(){return typeof this.M4==ub.a?this.M4:null;};i.getStack=function(){return this.M4 instanceof Array?this.M4:null;};i.getError=function(){return this.ex;};});jsx3.Class.defineClass("jsx3.util.Logger.Handler",null,null,function(s,i){var
ub={a:"",b:"objRecord"};var
La=jsx3.util.Logger;var
ra=jsx3.util.Logger.Manager;s.registerHandlerClass=function(e){ra.getManager().kS(e);};i.zo=ub.a;i.D5=null;i.init=function(k){this.zo=k;};i.onAfterInit=function(){};i.getName=function(){return this.zo;};i.getLevel=function(){return this.D5;};i.setLevel=function(f){f=Math.max(0,Math.min(La.d0,f));this.D5=f;};i.isLoggable=function(n){return this.D5==null||this.D5>=n;};i.handle=jsx3.Method.newAbstract(ub.b);});jsx3.Class.defineClass("jsx3.util.Logger.MemoryHandler",jsx3.util.Logger.Handler,null,function(d,o){o.r9=null;o.q4=100;o.init=function(p){this.jsxsuper(p);this.r9=[];};o.handle=function(p){this.r9[this.r9.length]=p;if(this.r9.length>this.q4)this.r9.shift();};o.clearBuffer=function(){this.r9=[];};o.getBufferSize=function(){return this.q4;};o.setBufferSize=function(e){this.q4=Math.max(1,e);if(this.r9.length>this.q4)this.r9.splice(0,this.r9.length-this.q4);};o.getRecords=function(a){if(a==null)a=this.r9.length;return this.r9.slice(this.r9.length-a);};});jsx3.Class.defineClass("jsx3.util.Logger.FormatHandler",jsx3.util.Logger.Handler,null,function(d,e){var
ub={o:/\%f/g,d:" (",k:/\%l/g,r:"{5,date,yyyy-MM-dd}",c:" ",h:"{0}",p:"{4}",g:/\%s/g,l:"{2}",q:/\%d/g,b:"",i:/\%n/g,m:/\%M/g,a:"%d %t %n (%l) - %M",f:"\n",t:"{5,date,HH:mm:ss.SSS}",j:"{1}",n:"{3}",s:/\%t/g,e:") "};e.b6=ub.a;e.VB=ub.b;e.init=function(i){this.jsxsuper(i);};e.format=function(i){var
U=i.getDate();var
Pa=i.getFunction();var
Sa=i.getMessage()||ub.b;var
Fa=this.Kv();var
R=jsx3.util.Logger.levelAsString(i.getLevel());var
Oa=Fa?Fa.format(i.getSerial(),i.getLoggerName(),R,Sa,Pa!=null?Pa.jsxmethod!=null?Pa.jsxmethod.toString():Pa.toString():ub.b,U):U+ub.c+i.getLoggerName()+ub.d+R+ub.e+Sa;var
eb=i.getError();var
xa=i.getStack();if(eb!=null){Oa=Oa+(ub.f+eb.printStackTrace());}else if(xa!=null)Oa=Oa+(ub.f+jsx3.Exception.formatStack(xa));return Oa;};e.getFormat=function(){return this.b6;};e.setFormat=function(g){this.b6=g;this.U1=null;};e.Kv=function(){if(this.U1==null&&jsx3.util.MessageFormat){var
v=this.b6||ub.b;v=v.replace(ub.g,ub.h);v=v.replace(ub.i,ub.j);v=v.replace(ub.k,ub.l);v=v.replace(ub.m,ub.n);v=v.replace(ub.o,ub.p);v=v.replace(ub.q,ub.r);v=v.replace(ub.s,ub.t);this.U1=new
jsx3.util.MessageFormat(v);}return this.U1;};e.getResourceUrls=function(){return this.VB;};e.setResourceUrls=function(q){this.VB=q;};});jsx3.Class.defineClass("jsx3.util.Logger.AlertHandler",jsx3.util.Logger.FormatHandler,null,function(q,c){var
ub={a:"logr.alrt_ctd",b:"logr.alrt_err"};c.bI=10;c.sF=0;c.OR=false;c.handle=function(d){if(this.OR)return;this.sF++;try{if(this.bI>0&&this.sF%this.bI==0)if(!window.confirm(jsx3._msg(ub.a,this.getName()))){this.OR=true;return;}window.alert(this.format(d));}catch(Kb){window.alert(jsx3._msg(ub.b,jsx3.NativeError.wrap(Kb)));}};c.getConfirmInterval=function(){return this.bI;};c.setConfirmInterval=function(s){this.bI=s;};});jsx3.ERROR=jsx3.util.Logger;jsx3.Class.defineClass("jsx3.util.Locale",null,null,function(b,j){var
ub={d:"GB",i:/[\-_]/,a:"",h:"_",c:"US",f:"string.terr.",g:"format.locale.displayname",b:"en",e:"string.lang."};j.init=function(a,d){this.Wr=a?a.toLowerCase():ub.a;this.AS=d?d.toUpperCase():ub.a;};b.ENGLISH=new
b(ub.b);b.US=new
b(ub.b,ub.c);b.UK=new
b(ub.b,ub.d);j.getLanguage=function(){return this.Wr;};j.getCountry=function(){return this.AS;};j.getDisplayLanguage=function(g){return jsx3.System.getLocaleProperties(g).get(ub.e+this.Wr)||ub.a;};j.getDisplayCountry=function(q){return jsx3.System.getLocaleProperties(q).get(ub.f+this.AS)||ub.a;};j.getDisplayName=function(o){var
B=this.getDisplayLanguage(o);var
ka=this.getDisplayCountry(o);if(!B)return ka;if(!ka)return B;var
na=jsx3.System.getLocaleProperties(o).get(ub.g);return (new
jsx3.util.MessageFormat(na)).format(B,ka);};j.getSearchPath=function(){var
K=[this];if(this.AS!=ub.a||this.Wr!=ub.a){if(this.AS!=ub.a&&this.Wr!=ub.a)K.push(new
b(this.Wr));K.push(new
b(ub.a));}return K;};j.equals=function(a){return this===a||a instanceof b&&a.Wr==this.Wr&&a.AS==this.AS;};j.toString=function(){if(this.AS)return this.Wr+ub.h+this.AS;else return this.Wr;};b.valueOf=function(r){var
_a=r.split(ub.i);return new
b(_a[0],_a[1]);};});jsx3.Class.defineClass("jsx3.util.NumberFormat",null,null,function(h,o){var
ub={k:"number.zero",h:"number",c:".currency",H:"-",p:"unshift",q:"push",G:"number.permille",v:";",i:"number.minus",A:"#",a:".integer",F:"\u2030",u:"nmfmt.sq",t:"'",j:"number.infinity",s:"0#,.",z:"nmfmt.gpdec",d:".percent",o:"number.decimal",D:"%",w:"nmfmt.numpt",r:"0",C:"number.currency",B:"\u00A4",l:"number.currency.grouping",g:"number.NaN",b:"",m:"number.grouping",f:"._instance",y:",",n:"number.currency.decimal",x:".",E:"number.percent",e:"format.number"};h.getIntegerInstance=function(p){return h.DT(p,ub.a);};h.getNumberInstance=function(d){return h.DT(d,ub.b);};h.getCurrencyInstance=function(n){return h.DT(n,ub.c);};h.getPercentInstance=function(i){return h.DT(i,ub.d);};h.DT=function(r,m){var
ha=jsx3.System.getLocaleProperties(r);var
ob=ub.e+m+ub.f;var
Cb=ha.get(ob);if(!ha.containsKey(ob)||Cb==null){var
Na=ha.get(ub.e+m);Cb=new
h(Na,r);ha.set(ob,Cb);}return Cb;};o.BF=0;o.mu=ub.b;o.BY=ub.b;o.EJ=null;o.mB=null;o.QI=false;o.VP=Number.MAX_VALUE;o.qY=0;o.vY=0;o.Qv=0;o.k1=1;o.JI=false;o.init=function(d,l){this.b6=d;this.MJ=l||jsx3.System.getLocale();this.EM();};o.getLocale=function(){return this.MJ;};o.setLocale=function(q){this.MJ=q;this.EM();};o.format=function(l){var
hb=jsx3.System.getLocaleProperties(this.MJ);if(isNaN(l)){return hb.get(ub.g);}else{if(typeof l!=ub.h)l=Number(l);var
bb=l>=0;var
ua=bb?this.mu:this.EJ!=null?this.EJ:hb.get(ub.i)+this.mu;var
ab=bb?this.BY:this.mB!=null?this.mB:this.BY;var
Va=null;if(!isFinite(l)){Va=hb.get(ub.j);}else{l=this.k1*Math.abs(l);var
jb=h.cV(l);var
Eb=jb[0];var
ia=jb[1];if(this.vY<Eb.length-ia){var
ya=Eb.splice(ia+this.vY,Eb.length-ia-this.vY);if(h.jy(Eb,ya))ia++;}var
_a=ia>=0?Eb.slice(0,ia):Eb;var
B=ia>=0?Eb.slice(ia):[];h.d9(_a,this.VP,this.qY,true);h.d9(B,this.vY,this.Qv,false);var
xa=hb.get(ub.k);h.r3(_a,xa);h.r3(B,xa);if(this.BF>0){var
v=this.JI?hb.get(ub.l)||hb.get(ub.m):hb.get(ub.m);for(var
w=_a.length-this.BF;w>=1;w=w-this.BF)_a.splice(w,0,v);}Va=_a.join(ub.b);if(this.QI||B.length>0)Va=Va+((this.JI?hb.get(ub.n)||hb.get(ub.o):hb.get(ub.o))+B.join(ub.b));}return ua+Va+ab;}};h.qP="0".charCodeAt(0);h.qN=".".charCodeAt(0);h.cV=function(b){if(b<0)throw new
jsx3.Exception();var
R=Math.log(b)*Math.LOG10E;if(!isFinite(R)){if(b==0)return [[0],0];throw new
jsx3.Exception();}var
S=b.toString();var
A=[];var
Oa=null;for(var
lb=0;lb<S.length;lb++){var
ba=S.charCodeAt(lb);var
X=ba-h.qP;if(X>=0&&X<=9){A[A.length]=X;}else if(ba==h.qN){}else break;}if(R>=0){var
oa=Math.floor(R+1.00001);if(A.length>oa){Oa=oa;}else{if(A.length<oa)for(var
lb=A.length;lb<oa;lb++)A[A.length]=0;Oa=A.length;}}else{var
sa=Math.ceil(-1.00001-R);var
aa=A.indexOf(0);A.splice(0,aa+1);for(var
lb=0;lb<sa;lb++)if(A[lb]!=0)A.splice(lb,0,0);Oa=0;}return [A,Oa];};h.d9=function(r,c,l,s){if(r.length>c){if(s){r.splice(0,r.length-c);}else r.splice(c,r.length-c);}else if(r.length<l){var
lb=s?ub.p:ub.q;for(var
Cb=r.length;Cb<l;Cb++)r[lb](ub.r);}};h.jy=function(e,c){if(c[0]>=5)for(var
R=e.length-1;R>=0;R--){var
Qa=e[R]+1;if(Qa>=10){e[R]=0;if(R==0){e.unshift(1);return true;}}else{e[R]=Qa;break;}}return false;};h.r3=function(q,s){var
aa=s.charCodeAt(0);for(var
pb=0;pb<q.length;pb++)q[pb]=String.fromCharCode(q[pb]+aa);};h.jV=ub.s;o.EM=function(){var
Ea=ub.t;var
N=jsx3.System.getLocaleProperties(this.MJ);var
Eb=this.b6;var
w=0;var
ob=Eb.length;var
Ba=1;var
na=null,H=null;while(w<ob){var
mb=Eb.charAt(w);if(Ba==1){if(mb==Ea){var
Ib=Eb.indexOf(Ea,w+1);if(Ib==w+1){this.mu+=Ea;w=w+2;}else if(Ib>=0){this.mu+=Eb.substring(w+1,Ib);w=Ib+1;}else throw new
jsx3.Exception(jsx3._msg(ub.u,w,this));}else if(h.jV.indexOf(mb)>=0){na=w;Ba++;}else{this.mu+=this.FB(mb,N);w++;}}else if(Ba==2){if(h.jV.indexOf(mb)>=0){w++;}else{H=w;Ba++;}}else if(Ba==3){if(mb==Ea){var
Ib=Eb.indexOf(Ea,w+1);if(Ib==w+1){this.BY+=Ea;w=w+2;}else if(Ib>=0){this.BY+=Eb.substring(w+1,Ib);w=Ib+1;}else throw new
jsx3.Exception(jsx3._msg(ub.u,w,this));}else if(mb==ub.v){this.EJ=ub.b;this.mB=ub.b;Ba++;}else this.BY+=this.FB(mb,N);w++;}else if(Ba==4){if(mb==Ea){var
Ib=Eb.indexOf(Ea,w+1);if(Ib==w+1){this.EJ+=Ea;w=w+2;}else if(Ib>=0){this.EJ+=Eb.substring(w+1,Ib);w=Ib+1;}else throw new
jsx3.Exception(jsx3._msg(ub.u,w,this));}else if(h.jV.indexOf(mb)>=0){Ba++;}else{this.EJ+=this.FB(mb,N);w++;}}else if(Ba==5){if(h.jV.indexOf(mb)>=0){w++;}else Ba++;}else if(Ba==6){if(mb==Ea){var
Ib=Eb.indexOf(Ea,w+1);if(Ib==w+1){this.mB+=Ea;w=w+2;}else if(Ib>=0){this.mB+=Eb.substring(w+1,Ib);w=Ib+1;}else throw new
jsx3.Exception(jsx3._msg(ub.u,w,this));}else this.mB+=this.FB(mb,N);w++;}}if(H==null)H=ob;if(na==null)throw new
jsx3.Exception(jsx3._msg(ub.w,Eb));this.jz(Eb.substring(na,H));};o.jz=function(n){var
ma=n.indexOf(ub.x);if(ma<0)ma=n.length;else if(ma==n.length-1)this.QI=true;var
Cb=n.lastIndexOf(ub.y);if(Cb>=0){var
Kb=ma-Cb-1;if(Kb<1)throw new
jsx3.Exception(jsx3._msg(ub.z,n));this.BF=Kb;}for(var
eb=0;eb<ma;eb++)if(n.charAt(eb)==ub.r)this.qY++;for(var
eb=ma+1;eb<n.length;eb++)if(n.charAt(eb)==ub.r){this.Qv++;this.vY++;}else if(n.charAt(eb)==ub.A)this.vY++;};o.FB=function(q,p){if(q==ub.B){this.JI=true;return p.get(ub.C);}else if(q==ub.D){this.k1=100;return p.get(ub.E);}else if(q==ub.F){this.k1=1000;return p.get(ub.G);}else if(q==ub.H){return p.get(ub.i);}else return q;};o.toString=function(){return this.b6;};});jsx3.Class.defineClass("jsx3.util.DateFormat",null,null,function(r,e){var
ub={O:"dtfmt.parse",k:"date.era",h:"intDateType",c:"long",H:"a",p:"time.ampm",q:"GMT",G:"E",v:"-",I:"x",i:"intTimeType",A:"m",a:"short",F:"M",u:"date.month",t:"date.month.abbrev",j:"format.datetime",N:"objDate",s:"",z:"d",d:"full",o:"date.day",D:"gmt",K:/[a-zA-Z']/g,w:"+",r:":",C:"S",B:"s",l:"date.era.long",g:"format.time.",L:"dtfmt.sq",b:"medium",M:"dtfmt.token",m:"date.day.narrow",f:"intType",J:"'",y:"y",n:"date.day.abbrev",x:"0",E:"string",e:"format.date."};r.SHORT=1;r.MEDIUM=2;r.LONG=3;r.FULL=4;r.hH=2;r.eM=[null,ub.a,ub.b,ub.c,ub.d];r.getDateInstance=function(k,f){var
w=jsx3.System.getLocaleProperties(f).get(ub.e+r.eM[k||r.hH]);if(w==null)throw new
jsx3.IllegalArgumentException(ub.f,k);return new
r(w,f);};r.getTimeInstance=function(s,q){var
lb=jsx3.System.getLocaleProperties(q).get(ub.g+r.eM[s||r.hH]);if(lb==null)throw new
jsx3.IllegalArgumentException(ub.f,s);return new
r(lb,q);};r.getDateTimeInstance=function(o,f,n){var
gb=jsx3.System.getLocaleProperties(n);var
Na=gb.get(ub.e+r.eM[o||r.hH]);var
qb=gb.get(ub.g+r.eM[f||r.hH]);if(Na==null)throw new
jsx3.IllegalArgumentException(ub.h,o);if(qb==null)throw new
jsx3.IllegalArgumentException(ub.i,f);var
cb=new
jsx3.util.MessageFormat(gb.get(ub.j));return new
r(cb.format(qb,Na),n);};r.tN={G:function(b,m,s){var
wa=b.getFullYear()<1;return r.JQ(s.pu(m<4?ub.k:ub.l)[wa?0:1]);},y:function(l,d,j){return r.V5(l.getFullYear(),d);},M:function(a,l,k){return r.YV(k,a.getMonth(),l);},d:function(q,i,o){return r.uG(q.getDate(),i);},E:function(f,q,d){return r.JQ(d.pu(q<3?ub.m:q<4?ub.n:ub.o)[f.getDay()]);},H:function(g,p,c){return r.uG(g.getHours(),p);},h:function(d,o,b){return r.uG(d.getHours()%12||Number(12),o);},m:function(l,d,j){return r.uG(l.getMinutes(),d);},s:function(i,a,g){return r.uG(i.getSeconds(),a);},S:function(h,s,f){return r.uG(h.getMilliseconds(),s);},a:function(l,p,c){return r.JQ(c.pu(ub.p)[Math.floor(l.getHours()/12)]);},z:function(m,n,k){var
Ia=r.m2(m,k);return ub.q+Ia[0]+ub.r+Ia[1];},Z:function(j,b,h){var
_=r.m2(j,h);return _[0]+_[1];}};r.V5=function(q,p){if(p==2){var
Xa=ub.s+q;return Xa.substring(Xa.length-2);}else{if(q<1)q=1-q;return r.uG(q,p);}};r.uG=function(a,m){var
ga=a.toString().split(ub.s);while(ga.length<m)ga.unshift(0);return ga.join(ub.s);};r.JQ=function(d,j){if(j==null||j>=4||d.length<=j)return d;else return d.substring(0,j);};r.YV=function(q,d,i){if(i<=2)return r.uG(d+1,i);else return r.JQ(q.pu(i<4?ub.t:ub.u)[d],i);};r.m2=function(b,a){var
V=a.getTimeZoneOffset(b);var
fa=V<0?ub.v:ub.w;V=Math.abs(V);var
L=Math.floor(V/60).toString();var
tb=Math.floor(V%60).toString();if(L.length<2)L=ub.x+L;if(tb.length<2)tb=ub.x+tb;return [fa+L,tb];};r.No={G:function(o,s,c,g,m,k,n){var
S=r.AZ(s,c,[o.pu(ub.k),o.pu(ub.l)],g,false,k);var
u=S[0],D=S[1];if(u>=0){n.bc=u==0;return D;}else return -1;},y:function(i,f,p,a,g,d,k){if(a<=2){var
Pa=r.Gp(f,p,a,null,d);var
Ra=Number(Pa);if(!isNaN(Ra)){if(Pa.length==2){var
eb=new
Date();var
Ib=eb.getFullYear();var
V=100*Math.floor(Ib/100)+Ra;if(V>=Ib+20){V=V-100;}else if(V<Ib-80)V=V+100;Ra=V;}k.y=Ra;return Pa.length;}else return -1;}else{var
aa=r.Gp(f,p,a,null,d);return r.oK(aa,ub.y,k);}},M:function(q,p,f,i,o,m,c){if(i<=2){var
V=r.Gp(p,f,i,2,m);var
bb=Number(V);if(!isNaN(bb)){c.M=bb-1;return V.length;}else return -1;}else{var
_a=r.AZ(p,f,[q.pu(ub.t),q.pu(ub.u)],i,false,m);var
Ib=_a[0],fa=_a[1];if(Ib>=0){c.M=Ib;return fa;}else return -1;}},d:function(n,a,b,f,l,j,o){var
aa=r.Gp(a,b,f,2,j);return r.oK(aa,ub.z,o);},E:function(q,p,f,i,o,m,c){var
Eb=r.AZ(p,f,[q.pu(ub.m),q.pu(ub.o),q.pu(ub.n)],i,false,m);var
w=Eb[0],ha=Eb[1];if(w>=0){return ha;}else return 0;},H:function(s,o,g,k,q,j,a){var
jb=r.Gp(o,g,k,2,j);var
Mb=Number(jb);if(!isNaN(Mb)){a.hours24=Mb;return jb.length;}else return -1;},h:function(h,g,o,s,f,d,l){var
Ua=r.Gp(g,o,s,2,d);var
ha=Number(Ua);if(!isNaN(ha)){l.hours12=ha;return Ua.length;}else return -1;},m:function(h,g,o,s,f,d,l){var
E=r.Gp(g,o,s,2,d);return r.oK(E,ub.A,l);},s:function(s,o,g,k,q,j,a){var
bb=r.Gp(o,g,k,2,j);return r.oK(bb,ub.B,a);},S:function(a,n,h,l,o,p,s){var
E=r.Gp(n,h,l,3,p);return r.oK(E,ub.C,s);},a:function(j,a,q,b,h,f,m){var
zb=r.OX(a,q,j.pu(ub.p),b,false,f);var
Ta=zb[0],Bb=zb[1];if(Ta>=0){m.pm=Ta==1;return Bb;}else return -1;},z:function(f,i,m,q,d,b,n){var
Ca=i.substring(m,m+3);var
sa=i.charAt(m+3);var
Lb=Number(i.substring(m+4,m+6));var
aa=Number(i.substring(m+7,m+9));if(Ca.toLowerCase()==ub.D&&(sa==ub.w||sa==ub.v)&&!isNaN(Lb)&&!isNaN(aa)){var
V=60*Lb+aa;if(sa==ub.v)V=V*-1;n.timezone=V;return 9;}else return -1;},Z:function(d,k,p,o,b,s,f){var
J=k.charAt(p);var
zb=Number(k.substring(p+1,p+3));var
xa=Number(k.substring(p+3,p+5));if((J==ub.w||J==ub.v)&&!isNaN(zb)&&!isNaN(xa)){var
ia=60*zb+xa;if(J==ub.v)ia=ia*-1;f.timezone=ia;return 5;}else return -1;}};r.PD=function(a,b,j){if(a.indexOf(j,b)==b)return j.length;else return -1;};r.Gp=function(g,o,b,p,m){var
O=m==null||typeof m==ub.E&&!r.EV(m,0);if(m instanceof Array)O=O||m[0]==ub.F&&m[1]>2||m[0]==ub.G||m[0]==ub.H;if(O){var
Qa=o;while(r.EV(g,Qa))Qa++;return g.substring(o,Qa);}else{for(var
D=0;D<b;D++)if(!r.EV(g,o+D))return ub.I;return g.substring(o,o+b);}};r.AZ=function(g,o,m,s,n,a){var
H=[];for(var
Ab=0;Ab<m.length;Ab++)H.push.apply(H,m[Ab]);var
hb=r.OX(g,o,H,s,n,a);hb[0]=hb[0]%m[0].length;return hb;};r.OX=function(l,j,k,n,s,h){var
ha=-1;var
Jb=0;if(!s)l=l.toLowerCase();for(var
hb=0;hb<k.length;hb++){var
U=0;var
gb=s?k[hb]:k[hb].toLowerCase();while(l.length>U&&gb.length>U&&l.charAt(j+U)==gb.charAt(U))U++;if(U>Jb){Jb=U;ha=hb;}}return [ha,Jb];};r.oK=function(g,p,n){var
rb=Number(g);if(!isNaN(rb)){n[p]=rb;return g.length;}else return -1;};r.EV=function(a,k){var
_a=a.charCodeAt(k);return _a>=48&&_a<=57;};e.init=function(k,o){this.b6=k;this.sC=null;this.MJ=o||jsx3.System.getLocale();this.Jy();};e.getLocale=function(){return this.MJ;};e.setLocale=function(q){this.MJ=q;};e.getTimeZoneOffset=function(b){return this.sC!=null?this.sC:-1*(b||new
Date()).getTimezoneOffset();};e.setTimeZoneOffset=function(n){this.sC=n;};e.pu=function(j){return jsx3.System.getLocaleProperties(this.MJ).get(j);};e.Jy=function(){var
ja=ub.J;var
Gb=[];this.Cx=Gb;var
E=this.b6;var
ib=E.length;var
aa=0;var
t=[];var
O=ub.K;while(aa<ib){var
L=E.charAt(aa);if(L==ja){var
ea=E.indexOf(ja,aa+1);if(ea==aa+1){t[t.length]=ja;aa=aa+2;}else if(ea>=0){t[t.length]=E.substring(aa+1,ea);aa=ea+1;}else throw new
jsx3.Exception(jsx3._msg(ub.L,aa,this));}else if(r.tN[L]){var
Ib=1;while(E.charAt(aa+Ib)==L)Ib++;var
Bb=t.join(ub.s);if(Bb.length>0){Gb[Gb.length]=Bb;t.splice(0,t.length);}Gb[Gb.length]=[L,Ib];aa=aa+Ib;}else if(L.match(O)){throw new
jsx3.Exception(jsx3._msg(ub.M,aa,E));}else{O.lastIndex=aa+1;if(O.exec(E)){t[t.length]=E.substring(aa,O.lastIndex-1);aa=O.lastIndex-1;}else{t[t.length]=E.substring(aa);break;}}}var
Bb=t.join(ub.s);if(Bb.length>0){Gb[Gb.length]=Bb;t.splice(0,t.length);}};e.format=function(f){if(!(f instanceof Date)){if(!isNaN(f)){f=new
Date(Number(f));}else f=new
Date(f);if(isNaN(f))throw new
jsx3.IllegalArgumentException(ub.N,f);}var
da=new
Array(this.Cx.length);var
pb=new
Date();pb.setTime(f.getTime()+(this.getTimeZoneOffset(f)+f.getTimezoneOffset())*1000*60);for(var
B=0;B<this.Cx.length;B++){var
La=this.Cx[B];if(La instanceof Array){var
Ia=La[0];var
jb=La[1];da[B]=r.tN[Ia](pb,jb,this);}else da[B]=La;}return da.join(ub.s);};e.parse=function(q){var
ia=new
Date();ia.setTime(0);var
B=0;var
pa={};for(var
Lb=0;Lb<this.Cx.length;Lb++){var
oa=this.Cx[Lb];var
ab=-1;if(oa instanceof Array){ab=r.No[oa[0]](this,q,B,oa[1],ia,this.Cx[Lb+1],pa);}else ab=r.PD(q,B,oa);if(ab<0)throw new
jsx3.Exception(jsx3._msg(ub.O,q,this));B=B+ab;}if(pa.y!=null)ia.setUTCFullYear(pa.y);if(pa.bc)ia.setUTCFullYear(1-ia.getUTCFullYear());if(pa.M!=null)ia.setUTCMonth(pa.M);if(pa.d!=null)ia.setUTCDate(pa.d);if(pa.hours24){ia.setUTCHours(pa.hours24);}else if(pa.hours12){var
E=pa.hours12;if(pa.pm)E=E+12;ia.setUTCHours(E);}if(pa.m!=null)ia.setUTCMinutes(pa.m);if(pa.s!=null)ia.setUTCSeconds(pa.s);if(pa.S!=null)ia.setUTCMilliseconds(pa.S);if(pa.timezone!=null){ia.setTime(ia.getTime()-pa.timezone*1000*60);}else ia.setTime(ia.getTime()-this.getTimeZoneOffset(ia)*1000*60);return ia;};e.getFormat=function(){return this.b6;};e.toString=function(){return this.b6;};});jsx3.Class.defineClass("jsx3.util.MessageFormat",null,null,function(k,j){var
ub={o:"short",d:"number",k:"getTimeInstance",w:"msfmt.bad_type",r:"full",c:"string",h:"msfmt.sq",p:"medium",g:"'",l:"getDateTimeInstance",q:"long",b:"}",v:"currency",i:"msfmt.bracket",m:",",a:"{",u:"percent",f:"",t:"integer",j:"getDateInstance",n:"msfmt.bad_ind",s:"datetime",e:"null"};var
Ea=jsx3.util.NumberFormat;var
L=jsx3.util.DateFormat;j.init=function(d,l){this.b6=d;this.MJ=l||jsx3.System.getLocale();this.EM();};j.getLocale=function(){return this.MJ;};j.setLocale=function(f){this.MJ=f;this.EM();};j.format=function(b){var
t=new
Array(this.Cx.length);var
Da=arguments[0] instanceof Array?arguments[0]:arguments;for(var
w=0;w<t.length;w++){var
Ta=this.Cx[w];if(Ta instanceof Array){var
Db=Ta[0];var
Ha=Ta[1];var
ya=Da[Db];if(Db>=Da.length){t[w]=ub.a+Db+ub.b;}else if(Ha!=null){t[w]=Ha.format(ya);}else if(typeof ya==ub.c){t[w]=ya;}else if(typeof ya==ub.d&&Ea){if(!this.qK)this.qK=Ea.getNumberInstance(this.MJ);t[w]=this.qK.format(ya);}else if(ya==null){t[w]=ub.e;}else if(ya instanceof Date&&L){t[w]=L.getDateTimeInstance(1,1,this.MJ).format(ya);}else t[w]=ya.toString();}else t[w]=this.Cx[w];}return t.join(ub.f);};j.EM=function(){var
fb=ub.g;var
va=[];this.Cx=va;var
Bb=this.b6;var
ya=Bb.length;var
sb=0;var
w=false;var
Ya=[];while(sb<ya){var
Ka=Bb.indexOf(fb,sb);var
Lb=Bb.indexOf(ub.a,sb);if(Ka>=0&&(Ka<Lb||Lb<0)){if(Ka>sb)Ya[Ya.length]=Bb.substring(sb,Ka);var
Ga=Bb.indexOf(fb,Ka+1);if(Ga==Ka+1){Ya[Ya.length]=fb;sb=Ga+1;}else if(Ga>=0){Ya[Ya.length]=Bb.substring(Ka+1,Ga);sb=Ga+1;}else throw new
jsx3.Exception(jsx3._msg(ub.h,Ka,this));}else if(Lb>=0){if(Lb>sb)Ya[Ya.length]=Bb.substring(sb,Lb);va[va.length]=Ya.join(ub.f);Ya.splice(0,Ya.length);sb=Lb+1;var
ua=[];while(true){var
Ib=Bb.charAt(sb);if(Ib==ub.f)throw new
jsx3.Exception(jsx3._msg(ub.i,ya-ua.length-1,this));if(Ib==fb){if(Bb.charAt(sb+1)==fb){ua[ua.length]=Ib;sb=sb+2;}else{w=!w;sb=sb+1;}}else if(w){ua[ua.length]=Ib;sb++;}else if(Ib==ub.b){sb++;break;}else{ua[ua.length]=Ib;sb++;}}va[va.length]=this.IS(ua.join(ub.f));}else{Ya[Ya.length]=Bb.substring(sb);break;}}var
la=Ya.join(ub.f);if(la.length>0)va[va.length]=la;};k.yu={date:ub.j,time:ub.k,datetime:ub.l};j.IS=function(s){var
nb=s.split(ub.m);var
xb=Number(nb[0]);if(isNaN(xb))throw new
jsx3.Exception(jsx3._msg(ub.n,s,this));if(nb.length>1){var
mb=nb[1];var
X=nb.slice(2).join(ub.m);if(k.yu[mb]){if(!L)return [xb,null];var
yb=k.yu[mb];var
Aa=null;if(X==ub.o)Aa=1;else if(X==ub.p)Aa=2;else if(X==ub.q)Aa=3;else if(X==ub.r)Aa=4;if(Aa!=null||jsx3.util.strEmpty(X))return [xb,mb==ub.s?L[yb](Aa,Aa,this.MJ):L[yb](Aa,this.MJ)];return [xb,new
L(X,this.MJ)];}else if(mb==ub.d){if(!Ea)return [xb,null];if(jsx3.util.strEmpty(X))return [xb,Ea.getNumberInstance(this.MJ)];else if(X==ub.t)return [xb,Ea.getIntegerInstance(this.MJ)];else if(X==ub.u)return [xb,Ea.getPercentInstance(this.MJ)];else if(X==ub.v)return [xb,Ea.getCurrencyInstance(this.MJ)];else return [xb,new
Ea(X,this.MJ)];}else throw new
jsx3.Exception(jsx3._msg(ub.w,s,this));}else return [xb,null];};j.toString=function(){return this.b6;};});jsx3.Package.definePackage("jsx3.html",function(r){var
ub={oa:"unshift",k:"event",O:"html.adj",ea:/&([a-zA-Z_]+);/g,_:"disable-output-escp",p:" {",P:"-moz-opacity:",ra:/\//g,q:"}",V:"jsx:///images/icons/h.png",v:"span",ha:"&#",I:";",a:"",F:"<![CDATA[",u:"screen",U:"@mozilla.org/supports-string;1",j:"function",ka:"_",d:'<input type="text" id="_jsx3_html_b1" style="position:absolute;top:0px;left:-120px;width:100px;height:30px;padding:8px;margin:0px;"/>',z:'"',S:"@mozilla.org/widget/transferable;1",fa:"class",D:">",w:"div",K:"-->",R:"@mozilla.org/widget/clipboard;1",aa:/&lt;/g,g:'<div id="_jsx3_html_b2" style="position:absolute;top:0px;left:-116px;width:100px;height:24px;padding:8px;"></div>',B:'="',b:"overflow:hidden;",Q:"UniversalXPConnect",M:"beforeend",qa:"string",la:"img",f:"_jsx3_html_b1",ga:/<span class=\"disable-output-escp\">([\s\S]*?)<\/span>/g,y:' xmlns="',T:"text/unicode",ba:/&gt;/g,x:"<",e:"beforeEnd",ja:"none",c:"body",h:"_jsx3_html_b2",W:"jsx:///images/icons/v.png",H:"&",G:"]]>",ma:"id",i:/^on/,ca:/&quot;/g,A:" ",t:"media",pa:"push",N:"beforebegin",s:"text/css",X:"background-image:url(",Z:"_jsx_",o:"style",r:"type",C:"/>",na:"src",l:"head",ia:"jsx_image_loader",da:/&amp;/g,L:"html.set_outer",m:"undefined",Y:");",J:"<!--",n:"http://www.w3.org/1999/xhtml",E:"</"};var
kb=jsx3.gui.Event;r.MODE_IE_QUIRKS=0;r.MODE_FF_QUIRKS=1;r.MODE_IE_STRICT=2;r.MODE_FF_STRICT=3;r.Kf=ub.a;r.Fc=ub.b;r.getMode=function(g){if(r.BB==null){var
P=g!=null?g.ownerDocument:document;var
pa=g||document.getElementsByTagName(ub.c)[0];r.BB=0;var
ua=ub.d;jsx3.html.insertAdjacentHTML(pa,ub.e,ua);var
wa=P.getElementById(ub.f);if(wa.offsetHeight!=30){r.BB=jsx3.CLASS_LOADER.IE?2:3;}else{var
db=ub.g;jsx3.html.insertAdjacentHTML(pa,ub.e,db);var
W=P.getElementById(ub.h);if(parseInt(W.offsetWidth)>100)r.BB=1;pa.removeChild(W);}pa.removeChild(wa);}return r.BB;};r.getScrollSizeOffset=function(h,k){return 0;};r.addEventListener=function(b,n,s){n=n.replace(ub.i,ub.a);b.addEventListener(n,typeof s==ub.j?s:new
Function(ub.k,s),false);};r.removeEventListener=function(m,c,h){c=c.replace(ub.i,ub.a);m.removeEventListener(c,h,false);};r._FOCUSABLE={input:true,textarea:true,select:true,body:true,a:true,img:true,button:true,frame:true,iframe:true,object:true};r.isFocusable=function(p){return p.focus!=null&&(parseInt(p.tabIndex)>=0||r._FOCUSABLE[p.tagName.toLowerCase()]);};r.createRule=function(h,c,p){if(!p)p=document;var
y=p.getElementsByTagName(ub.l)[0];var
H=typeof p.createElementNS!=ub.m?p.createElementNS(ub.n,ub.o):p.createElement(ub.o);var
_=p.createTextNode(h+ub.p+c+ub.q);H.appendChild(_);H.setAttribute(ub.r,ub.s);H.setAttribute(ub.t,ub.u);y.appendChild(H);};r.getRuleByName=function(b){var
E=document.styleSheets;for(var
va=0;va<E.length;va++){var
Ea=E[va];for(var
da=0;da<Ea.cssRules.length;da++)if(Ea.cssRules[da].selectorText==b)return Ea.cssRules[da];}return null;};r.getRelativePosition=function(e,h){var
vb={W:h.offsetWidth,H:h.offsetHeight};var
pb=h.scrollLeft;var
pa=h.scrollTop;var
Za=0;var
w=0;var
V=h.offsetTop;var
Ua=h.offsetLeft;var
Hb=h;var
Ya=0;var
qb=0;var
Ba;if(h.offsetParent){qb=qb-h.offsetParent.scrollLeft;Ya=Ya-h.offsetParent.scrollTop;Ba=h.offsetParent.style.borderLeftWidth?parseInt(h.offsetParent.style.borderLeftWidth):0;if(!isNaN(Ba))Za=Za+Ba;Ba=h.offsetParent.style.borderTopWidth?parseInt(h.offsetParent.style.borderTopWidth):0;if(!isNaN(Ba))w=w+Ba;}while((h=h.offsetParent)!=null&&h!=e){V=V+h.offsetTop;Ua=Ua+h.offsetLeft;if(h.offsetParent){Ba=h.offsetParent.style.borderLeftWidth?parseInt(h.offsetParent.style.borderLeftWidth):0;if(!isNaN(Ba))Za=Za+Ba;Ba=h.offsetParent.style.borderTopWidth?parseInt(h.offsetParent.style.borderTopWidth):0;if(!isNaN(Ba))w=w+Ba;qb=qb+h.offsetParent.scrollLeft;Ya=Ya+h.offsetParent.scrollTop;}if(h.offsetParent){var
Fb=h.offsetParent.scrollTop;if(!isNaN(Fb))Ya=Ya-Fb;var
Q=h.offsetParent.scrollLeft;if(!isNaN(Q))qb=qb-Q;}}h=Hb;while((h=h.parentNode)!=null&&h!=e)if(h.parentNode){var
Fb=h.parentNode.scrollTop;if(!isNaN(Fb)&&Fb>0)Ya=Ya-Fb;var
Q=h.parentNode.scrollLeft;if(!isNaN(Q)&&Q>0)qb=qb-Q;}vb.L=Ua+qb+2*Za;vb.T=V+Ya+2*w;return vb;};r.scrollIntoView=function(m,s,b,a){var
Ma=m.parentNode;while(Ma!=null){var
rb=Ma.tagName.toLowerCase();if(rb==ub.v||rb==ub.w){var
Cb=r.getRelativePosition(Ma,m);if(Ma.clientWidth+Ma.scrollLeft<=Cb.L){Ma.scrollLeft=Cb.L+m.offsetWidth-Ma.clientWidth+b;}else if(Ma.clientWidth+Ma.scrollLeft<Cb.L+m.offsetWidth&&b!=null)Ma.scrollLeft=Cb.L+m.offsetWidth-Ma.clientWidth+b;if(Ma.scrollLeft>=Cb.L+m.offsetWidth){Ma.scrollLeft=Cb.L-b;}else if(Ma.scrollLeft>Cb.L&&b!=null)Ma.scrollLeft=Cb.L-b;if(Ma.clientHeight+Ma.scrollTop<=Cb.T){Ma.scrollTop=Cb.T+m.offsetHeight-Ma.clientHeight+a;}else if(Ma.clientHeight+Ma.scrollTop<Cb.T+m.offsetHeight&&a!=null)Ma.scrollTop=Cb.T+m.offsetHeight-Ma.clientHeight+a;if(Ma.scrollTop>=Cb.T+m.offsetHeight){Ma.scrollTop=Cb.T-a;}else if(Ma.scrollTop>Cb.T&&a!=null)Ma.scrollTop=Cb.T-a;}if(Ma==s)break;Ma=Ma.parentNode;}};r.getOuterHTML=function(i){if(window.SVGElement&&i instanceof SVGElement){return (new
XMLSerializer()).serializeToString(i);}else{var
H=[];switch(i.nodeType){case 1:H[H.length]=ub.x+i.nodeName.toLowerCase();if(i.namespaceURI)H[H.length]=ub.y+i.namespaceURI+ub.z;for(var
Ma=0;Ma<i.attributes.length;Ma++){var
Eb=i.attributes.item(Ma);if(Eb.nodeValue!=null)H[H.length]=ub.A+Eb.nodeName+ub.B+Eb.nodeValue+ub.z;}if(i.childNodes.length==0)H[H.length]=ub.C;else H[H.length]=ub.D+i.innerHTML+ub.E+i.nodeName.toLowerCase()+ub.D;break;case 3:H[H.length]=i.nodeValue;break;case 4:H[H.length]=ub.F+i.nodeValue+ub.G;break;case 5:H[H.length]=ub.H+i.nodeName+ub.I;break;case 8:H[H.length]=ub.J+i.nodeValue+ub.K;break;}return H.join(ub.a);}};r.setOuterHTML=function(k,c){if(window.SVGElement&&k instanceof SVGElement){if(!c){k.parentNode.removeChild(k);}else{var
Q=k.ownerDocument.createRange();Q.setStartBefore(k);var
Ca=Q.createContextualFragment(c);k.parentNode.replaceChild(Ca,k);}}else try{var
Q=k.ownerDocument.createRange();Q.setStartBefore(k);var
Ca=Q.createContextualFragment(c);k.parentNode.replaceChild(Ca,k);}catch(Kb){var
eb=c;throw new
jsx3.Exception(jsx3._msg(ub.L,k,eb),jsx3.NativeError.wrap(Kb));}};r.removeNode=function(a){a.parentNode.removeChild(a);};r.setInnerText=function(c,d){for(var
ia=c.childNodes.length-1;ia>=0;ia--)c.removeChild(c.childNodes[ia]);c.appendChild(c.ownerDocument.createTextNode(d));};r.insertAdjacentHTML=function(s,h,k){if(h.toLowerCase()==ub.M){var
Nb=s.ownerDocument.createRange();Nb.setStartAfter(s);var
U=Nb.createContextualFragment(k);s.appendChild(U);return k;}else if(h.toLowerCase()==ub.N){var
Nb=s.ownerDocument.createRange();Nb.setStartBefore(s);var
U=Nb.createContextualFragment(k);s.parentNode.insertBefore(U,s);return k;}else throw new
jsx3.Exception(jsx3._msg(ub.O,h));};r.updateCSSOpacity=function(j,s){j.style.MozOpacity=s.toString();};r.getCSSOpacity=function(g){return ub.P+g+ub.I;};r.copy=function(p){netscape.security.PrivilegeManager.enablePrivilege(ub.Q);var
cb=Components.classes[ub.R].createInstance(Components.interfaces.nsIClipboard);if(cb){var
X=Components.classes[ub.S].createInstance(Components.interfaces.nsITransferable);if(X){X.addDataFlavor(ub.T);var
ua=Components.classes[ub.U].createInstance(Components.interfaces.nsISupportsString);ua.data=p;X.setTransferData(ub.T,ua,p.length*2);var
Wa=Components.interfaces.nsIClipboard;cb.setData(X,null,Wa.kGlobalClipboard);}}};r.paste=function(){netscape.security.PrivilegeManager.enablePrivilege(ub.Q);var
D=Components.classes[ub.R].getService(Components.interfaces.nsIClipboard);if(D){var
qb=Components.classes[ub.S].createInstance(Components.interfaces.nsITransferable);if(qb){qb.addDataFlavor(ub.T);D.getData(qb,D.kGlobalClipboard);var
aa={};var
Q={};qb.getTransferData(ub.T,aa,Q);if(aa)aa=aa.value.QueryInterface(Components.interfaces.nsISupportsString);return aa?aa.data.substring(0,Q.value/2):null;}}return null;};r.jY=jsx3.resolveURI(ub.V);r.Qs=jsx3.resolveURI(ub.W);r.getCSSFade=function(n){return r.getCSSPNG(n?r.jY:r.Qs);};r.getCSSPNG=function(s){return ub.X+s+ub.Y;};r.getJSXParent=function(q,s){while(q!=null){if(q.id&&q.id.indexOf(ub.Z)==0){var
xb=s?s.getJSXById(q.id):jsx3.GO(q.id);if(xb!=null)return xb;}q=q.parentNode;}return null;};r.removeOutputEscaping=function(a){var
eb=a?[a]:[];while(eb.length>0){var
ta=eb.shift();if(ta.nodeName&&ta.nodeName.toLowerCase()==ub.v&&ta.className==ub._){ta.innerHTML=ta.innerHTML.replace(ub.aa,ub.x).replace(ub.ba,ub.D).replace(ub.ca,ub.z).replace(ub.da,ub.H).replace(ub.ea,r.Eu);ta.removeAttribute(ub.fa);}else if(ta.childNodes)eb.push.apply(eb,this.nodesToArray(ta.childNodes));}};r.removeOutputEscapingSpan=function(e){return e.replace(ub.ga,function(d,j){return j.replace(ub.aa,ub.x).replace(ub.ba,ub.D).replace(ub.ca,ub.z).replace(ub.da,ub.H).replace(ub.ea,r.Eu);});};r.yG={nbsp:160,copy:169,reg:174,deg:176,middot:183,le:8804,ge:8805,lt:60,gt:62,euro:8364,ndash:8211,mdash:8212,lsquo:8216,rsquo:8217,ldquo:8220,rdquo:8221,permil:8240};r.Eu=function(d,i){var
Pa=r.yG[i];return Pa?ub.ha+Pa+ub.I:d;};r.SU=ub.ia;r.loadImages=function(e){var
L=document.getElementById(r.SU);if(L==null){var
Ra=document.getElementsByTagName(ub.c)[0];if(Ra){L=document.createElement(ub.w);L.id=r.SU;L.style.display=ub.ja;Ra.insertBefore(L,Ra.firstChild);}else return;}var
_a=e instanceof Array?e:arguments;for(var
Ua=0;Ua<_a.length;Ua++){if(!_a[Ua])continue;var
v=jsx3.resolveURI(_a[Ua]);var
va=r.SU+ub.ka+v;if(document.getElementById(va)==null){var
pb=document.createElement(ub.la);pb.setAttribute(ub.ma,va);pb.setAttribute(ub.na,v);L.appendChild(pb);}}};r.updateRule=function(i,c,b){var
vb=jsx3.html.getRuleByName(i);if(vb)vb.style[c]=b;};r.getElementById=function(l,e,s){return this.findElements(l,function(f){return f.id==e;},s,false,false,true);};r.getElementByTagName=function(j,h,q){h=h.toLowerCase();return this.findElements(j,function(p){return p.tagName.toLowerCase()==h;},q,false,false,true);};r.getElementsByTagName=function(s,q,g){q=q.toLowerCase();return this.findElements(s,function(e){return e.tagName.toLowerCase()==q;},g,true,false,true);};r.findElements=function(j,b,q,h,f,l){var
Ta=q?ub.oa:ub.pa;var
Sa=h?[]:null;var
nb=l?[j]:this.nodesToArray(j.childNodes);while(nb.length>0){var
Za=nb.shift();if(b.call(null,Za))if(h)Sa[Sa.length]=Za;else return Za;if(!f)nb[Ta].apply(nb,this.nodesToArray(Za.childNodes));}return Sa;};r.getElmUpByTagName=function(l,a,e){return r.findElementUp(l,function(o){return o.nodeName.toLowerCase()==a;},e);};r.findElementUp=function(a,s,m){var
Na=a.ownerDocument.documentElement;var
rb=m?a:a.parentNode;while(rb!=null){if(s.call(null,rb))return rb;if(rb==Na)break;rb=rb.parentNode;}return null;};r.selectSingleElm=function(k,p){var
Ib=1,U=arguments;if(arguments.length==2)if(typeof p==ub.qa){Ib=0;U=p.split(ub.ra);}else if(p instanceof Array){Ib=0;U=p;}var
R=k;for(var
aa=Ib;R!=null&&aa<U.length;aa++){var
Va=U[aa];if(!isNaN(Va)){var
la=Number(Va);var
wa=R.childNodes.length;var
nb=0,Ra=0;for(;nb<wa&&Ra<la;nb++)if(R.childNodes[nb].nodeType==1)Ra++;R=R.childNodes[nb];}else throw new
jsx3.Exception();}return R;};r.nodesToArray=function(k){var
T=new
Array(k.length);for(var
R=0;R<k.length;R++)T[R]=k[R];return T;};r.getSelection=function(o){return new
r.Selection(o);};r.focusNext=function(o,q){var
fa=o;while(fa.lastChild)fa=fa.lastChild;while(!r.isFocusable(fa)&&fa!=o)fa=fa.previousSibling||fa.parentNode;if(fa!=o||r.isFocusable(fa))if(fa.onfocus!=null){var
eb=fa.onfocus;fa.onfocus=null;fa.focus();jsx3.sleep(function(){fa.onfocus=eb;});}else fa.focus();};r.focusPrevious=function(p,d){var
qa=this.findElements(p,function(m){return r.isFocusable(m);},true,false,false,true);if(qa!=null)if(qa.onfocus!=null){var
Bb=qa.onfocus;qa.onfocus=null;qa.focus();jsx3.sleep(function(){qa.onfocus=Bb;});}else qa.focus();};});jsx3.Class.defineClass("jsx3.html.Selection",null,null,function(m,g){var
ub={a:"end"};g.init=function(p){this.tK={elm:p,start:p.selectionStart,end:p.selectionEnd,scrollt:p.scrollTop,scrolll:p.scrollLeft};};g.getStartIndex=function(){return this.tK.start;};g.getEndIndex=function(){return this.tK.end;};g.setRange=function(l,s){this.tK.start=l;this.tK.end=s;this.tK.elm.setSelectionRange(l,s);};g.getOffsetLeft=function(){if(this.tK.pos==null)this.tK.pos=jsx3.html.getRelativePosition(null,this.tK.elm);return this.tK.pos.L;};g.getOffsetTop=function(){if(this.tK.pos==null)this.tK.pos=jsx3.html.getRelativePosition(null,this.tK.elm);return this.tK.pos.T;};g.getText=function(){return this.tK.elm.value.substring(this.tK.start,this.tK.end);};g.setText=function(h){this.tK.elm.value=this.tK.elm.value.substring(0,this.tK.start)+h+this.tK.elm.value.substring(this.tK.end);this.tK.elm.setSelectionRange(this.tK.start,this.tK.start+h.length);this.tK.elm.end=this.tK.elm.selectionEnd;};g.insertCaret=function(q){this.tK.elm.focus();if(q==ub.a){this.tK.elm.setSelectionRange(this.tK.elm.end,this.tK.elm.end);}else throw new
jsx3.Exception();this.tK.elm.scrollTop=this.tK.scrollt;this.tK.elm.scrollLeft=this.tK.scrolll;};});jsx3.Class.defineClass("jsx3.app.Cache",null,[jsx3.util.EventDispatcher],function(c,l){var
ub={o:"strId",d:"http://xsd.tns.tibco.com/gi/cache",k:"_jsxcacheid",r:"gU",c:"change",h:'<error xmlns="',p:"objDocument",g:'<timeout xmlns="',l:"response",q:"Fx",b:"add",i:"*",m:"timeout",a:"remove",f:'"/>',j:"p1",n:"error",e:'<loading xmlns="'};var
yb=jsx3.xml.Document;c.REMOVE=ub.a;c.ADD=ub.b;c.CHANGE=ub.c;c.ASYNC_TIMEOUT=60000;c.XSDNS=ub.d;c.zt=(new
yb()).loadXML(ub.e+c.XSDNS+ub.f);c.Iw=(new
yb()).loadXML(ub.g+c.XSDNS+ub.f);c.X7=(new
yb()).loadXML(ub.h+c.XSDNS+ub.f);l.init=function(){this.Fx={};this.gU=[];};l.addParent=function(d){this.gU.push(d);};l.clearById=function(j){var
Db=this.Fx[j];if(Db){delete this.Fx[j];this.publish({subject:j,action:ub.a});this.publish({subject:ub.c,id:j,action:ub.a});return Db.kD;}};l.isSystem=function(i){return false;};l.clearByTimestamp=function(e){if(e instanceof Date)e=e.getTime();var
Z=false;var
H=[];for(var ab in this.Fx){var
Ab=this.Fx[ab];if(Ab.tH<e){delete this.Fx[ab];this.publish({subject:ab,action:ub.a});H.push(ab);}}if(H.length>0)this.publish({subject:ub.c,ids:H,action:ub.a});return H;};l.getDocument=function(q){if(this.Fx[q]!=null)return this.Fx[q].kD;for(var
u=0;u<this.gU.length;u++){var
nb=this.gU[u].getDocument(q);if(nb!=null)return nb;}return null;};l.getOrOpenDocument=function(h,f,e){if(f==null)f=h.toString();return this.getDocument(f)||this.JD(h,f,e,false);};l.openDocument=function(q,p,o){return this.JD(q,p,o,false);};l.getOrOpenAsync=function(b,m,a){if(m==null)m=b.toString();return this.getDocument(m)||this.JD(b,m,a,true);};l.JD=function(e,i,h,f){if(h==null)h=yb.jsxclass;if(i==null)i=e.toString();var
gb=h.newInstance();gb.setAsync(f);if(f){gb.subscribe(ub.i,this,ub.j);gb._jsxcacheid=i;gb.load(e,c.ASYNC_TIMEOUT);gb=c.zt.cloneDocument();}else gb.load(e);this.setDocument(i,gb);return gb;};l.p1=function(o){var
fb=o.target;var
vb=o.subject;var
Mb=fb._jsxcacheid;delete fb[ub.k];fb.unsubscribe(ub.i,this);if(this.Fx)if(vb==ub.l){this.setDocument(Mb,fb);}else if(vb==ub.m){this.setDocument(Mb,c.Iw.cloneDocument());}else if(vb==ub.n){var
_=c.X7.cloneDocument();_.setAttribute(ub.n,fb.getError().toString());this.setDocument(Mb,_);}};l.setDocument=function(d,o){if(d==null)throw new
jsx3.IllegalArgumentException(ub.o,d);if(o==null)throw new
jsx3.IllegalArgumentException(ub.p,o);var
Cb={};Cb.tH=(new
Date()).getTime();Cb.kD=o;var
gb=this.Fx[d]?ub.c:ub.b;this.Fx[d]=Cb;this.publish({subject:d,action:gb});this.publish({subject:ub.c,action:gb,id:d});};l.getTimestamp=function(i){var
cb=this.Fx[i];return cb!=null?cb.tH:null;};l.keys=function(){var
Qa=[];for(var Fb in this.Fx)Qa[Qa.length]=Fb;return Qa;};l.destroy=function(){delete this[ub.q];delete this[ub.r];};});jsx3.Cache=jsx3.app.Cache;jsx3.Class.defineClass("jsx3.app.Properties",null,null,function(m,d){var
ub={d:"eval",i:"load",k:"strValue",a:"_global",h:"props.eval",c:"jsxid",f:"1",j:"undefined",g:"true",b:"./record",e:"jsxtext"};var
pb=jsx3.util.Logger.getLogger(m.jsxclass.getName());m.MC=ub.a;d.init=function(){this.gU=[];this.r7=[];this.OE=[m.MC];this.dN={};this.dN[m.MC]={};this.AU={};this.kE=false;this.HO={};this.nR=false;};d.loadXML=function(e,b){var
F=new
jsx3.util.Timer(m.jsxclass,b||(e instanceof jsx3.xml.Document?e.getSourceURL():e.getNodeName()));if(b==null){b=m.MC;}else if(jsx3.util.arrIndexOf(this.OE,b)<0)this.OE.splice(1,0,b);var
sb=this.dN[b];if(sb==null)sb=this.dN[b]={};for(var
ea=e.selectNodeIterator(ub.b);ea.hasNext();){var
R=ea.next();var
pa=R.getAttribute(ub.c);var
eval=R.getAttribute(ub.d);var
Da=R.getAttribute(ub.e);if(eval==ub.f||eval==ub.g)try{Da=isNaN(Da)?jsx3.eval(Da):Number(Da);}catch(Kb){pb.warn(jsx3._msg(ub.h,pa,Da),jsx3.NativeError.wrap(Kb));}sb[pa]=Da;}this.kE=true;F.log(ub.i);};d.addParent=function(b){this.gU.splice(0,0,b);b.r7.push(this);this.uR(true);};d.removeParent=function(a){var
oa=jsx3.util.arrIndexOf(this.gU,a);if(oa>=0){this.gU.splice(oa,1);a.RT(this);this.uR(true);}};d.RT=function(s){var
Ha=jsx3.util.arrIndexOf(this.r7,s);if(Ha>=0)this.r7.splice(Ha,1);};d.removeAllParents=function(){if(this.gU.length>0){for(var
sa=0;sa<this.gU.length;sa++)this.gU[sa].RT(this);this.gU=[];this.nR=false;this.HO={};}};d.getParents=function(){return this.gU.concat();};d.containsKey=function(h){if(this.kE)this.mO();return typeof this.AU[h]!=ub.j;};d.getKeys=function(){if(this.kE)this.mO();var
x=[];for(var da in this.AU)x[x.length]=da;return x;};d.get=function(g){if(this.kE)this.mO();if(typeof this.AU[g]!=ub.j)return this.AU[g];if(this.nR)this.pL();return this.HO[g];};d.set=function(s,h){if(typeof h==ub.j)throw new
jsx3.IllegalArgumentException(ub.k,h);this.dN[m.MC][s]=h;this.AU[s]=h;this.uR();};d.remove=function(f){for(var aa in this.dN)delete this.dN[aa][f];delete this.AU[f];this.uR();};d.mO=function(){this.kE=false;var
xa=this.AU={};for(var
ca=this.OE.length-1;ca>=0;ca--){var
fa=this.dN[this.OE[ca]];for(var ab in fa)xa[ab]=fa[ab];}};d.pL=function(){this.nR=false;var
X=this.HO={};for(var
Oa=this.gU.length-1;Oa>=0;Oa--){var
Bb=this.gU[Oa];if(Bb.kE)Bb.mO();if(Bb.nR)Bb.pL();var
Ab=Bb.AU;var
rb=Bb.HO;for(var da in rb)X[da]=rb[da];for(var da in Ab)X[da]=Ab[da];}};d.uR=function(b){var
Ta=b?[this]:this.r7.concat();while(Ta.length>0){var
Ba=Ta.shift();if(!Ba.nR){Ba.nR=true;Ta.push.apply(Ta,Ba.r7);}}};});jsx3.Class.defineClass("jsx3.app.PropsBundle",jsx3.app.Properties,null,function(e,j){var
ub={o:"key",d:"meta",k:"propsbundle",r:"locales",c:".",h:"propbn.err_key",p:"Error loading localized bundle meta file ",g:"propbn.err",l:"propbn.err_file",q:": ",b:".xml",i:"/",m:"",a:"__root",u:/\s*,\s*/,f:"::",t:"jsxtext",j:"jsxnamespace",n:"/data/locale",s:"/data/record[@jsxid='locales']",e:"default"};var
J=jsx3.util.Locale;var
Ta=jsx3.util.Logger.getLogger(e.jsxclass.getName());e.BL=ub.a;e.j8=ub.b;e.KB=ub.c;e.NJ=ub.d;e.eL=ub.e;e.R5=-1;e.Z5={};e.fI={};e.getProps=function(q,d,r){q=q.toString();if(d==null)d=jsx3.System.getLocale();var
qa=q+ub.f+d.toString();if(e.fI[qa]==null){if(e.Z5[q]==null){e.jC(q,e.BL,r);if(e.Z5[q]==null)e.Qp(q,r);}if(e.Z5[q]==e.R5)throw new
jsx3.Exception(jsx3._msg(ub.g,q));var
N=e.QW(q,d);var
y=q+ub.f+N;if(e.fI[y]==null)e.jC(q,N,r);if(e.fI[y]==e.R5)throw new
jsx3.Exception(jsx3._msg(ub.h,q,N));y=q+ub.f+e.QW(q,d);e.fI[qa]=e.fI[y];}return e.fI[qa];};e.jC=function(a,o,n){var
fb=a+ub.f+o;var
ja=null;if(jsx3.util.strEndsWith(a,ub.i)){ja=a+(o==e.BL?e.eL:o)+e.j8;}else{var
Lb=a.lastIndexOf(ub.c);ja=o==e.BL?a:a.substring(0,Lb)+e.KB+o+a.substring(Lb);}var
Oa=null;if(n!=null){Oa=n.getOrOpenDocument(ja);}else{Oa=new
jsx3.xml.Document();Oa.load(ja);}if(!Oa.hasError()){if(Oa.getAttribute(ub.j)==ub.k){e.GL(a,Oa);e.br(a,Oa);}else e.bA(a,o,Oa,n);}else{Ta.error(jsx3._msg(ub.l,ja,Oa.getError()));e.fI[fb]=e.R5;}};e.bA=function(b,p,l,n){var
W=new
e();W.loadXML(l);W.ks=b;W.MJ=J.valueOf(p==e.BL?ub.m:p);e.fI[b+ub.f+p]=W;if(p!=e.BL){var
U=J.valueOf(p).getSearchPath()[1];if(U!=null)W.addParent(e.getProps(b,U,n));}};e.br=function(c,m){for(var
y=m.selectNodeIterator(ub.n);y.hasNext();){var
yb=y.next();var
La=yb.getAttribute(ub.o);if(jsx3.util.strEmpty(La))La=e.BL;e.bA(c,La,yb);e.Z5[c][La]=true;}};e.QW=function(s,f){var
ya=e.Z5[s];var
z=f.getSearchPath();for(var
S=0;S<z.length;S++)if(z[S].toString().length>0&&ya[z[S].toString()])return z[S].toString();return e.BL;};e.Qp=function(s,p){var
Db=null;if(jsx3.util.strEndsWith(s,ub.i)){Db=s+e.NJ+e.j8;}else{var
W=s.lastIndexOf(ub.c);Db=s.substring(0,W)+e.KB+e.NJ+s.substring(W);}var
zb=null;if(p!=null){zb=p.getDocument(Db)||p.openDocument(Db,Db);}else{zb=new
jsx3.xml.Document();zb.load(Db);}var
tb=false;if(!zb.hasError()){e.GL(s,zb);tb=true;}else Ta.error(ub.p+Db+ub.q+zb.getError());if(!tb)e.Z5[s]=e.R5;};e.GL=function(a,k){var
Ya=k.getAttribute(ub.r);if(Ya==null){var
t=k.selectSingleNode(ub.s);if(t!=null)Ya=t.getAttribute(ub.t);}if(e.Z5[a]==null)e.Z5[a]={};if(Ya!=null){var
aa=Ya.split(ub.u);for(var
I=0;I<aa.length;I++)if(aa[I])e.Z5[a][aa[I]]=true;}};j.getLocale=function(){return this.MJ;};j.getPath=function(){return this.ks;};e.clearCache=function(f,l){if(f){delete e.Z5[f];var
za=f+ub.f;for(var sa in e.fI)if(sa.indexOf(za)==0)delete e.fI[sa];if(l){var
wa=l.keys();for(var
Na=0;Na<wa.length;Na++)if(wa[Na].indexOf(f)==0)l.clearById(wa[Na]);}}else{e.Z5={};e.fI={};}};});jsx3.Class.defineClass("jsx3.lang.System",null,null,function(g,o){var
ub={o:"3.5.0.14",d:"",k:"apppath",c:"jsx:///locale/locale.xml",h:/\./,p:/\d/,g:"namespace",l:"user:",q:"3.5.0",b:"ljss",i:"system",m:"jsxuser:/addins/",a:"_",f:"_jsx_",j:"apppathrel",n:"/",e:" "};g.LJSS=new
jsx3.app.Properties();g.JSS=new
jsx3.app.Properties();g.JSS.addParent(g.LJSS);g.H3={};g.getProperty=function(h){return g.JSS.get(h);};g.getLocale=function(){if(g.MJ==null&&jsx3.util.Locale){var
Fb=jsx3.app.Browser.getLocaleString();if(Fb){var
gb=Fb.split(ub.a);g.MJ=new
jsx3.util.Locale(gb[0],gb[1]);}else g.MJ=jsx3.util.Locale.US;}return g.MJ;};g.setLocale=function(p){if(p!=g.MJ){g.JSS.removeParent(g.getLocaleProperties());g.MJ=p;g.JSS.addParent(g.getLocaleProperties());}};g.reloadLocalizedResources=function(){g.LJSS.removeAllParents();if(jsx3.app.PropsBundle){var
xa=jsx3.lang.ClassLoader.INCLUDES;for(var
t=0;t<xa.length;t++){var
F=F[t];if(F.type==ub.b){var
R=jsx3.net.URIResolver.JSX.resolveURI(F.src);g.LJSS.addParent(jsx3.app.PropsBundle.getProps(R,g.getLocale(),jsx3.getSystemCache()));}}}};g.IV=jsx3.resolveURI(ub.c);g.getLocaleProperties=function(a){if(!g.BE)g.BE=jsx3.getSystemCache().getDocument(g.IV)!=null;if(g.BE){if(a==null)a=g.getLocale();return jsx3.app.PropsBundle.getProps(g.IV,a);}else return new
jsx3.app.Properties();};g.getMessage=function(j,a){var
D=g.LJSS.get(j);var
Cb=ub.d;if(arguments.length>1){var
Aa=jsx3.Method.argsAsArray(arguments,1);if(D!=null&&jsx3.util.MessageFormat){try{var
Hb=new
jsx3.util.MessageFormat(D);return Hb.format(Aa);}catch(Kb){}}else Cb=ub.e+Aa.join(ub.e);}if(D==null)D=j;return D+Cb;};jsx3.lang.System.GO=function(q,p){var
I=null;if(q!=null)if(q.indexOf(ub.f)==0){var
da=jsx3.app.DOM.getNamespaceForId(q);if(p&&p!=da)return null;if(g.H3[da])I=g.H3[da].getJSXById(q);}else if(p){if(g.H3[p])I=g.H3[p].getJSXByName(q);}else for(var pa in g.H3)if((I=g.H3[pa].getJSXByName(q))!=null)return I;return I;};g.getApp=function(i){return g.H3[i];};g.getAllApps=function(){var
Ta=[];for(var oa in g.H3)Ta.push(g.H3[oa]);return Ta;};g.registerApp=function(r){var
xa=r.getEnv(ub.g);var
sa=xa.split(ub.h);var
O=window;for(var
T=0;T<sa.length-1;T++){if(O[sa[T]]==null)O[sa[T]]={};O=O[sa[T]];}O[sa[sa.length-1]]=r;g.H3[xa]=r;};g.deregisterApp=function(q){var
wa=q.getEnv(ub.g);var
Ga=wa.split(ub.h);var
J=q.getEnv(ub.i)?jsx3:window;for(var
eb=0;eb<Ga.length-1;eb++){if(J[Ga[eb]]==null)J[Ga[eb]]={};J=J[Ga[eb]];}if(J[Ga[Ga.length-1]]==q)if(J==window)J[Ga[Ga.length-1]]=null;else delete J[Ga[Ga.length-1]];if(g.H3[wa]==q)delete g.H3[wa];};g.activateApp=function(a){jsx3.registerApp(a);};g.getLoadedAppByPath=function(c){for(var aa in g.H3){var
Ha=g.H3[aa];if(Ha.getEnv(ub.j)==c||Ha.getEnv(ub.k)==c)return Ha;}return null;};g.getAddins=function(){return [];};g.Jp=[];g.cF={};g.addinKeyToPath=function(r){if(r.indexOf(ub.l)==0){return jsx3.resolveURI(ub.m+r.substring(5)+ub.n);}else return jsx3.resolveURI(jsx3.ADDINSPATH+r+ub.n);};g.registerAddin=function(q,p){var
Ma=q.split(ub.h);var
w=window;for(var
K=0;K<Ma.length-1;K++){var
Ab=Ma[K];if(w[Ab]==null)w[Ab]={};w=w[Ab];}w[Ma[Ma.length-1]]=p;g.Jp.push(p);g.cF[p.getKey()]=p;};g.getLoadedAddins=function(){return g.Jp.concat();};g.getLoadedAddinByKey=function(h){return g.cF[h];};g.getVersion=function(){var
Cb=ub.o;return Cb.match(ub.p)?Cb:ub.q;};});jsx3.GO=jsx3.lang.System.GO;jsx3.getApp=jsx3.lang.System.getApp;jsx3.registerApp=jsx3.lang.System.registerApp;jsx3.activateApp=jsx3.lang.System.activateApp;jsx3.deregisterApp=jsx3.lang.System.deregisterApp;jsx3.getAddins=jsx3.lang.System.getAddins;jsx3.getVersion=jsx3.lang.System.getVersion;jsx3.Class.defineInterface("jsx3.xml.CDF",null,function(e,b){var
ub={k:"jsxkeycode",O:"undefined",p:"cdf.prop_ins",P:"//@",q:"cdf.prop_del",V:"jsx_",v:"cdf.adopt_dest",I:"name()='",a:"data",F:'"]',u:"cdf.adopt_col",U:' jsxid="jsxroot"/>',j:"jsxtip",d:"jsxtext",z:"cdf.before_rec",S:"}",D:"']",w:"cdf.adopt_src",K:"substring(.,1,1)='{' and substring(.,string-length(.),1)='}'",R:"{",g:"jsxselected",B:"'",b:"record",Q:" | //@",M:") and (",f:"jsxdisabled",y:"cdf.before_col",T:"<",x:"adoptRecord() no object with id: ",e:"jsxexecute",c:"jsxid",h:"jsxunselectable",W:"3.00.00",H:/\[(\w+)\]$/,G:"//record[not(@jsxid)]",i:"jsximg",A:"",t:"string",N:")]",s:"intAction",o:"objRecordNode",r:"strRecordId",C:"//*[@jsxid='",l:"jsxstyle",L:"//@*[(",m:"jsxclass",J:" or ",n:"objRecord",E:'//*[@jsxid="'};e.aM=jsx3.util.Logger.getLogger(e.jsxclass.getName());e.DELETE=0;e.INSERT=1;e.UPDATE=2;e.INSERTBEFORE=3;e.ELEM_ROOT=ub.a;e.ELEM_RECORD=ub.b;e.ATTR_ID=ub.c;e.ATTR_TEXT=ub.d;e.ATTR_EXECUTE=ub.e;e.ATTR_DISABLED=ub.f;e.ATTR_SELECTED=ub.g;e.ATTR_UNSELECTABLE=ub.h;e.ATTR_IMG=ub.i;e.ATTR_TIP=ub.j;e.ATTR_KEYCODE=ub.k;e.Wo=[ub.d,ub.j,ub.i,ub.k,ub.l,ub.m];e.cN=1;b.insertRecord=function(q,i,c){if(q instanceof Object){var
Y=this.getXML();var
I=1;var
Jb=Y.selectSingleNode(this.Ee(q.jsxid));if(Jb!=null){I=2;}else{Jb=Y.createNode(1,ub.b);var
kb=i!=null?Y.selectSingleNode(this.Ee(i)):null;if(kb==null)kb=Y.getRootNode();kb.appendChild(Jb);}for(var pb in q)if(q[pb]!=null)Jb.setAttribute(pb,q[pb].toString());if(c!==false)this.redrawRecord(q[ub.c],I);return Jb;}else throw new
jsx3.IllegalArgumentException(ub.n,q);};b.insertRecordNode=function(o,g,k){if(o instanceof jsx3.xml.Entity){var
yb=this.getXML();var
na=g!=null?yb.selectSingleNode(this.Ee(g)):null;if(na==null)na=yb.getRootNode();na.appendChild(o);if(k!==false)this.redrawRecord(o.getAttribute(ub.c),1);}else throw new
jsx3.IllegalArgumentException(ub.o,o);};b.insertRecordProperty=function(r,l,g,m){var
O=this.getRecordNode(r);if(O!=null){O.setAttribute(l,g);if(m!==false)this.redrawRecord(r,2);}else e.aM.debug(jsx3._msg(ub.p,r));return this;};b.deleteRecordProperty=function(f,s,a){var
ca=this.getXML();var
Pa=ca.selectSingleNode(this.Ee(f));if(Pa!=null){Pa.removeAttribute(s);if(a!==false)this.redrawRecord(f,2);}else e.aM.debug(jsx3._msg(ub.q,f));};b.redrawRecord=jsx3.Method.newAbstract(ub.r,ub.s);b.adoptRecord=function(g,d,p,r){var
za=g;if(typeof g==ub.t)za=jsx3.GO(g);if(za!=null){var
Lb=za.getRecordNode(d);if(Lb!=null){var
Ib=p==null?this.getXML().getRootNode():this.getRecordNode(p);if(Ib!=null){var
ab=Ib;while(ab!=null&&!ab.equals(Lb))ab=ab.getParent();if(ab==null){if(za!=this){var
ea=this.getRecordNode(d);if(ea!=null){e.aM.debug(jsx3._msg(ub.u,this,d));return;}}var
sa=za.deleteRecord(d);this.insertRecordNode(sa,p,r);return this.getRecordNode(d);}else{}}else e.aM.debug(jsx3._msg(ub.v,this,d,p));}else e.aM.debug(jsx3._msg(ub.w,this,d,za));}else e.aM.debug(ub.x+g);};b.insertRecordBefore=function(o,l,g){var
zb=this.getXML();var
z=zb.selectSingleNode(this.Ee(o.jsxid));if(z){e.aM.debug(jsx3._msg(ub.y,o.jsxid,this));}else{var
Ma=zb.selectSingleNode(this.Ee(l));if(Ma!=null&&Ma.getParent()!=null){var
db=this.insertRecord(o,Ma.getParent().getAttribute(ub.c),false);if(db){this.adoptRecordBefore(this,o.jsxid,l,g);return db;}}else e.aM.debug(jsx3._msg(ub.z,l,this));}};b.adoptRecordBefore=function(d,j,c,q){var
L=d;if(typeof d==ub.t)L=jsx3.GO(d);if(L==this&&j==c){}else{var
ab=this.getRecordNode(c).getParent();var
Ea=ab.getAttribute(ub.c);var
Ra=this.adoptRecord(d,j,Ea,false);if(Ra){var
wb=this.getRecordNode(c);ab.insertBefore(Ra,wb);if(q!==false)this.redrawRecord(Ra.getAttribute(ub.c),3);return Ra;}}};b.deleteRecord=function(r,m){var
ua=this.getXML();var
Ta=ua.selectSingleNode(this.Ee(r));if(Ta!=null){Ta=Ta.getParent().removeChild(Ta);if(m!==false)this.redrawRecord(r,0);return Ta;}return null;};b.getRecord=function(r){var
v=this.getRecordNode(r);if(v!=null){var
wb={};var
eb=v.getAttributeNames();for(var
Wa=0;Wa<eb.length;Wa++)wb[eb[Wa]]=v.getAttribute(eb[Wa]);return wb;}return null;};b.getRecordNode=function(f){var
nb=this.getXML();return nb.selectSingleNode(this.Ee(f));};b.Ee=function(o){return (o+ub.A).indexOf(ub.B)==-1?ub.C+o+ub.D:ub.E+o+ub.F;};b.resetData=function(g){if(jsx3.xml.Cacheable&&this.instanceOf(jsx3.xml.Cacheable)){this.clearXmlData();if(g)this.repaint();}};b.reloadFromSource=function(g){if(jsx3.xml.Cacheable&&this.instanceOf(jsx3.xml.Cacheable))this.resetXmlCacheData();};b.assignIds=function(){var
Wa=this.getXML();for(var
Y=Wa.selectNodeIterator(ub.G);Y.hasNext();){var
bb=Y.next();bb.setAttribute(ub.c,e.getKey());}};e.n8=ub.H;b.convertProperties=function(s,n,o){if(n==null)n=e.Wo;else if(o)n.push.apply(n,e.Wo);if(jsx3.getXmlVersion()>=4&&!jsx3.CLASS_LOADER.SAF){var
w=new
Array(n.length);for(var
x=0;x<n.length;x++)w[x]=ub.I+n[x]+ub.B;var
ra=w.join(ub.J);var
yb=ub.K;var
C=ub.L+ra+ub.M+yb+ub.N;for(var
x=this.getXML().selectNodeIterator(C);x.hasNext();){var
y=x.next();var
ob=y.getValue();var
Bb=ob.substring(1,ob.length-1);var
I=null;if(Bb.match(e.n8)){Bb=RegExp.leftContext;I=RegExp.$1;}var
ua=s.get(Bb);if(typeof ua!=ub.O)if(I!=null&&ua instanceof Object)y.setValue(ua[I]);else y.setValue(ua);}}else{var
C=ub.P+n.join(ub.Q);for(var
Ya=this.getXML().selectNodeIterator(C);Ya.hasNext();){var
y=Ya.next();var
ob=y.getValue();if(ob.indexOf(ub.R)==0&&jsx3.util.strEndsWith(ob,ub.S)){var
Bb=ob.substring(1,ob.length-1);var
I=null;if(Bb.match(e.n8)){Bb=RegExp.leftContext;I=RegExp.$1;}var
ua=s.get(Bb);if(typeof ua!=ub.O)if(I!=null&&ua instanceof Object)y.setValue(ua[I]);else y.setValue(ua);}}}};e.newDocument=function(){var
F=new
jsx3.xml.Document();F.loadXML(ub.T+ub.a+ub.U);return F;};e.getKey=function(){return ub.V+(e.cN++
).toString(36);};e.getVersion=function(){return ub.W;};});jsx3.Class.defineClass("jsx3.xml.CDF.Document",jsx3.xml.Document,[jsx3.xml.CDF],function(h,p){var
ub={a:"<",c:' jsxid="jsxroot"/>',b:"data"};p.getXML=function(){return this;};p.redrawRecord=function(e,c){};p.cloneDocument=function(){return h.wrap(this.jsxsuper());};h.newDocument=function(){var
za=new
h();za.loadXML(ub.a+ub.b+ub.c);return za;};h.wrap=function(s){return new
h(s.getNativeDocument());};});jsx3.CDF=jsx3.xml.CDF;jsx3.Class.defineClass("jsx3.app.DOM",null,[jsx3.util.EventDispatcher],function(e,q){var
ub={d:"mH",a:"change",c:"_",f:"",b:"_jsx_",e:"QH"};e.Xz={};e.U4={};e.j6={};e.Z8=0;e.TYPEADD=0;e.TYPEREMOVE=1;e.TYPEREARRANGE=2;e.EVENT_CHANGE=ub.a;e.newId=function(d){var
la=e.Nr(d);return ub.b+la+ub.c+e.NU(la).toString(36);};e.Nr=function(m){if(e.U4[m]==null){var
Eb=(e.Z8++
).toString(36);e.U4[m]=Eb;e.j6[Eb]=m;}return e.U4[m];};e.NU=function(m){if(e.Xz[m]==null)e.Xz[m]=0;return ++e.Xz[m];};e.getNamespaceForId=function(i){var
G=i.substring(5,i.indexOf(ub.c,5));return e.j6[G];};q.init=function(){this.mH={};this.QH={};};q.destroy=function(){delete this[ub.d];delete this[ub.e];};q.get=function(d){return this.mH[d]||this.getByName(d);};q.getByName=function(i){var
Jb=this.QH[i];return Jb!=null?Jb.get(0):null;};e.l9=[];q.getAllByName=function(j){var
W=this.QH[j];return W!=null?W.toArray():e.l9;};q.getById=function(f){return this.mH[f];};q.add=function(b){this.mH[b.getId()]=b;var
gb=b.getName();if(gb!=null&&gb!==ub.f)if(this.QH[gb]==null)this.QH[gb]=jsx3.util.List.wrap([b]);else this.QH[gb].add(b,0);};q.remove=function(i){delete this.mH[i.getId()];var
ka=this.QH[i.getName()];if(ka!=null)ka.remove(i);};q.onNameChange=function(d,s){var
O=this.QH[s];if(O!=null)O.remove(d);var
ta=d.getName();if(ta!=null&&ta!==ub.f)if(this.QH[ta]==null)this.QH[ta]=jsx3.util.List.wrap([d]);else this.QH[ta].add(d,0);};q.onChange=function(f,l,g){this.publish({subject:ub.a,type:f,parentId:l,jsxId:g});};});jsx3.DOM=jsx3.app.DOM;jsx3.Class.defineClass("jsx3.app.Server",null,[jsx3.util.EventDispatcher,jsx3.net.URIResolver],function(o,a){var
ub={Qa:"serv.err_jss",_:"onunload",ea:"change",q:"caption",ra:"overflow:auto;",V:"<b>Path:</b> ",v:"JSXROOT",rb:"!",U:"<div>",u:"jsx3.gui.Block",ka:"jsx3.app.Server.",z:"100%",d:"jsxsettings",La:"px",D:"JSXINVISIBLE",fa:"keydown",qb:/\//g,R:"<b>",w:"jsxbgcolor",Ca:"WIDTH",kb:"serv.win_name",Q:"<hr/>",Na:"css",Pa:"ljss",la:"();",qa:"OVERFLOW",Wa:"strType",y:"JSXBODY",lb:"3.2",x:"@Solid DarkShadow",Za:"serv.err_badid",ja:"Y5",W:"version",Fa:'<div id="',Ha:'"></div>',i:"/",Sa:"xsl",A:"@Solid Light",t:"cancelerror",Ia:"serv.err_paint",s:"return false;",X:"<b>GI Version:</b> ",tb:"type",r:"cancelrightclick",sa:"overflow:hidden;",C:"NAMESPACE",Ua:"services",l:"eventsvers",jb:"serv.win_notwin",L:" (",ia:"BODYHOTKEYS",Ka:"onload",sb:"default_locale",_a:"=",eb:"; ",J:"<b>System Locale:</b> ",Da:"height:",ta:"POSITION",oa:"jsx3.app.Server.help",O:"<b>Operating System:</b> ",k:"liquid",gb:"JSXWINDOWS",Ga:'" style="position:',p:"serv.no_ns",P:")",I:"<b>XML Version:</b> ",ha:"jsx3.gui.Alerts",a:"inited",Aa:"TOP",F:"<b>Version:</b> ",Ra:"xml",hb:"strName",j:"JSXAPPS",ua:"relative",ib:"serv.win_err",S:"namespace",Ma:"?timestamp=",K:"<b>Browser:</b> ",bb:"; path=",aa:"serv.err_unload",B:"GUIREF",g:"undefined",b:"help",va:"absolute",M:", ",ab:"; expires=",f:"",vb:":",T:"</b>",ga:"checkHotKeys",ba:"jsx3.gui.Painted",mb:"apppathuri",e:/\/*$/,pb:"jsxapp",za:"top:",h:"object",c:"JSX",H:"<br/>",fb:"jsx3.gui.Window",wa:"left:",ob:"apppathrel",Ya:"id",G:"3.5.0; build 14",Ja:"objectseturl",ma:"if (jsx3.EventHelp.isDragging()) jsx3.EventHelp.reset();",ca:"LIQUID",pa:"R9",N:")<br/>",o:"jsxabspath",Z:"apppath",xa:"LEFT",ub:" ",Ta:"script",na:"jsx3.app.Server.help.",Ba:"width:",da:"_jsxje",nb:"apppathabs",Oa:"jss",wb:"3.00.00",m:"jsxversion",db:"; secure",Y:"</div>",Ea:"HEIGHT",n:"3.1",Xa:"includes",Va:"jsx3.net.Service",ya:";",cb:"; domain=",E:'<div class="jsx30block jsx30env">'};jsx3.util.EventDispatcher.jsxclass.mixin(o);var
Pa=jsx3.gui.Event;var
Qa=jsx3.net.URIResolver;var
Sa=jsx3.app.Browser;var
ea=null;var
na=jsx3.util.Logger.getLogger(o.jsxclass.getName());o.Z_DIALOG=2;o.Z_DRAG=3;o.Z_MENU=4;o.INITED=ub.a;o.HELP=ub.b;o.SM=ub.c;o.Q4=new
jsx3.util.List();a.JSXROOT=null;a.JSXBODY=null;a.Cache=null;a.ENVIRONMENT=null;a.DOM=null;a.JSS=null;a.init=function(m,h,p,r){this.DOM=new
jsx3.app.DOM();this.Cache=new
jsx3.app.Cache();this.Cache.addParent(jsx3.getSharedCache());if(r!=null&&r.jsxsettings!=null){this._jsxQZ=r.jsxsettings;delete r[ub.d];}this.ENVIRONMENT=r=r!=null?jsx3.clone(r):{};r.apppath=m.replace(ub.e,ub.f);var
Na=this.getSettings();var
cb=Na.get();for(var _ in cb){var
Ia=_.toLowerCase();if(typeof r[Ia]==ub.g&&typeof cb[_]!=ub.h)r[Ia]=cb[_];}r.apppathuri=new
jsx3.net.URI(r.apppath+ub.i);if(r.jsxappbase)r.apppathuri=r.apppathuri.resolve(r.jsxappbase);var
Nb=r.apppath.indexOf(ub.j);if(Nb>=0)r.apppathrel=r.apppath.substring(Nb+"JSXAPPS".length+1);r.apppathabs=Sa.getLocation().resolve(r.apppathuri);if(r[ub.k]==null)r[ub.k]=true;if(r[ub.l]==null)r[ub.l]=3;if(r[ub.m]==null)r[ub.m]=ub.n;r.abspath=jsx3.getEnv(ub.o);r.guiref=h;r.namespace=r.jsxappns||r.namespace;if(r.namespace==null)throw new
jsx3.Exception(jsx3._msg(ub.p,m));if(h&&this.getEnv(ub.q))h.ownerDocument.title=this.getEnv(ub.q);if(h&&this.getEnv(ub.r))h.ownerDocument.oncontextmenu=new
Function(ub.s);if(this.getEnv(ub.t))jsx3.NativeError.initErrorCapture();o.Q4.add(this);jsx3.registerApp(this);this.JSS=new
jsx3.app.Properties();this.LJSS=new
jsx3.app.Properties();this.JSS.addParent(this.LJSS);this.JSS.addParent(jsx3.System.JSS);o.publish({subject:o.INITED,target:this});if(p)this.paint();};a.RZ=function(){jsx3.require(ub.u);ea=jsx3.gui.Block;if(this.JSXROOT)return;var
Xa=this.JSXROOT=this._r(ub.v);Xa.setDynamicProperty(ub.w,ub.x);Xa.setRelativePosition(0);Xa.setOverflow(3);Xa.setAlwaysCheckHotKeys(true);Xa.setIndex(1);var
Za=this.JSXBODY=new
ea(ub.y,0,0,ub.z,ub.z,ub.f);Za.setDynamicProperty(ub.w,ub.A);Za.setRelativePosition(0);Za.setZIndex(1);Za.setOverflow(3).setIndex(0);Xa.setChild(Za);};a.onResize=function(){if(jsx3.CLASS_LOADER.IE){window.clearTimeout(this.resize_timeout_id);var
Eb=this;this.resize_timeout_id=window.setTimeout(function(){Eb.Dv();},250);}else this.Dv();};a.Dv=function(){var
_=this.getEnv(ub.B);if(_)this.getRootBlock().af({left:0,top:0,parentwidth:_.clientWidth,parentheight:_.clientHeight},true);};a.getNextZIndex=function(f){if(this.sY==null){this.sY=[];this.sY[0]=0;this.sY[1]=1000;this.sY[o.Z_DIALOG]=5000;this.sY[o.Z_DRAG]=7500;this.sY[o.Z_MENU]=10000;this.sY[5]=25000;}return this.sY[f]++;};o.allServers=function(){return o.Q4.toArray();};a._r=function(q){if(this._jsxje==null)this._jsxje=[];var
ib=new
ea(q,0,0,ub.z,ub.z,ub.f);ib._jsxml=this.getEnv(ub.C);ib._jsxid=jsx3.app.DOM.newId(this.getEnv(ub.C));ib._jsxCe=this;this.DOM.add(ib);this._jsxje.push(ib);return ib;};a.getInvisibleRoot=function(){if(this.INVISIBLE==null)this.INVISIBLE=this._r(ub.D);return this.INVISIBLE;};a.getEnv=function(g){return this.ENVIRONMENT[g.toLowerCase()];};o.Y5=function(h){var
Ua=[ub.E];Ua.push(ub.F,ub.G,ub.H);Ua.push(ub.I,jsx3.getXmlVersion(),ub.H);Ua.push(ub.J,jsx3.System.getLocale().getDisplayName(),ub.H);Ua.push(ub.K,jsx3.CLASS_LOADER.getType()+ub.L+jsx3.CLASS_LOADER.getTypes().join(ub.M),ub.N);Ua.push(ub.O,jsx3.app.Browser.getSystem()+ub.L+navigator.userAgent+ub.P);Ua.push(ub.Q);var
ka=null;var
Ib=o.allServers();for(var
pa=0;pa<Ib.length;pa++){var
eb=Ib[pa];Ua.push(ub.R,eb.getEnv(ub.S),ub.T,ub.U);Ua.push(ub.V,eb.getAppPath(),ub.H);Ua.push(ub.F,eb.getEnv(ub.W),ub.H);Ua.push(ub.X,eb.getEnv(ub.m));Ua.push(ub.Y);if(ka==null){ka=eb.getRootBlock();if(ka.getRendered()==null)ka=null;}}Ua.push(ub.Y);Ua=Ua.join(ub.f);if(ka)ka.showSpy(Ua,Math.round(ka.getRendered().clientWidth/2),Math.round(ka.getRendered().clientHeight/2-100));else window.alert(Ua);};a.getSettings=function(){if(this._jsxQZ==null)this._jsxQZ=new
jsx3.app.Settings(2,this);return this._jsxQZ;};a.getAppPath=function(){return this.getEnv(ub.Z);};o.kL=function(k){return k instanceof jsx3.gui.WindowBar&&k.getType()==3;};a.getTaskBar=function(e){if(!jsx3.gui.WindowBar)return null;if(e==null)e=this.JSXROOT;return e.findDescendants(o.kL,false,false,false,true);};a.destroy=function(){var
eb=this.getEnv(ub._);if(eb)try{this.eval(eb);}catch(Kb){na.error(jsx3._msg(ub.aa,this),jsx3.NativeError.wrap(Kb));}if(jsx3.Class.forName(ub.ba))jsx3.gui.Painted.Box.unregisterServer(this,this.getEnv(ub.ca));if(this.JSXROOT){var
mb=this.JSXROOT.getRendered();if(mb){if(mb.parentNode.id==o.SM)mb=mb.parentNode;jsx3.html.removeNode(mb);}}if(this._jsxje)for(var
sa=0;sa<this._jsxje.length;sa++){var
H=this._jsxje[sa];H.removeChildren();}delete this[ub.da];this.DOM.unsubscribeAllFromAll();this.DOM.destroy();this.Cache.unsubscribeAll(ub.ea);this.Cache.destroy();jsx3.deregisterApp(this);o.Q4.remove(this);Pa.unsubscribe(ub.fa,this,ub.ga);this.ENVIRONMENT={};};a.paint=function(r){jsx3.require(ub.ha,ub.u);ea=jsx3.gui.Block;if(!jsx3.gui.Alerts.jsxclass.isAssignableFrom(o.jsxclass))o.jsxclass.addInterface(jsx3.gui.Alerts.jsxclass);jsx3.html.getMode(this.getEnv(ub.B));this.RZ();jsx3.gui.Painted.Box.registerServer(this,this.getEnv(ub.ca));if(this.getEnv(ub.ia))Pa.subscribe(ub.fa,this,ub.ga);var
Db=ub.ja;this.registerHotKey(new
Function(ub.ka+Db+ub.la),74,true,true,true);this.registerHotKey(new
Function(ub.ma),27,false,false,false);var
Ma=this.getDynamicProperty(ub.na+jsx3.app.Browser.getSystem())||this.getDynamicProperty(ub.oa);if(Ma)this.registerHotKey(jsx3.gui.HotKey.valueOf(Ma,jsx3.makeCallback(ub.pa,this)));try{var
w=this.getEnv(ub.B);var
Ra=this.getRootDocument();var
Ca=ub.f;var
sb=this.getEnv(ub.qa);if(sb==1){Ca=ub.ra;}else if(sb==2)Ca=ub.sa;var
ob,Ba=ub.f,xa=ub.f;if(this.getEnv(ub.ta)==0){ob=ub.ua;}else{ob=ub.va;Ba=ub.wa+this.getEnv(ub.xa)+ub.ya;xa=ub.za+this.getEnv(ub.Aa)+ub.ya;}var
ca=ub.Ba+this.getEnv(ub.Ca)+ub.ya;var
Ta=ub.Da+this.getEnv(ub.Ea)+ub.ya;if(w){w.innerHTML=ub.Fa+o.SM+ub.Ga+ob+ub.ya+Ca+Ba+xa+ca+Ta+ub.Ha;w=w.lastChild;this.JSXROOT.ac({left:0,top:0,parentwidth:w.clientWidth,parentheight:w.clientHeight});}if(w)w.innerHTML=this.JSXROOT.paint();this.Ji(r);}catch(Kb){na.fatal(jsx3._msg(ub.Ia,this),jsx3.NativeError.wrap(Kb));}};a.Ji=function(r){this.FH(r);};a.FH=function(q){var
ha=null;if(q){ha=this.JSXBODY.loadXML(q,true);}else{var
ia=this.getEnv(ub.Ja);if(ia!=ub.f)ha=this.JSXBODY.load(ia,true);}if(ha)ha.setPersistence(1);this.eval(this.getEnv(ub.Ka));};a.setDimensions=function(g,j,m,k){if(g instanceof Array){j=g[1];m=g[2];k=g[3];g=g[0];}var
ka=this.JSXROOT.getRendered();if(ka){if(g)ka.parentNode.style.left=g+ub.La;if(j)ka.parentNode.style.top=j+ub.La;if(m)ka.parentNode.style.width=m+ub.La;if(k)ka.parentNode.style.height=k+ub.La;}};a.loadInclude=function(q,k,e,f){if(k==null)k=ub.f;var
wa=f?ub.Ma+(new
Date()).valueOf():ub.f;if(e==ub.Na){jsx3.CLASS_LOADER.loadResource(q+wa,k,e);}else if(e==ub.Oa||e==ub.Pa&&!jsx3.app.PropsBundle){var
Fb=this.Cache.openDocument(q,k);if(Fb.hasError()){jsx3.util.Logger.GLOBAL.error(jsx3._msg(ub.Qa,+q,Fb.getError()));}else this.getProperties().loadXML(Fb,k);}else if(e==ub.Pa){var
Ta=this.getCache();if(f){var
ba=this.LJSS.getParents();for(var
u=0;u<ba.length;u++)if(ba[u].getPath()==q)this.LJSS.removeParent(ba[u]);jsx3.app.PropsBundle.clearCache(q,Ta);}this.LJSS.addParent(jsx3.app.PropsBundle.getProps(q,this.getLocale(),Ta));}else if(e==ub.Ra||e==ub.Sa){return this.Cache.openDocument(q,k);}else if(e==ub.Ta){this.unloadInclude(k);jsx3.CLASS_LOADER.loadResource(q+wa,k,e);}else if(e==ub.Ua){jsx3.require(ub.Va);return (new
jsx3.net.Service(q)).setNamespace(this);}else throw new
jsx3.IllegalArgumentException(ub.Wa,e);};a.unloadInclude=function(q){var
Y=this.getRootDocument().getElementById(q);try{if(Y)Y.parentNode.removeChild(Y);}catch(Kb){na.warn(jsx3._msg(ub.aa,q,this),jsx3.NativeError.wrap(Kb));}};a.loadResource=function(i){var
Aa=this.getSettings();var
tb=Aa.getMapsInArrayByField(ub.Xa,ub.Ya,i);var
ab;for(var
Ea=0;Ea<tb.length;Ea++){var
va=tb[Ea];ab=this.loadInclude(this.resolveURI(va.src),va.id,va.type);}if(tb.length==0)na.warn(jsx3._msg(ub.Za,i));return ab;};a.setDynamicProperty=function(l,f){var
u=this.getProperties();u.set(l,f);};a.getDynamicProperty=function(s,k){var
J=this.getProperties().get(s);if(arguments.length>1&&jsx3.util.MessageFormat)try{var
bb=new
jsx3.util.MessageFormat(J);var
gb=new
Array(arguments.length-1);for(var
L=1;L<arguments.length;L++)gb[L-1]=arguments[L];return bb.format(gb);}catch(Kb){}return J;};a.setCookie=function(r,p,l,i,g,c,n){this.getRootDocument().cookie=r+ub._a+(n?p:escape(p))+(l?ub.ab+l.toGMTString():ub.f)+(i?ub.bb+i:ub.f)+(g?ub.cb+g:ub.f)+(c?ub.db:ub.f);};a.getCookie=function(d,q){var
ia=this.getRootDocument();var
Xa=ia.cookie;var
ma=d+ub._a;var
x=Xa.indexOf(ub.eb+ma);if(x==-1){x=Xa.indexOf(ma);if(x!=0)return null;}else x=x+2;var
Na=ia.cookie.indexOf(ub.ya,x);if(Na==-1)Na=Xa.length;var
B=Xa.substring(x+ma.length,Na);return q?B:unescape(B);};a.deleteCookie=function(b,l,d){this.setCookie(b,ub.f,new
Date(1970,0,1),l,d);};a.getRootBlock=function(){if(this.JSXROOT==null)this.RZ();return this.JSXROOT;};a.getAlertsParent=function(){return this.getRootBlock();};a.getBodyBlock=function(){if(this.JSXROOT==null)this.RZ();return this.JSXBODY;};a.getRootObjects=function(){return this.JSXBODY.getChildren().concat();};a.getCache=function(){return this.Cache;};a.getProperties=function(){return this.JSS;};a.getDOM=function(){return this.DOM;};a.getJSX=function(b){return this.DOM.get(b);};a.getJSXByName=function(r){return this.DOM.getByName(r);};a.getJSXById=function(f){return this.DOM.getById(f);};a.beep=function(){if(!jsx3.gui.WindowBar)return;var
L=this.JSXROOT.findDescendants(function(g){return g instanceof jsx3.gui.WindowBar&&g.getType()==2;},false,false);if(L!=null)L.beep();};a.createAppWindow=function(n){jsx3.require(ub.fb);if(this.WINDOWS==null)this.WINDOWS=this._r(ub.gb);if(this.WINDOWS.getChild(n)!=null)throw new
jsx3.IllegalArgumentException(ub.hb,n);var
ia=new
jsx3.gui.Window(n);this.WINDOWS.setChild(ia);return ia;};a.loadAppWindow=function(i){jsx3.require(ub.fb);if(this.WINDOWS==null)this.WINDOWS=this._r(ub.gb);var
Fa=null;if(i instanceof jsx3.xml.Entity)Fa=this.WINDOWS.loadXML(i,false);else Fa=this.WINDOWS.load(i,false);if(Fa==null)throw new
jsx3.Exception(jsx3._msg(ub.ib,i));if(!(Fa instanceof jsx3.gui.Window)){Fa.getParent().removeChild(Fa);throw new
jsx3.Exception(jsx3._msg(ub.jb,i,Fa.getClass()));}if(this.WINDOWS.getChild(Fa.getName())!=Fa){Fa.getParent().removeChild(Fa);throw new
jsx3.Exception(jsx3._msg(ub.kb,i,Fa.getName()));}return Fa;};a.getAppWindow=function(i){if(this.WINDOWS!=null)return this.WINDOWS.getChild(i);return null;};a.getDocumentOf=function(h){var
Ib=h;while(Ib!=null){if(jsx3.gui.Window&&Ib instanceof jsx3.gui.Window)return Ib.getDocument();if(Ib._jsxCe!=null)return this.getRootDocument();Ib=Ib.getParent();}return this.getRootDocument();};a.getRootDocument=function(){var
v=this.getEnv(ub.B);return v!=null?v.ownerDocument:null;};a.getRenderedOf=function(m){var
K=m.getId();var
z=this.getRootDocument();if(z==null)return null;var
v=z.getElementById(K);if(v==null&&this.WINDOWS!=null){var
xb=this.WINDOWS.getChildren();for(var
Ya=0;v==null&&Ya<xb.length;Ya++){var
lb=xb[Ya].getDocument();if(lb)v=lb.getElementById(K);}}return v;};a.registerHotKey=function(i,f,d,m,q){return this.getRootBlock().registerHotKey(i,f,d,m,q);};a.checkHotKeys=function(e){return this.getRootBlock().checkHotKeys(e.event);};a.getServer=function(){return this;};a.isAtLeastVersion=function(p){return jsx3.util.compareVersions(this.getEnv(ub.m),p)>=0;};a.resolveURI=function(h){var
Wa=jsx3.net.URI.valueOf(h);if(this.isAtLeastVersion(ub.lb)&&!Qa.isAbsoluteURI(Wa)){var
Ga=this.getEnv(ub.mb);return Qa.DEFAULT.resolveURI(Ga.resolve(Wa.toString()));}else return Qa.DEFAULT.resolveURI(Wa);};a.getUriPrefix=function(){return this.getEnv(ub.mb).toString();};a.relativizeURI=function(s,g){var
B=Sa.getLocation();var
Ea=this.getEnv(ub.nb);var
za=Ea.relativize(B.resolve(s));if(za.isAbsolute()||g){return za;}else{var
Ra=this.getEnv(ub.ob);if(Ra){return jsx3.net.URI.fromParts(ub.pb,null,Ra.replace(ub.qb,ub.rb),null,ub.i+za.getPath(),za.getQuery(),za.getFragment());}else return jsx3.net.URI.fromParts(null,null,null,null,this.getEnv(ub.Z)+ub.i+za.getPath(),za.getQuery(),za.getFragment());}};a.getLocale=function(){if(this.MJ==null)this.MJ=this.getDefaultLocale();return this.MJ!=null?this.MJ:jsx3.System.getLocale();};a.setLocale=function(r){this.MJ=r;};a.getDefaultLocale=function(){var
Q=this.getSettings().get(ub.sb);if(Q==null)return null;Q=jsx3.util.strTrim(Q.toString());return Q.length>0&&jsx3.util.Locale?jsx3.util.Locale.valueOf(Q):null;};a.reloadLocalizedResources=function(){this.LJSS.removeAllParents();var
jb=this.getSettings().getMapsInArrayByField(ub.Xa,ub.tb,ub.Pa);for(var
Ea=0;Ea<jb.length;Ea++){var
Ka=jb[Ea];this.loadInclude(this.resolveURI(Ka.src),Ka.id,Ka.type,false);}};a.R9=function(l){var
Va=jsx3.html.getJSXParent(l.srcElement(),this);if(!Va)Va=this.JSXROOT;if(Va)this.invokeHelp(Va);};a.invokeHelp=function(s){var
O=null;while(s&&!O){O=s.getHelpId();s=s.getParent();}if(O)this.publish({subject:o.HELP,helpid:O,model:s});return Boolean(O);};a.toString=function(){return this.jsxsuper()+ub.ub+this.getAppPath()+ub.vb+this.getEnv(ub.S);};o.getVersion=function(){return ub.wb;};});if(jsx3.gui.Alerts)jsx3.app.Server.jsxclass.addInterface(jsx3.gui.Alerts.jsxclass);jsx3.Server=jsx3.app.Server;jsx3.Class.defineClass("jsx3.app.Model",null,[jsx3.util.EventDispatcher],function(g,j){var
ub={oa:"model.bad_type",k:"_jsxxK",O:"]",Ga:" ",ea:"jsx1:object | ",_:"model.future_vers",p:"unshift",P:"model.bad_comp",ra:"jsxcustom",q:"push",V:"onBeforeDeserialize",v:"dynamics",ha:"load",I:"false",Aa:"response",a:"urn:tibco.com/v3.0",F:"src",u:".",U:"description",ua:"jsx1:events",j:"vntItem",ka:"strKey",d:"jsx:///xsl/cif_resolver.xsl",z:"object",S:"name",fa:"jsx1:objects/jsx1:object",D:"model.async_convt",w:"properties",K:"_jsx",R:"jsx3.xml.Template",Ca:"timeout",aa:"/jsx1:serialization/",g:"url",B:"variants",b:"http://xsd.tns.tibco.com/gi/cif/2006",Q:"model.bad_compobj",va:"jsxxslparams",M:"[",qa:"jsx1:dynamics",la:"jsx1:variants/@jsxloadtype",f:"string",ga:"jsx1:",y:"xslparameters",T:"icon",ba:"xmlns:jsx1='",x:"events",e:"jsx3.app.Model",za:"model.onafter",ja:"",c:"JSXFRAG",h:"model.set_frag",W:"onAfterDeserialize",H:"true",wa:"jsx1:xslparameters",Ha:"/",Fa:"@",G:"async",ma:"jsx3.gui.Painted",i:"_jsxDW",ca:"jsx1:onBeforeDeserialize",A:"type",t:"jsxversion",pa:"_jsxSi",N:",",Ia:"3.00.00",s:"serialization",X:"objXML",Z:"xmlns",o:"function",xa:"jsx1:object | jsx1:include | jsx1:children/jsx1:object | jsx1:children/jsx1:include",sa:"jsx1:properties",r:"undefined",C:"strings",Ba:"error",na:"model.load_cls",l:"intIndex",ia:"paint",da:"model.onbefore",L:"'",m:"number",Da:"jsx1:strings",Y:"model.bad_vers",J:"model.child_notarr",Ea:"jsx1:variants",n:"model.clone_frag",ya:"onafter",ta:"_jsxHj",E:"include"};var
ka=jsx3.xml.Entity;var
Sa=jsx3.xml.Document;g.PERSISTNONE=0;g.PERSISTEMBED=1;g.PERSISTREF=2;g.PERSISTREFASYNC=3;g.LT_NORMAL=0;g.LT_SLEEP_PAINT=1;g.LT_SLEEP_DESER=2;g.LT_SLEEP_PD=3;g.LT_SHOW_PAINT=4;g.LT_SHOW_DESER=5;g.CURRENT_VERSION=ub.a;g.CIF_VERSION=ub.b;g.FRAGMENTNS=ub.c;g.ASYNC_LOAD_TIMEOUT=60000;g.hZ=jsx3.resolveURI(ub.d);j._jsxid=null;j._jsxxK=null;j._jsxE1=null;j._jsxml=null;j._jsxCe=null;j._jsxDW=null;j._jsxC5=null;j._jsxgV=null;j._jsxH9=true;j.init=function(i,h){this.jsxsuper();this.jsxinstanceof=h==null?ub.e:h;this.jsxname=i;};j.getChild=function(c){var
v=null;if(this._jsxE1!=null){if(typeof c==ub.f||isNaN(c)){var
x=-1;var
ga=this.getChildren();var
Jb=ga.length;for(var
E=0;E<Jb;E++)if(c==ga[E].getName()){v=E;break;}}else v=c;if(v>=0&&v<this._jsxE1.length)return this._jsxE1[v];}return null;};j.getFirstChild=function(){return this.getChild(0);};j.getLastChild=function(){return this.getChild(this.getChildren().length-1);};j.getNextSibling=function(){if(!this._jsxxK)return null;return this._jsxxK.getChild(this.getChildIndex()+1);};j.getPreviousSibling=function(){if(!this._jsxxK)return null;return this._jsxxK.getChild(this.getChildIndex()-1);};j.getChildren=function(){if(this._jsxE1==null)this._jsxE1=[];return this._jsxE1;};j.getPersistence=function(){return this._jsxC5;};j.getPersistenceUrl=function(){return this.getMetaValue(ub.g);};j.setPersistence=function(h){this._jsxC5=h;return this;};j.setChild=function(n,p,c,h){if(!this.onSetChild(n)||!n.onSetParent(this))return false;var
ob=false;if(h==null&&this._jsxml==null){throw new
jsx3.Exception(jsx3._msg(ub.h,n,this));}else if(h!=null){ob=true;}else h=this._jsxml;var
_a=this.getServer();if(h!=ub.c&&_a&&this._jsxml==h){this.x9(n,h,n._jsxDW!=null,_a);}else this._jsxDW=1;var
ia=this._jsxE1;if(!ia)ia=this._jsxE1=[];ia[ia.length]=n;n._jsxxK=this;if(p==null)p=0;n._jsxC5=p;if(c&&(p==2||p==3))n.setMetaValue(ub.g,c.toString());if(!ob&&h!=ub.c)_a.getDOM().onChange(0,this.getId(),n.getId());return this;};j.onSetChild=function(f){this.jsxmix(f);return true;};j.onSetParent=function(r){this.jsxmix(r);return true;};j.onRemoveChild=function(a,n){this.jsxmix(a,n);};j.x9=function(d,q,m,b){d._jsxml=q;d._jsxid=jsx3.app.DOM.newId(q);b.getDOM().add(d);if(m){delete d[ub.i];var
Va=d.getChildren();var
Da=Va.length;for(var
db=0;db<Da;db++)d.x9(Va[db],q,true,b);}};j.removeChild=function(n){var
ea=-1;if(!isNaN(n)){ea=Number(n);}else if(n instanceof jsx3.app.Model){ea=n._jsxxK==this?n.getChildIndex():-1;}else throw new
jsx3.IllegalArgumentException(ub.j,n);var
Y=this.getChild(ea);if(Y!=null){var
N=this.getServer();this.bK(ea,N);this.onRemoveChild(Y,ea);N.getDOM().onChange(1,this.getId(),Y.getId());}};j.bK=function(m,e,q){if(m>=0&&m<this.getChildren().length){var
ib=this.getChild(m);if(!q)ib.Zn(this);var
Ba=ib.getChildren().length;for(var
bb=Ba-1;bb>=0;bb--)ib.bK(bb,e,true);e.getDOM().remove(ib);delete ib[ub.k];if(!q)this._jsxE1.splice(m,1);else if(m==0)this._jsxE1.splice(0,this._jsxE1.length);ib.onDestroy(this);}else throw new
jsx3.IllegalArgumentException(ub.l,m);};j.removeChildren=function(c){var
da=this.getServer();if(c==null){c=this.getChildren().concat();for(var
mb=c.length-1;mb>=0;mb--){c[mb].Zn(this);this.bK(mb,da,true);}}else{var
v=null;c=c.concat();for(var
mb=c.length-1;mb>=0;mb--){var
Xa=c[mb];if(typeof Xa==ub.m){v=Xa;c[mb]=this.getChild(v);}else v=Xa.getChildIndex();this.bK(v,da,false);}}if(c.length>0)this.onRemoveChild(c,null);return this;};j.getServer=function(){var
na=this;while(na){if(na._jsxCe)return na._jsxCe;na=na._jsxxK;}return null;};j.pc=function(){var
Eb=this.getServer();return Eb!=null?Eb.getLocale():jsx3.System.getLocale();};j.wi=function(d){return jsx3.System.getLocaleProperties(this.pc()).get(d);};j.adoptChild=function(n,k,c){this.mU(n.getChildIndex(),n,k,c,false);};j.adoptChildrenFrom=function(q,e,d,c){if(!e)e=q.getChildren().concat();if(e.length>0){for(var
V=0;V<e.length;V++)this.mU(e[V].getChildIndex(),e[V],d,c,true);q.onRemoveChild(e,null);}};j.r1=function(b,f){if(b==f||b==f-1)return false;var
Ab=this.getChildren();var
Mb=b<f?f-1:f;var
Qa=Ab.splice(b,1);var
D=Ab.splice(0,Mb);this._jsxE1=D.concat(Qa,Ab);this.getServer().getDOM().onChange(2,this.getId(),f);return true;};j.insertBefore=function(d,l,r){var
kb=true;if(!d._jsxxK||!l){kb=this.setChild(d);}else if(d._jsxxK!=this)kb=this.mU(d.getChildIndex(),d,false,true,false);if(!kb)return;if(l)kb=this.r1(d.getChildIndex(),l.getChildIndex());if(r!==false)this.repaint();return kb;};j.mU=function(m,f,r,o,i){if(o){this.onSetChild(f);f.onSetParent(this);}else if(!this.onSetChild(f)||!f.onSetParent(this))return false;var
lb=f._jsxxK;delete f[ub.k];f.ki(true);if(lb._jsxE1!=null)lb._jsxE1.splice(m,1);f.Zn(lb);if(!i)lb.onRemoveChild(f,m);var
Kb=lb.getServer();var
S=this.getServer();var
fb=Kb!=S;if(fb)this.sN(f,lb,Kb,S);Kb.getDOM().onChange(1,lb.getId(),f.getId());var
wa=this._jsxE1;if(!wa)wa=this._jsxE1=[];wa[wa.length]=f;f._jsxxK=this;if(r!=false)this.Lc(f,i&&lb._jsxE1.length>0);S.getDOM().onChange(0,this.getId(),f.getId());return this;};j.Lc=function(n,k){};j.Zn=function(r){var
Qa=r.getServer().getRenderedOf(this);if(Qa)jsx3.html.removeNode(Qa);};j.sN=function(e,p,o,b){o.getDOM().remove(e);e._jsxml=this._jsxml;b.getDOM().add(e);e.onChangeServer(b,o);var
Z=e.getChildren();for(var
Ta=0;Ta<Z.length;Ta++)e.sN(Z[Ta],null,o,b);};j.onChangeServer=function(i,h){this.jsxmix(i,h);};j.doClone=function(c,d){var
W=d==2?this.getServer().getRootBlock():this._jsxxK;if(W){var
_a=W.rx(this.toXMLDoc(),d<1,c,null,null,d==2?ub.c:null);return _a?_a[0]:null;}else throw new
jsx3.Exception(jsx3._msg(ub.n,this));};j.getDescendantOfName=function(p,m,a){return this.findDescendants(function(o){return o.getName()==p;},m,false,a,false);};j.getFirstChildOfType=function(b,l){if(l){var
T=null;if(typeof b==ub.f)T=jsx3.Class.forName(b);else if(typeof b==ub.o&&b.jsxclass instanceof jsx3.Class)T=b.jsxclass;else if(b instanceof jsx3.Class)T=b;return this.findDescendants(function(a){return a.getClass().equals(T);},false,false,true,false);}else return this.findDescendants(function(r){return r.instanceOf(b);},false,false,true,false);};j.getDescendantsOfType=function(l,o){return this.findDescendants(function(a){return a.instanceOf(l);},false,true,o,false);};j.findDescendants=function(q,b,d,e,p){var
Fa=b?ub.p:ub.q;var
Va=d?[]:null;var
T=p?[this]:this.getChildren().concat();while(T.length>0){var
yb=T.shift();if(q.call(null,yb))if(d)Va[Va.length]=yb;else return yb;if(!e)T[Fa].apply(T,yb.getChildren());}return Va;};j.onDestroy=function(f){this.jsxmix(f);};j.getId=function(){return this._jsxid;};j.getChildIndex=function(){var
L=this._jsxxK;if(L!=null)return jsx3.util.arrIndexOf(L.getChildren(),this);return -1;};j.getName=function(){return this.jsxname;};j.setName=function(m){if(m!=null){var
La=this.jsxname;this.jsxname=m;var
Ib=this.getServer();if(Ib)Ib.getDOM().onNameChange(this,La);}return this;};j.getHelpId=function(){return this.jsxhelpid;};j.setHelpId=function(o){this.jsxhelpid=o;};j.getLoadType=function(){return this.jsxloadtype||0;};j.setLoadType=function(d){this.jsxloadtype=d;};j.cj=function(){return this._jsxH9;};j.Em=function(o){if(this._jsxH9!=o){this._jsxH9=o;if(o){var
yb=this.getRendered();if(yb&&!yb.firstChild)this.repaint();}}};j.getParent=function(){return this._jsxxK;};j.getAncestorOfType=function(b){return this.findAncestor(function(p){return p.instanceOf(b);},false);};j.getAncestorOfName=function(k){return this.findAncestor(function(l){return l.getName()==k;},false);};j.findAncestor=function(d,o){var
zb=o?this:this._jsxxK;while(zb!=null){if(d.call(null,zb))return zb;zb=zb._jsxxK;}return null;};j.toXML=function(m){return this.toXMLDoc(m).serialize(true,m!=null?m.charset:null);};j.toXMLDoc=function(p){if(p==null){p=this._jsxgV;if(p==null)p={};}else if(this._jsxgV!=null){p=jsx3.clone(p);for(var db in this._jsxgV)if(typeof p[db]==ub.r)p[db]=this._jsxgV[db];}var
B=g.CURRENT_VERSION;var
Mb=new
jsx3.xml.Document();var
oa=Mb.createDocumentElement(ub.s,B);oa.setAttribute(ub.t,g.OL());for(var db in g.rp){var
na=g.rp[db];var
N=Mb.createNode(1,na,B);N.appendChild(Mb.createNode(4,p[db],B));oa.appendChild(N);}if(p.children){var
P=this.getChildren().length;for(var
V=0;V<P;V++)oa.appendChild(this.getChild(V).zq(Mb,p));}else oa.appendChild(this.zq(Mb,p));return Mb;};g.OL=function(){var
Da=jsx3.System.getVersion().split(ub.u);return Da[0]+ub.u+Da[1];};g.Cy={_jsxSi:ub.v,jsxcustom:ub.w,_jsxHj:ub.x,jsxxslparams:ub.y};g.Cz={"boolean":1,number:1};j.zq=function(a,d){var
kb=g.CURRENT_VERSION;var
Db=a.createNode(1,ub.z,kb);var
aa=this.getClass();var
Za=aa!=null?aa.getName():null;if(Za==null)Za=this.jsxinstanceof;Db.setAttribute(ub.A,Za);var
rb=a.createNode(1,ub.B,kb);var
Kb=a.createNode(1,ub.C,kb);Db.appendChild(rb);Db.appendChild(Kb);for(var Hb in g.Cy){var
Aa=this[Hb];if(Aa!=null&&typeof Aa==ub.z){var
Ya=g.c4(a,g.Cy[Hb],Aa);if(Ya!=null)Db.appendChild(Ya);}}var
Nb=this._jsxE1;if(Nb)if(Nb instanceof Array){var
R=Nb.length;if(R>0)for(var
ab=0;ab<R;ab++){var
T=Nb[ab];var
jb=T._jsxC5;if(jb==2||jb==3){if(jb==3&&ab!=R-1){jsx3.util.Logger.GLOBAL.warn(jsx3._msg(ub.D,this));jb=T._jsxC5=2;}var
x=a.createNode(1,ub.E,kb);x.setAttribute(ub.F,T.getPersistenceUrl());x.setAttribute(ub.G,jb==3?ub.H:ub.I);Db.appendChild(x);}else if(jb==1||d.persistall)Db.appendChild(T.zq(a,d));}}else jsx3.util.Logger.GLOBAL.error(jsx3._msg(ub.J,this,this[ca]));for(var ca in this){var
Ib=this[ca];var
ib=typeof Ib;if(ib==ub.o||ca.indexOf(ub.K)==0||Ib==null){}else if(Ib instanceof Array){var
A=new
Array(Ib.length);for(var
ab=0;ab<Ib.length;ab++){var
W=Ib[ab];A[ab]=g.Cz[typeof W]?W:ub.L+W+ub.L;}rb.setAttribute(ca,ub.M+A.join(ub.N)+ub.O);}else if(ib==ub.z){}else if(this._jsxSi==null||this._jsxSi[ca]==null)if(g.Cz[ib]){rb.setAttribute(ca,String(Ib));}else Kb.setAttribute(ca,Ib);}return Db;};g.c4=function(d,h,k){var
Ia=null;for(var M in k){if(Ia==null)Ia=d.createNode(1,h,g.CURRENT_VERSION);Ia.setAttribute(M,String(k[M]));}return Ia;};j.getNS=function(){return this._jsxml;};j.getUriResolver=function(){var
L=this;while(L!=null){if(L._jsxwu!=null)return L._jsxwu;if(L._jsxCe!=null)return L._jsxCe;L=L._jsxxK;}return null;};j.load=function(d,k,l){var
t=(l||this.getUriResolver()).resolveURI(d);var
U=(new
Sa()).load(t);if(U.hasError())throw new
jsx3.Exception(jsx3._msg(ub.P,t,U.getError()));return this.rx(U,k,null,t,d,null,null,l,null)[0];};j.loadXML=function(n,f,l){var
G=n instanceof Sa?n:(new
Sa()).loadXML(n);if(G.hasError()){var
Y=G.getSourceURL();var
la=Y?ub.P:ub.Q;throw new
jsx3.Exception(jsx3._msg(la,Y,G.getError()));}return this.rx(G,f,null,G.getSourceURL(),null,null,null,l,null)[0];};j.loadAndCache=function(r,s,o,q){if(o==null)o=this.getServer().getCache();var
Ea=(q||this.getUriResolver()).resolveURI(r);var
Ta=o.getOrOpenDocument(Ea);if(Ta.hasError())throw new
jsx3.Exception(jsx3._msg(ub.P,Ea,Ta.getError()));return this.rx(Ta,s,null,Ea,r,null,o,q,null)[0];};g.kO=function(e){jsx3.require(ub.R);var
Jb=jsx3.getSystemCache().getOrOpenDocument(g.hZ,null,jsx3.xml.XslDocument.jsxclass);return Jb.transformToObject(e);};g.rp={name:ub.S,icon:ub.T,description:ub.U,onbefore:ub.V,onafter:ub.W};j.rx=function(n,b,f,p,h,q,l,a,e){if(!e||f==3)var
x=new
jsx3.util.Timer(g.jsxclass,p);if(n==null)throw new
jsx3.IllegalArgumentException(ub.X,n);if(n.getRootNode().getNamespaceURI().indexOf(g.CIF_VERSION)==0){n=g.kO(n);if(n==null)throw new
jsx3.IllegalArgumentException(ub.X,n);}if(n.getRootNode().getNamespaceURI().indexOf(g.CURRENT_VERSION)!=0){throw new
jsx3.Exception(jsx3._msg(ub.Y,p,n.getRootNode().getAttribute(ub.Z)));}else{var
Qa=n.getRootNode().getAttribute(ub.t);if(Qa&&jsx3.util.compareVersions(Qa,jsx3.System.getVersion())>0)throw new
jsx3.Exception(jsx3._msg(ub._,p,Qa));}var
Oa=q==ub.c;var
Ja=ub.aa;n.setSelectionNamespaces(ub.ba+g.CURRENT_VERSION+ub.L);var
R=n.selectSingleNode(Ja+ub.ca);if(R!=null){var
xb=R.getValue();if(xb&&!n._jsxQm)try{jsx3.eval(xb,{objPARENT:this,objXML:n});n._jsxQm=true;}catch(Nb){jsx3.util.Logger.GLOBAL.error(jsx3._msg(ub.da,p),jsx3.NativeError.wrap(Nb));}}if(q==null)q=this._jsxml;var
eb=this.getUriResolver()||e;if(a==null){var
Kb=jsx3.net.URIResolver.getResolver(h);if(Kb!=jsx3.net.URIResolver.DEFAULT)a=Kb;}else if(h)if(a.getUriPrefix()!=eb.getUriPrefix()){h=jsx3.net.URI.valueOf(h);if(!jsx3.net.URIResolver.isAbsoluteURI(h))h=a.relativizeURI(h);}var
Aa=this.getServer();if(e==null)e=Aa;var
w=a||eb;var
wa=n.selectNodeIterator(Ja+ub.ea+Ja+ub.fa);var
fa=[];while(wa.hasNext()){var
P=wa.next();var
B=this.Ji(P,p,q,e,l,w,a);if(B!=null){fa[fa.length]=B;if(!Oa)var
Za=this.setChild(B,f,p,q);if(Za===false)return false;if(fa.length==1){if(h)B.setMetaValue(ub.g,h.toString());for(var ha in g.rp){var
ca=n.selectSingleNode(Ja+ub.ga+g.rp[ha]);if(ca!=null)B.setMetaValue(ha,ca.getValue());}}if(Aa!=null)B.onAfterAttach();}}if(!Oa&&fa.length>0&&Aa)Aa.getDOM().onChange(0,this.getId(),fa[0].getId());if(x)x.log(ub.ha,true);if(b!==false){for(var
L=0;L<fa.length;L++)this.Lc(fa[L],L<fa.length-1);if(x)x.log(ub.ia);}return fa;};g.META_FIELDS={url:1,name:1,icon:1,description:1,onafter:1,onattach:1,onbefore:1,unicode:1};j.getMetaValue=function(h){if(g.META_FIELDS[h])return this._jsxgV?this._jsxgV[h]:ub.ja;else throw new
jsx3.IllegalArgumentException(ub.ka,h);};j.setMetaValue=function(i,q){if(g.META_FIELDS[i]){if(this._jsxgV==null)this._jsxgV={};this._jsxgV[i]=q;}else throw new
jsx3.IllegalArgumentException(ub.ka,i);};j.Ji=function(n,p,q,b,l,a,k){if(n==null)return null;if(!n._jsxbm){var
Ab=n.selectSingleNode(ub.la);Ab=Ab?parseInt(Ab.getValue()):0;if(Ab==2||Ab==3||Ab==5){jsx3.require(ub.ma);var
Ka=new
g.Loading(n,Ab,[p,q,b,l,a,k]);Ka._jsxml=q;return Ka;}}var
T=n.getAttribute(ub.A);var
I=jsx3.Class.forName(T);if(I==null)I=jsx3.getClass(T);if(I==null)try{I=jsx3.CLASS_LOADER.loadClass(T);}catch(Kb){jsx3.util.Logger.GLOBAL.error(jsx3._msg(ub.na,T),jsx3.NativeError.wrap(Kb));}if(I==null){jsx3.util.Logger.GLOBAL.error(jsx3._msg(ub.oa,p,T));return null;}var
Ka=null;if(I instanceof jsx3.Class){Ka=I.bless();}else{Ka=new
I(jsx3.DESERIALIZE);Ka.jsxinstanceof=T;}if(k)Ka._jsxwu=k;Ka._jsxml=q;Ka.onBeforeAssemble(this,b);g.JR(Ka,n);g.PZ(Ka,n);g.xs(Ka,n,ub.pa,ub.qa);g.xs(Ka,n,ub.ra,ub.sa);g.xs(Ka,n,ub.ta,ub.ua);g.xs(Ka,n,ub.va,ub.wa);var
db=n.selectNodeIterator(ub.xa);while(db.hasNext()){var
v=db.next();if(v.getBaseName()==ub.z){var
Gb=Ka.Ji(v,p,q,b,l,a,null);if(Gb)Ka.setChild(Gb,1,null,q);}else if(v.getBaseName()==ub.E){var
y=v.getAttribute(ub.F);var
zb=a.resolveURI(y);var
Ma=true;if(v.getAttribute(ub.G)==ub.H){Ma=false;if(db.hasNext()){jsx3.util.Logger.GLOBAL.warn(jsx3._msg(ub.D,Ka));Ma=true;}}if(Ma){var
x=l!=null?l.getOrOpenDocument(zb):(new
Sa()).load(zb);if(x.hasError())throw new
jsx3.Exception(jsx3._msg(ub.P,zb,n.getError()));Ka.rx(x,false,2,zb,y,q,l,null,b);}else Ka.l3(zb,y,q,l,b);}else throw new
jsx3.Exception();}Ka.onAfterAssemble(this,b);return Ka;};j.onBeforeAssemble=function(q,s){this.jsxmix(q,s);};j.onAfterAssemble=function(a,c){this.jsxmix(a,c);};j.onAfterAttach=function(){this.jsxmix();var
A=this.getChildren().concat();for(var
G=A.length-1;G>=0;G--)if(A[G]._jsxxK==this)A[G].onAfterAttach();var
gb=this.getMetaValue(ub.ya);if(gb)try{jsx3.eval(gb,{objJSX:this});}catch(Kb){var
oa=this.getMetaValue(ub.g);jsx3.util.Logger.GLOBAL.error(jsx3._msg(ub.za,oa),jsx3.NativeError.wrap(Kb));}};j.l3=function(h,s,k,f,o){var
mb=this;if(f!=null&&f.getDocument(h.toString())!=null){var
Ka=f.getDocument(h.toString());jsx3.sleep(function(){this.rx(Ka,true,3,h,s,k,f,null,o);},null,this);}else{var
Ka=new
Sa();Ka.setAsync(true);Ka.subscribe(ub.Aa,function(m){if(f!=null)f.setDocument(h,m.target);mb.rx(m.target,true,3,h,s,k,f,null,o);});Ka.subscribe([ub.Ba,ub.Ca],function(m){throw new
jsx3.Exception(jsx3._msg(ub.P,h,m.target.getError()));});Ka.load(h,g.ASYNC_LOAD_TIMEOUT);}};g.JR=function(e,a){var
Bb=a.selectSingleNode(ub.Da);if(Bb!=null){var
O=Bb.getAttributeNames();for(var
H=0;H<O.length;H++){var
ob=O[H];e[ob]=Bb.getAttribute(ob);}}};g.PZ=function(s,f){var
O=f.selectSingleNode(ub.Ea);if(O!=null){var
Pa=O.getAttributeNames();for(var
Hb=0;Hb<Pa.length;Hb++){var
Cb=Pa[Hb];var
D=O.getAttribute(Cb);s[Cb]=isNaN(D)?jsx3.eval(D):Number(D);}}};g.xs=function(e,a,p,l){var
Ra=a.selectSingleNode(l);if(Ra!=null){var
E=e[p]={};var
X=Ra.getAttributeNames();for(var
Ya=0;Ya<X.length;Ya++){var
Fb=X[Ya];E[Fb]=Ra.getAttribute(Fb);}}};j.toString=function(){return ub.Fa+this.getClass().getName()+ub.Ga+this.getId()+ub.Ha+this.getName();};g.getVersion=function(){return ub.Ia;};});jsx3.Model=jsx3.app.Model;jsx3.Class.defineClass("jsx3.gui.HotKey",null,[jsx3.util.EventDispatcher],function(p,o){var
ub={k:"callback",O:"Meta",ea:"PgDn",_:"Left",p:"ctrl+",P:"Alt",q:"shift+",V:"Del",v:"\u2318",ha:" ctrl:",I:"\u2192",a:"invoked",F:"\u2191",u:"u4",U:"Tab",j:"function",ka:/^[fF](\d\d?)$/,d:"shift",z:"\u21A9",S:"Enter",fa:"@HotKey key:",D:"\u2423",w:"\u2325",K:"\u2196",R:"Shift",aa:"Right",g:"string",B:"\u21E5",b:"+",Q:"Ctrl",M:"\u21DE",la:"F",f:"meta",ga:" shift:",y:"\u21EA",T:"Esc",ba:"Home",x:"\u2303",e:"alt",ja:" meta:",c:"ctrl",h:/^\[(\d+)\]$/,W:"Space",H:"\u2190",G:"\u2193",i:"",ca:"End",A:"\u238B",t:"gui.hk.dest",N:"\u21DF",s:"]",X:"Backspace",Z:"Down",o:"alt+",r:"[",C:"\u2326",l:"number",ia:" alt:",da:"PgUp",L:"\u2198",m:"key",Y:"Up",J:"Insert",n:"meta+",E:"\u232B"};var
Ea=jsx3.gui.Event;p.WAS_INVOKED=ub.a;o.u4=null;o.iJ=null;o.Up=false;o.Iy=false;o.fV=false;o._L=false;o.ZY=true;o.XT=false;p.valueOf=function(k,s){var
wb=k.toLowerCase().split(ub.b);var
Aa=wb.pop();var
C=wb.indexOf(ub.c)>=0;var
ua=wb.indexOf(ub.d)>=0;var
wa=wb.indexOf(ub.e)>=0;var
fb=wb.indexOf(ub.f)>=0;if(typeof Aa==ub.g&&Aa.match(ub.h))Aa=parseInt(RegExp.$1);return new
p(s||new
Function(ub.i),Aa,ua,C,wa,fb);};o.init=function(h,i,a,j,b,n){if(!(typeof h==ub.j))throw new
jsx3.IllegalArgumentException(ub.k,h);this.u4=h;this.Up=a==null?null:Boolean(a);this.Iy=j==null?null:Boolean(j);this._L=b==null?null:Boolean(b);this.fV=n==null?null:Boolean(n);this.iJ=typeof i==ub.l?i:p.keyDownCharToCode(i);if(this.iJ==null)throw new
jsx3.IllegalArgumentException(ub.m,i);};o.getKey=function(){var
Z=ub.i;if(this.fV)Z=Z+ub.n;if(this._L)Z=Z+ub.o;if(this.Iy)Z=Z+ub.p;if(this.Up)Z=Z+ub.q;var
xa=p.keyDownCodeToChar(this.iJ);Z=Z+(xa!=null?xa:ub.r+this.iJ+ub.s);return Z;};o.getKeyCode=function(){return this.iJ;};o.isMatch=function(h){var
mb=h.keyCode()==this.iJ&&(this.Up==null||h.shiftKey()==this.Up)&&(this.Iy==null||h.ctrlKey()==this.Iy)&&(this.fV==null||h.metaKey()==this.fV)&&(this._L==null||h.altKey()==this._L);return mb;};o.invoke=function(e,d){if(this.XT||!this.ZY)throw new
jsx3.Exception(jsx3._msg(ub.t,this));var
za=this.u4.apply(e,d);this.publish({subject:ub.a});return za;};o.isEnabled=function(){return this.ZY;};o.setEnabled=function(n){this.ZY=n;};o.isDestroyed=function(){return this.XT;};o.destroy=function(){this.XT=true;delete this[ub.u];};o.getFormatted=function(){var
Aa=null,Fb=null;if(jsx3.app.Browser.macosx){Aa=ub.i;Fb=p.eV;}else{Aa=ub.b;Fb=p.u5;}var
Lb=ub.i;if(this.Iy)Lb=Lb+(Fb.ctrl[0]+Aa);if(this._L)Lb=Lb+(Fb.alt[0]+Aa);if(this.Up)Lb=Lb+(Fb.shift[0]+Aa);if(this.fV)Lb=Lb+(Fb.meta[0]+Aa);var
bb=p.keyDownCodeToChar(this.iJ,true);Lb=Lb+(bb!=null?bb.length==1?bb.toUpperCase():bb:ub.r+this.iJ+ub.s);return Lb;};p.eV={meta:[ub.v,-1],alt:[ub.w,18],ctrl:[ub.x,17],shift:[ub.y,16],enter:[ub.z,13],esc:[ub.A,27],tab:[ub.B,9],del:[ub.C,46],space:[ub.D,32],backspace:[ub.E,8],up:[ub.F,38],down:[ub.G,40],left:[ub.H,37],right:[ub.I,39],insert:[ub.J,45],home:[ub.K,36],end:[ub.L,35],pgup:[ub.M,33],pgdn:[ub.N,34]};p.u5={meta:[ub.O,-1],alt:[ub.P,18],ctrl:[ub.Q,17],shift:[ub.R,16],enter:[ub.S,13],esc:[ub.T,27],tab:[ub.U,9],del:[ub.V,46],space:[ub.W,32],backspace:[ub.X,8],up:[ub.Y,38],down:[ub.Z,40],left:[ub._,37],right:[ub.aa,39],insert:[ub.J,45],home:[ub.ba,36],end:[ub.ca,35],pgup:[ub.da,33],pgdn:[ub.ea,34]};o.toString=function(){return ub.fa+this.iJ+ub.ga+this.Up+ub.ha+this.Iy+ub.ia+this._L+ub.ja+this.fV;};p.rR={39:222,44:188,45:189,46:190,47:191,59:186,61:187,91:219,92:220,93:221,96:192};p.keyDownCharToCode=function(d){var
nb=null;if(d.length==1){var
La=d.charCodeAt(0);if(La>=65&&La<=90)nb=La;else if(La>=97&&La<=122)nb=La-32;else if(La>=48&&La<=57)nb=La;else nb=p.rR[La];}else if(p.u5[d.toLowerCase()]){nb=p.u5[d.toLowerCase()][1];}else if(d.match(ub.ka))nb=parseInt(RegExp.$1)+112-1;return nb;};p.keyDownCodeToChar=function(a,l){var
t=null;if(a>=65&&a<=90)t=String.fromCharCode(a+97-65);else if(a>=48&&a<=57)t=String.fromCharCode(a);else if(a>=112&&a<=126)t=ub.la+(a-112+1);else{for(var Da in p.rR)if(p.rR[Da]==a){t=String.fromCharCode(Da);break;}if(t==null){var
Cb=l&&jsx3.app.Browser.macosx?p.eV:p.u5;for(var Da in Cb)if(Cb[Da][1]==a){t=Cb[Da][0];break;}}}return t;};});jsx3.Class.defineClass("jsx3.gui.Painted",jsx3.app.Model,null,function(r,f){var
ub={k:"jsx3.gui.Painted.recalc",h:"object",c:/\"/g,H:"update box chunked",p:"beforeEnd",q:"paint inserted",G:"done",v:/\s*;\s*/g,I:"update box sync",i:"bench.",A:"Bottom",a:" ",F:"jsxafterresizeview",u:"_jsxxw",t:"_jsxFD",j:" repaints of ",s:"_jsxHK",z:"Right",d:"&quot;",o:"jsx3.gui.Painted.domPaint",D:"margin",w:/\s*:\s*/,r:"jsx3.gui.Painted.repaint",C:"padding",B:"Left",l:"repaint",g:"jsxcustom",b:'="',m:'<span id="',f:"",J:" box updates of ",y:"Top",n:'" style="display:none;"></span>',x:/(-\S)/gi,E:"border",e:'"'};r.MASK_NO_EDIT={NN:false,EE:false,SS:false,WW:false,MM:false};r.MASK_ALL_EDIT={NN:true,EE:true,SS:true,WW:true,MM:true};f.init=function(g){this.jsxsuper(g);};f.getAbsolutePosition=function(i,d){if(d==null)d=this.getRendered(i);if(d==null)return {L:0,T:0,W:0,H:0};if(i==null)i=this.kB().getRendered(d);return jsx3.html.getRelativePosition(i,d);};f.applyDynamicProperties=function(){if(this._jsxSi!=null){var
kb=this.getServer();if(kb==null)return;var
Aa=kb.getProperties();for(var ja in this._jsxSi)this[ja]=Aa.get(this._jsxSi[ja]);}};f.setDynamicProperty=function(b,m){if(this._jsxSi==null)this._jsxSi={};if(m==null){delete this._jsxSi[b];}else this._jsxSi[b]=m;return this;};f.getDynamicProperty=function(b){if(this._jsxSi)return this._jsxSi[b];};f.setAttribute=function(b,m){this.getAttributes()[b]=m;return this;};f.getAttribute=function(j){return this.getAttributes()[j];};f.getAttributes=function(){if(this.jsxcustom==null)this.jsxcustom={};return this.jsxcustom;};f.renderAttributes=function(p,c){var
la=[];if(this.jsxcustom!=null){var
v=jsx3.gui.Interactive;var
qa=v&&this.instanceOf(v);for(var xa in this.jsxcustom){var
nb=p!=null&&(p instanceof Array&&jsx3.util.arrIndexOf(p,xa)>=0||p[xa])||c&&qa&&v.isBridgeEventHandler(xa);var
ba=this.jsxcustom[xa];if(!nb&&ba!=null)la[la.length]=ub.a+xa+ub.b+ba.replace(ub.c,ub.d)+ub.e;}}return la.join(ub.f);};f.removeAttribute=function(n){delete this.getAttributes()[n];return this;};f.removeAttributes=function(){delete this[ub.g];return this;};f.focus=function(){var
t=this.getRendered();if(t!=null&&t.focus)t.focus();return t;};f.getMaskProperties=function(){return r.MASK_NO_EDIT;};f.getRendered=function(b){var
Ja=null;if(b instanceof jsx3.gui.Event){if(b.srcElement())Ja=b.srcElement().ownerDocument;}else if(b&&typeof b==ub.h)Ja=b.ownerDocument||(b.getElementById?b:null);if(Ja==null)Ja=this.getDocument();return Ja!=null?Ja.getElementById(this.getId()):null;};f.containsHtmlElement=function(m){var
nb=this.getRendered(m);if(nb)while(m!=null){if(nb==m)return true;m=m.parentNode;}return false;};f.getDocument=function(){var
wa=this;while(wa!=null){if(jsx3.gui.Window&&wa instanceof jsx3.gui.Window)return wa.getDocument();else if(wa._jsxCe!=null)return wa._jsxCe.getRootDocument();wa=wa.getParent();}return null;};f.kB=function(){var
E=this;while(E!=null){if(jsx3.gui.Window&&E instanceof jsx3.gui.Window)return E.getRootBlock();else if(E._jsxCe!=null)return E._jsxCe.getRootBlock();E=E.getParent();}return null;};r.CH=null;r.REPAINT_MAP=new
jsx3.util.WeakMap();f.repaint=function(){var
Lb=[this];while(Lb.length>0){var
Kb=Lb.shift();var
ua=1+(r.REPAINT_MAP.get(Kb.getId())||Number(0));if(ua>1){jsx3.util.Logger.getLogger(ub.i+r.jsxclass).warn(ua+ub.j+Kb);}else{var
ab=Kb.getChildren();if(ab.length>0)Lb.push.apply(Lb,ab);}r.REPAINT_MAP.set(Kb.getId(),ua);}var
Cb=this.getRendered();if(this.isDomPaint()){if(Cb!=null){var
ja=Cb.previousSibling;var
na=this.paintDom();if(na!=Cb)Cb.parentNode.replaceChild(na,Cb);else if(na.parentNode==null)ja.parentNode.insertBefore(na,ja);}return null;}else{var
A=null;if(Cb!=null){var
B=new
jsx3.util.Timer(r.jsxclass,this);A=this.paint();jsx3.html.setOuterHTML(Cb,A);r.n5(this,Cb);var
ib=Cb.ownerDocument;if(ib.recalc!=null)jsx3.sleep(function(){ib.recalc(true);},ub.k);B.log(ub.l);}return A;}};f.paint=jsx3.Method.newAbstract();f.onAfterPaint=function(s){};r.n5=function(h,e){var
Kb=[h];while(Kb.length>0){var
Nb=Kb.shift();if(Nb.onAfterPaint!=f.onAfterPaint){var
ta=Nb.getRendered(e);if(ta)Nb.onAfterPaint(ta);}var
ab=Nb.getChildren();if(ab.length>0)Kb.unshift.apply(Kb,ab);}};f.isDomPaint=function(){return false;};f.paintDom=function(){throw new
jsx3.Exception();};f.e1=function(){return ub.m+this.getId()+ub.n;};r.DI=[];r.sW=function(o){r.DI.push(o);jsx3.sleep(r.Mo,ub.o);};r.Mo=function(){for(var
eb=0;eb<r.DI.length;eb++){var
Ba=r.DI[eb];var
Q=Ba.getRendered();if(Q!=null){var
v=Ba.paintDom();Q.parentNode.replaceChild(v,Q);}}r.DI.splice(0,r.DI.length);};f.paintChild=function(e,b,l,h){if(l==null)l=this.getRendered();if(l!=null){if(!h)if(e.isDomPaint()){l.appendChild(e.paintDom());}else{var
t=new
jsx3.util.Timer(r.jsxclass,this);jsx3.html.insertAdjacentHTML(l,ub.p,e.paint());t.log(ub.q);}r.n5(e,l);}};f.Lc=function(s,p){this.paintChild(s,p);};f.insertHTML=function(o){this.paintChild(o);return this;};f.paintChildren=function(c){if(c==null)c=this.getChildren();var
M=new
Array(c.length);for(var
da=0;da<c.length;da++){var
la=c[da];if(la.isDomPaint()){M[da]=la.e1();r.sW(la);}else{var
Fa=la.getLoadType();if(Fa==1||Fa==2||Fa==3){M[da]=la.e1();jsx3.sleep(jsx3.makeCallback(ub.l,la),ub.r+la.getId());}else if((Fa==5||Fa==4)&&!la.cj()){M[da]=la.e1();}else M[da]=la.paint();}}return M.join(ub.f);};f.Wl=function(l,p){if(this._jsxHK)this.ki();if(this._jsxxw==null&&l)this._jsxxw=this.Gc(p);return this._jsxxw;};f.vl=function(p){this._jsxxw=p;};f.ce=function(){this._jsxHK=true;};f.ki=function(m){var
_=[this];while(_.length>0){var
S=_.shift();delete S[ub.s];delete S[ub.t];if(S._jsxxw){delete S[ub.u];if(m){var
cb=S.getChildren();if(cb.length>0)_.push.apply(_,cb);}}}};r.Si=function(e,d,i){if(d){var
sb=jsx3.util.strTrim(d).split(ub.v);for(var
x=0;x<sb.length;x++){var
G=sb[x];if(G==ub.f)continue;var
w=G.split(ub.w);if(w&&w.length==2){var
V=w[0].replace(ub.x,function(q,h){return h.substring(1).toUpperCase();});e.style[V]=w[1];}}}else if(i){var
Bb=[ub.y,ub.z,ub.A,ub.B];for(var
x=0;x<4;x++){var
V=i+Bb[x];e.style[V]=ub.f;}}};f.recalcBox=function(i){this.findDescendants(function(j){j.ki(false);},true,true,false,true);this.ac(this.getParent()?this.getParent().uf(this):null,this.getRendered());if(i){var
xb=this.getRendered();if(xb!=null){var
bb=this.Wl(true);for(var
jb=0;jb<i.length;jb++)if(i[jb]==ub.C){r.Si(xb,bb.hm(),ub.C);}else if(i[jb]==ub.D){r.Si(xb,bb.Ph(),ub.D);}else if(i[jb]==ub.E)r.Si(xb,bb.Dh(),ub.E);}}};f.uf=function(){var
aa=this._jsxxw;return aa!=null?{parentwidth:aa.mn(),parentheight:aa.ec()}:{};};f.bm=function(h){var
C=this._jsxFD?this._jsxFD[h]:null;return C;};f.Ck=function(d,l){if(!this._jsxFD)this._jsxFD=[];this._jsxFD[d]=l;return l;};f.Gc=function(g){return new
r.Box({});};f.Rc=function(j,c,l){this.Gi(j,c,l,1);};f.Gi=function(q,n,a,d){if(d==1){this.ce();if(n!=null)a.addRepaint(this);}else if(d==2||d==4){var
Fb=this.Wl(true,q);var
pa=Fb.recalculate(q,n,a);if(pa.w||pa.h){if(!r._RESIZE_EVENT&&jsx3.gui.Interactive)r._RESIZE_EVENT={subject:ub.F};if(r._RESIZE_EVENT)this.publish(r._RESIZE_EVENT);var
N=this.getChildren();var
w=d==4&&n?Math.max(0,n.childNodes.length-N.length):0;for(var
Eb=N.length-1;Eb>=0;Eb--){var
u=N[Eb];var
x=u._jsxxw;if(x&&x.Or())continue;var
U=Eb+w;var
ga=n?n.childNodes[U]?n.childNodes[U]:true:null;a.add(u,{parentwidth:Fb.mn(),parentheight:Fb.ec()},ga,true);}}else{}}else if(d==3){var
Fb=this.Wl(true,q);if(n)Fb.recalculate(q,n,a);}};f.af=function(m,o){var
Z=new
jsx3.util.Timer(r.jsxclass,this);var
Bb=new
r.Queue();Bb.add(this,m,o);Bb.subscribe(ub.G,function(){Z.log(ub.H);});Bb.start();};r.RO={};r.RO.add=function(p,o,n){var
oa=new
jsx3.util.Timer(r.jsxclass,p);p.t5(this,o,n);oa.log(ub.I);};r.RO.addRepaint=function(b){b.repaint();};f.ac=function(o,l){r.RO.add(this,o,l);};r.UPDATE_MAP=new
jsx3.util.WeakMap();f.t5=function(o,c,s){var
pa=1+(r.UPDATE_MAP.get(this.getId())||Number(0));if(pa>1)jsx3.util.Logger.getLogger(ub.i+r.jsxclass).warn(pa+ub.J+this);r.UPDATE_MAP.set(this.getId(),pa);this.applyDynamicProperties();delete this[ub.t];this.Rc(c,s,o);};});jsx3.Class.defineClass("jsx3.gui.Painted.Queue",jsx3.lang.Object,[jsx3.util.EventDispatcher],function(h,g){var
ub={d:"done",i:"}",a:"jsx3.gui.Painted.queue",h:"-",c:"push",f:"{Painted.Queue ",g:" ",b:"unshift",e:"e7"};h.F1=250;h.cN=0;h.Tp=new
jsx3.util.List();h.lO=true;h.oq=false;h.enableChunking=function(j){h.lO=j;};h.OJ=function(){h.doChunk();};h.doChunk=function(){if(h.lO){if(h.oq)return;h.oq=true;var
xb=(new
Date()).getTime()+h.F1;var
kb=(new
Date()).getTime();var
wb=h.Tp.removeAt(0);while(wb!=null&&kb<xb)if(wb.e7.length>0){var
Ib=wb.e7.shift();if(Ib instanceof Array)Ib[0].t5(wb,Ib[1],Ib[2]);else Ib.repaint();kb=(new
Date()).getTime();}else{wb.destroy();wb=h.Tp.removeAt(0);}if(wb!=null){h.Tp.add(wb,0);jsx3.sleep(h.doChunk,ub.a);}h.oq=false;}else while(h.Tp.size()>0){var
wb=h.Tp.removeAt(0);while(wb.e7.length>0){var
Ib=wb.e7.shift();if(Ib instanceof Array)Ib[0].t5(wb,Ib[1],Ib[2]);else Ib.repaint();}}};g.init=function(){this.rS=
++h.cN;this.e7=[];h.Tp.add(this);};g.add=function(m,b,r,d){if(r===true)r=m.getRendered();this.e7[d?ub.b:ub.c]([m,b,r]);};g.addRepaint=function(j,c){this.e7[c?ub.b:ub.c](j);};g.start=function(){h.OJ();};g.destroy=function(){this.publish({subject:ub.d});delete this[ub.e];h.Tp.remove(this);};g.toString=function(){return ub.f+this.rS+ub.g+(this.e7!=null?this.e7.length:ub.h)+ub.i;};});jsx3.Class.defineClass("jsx3.gui.Painted.Box",jsx3.lang.Object,null,function(e,n){var
xb={Qa:": ",_:"</",ea:"input",q:"U0",ra:"_jsx3_html_scr",V:"top:0px",v:"right",U:"left:",u:"Q6",ka:"border-left:",z:"display:-moz-inline-box;",d:"tagname",La:/border(?:(?:-bottom(?:-style)?)|(?:-style))?:[^;]*(dashed|dotted|double|groove|hidden|inset|none|outset|ridge|solid)/gi,D:"string",fa:/(^[;\s]*)|([;\s]*$)/g,R:'"/>',w:"bottom",Ca:/-left/,Q:"height:",Na:/border(?:(?:-left(?:-color)?)|(?:-color))?:[^;]*((?:#[a-zA-Z0-9]{6})|(?:rgb\s*\(\s*\d{1,3}%?\s*,\s*\d{1,3}%?\s*,\s*\d{1,3}%?\s*\))|(?:AliceBlue|AntiqueWhite|Aqua|Aquamarine|Azure|Beige|Bisque|Black|BlanchedAlmond|Blue|BlueViolet|Brown|BurlyWood|CadetBlue|Chartreuse|Chocolate|Coral|CornflowerBlue|Cornsilk|Crimson|Cyan|DarkBlue|DarkCyan|DarkGoldenrod|DarkGray|DarkGreen|DarkKhaki|DarkMagenta|DarkOliveGreen|DarkOrange|DarkOrchid|DarkRed|DarkSalmon|DarkSeaGreen|DarkSlateBlue|DarkSlateGray|DarkTurquoise|DarkViolet|DeepPink|DeepSkyBlue|DimGray|DodgerBlue|FireBrick|FloralWhite|ForestGreen|Fuchsia|Gainsboro|GhostWhite|Gold|Goldenrod|Gray|Green|GreenYellow|Honeydew|HotPink|IndianRed|Indigo|Ivory|Khaki|Lavender|LavenderBlush|LawnGreen|LemonChiffon|LightBlue|LightCora|LightCyan|LightGoldenrodYellow|LightGreen|LightGrey|LightPink|LightSalmon|LightSeaGreen|LightSkyBlue|LightSlateGray|LightSteelBlu|LightYellow|Lime|LimeGreen|Linen|Magenta|Maroon|MediumAquamarine|MediumBlue|MediumOrchid|MediumPurple|MediumSeaGreen|MediumSlateBlue|MediumSpringGreen|MediumTurquoise|MediumVioletRed|MidnightBlue|MintCream|MistyRose|Moccasin|NavajoWhite|Navy|OldLace|Olive|OliveDrab|Orange|OrangeRed|Orchid|PaleGoldenrod|PaleGreen|PaleTurquoise|PaleVioletRed|PapayaWhip|PeachPuff|Peru|Pink|Plum|PowderBlue|Purple|Red|RosyBrown|RoyalBlue|SaddleBrown|Salmon|SandyBrown|SeaGreen|Seashell|Sienna|Silver|SkyBlue|SlateBlue|SlateGray|Snow|SpringGreen|SteelBlue|Tan|Teal|Thistle|Tomato|Turquoise|Violet|Wheat|White|WhiteSmoke|Yellow|YellowGreen))/gi,Pa:"IMPLICIT:\n",la:"text",qa:"beforeEnd",y:"display:inline-table;",x:"",ja:"border-bottom:",W:"top:",Fa:/border(?:(?:-top(?:-style)?)|(?:-style))?:[^;]*(dashed|dotted|double|groove|hidden|inset|none|outset|ridge|solid)/gi,Ha:/border(?:(?:-right(?:-color)?)|(?:-color))?:[^;]*((?:#[a-zA-Z0-9]{6})|(?:rgb\s*\(\s*\d{1,3}%?\s*,\s*\d{1,3}%?\s*,\s*\d{1,3}%?\s*\))|(?:AliceBlue|AntiqueWhite|Aqua|Aquamarine|Azure|Beige|Bisque|Black|BlanchedAlmond|Blue|BlueViolet|Brown|BurlyWood|CadetBlue|Chartreuse|Chocolate|Coral|CornflowerBlue|Cornsilk|Crimson|Cyan|DarkBlue|DarkCyan|DarkGoldenrod|DarkGray|DarkGreen|DarkKhaki|DarkMagenta|DarkOliveGreen|DarkOrange|DarkOrchid|DarkRed|DarkSalmon|DarkSeaGreen|DarkSlateBlue|DarkSlateGray|DarkTurquoise|DarkViolet|DeepPink|DeepSkyBlue|DimGray|DodgerBlue|FireBrick|FloralWhite|ForestGreen|Fuchsia|Gainsboro|GhostWhite|Gold|Goldenrod|Gray|Green|GreenYellow|Honeydew|HotPink|IndianRed|Indigo|Ivory|Khaki|Lavender|LavenderBlush|LawnGreen|LemonChiffon|LightBlue|LightCora|LightCyan|LightGoldenrodYellow|LightGreen|LightGrey|LightPink|LightSalmon|LightSeaGreen|LightSkyBlue|LightSlateGray|LightSteelBlu|LightYellow|Lime|LimeGreen|Linen|Magenta|Maroon|MediumAquamarine|MediumBlue|MediumOrchid|MediumPurple|MediumSeaGreen|MediumSlateBlue|MediumSpringGreen|MediumTurquoise|MediumVioletRed|MidnightBlue|MintCream|MistyRose|Moccasin|NavajoWhite|Navy|OldLace|Olive|OliveDrab|Orange|OrangeRed|Orchid|PaleGoldenrod|PaleGreen|PaleTurquoise|PaleVioletRed|PapayaWhip|PeachPuff|Peru|Pink|Plum|PowderBlue|Purple|Red|RosyBrown|RoyalBlue|SaddleBrown|Salmon|SandyBrown|SeaGreen|Seashell|Sienna|Silver|SkyBlue|SlateBlue|SlateGray|Snow|SpringGreen|SteelBlue|Tan|Teal|Thistle|Tomato|Turquoise|Violet|Wheat|White|WhiteSmoke|Yellow|YellowGreen))/gi,i:"top",Sa:"\nEXPLICIT:\n",A:"display:inline-block;",t:"E3",Ia:/border(?:(?:-right(?:-style)?)|(?:-style))?:[^;]*(dashed|dotted|double|groove|hidden|inset|none|outset|ridge|solid)/gi,s:"yy",X:"position:absolute;",r:"rq",sa:"not matched",C:"0px",l:"empty",L:":",ia:"border-right:",Ka:/border(?:(?:-bottom(?:-color)?)|(?:-color))?:[^;]*((?:#[a-zA-Z0-9]{6})|(?:rgb\s*\(\s*\d{1,3}%?\s*,\s*\d{1,3}%?\s*,\s*\d{1,3}%?\s*\))|(?:AliceBlue|AntiqueWhite|Aqua|Aquamarine|Azure|Beige|Bisque|Black|BlanchedAlmond|Blue|BlueViolet|Brown|BurlyWood|CadetBlue|Chartreuse|Chocolate|Coral|CornflowerBlue|Cornsilk|Crimson|Cyan|DarkBlue|DarkCyan|DarkGoldenrod|DarkGray|DarkGreen|DarkKhaki|DarkMagenta|DarkOliveGreen|DarkOrange|DarkOrchid|DarkRed|DarkSalmon|DarkSeaGreen|DarkSlateBlue|DarkSlateGray|DarkTurquoise|DarkViolet|DeepPink|DeepSkyBlue|DimGray|DodgerBlue|FireBrick|FloralWhite|ForestGreen|Fuchsia|Gainsboro|GhostWhite|Gold|Goldenrod|Gray|Green|GreenYellow|Honeydew|HotPink|IndianRed|Indigo|Ivory|Khaki|Lavender|LavenderBlush|LawnGreen|LemonChiffon|LightBlue|LightCora|LightCyan|LightGoldenrodYellow|LightGreen|LightGrey|LightPink|LightSalmon|LightSeaGreen|LightSkyBlue|LightSlateGray|LightSteelBlu|LightYellow|Lime|LimeGreen|Linen|Magenta|Maroon|MediumAquamarine|MediumBlue|MediumOrchid|MediumPurple|MediumSeaGreen|MediumSlateBlue|MediumSpringGreen|MediumTurquoise|MediumVioletRed|MidnightBlue|MintCream|MistyRose|Moccasin|NavajoWhite|Navy|OldLace|Olive|OliveDrab|Orange|OrangeRed|Orchid|PaleGoldenrod|PaleGreen|PaleTurquoise|PaleVioletRed|PapayaWhip|PeachPuff|Peru|Pink|Plum|PowderBlue|Purple|Red|RosyBrown|RoyalBlue|SaddleBrown|Salmon|SandyBrown|SeaGreen|Seashell|Sienna|Silver|SkyBlue|SlateBlue|SlateGray|Snow|SpringGreen|SteelBlue|Tan|Teal|Thistle|Tomato|Turquoise|Violet|Wheat|White|WhiteSmoke|Yellow|YellowGreen))/gi,J:"px;",Da:/border(?:(?:-top(?:-width)?)|(?:-width))?:[^0-9]*([0-9]*)px/gi,ta:"0",oa:"body",O:" ",k:"height",Ga:/border(?:(?:-right(?:-width)?)|(?:-width))?:[^0-9]*([0-9]*)px/gi,p:"lT",P:"width:",I:"px",ha:"border-top:",a:/[^\d-]*([-]*[\d]*)[^\d-]*([-]*[\d]*)[^\d-]*([-]*[\d]*)[^\d-]*([-]*[\d]*)/,Aa:/-right/,F:"object",Ra:"\n",j:"width",ua:/(\s*(padding|padding-top|padding-right|padding-bottom|padding-left)\s*:\s*(\d+)(px)?\s*((\d+)(px)?)?\s*((\d+)(px)?)?\s*((\d+)(px)?)?\s*;)+/ig,S:'">',Ma:/border(?:(?:-left(?:-width)?)|(?:-width))?:[^0-9]*([0-9]*)px/gi,K:"px ",aa:">",B:"box",g:"border",b:/\b(\d*)px/g,va:/(\s*(margin|margin-top|margin-right|margin-bottom|margin-left)\s*:\s*(-*\d+)(px)?\s*((-*\d+)(px)?)?\s*((-*\d+)(px)?)?\s*((-*\d+)(px)?)?\s*;)+/ig,M:"relativebox",f:"padding",T:"left:0px",ga:"pseudo",ba:"resize",e:"margin",za:/-top/,h:"left",c:"boxtype",H:";",wa:"Missing Semicolon",G:"%",Ja:/border(?:(?:-bottom(?:-width)?)|(?:-width))?:[^0-9]*([0-9]*)px/gi,ma:"password",ca:"onResize",pa:'<div id="_jsx3_html_scr" class="jsx30block" style="padding:0px;margin:0px;border-width:0px;position:absolute;width:100px;height:100px;left:-100px;top:-100px;overflow:scroll;">&#160;</div>',N:"<",o:"m3",Z:"position:relative;",xa:/[^\s*]/i,na:"textarea",Ba:/-bottom/,da:/input\[(\S*)\]/i,Oa:/border(?:(?:-left(?:-style)?)|(?:-style))?:[^;]*(dashed|dotted|double|groove|hidden|inset|none|outset|ridge|solid)/gi,m:"container",Y:' style="',Ea:/border(?:(?:-top(?:-color)?)|(?:-color))?:[^;]*((?:#[a-zA-Z0-9]{6})|(?:rgb\s*\(\s*\d{1,3}%?\s*,\s*\d{1,3}%?\s*,\s*\d{1,3}%?\s*\))|(?:AliceBlue|AntiqueWhite|Aqua|Aquamarine|Azure|Beige|Bisque|Black|BlanchedAlmond|Blue|BlueViolet|Brown|BurlyWood|CadetBlue|Chartreuse|Chocolate|Coral|CornflowerBlue|Cornsilk|Crimson|Cyan|DarkBlue|DarkCyan|DarkGoldenrod|DarkGray|DarkGreen|DarkKhaki|DarkMagenta|DarkOliveGreen|DarkOrange|DarkOrchid|DarkRed|DarkSalmon|DarkSeaGreen|DarkSlateBlue|DarkSlateGray|DarkTurquoise|DarkViolet|DeepPink|DeepSkyBlue|DimGray|DodgerBlue|FireBrick|FloralWhite|ForestGreen|Fuchsia|Gainsboro|GhostWhite|Gold|Goldenrod|Gray|Green|GreenYellow|Honeydew|HotPink|IndianRed|Indigo|Ivory|Khaki|Lavender|LavenderBlush|LawnGreen|LemonChiffon|LightBlue|LightCora|LightCyan|LightGoldenrodYellow|LightGreen|LightGrey|LightPink|LightSalmon|LightSeaGreen|LightSkyBlue|LightSlateGray|LightSteelBlu|LightYellow|Lime|LimeGreen|Linen|Magenta|Maroon|MediumAquamarine|MediumBlue|MediumOrchid|MediumPurple|MediumSeaGreen|MediumSlateBlue|MediumSpringGreen|MediumTurquoise|MediumVioletRed|MidnightBlue|MintCream|MistyRose|Moccasin|NavajoWhite|Navy|OldLace|Olive|OliveDrab|Orange|OrangeRed|Orchid|PaleGoldenrod|PaleGreen|PaleTurquoise|PaleVioletRed|PapayaWhip|PeachPuff|Peru|Pink|Plum|PowderBlue|Purple|Red|RosyBrown|RoyalBlue|SaddleBrown|Salmon|SandyBrown|SeaGreen|Seashell|Sienna|Silver|SkyBlue|SlateBlue|SlateGray|Snow|SpringGreen|SteelBlue|Tan|Teal|Thistle|Tomato|Turquoise|Violet|Wheat|White|WhiteSmoke|Yellow|YellowGreen))/gi,n:"BW",ya:"Mismatch Rule",E:"number"};e.l6=xb.a;e.X5=xb.b;e.T6=[xb.c,xb.d,xb.e,xb.f,xb.g,xb.h,xb.i,xb.j,xb.k,xb.l,xb.m];e.O1=[xb.c,xb.h,xb.i,xb.j,xb.k];e.CB={width:xb.n,height:xb.o,top:xb.p,left:xb.q,padding:xb.r,border:xb.s,margin:xb.t,tagname:xb.u};e.Zr=[xb.i,xb.v,xb.w,xb.h];e.Os=null;if(jsx3.CLASS_LOADER.SAF){e.VV=[xb.x,xb.y,xb.x,xb.y];}else e.VV=[xb.x,xb.z,xb.A,xb.z];e.bX={pad:xb.f,mar:xb.e,e:xb.x,box:xb.B,zpx:xb.C,str:xb.D,num:xb.E,obj:xb.F,pct:xb.G,semi:xb.H,px:xb.I,pxs:xb.J,pxc:xb.K,c:xb.L,rbox:xb.M,bor:xb.g};e.D6={hph:{height:1,parentheight:1},wpw:{width:1,parentwidth:1}};e.OQ=[xb.N,xb.O,xb.P,xb.Q,xb.R,xb.S,xb.T,xb.U,xb.V,xb.W,xb.X,xb.Y,xb.Z,xb._,xb.aa,xb.x];e.getCssFix=function(){return e.VV[jsx3.html.getMode()];};n.W6=xb.x;n.ix=xb.x;n.mE=xb.x;n.fG=true;n.styles=xb.x;n.attributes=xb.x;n.init=function(d){this.implicit=d||{};this.calculate();};n.paint=function(){this.fG=false;var
Da=new
Array(2);var
ea=e.OQ[0]+this.Ts.tagname+e.OQ[1]+this.attributes;var
Ca=this.cm();Ca=Ca!=null?e.OQ[2]+Math.max(0,Ca)+e.bX.pxs:e.bX.e;var
ha=this.Zm();ha=ha!=null?e.OQ[3]+Math.max(0,ha)+e.bX.pxs:e.bX.e;var
V=this.Ts.empty?e.OQ[4]:e.OQ[5];if(this.Ts.boxtype==e.bX.box){var
ua=this.Ts.left;ua=ua==null?e.OQ[6]:e.OQ[7]+ua+e.bX.pxs;var
_=this.Ts.top;_=_==null?e.OQ[8]:e.OQ[9]+_+e.bX.pxs;var
pa=this.implicit.omitpos?e.bX.e:e.OQ[10];Da[0]=ea+e.OQ[11]+pa+Ca+ha+ua+_+this.hm()+this.Ph()+this.Dh()+this.styles+V;}else if(this.Ts.boxtype==e.bX.rbox){var
pa=this.implicit.omitpos?e.bX.e:e.OQ[12];Da[0]=ea+e.OQ[11]+pa+this.dK()+Ca+ha+this.hm()+this.Ph()+this.Dh()+this.styles+V;}else{var
ua=this.Ts.left;ua=ua==null?e.bX.e:e.OQ[7]+ua+e.bX.pxs;var
_=this.Ts.top;_=_==null?e.bX.e:e.OQ[9]+_+e.bX.pxs;var
pa=this.implicit.omitpos?e.bX.e:e.OQ[12];Da[0]=ea+e.OQ[11]+pa+Ca+ha+ua+_+this.hm()+this.Ph()+this.Dh()+this.styles+V;}Da[1]=this.Ts.empty?e.OQ[15]:e.OQ[13]+this.Ts.tagname+e.OQ[14];return Da;};n.setStyles=function(h){this.styles=h;return this;};n.setAttributes=function(s){this.attributes=s;return this;};n.dK=function(){return this.Ts.container&&(jsx3.html.getMode()==3||jsx3.html.getMode()==1)?e.bX.e:e.getCssFix();};n.Or=function(){var
pa=this.implicit;return (typeof pa.width!=e.bX.str||pa.width.indexOf(e.bX.pct)<0)&&(typeof pa.height!=e.bX.str||pa.height.indexOf(e.bX.pct)<0)&&(typeof pa.left!=e.bX.str||pa.left.indexOf(e.bX.pct)<0)&&(typeof pa.top!=e.bX.str||pa.top.indexOf(e.bX.pct)<0);};e._RECALC_VALS=[[[[{n:1},{h:1}],[{w:1},{w:1,h:1}]],[[{t:1},{t:1,h:1}],[{t:1,w:1},{t:1,w:1,h:1}]]],[[[{l:1},{l:1,h:1}],[{l:1,w:1},{l:1,w:1,h:1}]],[[{l:1,t:1},{l:1,t:1,h:1}],[{l:1,t:1,w:1},{l:1,t:1,w:1,h:1,a:1}]]]];n.recalculate=function(g,p,r){var
J=this.fG;var
Y=0,w=0,R=0,eb=0;for(var ab in g)if(this.implicit[ab]!=g[ab]){this.implicit[ab]=g[ab];J=true;if(!R&&e.D6.wpw[ab])R=1;if(!eb&&e.D6.hph[ab])eb=1;}if(J){this.calculate(e.O1);if(p){var
K=p.style;if(this.Ts.boxtype==e.bX.box&&this.Ts.left!=null&&this.Ts.top!=null){if(parseInt(K.left)!=this.Ts.left){K.left=this.Ts.left+e.bX.px;Y=1;}if(parseInt(K.top)!=this.Ts.top){K.top=this.Ts.top+e.bX.px;w=1;}}if(g.parentheight!=null||g.parentwidth!=null||g.width!=null||g.height!=null){var
Eb=this.cm();var
z=this.Zm();if(Eb!=null&&parseInt(K.width)!=Eb){K.width=Math.max(0,Eb)+e.bX.px;R=1;}else R=0;if(z!=null&&parseInt(K.height)!=z){K.height=Math.max(0,z)+e.bX.px;eb=1;}else eb=0;}}}this.fG=false;return e._RECALC_VALS[Y][w][R][eb];};e._x={left:1,top:1};e.pH=function(){};e.pH.prototype={padding:xb.x,margin:xb.x,border:xb.x,bwidth:0,bheight:0,btop:0,bleft:0,pwidth:0,pheight:0,ptop:0,pleft:0};n.calculate=function(f){if(!f)f=e.T6;if(!this.Ts)this.Ts=new
e.pH();var
ka=this.Ts;for(var
da=0;da<f.length;da++){var
I=f[da];var
Ab=this.implicit[I];if(e._x[I]&&(Ab==null||Ab==e.bX.e)&&this.implicit.boxtype==e.bX.box){ka[I]=0;}else{var
G=e.CB[I];if(G){if(Ab===e.bX.e)Ab=null;this[G](Ab);}else this.Ts[I]=Ab;}}};e.registerServer=function(q,g){if(g)jsx3.gui.Event.subscribe(xb.ba,q,xb.ca);};e.unregisterServer=function(f,r){if(r)jsx3.gui.Event.unsubscribe(xb.ba,f,xb.ca);};n.BW=function(c){if(c==null){this.Ts.width=this.Ts.clientwidth=null;}else{if(typeof c==e.bX.str&&c.indexOf(e.bX.pct)>=0)c=Math.round(this.implicit.parentwidth*parseInt(c)/100);else c=Number(c);this.Ts.width=c;this.Ts.clientwidth=Math.max(0,c-this.Ts.pwidth-this.Ts.bwidth);}};n.m3=function(j){if(j==null){this.Ts.height=this.Ts.clientheight=null;}else{if(typeof j==e.bX.str&&j.indexOf(e.bX.pct)>=0)j=Math.round(this.implicit.parentheight*parseInt(j)/100);else j=Number(j);this.Ts.height=j;this.Ts.clientheight=Math.max(0,j-this.Ts.pheight-this.Ts.bheight);}};n.U0=function(c){this.Ts.left=typeof c==e.bX.str&&c.indexOf(e.bX.pct)>=0?Math.round(this.implicit.parentwidth*parseInt(c)/100):c==null?c:Number(c);};n.lT=function(h){this.Ts.top=typeof h==e.bX.str&&h.indexOf(e.bX.pct)>=0?Math.round(this.implicit.parentheight*parseInt(h)/100):h==null?h:Number(h);};n.Q6=function(j){if(j==null){this.Ts.tagname=j;this.Ts.type=j;}else if(j.search(xb.da)>-1){this.Ts.tagname=xb.ea;this.Ts.type=RegExp.$1.toLowerCase();}else this.Ts.tagname=j;};n.yy=function(h){if(h==null)h=e.bX.e;if(this.W6===h)return;this.W6=h;var
sa=null,F=null;if(typeof h==e.bX.str&&h.indexOf(xb.L)>=0){var
G=e.U7(h);if(typeof G!=e.bX.obj)sa=G.split(e.bX.semi);}else{h=h.replace(xb.fa,e.bX.e);if(h!==e.bX.e)sa=h.split(e.bX.semi);}if(sa&&sa.length>1){var
Va=true;for(var
jb=0;Va&&jb<sa.length-1&&jb<3;jb++)if(sa[jb]!=sa[jb+1])Va=false;if(Va)sa.splice(1,sa.length);}if(!sa){F=[0,0,0,0];}else if(sa.length==1){var
v=sa[0].match(e.X5);var
H=v?parseInt(v[0]):0;if(isNaN(H))H=0;F=[H,H,H,H];}else{F=[];for(var
jb=0;jb<4;jb++){var
v=sa[jb].match(e.X5);var
H=v?parseInt(v[0]):0;if(isNaN(H))H=0;F[jb]=H;}}this.Ts.bwidth=F[1]+F[3];this.Ts.bheight=F[0]+F[2];this.Ts.bleft=F[3];this.Ts.btop=F[0];if(sa)for(var
jb=0;jb<sa.length;jb++)if(sa[jb].indexOf(xb.ga)>=0)sa[jb]=e.bX.e;if(sa==null){this.Ts.border=e.bX.e;}else if(sa.length==1){this.Ts.border=sa[0]?e.bX.bor+e.bX.c+(F[0]>0?sa[0]:e.bX.zpx)+e.bX.semi:e.bX.e;}else if(sa.length==4)this.Ts.border=(sa[0]?xb.ha+(F[0]>0?sa[0]:e.bX.zpx)+e.bX.semi:e.bX.e)+(sa[1]?xb.ia+(F[1]>0?sa[1]:e.bX.zpx)+e.bX.semi:e.bX.e)+(sa[2]?xb.ja+(F[2]>0?sa[2]:e.bX.zpx)+e.bX.semi:e.bX.e)+(sa[3]?xb.ka+(F[3]>0?sa[3]:e.bX.zpx)+e.bX.semi:e.bX.e);};n.E3=function(l){if(l==null)l=e.bX.e;if(this.ix===l)return;this.ix=l;var
Db=null;if(typeof l==e.bX.str&&l.indexOf(xb.L)>-1){var
ba=e.gY(l,e.bX.mar);if(typeof ba!=e.bX.obj)Db=ba.match(e.l6);}else if(typeof l==e.bX.num){Db=[l];}else{l=jsx3.util.strTrim(String(l));if(l!==e.bX.e)if(isNaN(l))Db=l.match(e.l6);else Db=[Number(l)];}if(Db==null)this.Ts.margin=e.bX.e;else if(Db.length==1)this.Ts.margin=e.bX.mar+e.bX.c+Db[0]+e.bX.pxs;else this.Ts.margin=e.bX.mar+e.bX.c+Db[1]+e.bX.pxc+Db[2]+e.bX.pxc+Db[3]+e.bX.pxc+Db[4]+e.bX.pxs;};n.rq=function(c){if(c==null)c=e.bX.e;if(this.mE===c)return;this.mE=c;var
ob=null;if(typeof c==e.bX.str&&c.indexOf(xb.L)>-1){var
t=e.gY(c,e.bX.pad);if(typeof t!=e.bX.obj)ob=t.match(e.l6);}else if(typeof c==e.bX.num){ob=[c];}else{c=jsx3.util.strTrim(String(c));if(c!==e.bX.e)if(isNaN(c))ob=c.match(e.l6);else ob=[Number(c)];}var
M=null;if(ob==null){M=[0,0,0,0];this.Ts.padding=e.bX.e;}else if(ob.length==1){var
pb=ob[0];M=[pb,pb,pb,pb];this.Ts.padding=e.bX.pad+e.bX.c+pb+e.bX.pxs;}else{M=[];for(var
Ma=1;Ma<5;Ma++){var
pb=parseInt(ob[Ma]);if(isNaN(pb))pb=0;M[Ma-1]=pb;}this.Ts.padding=e.bX.pad+e.bX.c+M[0]+e.bX.pxc+M[1]+e.bX.pxc+M[2]+e.bX.pxc+M[3]+e.bX.pxs;}this.Ts.pwidth=M[1]+M[3];this.Ts.pheight=M[0]+M[2];this.Ts.ptop=M[0];this.Ts.pleft=M[3];};n.Yf=function(p){var
Hb=this.r7;if(!Hb)Hb=this.r7=[];Hb[Hb.length]=p;};n.lg=function(h){return this.r7?this.r7[h]:null;};n.xm=function(){return this.Ts.bleft+this.Ts.pleft;};n.gl=function(){return this.Ts.btop+this.Ts.ptop;};n.mn=function(){return this.Ts.clientwidth;};n.ec=function(){return this.Ts.clientheight;};n._b=function(){return this.Ts.width;};n.lh=function(){return this.Ts.height;};n.getBorderWidth=function(){return this.Ts.bwidth;};n.cm=function(){var
Ha=this.Ts.type;var
cb=jsx3.html.getMode();var
ta=cb==0||(Ha==xb.la||Ha==xb.ma||this.Ts.tagname==xb.na)&&cb==1?this.Ts.width:this.Ts.clientwidth;return ta===e.bX.e||isNaN(ta)?null:ta;};n.Zm=function(){var
E=this.Ts.type;var
Ia=jsx3.html.getMode();var
ja=Ia==0||(E==xb.la||E==xb.ma||this.Ts.tagname==xb.na)&&Ia==1?this.Ts.height:this.Ts.clientheight;return ja===e.bX.e||isNaN(ja)?null:ja;};n.bc=function(){return this.Ts.left;};n.mh=function(){return this.Ts.top;};n.getBoxType=function(){return this.Ts.boxtype;};n.Ph=function(){return this.Ts.margin||e.bX.e;};n.hm=function(){return this.Ts.padding||e.bX.e;};n.Dh=function(){return this.Ts.border||e.bX.e;};e.getBody=function(){return document.getElementsByTagName(xb.oa)[0];};e.getScrollSize=function(p){if(e.Os==null){var
Xa=p||e.getBody();var
Qa=xb.pa;jsx3.html.insertAdjacentHTML(Xa,xb.qa,Qa);var
na=document.getElementById(xb.ra);e.Os=100-parseInt(na.clientWidth);Xa.removeChild(na);}return e.Os;};e.getScrollSizeOffset=function(l){var
_a=e.getScrollSize();return jsx3.html.getScrollSizeOffset(_a,l);};e.gY=function(b,i){var
ta=xb.sa;var
Ba=xb.ta;var
Bb=xb.ta;var
Ca=xb.ta;var
tb=xb.ta;var
ya=xb.ua;var
va=xb.va;var
aa=i==e.bX.pad?ya:va;var
P=b.split(e.bX.semi);if(P)for(var
wa=0;wa<P.length;wa++){var
Ja=P[wa]+e.bX.semi;var
w=Ja.search(aa);if(w>0){return {desc:xb.wa,cause:P[wa]};}else if(w==-1){if(P[wa].search(xb.xa)>=0)return {desc:xb.ya,cause:P[wa]};}else{ta=Ja.replace(aa,function(r,q,p,o,s,m,l,k,j,a,c,d,f){if(p.match(xb.za)){Ba=o==null?xb.ta:o;}else if(p.match(xb.Aa)){Bb=o==null?xb.ta:o;}else if(p.match(xb.Ba)){Ca=o==null?xb.ta:o;}else if(p.match(xb.Ca)){tb=o==null?xb.ta:o;}else{Ba=jsx3.util.strEmpty(o)?xb.ta:o;Bb=jsx3.util.strEmpty(l)?Ba:l;Ca=jsx3.util.strEmpty(a)?Ba:a;tb=jsx3.util.strEmpty(f)?Bb:f;}return Ba+xb.O+Bb+xb.O+Ca+xb.O+tb;});ta=Ba+xb.O+Bb+xb.O+Ca+xb.O+tb;}}return ta;};var
nb=xb.Da;var
ub=xb.Ea;var
rb=xb.Fa;var
Lb=xb.Ga;var
Sa=xb.Ha;var
L=xb.Ia;var
ma=xb.Ja;var
Fb=xb.Ka;var
Ua=xb.La;var
Fa=xb.Ma;var
B=xb.Na;var
Pa=xb.Oa;e.U7=function(q){var
wa={top:{width:0,color:xb.x,style:xb.x},right:{width:0,color:xb.x,style:xb.x},bottom:{width:0,color:xb.x,style:xb.x},left:{width:0,color:xb.x,style:xb.x}};while(nb.exec(q))wa.top.width=RegExp.$1;while(ub.exec(q))wa.top.color=RegExp.$1;while(rb.exec(q))wa.top.style=RegExp.$1;while(Lb.exec(q))wa.right.width=RegExp.$1;while(Sa.exec(q))wa.right.color=RegExp.$1;while(L.exec(q))wa.right.style=RegExp.$1;while(ma.exec(q))wa.bottom.width=RegExp.$1;while(Fb.exec(q))wa.bottom.color=RegExp.$1;while(Ua.exec(q))wa.bottom.style=RegExp.$1;while(Fa.exec(q))wa.left.width=RegExp.$1;while(B.exec(q))wa.left.color=RegExp.$1;while(Pa.exec(q))wa.left.style=RegExp.$1;return wa.top.width+e.bX.pxc+wa.top.style+xb.O+wa.top.color+e.bX.semi+wa.right.width+e.bX.pxc+wa.right.style+xb.O+wa.right.color+e.bX.semi+wa.bottom.width+e.bX.pxc+wa.bottom.style+xb.O+wa.bottom.color+e.bX.semi+wa.left.width+e.bX.pxc+wa.left.style+xb.O+wa.left.color;};n.toString=function(){var
kb=xb.Pa;for(var X in this.implicit)kb=kb+(X+xb.Qa+this.implicit[X]+xb.Ra);kb=kb+xb.Sa;for(var X in this.Ts)kb=kb+(X+xb.Qa+this.Ts[X]+xb.Ra);return kb;};});jsx3.Class.defineClass("jsx3.app.Model.Loading",jsx3.gui.Painted,null,function(n,b){b.init=function(a,q,l){this._jsxIw=a;a._jsxbm=true;this._jsxZR=q;this._jsxwP=l;if(q==2||q==3)jsx3.sleep(function(){var
va=this.A9();if(q==2)this._z();else jsx3.sleep(function(){this._z();},null,this);},null,this);};b.A9=function(){var
ia=this.getParent();var
ca=ia.Ji.apply(this,[this._jsxIw].concat(this._jsxwP));ia.setChild(ca,1,null,this._jsxwP[1]);ia.insertBefore(ca,this,false);var
Ta=ia.getDocument();if(Ta){var
Ia=Ta.getElementById(this._jsxid);if(Ia)Ia.id=ca._jsxid;}ia.removeChild(this);this._jsxBE=ca;if(this._jsxZR==5)this._z();return ca;};b._z=function(){this._jsxBE.repaint();};b.paint=function(){return this.e1();};b.getRendered=function(){return null;};b.Em=function(h){if(h&&this._jsxZR==5)this.A9();this.jsxsuper(h);return this._jsxBE;};});jsx3.Class.defineClass("jsx3.gui.Heavyweight",null,null,function(a,s){var
ub={Qa:"/",_:"width",ea:"offsetHeight",q:';visibility:hidden;">',ra:" anchor-dir:",V:'"',v:"jsxdestroy",U:"');",u:"X",ka:"Apply ",z:"1px",d:"resize",La:"SE",D:');"',fa:"top",R:"jsx3.gui.Heavyweight.GO('",w:"yr",Ca:" point:",Q:'="',Na:"NW",Pa:" ",la:" Rules -- content-size:",qa:" rule is not perfect -- anchor-pos:",y:"hidden",x:"px",ja:"borderRightWidth",W:"0/0",Fa:"100px",Ha:"number",i:"width:",A:'<span class="jsx30scroller" style="position:absolute;top:0px;left:0px;width:',t:"Y",Ia:"N",s:"beforeEnd",X:"clientWidth",r:"</span>",sa:" inverse:",C:"px;background-image:url(",l:'<div class="jsx30scrollpane" style="top:0px;">',L:"SC",ia:"borderLeftWidth",Ka:"NE",J:'<span class="jsx30scroller" style="position:absolute;top:',Da:" left-top:",ta:" content-size:",oa:"E",O:"dm",k:"height:",Ga:"object",p:"left:0px;top:0px;z-index:",P:" on",I:">&#160;</span>",ha:"scrollTop",a:"jsx:///images/menu/scroll_up.gif",Aa:" anchor-pos:",F:"wp",Ra:"3.00.00",j:"px;",ua:" origin:",S:"').",Ma:"SW",K:"px;left:0px;width:",aa:"scrollLeft",B:"px;height:",g:"gui.hw.own",b:"jsx:///images/menu/scroll_down.gif",va:" max-size:",M:"visible",f:"gui.hw.doc",T:"(event,this,'",ga:"height",ba:"borderTopWidth",e:"body",za:" rule -- perfect:",h:"",c:"jsx_heavyweight_",H:"uT",wa:"Found perfect ",G:"mouseout",Ja:"W",ma:" explicit-size:",ca:"borderBottomWidth",pa:"S",N:"_jsxXY",o:'" class="jsx30block" style="position:absolute;overflow:;',Z:"left",xa:" rule -- anchor-pos:",na:" total-space:",Ba:" available-size:",da:"clientHeight",Oa:"O",m:"</div>",Y:"offsetWidth",Ea:" width-height:",n:'<span id="',ya:"Applying best ",E:"mouseover"};var
Lb=jsx3.util.Logger.getLogger(a.jsxclass.getName());var
pb=jsx3.gui.Event;a.Q4={};a.cN=1;a._R=jsx3.resolveURI(ub.a);a.DL=jsx3.resolveURI(ub.b);a.Vo=75;a.V6=12;a.AW=18;a.DEFAULTZINDEX=32000;a.ML=10;s.init=function(p,e){this.ST=p!=null?p:a.gV();this.Ev=e;if(a.Q4[this.ST]!=null)a.Q4[this.ST].destroy();a.Q4[this.ST]=this;};a.gV=function(){return ub.c+a.cN++;};a.sS=function(){var
N=a.Q4;var
ya=false;for(var ca in N){N[ca].hide();if(!ya&&N[ca].Ev){N[ca].Ev.focus();ya=true;}}};pb.subscribe(ub.d,a.sS);s.wt=function(){if(this.Ev){var
ca=this.Ev.getDocument();if(ca)return ca.getElementsByTagName(ub.e)[0];else Lb.warn(jsx3._msg(ub.f,this));}else Lb.warn(jsx3._msg(ub.g,this));return null;};a.GO=function(j){return a.Q4[j];};s.show=function(e){var
Db=this.getId();var
va=this.getHTML();if(jsx3.util.strEmpty(va))return;var
D=this.getWidth();var
Ua=D==null?ub.h:ub.i+D+ub.j;var
Aa=this.getHeight();var
N=Aa==null?ub.h:ub.k+Aa+ub.j;if(this.OV)va=ub.l+va+ub.m;var
ob=ub.n+Db+ub.o+Ua+N+ub.p+this.getZIndex()+ub.q+va+ub.r;var
lb=this.getDomParent();jsx3.html.insertAdjacentHTML(lb,ub.s,ob);var
cb=this.getRendered();this.applyRatio();var
Qa=this.c9(cb);var
eb=Qa.offsetWidth,Mb=Qa.offsetHeight;this.applyRules(ub.t,Mb);this.applyRules(ub.u,eb);if(e!=false)this.setVisibility(ub.h);if(this.Ev)this.Ev.subscribe(ub.v,this,ub.w);if(this.OV){var
w=cb.childNodes[0];w.style.width=cb.offsetWidth+ub.x;w.style.height=cb.offsetHeight+ub.x;cb.style.overflow=ub.y;this.cr(cb);}};s.cr=function(l){var
Jb=l.childNodes[0];var
ya=this.c9(l);var
Na=l.childNodes[1];var
y=l.childNodes[2];var
t=Jb.clientWidth,Aa=Jb.clientHeight;var
Va=new
jsx3.gui.Painted.Box({width:t,height:a.AW,border:ub.z});Va.calculate();var
Za=Va.cm();var
x=Va.Zm();if(ya.offsetHeight>Aa){if(!Na)jsx3.html.insertAdjacentHTML(l,ub.s,ub.A+Za+ub.B+x+ub.C+a._R+ub.D+this.xI(ub.E,ub.F)+this.xI(ub.G,ub.H)+ub.I);if(!y)jsx3.html.insertAdjacentHTML(l,ub.s,ub.J+(Aa-a.AW)+ub.K+Za+ub.B+x+ub.C+a.DL+ub.D+this.xI(ub.E,ub.L)+this.xI(ub.G,ub.H)+ub.I);this.wY(l);}else{if(Na)jsx3.html.removeNode(Na);if(y)jsx3.html.removeNode(y);}};s.wY=function(b){var
E=b.childNodes[1];var
db=b.childNodes[2];var
sa=b.offsetHeight,lb=b.childNodes[0].childNodes[0].offsetHeight,Ia=parseInt(b.childNodes[0].style.top);E.style.visibility=Ia>=0?ub.y:ub.M;db.style.visibility=sa>=lb+Ia?ub.y:ub.M;};s.wp=function(h,e){this.p9(h,e,true);};s.SC=function(k,b){this.p9(k,b,false);};s.uT=function(k,b){window.clearInterval(this._jsxXY);delete this[ub.N];};s.p9=function(n,r,g){if(this._jsxXY==null){var
vb=this;this._jsxXY=window.setInterval(function(){vb.GN(g);},a.Vo);vb.GN(g);}};s.GN=function(r){var
ma=this.getRendered();if(ma){var
eb=ma.childNodes[0];var
ka=ma.offsetHeight,kb=eb.childNodes[0].offsetHeight,Hb=parseInt(eb.style.top);var
yb=Math.min(0,Math.max(Hb+(r?1:-1)*a.V6,ka-kb));eb.style.top=yb+ub.x;this.wY(ma);}else this.uT();};s.scrollTo=function(i){var
Q=this.getRendered();if(!Q||!Q.childNodes[1])return;Q.scrollTop=0;var
ob=Q.childNodes[0];var
Fa=Q.offsetHeight,G=ob.childNodes[0].offsetHeight,A=-1*parseInt(ob.style.top),xb=i.offsetTop,va=i.offsetHeight;var
ta=null;if(xb<A+a.AW){ta=xb-a.AW;}else if(xb>Fa+A-a.AW-va)ta=xb-Fa+a.AW+va;if(ta!=null){ob.style.top=Math.min(0,Math.max(-1*ta,Fa-G))+ub.x;this.wY(Q);}};s.xI=function(g,b){var
Ua=ub.O;return ub.P+g+ub.Q+ub.R+this.getId()+ub.S+Ua+ub.T+b+ub.U+ub.V;};s.dm=function(r,i,e){this[e].call(this,pb.wrap(r),i);};s.applyRatio=function(){var
na=this.getRatio();if(na){var
V=this.getRendered();if(V&&V.style){var
ga=null,Fb=null;ga=V.offsetHeight;Fb=V.offsetWidth;Fb=parseInt(na*(Fb+ga));V.style.width=Fb+ub.x;V.style.height=ga+ub.x;}}};s.c9=function(g){return jsx3.html.selectSingleElm(g,this.OV?ub.W:0);};s.applyRules=function(b,j){var
yb=this.getRendered();var
jb=this.getDomParent();if(yb==null||jb==null)return;var
_,Ma,pa,kb,Fb,X,fa,Ab;if(b==ub.u){_=ub.X;pa=ub.Y;Ma=ub.Z;kb=ub._;Fb=ub.aa;X=this.getWidth();fa=ub.ba;Ab=ub.ca;}else{_=ub.da;pa=ub.ea;Ma=ub.fa;kb=ub.ga;Fb=ub.ha;X=this.getHeight();fa=ub.ia;Ab=ub.ja;}var
Ia=this.getPositionRules()[b].length;var
Mb=jb[_];var
sb=0;var
L=null;Lb.trace(ub.ka+b+ub.la+j+ub.ma+X+ub.na+Mb);for(var
ib=0;ib<Ia&&!L;ib++){var
fb=this.getPositionRule(ib,b);var
Pa=fb.xZ==ub.oa||fb.xZ==ub.pa;if(fb.qF==null){sb=Mb-j;}else{var
Oa=this.getPoint(yb,fb.xZ);sb=Pa?fb.qF-Oa[b]-j:fb.qF-Oa[b];}if(Pa){fb.Cv=Math.max(a.ML+jb[Fb],sb);fb.aC=fb.qF-a.ML;}else{fb.Cv=Math.max(0,sb);fb.aC=Math.min(Mb-a.ML-fb.qF,Mb-2*a.ML);}if(j>fb.aC){Lb.trace(b+ub.qa+fb.qF+ub.ra+fb.xZ+ub.sa+Pa+ub.ta+j+ub.ua+fb.Cv+ub.na+Mb+ub.va+fb.aC);}else{L=fb;Lb.trace(ub.wa+b+ub.xa+fb.qF+ub.ra+fb.xZ);}}var
Ib=L!=null;if(!L)for(var
ib=0;ib<Ia;ib++){var
fb=this.getPositionRule(ib,b);if(!L||L.aC<fb.aC)L=fb;}if(L){var
Wa=L.Cv;var
ba=Math.min(j,L.aC);Lb.trace(ub.ya+b+ub.za+Ib+ub.Aa+L.qF+ub.Ba+L.aC+ub.Ca+L.xZ+ub.ma+X+ub.Da+Wa+ub.Ea+ba);yb.style[Ma]=Wa+ub.x;if(X==null){yb.style[kb]=ba+ub.x;if(!Ib){var
tb=yb.childNodes[0];tb.style[kb]=ub.Fa;var
za=tb[pa]-100;tb.style[kb]=Math.max(0,ba-za)+ub.x;}}}};s.hide=function(){var
ra=this.getRendered();if(ra)jsx3.html.removeNode(ra);if(this.Ev)this.Ev.unsubscribe(ub.v,this);};s.yr=function(r){var
ja=r.context.objPARENT;var
Jb=ja.getDocument().getElementById(this.getId());if(Jb)jsx3.html.removeNode(Jb);this.Ev.unsubscribe(ub.v,this);this.Ev=null;delete a.Q4[this.getId()];this.ST=null;};s.destroy=function(){if(!a.Q4[this.ST])return;this.hide();this.Ev=null;delete a.Q4[this.ST];this.ST=null;};s.getRendered=function(m){var
Ua=this.getId();if(Ua==null)return null;var
rb=null;if(m instanceof pb){if(m.srcElement())rb=m.srcElement().ownerDocument;}else if(m!=null)rb=m.getElementById?m:m.ownerDocument;if(!rb&&this.Ev)rb=this.Ev.getDocument();if(rb)return rb.getElementById(Ua);else if(this.Ev!=null&&this.Ev.getServer()!=null)Lb.warn(jsx3._msg(ub.f,this));return null;};s.containsHtmlElement=function(c){var
Fb=this.getRendered(c);if(Fb)while(c!=null){if(Fb==c)return true;c=c.parentNode;}return false;};s.getId=function(){return this.ST;};s._setId=function(h){this.ST=h;};s.getHTML=function(){return this.html;};s.setHTML=function(h,o){this.html=h;if(o){var
Hb=this.getRendered();if(Hb!=null)Hb.innerHTML=h;}return this;};s.getDomParent=function(){return this.a2==null?this.wt():this.a2;};s.setDomParent=function(n){this.a2=n;return this;};s.getRatio=function(){return this.l2;};s.setRatio=function(c){this.l2=c;return this;};s.getOverflow=function(){return this.up==null?2:this.up;};s.setOverflow=function(m){this.up=m;return this;};s.setScrolling=function(g){this.OV=g;};s.setVisibility=function(g){var
O=this.getRendered();if(O)O.style.visibility=g;return this;};s.getZIndex=function(){return this.qr!=null?this.qr:a.DEFAULTZINDEX;};s.setZIndex=function(h){this.qr=h;return this;};s.getWidth=function(){return this.qz==null?null:this.qz;};s.setWidth=function(k){this.qz=k;return this;};s.getHeight=function(){return this.rz==null?null:this.rz;};s.setHeight=function(k){this.rz=k;return this;};s.addXRule=function(f,o,n,q){var
Ka=f instanceof jsx3.gui.Event?f.clientX():this.getPoint(f,o).X;this.addRule(Ka,n,q,ub.u);return this;};s.addYRule=function(k,j,b,l){var
Na=k instanceof jsx3.gui.Event?k.clientY():this.getPoint(k,j).Y;this.addRule(Na,b,l,ub.t);return this;};s.addRule=function(d,g,e,r){var
P=this.getPositionRules();var
Ya=P[r];Ya[Ya.length]={qF:d==null?a.ML:d+e,xZ:g};return this;};s.getPositionRule=function(c,m){return this.getPositionRules()[m][c];};s.getPositionRules=function(){if(typeof this.zz!=ub.Ga){this.zz={};this.zz.X=[];this.zz.Y=[];}return this.zz;};s.getPoint=function(e,p){if(typeof e==ub.Ha)return {X:e,Y:e};var
Oa=null;if(e instanceof jsx3.gui.Block)Oa=e.getAbsolutePosition(this.getDomParent());else Oa=jsx3.html.getRelativePosition(this.getDomParent(),e);switch(p){case ub.Ia:return {X:Oa.L+Math.floor(Oa.W/2),Y:Oa.T};case ub.pa:return {X:Oa.L+Math.floor(Oa.W/2),Y:Oa.T+Oa.H};case ub.oa:return {X:Oa.L+Oa.W,Y:Oa.T+Math.floor(Oa.H/2)};case ub.Ja:return {X:Oa.L,Y:Oa.T+Math.floor(Oa.H/2)};case ub.Ka:return {X:Oa.L+Oa.W,Y:Oa.T};case ub.La:return {X:Oa.L+Oa.W,Y:Oa.T+Oa.H};case ub.Ma:return {X:Oa.L,Y:Oa.T+Oa.H};case ub.Na:return {X:Oa.L,Y:Oa.T};case ub.Oa:return {X:Oa.L+Math.floor(Oa.W/2),Y:Oa.T+Math.floor(Oa.H/2)};}};s.toString=function(){return this.jsxsuper()+ub.Pa+this.getId()+ub.Qa+this.Ev;};a.getVersion=function(){return ub.Ra;};});jsx3.Heavyweight=jsx3.gui.Heavyweight;jsx3.require("jsx3.gui.HotKey","jsx3.gui.Heavyweight");jsx3.Class.defineInterface("jsx3.gui.Interactive",null,function(j,i){var
ub={Qa:/;\s*$/,_:"dblclick",ea:"mousedown",q:"jsxafterappend",ra:"kf",V:/\S/,v:"jsxafterresize",rb:"EVENTSVERS",U:"jsxtoggle",u:"jsxafterreorder",ka:"cn",z:"jsxbeforedrop",d:"jsxdblclick",La:"');",D:"jsxbeforeselect",fa:"mousemove",qb:"3.00.00",R:"jsxselect",w:"jsxafterresizeview",Ca:"&quot;",kb:"jsxspystylevalues",Q:"jsxscroll",Na:'="',Pa:'"',la:"zc",qa:"kc",Wa:"JSXDragId",y:"jsxbeforeappend",lb:/ *; */,x:"jsxaftersort",Za:'<span onmouseout="if (event.toElement != this.childNodes[0]) jsx3.gui.Interactive.hideSpy();" class="jsx30spyglassbuffer"><div class="jsx30spyglass">',ja:"mousewheel",W:"function",Fa:"jsx3.",Ha:"',",i:"jsxload",Sa:"id",A:"jsxbeforeedit",t:"jsxaftermove",Ia:");",s:"jsxafteredit",X:"blur",tb:"Wx",r:"jsxaftercommit",sa:"Dc",C:"jsxbeforeresize",Ua:"gui.int.eb",l:"jsxmouseover",jb:"jsxspystylekeys",L:"jsxexecute",ia:"mouseup",Ka:"').",sb:"onDestroy",_a:"</div></span>",eb:"N",J:"jsxdrag",Da:";",ta:"_ebMouseMove",oa:"Vl",O:"jsxinput",k:"jsxmouseout",gb:"Y",Ga:"(event,this,'",p:"jsxadopt",P:"jsxmenu",I:"jsxdata",ha:"mouseover",a:"jsxblur",Aa:"dl",F:"jsxcanceldrop",Ra:"gui.int.br",hb:"_jsxIu",j:"jsxmousedown",ua:"Qi",ib:"_jsxsP",S:"jsxshow",Ma:" ",K:"jsxdrop",bb:"W",aa:"focus",B:"jsxbeforemove",g:"jsxkeypress",b:"jsxchange",va:"el",M:"jsxhide",ab:"_jsxspy",f:"jsxkeydown",T:"jsxspy",ga:"mouseout",ba:"keydown",mb:/(-\S)/gi,e:"jsxfocus",pb:"vntCallback",za:"dm",h:"jsxkeyup",c:"jsxclick",H:"jsxdestroy",fb:"X",wa:"Df",ob:"jsxmodal",Ya:"jsx30spyglassbuffer",G:"jsxctrldrop",Ja:"jsx3.GO('",ma:"Tg",ca:"keypress",pa:"oe",N:"jsxincchange",o:"text-decoration:underline",Z:"click",xa:"lk",Ta:"body",na:"Oh",Ba:/\"/g,da:"keyup",nb:":",Oa:"",m:"jsxmouseup",db:"S",Y:"change",Ea:"string",n:"jsxmousewheel",Xa:"JSXDragType",Va:"absolute",ya:"on",cb:"E",E:"jsxbeforesort"};var
ta=jsx3.gui.Event;j.JSXBLUR=ub.a;j.JSXCHANGE=ub.b;j.JSXCLICK=ub.c;j.JSXDOUBLECLICK=ub.d;j.JSXFOCUS=ub.e;j.JSXKEYDOWN=ub.f;j.JSXKEYPRESS=ub.g;j.JSXKEYUP=ub.h;j.JSXLOAD=ub.i;j.JSXMOUSEDOWN=ub.j;j.JSXMOUSEOUT=ub.k;j.JSXMOUSEOVER=ub.l;j.JSXMOUSEUP=ub.m;j.JSXMOUSEWHEEL=ub.n;j.FOCUS_STYLE=ub.o;j.ADOPT=ub.p;j.AFTER_APPEND=ub.q;j.AFTER_COMMIT=ub.r;j.AFTER_EDIT=ub.s;j.AFTER_MOVE=ub.t;j.AFTER_REORDER=ub.u;j.AFTER_RESIZE=ub.v;j.AFTER_RESIZE_VIEW=ub.w;j.AFTER_SORT=ub.x;j.BEFORE_APPEND=ub.y;j.BEFORE_DROP=ub.z;j.BEFORE_EDIT=ub.A;j.BEFORE_MOVE=ub.B;j.BEFORE_RESIZE=ub.C;j.BEFORE_SELECT=ub.D;j.BEFORE_SORT=ub.E;j.CANCEL_DROP=ub.F;j.CHANGE=ub.b;j.CTRL_DROP=ub.G;j.DESTROY=ub.H;j.DATA=ub.I;j.DRAG=ub.J;j.DROP=ub.K;j.EXECUTE=ub.L;j.HIDE=ub.M;j.INCR_CHANGE=ub.N;j.INPUT=ub.O;j.MENU=ub.P;j.SCROLL=ub.Q;j.SELECT=ub.R;j.SHOW=ub.S;j.SPYGLASS=ub.T;j.TOGGLE=ub.U;i.cn=function(m,s){this.doEvent(ub.a,{objEVENT:m});};i.zc=function(q,o){this.doEvent(ub.b,{objEVENT:q});};i.Tg=function(a,l){this.doEvent(ub.c,{objEVENT:a});};i.Oh=function(n,r){this.doEvent(ub.d,{objEVENT:n});};i.Vl=function(s,m){this.doEvent(ub.e,{objEVENT:s});};i.oe=function(f,c){var
pa=false;if(this.hasHotKey())pa=this.checkHotKeys(f);if(!pa)this.doEvent(ub.f,{objEVENT:f});return pa;};i.kc=function(s,m){this.doEvent(ub.g,{objEVENT:s});};i.kf=function(l,a){this.doEvent(ub.h,{objEVENT:l});};i.Dc=function(r,n){this.doEvent(ub.j,{objEVENT:r});};i.Qi=function(n,r){this.doEvent(ub.k,{objEVENT:n});};i.el=function(m,s){this.doEvent(ub.l,{objEVENT:m});};i.Df=function(q,o){var
Ga=null;this.doEvent(ub.m,{objEVENT:q});if(q.rightButton()&&(Ga=this.getMenu())!=null){var
Ta=this.getServer().getJSX(Ga);if(Ta!=null){var
Q=this.doEvent(ub.P,{objEVENT:q,objMENU:Ta});if(Q!==false){if(Q instanceof Object&&Q.objMENU instanceof jsx3.gui.Menu)Ta=Q.objMENU;Ta.showContextMenu(q,this);}}}};i.lk=function(l,a){this.doEvent(ub.n,{objEVENT:l});};i.setEvent=function(d,a){this.getEvents()[a]=d;return this;};i.getEvents=function(){if(this._jsxHj==null)this._jsxHj={};return this._jsxHj;};i.getEvent=function(p){if(this._jsxHj)return this._jsxHj[p];};i.hasEvent=function(b){return this._jsxHj!=null&&this._jsxHj[b]!=null&&this._jsxHj[b].match(ub.V);};i.doEvent=function(r,e){var
Wa=this.getEvent(r);if(typeof this.publish==ub.W)this.publish({subject:r,context:e});return this.eval(Wa,e);};i.removeEvent=function(d){if(this._jsxHj!=null)delete this._jsxHj[d];return this;};i.removeEvents=function(){this._jsxHj={};return this;};i.setCanMove=function(b){this.jsxmove=b;return this;};i.getCanMove=function(){return this.jsxmove||0;};i.setCanDrag=function(l){this.jsxdrag=l;return this;};i.getCanDrag=function(){return this.jsxdrag||0;};i.setCanDrop=function(p){this.jsxdrop=p;return this;};i.getCanDrop=function(){return this.jsxdrop||0;};i.setCanSpy=function(r){this.jsxspy=r;return this;};i.getCanSpy=function(){return this.jsxspy||0;};i.getMenu=function(){return this.jsxmenu;};i.setMenu=function(c){this.jsxmenu=c;return this;};j.Tj=[ub.X,ub.Y,ub.Z,ub._,ub.aa,ub.ba,ub.ca,ub.da,ub.ea,ub.fa,ub.ga,ub.ha,ub.ia,ub.ja];j.Sm={};j.Sm[ub.X]=ub.ka;j.Sm[ub.Y]=ub.la;j.Sm[ub.Z]=ub.ma;j.Sm[ub._]=ub.na;j.Sm[ub.aa]=ub.oa;j.Sm[ub.ba]=ub.pa;j.Sm[ub.ca]=ub.qa;j.Sm[ub.da]=ub.ra;j.Sm[ub.ea]=ub.sa;j.Sm[ub.fa]=ub.ta;j.Sm[ub.ga]=ub.ua;j.Sm[ub.ha]=ub.va;j.Sm[ub.ia]=ub.wa;j.Sm[ub.ja]=ub.xa;j.isBridgeEventHandler=function(p){if(j.wC==null){j.wC={};for(var
bb=0;bb<j.Tj.length;bb++)j.wC[ub.ya+j.Tj[bb]]=true;}return j.wC[p];};j.mw=ub.za;j.oR=ub.Aa;i.jh=function(b,g){var
db={};if((b==null||!b[ub.ba])&&(this.hasHotKey()||this.getAlwaysCheckHotKeys()))db[ub.ba]=true;if((b==null||!b[ub.ia])&&this.getMenu())db[ub.ia]=true;var
Da=[];var
La=this.instanceOf(jsx3.gui.Painted);var
fa=this.getId();for(var
ma=0;ma<j.Tj.length;ma++){var
Ab=j.Tj[ma];var
nb=ub.ya+Ab;var
Qa=[];var
sb=La?this.getAttribute(nb):null;if(sb)Qa[Qa.length]=sb.replace(ub.Ba,ub.Ca)+ub.Da;var
Xa=b&&b[Ab]||db[Ab];if(Xa){if(typeof Xa!=ub.Ea)Xa=j.Sm[Ab];if(g!=null)Qa[Qa.length]=ub.Fa+j.oR+ub.Ga+Xa+ub.Ha+g+ub.Ia;else Qa[Qa.length]=ub.Ja+fa+ub.Ka+j.mw+ub.Ga+Xa+ub.La;}if(Qa.length>0)Da[Da.length]=ub.Ma+nb+ub.Na+Qa.join(ub.Oa)+ub.Pa;}return Da.join(ub.Oa);};i.Gj=function(d,q,r){var
gb=ub.ya+d;var
_a=ub.Oa;var
Hb=false;if(Hb){var
za=this.getAttribute(gb);if(za){_a=_a+za;if(!za.match(ub.Qa))_a=_a+ub.Da;}}var
pb=r!=null?ub.Fa+j.oR+ub.Ga+q+ub.Ha+r+ub.Ia:ub.Ja+this.getId()+ub.Ka+j.mw+ub.Ga+q+ub.La;return ub.Ma+gb+ub.Na+_a+pb+ub.Pa;};i.dm=function(b,l,h){var
aa=this[h];var
gb=jsx3.gui.Event.wrap(b);if(aa){aa.call(this,gb,l);}else throw new
jsx3.Exception(jsx3._msg(ub.Ra,h,gb.getType(),this));};jsx3.dl=function(a,l,c,m){var
Da=l;m=m||Number(0);for(var
ca=0;ca<m;ca++)Da=Da.parentNode;var
A=Da.getAttribute(ub.Sa);var
qb=jsx3.GO(A);if(qb!=null)qb.dm(a,l,c);else if(jsx3.html.getElmUpByTagName(l,ub.Ta)!=null)throw new
jsx3.Exception(jsx3._msg(ub.Ua,A,m,l));};j.Un=function(b,k,l,q){var
y=k.ownerDocument;jsx3.gui.Event.preventSelection(y);var
z=b.getTrueX();var
V=k.offsetLeft;jsx3.EventHelp.constrainY=q;jsx3.EventHelp.xOff=V-z;var
Z=b.getTrueY();var
mb=k.offsetTop;jsx3.EventHelp.constrainX=l;jsx3.EventHelp.yOff=mb-Z;jsx3.EventHelp.dH=k;jsx3.EventHelp.FLAG=1;jsx3.EventHelp.beginTrackMouse(b);b.setCapture(k);b.cancelReturn();b.cancelBubble();};j.Bn=function(b,k,f){var
xa=k.ownerDocument;jsx3.gui.Event.preventSelection(xa);jsx3.EventHelp.startX=b.getTrueX();jsx3.EventHelp.startY=b.getTrueY();jsx3.EventHelp.xOff=k.offsetLeft;jsx3.EventHelp.yOff=k.offsetTop;jsx3.EventHelp.dragRounder=f;jsx3.EventHelp.dH=k;jsx3.EventHelp.FLAG=3;jsx3.EventHelp.beginTrackMouse(b);b.setCapture(k);b.cancelReturn();b.cancelBubble();};i.doBeginMove=function(s,d){if(!s.leftButton())return;if(d==null)d=this.getRendered();var
Jb=d.ownerDocument;var
Ba=this.doEvent(ub.B,{objEVENT:s});var
kb=Ba===false;if(d!=null&&!kb){d.style.zIndex=this.getServer().getNextZIndex(jsx3.app.Server.Z_DRAG);jsx3.gui.Event.preventSelection(Jb);var
D=s.getTrueX();var
W=d.style.position==ub.Va?parseInt(d.style.left)||0:d.scrollLeft;if(Ba&&Ba.bCONSTRAINY)jsx3.EventHelp.constrainY=true;jsx3.EventHelp.xOff=W-D;var
Db=s.getTrueY();var
V=d.style.position==ub.Va?parseInt(d.style.top)||0:d.scrollTop;if(Ba&&Ba.bCONSTRAINX)jsx3.EventHelp.constrainX=true;jsx3.EventHelp.yOff=V-Db;jsx3.EventHelp.dH=d;jsx3.EventHelp.FLAG=1;jsx3.EventHelp.beginTrackMouse(s);s.setCapture(d);}};i.doEndMove=function(k,b){if(b==null)b=this.getRendered();if(b!=null){b.style.zIndex=this.getZIndex();k.releaseCapture(b);var
la=parseInt(b.style.left);var
ra=parseInt(b.style.top);this.setLeft(la);this.setTop(ra);this.doEvent(ub.t,{objEVENT:k});}};i.doDrag=function(b,k,q,o){b.cancelAll();if(k==null){k=b.srcElement();while(k!=null&&k.getAttribute(ub.Wa)==null){k=k.parentNode;if(k=k.ownerDocument.getElementsByTagName(ub.Ta)[0])k=null;}if(k==null)return;}var
sb=k.getAttribute(ub.Wa);var
Na=k.getAttribute(ub.Xa);if(o==null)o={};o.strDRAGID=k.getAttribute(ub.Wa);o.strDRAGTYPE=k.getAttribute(ub.Xa);o.objGUI=k;o.objEVENT=b;if(this.doEvent(ub.J,o)===false)return;jsx3.EventHelp.DRAGTYPE=o.strDRAGTYPE;jsx3.EventHelp.DRAGID=o.strDRAGID;if(o.strDRAGIDS instanceof Array)jsx3.EventHelp.DRAGIDS=o.strDRAGIDS;jsx3.EventHelp.JSXID=this;if(q==null)q=jsx3.EventHelp.drag;var
I=q(k,this,jsx3.EventHelp.DRAGTYPE,jsx3.EventHelp.DRAGID);if(I==null){return false;}else{jsx3.EventHelp.dragItemHTML=I;jsx3.EventHelp.FLAG=2;jsx3.EventHelp.beginTrackMouse(b);}jsx3.EventHelp.constrainX=false;jsx3.EventHelp.constrainY=false;};i.doDrop=function(m,s,k){if(jsx3.EventHelp.DRAGID!=null){var
X=jsx3.EventHelp.JSXID;var
ga=jsx3.EventHelp.DRAGID;var
va=jsx3.EventHelp.DRAGTYPE;var
xa={objEVENT:m,objSOURCE:X,strDRAGID:ga,strDRAGTYPE:va};if(k==jsx3.EventHelp.ONDROP&&jsx3.gui.isMouseEventModKey(m)){xa.objGUI=m.srcElement();this.doEvent(ub.G,xa);jsx3.EventHelp.reset();}else if(k==jsx3.EventHelp.ONDROP){xa.objGUI=m.srcElement();this.doEvent(ub.K,xa);jsx3.EventHelp.reset();}else if(k==jsx3.EventHelp.ONBEFOREDROP){xa.objGUI=m.toElement();this.doEvent(ub.z,xa);}else if(k==jsx3.EventHelp.ONCANCELDROP){xa.objGUI=m.fromElement();this.doEvent(ub.F,xa);}}};i.doSpyOver=function(f,g,k){var
za=f.getTrueX();var
Fa=f.getTrueY();if(this._jsxspytimeout)return;if(k==null)k={};f.sf();k.objEVENT=f;var
Za=this;this._jsxspytimeout=window.setTimeout(function(){if(Za.getParent()==null)return;Za._jsxspytimeout=null;var
L=Za.doEvent(ub.T,k);if(L)Za.showSpy(L,f);},jsx3.EventHelp.SPYDELAY);};i.doSpyOut=function(q,o){if(q.toElement()!=null&&q.toElement().className==ub.Ya)return;window.clearTimeout(this._jsxspytimeout);this._jsxspytimeout=null;j.hideSpy();};i.showSpy=function(a,l,h){if(a!=null){j.hideSpy();a=ub.Za+a+ub._a;var
tb=new
jsx3.gui.Heavyweight(ub.ab,this);tb.setHTML(a);if(l instanceof ta){tb.addXRule(l,ub.bb,ub.bb,12);tb.addXRule(l,ub.cb,ub.cb,-12);tb.addYRule(l,ub.db,ub.eb,6);tb.addYRule(l,ub.eb,ub.db,-6);}else{tb.addRule(l,ub.bb,-2,ub.fb);tb.addRule(l,ub.cb,12,ub.fb);tb.addRule(null,ub.bb,-24,ub.fb);tb.addRule(h,ub.eb,-2,ub.gb);tb.addRule(h,ub.db,-6,ub.gb);tb.setOverflow(3);}tb.show();var
Ga=tb.getRendered();if(Ga){var
N=Ga.ownerDocument.getElementsByTagName(ub.Ta)[0];if(parseInt(Ga.style.width)+parseInt(Ga.style.left)>N.offsetWidth)tb.applyRules(ub.fb);}ta.subscribe(ub.ea,jsx3.gui.Interactive.hideSpy);}};j.hideSpy=function(){if(jsx3.gui.Heavyweight){var
Wa=jsx3.gui.Heavyweight.GO(ub.ab);if(Wa){Wa.destroy();ta.unsubscribe(ub.ea,jsx3.gui.Interactive.hideSpy);}}};i.getSpyStyles=function(b){return this.jsxspystyle?this.jsxspystyle:b?b:null;};i.setSpyStyles=function(l){delete this[ub.hb];delete this[ub.ib];delete this[ub.jb];delete this[ub.kb];this.jsxspystyle=l;};i.Du=function(){var
A={};if(jsx3.util.strEmpty(this.getSpyStyles())&&this.jsxspystylekeys!=null){var
C=(this.jsxspystylekeys||ub.Oa).split(ub.lb);var
O=(this.jsxspystylevalues||ub.Oa).split(ub.lb);for(var
G=0;G<C.length;G++)A[C[G]]=O[G];}else{var
nb=this.getSpyStyles(ub.o);var
La=ub.mb;var
A={};var
Za=nb.split(ub.Da);for(var
G=0;G<Za.length;G++){var
ma=Za[G]+ub.Oa;var
gb=ma.split(ub.nb);if(gb&&gb.length==2){var
rb=gb[0].replace(La,function(l,k){return k.substring(1).toUpperCase();});A[rb]=gb[1];}}}return A;};i.applySpyStyle=function(g){if(this._jsxIu==null)this._jsxIu=this.Du();if(this._jsxsP==null){this._jsxsP={};for(var tb in this._jsxIu)this._jsxsP[tb]=g.style[tb];}for(var tb in this._jsxIu)g.style[tb]=this._jsxIu[tb];};i.removeSpyStyle=function(r){for(var t in this._jsxsP)r.style[t]=this._jsxsP[t];};i.checkHotKeys=function(r){if(this._jsxVY==null)return false;if(r.isModifierKey())return false;var
Fb=false;var
Wa=r.getAttribute(ub.ob);for(var Ya in this._jsxVY){var
Ca=this._jsxVY[Ya];if(Ca instanceof jsx3.gui.HotKey){if(Ca.isDestroyed()){delete this._jsxVY[Ya];continue;}else if(!Ca.isEnabled())continue;if(Ca.isMatch(r)){var
jb=true;if(!Wa)jb=Ca.invoke(this,[r]);if(jb!==false)Fb=true;}}}if(Fb)r.cancelAll();return Fb;};i.registerHotKey=function(s,o,n,d,c){var
w;if(s instanceof jsx3.gui.HotKey){w=s;}else{var
qb=typeof s==ub.W?s:this[s];if(!(typeof qb==ub.W))throw new
jsx3.IllegalArgumentException(ub.pb,s);w=new
jsx3.gui.HotKey(qb,o,n,d,c);}if(this._jsxVY==null)this._jsxVY={length:0};var
Lb=w.getKey();this._jsxVY.length+=(this._jsxVY[Lb]?0:1);this._jsxVY[Lb]=w;return w;};i.hasHotKey=function(){return this._jsxVY!=null&&this._jsxVY.length>0;};i.setAlwaysCheckHotKeys=function(e){this.jsxalwayscheckhk=e;return this;};i.getAlwaysCheckHotKeys=function(){return this.jsxalwayscheckhk;};i.clearHotKeys=function(){this._jsxVY=null;};j.getVersion=function(){return ub.qb;};i.isOldEventProtocol=function(){var
La=this.getServer();return La&&La.getEnv(ub.rb)<3.1;};i.Wx=function(k){this.doEvent(ub.H,{objPARENT:k});};jsx3.app.Model.jsxclass.addMethodMixin(ub.sb,j.jsxclass,ub.tb);});jsx3.Event=jsx3.gui.Interactive;jsx3.Class.defineClass("jsx3.EventHelp",null,null,function(k,p){var
ub={o:' style="position:absolute;left:',d:/<[^>]*>/gi,k:"dragRounder",r:';">',c:"",h:"<span class='jsx30block_drag'>",p:"px;top:",g:"... ... ...",l:"_jsxdrag",q:"px;min-width:10px;z-index:",b:"mouseup",i:"</span>",m:"body",a:"mousemove",f:"...",t:"beforeEnd",j:"px",n:'<div id="_jsxdrag"',s:"</div>",e:" "};k.ONBEFOREDROP=0;k.ONDROP=1;k.ONCANCELDROP=2;k.DRAGICONINDEX=32000;k.DEFAULTSPYLEFTOFFSET=5;k.DEFAULTSPYTOPOFFSET=5;k.SPYDELAY=300;k.FLAG=0;k.yOff=0;k.xOff=0;k.dH=null;k.beginTrackMouse=function(f){jsx3.gui.Event.subscribe(ub.a,k.mouseTracker);jsx3.gui.Event.subscribe(ub.b,k.mouseUpTracker);};k.endTrackMouse=function(){jsx3.gui.Event.unsubscribe(ub.a,k.mouseTracker);jsx3.gui.Event.unsubscribe(ub.b,k.mouseUpTracker);};k.mouseTracker=function(l){k.doMouseMove(l.event);};k.mouseUpTracker=function(a){k.reset();};k.drag=function(b,e,f,j){var
N=b&&b.innerHTML?jsx3.util.strTruncate((b.innerHTML+ub.c).replace(ub.d,ub.e),25,ub.f,0.5):ub.g;return ub.h+N+ub.i;};k.doMouseMove=function(i){if(k.FLAG==1||k.FLAG==3){var
_a=k.dH.ownerDocument;if(k.FLAG==1){if(!k.constrainX)k.dH.style.left=i.getTrueX()+k.xOff+ub.j;if(!k.constrainY)k.dH.style.top=i.getTrueY()+k.yOff+ub.j;}else{var
y=i.getTrueX()-k.startX;var
L=i.getTrueY()-k.startY;var
M=k[ub.k](k.xOff+y,k.yOff+L,i);if(M[0]!=k.offsetLeft||M[1]!=k.offsetTop){if(!isNaN(M[0]))k.dH.style.left=M[0]+ub.j;if(!isNaN(M[1]))k.dH.style.top=M[1]+ub.j;}}}else if(k.FLAG==2){var
_a=k.JSXID.getDocument();var
Mb=_a.getElementById(ub.l);if(Mb)jsx3.html.removeNode(Mb);var
Oa=_a.getElementsByTagName(ub.m)[0];k.xOff=10;k.yOff=10;jsx3.gui.Event.preventSelection(_a);var
Nb=ub.n+jsx3.html.Kf+ub.o+(k.constrainX?parseInt(k.dH.style.left):i.getTrueX()+k.xOff)+ub.p+(k.constrainY?parseInt(k.dH.style.top):i.getTrueY()+k.yOff)+ub.q+k.DRAGICONINDEX+ub.r+k.dragItemHTML+ub.s;jsx3.html.insertAdjacentHTML(Oa,ub.t,Nb);k.dH=_a.getElementById(ub.l);k.FLAG=1;}else k.endTrackMouse();};k.reset=function(){k.DRAGTYPE=null;k.DRAGID=null;k.DRAGIDS=null;k.FLAG=0;k.endTrackMouse();if(k.dH){if(k.dH.id==ub.l)jsx3.html.removeNode(k.dH);if(jsx3.CLASS_LOADER.IE)k.dH.releaseCapture();k.dH=null;k.constrainX=false;k.constrainY=false;}};k.isDragging=function(){return k.dH!=null;};k.getDragIcon=function(){return k.dH;};k.getDragSource=function(){return k.JSXID;};k.getDragType=function(){return k.DRAGTYPE;};k.getDragId=function(){return k.DRAGID;};k.getDragIds=function(){return k.DRAGIDS instanceof Array?k.DRAGIDS:[k.DRAGID];};});jsx3.require("jsx3.gui.Interactive");jsx3.Class.defineInterface("jsx3.gui.Alerts",null,function(h,i){var
ub={o:"no",d:"message",k:"var d = this.getAncestorOfType(jsx3.gui.Dialog); this.",c:"title",h:"jsxexecute",p:"",g:"(this.getAncestorOfType(jsx3.gui.Dialog));",l:"(d, d.getDescendantOfName('value').getValue());",q:"bold",b:"ok",i:"xml/components/dialog_prompt.xml",m:"value",a:"xml/components/dialog_alert.xml",f:"this.",j:"cancel",n:"xml/components/dialog_confirm.xml",e:"y0"};var
G=jsx3.gui.Interactive;i.getAlertsParent=jsx3.Method.newAbstract();i.alert=function(c,r,g,o,s){var
ha=jsx3.net.URIResolver.JSX;var
Kb=this.getAlertsParent().loadAndCache(ub.a,false,null,ha);var
vb=Kb.getDescendantOfName(ub.b);if(c!=null)Kb.getDescendantOfName(ub.c).setText(c);if(r!=null)Kb.getDescendantOfName(ub.d).setText(r);if(o===false)Kb.hideButton();else if(o!=null)vb.setText(o);if(g!=null){var
ta=ub.e;vb.y0=g;vb.setEvent(ub.f+ta+ub.g,ub.h);}this.configureAlert(Kb,s);this.getAlertsParent().paintChild(Kb);Kb.focus();return Kb;};i.prompt=function(g,c,k,e,m,q,o){var
la=jsx3.net.URIResolver.JSX;var
ta=this.getAlertsParent().loadAndCache(ub.i,false,null,la);var
kb=ta.getDescendantOfName(ub.b);var
vb=ta.getDescendantOfName(ub.j);if(g!=null)ta.getDescendantOfName(ub.c).setText(g);if(c!=null)ta.getDescendantOfName(ub.d).setText(c);if(m!=null)kb.setText(m);if(q!=null)vb.setText(q);if(k!=null){var
_=ub.e;kb.y0=k;kb.setEvent(ub.k+_+ub.l,ub.h);}if(e!=null){var
_=ub.e;vb.y0=e;vb.setEvent(ub.f+_+ub.g,ub.h);}this.configureAlert(ta,o);this.getAlertsParent().paintChild(ta);jsx3.sleep(function(){ta.getDescendantOfName(ub.m).focus();});return ta;};i.confirm=function(n,j,r,q,d,o,m,g,a,s){var
ua=jsx3.net.URIResolver.JSX;var
Bb=this.getAlertsParent().loadAndCache(ub.n,false,null,ua);var
rb=Bb.getDescendantOfName(ub.b);var
la=Bb.getDescendantOfName(ub.j);var
xa=Bb.getDescendantOfName(ub.o);var
jb=[rb,la,xa];m=m!=null?m-1:0;if(n!=null)Bb.getDescendantOfName(ub.c).setText(n);if(j!=null)Bb.getDescendantOfName(ub.d).setText(j);if(d!=null)rb.setText(d);if(o!=null)la.setText(o);if(q!=null){var
xb=ub.e;la.y0=q;la.setEvent(ub.f+xb+ub.g,ub.h);}if(r!=null){var
xb=ub.e;rb.y0=r;rb.setEvent(ub.f+xb+ub.g,ub.h);}if(g!=null||a!=null||m==3){if(a)xa.setText(a);if(g){var
xb=ub.e;xa.y0=g;xa.setEvent(ub.f+xb+ub.g,ub.h);}xa.setDisplay(ub.p);}var
U=jb[m];if(U){U.setFontWeight(ub.q);Bb.registerHotKey(function(b){if(b.enterKey()){this.getDescendantOfName(U.getName()).doExecute(b);b.cancelBubble();}},13,false,false,false);}this.configureAlert(Bb,s);this.getAlertsParent().paintChild(Bb);Bb.focus();return Bb;};i.configureAlert=function(m,k){if(k==null)return;if(k.width)m.setWidth(k.width,false);if(k.height)m.setHeight(k.height,false);if(k.noTitle)m.removeChild(m.getChild(ub.c));if(k.nonModal)m.setModal(0);};});jsx3.Alerts=jsx3.gui.Alerts;jsx3.require("jsx3.gui.Painted","jsx3.gui.Interactive");jsx3.Class.defineClass("jsx3.gui.Block",jsx3.gui.Painted,[jsx3.gui.Interactive],function(l,k){var
ub={oa:"font-size:",k:"left",O:"div",Ga:"@Mask 70%",ea:'" ',_:' JSXDragId="',p:"border",P:"jsxdblclick",ra:' tabindex="',q:"color",V:"mouseover",v:"margin",ha:";",I:"relative",Aa:"z-index:",a:"Verdana",F:"0px",u:"display",U:"keydown",ua:"overflow:hidden;",j:"hidden",ka:"block",La:"_jsxcQ",d:"jsx30block",z:" ",S:"click",Ma:"tabIndex",fa:' label="',D:"</span>",w:"mouseup",K:"visibility",R:"jsxclick",Ca:"_mask",aa:'" JSXDragType="JSX_GENERIC"',g:"normal",B:"... ... ...",b:"#000000",Q:"dblclick",va:"text-align:",M:"box",Na:"_jsxZs",qa:"font-weight:",la:"display:block;",f:"bold",ga:"background-color:",y:/<[^>]*>/gi,T:"jsxkeydown",ba:' id="',x:"doEndMove",e:"span",za:"visibility:hidden;",ja:"cursor:",c:"&#160;",h:"",W:"mouseout",H:"position",wa:/\"/g,Ha:"jsxcursor",Fa:"jsxbg",G:"top",Ja:"if (objEVENT.tabKey() && objEVENT.shiftKey()) { this.getParent().focus(); }",ma:"display:none;",i:"none",ca:'"',A:"...",t:":",pa:"px;",N:"100%",Ia:"@Wait",s:"relativebox",X:"mousedown",Z:"doBeginDrag",o:"backgroundColor",xa:"&quot;",sa:'" jsxtabindex="',r:"cursor",C:"<span class='jsx30block_drag'>",Ba:"onfocus",na:"font-family:",l:"center",Ka:"var objEVENT = jsx3.gui.Event.wrap(event); if (objEVENT.shiftKey()) { jsx3.GO(this.id).getParent().focus(); }",ia:"color:",da:' class="',L:"zIndex",Oa:"3.00.00",m:"right",Da:"jsxbgcolor",Y:"doBeginMove",J:"absolute",Ea:"@Solid Shadow",n:"jsx:///images/spc.gif",ya:' title="',ta:"overflow:auto;",E:"padding"};var
ga=jsx3.gui.Event;var
F=jsx3.gui.Interactive;l.SCROLLSIZE=16;l.OVERFLOWSCROLL=1;l.OVERFLOWHIDDEN=2;l.OVERFLOWEXPAND=3;l.DEFAULTFONTNAME=ub.a;l.DEFAULTFONTSIZE=10;l.DEFAULTCOLOR=ub.b;l.DEFAULTTEXT=ub.c;l.DEFAULTCLASSNAME=ub.d;l.DEFAULTTAGNAME=ub.e;l.FONTBOLD=ub.f;l.FONTNORMAL=ub.g;l.DISPLAYBLOCK=ub.h;l.DISPLAYNONE=ub.i;l.VISIBILITYVISIBLE=ub.h;l.VISIBILITYHIDDEN=ub.j;l.NULLSTYLE=-1;l.ALIGNLEFT=ub.k;l.ALIGNCENTER=ub.l;l.ALIGNRIGHT=ub.m;l.ABSOLUTE=0;l.RELATIVE=1;l.MASK_NO_EDIT=jsx3.gui.Painted.MASK_NO_EDIT;l.MASK_ALL_EDIT=jsx3.gui.Painted.MASK_ALL_EDIT;l.MASK_MOVE_ONLY={MM:true};l.MASK_EAST_ONLY={NN:false,EE:true,SS:false,WW:false,MM:false};l.SPACE=jsx3.resolveURI(ub.n);jsx3.html.loadImages(l.SPACE);k.init=function(s,f,a,q,n,e){this.jsxsuper(s);if(f!=null)this.setLeft(f);if(a!=null)this.setTop(a);if(q!=null)this.setWidth(q);if(n!=null)this.setHeight(n);if(e!=null)this.setText(e);};k.getBackgroundColor=function(){return this.jsxbgcolor;};k.setBackgroundColor=function(f,b){this.jsxbgcolor=f;if(b)this.updateGUI(ub.o,f==l.NULLSTYLE?ub.h:f);return this;};k.getBackground=function(){return this.jsxbg;};k.setBackground=function(e){this.jsxbg=e;return this;};k.getBorder=function(){return this.jsxborder;};k.setBorder=function(q,g){this.jsxborder=q;if(g)this.recalcBox([ub.p]);else this.ce();return this;};k.getColor=function(){return this.jsxcolor;};k.setColor=function(g,q){this.jsxcolor=g;if(q)this.updateGUI(ub.q,g==l.NULLSTYLE?ub.h:g);return this;};k.getCursor=function(){return this.jsxcursor;};k.setCursor=function(q,a){this.jsxcursor=q;if(a)this.updateGUI(ub.r,q);return this;};k.getCSSOverride=function(){return this.jsxstyleoverride;};k.setCSSOverride=function(f){this.jsxstyleoverride=f;return this;};k.getClassName=function(){return this.jsxclassname;};k.setClassName=function(c){this.jsxclassname=c;return this;};k.getDisplay=function(){return this.jsxdisplay;};k.setDisplay=function(o,b){if(this.jsxdisplay!=o){this.jsxdisplay=o;if(b){if(o!=ub.i){var
L=this.Wl();if(!(this.getRelativePosition()==0||L&&L.getBoxType()!=ub.s)){var
za=jsx3.gui.Painted.Box.getCssFix().split(ub.t);if(za.length==2)o=za[1];}}this.updateGUI(ub.u,o);}}return this;};k.getFontName=function(){return this.jsxfontname;};k.setFontName=function(g){this.jsxfontname=g;return this;};k.getFontSize=function(){return this.jsxfontsize;};k.setFontSize=function(e){this.jsxfontsize=e;return this;};k.getFontWeight=function(){return this.jsxfontweight;};k.setFontWeight=function(e){this.jsxfontweight=e;return this;};k.getHeight=function(){return this.jsxheight;};k.setHeight=function(b,o){this.jsxheight=b;if(o){this.af({height:b},true);}else this.ce();return this;};k.getIndex=function(){return this.jsxindex;};k.setIndex=function(r,m){this.jsxindex=r;if(m){var
_=this.getRendered();if(_!=null)_.tabIndex=r;}return this;};l.getJSXParent=function(m){return jsx3.html.getJSXParent(m);};k.getLeft=function(){return this.jsxleft;};k.setLeft=function(c,o){this.jsxleft=c;if(o){if(isNaN(c))c=0;this.af({left:c},true);}else this.ki(false);return this;};k.setDimensions=function(q,a,c,b,m){if(q instanceof Array){m=a;b=q[3];c=q[2];a=q[1];q=q[0];}if(q!=null)this.jsxleft=q;if(a!=null)this.jsxtop=a;if(c!=null)this.jsxwidth=c;if(b!=null)this.jsxheight=b;if(m){this.af({left:this.jsxleft,top:this.jsxtop,width:this.jsxwidth,height:this.jsxheight},true);}else this.ce();};k.getDimensions=function(){return [this.getLeft(),this.getTop(),this.getWidth(),this.getHeight()];};k.getMargin=function(){return this.jsxmargin;};k.setMargin=function(h,q){this.jsxmargin=h;if(q)this.recalcBox([ub.v]);else this.ce();return this;};k.getMaskProperties=function(){var
U={};U.NN=true;U.SS=true;U.EE=true;U.WW=true;U.MM=this.getRelativePosition()==0;return U;};k.doBeginMove=function(p,i){if(p.leftButton()){this.jsxsupermix(p,i);ga.subscribe(ub.w,this,ub.x);p.cancelAll();}};k.doEndMove=function(d){d=d.event;var
ja=this.getRendered(d);if(d.leftButton()){ga.unsubscribe(ub.w,this,ub.x);this.jsxsupermix(d,ja);}else this.Df(d,ja);};k.getDragIcon=function(g,j,s,o){var
oa=g&&g.innerHTML?jsx3.util.strTruncate((g.innerHTML+ub.h).replace(ub.y,ub.z),25,ub.A,0.5):ub.B;return ub.C+oa+ub.D;};k.doBeginDrag=function(c,j){if(c.leftButton())this.doDrag(c,j,this.getDragIcon);};k.getOverflow=function(){return this.jsxoverflow;};k.setOverflow=function(q){this.jsxoverflow=q;return this;};k.getPadding=function(){return this.jsxpadding;};k.setPadding=function(p,f){this.jsxpadding=p;if(f)this.recalcBox([ub.E]);else this.ce();return this;};k.getPropertiesPath=function(){return null;};k.getModelEventsPath=function(){return null;};k.getRelativePosition=function(){return this.jsxrelativeposition;};k.setRelativePosition=function(g,e){if(this.jsxrelativeposition!=g){this.ce();this.jsxrelativeposition=g;if(e){if(g==0){this.setDimensions(this.getLeft()||Number(0),this.getTop()||Number(0),null,null,true);this.updateGUI(ub.v,ub.F);}else{this.updateGUI(ub.k,ub.F);this.updateGUI(ub.G,ub.F);if(this.getMargin())this.setMargin(this.getMargin(),true);}this.updateGUI(ub.H,g==1?ub.I:ub.J);if(this.getDisplay()!=ub.i)this.setDisplay(ub.h,true);}}return this;};k.getTagName=function(){return this.jsxtagname;};k.setTagName=function(f){this.jsxtagname=f;this.ce();return this;};k.getText=function(){return this.jsxtext;};k.setText=function(a,d){this.jsxtext=a;if(d)if(this.getChild(0)!=null||this.Wl(true).lg(0)!=null){this.repaint();}else{var
Ja=this.getRendered();if(Ja!=null)Ja.innerHTML=a;}return this;};k.getTextAlign=function(){return this.jsxtextalign;};k.setTextAlign=function(q){this.jsxtextalign=q;return this;};k.getTip=function(){return this.jsxtip;};k.setTip=function(g){this.jsxtip=g;var
yb;if(yb=this.getRendered())yb.title=g;return this;};k.getTop=function(){return this.jsxtop;};k.setTop=function(r,p){this.jsxtop=r;if(p){if(isNaN(r))r=0;this.af({top:r},true);}else this.ki(false);return this;};k.updateGUI=function(e,h){var
Va=this.getRendered();if(Va!=null)try{Va.style[e]=h;}catch(Kb){}};k.getVisibility=function(){return this.jsxvisibility;};k.setVisibility=function(r,s){this.jsxvisibility=r;if(s)this.updateGUI(ub.K,r);return this;};k.getWidth=function(){return this.jsxwidth;};k.setWidth=function(a,o){this.jsxwidth=a;if(o){this.af({width:a},true);}else this.ce();return this;};k.getZIndex=function(){return this.jsxzindex;};k.setZIndex=function(h,a){this.jsxzindex=h;if(a)this.updateGUI(ub.L,h);return this;};k.Rc=function(p,m,b){this.Gi(p,m,b,4);};k.Gc=function(h){this.applyDynamicProperties();if(this.getParent()&&(h==null||isNaN(h.parentwidth)||isNaN(h.parentheight))){h=this.getParent().uf(this);}else if(h==null)h={};var
Qa=h.boxtype&&h.boxtype!=ub.M||this.getRelativePosition()!=0;var
vb=Qa?null:h.left?h.left:this.getLeft();var
O=Qa?null:h.top?h.top:this.getTop();if(!Qa&&!vb)vb=0;if(!Qa&&!O)O=0;var
Lb,Ua,ta,V;if(!h.boxtype)h.boxtype=Qa?ub.s:ub.M;if(h.tagname==null)h.tagname=(Lb=this.getTagName())?Lb.toLowerCase():jsx3.gui.Block.DEFAULTTAGNAME;if(h.left==null&&h.boxtype==ub.M)h.left=vb;if(h.top==null&&h.boxtype==ub.M)h.top=O;if(h.width==null)h.width=h.width?h.width:this.getWidth();if(h.height==null)h.height=h.height?h.height:this.getHeight();if(h.width==ub.N||h.tagName==ub.O&&this.wg()==ub.h){h.tagname=ub.O;h.container=true;}if((Ua=this.getPadding())!=null&&Ua!=ub.h)h.padding=Ua;if(Qa&&(ta=this.getMargin())!=null&&ta!=ub.h)h.margin=ta;if((V=this.getBorder())!=null&&V!=ub.h)h.border=V;return new
jsx3.gui.Painted.Box(h);};k.paint=function(q){this.applyDynamicProperties();q=q==null?this.wg():q;var
Fb=this.getId();var
Y={};if(this.hasEvent(ub.P))Y[ub.Q]=true;if(this.hasEvent(ub.R))Y[ub.S]=true;if(this.hasEvent(ub.T))Y[ub.U]=true;var
ja=ub.h;if(this.getCanSpy()==1){Y[ub.V]=true;Y[ub.W]=true;}if(this.getCanMove()==1){if(this.getCanMove()==1)Y[ub.X]=ub.Y;}else if(this.getMenu()!=null){Y[ub.w]=true;}else if(this.getCanDrop()==1)Y[ub.w]=true;if(Y[ub.X]==null&&this.getCanDrag()==1){Y[ub.X]=ub.Z;ja=ja+(ub._+Fb+ub.aa);}var
Oa=this.jh(Y,0)+ja;var
Xa=this.renderAttributes(null,true);var
y=this.Wl(true);y.setAttributes(this.Fo()+this.bn()+Oa+ub.ba+Fb+ub.ca+this.on()+ub.da+this.to()+ub.ea+Xa);y.setStyles(this.Yh()+this.zf()+this.Of()+this.eh()+this.dk()+this.ad()+this.zk()+this.Bf()+this.fn()+this.Xe()+this.bd()+this.paintBlockDisplay()+this.fh());return y.paint().join(q+this.paintChildren());};k.el=function(q,o){if(this.getCanSpy()==1)this.doSpyOver(q,o);if(this.getCanDrop()==1)this.doDrop(q,o,jsx3.EventHelp.ONBEFOREDROP);};k.Qi=function(c,j){if(this.getCanSpy()==1)this.doSpyOut(c,j);if(this.getCanDrop()==1)this.doDrop(c,j,jsx3.EventHelp.ONCANCELDROP);};k.Df=function(p,i){if(this.getCanDrop()==1)this.doDrop(p,i,jsx3.EventHelp.ONDROP);else this.jsxsupermix(p,i);};k.on=function(){var
G=this.getName();return G!=null?ub.fa+G+ub.ca:ub.h;};k.zf=function(){var
Ta=this.getBackgroundColor();return Ta?ub.ga+Ta+ub.ha:ub.h;};k.Of=function(){return this.getBackground()?this.getBackground()+ub.ha:ub.h;};k.eh=function(){var
wb=this.getColor();return wb?ub.ia+wb+ub.ha:ub.h;};k.Xe=function(){var
bb=this.getCursor();return bb?ub.ja+bb+ub.ha:ub.h;};k.fh=function(){return this.getCSSOverride()?this.getCSSOverride():ub.h;};k.to=function(){var
tb=this.getClassName();return l.DEFAULTCLASSNAME+(tb?ub.z+tb:ub.h);};k.paintBlockDisplay=function(){if(jsx3.util.strEmpty(this.getDisplay())||this.getDisplay()==ub.ka){var
zb=this.Wl();if(this.getWidth()==ub.N){return ub.la;}else return ub.h;}else if(this.getDisplay()==ub.i)return ub.ma;return ub.h;};k.bi=function(){var
wa=this.getDisplay();return jsx3.util.strEmpty(wa)||wa==ub.h?ub.h:ub.ma;};k.ad=function(){var
_a=this.getFontName();return _a?ub.na+_a+ub.ha:ub.h;};k.Yh=function(){var
ma=parseInt(this.getFontSize());return isNaN(ma)?ub.h:ub.oa+ma+ub.pa;};k.Bf=function(){var
oa=this.getFontWeight();return oa?ub.qa+oa+ub.ha:ub.h;};k.Fo=function(s){if(s==null)s=this.getIndex();return s!=null?ub.ra+s+ub.sa+s+ub.ca:ub.h;};k.dk=function(){if(this.getOverflow()==1){return ub.ta;}else if(this.getOverflow()==2){return ub.ua;}else return ub.h;};k.wg=function(){return this.getText()?this.getText():ub.h;};k.fn=function(){var
Bb=this.getTextAlign();return Bb?ub.va+Bb+ub.ha:ub.h;};k.bn=function(){var
da=this.getTip();if(da!=null){da=da.replace(ub.wa,ub.xa);return da?ub.ya+da+ub.ea:ub.h;}else if(jsx3.gui.Form&&this.instanceOf(jsx3.gui.Form)){var
lb=this.getKeyBinding();return lb?ub.ya+lb.replace(ub.wa,ub.xa)+ub.ea:ub.h;}else return ub.h;};k.bd=function(){return jsx3.util.strEmpty(this.getVisibility())||this.getVisibility()==ub.h?ub.h:ub.za;};k.zk=function(){var
bb=this.getZIndex();return isNaN(bb)?ub.h:ub.Aa+bb+ub.ha;};k.showMask=function(q){if(this._jsxcQ)this.hideMask();var
Za;if(Za=this.getRendered()){var
Gb=this.getAbsolutePosition().H;if(Za.onfocus)Za._jsxZs=Za.onfocus;jsx3.html.addEventListener(Za,ub.Ba,l.r5);if(Za.tabIndex)Za._jsxtabindex=Za.tabIndex;Za.tabIndex=0;this._jsxcQ=this.getId()+ub.Ca;var
Ea=(new
l(this._jsxcQ,0,0,ub.N,ub.N,q)).setOverflow(2).setFontWeight(ub.f).setTextAlign(ub.l).setIndex(0).setRelativePosition(0).setZIndex(32000).setDynamicProperty(ub.Da,ub.Ea).setDynamicProperty(ub.Fa,ub.Ga).setDynamicProperty(ub.Ha,ub.Ia);Ea.setWidth(ub.N);Ea.setHeight(ub.N);Ea.setPadding(parseInt(Gb/2));Ea.setEvent(ub.Ja,ub.T);Ea.setAttribute(ub.Ba,ub.Ka);this.setChild(Ea);this.paintChild(Ea);}};l.r5=function(j){var
Oa=jsx3.GO(this.id);if(Oa){var
Qa=ga.wrap(j||window.event);if(!Qa.shiftKey())if(Oa.getChildren().length)Oa.getLastChild().focus();}};k.hideMask=function(){var
Eb;if(Eb=this.getChild(this._jsxcQ)){this.removeChild(Eb);delete this[ub.La];var
Ba;if(Ba=this.getRendered()){if(Ba._jsxtabindex){Ba.tabIndex=Ba._jsxtabindex;}else Ba.removeAttribute(ub.Ma);jsx3.html.removeEventListener(Ba,ub.Ba,l.r5);if(Ba._jsxZs){Ba.onfocus=Ba._jsxZs;delete Ba[ub.Na];}else{}}}};l.getVersion=function(){return ub.Oa;};});jsx3.Block=jsx3.gui.Block;