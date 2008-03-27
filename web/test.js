var failures;
var progress;
var failreport;
var execreport;
var starttime;
var failcount = 0;
var index = 0;
var tests = new Array();

tests[tests.length] = { code:"Test.voidParam(testResults)", expected:null };

tests[tests.length] = { code:"Test.booleanParam(testResults, true)", expected:true };
tests[tests.length] = { code:"Test.booleanParam(testResults, false)", expected:false };

tests[tests.length] = { code:"Test.byteParam(testResults, -128)", expected:-128 };
tests[tests.length] = { code:"Test.byteParam(testResults, -1)", expected:-1 };
tests[tests.length] = { code:"Test.byteParam(testResults, 0)", expected:0 };
tests[tests.length] = { code:"Test.byteParam(testResults, 1)", expected:1 };
tests[tests.length] = { code:"Test.byteParam(testResults, 127)", expected:127 };

tests[tests.length] = { code:"Test.shortParam(testResults, -32768)", expected:-32768 };
tests[tests.length] = { code:"Test.shortParam(testResults, -1)", expected:-1 };
tests[tests.length] = { code:"Test.shortParam(testResults, 0)", expected:0 };
tests[tests.length] = { code:"Test.shortParam(testResults, 1)", expected:1 };
tests[tests.length] = { code:"Test.shortParam(testResults, 32767)", expected:32767 };

tests[tests.length] = { code:"Test.intParam(testResults, -2147483648)", expected:-2147483648 };
tests[tests.length] = { code:"Test.intParam(testResults, -1)", expected:-1 };
tests[tests.length] = { code:"Test.intParam(testResults, 0)", expected:0 };
tests[tests.length] = { code:"Test.intParam(testResults, 1)", expected:1 };
tests[tests.length] = { code:"Test.intParam(testResults, 2147483647)", expected:2147483647 };

// Mozilla rounds 9223372036854775808 to 9223372036854776000 which overflows so we round down
tests[tests.length] = { code:"Test.longParam(testResults, -9223372036854775000)", expected:-9223372036854775000 };
tests[tests.length] = { code:"Test.longParam(testResults, -1)", expected:-1 };
tests[tests.length] = { code:"Test.longParam(testResults, 0)", expected:0 };
tests[tests.length] = { code:"Test.longParam(testResults, 1)", expected:1 };
tests[tests.length] = { code:"Test.longParam(testResults, 9223372036854775000)", expected:9223372036854775000 };

tests[tests.length] = { code:"Test.floatParam(testResults, -100000000000000000000)", expected:-100000000000000000000 };
tests[tests.length] = { code:"Test.floatParam(testResults, -1)", expected:-1 };
tests[tests.length] = { code:"Test.floatParam(testResults, 0)", expected:0 };
tests[tests.length] = { code:"Test.floatParam(testResults, 1)", expected:1 };
tests[tests.length] = { code:"Test.floatParam(testResults, 100000000000000000000)", expected:100000000000000000000 };

tests[tests.length] = { code:"Test.doubleParam(testResults, -100000000000000000000)", expected:-100000000000000000000 };
tests[tests.length] = { code:"Test.doubleParam(testResults, -1)", expected:-1 };
tests[tests.length] = { code:"Test.doubleParam(testResults, 0)", expected:0 };
tests[tests.length] = { code:"Test.doubleParam(testResults, 1)", expected:1 };
tests[tests.length] = { code:"Test.doubleParam(testResults, 100000000000000000000)", expected:100000000000000000000 };

tests[tests.length] = { code:"Test.bigDecimalParam(testResults, -100000000000000000000)", expected:-100000000000000000000 };
tests[tests.length] = { code:"Test.bigDecimalParam(testResults, -1)", expected:-1 };
tests[tests.length] = { code:"Test.bigDecimalParam(testResults, 0)", expected:0 };
tests[tests.length] = { code:"Test.bigDecimalParam(testResults, 1)", expected:1 };
tests[tests.length] = { code:"Test.bigDecimalParam(testResults, 100000000000000000000)", expected:100000000000000000000 };

