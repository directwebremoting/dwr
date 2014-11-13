/*
 * Copyright (c) 2001-2007, TIBCO Software Inc.
 * Use, modification, and distribution subject to terms of license.
 */
jsx3.Class.defineClass("jsx3.app.Monitor",jsx3.util.Logger.FormatHandler,null,function(f,k){var
ub={d:"m",a:"jsx:///html/jsx3.app.Monitor.html",h:"function",c:"TR",f:"Monitor_",g:"directories=no,location=no,menubar=no,status=yes,personalbar=no,titlebar=yes,toolbar=no,resizable=yes,scrollbars=no,width=500,height=400",b:"jsx3.lang.System",e:"namespace"};var
t=jsx3.app.Server;f._8=false;f.KZ=jsx3.net.URIResolver.DEFAULT.resolveURI(ub.a);f.ideDidLoad=function(){f._8=true;};k.os=false;k.ZT=true;k.ww=null;k.K6=false;k.mz=null;k.ez=null;k.init=function(p){this.jsxsuper(p);};k.onAfterInit=function(){if(this.ww!=null){var
ja=null;if(jsx3.Class.forName(ub.b))ja=jsx3.System.getApp(this.ww);if(ja!=null){this.qE(ja);}else t.subscribe(t.INITED,this,ub.c);}else{this.ZT=false;this.K6=false;this.os=true;this.vs();}};k.qE=function(c){this.os=true;this.mz=c;if(this.K6){var
nb=this;c.registerHotKey(function(o){nb.zw();},ub.d,false,true,true);}else this.vs();};k.TR=function(r){var
sa=r.target;if(sa.getEnv(ub.e)==this.ww){if(!f._8||!this.ZT)this.qE(sa);t.unsubscribe(t.INITED,this);}};k.handle=function(b){if(this.os&&(!f._8||!this.ZT))if(this.ez){if(this.ez.closed)if(!this.K6)this.vs();if(!this.ez.closed&&this.ez.appendMessage){var
sb=jsx3.util.strEscapeHTML(this.format(b));this.ez.appendMessage(sb,jsx3.util.Logger.levelAsString(b.getLevel()));}}};k.zw=function(){if(this.ez==null||this.ez.closed)this.vs();};k.vs=function(){this.ez=window.open(f.KZ,ub.f+this.getName(),ub.g);if(this.ez){if(this.mz)if(typeof this.ez.setName==ub.h)this.ez.setName(this.mz.getEnv(ub.e));else this.ez._jsxname=this.mz.getEnv(ub.e);window.focus();}};k.getDisableInIDE=function(){return this.ZT;};k.setDisableInIDE=function(n){this.ZT=n;};k.getServerNamespace=function(){return this.ww;};k.setServerNamespace=function(n){this.ww=n;};k.getActivateOnHotKey=function(){return this.K6;};k.setActivateOnHotKey=function(g){this.K6=g;};});jsx3.util.Logger.Handler.registerHandlerClass(jsx3.app.Monitor.jsxclass);
