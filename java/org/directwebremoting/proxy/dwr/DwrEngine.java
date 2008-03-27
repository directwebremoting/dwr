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
 * DwrEngine is a server-side proxy that allows Java programmers to call client
 * side Javascript from Java.
 * @see DwrUtil for more documenation on server-side proxies
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrEngine extends ScriptProxy
{
    /**
     * Http thread constructor, that affects no browsers.
     * Calls to {@link DwrEngine#addScriptSession(ScriptSession)} or to
     * {@link DwrEngine#addScriptSessions(Collection)} will be needed  
     */
    public DwrEngine()
    {
        super();
    }

    /**
     * Http thread constructor that alters a single browser
     * @param scriptSession The browser to alter
     */
    public DwrEngine(ScriptSession scriptSession)
    {
        super(scriptSession);
    }

    /**
     * Http thread constructor that alters a number of browsers
     * @param scriptSessions A collection of ScriptSessions that we should act on.
     */
    public DwrEngine(Collection scriptSessions)
    {
        super(scriptSessions);
    }

    /**
     * XHR remoting type constant. See DWREngine.set[Rpc|Poll]Type()
     */
    public static final int XMLHttpRequest = 1;

    /**
     * XHR remoting type constant. See DWREngine.set[Rpc|Poll]Type()
     */
    public static final int IFrame = 2;

    /**
     * XHR remoting type constant. See DWREngine.setRpcType()
     */
    public static final int ScriptTag = 3;

    /**
     * Set a default timeout value for all calls. 0 (the default) turns timeouts off.
     * @param timeout The time to wait in milliseconds
     * @see <a href="http://getahead.ltd.uk/dwr/browser/engine/errors">Error handling documentation</a>
     */
    public void setTimeout(int timeout)
    {
        ScriptBuffer script = createScriptBuffer();
        script.appendScript("DWREngine.setTimeout(")
              .appendData(timeout)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Set the preferred remoting type.
     * @param newType One of DWREngine.XMLHttpRequest or DWREngine.IFrame or DWREngine.ScriptTag
     * @see <a href="http://getahead.ltd.uk/dwr/browser/engine/options">Options documentation</a>
     */
    public void setRpcType(int newType)
    {
        ScriptBuffer script = createScriptBuffer();
        script.appendScript("DWREngine.setRpcType(")
              .appendData(newType)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Which HTTP method do we use to send results? Must be one of "GET" or "POST".
     * @param httpMethod One of {@link #XMLHttpRequest}, {@link #IFrame} or {@link #ScriptTag}
     * @see <a href="http://getahead.ltd.uk/dwr/browser/engine/options">Options documentation</a>
     */
    public void setHttpMethod(String httpMethod)
    {
        ScriptBuffer script = createScriptBuffer();
        script.appendScript("DWREngine.setHttpMethod(")
              .appendData(httpMethod)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Ensure that remote calls happen in the order in which they were sent? (Default: false)
     * @param ordered True to set call ordering.
     * @see <a href="http://getahead.ltd.uk/dwr/browser/engine/ordering">Ordering documentation</a>
     */
    public void setOrdered(boolean ordered)
    {
        ScriptBuffer script = createScriptBuffer();
        script.appendScript("DWREngine.setOrdered(")
              .appendData(ordered)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Do we ask the XHR object to be asynchronous? (Default: true)
     * @param async False to become synchronous (not recommended)
     * @see <a href="http://getahead.ltd.uk/dwr/browser/engine/options">Options documentation</a>
     */
    public void setAsync(boolean async)
    {
        ScriptBuffer script = createScriptBuffer();
        script.appendScript("DWREngine.setAsync(")
              .appendData(async)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Does DWR poll the server for updates? (Default: false)
     * @param reverseAjax True/False to turn RA on/off
     * @see <a href="http://getahead.ltd.uk/dwr/browser/engine/options">Options documentation</a>
     */
    public void setReverseAjax(boolean reverseAjax)
    {
        ScriptBuffer script = createScriptBuffer();
        script.appendScript("DWREngine.setReverseAjax(")
              .appendData(reverseAjax)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Does DWR us comet polling? (Default: true)
     * @param pollComet True/False to use Comet where supported
     * @see <a href="http://getahead.ltd.uk/dwr/browser/engine/options">Options documentation</a>
     */
    public void setPollUsingComet(boolean pollComet)
    {
        ScriptBuffer script = createScriptBuffer();
        script.appendScript("DWREngine.setPollUsingComet(")
              .appendData(pollComet)
              .appendScript(");");
        addScript(script);
    }

    /**
     * Set the preferred polling type.
     * @param newPollType One of {@link #XMLHttpRequest}, {@link #IFrame} or {@link #ScriptTag}
     * @see <a href="http://getahead.ltd.uk/dwr/browser/engine/options">Options documentation</a>
     */
    public void setPollType(int newPollType)
    {
        ScriptBuffer script = createScriptBuffer();
        script.appendScript("DWREngine.setPollUsingComet(")
              .appendData(newPollType)
              .appendScript(");");
        addScript(script);
    }
}