tests[tests.length] = { code:"Test.bigIntegerParam(testResults, -100000000000000000000)", expected:-100000000000000000000 };
tests[tests.length] = { code:"Test.bigIntegerParam(testResults, -1)", expected:-1 };
tests[tests.length] = { code:"Test.bigIntegerParam(testResults, 0)", expected:0 };
tests[tests.length] = { code:"Test.bigIntegerParam(testResults, 1)", expected:1 };
tests[tests.length] = { code:"Test.bigIntegerParam(testResults, 100000000000000000000)", expected:100000000000000000000 };

// Opera 8 has issues with this one. It appears to not like \0
tests[tests.length] = { code:"Test.charParam(testResults, '\\0')", expected:"\0" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\t')", expected:"\t" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\n')", expected:"\n" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\v')", expected:"\v" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\f')", expected:"\f" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\r')", expected:"\r" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\x07')", expected:"\x07" };

tests[tests.length] = { code:"Test.charParam(testResults, ' ')", expected:" " };
tests[tests.length] = { code:"Test.charParam(testResults, '!')", expected:"!" };
tests[tests.length] = { code:"Test.charParam(testResults, '\"')", expected:'"' };
tests[tests.length] = { code:"Test.charParam(testResults, '#')", expected:"#" };
tests[tests.length] = { code:"Test.charParam(testResults, '$')", expected:"$" };
tests[tests.length] = { code:"Test.charParam(testResults, '%')", expected:"%" };
tests[tests.length] = { code:"Test.charParam(testResults, '&')", expected:"&" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\'')", expected:"'" }; // double escape because it is evaled twice
tests[tests.length] = { code:"Test.charParam(testResults, '(')", expected:"(" };
tests[tests.length] = { code:"Test.charParam(testResults, ')')", expected:")" };
tests[tests.length] = { code:"Test.charParam(testResults, '*')", expected:"*" };
tests[tests.length] = { code:"Test.charParam(testResults, '+')", expected:"+" };
tests[tests.length] = { code:"Test.charParam(testResults, ',')", expected:"," };
tests[tests.length] = { code:"Test.charParam(testResults, '-')", expected:"-" };
tests[tests.length] = { code:"Test.charParam(testResults, '.')", expected:"." };
tests[tests.length] = { code:"Test.charParam(testResults, '/')", expected:"/" };
tests[tests.length] = { code:"Test.charParam(testResults, '0')", expected:"0" };
tests[tests.length] = { code:"Test.charParam(testResults, '9')", expected:"9" };
tests[tests.length] = { code:"Test.charParam(testResults, ':')", expected:":" };
tests[tests.length] = { code:"Test.charParam(testResults, ';')", expected:";" };
tests[tests.length] = { code:"Test.charParam(testResults, '<')", expected:"<" };
tests[tests.length] = { code:"Test.charParam(testResults, '=')", expected:"=" };
tests[tests.length] = { code:"Test.charParam(testResults, '>')", expected:">" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '@')", expected:"@" };
tests[tests.length] = { code:"Test.charParam(testResults, 'A')", expected:"A" };
tests[tests.length] = { code:"Test.charParam(testResults, 'Z')", expected:"Z" };
tests[tests.length] = { code:"Test.charParam(testResults, '[')", expected:"[" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\\\')", expected:"\\" }; // double escape because it is evaled twice
tests[tests.length] = { code:"Test.charParam(testResults, ']')", expected:"]" };
tests[tests.length] = { code:"Test.charParam(testResults, '^')", expected:"^" };
tests[tests.length] = { code:"Test.charParam(testResults, '_')", expected:"_" };
tests[tests.length] = { code:"Test.charParam(testResults, '`')", expected:"`" };
tests[tests.length] = { code:"Test.charParam(testResults, 'a')", expected:"a" };
tests[tests.length] = { code:"Test.charParam(testResults, 'z')", expected:"z" };
tests[tests.length] = { code:"Test.charParam(testResults, '{')", expected:"{" };
tests[tests.length] = { code:"Test.charParam(testResults, '|')", expected:"|" };
tests[tests.length] = { code:"Test.charParam(testResults, '}')", expected:"}" };
tests[tests.length] = { code:"Test.charParam(testResults, '~')", expected:"~" };

tests[tests.length] = { code:"Test.stringParam(testResults, ' ')", expected:" " };
tests[tests.length] = { code:"Test.stringParam(testResults, '!')", expected:"!" };
tests[tests.length] = { code:"Test.stringParam(testResults, '\"')", expected:'"' };
tests[tests.length] = { code:"Test.stringParam(testResults, '#')", expected:"#" };
tests[tests.length] = { code:"Test.stringParam(testResults, '$')", expected:"$" };
tests[tests.length] = { code:"Test.stringParam(testResults, '%')", expected:"%" };
tests[tests.length] = { code:"Test.stringParam(testResults, '&')", expected:"&" };
tests[tests.length] = { code:"Test.stringParam(testResults, '\\'')", expected:"'" }; // double escape because it is evaled twice
tests[tests.length] = { code:"Test.stringParam(testResults, '(')", expected:"(" };
tests[tests.length] = { code:"Test.stringParam(testResults, ')')", expected:")" };
tests[tests.length] = { code:"Test.stringParam(testResults, '*')", expected:"*" };
tests[tests.length] = { code:"Test.stringParam(testResults, '+')", expected:"+" };
tests[tests.length] = { code:"Test.stringParam(testResults, ',')", expected:"," };
tests[tests.length] = { code:"Test.stringParam(testResults, '-')", expected:"-" };
tests[tests.length] = { code:"Test.stringParam(testResults, '.')", expected:"." };
tests[tests.length] = { code:"Test.stringParam(testResults, '/')", expected:"/" };
tests[tests.length] = { code:"Test.stringParam(testResults, '0')", expected:"0" };
tests[tests.length] = { code:"Test.stringParam(testResults, '9')", expected:"9" };
tests[tests.length] = { code:"Test.stringParam(testResults, ':')", expected:":" };
tests[tests.length] = { code:"Test.stringParam(testResults, ';')", expected:";" };
tests[tests.length] = { code:"Test.stringParam(testResults, '<')", expected:"<" };
tests[tests.length] = { code:"Test.stringParam(testResults, '=')", expected:"=" };
tests[tests.length] = { code:"Test.stringParam(testResults, '>')", expected:">" };
tests[tests.length] = { code:"Test.stringParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.stringParam(testResults, '@')", expected:"@" };
tests[tests.length] = { code:"Test.stringParam(testResults, 'A')", expected:"A" };
tests[tests.length] = { code:"Test.stringParam(testResults, 'Z')", expected:"Z" };
tests[tests.length] = { code:"Test.stringParam(testResults, '[')", expected:"[" };
tests[tests.length] = { code:"Test.stringParam(testResults, '\\\\')", expected:"\\" }; // double escape because it is evaled twice
tests[tests.length] = { code:"Test.stringParam(testResults, ']')", expected:"]" };
tests[tests.length] = { code:"Test.stringParam(testResults, '^')", expected:"^" };
tests[tests.length] = { code:"Test.stringParam(testResults, '_')", expected:"_" };
tests[tests.length] = { code:"Test.stringParam(testResults, '`')", expected:"`" };
tests[tests.length] = { code:"Test.stringParam(testResults, 'a')", expected:"a" };
tests[tests.length] = { code:"Test.stringParam(testResults, 'z')", expected:"z" };
tests[tests.length] = { code:"Test.stringParam(testResults, '{')", expected:"{" };
tests[tests.length] = { code:"Test.stringParam(testResults, '|')", expected:"|" };
tests[tests.length] = { code:"Test.stringParam(testResults, '}')", expected:"}" };
tests[tests.length] = { code:"Test.stringParam(testResults, '~')", expected:"~" };

tests[tests.length] = { code:"Test.stringParam(testResults, '')", expected:"" };
tests[tests.length] = { code:"Test.stringParam(testResults, null)", expected:null };
tests[tests.length] = { code:"Test.stringParam(testResults, 'null')", expected:"null" };

tests[tests.length] = { code:"Test.stringParam(testResults, ' !\"#$%&\\'()*+,-/')", expected:" !\"#$%&\'()*+,-/" };
tests[tests.length] = { code:"Test.stringParam(testResults, '0123456789')", expected:"0123456789" };
tests[tests.length] = { code:"Test.stringParam(testResults, ':;<=>?@')", expected:":;<=>?@" };
tests[tests.length] = { code:"Test.stringParam(testResults, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ')", expected:"ABCDEFGHIJKLMNOPQRSTUVWXYZ" };
tests[tests.length] = { code:"Test.stringParam(testResults, '[\\\\]^_`')", expected:"[\\]^_`" };
tests[tests.length] = { code:"Test.stringParam(testResults, 'abcdefghijklmnopqrstuvwxyz')", expected:"abcdefghijklmnopqrstuvwxyz" };
tests[tests.length] = { code:"Test.stringParam(testResults, '{|}~')", expected:"{|}~" };

tests[tests.length] = { code:"Test.stringParam(testResults, 'call.callback = null')", expected:"call.callback = null" };
tests[tests.length] = { code:"Test.stringStringParam(testResults, ' !\"#$%&\\'()*+,-/0123456789', ':;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ')", expected:"param1=' !\"#$%&\'()*+,-/0123456789' param2=':;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ'" };
tests[tests.length] = { code:"Test.stringStringParam(testResults, '[\\\\]^_`', 'abcdefghijklmnopqrstuvwxyz{|}~')", expected:"param1='[\\]^_`' param2='abcdefghijklmnopqrstuvwxyz{|}~'" };
tests[tests.length] = { code:"Test.booleanArrayParam(testResults, [ true, false, true, false ])", expected:[ true, false, true, false ] };
tests[tests.length] = { code:"Test.charArrayParam(testResults, [ 'a', ',', '[', ']' ])", expected:[ 'a', ',', '[', ']' ] };
tests[tests.length] = { code:"Test.byteArrayParam(testResults, [ -128, -128, -128, -128, -127 ])", expected:[ -128, -128, -128, -128, -127 ] };
tests[tests.length] = { code:"Test.byteArrayParam(testResults, [ -128, -1, 0, 1, 127 ])", expected:[ -128, -1, 0, 1, 127 ] };
tests[tests.length] = { code:"Test.shortArrayParam(testResults, [ -32768, -1, 0, 1, 32767 ])", expected:[ -32768, -1, 0, 1, 32767 ] };
tests[tests.length] = { code:"Test.intArrayParam(testResults, [ -2147483648, -1, 0, 1, 2147483647 ])", expected:[ -2147483648, -1, 0, 1, 2147483647 ] };
tests[tests.length] = { code:"Test.longArrayParam(testResults, [ -9223372036854775000, -1, 0, 1, 9223372036854775000 ])", expected:[ -9223372036854775000, -1, 0, 1, 9223372036854775000 ] };
tests[tests.length] = { code:"Test.floatArrayParam(testResults, [ -100000000000000000000, -1, 0, 1, 100000000000000000000 ])", expected:[ -100000000000000000000, -1, 0, 1, 100000000000000000000 ] };
tests[tests.length] = { code:"Test.doubleArrayParam(testResults, [ -100000000000000000000, -1, 0, 1, 100000000000000000000 ])", expected:[ -100000000000000000000, -1, 0, 1, 100000000000000000000 ] };

// Unicode: we could be here for some time, so I just picked some commmon ones
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };
tests[tests.length] = { code:"Test.charParam(testResults, '?')", expected:"?" };

