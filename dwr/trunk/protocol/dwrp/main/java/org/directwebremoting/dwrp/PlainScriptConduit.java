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
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.extend.Sleeper;
import org.directwebremoting.util.MimeConstants;

/**
 * A ScriptConduit for use with plain Javascript output.
 * <p>Scripts are plain Javascript without 'execute-in-parent-context' wrapping,
 * but with script-start and script-end markers.
 * <p>If this conduit is used the client should direct the output to an iframe
 * and then poll, looking for new data into the iframe. The html tags should be
 * removed and script between script-start and script-end tags eval()ed.
 * <p>This conduit is useful for Firefox. It will not work as it stands with IE
 * 6/6 because they don't allow the browser to see data entering an iframe until
 * it overflows a 4k buffer. See the {@link Html4kScriptConduit} for a version
 * that works around this problem.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PlainScriptConduit extends BaseScriptConduit
{
    /**
     * Simple ctor
     * @param response Used to flush output
     * @param batchId The id of the batch that we are responding to
     * @param converterManager How we convert objects to script
     * @throws IOException If stream actions fail
     */
    public PlainScriptConduit(Sleeper sleeper, HttpServletResponse response, String batchId, ConverterManager converterManager, boolean jsonOutput) throws IOException
    {
        super(sleeper, response, batchId, converterManager, jsonOutput);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#preStreamSetup()
     */
    @Override
    protected String getOutboundMimeType()
    {
        return MimeConstants.MIME_JS;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#beginStream()
     */
    @Override
    public void beginStream()
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseScriptConduit#endStream()
     */
    @Override
    public void endStream()
    {
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
            out.println(ProtocolConstants.SCRIPT_START_MARKER);
            out.println(script);
            out.println(ProtocolConstants.SCRIPT_END_MARKER);

            return flush();
        }
    }
}
