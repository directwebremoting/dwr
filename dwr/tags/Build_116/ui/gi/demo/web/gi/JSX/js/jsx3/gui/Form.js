/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.Class.defineInterface("jsx3.gui.Form",null,function(p,c){var
ub={o:"</xsl:when>",d:";",k:"3.00.00",r:"</xsl:choose>",c:"background-color:",h:"pointer",p:"<xsl:otherwise>",g:' disabled="disabled" ',l:"none",q:"</xsl:otherwise>",b:"#d8d8e5",i:"default",m:"Not implemented.",a:"#a8a8b5",f:"color:",j:"cursor:",n:"<xsl:choose xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:when test=\"@jsxnomask='1'\"></xsl:when><xsl:when test=\"@jsxdisabled='1'\">",e:""};var
kb=jsx3.gui.Event;p.DEFAULTDISABLEDCOLOR=ub.a;p.DEFAULTDISABLEDBACKGROUNDCOLOR=ub.b;p.STATEINVALID=0;p.STATEVALID=1;p.STATEDISABLED=0;p.STATEENABLED=1;p.OPTIONAL=0;p.REQUIRED=1;c.doKeyBinding=function(a,h){var
ha=jsx3.gui.HotKey.valueOf(h,a);return this.uq().registerHotKey(ha);};c.uq=function(){var
zb=jsx3.gui.Window!=null;var
Jb=jsx3.gui.Dialog!=null;var
W=this;while(W!=null){if(zb&&W instanceof jsx3.gui.Window)return W.getRootBlock();if(Jb&&W instanceof jsx3.gui.Dialog)return W;var
ja=W.getParent();if(ja==null)return W.getServer();W=ja;}return null;};c.getKeyBinding=function(){return this.jsxkeycode==null?null:this.jsxkeycode;};c.setKeyBinding=function(k){this.jsxkeycode=k;return this;};c.getDisabledBackgroundColor=function(){return this.jsxdisabledbgcolor;};c.setDisabledBackgroundColor=function(r){this.jsxdisabledbgcolor=r;return this;};c.zf=function(){var
v=this.getEnabled()!=0?this.getBackgroundColor():this.getDisabledBackgroundColor();return v?ub.c+v+ub.d:ub.e;};c.getDisabledColor=function(){return this.jsxdisabledcolor;};c.setDisabledColor=function(e){this.jsxdisabledcolor=e;return this;};c.getEnabled=function(){return this.jsxenabled==null?1:this.jsxenabled;};c.getValue=function(){return this.jsxvalue;};c.setValue=function(d){this.jsxvalue=d;return this;};c.setEnabled=function(k,g){if(this.jsxenabled!=k){this.jsxenabled=k;if(g)this.repaint();}return this;};c.eh=function(){if(this.getEnabled()!=0){var
Ta=this.getColor();return Ta?ub.f+Ta+ub.d:ub.e;}else return ub.f+(this.getDisabledColor()?this.getDisabledColor():p.DEFAULTDISABLEDCOLOR)+ub.d;};c.wn=function(){return this.getEnabled()==1?ub.e:ub.g;};c.Fo=function(){return jsx3.gui.Block.prototype.Fo.call(this,this.getIndex()||Number(0));};c.Xe=function(e){var
Da=this.getCursor();if(!Da&&e)Da=this.getEnabled()==1?ub.h:ub.i;return Da?ub.j+Da+ub.d:ub.e;};c.getRequired=function(){return this.jsxrequired==null?0:this.jsxrequired;};c.setRequired=function(f){this.jsxrequired=f;return this;};c.getValidationState=function(){return this._jsxuV==null?1:this._jsxuV;};c.setValidationState=function(f){this._jsxuV=f;return this;};c.doValidate=jsx3.Method.newAbstract();c.doReset=function(){this.setValidationState(1);return this;};p.validate=function(m,h){var
zb=m.getDescendantsOfType(jsx3.gui.Form);if(m.instanceOf(jsx3.gui.Form))zb.unshift(m);var
Ja=1;for(var
La=0;La<zb.length;La++){var
V=zb[La].doValidate();if(h)h(zb[La],V);if(V!=1)Ja=V;}return Ja;};p.reset=function(b){var
lb=b.getDescendantsOfType(jsx3.gui.Form);if(b.instanceOf(jsx3.gui.Form))lb.unshift(b);for(var
ga=0;ga<lb.length;ga++)lb[ga].doReset();};p.getVersion=function(){return ub.k;};c.emInit=function(o){if(this.emGetType()==jsx3.gui.Matrix.EditMask.NORMAL){this.setRelativePosition(0,true);this.setDisplay(ub.l,true);}this._jsxo1=this.getWidth();this._jsxGN=this.getHeight();};c.emGetType=function(){return jsx3.gui.Matrix.EditMask.NORMAL;};c.emPaintTemplate=function(){throw new
jsx3.Exception(ub.m);};c.Tn=function(r,g){return ub.n+g+ub.o+ub.p+r+ub.q+ub.r;};c.emBeginEdit=function(n,a,j,s,k,r,g){if(this.emGetType()==jsx3.gui.Matrix.EditMask.NORMAL){this.setRelativePosition(0,true);this.emUpdateDisplay(a,j);this.setDisplay(ub.e,true);this.setZIndex(10,true);this.focus();this.emFocus();}this.emSetValue(n);};c.emEndEdit=function(){if(this.emGetType()==jsx3.gui.Matrix.EditMask.NORMAL)this.emRestoreDisplay();return this.emGetValue();};c.emSetValue=function(b){this.setValue(b);};c.emGetValue=function(){var
Ea=this.getValue();return Ea!=null?Ea.toString():null;};c.emUpdateDisplay=function(s,r){var
qb=isNaN(this._jsxo1)?s.W:Math.min(parseInt(this._jsxo1),s.W);var
ka=isNaN(this._jsxGN)?s.H:Math.min(parseInt(this._jsxGN),s.H);this.setDimensions(s.L,s.T,qb,ka,true);};c.emRestoreDisplay=function(){this.setDisplay(ub.l,true);};c.emFocus=function(){};});jsx3.Form=jsx3.gui.Form;
