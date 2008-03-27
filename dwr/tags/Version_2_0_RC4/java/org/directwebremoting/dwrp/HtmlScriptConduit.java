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

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.util.MimeConstants;

/**
 * A ScriptConduit for use with HTML wrapped Javascript output.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HtmlScriptConduit extends BaseScriptConduit
{
    /**
     * Simple ctor
     * @param response Used to flush output
     * @param batchId The id of the batch that we are responding to
     * @param converterManager How we convert objects to script
     * @throws IOException If stream ops fail
     */
    public HtmlScriptConduit(HttpServletResponse response, String batchId, ConverterManager converterManager) throws IOException
    {
        super(response, batchId, converterManager);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#preStreamSetup()
     */
    public void preStreamSetup()
    {
        response.setContentType(MimeConstants.MIME_HTML);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#beginStream()
     */
    public void beginStream()
    {
        out.println("<html><body>");
        out.println("<script type=\"text/javascript\">");
        out.println(ProtocolConstants.SCRIPT_START_MARKER);
        out.println(EnginePrivate.remoteBeginIFrameResponse(batchId, false));
        out.println(ProtocolConstants.SCRIPT_END_MARKER);
        out.println("</script>");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#endStream()
     */
    public void endStream()
    {
        out.println("<script type=\"text/javascript\">");
        out.println(EnginePrivate.remoteEndIFrameResponse(batchId, false));
        out.println(ProtocolConstants.SCRIPT_START_MARKER);
        out.println("</script>");
        out.println("</body></html>");
        out.println(ProtocolConstants.SCRIPT_END_MARKER);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
     */
    public boolean addScript(ScriptBuffer scriptBuffer) throws IOException, MarshallException
    {
        String script = ScriptBufferUtil.createOutput(scriptBuffer, converterManager);

        // Write a script out in a synchronized manner to avoid thread clashes
        synchronized (out)
        {
            out.println("<script type=\"text/javascript\">");
            out.println(ProtocolConstants.SCRIPT_START_MARKER);
            out.println(EnginePrivate.remoteEval(script));
            out.println(ProtocolConstants.SCRIPT_END_MARKER);
            out.println("</script>");

            return flush();
        }
    }
}
