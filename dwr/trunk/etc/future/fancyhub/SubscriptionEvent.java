package org.directwebremoting.event;

import java.util.EventObject;

import org.directwebremoting.proxy.openajax.PubSubHub;

/**
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SubscriptionEvent extends EventObject
{
    /**
     * 
     */
    public SubscriptionEvent(PubSubHub pubSubHub, String httpSessionId, String scriptSessionId, String prefix, String name, PublishListener publishListener)
    {
        super(pubSubHub);
        this.pubSubHub = pubSubHub;

        this.httpSessionId = httpSessionId;
        this.scriptSessionId = scriptSessionId;
        this.prefix = prefix;
        this.name = name;

        this.publishListener = publishListener;
    }

    /**
     * 
     */
    public PubSubHub getPubSubHub()
    {
        return pubSubHub;
    }

    /**
     * 
     */
    public String getHttpSessionId()
    {
        return httpSessionId;
    }

    /**
     * 
     */
    public String getScriptSessionId()
    {
        return scriptSessionId;
    }

    /**
     * 
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * 
     */
    public String getName()
    {
        return name;
    }

    /**
     * 
     */
    public Object getPublishListener()
    {
        return publishListener;
    }

    /**
     * 
     */
    public Object getSubscriberData()
    {
        return publishListener;
    }

    private PubSubHub pubSubHub;

    private String httpSessionId;

    private String scriptSessionId;

    private String prefix;

    private String name;

    private PublishListener publishListener;

    /**
     * 
     */
    private static final long serialVersionUID = 7200050970371319986L;
}