// Many of these are duplicates, maybe this tests encoding differently?
tests[tests.length] = { code:"Test.charParam(testResults, '\\u0080')", expected:"\u0080" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u0091')", expected:"\u0091" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u0092')", expected:"\u0092" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u0093')", expected:"\u0093" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u0094')", expected:"\u0094" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u0095')", expected:"\u0095" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u0098')", expected:"\u0098" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u0099')", expected:"\u0099" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u00A0')", expected:"\u00A0" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u00A3')", expected:"\u00A3" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u00A5')", expected:"\u00A5" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u00A6')", expected:"\u00A6" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u00A9')", expected:"\u00A9" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u00AC')", expected:"\u00AC" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u00C7')", expected:"\u00C7" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u00C6')", expected:"\u00C6" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u00DF')", expected:"\u00DF" };
tests[tests.length] = { code:"Test.charParam(testResults, '\\u00FF')", expected:"\u00FF" };

var nested = { integer:0, string:'0123456789' };
nested.testBean = nested;

tests[tests.length] = { code:"Test.byteArrayParam(testResults, [ -128, -128, -128, -128, -127 ])", expected:[ -128, -128, -128, -128, -127 ] };

tests[tests.length] = { code:"Test.testBeanParam(testResults, { integer:-2147483648, string:'!\"$%^&*()', testBean:null })", expected:{ integer:-2147483648, string:'!"$%^&*()', testBean:null } };
tests[tests.length] = { code:"Test.testBeanParam(testResults, { integer:-1, string:'Null', testBean:null })", expected:{ integer:-1, string:'Null', testBean:null } };
tests[tests.length] = { code:"Test.testBeanParam(testResults, { integer:0, string:'null', testBean:null })", expected:{ integer:0, string:'null', testBean:null } };
tests[tests.length] = { code:"Test.testBeanParam(testResults, { integer:1, string:'0987654321', testBean:nested })", expected:{ integer:1, string:'0987654321', testBean:nested } };

