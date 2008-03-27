package uk.ltd.getahead.dwr.create;

import java.util.Map;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.Messages;

/**
 * A simple implementation of the basic parts of Creator
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractCreator implements Creator
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#setProperties(java.util.Map)
     */
    public void setProperties(Map params) throws IllegalArgumentException
    {
        // The default is to use getters and setters
    }

    /**
     * @return Returns the javascript name.
     */
    public String getJavascript()
    {
        return javascript;
    }

    /**
     * @param javascript The javascript name to set.
     */
    public void setJavascript(String javascript)
    {
        this.javascript = javascript;
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
     * @see uk.ltd.getahead.dwr.Creator#getScope()
     */
    public String getScope()
    {
        return scope;
    }

    /**
     * The javascript name for the class
     */
    private String javascript = null;

    /**
     * The scope of the objects created by this creator
     */
    private String scope = PAGE;

    /**
     * Is the given scope valid?
     * @param cscope The scope string to match
     */
    public static void checkScope(String cscope)
    {
        if (!cscope.equals(PAGE) &&
            !cscope.equals(REQUEST) &&
            !cscope.equals(SESSION) &&
            !cscope.equals(APPLICATION))
        {
            throw new IllegalArgumentException(Messages.getString("AbstractCreator.IllegalScope", cscope)); //$NON-NLS-1$
        }
    }
}
