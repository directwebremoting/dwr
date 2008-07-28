
createTestGroup("Marshall");

function testMarshallByteParam() {
  runComparisonTests([
    { code:"byteParam", data:-128 },
    { code:"byteParam", data:-1 },
    { code:"byteParam", data:0 },
    { code:"byteParam", data:1 },
    { code:"byteParam", data:127 }
  ]);
}

function testMarshallBooleanParam() {
  runComparisonTests([
    { code:"booleanParam", data:true },
    { code:"booleanParam", data:false }
  ]);
}

function testMarshallShortParam() {
  runComparisonTests([
    { code:"shortParam", data:-32768 },
    { code:"shortParam", data:-1 },
    { code:"shortParam", data:0 },
    { code:"shortParam", data:1 },
    { code:"shortParam", data:32767 }
  ]);
}

function testMarshallIntParam() {
  runComparisonTests([
    { code:"intParam", data:-2147483648 },
    { code:"intParam", data:-1 },
    { code:"intParam", data:0 },
    { code:"intParam", data:1 },
    { code:"intParam", data:2147483647 }
  ]);
}

function testMarshallLongParam() {
  runComparisonTests([
    // Mozilla rounds 9223372036854775808 to 9223372036854776000 which overflows so we round down
    { code:"longParam", data:-9223372036854775000 },
    { code:"longParam", data:-1 },
    { code:"longParam", data:0 },
    { code:"longParam", data:1 },
    { code:"longParam", data:9223372036854775000 }
  ]);
}

function testMarshallFloatParam() {
  runComparisonTests([
    { code:"floatParam", data:-100000000000000000000 },
    { code:"floatParam", data:-1 },
    { code:"floatParam", data:0 },
    { code:"floatParam", data:1 },
    { code:"floatParam", data:100000000000000000000 }
  ]);
}

function testMarshallDoubleParam() {
  runComparisonTests([
    { code:"doubleParam", data:-100000000000000000000 },
    { code:"doubleParam", data:-1 },
    { code:"doubleParam", data:0 },
    { code:"doubleParam", data:1 },
    { code:"doubleParam", data:100000000000000000000 },
    { code:"doubleParam", data:Number.MAX_VALUE },
    { code:"doubleParam", data:Number.MIN_VALUE },
    { code:"doubleParam", data:Number.NaN },
    { code:"doubleParam", data:Number.NEGATIVE_INFINITY },
    { code:"doubleParam", data:Number.POSITIVE_INFINITY }
  ]);
}

function testMarshallBigDecimalParam() {
  runComparisonTests([
    { code:"bigDecimalParam", data:-100000000000000000000 },
    { code:"bigDecimalParam", data:-1 },
    { code:"bigDecimalParam", data:0 },
    { code:"bigDecimalParam", data:1 },
    { code:"bigDecimalParam", data:100000000000000000000 }
  ]);
}

function testMarshallBigIntegerParam() {
  runComparisonTests([
    { code:"bigIntegerParam", data:-100000000000000000000 },
    { code:"bigIntegerParam", data:-1 },
    { code:"bigIntegerParam", data:0 },
    { code:"bigIntegerParam", data:1 },
    { code:"bigIntegerParam", data:100000000000000000000 }
  ]);
}