tests[tests.length] = { code:"Test.stringCollectionParam(testResults, [ 'abcdef', 'hgijklm', 'nopqrst' ])", expected:[ 'abcdef', 'hgijklm', 'nopqrst' ]};
tests[tests.length] = { code:"Test.stringLinkedListParam(testResults, [ 'abcdef', 'hgijklm', 'nopqrst' ])", expected:[ 'abcdef', 'hgijklm', 'nopqrst' ]};
tests[tests.length] = { code:"Test.stringArrayListParam(testResults, [ 'abcdef', 'hgijklm', 'nopqrst' ])", expected:[ 'abcdef', 'hgijklm', 'nopqrst' ]};
tests[tests.length] = { code:"Test.stringListParam(testResults, [ 'abcdef', 'hgijklm', 'nopqrst' ])", expected:[ 'abcdef', 'hgijklm', 'nopqrst' ]};
// Note the next 2 are unordered so we cheat by only having 1 element
tests[tests.length] = { code:"Test.stringSetParam(testResults, [ 'abcdef' ])", expected:[ 'abcdef' ]};
tests[tests.length] = { code:"Test.stringHashSetParam(testResults, [ 'abcdef' ])", expected:[ 'abcdef' ]};
tests[tests.length] = { code:"Test.stringTreeSetParam(testResults, [ 'abcdef', 'hgijklm', 'nopqrst' ])", expected:[ 'abcdef', 'hgijklm', 'nopqrst' ]};

