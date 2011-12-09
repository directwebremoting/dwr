/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.create;

import java.util.Map;

import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;

/**
 * A simple implementation of the basic parts of Creator
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractCreator implements Creator
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#setProperties(java.util.Map)
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
     * @see org.directwebremoting.Creator#getScope()
     */
    public String getScope()
    {
        return scope;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#isCacheable()
     */
    public boolean isCacheable()
    {
        return cacheable;
    }

    /**
     * @param cacheable Whether or not to cache the script.
     */
    public void setCacheable(boolean cacheable)
    {
        this.cacheable = cacheable;
    }

    /**
     * Is the given scope valid?
     * @param cscope The scope string to match
     */
    protected static void checkScope(String cscope)
    {
        if (!cscope.equals(SCRIPT) && !cscope.equals(PAGE) && !cscope.equals(REQUEST) && !cscope.equals(SESSION) && !cscope.equals(APPLICATION))
        {
            throw new IllegalArgumentException(Messages.getString("AbstractCreator.IllegalScope", cscope));
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return LocalUtil.getShortClassName(getClass()) + "[" + getJavascript() + "]";
    }

    /**
     * Do the methods on the Creator change over time?
     */
    private boolean cacheable = false;

    /**
     * The javascript name for the class
     */
    private String javascript = null;

    /**
     * The scope of the objects created by this creator
     */
    private String scope = PAGE;
}
