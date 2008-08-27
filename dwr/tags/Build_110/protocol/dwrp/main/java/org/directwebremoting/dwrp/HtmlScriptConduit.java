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

import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.util.MimeConstants;

/**
 * A ScriptConduit for use with HTML/SCRIPT wrapped Javascript output.
 * <p>Scripts begin with an html and script tags. The scripts have been altered
 * to include an 'execute-in-parent-context' wrapper.
 * <p>If this conduit is used, the output should be directed to an iframe. No
 * polling should be required.
 * <p>This conduit works with IE 6/7 since the 4k buffer drawback (see
 * {@link PlainScriptConduit} and {@link Html4kScriptConduit} does not prevent
 * the execution of script elements.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HtmlScriptConduit extends BaseScriptConduit
{
    /**
     * Simple ctor
     * @param response Used to flush output
     * @param batchId The id of the batch that we are responding to
     * @param converterManager How we convert objects to script
     * @throws IOException If stream actions fail
     */
    public HtmlScriptConduit(Sleeper sleeper, HttpServletResponse response, String batchId, ConverterManager converterManager, boolean jsonOutput) throws IOException
    {
        super(sleeper, response, batchId, converterManager, jsonOutput);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseCallHandler#getOutboundMimeType()
     */
    @Override
    protected String getOutboundMimeType()
    {
        return MimeConstants.MIME_HTML;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#beginStream()
     */
    @Override
    public void beginStream()
    {
        synchronized (out)
        {
            out.println("<html><body>");
            out.println("<script type=\"text/javascript\">");
            out.println(EnginePrivate.remoteBeginIFrameResponse(batchId, true));
            out.println("</script>");
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#endStream()
     */
    @Override
    public void endStream()
    {
        synchronized (out)
        {
            out.println("<script type=\"text/javascript\">");
            out.println(EnginePrivate.remoteEndIFrameResponse(batchId, true));
            out.println("</script>");
            out.println("</body></html>");
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
     */
    @Override
    public boolean addScript(ScriptBuffer scriptBuffer) throws IOException, ConversionException
    {
        String script = ScriptBufferUtil.createOutput(scriptBuffer, converterManager, jsonOutput);

        synchronized (out)
        {
            // TODO: We don't think the comments are needed because the exec is automatic
            out.println("<script type=\"text/javascript\">");
            // out.println(ProtocolConstants.SCRIPT_START_MARKER);
            out.println(EnginePrivate.remoteEval(script));
            // out.println(ProtocolConstants.SCRIPT_END_MARKER);
            out.println("</script>");

            return flush();
        }
    }
}
