//  DHTML Window script- Copyright Dynamic Drive (http://www.dynamicdrive.com)
//  For full source code, documentation, and terms of usage,
//  Visit http://www.dynamicdrive.com/dynamicindex9/dhtmlwindow.htm

/***********************************************
* Alterations (C) Joe Walker 2005
***********************************************/

var dragging = null;
var initialwidth;
var initialheight;
var ie5 = document.all && document.getElementById;
var ns6 = document.getElementById && !document.all;

/**
 *
 */
function loadWindow(id)
{
    var ele = document.getElementById(id);

    // If we are already open, don't change position.
    if (ele.style.display != "block")
    {
        ele.style.display = "block";

        initialwidth = ele.style.width;
        initialheight = ele.style.height;

        // Center the window
        var el = document.getElementById(id);

        if (ie5)
        {
            var windowWidth = document.body.clientWidth;
            var windowHeight = document.body.clientHeight;
        }
        else
        {
            var windowWidth = window.innerWidth;
            var windowHeight = window.innerHeight;
        }

        var elWidth = el.offsetWidth;
        var elHeight = el.offsetHeight;

        var cx = Math.floor((windowWidth - elWidth) / 2);
        var cy = Math.floor((windowHeight - elHeight) / 2);

        // check we are not too close to the edge (or off it)
        if (cx < 10)
        {
            cx = 10;
        }
        if (cy < 10)
        {
            cy = 10;
        }

        el.style.left = cx + "px";
        el.style.top = cy + "px";
    }
}

/**
 *
 */
function closeWindow(id)
{
    var ele = document.getElementById(id);
    ele.style.display = "none";
}

/**
 *
 */
function ieCompatTest()
{
    if (!window.opera && document.compatMode && document.compatMode != "BackCompat")
    {
        return document.documentElement;
    }
    else
    {
        return document.body;
    }
}

/**
 *
 */
function initializeDrag(id, ev)
{
    var ele = document.getElementById(id);

    if (ie5)
    {
        offsetx = event.clientX;
        offsety = event.clientY;
    }
    else
    {
        offsetx = ev.clientX;
        offsety = ev.clientY;
    }

    tempx = parseInt(ele.style.left);
    tempy = parseInt(ele.style.top);
    
    dragging = ele;
    ele.onmousemove = dragDrop;
}

/**
 *
 */
function dragDrop(ev)
{
    if (dragging != null)
    {
        if (ie5 && event.button == 1)
        {
            dragging.style.left = tempx + event.clientX - offsetx + "px";
            dragging.style.top = tempy + event.clientY - offsety + "px";
        }
        else if (ns6)
        {
            dragging.style.left = tempx + ev.clientX - offsetx + "px";
            dragging.style.top = tempy + ev.clientY - offsety + "px";
        }
    }
}

/**
 *
 */
function maximize(id, max, maxico, resico)
{
    var ele = document.getElementById(id);
    var maxele = document.getElementById(max);

    var maximized = maxele.setAttribute("src") == resico;

    if (maximized)
    {
        // maximize window
        maxele.setAttribute("src", resico);

        if (ns6)
        {
            ele.style.width = window.innerWidth - 20 + "px";
            ele.style.height = window.innerHeight - 20 + "px";
        }
        else
        {
            ele.style.width = ieCompatTest().clientWidth + "px";
            ele.style.height = ieCompatTest().clientHeight + "px";
        }
    }
    else
    {
        // restore window
        maxele.setAttribute("src", maxico);

        ele.style.width = initialwidth;
        ele.style.height = initialheight;
    }

    if (ns6)
    {
        ele.style.left = window.pageXOffset + "px";
        ele.style.top = window.pageYOffset + "px";
    }
    else
    {
        ele.style.left = ieCompatTest().scrollLeft + "px";
        ele.style.top = ieCompatTest().scrollTop + "px";
    }
}

/**
 *
 */
function stopDrag(id)
{
    var ele = document.getElementById(id);

    dragging = null;
    ele.onmousemove = null;
}
