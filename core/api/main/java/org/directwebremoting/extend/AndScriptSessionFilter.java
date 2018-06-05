package org.directwebremoting.extend;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;

/**
 * A {@link ScriptSessionFilter} that only allows {@link ScriptSession}s
 * that match pass both the given {@link ScriptSessionFilter}s
 */
public class AndScriptSessionFilter implements ScriptSessionFilter
{
    public AndScriptSessionFilter(ScriptSessionFilter left, ScriptSessionFilter right)
    {
        this.left = left;
        this.right = right;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionFilter#match(org.directwebremoting.ScriptSession)
     */
    public boolean match(ScriptSession session)
    {
        return left.match(session) && right.match(session);
    }

    private final ScriptSessionFilter left;

    private final ScriptSessionFilter right;
}
