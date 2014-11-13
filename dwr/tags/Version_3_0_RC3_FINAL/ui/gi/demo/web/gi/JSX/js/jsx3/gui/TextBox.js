/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.TextBox",jsx3.gui.Block,[jsx3.gui.Form],function(d,m){var
ub={oa:"jS",k:"uszip",O:"jsxclick",ea:"background-color:",_:"overflow:",p:/([a-zA-Z0-9_~\-\.]+)@([a-zA-Z0-9]+)\.[a-zA-Z0-9]{2,}/,P:"click",q:/^\d+$/,V:'"',v:"jsxchange",ha:"password",I:"keyup",a:"",F:"keypress",u:"jsxkeypress",U:' id="',j:"letter",ka:"string",d:"#ffffff",z:"input[password]",S:"jsxmousewheel",fa:' type="',D:"2 0 0 2",w:"jsxkeyup",K:"jsxfocus",R:"mousedown",aa:";",g:"phone",B:"inline",b:"auto",Q:"jsxmousedown",M:"jsxdblclick",la:"#FFFF66",f:"ssn",ga:"text",y:"input[text]",T:"mousewheel",ba:' wrap="',x:"jsxincchange",e:"none",ja:" ",c:"scroll",h:"email",W:' value="',H:"keydown",G:"jsxkeydown",ma:"3.0.00",i:"number",ca:"virtual",A:"textarea",t:"jsxexecute",N:"dblclick",s:/^\d{5}(-\d{4})?$/,X:'" class="',Z:' class="',o:/^[0-9\-\(\) ]+$/,r:/^[a-zA-Z ,-\.]+$/,C:"box",na:"1;",l:"jsx30textbox",ia:' MAXLENGTH="',da:"off",L:"focus",m:/[\s\S]*/,Y:'" ',J:"blur",n:/^\d{3}-\d{2}-\d{4}$/,E:"solid 1px #a6a6af;solid 1px #e6e6e6;solid 1px #e6e6e6;solid 1px #a6a6af"};var
ga=jsx3.gui.Event;var
F=jsx3.gui.Interactive;d.TYPETEXT=0;d.TYPETEXTAREA=1;d.TYPEPASSWORD=2;d.WRAPYES=1;d.WRAPNO=0;d.OVERFLOWNORMAL=ub.a;d.OVERFLOWAUTO=ub.b;d.OVERFLOWSCROLL=ub.c;jsx3.gui.TextBox.DEFAULTBACKGROUNDCOLOR=ub.d;d.VALIDATIONNONE=ub.e;d.VALIDATIONSSN=ub.f;d.VALIDATIONPHONE=ub.g;d.VALIDATIONEMAIL=ub.h;d.VALIDATIONNUMBER=ub.i;d.VALIDATIONLETTER=ub.j;d.VALIDATIONUSZIP=ub.k;d.DEFAULTCLASSNAME=ub.l;d.zM={};d.zM[ub.e]=ub.m;d.zM[ub.f]=ub.n;d.zM[ub.g]=ub.o;d.zM[ub.h]=ub.p;d.zM[ub.i]=ub.q;d.zM[ub.j]=ub.r;d.zM[ub.k]=ub.s;m.init=function(p,i,a,n,k,r,c){this.jsxsuper(p,i,a,n,k);if(r!=null){this.setDefaultValue(r);this.setValue(r);}if(c!=null)this.setType(c);};m.kc=function(q,o){if(q.enterKey()&&this.getEvent(ub.t)){this.doEvent(ub.t,{objEVENT:q});}else this.doEvent(ub.u,{objEVENT:q});};m.cn=function(o,q){this.jsxsupermix(o,q);if(this.jsxvalue!==q.value){var
ha=this.doEvent(ub.v,{objEVENT:o,strPREVIOUS:this.jsxvalue,strVALUE:q.value});if(ha===false){q.value=this.jsxvalue;}else this.jsxvalue=q.value;}};m.kf=function(j,c){this.doEvent(ub.w,{objEVENT:j});if(this.getType()==1){var
Aa=this.getMaxLength();if(Aa>0){var
Ya=c.value;if(Ya&&Ya.length>Aa)c.value=Ya.substring(0,Aa);}}if(this.hasEvent(ub.x)){var
db=c.value;if(this._jsxYp!=db){var
X=this.doEvent(ub.x,{objEVENT:j,strPREVIOUS:this._jsxYp,strVALUE:db});if(X===false){c.value=this._jsxYp!=null?this._jsxYp:ub.a;}else this._jsxYp=db;}}};m.getMaxLength=function(){return this.jsxmaxlength!=null?this.jsxmaxlength:null;};m.setMaxLength=function(q){this.jsxmaxlength=q;return this;};m.getType=function(){return this.jsxtype==null?0:this.jsxtype;};m.setType=function(p){this.jsxtype=p;this.ce();return this;};m.getValue=function(){var
Fb=this.getRendered();if(Fb!=null){return Fb.value;}else return this.jsxvalue!=null?this.jsxvalue:this.getDefaultValue();};m.MM=function(){var
la=null;var
ea=this.getRendered();if(ea!=null)la=ea.value;if(la==null)la=this.jsxvalue!=null?this.jsxvalue:this.getDefaultValue();return jsx3.util.strEscapeHTML(la.toString());};m.wg=function(){return this.getText()?this.getText():ub.a;};m.getDefaultValue=function(){return this.wg();};m.setValue=function(b){this.jsxvalue=b;this._jsxYp=b;var
aa=this.getRendered();if(aa!=null)aa.value=b!=null?b:ub.a;return this;};m.setDefaultValue=function(g){this.setText(g);return this;};m.getWrap=function(){return this.jsxwrap==null?1:this.jsxwrap;};m.setWrap=function(r){this.jsxwrap=r;return this;};m.Rc=function(f,c,l){this.Gi(f,c,l,3);};m.Gc=function(q){if(this.getParent()&&(q==null||isNaN(q.parentwidth)||isNaN(q.parentheight))){q=this.getParent().uf(this);}else if(q==null)q={};this.applyDynamicProperties();var
ra=this.getRelativePosition()!=0;var
Hb,Q,mb,ta,Bb;var
Ea=!ra&&!jsx3.util.strEmpty(this.getTop())?this.getTop():0;var
O=!ra&&!jsx3.util.strEmpty(this.getLeft())?this.getLeft():0;if(q.left==null)q.left=O;if(q.top==null)q.top=Ea;if(q.width==null)q.width=(ta=this.getWidth())!=null?ta:100;if(q.height==null)q.height=(Bb=this.getHeight())!=null?Bb:18;var
Ja=this.getType();if(Ja==0){q.tagname=ub.y;q.empty=true;}else if(Ja==2){q.tagname=ub.z;q.empty=true;}else q.tagname=ub.A;if(!q.boxtype)q.boxtype=ra?ub.B:ub.C;q.padding=(Hb=this.getPadding())!=null&&Hb!=ub.a?Hb:ub.D;if(q.tagname!=ub.A)q.margin=ra&&(Q=this.getMargin())!=null&&Q!=ub.a?Q:null;q.border=(mb=this.getBorder())!=null&&mb!=ub.a?mb:ub.E;var
u=new
jsx3.gui.Painted.Box(q);return u;};m.paint=function(){this.applyDynamicProperties();var
Ha=this.getId();var
sb=this.getType();var
O={};if(this.hasEvent(ub.t)||this.hasEvent(ub.u))O[ub.F]=true;if(this.hasEvent(ub.G))O[ub.H]=true;if(this.hasEvent(ub.w)||this.hasEvent(ub.x)||this.getType()==1&&this.getMaxLength()>0){O[ub.I]=true;this._jsxYp=this.getValue();}O[ub.J]=true;if(this.hasEvent(ub.K))O[ub.L]=true;if(this.hasEvent(ub.M))O[ub.N]=true;if(this.hasEvent(ub.O))O[ub.P]=true;if(this.hasEvent(ub.Q))O[ub.R]=true;if(this.hasEvent(ub.S))O[ub.T]=true;var
U=this.jh(O,0);var
_=this.renderAttributes(null,true);var
T=this.Wl(true);if(sb==0||sb==2){T.setAttributes(this.pX()+ub.U+Ha+ub.V+this.on()+this.wn()+this.RY()+this.Fo()+this.bn()+U+ub.W+this.MM()+ub.X+this.to()+ub.Y+_);T.setStyles(this.ad()+this.Yh()+this.Bf()+this.bd()+this.bi()+this.zk()+this.zf()+this.Of()+this.eh()+this.fn()+this.fh());var
Ib=ub.a;}else{T.setAttributes(ub.U+Ha+ub.V+this.on()+this.wn()+this.Fo()+this.bn()+U+ub.Z+this.to()+ub.Y+this.renderAttributes()+this.p2());T.setStyles(this.ad()+this.Yh()+this.Bf()+this.bd()+this.bi()+this.zk()+this.dk()+this.zf()+this.Of()+this.eh()+this.fn()+this.fh());var
Ib=this.MM();}return T.paint().join(Ib);};m.dk=function(){return ub._+(this.getOverflow()?this.getOverflow():ub.a)+ub.aa;};m.p2=function(){return ub.ba+(this.getWrap()==1?ub.ca:ub.da)+ub.V;};m.zf=function(){var
db=this.getEnabled()!=0?this.getBackgroundColor()||d.DEFAULTBACKGROUNDCOLOR:this.getDisabledBackgroundColor()||jsx3.gui.Form.DEFAULTDISABLEDBACKGROUNDCOLOR;return ub.ea+db+ub.aa;};m.pX=function(){return ub.fa+(this.getType()==0?ub.ga:ub.ha)+ub.V;};m.RY=function(c){return this.getMaxLength()!=null?ub.ia+parseInt(this.getMaxLength())+ub.Y:ub.a;};m.to=function(){var
oa=this.getClassName();return d.DEFAULTCLASSNAME+(oa?ub.ja+oa:ub.a);};m.getValidationType=function(){return jsx3.util.strEmpty(this.jsxvalidationtype)?ub.e:this.jsxvalidationtype;};m.setValidationType=function(p){this.jsxvalidationtype=p;return this;};m.getValidationExpression=function(){return jsx3.util.strEmpty(this.jsxvalidationexpression)?null:this.jsxvalidationexpression;};m.setValidationExpression=function(j){this.jsxvalidationexpression=j;return this;};m.doValidate=function(){this.setValidationState(1);var
_=this.getValue();var
G=null;if(_==null||jsx3.util.strTrim(_)==ub.a){if(this.getRequired()==1)this.setValidationState(0);}else if(typeof _==ub.ka){var
ca=this.getValidationExpression();if(ca==null){G=d.zM[this.getValidationType()];}else G=new
RegExp(ca);this.setValidationState(_.search(G)==0?1:0);}return this.getValidationState();};m.beep=function(){jsx3.gui.Yl(this.getRendered(),{backgroundColor:ub.la});};d.getVersion=function(){return ub.ma;};m.emInit=function(q){this.jsxsupermix(q);var
xa=ub.G;if(!this.hasEvent(xa))this.setEvent(ub.na,xa);this.subscribe(xa,this,ub.oa);};m.emUpdateDisplay=function(b,n){if(this.getType()==1){var
B=isNaN(this._jsxo1)?b.W:Math.min(parseInt(this._jsxo1),b.W);var
Lb=isNaN(this._jsxGN)?b.H:Math.min(parseInt(this._jsxGN),n.H-b.T);this.setDimensions(b.L,b.T,B,Lb,true);}else this.jsxsupermix(b,n);};m.emGetValue=function(){var
v=this.emGetSession().value;var
Va=this.getValue();return v===null&&Va===ub.a?v:Va;};m.jS=function(b){var
tb=b.context.objEVENT;var
Ua=false;if(!tb.hasModifier(true)){var
Z=tb.keyCode();var
R=false;if(this.getType()==1){Ua=!tb.shiftKey()&&Z==13;R=tb.isArrowKey();}else R=Z==37||Z==39;if(!Ua&&R){var
x=this.getRendered(tb);var
aa=jsx3.html.getSelection(x);var
wb=x.value;var
Gb=Z==37||Z==38;Ua=Gb&&(aa.getStartIndex()>0||aa.getEndIndex()>0)||!Gb&&(aa.getStartIndex()<wb.length||aa.getEndIndex()<wb.length);}}if(Ua)tb.cancelBubble();};});jsx3.TextBox=jsx3.gui.TextBox;
