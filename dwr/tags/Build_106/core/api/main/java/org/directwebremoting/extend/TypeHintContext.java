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
     * @param method The method to annotate
     * @param parameterNumber The number of the parameter to edit (counts from 0)
     */
    public TypeHintContext(ConverterManager converterManager, Method method, int parameterNumber)
    {
        this.converterManager = converterManager;
        this.method = method;
        this.parameterNumber = parameterNumber;

        if (method != null)
        {
            Type[] types = method.getGenericParameterTypes();
            if (types != null && types.length > 0)
            {
                if (parameterNumber >= types.length)
                {
                    throw new IllegalArgumentException("parameterNumber=" + parameterNumber + " is too big when method=" + method.getName() + " returns genericParameterTypes.length=" + types.length);
                }

                this.parameterType = types[parameterNumber];
            }
            else
            {
                this.parameterType = null;
            }
        }
        else
        {
            this.parameterType = null;
        }

        parameterNumberTree = new ArrayList<Integer>();
        parameterTypeTree = new ArrayList<Type>();
    }

    /**
     * Internal setup for nested object
     * @param manager For when we can't work out the parameterized type info
     * @param method The method to annotate
     * @param parameterNumber The number of the parameter to edit (counts from 0)
     * @param parameterType The Type for this context
     */
    private TypeHintContext(ConverterManager manager, Method method, int parameterNumber, Type parameterType)
    {
        this.converterManager = manager;
        this.method = method;
        this.parameterNumber = parameterNumber;
        this.parameterType = parameterType;

        parameterNumberTree = new ArrayList<Integer>();
        parameterTypeTree = new ArrayList<Type>();
    }

    /**
     * Create a child TypeHintContext based on this one
     * @param newParameterNumber The index of the item between &lt; and &gt;.
     * @return a new TypeHintContext
     */
    public TypeHintContext createChildContext(int newParameterNumber)
    {
        Type childType = null;

        if (parameterType instanceof ParameterizedType)
        {
            ParameterizedType ptype = (ParameterizedType) parameterType;
            Type[] actualTypeArguments = ptype.getActualTypeArguments();

            if (newParameterNumber >= actualTypeArguments.length)
            {
                throw new IllegalArgumentException("newParameterNumber=" + newParameterNumber + " is too big when parameterType=" + parameterType + " give actualTypeArguments.length=" + actualTypeArguments.length);
            }

            childType = actualTypeArguments[newParameterNumber];
        }

        TypeHintContext child = new TypeHintContext(converterManager, this.method, this.parameterNumber, childType);

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
        if (method == null)
        {
            return parameterNumber + parameterNumberTree.hashCode();
        }
        else
        {
            return method.hashCode() + parameterNumber + parameterNumberTree.hashCode();
        }
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

        if (this.method == null)
        {
            if (that.method != null)
            {
                return false;
            }
        }
        else
        {
            if (!this.method.equals(that.method))
            {
                return false;
            }
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
    // This really wants to go in the second loop but see: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6297416
    @SuppressWarnings("unused")
    @Override
    public String toString()
    {
        if (cachedToString == null)
        {
            StringBuffer buffer = new StringBuffer();

            if (method == null)
            {
                buffer.append("UnknownMethod");
            }
            else
            {
                buffer.append(method.getDeclaringClass().getName());
                buffer.append(".");
                buffer.append(method.getName());
            }
            buffer.append("(param#=");
            buffer.append(parameterNumber);

            for (Integer i : parameterNumberTree)
            {
                buffer.append('<');
                buffer.append(i);
            }

            for (Integer i : parameterNumberTree)
            {
                buffer.append('>');
            }

            buffer.append(')');

            cachedToString = buffer.toString();
        }

        return cachedToString;
    }

    /**
     * When we can't work out a parameterized type, then we can ask here
     */
    private final ConverterManager converterManager;

    /**
     * Calculating toString could be costly, so we cache it
     */
    private String cachedToString = null;

    /**
     * The method that the conversion is happening for
     */
    private final Method method;

    /**
     * The parameter of the method that the conversion is happening for
     */
    private final int parameterNumber;

    /**
     * The type parameter of the method that the conversion is happening for
     */
    private final Type parameterType;

    /**
     * The list of generic parameters that we have dug into
     */
    private final List<Integer> parameterNumberTree;

    /**
     * The list of generic parameters that we have dug into
     */
    private final List<Type> parameterTypeTree;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(TypeHintContext.class);
}
