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
import org.directwebremoting.ConversionException;

/**
 * A Property for when we've been given override information in a signatures
 * element.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class OverrideProperty implements Property
{
    /**
     * All OverrideProperties need a name and the type they are providing
     */
    public OverrideProperty(Class<?> propertyType)
    {
        this.propertyType = propertyType;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#createChild(int)
     */
    public Property createChild(int index)
    {
        return new NestedProperty(this, null, null, 0, index);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getName()
     */
    public String getName()
    {
        return "OverrideProperty";
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getPropertyType()
     */
    public Class<?> getPropertyType()
    {
        return propertyType;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) throws ConversionException
    {
        log.error("Attempt to getValue() on Override Property.");
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object bean, Object value) throws ConversionException
    {
        log.error("Attempt to setValue() on Override Property.");
    }

    /**
     * @see org.directwebremoting.extend.Property#getName()
     */
    private final Class<?> propertyType;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(OverrideProperty.class);
}
