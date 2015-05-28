/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.List");jsx3.Class.defineClass("jsx3.gui.Grid",jsx3.gui.List,null,function(l,o){var
ub={k:"jsx:///images/list/header.gif",h:"background-image:url(",c:"fx",H:"TBODY",p:"hidden",q:"",G:"jsxexecute",v:"jsxbeforeedit",I:"TR",i:"jsx:///images/list/grid.gif",A:"\n",a:"jsx:///xsl/",F:"_jsxvalidrow",u:/@/g,t:"jsxselect",j:");",s:"jsxnull",z:"_jsxisblur",d:"/jsx30grid.xsl",o:";",D:"jsxafteredit",K:"JSXDragId",w:"_jsxbody",r:"jsxmask",C:"function",B:"onAfterEdit called for invalid row ",l:"#c8cfd8",g:")",L:"3.0.00",b:"ie",m:"jsx30list_r2",f:"jsx:///images/grid/select.gif",J:"TD",y:"character",n:"background-color:",x:"sentence",E:"_jsxcurmask",e:"url("};var
B=jsx3.util.Logger.getLogger(l.jsxclass.getName());var
Ua=jsx3.gui.Event;l.DEFAULTXSLURL=jsx3.resolveURI(ub.a+(jsx3.CLASS_LOADER.IE?ub.b:ub.c)+ub.d);l.SELECTBGIMAGE=ub.e+jsx3.resolveURI(ub.f)+ub.g;l.DEFAULTBACKGROUND=ub.h+jsx3.resolveURI(ub.i)+ub.j;l.DEFAULTBACKGROUNDHEAD=ub.h+jsx3.resolveURI(ub.k)+ub.j;jsx3.html.loadImages(ub.f,ub.i,ub.k);l.DEFAULTBACKGROUNDCOLOR=ub.l;l.DEFAULTBACKGROUNDCOLORHEAD=ub.l;l.DEFAULTROWCLASS=ub.m;o.init=function(n){this.jsxsuper(n);};o.QE=function(){this._jsxFh=null;this._jsxcellindex=null;this._jsxrowid=null;};o.getTabListener=function(){return this.jsxtablistener;};o.setTabListener=function(q){this.jsxtablistener=q;return this;};o.paint=function(){this.resetMask();this.QE();return this.jsxsuper();};o.getClassName=function(){return this.jsxclassname==null?l.DEFAULTROWCLASS:this.jsxclassname;};o.getXSL=function(){return this.qj(l.DEFAULTXSLURL);};o.doValidate=function(){return this.setValidationState(1).getValidationState();};o.zf=function(){return ub.n+(this.getBackgroundColor()?this.getBackgroundColor():l.DEFAULTBACKGROUNDCOLOR)+ub.o;};o.Of=function(){return this.getBackground()?this.getBackground()+ub.o:l.DEFAULTBACKGROUND;};o.zn=function(b,k){this.resetMask();this.jsxsuper(b,k);};o.resetMask=function(m){if(m||(m=this.getActiveMask())!=null)m.setVisibility(ub.p,true).setLeft(0,true).setTop(0,true);};o.doSort=function(n){var
qb;if(qb=this.getActiveMask())this.onAfterEdit(null,qb);this.resetMask();this.jsxsuper(n);};o.repaintBody=function(){this.resetMask();this.jsxsuper();};o.doBlurItem=function(e){};o.doFocusItem=function(f,s){var
qa=Ua.getCurrent();var
ob=f.ownerDocument;var
ib=f.parentNode.id.substring(this.getId().length+1);if(!this.Pk(ib))return;var
vb=f!=this._jsxFh||s;if(vb&&this._jsxcurmask!=null&&(s==true||s!=true&&!(this._jsxcurmask instanceof jsx3.gui.TextBox))){this.onAfterEdit(this._jsxFh,this._jsxcurmask);}else if(vb&&this._jsxcurmask!=null)this.resetMask();try{f.focus();if(this._jsxFh!=null)this._jsxFh.style.backgroundImage=this._jsxFh.prevBG!=null?this._jsxFh.prevBG:ub.q;if(this.getGrowBy()>0&&f.parentNode==f.parentNode.parentNode.lastChild)this.appendRow();}catch(Kb){var
va=ob.getElementById(this._jsxrowid);if(va!=null)va.childNodes[this._jsxcellindex].style.backgroundImage=ub.q;return false;}f.prevBG=f.style.backgroundImage;f.style.backgroundImage=l.SELECTBGIMAGE;this._jsxFh=f;this._jsxcellindex=f.cellIndex;this._jsxrowid=f.parentNode.id;var
Ja=this.Ld()[this._jsxcellindex];if(s==null){var
da=f.getAttribute(ub.r);if(!jsx3.util.strEmpty(da)&&da!=ub.s){var
la=Ja.getDescendantOfName(da)||this.getDescendantOfName(da);this.OZ(qa,f,la);}else this.doEvent(ub.t,{objEVENT:qa,strRECORDID:ib,intCOLUMNINDEX:this._jsxcellindex});}};o.OZ=function(c,j,r){this.resetMask();this._jsxcurmask=r;var
x=this.Ld()[this._jsxcellindex].getPath().replace(ub.u,ub.q);var
rb=this.getSelectedId();r.setValue();var
jb=this.doEvent(ub.v,{objEVENT:c,strATTRIBUTENAME:x,strRECORDID:rb,objMASK:r,intCOLUMNINDEX:this._jsxcellindex});if(jb!==false&&j!=null&&r!=null){var
ka=j.ownerDocument;var
da=this.getHeaderHeight()===0||this.getHeaderHeight()?this.getHeaderHeight():jsx3.gui.List.DEFAULTHEADERHEIGHT;r._jsxvalidrow=j.parentNode.id;var
ia=ka.getElementById(this.getId()+ub.w);var
yb=this.getRecord(rb);var
Ab=yb[x];if(Ab==null)Ab=ub.q;var
Ta=this.getAbsolutePosition(this.getRendered(),j);if(r instanceof jsx3.gui.TextBox){r.setLeft(Ta.L+ia.scrollLeft,true);r.setTop(Ta.T-da+ia.scrollTop,true);r.setWidth(Ta.W-1,true);r.setHeight(Ta.H-1,true);r.setValue(Ab);r._jsxprevmaskvalue=Ab;r.setVisibility(ub.q,true);if((j=r.getRendered())&&j.scrollHeight>j.offsetHeight)r.setHeight(Ta.H-1+Ta.H,true);if(r.getType()==1){r.focus();r.getRendered().select();}else{r.focus();if(ka.selection){var
vb=ka.selection.createRange();vb.move(ub.x,-1);vb.moveEnd(ub.y,Ab.length);vb.select();}}}else if(r instanceof jsx3.gui.ToolbarButton||r instanceof jsx3.gui.Menu){r.setLeft(Ta.L+Ta.W-r.getAbsolutePosition().W+ia.scrollLeft+(r instanceof jsx3.gui.Menu?2:0),true);r.setTop(Ta.T-da+ia.scrollTop-2,true);r.setVisibility(ub.q,true);r._jsxvalue=Ab==ub.q?null:Ab;r._jsxprevmaskvalue=Ab;if(c.ctrlKey())if(r instanceof jsx3.gui.Menu){r.showMenu(c,r.getRendered(),1);}else r.focus();}else if(r instanceof jsx3.gui.Select){if(r.getValue()==null){r.setValue(Ab==ub.q?null:Ab);r.setText(Ab==null?ub.q:Ab);}r.setLeft(Ta.L+ia.scrollLeft,true);r.setTop(Ta.T-da+ia.scrollTop,true);r.setWidth(Ta.W-1,true);r.setHeight(Ta.H-1,true);r._jsxprevmaskvalue=r.getValue();r.setVisibility(ub.q,true);if(c.ctrlKey()){r.show();}else r.focus();}delete this[ub.z];}};o.doMaskKeyDown=function(q,s){if(s==null)s=Ua.getCurrent();var
db=false;var
ta=s.keyCode();var
_=ta>36&&ta<41;if((s.enterKey()||s.tabKey())&&!s.ctrlKey()||_&&(s.ctrlKey()||s.altKey())){if(s.enterKey()&&s.shiftKey()||ta==38){s.setKeyCode(38);}else if(s.enterKey()||ta==40){s.setKeyCode(40);}else if(s.tabKey()&&s.shiftKey()&&!(!this.getTabListener()||this.getTabListener()==1)){try{this.focus();return;}catch(Kb){}}else if(s.tabKey()&&!(!this.getTabListener()||this.getTabListener()==1))try{this.getRendered().lastChild.focus();return;}catch(Kb){}this._jsxisblur=this.getActiveMask()&&this.getActiveMask() instanceof jsx3.gui.TextBox;this.oe(s,q);db=true;}else if(s.ctrlKey()&&s.enterKey()){var
Ha=this.getDocument().selection.createRange();Ha.text=ub.A;Ha.select();db=true;}else if(_)db=true;if(db)s.cancelBubble();};o.getSelectedId=function(){if(this._jsxrowid!=null){var
Wa=this.getRendered();if(Wa){var
Bb=Wa.ownerDocument.getElementById(this._jsxrowid);if(Bb!=null)return this._jsxrowid.substring(this.getId().length+1);}this.QE();}return null;};o.getActiveMask=function(){return this._jsxcurmask;};o.getActiveCell=function(){return this._jsxFh;};o.getActiveRow=function(){var
O;return (O=this.getActiveCell())?O.parentNode:null;};o.getActiveAttribute=function(){if(this._jsxcellindex){var
qb=this.Ld()[this._jsxcellindex];if(qb)return qb.getPath().replace(ub.u,ub.q);}return null;};o.onAfterEdit=function(d,p,b,n){var
fa=Ua.getCurrent();var
Jb=this.Ld()[this._jsxcellindex].getPath().replace(ub.u,ub.q);var
Sa=this.getSelectedId();if(d==null)d=this._jsxFh;var
t=d&&d.parentNode?d.parentNode.id:null;if(p._jsxvalidrow==null||p._jsxvalidrow!=t){B.warn(ub.B+t);return;}if(!(jsx3.util.strEmpty(d.getAttribute(ub.r))||d.getAttribute(ub.r)==ub.s))if(p==null||typeof p.getValue==ub.C&&p.getValue()!=p._jsxprevmaskvalue){var
eb=this.doEvent(ub.D,{objEVENT:fa,strATTRIBUTENAME:Jb,strRECORDID:Sa,objMASK:p,objGUI:d,intCOLUMNINDEX:this._jsxcellindex});if(eb!==false&&p!=null){this.updateCell(Jb,Sa,d,p);if(b&&p instanceof jsx3.gui.Menu)this.getActiveCell().focus();}}if(p!=null&&n!==true){this.resetMask(p);delete this[ub.E];delete p[ub.F];}};o.updateCell=function(g,f,d,p){if(p==null)p=this.getActiveMask();if(d==null)d=this._jsxFh;if(p&&d){if(g==null)g=this.Ld()[this._jsxcellindex].getPath().replace(ub.u,ub.q);if(f==null)f=this.getSelectedId();var
K=typeof p.getMaskValue==ub.C?p.getMaskValue():p.getValue();if(K==null)K=ub.q;this.insertRecordProperty(f,g,K,true);}};o.oe=function(a,d){if(this.jsxsupermix(a,d))return;if(this._jsxFh==null||this._jsxFh.parentNode==null)return;var
Cb=this._jsxFh;var
V=this.Ld().length;var
tb=a.keyCode();var
db=tb>=37&&tb<=40||tb==9&&(!this.getTabListener()||this.getTabListener()==1);var
za=db;if(db){var
F=null;var
y=Cb==Cb.parentNode.firstChild;var
na=Cb.cellIndex>=V-1;var
ya=Cb.parentNode==Cb.parentNode.parentNode.firstChild;var
ea=Cb.parentNode==Cb.parentNode.parentNode.lastChild;if(tb==37||a.shiftKey()&&tb==9){if(tb==9)tb=37;if(y){if(ya)F=Cb;else F=Cb.parentNode.previousSibling.childNodes[V-1];}else F=Cb.previousSibling;}else if(tb==38){if(ya)F=Cb;else F=Cb.parentNode.previousSibling.childNodes[Cb.cellIndex];}else if(tb==39||tb==9){if(tb==9)tb=39;if(na){if(ea)F=Cb;else F=Cb.parentNode.nextSibling.childNodes[0];}else F=Cb.nextSibling;}else if(tb==40)if(ea)F=Cb;else F=Cb.parentNode.nextSibling.childNodes[Cb.cellIndex];this.doFocusItem(F,true);}else if(tb==13){}else if(tb==9&&a.shiftKey()){this.focus();za=true;}else if(tb==9){this.getRendered().lastChild.focus();za=true;}if(za){a.cancelBubble();a.cancelReturn();}};o.doExecute=function(k,m){this.u3(this.isOldEventProtocol(),k,m);};o.u3=function(a,f,r){if(f==null&&this._jsxrowid)f=this._jsxrowid.substring(this.getId().length+1);if(r==null)r=this._jsxcellindex;if(f==null)return;if(!this.Pk(f))return;var
A=this.getRecordNode(f);if(A!=null)this.eval(A.getAttribute(ub.G),{strRECORDID:f,intCOLUMNINDEX:r});if(a)this.doEvent(ub.G,{objEVENT:a instanceof Ua?a:null,strRECORDID:f,intCOLUMNINDEX:r});};o.Tg=function(n,r){var
la=n.srcElement();if(la.tagName==ub.H||la.tagName==ub.I)return;var
_a=this.getRendered();while(la.tagName!=ub.J&&la!=_a)la=la.parentNode;if(la.tagName==ub.J){if(la.parentNode.getAttribute(ub.K)==null)return;if(la.cellIndex==this.Ld().length)return;la.focus();n.cancelBubble();n.cancelReturn();}};o.bk=function(){var
rb=ub.q;var
Xa=this.Ld();var
pb=Xa.length;for(var
fb=0;fb<pb;fb++){var
pa=Xa[fb];var
Za=pa.getChildren();for(var
aa=0;aa<Za.length;aa++)rb=rb+Za[aa].paint();}return rb;};o.redrawRecord=function(k,n){if(n==0)this.resetMask();this.jsxsuper(k,n);};l.getVersion=function(){return ub.L;};});jsx3.Grid=jsx3.gui.Grid;
