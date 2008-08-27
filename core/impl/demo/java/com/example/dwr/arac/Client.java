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
package com.example.dwr.arac;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 *
 */
public class Client
{
    /**
     * @param name
     * @param session
     */
    public Client(String name, ScriptSession session)
    {
        this.name = name;
        this.session = session;

        WebContext wctx = WebContextFactory.get();
        HttpServletRequest request = wctx.getHttpServletRequest();
        type = request.getHeader("User-Agent");

        page = wctx.getCurrentPage();

        created = new Date();
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the session
     */
    public ScriptSession getSession()
    {
        return session;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @return the page
     */
    public String getPage()
    {
        return page;
    }

    /**
     * @return the created
     */
    public Date getCreated()
    {
        return created;
    }

    /**
     * @return the lastPinged
     */
    public Date getLastPinged()
    {
        return lastPinged;
    }

    /**
     * @param lastPinged the lastPinged to set
     */
    public void setLastPinged(Date lastPinged)
    {
        this.lastPinged = lastPinged;
    }

    /**
     * @return the lastMessaged
     */
    public Date getLastMessaged()
    {
        return lastMessaged;
    }

    /**
     * @param lastMessaged the lastMessaged to set
     */
    public void setLastMessaged(Date lastMessaged)
    {
        this.lastMessaged = lastMessaged;
    }

    private final int id = nextClientId();
    private String name;
    private final ScriptSession session;
    private final String type;
    private final String page;
    private final Date created;
    private Date lastPinged;
    private Date lastMessaged;

    private static synchronized int nextClientId()
    {
        synchronized (Client.class)
        {
            return nextClientId++;
        }
    }

    private static int nextClientId = 0;
}
