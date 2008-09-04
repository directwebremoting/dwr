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
package org.directwebremoting.extend;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.util.CompareUtil;

/**
 * {@link TypeHintContext} is a way to provide help to the converter in
 * describing what types it should be converting to.
 * Since Java does generics by erasure, the runtime type information isn't
 * available. However static type information is available. So while we can't
 * tell from a Class object what the parameterized types are, we can get the
 * information from the parameters to a method. The issue is that deep down in
 * the guts of a conversion process we have no idea what the method context is.
 * This class provides the means to retrieve this context.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TypeHintContext
{
    /**
     * External setup this object
     * @param converterManager For when we can't work out the parameterized type info
     */
    public TypeHintContext(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
        this.property = null;
        this.parameterNumber = 0;
    }

    /**
     * External setup this object
     * @param converterManager For when we can't work out the parameterized type info
     * @param property The property to annotate
     * @param parameterNumber The number of the parameter to edit (counts from 0)
     */
    public TypeHintContext(ConverterManager converterManager, Property property, int parameterNumber)
    {
        this.converterManager = converterManager;
        this.property = property;
        this.parameterNumber = parameterNumber;
    }

    /**
     * Create a child TypeHintContext based on this one
     * @param newParameterNumber The index of the item between &lt; and &gt;.
     * @return a new TypeHintContext
     */
    public TypeHintContext createChildContext(int newParameterNumber)
    {
        Method method = null;
        Type parameterType = null;
        if (property instanceof MethodProperty)
        {
            method = ((MethodProperty) property).getMethod();

            if (property instanceof NestedProperty)
            {
                parameterType = ((NestedProperty) property).getParameterType();
            }
            else
            {
                Type[] types = method.getGenericParameterTypes();
                if (types != null && types.length > 0)
                {
                    if (parameterNumber >= types.length)
                    {
                        throw new IllegalArgumentException("parameterNumber=" + parameterNumber + " is too big when method=" + method.getName() + " returns genericParameterTypes.length=" + types.length);
                    }

                    parameterType = types[parameterNumber];
                }
            }
        }

        Property childProperty = new NestedProperty(method, parameterType, parameterNumber, newParameterNumber);
        TypeHintContext child = childProperty.createTypeHintContext(converterManager);

        child.parameterNumberTree.addAll(this.parameterNumberTree);
        child.parameterNumberTree.add(newParameterNumber);

        child.parameterTypeTree.addAll(parameterTypeTree);
        child.parameterTypeTree.add(parameterType);

        return child;
    }

    /**
     * Find the parameterized type information for this parameter either using
     * JDK5 introspection or
     * @return The extra type information for this context
     */
    public Class<?> getExtraTypeInfo()
    {
        Class<?> type;

        if (converterManager != null)
        {
            type = converterManager.getExtraTypeInfo(this);
            if (type != null)
            {
                log.debug("Using type info from <signature> " + toString() + " of " + type);
                return type;
            }
        }

        Type parameterType = null;
        if (property instanceof MethodProperty)
        {
            if (property instanceof NestedProperty)
            {
                parameterType = ((NestedProperty) property).getParameterType();
            }
            else
            {
                Method method = null;
                method = ((MethodProperty) property).getMethod();

                Type[] types = method.getGenericParameterTypes();
                if (types != null && types.length > 0)
                {
                    if (parameterNumber >= types.length)
                    {
                        throw new IllegalArgumentException("parameterNumber=" + parameterNumber + " is too big when method=" + method.getName() + " returns genericParameterTypes.length=" + types.length);
                    }

                    parameterType = types[parameterNumber];
                }
            }
        }

        if (parameterType instanceof ParameterizedType)
        {
            ParameterizedType ptype = (ParameterizedType) parameterType;
            Type rawType = ptype.getRawType();

            if (rawType instanceof Class)
            {
                type = (Class<?>) rawType;
                log.debug("Using type info from JDK5 ParameterizedType of " + type.getName() + " for " + toString());
                return type;
            }
        }
        else if (parameterType instanceof Class)
        {
            type = (Class<?>) parameterType;
            log.debug("Using type info from JDK5 reflection of " + type.getName() + " for " + toString());
            return type;
        }

        log.warn("Missing type info for " + toString() + ". Assuming this is a map with String keys. Please add to <signatures> in dwr.xml");
        return String.class;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hash = 1488;
        hash += (property == null) ? 64 : property.hashCode();
        hash += parameterNumber;
        hash += (parameterNumberTree == null) ? 4648 : parameterNumberTree.hashCode();
        return 1;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        TypeHintContext that = (TypeHintContext) obj;

        if (!CompareUtil.equals(this.property, that.property))
        {
            return false;
        }

        if (this.parameterNumber != that.parameterNumber)
        {
            return false;
        }

        return this.parameterNumberTree.equals(that.parameterNumberTree);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("unused")
    @Override
    public String toString()
    {
        return property.toString();
    }

    /**
     * When we can't work out a parameterized type, then we can ask here
     */
    private final ConverterManager converterManager;

    /**
     * The property that the conversion is happening for
     */
    private final Property property;

    /**
     * The parameter of the method that the conversion is happening for
     */
    private final int parameterNumber;

    /**
     * The list of generic parameters that we have dug into
     */
    private final List<Integer> parameterNumberTree = new ArrayList<Integer>();

    /**
     * The list of generic parameters that we have dug into
     */
    private final List<Type> parameterTypeTree = new ArrayList<Type>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(TypeHintContext.class);
}
