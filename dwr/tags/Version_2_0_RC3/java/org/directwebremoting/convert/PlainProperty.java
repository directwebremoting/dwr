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
package org.directwebremoting.convert;

import java.lang.reflect.Method;

import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.Property;
import org.directwebremoting.util.Logger;

/**
 * An implementation of {@link Property} that simply uses stored values.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PlainProperty implements Property
{
    /**
     * @param name The property name
     * @param value The property value irrespective of the object that we read it on
     */
    public PlainProperty(String name, Object value)
    {
        this.name = name;
        this.value = value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getName()
     */
    public String getName()
    {
        return name;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getPropertyType()
     */
    public Class getPropertyType()
    {
        return value.getClass();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getSetter()
     */
    public Method getSetter()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) throws MarshallException
    {
        return value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object bean, Object value) throws MarshallException
    {
        log.warn("Attempt to setValue() on plain property.");
    }

    /**
     * The name of this property
     */
    private String name;

    /**
     * The property value irrespective of the object that we read it on
     */
    private Object value;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(PlainProperty.class);
}
