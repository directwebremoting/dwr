/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.Series");jsx3.Class.defineClass("jsx3.chart.PieSeries",jsx3.chart.Series,null,function(i,h){var
ub={d:"un",i:"left",k:"%",a:"bottom",h:"right",c:"labelPlacement",f:"jsxid",j:", ",g:"top",b:"jsx3.chart.PieSeries.tooltip",e:"_s"};var
G=jsx3.vector;i.LP={top:1,right:1,bottom:1,left:1};h.init=function(q,e){this.jsxsuper(q,e);this.field=null;this.totalAngle=null;this.startAngle=null;this.colors=null;this.colorFunction=null;this.stroke=null;this.labelPlacement=ub.a;this.labelOffset=10;this.setTooltipFunction(ub.b);};h.getTotalAngle=function(){return this.totalAngle;};h.setTotalAngle=function(l){this.totalAngle=l==null?null:Math.max(0,Math.min(360,l));};h.getStartAngle=function(){return this.startAngle;};h.setStartAngle=function(a){this.startAngle=a==null?null:jsx3.util.numMod(a,360);};h.getField=function(){return this.field;};h.setField=function(q){this.field=q;};h.getValue=function(q){if(this.field)return jsx3.chart.asNumber(q.getAttribute(this.field));return null;};h.getColors=function(){return this.colors;};h.setColors=function(a){this.colors=a;};h.getLabelPlacement=function(){return this.labelPlacement!=null?this.labelPlacement:ub.a;};h.setLabelPlacement=function(g){if(i.LP[g]){this.labelPlacement=g;}else throw new
jsx3.IllegalArgumentException(ub.c,g);};h.getLabelOffset=function(){return this.labelOffset!=null?this.labelOffset:0;};h.setLabelOffset=function(d){this.labelOffset=Math.round(d);};h.vn=function(m,d){var
ia=this.getColors();if(ia!=null&&ia.length>0)return G.Fill.valueOf(ia[d%ia.length]);var
D=this.getColorFunction();if(D!=null)return D.call(null,m,d);var
x=this.getChart();if(x!=null)return x.vn(m,d);return new
G.Fill();};h.Ys=function(s){if(this.stroke)return G.Stroke.valueOf(this.stroke);var
Za=this.getChart();if(Za!=null){var
sb=Za.getSeriesStroke();if(sb)return G.Stroke.valueOf(sb);}return null;};h.Fe=function(m,e,d,l,c,q,r,f){var
H=this.pj(ub.d);if(H==null){H=[];this.Ho(ub.d,H);}H.push([m,e,d,l,c,q,r,f]);};h.bf=function(){this.Fe(null);};h.Ie=function(){var
T=this.pj(ub.d);if(T!=null)T.splice(0,T.length);};h.updateView=function(){this.jsxsuper();var
ob=this.getCanvas();var
hb=ob.getWidth();var
Ya=ob.getHeight();var
ba=this.pj(ub.d);if(ba==null)return;var
N=this.getTooltipFunction();for(var
Y=0;Y<ba.length;Y++){var
ha=ba[Y][0];if(ha==null)continue;var
pb=ba[Y][1];var
S=ba[Y][2];var
bb=ba[Y][3];var
xb=ba[Y][4];var
Qa=ba[Y][5];var
jb=ba[Y][6];var
U=ba[Y][7];var
tb=new
G.Shape(null,0,0,hb,Ya);tb.setId(this.getId()+ub.e+Y);ob.appendChild(tb);tb.setFill(this.vn(ha,Y));tb.setStroke(this.Ys(Y));this.tg(tb,Y,ha.getAttribute(ub.f));var
F=G.degreesToRadians(xb);var
C=G.degreesToRadians(bb);var
kb=Math.round(jb*Math.cos(F))+pb;var
Va=Math.round(-1*jb*Math.sin(F))+S;var
da=Math.round(jb*Math.cos(C))+pb;var
D=Math.round(-1*jb*Math.sin(C))+S;var
Mb=kb==da&&Va==D&&xb-bb>180;tb.pathMoveTo(kb,Va);if(Mb||kb!=da||Va!=D)tb.pathArcTo(pb,S,jb,jb,kb,Va,da,D,false);if(Qa>0){var
Ja=Math.round(Qa*Math.cos(F))+pb;var
fb=Math.round(-1*Qa*Math.sin(F))+S;var
Na=Math.round(Qa*Math.cos(C))+pb;var
Ga=Math.round(-1*Qa*Math.sin(C))+S;tb.pathLineTo(Na,Ga);if(Mb||Ja!=Na||fb!=Ga)tb.pathArcTo(pb,S,Qa,Qa,Na,Ga,Ja,fb,true);}else if(!Mb)tb.pathLineTo(pb,S);tb.pathClose();if(N!=null)tb.setToolTip(N.call(null,this,ha,U));}var
H=this.getLabel();if(H!=null&&H.getDisplay()!=jsx3.gui.Block.DISPLAYNONE){var
Gb=[0,0,hb,Ya];if(ba.length>0)Gb=[ba[0][1]-ba[0][6],ba[0][2]-ba[0][6],ba[0][1]+ba[0][6],ba[0][2]+ba[0][6]];var
ya=H.getPreferredWidth();var
qb=H.getPreferredHeight();var
ra=0,z=0;if(this.labelPlacement==ub.g){ra=Math.round(hb/2-ya/2);z=Gb[1]-this.getLabelOffset()-qb;}else if(this.labelPlacement==ub.h){ra=Gb[2]+this.getLabelOffset();z=Math.round(Ya/2-qb/2);}else if(this.labelPlacement==ub.i){ra=Gb[0]-this.getLabelOffset()-ya;z=Math.round(Ya/2-qb/2);}else{ra=Math.round(hb/2-ya/2);z=Gb[3]+this.getLabelOffset();}H.setDimensions(ra,z,ya,qb);H.setText(this.getSeriesName());H.updateView();ob.appendChild(H.getCanvas());}};i.tooltip=function(k,o,d){var
ha=k.getValue(o);return ha+ub.j+Math.round(d*10)/10+ub.k;};i.getVersion=function(){return jsx3.chart.si;};});
