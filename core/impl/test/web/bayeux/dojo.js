/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

/*
	This is a compiled version of Dojo, built for deployment and not for
	development. To get an editable version, please visit:

		http://dojotoolkit.org

	for documentation and information on getting the source.
*/

if(typeof dojo=="undefined"){
var dj_global=this;
var dj_currentContext=this;
function dj_undef(_1,_2){
return (typeof (_2||dj_currentContext)[_1]=="undefined");
}
if(dj_undef("djConfig",this)){
var djConfig={};
}
if(dj_undef("dojo",this)){
var dojo={};
}
dojo.global=function(){
return dj_currentContext;
};
dojo.locale=djConfig.locale;
dojo.version={major:0,minor:0,patch:0,flag:"dev",revision:Number("$Rev: 7460 $".match(/[0-9]+/)[0]),toString:function(){
with(dojo.version){
return major+"."+minor+"."+patch+flag+" ("+revision+")";
}
}};
dojo.getObject=function(_3,_4,_5,_6){
var _7,_8;
if(typeof _3!="string"){
return undefined;
}
_7=_5;
if(!_7){
_7=dojo.global();
}
var _9=_3.split("."),i=0,_b,_c,_d;
do{
_b=_7;
_d=_9[i];
_c=_7[_9[i]];
if((_4)&&(!_c)){
_c=_7[_9[i]]={};
}
_7=_c;
i++;
}while(i<_9.length&&_7);
_8=_7;
_7=_b;
return (_6)?{obj:_7,prop:_d}:_8;
};
dojo.exists=function(_e,_f){
if(typeof _f=="string"){
dojo.deprecated("dojo.exists(obj, name)","use dojo.exists(name, obj, /*optional*/create)","0.6");
var tmp=_e;
_e=_f;
_f=tmp;
}
return (!!dojo.getObject(_e,false,_f));
};
dojo.evalProp=function(_11,_12,_13){
dojo.deprecated("dojo.evalProp","just use hash syntax. Sheesh.","0.6");
return _12[_11]||(_13?(_12[_11]={}):undefined);
};
dojo.parseObjPath=function(_14,_15,_16){
dojo.deprecated("dojo.parseObjPath","use dojo.getObject(path, create, context, true)","0.6");
return dojo.getObject(_14,_16,_15,true);
};
dojo.evalObjPath=function(_17,_18){
dojo.deprecated("dojo.evalObjPath","use dojo.getObject(path, create)","0.6");
return dojo.getObject(_17,_18);
};
dojo.errorToString=function(_19){
return (_19["message"]||_19["description"]||_19);
};
dojo.raise=function(_1a,_1b){
if(_1b){
_1a=_1a+": "+dojo.errorToString(_1b);
}else{
_1a=dojo.errorToString(_1a);
}
try{
if(djConfig.isDebug){
dojo.hostenv.println("FATAL exception raised: "+_1a);
}
}
catch(e){
}
throw _1b||Error(_1a);
};
dojo.debug=function(){
};
dojo.debugShallow=function(obj){
};
dojo.profile={start:function(){
},end:function(){
},stop:function(){
},dump:function(){
}};
function dj_eval(_1d){
return dj_global.eval?dj_global.eval(_1d):eval(_1d);
}
dojo.unimplemented=function(_1e,_1f){
var _20="'"+_1e+"' not implemented";
if(_1f!=null){
_20+=" "+_1f;
}
dojo.raise(_20);
};
dojo.deprecated=function(_21,_22,_23){
var _24="DEPRECATED: "+_21;
if(_22){
_24+=" "+_22;
}
if(_23){
_24+=" -- will be removed in version: "+_23;
}
dojo.debug(_24);
};
dojo.render=(function(){
function vscaffold(_25,_26){
var tmp={capable:false,support:{builtin:false,plugin:false},prefixes:_25};
for(var i=0;i<_26.length;i++){
tmp[_26[i]]=false;
}
return tmp;
}
return {name:"",ver:dojo.version,os:{win:false,linux:false,osx:false},html:vscaffold(["html"],["ie","opera","khtml","safari","moz"]),svg:vscaffold(["svg"],["corel","adobe","batik"]),vml:vscaffold(["vml"],["ie"]),swf:vscaffold(["Swf","Flash","Mm"],["mm"]),swt:vscaffold(["Swt"],["ibm"])};
})();
dojo.hostenv=(function(){
var _29={isDebug:false,allowQueryConfig:false,baseScriptUri:"",baseRelativePath:"",libraryScriptUri:"",iePreventClobber:false,ieClobberMinimal:true,preventBackButtonFix:true,delayMozLoadingFix:false,searchIds:[],parseWidgets:true};
if(typeof djConfig=="undefined"){
djConfig=_29;
}else{
for(var _2a in _29){
if(typeof djConfig[_2a]=="undefined"){
djConfig[_2a]=_29[_2a];
}
}
}
return {name_:"(unset)",version_:"(unset)",getName:function(){
return this.name_;
},getVersion:function(){
return this.version_;
},getText:function(uri){
dojo.unimplemented("getText","uri="+uri);
}};
})();
dojo.hostenv.getBaseScriptUri=function(){
if(djConfig.baseScriptUri.length){
return djConfig.baseScriptUri;
}
var uri=new String(djConfig.libraryScriptUri||djConfig.baseRelativePath);
if(!uri){
dojo.raise("Nothing returned by getLibraryScriptUri(): "+uri);
}
djConfig.baseScriptUri=djConfig.baseRelativePath;
return djConfig.baseScriptUri;
};
(function(){
var _2d={pkgFileName:"__package__",loading_modules_:{},loaded_modules_:{},addedToLoadingCount:[],removedFromLoadingCount:[],inFlightCount:0,modulePrefixes_:{dojo:{name:"dojo",value:"src"}},registerModulePath:function(_2e,_2f){
this.modulePrefixes_[_2e]={name:_2e,value:_2f};
},moduleHasPrefix:function(_30){
var mp=this.modulePrefixes_;
return Boolean(mp[_30]&&mp[_30].value);
},getModulePrefix:function(_32){
if(this.moduleHasPrefix(_32)){
return this.modulePrefixes_[_32].value;
}
return _32;
},getTextStack:[],loadUriStack:[],loadedUris:[],post_load_:false,modulesLoadedListeners:[],unloadListeners:[],loadNotifying:false};
for(var _33 in _2d){
dojo.hostenv[_33]=_2d[_33];
}
})();
dojo.hostenv.loadPath=function(_34,_35,cb){
var uri;
if(_34.charAt(0)=="/"||_34.match(/^\w+:/)){
uri=_34;
}else{
uri=this.getBaseScriptUri()+_34;
}
if(djConfig.cacheBust&&dojo.render.html.capable){
uri+="?"+String(djConfig.cacheBust).replace(/\W+/g,"");
}
try{
return !_35?this.loadUri(uri,cb):this.loadUriAndCheck(uri,_35,cb);
}
catch(e){
dojo.debug(e);
return false;
}
};
dojo.hostenv.loadUri=function(uri,cb){
if(this.loadedUris[uri]){
return true;
}
var _3a=this.getText(uri,null,true);
if(!_3a){
return false;
}
this.loadedUris[uri]=true;
if(cb){
_3a="("+_3a+")";
}
var _3b=dj_eval(_3a);
if(cb){
cb(_3b);
}
return true;
};
dojo.hostenv.loadUriAndCheck=function(uri,_3d,cb){
var ok=true;
try{
ok=this.loadUri(uri,cb);
}
catch(e){
dojo.debug("failed loading ",uri," with error: ",e);
}
return Boolean(ok&&this.findModule(_3d,false));
};
dojo.loaded=function(){
};
dojo.unloaded=function(){
};
dojo.hostenv.loaded=function(){
this.loadNotifying=true;
this.post_load_=true;
var mll=this.modulesLoadedListeners;
for(var x=0;x<mll.length;x++){
mll[x]();
}
this.modulesLoadedListeners=[];
this.loadNotifying=false;
dojo.loaded();
};
dojo.hostenv.unloaded=function(){
var mll=this.unloadListeners;
while(mll.length){
(mll.pop())();
}
dojo.unloaded();
};
dojo.addOnLoad=function(obj,_44){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.modulesLoadedListeners.push(obj);
}else{
if(arguments.length>1){
dh.modulesLoadedListeners.push(function(){
obj[_44]();
});
}
}
if(dh.post_load_&&dh.inFlightCount==0&&!dh.loadNotifying){
dh.callLoaded();
}
};
dojo.addOnUnload=function(obj,_47){
var dh=dojo.hostenv;
if(arguments.length==1){
dh.unloadListeners.push(obj);
}else{
if(arguments.length>1){
dh.unloadListeners.push(function(){
obj[_47]();
});
}
}
};
dojo.hostenv.modulesLoaded=function(){
if(this.post_load_){
return;
}
if(this.loadUriStack.length==0&&this.getTextStack.length==0){
if(this.inFlightCount>0){
dojo.debug("files still in flight!");
return;
}
dojo.hostenv.callLoaded();
}
};
dojo.hostenv.callLoaded=function(){
if(typeof setTimeout=="object"||(djConfig["useXDomain"]&&dojo.render.html.opera)){
setTimeout("dojo.hostenv.loaded();",0);
}else{
dojo.hostenv.loaded();
}
};
dojo.hostenv.getModuleSymbols=function(_49){
var _4a=_49.split(".");
for(var i=_4a.length;i>0;i--){
var _4c=_4a.slice(0,i).join(".");
if((i==1)&&!this.moduleHasPrefix(_4c)){
_4a[0]="../"+_4a[0];
}else{
var _4d=this.getModulePrefix(_4c);
if(_4d!=_4c){
_4a.splice(0,i,_4d);
break;
}
}
}
return _4a;
};
dojo.hostenv._global_omit_module_check=false;
dojo.hostenv.loadModule=function(_4e,_4f,_50){
if(!_4e){
return;
}
_50=this._global_omit_module_check||_50;
var _51=this.findModule(_4e,false);
if(_51){
return _51;
}
if(dj_undef(_4e,this.loading_modules_)){
this.addedToLoadingCount.push(_4e);
}
this.loading_modules_[_4e]=1;
var _52=_4e.replace(/\./g,"/")+".js";
var _53=_4e.split(".");
var _54=this.getModuleSymbols(_4e);
var _55=((_54[0].charAt(0)!="/")&&!_54[0].match(/^\w+:/));
var _56=_54[_54.length-1];
var ok;
if(_56=="*"){
_4e=_53.slice(0,-1).join(".");
while(_54.length){
_54.pop();
_54.push(this.pkgFileName);
_52=_54.join("/")+".js";
if(_55&&_52.charAt(0)=="/"){
_52=_52.slice(1);
}
ok=this.loadPath(_52,!_50?_4e:null);
if(ok){
break;
}
_54.pop();
}
}else{
_52=_54.join("/")+".js";
_4e=_53.join(".");
var _58=!_50?_4e:null;
ok=this.loadPath(_52,_58);
if(!ok&&!_4f){
_54.pop();
while(_54.length){
_52=_54.join("/")+".js";
ok=this.loadPath(_52,_58);
if(ok){
break;
}
_54.pop();
_52=_54.join("/")+"/"+this.pkgFileName+".js";
if(_55&&_52.charAt(0)=="/"){
_52=_52.slice(1);
}
ok=this.loadPath(_52,_58);
if(ok){
break;
}
}
}
if(!ok&&!_50){
dojo.raise("Could not load '"+_4e+"'; last tried '"+_52+"'");
}
}
if(!_50&&!this["isXDomain"]){
_51=this.findModule(_4e,false);
if(!_51){
dojo.raise("symbol '"+_4e+"' is not defined after loading '"+_52+"'");
}
}
return _51;
};
dojo.hostenv.startPackage=function(_59){
var _5a=String(_59);
var _5b=_5a;
var _5c=_59.split(/\./);
if(_5c[_5c.length-1]=="*"){
_5c.pop();
_5b=_5c.join(".");
}
var _5d=dojo.getObject(_5b,true);
this.loaded_modules_[_5a]=_5d;
this.loaded_modules_[_5b]=_5d;
return _5d;
};
dojo.hostenv.findModule=function(_5e,_5f){
var lmn=String(_5e);
if(this.loaded_modules_[lmn]){
return this.loaded_modules_[lmn];
}
if(_5f){
dojo.raise("no loaded module named '"+_5e+"'");
}
return null;
};
dojo.kwCompoundRequire=function(_61){
var _62=_61["common"]||[];
var _63=_62.concat(_61[dojo.hostenv.name_]||_61["default"]||[]);
for(var x=0;x<_63.length;x++){
var _65=_63[x];
if(_65.constructor==Array){
dojo.hostenv.loadModule.apply(dojo.hostenv,_65);
}else{
dojo.hostenv.loadModule(_65);
}
}
};
dojo.require=function(_66){
dojo.hostenv.loadModule.apply(dojo.hostenv,arguments);
};
dojo.requireIf=function(_67,_68){
var _69=arguments[0];
if((_69===true)||(_69=="common")||(_69&&dojo.render[_69].capable)){
var _6a=[];
for(var i=1;i<arguments.length;i++){
_6a.push(arguments[i]);
}
dojo.require.apply(dojo,_6a);
}
};
dojo.requireAfterIf=dojo.requireIf;
dojo.provide=function(_6c){
return dojo.hostenv.startPackage.apply(dojo.hostenv,arguments);
};
dojo.registerModulePath=function(_6d,_6e){
return dojo.hostenv.registerModulePath(_6d,_6e);
};
if(djConfig["modulePaths"]){
for(var param in djConfig["modulePaths"]){
dojo.registerModulePath(param,djConfig["modulePaths"][param]);
}
}
dojo.requireLocalization=function(_6f,_70,_71,_72){
dojo.require("dojo.i18n.loader");
dojo.i18n._requireLocalization.apply(dojo.hostenv,arguments);
};
}
if(typeof window!="undefined"){
(function(){
if(djConfig.allowQueryConfig){
var _73=document.location.toString();
var _74=_73.split("?",2);
if(_74.length>1){
var _75=_74[1];
var _76=_75.split("&");
for(var x in _76){
var sp=_76[x].split("=");
if((sp[0].length>9)&&(sp[0].substr(0,9)=="djConfig.")){
var opt=sp[0].substr(9);
try{
djConfig[opt]=eval(sp[1]);
}
catch(e){
djConfig[opt]=sp[1];
}
}
}
}
}
if(((djConfig["baseScriptUri"]=="")||(djConfig["baseRelativePath"]==""))&&(document&&document.getElementsByTagName)){
var _7a=document.getElementsByTagName("script");
var _7b=/(__package__|dojo|bootstrap1)\.js([\?\.]|$)/i;
for(var i=0;i<_7a.length;i++){
var src=_7a[i].getAttribute("src");
if(!src){
continue;
}
var m=src.match(_7b);
if(m){
var _7f=src.substring(0,m.index);
if(src.indexOf("bootstrap1")>-1){
_7f+="../";
}
if(!this["djConfig"]){
djConfig={};
}
if(djConfig["baseScriptUri"]==""){
djConfig["baseScriptUri"]=_7f;
}
if(djConfig["baseRelativePath"]==""){
djConfig["baseRelativePath"]=_7f;
}
break;
}
}
}
var dr=dojo.render;
var drh=dojo.render.html;
var drs=dojo.render.svg;
var dua=(drh.UA=navigator.userAgent);
var dav=(drh.AV=navigator.appVersion);
var t=true;
var f=false;
drh.capable=t;
drh.support.builtin=t;
dr.ver=parseFloat(drh.AV);
dr.os.mac=dav.indexOf("Macintosh")>=0;
dr.os.win=dav.indexOf("Windows")>=0;
dr.os.linux=dav.indexOf("X11")>=0;
drh.opera=dua.indexOf("Opera")>=0;
drh.khtml=(dav.indexOf("Konqueror")>=0)||(dav.indexOf("Safari")>=0);
drh.safari=dav.indexOf("Safari")>=0;
var _87=dua.indexOf("Gecko");
drh.mozilla=drh.moz=(_87>=0)&&(!drh.khtml);
if(drh.mozilla){
drh.geckoVersion=dua.substring(_87+6,_87+14);
}
drh.ie=(document.all)&&(!drh.opera);
drh.ie50=drh.ie&&dav.indexOf("MSIE 5.0")>=0;
drh.ie55=drh.ie&&dav.indexOf("MSIE 5.5")>=0;
drh.ie60=drh.ie&&dav.indexOf("MSIE 6.0")>=0;
drh.ie70=drh.ie&&dav.indexOf("MSIE 7.0")>=0;
var cm=document["compatMode"];
drh.quirks=(cm=="BackCompat")||(cm=="QuirksMode")||drh.ie55||drh.ie50;
dojo.locale=dojo.locale||(drh.ie?navigator.userLanguage:navigator.language).toLowerCase();
dr.vml.capable=drh.ie;
drs.capable=f;
drs.support.plugin=f;
drs.support.builtin=f;
var _89=window["document"];
var tdi=_89["implementation"];
if((tdi)&&(tdi["hasFeature"])&&(tdi.hasFeature("org.w3c.dom.svg","1.0"))){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
if(drh.safari){
var tmp=dua.split("AppleWebKit/")[1];
var ver=parseFloat(tmp.split(" ")[0]);
if(ver>=420){
drs.capable=t;
drs.support.builtin=t;
drs.support.plugin=f;
}
}else{
}
})();
dojo.hostenv.startPackage("dojo.hostenv");
dojo.render.name=dojo.hostenv.name_="browser";
dojo.hostenv.searchIds=[];
dojo.hostenv._XMLHTTP_PROGIDS=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"];
dojo.hostenv.getXmlhttpObject=function(){
var _8d=null;
var _8e=null;
try{
_8d=new XMLHttpRequest();
}
catch(e){
}
if(!_8d){
for(var i=0;i<3;++i){
var _90=dojo.hostenv._XMLHTTP_PROGIDS[i];
try{
_8d=new ActiveXObject(_90);
}
catch(e){
_8e=e;
}
if(_8d){
dojo.hostenv._XMLHTTP_PROGIDS=[_90];
break;
}
}
}
if(!_8d){
return dojo.raise("XMLHTTP not available",_8e);
}
return _8d;
};
dojo.hostenv._blockAsync=false;
dojo.hostenv.getText=function(uri,_92,_93){
if(!_92){
this._blockAsync=true;
}
var _94=this.getXmlhttpObject();
function isDocumentOk(_95){
var _96=_95["status"];
return Boolean((!_96)||((200<=_96)&&(300>_96))||(_96==304));
}
if(_92){
var _97=this,_98=null,gbl=dojo.global();
var xhr=dojo.getObject("dojo.io.XMLHTTPTransport");
_94.onreadystatechange=function(){
if(_98){
gbl.clearTimeout(_98);
_98=null;
}
if(_97._blockAsync||(xhr&&xhr._blockAsync)){
_98=gbl.setTimeout(function(){
_94.onreadystatechange.apply(this);
},10);
}else{
if(4==_94.readyState){
if(isDocumentOk(_94)){
_92(_94.responseText);
}
}
}
};
}
_94.open("GET",uri,_92?true:false);
try{
_94.send(null);
if(_92){
return null;
}
if(!isDocumentOk(_94)){
var err=Error("Unable to load "+uri+" status:"+_94.status);
err.status=_94.status;
err.responseText=_94.responseText;
throw err;
}
}
catch(e){
this._blockAsync=false;
if((_93)&&(!_92)){
return null;
}else{
throw e;
}
}
this._blockAsync=false;
return _94.responseText;
};
dojo.hostenv.defaultDebugContainerId="dojoDebug";
dojo.hostenv._println_buffer=[];
dojo.hostenv._println_safe=false;
dojo.hostenv.println=function(_9c){
if(!dojo.hostenv._println_safe){
dojo.hostenv._println_buffer.push(_9c);
}else{
try{
var _9d=document.getElementById(djConfig.debugContainerId?djConfig.debugContainerId:dojo.hostenv.defaultDebugContainerId);
if(!_9d){
_9d=dojo.body();
}
var div=document.createElement("div");
div.appendChild(document.createTextNode(_9c));
_9d.appendChild(div);
}
catch(e){
try{
document.write("<div>"+_9c+"</div>");
}
catch(e2){
window.status=_9c;
}
}
}
};
dojo.addOnLoad(function(){
dojo.hostenv._println_safe=true;
while(dojo.hostenv._println_buffer.length>0){
dojo.hostenv.println(dojo.hostenv._println_buffer.shift());
}
});
function dj_addNodeEvtHdlr(_9f,_a0,fp){
var _a2=_9f["on"+_a0]||function(){
};
_9f["on"+_a0]=function(){
fp.apply(_9f,arguments);
_a2.apply(_9f,arguments);
};
return true;
}
dojo.hostenv._djInitFired=false;
function dj_load_init(e){
dojo.hostenv._djInitFired=true;
var _a4=(e&&e.type)?e.type.toLowerCase():"load";
if(arguments.callee.initialized||(_a4!="domcontentloaded"&&_a4!="load")){
return;
}
arguments.callee.initialized=true;
if(typeof (_timer)!="undefined"){
clearInterval(_timer);
delete _timer;
}
var _a5=function(){
if(dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
};
if(dojo.hostenv.inFlightCount==0){
_a5();
dojo.hostenv.modulesLoaded();
}else{
dojo.hostenv.modulesLoadedListeners.unshift(_a5);
}
}
if(document.addEventListener){
if(dojo.render.html.opera||(dojo.render.html.moz&&(djConfig["enableMozDomContentLoaded"]===true))){
document.addEventListener("DOMContentLoaded",dj_load_init,null);
}
window.addEventListener("load",dj_load_init,null);
}
if(dojo.render.html.ie&&dojo.render.os.win){
document.write("<scr"+"ipt defer src=\"//:\" "+"onreadystatechange=\"if(this.readyState=='complete'){dj_load_init();}\">"+"</scr"+"ipt>");
}
if(/(WebKit|khtml)/i.test(navigator.userAgent)){
var _timer=setInterval(function(){
if(/loaded|complete/.test(document.readyState)){
dj_load_init();
}
},10);
}
if(dojo.render.html.ie){
dj_addNodeEvtHdlr(window,"beforeunload",function(){
dojo.hostenv._unloading=true;
window.setTimeout(function(){
dojo.hostenv._unloading=false;
},0);
});
}
dj_addNodeEvtHdlr(window,"unload",function(){
if((!dojo.render.html.ie)||(dojo.render.html.ie&&dojo.hostenv._unloading)){
dojo.hostenv.unloaded();
}
});
dojo.hostenv.makeWidgets=function(){
var _a6=[];
if(djConfig.searchIds&&djConfig.searchIds.length>0){
_a6=_a6.concat(djConfig.searchIds);
}
if(dojo.hostenv.searchIds&&dojo.hostenv.searchIds.length>0){
_a6=_a6.concat(dojo.hostenv.searchIds);
}
if((djConfig.parseWidgets)||(_a6.length>0)){
if(dojo.getObject("dojo.widget.Parse")){
var _a7=new dojo.xml.Parse();
if(_a6.length>0){
for(var x=0;x<_a6.length;x++){
var _a9=document.getElementById(_a6[x]);
if(!_a9){
continue;
}
var _aa=_a7.parseElement(_a9,null,true);
dojo.widget.getParser().createComponents(_aa);
}
}else{
if(djConfig.parseWidgets){
var _aa=_a7.parseElement(dojo.body(),null,true);
dojo.widget.getParser().createComponents(_aa);
}
}
}
}
};
dojo.addOnLoad(function(){
if(!dojo.render.html.ie){
dojo.hostenv.makeWidgets();
}
});
try{
if(dojo.render.html.ie){
document.namespaces.add("v","urn:schemas-microsoft-com:vml");
document.createStyleSheet().addRule("v\\:*","behavior:url(#default#VML)");
}
}
catch(e){
}
dojo.hostenv.writeIncludes=function(){
};
if(!dj_undef("document",this)){
dj_currentDocument=this.document;
}
dojo.doc=function(){
return dj_currentDocument;
};
dojo.body=function(){
return dojo.doc().body||dojo.doc().getElementsByTagName("body")[0];
};
dojo.byId=function(id,doc){
if((id)&&((typeof id=="string")||(id instanceof String))){
if(!doc){
doc=dj_currentDocument;
}
var ele=doc.getElementById(id);
if(ele&&(ele.id!=id)&&doc.all){
ele=null;
eles=doc.all[id];
if(eles){
if(eles.length){
for(var i=0;i<eles.length;i++){
if(eles[i].id==id){
ele=eles[i];
break;
}
}
}else{
ele=eles;
}
}
}
return ele;
}
return id;
};
dojo.setContext=function(_af,_b0){
dj_currentContext=_af;
dj_currentDocument=_b0;
};
dojo._fireCallback=function(_b1,_b2,_b3){
if((_b2)&&((typeof _b1=="string")||(_b1 instanceof String))){
_b1=_b2[_b1];
}
return (_b2?_b1.apply(_b2,_b3||[]):_b1());
};
dojo.withGlobal=function(_b4,_b5,_b6,_b7){
var _b8;
var _b9=dj_currentContext;
var _ba=dj_currentDocument;
try{
dojo.setContext(_b4,_b4.document);
_b8=dojo._fireCallback(_b5,_b6,_b7);
}
finally{
dojo.setContext(_b9,_ba);
}
return _b8;
};
dojo.withDoc=function(_bb,_bc,_bd,_be){
var _bf;
var _c0=dj_currentDocument;
try{
dj_currentDocument=_bb;
_bf=dojo._fireCallback(_bc,_bd,_be);
}
finally{
dj_currentDocument=_c0;
}
return _bf;
};
}
dojo.requireIf((djConfig["isDebug"]||djConfig["debugAtAllCosts"]),"dojo.debug");
dojo.requireIf(djConfig["debugAtAllCosts"]&&!window.widget&&!djConfig["useXDomain"],"dojo.browser_debug");
dojo.requireIf(djConfig["debugAtAllCosts"]&&!window.widget&&djConfig["useXDomain"],"dojo.browser_debug_xd");
dojo.provide("dojo.string.common");
dojo.string.trim=function(str,wh){
if(!str.replace){
return str;
}
if(!str.length){
return str;
}
var re=(wh>0)?(/^\s+/):(wh<0)?(/\s+$/):(/^\s+|\s+$/g);
return str.replace(re,"");
};
dojo.string.trimStart=function(str){
return dojo.string.trim(str,1);
};
dojo.string.trimEnd=function(str){
return dojo.string.trim(str,-1);
};
dojo.string.repeat=function(str,_c7,_c8){
var out="";
for(var i=0;i<_c7;i++){
out+=str;
if(_c8&&i<_c7-1){
out+=_c8;
}
}
return out;
};
dojo.string.pad=function(str,len,c,dir){
var out=String(str);
if(!c){
c="0";
}
if(!dir){
dir=1;
}
while(out.length<len){
if(dir>0){
out=c+out;
}else{
out+=c;
}
}
return out;
};
dojo.string.padLeft=function(str,len,c){
return dojo.string.pad(str,len,c,1);
};
dojo.string.padRight=function(str,len,c){
return dojo.string.pad(str,len,c,-1);
};
dojo.provide("dojo.string");
dojo.provide("dojo.lang.common");
dojo.lang.inherits=function(_d6,_d7){
if(!dojo.lang.isFunction(_d7)){
dojo.raise("dojo.inherits: superclass argument ["+_d7+"] must be a function (subclass: ["+_d6+"']");
}
_d6.prototype=new _d7();
_d6.prototype.constructor=_d6;
_d6.superclass=_d7.prototype;
_d6["super"]=_d7.prototype;
};
dojo.lang._mixin=function(obj,_d9){
var _da={};
for(var x in _d9){
if((typeof _da[x]=="undefined")||(_da[x]!=_d9[x])){
obj[x]=_d9[x];
}
}
if(dojo.render.html.ie&&(typeof (_d9["toString"])=="function")&&(_d9["toString"]!=obj["toString"])&&(_d9["toString"]!=_da["toString"])){
obj.toString=_d9.toString;
}
return obj;
};
dojo.lang.mixin=function(obj,_dd){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(obj,arguments[i]);
}
return obj;
};
dojo.lang.extend=function(_e0,_e1){
for(var i=1,l=arguments.length;i<l;i++){
dojo.lang._mixin(_e0.prototype,arguments[i]);
}
return _e0;
};
dojo.lang._delegate=function(obj,_e5){
function TMP(){
}
TMP.prototype=obj;
var tmp=new TMP();
if(_e5){
dojo.lang.mixin(tmp,_e5);
}
return tmp;
};
dojo.inherits=dojo.lang.inherits;
dojo.mixin=dojo.lang.mixin;
dojo.extend=dojo.lang.extend;
dojo.lang.find=function(_e7,_e8,_e9,_ea){
var _eb=dojo.lang.isString(_e7);
if(_eb){
_e7=_e7.split("");
}
if(_ea){
var _ec=-1;
var i=_e7.length-1;
var end=-1;
}else{
var _ec=1;
var i=0;
var end=_e7.length;
}
if(_e9){
while(i!=end){
if(_e7[i]===_e8){
return i;
}
i+=_ec;
}
}else{
while(i!=end){
if(_e7[i]==_e8){
return i;
}
i+=_ec;
}
}
return -1;
};
dojo.lang.indexOf=dojo.lang.find;
dojo.lang.findLast=function(_ef,_f0,_f1){
return dojo.lang.find(_ef,_f0,_f1,true);
};
dojo.lang.lastIndexOf=dojo.lang.findLast;
dojo.lang.inArray=function(_f2,_f3){
return dojo.lang.find(_f2,_f3)>-1;
};
dojo.lang.isObject=function(it){
if(typeof it=="undefined"){
return false;
}
return (typeof it=="object"||it===null||dojo.lang.isArray(it)||dojo.lang.isFunction(it));
};
dojo.lang.isArray=function(it){
return (it&&it instanceof Array||typeof it=="array");
};
dojo.lang.isArrayLike=function(it){
if((!it)||(dojo.lang.isUndefined(it))){
return false;
}
if(dojo.lang.isString(it)){
return false;
}
if(dojo.lang.isFunction(it)){
return false;
}
if(dojo.lang.isArray(it)){
return true;
}
if((it.tagName)&&(it.tagName.toLowerCase()=="form")){
return false;
}
if(dojo.lang.isNumber(it.length)&&isFinite(it.length)){
return true;
}
return false;
};
dojo.lang.isFunction=function(it){
return (it instanceof Function||typeof it=="function");
};
(function(){
if((dojo.render.html.capable)&&(dojo.render.html["safari"])){
dojo.lang.isFunction=function(it){
if((typeof (it)=="function")&&(it=="[object NodeList]")){
return false;
}
return (it instanceof Function||typeof it=="function");
};
}
})();
dojo.lang.isString=function(it){
return (typeof it=="string"||it instanceof String);
};
dojo.lang.isAlien=function(it){
if(!it){
return false;
}
return !dojo.lang.isFunction(it)&&/\{\s*\[native code\]\s*\}/.test(String(it));
};
dojo.lang.isBoolean=function(it){
return (it instanceof Boolean||typeof it=="boolean");
};
dojo.lang.isNumber=function(it){
return (it instanceof Number||typeof it=="number");
};
dojo.lang.isUndefined=function(it){
return ((typeof (it)=="undefined")&&(it==undefined));
};
dojo.provide("dojo.lang.extras");
dojo.lang.setTimeout=function(_fe,_ff){
var _100=window,_101=2;
if(!dojo.lang.isFunction(_fe)){
_100=_fe;
_fe=_ff;
_ff=arguments[2];
_101++;
}
if(dojo.lang.isString(_fe)){
_fe=_100[_fe];
}
var args=[];
for(var i=_101;i<arguments.length;i++){
args.push(arguments[i]);
}
return dojo.global().setTimeout(function(){
_fe.apply(_100,args);
},_ff);
};
dojo.lang.clearTimeout=function(_104){
dojo.global().clearTimeout(_104);
};
dojo.lang.getNameInObj=function(ns,item){
if(!ns){
ns=dj_global;
}
for(var x in ns){
if(ns[x]===item){
return new String(x);
}
}
return null;
};
dojo.lang.shallowCopy=function(obj,deep){
var i,ret;
if(obj===null){
return null;
}
if(dojo.lang.isObject(obj)){
ret=new obj.constructor();
for(i in obj){
if(dojo.lang.isUndefined(ret[i])){
ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];
}
}
}else{
if(dojo.lang.isArray(obj)){
ret=[];
for(i=0;i<obj.length;i++){
ret[i]=deep?dojo.lang.shallowCopy(obj[i],deep):obj[i];
}
}else{
ret=obj;
}
}
return ret;
};
dojo.lang.firstValued=function(){
for(var i=0;i<arguments.length;i++){
if(typeof arguments[i]!="undefined"){
return arguments[i];
}
}
return undefined;
};
dojo.lang.getObjPathValue=function(_10d,_10e,_10f){
dojo.deprecated("dojo.lang.getObjPathValue","use dojo.getObject","0.6");
with(dojo.parseObjPath(_10d,_10e,_10f)){
return dojo.evalProp(prop,obj,_10f);
}
};
dojo.lang.setObjPathValue=function(_110,_111,_112,_113){
dojo.deprecated("dojo.lang.setObjPathValue","use dojo.parseObjPath and the '=' operator","0.6");
if(arguments.length<4){
_113=true;
}
with(dojo.parseObjPath(_110,_112,_113)){
if(obj&&(_113||(prop in obj))){
obj[prop]=_111;
}
}
};
dojo.provide("dojo.io.common");
dojo.io.transports=[];
dojo.io.hdlrFuncNames=["load","error","timeout"];
dojo.io.Request=function(url,_115,_116,_117){
if((arguments.length==1)&&(arguments[0].constructor==Object)){
this.fromKwArgs(arguments[0]);
}else{
this.url=url;
if(_115){
this.mimetype=_115;
}
if(_116){
this.transport=_116;
}
if(arguments.length>=4){
this.changeUrl=_117;
}
}
};
dojo.lang.extend(dojo.io.Request,{url:"",mimetype:"text/plain",method:"GET",content:undefined,transport:undefined,changeUrl:undefined,formNode:undefined,sync:false,bindSuccess:false,useCache:false,preventCache:false,jsonFilter:function(_118){
if((this.mimetype=="text/json-comment-filtered")||(this.mimetype=="application/json-comment-filtered")){
var _119=_118.indexOf("/*");
var _11a=_118.lastIndexOf("*/");
if((_119==-1)||(_11a==-1)){
dojo.debug("your JSON wasn't comment filtered!");
return "";
}
return _118.substring(_119+2,_11a);
}
dojo.debug("please consider using a mimetype of text/json-comment-filtered to avoid potential security issues with JSON endpoints");
return _118;
},load:function(type,data,_11d,_11e){
},error:function(type,_120,_121,_122){
},timeout:function(type,_124,_125,_126){
},handle:function(type,data,_129,_12a){
},timeoutSeconds:0,abort:function(){
},fromKwArgs:function(_12b){
if(_12b["url"]){
_12b.url=_12b.url.toString();
}
if(_12b["formNode"]){
_12b.formNode=dojo.byId(_12b.formNode);
}
if(!_12b["method"]&&_12b["formNode"]&&_12b["formNode"].method){
_12b.method=_12b["formNode"].method;
}
if(!_12b["handle"]&&_12b["handler"]){
_12b.handle=_12b.handler;
}
if(!_12b["load"]&&_12b["loaded"]){
_12b.load=_12b.loaded;
}
if(!_12b["changeUrl"]&&_12b["changeURL"]){
_12b.changeUrl=_12b.changeURL;
}
_12b.encoding=dojo.lang.firstValued(_12b["encoding"],djConfig["bindEncoding"],"");
_12b.sendTransport=dojo.lang.firstValued(_12b["sendTransport"],djConfig["ioSendTransport"],false);
var _12c=dojo.lang.isFunction;
for(var x=0;x<dojo.io.hdlrFuncNames.length;x++){
var fn=dojo.io.hdlrFuncNames[x];
if(_12b[fn]&&_12c(_12b[fn])){
continue;
}
if(_12b["handle"]&&_12c(_12b["handle"])){
_12b[fn]=_12b.handle;
}
}
dojo.lang.mixin(this,_12b);
}});
dojo.io.Error=function(msg,type,num){
this.message=msg;
this.type=type||"unknown";
this.number=num||0;
};
dojo.io.transports.addTransport=function(name){
this.push(name);
this[name]=dojo.io[name];
};
dojo.io.bind=function(_133){
if(!(_133 instanceof dojo.io.Request)){
try{
_133=new dojo.io.Request(_133);
}
catch(e){
dojo.debug(e);
}
}
var _134="";
if(_133["transport"]){
_134=_133["transport"];
if(!this[_134]){
dojo.io.sendBindError(_133,"No dojo.io.bind() transport with name '"+_133["transport"]+"'.");
return _133;
}
if(!this[_134].canHandle(_133)){
dojo.io.sendBindError(_133,"dojo.io.bind() transport with name '"+_133["transport"]+"' cannot handle this type of request.");
return _133;
}
}else{
for(var x=0;x<dojo.io.transports.length;x++){
var tmp=dojo.io.transports[x];
if((this[tmp])&&(this[tmp].canHandle(_133))){
_134=tmp;
break;
}
}
if(_134==""){
dojo.io.sendBindError(_133,"None of the loaded transports for dojo.io.bind()"+" can handle the request.");
return _133;
}
}
this[_134].bind(_133);
_133.bindSuccess=true;
return _133;
};
dojo.io.sendBindError=function(_137,_138){
if((typeof _137.error=="function"||typeof _137.handle=="function")&&(typeof setTimeout=="function"||typeof setTimeout=="object")){
var _139=new dojo.io.Error(_138);
setTimeout(function(){
_137[(typeof _137.error=="function")?"error":"handle"]("error",_139,null,_137);
},50);
}else{
dojo.raise(_138);
}
};
dojo.io.queueBind=function(_13a){
if(!(_13a instanceof dojo.io.Request)){
try{
_13a=new dojo.io.Request(_13a);
}
catch(e){
dojo.debug(e);
}
}
var _13b=_13a.load;
_13a.load=function(){
dojo.io._queueBindInFlight=false;
var ret=_13b.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
var _13d=_13a.error;
_13a.error=function(){
dojo.io._queueBindInFlight=false;
var ret=_13d.apply(this,arguments);
dojo.io._dispatchNextQueueBind();
return ret;
};
dojo.io._bindQueue.push(_13a);
dojo.io._dispatchNextQueueBind();
return _13a;
};
dojo.io._dispatchNextQueueBind=function(){
if(!dojo.io._queueBindInFlight){
dojo.io._queueBindInFlight=true;
if(dojo.io._bindQueue.length>0){
dojo.io.bind(dojo.io._bindQueue.shift());
}else{
dojo.io._queueBindInFlight=false;
}
}
};
dojo.io._bindQueue=[];
dojo.io._queueBindInFlight=false;
dojo.io.argsFromMap=function(map,_140,last){
var enc=/utf/i.test(_140||"")?encodeURIComponent:dojo.string.encodeAscii;
var _143=[];
var _144=new Object();
for(var name in map){
var _146=function(elt){
var val=enc(name)+"="+enc(elt);
_143[(last==name)?"push":"unshift"](val);
};
if(!_144[name]){
var _149=map[name];
if(dojo.lang.isArray(_149)){
dojo.lang.forEach(_149,_146);
}else{
_146(_149);
}
}
}
return _143.join("&");
};
dojo.io.setIFrameSrc=function(_14a,src,_14c){
try{
var r=dojo.render.html;
if(!_14c){
if(r.safari){
_14a.location=src;
}else{
frames[_14a.name].location=src;
}
}else{
var idoc;
if(r.ie){
idoc=_14a.contentWindow.document;
}else{
if(r.safari){
idoc=_14a.document;
}else{
idoc=_14a.contentWindow;
}
}
if(!idoc){
_14a.location=src;
return;
}else{
idoc.location.replace(src);
}
}
}
catch(e){
dojo.debug(e);
dojo.debug("setIFrameSrc: "+e);
}
};
dojo.provide("dojo.lang.func");
dojo.lang.hitch=function(_14f,_150){
var args=[];
for(var x=2;x<arguments.length;x++){
args.push(arguments[x]);
}
var fcn=(dojo.lang.isString(_150)?_14f[_150]:_150)||function(){
};
return function(){
var ta=args.concat([]);
for(var x=0;x<arguments.length;x++){
ta.push(arguments[x]);
}
return fcn.apply(_14f,ta);
};
};
dojo.lang.anonCtr=0;
dojo.lang.anon={};
dojo.lang.nameAnonFunc=function(_156,_157,_158){
var isIE=(dojo.render.html.capable&&dojo.render.html["ie"]);
var jpn="$joinpoint";
var nso=(_157||dojo.lang.anon);
if(isIE){
var cn=_156["__dojoNameCache"];
if(cn&&nso[cn]===_156){
return _156["__dojoNameCache"];
}else{
if(cn){
var _15d=cn.indexOf(jpn);
if(_15d!=-1){
return cn.substring(0,_15d);
}
}
}
}
if((_158)||((dj_global["djConfig"])&&(djConfig["slowAnonFuncLookups"]==true))){
for(var x in nso){
try{
if(nso[x]===_156){
if(isIE){
_156["__dojoNameCache"]=x;
var _15d=x.indexOf(jpn);
if(_15d!=-1){
x=x.substring(0,_15d);
}
}
return x;
}
}
catch(e){
}
}
}
var ret="__"+dojo.lang.anonCtr++;
while(typeof nso[ret]!="undefined"){
ret="__"+dojo.lang.anonCtr++;
}
nso[ret]=_156;
return ret;
};
dojo.lang.forward=function(_160){
return function(){
return this[_160].apply(this,arguments);
};
};
dojo.lang.curry=function(_161,func){
var _163=[];
_161=_161||dj_global;
if(dojo.lang.isString(func)){
func=_161[func];
}
for(var x=2;x<arguments.length;x++){
_163.push(arguments[x]);
}
var _165=(func["__preJoinArity"]||func.length)-_163.length;
function gather(_166,_167,_168){
var _169=_168;
var _16a=_167.slice(0);
for(var x=0;x<_166.length;x++){
_16a.push(_166[x]);
}
_168=_168-_166.length;
if(_168<=0){
var res=func.apply(_161,_16a);
_168=_169;
return res;
}else{
return function(){
return gather(arguments,_16a,_168);
};
}
}
return gather([],_163,_165);
};
dojo.lang.curryArguments=function(_16d,func,args,_170){
var _171=[];
var x=_170||0;
for(x=_170;x<args.length;x++){
_171.push(args[x]);
}
return dojo.lang.curry.apply(dojo.lang,[_16d,func].concat(_171));
};
dojo.lang.tryThese=function(){
for(var x=0;x<arguments.length;x++){
try{
if(typeof arguments[x]=="function"){
var ret=(arguments[x]());
if(ret){
return ret;
}
}
}
catch(e){
dojo.debug(e);
}
}
};
dojo.lang.delayThese=function(farr,cb,_177,_178){
if(!farr.length){
if(typeof _178=="function"){
_178();
}
return;
}
if((typeof _177=="undefined")&&(typeof cb=="number")){
_177=cb;
cb=function(){
};
}else{
if(!cb){
cb=function(){
};
if(!_177){
_177=0;
}
}
}
setTimeout(function(){
(farr.shift())();
cb();
dojo.lang.delayThese(farr,cb,_177,_178);
},_177);
};
dojo.provide("dojo.AdapterRegistry");
dojo.AdapterRegistry=function(_179){
this.pairs=[];
this.returnWrappers=_179||false;
};
dojo.lang.extend(dojo.AdapterRegistry,{register:function(name,_17b,wrap,_17d,_17e){
var type=(_17e)?"unshift":"push";
this.pairs[type]([name,_17b,wrap,_17d]);
},match:function(){
for(var i=0;i<this.pairs.length;i++){
var pair=this.pairs[i];
if(pair[1].apply(this,arguments)){
if((pair[3])||(this.returnWrappers)){
return pair[2];
}else{
return pair[2].apply(this,arguments);
}
}
}
throw new Error("No match found");
},unregister:function(name){
for(var i=0;i<this.pairs.length;i++){
var pair=this.pairs[i];
if(pair[0]==name){
this.pairs.splice(i,1);
return true;
}
}
return false;
}});
dojo.provide("dojo.lang.array");
dojo.lang.mixin(dojo.lang,{has:function(obj,name){
try{
return typeof obj[name]!="undefined";
}
catch(e){
return false;
}
},isEmpty:function(obj){
if(dojo.lang.isArrayLike(obj)||dojo.lang.isString(obj)){
return obj.length===0;
}else{
if(dojo.lang.isObject(obj)){
var tmp={};
for(var x in obj){
if(obj[x]&&(!tmp[x])){
return false;
}
}
return true;
}
}
},map:function(arr,obj,_18c){
var _18d=dojo.lang.isString(arr);
if(_18d){
arr=arr.split("");
}
if(dojo.lang.isFunction(obj)&&(!_18c)){
_18c=obj;
obj=dj_global;
}else{
if(dojo.lang.isFunction(obj)&&_18c){
var _18e=obj;
obj=_18c;
_18c=_18e;
}
}
if(Array.map){
var _18f=Array.map(arr,_18c,obj);
}else{
var _18f=[];
for(var i=0;i<arr.length;++i){
_18f.push(_18c.call(obj,arr[i]));
}
}
if(_18d){
return _18f.join("");
}else{
return _18f;
}
},reduce:function(arr,_192,_193,_194){
var _195=_193;
if(arguments.length==2){
_195=arr[0];
arr=arr.slice(1);
}
var ob=_194||dj_global;
dojo.lang.map(arr,function(val){
_195=_192.call(ob,_195,val);
});
return _195;
},forEach:function(_198,_199,_19a){
if(dojo.lang.isString(_198)){
_198=_198.split("");
}
if(Array.forEach){
Array.forEach(_198,_199,_19a);
}else{
if(!_19a){
_19a=dj_global;
}
for(var i=0,l=_198.length;i<l;i++){
_199.call(_19a,_198[i],i,_198);
}
}
},_everyOrSome:function(_19d,arr,_19f,_1a0){
if(dojo.lang.isString(arr)){
arr=arr.split("");
}
if(Array.every){
return Array[_19d?"every":"some"](arr,_19f,_1a0);
}else{
if(!_1a0){
_1a0=dj_global;
}
for(var i=0,l=arr.length;i<l;i++){
var _1a3=_19f.call(_1a0,arr[i],i,arr);
if(_19d&&!_1a3){
return false;
}else{
if((!_19d)&&(_1a3)){
return true;
}
}
}
return Boolean(_19d);
}
},every:function(arr,_1a5,_1a6){
return this._everyOrSome(true,arr,_1a5,_1a6);
},some:function(arr,_1a8,_1a9){
return this._everyOrSome(false,arr,_1a8,_1a9);
},filter:function(arr,_1ab,_1ac){
var _1ad=dojo.lang.isString(arr);
if(_1ad){
arr=arr.split("");
}
var _1ae;
if(Array.filter){
_1ae=Array.filter(arr,_1ab,_1ac);
}else{
if(!_1ac){
if(arguments.length>=3){
dojo.raise("thisObject doesn't exist!");
}
_1ac=dj_global;
}
_1ae=[];
for(var i=0;i<arr.length;i++){
if(_1ab.call(_1ac,arr[i],i,arr)){
_1ae.push(arr[i]);
}
}
}
if(_1ad){
return _1ae.join("");
}else{
return _1ae;
}
},unnest:function(){
var out=[];
for(var i=0;i<arguments.length;i++){
if(dojo.lang.isArrayLike(arguments[i])){
var add=dojo.lang.unnest.apply(this,arguments[i]);
out=out.concat(add);
}else{
out.push(arguments[i]);
}
}
return out;
},toArray:function(_1b3,_1b4){
var _1b5=[];
for(var i=_1b4||0;i<_1b3.length;i++){
_1b5.push(_1b3[i]);
}
return _1b5;
}});
dojo.provide("dojo.string.extras");
dojo.string.substitute=function(_1b7,map,_1b9,_1ba){
return _1b7.replace(/\$\{([^\s\:\}]+)(?:\:(\S+))?\}/g,function(_1bb,key,_1bd){
var _1be=dojo.getObject(key,false,map).toString();
if(_1bd){
_1be=dojo.getObject(_1bd,false,_1ba)(_1be);
}
if(_1b9){
_1be=_1b9(_1be);
}
return _1be;
});
};
dojo.string.capitalize=function(str){
if(!dojo.lang.isString(str)){
return "";
}
return str.replace(/[^\s]+/g,function(word){
return word.substring(0,1).toUpperCase()+word.substring(1);
});
};
dojo.string.isBlank=function(str){
if(!dojo.lang.isString(str)){
return true;
}
return (dojo.string.trim(str).length==0);
};
dojo.string.encodeAscii=function(str){
if(!dojo.lang.isString(str)){
return str;
}
var ret="";
var _1c4=escape(str);
var _1c5,re=/%u([0-9A-F]{4})/i;
while((_1c5=_1c4.match(re))){
var num=Number("0x"+_1c5[1]);
var _1c8=escape("&#"+num+";");
ret+=_1c4.substring(0,_1c5.index)+_1c8;
_1c4=_1c4.substring(_1c5.index+_1c5[0].length);
}
ret+=_1c4.replace(/\+/g,"%2B");
return ret;
};
dojo.string.escape=function(type,str){
var args=dojo.lang.toArray(arguments,1);
switch(type.toLowerCase()){
case "xml":
case "html":
case "xhtml":
return dojo.string.escapeXml.apply(this,args);
case "sql":
return dojo.string.escapeSql.apply(this,args);
case "regexp":
case "regex":
return dojo.string.escapeRegExp.apply(this,args);
case "javascript":
case "jscript":
case "js":
return dojo.string.escapeJavaScript.apply(this,args);
case "ascii":
return dojo.string.encodeAscii.apply(this,args);
default:
return str;
}
};
dojo.string.escapeXml=function(str,_1cd){
str=str.replace(/&/gm,"&amp;").replace(/</gm,"&lt;").replace(/>/gm,"&gt;").replace(/"/gm,"&quot;");
if(!_1cd){
str=str.replace(/'/gm,"&#39;");
}
return str;
};
dojo.string.escapeSql=function(str){
return str.replace(/'/gm,"''");
};
dojo.string.escapeRegExp=function(str,_1d0){
return str.replace(/([\.$?*!=:|{}\(\)\[\]\\\/^])/g,function(ch){
if(_1d0&&_1d0.indexOf(ch)!=-1){
return ch;
}
return "\\"+ch;
});
};
dojo.string.escapeJavaScript=function(str){
return str.replace(/(["'\f\b\n\t\r])/gm,"\\$1");
};
dojo.string.escapeString=function(str){
return ("\""+str.replace(/(["\\])/g,"\\$1")+"\"").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r");
};
dojo.string.summary=function(str,len){
if(!len||str.length<=len){
return str;
}
return str.substring(0,len).replace(/\.+$/,"")+"...";
};
dojo.string.endsWith=function(str,end,_1d8){
if(_1d8){
str=str.toLowerCase();
end=end.toLowerCase();
}
if((str.length-end.length)<0){
return false;
}
return str.lastIndexOf(end)==str.length-end.length;
};
dojo.string.endsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.endsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.startsWith=function(str,_1dc,_1dd){
if(_1dd){
str=str.toLowerCase();
_1dc=_1dc.toLowerCase();
}
return str.indexOf(_1dc)==0;
};
dojo.string.startsWithAny=function(str){
for(var i=1;i<arguments.length;i++){
if(dojo.string.startsWith(str,arguments[i])){
return true;
}
}
return false;
};
dojo.string.has=function(str){
for(var i=1;i<arguments.length;i++){
if(str.indexOf(arguments[i])>-1){
return true;
}
}
return false;
};
dojo.string.normalizeNewlines=function(text,_1e3){
if(_1e3=="\n"){
text=text.replace(/\r\n/g,"\n");
text=text.replace(/\r/g,"\n");
}else{
if(_1e3=="\r"){
text=text.replace(/\r\n/g,"\r");
text=text.replace(/\n/g,"\r");
}else{
text=text.replace(/([^\r])\n/g,"$1\r\n").replace(/\r([^\n])/g,"\r\n$1");
}
}
return text;
};
dojo.string.splitEscaped=function(str,_1e5){
var _1e6=[];
for(var i=0,_1e8=0;i<str.length;i++){
if(str.charAt(i)=="\\"){
i++;
continue;
}
if(str.charAt(i)==_1e5){
_1e6.push(str.substring(_1e8,i));
_1e8=i+1;
}
}
_1e6.push(str.substr(_1e8));
return _1e6;
};
dojo.provide("dojo.json");
dojo.json={jsonRegistry:new dojo.AdapterRegistry(),register:function(name,_1ea,wrap,_1ec){
dojo.json.jsonRegistry.register(name,_1ea,wrap,_1ec);
},evalJson:function(json){
try{
return eval("("+json+")");
}
catch(e){
dojo.debug(e);
return json;
}
},serialize:function(o){
var _1ef=typeof (o);
if(_1ef=="undefined"){
return "undefined";
}else{
if((_1ef=="number")||(_1ef=="boolean")){
return o+"";
}else{
if(o===null){
return "null";
}
}
}
if(_1ef=="string"){
return dojo.string.escapeString(o);
}
var me=arguments.callee;
var _1f1;
if(typeof (o.__json__)=="function"){
_1f1=o.__json__();
if(o!==_1f1){
return me(_1f1);
}
}
if(typeof (o.json)=="function"){
_1f1=o.json();
if(o!==_1f1){
return me(_1f1);
}
}
if(_1ef!="function"&&typeof (o.length)=="number"){
var res=[];
for(var i=0;i<o.length;i++){
var val=me(o[i]);
if(typeof (val)!="string"){
val="undefined";
}
res.push(val);
}
return "["+res.join(",")+"]";
}
try{
window.o=o;
_1f1=dojo.json.jsonRegistry.match(o);
return me(_1f1);
}
catch(e){
}
if(_1ef=="function"){
return null;
}
res=[];
for(var k in o){
var _1f6;
if(typeof (k)=="number"){
_1f6="\""+k+"\"";
}else{
if(typeof (k)=="string"){
_1f6=dojo.string.escapeString(k);
}else{
continue;
}
}
val=me(o[k]);
if(typeof (val)!="string"){
continue;
}
res.push(_1f6+":"+val);
}
return "{"+res.join(",")+"}";
}};
dojo.provide("dojo.dom");
dojo.dom.ELEMENT_NODE=1;
dojo.dom.ATTRIBUTE_NODE=2;
dojo.dom.TEXT_NODE=3;
dojo.dom.CDATA_SECTION_NODE=4;
dojo.dom.ENTITY_REFERENCE_NODE=5;
dojo.dom.ENTITY_NODE=6;
dojo.dom.PROCESSING_INSTRUCTION_NODE=7;
dojo.dom.COMMENT_NODE=8;
dojo.dom.DOCUMENT_NODE=9;
dojo.dom.DOCUMENT_TYPE_NODE=10;
dojo.dom.DOCUMENT_FRAGMENT_NODE=11;
dojo.dom.NOTATION_NODE=12;
dojo.dom.dojoml="http://www.dojotoolkit.org/2004/dojoml";
dojo.dom.xmlns={svg:"http://www.w3.org/2000/svg",smil:"http://www.w3.org/2001/SMIL20/",mml:"http://www.w3.org/1998/Math/MathML",cml:"http://www.xml-cml.org",xlink:"http://www.w3.org/1999/xlink",xhtml:"http://www.w3.org/1999/xhtml",xul:"http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul",xbl:"http://www.mozilla.org/xbl",fo:"http://www.w3.org/1999/XSL/Format",xsl:"http://www.w3.org/1999/XSL/Transform",xslt:"http://www.w3.org/1999/XSL/Transform",xi:"http://www.w3.org/2001/XInclude",xforms:"http://www.w3.org/2002/01/xforms",saxon:"http://icl.com/saxon",xalan:"http://xml.apache.org/xslt",xsd:"http://www.w3.org/2001/XMLSchema",dt:"http://www.w3.org/2001/XMLSchema-datatypes",xsi:"http://www.w3.org/2001/XMLSchema-instance",rdf:"http://www.w3.org/1999/02/22-rdf-syntax-ns#",rdfs:"http://www.w3.org/2000/01/rdf-schema#",dc:"http://purl.org/dc/elements/1.1/",dcq:"http://purl.org/dc/qualifiers/1.0","soap-env":"http://schemas.xmlsoap.org/soap/envelope/",wsdl:"http://schemas.xmlsoap.org/wsdl/",AdobeExtensions:"http://ns.adobe.com/AdobeSVGViewerExtensions/3.0/"};
dojo.dom.isNode=function(wh){
if(typeof Element=="function"){
try{
return wh instanceof Element;
}
catch(e){
}
}else{
return wh&&!isNaN(wh.nodeType);
}
};
dojo.dom.getUniqueId=function(){
var _1f8=dojo.doc();
do{
var id="dj_unique_"+(++arguments.callee._idIncrement);
}while(_1f8.getElementById(id));
return id;
};
dojo.dom.getUniqueId._idIncrement=0;
dojo.dom.firstElement=dojo.dom.getFirstChildElement=function(_1fa,_1fb){
var node=_1fa.firstChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.nextSibling;
}
if(_1fb&&node&&node.tagName&&node.tagName.toLowerCase()!=_1fb.toLowerCase()){
node=dojo.dom.nextElement(node,_1fb);
}
return node;
};
dojo.dom.lastElement=dojo.dom.getLastChildElement=function(_1fd,_1fe){
var node=_1fd.lastChild;
while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE){
node=node.previousSibling;
}
if(_1fe&&node&&node.tagName&&node.tagName.toLowerCase()!=_1fe.toLowerCase()){
node=dojo.dom.prevElement(node,_1fe);
}
return node;
};
dojo.dom.nextElement=dojo.dom.getNextSiblingElement=function(node,_201){
if(!node){
return null;
}
do{
node=node.nextSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_201&&_201.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.nextElement(node,_201);
}
return node;
};
dojo.dom.prevElement=dojo.dom.getPreviousSiblingElement=function(node,_203){
if(!node){
return null;
}
if(_203){
_203=_203.toLowerCase();
}
do{
node=node.previousSibling;
}while(node&&node.nodeType!=dojo.dom.ELEMENT_NODE);
if(node&&_203&&_203.toLowerCase()!=node.tagName.toLowerCase()){
return dojo.dom.prevElement(node,_203);
}
return node;
};
dojo.dom.moveChildren=function(_204,_205,trim){
var _207=0;
if(trim){
while(_204.hasChildNodes()&&_204.firstChild.nodeType==dojo.dom.TEXT_NODE){
_204.removeChild(_204.firstChild);
}
while(_204.hasChildNodes()&&_204.lastChild.nodeType==dojo.dom.TEXT_NODE){
_204.removeChild(_204.lastChild);
}
}
while(_204.hasChildNodes()){
_205.appendChild(_204.firstChild);
_207++;
}
return _207;
};
dojo.dom.copyChildren=function(_208,_209,trim){
var _20b=_208.cloneNode(true);
return this.moveChildren(_20b,_209,trim);
};
dojo.dom.replaceChildren=function(node,_20d){
var _20e=[];
if(dojo.render.html.ie){
for(var i=0;i<node.childNodes.length;i++){
_20e.push(node.childNodes[i]);
}
}
dojo.dom.removeChildren(node);
node.appendChild(_20d);
for(var i=0;i<_20e.length;i++){
dojo.dom.destroyNode(_20e[i]);
}
};
dojo.dom.removeChildren=function(node){
var _211=node.childNodes.length;
while(node.hasChildNodes()){
dojo.dom.removeNode(node.firstChild);
}
return _211;
};
dojo.dom.replaceNode=function(node,_213){
return node.parentNode.replaceChild(_213,node);
};
dojo.dom.destroyNode=function(node){
if(node.parentNode){
node=dojo.dom.removeNode(node);
}
if(node.nodeType!=3){
if(dojo.exists("dojo.event.browser.clean")){
dojo.event.browser.clean(node);
}
if(dojo.render.html.ie){
node.outerHTML="";
}
}
};
dojo.dom.removeNode=function(node){
if(node&&node.parentNode){
return node.parentNode.removeChild(node);
}
};
dojo.dom.getAncestors=function(node,_217,_218){
var _219=[];
var _21a=(_217&&(_217 instanceof Function||typeof _217=="function"));
while(node){
if(!_21a||_217(node)){
_219.push(node);
}
if(_218&&_219.length>0){
return _219[0];
}
node=node.parentNode;
}
if(_218){
return null;
}
return _219;
};
dojo.dom.getAncestorsByTag=function(node,tag,_21d){
tag=tag.toLowerCase();
return dojo.dom.getAncestors(node,function(el){
return ((el.tagName)&&(el.tagName.toLowerCase()==tag));
},_21d);
};
dojo.dom.getFirstAncestorByTag=function(node,tag){
return dojo.dom.getAncestorsByTag(node,tag,true);
};
dojo.dom.isDescendantOf=function(node,_222,_223){
if(_223&&node){
node=node.parentNode;
}
while(node){
if(node==_222){
return true;
}
node=node.parentNode;
}
return false;
};
dojo.dom.innerXML=function(node){
if(node.innerXML){
return node.innerXML;
}else{
if(node.xml){
return node.xml;
}else{
if(typeof XMLSerializer!="undefined"){
return (new XMLSerializer()).serializeToString(node);
}
}
}
};
dojo.dom.createDocument=function(){
var doc=null;
var _226=dojo.doc();
if(!dj_undef("ActiveXObject")){
var _227=["MSXML2","Microsoft","MSXML","MSXML3"];
for(var i=0;i<_227.length;i++){
try{
doc=new ActiveXObject(_227[i]+".XMLDOM");
}
catch(e){
}
if(doc){
break;
}
}
}else{
if((_226.implementation)&&(_226.implementation.createDocument)){
doc=_226.implementation.createDocument("","",null);
}
}
return doc;
};
dojo.dom.createDocumentFromText=function(str,_22a){
if(!_22a){
_22a="text/xml";
}
if(!dj_undef("DOMParser")){
var _22b=new DOMParser();
return _22b.parseFromString(str,_22a);
}else{
if(!dj_undef("ActiveXObject")){
var _22c=dojo.dom.createDocument();
if(_22c){
_22c.async=false;
_22c.loadXML(str);
return _22c;
}else{
dojo.debug("toXml didn't work?");
}
}else{
var _22d=dojo.doc();
if(_22d.createElement){
var tmp=_22d.createElement("xml");
tmp.innerHTML=str;
if(_22d.implementation&&_22d.implementation.createDocument){
var _22f=_22d.implementation.createDocument("foo","",null);
for(var i=0;i<tmp.childNodes.length;i++){
_22f.importNode(tmp.childNodes.item(i),true);
}
return _22f;
}
return ((tmp.document)&&(tmp.document.firstChild?tmp.document.firstChild:tmp));
}
}
}
return null;
};
dojo.dom.prependChild=function(node,_232){
if(_232.firstChild){
_232.insertBefore(node,_232.firstChild);
}else{
_232.appendChild(node);
}
return true;
};
dojo.dom.insertBefore=function(node,ref,_235){
if((_235!=true)&&(node===ref||node.nextSibling===ref)){
return false;
}
var _236=ref.parentNode;
_236.insertBefore(node,ref);
return true;
};
dojo.dom.insertAfter=function(node,ref,_239){
var pn=ref.parentNode;
if(ref==pn.lastChild){
if((_239!=true)&&(node===ref)){
return false;
}
pn.appendChild(node);
}else{
return this.insertBefore(node,ref.nextSibling,_239);
}
return true;
};
dojo.dom.insertAtPosition=function(node,ref,_23d){
if((!node)||(!ref)||(!_23d)){
return false;
}
switch(_23d.toLowerCase()){
case "before":
return dojo.dom.insertBefore(node,ref);
case "after":
return dojo.dom.insertAfter(node,ref);
case "first":
if(ref.firstChild){
return dojo.dom.insertBefore(node,ref.firstChild);
}else{
ref.appendChild(node);
return true;
}
break;
default:
ref.appendChild(node);
return true;
}
};
dojo.dom.insertAtIndex=function(node,_23f,_240){
var _241=_23f.childNodes;
if(!_241.length||_241.length==_240){
_23f.appendChild(node);
return true;
}
if(_240==0){
return dojo.dom.prependChild(node,_23f);
}
return dojo.dom.insertAfter(node,_241[_240-1]);
};
dojo.dom.textContent=function(node,text){
if(arguments.length>1){
var _244=dojo.doc();
dojo.dom.replaceChildren(node,_244.createTextNode(text));
return text;
}else{
if(node["textContent"]!=undefined){
return node.textContent;
}
var _245="";
if(node==null){
return _245;
}
var i=0,n;
while(n=node.childNodes[i++]){
switch(n.nodeType){
case 1:
case 5:
_245+=dojo.dom.textContent(n);
break;
case 3:
case 2:
case 4:
_245+=n.nodeValue;
break;
default:
break;
}
}
return _245;
}
};
dojo.dom.hasParent=function(node){
return Boolean(node&&node.parentNode&&dojo.dom.isNode(node.parentNode));
};
dojo.dom.isTag=function(node){
if(node&&node.tagName){
for(var i=1;i<arguments.length;i++){
if(node.tagName==String(arguments[i])){
return String(arguments[i]);
}
}
}
return "";
};
dojo.dom.setAttributeNS=function(elem,_24c,_24d,_24e){
if(elem==null||((elem==undefined)&&(typeof elem=="undefined"))){
dojo.raise("No element given to dojo.dom.setAttributeNS");
}
if(!((elem.setAttributeNS==undefined)&&(typeof elem.setAttributeNS=="undefined"))){
elem.setAttributeNS(_24c,_24d,_24e);
}else{
var _24f=elem.ownerDocument;
var _250=_24f.createNode(2,_24d,_24c);
_250.nodeValue=_24e;
elem.setAttributeNode(_250);
}
};
dojo.provide("dojo.undo.browser");
try{
if((!djConfig["preventBackButtonFix"])&&(!dojo.hostenv.post_load_)){
document.write("<iframe style='border: 0px; width: 1px; height: 1px; position: absolute; bottom: 0px; right: 0px; visibility: visible;' name='djhistory' id='djhistory' src='"+(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"'></iframe>");
}
}
catch(e){
}
if(dojo.render.html.opera){
dojo.debug("Opera is not supported with dojo.undo.browser, so back/forward detection will not work.");
}
dojo.undo.browser={initialHref:(!dj_undef("window"))?window.location.href:"",initialHash:(!dj_undef("window"))?window.location.hash:"",moveForward:false,historyStack:[],forwardStack:[],historyIframe:null,bookmarkAnchor:null,locationTimer:null,setInitialState:function(args){
this.initialState=this._createState(this.initialHref,args,this.initialHash);
},addToHistory:function(args){
this.forwardStack=[];
var hash=null;
var url=null;
if(!this.historyIframe){
if(djConfig["useXDomain"]&&!djConfig["dojoIframeHistoryUrl"]){
dojo.debug("dojo.undo.browser: When using cross-domain Dojo builds,"+" please save iframe_history.html to your domain and set djConfig.dojoIframeHistoryUrl"+" to the path on your domain to iframe_history.html");
}
this.historyIframe=window.frames["djhistory"];
}
if(!this.bookmarkAnchor){
this.bookmarkAnchor=document.createElement("a");
dojo.body().appendChild(this.bookmarkAnchor);
this.bookmarkAnchor.style.display="none";
}
if(args["changeUrl"]){
hash="#"+((args["changeUrl"]!==true)?args["changeUrl"]:(new Date()).getTime());
if(this.historyStack.length==0&&this.initialState.urlHash==hash){
this.initialState=this._createState(url,args,hash);
return;
}else{
if(this.historyStack.length>0&&this.historyStack[this.historyStack.length-1].urlHash==hash){
this.historyStack[this.historyStack.length-1]=this._createState(url,args,hash);
return;
}
}
this.changingUrl=true;
setTimeout("window.location.href = '"+hash+"'; dojo.undo.browser.changingUrl = false;",1);
this.bookmarkAnchor.href=hash;
if(dojo.render.html.ie){
url=this._loadIframeHistory();
var _255=args["back"]||args["backButton"]||args["handle"];
var tcb=function(_257){
if(window.location.hash!=""){
setTimeout("window.location.href = '"+hash+"';",1);
}
_255.apply(this,[_257]);
};
if(args["back"]){
args.back=tcb;
}else{
if(args["backButton"]){
args.backButton=tcb;
}else{
if(args["handle"]){
args.handle=tcb;
}
}
}
var _258=args["forward"]||args["forwardButton"]||args["handle"];
var tfw=function(_25a){
if(window.location.hash!=""){
window.location.href=hash;
}
if(_258){
_258.apply(this,[_25a]);
}
};
if(args["forward"]){
args.forward=tfw;
}else{
if(args["forwardButton"]){
args.forwardButton=tfw;
}else{
if(args["handle"]){
args.handle=tfw;
}
}
}
}else{
if(dojo.render.html.moz){
if(!this.locationTimer){
this.locationTimer=setInterval("dojo.undo.browser.checkLocation();",200);
}
}
}
}else{
url=this._loadIframeHistory();
}
this.historyStack.push(this._createState(url,args,hash));
},checkLocation:function(){
if(!this.changingUrl){
var hsl=this.historyStack.length;
if((window.location.hash==this.initialHash||window.location.href==this.initialHref)&&(hsl==1)){
this.handleBackButton();
return;
}
if(this.forwardStack.length>0){
if(this.forwardStack[this.forwardStack.length-1].urlHash==window.location.hash){
this.handleForwardButton();
return;
}
}
if((hsl>=2)&&(this.historyStack[hsl-2])){
if(this.historyStack[hsl-2].urlHash==window.location.hash){
this.handleBackButton();
return;
}
}
}
},iframeLoaded:function(evt,_25d){
if(!dojo.render.html.opera){
var _25e=this._getUrlQuery(_25d.href);
if(_25e==null){
if(this.historyStack.length==1){
this.handleBackButton();
}
return;
}
if(this.moveForward){
this.moveForward=false;
return;
}
if(this.historyStack.length>=2&&_25e==this._getUrlQuery(this.historyStack[this.historyStack.length-2].url)){
this.handleBackButton();
}else{
if(this.forwardStack.length>0&&_25e==this._getUrlQuery(this.forwardStack[this.forwardStack.length-1].url)){
this.handleForwardButton();
}
}
}
},handleBackButton:function(){
var _25f=this.historyStack.pop();
if(!_25f){
return;
}
var last=this.historyStack[this.historyStack.length-1];
if(!last&&this.historyStack.length==0){
last=this.initialState;
}
if(last){
if(last.kwArgs["back"]){
last.kwArgs["back"]();
}else{
if(last.kwArgs["backButton"]){
last.kwArgs["backButton"]();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("back");
}
}
}
}
this.forwardStack.push(_25f);
},handleForwardButton:function(){
var last=this.forwardStack.pop();
if(!last){
return;
}
if(last.kwArgs["forward"]){
last.kwArgs.forward();
}else{
if(last.kwArgs["forwardButton"]){
last.kwArgs.forwardButton();
}else{
if(last.kwArgs["handle"]){
last.kwArgs.handle("forward");
}
}
}
this.historyStack.push(last);
},_createState:function(url,args,hash){
return {"url":url,"kwArgs":args,"urlHash":hash};
},_getUrlQuery:function(url){
var _266=url.split("?");
if(_266.length<2){
return null;
}else{
return _266[1];
}
},_loadIframeHistory:function(){
var url=(djConfig["dojoIframeHistoryUrl"]||dojo.hostenv.getBaseScriptUri()+"iframe_history.html")+"?"+(new Date()).getTime();
this.moveForward=true;
dojo.io.setIFrameSrc(this.historyIframe,url,false);
return url;
}};
dojo.provide("dojo.io.BrowserIO");
if(!dj_undef("window")){
dojo.io.checkChildrenForFile=function(node){
var _269=false;
var _26a=node.getElementsByTagName("input");
dojo.lang.forEach(_26a,function(_26b){
if(_269){
return;
}
if(_26b.getAttribute("type")=="file"){
_269=true;
}
});
return _269;
};
dojo.io.formHasFile=function(_26c){
return dojo.io.checkChildrenForFile(_26c);
};
dojo.io.updateNode=function(node,_26e){
node=dojo.byId(node);
var args=_26e;
if(dojo.lang.isString(_26e)){
args={url:_26e};
}
args.mimetype="text/html";
args.load=function(t,d,e){
while(node.firstChild){
dojo.dom.destroyNode(node.firstChild);
}
node.innerHTML=d;
};
dojo.io.bind(args);
};
dojo.io.formFilter=function(node){
var type=(node.type||"").toLowerCase();
return !node.disabled&&node.name&&!dojo.lang.inArray(["file","submit","image","reset","button"],type);
};
dojo.io.encodeForm=function(_275,_276,_277){
if((!_275)||(!_275.tagName)||(!_275.tagName.toLowerCase()=="form")){
dojo.raise("Attempted to encode a non-form element.");
}
if(!_277){
_277=dojo.io.formFilter;
}
var enc=/utf/i.test(_276||"")?encodeURIComponent:dojo.string.encodeAscii;
var _279=[];
for(var i=0;i<_275.elements.length;i++){
var elm=_275.elements[i];
if(!elm||elm.tagName.toLowerCase()=="fieldset"||!_277(elm)){
continue;
}
var name=enc(elm.name);
var type=elm.type.toLowerCase();
if(type=="select-multiple"){
for(var j=0;j<elm.options.length;j++){
if(elm.options[j].selected){
_279.push(name+"="+enc(elm.options[j].value));
}
}
}else{
if(dojo.lang.inArray(["radio","checkbox"],type)){
if(elm.checked){
_279.push(name+"="+enc(elm.value));
}
}else{
_279.push(name+"="+enc(elm.value));
}
}
}
var _27f=_275.getElementsByTagName("input");
for(var i=0;i<_27f.length;i++){
var _280=_27f[i];
if(_280.type.toLowerCase()=="image"&&_280.form==_275&&_277(_280)){
var name=enc(_280.name);
_279.push(name+"="+enc(_280.value));
_279.push(name+".x=0");
_279.push(name+".y=0");
}
}
return _279.join("&")+"&";
};
dojo.io.FormBind=function(args){
this.bindArgs={};
if(args&&args.formNode){
this.init(args);
}else{
if(args){
this.init({formNode:args});
}
}
};
dojo.lang.extend(dojo.io.FormBind,{form:null,bindArgs:null,clickedButton:null,init:function(args){
var form=dojo.byId(args.formNode);
if(!form||!form.tagName||form.tagName.toLowerCase()!="form"){
throw new Error("FormBind: Couldn't apply, invalid form");
}else{
if(this.form==form){
return;
}else{
if(this.form){
throw new Error("FormBind: Already applied to a form");
}
}
}
dojo.lang.mixin(this.bindArgs,args);
this.form=form;
this.connect(form,"onsubmit","submit");
for(var i=0;i<form.elements.length;i++){
var node=form.elements[i];
if(node&&node.type&&dojo.lang.inArray(["submit","button"],node.type.toLowerCase())){
this.connect(node,"onclick","click");
}
}
var _286=form.getElementsByTagName("input");
for(var i=0;i<_286.length;i++){
var _287=_286[i];
if(_287.type.toLowerCase()=="image"&&_287.form==form){
this.connect(_287,"onclick","click");
}
}
},onSubmit:function(form){
return true;
},submit:function(e){
e.preventDefault();
if(this.onSubmit(this.form)){
dojo.io.bind(dojo.lang.mixin(this.bindArgs,{formFilter:dojo.lang.hitch(this,"formFilter")}));
}
},click:function(e){
var node=e.currentTarget;
if(node.disabled){
return;
}
this.clickedButton=node;
},formFilter:function(node){
var type=(node.type||"").toLowerCase();
var _28e=false;
if(node.disabled||!node.name){
_28e=false;
}else{
if(dojo.lang.inArray(["submit","button","image"],type)){
if(!this.clickedButton){
this.clickedButton=node;
}
_28e=node==this.clickedButton;
}else{
_28e=!dojo.lang.inArray(["file","submit","reset","button"],type);
}
}
return _28e;
},connect:function(_28f,_290,_291){
if(dojo.getObject("dojo.event.connect")){
dojo.event.connect(_28f,_290,this,_291);
}else{
var fcn=dojo.lang.hitch(this,_291);
_28f[_290]=function(e){
if(!e){
e=window.event;
}
if(!e.currentTarget){
e.currentTarget=e.srcElement;
}
if(!e.preventDefault){
e.preventDefault=function(){
window.event.returnValue=false;
};
}
fcn(e);
};
}
}});
dojo.io.XMLHTTPTransport=new function(){
var _294=this;
var _295={};
this.useCache=false;
this.preventCache=false;
function getCacheKey(url,_297,_298){
return url+"|"+_297+"|"+_298.toLowerCase();
}
function addToCache(url,_29a,_29b,http){
_295[getCacheKey(url,_29a,_29b)]=http;
}
function getFromCache(url,_29e,_29f){
return _295[getCacheKey(url,_29e,_29f)];
}
this.clearCache=function(){
_295={};
};
function doLoad(_2a0,http,url,_2a3,_2a4){
if(((http.status>=200)&&(http.status<300))||(http.status==304)||(http.status==1223)||(location.protocol=="file:"&&(http.status==0||http.status==undefined))||(location.protocol=="chrome:"&&(http.status==0||http.status==undefined))){
var ret;
if(_2a0.method.toLowerCase()=="head"){
var _2a6=http.getAllResponseHeaders();
ret={};
ret.toString=function(){
return _2a6;
};
var _2a7=_2a6.split(/[\r\n]+/g);
for(var i=0;i<_2a7.length;i++){
var pair=_2a7[i].match(/^([^:]+)\s*:\s*(.+)$/i);
if(pair){
ret[pair[1]]=pair[2];
}
}
}else{
if(_2a0.mimetype=="text/javascript"){
try{
ret=dj_eval(http.responseText);
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=null;
}
}else{
if(_2a0.mimetype.substr(0,9)=="text/json"||_2a0.mimetype.substr(0,16)=="application/json"){
try{
ret=dj_eval("("+_2a0.jsonFilter(http.responseText)+")");
}
catch(e){
dojo.debug(e);
dojo.debug(http.responseText);
ret=false;
}
}else{
if((_2a0.mimetype=="application/xml")||(_2a0.mimetype=="text/xml")){
ret=http.responseXML;
if(!ret||typeof ret=="string"||!http.getResponseHeader("Content-Type")){
ret=dojo.dom.createDocumentFromText(http.responseText);
}
}else{
ret=http.responseText;
}
}
}
}
if(_2a4){
addToCache(url,_2a3,_2a0.method,http);
}
_2a0[(typeof _2a0.load=="function")?"load":"handle"]("load",ret,http,_2a0);
}else{
var _2aa=new dojo.io.Error("XMLHttpTransport Error: "+http.status+" "+http.statusText);
_2a0[(typeof _2a0.error=="function")?"error":"handle"]("error",_2aa,http,_2a0);
}
}
function setHeaders(http,_2ac){
if(_2ac["headers"]){
for(var _2ad in _2ac["headers"]){
if(_2ad.toLowerCase()=="content-type"&&!_2ac["contentType"]){
_2ac["contentType"]=_2ac["headers"][_2ad];
}else{
http.setRequestHeader(_2ad,_2ac["headers"][_2ad]);
}
}
}
}
this.inFlight=[];
this.inFlightTimer=null;
this.startWatchingInFlight=function(){
if(!this.inFlightTimer){
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);
}
};
this.watchInFlight=function(){
var now=null;
if(!dojo.hostenv._blockAsync&&!_294._blockAsync){
for(var x=this.inFlight.length-1;x>=0;x--){
try{
var tif=this.inFlight[x];
if(!tif||tif.http._aborted||!tif.http.readyState){
this.inFlight.splice(x,1);
continue;
}
if(4==tif.http.readyState){
this.inFlight.splice(x,1);
doLoad(tif.req,tif.http,tif.url,tif.query,tif.useCache);
}else{
if(tif.startTime){
if(!now){
now=(new Date()).getTime();
}
if(tif.startTime+(tif.req.timeoutSeconds*1000)<now){
if(typeof tif.http.abort=="function"){
tif.http.abort();
}
this.inFlight.splice(x,1);
tif.req[(typeof tif.req.timeout=="function")?"timeout":"handle"]("timeout",null,tif.http,tif.req);
}
}
}
}
catch(e){
try{
var _2b1=new dojo.io.Error("XMLHttpTransport.watchInFlight Error: "+e);
tif.req[(typeof tif.req.error=="function")?"error":"handle"]("error",_2b1,tif.http,tif.req);
}
catch(e2){
dojo.debug("XMLHttpTransport error callback failed: "+e2);
}
}
}
}
clearTimeout(this.inFlightTimer);
if(this.inFlight.length==0){
this.inFlightTimer=null;
return;
}
this.inFlightTimer=setTimeout("dojo.io.XMLHTTPTransport.watchInFlight();",10);
};
var _2b2=dojo.hostenv.getXmlhttpObject()?true:false;
this.canHandle=function(_2b3){
var mlc=_2b3["mimetype"].toLowerCase()||"";
return _2b2&&((dojo.lang.inArray(["text/plain","text/html","application/xml","text/xml","text/javascript"],mlc))||(mlc.substr(0,9)=="text/json"||mlc.substr(0,16)=="application/json"))&&!(_2b3["formNode"]&&dojo.io.formHasFile(_2b3["formNode"]));
};
this.multipartBoundary="45309FFF-BD65-4d50-99C9-36986896A96F";
this.bind=function(_2b5){
var url=_2b5.url;
var _2b7="";
if(_2b5["formNode"]){
var ta=_2b5.formNode.getAttribute("action");
if(typeof (ta)!="string"){
ta=_2b5.formNode.attributes.action.value;
}
if((ta)&&(!_2b5["url"])){
url=ta;
}
var tp=_2b5.formNode.getAttribute("method");
if((tp)&&(!_2b5["method"])){
_2b5.method=tp;
}
_2b7+=dojo.io.encodeForm(_2b5.formNode,_2b5.encoding,_2b5["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
if(_2b5["file"]){
_2b5.method="post";
}
if(!_2b5["method"]){
_2b5.method="get";
}
if(_2b5.method.toLowerCase()=="get"){
_2b5.multipart=false;
}else{
if(_2b5["file"]){
_2b5.multipart=true;
}else{
if(!_2b5["multipart"]){
_2b5.multipart=false;
}
}
}
if(_2b5["backButton"]||_2b5["back"]||_2b5["changeUrl"]){
dojo.undo.browser.addToHistory(_2b5);
}
var _2ba=_2b5["content"]||{};
if(_2b5.sendTransport){
_2ba["dojo.transport"]="xmlhttp";
}
do{
if(_2b5.postContent){
_2b7=_2b5.postContent;
break;
}
if(_2ba){
_2b7+=dojo.io.argsFromMap(_2ba,_2b5.encoding);
}
if(_2b5.method.toLowerCase()=="get"||!_2b5.multipart){
break;
}
var t=[];
if(_2b7.length){
var q=_2b7.split("&");
for(var i=0;i<q.length;++i){
if(q[i].length){
var p=q[i].split("=");
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+p[0]+"\"","",p[1]);
}
}
}
if(_2b5.file){
if(dojo.lang.isArray(_2b5.file)){
for(var i=0;i<_2b5.file.length;++i){
var o=_2b5.file[i];
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}else{
var o=_2b5.file;
t.push("--"+this.multipartBoundary,"Content-Disposition: form-data; name=\""+o.name+"\"; filename=\""+("fileName" in o?o.fileName:o.name)+"\"","Content-Type: "+("contentType" in o?o.contentType:"application/octet-stream"),"",o.content);
}
}
if(t.length){
t.push("--"+this.multipartBoundary+"--","");
_2b7=t.join("\r\n");
}
}while(false);
var _2c0=_2b5["sync"]?false:true;
var _2c1=_2b5["preventCache"]||(this.preventCache==true&&_2b5["preventCache"]!=false);
var _2c2=_2b5["useCache"]==true||(this.useCache==true&&_2b5["useCache"]!=false);
if(!_2c1&&_2c2){
var _2c3=getFromCache(url,_2b7,_2b5.method);
if(_2c3){
doLoad(_2b5,_2c3,url,_2b7,false);
return;
}
}
var http=dojo.hostenv.getXmlhttpObject(_2b5);
var _2c5=false;
if(_2c0){
var _2c6=this.inFlight.push({"req":_2b5,"http":http,"url":url,"query":_2b7,"useCache":_2c2,"startTime":_2b5.timeoutSeconds?(new Date()).getTime():0});
this.startWatchingInFlight();
}else{
_294._blockAsync=true;
}
if(_2b5.method.toLowerCase()=="post"){
if(!_2b5.user){
http.open("POST",url,_2c0);
}else{
http.open("POST",url,_2c0,_2b5.user,_2b5.password);
}
setHeaders(http,_2b5);
http.setRequestHeader("Content-Type",_2b5.multipart?("multipart/form-data; boundary="+this.multipartBoundary):(_2b5.contentType||"application/x-www-form-urlencoded"));
try{
http.send(_2b7);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_2b5,{status:404},url,_2b7,_2c2);
}
}else{
var _2c7=url;
if(_2b7!=""){
_2c7+=(_2c7.indexOf("?")>-1?"&":"?")+_2b7;
}
if(_2c1){
_2c7+=(dojo.string.endsWithAny(_2c7,"?","&")?"":(_2c7.indexOf("?")>-1?"&":"?"))+"dojo.preventCache="+new Date().valueOf();
}
if(!_2b5.user){
http.open(_2b5.method.toUpperCase(),_2c7,_2c0);
}else{
http.open(_2b5.method.toUpperCase(),_2c7,_2c0,_2b5.user,_2b5.password);
}
setHeaders(http,_2b5);
try{
http.send(null);
}
catch(e){
if(typeof http.abort=="function"){
http.abort();
}
doLoad(_2b5,{status:404},url,_2b7,_2c2);
}
}
if(!_2c0){
doLoad(_2b5,http,url,_2b7,_2c2);
_294._blockAsync=false;
}
_2b5.abort=function(){
try{
http._aborted=true;
}
catch(e){
}
return http.abort();
};
return;
};
dojo.io.transports.addTransport("XMLHTTPTransport");
};
}
dojo.provide("dojo.uri.Uri");
dojo.uri=new function(){
this.dojoUri=function(uri){
return new dojo.uri.Uri(dojo.hostenv.getBaseScriptUri(),uri);
};
this.moduleUri=function(_2c9,uri){
var loc=dojo.hostenv.getModuleSymbols(_2c9).join("/");
if(!loc){
return null;
}
if(loc.lastIndexOf("/")!=loc.length-1){
loc+="/";
}
var _2cc=loc.indexOf(":");
var _2cd=loc.indexOf("/");
if(loc.charAt(0)!="/"&&(_2cc==-1||_2cc>_2cd)){
loc=dojo.hostenv.getBaseScriptUri()+loc;
}
return new dojo.uri.Uri(loc,uri);
};
this.Uri=function(){
var uri=arguments[0];
for(var i=1;i<arguments.length;i++){
if(!arguments[i]){
continue;
}
var _2d0=new dojo.uri.Uri(arguments[i].toString());
var _2d1=new dojo.uri.Uri(uri.toString());
if((_2d0.path=="")&&(_2d0.scheme==null)&&(_2d0.authority==null)&&(_2d0.query==null)){
if(_2d0.fragment!=null){
_2d1.fragment=_2d0.fragment;
}
_2d0=_2d1;
}else{
if(_2d0.scheme==null){
_2d0.scheme=_2d1.scheme;
if(_2d0.authority==null){
_2d0.authority=_2d1.authority;
if(_2d0.path.charAt(0)!="/"){
var path=_2d1.path.substring(0,_2d1.path.lastIndexOf("/")+1)+_2d0.path;
var segs=path.split("/");
for(var j=0;j<segs.length;j++){
if(segs[j]=="."){
if(j==segs.length-1){
segs[j]="";
}else{
segs.splice(j,1);
j--;
}
}else{
if(j>0&&!(j==1&&segs[0]=="")&&segs[j]==".."&&segs[j-1]!=".."){
if(j==segs.length-1){
segs.splice(j,1);
segs[j-1]="";
}else{
segs.splice(j-1,2);
j-=2;
}
}
}
}
_2d0.path=segs.join("/");
}
}
}
}
uri="";
if(_2d0.scheme!=null){
uri+=_2d0.scheme+":";
}
if(_2d0.authority!=null){
uri+="//"+_2d0.authority;
}
uri+=_2d0.path;
if(_2d0.query!=null){
uri+="?"+_2d0.query;
}
if(_2d0.fragment!=null){
uri+="#"+_2d0.fragment;
}
}
this.uri=uri.toString();
var _2d5="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";
var r=this.uri.match(new RegExp(_2d5));
this.scheme=r[2]||(r[1]?"":null);
this.authority=r[4]||(r[3]?"":null);
this.path=r[5];
this.query=r[7]||(r[6]?"":null);
this.fragment=r[9]||(r[8]?"":null);
if(this.authority!=null){
_2d5="^((([^:]+:)?([^@]+))@)?([^:]*)(:([0-9]+))?$";
r=this.authority.match(new RegExp(_2d5));
this.user=r[3]||null;
this.password=r[4]||null;
this.host=r[5];
this.port=r[7]||null;
}
this.toString=function(){
return this.uri;
};
};
};
dojo.kwCompoundRequire({common:[["dojo.uri.Uri",false,false]]});
dojo.provide("dojo.uri.*");
dojo.provide("dojo.io.IframeIO");
dojo.io.createIFrame=function(_2d7,_2d8,uri){
if(window[_2d7]){
return window[_2d7];
}
if(window.frames[_2d7]){
return window.frames[_2d7];
}
var r=dojo.render.html;
var _2db=null;
var turi=uri;
if(!turi){
if(djConfig["useXDomain"]&&!djConfig["dojoIframeHistoryUrl"]){
dojo.debug("dojo.io.createIFrame: When using cross-domain Dojo builds,"+" please save iframe_history.html to your domain and set djConfig.dojoIframeHistoryUrl"+" to the path on your domain to iframe_history.html");
}
turi=(djConfig["dojoIframeHistoryUrl"]||dojo.uri.moduleUri("dojo","../iframe_history.html"))+"#noInit=true";
}
var _2dd=((r.ie)&&(dojo.render.os.win))?"<iframe name=\""+_2d7+"\" src=\""+turi+"\" onload=\""+_2d8+"\">":"iframe";
_2db=document.createElement(_2dd);
with(_2db){
name=_2d7;
setAttribute("name",_2d7);
id=_2d7;
}
dojo.body().appendChild(_2db);
window[_2d7]=_2db;
with(_2db.style){
if(!r.safari){
position="absolute";
}
left=top="0px";
height=width="1px";
visibility="hidden";
}
if(!r.ie){
dojo.io.setIFrameSrc(_2db,turi,true);
_2db.onload=new Function(_2d8);
}
return _2db;
};
dojo.io.IframeTransport=new function(){
var _2de=this;
this.currentRequest=null;
this.requestQueue=[];
this.iframeName="dojoIoIframe";
this.fireNextRequest=function(){
try{
if((this.currentRequest)||(this.requestQueue.length==0)){
return;
}
var cr=this.currentRequest=this.requestQueue.shift();
cr._contentToClean=[];
var fn=cr["formNode"];
var _2e1=cr["content"]||{};
if(cr.sendTransport){
_2e1["dojo.transport"]="iframe";
}
if(fn){
if(_2e1){
for(var x in _2e1){
if(!fn[x]){
var tn;
if(dojo.render.html.ie){
tn=document.createElement("<input type='hidden' name='"+x+"' value='"+_2e1[x]+"'>");
fn.appendChild(tn);
}else{
tn=document.createElement("input");
fn.appendChild(tn);
tn.type="hidden";
tn.name=x;
tn.value=_2e1[x];
}
cr._contentToClean.push(x);
}else{
fn[x].value=_2e1[x];
}
}
}
if(cr["url"]){
cr._originalAction=fn.getAttribute("action");
fn.setAttribute("action",cr.url);
}
if(!fn.getAttribute("method")){
fn.setAttribute("method",(cr["method"])?cr["method"]:"post");
}
cr._originalTarget=fn.getAttribute("target");
fn.setAttribute("target",this.iframeName);
fn.target=this.iframeName;
fn.submit();
}else{
var _2e4=dojo.io.argsFromMap(this.currentRequest.content);
var _2e5=cr.url+(cr.url.indexOf("?")>-1?"&":"?")+_2e4;
dojo.io.setIFrameSrc(this.iframe,_2e5,true);
}
}
catch(e){
this.iframeOnload(e);
}
};
this.canHandle=function(_2e6){
return ((dojo.lang.inArray(["text/plain","text/html","text/javascript","text/json","application/json"],_2e6["mimetype"]))&&(dojo.lang.inArray(["post","get"],_2e6["method"].toLowerCase()))&&(!((_2e6["sync"])&&(_2e6["sync"]==true))));
};
this.bind=function(_2e7){
if(!this["iframe"]){
this.setUpIframe();
}
this.requestQueue.push(_2e7);
this.fireNextRequest();
return;
};
this.setUpIframe=function(){
this.iframe=dojo.io.createIFrame(this.iframeName,"dojo.io.IframeTransport.iframeOnload();");
};
this.iframeOnload=function(_2e8){
if(!_2de.currentRequest){
_2de.fireNextRequest();
return;
}
var req=_2de.currentRequest;
if(req.formNode){
var _2ea=req._contentToClean;
for(var i=0;i<_2ea.length;i++){
var key=_2ea[i];
if(dojo.render.html.safari){
var _2ed=req.formNode;
for(var j=0;j<_2ed.childNodes.length;j++){
var _2ef=_2ed.childNodes[j];
if(_2ef.name==key){
var _2f0=_2ef.parentNode;
_2f0.removeChild(_2ef);
break;
}
}
}else{
var _2f1=req.formNode[key];
req.formNode.removeChild(_2f1);
req.formNode[key]=null;
}
}
if(req["_originalAction"]){
req.formNode.setAttribute("action",req._originalAction);
}
if(req["_originalTarget"]){
req.formNode.setAttribute("target",req._originalTarget);
req.formNode.target=req._originalTarget;
}
}
var _2f2=function(_2f3){
var doc=_2f3.contentDocument||((_2f3.contentWindow)&&(_2f3.contentWindow.document))||((_2f3.name)&&(document.frames[_2f3.name])&&(document.frames[_2f3.name].document))||null;
return doc;
};
var _2f5;
var _2f6=false;
if(_2e8){
this._callError(req,"IframeTransport Request Error: "+_2e8);
}else{
var ifd=_2f2(_2de.iframe);
try{
var cmt=req.mimetype;
if((cmt=="text/javascript")||(cmt=="text/json")||(cmt=="application/json")){
var js=ifd.getElementsByTagName("textarea")[0].value;
if(cmt=="text/json"||cmt=="application/json"){
js="("+js+")";
}
_2f5=dj_eval(js);
}else{
if(cmt=="text/html"){
_2f5=ifd;
}else{
_2f5=ifd.getElementsByTagName("textarea")[0].value;
}
}
_2f6=true;
}
catch(e){
this._callError(req,"IframeTransport Error: "+e);
}
}
try{
if(_2f6&&dojo.lang.isFunction(req["load"])){
req.load("load",_2f5,req);
}
}
catch(e){
throw e;
}
finally{
_2de.currentRequest=null;
_2de.fireNextRequest();
}
};
this._callError=function(req,_2fb){
var _2fc=new dojo.io.Error(_2fb);
if(dojo.lang.isFunction(req["error"])){
req.error("error",_2fc,req);
}
};
dojo.io.transports.addTransport("IframeTransport");
};
dojo.provide("dojo.io.ScriptSrcIO");
dojo.io.ScriptSrcTransport=new function(){
this.preventCache=false;
this.maxUrlLength=1000;
this.inFlightTimer=null;
this.DsrStatusCodes={Continue:100,Ok:200,Error:500};
this.startWatchingInFlight=function(){
if(!this.inFlightTimer){
this.inFlightTimer=setInterval("dojo.io.ScriptSrcTransport.watchInFlight();",100);
}
};
this.watchInFlight=function(){
var _2fd=0;
var _2fe=0;
for(var _2ff in this._state){
_2fd++;
var _300=this._state[_2ff];
if(_300.isDone){
_2fe++;
delete this._state[_2ff];
}else{
if(!_300.isFinishing){
var _301=_300.kwArgs;
try{
if(_300.checkString&&eval("typeof("+_300.checkString+") != 'undefined'")){
_300.isFinishing=true;
this._finish(_300,"load");
_2fe++;
delete this._state[_2ff];
}else{
if(_301.timeoutSeconds&&_301.timeout){
if(_300.startTime+(_301.timeoutSeconds*1000)<(new Date()).getTime()){
_300.isFinishing=true;
this._finish(_300,"timeout");
_2fe++;
delete this._state[_2ff];
}
}else{
if(!_301.timeoutSeconds){
_2fe++;
}
}
}
}
catch(e){
_300.isFinishing=true;
this._finish(_300,"error",{status:this.DsrStatusCodes.Error,response:e});
}
}
}
}
if(_2fe>=_2fd){
clearInterval(this.inFlightTimer);
this.inFlightTimer=null;
}
};
this.canHandle=function(_302){
return dojo.lang.inArray(["text/javascript","text/json","application/json"],(_302["mimetype"].toLowerCase()))&&(_302["method"].toLowerCase()=="get")&&!(_302["formNode"]&&dojo.io.formHasFile(_302["formNode"]))&&(!_302["sync"]||_302["sync"]==false)&&!_302["file"]&&!_302["multipart"];
};
this.removeScripts=function(){
var _303=document.getElementsByTagName("script");
for(var i=0;_303&&i<_303.length;i++){
var _305=_303[i];
if(_305.className=="ScriptSrcTransport"){
var _306=_305.parentNode;
_306.removeChild(_305);
i--;
}
}
};
this.bind=function(_307){
var url=_307.url;
var _309="";
if(_307["formNode"]){
var ta=_307.formNode.getAttribute("action");
if((ta)&&(!_307["url"])){
url=ta;
}
var tp=_307.formNode.getAttribute("method");
if((tp)&&(!_307["method"])){
_307.method=tp;
}
_309+=dojo.io.encodeForm(_307.formNode,_307.encoding,_307["formFilter"]);
}
if(url.indexOf("#")>-1){
dojo.debug("Warning: dojo.io.bind: stripping hash values from url:",url);
url=url.split("#")[0];
}
var _30c=url.split("?");
if(_30c&&_30c.length==2){
url=_30c[0];
_309+=(_309?"&":"")+_30c[1];
}
if(_307["backButton"]||_307["back"]||_307["changeUrl"]){
dojo.undo.browser.addToHistory(_307);
}
var id=_307["apiId"]?_307["apiId"]:"id"+this._counter++;
var _30e=_307["content"];
var _30f=_307.jsonParamName;
if(_307.sendTransport||_30f){
if(!_30e){
_30e={};
}
if(_307.sendTransport){
_30e["dojo.transport"]="scriptsrc";
}
if(_30f){
_30e[_30f]="dojo.io.ScriptSrcTransport._state."+id+".jsonpCall";
}
}
if(_307.postContent){
_309=_307.postContent;
}else{
if(_30e){
_309+=((_309)?"&":"")+dojo.io.argsFromMap(_30e,_307.encoding,_30f);
}
}
if(_307["apiId"]){
_307["useRequestId"]=true;
}
var _310={"id":id,"idParam":"_dsrid="+id,"url":url,"query":_309,"kwArgs":_307,"startTime":(new Date()).getTime(),"isFinishing":false};
if(!url){
this._finish(_310,"error",{status:this.DsrStatusCodes.Error,statusText:"url.none"});
return;
}
if(_30e&&_30e[_30f]){
_310.jsonp=_30e[_30f];
_310.jsonpCall=function(data){
if(data["Error"]||data["error"]){
if(dojo["json"]&&dojo["json"]["serialize"]){
dojo.debug(dojo.json.serialize(data));
}
dojo.io.ScriptSrcTransport._finish(this,"error",data);
}else{
dojo.io.ScriptSrcTransport._finish(this,"load",data);
}
};
}
if(_307["useRequestId"]||_307["checkString"]||_310["jsonp"]){
this._state[id]=_310;
}
if(_307["checkString"]){
_310.checkString=_307["checkString"];
}
_310.constantParams=(_307["constantParams"]==null?"":_307["constantParams"]);
if(_307["preventCache"]||(this.preventCache==true&&_307["preventCache"]!=false)){
_310.nocacheParam="dojo.preventCache="+new Date().valueOf();
}else{
_310.nocacheParam="";
}
var _312=_310.url.length+_310.query.length+_310.constantParams.length+_310.nocacheParam.length+this._extraPaddingLength;
if(_307["useRequestId"]){
_312+=_310.idParam.length;
}
if(!_307["checkString"]&&_307["useRequestId"]&&!_310["jsonp"]&&!_307["forceSingleRequest"]&&_312>this.maxUrlLength){
if(url>this.maxUrlLength){
this._finish(_310,"error",{status:this.DsrStatusCodes.Error,statusText:"url.tooBig"});
return;
}else{
this._multiAttach(_310,1);
}
}else{
var _313=[_310.constantParams,_310.nocacheParam,_310.query];
if(_307["useRequestId"]&&!_310["jsonp"]){
_313.unshift(_310.idParam);
}
var _314=this._buildUrl(_310.url,_313);
_310.finalUrl=_314;
this._attach(_310.id,_314);
}
this.startWatchingInFlight();
};
this._counter=1;
this._state={};
this._extraPaddingLength=16;
this._buildUrl=function(url,_316){
var _317=url;
var _318="?";
for(var i=0;i<_316.length;i++){
if(_316[i]){
_317+=_318+_316[i];
_318="&";
}
}
return _317;
};
this._attach=function(id,url){
var _31c=document.createElement("script");
_31c.type="text/javascript";
_31c.src=url;
_31c.id=id;
_31c.className="ScriptSrcTransport";
document.getElementsByTagName("head")[0].appendChild(_31c);
};
this._multiAttach=function(_31d,part){
if(_31d.query==null){
this._finish(_31d,"error",{status:this.DsrStatusCodes.Error,statusText:"query.null"});
return;
}
if(!_31d.constantParams){
_31d.constantParams="";
}
var _31f=this.maxUrlLength-_31d.idParam.length-_31d.constantParams.length-_31d.url.length-_31d.nocacheParam.length-this._extraPaddingLength;
var _320=_31d.query.length<_31f;
var _321;
if(_320){
_321=_31d.query;
_31d.query=null;
}else{
var _322=_31d.query.lastIndexOf("&",_31f-1);
var _323=_31d.query.lastIndexOf("=",_31f-1);
if(_322>_323||_323==_31f-1){
_321=_31d.query.substring(0,_322);
_31d.query=_31d.query.substring(_322+1,_31d.query.length);
}else{
_321=_31d.query.substring(0,_31f);
var _324=_321.substring((_322==-1?0:_322+1),_323);
_31d.query=_324+"="+_31d.query.substring(_31f,_31d.query.length);
}
}
var _325=[_321,_31d.idParam,_31d.constantParams,_31d.nocacheParam];
if(!_320){
_325.push("_part="+part);
}
var url=this._buildUrl(_31d.url,_325);
this._attach(_31d.id+"_"+part,url);
};
this._finish=function(_327,_328,_329){
if(_328!="partOk"&&!_327.kwArgs[_328]&&!_327.kwArgs["handle"]){
if(_328=="error"){
_327.isDone=true;
throw _329;
}
}else{
switch(_328){
case "load":
var _32a=_329?_329.response:null;
if(!_32a){
_32a=_329;
}
_327.kwArgs[(typeof _327.kwArgs.load=="function")?"load":"handle"]("load",_32a,_329,_327.kwArgs);
_327.isDone=true;
break;
case "partOk":
var part=parseInt(_329.response.part,10)+1;
if(_329.response.constantParams){
_327.constantParams=_329.response.constantParams;
}
this._multiAttach(_327,part);
_327.isDone=false;
break;
case "error":
_327.kwArgs[(typeof _327.kwArgs.error=="function")?"error":"handle"]("error",_329.response,_329,_327.kwArgs);
_327.isDone=true;
break;
default:
_327.kwArgs[(typeof _327.kwArgs[_328]=="function")?_328:"handle"](_328,_329,_329,_327.kwArgs);
_327.isDone=true;
}
}
};
dojo.io.transports.addTransport("ScriptSrcTransport");
};
if(typeof window!="undefined"){
window.onscriptload=function(_32c){
var _32d=null;
var _32e=dojo.io.ScriptSrcTransport;
if(_32e._state[_32c.id]){
_32d=_32e._state[_32c.id];
}else{
var _32f;
for(var _330 in _32e._state){
_32f=_32e._state[_330];
if(_32f.finalUrl&&_32f.finalUrl==_32c.id){
_32d=_32f;
break;
}
}
if(_32d==null){
var _331=document.getElementsByTagName("script");
for(var i=0;_331&&i<_331.length;i++){
var _333=_331[i];
if(_333.getAttribute("class")=="ScriptSrcTransport"&&_333.src==_32c.id){
_32d=_32e._state[_333.id];
break;
}
}
}
if(_32d==null){
throw "No matching state for onscriptload event.id: "+_32c.id;
}
}
var _334="error";
switch(_32c.status){
case dojo.io.ScriptSrcTransport.DsrStatusCodes.Continue:
_334="partOk";
break;
case dojo.io.ScriptSrcTransport.DsrStatusCodes.Ok:
_334="load";
break;
}
_32e._finish(_32d,_334,_32c);
};
}
dojo.provide("dojo.io.cookie");
dojo.io.cookie.setCookie=function(name,_336,days,path,_339,_33a){
var _33b=-1;
if((typeof days=="number")&&(days>=0)){
var d=new Date();
d.setTime(d.getTime()+(days*24*60*60*1000));
_33b=d.toGMTString();
}
_336=escape(_336);
document.cookie=name+"="+_336+";"+(_33b!=-1?" expires="+_33b+";":"")+(path?"path="+path:"")+(_339?"; domain="+_339:"")+(_33a?"; secure":"");
};
dojo.io.cookie.set=dojo.io.cookie.setCookie;
dojo.io.cookie.getCookie=function(name){
var idx=document.cookie.lastIndexOf(name+"=");
if(idx==-1){
return null;
}
var _33f=document.cookie.substring(idx+name.length+1);
var end=_33f.indexOf(";");
if(end==-1){
end=_33f.length;
}
_33f=_33f.substring(0,end);
_33f=unescape(_33f);
return _33f;
};
dojo.io.cookie.get=dojo.io.cookie.getCookie;
dojo.io.cookie.deleteCookie=function(name){
dojo.io.cookie.setCookie(name,"-",0);
};
dojo.io.cookie.setObjectCookie=function(name,obj,days,path,_346,_347,_348){
if(arguments.length==5){
_348=_346;
_346=null;
_347=null;
}
var _349=[],_34a,_34b="";
if(!_348){
_34a=dojo.io.cookie.getObjectCookie(name);
}
if(days>=0){
if(!_34a){
_34a={};
}
for(var prop in obj){
if(obj[prop]==null){
delete _34a[prop];
}else{
if((typeof obj[prop]=="string")||(typeof obj[prop]=="number")){
_34a[prop]=obj[prop];
}
}
}
prop=null;
for(var prop in _34a){
_349.push(escape(prop)+"="+escape(_34a[prop]));
}
_34b=_349.join("&");
}
dojo.io.cookie.setCookie(name,_34b,days,path,_346,_347);
};
dojo.io.cookie.getObjectCookie=function(name){
var _34e=null,_34f=dojo.io.cookie.getCookie(name);
if(_34f){
_34e={};
var _350=_34f.split("&");
for(var i=0;i<_350.length;i++){
var pair=_350[i].split("=");
var _353=pair[1];
if(isNaN(_353)){
_353=unescape(pair[1]);
}
_34e[unescape(pair[0])]=_353;
}
}
return _34e;
};
dojo.io.cookie.isSupported=function(){
if(typeof navigator.cookieEnabled!="boolean"){
dojo.io.cookie.setCookie("__TestingYourBrowserForCookieSupport__","CookiesAllowed",90,null);
var _354=dojo.io.cookie.getCookie("__TestingYourBrowserForCookieSupport__");
navigator.cookieEnabled=(_354=="CookiesAllowed");
if(navigator.cookieEnabled){
this.deleteCookie("__TestingYourBrowserForCookieSupport__");
}
}
return navigator.cookieEnabled;
};
if(!dojo.io.cookies){
dojo.io.cookies=dojo.io.cookie;
}
dojo.provide("dojo.event.common");
dojo.event=new function(){
this._canTimeout=dojo.lang.isFunction(dj_global["setTimeout"])||dojo.lang.isAlien(dj_global["setTimeout"]);
function interpolateArgs(args,_356){
var dl=dojo.lang;
var ao={srcObj:dj_global,srcFunc:null,adviceObj:dj_global,adviceFunc:null,aroundObj:null,aroundFunc:null,adviceType:(args.length>2)?args[0]:"after",precedence:"last",once:false,delay:null,rate:0,adviceMsg:false,maxCalls:-1};
switch(args.length){
case 0:
return;
case 1:
return;
case 2:
ao.srcFunc=args[0];
ao.adviceFunc=args[1];
break;
case 3:
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isString(args[1]))&&(dl.isString(args[2]))){
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
}else{
if((dl.isObject(args[0]))&&(dl.isString(args[1]))&&(dl.isFunction(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
var _359=dl.nameAnonFunc(args[2],ao.adviceObj,_356);
ao.adviceFunc=_359;
}else{
if((dl.isFunction(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))){
ao.adviceType="after";
ao.srcObj=dj_global;
var _359=dl.nameAnonFunc(args[0],ao.srcObj,_356);
ao.srcFunc=_359;
ao.adviceObj=args[1];
ao.adviceFunc=args[2];
}
}
}
}
break;
case 4:
if((dl.isObject(args[0]))&&(dl.isObject(args[2]))){
ao.adviceType="after";
ao.srcObj=args[0];
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isString(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isFunction(args[1]))&&(dl.isObject(args[2]))){
ao.adviceType=args[0];
ao.srcObj=dj_global;
var _359=dl.nameAnonFunc(args[1],dj_global,_356);
ao.srcFunc=_359;
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
if((dl.isString(args[0]))&&(dl.isObject(args[1]))&&(dl.isString(args[2]))&&(dl.isFunction(args[3]))){
ao.srcObj=args[1];
ao.srcFunc=args[2];
var _359=dl.nameAnonFunc(args[3],dj_global,_356);
ao.adviceObj=dj_global;
ao.adviceFunc=_359;
}else{
if(dl.isObject(args[1])){
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=dj_global;
ao.adviceFunc=args[3];
}else{
if(dl.isObject(args[2])){
ao.srcObj=dj_global;
ao.srcFunc=args[1];
ao.adviceObj=args[2];
ao.adviceFunc=args[3];
}else{
ao.srcObj=ao.adviceObj=ao.aroundObj=dj_global;
ao.srcFunc=args[1];
ao.adviceFunc=args[2];
ao.aroundFunc=args[3];
}
}
}
}
}
}
break;
case 6:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundFunc=args[5];
ao.aroundObj=dj_global;
break;
default:
ao.srcObj=args[1];
ao.srcFunc=args[2];
ao.adviceObj=args[3];
ao.adviceFunc=args[4];
ao.aroundObj=args[5];
ao.aroundFunc=args[6];
ao.once=args[7];
ao.delay=args[8];
ao.rate=args[9];
ao.adviceMsg=args[10];
ao.maxCalls=(!isNaN(parseInt(args[11])))?args[11]:-1;
break;
}
if(dl.isFunction(ao.aroundFunc)){
var _359=dl.nameAnonFunc(ao.aroundFunc,ao.aroundObj,_356);
ao.aroundFunc=_359;
}
if(dl.isFunction(ao.srcFunc)){
ao.srcFunc=dl.getNameInObj(ao.srcObj,ao.srcFunc);
}
if(dl.isFunction(ao.adviceFunc)){
ao.adviceFunc=dl.getNameInObj(ao.adviceObj,ao.adviceFunc);
}
if((ao.aroundObj)&&(dl.isFunction(ao.aroundFunc))){
ao.aroundFunc=dl.getNameInObj(ao.aroundObj,ao.aroundFunc);
}
if(!ao.srcObj){
dojo.raise("bad srcObj for srcFunc: "+ao.srcFunc);
}
if(!ao.adviceObj){
dojo.raise("bad adviceObj for adviceFunc: "+ao.adviceFunc);
}
if(!ao.adviceFunc){
dojo.debug("bad adviceFunc for srcFunc: "+ao.srcFunc);
dojo.debugShallow(ao);
}
return ao;
}
this.connect=function(){
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments,true);
}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){
if(dojo.render.html.ie){
ao.srcFunc="onkeydown";
this.connect(ao);
}
ao.srcFunc="onkeypress";
}
if(dojo.lang.isArray(ao.srcObj)&&ao.srcObj!=""){
var _35b={};
for(var x in ao){
_35b[x]=ao[x];
}
var mjps=[];
dojo.lang.forEach(ao.srcObj,function(src){
if((dojo.render.html.capable)&&(dojo.lang.isString(src))){
src=dojo.byId(src);
}
_35b.srcObj=src;
mjps.push(dojo.event.connect.call(dojo.event,_35b));
});
return mjps;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc);
if(ao.adviceFunc){
var mjp2=dojo.event.MethodJoinPoint.getForMethod(ao.adviceObj,ao.adviceFunc);
}
mjp.kwAddAdvice(ao);
return mjp;
};
this.log=function(a1,a2){
var _363;
if((arguments.length==1)&&(typeof a1=="object")){
_363=a1;
}else{
_363={srcObj:a1,srcFunc:a2};
}
_363.adviceFunc=function(){
var _364=[];
for(var x=0;x<arguments.length;x++){
_364.push(arguments[x]);
}
dojo.debug("("+_363.srcObj+")."+_363.srcFunc,":",_364.join(", "));
};
this.kwConnect(_363);
};
this.connectBefore=function(){
var args=["before"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this.connectAround=function(){
var args=["around"];
for(var i=0;i<arguments.length;i++){
args.push(arguments[i]);
}
return this.connect.apply(this,args);
};
this.connectOnce=function(){
var ao=interpolateArgs(arguments,true);
ao.once=true;
return this.connect(ao);
};
this.connectRunOnce=function(){
var ao=interpolateArgs(arguments,true);
ao.maxCalls=1;
return this.connect(ao);
};
this._kwConnectImpl=function(_36c,_36d){
var fn=(_36d)?"disconnect":"connect";
if(typeof _36c["srcFunc"]=="function"){
_36c.srcObj=_36c["srcObj"]||dj_global;
var _36f=dojo.lang.nameAnonFunc(_36c.srcFunc,_36c.srcObj,true);
_36c.srcFunc=_36f;
}
if(typeof _36c["adviceFunc"]=="function"){
_36c.adviceObj=_36c["adviceObj"]||dj_global;
var _36f=dojo.lang.nameAnonFunc(_36c.adviceFunc,_36c.adviceObj,true);
_36c.adviceFunc=_36f;
}
_36c.srcObj=_36c["srcObj"]||dj_global;
_36c.adviceObj=_36c["adviceObj"]||_36c["targetObj"]||dj_global;
_36c.adviceFunc=_36c["adviceFunc"]||_36c["targetFunc"];
return dojo.event[fn](_36c);
};
this.kwConnect=function(_370){
return this._kwConnectImpl(_370,false);
};
this.disconnect=function(){
if(arguments.length==1){
var ao=arguments[0];
}else{
var ao=interpolateArgs(arguments,true);
}
if(!ao.adviceFunc){
return;
}
if(dojo.lang.isString(ao.srcFunc)&&(ao.srcFunc.toLowerCase()=="onkey")){
if(dojo.render.html.ie){
ao.srcFunc="onkeydown";
this.disconnect(ao);
}
ao.srcFunc="onkeypress";
}
if(!ao.srcObj[ao.srcFunc]){
return null;
}
var mjp=dojo.event.MethodJoinPoint.getForMethod(ao.srcObj,ao.srcFunc,true);
mjp.removeAdvice(ao.adviceObj,ao.adviceFunc,ao.adviceType,ao.once);
return mjp;
};
this.kwDisconnect=function(_373){
return this._kwConnectImpl(_373,true);
};
};
dojo.event.MethodInvocation=function(_374,obj,args){
this.jp_=_374;
this.object=obj;
this.args=[];
for(var x=0;x<args.length;x++){
this.args[x]=args[x];
}
this.around_index=-1;
};
dojo.event.MethodInvocation.prototype.proceed=function(){
this.around_index++;
if(this.around_index>=this.jp_.around.length){
return this.jp_.object[this.jp_.methodname].apply(this.jp_.object,this.args);
}else{
var ti=this.jp_.around[this.around_index];
var mobj=ti[0]||dj_global;
var meth=ti[1];
return mobj[meth].call(mobj,this);
}
};
dojo.event.MethodJoinPoint=function(obj,_37c){
this.object=obj||dj_global;
this.methodname=_37c;
this.methodfunc=this.object[_37c];
};
dojo.event.MethodJoinPoint.getForMethod=function(obj,_37e){
if(!obj){
obj=dj_global;
}
var ofn=obj[_37e];
if(!ofn){
ofn=obj[_37e]=function(){
};
if(!obj[_37e]){
dojo.raise("Cannot set do-nothing method on that object "+_37e);
}
}else{
if((typeof ofn!="function")&&(!dojo.lang.isFunction(ofn))&&(!dojo.lang.isAlien(ofn))){
return null;
}
}
var _380=_37e+"$joinpoint";
var _381=_37e+"$joinpoint$method";
var _382=obj[_380];
if(!_382){
var _383=false;
if(dojo.event["browser"]){
if((obj["attachEvent"])||(obj["nodeType"])||(obj["addEventListener"])){
_383=true;
dojo.event.browser.addClobberNodeAttrs(obj,[_380,_381,_37e]);
}
}
var _384=ofn.length;
obj[_381]=ofn;
_382=obj[_380]=new dojo.event.MethodJoinPoint(obj,_381);
if(!_383){
obj[_37e]=function(){
return _382.run.apply(_382,arguments);
};
}else{
obj[_37e]=function(){
var args=[];
if(!arguments.length){
var evt=null;
try{
if(obj.ownerDocument){
evt=obj.ownerDocument.parentWindow.event;
}else{
if(obj.documentElement){
evt=obj.documentElement.ownerDocument.parentWindow.event;
}else{
if(obj.event){
evt=obj.event;
}else{
evt=window.event;
}
}
}
}
catch(e){
evt=window.event;
}
if(evt){
args.push(dojo.event.browser.fixEvent(evt,this));
}
}else{
for(var x=0;x<arguments.length;x++){
if((x==0)&&(dojo.event.browser.isEvent(arguments[x]))){
args.push(dojo.event.browser.fixEvent(arguments[x],this));
}else{
args.push(arguments[x]);
}
}
}
return _382.run.apply(_382,args);
};
}
obj[_37e].__preJoinArity=_384;
}
return _382;
};
dojo.lang.extend(dojo.event.MethodJoinPoint,{squelch:false,unintercept:function(){
this.object[this.methodname]=this.methodfunc;
this.before=[];
this.after=[];
this.around=[];
},disconnect:dojo.lang.forward("unintercept"),run:function(){
var obj=this.object||dj_global;
var args=arguments;
var _38a=[];
for(var x=0;x<args.length;x++){
_38a[x]=args[x];
}
var _38c=function(marr){
if(!marr){
dojo.debug("Null argument to unrollAdvice()");
return;
}
var _38e=marr[0]||dj_global;
var _38f=marr[1];
if(!_38e[_38f]){
dojo.raise("function \""+_38f+"\" does not exist on \""+_38e+"\"");
}
var _390=marr[2]||dj_global;
var _391=marr[3];
var msg=marr[6];
var _393=marr[7];
if(_393>-1){
if(_393==0){
return;
}
marr[7]--;
}
var _394;
var to={args:[],jp_:this,object:obj,proceed:function(){
return _38e[_38f].apply(_38e,to.args);
}};
to.args=_38a;
var _396=parseInt(marr[4]);
var _397=((!isNaN(_396))&&(marr[4]!==null)&&(typeof marr[4]!="undefined"));
if(marr[5]){
var rate=parseInt(marr[5]);
var cur=new Date();
var _39a=false;
if((marr["last"])&&((cur-marr.last)<=rate)){
if(dojo.event._canTimeout){
if(marr["delayTimer"]){
clearTimeout(marr.delayTimer);
}
var tod=parseInt(rate*2);
var mcpy=dojo.lang.shallowCopy(marr);
marr.delayTimer=setTimeout(function(){
mcpy[5]=0;
_38c(mcpy);
},tod);
}
return;
}else{
marr.last=cur;
}
}
if(_391){
_390[_391].call(_390,to);
}else{
if((_397)&&((dojo.render.html)||(dojo.render.svg))){
dj_global["setTimeout"](function(){
if(msg){
_38e[_38f].call(_38e,to);
}else{
_38e[_38f].apply(_38e,args);
}
},_396);
}else{
if(msg){
_38e[_38f].call(_38e,to);
}else{
_38e[_38f].apply(_38e,args);
}
}
}
};
var _39d=function(){
if(this.squelch){
try{
return _38c.apply(this,arguments);
}
catch(e){
dojo.debug(e);
}
}else{
return _38c.apply(this,arguments);
}
};
if((this["before"])&&(this.before.length>0)){
dojo.lang.forEach(this.before.concat(new Array()),_39d);
}
var _39e;
try{
if((this["around"])&&(this.around.length>0)){
var mi=new dojo.event.MethodInvocation(this,obj,args);
_39e=mi.proceed();
}else{
if(this.methodfunc){
_39e=this.object[this.methodname].apply(this.object,args);
}
}
}
catch(e){
if(!this.squelch){
dojo.debug(e,"when calling",this.methodname,"on",this.object,"with arguments",args);
dojo.raise(e);
}
}
if((this["after"])&&(this.after.length>0)){
dojo.lang.forEach(this.after.concat(new Array()),_39d);
}
return (this.methodfunc)?_39e:null;
},getArr:function(kind){
var type="after";
if((typeof kind=="string")&&(kind.indexOf("before")!=-1)){
type="before";
}else{
if(kind=="around"){
type="around";
}
}
if(!this[type]){
this[type]=[];
}
return this[type];
},kwAddAdvice:function(args){
this.addAdvice(args["adviceObj"],args["adviceFunc"],args["aroundObj"],args["aroundFunc"],args["adviceType"],args["precedence"],args["once"],args["delay"],args["rate"],args["adviceMsg"],args["maxCalls"]);
},addAdvice:function(_3a3,_3a4,_3a5,_3a6,_3a7,_3a8,once,_3aa,rate,_3ac,_3ad){
var arr=this.getArr(_3a7);
if(!arr){
dojo.raise("bad this: "+this);
}
var ao=[_3a3,_3a4,_3a5,_3a6,_3aa,rate,_3ac,_3ad];
if(once){
if(this.hasAdvice(_3a3,_3a4,_3a7,arr)>=0){
return;
}
}
if(_3a8=="first"){
arr.unshift(ao);
}else{
arr.push(ao);
}
},hasAdvice:function(_3b0,_3b1,_3b2,arr){
if(!arr){
arr=this.getArr(_3b2);
}
var ind=-1;
for(var x=0;x<arr.length;x++){
var aao=(typeof _3b1=="object")?(new String(_3b1)).toString():_3b1;
var a1o=(typeof arr[x][1]=="object")?(new String(arr[x][1])).toString():arr[x][1];
if((arr[x][0]==_3b0)&&(a1o==aao)){
ind=x;
}
}
return ind;
},removeAdvice:function(_3b8,_3b9,_3ba,once){
var arr=this.getArr(_3ba);
var ind=this.hasAdvice(_3b8,_3b9,_3ba,arr);
if(ind==-1){
return false;
}
while(ind!=-1){
arr.splice(ind,1);
if(once){
break;
}
ind=this.hasAdvice(_3b8,_3b9,_3ba,arr);
}
return true;
}});
dojo.provide("dojo.event.topic");
dojo.event.topic=new function(){
this.topics={};
this.getTopic=function(_3be){
if(!this.topics[_3be]){
this.topics[_3be]=new this.TopicImpl(_3be);
}
return this.topics[_3be];
};
this.registerPublisher=function(_3bf,obj,_3c1){
var _3bf=this.getTopic(_3bf);
_3bf.registerPublisher(obj,_3c1);
};
this.subscribe=function(_3c2,obj,_3c4){
var _3c2=this.getTopic(_3c2);
_3c2.subscribe(obj,_3c4);
};
this.unsubscribe=function(_3c5,obj,_3c7){
var _3c5=this.getTopic(_3c5);
_3c5.unsubscribe(obj,_3c7);
};
this.destroy=function(_3c8){
this.getTopic(_3c8).destroy();
delete this.topics[_3c8];
};
this.publishApply=function(_3c9,args){
var _3c9=this.getTopic(_3c9);
_3c9.sendMessage.apply(_3c9,args);
};
this.publish=function(_3cb,_3cc){
var _3cb=this.getTopic(_3cb);
var args=[];
for(var x=1;x<arguments.length;x++){
args.push(arguments[x]);
}
_3cb.sendMessage.apply(_3cb,args);
};
};
dojo.event.topic.TopicImpl=function(_3cf){
this.topicName=_3cf;
this.subscribe=function(_3d0,_3d1){
var tf=_3d1||_3d0;
var to=(!_3d1)?dj_global:_3d0;
return dojo.event.kwConnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this.unsubscribe=function(_3d4,_3d5){
var tf=(!_3d5)?_3d4:_3d5;
var to=(!_3d5)?null:_3d4;
return dojo.event.kwDisconnect({srcObj:this,srcFunc:"sendMessage",adviceObj:to,adviceFunc:tf});
};
this._getJoinPoint=function(){
return dojo.event.MethodJoinPoint.getForMethod(this,"sendMessage");
};
this.setSquelch=function(_3d8){
this._getJoinPoint().squelch=_3d8;
};
this.destroy=function(){
this._getJoinPoint().disconnect();
};
this.registerPublisher=function(_3d9,_3da){
dojo.event.connect(_3d9,_3da,this,"sendMessage");
};
this.sendMessage=function(_3db){
};
};
dojo.provide("dojo.event.browser");
dojo._ie_clobber=new function(){
this.clobberNodes=[];
function nukeProp(node,prop){
try{
node[prop]=null;
}
catch(e){
}
try{
delete node[prop];
}
catch(e){
}
try{
node.removeAttribute(prop);
}
catch(e){
}
}
this.clobber=function(_3de){
var na;
var tna;
if(_3de){
tna=_3de.all||_3de.getElementsByTagName("*");
na=[_3de];
for(var x=0;x<tna.length;x++){
if(tna[x]["__doClobber__"]){
na.push(tna[x]);
}
}
}else{
try{
window.onload=null;
}
catch(e){
}
na=(this.clobberNodes.length)?this.clobberNodes:document.all;
}
tna=null;
var _3e2={};
for(var i=na.length-1;i>=0;i=i-1){
var el=na[i];
try{
if(el&&el["__clobberAttrs__"]){
for(var j=0;j<el.__clobberAttrs__.length;j++){
nukeProp(el,el.__clobberAttrs__[j]);
}
nukeProp(el,"__clobberAttrs__");
nukeProp(el,"__doClobber__");
}
}
catch(e){
}
}
na=null;
};
};
if(dojo.render.html.ie){
dojo.addOnUnload(function(){
dojo._ie_clobber.clobber();
try{
if((dojo["widget"])&&(dojo.widget["manager"])){
dojo.widget.manager.destroyAll();
}
}
catch(e){
}
if(dojo.widget){
for(var name in dojo.widget._templateCache){
if(dojo.widget._templateCache[name].node){
dojo.dom.destroyNode(dojo.widget._templateCache[name].node);
dojo.widget._templateCache[name].node=null;
delete dojo.widget._templateCache[name].node;
}
}
}
try{
window.onload=null;
}
catch(e){
}
try{
window.onunload=null;
}
catch(e){
}
dojo._ie_clobber.clobberNodes=[];
});
}
dojo.event.browser=new function(){
var _3e7=0;
this.normalizedEventName=function(_3e8){
switch(_3e8){
case "CheckboxStateChange":
case "DOMAttrModified":
case "DOMMenuItemActive":
case "DOMMenuItemInactive":
case "DOMMouseScroll":
case "DOMNodeInserted":
case "DOMNodeRemoved":
case "RadioStateChange":
return _3e8;
break;
default:
var lcn=_3e8.toLowerCase();
return (lcn.indexOf("on")==0)?lcn.substr(2):lcn;
break;
}
};
this.clean=function(node){
if(dojo.render.html.ie){
dojo._ie_clobber.clobber(node);
}
};
this.addClobberNode=function(node){
if(!dojo.render.html.ie){
return;
}
if(!node["__doClobber__"]){
node.__doClobber__=true;
dojo._ie_clobber.clobberNodes.push(node);
node.__clobberAttrs__=[];
}
};
this.addClobberNodeAttrs=function(node,_3ed){
if(!dojo.render.html.ie){
return;
}
this.addClobberNode(node);
for(var x=0;x<_3ed.length;x++){
node.__clobberAttrs__.push(_3ed[x]);
}
};
this.removeListener=function(node,_3f0,fp,_3f2){
if(!_3f2){
var _3f2=false;
}
_3f0=dojo.event.browser.normalizedEventName(_3f0);
if(_3f0=="key"){
if(dojo.render.html.ie){
this.removeListener(node,"onkeydown",fp,_3f2);
}
_3f0="keypress";
}
if(node.removeEventListener){
node.removeEventListener(_3f0,fp,_3f2);
}
};
this.addListener=function(node,_3f4,fp,_3f6,_3f7){
if(!node){
return;
}
if(!_3f6){
var _3f6=false;
}
_3f4=dojo.event.browser.normalizedEventName(_3f4);
if(_3f4=="key"){
if(dojo.render.html.ie){
this.addListener(node,"onkeydown",fp,_3f6,_3f7);
}
_3f4="keypress";
}
if(!_3f7){
var _3f8=function(evt){
if(!evt){
evt=window.event;
}
var ret=fp(dojo.event.browser.fixEvent(evt,this));
if(_3f6){
dojo.event.browser.stopEvent(evt);
}
return ret;
};
}else{
_3f8=fp;
}
if(node.addEventListener){
node.addEventListener(_3f4,_3f8,_3f6);
return _3f8;
}else{
_3f4="on"+_3f4;
if(typeof node[_3f4]=="function"){
var _3fb=node[_3f4];
node[_3f4]=function(e){
_3fb(e);
return _3f8(e);
};
}else{
node[_3f4]=_3f8;
}
if(dojo.render.html.ie){
this.addClobberNodeAttrs(node,[_3f4]);
}
return _3f8;
}
};
this.isEvent=function(obj){
return (typeof obj!="undefined")&&(obj)&&(typeof Event!="undefined")&&(obj.eventPhase);
};
this.currentEvent=null;
this.callListener=function(_3fe,_3ff){
if(typeof _3fe!="function"){
dojo.raise("listener not a function: "+_3fe);
}
dojo.event.browser.currentEvent.currentTarget=_3ff;
return _3fe.call(_3ff,dojo.event.browser.currentEvent);
};
this._stopPropagation=function(){
dojo.event.browser.currentEvent.cancelBubble=true;
};
this._preventDefault=function(){
dojo.event.browser.currentEvent.returnValue=false;
};
this.keys={KEY_BACKSPACE:8,KEY_TAB:9,KEY_CLEAR:12,KEY_ENTER:13,KEY_SHIFT:16,KEY_CTRL:17,KEY_ALT:18,KEY_PAUSE:19,KEY_CAPS_LOCK:20,KEY_ESCAPE:27,KEY_SPACE:32,KEY_PAGE_UP:33,KEY_PAGE_DOWN:34,KEY_END:35,KEY_HOME:36,KEY_LEFT_ARROW:37,KEY_UP_ARROW:38,KEY_RIGHT_ARROW:39,KEY_DOWN_ARROW:40,KEY_INSERT:45,KEY_DELETE:46,KEY_HELP:47,KEY_LEFT_WINDOW:91,KEY_RIGHT_WINDOW:92,KEY_SELECT:93,KEY_NUMPAD_0:96,KEY_NUMPAD_1:97,KEY_NUMPAD_2:98,KEY_NUMPAD_3:99,KEY_NUMPAD_4:100,KEY_NUMPAD_5:101,KEY_NUMPAD_6:102,KEY_NUMPAD_7:103,KEY_NUMPAD_8:104,KEY_NUMPAD_9:105,KEY_NUMPAD_MULTIPLY:106,KEY_NUMPAD_PLUS:107,KEY_NUMPAD_ENTER:108,KEY_NUMPAD_MINUS:109,KEY_NUMPAD_PERIOD:110,KEY_NUMPAD_DIVIDE:111,KEY_F1:112,KEY_F2:113,KEY_F3:114,KEY_F4:115,KEY_F5:116,KEY_F6:117,KEY_F7:118,KEY_F8:119,KEY_F9:120,KEY_F10:121,KEY_F11:122,KEY_F12:123,KEY_F13:124,KEY_F14:125,KEY_F15:126,KEY_NUM_LOCK:144,KEY_SCROLL_LOCK:145};
this.revKeys=[];
for(var key in this.keys){
this.revKeys[this.keys[key]]=key;
}
this.fixEvent=function(evt,_402){
if(!evt){
if(window["event"]){
evt=window.event;
}
}
if((evt["type"])&&(evt["type"].indexOf("key")==0)){
evt.keys=this.revKeys;
for(var key in this.keys){
evt[key]=this.keys[key];
}
if(evt["type"]=="keydown"&&dojo.render.html.ie){
switch(evt.keyCode){
case evt.KEY_SHIFT:
case evt.KEY_CTRL:
case evt.KEY_ALT:
case evt.KEY_CAPS_LOCK:
case evt.KEY_LEFT_WINDOW:
case evt.KEY_RIGHT_WINDOW:
case evt.KEY_SELECT:
case evt.KEY_NUM_LOCK:
case evt.KEY_SCROLL_LOCK:
case evt.KEY_NUMPAD_0:
case evt.KEY_NUMPAD_1:
case evt.KEY_NUMPAD_2:
case evt.KEY_NUMPAD_3:
case evt.KEY_NUMPAD_4:
case evt.KEY_NUMPAD_5:
case evt.KEY_NUMPAD_6:
case evt.KEY_NUMPAD_7:
case evt.KEY_NUMPAD_8:
case evt.KEY_NUMPAD_9:
case evt.KEY_NUMPAD_PERIOD:
break;
case evt.KEY_NUMPAD_MULTIPLY:
case evt.KEY_NUMPAD_PLUS:
case evt.KEY_NUMPAD_ENTER:
case evt.KEY_NUMPAD_MINUS:
case evt.KEY_NUMPAD_DIVIDE:
break;
case evt.KEY_PAUSE:
case evt.KEY_TAB:
case evt.KEY_BACKSPACE:
case evt.KEY_ENTER:
case evt.KEY_ESCAPE:
case evt.KEY_PAGE_UP:
case evt.KEY_PAGE_DOWN:
case evt.KEY_END:
case evt.KEY_HOME:
case evt.KEY_LEFT_ARROW:
case evt.KEY_UP_ARROW:
case evt.KEY_RIGHT_ARROW:
case evt.KEY_DOWN_ARROW:
case evt.KEY_INSERT:
case evt.KEY_DELETE:
case evt.KEY_F1:
case evt.KEY_F2:
case evt.KEY_F3:
case evt.KEY_F4:
case evt.KEY_F5:
case evt.KEY_F6:
case evt.KEY_F7:
case evt.KEY_F8:
case evt.KEY_F9:
case evt.KEY_F10:
case evt.KEY_F11:
case evt.KEY_F12:
case evt.KEY_F12:
case evt.KEY_F13:
case evt.KEY_F14:
case evt.KEY_F15:
case evt.KEY_CLEAR:
case evt.KEY_HELP:
evt.key=evt.keyCode;
break;
default:
if(evt.ctrlKey||evt.altKey){
var _404=evt.keyCode;
if(_404>=65&&_404<=90&&evt.shiftKey==false){
_404+=32;
}
if(_404>=1&&_404<=26&&evt.ctrlKey){
_404+=96;
}
evt.key=String.fromCharCode(_404);
}
}
}else{
if(evt["type"]=="keypress"){
if(dojo.render.html.opera){
if(evt.which==0){
evt.key=evt.keyCode;
}else{
if(evt.which>0){
switch(evt.which){
case evt.KEY_SHIFT:
case evt.KEY_CTRL:
case evt.KEY_ALT:
case evt.KEY_CAPS_LOCK:
case evt.KEY_NUM_LOCK:
case evt.KEY_SCROLL_LOCK:
break;
case evt.KEY_PAUSE:
case evt.KEY_TAB:
case evt.KEY_BACKSPACE:
case evt.KEY_ENTER:
case evt.KEY_ESCAPE:
evt.key=evt.which;
break;
default:
var _404=evt.which;
if((evt.ctrlKey||evt.altKey||evt.metaKey)&&(evt.which>=65&&evt.which<=90&&evt.shiftKey==false)){
_404+=32;
}
evt.key=String.fromCharCode(_404);
}
}
}
}else{
if(dojo.render.html.ie){
if(!evt.ctrlKey&&!evt.altKey&&evt.keyCode>=evt.KEY_SPACE){
evt.key=String.fromCharCode(evt.keyCode);
}
}else{
if(dojo.render.html.safari){
switch(evt.keyCode){
case 25:
evt.key=evt.KEY_TAB;
evt.shift=true;
break;
case 63232:
evt.key=evt.KEY_UP_ARROW;
break;
case 63233:
evt.key=evt.KEY_DOWN_ARROW;
break;
case 63234:
evt.key=evt.KEY_LEFT_ARROW;
break;
case 63235:
evt.key=evt.KEY_RIGHT_ARROW;
break;
case 63236:
evt.key=evt.KEY_F1;
break;
case 63237:
evt.key=evt.KEY_F2;
break;
case 63238:
evt.key=evt.KEY_F3;
break;
case 63239:
evt.key=evt.KEY_F4;
break;
case 63240:
evt.key=evt.KEY_F5;
break;
case 63241:
evt.key=evt.KEY_F6;
break;
case 63242:
evt.key=evt.KEY_F7;
break;
case 63243:
evt.key=evt.KEY_F8;
break;
case 63244:
evt.key=evt.KEY_F9;
break;
case 63245:
evt.key=evt.KEY_F10;
break;
case 63246:
evt.key=evt.KEY_F11;
break;
case 63247:
evt.key=evt.KEY_F12;
break;
case 63250:
evt.key=evt.KEY_PAUSE;
break;
case 63272:
evt.key=evt.KEY_DELETE;
break;
case 63273:
evt.key=evt.KEY_HOME;
break;
case 63275:
evt.key=evt.KEY_END;
break;
case 63276:
evt.key=evt.KEY_PAGE_UP;
break;
case 63277:
evt.key=evt.KEY_PAGE_DOWN;
break;
case 63302:
evt.key=evt.KEY_INSERT;
break;
case 63248:
case 63249:
case 63289:
break;
default:
evt.key=evt.charCode>=evt.KEY_SPACE?String.fromCharCode(evt.charCode):evt.keyCode;
}
}else{
evt.key=evt.charCode>0?String.fromCharCode(evt.charCode):evt.keyCode;
}
}
}
}
}
}
if(dojo.render.html.ie){
if(!evt.target){
evt.target=evt.srcElement;
}
if(!evt.currentTarget){
evt.currentTarget=(_402?_402:evt.srcElement);
}
if(!evt.layerX){
evt.layerX=evt.offsetX;
}
if(!evt.layerY){
evt.layerY=evt.offsetY;
}
var doc=(evt.srcElement&&evt.srcElement.ownerDocument)?evt.srcElement.ownerDocument:document;
var _406=((dojo.render.html.ie55)||(doc["compatMode"]=="BackCompat"))?doc.body:doc.documentElement;
if(!evt.pageX){
evt.pageX=evt.clientX+(_406.scrollLeft||0);
}
if(!evt.pageY){
evt.pageY=evt.clientY+(_406.scrollTop||0);
}
if(evt.type=="mouseover"){
evt.relatedTarget=evt.fromElement;
}
if(evt.type=="mouseout"){
evt.relatedTarget=evt.toElement;
}
this.currentEvent=evt;
evt.callListener=this.callListener;
evt.stopPropagation=this._stopPropagation;
evt.preventDefault=this._preventDefault;
}
return evt;
};
this.stopEvent=function(evt){
if(window.event){
evt.cancelBubble=true;
evt.returnValue=false;
}else{
evt.preventDefault();
evt.stopPropagation();
}
};
};
dojo.kwCompoundRequire({common:["dojo.event.common","dojo.event.topic"],browser:["dojo.event.browser"],dashboard:["dojo.event.browser"]});
dojo.provide("dojo.event.*");
dojo.provide("dojo.io.cometd");
cometd=new function(){
this.initialized=false;
this.connected=false;
this.connectionTypes=new dojo.AdapterRegistry(true);
this.version=0.1;
this.minimumVersion=0.1;
this.clientId=null;
this._isXD=false;
this.handshakeReturn=null;
this.currentTransport=null;
this.url=null;
this.lastMessage=null;
this.globalTopicChannels={};
this.backlog=[];
this.tunnelInit=function(_408,_409){
};
this.tunnelCollapse=function(){
dojo.debug("tunnel collapsed!");
};
this.init=function(_40a,root,_40c){
_40a=_40a||{};
_40a.version=this.version;
_40a.minimumVersion=this.minimumVersion;
_40a.channel="/meta/handshake";
this.url=root||djConfig["cometdRoot"];
if(!this.url){
dojo.debug("no cometd root specified in djConfig and no root passed");
return;
}
var _40d={url:this.url,method:"POST",mimetype:"text/json",load:dojo.lang.hitch(this,"finishInit"),content:{"message":dojo.json.serialize([_40a])}};
var _40e="^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$";
var r=(""+window.location).match(new RegExp(_40e));
if(r[4]){
var tmp=r[4].split(":");
var _411=tmp[0];
var _412=tmp[1]||"80";
r=this.url.match(new RegExp(_40e));
if(r[4]){
tmp=r[4].split(":");
var _413=tmp[0];
var _414=tmp[1]||"80";
if((_413!=_411)||(_414!=_412)){
dojo.debug(_411,_413);
dojo.debug(_412,_414);
this._isXD=true;
_40d.transport="ScriptSrcTransport";
_40d.jsonParamName="jsonp";
_40d.method="GET";
}
}
}
if(_40c){
dojo.lang.mixin(_40d,_40c);
}
return dojo.io.bind(_40d);
};
this.finishInit=function(type,data,evt,_418){
data=data[0];
this.handshakeReturn=data;
if(data["authSuccessful"]==false){
dojo.debug("cometd authentication failed");
return;
}
if(data.version<this.minimumVersion){
dojo.debug("cometd protocol version mismatch. We wanted",this.minimumVersion,"but got",data.version);
return;
}
this.currentTransport=this.connectionTypes.match(data.supportedConnectionTypes,data.version,this._isXD);
this.currentTransport.version=data.version;
this.clientId=data.clientId;
this.tunnelInit=dojo.lang.hitch(this.currentTransport,"tunnelInit");
this.tunnelCollapse=dojo.lang.hitch(this.currentTransport,"tunnelCollapse");
this.initialized=true;
this.currentTransport.startup(data);
while(this.backlog.length!=0){
var cur=this.backlog.shift();
var fn=cur.shift();
this[fn].apply(this,cur);
}
};
this._getRandStr=function(){
return Math.random().toString().substring(2,10);
};
this.deliver=function(_41b){
dojo.lang.forEach(_41b,this._deliver,this);
};
this._deliver=function(_41c){
if(!_41c["channel"]){
dojo.debug("cometd error: no channel for message!");
return;
}
if(!this.currentTransport){
this.backlog.push(["deliver",_41c]);
return;
}
this.lastMessage=_41c;
if((_41c.channel.length>5)&&(_41c.channel.substr(0,5)=="/meta")){
switch(_41c.channel){
case "/meta/subscribe":
if(!_41c.successful){
dojo.debug("cometd subscription error for channel",_41c.channel,":",_41c.error);
return;
}
this.subscribed(_41c.subscription,_41c);
break;
case "/meta/unsubscribe":
if(!_41c.successful){
dojo.debug("cometd unsubscription error for channel",_41c.channel,":",_41c.error);
return;
}
this.unsubscribed(_41c.subscription,_41c);
break;
}
}
this.currentTransport.deliver(_41c);
if(_41c.data){
var _41d=(this.globalTopicChannels[_41c.channel])?_41c.channel:"/cometd"+_41c.channel;
dojo.event.topic.publish(_41d,_41c);
}
};
this.disconnect=function(){
if(!this.currentTransport){
dojo.debug("no current transport to disconnect from");
return;
}
this.currentTransport.disconnect();
};
this.publish=function(_41e,data,_420){
if(!this.currentTransport){
this.backlog.push(["publish",_41e,data,_420]);
return;
}
var _421={data:data,channel:_41e};
if(_420){
dojo.lang.mixin(_421,_420);
}
return this.currentTransport.sendMessage(_421);
};
this.subscribe=function(_422,_423,_424,_425){
if(!this.currentTransport){
this.backlog.push(["subscribe",_422,_423,_424,_425]);
return;
}
if(_424){
var _426=(_423)?_422:"/cometd"+_422;
if(_423){
this.globalTopicChannels[_422]=true;
}
dojo.event.topic.subscribe(_426,_424,_425);
}
return this.currentTransport.sendMessage({channel:"/meta/subscribe",subscription:_422});
};
this.subscribed=function(_427,_428){
dojo.debug(_427);
dojo.debugShallow(_428);
};
this.unsubscribe=function(_429,_42a,_42b,_42c){
if(!this.currentTransport){
this.backlog.push(["unsubscribe",_429,_42a,_42b,_42c]);
return;
}
if(_42b){
var _42d=(_42a)?_429:"/cometd"+_429;
dojo.event.topic.unsubscribe(_42d,_42b,_42c);
}
return this.currentTransport.sendMessage({channel:"/meta/unsubscribe",subscription:_429});
};
this.unsubscribed=function(_42e,_42f){
dojo.debug(_42e);
dojo.debugShallow(_42f);
};
};
cometd.iframeTransport=new function(){
this.connected=false;
this.connectionId=null;
this.rcvNode=null;
this.rcvNodeName="";
this.phonyForm=null;
this.authToken=null;
this.lastTimestamp=null;
this.lastId=null;
this.backlog=[];
this.check=function(_430,_431,_432){
return ((!_432)&&(!dojo.render.html.safari)&&(dojo.lang.inArray(_430,"iframe")));
};
this.tunnelInit=function(){
this.postToIframe({message:dojo.json.serialize([{channel:"/meta/connect",clientId:cometd.clientId,connectionType:"iframe"}])});
};
this.tunnelCollapse=function(){
if(this.connected){
this.connected=false;
this.postToIframe({message:dojo.json.serialize([{channel:"/meta/reconnect",clientId:cometd.clientId,connectionId:this.connectionId,timestamp:this.lastTimestamp,id:this.lastId}])});
}
};
this.deliver=function(_433){
if(_433["timestamp"]){
this.lastTimestamp=_433.timestamp;
}
if(_433["id"]){
this.lastId=_433.id;
}
if((_433.channel.length>5)&&(_433.channel.substr(0,5)=="/meta")){
switch(_433.channel){
case "/meta/connect":
if(!_433.successful){
dojo.debug("cometd connection error:",_433.error);
return;
}
this.connectionId=_433.connectionId;
this.connected=true;
this.processBacklog();
break;
case "/meta/reconnect":
if(!_433.successful){
dojo.debug("cometd reconnection error:",_433.error);
return;
}
this.connected=true;
break;
case "/meta/subscribe":
if(!_433.successful){
dojo.debug("cometd subscription error for channel",_433.channel,":",_433.error);
return;
}
dojo.debug(_433.channel);
break;
}
}
};
this.widenDomain=function(_434){
var cd=_434||document.domain;
if(cd.indexOf(".")==-1){
return;
}
var dps=cd.split(".");
if(dps.length<=2){
return;
}
dps=dps.slice(dps.length-2);
document.domain=dps.join(".");
return document.domain;
};
this.postToIframe=function(_437,url){
if(!this.phonyForm){
if(dojo.render.html.ie){
this.phonyForm=document.createElement("<form enctype='application/x-www-form-urlencoded' method='POST' style='display: none;'>");
dojo.body().appendChild(this.phonyForm);
}else{
this.phonyForm=document.createElement("form");
this.phonyForm.style.display="none";
dojo.body().appendChild(this.phonyForm);
this.phonyForm.enctype="application/x-www-form-urlencoded";
this.phonyForm.method="POST";
}
}
this.phonyForm.action=url||cometd.url;
this.phonyForm.target=this.rcvNodeName;
this.phonyForm.setAttribute("target",this.rcvNodeName);
while(this.phonyForm.firstChild){
this.phonyForm.removeChild(this.phonyForm.firstChild);
}
for(var x in _437){
var tn;
if(dojo.render.html.ie){
tn=document.createElement("<input type='hidden' name='"+x+"' value='"+_437[x]+"'>");
this.phonyForm.appendChild(tn);
}else{
tn=document.createElement("input");
this.phonyForm.appendChild(tn);
tn.type="hidden";
tn.name=x;
tn.value=_437[x];
}
}
this.phonyForm.submit();
};
this.processBacklog=function(){
while(this.backlog.length>0){
this.sendMessage(this.backlog.shift(),true);
}
};
this.sendMessage=function(_43b,_43c){
if((_43c)||(this.connected)){
_43b.connectionId=this.connectionId;
_43b.clientId=cometd.clientId;
var _43d={url:cometd.url||djConfig["cometdRoot"],method:"POST",mimetype:"text/json",content:{message:dojo.json.serialize([_43b])}};
return dojo.io.bind(_43d);
}else{
this.backlog.push(_43b);
}
};
this.startup=function(_43e){
dojo.debug("startup!");
dojo.debug(dojo.json.serialize(_43e));
if(this.connected){
return;
}
this.rcvNodeName="cometdRcv_"+cometd._getRandStr();
var _43f=cometd.url+"/?tunnelInit=iframe";
if(false&&dojo.render.html.ie){
this.rcvNode=new ActiveXObject("htmlfile");
this.rcvNode.open();
this.rcvNode.write("<html>");
this.rcvNode.write("<script>document.domain = '"+document.domain+"'");
this.rcvNode.write("</html>");
this.rcvNode.close();
var _440=this.rcvNode.createElement("div");
this.rcvNode.appendChild(_440);
this.rcvNode.parentWindow.dojo=dojo;
_440.innerHTML="<iframe src='"+_43f+"'></iframe>";
}else{
this.rcvNode=dojo.io.createIFrame(this.rcvNodeName,"",_43f);
}
};
};
cometd.mimeReplaceTransport=new function(){
this.connected=false;
this.connectionId=null;
this.xhr=null;
this.authToken=null;
this.lastTimestamp=null;
this.lastId=null;
this.backlog=[];
this.check=function(_441,_442,_443){
return ((!_443)&&(dojo.render.html.mozilla)&&(dojo.lang.inArray(_441,"mime-message-block")));
};
this.tunnelInit=function(){
if(this.connected){
return;
}
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/connect",clientId:cometd.clientId,connectionType:"mime-message-block"}])});
this.connected=true;
};
this.tunnelCollapse=function(){
if(this.connected){
this.connected=false;
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/reconnect",clientId:cometd.clientId,connectionId:this.connectionId,timestamp:this.lastTimestamp,id:this.lastId}])});
}
};
this.deliver=cometd.iframeTransport.deliver;
this.handleOnLoad=function(resp){
cometd.deliver(dojo.json.evalJson(this.xhr.responseText));
};
this.openTunnelWith=function(_445,url){
this.xhr=dojo.hostenv.getXmlhttpObject();
this.xhr.multipart=true;
if(dojo.render.html.mozilla){
this.xhr.addEventListener("load",dojo.lang.hitch(this,"handleOnLoad"),false);
}else{
if(dojo.render.html.safari){
dojo.debug("Webkit is broken with multipart responses over XHR = (");
this.xhr.onreadystatechange=dojo.lang.hitch(this,"handleOnLoad");
}else{
this.xhr.onload=dojo.lang.hitch(this,"handleOnLoad");
}
}
this.xhr.open("POST",(url||cometd.url),true);
this.xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
dojo.debug(dojo.json.serialize(_445));
this.xhr.send(dojo.io.argsFromMap(_445,"utf8"));
};
this.processBacklog=function(){
while(this.backlog.length>0){
this.sendMessage(this.backlog.shift(),true);
}
};
this.sendMessage=function(_447,_448){
if((_448)||(this.connected)){
_447.connectionId=this.connectionId;
_447.clientId=cometd.clientId;
var _449={url:cometd.url||djConfig["cometdRoot"],method:"POST",mimetype:"text/json",content:{message:dojo.json.serialize([_447])}};
return dojo.io.bind(_449);
}else{
this.backlog.push(_447);
}
};
this.startup=function(_44a){
dojo.debugShallow(_44a);
if(this.connected){
return;
}
this.tunnelInit();
};
};
cometd.longPollTransport=new function(){
this.connected=false;
this.connectionId=null;
this.authToken=null;
this.lastTimestamp=null;
this.lastId=null;
this.backlog=[];
this.check=function(_44b,_44c,_44d){
return ((!_44d)&&(dojo.lang.inArray(_44b,"long-polling")));
};
this.tunnelInit=function(){
if(this.connected){
return;
}
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/connect",clientId:cometd.clientId,connectionType:"long-polling"}])});
this.connected=true;
};
this.tunnelCollapse=function(){
if(!this.connected){
this.connected=false;
dojo.debug("clientId:",cometd.clientId);
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/reconnect",connectionType:"long-polling",clientId:cometd.clientId,connectionId:this.connectionId,timestamp:this.lastTimestamp,id:this.lastId}])});
}
};
this.deliver=cometd.iframeTransport.deliver;
this.openTunnelWith=function(_44e,url){
dojo.io.bind({url:(url||cometd.url),method:"post",content:_44e,mimetype:"text/json",load:dojo.lang.hitch(this,function(type,data,evt,args){
cometd.deliver(data);
this.connected=false;
this.tunnelCollapse();
}),error:function(){
dojo.debug("tunnel opening failed");
}});
this.connected=true;
};
this.processBacklog=function(){
while(this.backlog.length>0){
this.sendMessage(this.backlog.shift(),true);
}
};
this.sendMessage=function(_454,_455){
if((_455)||(this.connected)){
_454.connectionId=this.connectionId;
_454.clientId=cometd.clientId;
var _456={url:cometd.url||djConfig["cometdRoot"],method:"post",mimetype:"text/json",content:{message:dojo.json.serialize([_454])},load:dojo.lang.hitch(this,function(type,data,evt,args){
cometd.deliver(data);
})};
return dojo.io.bind(_456);
}else{
this.backlog.push(_454);
}
};
this.startup=function(_45b){
if(this.connected){
return;
}
this.tunnelInit();
};
};
cometd.callbackPollTransport=new function(){
this.connected=false;
this.connectionId=null;
this.authToken=null;
this.lastTimestamp=null;
this.lastId=null;
this.backlog=[];
this.check=function(_45c,_45d,_45e){
return dojo.lang.inArray(_45c,"callback-polling");
};
this.tunnelInit=function(){
if(this.connected){
return;
}
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/connect",clientId:cometd.clientId,connectionType:"callback-polling"}])});
this.connected=true;
};
this.tunnelCollapse=function(){
if(!this.connected){
this.connected=false;
this.openTunnelWith({message:dojo.json.serialize([{channel:"/meta/reconnect",connectionType:"long-polling",clientId:cometd.clientId,connectionId:this.connectionId,timestamp:this.lastTimestamp,id:this.lastId}])});
}
};
this.deliver=cometd.iframeTransport.deliver;
this.openTunnelWith=function(_45f,url){
var req=dojo.io.bind({url:(url||cometd.url),content:_45f,mimetype:"text/json",transport:"ScriptSrcTransport",jsonParamName:"jsonp",load:dojo.lang.hitch(this,function(type,data,evt,args){
cometd.deliver(data);
this.connected=false;
this.tunnelCollapse();
}),error:function(){
dojo.debug("tunnel opening failed");
}});
this.connected=true;
};
this.processBacklog=function(){
while(this.backlog.length>0){
this.sendMessage(this.backlog.shift(),true);
}
};
this.sendMessage=function(_466,_467){
if((_467)||(this.connected)){
_466.connectionId=this.connectionId;
_466.clientId=cometd.clientId;
var _468={url:cometd.url||djConfig["cometdRoot"],mimetype:"text/json",transport:"ScriptSrcTransport",jsonParamName:"jsonp",content:{message:dojo.json.serialize([_466])},load:dojo.lang.hitch(this,function(type,data,evt,args){
cometd.deliver(data);
})};
return dojo.io.bind(_468);
}else{
this.backlog.push(_466);
}
};
this.startup=function(_46d){
if(this.connected){
return;
}
this.tunnelInit();
};
};
cometd.connectionTypes.register("mime-message-block",cometd.mimeReplaceTransport.check,cometd.mimeReplaceTransport);
cometd.connectionTypes.register("long-polling",cometd.longPollTransport.check,cometd.longPollTransport);
cometd.connectionTypes.register("callback-polling",cometd.callbackPollTransport.check,cometd.callbackPollTransport);
cometd.connectionTypes.register("iframe",cometd.iframeTransport.check,cometd.iframeTransport);
dojo.io.cometd=cometd;

