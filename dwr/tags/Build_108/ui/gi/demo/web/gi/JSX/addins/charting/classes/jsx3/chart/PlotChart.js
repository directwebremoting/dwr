/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.CartesianChart","jsx3.chart.PointSeries");jsx3.Class.defineClass("jsx3.chart.PlotChart",jsx3.chart.CartesianChart,null,function(p,d){var
ub={d:"magnitudeMethod",a:"radius",h:"bad y axis type: ",c:"area",f:"getYValue",g:"bad x axis type: ",b:"diameter",e:"getXValue"};p.MAG_RADIUS=ub.a;p.MAG_DIAMETER=ub.b;p.MAG_AREA=ub.c;p.DEFAULT_MAX_POINT_RADIUS=30;p.GD={radius:1,diameter:1,area:1};d.init=function(k,o,r,e,s){this.jsxsuper(k,o,r,e,s);this.maxPointRadius=p.DEFAULT_MAX_POINT_RADIUS;this.magnitudeMethod=ub.a;};d.getMaxPointRadius=function(){return this.maxPointRadius!=null?this.maxPointRadius:Number.POSITIVE_INFINITY;};d.setMaxPointRadius=function(k){this.maxPointRadius=k;};d.getMagnitudeMethod=function(){return this.magnitudeMethod;};d.setMagnitudeMethod=function(c){if(p.GD[c]){this.magnitudeMethod=c;}else throw new
jsx3.IllegalArgumentException(ub.d,c);};d.getXRange=function(f){return this.getRangeForField(f,ub.e);};d.getYRange=function(q){return this.getRangeForField(q,ub.f);};d.createVector=function(){this.jsxsuper();this.nD();};d.nD=function(){var
ob=this.wf();var
Ta=this.Cl();var
Ia=this.bh();var
Ra=this.getPrimaryXAxis();var
jb=this.getPrimaryYAxis();if(Ra==null||jb==null||Ta.length==0||Ia==null)return;if(!jsx3.chart.isValueAxis(Ra)){jsx3.chart.LOG.error(ub.g+Ra.getClass());return;}if(!jsx3.chart.isValueAxis(jb)){jsx3.chart.LOG.error(ub.h+jb.getClass());return;}for(var
yb=0;yb<Ta.length;yb++){var
Hb=Ta[yb];Hb._n();for(var
lb=0;lb<Ia.length;lb++){var
Pa=Ia[lb];var
U=Hb.getXValue(Pa);var
t=Hb.getYValue(Pa);if(U==null||t==null)continue;U=Ra.getCoordinateForNoClip(U);t=jb.getCoordinateForNoClip(t);if(Hb instanceof jsx3.chart.PointSeries){Hb.fm(Pa,lb,U,t);}else{var
fa=Hb.getMagnitudeValue(Pa);if(fa!=null)Hb.fm(Pa,lb,U,t,fa);}}Hb.updateView();ob.appendChild(Hb.getCanvas());}};d.rl=function(s){return s instanceof jsx3.chart.PlotSeries;};p.getVersion=function(){return jsx3.chart.si;};});
