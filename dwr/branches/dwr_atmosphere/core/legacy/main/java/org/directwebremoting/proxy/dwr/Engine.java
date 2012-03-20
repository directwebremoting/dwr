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
package org.directwebremoting.proxy.dwr;

import java.util.Collection;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.proxy.ScriptProxy;

/**
 * Engine is a server-side proxy that allows Java programmers to call client
 * side Javascript from Java.
 * @see Util for more documenation on server-side proxies
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @deprecated Use org.directwebremoting.ui.dwr.Engine
 * @see org.directwebremoting.ui.dwr.Engine
 */
@Deprecated
public class Engine extends ScriptProxy
{
    /**
     * Http thread constructor, that affects no browsers.
     * Calls to {@link Engine#addScriptSession(ScriptSession)} or to
     * {@link Engine#addScriptSessions(Collection)} will be needed
     */
    public Engine()
    {
    }

    /**
     * Http thread constructor that alters a single browser
     * @param scriptSession The browser to alter
     */
    public Engine(ScriptSession scriptSession)
    {
        super(scriptSession);
    }

    /**
     * Http thread constructor that alters a number of browsers
     * @param scriptSessions A collection of ScriptSessions that we should act on.
     */
    public Engine(Collection<ScriptSession> scriptSessions)
    {
        super(scriptSessions);
    }

    /**
     * XHR remoting type constant. See dwr.engine.set[Rpc|Poll]Type()
     */
    public static final int XMLHttpRequest = 1;

    /**
     * XHR remoting type constant. See dwr.engine.set[Rpc|Poll]Type()
     */
    public static final int IFrame = 2;

    /**
     * XHR remoting type constant. See dwr.engine.setRpcType()
     */
    public static final int ScriptTag = 3;

    /**
     * Set a default timeout value for all calls. 0 (the default) turns timeouts off.
     * @param timeout The time to wait in milliseconds
     * @see <a href="http://getahead.org/dwr/browser/engine/errors">Error handling documentation</a>
     */
    public void setTimeout(int timeout)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setTimeout(")
              .appendData(timeout)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Set the preferred remoting type.
     * @param newType One of dwr.engine.XMLHttpRequest or dwr.engine.IFrame or dwr.engine.ScriptTag
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public void setRpcType(int newType)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setRpcType(")
              .appendData(newType)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Which HTTP method do we use to send results? Must be one of "GET" or "POST".
     * @param httpMethod One of {@link #XMLHttpRequest}, {@link #IFrame} or {@link #ScriptTag}
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public void setHttpMethod(String httpMethod)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setHttpMethod(")
              .appendData(httpMethod)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Ensure that remote calls happen in the order in which they were sent? (Default: false)
     * @param ordered True to set call ordering.
     * @see <a href="http://getahead.org/dwr/browser/engine/ordering">Ordering documentation</a>
     */
    public void setOrdered(boolean ordered)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setOrdered(")
              .appendData(ordered)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Do we ask the XHR object to be asynchronous? (Default: true)
     * @param async False to become synchronous (not recommended)
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public void setAsync(boolean async)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setAsync(")
              .appendData(async)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Does DWR poll the server for updates? (Default: false)
     * @param activeReverseAjax True/False to turn RA on/off
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public void setActiveReverseAjax(boolean activeReverseAjax)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setActiveReverseAjax(")
              .appendData(activeReverseAjax)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Does DWR us comet polling? (Default: true)
     * @param pollComet True/False to use Comet where supported
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public void setPollUsingComet(boolean pollComet)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setPollUsingComet(")
              .appendData(pollComet)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Set the preferred polling type.
     * @param newPollType One of {@link #XMLHttpRequest}, {@link #IFrame} or {@link #ScriptTag}
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public void setPollType(int newPollType)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setPollUsingComet(")
              .appendData(newPollType)
              .appendScript(");");
        addScript(script);
    }
}
