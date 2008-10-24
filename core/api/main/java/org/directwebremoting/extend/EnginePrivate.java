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

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.util.JavascriptUtil;

/**
 * An abstraction of the dwr.engine Javascript class.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class EnginePrivate
{
    /**
     * Call the dwr.engine.remote.handleResponse() in the browser
     * @param batchId The identifier of the batch that we are handling a response for
     * @param callId The identifier of the call that we are handling a response for
     * @param data The data to pass to the callback function
     * @return The script to send to the browser
     */
    public static ScriptBuffer getRemoteHandleCallbackScript(String batchId, String callId, Object data)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("dwr.engine.remote.handleCallback", batchId, callId, data);
        return script;
    }

    /**
     * Call dwr.engine.remote.handleException() in the browser
     * @param batchId The identifier of the batch that we are handling a response for
     * @param callId The id of the call we are replying to
     * @param ex The exception to throw on the remote end
     * @return The script to send to the browser
     */
    public static ScriptBuffer getRemoteHandleExceptionScript(String batchId, String callId, Throwable ex)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("dwr.engine.remote.handleException", batchId, callId, ex);
        return script;
    }

    /**
     * Call dwr.engine.remote.handleNewScriptSession() in the browser
     * @param newSessionId The new script session id for the browser to reuse
     * @return The script to send to the browser
     */
    public static ScriptBuffer getRemoteHandleNewScriptSessionScript(String newSessionId)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("dwr.engine.remote.handleNewScriptSession", newSessionId);
        return script;
    }

    /**
     * Call dwr.engine.remote.handleNewWindowName() in the browser
     * @param windowName The new window name for the page
     * @return The script to send to the browser
     */
    public static ScriptBuffer getRemoteHandleNewWindowNameScript(String windowName)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("dwr.engine.remote.handleNewWindowName", windowName);
        return script;
    }

    /**
     * Call dwr.engine.remote.handleServerException() in the browser
     * @param batchId The identifier of the batch that we are handling a response for
     * @param ex The exception from which we make a reply
     * @return The script to send to the browser
     */
    public static String getRemoteHandleBatchExceptionScript(String batchId, Exception ex)
    {
        StringBuffer reply = new StringBuffer();

        String output = JavascriptUtil.escapeJavaScript(ex.getMessage());
        String params = "{ name:'" + ex.getClass().getName() + "', message:'" + output + "' }";
        if (batchId != null)
        {
            params += ", '" + batchId + "'";
        }

        reply.append(ProtocolConstants.SCRIPT_CALL_REPLY).append("\r\n");
        reply.append("if (window.dwr) dwr.engine.remote.handleBatchException(").append(params).append(");\r\n");
        reply.append("else if (window.parent.dwr) window.parent.dwr.engine.remote.handleBatchException(").append(params).append(");\r\n");

        return reply.toString();
    }

    /**
     * Call dwr.engine.remote.executeFunction() in the browser
     * @param id The registered function name
     * @param params The data to pass to the function
     * @return The script to send to the browser
     */
    public static ScriptBuffer getRemoteExecuteFunctionScript(String id, Object[] params)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("dwr.engine.remote.handleFunctionCall", id, params);
        return script;
    }

    /**
     * Call dwr.engine.remote.executeFunction() in the browser
     * @param id The registered function name
     * @param params The data to pass to the function
     * @return The script to send to the browser
     */
    public static ScriptBuffer getRemoteExecuteObjectScript(String id, String methodName, Object[] params)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("dwr.engine.remote.handleObjectCall", id, methodName, params);
        return script;
    }

    /**
     * Call dwr.engine.remote.executeFunction() in the browser
     * @param id The registered function name
     * @param propertyName The name of the property to alter on the client object
     * @param data The new value for the client object property
     * @return The script to send to the browser
     */
    public static ScriptBuffer getRemoteSetObjectScript(String id, String propertyName, Object data)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("dwr.engine.remote.handleSetCall", id, propertyName, data);
        return script;
    }

    /**
     * Call dwr.engine.remote.closeFunction() in the browser
     * @param id The registered function name
     * @return The script to send to the browser
     */
    public static ScriptBuffer getRemoteCloseFunctionScript(String id)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("dwr.engine.remote.handleFunctionClose", id);
        return script;
    }

    /**
     * Call dwr.engine.remote.pollCometDisabled() in the browser
     * @param batchId The identifier of the batch that we are handling a response for
     * @return The script to send to the browser
     */
    public static String getRemotePollCometDisabledScript(String batchId)
    {
        StringBuffer reply = new StringBuffer();

        String params = "{ name:'dwr.engine.pollAndCometDisabled', message:'Polling and Comet are disabled. See the server logs.' }";
        if (batchId != null)
        {
            params += ", '" + batchId + "'";
        }

        reply.append(ProtocolConstants.SCRIPT_CALL_REPLY).append("\r\n");
        reply.append("if (window.dwr) dwr.engine.remote.pollCometDisabled(").append(params).append(");\r\n");
        reply.append("else if (window.parent.dwr) window.parent.dwr.engine.remote.pollCometDisabled(").append(params).append(");\r\n");

        return reply.toString();
    }

    /**
     * Take an XML string, and convert it into some Javascript that when
     * executed will return a DOM object that represents the same XML object
     * @param xml The XML string to convert
     * @return The Javascript
     */
    public static String xmlStringToJavascriptDomElement(String xml)
    {
        String xmlout = JavascriptUtil.escapeJavaScript(xml);
        return "dwr.engine.serialize.toDomElement(\"" + xmlout + "\")";
    }

    /**
     * Take an XML string, and convert it into some Javascript that when
     * executed will return a DOM object that represents the same XML object
     * @param xml The XML string to convert
     * @return The Javascript
     */
    public static String xmlStringToJavascriptDomDocument(String xml)
    {
        String xmlout = JavascriptUtil.escapeJavaScript(xml);
        return "dwr.engine.serialize.toDomDocument(\"" + xmlout + "\")";
    }

    /**
     * Get a string which will initialize a dwr.engine object
     * @return A dwr.engine init script
     */
    public static String getEngineInitScript()
    {
        return "// Provide a default path to dwr.engine\n" +
               "if (typeof this['dwr'] == 'undefined') this.dwr = {};\n" +
               "if (typeof dwr['engine'] == 'undefined') dwr.engine = {};\n" +
               "if (typeof dwr.engine['_mappedClasses'] == 'undefined') dwr.engine._mappedClasses = {};\n" +
               '\n';
    }

    /**
     * The DefaultRemoter needs to know the name of the execute function
     * @return The execute function name
     */
    public static String getExecuteFunctionName()
    {
        return "dwr.engine._execute";
    }

    /**
     * A script to send at the beginning of an iframe response
     * @param batchId The id of the current batch
     * @param useWindowParent Will the exec happen from a child iframe which is
     * the case for normal iframe based calls, or from the main window, which is
     * the case for iframe streamed polling.
     * @return A script to init the environment
     */
    public static String remoteBeginIFrameResponse(String batchId, boolean useWindowParent)
    {
        String script = "dwr.engine.transport.iframe.remote.beginIFrameResponse(this.frameElement"+(batchId == null?"":", '" + batchId+"'") + ");";
        if (useWindowParent)
        {
            script = addWindowParent(script);
        }

        return script;
    }

    /**
     * A script to send at the end of an iframe response
     * @param batchId The id of the current batch
     * @param useWindowParent Will the exec happen from a child iframe which is
     * the case for normal iframe based calls, or from the main window, which is
     * the case for iframe streamed polling.
     * @return A script to tidy up the environment
     */
    public static String remoteEndIFrameResponse(String batchId, boolean useWindowParent)
    {
        String script = "dwr.engine.transport.iframe.remote.endIFrameResponse("+(batchId == null?"":"'" + batchId+"'")+");";
        if (useWindowParent)
        {
            script = addWindowParent(script);
        }

        return script;
    }

    /**
     * Prepare a script for execution in an iframe environment
     * @param script The script to modify
     * @return The modified script
     */
    public static String remoteEval(String script)
    {
        String script2 = "dwr.engine._eval(\"" + JavascriptUtil.escapeJavaScript(script) + "\");";
        return addWindowParent(script2);
    }

    /**
     * Evade the 2 connection limit by sending scripts to the wrong window and
     * having that use window.name to push it to the right window
     * @param original The script that we wish to be proxied
     * @param windowName The window to which we wish the window to be proxied
     * @return A script to send to a different window to cause proxying
     */
    public static ScriptBuffer createForeignWindowProxy(String windowName, ScriptBuffer original)
    {
        String proxy = JavascriptUtil.escapeJavaScript(original.toString());

        ScriptBuffer reply = new ScriptBuffer();
        reply.appendCall("dwr.engine.remote.handleForeign", windowName, proxy);
        reply.appendData(proxy);
        return reply;
    }

    /**
     * A Utility to add a try/catch block to get rid of the infamous IE
     * "Can't execute code from a freed script" errors
     * @param script The script to wrap in a try/catch
     * @return The wrapped script
     */
    private static String addWindowParent(String script)
    {
        return "try { window.parent." + script + " } catch(ex) { if (!(ex.number && ex.number == -2146823277)) { throw ex; }}";
    }
}
