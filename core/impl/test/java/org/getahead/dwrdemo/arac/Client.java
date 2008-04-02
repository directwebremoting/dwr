package org.getahead.dwrdemo.arac;

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

    private int id = nextClientId();
    private String name;
    private ScriptSession session;
    private String type;
    private String page;
    private Date created;
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
