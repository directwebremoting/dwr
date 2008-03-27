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
package org.directwebremoting.impl;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.dwrp.BaseCallMarshaller;
import org.directwebremoting.dwrp.PollHandler;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.ScriptConduit;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.MimeConstants;

/**
 * An abstraction of the DWREngine Javascript class for use by
 * {@link BaseCallMarshaller}, {@link PollHandler} and a few others that need
 * to call internal functions in engine.js
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class RemoteDwrEngine extends ScriptProxy
{
    /**
     * Call the DWREngine._remoteHandleServerException() in the browser
     * @param response The HTTP response to write to
     * @param ex The execption from which we make a reply
     * @throws IOException If writing fails.
     */
    public static void remoteHandleServerException(HttpServletResponse response, ServerException ex) throws IOException
    {
        response.setContentType(MimeConstants.MIME_PLAIN);
        PrintWriter out = response.getWriter();

        String output = JavascriptUtil.escapeJavaScript(ex.getMessage());

        // We know nothing about the browser state - this needs to work in an
        // HTML context as well as a Javascript context
        out.println("// <script>");
        out.print("DWREngine._remoteHandleServerException(\'Server Error");
        out.print(output);
        out.println("\');");
        out.println("// </script>");
    }

    /**
     * Call the DWREngine._remoteHandleResponse() in the browser
     * @param conduit The browser pipe to write to
     * @param id The identifier of the call that we are handling a response for
     * @param data The data to pass to the callback function
     * @throws IOException If writing fails.
     * @throws MarshallException If objects in the script can not be marshalled
     */
    public static void remoteHandleCallback(ScriptConduit conduit, String id, Object data) throws IOException, MarshallException
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("DWREngine._remoteHandleCallback(\'")
              .appendScript(id)
              .appendScript("\',")
              .appendData(data)
              .appendScript(");");

        conduit.addScript(script);
    }

    /**
     * Call the DWREngine._remoteHandleException() in the browser
     * @param conduit The browser pipe to write to
     * @param id The id of the call we are replying to
     * @param ex The exception to throw on the remote end
     * @throws IOException If writing fails.
     */
    public static void remoteHandleException(ScriptConduit conduit, String id, Throwable ex) throws IOException
    {
        try
        {
            ScriptBuffer script = new ScriptBuffer();
            script.appendScript("DWREngine._remoteHandleException(\'")
                  .appendScript(id)
                  .appendScript("\',")
                  .appendData(ex)
                  .appendScript(");");
    
            conduit.addScript(script);
        }
        catch (MarshallException mex)
        {
            ScriptBuffer script = new ScriptBuffer();
            script.appendScript("DWREngine._remoteHandleException(\'")
                  .appendScript(id)
                  .appendScript("\',")
                  .appendData(ex)
                  .appendScript(");");

            addScriptWithoutException(conduit, script);
        }
    }

    /**
     * Call the DWREngine._remoteHandleException() in the browser
     * @param conduit The browser pipe to write to
     * @param id The id of the call we are replying to
     * @param ex The exception to throw on the remote end
     * @throws IOException If writing fails.
     */
    public static void remoteHandleMarshallException(ScriptConduit conduit, String id, MarshallException ex) throws IOException
    {
        try
        {
            ScriptBuffer script = new ScriptBuffer();
            script.appendScript("DWREngine._remoteHandleException(\'")
                  .appendScript(id)
                  .appendScript("\',")
                  .appendData(ex)
                  .appendScript(");");

            conduit.addScript(script);
        }
        catch (MarshallException mex)
        {
            ScriptBuffer script = new ScriptBuffer();
            script.appendScript("DWREngine._remoteHandleException(\'")
                  .appendScript(id)
                  .appendScript("\',")
                  .appendData(ex)
                  .appendScript(");");

            addScriptWithoutException(conduit, script);
        }
    }

    /**
     * Calling {@link ScriptConduit#addScript(ScriptBuffer)} throws a
     * {@link MarshallException} which we want to ignore since it almost
     * certainly can't happen for real.
     * @param conduit The browser pipe to write to
     * @param script The buffer to add to the current {@link ScriptConduit}
     * @throws IOException If the write process fails
     */
    private static void addScriptWithoutException(ScriptConduit conduit, ScriptBuffer script) throws IOException
    {
        try
        {
            conduit.addScript(script);
        }
        catch (MarshallException ex)
        {
            log.warn("This exception can't happen", ex);
        }
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(RemoteDwrEngine.class);
}
