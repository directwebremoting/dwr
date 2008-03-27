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
package org.directwebremoting.export;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Hub;
import org.directwebremoting.HubFactory;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.MessageEvent;
import org.directwebremoting.event.MessageListener;
import org.directwebremoting.extend.CallbackHelper;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.io.RawData;

/**
 * Various functions exported by DWR to help us with various book-keeping
 * duties.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class System
{
    /**
     * A method designed to be called on page load so the client knows about
     * the http and script sessions. This method doesn't actually do anything
     * however it does allow DWR's script session checking code to work
     */
    public void pageLoaded()
    {
    }

    /**
     * Call {@link ScriptSession#invalidate()} on the {@link ScriptSession}
     * that called this method.
     * Used by the page unloader.
     */
    public void pageUnloaded()
    {
        WebContext wctx = WebContextFactory.get();
        ScriptSession scriptSession = wctx.getScriptSession();

        log.debug("scriptSession.invalidate(): " + scriptSession.getId());
        scriptSession.invalidate();
    }

    /**
     * Used by reverse ajax proxies to send data back to the server
     * @param key The unique id under which a callback is registered
     * @param data The data to decode and pass to the callback
     */
    public void activateCallback(String key, RawData data)
    {
        try
        {
            CallbackHelper.executeCallback(key, data);
        }
        catch (Exception ex)
        {
            log.error("Failed to marshall data from callback", ex);
        }
    }

    /**
     * Something has published to the client side 'hub' and we're getting to
     * know about it.
     * @param topic The topic that has been published to
     * @param data The published data
     */
    public void publish(String topic, RawData data)
    {
        WebContext webContext = WebContextFactory.get();
        ConverterManager converterManager = webContext.getContainer().getBean(ConverterManager.class);
        ServletContext servletContext = webContext.getServletContext();
        Hub hub = HubFactory.get(servletContext);

        MessageEvent event = new MessageEvent(hub, converterManager, data);
        hub.publish(topic, event);
    }

    /**
     * Ensure that the clients know about server publishes
     * @param topic The topic being subscribed to
     * @param subscriptionId The ID to pass back to link to client side data
     */
    @SuppressWarnings("unchecked")
    public void subscribe(String topic, String subscriptionId)
    {
        WebContext webContext = WebContextFactory.get();
        ServletContext servletContext = webContext.getServletContext();
        Hub hub = HubFactory.get(servletContext);
        final ScriptSession session = webContext.getScriptSession();

        // Create a subscription block
        BrowserMessageListener subscription = new BrowserMessageListener(session, topic, subscriptionId);

        Map<String, BrowserMessageListener> subscriptions = (Map<String, BrowserMessageListener>) session.getAttribute(ATTRIBUTE_SUBSCRIPTIONS);
        if (subscriptions == null)
        {
            subscriptions = new HashMap<String, BrowserMessageListener>();
        }
        subscriptions.put(subscriptionId, subscription);
        session.setAttribute(ATTRIBUTE_SUBSCRIPTIONS, subscriptions);

        hub.subscribe(subscription.topic, subscription);
    }

    /**
     * Stop notifications of against a subscription id
     * @param subscriptionId The ID to pass back to link to client side data
     * @return true iff someone was unsubscribed
     */
    @SuppressWarnings("unchecked")
    public boolean unsubscribe(String subscriptionId)
    {
        WebContext webContext = WebContextFactory.get();
        ServletContext servletContext = webContext.getServletContext();
        Hub hub = HubFactory.get(servletContext);
        ScriptSession session = webContext.getScriptSession();

        Map<String, BrowserMessageListener> subscriptions = (Map<String, BrowserMessageListener>) session.getAttribute(ATTRIBUTE_SUBSCRIPTIONS);
        BrowserMessageListener subscription = subscriptions.get(subscriptionId);

        return hub.unsubscribe(subscription.topic, subscription);
    }

    /**
     * A struct to collect the data we need to remember against a subscription
     */
    protected class BrowserMessageListener implements MessageListener
    {
        /**
         * 
         */
        public BrowserMessageListener(ScriptSession session, String topic, String subscriptionId)
        {
            this.session = session;
            this.topic = topic;
            this.subscriptionId = subscriptionId;
        }

        /* (non-Javadoc)
         * @see org.directwebremoting.event.MessageListener#onMessage(org.directwebremoting.event.MessageEvent)
         */
        public void onMessage(MessageEvent message)
        {
            ScriptBuffer script = new ScriptBuffer();
            script.appendCall("dwr.hub._remotePublish", subscriptionId, message.getRawData());
            session.addScript(script);
        }

        protected String topic;
        protected String subscriptionId;
        protected ScriptSession session;
    }

    /**
     * 
     */
    private static final String ATTRIBUTE_SUBSCRIPTIONS = "org.directwebremoting.export.System.subscriptions";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(System.class);
}
