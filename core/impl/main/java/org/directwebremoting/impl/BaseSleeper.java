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
package org.directwebremoting.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.extend.Sleeper;

/**
 * Common functionality for stock Sleepers.
 * @author Mike Wilson
 */
public abstract class BaseSleeper implements Sleeper
{
    public BaseSleeper(HttpServletResponse response, RealScriptSession scriptSession, ScriptConduit conduit) throws IOException
    {
        this.response = response;
        this.scriptSession = scriptSession;
        this.conduit = conduit;

        out = response.getWriter();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Sleeper#enterSleep(java.lang.Runnable, int)
     */
    @SuppressWarnings("hiding")
    public final void enterSleep(Runnable onClose, int disconnectedTime) throws IOException
    {
        this.onClose = onClose;
        this.disconnectedTime = disconnectedTime;
        enterSleep();
    }

    /**
     * Should "halt" the current poll request while allowing output be written to its response.
     * (abstract method to be implemented by concrete Sleepers)
     */
    protected abstract void enterSleep();

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Sleeper#wakeUpForData()
     */
    public final void wakeUpForData()
    {
        wakeUp();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Sleeper#wakeUpToClose()
     */
    public final void wakeUpToClose()
    {
        synchronized (lock) {
            closePending = true;
        }
        wakeUp();
    }

    /**
     * Should cause the doWork() method to be called on a background or 
     * container thread.
     * (abstract method to be implemented by concrete Sleepers)
     */
    protected abstract void wakeUp();

    /**
     * This method should be called upon by the concrete Sleepers when they
     * are awakened by the wakeUp() call. They should guarantee that the call
     * is made on a container or background thread (thus not blocking the 
     * wakeUp() call) and that it is invoked by no more than a single thread 
     * at a time.
     */
    protected final void doWork()
    {
        try {
            if (closed) {
                return;
            }
            synchronized (lock) {
                if (closePending) {
                    closePending = false;
                    closed = true;
                }
            }
            if (!opened) {
                sendBeginStream();
                opened = true;
            }
            if (closed) {
                sendEndStream();
                doClose();
                return;
            }
            sendNewScripts();
        } catch(Exception ex) {
            log.warn("Poll ended unexpectedly (" + ex.getMessage() + ").");
            closed = true;
            try {
                sendEndStream();
            } catch(Exception ignore) {
                // Swallow any exception and fall through to attempt to close poll below
            }
            doClose();
        }
    }

    private void sendBeginStream()
    {
        response.setContentType(conduit.getOutboundMimeType());
        conduit.sendBeginStream(out);
    }

    private void sendEndStream() throws IOException
    {
        conduit.sendEndStream(out, disconnectedTime);
    }

    private void sendNewScripts() throws IOException
    {
        RealScriptSession.Scripts scripts = scriptSession.getScripts(nextScriptIndex);
        if (scripts.getScripts().size() > 0) {
            // Send unsent scripts
            conduit.sendBeginChunk(out);
            for(int i=0; i<scripts.getScripts().size(); i++) {
                conduit.sendScript(
                    out,
                    EnginePrivate.getRemoteHandleReverseAjaxScript(
                        scripts.getScriptIndexOffset() + i,
                        scripts.getScripts().get(i)));
            }
            conduit.sendEndChunk(out);
            // Flush and check errors
            out.flush();
            if (out.checkError())
            {
                throw new IOException("Stream write error");
            }
            response.flushBuffer();
            // Advance counter so we don't try to send the same scripts again
            nextScriptIndex = scripts.getScriptIndexOffset() + scripts.getScripts().size();
        }
    }

    private void doClose()
    {
        close();
        if (onClose != null)
        {
            onClose.run();
        }
    }

    /**
     * Should close the ongoing poll request and end any asynchronous cycle.
     * (abstract method to be implemented by concrete Sleepers)
     */
    protected abstract void close();

    // Members set in constructor
    private final HttpServletResponse response;
    private final RealScriptSession scriptSession;
    private final ScriptConduit conduit;
    private final PrintWriter out;

    // Members set later
    private Runnable onClose;
    private int disconnectedTime;

    // State manipulated by calls on public API
    private final Object lock = new Object();
    private boolean closePending = false;

    // State manipulated by (thread-safe) internal calls from sub-classes
    private boolean opened = false;
    private boolean closed = false;
    private long nextScriptIndex = 0;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BaseSleeper.class);
}
