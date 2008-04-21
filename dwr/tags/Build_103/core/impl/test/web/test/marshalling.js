// This file is in UTF-8

var failures;
var failreport;
var execreport;
var starttime;
var failcount = 0;
var tests = new Array();
var parallel = false;
var batch = 1;

// tests[tests.length] = { code:"voidParam", data:null };

tests[tests.length] = { code:"waitFor", data:1000 };
tests[tests.length] = { code:"waitFor", data:5000 };
tests[tests.length] = { code:"waitFor", data:10000 };

tests[tests.length] = { code:"booleanParam", data:true };
tests[tests.length] = { code:"booleanParam", data:false };

tests[tests.length] = { code:"byteParam", data:-128 };
tests[tests.length] = { code:"byteParam", data:-1 };
tests[tests.length] = { code:"byteParam", data:0 };
tests[tests.length] = { code:"byteParam", data:1 };
tests[tests.length] = { code:"byteParam", data:127 };

tests[tests.length] = { code:"shortParam", data:-32768 };
tests[tests.length] = { code:"shortParam", data:-1 };
tests[tests.length] = { code:"shortParam", data:0 };
tests[tests.length] = { code:"shortParam", data:1 };
tests[tests.length] = { code:"shortParam", data:32767 };

tests[tests.length] = { code:"intParam", data:-2147483648 };
tests[tests.length] = { code:"intParam", data:-1 };
tests[tests.length] = { code:"intParam", data:0 };
tests[tests.length] = { code:"intParam", data:1 };
tests[tests.length] = { code:"intParam", data:2147483647 };

// Mozilla rounds 9223372036854775808 to 9223372036854776000 which overflows so we round down
tests[tests.length] = { code:"longParam", data:-9223372036854775000 };
tests[tests.length] = { code:"longParam", data:-1 };
tests[tests.length] = { code:"longParam", data:0 };
tests[tests.length] = { code:"longParam", data:1 };
tests[tests.length] = { code:"longParam", data:9223372036854775000 };

tests[tests.length] = { code:"floatParam", data:-100000000000000000000 };
tests[tests.length] = { code:"floatParam", data:-1 };
tests[tests.length] = { code:"floatParam", data:0 };
tests[tests.length] = { code:"floatParam", data:1 };
tests[tests.length] = { code:"floatParam", data:100000000000000000000 };

tests[tests.length] = { code:"doubleParam", data:-100000000000000000000 };
tests[tests.length] = { code:"doubleParam", data:-1 };
tests[tests.length] = { code:"doubleParam", data:0 };
tests[tests.length] = { code:"doubleParam", data:1 };
tests[tests.length] = { code:"doubleParam", data:100000000000000000000 };
tests[tests.length] = { code:"doubleParam", data:Number.MAX_VALUE };
tests[tests.length] = { code:"doubleParam", data:Number.MIN_VALUE };
tests[tests.length] = { code:"doubleParam", data:Number.NaN };
tests[tests.length] = { code:"doubleParam", data:Number.NEGATIVE_INFINITY };
tests[tests.length] = { code:"doubleParam", data:Number.POSITIVE_INFINITY };

tests[tests.length] = { code:"bigDecimalParam", data:-100000000000000000000 };
tests[tests.length] = { code:"bigDecimalParam", data:-1 };
tests[tests.length] = { code:"bigDecimalParam", data:0 };
tests[tests.length] = { code:"bigDecimalParam", data:1 };
tests[tests.length] = { code:"bigDecimalParam", data:100000000000000000000 };

tests[tests.length] = { code:"bigIntegerParam", data:-100000000000000000000 };
tests[tests.length] = { code:"bigIntegerParam", data:-1 };
tests[tests.length] = { code:"bigIntegerParam", data:0 };
tests[tests.length] = { code:"bigIntegerParam", data:1 };
tests[tests.length] = { code:"bigIntegerParam", data:100000000000000000000 };

// Opera 8 has issues with this one. It appears to not like \0
//tests[tests.length] = { code:"charParam", data:"\0" };
tests[tests.length] = { code:"charParam", data:"\t" };
tests[tests.length] = { code:"charParam", data:"\n" };
tests[tests.length] = { code:"charParam", data:"\v" };
tests[tests.length] = { code:"charParam", data:"\f" };
tests[tests.length] = { code:"charParam", data:"\r" };
tests[tests.length] = { code:"charParam", data:"\x07" };

