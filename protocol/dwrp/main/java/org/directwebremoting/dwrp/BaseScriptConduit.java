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

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.extend.ScriptConduit;

/**
 * A ScriptConduit that works with the parent Marshaller.
 * In some ways this is nasty because it has access to essentially private parts
 * of BasePollHandler, however there is nowhere sensible to store them
 * within that class, so this is a hacky simplification.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BaseScriptConduit implements ScriptConduit
{
    /**
     * Simple ctor
     * @param batchId The id of the batch that we are responding to
     * @param converterManager How we convert objects to script
     * @throws IOException If stream actions fail
     */
    public BaseScriptConduit(PrintWriter out, String instanceId, String batchId, ConverterManager converterManager, boolean jsonOutput) throws IOException
    {
        this.out = out;
        this.instanceId = instanceId;
        this.batchId = batchId;
        this.converterManager = converterManager;
        this.jsonOutput = jsonOutput;
    }

    /**
     * A poll has finished, get the client to call us back
     * @param timetoNextPoll How long before we tell the browser to come back?
     * @throws IOException When we fail to call endStream()
     */
    protected void sendPollReply(int timetoNextPoll) throws IOException
    {
        sendBeginChunk();
        ScriptBuffer script = EnginePrivate.getRemoteHandleCallbackScript(batchId, "0", timetoNextPoll);
        sendScript(ScriptBufferUtil.createOutput(script, converterManager, jsonOutput));
        sendEndChunk();
    }

    /**
     * When and what should we log? Options are (specified in the DWR servlet's init-params):
     * 1) call (start of call + successful return values).
     * 2) exception (checked) - default for debug.
     * 3) runtimeexception (unchecked).
     * 4) error - default for production.
     * 5) off.
     */
    public void setAccessLogLevel(String accessLogLevel)
    {
        this.accessLogLevel = accessLogLevel;
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
     * When and what should we log? Options are (specified in the DWR servlet's init-params):
     * 1) call (start of call + successful return values).
     * 2) exception (checked) - default for debug.
     * 3) runtimeexception (unchecked).
     * 4) error - default for production.
     * 5) off.
     */
    protected String accessLogLevel = null;

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * The response output stream
     */
    protected final PrintWriter out;

    /**
     * What is the ID of the DWR instance that we are responding to?
     */
    protected final String instanceId;

    /**
     * What is the ID of the request that we are responding to?
     */
    protected final String batchId;
}
