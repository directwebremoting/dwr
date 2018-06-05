package org.directwebremoting.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.util.LocalUtil;

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
    public final void enterSleep(String batchId, Runnable onClose, int disconnectedTime)
    {
        this.batchId = batchId;
        this.onClose = onClose;
        this.disconnectedTime = disconnectedTime;
        // Send beginStream and execute Runnables in original request thread before sleeping
        doWork();
        if (!closed)
        {
            enterSleep();
        }
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
    public final int wakeUpToClose()
    {
        synchronized (lock) {
            closePending = true;
        }
        wakeUp();
        return disconnectedTime;
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
                beginStreamAndChunk();
                opened = true;
            }
            sendNewChunkScripts();
            checkNonChunkScripts();
            if (closed) {
                sendPollReply();
                endStreamAndChunk();
                doClose();
            } else {
                endChunk();
            }

            // If we got here without exceptions then writing was ok so we can update alive timestamp
            scriptSession.updateLastAccessedTime();
        } catch(Exception ex) {
            if (LocalUtil.getRootCause(ex) instanceof IOException) {
                log.debug("Poll I/O error", ex);
            } else {
                log.error("Error during poll.", ex);
            }
            closed = true;
            try {
                sendPollReply();
                endStreamAndChunk();
            } catch(Exception ignore) {
                // Swallow any exception and fall through to attempt to close poll below
            }
            doClose();
        }
    }

    private void beginStreamAndChunk() throws Exception
    {
        response.setContentType(conduit.getOutboundMimeType());
        // If there is a runnable for beginning of response then run it
        RealScriptSession.Script script = scriptSession.getScript(nextScriptIndex);
        boolean beginningRunnable = false;
        if (script != null && script.getScript() instanceof Runnable) {
            try {
                ((Runnable) script.getScript()).run();
            } catch(Exception ex) {
                log.error("Exception when executing Script Runnable.", ex);
            }
            beginningRunnable = true;
        }
        // Send stream prefix
        conduit.beginStreamAndChunk();
        // Send confirmation for the runnable to client after stream prefix
        if (beginningRunnable) {
            conduit.sendScript(EnginePrivate.getRemoteHandleReverseAjaxScript(script.getIndex(), ""));
            nextScriptIndex = script.getIndex() + 1;
        }
    }

    private void sendNewChunkScripts() throws Exception
    {
        // Scripts
        while(true) {
            RealScriptSession.Script script = scriptSession.getScript(nextScriptIndex);
            if (script != null && script.getScript() instanceof String) {
                conduit.sendScript(EnginePrivate.getRemoteHandleReverseAjaxScript(script.getIndex(), (String) script.getScript()));
                nextScriptIndex = script.getIndex() + 1;
            } else {
                break;
            }
        }
    }

    private void checkNonChunkScripts()
    {
        RealScriptSession.Script script = scriptSession.getScript(nextScriptIndex);
        if (script != null && !(script.getScript() instanceof String)) {
            // Trigger a new poll request without waiting so non-chunk scripts can execute
            closed = true;
            disconnectedTime = 0;
        }
    }

    private void sendPollReply() throws ConversionException, IOException
    {
        ScriptBuffer script = EnginePrivate.getRemoteHandleCallbackScript(batchId, "0", disconnectedTime);
        conduit.sendScript(ScriptBufferUtil.createOutput(script, converterManager, jsonOutput));
    }

    private void endStreamAndChunk() throws Exception
    {
        conduit.endStreamAndChunk();
    }

    private void endChunk() throws IOException
    {
        conduit.endChunk();

        // Flush and check errors
        out.flush();
        if (out.checkError())
        {
            throw new IOException("Stream write error");
        }
        response.flushBuffer();
    }

    private void doClose()
    {
        if (onClose != null)
        {
            onClose.run();
        }
        close();
    }

    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    public void setJsonOutput(boolean jsonOutput)
    {
        this.jsonOutput = jsonOutput;
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

    // Members set in enterSleep
    private String batchId;
    private Runnable onClose;
    private int disconnectedTime;

    // Injected from container
    protected ConverterManager converterManager = null;
    protected boolean jsonOutput = false;

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