tests[tests.length] = { code:"charParam", data:" " };
tests[tests.length] = { code:"charParam", data:"!" };
tests[tests.length] = { code:"charParam", data:'"' };
tests[tests.length] = { code:"charParam", data:"#" };
tests[tests.length] = { code:"charParam", data:"$" };
tests[tests.length] = { code:"charParam", data:"%" };
tests[tests.length] = { code:"charParam", data:"&" };
tests[tests.length] = { code:"charParam", data:"'" }; // double escape because it is evaled twice
tests[tests.length] = { code:"charParam", data:"(" };
tests[tests.length] = { code:"charParam", data:")" };
tests[tests.length] = { code:"charParam", data:"*" };
tests[tests.length] = { code:"charParam", data:"+" };
tests[tests.length] = { code:"charParam", data:"," };
tests[tests.length] = { code:"charParam", data:"-" };
tests[tests.length] = { code:"charParam", data:"." };
tests[tests.length] = { code:"charParam", data:"/" };
tests[tests.length] = { code:"charParam", data:"0" };
tests[tests.length] = { code:"charParam", data:"9" };
tests[tests.length] = { code:"charParam", data:":" };
tests[tests.length] = { code:"charParam", data:";" };
tests[tests.length] = { code:"charParam", data:"<" };
tests[tests.length] = { code:"charParam", data:"=" };
tests[tests.length] = { code:"charParam", data:">" };
tests[tests.length] = { code:"charParam", data:"?" };
tests[tests.length] = { code:"charParam", data:"@" };
tests[tests.length] = { code:"charParam", data:"A" };
tests[tests.length] = { code:"charParam", data:"Z" };
tests[tests.length] = { code:"charParam", data:"[" };
tests[tests.length] = { code:"charParam", data:"\\" }; // double escape because it is evaled twice
tests[tests.length] = { code:"charParam", data:"]" };
tests[tests.length] = { code:"charParam", data:"^" };
tests[tests.length] = { code:"charParam", data:"_" };
tests[tests.length] = { code:"charParam", data:"`" };
tests[tests.length] = { code:"charParam", data:"a" };
tests[tests.length] = { code:"charParam", data:"z" };
tests[tests.length] = { code:"charParam", data:"{" };
tests[tests.length] = { code:"charParam", data:"|" };
tests[tests.length] = { code:"charParam", data:"}" };
tests[tests.length] = { code:"charParam", data:"~" };

tests[tests.length] = { code:"stringParam", data:" " };
tests[tests.length] = { code:"stringParam", data:"!" };
tests[tests.length] = { code:"stringParam", data:'"' };
tests[tests.length] = { code:"stringParam", data:"#" };
tests[tests.length] = { code:"stringParam", data:"$" };
tests[tests.length] = { code:"stringParam", data:"%" };
tests[tests.length] = { code:"stringParam", data:"&" };
tests[tests.length] = { code:"stringParam", data:"'" }; // double escape because it is evaled twice
tests[tests.length] = { code:"stringParam", data:"(" };
tests[tests.length] = { code:"stringParam", data:")" };
tests[tests.length] = { code:"stringParam", data:"*" };
tests[tests.length] = { code:"stringParam", data:"+" };
tests[tests.length] = { code:"stringParam", data:"," };
tests[tests.length] = { code:"stringParam", data:"-" };
tests[tests.length] = { code:"stringParam", data:"." };
tests[tests.length] = { code:"stringParam", data:"/" };
tests[tests.length] = { code:"stringParam", data:"0" };
tests[tests.length] = { code:"stringParam", data:"9" };
tests[tests.length] = { code:"stringParam", data:":" };
tests[tests.length] = { code:"stringParam", data:";" };
tests[tests.length] = { code:"stringParam", data:"<" };
tests[tests.length] = { code:"stringParam", data:"=" };
tests[tests.length] = { code:"stringParam", data:">" };
tests[tests.length] = { code:"stringParam", data:"?" };
tests[tests.length] = { code:"stringParam", data:"@" };
tests[tests.length] = { code:"stringParam", data:"A" };
tests[tests.length] = { code:"stringParam", data:"Z" };
tests[tests.length] = { code:"stringParam", data:"[" };
tests[tests.length] = { code:"stringParam", data:"\\" }; // double escape because it is evaled twice
tests[tests.length] = { code:"stringParam", data:"]" };
tests[tests.length] = { code:"stringParam", data:"^" };
tests[tests.length] = { code:"stringParam", data:"_" };
tests[tests.length] = { code:"stringParam", data:"`" };
tests[tests.length] = { code:"stringParam", data:"a" };
tests[tests.length] = { code:"stringParam", data:"z" };
tests[tests.length] = { code:"stringParam", data:"{" };
tests[tests.length] = { code:"stringParam", data:"|" };
tests[tests.length] = { code:"stringParam", data:"}" };
tests[tests.length] = { code:"stringParam", data:"~" };

