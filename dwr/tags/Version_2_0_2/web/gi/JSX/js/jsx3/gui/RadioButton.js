/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.require("jsx3.gui.Form","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.RadioButton",jsx3.gui.Block,[jsx3.gui.Form],function(k,f){k.UNSELECTED=0;k.SELECTED=1;k.DEFAULTCLASSNAME="jsx30radio";f.init=function(p,s,n,d,a,b,i,j,h){this.jsxsuper(p,s,n,d,a,b);this.setGroupName(j);this.setValue(i);this.setDefaultSelected(h==null?k.SELECTED:h);if(h!=null)this.setSelected(h);};f.getGroupName=function(){return this.jsxgroupname;};f.setGroupName=function(g){this.jsxgroupname=g;};f.IU=function(r,n){if(!r.leftButton()&&r.isMouseEvent())return;if(this.getSelected()!=k.SELECTED){var Ib=this.doEvent(jsx3.gui.Interactive.SELECT,{objEVENT:r});if(Ib!==false)this.setSelected(k.SELECTED,n);}};f.DY=function(a,l){if(a.enterKey()){this.IU(a,l);a.cancelAll();}};f.getDefaultSelected=function(){return this.jsxdefaultselected;};f.setDefaultSelected=function(h){this.jsxdefaultselected=h;return this;};f.getSelected=function(){return this.jsxselected!=null?this.jsxselected:this.getDefaultSelected();};f.setSelected=function(l,s){l=l!=null?l:k.SELECTED;this.jsxselected=l;if(s==null)s=this.getRendered();if(s){jsx3.html.selectSingleElm(s,0,0,0).checked=l==k.SELECTED;if(l==k.SELECTED){var ub=this.getSiblings();for(var hb=0;hb<ub.length;hb++)ub[hb].jsxselected=k.UNSELECTED;}}return this;};f.getSiblings=function(h){var tc=[];var x=this.getDocument();if(x==null)return tc;var bb=this.getId();var _b=x.getElementsByName(this.getGroupName());for(var zc=0;zc<_b.length;zc++){var jb=_b[zc];if(jb.nodeName.toLowerCase()=="input"&&jb.type.toLowerCase()=="radio"){var J=jb.parentNode.parentNode.parentNode;if(J.id!=bb)tc.push(h?[jsx3.GO(J.id),J]:jsx3.GO(J.id));}}return tc;};k.getValue=function(g){var z=document;if(z!=null){var L=z.getElementsByName(g);if(L!=null){var nc=L.length;for(var vb=0;vb<nc;vb++){if(L[vb].checked)return L[vb].value;}}}};k.setValue=function(n,j){var _b=document;if(_b==null)return null;var Qb=_b.getElementsByName(n);if(Qb!=null){var t=Qb.length;for(var ac=0;ac<t;ac++){if(Qb[ac].value==j){var Dc=jsx3.GO(Qb[ac].parentNode.parentNode.parentNode.id);Dc.setSelected(k.SELECTED);return Dc;}}}return null;};f.getValue=function(){return this.jsxvalue;};f.setValue=function(l){this.jsxvalue=l;return this;};f.getGroupValue=function(){if(this.getSelected()==k.SELECTED)return this.getValue();var C=this.getSiblings();for(var Ic=0;Ic<C.length;Ic++){var zc=C[Ic];if(zc.getSelected()==k.SELECTED)return zc.getValue();}return null;};f.setGroupValue=function(m){if(this.getValue()==m){if(this.getSelected()!=k.SELECTED)this.setSelected(k.SELECTED);}else{this.jsxselected=k.UNSELECTED;var ec=this.getSiblings(true);for(var yc=0;yc<ec.length;yc++){var B=ec[yc][0];var x=ec[yc][1];var Cc=B.getValue()==m;B.jsxselected=Cc?k.SELECTED:k.UNSELECTED;jsx3.html.selectSingleElm(x,0,0,0).checked=Cc;}}};f.doValidate=function(){this.setValidationState(this.getRequired()==jsx3.gui.Form.OPTIONAL||this.getGroupValue()!=null?jsx3.gui.Form.STATEVALID:jsx3.gui.Form.STATEINVALID);return this.getValidationState();};k.s5={};k.s5[jsx3.gui.Event.CLICK]=true;k.s5[jsx3.gui.Event.KEYDOWN]=true;f.k7=function(p,m,b){this.B_(p,m,b,3);};f.T5=function(n){if(this.getParent()&&(n==null||isNaN(n.parentwidth)||isNaN(n.parentheight))){n=this.getParent().IO(this);}else{if(n==null){n={};}}var z=this.getRelativePosition()!=0&&(!this.getRelativePosition()||this.getRelativePosition()==jsx3.gui.Block.RELATIVE);var lc,tb,B,Ub;if(n.tagname==null)n.tagname="span";if((lc=this.getBorder())!=null&&lc!="")n.border=lc;if(z&&(tb=this.getMargin())!=null&&tb!="")n.margin=tb;if(n.boxtype==null)n.boxtype=z?"relativebox":"box";if(n.left==null)n.left=!z&&!jsx3.util.strEmpty(this.getLeft())?this.getLeft():0;if(n.top==null)n.top=!z&&!jsx3.util.strEmpty(this.getTop())?this.getTop():0;if(n.height==null)n.height=(B=this.getHeight())!=null?B:18;if(n.width==null){if((Ub=this.getWidth())!=null)n.width=Ub;}var vb=new jsx3.gui.Painted.Box(n);var ab={};ab.tagname="div";ab.boxtype="inline";var Ec=new jsx3.gui.Painted.Box(ab);vb.W8(Ec);ab={};ab.tagname="span";ab.boxtype="box";ab.width=16;ab.parentheight=vb.P5();ab.height=18;var Cc=new jsx3.gui.Painted.Box(ab);Ec.W8(Cc);ab={};ab.tagname="input[radio]";ab.empty=true;ab.omitpos=true;var ac=new jsx3.gui.Painted.Box(ab);Cc.W8(ac);ab={};ab.tagname="span";ab.boxtype="inline";ab.top=2;ab.parentheight=vb.P5();ab.height="100%";ab.padding="0 0 0 18";var Wb=new jsx3.gui.Painted.Box(ab);vb.W8(Wb);return vb;};f.paint=function(){this.applyDynamicProperties();var Wb=this.getEnabled()==jsx3.gui.Form.STATEENABLED;var F=Wb?this.lM(k.s5,0):"";var Qb=this.renderAttributes(null,true);var _b=this.RL(true);_b.setAttributes(" id=\""+this.getId()+"\" label=\""+this.getName()+"\" class=\""+this.CH()+"\" unselectable=\"on\" "+this.vH()+this.WP()+F+Qb);_b.setStyles((_b.KZ()?"overflow-x:hidden;":"")+this.I6()+this.oY()+this.g0()+this.D6()+this.QP()+this.UZ()+this.T1()+this.MU()+this.d9()+this.iN());var vb=_b.pQ(0);var jc=vb.pQ(0);jc.setAttributes(" class=\"jsx30radio_tristate\" ");var E=jc.pQ(0);E.setAttributes(" type=\"radio\" name=\""+this.getGroupName()+"\" value=\""+this.getValue()+"\" "+this.WP()+this.paintSelected()+this.CI());var Jc=_b.pQ(1);Jc.setAttributes(" unselectable=\"on\" ");return _b.paint().join(vb.paint().join(jc.paint().join(E.paint().join(""))+Jc.paint().join(this.AN())));};f.I6=function(){return this.getCursor()==null?"cursor:hand;":"cursor:"+this.getCursor()+";";};f.paintSelected=function(){return this.getSelected()==k.SELECTED?" checked=\"checked\" ":"";};f.CH=function(){var bb=this.getClassName();return k.DEFAULTCLASSNAME+(bb?" "+bb:"");};k.getVersion=function(){return "3.0.00";};f.emGetType=function(){return jsx3.gui.Matrix.EditMask.FORMAT;};f.emInit=function(a){this.jsxsupermix(a);this.subscribe(jsx3.gui.Interactive.SELECT,this,"Ti");};f.emSetValue=function(c){this.jsxselected=Number(c)==k.SELECTED?k.SELECTED:k.UNSELECTED;};f.emGetValue=function(){var z=this.emGetSession();if(z)return z.column.getValueForRecord(z.recordId);return null;};f.emBeginEdit=function(s,i,m,e,l,n,g){var nb=jsx3.html.selectSingleElm(g,0,0,0,0,0);if(nb&&!nb.getAttribute("disabled")){this.jsxsupermix(s,i,m,e,l,n,g);nb.focus();}else{return false;}};f.emPaintTemplate=function(){this.jsxselected=k.UNSELECTED;this.setText("");this.setEnabled(jsx3.gui.Form.STATEDISABLED);var wb=this.paint();this.setEnabled(jsx3.gui.Form.STATEENABLED);var I=this.paint();var _=this.PS(I,wb);_=_.replace(/<(input .*?)\/>/g,"<$1><xsl:if test=\"{0}='1'\"><xsl:attribute name=\"checked\">checked</xsl:attribute></xsl:if><xsl:if test=\"@jsxdisabled='1'\"><xsl:attribute name=\"disabled\">disabled</xsl:attribute></xsl:if></input>");return _;};f.Ti=function(p){var Hb=this.emGetSession();if(Hb){var Gb=Hb.column.getPath();var db=Hb.matrix.getXML().selectNodes("//record[@"+Gb+"='1']");for(var Sb=0;Sb<db.getLength();Sb++)db.getItem(Sb).removeAttribute(Gb);Hb.column.setValueForRecord(Hb.recordId,"1");}};});jsx3.RadioButton=jsx3.gui.RadioButton;
