package org.directwebremoting.dwrp;

import java.io.PrintWriter;

import org.directwebremoting.extend.ScriptConduit;

/**
 * A Handler standard DWR calls whose replies are HTML wrapped.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HtmlCallHandler extends BaseCallHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseCallHandler#createScriptConduit(java.io.PrintWriter, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    protected ScriptConduit createScriptConduit(PrintWriter out, String instanceId, String batchId, String documentDomain)
    {
        return new HtmlScriptConduit(out, instanceId, batchId, documentDomain);
    }
}
