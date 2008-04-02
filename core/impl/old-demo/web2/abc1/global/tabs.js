
/***********************************************
* Tab Content script- (C) Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/

/***********************************************
* Alterations (C) Joe Walker 2005
***********************************************/

function Tabs()
{
}

/**
 * Move to new tab
 * @param cid The name of the tab that should be made visible
 * @param ele The 'a' element that we change the highlighting of
 */
Tabs.expandContent = function(cid, ele)
{
    if (document.getElementById)
    {
        Tabs.highlightTab(ele, "tablist");

        if (Tabs.previousTab != "")
        {
            var prevele = document.getElementById(Tabs.previousTab);
            if (prevele == null)
            {
                alert("Tabs.expandContent() can't find element with id '" + Tabs.previousTab + "'");
                throw Tabs.previousTab;
            }

            prevele.style.display = "none";
        }

        var ele = document.getElementById(cid);
        if (ele == null)
        {
            alert("Tabs.expandContent() can't find element with id '" + cid + "'");
            throw cid;
        }

        ele.style.display = "block";
        Tabs.previousTab = cid;

        if (ele.blur)
        {
            ele.blur();
        }

        return false;
    }
    else
    {
        return true;
    }
}

Tabs.previousTab = "";

/**
 * For internal use only
 * Highlight a tab link
 * @param ele The 'a' element that we change the highlighting of
 * @param id The id of the list of tabs i.e. the ul container
 */
Tabs.highlightTab = function(ele, id)
{
    var tabLinks = Tabs.collectTabLinks(id);

    for (i = 0; i < tabLinks.length; i++)
    {
        tabLinks[i].style.backgroundColor = initTabcolor;
    }

    var themecolor = ele.getAttribute("theme");
    if (themecolor == null)
    {
        themecolor = initTabpostcolor;
    }

    ele.style.backgroundColor = themecolor;
    document.getElementById("tabcontentcontainer").style.backgroundColor = themecolor;
}

/**
 * Find all the 'a' elements in the list of tabs
 */
Tabs.collectTabLinks = function(id)
{
    var tabobj = document.getElementById(id);
    return tabobj.getElementsByTagName("A");
}

/**
 * Various setup functions
 */
Tabs.init = function()
{
    var tabLinks = Tabs.collectTabLinks("tablist");

    initTabcolor = Tabs.getComputedValue(tabLinks[1], "backgroundColor", "background-color");
    initTabpostcolor = Tabs.getComputedValue(tabLinks[0], "backgroundColor", "background-color");

    Tabs.expandContent(initialtab[1], tabLinks[initialtab[0]-1]);
}

/**
 * This should probably be in a utility somewhere.
 * @param ele The element to get the actual style information from.
 * @param cssproperty The scripting version of the style to read (used by IE)
 * @param csspropertyNS The CSS version of the style to read (as in DOM standard)
 */
Tabs.getComputedValue = function(ele, cssproperty, csspropertyNS)
{
    if (ele.currentStyle)
    {
        return ele.currentStyle[cssproperty];
    }
    else if (window.getComputedStyle)
    {
        var elstyle = window.getComputedStyle(ele, "");
        return elstyle.getPropertyValue(csspropertyNS);
    }
}

if (window.addEventListener)
{
    window.addEventListener("load", Tabs.init, false);
}
else if (window.attachEvent)
{
    window.attachEvent("onload", Tabs.init);
}
else
{
    window.onload = Tabs.init;
}
