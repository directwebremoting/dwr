/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.Axis");jsx3.Class.defineClass("jsx3.chart.CategoryAxis",jsx3.chart.Axis,null,function(c,p){var
ub={d:"h6",a:"aligned",c:"av",f:"gn",b:"between",e:"tickAlignment"};c.TICKS_ALIGNED=ub.a;c.TICKS_BETWEEN=ub.b;c.MAX_TICKS=200;c.BG={aligned:1,between:1};p.init=function(i,r,q){this.jsxsuper(i,r,q);this.tickAlignment=ub.b;this.categoryField=null;this.paddingLow=null;this.paddingHigh=null;this.Ho(ub.c,0);this.Ho(ub.d,0);};p.getTickAlignment=function(){return this.tickAlignment;};p.setTickAlignment=function(l){if(c.BG[l]){this.tickAlignment=l;}else throw new
jsx3.IllegalArgumentException(ub.e,l);};p.getCategoryField=function(){return this.categoryField;};p.setCategoryField=function(m){this.categoryField=m;};p.getPaddingLow=function(){return this.paddingLow!=null?this.paddingLow:0;};p.setPaddingLow=function(h){this.paddingLow=h;};p.getPaddingHigh=function(){return this.paddingHigh!=null?this.paddingHigh:0;};p.setPaddingHigh=function(r){this.paddingHigh=r;};p.fl=function(){this.Ll(ub.f);var
da=this.getChart();if(da==null){this.Ho(ub.c,0);this.Ho(ub.d,0);}else{var
tb=da.pe(this,true);var
ib=da.bh();this.Ho(ub.d,tb.length);this.Ho(ub.c,ib!=null?ib.length:0);}};p.Hf=function(){var
nb=this.pj(ub.f);if(nb!=null)return nb;var
B=this.pj(ub.c);nb=[];if(B<1)return nb;var
ga=this.getPaddingLow();var
Va=this.getPaddingHigh();var
x=this.tickAlignment==ub.b?B+1:B;var
La=x-1;var
fb=La+ga+Va;var
pb=this.length/fb;var
C=ga*pb;for(var
Qa=0;Qa<x&&Qa<c.MAX_TICKS;Qa++)nb.push(Math.round(C+Qa*pb));this.Ho(ub.f,nb);return nb;};p.se=function(){var
B=this.pj(ub.c);if(this.tickAlignment==ub.b){var
Ab=this.Hf();var
Xa=[];for(var
va=0;va<B;va++)Xa[va]=Math.round((Ab[va]+Ab[va+1])/2);return Xa;}else return this.Hf();};p.Xj=function(b){var
Pa=b;var
z=this.getChart();if(this.categoryField&&z!=null){var
ab=z.bh();if(ab!=null){var
ga=ab[b];if(ga!=null)Pa=ga.getAttribute([this.categoryField]);}}return Pa;};p.mo=function(){return false;};p.getRangeForCategory=function(j){var
_=this.Hf();if(this.tickAlignment==ub.b){if(j<0||j>=_.length-1)return null;else return [_[j],_[j+1]];}else{if(j<0||j>=_.length||_.length<2)return null;var
Xa=j==0?_[1]-_[0]:_[j]-_[j-1];return [Math.round(_[j]-Xa/2),Math.round(_[j]+Xa/2)];}};p.getPointForCategory=function(n){var
Aa=this.Hf();if(this.tickAlignment==ub.b){if(n<0||n>=Aa.length-1)return null;else return Math.round((Aa[n]+Aa[n+1])/2);}else return Aa[n];};c.getVersion=function(){return jsx3.chart.si;};});
