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
package uk.ltd.getahead.dwr;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Something to hold the method, paramNo and index together as an object
 * that can be a key in a Map.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TypeHintContext
{
    /**
     * Setup this object
     * @param method The method to annotate
     * @param parameterNumber The number of the parameter to edit (counts from 0)
     */
    public TypeHintContext(Method method, int parameterNumber)
    {
        this.method = method;
        this.parameterNumber = parameterNumber;
        genericParameterTree = new ArrayList();
    }

    /**
     * Create a child TypeHintContext based on this one
     * @param genericParameterNumber The index of the item between &lt; and &gt;.
     * @return a new TypeHintContext
     */
    public TypeHintContext createChildContext(int genericParameterNumber)
    {
        TypeHintContext child = new TypeHintContext(getMethod(), getParameterNumber());
        child.genericParameterTree.addAll(this.getGenericParameterTree());
        child.genericParameterTree.add(new Integer(genericParameterNumber));

        return child;
    }

    /**
     * Accessor for the method that we are converting for
     * @return The method that we are converting for
     */
    public Method getMethod()
    {
        return method;
    }

    /**
     * Accessor for the parameter that we are converting for
     * @return The parameter that we are converting for
     */
    public int getParameterNumber()
    {
        return parameterNumber;
    }

    /**
     * Accessor for the generic parameter number
     * @return The generic parameter number
     */
    public List getGenericParameterTree()
    {
        return genericParameterTree;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return method.hashCode() + parameterNumber + genericParameterTree.hashCode();
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

        return this.genericParameterTree.equals(that.genericParameterTree);
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

            for (Iterator it = genericParameterTree.iterator(); it.hasNext();)
            {
                buffer.append('<');
                buffer.append(it.next());
            }
            for (Iterator it = genericParameterTree.iterator(); it.hasNext();)
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
     * The list of generic parameters that we have dug into
     */
    private final List genericParameterTree;
}