tests[tests.length] = { code:"Test.stringCollectionParam(testResults, [ ])", expected:[ ]};
tests[tests.length] = { code:"Test.stringLinkedListParam(testResults, [ ])", expected:[ ]};
tests[tests.length] = { code:"Test.stringArrayListParam(testResults, [ ])", expected:[ ]};
tests[tests.length] = { code:"Test.stringListParam(testResults, [ ])", expected:[ ]};
tests[tests.length] = { code:"Test.stringSetParam(testResults, [ ])", expected:[ ]};
tests[tests.length] = { code:"Test.stringHashSetParam(testResults, [ ])", expected:[ ]};
tests[tests.length] = { code:"Test.stringTreeSetParam(testResults, [ ])", expected:[ ]};

tests[tests.length] = { code:"Test.stringCollectionParam(testResults, [ 'abcdef' ])", expected:[ 'abcdef' ]};
tests[tests.length] = { code:"Test.stringLinkedListParam(testResults, [ 'abcdef' ])", expected:[ 'abcdef' ]};
tests[tests.length] = { code:"Test.stringArrayListParam(testResults, [ 'abcdef' ])", expected:[ 'abcdef' ]};
tests[tests.length] = { code:"Test.stringListParam(testResults, [ 'abcdef' ])", expected:[ 'abcdef' ]};
tests[tests.length] = { code:"Test.stringSetParam(testResults, [ 'abcdef' ])", expected:[ 'abcdef' ]};
tests[tests.length] = { code:"Test.stringHashSetParam(testResults, [ 'abcdef' ])", expected:[ 'abcdef' ]};
tests[tests.length] = { code:"Test.stringTreeSetParam(testResults, [ 'abcdef' ])", expected:[ 'abcdef' ]};

