
// tabs
var initialtab = [1, "tabSetValue"];

function objectEval(text)
{
    // eval() breaks when we use it to get an object using the { a:42, b:'x' }
    // syntax because it thinks that { and } surround a block and not an object
    // So we wrap it in an array and extract the first element to get around
    // this.
    // The regex = [start of line][whitespace]{[stuff]}[whitespace][end of line]
    text = text.replace(/\n/g, " ");
    text = text.replace(/\r/g, " ");
    if (text.match(/^\s*\{.*\}\s*$/))
    {
        text = "[" + text + "]";
    }

    return eval(text)[0];
}

function doSetValue()
{
    DWRUtil.setValue($('setvalueele').value, $('setvalueval').value);
}

function doGetValue()
{
    var ret = DWRUtil.getValue($('getvalue').value);
    DWRUtil.setValue("getvalueret", ret);
}

function inlineExample()
{
    DWRUtil.setValue("setvalues", "{\n  b:'this is b content',\n  strong:'this is strong content',\n  i:'this is i content',\n  em:'this is em content',\n  cite:'this is cite content',\n  dfn:'this is dfn content',\n  u:'this is u content',\n  big:'this is big content',\n  small:'this is small content',\n  sub:'this is sub content',\n  sup:'this is sup content',\n  span:'this is span content',\n  tt:'this is tt content',\n  code:'this is code content',\n  kbd:'this is kbd content',\n  samp:'this is samp content',\n  acronym:'this is acronym content',\n  bdo:'this is bdo content',\n  q:'this is q content'\n}");
}

function inlineClear()
{
    DWRUtil.setValue("setvalues", "{\n  b:null,\n  strong:null,\n  i:null,\n  em:null,\n  cite:null,\n  dfn:null,\n  u:null,\n  big:null,\n  small:null,\n  sub:null,\n  sup:null,\n  span:null,\n  tt:null,\n  code:null,\n  kbd:null,\n  samp:null,\n  acronym:null,\n  bdo:null,\n  q:null\n}");
}

function blockExample()
{
    DWRUtil.setValue("setvalues", "{\n  p:'this is p content',\n  div:'this is div content',\n  h1:'h1',\n  h2:'h2',\n  h3:'h3',\n  h4:'h4',\n  h5:'h5'\n}");
}

function blockClear()
{
    DWRUtil.setValue("setvalues", "{\n  p:null,\n  div:null,\n  h1:null,\n  h2:null,\n  h3:null,\n  h4:null,\n  h5:null\n}");
}

function doSetValues()
{
    var text = DWRUtil.getValue('setvalues');
    var object = objectEval(text);
    DWRUtil.setValues(object);
}

function doGetValues()
{
    var text = DWRUtil.getValue('getvalues');
    var object = objectEval(text);
    DWRUtil.getValues(object);
    var reply = DWRUtil.toDescriptiveString(object);
    DWRUtil.setValue("getvaluesret", reply);
}

function doGetText()
{
    var ret = DWRUtil.getText($('gettext').value);
    DWRUtil.setValue("gettextret", ret);
}

function doGetText2()
{
    var ret = DWRUtil.getValue($('gettext2').value);
    DWRUtil.setValue("gettextret2", ret);
}