function testMarshallCharParam() {
  runComparisonTests([
    // Opera 8 has issues with this one. It appears to not like \0
    //{ code:"charParam", data:"\0" },
    { code:"charParam", data:"\t" },
    { code:"charParam", data:"\n" },
    { code:"charParam", data:"\v" },
    { code:"charParam", data:"\f" },
    { code:"charParam", data:"\r" },
    { code:"charParam", data:"\x07" },
    { code:"charParam", data:" " },
    { code:"charParam", data:"!" },
    { code:"charParam", data:'"' },
    { code:"charParam", data:"#" },
    { code:"charParam", data:"$" },
    { code:"charParam", data:"%" },
    { code:"charParam", data:"&" },
    { code:"charParam", data:"'" },
    { code:"charParam", data:"(" },
    { code:"charParam", data:")" },
    { code:"charParam", data:"*" },
    { code:"charParam", data:"+" },
    { code:"charParam", data:"," },
    { code:"charParam", data:"-" },
    { code:"charParam", data:"." },
    { code:"charParam", data:"/" },
    { code:"charParam", data:"0" },
    { code:"charParam", data:"9" },
    { code:"charParam", data:":" },
    { code:"charParam", data:"," },
    { code:"charParam", data:"<" },
    { code:"charParam", data:"=" },
    { code:"charParam", data:">" },
    { code:"charParam", data:"?" },
    { code:"charParam", data:"@" },
    { code:"charParam", data:"A" },
    { code:"charParam", data:"Z" },
    { code:"charParam", data:"[" },
    { code:"charParam", data:"\\" },
    { code:"charParam", data:"]" },
    { code:"charParam", data:"^" },
    { code:"charParam", data:"_" },
    { code:"charParam", data:"`" },
    { code:"charParam", data:"a" },
    { code:"charParam", data:"z" },
    { code:"charParam", data:"{" },
    { code:"charParam", data:"|" },
    { code:"charParam", data:"}" },
    { code:"charParam", data:"~" },

    // Unicode: we could be here for some time, so I just picked some commmon ones
    { code:"charParam", data:"\u0080" },
    { code:"charParam", data:"\u0091" },
    { code:"charParam", data:"\u0092" },
    { code:"charParam", data:"\u0093" },
    { code:"charParam", data:"\u0094" },
    { code:"charParam", data:"\u0095" },
    { code:"charParam", data:"\u0098" },
    { code:"charParam", data:"\u0099" },
    { code:"charParam", data:"\u00A0" },
    { code:"charParam", data:"\u00A3" },
    { code:"charParam", data:"\u00A5" },
    { code:"charParam", data:"\u00A6" },
    { code:"charParam", data:"\u00A9" },
    { code:"charParam", data:"\u00AC" },
    { code:"charParam", data:"\u00C7" },
    { code:"charParam", data:"\u00C6" },
    { code:"charParam", data:"\u00DF" },
    { code:"charParam", data:"\u00FF" }
  ]);
}

