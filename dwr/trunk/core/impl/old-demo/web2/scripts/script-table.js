
function direct(data) { return data; }

function altered(data) { return data.toUpperCase(); }

function dom(data)
{
    var input = document.createElement("input");
    input.setAttribute("type", "button");
    input.setAttribute("value", "DOM Test");
    input.setAttribute("onclick", "alert('" + data + "');");
    return input;
}

function innerhtml(data)
{
    return "<input type='button' value='innerHTML Test' onclick='alert(\"" + data + "\");'>";
}

function doAddRows()
{
    DWRUtil.addRows('demo1', eval($('simple').value), [ direct, altered, dom, innerhtml ]);
}
