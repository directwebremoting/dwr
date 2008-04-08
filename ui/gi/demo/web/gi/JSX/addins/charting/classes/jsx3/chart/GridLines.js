/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.chart.ChartComponent");jsx3.Class.defineClass("jsx3.chart.GridLines",jsx3.chart.ChartComponent,null,function(a,i){var
xb=jsx3.vector;var
V=xb.Stroke;i.init=function(r,c,f,q,g){this.jsxsuper(r);this.setDimensions(c,f,q,g);this.useXPrimary=jsx3.Boolean.TRUE;this.useYPrimary=jsx3.Boolean.TRUE;this.horizontalAbove=jsx3.Boolean.TRUE;this.inForeground=jsx3.Boolean.FALSE;this.borderStroke=null;this.fillV=null;this.strokeMajorV=null;this.strokeMinorV=null;this.fillH=null;this.strokeMajorH=null;this.strokeMinorH=null;};i.getUseXPrimary=function(){return this.useXPrimary;};i.setUseXPrimary=function(l){this.useXPrimary=l;};i.getUseYPrimary=function(){return this.useYPrimary;};i.setUseYPrimary=function(c){this.useYPrimary=c;};i.getHorizontalAbove=function(){return this.horizontalAbove;};i.setHorizontalAbove=function(n){this.horizontalAbove=n;};i.getInForeground=function(){return this.inForeground;};i.setInForeground=function(o){this.inForeground=o;};i.getBorderStroke=function(){return this.borderStroke;};i.setBorderStroke=function(m){this.borderStroke=m;};i.getFillV=function(){return this.fillV;};i.setFillV=function(r){this.fillV=r;};i.getStrokeMajorV=function(){return this.strokeMajorV;};i.setStrokeMajorV=function(l){this.strokeMajorV=l;};i.getStrokeMinorV=function(){return this.strokeMinorV;};i.setStrokeMinorV=function(n){this.strokeMinorV=n;};i.getFillH=function(){return this.fillH;};i.setFillH=function(e){this.fillH=e;};i.getStrokeMajorH=function(){return this.strokeMajorH;};i.setStrokeMajorH=function(r){this.strokeMajorH=r;};i.getStrokeMinorH=function(){return this.strokeMinorH;};i.setStrokeMinorH=function(q){this.strokeMinorH=q;};i.getXAxis=function(){var
Y=this.getChart();if(Y==null)return null;return this.useXPrimary?Y.getPrimaryXAxis():Y.getSecondaryXAxis();};i.getYAxis=function(){var
sb=this.getChart();if(sb==null)return null;return this.useYPrimary?sb.getPrimaryYAxis():sb.getSecondaryYAxis();};i.updateView=function(){this.jsxsuper();var
M=this.getCanvas();this.tg();var
T=new
xb.Group();T.setZIndex(2);M.appendChild(T);var
I=new
xb.Group();I.setZIndex(this.horizontalAbove?3:1);M.appendChild(I);var
Ya=this.getWidth();var
Fb=this.getHeight();if(this.borderStroke){var
ma=V.valueOf(this.borderStroke);var
tb=new
xb.Rectangle(0,0,Ya,Fb);tb.setZIndex(10);tb.setStroke(ma);M.appendChild(tb);}this.TQ(T,Ya,Fb,this.getXAxis(),this.fillV,this.strokeMajorV,this.strokeMinorV,false);this.TQ(I,Ya,Fb,this.getYAxis(),this.fillH,this.strokeMajorH,this.strokeMinorH,true);};i.TQ=function(o,g,c,q,l,n,j,e){o.setDimensions(0,0,g,c);var
tb=this._s(l);if(q!=null){var
T=q.Hf();if(T.length==0||tb==1)this.kY(o,0,0,g,c,this.TL(l,0));if(T.length>0){if(tb>1)this.z4(o,q,T,l,null,e);this.MU(o,q,T,n,null,e);this.lF(o,q,T,j,e);}}else this.kY(o,0,0,g,c,this.TL(l,0));};i.z4=function(j,c,m,g,h,n){if(h==null)h=new
Array(this._s(g));if(h.length==0)return;var
Kb=j.getHeight();var
Ha=j.getWidth();var
O=n?Kb:Ha;var
ta=n?Ha:Kb;for(var
Va=0;Va<=m.length;Va++){var
B=h[Va%h.length];if(B==null){B=h[Va%h.length]=new
xb.RectangleGroup(0,0,Ha,Kb);B.setFill(this.TL(g,Va));j.appendChild(B);}if(Va==m.length){if(m[Va-1]<O)this.Uv(B,m[Va-1],0,O,ta,n);}else if(Va==0){if(m[Va]>0)this.Uv(B,0,0,m[Va],ta,n);}else this.Uv(B,m[Va-1],0,m[Va],ta,n);}};i.Uv=function(q,e,l,d,k,c){if(c)q.addRectangle(l,e,k,d);else q.addRectangle(e,l,d,k);};i.MU=function(j,c,m,b,h,n){if(h==null)h=new
Array(this._s(b));if(h.length==0)return;var
Za=j.getHeight();var
Ga=j.getWidth();for(var
Ab=0;Ab<m.length;Ab++){var
jb=h[Ab%h.length];if(jb==null){jb=h[Ab%h.length]=new
xb.LineGroup(0,0,Ga,Za);jb.setStroke(this.ZH(b,Ab));j.appendChild(jb);}if(n)jb.addRelativeLine(0,m[Ab],Ga,0);else jb.addRelativeLine(m[Ab],0,0,Za);}};i.lF=function(h,e,k,s,l){var
Ta=this._s(s);if(Ta==0)return;var
da=new
Array(Ta);for(var
pb=0;pb<k.length;pb++){var
Ja=e.Gl(k,pb);this.MU(h,e,Ja,s,da,l);}};i.kY=function(q,m,e,b,l,d,s){if(d!=null){var
Q=new
xb.Rectangle(m,e,b,l);if(s!=null)Q.setZIndex(s);Q.setFill(d);q.appendChild(Q);}};i.TL=function(j,m){if(j==null)return null;if(j instanceof Array){if(j.length>0){return xb.Fill.valueOf(j[m%j.length]);}else return null;}else return xb.Fill.valueOf(j);};i.ZH=function(d,s){if(d==null)return null;if(d instanceof Array){if(d.length>0){return V.valueOf(d[s%d.length]);}else return null;}else return V.valueOf(d);};i._s=function(k){if(k==null)return 0;return k instanceof Array?k.length:1;};i.onSetChild=function(){return false;};i.onSetParent=function(n){return jsx3.chart.Chart&&n instanceof jsx3.chart.Chart;};a.getVersion=function(){return jsx3.chart.si;};});
