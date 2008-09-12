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
package org.directwebremoting.jsonrpc;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.extend.Replies;
import org.directwebremoting.extend.Reply;
import org.directwebremoting.json.JsonUtil;
import org.directwebremoting.json.parse.JsonParseException;
import org.directwebremoting.jsonrpc.io.JsonRpcError;
import org.directwebremoting.jsonrpc.io.JsonRpcRequest;
import org.directwebremoting.util.MimeConstants;

import static javax.servlet.http.HttpServletResponse.*;

import static org.directwebremoting.jsonrpc.JsonRpcConstants.*;

/**
 * A Handler for JSON-RPC calls.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonRpcCallHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        if (!jsonRpcEnabled)
        {
            log.warn("JSON-RPC request denied. To enable JSON mode add an init-param of jsonRpcEnabled=true to web.xml");
            throw new SecurityException("JSON interface disabled");
        }

        Calls calls = new Calls();
        JsonRpcRequest jsonRpc;

        try
        {
            // TODO: We do not support JSON-RPC-GET. Is this legal?
            // I'm of the opinion that allow any kind of RPC over GET is against the
            // HTTP spec so I'm not rushing to fix this error
            Reader in = request.getReader();
            jsonRpc = JsonUtil.toReflectedTypes(JsonRpcRequest.class, in);
        }
        catch (JsonParseException ex)
        {
            JsonRpcError error = new JsonRpcError("2.0", null, ex.getMessage(), ERROR_CODE_PARSE, null);
            writeResponse(error, response, SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Fill out the Calls structure
        Call call = new Call();
        try
        {
            // JSON-RPC does not support batching
            calls.addCall(call);
            call.setCallId(jsonRpc.getId());

            String exec = jsonRpc.getMethod();
            String[] parts = exec.split(".");
            if (parts.length != 2)
            {
                log.warn("Got method='" + exec + "', but this does not split into 2 parts (parts=" + parts + ").");
                JsonRpcError error = new JsonRpcError(jsonRpc, "Method parameter not valid", ERROR_CODE_INVALID, null);
                writeResponse(error, response, SC_BAD_REQUEST);
                return;
            }

            call.setScriptName(parts[0]);
            call.setMethodName(parts[1]);

            Creator creator = creatorManager.getCreator(parts[0], false);
            if (creator == null)
            {
                log.warn("No creator found: " + parts[0]);
                JsonRpcError error = new JsonRpcError(jsonRpc, "Object not valid", ERROR_CODE_INVALID, null);
                writeResponse(error, response, SC_BAD_REQUEST);
                return;
            }

            Class<?> type = creator.getType();

            // Get the types of the parameters
            List<Object> params = jsonRpc.getParams();
            List<Class<?>> paramTypes = new ArrayList<Class<?>>();
            for (Object param : params)
            {
                paramTypes.add(param.getClass());
            }
            Class<?>[] typeArray = paramTypes.toArray(new Class[paramTypes.size()]);

            Method method = type.getMethod(call.getMethodName(), typeArray);
            call.setMethod(method);

            call.setParameters(params.toArray());
        }
        catch (SecurityException ex)
        {
            log.warn("Method not allowed: " + jsonRpc.getMethod() + ".", ex);
            JsonRpcError error = new JsonRpcError(jsonRpc, "Method not allowed", ERROR_CODE_INVALID, null);
            writeResponse(error, response, SC_BAD_REQUEST);
            return;
        }
        catch (NoSuchMethodException ex)
        {
            log.warn("Method not found: " + jsonRpc.getMethod() + ".", ex);
            JsonRpcError error = new JsonRpcError(jsonRpc, "Method not found", ERROR_CODE_INVALID, null);
            writeResponse(error, response, SC_BAD_REQUEST);
            return;
        }

        // Check the methods are accessible
        try
        {
            for (Call c : calls)
            {
                Creator creator = creatorManager.getCreator(c.getScriptName(), true);
                accessControl.assertExecutionIsPossible(creator, c.getScriptName(), c.getMethod());
            }
        }
        catch (SecurityException ex)
        {
            JsonRpcError error = new JsonRpcError(jsonRpc, ex.getMessage(), ERROR_CODE_NO_METHOD, null);
            writeResponse(error, response, SC_NOT_FOUND);
        }

        try
        {
            Replies replies = remoter.execute(calls);
            if (replies.getReplyCount() != 1)
            {
                JsonRpcError error = new JsonRpcError(jsonRpc, "Multiple replies", ERROR_CODE_INTERNAL, null);
                writeResponse(error, response, SC_INTERNAL_SERVER_ERROR);
            }

            Reply reply = replies.getReply(0);

            // The existence of a throwable indicates that something went wrong
            if (reply.getThrowable() != null)
            {
                Throwable ex = reply.getThrowable();
                JsonRpcError error = new JsonRpcError(jsonRpc, ex.getMessage(), ERROR_CODE_SERVER, null);
                writeResponse(error, response, SC_INTERNAL_SERVER_ERROR);
                return;
            }

            writeResponse(reply.getReply(), response, HttpServletResponse.SC_OK);
        }
        catch (IOException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            JsonRpcError error = new JsonRpcError(jsonRpc, ex.getMessage(), ERROR_CODE_SERVER, null);
            writeResponse(error, response, SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     * @param data
     * @param response
     * @throws IOException
     */
    protected void writeResponse(Object data, HttpServletResponse response, int httpStatus) throws IOException
    {
        // Get the output stream and setup the mime type
        response.setContentType(MimeConstants.MIME_JSON);
        response.setStatus(httpStatus);
        JsonUtil.toJson(data, response.getWriter());
    }

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param converterManager The new DefaultConverterManager
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * Are we allowing remote hosts to contact us using JSON?
     */
    public void setJsonRpcEnabled(boolean jsonRpcEnabled)
    {
        this.jsonRpcEnabled = jsonRpcEnabled;
    }

    /**
     * Are we allowing remote hosts to contact us using JSON?
     */
    protected boolean jsonRpcEnabled = false;

    /**
     * Setter for the remoter
     * @param remoter The new remoter
     */
    public void setRemoter(Remoter remoter)
    {
        this.remoter = remoter;
    }

    /**
     * The bean to execute remote requests and generate interfaces
     */
    protected Remoter remoter = null;

    /**
     * Accessor for the security manager
     * @param accessControl The accessControl to set.
     */
    public void setAccessControl(AccessControl accessControl)
    {
        this.accessControl = accessControl;
    }

    /**
     * The security manager
     */
    protected AccessControl accessControl = null;

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param creatorManager The new DefaultConverterManager
     */
    public void setCreatorManager(CreatorManager creatorManager)
    {
        this.creatorManager = creatorManager;
    }

    /**
     * How we create new beans
     */
    protected CreatorManager creatorManager = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JsonRpcCallHandler.class);

}