function testMarshallStringParam() {
  runComparisonTests([
    { code:"stringParam", data:" " },
    { code:"stringParam", data:"!" },
    { code:"stringParam", data:'"' },
    { code:"stringParam", data:"#" },
    { code:"stringParam", data:"$" },
    { code:"stringParam", data:"%" },
    { code:"stringParam", data:"&" },
    { code:"stringParam", data:"'" },
    { code:"stringParam", data:"(" },
    { code:"stringParam", data:")" },
    { code:"stringParam", data:"*" },
    { code:"stringParam", data:"+" },
    { code:"stringParam", data:"," },
    { code:"stringParam", data:"-" },
    { code:"stringParam", data:"." },
    { code:"stringParam", data:"/" },
    { code:"stringParam", data:"0" },
    { code:"stringParam", data:"9" },
    { code:"stringParam", data:":" },
    { code:"stringParam", data:"," },
    { code:"stringParam", data:"<" },
    { code:"stringParam", data:"=" },
    { code:"stringParam", data:">" },
    { code:"stringParam", data:"?" },
    { code:"stringParam", data:"@" },
    { code:"stringParam", data:"A" },
    { code:"stringParam", data:"Z" },
    { code:"stringParam", data:"[" },
    { code:"stringParam", data:"\\" },
    { code:"stringParam", data:"]" },
    { code:"stringParam", data:"^" },
    { code:"stringParam", data:"_" },
    { code:"stringParam", data:"`" },
    { code:"stringParam", data:"a" },
    { code:"stringParam", data:"z" },
    { code:"stringParam", data:"{" },
    { code:"stringParam", data:"|" },
    { code:"stringParam", data:"}" },
    { code:"stringParam", data:"~" },

    { code:"stringParam", data:"" },
    { code:"stringParam", data:null },
    { code:"stringParam", data:"null" },

    { code:"stringParam", data:" !\"#$%&\'()*+,-/" },
    { code:"stringParam", data:"0123456789" },
    { code:"stringParam", data:":,<=>?@" },
    { code:"stringParam", data:"ABCDEFGHIJKLMNOPQRSTUVWXYZ" },
    { code:"stringParam", data:"[\\]^_`" },
    { code:"stringParam", data:"abcdefghijklmnopqrstuvwxyz" },
    { code:"stringParam", data:"{|}~" },

    { code:"stringParam", data:"call.callback = null" },

    // This line should contain a string of mostly non-western characters and no question marks
    { code:"stringParam", data:"??BBC?1994??????~~??????????????????~~?á?????????????~~?????????????????BBC?1994??????~~??????????????????~~?á?????????????~~???????????????" },
    { code:"stringParam", data:"\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\" }
  ]);
}

function testMarshallStringStringParam() {
  runComparisonTests([
    { code:"stringStringParam", data:"param1=' !\"#$%&\'()*+,-/0123456789' param2=':,<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ'" },
    { code:"stringStringParam", data:"param1='[\\]^_`' param2='abcdefghijklmnopqrstuvwxyz{|}~'" }
  ]);
}

function testMarshallBooleanArrayParam() {
  runComparisonTests([
    { code:"booleanArrayParam", data:[ ] },
    { code:"booleanArrayParam", data:[ true, false, true, false ] }
  ]);
}

function testMarshallCharArrayParam() {
  runComparisonTests([
    { code:"charArrayParam", data:[ ] },
    { code:"charArrayParam", data:[ 'a', ',', '[', ']' ] }
  ]);
}

function testMarshallByteArrayParam() {
  runComparisonTests([
    { code:"byteArrayParam", data:[ ] },
    { code:"byteArrayParam", data:[ -128, -128, -128, -128, -127 ] },
    { code:"byteArrayParam", data:[ -128, -1, 0, 1, 127 ] }
  ]);
}

function testMarshallShortArrayParam() {
  runComparisonTests([
    { code:"shortArrayParam", data:[ ] },
    { code:"shortArrayParam", data:[ -32768, -1, 0, 1, 32767 ] }
  ]);
}

function testMarshallIntArrayParam() {
  runComparisonTests([
    { code:"intArrayParam", data:[ ] },
    { code:"intArrayParam", data:[ -2147483648 ] },
    { code:"intArrayParam", data:[ -1 ] },
    { code:"intArrayParam", data:[ 0 ] },
    { code:"intArrayParam", data:[ 1 ] },
    { code:"intArrayParam", data:[ 2147483647 ] },
    { code:"intArrayParam", data:[ -2147483648, -1, 0, 1, 2147483647 ] }
  ]);
}

function testMarshallLongArrayParam() {
  runComparisonTests([
    { code:"longArrayParam", data:[ ] },
    { code:"longArrayParam", data:[ -9223372036854775000, -1, 0, 1, 9223372036854775000 ] }
  ]);
}

function testMarshallFloatArrayParam() {
  runComparisonTests([
    { code:"floatArrayParam", data:[ ] },
    { code:"floatArrayParam", data:[ -100000000000000000000, -1, 0, 1, 100000000000000000000 ] }
  ]);
}

function testMarshallXDArrayParam() {
  var double1D = [ -100000000000000000000, -1, 0, 1, 100000000000000000000 ];
  var double2D = [ double1D, double1D ];
  var double3D = [ double2D, double2D ];
  var double4D = [ double3D, double3D ];
  var double5D = [ double4D, double4D ];

  runComparisonTests([
    { code:"doubleArrayParam", data:double1D },
    { code:"double2DArrayParam", data:double2D },
    { code:"double3DArrayParam", data:double3D },
    { code:"double4DArrayParam", data:double4D },
    { code:"double5DArrayParam", data:double5D }
  ]);
}

// Used in a few tests
var nested = { integer:0, string:'0123456789' };
nested.testBean = nested;

var obja = new ObjA();
var objb = new ObjB();
objb.objA = obja;
obja.objB = objb;


function testMarshallLoopedParam() {
  runComparisonTests([
    { code:"testLooped", data:obja }
  ]);
}

function testMarshallTestBeanParam() {
  runComparisonTests([
    { code:"testBeanParam", data:{ integer:-2147483648, string:'!"$%^&*()', testBean:null } },
    { code:"testBeanParam", data:{ integer:-1, string:'Null', testBean:null } },
    { code:"testBeanParam", data:{ integer:0, string:'null', testBean:null } },
    { code:"testBeanParam", data:{ integer:1, string:'0987654321', testBean:nested } }
  ]);
}

function testMarshallTestBeanSetParam() {
  runComparisonTests([
    { code:"testBeanSetParam", data:[ ] },
    { code:"testBeanSetParam", data:[{ integer:1, string:'0987654321', testBean:nested }] },
    { code:"testBeanSetParam", data:[ nested ] }
  ]);
}

function testMarshallBeanListParam() {
  runComparisonTests([
    { code:"testBeanListParam", data:[ ] },
    { code:"testBeanListParam", data:[ nested ] },
    { code:"testBeanListParam", data:[ nested, nested ] },
    { code:"testBeanListParam", data:[ nested, nested, nested ] }
  ]);
}

function testMarshallTestBeanMapParam() {
  runComparisonTests([
    { code:"charTestBeanMapParam", data:{ } },
    { code:"charTestBeanMapParam", data:{ d:{ integer:1, string:'0987654321', testBean:nested } } }
  ]);
}

function testMarshallStringCollectionParam() {
  runComparisonTests([
    { code:"stringCollectionParam", data:[ ] },
    { code:"stringCollectionParam", data:[ 'abcdef' ] },
    { code:"stringCollectionParam", data:[ ",'{}[]" ] },
    { code:"stringCollectionParam", data:[ 'abcdef', 'hgijklm', 'nopqrst' ] },
    { code:"stringCollectionParam", data:[ ",'{}[]", 'null', ",'{}[]" ] }
  ]);
}

function testMarshallStringLinkedListParam() {
  runComparisonTests([
    { code:"stringLinkedListParam", data:[ ] },
    { code:"stringLinkedListParam", data:[ 'abcdef' ] },
    { code:"stringLinkedListParam", data:[ ",'{}[]" ] },
    { code:"stringLinkedListParam", data:[ 'abcdef', 'hgijklm', 'nopqrst' ] },
    { code:"stringLinkedListParam", data:[ ",'{}[]", 'null', ",'{}[]" ] }
  ]);
}

function testMarshallStringArrayListParam() {
  runComparisonTests([
    { code:"stringArrayListParam", data:[ ] },
    { code:"stringArrayListParam", data:[ ",'{}[]" ] },
    { code:"stringArrayListParam", data:[ 'abcdef' ] },
    { code:"stringArrayListParam", data:[ 'abcdef', 'hgijklm', 'nopqrst' ] },
    { code:"stringArrayListParam", data:[ ",'{}[]", 'null', ",'{}[]" ] }
  ]);
}

function testMarshallStringListParam() {
  runComparisonTests([
    { code:"stringListParam", data:[ ] },
    { code:"stringListParam", data:[ ",'{}[]" ] },
    { code:"stringListParam", data:[ 'abcdef' ] },
    { code:"stringListParam", data:[ 'abcdef', 'hgijklm', 'nopqrst' ] },
    { code:"stringListParam", data:[ ",'{}[]", 'null', ",'{}[]" ] }
  ]);
}

function testMarshallStringSetParam() {
  runComparisonTests([
    { code:"stringSetParam", data:[ ] },
    { code:"stringSetParam", data:[ 'abcdef' ] },
    { code:"stringSetParam", data:[ ",'{}[]" ] }
    // Unordered so we cheat by not using multiple elements
  ]);
}

function testMarshallStringHashSetParam() {
  runComparisonTests([
    { code:"stringHashSetParam", data:[ ] },
    { code:"stringHashSetParam", data:[ 'abcdef' ] },
    { code:"stringHashSetParam", data:[ ",'{}[]" ] }
    // Unordered so we cheat by not using multiple elements
  ]);
}

function testMarshallStringTreeSetParam() {
  runComparisonTests([
    { code:"stringTreeSetParam", data:[ ] },
    { code:"stringTreeSetParam", data:[ 'abcdef' ] },
    { code:"stringTreeSetParam", data:[ ",'{}[]" ] },
    { code:"stringTreeSetParam", data:[ 'abcdef', 'hgijklm', 'nopqrst' ] }
  ]);
}

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
map2['q,~#'] = 'q,~#';
map2['r?/,'] = 'r?/,';

function testMarshallStringStringMapParam() {
  runComparisonTests([
    { code:"stringStringMapParam", data:map1 },
    { code:"stringStringMapParam", data:map2 }
  ]);
}

function testMarshallStringStringHashMapParam() {
  runComparisonTests([
    { code:"stringStringHashMapParam", data:map1 },
    { code:"stringStringHashMapParam", data:map2 }
  ]);
}

function testMarshallStringStringTreeMapParam() {
  runComparisonTests([
    { code:"stringStringTreeMapParam", data:map1 },
    { code:"stringStringTreeMapParam", data:map2 }
  ]);
}

function testMarshallDomElementParam() {
  dwr.util.setValue(currentTest.scratch, '<p id="test">This is a <span style="color:#F00;">test node</span> to check on <strong>DOM</strong> <span class="small">manipulation</span>.</p>', { escapeHtml:false });
  var testNode = dwr.util.byId("test");

  runComparisonTests([
    { code:"dom4jElementParam", data:testNode },
    { code:"xomElementParam", data:testNode },
    { code:"jdomElementParam", data:testNode },
    { code:"domElementParam", data:testNode }
  ]);
}

/**
 *
 */
function runComparisonTests(compares) {
  for (var i = 0; i < compares.length; i++) {
    var compare = compares[i];

    Test[compare.code](compare.data, {
      callback:createDelayed(function(data) {
        assertEqual(data, compare.data);
      }),
      exceptionHandler:createDelayedError()
    });
  }
}
