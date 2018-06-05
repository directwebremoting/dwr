package org.directwebremoting.guice;

import java.util.Map;

import org.directwebremoting.extend.Creator;

import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Specialized creator implementation that uses an injector to
 * look up its instances. This class is used by {@link InternalCreatorManager}.
 * @author Tim Peierls [tim at peierls dot net]
 */
class InternalCreator implements Creator
{
    InternalCreator(Injector injector, Key<?> key, String scriptName)
    {
        this.injector = injector;
        this.key = key;
        this.scriptName = scriptName;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Creator#setProperties(java.util.Map)
     */
    public void setProperties(Map<String, String> params) throws IllegalArgumentException
    {
        // Do nothing, we ignore properties (since we inject everything we create).
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Creator#getType()
     */
    public Class<?> getType()
    {
        return (Class<?>) key.getTypeLiteral().getType();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        return injector.getInstance(key);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Creator#getScope()
     */
    public String getScope()
    {
        return Creator.PAGE; // i.e., tell DWR always to create
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Creator#isCacheable()
     */
    public boolean isCacheable()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Creator#isHidden()
     */
    public boolean isHidden()
    {
        // TODO: enable hidden created classes through Guice
        return false;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Creator#getJavascript()
     */
    public String getJavascript()
    {
        return scriptName;
    }

    private final Injector injector;

    private final Key<?> key;

    private final String scriptName;
}
