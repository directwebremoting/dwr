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
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.util.MimeConstants;

/**
 * A ScriptConduit for use with plain Javascript output.
 * <p>Scripts are plain Javascript without 'execute-in-parent-context' wrapping,
 * but with script-start and script-end markers.
 * <p>If this conduit is used the client should direct the output to an iframe
 * and then poll, looking for new data into the iframe. The html tags should be
 * removed and script between script-start and script-end tags eval()ed.
 * <p>This conduit is useful for Firefox. It will not work as it stands with IE
 * 6/7 because they don't allow the browser to see data entering an iframe until
 * it overflows a 4k buffer.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PlainScriptConduit extends BaseScriptConduit
{
    /**
     * Simple ctor
     * @param batchId The id of the batch that we are responding to
     * @param converterManager How we convert objects to script
     * @throws IOException If stream actions fail
     */
    public PlainScriptConduit(String instanceId, String batchId, ConverterManager converterManager, boolean jsonOutput) throws IOException
    {
        super(instanceId, batchId, converterManager, jsonOutput);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#preStreamSetup()
     */
    public String getOutboundMimeType()
    {
        return MimeConstants.MIME_JS;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#beginStream()
     */
    public void sendBeginStream(PrintWriter out)
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#beginChunk(java.io.PrintWriter)
     */
    public void sendBeginChunk(PrintWriter out)
    {
        out.println(ProtocolConstants.SCRIPT_START_MARKER);
        out.println(EnginePrivate.remoteBeginWrapper(instanceId, false, null));
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
     */
    public void sendScript(PrintWriter out, String script) throws IOException, ConversionException
    {
        if (log.isDebugEnabled()) {
            log.debug("Execution time: " + new Date().toString() + " - Writing to response: " + script);
        }
        out.println(script);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#endChunk(java.io.PrintWriter)
     */
    public void sendEndChunk(PrintWriter out)
    {
        out.println(EnginePrivate.remoteEndWrapper(instanceId, false));
        out.println(ProtocolConstants.SCRIPT_END_MARKER);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#endStream(java.io.PrintWriter, int)
     */
    public void sendEndStream(PrintWriter out, int timetoNextPoll) throws IOException
    {
        sendPollReply(out, timetoNextPoll);
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(PlainScriptConduit.class);
}
