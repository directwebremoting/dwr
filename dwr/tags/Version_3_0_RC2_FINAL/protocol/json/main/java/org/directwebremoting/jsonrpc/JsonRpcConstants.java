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

/**
 * Various constants from the JSON-RPC spec:
 * http://groups.google.com/group/json-rpc/web/json-rpc-1-2-proposal
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface JsonRpcConstants
{
    /**
     * Parse error
     * Invalid JSON. An error occurred on the server while parsing the JSON text
     */
    public static int ERROR_CODE_PARSE = -32700;

    /**
     * Invalid Request: The received JSON not a valid JSON-RPC Request
     */
    public static int ERROR_CODE_INVALID = -32600;

    /**
     * Method not found.
     * The requested remote-procedure does not exist / is not available.
     */
    public static int ERROR_CODE_NO_METHOD = -32601;

    /**
     * Invalid method parameters.
     */
    public static int ERROR_CODE_BAD_PARAMS = -32602;

    /**
     * Internal JSON-RPC error.
     */
    public static int ERROR_CODE_INTERNAL = -32603;

    /**
     * Reserved for implementation-defined server-errors.
     */
    public static int ERROR_CODE_SERVER = -32000;
}
