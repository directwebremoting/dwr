package org.directwebremoting.extend;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;

/**
 * A {@link ScriptSessionFilter} that passes everything.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class AllScriptSessionFilter implements ScriptSessionFilter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionFilter#match(org.directwebremoting.ScriptSession)
     */
    public boolean match(ScriptSession session)
    {
        return true;
    }
}
