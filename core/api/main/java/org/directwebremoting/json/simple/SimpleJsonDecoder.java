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
package org.directwebremoting.json.simple;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.directwebremoting.json.parse.JsonDecoder;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SimpleJsonDecoder implements JsonDecoder<Map<String, Object>>
{
    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#getRoot()
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getRoot()
    {
        return (Map<String, Object>) last;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginObject()
     */
    public void beginObject()
    {
        stack.addLast(new HashMap<String, Object>());
        modes.addLast(Mode.Object);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endObject()
     */
    public void endObject()
    {
        last = stack.removeLast();
        modes.removeLast();

        // Don't add the top level object to its parent
        if (stack.size() > 0)
        {
            add(last);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginMember()
     */
    public void beginMember()
    {
        modes.addLast(Mode.Member);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endMember()
     */
    public void endMember()
    {
        modes.removeLast();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginArray()
     */
    public void beginArray()
    {
        stack.addLast(new ArrayList<Object>());
        modes.addLast(Mode.Array);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endArray()
     */
    public void endArray()
    {
        last = stack.removeLast();
        modes.removeLast();

        add(last);
    }

    /**
     * Add the add methods (with the slight exception of {@link #addString}) do
     * basically the same thing - add themselves to the current object or
     * array.
     * Strings are slightly different because they are valid as object properly
     * names too.
     */
    @SuppressWarnings("unchecked")
    public void add(Object value)
    {
        Mode mode = modes.getLast();
        switch (mode)
        {
        case Object:
            propertyNames.addLast((String) value);
            break;

        case Member:
            Map<String, Object> object = (Map<String, Object>) stack.getLast();
            String propertyName = propertyNames.removeLast();
            object.put(propertyName, value);
            break;

        case Array:
            List<Object> array = (List<Object>) stack.getLast();
            array.add(value);
            break;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addString(java.lang.String)
     */
    public void addString(String value)
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNumber(java.math.BigDecimal)
     */
    public void addNumber(BigDecimal value)
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addDouble(double)
     */
    public void addDouble(double value)
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addLong(long)
     */
    public void addLong(long value)
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addInt(int)
     */
    public void addInt(int value)
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addBoolean(boolean)
     */
    public void addBoolean(boolean value)
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNull()
     */
    public void addNull()
    {
        add(null);
    }

    /**
     * What are we parsing right now?
     */
    private enum Mode
    {
        /**
         * We've just had a { so we're expecting a property name via
         * {@link JsonDecoder#addString(String)}.
         * Alternatively we might just have had a , inside a { }
         */
        Object,

        /**
         * We've just had a property name, so we're expecting a value for that
         * property name. Only valid inside objects
         */
        Member,

        /**
         * We're inside [] so all values that we add are array members.
         */
        Array
    }

    /**
     * The stack of objects that we have created. Sometimes (particularly with
     * objects there are a number of states within an object, so we need to
     * maintain a separate mode stack, see {@link #modes}.
     */
    private final LinkedList<Object> stack = new LinkedList<Object>();

    /**
     * Once we have finished doing something, we need to work out what we were
     * doing previously
     */
    private final LinkedList<Mode> modes = new LinkedList<Mode>();

    /**
     * If we are adding members to an object, we need to remember the name
     * of the current property
     */
    private final LinkedList<String> propertyNames = new LinkedList<String>();

    /**
     * {@link #getRoot()} is called after we have removed the last object on the
     * stack, so we need to remember what it was.
     */
    private Object last = null;
}
