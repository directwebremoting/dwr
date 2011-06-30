/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.RadialChart","jsx3.chart.PieSeries");jsx3.Class.defineClass("jsx3.chart.PieChart",jsx3.chart.RadialChart,null,function(n,g){var
ub={d:"jsx3.chart.Legend",a:"jsx3.chart.PieChart.defaultColoring",c:"getValue",b:"colorFunction"};g.init=function(r,c,f,q,i){this.jsxsuper(r,c,f,q,i);this.innerRadius=0;this.seriesPadding=0.1;this.totalAngle=360;this.startAngle=0;this.categoryField=null;this.colors=null;this.colorFunction=ub.a;this.seriesStroke=null;};g.getInnerRadius=function(){return this.innerRadius!=null?this.innerRadius:0;};g.setInnerRadius=function(c){this.innerRadius=c==null?null:Math.max(0,Math.min(1,c));};g.getSeriesPadding=function(){return this.seriesPadding!=null?this.seriesPadding:0.1;};g.setSeriesPadding=function(c){this.seriesPadding=c;};g.getTotalAngle=function(){return this.totalAngle!=null?this.totalAngle:360;};g.setTotalAngle=function(h){this.totalAngle=h==null?null:Math.max(0,Math.min(360,h));};g.getStartAngle=function(){return this.startAngle!=null?this.startAngle:0;};g.setStartAngle=function(m){this.startAngle=m;};g.getCategoryField=function(){return this.categoryField;};g.setCategoryField=function(c){this.categoryField=c;};g.getColors=function(){return this.colors;};g.setColors=function(j){this.colors=j;};g.getColorFunction=function(){return jsx3.chart.vc(this,ub.b);};g.setColorFunction=function(r){jsx3.chart.sj(this,ub.b,r);};g.getSeriesStroke=function(){return this.seriesStroke;};g.setSeriesStroke=function(h){this.seriesStroke=h;};g.vn=function(e,o){var
qb=this.getColors();if(qb!=null&&qb.length>0)return jsx3.vector.Fill.valueOf(qb[o%qb.length]);var
Jb=this.getColorFunction();if(Jb!=null)return Jb.call(null,e,o);return new
jsx3.vector.Fill();};g.createVector=function(){this.jsxsuper();var
_=this.wf();var
bb=this.Cl();var
Bb=this.bh();if(bb.length<1)return;if(Bb==null||Bb.length<1)return;var
Fa=bb[0].getWidth();var
va=bb[0].getHeight();var
J=Math.round(Fa/2);var
xb=Math.round(va/2);var
T=Math.floor(Math.min(Fa,va)/2);var
Pa=this.ak(bb,ub.c,true);var
K=T*(1-this.getInnerRadius())/(bb.length+(bb.length-1)*this.getSeriesPadding());var
Mb=T*this.getInnerRadius();for(var
v=0;v<bb.length;v++){var
da=bb[v];da.Ie();var
G=da.getStartAngle();if(G==null)G=this.getStartAngle();var
Ea=da.getTotalAngle();if(Ea==null)Ea=this.getTotalAngle();var
nb=Mb+K;for(var
Ja=0;Ja<Bb.length;Ja++){var
Gb=Bb[Ja];var
sa=da.getValue(Gb);if(sa==null||sa<=0){da.bf();continue;}var
Y=Ea*sa/Pa[v];da.Fe(Gb,J,xb,G,G+Y,Math.round(Mb),Math.round(nb),100*sa/Pa[v]);G=G+Y;}da.updateView();_.appendChild(da.getCanvas());Mb=Mb+K*(1+this.getSeriesPadding());}};g.kg=function(){return true;};g.rl=function(a){return a instanceof jsx3.chart.PieSeries;};g.getLegendEntryType=function(){jsx3.require(ub.d);return 2;};n.defaultColoring=function(c,m){return jsx3.chart.Chart.DEFAULT_FILLS[m%jsx3.chart.Chart.DEFAULT_FILLS.length];};n.getVersion=function(){return jsx3.chart.si;};});
