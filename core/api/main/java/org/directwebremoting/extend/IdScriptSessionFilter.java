package org.directwebremoting.extend;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;

/**
 * A {@link ScriptSessionFilter} that only allows {@link ScriptSession}s
 * that match the given session id.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class IdScriptSessionFilter implements ScriptSessionFilter
{
    public IdScriptSessionFilter(String id)
    {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionFilter#match(org.directwebremoting.ScriptSession)
     */
    public boolean match(ScriptSession session)
    {
        return session.getId().equals(id);
    }

    private final String id;
}
