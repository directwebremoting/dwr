package org.directwebremoting.json.parse.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.json.parse.JsonDecoder;
import org.directwebremoting.json.parse.JsonParseException;

/**
 * A proxy implementation of {@link JsonDecoder} which simply passes the calls
 * on to another JsonDecoder, but outputs debug logging what happened while the
 * parse was happening.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DebuggingJsonDecoder<T> implements JsonDecoder
{
    public DebuggingJsonDecoder(JsonDecoder proxy)
    {
        this.proxy = proxy;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#getRoot()
     */
    public Object getRoot() throws JsonParseException
    {
        return proxy.getRoot();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginObject(java.lang.String)
     */
    public void beginObject(String propertyName) throws JsonParseException
    {
        if (indent.length() == 0)
        {
            log.info("--------");
        }
        log.info(indent + "{");
        increaseIndent();

        proxy.beginObject(propertyName);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endObject(java.lang.String)
     */
    public void endObject(String propertyName) throws JsonParseException
    {
        proxy.endObject(propertyName);

        decreaseIndent();
        log.info(indent + "}");

        if (indent.length() == 0)
        {
            log.info("--------");
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginArray(java.lang.String)
     */
    public void beginArray(String propertyName) throws JsonParseException
    {
        log.info(indent + "[");
        increaseIndent();

        proxy.beginArray(propertyName);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endArray(java.lang.String)
     */
    public void endArray(String propertyName) throws JsonParseException
    {
        proxy.endArray(propertyName);

        decreaseIndent();
        log.info(indent + "]");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addString(java.lang.String, java.lang.String)
     */
    public void addString(String propertyName, String value) throws JsonParseException
    {
        log.info(indent + value);
        proxy.addString(propertyName, value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNumber(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void addNumber(String propertyName, String intPart, String floatPart, String expPart) throws JsonParseException
    {
        String value = intPart;
        if (floatPart != null)
        {
            value += "." + floatPart;
        }
        if (expPart != null)
        {
            value += "." + expPart;
        }
        log.info(indent + value);
        proxy.addNumber(propertyName, intPart, floatPart, expPart);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addBoolean(java.lang.String, boolean)
     */
    public void addBoolean(String propertyName, boolean value) throws JsonParseException
    {
        log.info(indent + value);
        proxy.addBoolean(propertyName, value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNull(java.lang.String)
     */
    public void addNull(String propertyName) throws JsonParseException
    {
        log.info(indent + "null");
        proxy.addNull(propertyName);
    }

    /**
     *
     */
    private void increaseIndent()
    {
        indent = indent + "  ";
    }

    /**
     *
     */
    private void decreaseIndent()
    {
        indent = indent.substring(2);
    }

    /**
     * The real {@link JsonDecoder} to which we proxy
     */
    private final JsonDecoder proxy;

    /**
     * How many levels deep are we?
     */
    private String indent = "";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DebuggingJsonDecoder.class);
}
