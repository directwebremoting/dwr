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
package org.directwebremoting.dwrp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.Alarm;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.util.DebuggingPrintWriter;

/**
 * A ScriptConduit that works with the parent Marshaller.
 * In some ways this is nasty because it has access to essentially private parts
 * of BasePollHandler, however there is nowhere sensible to store them
 * within that class, so this is a hacky simplification.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BaseScriptConduit extends ScriptConduit implements Alarm
{
    /**
     * Simple ctor
     * @param response Used to flush output
     * @param batchId The id of the batch that we are responding to
     * @param converterManager How we convert objects to script
     * @throws IOException If stream actions fail
     */
    public BaseScriptConduit(Sleeper sleeper, HttpServletResponse response, String batchId, ConverterManager converterManager, boolean jsonOutput) throws IOException
    {
        super(RANK_SLOW, true);

        this.response = response;
        this.batchId = batchId;
        this.converterManager = converterManager;
        this.jsonOutput = jsonOutput;
        this.sleeper = sleeper;

        response.setContentType(getOutboundMimeType());
        out = response.getWriter();

        if (debugScriptOutput && log.isDebugEnabled())
        {
            // This might be considered evil - altering the program flow
            // depending on the log status, however DebuggingPrintWriter is
            // very thin and only about debugging
            DebuggingPrintWriter dpw = new DebuggingPrintWriter("", out);
            dpw.setPrefix("out(" + hashCode() + "): ");

            out = dpw;
        }

        beginStream();
    }

    /**
     * What mime type should we send to the browser for this data?
     * @return A mime-type
     */
    protected abstract String getOutboundMimeType();

    /**
     * Called when we are initially setting up the stream. This does not send
     * any data to the client, just sets it up for data.
     * <p>This method is always called exactly once in the lifetime of a
     * conduit.
     */
    protected abstract void beginStream();

    /**
     * Called when we are shutting the stream down.
     * <p>This method is always called exactly once in the lifetime of a
     * conduit, just before the stream is closed.
     */
    protected abstract void endStream();

    /**
     * A poll has finished, get the client to call us back
     * @param timetoNextPoll How long before we tell the browser to come back?
     * @throws IOException When we fail to call endStream()
     */
    public void close(int timetoNextPoll) throws IOException
    {
        try
        {
            ScriptBuffer script = EnginePrivate.getRemoteHandleCallbackScript(batchId, "0", timetoNextPoll);
            addScript(script);
        }
        catch (Exception ex)
        {
            ScriptBuffer script = EnginePrivate.getRemoteHandleExceptionScript(batchId, "0", ex);
            try
            {
                addScript(script);
            }
            catch (ConversionException ex1)
            {
                log.warn("This can't happen:", ex1);
            }

            log.warn("--Erroring: batchId[" + batchId + "] message[" + ex.toString() + ']', ex);
        }

        endStream();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Alarm#cancel()
     */
    public void cancel()
    {
        // TODO: We shouldn't call sleeper.wakeUp(); any more
    }

    /**
     * Ensure that output we have done is written to the client
     * @return true/false depending on the write status
     */
    protected boolean flush()
    {
        out.flush();

        // A PrintWriter that encounters an error never recovers
        if (out.checkError())
        {
            log.debug("Error writing to stream");
            sleeper.wakeUp();
            return false;
        }

        try
        {
            response.flushBuffer();
            return true;
        }
        catch (IOException ex)
        {
            // This is likely to be because the user has gone away.
            log.debug("Error calling response.flushBuffer:" + ex);
            sleeper.wakeUp();
            return false;
        }
    }

    /**
     * Do we debug all the scripts that we output?
     * @param debugScriptOutput true to debug all of the output scripts (verbose)
     */
    public void setDebugScriptOutput(boolean debugScriptOutput)
    {
        this.debugScriptOutput = debugScriptOutput;
    }

    /**
     * Do we debug all the scripts that we output?
     */
    protected boolean debugScriptOutput = false;

    /**
     * Are we outputting in JSON mode?
     */
    protected boolean jsonOutput = false;

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * Used to flush data to the output stream
     */
    protected final HttpServletResponse response;

    /**
     * The PrintWriter to send output to, and that we should synchronize against
     */
    protected PrintWriter out;

    /**
     * What is the ID of the request that we are responding to?
     */
    protected final String batchId;

    /**
     * If an error happens, who wants to know?
     */
    protected final Sleeper sleeper;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BaseScriptConduit.class);
}
