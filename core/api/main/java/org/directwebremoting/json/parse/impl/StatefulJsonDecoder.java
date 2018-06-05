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
public abstract class StatefulJsonDecoder implements JsonDecoder
{
    /**
     * Create a new object.
     * This method should not actually add the object to the <code>parent</code>.
     * That will be done by {@link #addMemberToArray(Object, Object)}
     * @param parent The parent object that this will be added to. This will be
     * null if we are creating the root object
     * @param propertyName The member name if we are adding a property to an
     * object or null if we are adding to an array
     */
    protected abstract Object createObject(Object parent, String propertyName) throws JsonParseException;

    /**
     * Create a new array.
     * This method should not actually add the object to the <code>parent</code>.
     * That will be done by {@link #addMemberToArray(Object, Object)}
     * @param parent The parent object that this will be added to. This will be
     * null if we are creating the root object
     * @param propertyName The member name if we are adding a property to an
     * object or null if we are adding to an array
     */
    protected abstract Object createArray(Object parent, String propertyName) throws JsonParseException;

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
    public Object getRoot() throws JsonParseException
    {
        return last;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#beginObject()
     */
    public void beginObject(String propertyName) throws JsonParseException
    {
        Object obj = createObject(stackPeek(), propertyName);
        stack.addLast(obj);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#endObject()
     */
    public void endObject(String propertyName) throws JsonParseException
    {
        this.last = stack.removeLast();

        // Don't try to add the top level object to its parent
        if (!stack.isEmpty())
        {
            add(propertyName, last);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#beginArray()
     */
    public void beginArray(String propertyName) throws JsonParseException
    {
        Object obj = createArray(stackPeek(), propertyName);
        stack.addLast(obj);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#endArray()
     */
    public void endArray(String propertyName) throws JsonParseException
    {
        this.last = stack.removeLast();

        // Don't add the top level object to its parent
        if (!stack.isEmpty())
        {
            add(propertyName, last);
        }
    }

    /**
     * Add the add methods do basically the same thing - add themselves to the
     * current object or array.
     */
    public void add(String propertyName, Object value) throws JsonParseException
    {
        if (propertyName == null)
        {
            addMemberToArray(stackPeek(), value);
        }
        else
        {
            addMemberToObject(stackPeek(), propertyName, value);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addString(java.lang.String)
     */
    public void addString(String propertyName, String value) throws JsonParseException
    {
        add(propertyName, value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNumber(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void addNumber(String propertyName, String intPart, String floatPart, String expPart) throws JsonParseException
    {
        Object value = realizeNumber(intPart, floatPart, expPart);
        add(propertyName, value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addBoolean(boolean)
     */
    public void addBoolean(String propertyName, boolean value) throws JsonParseException
    {
        add(propertyName, value);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNull()
     */
    public void addNull(String propertyName) throws JsonParseException
    {
        add(propertyName, null);
    }

    /**
     * Peek at the top of the stack without changing it, or return null if there
     * is nothing on the stack.
     */
    protected Object stackPeek()
    {
        if (stack.isEmpty())
        {
            return null;
        }
        else
        {
            return stack.getLast();
        }
    }

    /**
     * The stack of objects that we have created. Sometimes (particularly with
     * objects there are a number of states within an object, so we need to
     * maintain a separate mode stack.
     */
    protected final LinkedList<Object> stack = new LinkedList<Object>();

    /**
     * {@link #getRoot()} is called after we have removed the last object on the
     * stack, so we need to remember what it was.
     */
    protected Object last = null;

    /**
     * Create a {@link BigDecimal}, double, int, or long depending on the input
     * strings.
     * @param intPart A string of [0-9]* representing the integer part of the
     * number.
     * @param floatPart A string of \.[0-9]* representing the floating point part
     * of the number. This INCLUDES the period
     * @param expPart A string of [eE][+-]?[0-9]* representing the integer part
     * of the number. This includes the 'e' or 'E'.
     */
    public static Object realizeNumber(String intPart, String floatPart, String expPart)
    {
        if (expPart != null)
        {
            return new BigDecimal(intPart + floatPart + expPart);
        }
        else if (floatPart != null)
        {
            return Double.parseDouble(intPart + floatPart);
        }
        else
        {
            try
            {
                return Integer.parseInt(intPart);
            }
            catch (NumberFormatException ex)
            {
                try
                {
                    return Long.parseLong(intPart);
                }
                catch (NumberFormatException ex2)
                {
                    return new BigDecimal(intPart);
                }
            }
        }
    }
}
