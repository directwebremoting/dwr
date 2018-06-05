package org.directwebremoting.dwrp;

import java.io.PrintWriter;

import org.directwebremoting.extend.ScriptConduit;

/**
 * A Handler standard DWR calls whose replies are NOT HTML wrapped.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PlainCallHandler extends BaseCallHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseCallHandler#createScriptConduit(java.io.PrintWriter, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    protected ScriptConduit createScriptConduit(PrintWriter out, String instanceId, String batchId, String documentDomain)
    {
        return new PlainScriptConduit(out, instanceId, (allowScriptTagRemoting ? null : scriptTagProtection));
    }

    /**
     * Do we allow ScriptTag remoting?
     * @param allowScriptTagRemoting The new value to set
     */
    public void setAllowScriptTagRemoting(boolean allowScriptTagRemoting)
    {
        this.allowScriptTagRemoting = allowScriptTagRemoting;
    }

    /**
     * Do we allow ScriptTag remoting.
     */
    private boolean allowScriptTagRemoting = false;

    /**
     * What is the string we use for script tag hack protection
     * @param scriptTagProtection the scriptTagProtection to set
     */
    public void setScriptTagProtection(String scriptTagProtection)
    {
        this.scriptTagProtection = scriptTagProtection;
    }

    /**
     * What is the string we use for script tag hack protection
     */
    private String scriptTagProtection;
}
