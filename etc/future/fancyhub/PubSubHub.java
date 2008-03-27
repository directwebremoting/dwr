package org.directwebremoting.proxy.openajax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.swing.event.EventListenerList;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.event.PublishEvent;
import org.directwebremoting.event.PublishListener;
import org.directwebremoting.event.SubscriptionEvent;
import org.directwebremoting.event.SubscriptionListener;

/**
 * A Server-side hub to manage subscriptions
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PubSubHub
{
    /**
     * Publishes (broadcasts) an event based on a library-specific prefix and
     * event name.
     * @param prefix The prefix that corresponds to this event. This must be a
     * prefix that has been registered via OpenAjax.registerLibrary().
     * @param name The name of the event to listen for. Names can be any string
     */
    public void publish(String prefix, String name)
    {
        publish(ANY_HTTP_SESSION, ANY_SCRIPT_SESSION, prefix, name, null, null);
    }

    /**
     * Publishes (broadcasts) an event based on a library-specific prefix and
     * event name.
     * @param prefix The prefix that corresponds to this event. This must be a
     * prefix that has been registered via OpenAjax.registerLibrary().
     * @param name The name of the event to listen for. Names can be any string
     * @param publisherData Data to be sent to the Listener along with the
     * eventHappened message
     */
    public void publish(String prefix, String name, Object publisherData)
    {
        publish(ANY_HTTP_SESSION, ANY_SCRIPT_SESSION, prefix, name, publisherData, null);
    }

    /**
     * Publishes (broadcasts) an event based on a library-specific prefix and
     * event name.
     * @param httpSessionId An HttpSession that we are publishing to. The value
     * should not be <code>null</code>, but can be {@link #ANY_HTTP_SESSION} to
     * denote a match for all {@link HttpSession}s.
     * @param scriptSessionId A ScriptSession that we are publishing to. The value
     * should not be <code>null</code>, but can be {@link #ANY_SCRIPT_SESSION}
     * to denote a match for all {@link ScriptSession}s.
     * @param prefix The prefix that corresponds to this event. This must be a
     * prefix that has been registered via OpenAjax.registerLibrary().
     * @param name The name of the event to listen for. Names can be any string
     */
    public void publish(String httpSessionId, String scriptSessionId, String prefix, String name)
    {
        publish(httpSessionId, scriptSessionId, prefix, name, null, null);
    }

    /**
     * Publishes (broadcasts) an event based on a library-specific prefix and
     * event name.
     * @param httpSessionId An HttpSession that we are publishing to. The value
     * should not be <code>null</code>, but can be {@link #ANY_HTTP_SESSION} to
     * denote a match for all {@link HttpSession}s.
     * @param scriptSessionId A ScriptSession that we are publishing to. The value
     * should not be <code>null</code>, but can be {@link #ANY_SCRIPT_SESSION}
     * to denote a match for all {@link ScriptSession}s.
     * @param prefix The prefix that corresponds to this event. This must be a
     * prefix that has been registered via OpenAjax.registerLibrary().
     * @param name The name of the event to listen for. Names can be any string
     * @param publisherData Data to be sent to the Listener along with the
     * eventHappened message
     */
    public void publish(String httpSessionId, String scriptSessionId, String prefix, String name, Object publisherData)
    {
        publish(httpSessionId, scriptSessionId, prefix, name, publisherData, null);
    }

    /**
     * Publishes (broadcasts) an event based on a library-specific prefix and
     * event name.
     * @param httpSessionId An HttpSession that we are publishing to. The value
     * should not be <code>null</code>, but can be {@link #ANY_HTTP_SESSION} to
     * denote a match for all {@link HttpSession}s.
     * @param scriptSessionId A ScriptSession that we are publishing to. The value
     * should not be <code>null</code>, but can be {@link #ANY_SCRIPT_SESSION}
     * to denote a match for all {@link ScriptSession}s.
     * @param prefix The prefix that corresponds to this event. This must be a
     * prefix that has been registered via OpenAjax.registerLibrary().
     * @param name The name of the event to listen for. Names can be any string
     * @param publisherData Data to be sent to the Listener along with the
     * eventHappened message
     * @param hubsVisited A list of the hubs that the message has passed through
     */
    public void publish(String httpSessionId, String scriptSessionId, String prefix, String name, Object publisherData, List<String> hubsVisited)
    {
        checkNulls(httpSessionId, scriptSessionId, prefix, name);

        if (hubsVisited == null)
        {
            hubsVisited = new ArrayList<String>();
        }
        else
        {
            if (hubsVisited.contains(getHubId()))
            {
                return;
            }
        }
        hubsVisited.add(getHubId());

        List<PublishListener> matches = new ArrayList<PublishListener>();
        matches.addAll(allListeners.keySet());

        if (matches.size() != 0 && !httpSessionId.equals(ANY_HTTP_SESSION))
        {
            List<PublishListener> httpSessionMatches = httpSessions.get(httpSessionId);
            if (httpSessionMatches != null)
            {
                matches.retainAll(httpSessionMatches);
            }
            else
            {
                matches.clear();
            }
        }

        if (matches.size() != 0 && !scriptSessionId.equals(ANY_SCRIPT_SESSION))
        {
            List<PublishListener> scriptSessionMatches = scriptSessions.get(scriptSessionId);
            if (scriptSessionMatches != null)
            {
                matches.retainAll(scriptSessionMatches);
            }
            else
            {
                matches.clear();
            }
        }

        if (matches.size() != 0 && !prefix.equals(ANY_PREFIX))
        {
            List<PublishListener> prefixMatches = prefixes.get(prefix);
            if (prefixMatches != null)
            {
                matches.retainAll(prefixMatches);
            }
            else
            {
                matches.clear();
            }
        }

        if (matches.size() != 0 && !name.equals(ANY_NAME))
        {
            List<PublishListener> nameMatches = names.get(name);
            if (nameMatches != null)
            {
                matches.retainAll(nameMatches);
            }
            else
            {
                matches.clear();
            }
        }

        for (PublishListener listener : matches)
        {
            Object subscriberData = allListeners.get(listener);
            PublishEvent event = new PublishEvent(this, httpSessionId, scriptSessionId, prefix, name, publisherData, subscriberData, hubsVisited);
            listener.publishHappened(event);
        }
    }

    /**
     * @see #subscribe(String, String, String, String, PublishListener, Object)
     */
    public void subscribe(String prefix, String name, PublishListener listener)
    {
        subscribe(ANY_HTTP_SESSION, ANY_SCRIPT_SESSION, prefix, name, listener, null);
    }

    /**
     * @see #subscribe(String, String, String, String, PublishListener, Object)
     */
    public void subscribe(String prefix, String name, PublishListener listener, Object subscriberData)
    {
        subscribe(ANY_HTTP_SESSION, ANY_SCRIPT_SESSION, prefix, name, listener, subscriberData);
    }

    /**
     * @see #subscribe(String, String, String, String, PublishListener, Object)
     */
    public void subscribe(String httpSessionId, String scriptSessionId, String prefix, String name, PublishListener listener)
    {
        subscribe(httpSessionId, scriptSessionId, prefix, name, listener, null);
    }

    /**
     * Allows registration of interest in named events based on library-specific
     * prefix and event name. Global event matching is provided by passing "*"
     * in the prefix and/or name arguments. Optional arguments may be specified
     * for executing the specified handler function in a provided scope and for
     * further filtering events prior to application.
     * <p>
     * The callback function will receive the following parameters
     * (see OpenAjax.publish() for description of publisherData):
     * <pre>
     * function(prefix, name, subscriberData, publisherData){ ... }
     * </pre>
     * @param httpSessionId An HttpSession that we are subscribing to. The value
     * should not be <code>null</code>, but can be {@link #ANY_HTTP_SESSION} to
     * denote a match for all {@link HttpSession}s.
     * @param scriptSessionId A ScriptSession that we are subscribing to. The value
     * should not be <code>null</code>, but can be {@link #ANY_SCRIPT_SESSION}
     * to denote a match for all {@link ScriptSession}s.
     * @param prefix The prefix that corresponds to this library. This is the
     * same value that was previously passed to registerLibrary(). Can be "*" to
     * match the provided event name across all libraries.
     * @param name The name of the event to listen for. Names can be any string.
     * Can be "*" to match all events in the specified toolkit (see prefix). If
     * both name and prefix specify "*", all events in the system will be routed
     * to the registered handler (modulo any filtering provided by filter).
     * @param listener The object to deliver messages to
     * @param subscriberData Data to be send to the Listener along with the
     * eventHappened message
     */
    public void subscribe(String httpSessionId, String scriptSessionId, String prefix, String name, PublishListener listener, Object subscriberData)
    {
        checkNulls(httpSessionId, scriptSessionId, prefix, name);

        if (httpSessionId != null && httpSessionId != ANY_HTTP_SESSION)
        {
            List<PublishListener> listeners = httpSessions.get(httpSessionId);
            if (listeners == null)
            {
                listeners = new ArrayList<PublishListener>();
                httpSessions.put(httpSessionId, listeners);
            }
            listeners.add(listener);
        }

        if (scriptSessionId != null && scriptSessionId != ANY_SCRIPT_SESSION)
        {
            List<PublishListener> listeners = scriptSessions.get(scriptSessionId);
            if (listeners == null)
            {
                listeners = new ArrayList<PublishListener>();
                scriptSessions.put(scriptSessionId, listeners);
            }
            listeners.add(listener);
        }

        if (prefix != null && prefix != ANY_PREFIX)
        {
            List<PublishListener> listeners = prefixes.get(prefix);
            if (listeners == null)
            {
                listeners = new ArrayList<PublishListener>();
                prefixes.put(prefix, listeners);
            }
            listeners.add(listener);
        }

        if (name != null && name != ANY_NAME)
        {
            List<PublishListener> listeners = names.get(name);
            if (listeners == null)
            {
                listeners = new ArrayList<PublishListener>();
                names.put(name, listeners);
            }
            listeners.add(listener);
        }

        allListeners.put(listener, subscriberData);
        fireSubscribeHappenedEvent(httpSessionId, scriptSessionId, prefix, name, listener);
    }

    /**
     * @see #unsubscribe(String, String, String, String, PublishListener)
     */
    public void unsubscribe(String prefix, String name, PublishListener listener)
    {
        unsubscribe(ANY_HTTP_SESSION, ANY_SCRIPT_SESSION, prefix, name, listener);
    }

    /**
     * Removes a subscription to an event. In order for a subscription to be
     * removed, the values of the parameters supplied to OpenAjax.unsubscribe()
     * must exactly match the values of the parameters supplied to a previous
     * call to OpenAjax.subscribe(). Note that it is possible that one
     * invocation of OpenAjax.unsubscribe() might result in removal of multiple
     * subscriptions.
     * @param prefix The prefix that corresponds to this library. This is the
     * same value that was previously passed to registerLibrary(). Can be "*" to
     * match the provided event name across all libraries.
     * @param name The name of the event to listen for. Names can be any string.
     * Can be "*" to match all events in the specified toolkit (see prefix). If
     * both name and prefix specify "*", all events in the system will be routed
     * to the registered handler (modulo any filtering provided by filter).
     * @param listener The object to deliver messages to
     */
    public void unsubscribe(String httpSessionId, String scriptSessionId, String prefix, String name, PublishListener listener)
    {
        checkNulls(httpSessionId, scriptSessionId, prefix, name);

        if (httpSessionId != null && httpSessionId != ANY_HTTP_SESSION)
        {
            List<PublishListener> listeners = httpSessions.get(httpSessionId);
            if (listeners != null)
            {
                listeners.remove(listener);
                if (listeners.size() == 0)
                {
                    httpSessions.remove(httpSessionId);
                }
            }
        }

        if (scriptSessionId != null && scriptSessionId != ANY_SCRIPT_SESSION)
        {
            List<PublishListener> listeners = scriptSessions.get(scriptSessionId);
            if (listeners != null)
            {
                listeners.remove(listener);
                if (listeners.size() == 0)
                {
                    scriptSessions.remove(scriptSessionId);
                }
            }
        }

        if (prefix != null && prefix != ANY_PREFIX)
        {
            List<PublishListener> listeners = prefixes.get(prefix);
            if (listeners != null)
            {
                listeners.remove(listener);
                if (listeners.size() == 0)
                {
                    prefixes.remove(prefix);
                }
            }
        }

        if (name != null && name != ANY_NAME)
        {
            List<PublishListener> listeners = names.get(name);
            if (listeners != null)
            {
                listeners.remove(listener);
                if (listeners.size() == 0)
                {
                    names.remove(name);
                }
            }
        }

        allListeners.remove(listener);
        fireUnsubscribeHappenedEvent(httpSessionId, scriptSessionId, prefix, name, listener);
    }

    /**
     * Maintain the list of {@link SubscriptionListener}s
     * @param li the SubscriptionListener to add
     */
    public void addSubscriptionListener(SubscriptionListener li)
    {
        subscriptionListeners.add(SubscriptionListener.class, li);
    }

    /**
     * Maintain the list of {@link SubscriptionListener}s
     * @param li the ScriptSessionListener to remove
     */
    public void removeSubscriptionListener(SubscriptionListener li)
    {
        subscriptionListeners.remove(SubscriptionListener.class, li);
    }

    /**
     * If other hubs wish to synchronize with the messages passed through this
     * hub they need to be able to filter to keep the message storm down.
     * @return A set of the names that this hub is interested in.
     */
    public Set<String> getSubscribedNames()
    {
        return Collections.unmodifiableSet(names.keySet());
    }

    /**
     * If other hubs wish to synchronize with the messages passed through this
     * hub they need to be able to filter to keep the message storm down.
     * @return A set of the prefixes that this hub is interested in.
     */
    public Set<String> getSubscribedPrefixes()
    {
        return Collections.unmodifiableSet(prefixes.keySet());
    }

    /**
     * To allow hubs to not create publish loops we need to know what 
     * @return The ID of this Hub
     */
    public String getHubId()
    {
        if (hubId == null)
        {
            hubId = "org.directwebremoting.proxy.openajax.PubSubHub." + hashCode();
        }

        return hubId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getHubId();
    }

    /**
     * This should be called whenever a {@link ScriptSession} is destroyed
     * @param httpSessionId The ID match of the {@link HttpSession} that was subscribed to
     * @param scriptSessionId The ID match of the {@link ScriptSession} that was subscribed to
     * @param prefix The prefix match that was subscribed to
     * @param name The name match that was subscripbed to
     * @param listener The subscribed object
     */
    protected void fireSubscribeHappenedEvent(String httpSessionId, String scriptSessionId, String prefix, String name, PublishListener listener)
    {
        SubscriptionEvent ev = new SubscriptionEvent(this, httpSessionId, scriptSessionId, prefix, name, listener);
        Object[] listeners = subscriptionListeners.getListenerList();
        for (int i = 0; i < listeners.length - 2; i += 2)
        {
            if (listeners[i] == SubscriptionListener.class)
            {
                ((SubscriptionListener) listeners[i + 1]).subscribeHappened(ev);
            }
        }
    }

    /**
     * This should be called whenever a {@link ScriptSession} is created
     * @param httpSessionId The ID match of the {@link HttpSession} that was subscribed to
     * @param scriptSessionId The ID match of the {@link ScriptSession} that was subscribed to
     * @param prefix The prefix match that was subscribed to
     * @param name The name match that was subscripbed to
     * @param listener The subscribed object
     */
    protected void fireUnsubscribeHappenedEvent(String httpSessionId, String scriptSessionId, String prefix, String name, PublishListener listener)
    {
        SubscriptionEvent ev = new SubscriptionEvent(this, httpSessionId, scriptSessionId, prefix, name, listener);
        Object[] listeners = subscriptionListeners.getListenerList();
        for (int i = 0; i < listeners.length - 2; i += 2)
        {
            if (listeners[i] == SubscriptionListener.class)
            {
                ((SubscriptionListener) listeners[i + 1]).unsubscribeHappened(ev);
            }
        }
    }

    /**
     * The list of current {@link SubscriptionListener}s
     */
    protected EventListenerList subscriptionListeners = new EventListenerList();

    /**
     * A constant to denote a match to any <code>prefix</code>.
     */
    public static final String ANY_PREFIX = "*";

    /**
     * A constant to denote a match to any <code>name</code>.
     */
    public static final String ANY_NAME = "*";

    /**
     * A constant to denote a match to any {@link HttpSession}.
     */
    public static final String ANY_HTTP_SESSION = "*";

    /**
     * A constant to denote a match to any {@link ScriptSession}.
     */
    public static final String ANY_SCRIPT_SESSION = "*";

    /**
     * Throw if any of the parameters are null
     */
    private void checkNulls(String httpSessionId, String scriptSessionId, String prefix, String name)
    {
        if (httpSessionId == null)
        {
            throw new NullPointerException("httpSessionId may not be null. Use PubSubHub.ANY_HTTP_SESSION for broad matching");
        }

        if (scriptSessionId == null)
        {
            throw new NullPointerException("scriptSession may not be null. Use PubSubHub.ANY_SCRIPT_SESSION for broad matching");
        }

        if (prefix == null)
        {
            throw new NullPointerException("prefix may not be null. Use PubSubHub.ANY_PREFIX or \"*\" for broad matching");
        }

        if (name == null)
        {
            throw new NullPointerException("name may not be null. Use PubSubHub.ANY_NAME or \"*\" for broad matching");
        }
    }

    private String hubId = null;

    private Map<PublishListener, Object> allListeners = new HashMap<PublishListener, Object>();

    private Map<String, List<PublishListener>> names = new HashMap<String, List<PublishListener>>();

    private Map<String, List<PublishListener>> prefixes = new HashMap<String, List<PublishListener>>();

    private Map<String, List<PublishListener>> httpSessions = new HashMap<String, List<PublishListener>>();

    private Map<String, List<PublishListener>> scriptSessions = new HashMap<String, List<PublishListener>>();
}
