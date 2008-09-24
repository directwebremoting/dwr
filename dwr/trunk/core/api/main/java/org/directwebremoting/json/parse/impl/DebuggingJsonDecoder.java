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
package org.directwebremoting.json.parse.impl;

import java.math.BigDecimal;

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
public class DebuggingJsonDecoder<T> implements JsonDecoder<T>
{
    public DebuggingJsonDecoder(JsonDecoder<T> proxy)
    {
        this.proxy = proxy;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#getRoot()
     */
    public T getRoot() throws JsonParseException
    {
        return proxy.getRoot();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginObject()
     */
    public void beginObject() throws JsonParseException
    {
        if (indent.length() == 0)
        {
            log.info("--------");
        }
        log.info(indent + "{");
        increaseIndent();

        proxy.beginObject();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endObject()
     */
    public void endObject() throws JsonParseException
    {
        proxy.endObject();

        decreaseIndent();
        log.info(indent + "}");

        if (indent.length() == 0)
        {
            log.info("--------");
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginArray()
     */
    public void beginArray() throws JsonParseException
    {
        log.info(indent + "[");
        increaseIndent();

        proxy.beginArray();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endArray()
     */
    public void endArray() throws JsonParseException
    {
        proxy.endArray();

        decreaseIndent();
        log.info(indent + "]");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginMember()
     */
    public void beginMember(String name) throws JsonParseException
    {
        log.info(indent + ":");
        proxy.beginMember(name);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endMember()
     */
    public void endMember(String name) throws JsonParseException
    {
        proxy.endMember(name);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addString(java.lang.String)
     */
    public void addString(String value) throws JsonParseException
    {
        log.info(indent + value);
        proxy.addString(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNumber(java.math.BigDecimal)
     */
    public void addNumber(BigDecimal value) throws JsonParseException
    {
        log.info(indent + value);
        proxy.addNumber(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addDouble(double)
     */
    public void addDouble(double value) throws JsonParseException
    {
        log.info(indent + value);
        proxy.addDouble(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addLong(long)
     */
    public void addLong(long value) throws JsonParseException
    {
        log.info(indent + value);
        proxy.addLong(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addInt(int)
     */
    public void addInt(int value) throws JsonParseException
    {
        log.info(indent + value);
        proxy.addInt(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addBoolean(boolean)
     */
    public void addBoolean(boolean value) throws JsonParseException
    {
        log.info(indent + value);
        proxy.addBoolean(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNull()
     */
    public void addNull() throws JsonParseException
    {
        log.info(indent + "null");
        proxy.addNull();
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
    private final JsonDecoder<T> proxy;

    /**
     * How many levels deep are we?
     */
    private String indent = "";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DebuggingJsonDecoder.class);
}
