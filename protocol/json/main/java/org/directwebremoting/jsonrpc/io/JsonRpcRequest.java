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

import java.util.List;

/**
 * A Container for a JSON-RPC request
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonRpcRequest
{
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
    public void setJsonrpc(String jsonrpc)
    {
        this.jsonrpc = jsonrpc;
    }

    /**
     * @see #getJsonrpc()
     */
    private String jsonrpc;

    /**
     * @return A String containing the name of the procedure to be invoked.
     */
    public String getMethod()
    {
        return method;
    }

    /**
     * @see #getMethod()
     */
    public void setMethod(String method)
    {
        this.method = method;
    }

    /**
     * @see #getMethod()
     */
    private String method;

    /**
     * An Array or Object, that holds the actual parameter values for the
     * invocation of the procedure.
     */
    public List<Object> getParams()
    {
        return params;
    }

    /**
     * @see #getParams()
     */
    public void setParams(List<Object> params)
    {
        this.params = params;
    }

    /**
     * @see #getParams()
     */
    private List<Object> params;

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
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @see #getId()
     */
    private String id;
}
