/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.Axis");jsx3.Class.defineClass("jsx3.chart.LogarithmicAxis",jsx3.chart.Axis,null,function(b,m){var
ub={d:" in chart ",a:"Zg",c:"no range for axis ",f:" is all negative ",b:"re",e:"range of axis "};b.MAX_TICKS=200;b.WM=1;b.PH=0;b.by=2;m.init=function(p,k,j){this.jsxsuper(p,k,j);this.autoAdjust=jsx3.Boolean.TRUE;this.baseAtZero=jsx3.Boolean.TRUE;this.showNegativeValues=jsx3.Boolean.FALSE;this.minExponent=null;this.maxExponent=null;this.base=10;this.majorDivisions=1;this.Ho(ub.a,b.PH);this.Ho(ub.b,b.by);};m.getAutoAdjust=function(){return this.autoAdjust;};m.setAutoAdjust=function(c){this.autoAdjust=c;};m.getBaseAtZero=function(){return this.baseAtZero;};m.setBaseAtZero=function(n){this.baseAtZero=n;};m.getShowNegativeValues=function(){return this.showNegativeValues;};m.setShowNegativeValues=function(f){this.showNegativeValues=f;};m.getMinExponent=function(){return this.minExponent;};m.setMinExponent=function(o){this.minExponent=o;};m.getMaxExponent=function(){return this.maxExponent;};m.setMaxExponent=function(e){this.maxExponent=e;};m.getBase=function(){return this.base;};m.setBase=function(n){this.base=n;};m.getMajorDivisions=function(){return this.majorDivisions;};m.setMajorDivisions=function(f){this.majorDivisions=f;};m.fl=function(){var
Aa=false;if(this.autoAdjust)Aa=this.Zp();if(!Aa){this.Ho(ub.a,this.minExponent!=null?this.minExponent:b.PH);this.Ho(ub.b,this.maxExponent!=null?this.maxExponent:b.by);}};m.Zp=function(){var
Eb=this.getChart();if(Eb==null)return false;var
sa=Eb.getRangeForAxis(this);if(sa==null){jsx3.chart.LOG.debug(ub.c+this+ub.d+Eb);return false;}var
O=Math.max(0,sa[0]);var
da=Math.max(0,sa[1]);if(da==0){jsx3.chart.LOG.debug(ub.e+this+ub.f+Eb);return false;}var
Ba=null,Ra=null;if(this.minExponent!=null){Ba=this.minExponent;}else if(this.baseAtZero)Ba=0;if(this.maxExponent!=null)Ra=this.maxExponent;O=O*b.WM;da=da*b.WM;if(Ba==null)if(O==0)Ba=0;else Ba=Math.floor(Math.log(O)/Math.log(this.base));if(Ra==null)Ra=Math.ceil(Math.log(da)/Math.log(this.base));this.Ho(ub.a,Ba);this.Ho(ub.b,Ra);return true;};m.Xj=function(f){var
x=this.pj(ub.a);var
Fb=Math.floor(x+f/this.majorDivisions);var
M=f%this.majorDivisions;if(M==0){return Math.pow(this.base,Fb);}else{var
Ta=Math.pow(this.base,Fb);var
sb=Math.pow(this.base,Fb+1);return Ta+M*(sb-Ta)/this.majorDivisions;}};m.Hf=function(){var
Ja=this.pj(ub.a);var
Za=this.pj(ub.b);var
sa=[];var
Ga=0;for(var
ra=Ja;ra<=Za&&Ga<b.MAX_TICKS;ra++){var
db=Math.pow(this.base,ra);if(ra>Ja){var
Va=Math.pow(this.base,ra-1);for(var
Ra=1;Ra<this.majorDivisions;Ra++){var
Pa=Va+Ra*(db-Va)/this.majorDivisions;sa.push(this.getCoordinateFor(Pa));Ga++;}}sa.push(this.getCoordinateFor(db));Ga++;}return sa;};m.Gl=function(j,f){var
cb=[];if(f==0){return [];}else if(f==j.length){return [];}else{var
L=this.Xj(f-1);var
Cb=this.Xj(f);for(var
ra=1;ra<this.minorTickDivisions;ra++){var
kb=L+ra*(Cb-L)/this.minorTickDivisions;cb.push(this.getCoordinateFor(kb));}}return cb;};m.mo=function(){return false;};m.getCoordinateFor=function(p){var
Ua=this.pj(ub.a);var
sa=this.pj(ub.b);var
cb=null;if(p<=0){cb=0;}else{var
ea=Math.log(p)/Math.log(this.base);if(ea<Ua){cb=0;}else if(ea>sa){cb=this.length;}else cb=Math.round(this.length*(ea-Ua)/(sa-Ua));}return this.horizontal?cb:this.length-cb;};m.getCoordinateForNoClip=function(j){var
Pa=this.pj(ub.a);var
yb=this.pj(ub.b);var
da=null;if(j<=0){da=-1;}else{var
Na=Math.log(j)/Math.log(this.base);da=Math.round(this.length*(Na-Pa)/(yb-Pa));}return this.horizontal?da:this.length-da;};b.getVersion=function(){return jsx3.chart.si;};});
