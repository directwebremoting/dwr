package org.directwebremoting.extend;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.io.JavascriptObject;

/**
 * Represents a callback function, passed in from a client for later execution.
 * <p>A DefaultJavascriptFunction is tied to a specific function in a specific browser
 * page. In this way the eval of a DefaultJavascriptFunction is outside of the normal
 * execution scoping provided by {@link org.directwebremoting.Browser}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultJavascriptObject implements JavascriptObject, InvocationHandler
{
    public DefaultJavascriptObject(ScriptSession session, String id)
    {
        this.session = session;
        this.id = id;
    }

    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        if (method.getName().equals("equals") && args.length == 1)
        {
            return equals(args[0]);
        }

        if (method.getName().equals("hashCode") && (args == null || args.length == 0))
        {
            return hashCode();
        }

        if (method.getName().equals("toString") && (args == null || args.length == 0))
        {
            return toString();
        }

        ScriptBuffer script = EnginePrivate.getRemoteExecuteObjectScript(id, method.getName(), args);
        session.addScript(script);
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.JavascriptObject#execute(String, java.lang.Object[])
     */
    public void execute(String methodName, Object... params)
    {
        ScriptBuffer script = EnginePrivate.getRemoteExecuteObjectScript(id, methodName, params);
        session.addScript(script);
    }

    /**
     * Add a property to a JavaScript object. The type of the data must be
     * convertible by DWR.
     */
    public void set(String propertyName, Object data)
    {
        ScriptBuffer script = EnginePrivate.getRemoteSetObjectScript(id, propertyName, data);
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
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "browser[" + session.getId() + "].object[" + id + "](...)";
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

        DefaultJavascriptObject that = (DefaultJavascriptObject) obj;

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
        int hash = 345;
        hash += (id == null) ? 768 : id.hashCode();
        hash += (session == null) ? 546 : session.hashCode();
        return hash;
    }

    /**
     * The browser window that owns this function
     */
    private ScriptSession session;

    /**
     * The id into the function cache
     */
    private String id;
}
