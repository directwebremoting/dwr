/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.CartesianChart","jsx3.chart.AreaSeries");jsx3.Class.defineClass("jsx3.chart.AreaChart",jsx3.chart.CartesianChart,null,function(r,q){var
ub={d:"type",a:"overlay",h:"bad y axis type: ",c:"stacked100",f:"unsupported Line Chart type: ",g:"getXValue",b:"stacked",e:"getYValue"};r.TYPE_OVERLAY=ub.a;r.TYPE_STACKED=ub.b;r.TYPE_STACKED100=ub.c;r.nT={overlay:1,stacked:1,stacked100:1};q.init=function(f,j,m,k,n){this.jsxsuper(f,j,m,k,n);this.type=ub.a;};q.getType=function(){return this.type;};q.setType=function(s){if(r.nT[s]){this.type=s;}else throw new
jsx3.IllegalArgumentException(ub.d,s);};q.getYRange=function(a){if(this.type==ub.a){return this.getRangeForField(a,ub.e);}else if(this.type==ub.b){return this.getStackedRangeForField(a,ub.e);}else if(this.type==ub.c){return this.getStacked100RangeForField(a,ub.e);}else{jsx3.chart.LOG.error(ub.f+this.type);return null;}};q.getXRange=function(m){return this.getRangeForField(m,ub.g);};q.createVector=function(){this.jsxsuper();this.nD();};q.nD=function(){var
Fb=this.wf();var
jb=this.Cl();var
Hb=this.bh();var
Na=this.getPrimaryXAxis();var
zb=this.getPrimaryYAxis();if(Na==null||zb==null||jb.length==0||Hb==null)return;if(!jsx3.chart.isValueAxis(zb)){jsx3.chart.LOG.error(ub.h+zb.getClass());return;}var
ba=null;if(this.type==ub.c)ba=this.bg(jb,ub.e);var
ka=null,rb=null;if(this.type==ub.b||this.type==ub.c){ka=new
Array(Hb.length);rb=new
Array(Hb.length);for(var
W=0;W<Hb.length;W++)ka[W]=rb[W]=0;}for(var
ea=0;ea<jb.length;ea++){var
kb=jb[ea];kb._n();for(var
W=0;W<Hb.length;W++){var
Z=Hb[W];var
I=null,nb=null,xb=null;var
S=kb.getYValue(Z);var
u=kb.getMinValue(Z);if(S==null)continue;if(this.type==ub.b||this.type==ub.c){if(S>=0){u=ka[W];S=ka[W]+S;ka[W]=S;}else{u=rb[W];S=rb[W]+S;rb[W]=S;}if(this.type==ub.c){S=100*(S/ba[W]);u=100*(u/ba[W]);}}if(u==null)u=0;nb=zb.getCoordinateForNoClip(S);xb=zb.getCoordinateFor(u);if(jsx3.chart.isValueAxis(Na)){var
ab=kb.getXValue(Z);if(ab!=null)I=Na.getCoordinateForNoClip(ab);}else if(jsx3.chart.isCategoryAxis(Na))I=Na.getPointForCategory(W);if(I==null)continue;kb.fm(Z,I,nb,W);kb.Bh(I,xb);}kb.updateView();Fb.appendChild(kb.getCanvas());}};q.kg=function(){return this.type==ub.b||this.type==ub.c;};q.rl=function(h){return h instanceof jsx3.chart.AreaSeries;};r.getVersion=function(){return jsx3.chart.si;};});
