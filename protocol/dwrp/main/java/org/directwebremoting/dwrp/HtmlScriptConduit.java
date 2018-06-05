package org.directwebremoting.dwrp;

import java.io.IOException;
import java.io.PrintWriter;

import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.util.MimeConstants;

/**
 * A ScriptConduit for use with HTML/SCRIPT wrapped Javascript output.
 * <p>Scripts begin with an html and script tags. The scripts have been altered
 * to include an 'execute-in-parent-context' wrapper.
 * <p>If this conduit is used, the output should be directed to an iframe. No
 * polling should be required.
 * <p>This conduit works with IE 6/7 since the 4k buffer drawback does not prevent
 * the execution of script elements.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HtmlScriptConduit extends BaseScriptConduit
{
    /**
     * Simple ctor
     */
    public HtmlScriptConduit(PrintWriter out, String instanceId, String batchId, String documentDomain)
    {
        super(out, instanceId);
        this.batchId = batchId;
        this.documentDomain = documentDomain;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#getOutboundMimeType()
     */
    public String getOutboundMimeType()
    {
        return MimeConstants.MIME_HTML;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#beginStreamAndChunk()
     */
    public void beginStreamAndChunk()
    {
        out.println("<html><body>");
        beginChunk(true);
        out.println(EnginePrivate.remoteBeginIFrameResponse(batchId, true));
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#beginChunk()
     */
    public void beginChunk()
    {
        beginChunk(false);
    }

    private void beginChunk(boolean setDocumentDomain)
    {
        if (!chunkOpen) {
            out.println("<script type=\"text/javascript\">");
            out.println(EnginePrivate.remoteBeginWrapper(instanceId, true, (setDocumentDomain ? documentDomain : null)));
            chunkOpen = true;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#sendScript(java.lang.String)
     */
    public void sendScript(String script) throws IOException
    {
        beginChunk(false);
        out.println(EnginePrivate.remoteExecute(script));
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#endChunk()
     */
    public void endChunk()
    {
        if (chunkOpen) {
            out.println(EnginePrivate.remoteEndWrapper(instanceId, true));
            out.println("</script>");
            chunkOpen = false;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#endStream(java.io.PrintWriter, int)
     */
    public void endStreamAndChunk() throws IOException
    {
        beginChunk();
        out.println(EnginePrivate.remoteEndIFrameResponse(batchId, true));
        endChunk();
        out.println("</body></html>");
    }

    private final String batchId;
    private final String documentDomain;

    /**
     * Is a chunk section currently open on the output?
     */
    private boolean chunkOpen = false;
}
