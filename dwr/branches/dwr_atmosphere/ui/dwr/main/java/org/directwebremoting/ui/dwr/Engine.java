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
package org.directwebremoting.ui.dwr;

import java.io.InputStream;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.io.FileTransfer;

/**
 * Engine is a server-side proxy that allows Java programmers to call client
 * side Javascript from Java.
 * @see Util for more documenation on server-side proxies
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Engine
{
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
     * Send some data to the client and have the browser offer it for download
     * @param blob The data to be downloaded
     */
    public static void openInDownload(byte[] blob)
    {
        ScriptSessions.addFunctionCall("dwr.engine.openInDownload", blob);
    }

    /**
     * Send some data to the client and have the browser offer it for download
     * @param in The data to be downloaded
     */
    public static void openInDownload(InputStream in)
    {
        ScriptSessions.addFunctionCall("dwr.engine.openInDownload", in);
    }

    /**
     * Send some data to the client and have the browser offer it for download
     * @param ft The data to be downloaded
     */
    public static void openInDownload(FileTransfer ft)
    {
        ScriptSessions.addFunctionCall("dwr.engine.openInDownload", ft);
    }

    /**
     * Set a default timeout value for all calls. 0 (the default) turns timeouts off.
     * @param timeout The time to wait in milliseconds
     * @see <a href="http://getahead.org/dwr/browser/engine/errors">Error handling documentation</a>
     */
    public static void setTimeout(int timeout)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setTimeout(")
              .appendData(timeout)
              .appendScript(");");
        ScriptSessions.addScript(script);
    }

    /**
     * Set the preferred remoting type.
     * @param newType One of dwr.engine.XMLHttpRequest or dwr.engine.IFrame or dwr.engine.ScriptTag
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public static void setRpcType(int newType)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setRpcType(")
              .appendData(newType)
              .appendScript(");");
        ScriptSessions.addScript(script);
    }

    /**
     * Which HTTP method do we use to send results? Must be one of "GET" or "POST".
     * @param httpMethod One of {@link #XMLHttpRequest}, {@link #IFrame} or {@link #ScriptTag}
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public static void setHttpMethod(String httpMethod)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setHttpMethod(")
              .appendData(httpMethod)
              .appendScript(");");
        ScriptSessions.addScript(script);
    }

    /**
     * Ensure that remote calls happen in the order in which they were sent? (Default: false)
     * @param ordered True to set call ordering.
     * @see <a href="http://getahead.org/dwr/browser/engine/ordering">Ordering documentation</a>
     */
    public static void setOrdered(boolean ordered)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setOrdered(")
              .appendData(ordered)
              .appendScript(");");
        ScriptSessions.addScript(script);
    }

    /**
     * Do we ask the XHR object to be asynchronous? (Default: true)
     * @param async False to become synchronous (not recommended)
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public static void setAsync(boolean async)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setAsync(")
              .appendData(async)
              .appendScript(");");
        ScriptSessions.addScript(script);
    }

    /**
     * Does DWR poll the server for updates? (Default: false)
     * @param activeReverseAjax True/False to turn RA on/off
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public static void setActiveReverseAjax(boolean activeReverseAjax)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setActiveReverseAjax(")
              .appendData(activeReverseAjax)
              .appendScript(");");
        ScriptSessions.addScript(script);
    }

    /**
     * Does DWR us comet polling? (Default: true)
     * @param pollComet True/False to use Comet where supported
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public static void setPollUsingComet(boolean pollComet)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setPollUsingComet(")
              .appendData(pollComet)
              .appendScript(");");
        ScriptSessions.addScript(script);
    }

    /**
     * Set the preferred polling type.
     * @param newPollType One of {@link #XMLHttpRequest}, {@link #IFrame} or {@link #ScriptTag}
     * @see <a href="http://getahead.org/dwr/browser/engine/options">Options documentation</a>
     */
    public static void setPollType(int newPollType)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("dwr.engine.setPollUsingComet(")
              .appendData(newPollType)
              .appendScript(");");
        ScriptSessions.addScript(script);
    }
}
