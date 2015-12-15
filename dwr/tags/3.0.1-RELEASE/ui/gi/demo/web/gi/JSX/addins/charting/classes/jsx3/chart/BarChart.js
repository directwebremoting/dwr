/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.CartesianChart","jsx3.chart.BarSeries");jsx3.Class.defineClass("jsx3.chart.BCChart",jsx3.chart.CartesianChart,null,function(d,k){var
ub={d:"type",i:"bad parallel axis type: ",a:"clustered",h:"zi",c:"stacked100",f:"getMinValue",g:"unsupported Bar/Column Chart type: ",b:"stacked",e:"Mg"};d.TYPE_CLUSTERED=ub.a;d.TYPE_STACKED=ub.b;d.TYPE_STACKED100=ub.c;d.nT={clustered:1,stacked:1,stacked100:1};k.init=function(f,j,m,l,n){this.jsxsuper(f,j,m,l,n);this.type=ub.a;this.seriesOverlap=0;this.categoryCoverage=0.65;};k.getType=function(){return this.type;};k.setType=function(j){if(d.nT[j])this.type=j;else throw new
jsx3.IllegalArgumentException(ub.d,j);};k.getSeriesOverlap=function(){return !isNaN(this.seriesOverlap)?this.seriesOverlap:0;};k.setSeriesOverlap=function(f){this.seriesOverlap=f;};k.getCategoryCoverage=function(){return !isNaN(this.categoryCoverage)?this.categoryCoverage:1;};k.setCategoryCoverage=function(g){this.categoryCoverage=g;};k.RE=jsx3.Method.newAbstract();k.tY=jsx3.Method.newAbstract();k.VT=function(h,g,j,l,f,m){if(this.type==ub.b||this.type==ub.c){j=0;l=1;}var
ka=g-h;var
Eb=ka*m;var
aa=(h+g)/2;var
Nb=Eb/(l-l*f+f);var
rb=Nb*(j-(l-1)/2)*(1-f);var
L=null;if(j>0&&f==0){var
vb=Nb*(j-1-(l-1)/2)*(1-f);L=Math.round(aa+vb+Nb/2);}else L=Math.round(aa+rb-Nb/2);var
u=Math.round(aa+rb+Nb/2)-1;return [L,u];};k.oX=function(a){if(this.type==ub.a){var
Qa=this.getRangeForField(a,ub.e);var
Wa=this.getRangeForField(a,ub.f);return this.getCombinedRange([Qa,Wa]);}else if(this.type==ub.b){return this.getStackedRangeForField(a,ub.e);}else if(this.type==ub.c){return this.getStacked100RangeForField(a,ub.e);}else{jsx3.chart.LOG.error(ub.g+this.type);return null;}};k.j9=function(r){return this.getRangeForField(r,ub.h);};k.createVector=function(){this.jsxsuper();this._drawSeries();};k._drawSeries=function(){var
rb=this.wf();var
na=this.Cl();var
la=na.length;if(la==0)return;var
Ua=this.bh();var
u=this.RE();var
kb=this.tY();if(Ua==null||u==null||kb==null)return;if(!jsx3.chart.isValueAxis(u)){jsx3.chart.LOG.error(ub.i+u.getClass());return;}var
U=this.getSeriesOverlap();var
K=this.getCategoryCoverage();var
ka=null;if(this.type==ub.c)ka=this.bg(na,ub.e);var
xa=null,ea=null;if(this.type==ub.b||this.type==ub.c){xa=new
Array(Ua.length);ea=new
Array(Ua.length);for(var
bb=0;bb<Ua.length;bb++)xa[bb]=ea[bb]=0;}for(var
bb=0;bb<la;bb++){var
La=na[bb];La.De();for(var
ca=0;ca<Ua.length;ca++){var
Ja=Ua[ca];var
fa=null,X=null;if(jsx3.chart.isValueAxis(u)){var
Eb=La.Mg(Ja);if(this.type==ub.a){fa=La.getMinValue(Ja);if(fa==null)fa=0;X=Eb;if(X==null)continue;}else if(this.type==ub.b||this.type==ub.c){var
Nb=this.type==ub.b?Eb:100*(Eb/ka[ca]);if(Nb>=0){fa=xa[ca];X=xa[ca]+Nb;xa[ca]=X;}else{fa=ea[ca]+Nb;X=ea[ca];ea[ca]=fa;}}}var
jb=u.getCoordinateForNoClip(fa);if(fa!=0)jb=jb+(u.getHorizontal()?1:-1);var
Ha=u.getCoordinateForNoClip(X);var
I=null,Qa=null;if(jsx3.chart.isValueAxis(kb)){var
aa=La.zi(Ja);if(aa==null)continue;var
za=kb.getCoordinateForNoClip(aa);var
mb=La.qk();I=za-Math.ceil(mb/2);Qa=za+Math.floor(mb/2);}else if(jsx3.chart.isCategoryAxis(kb)){var
Aa=kb.getRangeForCategory(ca);var
wa=this.VT(Aa[0],Aa[1],bb,la,U,K);I=wa[0];Qa=wa[1];}La.an(Ja,ca,jb,I,Ha,Qa);}La.updateView();rb.appendChild(La.getCanvas());}};d.getVersion=function(){return jsx3.chart.si;};});jsx3.Class.defineClass("jsx3.chart.BarChart",jsx3.chart.BCChart,null,function(e,n){n.init=function(l,p,s,d,a){this.jsxsuper(l,p,s,d,a);};n.rl=function(h){return h instanceof jsx3.chart.BarSeries;};n.getXRange=function(o){return this.oX(o);};n.getYRange=function(i){return this.j9(i);};n.RE=function(){return this.getPrimaryXAxis();};n.tY=function(){return this.getPrimaryYAxis();};});jsx3.Class.defineClass("jsx3.chart.ColumnChart",jsx3.chart.BCChart,null,function(d,a){a.init=function(c,g,j,m,k){this.jsxsuper(c,g,j,m,k);};a.rl=function(q){return q instanceof jsx3.chart.ColumnSeries;};a.getXRange=function(j){return this.j9(j);};a.getYRange=function(c){return this.oX(c);};a.RE=function(){return this.getPrimaryYAxis();};a.tY=function(){return this.getPrimaryXAxis();};});
