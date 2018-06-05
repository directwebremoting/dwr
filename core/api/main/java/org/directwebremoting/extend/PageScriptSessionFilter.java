package org.directwebremoting.extend;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ServerContext;

/**
 * A {@link ScriptSessionFilter} that only allows {@link ScriptSession}s
 * that match the given page.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PageScriptSessionFilter implements ScriptSessionFilter
{
    public PageScriptSessionFilter(ServerContext serverContext, String page)
    {
        PageNormalizer pageNormalizer = serverContext.getContainer().getBean(PageNormalizer.class);
        this.page = pageNormalizer.normalizePage(page);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptSessionFilter#match(org.directwebremoting.ScriptSession)
     */
    public boolean match(ScriptSession session)
    {
        return session.getPage().equals(page);
    }

    private final String page;
}
