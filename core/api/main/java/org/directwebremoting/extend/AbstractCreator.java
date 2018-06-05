package org.directwebremoting.extend;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.util.LocalUtil;

/**
 * A simple implementation of the basic parts of Creator
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractCreator implements Creator
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#setProperties(java.util.Map)
     */
    public void setProperties(Map<String, String> params) throws IllegalArgumentException
    {
        // The default is to use getters and setters
    }

    /**
     * @return Returns the javascript name.
     */
    public String getJavascript()
    {
        if (!javascriptInferred)
        {
            this.javascript = LocalUtil.inferWildcardReplacements(getType().getName(), javascript);
        }
        return javascript;
    }

    /**
     * @param javascript The javascript name to set.
     */
    public void setJavascript(String javascript)
    {
        if (!LocalUtil.isValidScriptName(javascript))
        {
            log.error("Illegal identifier: '" + javascript + "'");
            throw new IllegalArgumentException("Illegal identifier");
        }

        this.javascript = javascript;
        this.javascriptInferred = false;
    }

    /**
     * @param scope Set the scope.
     */
    public void setScope(String scope)
    {
        checkScope(scope);
        this.scope = scope;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getScope()
     */
    public String getScope()
    {
        return scope;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Creator#isCacheable()
     */
    public boolean isCacheable()
    {
        return cacheable;
    }

    /**
     * @param cacheable Whether or not to cache the script.
     * @see org.directwebremoting.extend.Creator#isCacheable()
     */
    public void setCacheable(boolean cacheable)
    {
        this.cacheable = cacheable;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Creator#isHidden()
     */
    public boolean isHidden()
    {
        return hidden;
    }

    /**
     * @param hidden the new hidden status.
     * @see #isHidden()
     */
    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    /**
     * Is the given scope valid?
     * @param cscope The scope string to match
     */
    protected static void checkScope(String cscope)
    {
        if (!cscope.equals(SCRIPT) && !cscope.equals(PAGE) && !cscope.equals(REQUEST) && !cscope.equals(SESSION) && !cscope.equals(APPLICATION))
        {
            throw new IllegalArgumentException("Illegal scope '" + cscope + "'. application, session, request, script or page required.");
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "[" + getJavascript() + "]";
    }

    /**
     * Do we attempt to deny the existence of classes created by this creator
     */
    private boolean hidden = false;

    /**
     * Do the methods on the Creator change over time?
     * TODO: This is only used by the dynamicReload function of ScriptedCreator
     * we might be able to simplify several things if we remove this
     */
    private boolean cacheable = false;

    /**
     * The javascript name for the class
     */
    private String javascript = null;

    /**
     * Has wildcard replacement been done on the javascript name?
     */
    private boolean javascriptInferred = false;

    /**
     * The scope of the objects created by this creator
     */
    private String scope = PAGE;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(AbstractCreator.class);
}
