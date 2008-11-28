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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.extend.Replies;
import org.directwebremoting.extend.Reply;
import org.directwebremoting.json.JsonUtil;
import org.directwebremoting.json.parse.JsonParseException;
import org.directwebremoting.json.parse.JsonParser;
import org.directwebremoting.json.parse.JsonParserFactory;
import org.directwebremoting.jsonrpc.io.JsonRpcCallException;
import org.directwebremoting.jsonrpc.io.JsonRpcCalls;
import org.directwebremoting.jsonrpc.io.JsonRpcCallsJsonDecoder;
import org.directwebremoting.jsonrpc.io.JsonRpcError;
import org.directwebremoting.jsonrpc.io.JsonRpcResponse;
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

        JsonRpcCalls calls = null;

        try
        {
            // TODO: We do not support JSON-RPC-GET. Is this legal?
            // I'm of the opinion that allow any kind of RPC over GET without an
            // explicit @idempotent marker is probably against the HTTP spec
            // Plus there are additional security issues with GET requests
            // So I'm not rushing to fix this error
            Reader in = request.getReader();
            JsonParser parser = JsonParserFactory.get();
            calls = (JsonRpcCalls) parser.parse(in, new JsonRpcCallsJsonDecoder(converterManager, creatorManager));

            if (calls.getCallCount() != 1)
            {
                JsonRpcError error = new JsonRpcError(calls, "Non unique call", ERROR_CODE_INTERNAL, null);
                writeResponse(error, response, SC_INTERNAL_SERVER_ERROR);
                return;
            }

            if (!calls.isParseErrorClean())
            {
                JsonRpcError error = new JsonRpcError(calls, calls.getParseErrors(), ERROR_CODE_PARSE, null);
                writeResponse(error, response, SC_INTERNAL_SERVER_ERROR);
                return;
            }

            // Check the methods are accessible
            for (Call c : calls)
            {
                Creator creator = creatorManager.getCreator(c.getScriptName(), true);
                accessControl.assertExecutionIsPossible(creator, c.getScriptName(), c.getMethod());
            }

            Replies replies = remoter.execute(calls);

            Reply reply = replies.getReply(0);

            // The existence of a throwable indicates that something went wrong
            if (reply.getThrowable() != null)
            {
                Throwable ex = reply.getThrowable();
                JsonRpcError error = new JsonRpcError(calls, ex.getMessage(), ERROR_CODE_SERVER, null);
                writeResponse(error, response, SC_INTERNAL_SERVER_ERROR);
                return;
            }

            JsonRpcResponse answer = new JsonRpcResponse(calls.getVersion(), calls.getId(), reply.getReply());
            writeResponse(answer, response, HttpServletResponse.SC_OK);
        }
        catch (JsonRpcCallException ex)
        {
            writeResponse(new JsonRpcError(ex), response, ex.getHttpStatusCode());
            return;
        }
        catch (JsonParseException ex)
        {
            JsonRpcError error = new JsonRpcError("2.0", null, ex.getMessage(), ERROR_CODE_PARSE, null);
            writeResponse(error, response, SC_INTERNAL_SERVER_ERROR);
            return;
        }
        catch (SecurityException ex)
        {
            JsonRpcError error = new JsonRpcError(calls, ex.getMessage(), ERROR_CODE_NO_METHOD, null);
            writeResponse(error, response, SC_NOT_FOUND);
        }
        catch (IOException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            log.warn("Unexpected error:", ex);
            JsonRpcError error = new JsonRpcError(calls, ex.getMessage(), ERROR_CODE_SERVER, null);
            writeResponse(error, response, SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create an output data packet that a JSON-RPC client can understand
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
