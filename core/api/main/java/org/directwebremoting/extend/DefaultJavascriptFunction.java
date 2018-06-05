package org.directwebremoting.extend;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.io.JavascriptFunction;

/**
 * Represents a callback function, passed in from a client for later execution.
 * <p>A DefaultJavascriptFunction is tied to a specific function in a specific browser
 * page. In this way the eval of a DefaultJavascriptFunction is outside of the normal
 * execution scoping provided by {@link org.directwebremoting.Browser}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultJavascriptFunction implements JavascriptFunction
{
    public DefaultJavascriptFunction(ScriptSession session, String id)
    {
        this.session = session;
        this.id = id;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.JavascriptFunction#execute(java.lang.Object[])
     */
    public void execute(Object... params)
    {
        ScriptBuffer script = EnginePrivate.getRemoteExecuteFunctionScript(id, params);
        session.addScript(script);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.JavascriptFunction#close()
     */
    public void close()
    {
        ScriptBuffer script = EnginePrivate.getRemoteCloseFunctionScript(id);
        session.addScript(script);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.JavascriptFunction#executeAndClose(java.lang.Object[])
     */
    public void executeAndClose(Object... params)
    {
        execute(params);
        close();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "browser[" + session.getId() + "].functions[" + id + "](...)";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass())
        {
            return false;
        }

        DefaultJavascriptFunction that = (DefaultJavascriptFunction) obj;

        if (!this.session.equals(that.session))
        {
            return false;
        }

        if (!this.id.equals(that.id))
        {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hash = 523;
        hash += (id == null) ? 392 : id.hashCode();
        hash += (session == null) ? 894 : session.hashCode();
        return hash;
    }

    /**
     * The browser window that owns this function
     */
    private final ScriptSession session;

    /**
     * The id into the function cache
     */
    private final String id;
}
