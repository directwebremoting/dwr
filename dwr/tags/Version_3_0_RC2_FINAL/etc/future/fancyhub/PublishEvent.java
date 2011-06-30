package org.directwebremoting.event;

import java.util.EventObject;
import java.util.List;

import org.directwebremoting.proxy.openajax.PubSubHub;

/**
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PublishEvent extends EventObject
{
    /**
     * 
     */
    public PublishEvent(PubSubHub pubSubHub, String httpSessionId, String scriptSessionId, String prefix, String name, Object publisherData, Object subscriberData, List<String> hubsVisited)
    {
        super(pubSubHub);
        this.pubSubHub = pubSubHub;

        this.httpSessionId = httpSessionId;
        this.scriptSessionId = scriptSessionId;
        this.prefix = prefix;
        this.name = name;

        this.publisherData = publisherData;
        this.subscriberData = subscriberData;

        this.hubsVisited = hubsVisited;
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
    public Object getPublisherData()
    {
        return publisherData;
    }

    /**
     * 
     */
    public Object getSubscriberData()
    {
        return subscriberData;
    }

    /**
     * 
     */
    public List<String> getHubsVisited()
    {
        return hubsVisited;
    }

    private PubSubHub pubSubHub;

    private String httpSessionId;

    private String scriptSessionId;

    private String prefix;

    private String name;

    private Object publisherData;

    private Object subscriberData;

    private List<String> hubsVisited;

    /**
     * 
     */
    private static final long serialVersionUID = 7200050970371319986L;
}
