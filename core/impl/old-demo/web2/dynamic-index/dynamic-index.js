
function init()
{
    DocIndex.getSiteMap(
    {
        callback:function(map)
        {
            DWRUtil.addRows("sitemap", map,
            [
                function(id) { return getContentsButton(id, map[id]); }
            ]);
        }
    });
}

function getContentsButton(id, name)
{
    var action = "show(\"" + id + "\")";
    return "<div class='link' onclick='" + action + "'>" + name + "</a>";
}

function show(id)
{
    DocIndex.getWebDocument(id, 
    {
        callback:draw
    });
}

function draw(webdoc)
{
    $("display").innerHTML = webdoc.contents;
}
