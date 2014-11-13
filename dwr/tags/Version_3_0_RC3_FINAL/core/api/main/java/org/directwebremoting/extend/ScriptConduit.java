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
package org.directwebremoting.extend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;

/**
 * While a Marshaller is processing a request it can register a ScriptConduit
 * with the ScriptSession to say - pass scripts straight to me and bypass the
 * temporary storage area.
 * This interface allows this to happen.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ScriptConduit
{
    /**
     * What mime type should we send to the browser for this data?
     * @return A mime-type
     */
    String getOutboundMimeType();

    /**
     * Called when we are initially setting up the stream. This does not send
     * any data to the client, just sets it up for data.
     * <p>This method is always called exactly once in the lifetime of a
     * conduit.
     */
    void sendBeginStream(PrintWriter out);

    /**
     * Called before a each set of scripts that are to be sent.
     */
    void sendBeginChunk(PrintWriter out);

    /**
     * Add a script to the list bound for remote execution.
     * <p>It is not an error to refuse to handle the script and return false, it
     * just indicates that this ScriptConduit did not accept the script.
     * If the ScriptConduit can no longer function then it should throw an
     * exception and it will be assumed to be no longer useful.
     * If you want to implement this method then you will probably be doing
     * something like calling {@link ServletOutputStream#print(String)} and
     * passing in the results of calling ScriptBufferUtil.createOutput().
     * @param script The script to execute
     * @throws IOException If this conduit is broken and should not be used
     */
    void sendScript(PrintWriter out, String script) throws IOException;

    /**
     * Called after each set of scripts when they have been sent.
     */
    void sendEndChunk(PrintWriter out);

    /**
     * Called when we are shutting the stream down.
     * The poll has finished, get the client to call us back
     * @param timetoNextPoll How long before we tell the browser to come back?
     */
    void sendEndStream(PrintWriter out, int timetoNextPoll) throws IOException;
}
