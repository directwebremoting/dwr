package org.directwebremoting.json.parse.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.directwebremoting.extend.Property;
import org.directwebremoting.json.parse.JsonDecoder;
import org.directwebremoting.json.parse.JsonParseException;

/**
 * This class is incomplete
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
class ConverterJsonDecoder implements JsonDecoder
{
    /**
     * We need to know what we are decoding into
     */
    public ConverterJsonDecoder(Property property)
    {
        destinations.addLast(property);
    }

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
        //Property property = destinations.getLast();
        //Class<?> type = property.getPropertyType();

        stack.addLast(new HashMap<String, Object>());
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
        stack.addLast(new ArrayList<Object>());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.StatefulJsonDecoder#endArray()
     */
    public void endArray(String propertyName) throws JsonParseException
    {
        last = stack.removeLast();

        // Don't add the top level object to its parent
        if (stack.size() > 0)
        {
            add(propertyName, last);
        }
    }

    /**
     * Add the add methods (with the slight exception of {@link #addString}) do
     * basically the same thing - add themselves to the current object or
     * array.
     * Strings are slightly different because they are valid as object properly
     * names too.
     */
    public void add(String propertyName, Object value)
    {
        if (propertyName == null)
        {
            @SuppressWarnings("unchecked")
            List<Object> array = (List<Object>) stack.getLast();
            array.add(value);
        }
        else
        {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) stack.getLast();
            map.put(propertyName, value);
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
        Object value = StatefulJsonDecoder.realizeNumber(intPart, floatPart, expPart);
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
     * The stack of objects that we have created. Sometimes (particularly with
     * objects there are a number of states within an object, so we need to
     * maintain a separate mode stack.
     */
    protected final LinkedList<Object> stack = new LinkedList<Object>();

    /**
     * The stack of types that we are in the process of creating
     */
    protected final LinkedList<Property> destinations = new LinkedList<Property>();

    /**
     * {@link #getRoot()} is called after we have removed the last object on the
     * stack, so we need to remember what it was.
     */
    protected Object last = null;
}
