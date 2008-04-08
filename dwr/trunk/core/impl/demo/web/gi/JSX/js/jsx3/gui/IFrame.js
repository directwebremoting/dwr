/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.IFrame",jsx3.gui.Block,null,function(p,s){var
ub={o:"</span>",d:' src="',k:'_iframe" class="jsx30iframe" frameborder="0"',r:"about:blank",c:"auto",h:'" class="jsx30iframe" style="',p:"_iframe",g:'<span id="',l:"load",q:"jsxload",b:"no",i:">",m:"S2",a:"yes",f:"",t:' scrolling="',j:'<iframe id="',n:"></iframe>",s:"src",e:'"'};p.SCROLLYES=1;p.SCROLLNO=2;p.SCROLLAUTO=3;p.Bw={1:ub.a,2:ub.b,3:ub.c};s.paint=function(){this.applyDynamicProperties();var
K=this.getSrc();K=K?ub.d+this.getUriResolver().resolveURI(K)+ub.e:ub.f;return ub.g+this.getId()+ub.h+this.bd()+this.bi()+this.fh()+ub.e+this.renderAttributes()+ub.i+ub.j+this.getId()+ub.k+K+this.M8()+this.Gj(ub.l,ub.m,1)+ub.n+ub.o;};s.getIFrame=function(){try{return this.eval(this.getId()+ub.p);}catch(Kb){return null;}};s.getContentDocument=function(){try{var
qa=this.getIFrame();if(qa)if(jsx3.CLASS_LOADER.IE){return qa.document;}else return qa.contentDocument;}catch(Kb){}return null;};s.S2=function(f,g){this.doEvent(ub.q,{objEVENT:f});};s.getSrc=function(){return this.jsxsrc;};s.setSrc=function(l){this.jsxsrc=l;if(l==null||l==ub.f)l=ub.r;if(jsx3.CLASS_LOADER.IE){var
pa=this.getContentDocument();if(pa)pa.location.href=this.getUriResolver().resolveURI(l).toString();}else{var
Pa=this.getIFrame();if(Pa&&Pa.setAttribute)Pa.setAttribute(ub.s,this.getUriResolver().resolveURI(l));else this.repaint();}return this;};s.getScrolling=function(){return this.jsxscroll;};s.setScrolling=function(q){this.jsxscroll=q;return this;};s.M8=function(){var
ca=p.Bw[this.jsxscroll];return ca?ub.t+ca+ub.e:ub.f;};s.onSetChild=function(i){return false;};s.Gc=function(q){return null;};s.Rc=function(r,o,l){};});