tests[tests.length] = { code:"stringParam", data:"" };
tests[tests.length] = { code:"stringParam", data:null };
tests[tests.length] = { code:"stringParam", data:"null" };

tests[tests.length] = { code:"stringParam", data:" !\"#$%&\'()*+,-/" };
tests[tests.length] = { code:"stringParam", data:"0123456789" };
tests[tests.length] = { code:"stringParam", data:":;<=>?@" };
tests[tests.length] = { code:"stringParam", data:"ABCDEFGHIJKLMNOPQRSTUVWXYZ" };
tests[tests.length] = { code:"stringParam", data:"[\\]^_`" };
tests[tests.length] = { code:"stringParam", data:"abcdefghijklmnopqrstuvwxyz" };
tests[tests.length] = { code:"stringParam", data:"{|}~" };

tests[tests.length] = { code:"stringParam", data:"call.callback = null" };
//tests[tests.length] = { code:"stringStringParam", data:"param1=' !\"#$%&\'()*+,-/0123456789' param2=':;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ'" };
//tests[tests.length] = { code:"stringStringParam", data:"param1='[\\]^_`' param2='abcdefghijklmnopqrstuvwxyz{|}~'" };
tests[tests.length] = { code:"booleanArrayParam", data:[ ] };
tests[tests.length] = { code:"booleanArrayParam", data:[ true, false, true, false ] };
tests[tests.length] = { code:"charArrayParam", data:[ ] };
tests[tests.length] = { code:"charArrayParam", data:[ 'a', ',', '[', ']' ] };
tests[tests.length] = { code:"byteArrayParam", data:[ ] };
tests[tests.length] = { code:"byteArrayParam", data:[ -128, -128, -128, -128, -127 ] };
tests[tests.length] = { code:"byteArrayParam", data:[ -128, -1, 0, 1, 127 ] };
tests[tests.length] = { code:"shortArrayParam", data:[ ] };
tests[tests.length] = { code:"shortArrayParam", data:[ -32768, -1, 0, 1, 32767 ] };
tests[tests.length] = { code:"intArrayParam", data:[ ] };
tests[tests.length] = { code:"intArrayParam", data:[ -2147483648 ] };
tests[tests.length] = { code:"intArrayParam", data:[ -1 ] };
tests[tests.length] = { code:"intArrayParam", data:[ 0 ] };
tests[tests.length] = { code:"intArrayParam", data:[ 1 ] };
tests[tests.length] = { code:"intArrayParam", data:[ 2147483647 ] };
tests[tests.length] = { code:"intArrayParam", data:[ -2147483648, -1, 0, 1, 2147483647 ] };
tests[tests.length] = { code:"longArrayParam", data:[ ] };
tests[tests.length] = { code:"longArrayParam", data:[ -9223372036854775000, -1, 0, 1, 9223372036854775000 ] };
tests[tests.length] = { code:"floatArrayParam", data:[ ] };
tests[tests.length] = { code:"floatArrayParam", data:[ -100000000000000000000, -1, 0, 1, 100000000000000000000 ] };

var double1D = [ -100000000000000000000, -1, 0, 1, 100000000000000000000 ];
tests[tests.length] = { code:"doubleArrayParam", data:double1D };
var double2D = [ double1D, double1D ];
tests[tests.length] = { code:"double2DArrayParam", data:double2D };
var double3D = [ double2D, double2D ];
tests[tests.length] = { code:"double3DArrayParam", data:double3D };
var double4D = [ double3D, double3D ];
tests[tests.length] = { code:"double4DArrayParam", data:double4D };
var double5D = [ double4D, double4D ];
tests[tests.length] = { code:"double5DArrayParam", data:double5D };

