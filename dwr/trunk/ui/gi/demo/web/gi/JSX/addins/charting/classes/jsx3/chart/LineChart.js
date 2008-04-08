/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.CartesianChart","jsx3.chart.LineSeries");jsx3.Class.defineClass("jsx3.chart.LineChart",jsx3.chart.CartesianChart,null,function(e,n){var
ub={d:"type",a:"overlay",h:"bad y axis type: ",c:"stacked100",f:"unsupported Line Chart type: ",g:"getXValue",b:"stacked",e:"getYValue"};e.TYPE_OVERLAY=ub.a;e.TYPE_STACKED=ub.b;e.TYPE_STACKED100=ub.c;e.nT={overlay:1,stacked:1,stacked100:1};n.init=function(f,j,m,k,l){this.jsxsuper(f,j,m,k,l);this.type=ub.a;};n.getType=function(){return this.type;};n.setType=function(s){if(e.nT[s]){this.type=s;}else throw new
jsx3.IllegalArgumentException(ub.d,s);};n.getYRange=function(a){if(this.type==ub.a){return this.getRangeForField(a,ub.e);}else if(this.type==ub.b){return this.getStackedRangeForField(a,ub.e);}else if(this.type==ub.c){return this.getStacked100RangeForField(a,ub.e);}else{jsx3.chart.LOG.error(ub.f+this.type);return null;}};n.getXRange=function(m){return this.getRangeForField(m,ub.g);};n.createVector=function(){this.jsxsuper();this._drawSeries();};n._drawSeries=function(){var
T=this.wf();var
x=this.Cl();var
wb=this.bh();var
tb=this.getPrimaryXAxis();var
Y=this.getPrimaryYAxis();if(tb==null||Y==null||x.length==0||wb==null)return;if(!jsx3.chart.isValueAxis(Y)){jsx3.chart.LOG.error(ub.h+Y.getClass());return;}var
xa=null;if(this.type==ub.c)xa=this.bg(x,ub.e);var
v=null,gb=null;if(this.type==ub.b||this.type==ub.c){v=new
Array(wb.length);gb=new
Array(wb.length);for(var
bb=0;bb<wb.length;bb++)v[bb]=gb[bb]=0;}for(var
Na=0;Na<x.length;Na++){var
Ib=x[Na];Ib.clear();for(var
bb=0;bb<wb.length;bb++){var
ja=wb[bb];var
jb=null,cb=null;var
y=Ib.getYValue(ja);if(y!=null&&(this.type==ub.b||this.type==ub.c)){if(y>=0)y=v[bb]=v[bb]+y;else y=gb[bb]=gb[bb]+y;if(this.type==ub.c)y=100*(y/xa[bb]);}if(y!=null)cb=Y.getCoordinateForNoClip(y);if(jsx3.chart.isValueAxis(tb)){var
Ma=Ib.getXValue(ja);if(Ma!=null)jb=tb.getCoordinateForNoClip(Ma);}else if(jsx3.chart.isCategoryAxis(tb))jb=tb.getPointForCategory(bb);if(jb!=null&&cb!=null)Ib.fm(ja,jb,cb,bb);else Ib.am(ja,jb,cb,bb);}Ib.updateView();T.appendChild(Ib.getCanvas());}};n.kg=function(){return this.type==ub.b||this.type==ub.c;};n.rl=function(p){return p instanceof jsx3.chart.LineSeries;};e.getVersion=function(){return jsx3.chart.si;};});
