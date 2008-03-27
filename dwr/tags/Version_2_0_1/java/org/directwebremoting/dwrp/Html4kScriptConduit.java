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
public class Html4kScriptConduit extends BaseScriptConduit
{
    /**
     * Simple ctor
     * @param response Used to flush output
     * @param partialResponse Do we do the IE 4k flush hack
     * @param batchId The id of the batch that we are responding to
     * @param converterManager How we convert objects to script
     * @throws IOException If stream ops fail
     */
    public Html4kScriptConduit(HttpServletResponse response, int partialResponse, String batchId, ConverterManager converterManager) throws IOException
    {
        super(response, batchId, converterManager);
        this.partialResponse = partialResponse;
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
            out.println(EnginePrivate.remoteEndIFrameResponse(batchId, false));
            out.println(ProtocolConstants.SCRIPT_START_MARKER);
            out.println("</pre></body></html>");
            out.println(ProtocolConstants.SCRIPT_END_MARKER);
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

            if (partialResponse == PollHandler.PARTIAL_RESPONSE_FLUSH)
            {
                out.print(fourKFlushData);
            }

            return flush();
        }
    }

    /**
     * Do we need to do the IE 4k flush thing?
     */
    protected final int partialResponse;
}
