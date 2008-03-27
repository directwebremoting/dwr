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

import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.dwrp.ConversionConstants;
import org.directwebremoting.impl.DefaultRemoter;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.MimeConstants;

/**
 * An abstraction of the DWREngine Javascript class for use by
 * {@link org.directwebremoting.dwrp.BaseCallMarshaller},
 * {@link org.directwebremoting.dwrp.PollHandler} and a few others that need
 * to call internal functions in engine.js
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class RemoteDwrEngine extends ScriptProxy
{
    /**
     * Call the DWREngine._remoteHandleResponse() in the browser
     * @param conduit The browser pipe to write to
     * @param batchId The identifier of the batch that we are handling a response for
     * @param callId The identifier of the call that we are handling a response for
     * @param data The data to pass to the callback function
     * @throws IOException If writing fails.
     * @throws MarshallException If objects in the script can not be marshalled
     */
    public static void remoteHandleCallback(ScriptConduit conduit, String batchId, String callId, Object data) throws IOException, MarshallException
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("DWREngine._remoteHandleCallback(\'")
              .appendScript(batchId)
              .appendScript("\',\'")
              .appendScript(callId)
              .appendScript("\',")
              .appendData(data)
              .appendScript(");");

        conduit.addScript(script);
    }

    /**
     * Call the DWREngine._remoteHandleException() in the browser
     * @param conduit The browser pipe to write to
     * @param batchId The identifier of the batch that we are handling a response for
     * @param callId The id of the call we are replying to
     * @param ex The exception to throw on the remote end
     * @throws IOException If writing fails.
     */
    public static void remoteHandleMarshallException(ScriptConduit conduit, String batchId, String callId, MarshallException ex) throws IOException
    {
        try
        {
            ScriptBuffer script = new ScriptBuffer();
            script.appendScript("DWREngine._remoteHandleException(\'")
                  .appendScript(batchId)
                  .appendScript("\',\'")
                  .appendScript(callId)
                  .appendScript("\',")
                  .appendData(ex)
                  .appendScript(");");

            conduit.addScript(script);
        }
        catch (MarshallException mex)
        {
            ScriptBuffer script = new ScriptBuffer();
            script.appendScript("DWREngine._remoteHandleException(\'")
                  .appendScript(batchId)
                  .appendScript("\',\'")
                  .appendScript(callId)
                  .appendScript("\',")
                  .appendData(ex)
                  .appendScript(");");

            addScriptWithoutException(conduit, script);
        }
    }

    /**
     * Call the DWREngine._remoteHandleException() in the browser
     * @param conduit The browser pipe to write to
     * @param batchId The identifier of the batch that we are handling a response for
     * @param callId The id of the call we are replying to
     * @param ex The exception to throw on the remote end
     * @throws IOException If writing fails.
     */
    public static void remoteHandleException(ScriptConduit conduit, String batchId, String callId, Throwable ex) throws IOException
    {
        try
        {
            ScriptBuffer script = new ScriptBuffer();
            script.appendScript("DWREngine._remoteHandleException(\'")
                  .appendScript(batchId)
                  .appendScript("\',\'")
                  .appendScript(callId)
                  .appendScript("\',")
                  .appendData(ex)
                  .appendScript(");");

            conduit.addScript(script);
        }
        catch (MarshallException mex)
        {
            ScriptBuffer script = new ScriptBuffer();
            script.appendScript("DWREngine._remoteHandleException(\'")
                  .appendScript(batchId)
                  .appendScript("\',\'")
                  .appendScript(callId)
                  .appendScript("\',")
                  .appendData(ex)
                  .appendScript(");");

            addScriptWithoutException(conduit, script);
        }
    }

    /**
     * Call the DWREngine._remoteHandleServerException() in the browser
     * @param response The HTTP response to write to
     * @param ex The execption from which we make a reply
     * @throws IOException If writing fails.
     */
    public static void remoteHandleExceptionWithoutCallId(HttpServletResponse response, Exception ex) throws IOException
    {
        response.setContentType(MimeConstants.MIME_PLAIN);
        PrintWriter out = response.getWriter();

        String output = JavascriptUtil.escapeJavaScript(ex.getMessage());
        String error = "{ type:'" + ex.getClass().getName() + "', message:'" + output + "' }";

        // We know nothing about the browser state - this needs to work in an
        // HTML context as well as a Javascript context
        out.println("// <script>");
        out.println(ConversionConstants.SCRIPT_CALL_REPLY);
        out.println("DWREngine._remoteHandleExceptionWithoutCallId(" + error + ");");
        out.println("// </script>");
    }

    /**
     * Take an XML string, and convert it into some Javascript that when
     * executed will return a DOM object that represents the same XML object
     * @param xml The XML string to convert
     * @return The Javascript
     */
    public static String xmlStringToJavascriptDom(String xml)
    {
        String xmlout = JavascriptUtil.escapeJavaScript(xml);
        return "DWREngine._unserializeDocument(\"" + xmlout + "\")";
    }

    /**
     * Get a string which will initialize a DWREngine object 
     * @param path The setting for the _defaultPath property
     * @return A DWREngine init script
     */
    public static String getEngineInitScript(String path)
    {
        return "// Provide a default path to DWREngine\n" +
               "if (dwr == null) var dwr = {};\n" +
               "if (dwr.engine == null) dwr.engine = {};\n" +
               "if (DWREngine == null) var DWREngine = dwr.engine;\n" +
               "\n" +
               "DWREngine._defaultPath = '" + path + "';\n" +
               '\n';
    }

    /**
     * {@link DefaultRemoter} needs to know the name of the execute function
     * @return The execute function name
     */
    public static String getExecuteFunctionName()
    {
        return "DWREngine._execute";
    }

    /**
     * A script to send at the beginning of an iframe response
     * @param batchId The id of the current batch
     * @return A script to init the environment
     */
    public static String remoteBeginIFrameResponse(String batchId)
    {
        return "window.parent.DWREngine._remoteBeginIFrameResponse(this.frameElement, '" + batchId + "');";
    }

    /**
     * A script to send at the end of an iframe response
     * @param batchId The id of the current batch
     * @return A script to tidy up the environment
     */
    public static String remoteEndIFrameResponse(String batchId)
    {
        return "window.parent.DWREngine._remoteEndIFrameResponse('" + batchId + "');";
    }

    /**
     * Prepare a script for execution in an iframe environment
     * @param script The script to modify
     * @return The modified script
     */
    public static String remoteEval(String script)
    {
        return "window.parent.DWREngine._remoteEval(\"" + JavascriptUtil.escapeJavaScript(script) + "\");";
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
