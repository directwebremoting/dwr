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
package uk.ltd.getahead.dwr.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import uk.ltd.getahead.dwr.Browser;
import uk.ltd.getahead.dwr.ClientScript;

/**
 * The default implementation of the Browser interface
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultBrowser implements Browser, HttpSessionBindingListener
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Browser#addScript(uk.ltd.getahead.dwr.ClientScript)
     */
    public void addScript(ClientScript script)
    {
        synchronized (scripts)
        {
            scripts.add(script);
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Browser#removeScript(uk.ltd.getahead.dwr.ClientScript)
     */
    public boolean removeScript(ClientScript script)
    {
        synchronized (scripts)
        {
            return scripts.remove(script);
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Browser#removeAllScripts()
     */
    public List removeAllScripts()
    {
        List copy = new ArrayList();

        synchronized (scripts)
        {
            copy.addAll(scripts);
            scripts.clear();
        }

        return copy;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Browser#isValid()
     */
    public boolean isValid()
    {
        return bound;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent event)
    {
        bound = true;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent event)
    {
        bound = false;
    }

    /**
     * Are we bound to a session?
     */
    private boolean bound = false;

    /**
     * The list of waiting scripts
     */
    private List scripts = new ArrayList();
}
