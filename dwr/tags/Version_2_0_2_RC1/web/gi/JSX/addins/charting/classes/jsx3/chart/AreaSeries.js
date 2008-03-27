/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.Series");jsx3.Class.defineClass("jsx3.chart.AreaSeries",jsx3.chart.Series,null,function(c,g){var t=jsx3.vector;var Gc=jsx3.chart;c.FORM_SEGMENT="segment";c.FORM_STEP="step";c.FORM_REVSTEP="reverseStep";c.Ae={segment:1,step:1,reverseStep:1};c.Xg=4;g.init=function(s,m){this.jsxsuper(s,m);this.xField=null;this.yField=null;this.minField=null;this.form=c.FORM_SEGMENT;this.pointRadius=null;this.pointRenderer=null;this.pointFill=null;this.pointStroke=null;this.pointGradient=null;this.setTooltipFunction("jsx3.chart.AreaSeries.tooltip");};g.getXField=function(){return this.xField;};g.setXField=function(s){this.xField=s;};g.getYField=function(){return this.yField;};g.setYField=function(j){this.yField=j;};g.getMinField=function(){return this.minField;};g.setMinField=function(b){this.minField=b;};g.getForm=function(){return this.form;};g.setForm=function(p){if(c.Ae[p]){this.form=p;}else{throw new jsx3.IllegalArgumentException("form",p);}};g.getPointRadius=function(){return this.pointRadius!=null?this.pointRadius:c.Xg;};g.setPointRadius=function(q){this.pointRadius=q;};g.getPointRenderer=function(){return this.d8("pointRenderer");};g.setPointRenderer=function(h){this.uR("pointRenderer",h);};g.getPointFill=function(){return this.pointFill;};g.setPointFill=function(q){this.pointFill=q;};g.getPointStroke=function(){return this.pointStroke;};g.setPointStroke=function(b){this.pointStroke=b;};g.getPointGradient=function(){return this.pointGradient;};g.setPointGradient=function(l){this.pointGradient=l;};g.B9=function(){var Cc=this.Q0("LJ");if(Cc==null)this.mG();return this.Q0("LJ");};g.b8=function(){var x=this.Q0("PY");if(x==null)this.mG();return this.Q0("PY");};g.mG=function(){var bb=this.qT();var Mc=t.Fill.valueOf(this.getPointFill());if(Mc==null&&bb!=null)Mc=new t.Fill(bb.getColor());if(Mc!=null)Mc=Gc.addGradient(Mc,this.pointGradient);var u=t.Stroke.valueOf(this.getPointStroke());this.xI("LJ",Mc);this.xI("PY",u);};g.getXValue=function(l){if(this.xField)return Gc.asNumber(l.getAttribute(this.xField));return null;};g.getYValue=function(p){if(this.yField)return Gc.asNumber(p.getAttribute(this.yField));return null;};g.getMinValue=function(s){if(this.minField)return Gc.asNumber(s.getAttribute(this.minField));return null;};g.PM=function(j,r,q,a){var G=this.Q0("ZN");if(G==null){G=[];this.xI("ZN",G);}G.push([j,r,q,a]);};g.YZ=function(m,l){var yb=this.Q0("vJ");if(yb==null){yb=[];this.xI("vJ",yb);}yb.push([m,l]);};g.BV=function(){var fc=this.Q0("ZN");if(fc!=null)fc.splice(0,fc.length);var u=this.Q0("vJ");if(u!=null)u.splice(0,u.length);};g.updateView=function(){this.jsxsuper();var Ob=this.l5();var yb=null,P=null;var F=this.D7();if(F!=null){yb=new t.Shape(null,0,0,Ob.getWidth(),Ob.getHeight());yb.setId(this.getId()+"_fill");Ob.appendChild(yb);yb.setFill(F);}var _=this.qT();if(_!=null){P=new t.Shape(null,0,0,Ob.getWidth(),Ob.getHeight());P.setId(this.getId()+"_stroke");Ob.appendChild(P);P.setStroke(_);}this.mG();var sb=this.Q0("ZN");var sc=this.Q0("vJ");if(sb==null||sb.length==0||sc==null||sc.length==0)return;var ub=this.getPointRenderer();var ib=this.getPointRadius();var R=this.getTooltipFunction();var D=this.B9();var Ic=this.b8();var pc=this.getColorFunction();this.A3(yb);for(var N=0;N<sb.length;N++){var wc=sb[N][0];var Lb=sb[N][1];var zb=sb[N][2];var T=sb[N][3];if(N==0){if(yb!=null)yb.X6(Lb,zb);if(P!=null)P.X6(Lb,zb);}else{var uc=sb[N-1][1];var O=sb[N-1][2];if(this.form==c.FORM_SEGMENT){if(yb!=null)yb.Z7(Lb,zb);if(P!=null)P.Z7(Lb,zb);}else{if(this.form==c.FORM_STEP){if(yb!=null)yb.Z7(Lb,O).Z7(Lb,zb);if(P!=null)P.Z7(Lb,O).Z7(Lb,zb);}else{if(this.form==c.FORM_REVSTEP){if(yb!=null)yb.Z7(uc,zb).Z7(Lb,zb);if(P!=null)P.Z7(uc,zb).Z7(Lb,zb);}else{Gc.LOG.error("bad AreaSeries form: "+this.form);}}}}if(ub!=null){var I=pc!=null?pc.call(null,wc,T):D;var Q=ub.render(Lb-ib,zb-ib,Lb+ib,zb+ib,I,Ic);Q.setId(this.getId()+"_p"+N);this.A3(Q,N,wc.getAttribute("jsxid"));Ob.appendChild(Q);if(R!=null)Q.setToolTip(R.call(null,this,wc));}}for(var N=sc.length-1;N>=0;N--){var Lb=sc[N][0];var zb=sc[N][1];if(N==sc.length-1){yb.Z7(Lb,zb);}else{var uc=sc[N+1][0];var O=sc[N+1][1];if(this.form==c.FORM_SEGMENT){yb.Z7(Lb,zb);}else{if(this.form==c.FORM_STEP){yb.Z7(uc,zb).Z7(Lb,zb);}else{if(this.form==c.FORM_REVSTEP){yb.Z7(Lb,O).Z7(Lb,zb);}else{Gc.LOG.error("bad AreaSeries form: "+this.form);}}}}}yb.w9();};c.tooltip=function(d,e){var wc=d.getXValue(e);var gc=d.getYValue(e);var db=d.getMinValue(e);var hc=db!=null?"{"+db+","+gc+"}":gc;if(wc!=null)hc=hc+(", x = "+wc);return hc;};g.toString=function(){return "[AreaSeries '"+this.getName()+"']";};c.getVersion=function(){return Gc.q2;};});
