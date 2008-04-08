/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.Chart");jsx3.Class.defineClass("jsx3.chart.RadialChart",jsx3.chart.Chart,null,function(m,a){a.init=function(c,g,j,d,k){this.jsxsuper(c,g,j,d,k);};a.createVector=function(){this.jsxsuper();var
ub=this.wf();var
ib=ub.getWidth();var
nb=ub.getHeight();var
Ta=ub.getPaddingDimensions();var
Ha=[Ta[3],Ta[0],ib-(Ta[3]+Ta[1]),nb-(Ta[0]+Ta[2])];var
Ma=this.Cl();for(var
sb=0;sb<Ma.length;sb++)Ma[sb].setDimensions(Ha);};m.degreesToRadians=function(n){return jsx3.util.numMod(2*Math.PI/360*(-1*n+90),2*Math.PI);};m.getVersion=function(){return jsx3.chart.si;};});
