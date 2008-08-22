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
import java.util.LinkedList;

import org.directwebremoting.json.parse.JsonDecoder;
import org.directwebremoting.json.parse.JsonParseException;

/**
 * A stateful implementation of {@link JsonDecoder} where we track the stack
 * of objects and allow a subclass to have a simpler set of things to do
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractJsonDecoder<T> implements JsonDecoder<T>
{
    /**
     * Create an object to be returned as the root by {@link #getRoot()}.
     * @throws JsonParseException If the root object was supposed to be an array
     */
    protected abstract T createObjectForRoot() throws JsonParseException;

    /**
     * We need to create an object for addition to an array.
     * Working out what type of object to create might be very hard.
     * This method does not need to actually add the object to the array. That
     * will be done by {@link #addMemberToArray(Object, Object)}
     */
    protected abstract Object createObjectForAddingToArray(Object parent) throws JsonParseException;

    /**
     * Create an object for a specific property of an object.
     * It is hoped that some type information can be used to allow creation of
     * the right type of object
     * This method does not need to actually add the object to the object. That
     * will be done by {@link #addMemberToObject(Object, String, Object)}
     */
    protected abstract Object createObjectForAddingToObject(Object parent, String propertyName) throws JsonParseException;

    /**
     * Create an array to be returned as the root by {@link #getRoot()}
     * @throws JsonParseException If the root object was supposed to be an object
     */
    protected abstract T createArrayForRoot(Object parent) throws JsonParseException;

    /**
     * We need to create an array for addition to an array.
     * Working out what type of array to create might be very hard.
     * This method does not need to actually add the array to the array. That
     * will be done by {@link #addMemberToArray(Object, Object)}
     */
    protected abstract Object createArrayForAddingToArray(Object parent) throws JsonParseException;

    /**
     * Create an array for a specific property of an object.
     * It is hoped that some type information can be used to allow creation of
     * the right type of array
     * This method does not need to actually add the array to the object. That
     * will be done by {@link #addMemberToObject(Object, String, Object)}
     */
    protected abstract Object createArrayForAddingToObject(Object parent, String propertyName) throws JsonParseException;

    /**
     * Add the given <code>value</code> to the <code>propertyName</code> property
     * of the <code>parent</code> object.
     * @throws JsonParseException If there are any errors in adding the value
     */
    protected abstract void addMemberToObject(Object parent, String propertyName, Object member) throws JsonParseException;

    /**
     * Add the given <code>value</code> to the <code>parent</code> array.
     * @throws JsonParseException If there are any errors in adding the value
     */
    protected abstract void addMemberToArray(Object parent, Object member) throws JsonParseException;

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#getRoot()
     */
    @SuppressWarnings("unchecked")
    public T getRoot() throws JsonParseException
    {
        return (T) last;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#beginObject()
     */
    public void beginObject() throws JsonParseException
    {
        Object obj = null;

        if (modes.size() == 0)
        {
            obj = createObjectForRoot();
        }
        else
        {
            switch (modes.getLast())
            {
            case Object:
                throw new RuntimeException("Not expecting an object nested directly in another object");

            case Member:
                String propertyName = propertyNames.getLast();
                obj = createObjectForAddingToObject(stack.getLast(), propertyName);
                break;

            case Array:
                obj = createObjectForAddingToArray(stack.getLast());
                break;
            }
        }

        stack.addLast(obj);
        modes.addLast(Mode.Object);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#endObject()
     */
    public void endObject() throws JsonParseException
    {
        this.last = stack.removeLast();
        modes.removeLast();

        // Don't try to add the top level object to its parent
        if (stack.size() > 0)
        {
            add(last);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginMember()
     */
    public void beginMember() throws JsonParseException
    {
        modes.addLast(Mode.Member);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endMember()
     */
    public void endMember() throws JsonParseException
    {
        modes.removeLast();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#beginArray()
     */
    public void beginArray() throws JsonParseException
    {
        Object obj = null;

        if (modes.size() == 0)
        {
            obj = createObjectForRoot();
        }
        else
        {
            switch (modes.getLast())
            {
            case Object:
                throw new RuntimeException("Not expecting an object nested directly in another object");

            case Member:
                String propertyName = propertyNames.getLast();
                obj = createArrayForAddingToObject(stack.getLast(), propertyName);
                break;

            case Array:
                obj = createArrayForAddingToArray(stack.getLast());
                break;
            }
        }

        stack.addLast(obj);
        modes.addLast(Mode.Array);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#endArray()
     */
    public void endArray() throws JsonParseException
    {
        last = stack.removeLast();
        modes.removeLast();

        // Don't add the top level object to its parent
        if (stack.size() > 0)
        {
            add(last);
        }
    }

    /**
     * Add the add methods (with the slight exception of {@link #addString}) do
     * basically the same thing - add themselves to the current object or
     * array.
     * Strings are slightly different because they are valid as object properly
     * names too.
     */
    public void add(Object value) throws JsonParseException
    {
        Mode mode = modes.getLast();
        switch (mode)
        {
        case Object:
            propertyNames.addLast((String) value);
            break;

        case Member:
            String propertyName = propertyNames.removeLast();
            addMemberToObject(stack.getLast(), propertyName, value);
            break;

        case Array:
            addMemberToArray(stack.getLast(), value);
            break;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addString(java.lang.String)
     */
    public void addString(String value) throws JsonParseException
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNumber(java.math.BigDecimal)
     */
    public void addNumber(BigDecimal value) throws JsonParseException
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addDouble(double)
     */
    public void addDouble(double value) throws JsonParseException
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addLong(long)
     */
    public void addLong(long value) throws JsonParseException
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addInt(int)
     */
    public void addInt(int value) throws JsonParseException
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addBoolean(boolean)
     */
    public void addBoolean(boolean value) throws JsonParseException
    {
        add(value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNull()
     */
    public void addNull() throws JsonParseException
    {
        add(null);
    }

    /**
     * What are we parsing right now?
     */
    protected enum Mode
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
     * If we are adding members to an object, we need to remember the name
     * of the current property
     */
    private final LinkedList<String> propertyNames = new LinkedList<String>();

    /**
     * Once we have finished doing something, we need to work out what we were
     * doing previously
     */
    private final LinkedList<Mode> modes = new LinkedList<Mode>();

    /**
     * The stack of objects that we have created. Sometimes (particularly with
     * objects there are a number of states within an object, so we need to
     * maintain a separate mode stack.
     */
    private final LinkedList<Object> stack = new LinkedList<Object>();

    /**
     * {@link #getRoot()} is called after we have removed the last object on the
     * stack, so we need to remember what it was.
     */
    private Object last = null;
}