// Unicode: we could be here for some time, so I just picked some commmon ones
tests[tests.length] = { code:"charParam", data:"\u0080" };
tests[tests.length] = { code:"charParam", data:"\u0091" };
tests[tests.length] = { code:"charParam", data:"\u0092" };
tests[tests.length] = { code:"charParam", data:"\u0093" };
tests[tests.length] = { code:"charParam", data:"\u0094" };
tests[tests.length] = { code:"charParam", data:"\u0095" };
tests[tests.length] = { code:"charParam", data:"\u0098" };
tests[tests.length] = { code:"charParam", data:"\u0099" };
tests[tests.length] = { code:"charParam", data:"\u00A0" };
tests[tests.length] = { code:"charParam", data:"\u00A3" };
tests[tests.length] = { code:"charParam", data:"\u00A5" };
tests[tests.length] = { code:"charParam", data:"\u00A6" };
tests[tests.length] = { code:"charParam", data:"\u00A9" };
tests[tests.length] = { code:"charParam", data:"\u00AC" };
tests[tests.length] = { code:"charParam", data:"\u00C7" };
tests[tests.length] = { code:"charParam", data:"\u00C6" };
tests[tests.length] = { code:"charParam", data:"\u00DF" };
tests[tests.length] = { code:"charParam", data:"\u00FF" };

