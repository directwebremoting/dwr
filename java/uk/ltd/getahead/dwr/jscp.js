/**
 * Declare a constructor function to which we can add real functions.
 * @constructor
 */
function JSCP()
{
}

/**
 * Insert the contents of a URL at a given screen location
 * @param id The XML id to alter to contain the contents of the given URL
 * @param url The URL to read
 */
JSCP.insert = function(id, url)
{
    var req;

    if (window.XMLHttpRequest)
    {
        req = new XMLHttpRequest();
    }
    else
    {
        req = new ActiveXObject("Microsoft.XMLHTTP");
        if (!req)
        {
            alert("Creation of XMLHttpRequest object failed.");
        }
    }

    req.onreadystatechange = function() { JSCP._insertReceive(id, req); };
    req.open("GET", url, true);
    req.send(null);
}

/**
 * The second part of JSCP.insert
 * @private
 */
JSCP._insertReceive = function(id, req)
{
    if (req.readyState == 4)
    {
        if (req.status)
        {
            try
            {
                // We're not checking req.status here.
                // We used to check for 200, but sometimes we got a 304 instead
                // and it was never clear that an alert was better anyway.
                DWRUtil.setValue(id, req.responseText);
            }
            catch (ex)
            {
                alert(ex);
            }
        }
        else
        {
            alert("No response from remote server.");
        }
    }
}
