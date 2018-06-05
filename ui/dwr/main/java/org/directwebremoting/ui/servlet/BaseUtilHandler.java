package org.directwebremoting.ui.servlet;

import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.servlet.FileJavaScriptHandler;

/**
 * A Handler that supports requests for util.js
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BaseUtilHandler extends FileJavaScriptHandler
{
    /**
     * Setup the default values
     */
    public BaseUtilHandler()
    {
        super(DwrConstants.PACKAGE_PATH + "/ui/servlet/util.js", DwrConstants.PACKAGE_PATH + "/copyright.txt");
    }
}
