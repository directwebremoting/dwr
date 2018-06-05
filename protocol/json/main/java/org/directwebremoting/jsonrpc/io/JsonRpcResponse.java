package org.directwebremoting.jsonrpc.io;

import org.directwebremoting.io.StringWrapper;

/**
 * A Container for a JSON-RPC response
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonRpcResponse
{
    /**
     * Create an Error from a request and the data to fulfill the request
     */
    public JsonRpcResponse(String version, StringWrapper id, Object result)
    {
        this.version = version;
        this.id = id;
        this.result = result;
    }

    /**
     * @return A String specifying the version of the JSON-RPC protocol.
     */
    public String getJsonRpc()
    {
        return version;
    }

    /**
     * @see #getJsonRpc()
     */
    private final String version;

    /**
     * @return The data that results from running a JSON-RPC request
     */
    public Object getResult()
    {
        return result;
    }

    /**
     * @see #getResult()
     */
    private final Object result;

    /**
     * A Request identifier that SHOULD be a JSON scalar (String, Number, True,
     * False), but SHOULD normally not be Null [1].
     * If omitted, the Request is a Notification.
     */
    public StringWrapper getId()
    {
        return id;
    }

    /**
     * @see #getId()
     */
    private final StringWrapper id;
}