tests[tests.length] = { code:"Test.stringCollectionParam(testResults, [ ',\\'{}[]' ])", expected:[ ",'{}[]" ]};
tests[tests.length] = { code:"Test.stringLinkedListParam(testResults, [ ',\\'{}[]' ])", expected:[ ",'{}[]" ]};
tests[tests.length] = { code:"Test.stringArrayListParam(testResults, [ ',\\'{}[]' ])", expected:[ ",'{}[]" ]};
tests[tests.length] = { code:"Test.stringListParam(testResults, [ ',\\'{}[]' ])", expected:[ ",'{}[]" ]};
tests[tests.length] = { code:"Test.stringSetParam(testResults, [ ',\\'{}[]' ])", expected:[ ",'{}[]" ]};
tests[tests.length] = { code:"Test.stringHashSetParam(testResults, [ ',\\'{}[]' ])", expected:[ ",'{}[]" ]};
tests[tests.length] = { code:"Test.stringTreeSetParam(testResults, [ ',\\'{}[]' ])", expected:[ ",'{}[]" ]};

tests[tests.length] = { code:"Test.stringCollectionParam(testResults, [ ',\\'{}[]', 'null', ',\\'{}[]' ])", expected:[ ",'{}[]", 'null', ",'{}[]" ]};
tests[tests.length] = { code:"Test.stringLinkedListParam(testResults, [ ',\\'{}[]', 'null', ',\\'{}[]' ])", expected:[ ",'{}[]", 'null', ",'{}[]" ]};
tests[tests.length] = { code:"Test.stringArrayListParam(testResults, [ ',\\'{}[]', 'null', ',\\'{}[]' ])", expected:[ ",'{}[]", 'null', ",'{}[]" ]};
tests[tests.length] = { code:"Test.stringListParam(testResults, [ ',\\'{}[]', 'null', ',\\'{}[]' ])", expected:[ ",'{}[]", 'null', ",'{}[]" ]};

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

tests[tests.length] = { code:"Test.stringStringMapParam(testResults, map1)", expected:map1 };
tests[tests.length] = { code:"Test.stringStringMapParam(testResults, map2)", expected:map2 };
tests[tests.length] = { code:"Test.stringStringHashMapParam(testResults, map1)", expected:map1 };
tests[tests.length] = { code:"Test.stringStringHashMapParam(testResults, map2)", expected:map2 };
tests[tests.length] = { code:"Test.stringStringTreeMapParam(testResults, map1)", expected:map1 };
tests[tests.length] = { code:"Test.stringStringTreeMapParam(testResults, map2)", expected:map2 };

function startTest()
{
    failures = DWRUtil.getElementById("failures");
    progress = DWRUtil.getElementById("progress");
    failreport = DWRUtil.getElementById("failreport");
    execreport = DWRUtil.getElementById("execreport");

    DWRUtil.getElementById("start").disabled = true;

    DWREngine.setErrorHandler(catchFailure);

    failures.innerHTML = "";
    failreport.innerHTML = "0";
    execreport.innerHTML = "0";
    progress.style.background = "green";
    progress.style.width = "0%";

    starttime = new Date();
    index = 0;
    failcount = 0;

    DWRUtil.showById("reply");

    executeTest();
}

function executeTest()
{
    if (index >= tests.length)
    {
        DWRUtil.getElementById("start").disabled = false;

        var mslen = new Date().getTime() - starttime.getTime();
        var tps = Math.round((10000 * tests.length) / mslen) / 10;
        var totals = "Tests executed in " + (mslen / 1000) + " seconds at an average of " + tps + " tests per second.";
        DWRUtil.getElementById("totals").innerHTML = totals;

        return;
    }

    var test = tests[index];
    eval(test.code); // this results in the calling of testResults()
}

