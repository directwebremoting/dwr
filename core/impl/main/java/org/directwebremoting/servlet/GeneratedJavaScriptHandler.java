package org.directwebremoting.servlet;

import org.directwebremoting.extend.Remoter;
import org.directwebremoting.util.LocalUtil;

/**
 * A base class for all generated JavaScript files
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public abstract class GeneratedJavaScriptHandler extends JavaScriptHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.CachingHandler#getLastModifiedTime()
     */
    @Override
    protected long getLastModifiedTime()
    {
        return LocalUtil.getSystemClassloadTime();
    }

    /**
     * Setter for the remoter
     * @param remoter The new remoter
     */
    public void setRemoter(Remoter remoter)
    {
        this.remoter = remoter;
    }

    /**
     * The bean to execute remote requests and generate interfaces
     */
    protected Remoter remoter = null;
}
