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
package org.directwebremoting.bayeux;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.dwrp.CallBatch;
import org.directwebremoting.dwrp.PlainCallHandler;
import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.extend.Replies;
import org.directwebremoting.extend.Reply;
import org.directwebremoting.extend.ScriptConduit;

import dojox.cometd.Bayeux;
import dojox.cometd.Channel;
import dojox.cometd.Client;
import dojox.cometd.Listener;
import dojox.cometd.Message;

/**
 * @author Greg Wilkins [gregw at webtide dot com]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BayeuxClient implements Listener
{
    public BayeuxClient(Bayeux bayeux)
    {
        this.bayeux = bayeux;

        // At this point BayeuxClient is fully initialized so it is safe to
        // allow other classes to see and use us.
        Channel channel = bayeux.getChannel("/dwr", true);
        channel.subscribe(client);
    }

    /* (non-Javadoc)
     * @see dojox.cometd.MessageListener#deliver(dojox.cometd.Client, dojox.cometd.Client, dojox.cometd.Message)
     */
    public void deliver(Client fromClient, Client toClient, Message message)
    {
        try
        {
            Map<String, FormField> fileParams = new HashMap<String, FormField>(message.size());
            for (Map.Entry<String, Object> entry : message.entrySet())
            {
                String param = (String) entry.getValue();
                FormField formField = new FormField(param);
                fileParams.put(entry.getKey(), formField);
            }

            CallBatch batch = new CallBatch(fileParams, false);
            Calls calls = plainCallHandler.marshallInbound(batch);

            log.debug("Calls="+calls);

            for (int i = 0; i < calls.getCallCount(); i++)
            {
                Call call = calls.getCall(i);
                Object[] params = call.getParameters();
                log.debug("Call[" + i + "]=" + call.getScriptName() + "." + call.getMethodName() + (params == null ? "[]" : Arrays.asList(params)));
            }

            Replies replies = remoter.execute(calls);

            ScriptConduit conduit = new BayeuxScriptConduit(converterManager, JSON_OUTPUT);
            for (Reply reply : replies)
            {
                String batchId = calls.getBatchId();
                log.debug("Reply="+reply+" BatchId="+batchId);

                if (reply.getThrowable() != null)
                {
                    Throwable ex = reply.getThrowable();
                    ScriptBuffer script = EnginePrivate.getRemoteHandleExceptionScript(batchId, reply.getCallId(), ex);
                    conduit.addScript(script);

                    log.warn("--Erroring: batchId[" + batchId + "] message[" + ex.toString() + ']');
                }
                else
                {
                    Object data = reply.getReply();
                    log.debug("data="+data);
                    ScriptBuffer script = EnginePrivate.getRemoteHandleCallbackScript(batchId, reply.getCallId(), data);
                    conduit.addScript(script);
                }
            }

            String output = conduit.toString();
            log.debug("<< "+output);
            Channel channel = bayeux.getChannel("/dwr" + fromClient.getId(), true);
            channel.publish(client, output, calls.getBatchId());
        }
        catch (Exception ex)
        {
            log.warn("Protocol Error", ex);
        }
    }

    /* (non-Javadoc)
     * @see dojox.cometd.Listener#removed(java.lang.String, boolean)
     */
    public void removed(String clientId, boolean timeout)
    {
    }

    /**
     * We're note constraining our output to JSON at the moment
     */
    private static final boolean JSON_OUTPUT = false;

    private final Bayeux bayeux;

    private Client client;

    /**
     * @param remoter
     */
    public void setRemoter(Remoter remoter)
    {
        this.remoter = remoter;
    }

    private Remoter remoter;

    /**
     * @param converterManager
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    private ConverterManager converterManager;

    /**
     * @param plainCallHandler
     */
    public void setPlainCallHandler(PlainCallHandler plainCallHandler)
    {
        this.plainCallHandler = plainCallHandler;
    }

    private PlainCallHandler plainCallHandler;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BayeuxClient.class);
}
