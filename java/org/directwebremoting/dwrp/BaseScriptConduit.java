package org.directwebremoting.dwrp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.util.DebuggingPrintWriter;
import org.directwebremoting.util.Logger;

/**
 * A ScriptConduit that works with the parent Marshaller.
 * In some ways this is nasty because it has access to essentially private parts
 * of PollHandler, however there is nowhere sensible to store them
 * within that class, so this is a hacky simplification.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BaseScriptConduit extends ScriptConduit
{
    /**
     * Simple ctor
     * @param response Used to flush output
     * @param batchId The id of the batch that we are responding to
     * @param converterManager How we convert objects to script
     * @throws IOException If stream ops fail
     */
    public BaseScriptConduit(HttpServletResponse response, String batchId, ConverterManager converterManager) throws IOException
    {
        super(RANK_FAST);

        this.response = response;
        this.batchId = batchId;
        this.converterManager = converterManager;

        response.setContentType(getOutboundMimeType());

        if (log.isDebugEnabled())
        {
            // This might be considered evil - altering the program flow
            // depending on the log status, however DebuggingPrintWriter is
            // very thin and only about debugging
            out = new DebuggingPrintWriter("", response.getWriter());
        }
        else
        {
            out = response.getWriter();
        }

        // Setup a debugging prefix
        if (out instanceof DebuggingPrintWriter)
        {
            DebuggingPrintWriter dpw = (DebuggingPrintWriter) out;
            dpw.setPrefix("out(" + hashCode() + "): ");
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
     * conduit, after {@link #preStreamSetup()} and before any scripts are sent.
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
     * @throws IOException
     */
    public void close(int timetoNextPoll) throws IOException
    {
        try
        {
            EnginePrivate.remoteHandleCallback(this, batchId, "0", new Integer(timetoNextPoll));
        }
        catch (Exception ex)
        {
            EnginePrivate.remoteHandleException(this, batchId, "0", ex);
            log.warn("--Erroring: batchId[" + batchId + "] message[" + ex.toString() + ']', ex);
        }

        endStream();
    }

    /**
     * Ensure that output we have done is written to the client
     * @return true/false depending on the write status
     */
    protected boolean flush()
    {
        out.flush();

        // I'm not totally sure if this is the right thing to do.
        // A PrintWriter that encounters an error never recovers so maybe
        // we could be more robust by using a lower level object and
        // working out what to do if something goes wrong. Annoyingly
        // PrintWriter also throws the original exception away.
        if (out.checkError())
        {
            log.debug("Error writing to stream");
            // throw new IOException("Error writing to stream");
            return false;
        }

        try
        {
            response.flushBuffer();
            return true;
        }
        catch (IOException ex)
        {
            // This is likely to be because the user has gone away. Maybe
            // we should do something clever like remove the script session?
            log.debug("Error writing to HTTP response:" + ex);
            return false;
        }
    }

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
    protected final PrintWriter out;

    /**
     * What is the ID of the request that we are responding to?
     */
    protected final String batchId;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(BaseScriptConduit.class);

    /**
     * The slab of data we send to IE to get it to stream
     */
    protected static final String fourKFlushData;
    static
    {
        StringBuffer buffer = new StringBuffer(409600);
        for (int i = 0; i < 4096; i++)
        {
            buffer.append(" ");
        }
        fourKFlushData = buffer.toString();
    }
}