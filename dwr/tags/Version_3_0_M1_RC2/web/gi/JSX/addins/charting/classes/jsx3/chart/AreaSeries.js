/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.Series");jsx3.Class.defineClass("jsx3.chart.AreaSeries",jsx3.chart.Series,null,function(c,g){var
ub={o:"jsxid",d:"jsx3.chart.AreaSeries.tooltip",k:"_fill",r:"}",c:"reverseStep",h:"Dn",p:"{",g:"nd",l:"_stroke",q:",",b:"step",i:"ee",m:"bad AreaSeries form: ",a:"segment",f:"pointRenderer",j:"Wn",n:"_p",s:", x = ",e:"form"};var
t=jsx3.vector;var
Gb=jsx3.chart;c.FORM_SEGMENT=ub.a;c.FORM_STEP=ub.b;c.FORM_REVSTEP=ub.c;c.GB={segment:1,step:1,reverseStep:1};c.bG=4;g.init=function(d,i){this.jsxsuper(d,i);this.xField=null;this.yField=null;this.minField=null;this.form=ub.a;this.pointRadius=null;this.pointRenderer=null;this.pointFill=null;this.pointStroke=null;this.pointGradient=null;this.setTooltipFunction(ub.d);};g.getXField=function(){return this.xField;};g.setXField=function(n){this.xField=n;};g.getYField=function(){return this.yField;};g.setYField=function(i){this.yField=i;};g.getMinField=function(){return this.minField;};g.setMinField=function(s){this.minField=s;};g.getForm=function(){return this.form;};g.setForm=function(j){if(c.GB[j]){this.form=j;}else throw new
jsx3.IllegalArgumentException(ub.e,j);};g.getPointRadius=function(){return this.pointRadius!=null?this.pointRadius:c.bG;};g.setPointRadius=function(p){this.pointRadius=p;};g.getPointRenderer=function(){return Gb.Sl(this,ub.f);};g.setPointRenderer=function(l){Gb.sj(this,ub.f,l);};g.getPointFill=function(){return this.pointFill;};g.setPointFill=function(j){this.pointFill=j;};g.getPointStroke=function(){return this.pointStroke;};g.setPointStroke=function(l){this.pointStroke=l;};g.getPointGradient=function(){return this.pointGradient;};g.setPointGradient=function(b){this.pointGradient=b;};g.Mf=function(){var
la=this.pj(ub.g);if(la==null)this.K2();return this.pj(ub.g);};g.El=function(){var
Xa=this.pj(ub.h);if(Xa==null)this.K2();return this.pj(ub.h);};g.K2=function(){var
ob=this.ef();var
A=t.Fill.valueOf(this.getPointFill());if(A==null&&ob!=null)A=new
t.Fill(ob.getColor());if(A!=null)A=Gb.addGradient(A,this.pointGradient);var
Q=t.Stroke.valueOf(this.getPointStroke());this.Ho(ub.g,A);this.Ho(ub.h,Q);};g.getXValue=function(b){if(this.xField)return Gb.asNumber(b.getAttribute(this.xField));return null;};g.getYValue=function(e){if(this.yField)return Gb.asNumber(e.getAttribute(this.yField));return null;};g.getMinValue=function(h){if(this.minField)return Gb.asNumber(h.getAttribute(this.minField));return null;};g.fm=function(l,p,o,b){var
x=this.pj(ub.i);if(x==null){x=[];this.Ho(ub.i,x);}x.push([l,p,o,b]);};g.Bh=function(j,f){var
J=this.pj(ub.j);if(J==null){J=[];this.Ho(ub.j,J);}J.push([j,f]);};g._n=function(){var
hb=this.pj(ub.i);if(hb!=null)hb.splice(0,hb.length);var
sb=this.pj(ub.j);if(sb!=null)sb.splice(0,sb.length);};g.updateView=function(){this.jsxsuper();var
jb=this.getCanvas();var
ib=null,za=null;var
V=this.Rj();if(V!=null){ib=new
t.Shape(null,0,0,jb.getWidth(),jb.getHeight());ib.setId(this.getId()+ub.k);jb.appendChild(ib);ib.setFill(V);}var
hb=this.ef();if(hb!=null){za=new
t.Shape(null,0,0,jb.getWidth(),jb.getHeight());za.setId(this.getId()+ub.l);jb.appendChild(za);za.setStroke(hb);}this.K2();var
X=this.pj(ub.i);var
Z=this.pj(ub.j);if(X==null||X.length==0||Z==null||Z.length==0)return;var
xb=this.getPointRenderer();var
eb=this.getPointRadius();var
Pa=this.getTooltipFunction();var
wa=this.Mf();var
L=this.El();var
Db=this.getColorFunction();this.tg(ib);for(var
Cb=0;Cb<X.length;Cb++){var
z=X[Cb][0];var
Ha=X[Cb][1];var
vb=X[Cb][2];var
oa=X[Cb][3];if(Cb==0){if(ib!=null)ib.pathMoveTo(Ha,vb);if(za!=null)za.pathMoveTo(Ha,vb);}else{var
Ua=X[Cb-1][1];var
J=X[Cb-1][2];if(this.form==ub.a){if(ib!=null)ib.pathLineTo(Ha,vb);if(za!=null)za.pathLineTo(Ha,vb);}else if(this.form==ub.b){if(ib!=null)ib.pathLineTo(Ha,J).pathLineTo(Ha,vb);if(za!=null)za.pathLineTo(Ha,J).pathLineTo(Ha,vb);}else if(this.form==ub.c){if(ib!=null)ib.pathLineTo(Ua,vb).pathLineTo(Ha,vb);if(za!=null)za.pathLineTo(Ua,vb).pathLineTo(Ha,vb);}else Gb.LOG.error(ub.m+this.form);}if(xb!=null){var
na=Db!=null?Db.call(null,z,oa):wa;var
ya=xb.render(Ha-eb,vb-eb,Ha+eb,vb+eb,na,L);ya.setId(this.getId()+ub.n+Cb);this.tg(ya,Cb,z.getAttribute(ub.o));jb.appendChild(ya);if(Pa!=null)ya.setToolTip(Pa.call(null,this,z));}}for(var
Cb=Z.length-1;Cb>=0;Cb--){var
Ha=Z[Cb][0];var
vb=Z[Cb][1];if(Cb==Z.length-1){ib.pathLineTo(Ha,vb);}else{var
Ua=Z[Cb+1][0];var
J=Z[Cb+1][1];if(this.form==ub.a){ib.pathLineTo(Ha,vb);}else if(this.form==ub.b){ib.pathLineTo(Ua,vb).pathLineTo(Ha,vb);}else if(this.form==ub.c){ib.pathLineTo(Ha,J).pathLineTo(Ha,vb);}else Gb.LOG.error(ub.m+this.form);}}ib.pathClose();};c.tooltip=function(a,s){var
ab=a.getXValue(s);var
D=a.getYValue(s);var
Ga=a.getMinValue(s);var
Ya=Ga!=null?ub.p+Ga+ub.q+D+ub.r:D;if(ab!=null)Ya=Ya+(ub.s+ab);return Ya;};c.getVersion=function(){return Gb.si;};});
