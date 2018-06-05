package org.directwebremoting.io;

import java.util.Map;

/**
 * DwrConvertedException are automatically exported by DWR along with the
 * message and (optionally) a map of data. It is the developers job to ensure
 * that all the objects in the data map are marked for conversion.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrConvertedException extends RuntimeException
{
    /**
     * @param message The message to pass to the exceptionHandler in JavaScript
     */
    public DwrConvertedException(String message)
    {
        super(message);
    }

    /**
     * @param message The message to pass to the exceptionHandler in JavaScript
     */
    public DwrConvertedException(String message, Map<?, ?> data)
    {
        super(message);
        this.data = data;
    }

    /**
     * @return the data
     */
    public Map<?, ?> getData()
    {
        return data;
    }

    private Map<?, ?> data;
}
