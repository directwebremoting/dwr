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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.directwebremoting.json.parse.JsonDecoder;
import org.directwebremoting.json.parse.JsonParseException;
import org.directwebremoting.util.LocalUtil;

/**
 * A {@link JsonDecoder} that de-serializes the data into an existing set of
 * objects.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ReflectionJsonDecoder<T> extends AbstractJsonDecoder<T>
{
    private final Class<T> marshallInto;

    /**
     * @param marshallInto
     */
    public ReflectionJsonDecoder(Class<T> marshallInto)
    {
        this.marshallInto = marshallInto;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#createObjectForRoot()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected T createObjectForRoot() throws JsonParseException
    {
        return (T) createType(marshallInto);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#createObject(java.lang.Object)
     */
    @Override
    protected Object createObjectForAddingToArray(Object parent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#createObject(java.lang.Object, java.lang.String)
     */
    @Override
    protected Object createObjectForAddingToObject(Object parent, String propertyName) throws JsonParseException
    {
        Class<?> type = LocalUtil.getPropertyType(parent.getClass(), propertyName);
        return createType(type);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#createArrayForRoot(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected T createArrayForRoot(Object parent) throws JsonParseException
    {
        return (T) createType(marshallInto);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#createArray(java.lang.Object)
     */
    @Override
    protected Object createArrayForAddingToArray(Object parent)
    {
        // This is probably broken, but I'm not sure that there is anything else
        // we can do simply right now
        return new ArrayList<Object>();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#createArray(java.lang.Object, java.lang.String)
     */
    @Override
    protected Object createArrayForAddingToObject(Object parent, String propertyName) throws JsonParseException
    {
        Class<?> type = LocalUtil.getPropertyType(parent.getClass(), propertyName);
        return createType(type);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#addMember(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    protected void addMemberToObject(Object parent, String propertyName, Object member) throws JsonParseException
    {
        try
        {
            LocalUtil.setProperty(parent, propertyName, member);
        }
        catch (SecurityException ex)
        {
            throw new JsonParseException(ex);
        }
        catch (IllegalArgumentException ex)
        {
            throw new JsonParseException(ex);
        }
        catch (NoSuchMethodException ex)
        {
            throw new JsonParseException(ex);
        }
        catch (IllegalAccessException ex)
        {
            throw new JsonParseException(ex);
        }
        catch (InvocationTargetException ex)
        {
            throw new JsonParseException(ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.impl.AbstractJsonDecoder#addMember(java.lang.Object, java.lang.Object)
     */
    @Override
    protected void addMemberToArray(Object parent, Object member)
    {
        @SuppressWarnings("unchecked")
        Collection<Object> col = (Collection<Object>) parent;
        col.add(member);
    }

    /**
     * Create an instance of a given type, wrapping any resulting exceptions in
     * a {@link JsonParseException}
     */
    private Object createType(Class<?> type) throws JsonParseException
    {
        try
        {
            return type.newInstance();
        }
        catch (InstantiationException ex)
        {
            throw new JsonParseException(ex);
        }
        catch (IllegalAccessException ex)
        {
            throw new JsonParseException(ex);
        }
    }
}
