package org.directwebremoting.webwork;

import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.servlet.FileJavaScriptHandler;

/**
 * A Handler that supports requests for DWRActionUtil.js
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class WebworkUtilHandler extends FileJavaScriptHandler
{
    /**
     * Setup the default values
     */
    public WebworkUtilHandler()
    {
        super(DwrConstants.PACKAGE_PATH + "/webwork/DWRActionUtil.js", DwrConstants.PACKAGE_PATH + "/copyright.txt");
    }
}
