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
 * A ScriptConduit for use with HTML/PRE wrapped Javascript output.
 * <p>Scripts begin with an html, body and pre tag followed by plain Javascript
 * without 'execute-in-parent-context' wrapping, but with script-start and
 * script-end markers.
 * <p>If this conduit is used the client should direct the output to an iframe
 * and then poll, looking for new data into the iframe. The html tags should be
 * removed and script between script-start and script-end tags eval()ed.
 * <p>This conduit also sends 4k of whitespace data on each flush. This causes
 * IE to recognise new content. This would be a significant network overhead
 * so it is important to use gzip on the connection. This complexity has caused
 * us to turn this conduit off at the moment.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Html4kScriptConduit extends BaseScriptConduit
{
    /**
     * Simple ctor
     * @param response Used to flush output
     * @param batchId The id of the batch that we are responding to
     * @param converterManager How we convert objects to script
     * @throws IOException If stream ops fail
     */
    public Html4kScriptConduit(HttpServletResponse response, String batchId, ConverterManager converterManager) throws IOException
    {
        super(response, batchId, converterManager);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseCallMarshaller#getOutboundMimeType()
     */
    protected String getOutboundMimeType()
    {
        return MimeConstants.MIME_HTML;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#beginStream()
     */
    public void beginStream()
    {
        synchronized (out)
        {
            out.println("<html><body><pre>");

            out.println(ProtocolConstants.SCRIPT_START_MARKER);
            out.println(EnginePrivate.remoteBeginIFrameResponse(batchId, false));
            out.println(ProtocolConstants.SCRIPT_END_MARKER);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#endStream()
     */
    public void endStream()
    {
        synchronized (out)
        {
            out.println(ProtocolConstants.SCRIPT_START_MARKER);
            out.println(EnginePrivate.remoteEndIFrameResponse(batchId, false));
            out.println(ProtocolConstants.SCRIPT_END_MARKER);

            out.println("</pre></body></html>");
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
     */
    public boolean addScript(ScriptBuffer scriptBuffer) throws IOException, MarshallException
    {
        String script = ScriptBufferUtil.createOutput(scriptBuffer, converterManager);

        synchronized (out)
        {
            out.println(ProtocolConstants.SCRIPT_START_MARKER);
            out.println(script);
            out.println(ProtocolConstants.SCRIPT_END_MARKER);

            out.print(fourKFlushData);

            return flush();
        }
    }

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