// This line should contain a string of non-western characters and no question marks
tests[tests.length] = { code:"stringParam", data:"一部BBC在1994年拍的老片子~~可是我却看了又看！可是相当的吸引人啊~~简·奥斯丁的名著，就不用说了吧~~不看不知道，看了绝对不会后悔！一部BBC在1994年拍的老片子~~可是我却看了又看！可是相当的吸引人啊~~简·奥斯丁的名著，就不用说了吧~~不看不知道，看了绝对不会后悔！" };
tests[tests.length] = { code:"stringParam", data:"\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\" };

var nested = { integer:0, string:'0123456789' };
nested.testBean = nested;

var obja = new ObjA();
var objb = new ObjB(); objb.objA = obja; obja.objB = objb;
tests[tests.length] = { code:"testLooped", data:obja };

tests[tests.length] = { code:"byteArrayParam", data:[ ] };
tests[tests.length] = { code:"byteArrayParam", data:[ -128, -128, -128, -128, -127 ] };

tests[tests.length] = { code:"testBeanParam", data:{ integer:-2147483648, string:'!"$%^&*()', testBean:null } };
tests[tests.length] = { code:"testBeanParam", data:{ integer:-1, string:'Null', testBean:null } };
tests[tests.length] = { code:"testBeanParam", data:{ integer:0, string:'null', testBean:null } };
tests[tests.length] = { code:"testBeanParam", data:{ integer:1, string:'0987654321', testBean:nested } };

tests[tests.length] = { code:"testBeanSetParam", data:[ ] };
tests[tests.length] = { code:"testBeanSetParam", data:[{ integer:1, string:'0987654321', testBean:nested }] };
tests[tests.length] = { code:"testBeanSetParam", data:[ nested ] };
tests[tests.length] = { code:"testBeanListParam", data:[ ] };
tests[tests.length] = { code:"testBeanListParam", data:[ nested ] };
tests[tests.length] = { code:"testBeanListParam", data:[ nested, nested ] };
tests[tests.length] = { code:"testBeanListParam", data:[ nested, nested, nested ] };

tests[tests.length] = { code:"charTestBeanMapParam", data:{ } };
tests[tests.length] = { code:"charTestBeanMapParam", data:{ d:{ integer:1, string:'0987654321', testBean:nested } } };

tests[tests.length] = { code:"stringCollectionParam", data:[ ] };
tests[tests.length] = { code:"stringCollectionParam", data:[ 'abcdef', 'hgijklm', 'nopqrst' ] };
tests[tests.length] = { code:"stringLinkedListParam", data:[ ] };
tests[tests.length] = { code:"stringLinkedListParam", data:[ 'abcdef', 'hgijklm', 'nopqrst' ] };
tests[tests.length] = { code:"stringArrayListParam", data:[ 'abcdef', 'hgijklm', 'nopqrst' ] };
tests[tests.length] = { code:"stringListParam", data:[ 'abcdef', 'hgijklm', 'nopqrst' ] };
// Note the next 2 are unordered so we cheat by only having 1 element
tests[tests.length] = { code:"stringSetParam", data:[ 'abcdef' ] };
tests[tests.length] = { code:"stringHashSetParam", data:[ 'abcdef' ] };
tests[tests.length] = { code:"stringTreeSetParam", data:[ 'abcdef', 'hgijklm', 'nopqrst' ] };

tests[tests.length] = { code:"stringCollectionParam", data:[ ] };
tests[tests.length] = { code:"stringLinkedListParam", data:[ ] };
tests[tests.length] = { code:"stringArrayListParam", data:[ ] };
tests[tests.length] = { code:"stringListParam", data:[ ] };
tests[tests.length] = { code:"stringSetParam", data:[ ] };
tests[tests.length] = { code:"stringHashSetParam", data:[ ] };
tests[tests.length] = { code:"stringTreeSetParam", data:[ ] };

tests[tests.length] = { code:"stringCollectionParam", data:[ 'abcdef' ] };
tests[tests.length] = { code:"stringLinkedListParam", data:[ 'abcdef' ] };
tests[tests.length] = { code:"stringArrayListParam", data:[ 'abcdef' ] };
tests[tests.length] = { code:"stringListParam", data:[ 'abcdef' ] };
tests[tests.length] = { code:"stringSetParam", data:[ 'abcdef' ] };
tests[tests.length] = { code:"stringHashSetParam", data:[ 'abcdef' ] };
tests[tests.length] = { code:"stringTreeSetParam", data:[ 'abcdef' ] };

tests[tests.length] = { code:"stringCollectionParam", data:[ ",'{}[]" ] };
tests[tests.length] = { code:"stringLinkedListParam", data:[ ",'{}[]" ] };
tests[tests.length] = { code:"stringArrayListParam", data:[ ",'{}[]" ] };
tests[tests.length] = { code:"stringListParam", data:[ ",'{}[]" ] };
tests[tests.length] = { code:"stringSetParam", data:[ ",'{}[]" ] };
tests[tests.length] = { code:"stringHashSetParam", data:[ ",'{}[]" ] };
tests[tests.length] = { code:"stringTreeSetParam", data:[ ",'{}[]" ] };

tests[tests.length] = { code:"stringCollectionParam", data:[ ",'{}[]", 'null', ",'{}[]" ] };
tests[tests.length] = { code:"stringLinkedListParam", data:[ ",'{}[]", 'null', ",'{}[]" ] };
tests[tests.length] = { code:"stringArrayListParam", data:[ ",'{}[]", 'null', ",'{}[]" ] };
tests[tests.length] = { code:"stringListParam", data:[ ",'{}[]", 'null', ",'{}[]" ] };

var map1 = { a:'a', b:'b', c:'c' };
var map2 = { };
map2['a.a'] = "a.a";
map2['b!'] = "b!";
map2['c$'] = "c$";
map2["d'"] = "d'";
map2['e"'] = 'e"';
map2['f '] = 'f ';
map2[' g'] = ' g';
map2['h&'] = 'h&';
map2['i<'] = 'i<';
map2['j>'] = 'j>';
map2['k:'] = 'k:';
map2['l['] = 'l[';
map2['m]'] = 'm]';
map2['o{'] = 'o{';
map2['p}'] = 'p}';
map2['q;~#'] = 'q;~#';
map2['r?/,'] = 'r?/,';

tests[tests.length] = { code:"stringStringMapParam", data:map1 };
tests[tests.length] = { code:"stringStringMapParam", data:map2 };
tests[tests.length] = { code:"stringStringHashMapParam", data:map1 };
tests[tests.length] = { code:"stringStringHashMapParam", data:map2 };
tests[tests.length] = { code:"stringStringTreeMapParam", data:map1 };
tests[tests.length] = { code:"stringStringTreeMapParam", data:map2 };

var testNode = dwr.util.byId("test");

tests[tests.length] = { code:"dom4jElementParam", data:testNode };
tests[tests.length] = { code:"xomElementParam", data:testNode };
tests[tests.length] = { code:"jdomElementParam", data:testNode };
tests[tests.length] = { code:"domElementParam", data:testNode };

function startTest() {
  dwr.util.byId("start").disabled = true;

  failures.innerHTML = "";
  failreport.innerHTML = "0";
  execreport.innerHTML = "0";
  for (var i = 0; i < tests.length; i++) {
    var numele = dwr.util.byId("t" + i + "-num");
    numele.style.backgroundColor = "white";
    dwr.util.setValue("t" + i + "-results", "");
  }

  setSettings();

  // What is the firing rate
  var rate = dwr.util.getValue("rate");

  // How many in a batch
  var size = dwr.util.getValue("size");

  sendBatch(0, size, rate);
}

function setSettings() {
  starttime = new Date();
  failcount = 0;

  // Are we in ordered mode
  var ordered = dwr.util.getValue("ordered");
  dwr.engine.setOrdered(ordered == "Yes");

  // Sync/Asynch?
  var async = dwr.util.getValue("async") == "async";
  dwr.engine.setAsync(async);

  // Sync/Asynch?
  var timeout = 0 + dwr.util.getValue("timeout");
  dwr.engine.setTimeout(timeout);
}

function sendBatch(start, size, rate) {
  var incr = size;
  var test, param;
  var numele;

  if (size == 0) {
    // otherwise we never progress
    incr = 1;
    runTest(start);
  }
  else {
    dwr.engine.beginBatch();
    for (var i = start; i < start + size && i < tests.length; i++) {
      runTest(i);
    }
    dwr.engine.endBatch();
  }

  dwr.util.setValue("testsDispatched", start + 1);

  if (start + incr < tests.length) {
    setTimeout("sendBatch(" + (start + incr) + ", " + size + ", " + rate + ")", rate);
  }
}

function runTest(num) {
  var numele = dwr.util.byId("t" + num + "-num");
  numele.style.backgroundColor = "#ff8";

  var test = tests[num];

  Test[test.code](test.data, {
    callback:function(data) { testResults(data, num); },
    exceptionHandler:function(data) { exceptionHandler(data, num); }
  });

  dwr.engine._debug("Sending test " + num + ": " + test.code + "/" + test.data);
}

function checkTidyUp(index) {
  if (index == tests.length) {
    dwr.util.byId("start").disabled = false;

    var mslen = new Date().getTime() - window.starttime.getTime();
    var tps = Math.round((10000 * tests.length) / mslen) / 10;
    dwr.util.setValue("timePerTest", tps);
  }
}

function testResults(data, index) {
  dwr.engine._debug("Receive test " + index + ": " + data);
  var test = tests[index];

  if (test == null) {
    alert("no test found: index=" + index + ", tests.length=" + tests.length);
    return;
  }

  var passed = testEquals(data, test.data, 0);
  var numele = dwr.util.byId("t" + index + "-num");

  execreport.innerHTML = (index + 1);

  if (typeof passed == "boolean" && passed) {
    numele.style.backgroundColor = "lightgreen";
    dwr.util.setValue("t" + index + "-results", "Passed");
  }
  else {
    var report = test.code + "<br/>&nbsp;&nbsp;" + passed + "<br/>&nbsp;&nbsp;Expected: " + dwr.util.toDescriptiveString(test.data) + " Actual: " + dwr.util.toDescriptiveString(data);
    failcount++;
    failreport.innerHTML = failcount;

    numele.style.backgroundColor = "lightpink";
    dwr.util.setValue("t" + index + "-results", report, { escapeHtml:false });
  }

  checkTidyUp(index + 1);
}

function exceptionHandler(data, index) {
  var test = tests[index];

  var numele = dwr.util.byId("t" + index + "-num");

  execreport.innerHTML = (index + 1);

  var report = test.code + "<br/>Exception: " + dwr.util.toDescriptiveString(data);
  failcount++;
  failreport.innerHTML = failcount;

  numele.style.backgroundColor = "lightpink";
  dwr.util.setValue("t" + index + "-results", report, { escapeHtml:false });

  checkTidyUp(index + 1);
}

function catchFailure(data) {
  failures.innerHTML += "Unknown Failure:&nbsp; " + data + "<br/>";
  failcount++;
  failreport.innerHTML = failcount;
}

function contentFailure() {
  failures.innerHTML += "Unknown Test: text/html content found.<br/>";
  failcount++;
  failreport.innerHTML = failcount;
}

function init() {
  dwr.util.setValue("testCount", tests.length);
  failures = dwr.util.byId("failures");
  failreport = dwr.util.byId("failreport");
  execreport = dwr.util.byId("execreport");

  dwr.engine.setErrorHandler(catchFailure);
  dwr.engine.setWarningHandler(catchFailure);
  dwr.engine.setTextHtmlHandler(contentFailure);

  dwr.util.addRows("chart", tests, [
    function(row, options) { return options.rowNum; },
    function(row, options) { return "" + row.code; },
    function(row, options) {
      var display = dwr.util.toDescriptiveString(row.data);
      if (display.length > 30) display = display.substring(0, 27) + "...";
      return display;
    },
    function(row, options) {
      return "<input type='button' value='Test' onclick='setSettings();runTest(" + options.rowNum + ")'/>";
    },
    function(row, options) { return ""; }
  ], {
    escapeHtml:false,
    cellCreator:function(options) {
      var td = document.createElement("td");
      td.setAttribute("id", "t" + options.rowNum + cellNumIdSuffix[options.cellNum]);
      return td;
    }
  });
}

var cellNumIdSuffix = [ "-num", "-code", "-data", "-button", "-results" ];
