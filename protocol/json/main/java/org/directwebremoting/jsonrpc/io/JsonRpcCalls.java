package org.directwebremoting.jsonrpc.io;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.extend.Calls;
import org.directwebremoting.io.StringWrapper;

/**
 * An extension to the {@link Calls} object to hold JsonRpc version information.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonRpcCalls extends Calls
{
    /**
     * A version of {@link #getBatchId()} that returns a StringWrapper so we can
     * return a verbatim string because id is really a variant type in
     * Javascript.
     * @see #getBatchId()
     */
    public StringWrapper getId()
    {
        return new StringWrapper(getBatchId());
    }

    /**
     * @return The JsonRpc version string as defined in the jsonrpc attribute
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * @see #getVersion()
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * If parsing has failed and we should not continue, then we might want to
     * carry on with the parse to get information like the id that is needed
     * for the response, but remembering the error messages
     */
    public void addParseError(String message)
    {
        parseErrors.add(message);
    }

    /**
     * Did parsing complete without errors?
     * @see #addParseError(String)
     */
    public boolean isParseErrorClean()
    {
        return parseErrors.isEmpty();
    }

    /**
     * Get a summary of the parse errors.
     * If there is more than one parse error, then return them all concatenated
     * by a ', '
     */
    public String getParseErrors()
    {
        StringBuilder buffer = new StringBuilder();
        for (String message : parseErrors)
        {
            if (buffer.length() != 0)
            {
                buffer.append(", ");
            }
            buffer.append(message);
        }
        return buffer.toString();
    }

    /**
     * @see #getVersion()
     */
    private String version = "2.0";

    /**
     * @see #addParseError(String)
     * @see #isParseErrorClean()
     */
    private final List<String> parseErrors = new ArrayList<String>();
}