function testResults(data)
{
    // which test have we just run?
    var test = tests[index];

    //var progress = DWRUtil.getElementById("progress");

    // TODO: maybe make a separate function for testing equality
    var passed = testEquals(data, test.expected, 0);
    if (typeof passed == "boolean" && passed)
    {
        //passes.innerHTML += test.code + "<br/>";
        execreport.innerHTML = (index + 1);
    }
    else
    {
        failures.innerHTML += test.code + "<br/>&nbsp;&nbsp;" + passed + "<br/>&nbsp;&nbsp;Expected: " + DWRUtil.toDescriptiveString(test.expected) + " Actual: " + DWRUtil.toDescriptiveString(data) + "<br/>";
        failcount++;
        failreport.innerHTML = failcount;

        if (failcount == 1)
        {
            // This is the first failure - make the bar go red
            progress.style.background = "red";
        }
    }

    index++;
    var percent = Math.ceil((index / tests.length) * 100);
    progress.style.width = percent + "%";

    executeTest();
}

function testEquals(actual, expected, depth)
{
    // Rather than failing we assume that it works!
    if (depth > 10)
    {
        return true;
    }

    if (expected == null)
    {
        if (actual != null)
        {
            return "expected: null, actual non-null: " + typeof actual + ":" + actual;
        }

        return true;
    }

    if (actual == null)
    {
        if (expected != null)
        {
            return "actual: null, expected non-null: " + typeof expected + ":" + expected;
        }

        // wont get here of course ...
        return true;
    }

    if (expected instanceof Object)
    {
        if (!(actual instanceof Object))
        {
            return "expected object, actual not an object";
        }

        var actualLength = 0;
        for (var prop in actual)
        {
            if (typeof actual[prop] != "function" || typeof expected[prop] != "function")
            {
                var nest = testEquals(actual[prop], expected[prop], depth + 1);
                if (typeof nest != "boolean" || !nest)
                {
                    return "element '" + prop + "' does not match: " + nest;
                }
            }

            actualLength++;
        }

        // need to check length too
        var expectedLength = 0;
        for (prop in expected)
        {
            expectedLength++;
        }

        if (actualLength != expectedLength)
        {
            return "expected object size = " + expectedLength + ", actual object size = " + actualLength;
        }

        return true;
    }

    if (actual != expected)
    {
        return "expected = " + expected + " (type=" + typeof expected + "), actual = " + actual + " (type=" + typeof actual + ")";
    }

    if (expected instanceof Array)
    {
        if (!(actual instanceof Array))
        {
            return "expected array, actual not an array";
        }

        if (actual.length != expected.length)
        {
            return "expected array length = " + expected.length + ", actual array length = " + actual.length;
        }

        for (var i = 0; i < actual.length; i++)
        {
            var nest = testEquals(actual[i], expected[i], depth + 1);
            if (typeof nest != "boolean" || !nest)
            {
                return "element " + i + " does not match: " + nest;
            }
        }

        return true;
    }

    return true;
}

function catchFailure(data)
{
    // which test have we just run?
    var test = tests[index];
    var code = "Unknown";
    if (test && test.code)
    {
        code = test.code;
    }

    failures.innerHTML += code + "<br/>&nbsp;&nbsp;Error: " + data + "<br/>";
    failcount++;
    failreport.innerHTML = failcount;

    if (failcount == 1)
    {
        // This is the first failure - make the bar go red
        progress.style.background = "red";
    }

    index++;
    executeTest();
}

function nestTest()
{
    Test.getNestingTest(nestReply);
}

function report()
{
    Test.reply(showResults, {
        totals:DWRUtil.getValue("totals"),
        moreinfo:DWRUtil.getValue("moreinfo"),
        failures:DWRUtil.getValue("failures"),
        execreport:DWRUtil.getValue("execreport"),
        failreport:DWRUtil.getValue("failreport"),
        useragentReported:DWRUtil.getValue("useragent-reported"),
        useragentReal:DWRUtil.getValue("useragent-real")
    });
}

function showResults(data)
{
    DWRUtil.showById("results");
    DWRUtil.clearChildNodes("resultsTable");
    DWRUtil.drawTable("resultsTable", data, [
        function(row)
        {
            return row;
        },
        function(row)
        {
            var results = data[row];
            var reply = "";
            for (var report in results)
            {
                reply += report + " failures, reported " + results[report] + " time(s).<br/>";
            }
            return reply;
        }
    ]);
}

function init()
{
    DWRUtil.setValue("useragent-real", navigator.userAgent);
    DWRUtil.setValue("useragent-reported", navigator.userAgent);
}

