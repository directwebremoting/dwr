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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;

/**
 * Something to hold the method, paramNo and index together as an object
 * that can be a key in a Map.
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
        if (method == null)
        {
            throw new IllegalArgumentException("The method can not be null");
        }

        this.converterManager = converterManager;
        this.method = method;
        this.parameterNumber = parameterNumber;

        // Type[] types = method.getGenericParameterTypes();
        Object[] types = (Object[]) LocalUtil.invoke(method, getGenericParameterTypesMethod, new Object[0]);

        if (types != null)
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

        parameterNumberTree = new ArrayList();
        parameterTypeTree = new ArrayList();
    }

    /**
     * Internal setup for nested object
     * @param manager For when we can't work out the parameterized type info
     * @param method The method to annotate
     * @param parameterNumber The number of the parameter to edit (counts from 0)
     * @param parameterType The Type for this context
     */
    private TypeHintContext(ConverterManager manager, Method method, int parameterNumber, Object/*Type*/ parameterType)
    {
        this.converterManager = manager;
        this.method = method;
        this.parameterNumber = parameterNumber;
        this.parameterType = parameterType;

        parameterNumberTree = new ArrayList();
        parameterTypeTree = new ArrayList();
    }

    /**
     * Create a child TypeHintContext based on this one
     * @param newParameterNumber The index of the item between &lt; and &gt;.
     * @return a new TypeHintContext
     */
    public TypeHintContext createChildContext(int newParameterNumber)
    {
        Object/*Type*/ childType = null;

        if (isGenericsSupported)
        {
            //if (parameterType instanceof ParameterizedType)
            if (parameterizedTypeClass.isInstance(parameterType))
            {
                Object/*Type*/ ptype = /*(Type)*/ parameterType;
                // Type[] rawParams = ptype.getActualTypeArguments();
                Object[] actualTypeArguments = (Object[]) LocalUtil.invoke(ptype, getActualTypeArgumentsMethod, new Object[0]);

                if (newParameterNumber >= actualTypeArguments.length)
                {
                    throw new IllegalArgumentException("newParameterNumber=" + newParameterNumber + " is too big when parameterType=" + parameterType + " give actualTypeArguments.length=" + actualTypeArguments.length);
                }

                childType = actualTypeArguments[newParameterNumber];
            }
        }

        TypeHintContext child = new TypeHintContext(converterManager, this.method, this.parameterNumber, childType);

        child.parameterNumberTree.addAll(this.parameterNumberTree);
        child.parameterNumberTree.add(new Integer(newParameterNumber));

        child.parameterTypeTree.addAll(parameterTypeTree);
        child.parameterTypeTree.add(parameterType);

        return child;
    }

    /**
     * Find the parameterized type information for this parameter either using
     * JDK5 introspection or
     * @return The extra type information for this context
     */
    public Class getExtraTypeInfo()
    {
        Class type = null;

        if (converterManager != null)
        {
            type = converterManager.getExtraTypeInfo(this);
            if (type != null)
            {
                log.debug("Using type info from <signature> " + toString() + " of " + type);
                return type;
            }
        }

        if (isGenericsSupported)
        {
            //if (parameterType instanceof ParameterizedType)
            if (parameterizedTypeClass.isInstance(parameterType))
            {
                Object/*Type*/ ptype = /*(Type)*/ parameterType;
                // Type rawType = ptype.getRawType();
                Object rawType = LocalUtil.invoke(ptype, getRawTypeMethod, new Object[0]);

                if (rawType instanceof Class)
                {
                    type = (Class) rawType;
                    log.debug("Using type info from JDK5 ParameterizedType of " + type.getName() + " for " + toString());
                    return type;
                }
            }
            else if (parameterType instanceof Class)
            {
                type = (Class) parameterType;
                log.debug("Using type info from JDK5 reflection of " + type.getName() + " for " + toString());
                return type;
            }
        }

        log.warn("Missing type info for " + toString() + ". Assuming this is a map with String keys. Please add to <signatures> in dwr.xml");
        return String.class;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return method.hashCode() + parameterNumber + parameterNumberTree.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

        if (!this.method.equals(that.method))
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
    public String toString()
    {
        if (cachedToString == null)
        {
            StringBuffer buffer = new StringBuffer();

            buffer.append(method.getName());
            buffer.append('(');
            buffer.append(parameterNumber);

            for (Iterator it = parameterNumberTree.iterator(); it.hasNext();)
            {
                buffer.append('<');
                buffer.append(it.next());
            }
            for (Iterator it = parameterNumberTree.iterator(); it.hasNext();)
            {
                buffer.append('>');
                it.next();
            }

            buffer.append(')');

            cachedToString = buffer.toString();
        }

        return cachedToString;
    }

    /**
     * When we can't work out a parameterized type, then we can ask here
     */
    private ConverterManager converterManager;

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
    private final Object/*Type*/ parameterType;

    /**
     * The list of generic parameters that we have dug into
     */
    private final List parameterNumberTree;

    /**
     * The list of generic parameters that we have dug into
     */
    private final List parameterTypeTree;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(TypeHintContext.class);

    /**
     * We have to use ParameterizedType through reflection since we work on JDK 1.3
     */
    private static final Class parameterizedTypeClass;

    /**
     * We have to execute getGenericParameterTypes() through reflection too
     */
    private static final Method getGenericParameterTypesMethod;

    /**
     * We have to execute getActualTypeArguments() through reflection too
     */
    private static final Method getActualTypeArgumentsMethod;

    /**
     * We have to execute getRawType() through reflection too
     */
    private static final Method getRawTypeMethod;

    /**
     * Can we use generic type info?
     */
    private static final boolean isGenericsSupported;

    static
    {
        // This is one of those times when you really wish you were in a
        // dynamic language ...

        // This may seem like a lot of bother just to call Class forName() a
        // couple of times, however it is complex because the fields are final
        // so we can only set them once
        int failures = 0;

        Class tempParameterizedTypeClass;
        try
        {
            tempParameterizedTypeClass = LocalUtil.classForName("java.lang.reflect.ParameterizedType");
            log.debug("JDK1.5 reflection available.");
        }
        catch (Exception ex)
        {
            tempParameterizedTypeClass = null;
            log.debug("JDK1.5 reflection not available. Generic parameters must use <signatures>.");
            failures++;
        }

        Method tempGetGenericParameterTypesMethod = null;
        try
        {
            tempGetGenericParameterTypesMethod = Method.class.getDeclaredMethod("getGenericParameterTypes", new Class[0]);
        }
        catch (Exception ex)
        {
            log.debug("Error finding Method.getGenericParameterTypes(): JDK1.5 reflection not available.");
            failures++;
        }
        catch (Error er)
        {
            // TODO: remove this error trapping - it's just here to debug a WL issue
            log.error("If you see this stack trace please report it to the DWR users mailing list", er);
        }

        Method tempGetActualTypeArgumentsMethod = null;
        try
        {
            if (tempParameterizedTypeClass != null)
            {
                tempGetActualTypeArgumentsMethod = tempParameterizedTypeClass.getDeclaredMethod("getActualTypeArguments", new Class[0]);
            }
        }
        catch (Exception ex)
        {
            log.debug("Error finding ParameterizedType.getActualTypeArguments(): JDK1.5 reflection not available.");
            failures++;
        }

        Method tempGetRawTypeMethod = null;
        try
        {
            if (tempParameterizedTypeClass != null)
            {
                tempGetRawTypeMethod = tempParameterizedTypeClass.getDeclaredMethod("getRawType", new Class[0]);
            }
        }
        catch (Exception ex)
        {
            log.debug("Error finding ParameterizedType.getRawType(): JDK1.5 reflection not available.");
            failures++;
        }

        if (failures == 0)
        {
            isGenericsSupported = true;

            parameterizedTypeClass = tempParameterizedTypeClass;
            getGenericParameterTypesMethod = tempGetGenericParameterTypesMethod;
            getActualTypeArgumentsMethod = tempGetActualTypeArgumentsMethod;
            getRawTypeMethod = tempGetRawTypeMethod;
        }
        else
        {
            isGenericsSupported = false;

            parameterizedTypeClass = null;
            getGenericParameterTypesMethod = null;
            getActualTypeArgumentsMethod = null;
            getRawTypeMethod = null;
        }
    }
}
