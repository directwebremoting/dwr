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
     * Begin wrapper with variable alias to do remote calls on the correct DWR instance.
     * @param instanceId DWR instance id from browser
     * @param useWindowParent should this alias target the same window or the parent window?
     * @return JavaScript snippet to be used by other remote calls
     */
    public static String remoteBeginWrapper(String instanceId, boolean useWindowParent, String documentDomain)
    {
        StringBuilder buf = new StringBuilder();
        if (documentDomain != null && !documentDomain.equals("")) {
            buf.append("document.domain='").append(documentDomain).append("';\r\n");
        }
        if (useWindowParent)
        {
        	// We need to protect from access exceptions f ex when a discarded
        	// iframe receives data and IE6/7 complains about "freed script"
            buf.append("try{\r\n");
            buf.append("if(window.parent.dwr){\r\n");
            buf.append("var dwr=window.parent.dwr._[" + instanceId + "];");
        }
        else
        {
            buf.append("(function(){\r\n");
            buf.append("if(!window.dwr)return;\r\n");
            buf.append("var dwr=window.dwr._[" + instanceId + "];");
        }
        return buf.toString();
    }

    /**
     * End wrapper with variable alias to do remote calls on the correct DWR instance.
     * @param instanceId DWR instance id from browser
     * @param useWindowParent should this alias target the same window or the parent window?
     * @return JavaScript snippet to be used by other remote calls
     */
    public static String remoteEndWrapper(String instanceId, boolean useWindowParent)
    {
        StringBuilder buf = new StringBuilder();
        if (useWindowParent)
        {
            buf.append("dwr.engine.transport.iframe.remote.endChunk(window);\r\n");
            buf.append("}\r\n");
            buf.append("}catch(e){}");
        } else {
            buf.append("})();");
        }
        return buf.toString();
    }

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
     * Call dwr.engine.remote.handleReverseAjax() in the browser
     * @param scriptIndex
     * @param script
     */
    public static String getRemoteHandleReverseAjaxScript(long scriptIndex, String script)
    {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("dwr.engine.remote.handleReverseAjax(").append(scriptIndex).append(",function(){\r\n");
        strbuf.append(script).append("\r\n");
        strbuf.append("});");
        return strbuf.toString();
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
        params += ", " + (batchId != null ? "'" + batchId + "'" : "null");

        reply.append(ProtocolConstants.SCRIPT_CALL_REPLY).append("\r\n");
        reply.append("dwr.engine.remote.handleBatchException(").append(params).append(");");

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

        String params = "{name:'dwr.engine.pollAndCometDisabled',message:'Polling and Comet are disabled. See the server logs.'}";
        if (batchId != null)
        {
            params += ",'" + batchId + "'";
        }

        reply.append(ProtocolConstants.SCRIPT_CALL_REPLY).append("\r\n");
        reply.append("dwr.engine.remote.pollCometDisabled(").append(params).append(");");

        return reply.toString();
    }

    /**
     * Returns the name of the newObject function.
     */
    public static String remoteNewObjectFunction()
    {
        return "dwr.engine.remote.newObject";
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
    public static String getRequireEngineScript()
    {
        return "if (typeof dwr == 'undefined' || dwr.engine == undefined) throw new Error('You must include DWR engine before including this file');\n";
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
        return "dwr.engine.transport.iframe.remote.beginIFrameResponse(this.frameElement"+(batchId == null?"":",'" + batchId+"'") + ");";
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
        return "dwr.engine.transport.iframe.remote.endIFrameResponse("+(batchId == null?"":"'" + batchId+"'")+");";
    }

    /**
     * Prepare a script for execution in an iframe environment
     * (we need to transfer the script to the parent window context before executing so referred globals will be found)
     * @param script The script to modify
     * @return The modified script
     */
    public static String remoteExecute(String script)
    {
        return "dwr.engine._executeScript(\"" + JavascriptUtil.escapeJavaScript(script) + "\");";
    }
}
