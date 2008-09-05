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
     */
    public TypeHintContext()
    {
        this.property = null;
    }

    /**
     * External setup this object
     * @param converterManager For when we can't work out the parameterized type info
     * @param property The property to annotate
     * @param parameterNumber The number of the parameter to edit (counts from 0)
     */
    public TypeHintContext(Property property)
    {
        this.property = property;
    }

    /**
     * Create a child TypeHintContext based on this one
     * @param newParameterNumber The index of the item between &lt; and &gt;.
     * @return a new TypeHintContext
     */
    public TypeHintContext createChildContext(ConverterManager converterManager, int newParameterNumber)
    {
        TypeHintContext child = property.createChild(newParameterNumber).createTypeHintContext(converterManager);
        return child;
    }

    /**
     * Find the parameterized type information for this parameter either using
     * JDK5 introspection, ConverterManager queries or defaults
     * @param converterManager TODO
     * @return The extra type information for this context
     */
    public Class<?> getExtraTypeInfo(ConverterManager converterManager)
    {
        Class<?> type = converterManager.getExtraTypeInfo(this);
        if (type != null)
        {
            log.debug("Using type info from <signature> " + toString() + " of " + type);
            return type;
        }

        return property.getPropertyType();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hash = 1488;
        hash += (property == null) ? 64 : property.hashCode();
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

        return CompareUtil.equals(this.property, that.property);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return property.toString();
    }

    /**
     * The property that the conversion is happening for
     */
    private final Property property;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(TypeHintContext.class);
}
