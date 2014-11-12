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

import org.directwebremoting.extend.ConverterManager;
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
     * @param batchId The id of the batch that we are responding to
     * @param converterManager How we convert objects to script
     * @throws IOException If stream actions fail
     */
    public HtmlScriptConduit(String instanceId, String batchId, String documentDomain, ConverterManager converterManager, boolean jsonOutput) throws IOException
    {
        super(instanceId, batchId, converterManager, jsonOutput);
        this.documentDomain = documentDomain;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseCallHandler#getOutboundMimeType()
     */
    public String getOutboundMimeType()
    {
        return MimeConstants.MIME_HTML;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#beginStream(java.io.PrintWriter)
     */
    public void sendBeginStream(PrintWriter out)
    {
        out.println("<html><body>");
        out.println("<script type=\"text/javascript\">");
        out.println(EnginePrivate.remoteBeginWrapper(instanceId, true, documentDomain));
        out.println(EnginePrivate.remoteBeginIFrameResponse(batchId, true));
        out.println(EnginePrivate.remoteEndWrapper(instanceId, true));
        out.println("</script>");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#beginChunk(java.io.PrintWriter)
     */
    public void sendBeginChunk(PrintWriter out)
    {
        out.println("<script type=\"text/javascript\">");
        out.println(EnginePrivate.remoteBeginWrapper(instanceId, true, null));
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#sendScript(java.io.PrintWriter, java.lang.String)
     */
    public void sendScript(PrintWriter out, String script) throws IOException
    {
        out.println(EnginePrivate.remoteExecute(script));
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#endChunk(java.io.PrintWriter)
     */
    public void sendEndChunk(PrintWriter out)
    {
        out.println(EnginePrivate.remoteEndWrapper(instanceId, true));
        out.println("</script>");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#endStream(java.io.PrintWriter, int)
     */
    public void sendEndStream(PrintWriter out, int timetoNextPoll) throws IOException
    {
        sendPollReply(out, timetoNextPoll);
        out.println("<script type=\"text/javascript\">");
        out.println(EnginePrivate.remoteBeginWrapper(instanceId, true, null));
        out.println(EnginePrivate.remoteEndIFrameResponse(batchId, true));
        out.println(EnginePrivate.remoteEndWrapper(instanceId, true));
        out.println("</script>");
        out.println("</body></html>");
    }

    private final String documentDomain;
}
