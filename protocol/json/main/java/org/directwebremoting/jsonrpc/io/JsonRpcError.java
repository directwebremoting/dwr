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
package org.directwebremoting.jsonrpc.io;


/**
 * A Container for a JSON-RPC request
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonRpcError
{
    /**
     * Create an Error from a request and an exception/error code
     */
    public JsonRpcError(JsonRpcRequest request, Throwable ex, int code, Object data)
    {
        this.jsonrpc = request.getJsonrpc();
        this.id = request.getId();
        this.data = data;
        this.message = ex.getMessage();
        this.code = code;
    }

    /**
     * Create an Error from a request and an exception/error code
     */
    public JsonRpcError(JsonRpcRequest request, String message, int code, Object data)
    {
        this.jsonrpc = request.getJsonrpc();
        this.id = request.getId();
        this.data = data;
        this.message = message;
        this.code = code;
    }

    /**
     * Create an Error from a request and an exception/error code
     */
    public JsonRpcError(String jsonrpc, String id, String message, int code, Object data)
    {
        this.jsonrpc = jsonrpc;
        this.id = id;
        this.data = data;
        this.message = message;
        this.code = code;
    }

    /**
     * @return A String specifying the version of the JSON-RPC protocol.
     */
    public String getJsonrpc()
    {
        return jsonrpc;
    }

    /**
     * @see #getJsonrpc()
     */
    private final String jsonrpc;

    /**
     * A Request identifier that SHOULD be a JSON scalar (String, Number, True,
     * False), but SHOULD normally not be Null [1].
     * If omitted, the Request is a Notification.
     */
    public String getId()
    {
        return id;
    }

    /**
     * @see #getId()
     */
    private final String id;

    /**
     * Additional information, may be omitted. Its contents is entirely defined
     * by the application (e.g. detailed error information, nested errors etc.).
     */
    public Object getData()
    {
        return data;
    }

    /**
     * @see #getData()
     */
    private final Object data;

    /**
     * A Number that indicates the actual error that occurred.
     * This MUST be an integer.
     */
    public int getCode()
    {
        return code;
    }

    /**
     * @see #getCode()
     */
    private final int code;

    /**
     * A String providing a short description of the error.
     * The message SHOULD be limited to a concise single sentence.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @see #getMessage()
     */
    private final String message;
}
