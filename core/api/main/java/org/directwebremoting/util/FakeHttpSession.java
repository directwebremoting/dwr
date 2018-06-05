package org.directwebremoting.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * For the benefit of anyone that wants to create a fake HttpSession
 * that doesn't do anything other than not be null.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FakeHttpSession implements HttpSession
{
    /**
     * Setup the creation time
     */
    public FakeHttpSession()
    {
        creationTime = System.currentTimeMillis();
    }

    /**
     * Setup the creation time
     * @param id The new session id
     */
    public FakeHttpSession(String id)
    {
        this.id = id;
        creationTime = System.currentTimeMillis();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getCreationTime()
     */
    public long getCreationTime()
    {
        return creationTime;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getId()
     */
    public String getId()
    {
        if (id == null)
        {
            log.warn("Inventing data in FakeHttpSession.getId() to remain plausible.");
            id = "fake";
        }

        return id;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getLastAccessedTime()
     */
    public long getLastAccessedTime()
    {
        return creationTime;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getServletContext()
     */
    public ServletContext getServletContext()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
     */
    public void setMaxInactiveInterval(int maxInactiveInterval)
    {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
     */
    public int getMaxInactiveInterval()
    {
        return maxInactiveInterval;
    }

    /**
     * @see javax.servlet.http.HttpSession#getSessionContext()
     * @deprecated
     */
    @SuppressWarnings({"UnnecessaryFullyQualifiedName"})
    @Deprecated
    public javax.servlet.http.HttpSessionContext getSessionContext()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
     */
    @Deprecated
    public Object getValue(String name)
    {
        return attributes.get(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getAttributeNames()
     */
    public Enumeration<String> getAttributeNames()
    {
        return Collections.enumeration(attributes.keySet());
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getValueNames()
     */
    @Deprecated
    public String[] getValueNames()
    {
        return attributes.keySet().toArray(new String[attributes.keySet().size()]);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String name, Object value)
    {
        attributes.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
     */
    @Deprecated
    public void putValue(String name, Object value)
    {
        attributes.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name)
    {
        attributes.remove(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
     */
    @Deprecated
    public void removeValue(String name)
    {
        attributes.remove(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#invalidate()
     */
    public void invalidate()
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#isNew()
     */
    public boolean isNew()
    {
        return true;
    }

    /**
     * The session id
     */
    private String id = null;

    /**
     * The list of attributes
     */
    private Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * When were we created
     */
    private long creationTime;

    /**
     * How long before we timeout?
     */
    private int maxInactiveInterval = 30 * 60 * 1000;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(FakeHttpSession.class);
}
