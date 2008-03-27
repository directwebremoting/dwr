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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

/**
 * It would be nice if {@link PropertyDescriptor}, and the various reflection
 * types like {@link Member} had a common supertype, but they don't. This is it.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Property
{
    /**
     * Gets the name of this property
     * @return The property name
     */
    public String getName();

    /**
     * What type does this property
     * @return The type of object that will be returned by {@link #getValue(Object)}
     */
    public Class getPropertyType();

    /**
     * Get the value of this property of the passed in java bean
     * @param bean The bean to introspect
     * @return The value assigned to this property of the passed in bean
     * @throws MarshallException If the reflection access fails
     */
    public Object getValue(Object bean) throws MarshallException;

    /**
     * Set the value of this property of the passed in java bean
     * @param bean The bean to introspect
     * @param value The value assigned to this property of the passed in bean
     * @throws MarshallException If the reflection access fails
     */
    public void setValue(Object bean, Object value) throws MarshallException;

    /**
     * This is a nasty hack - {@link TypeHintContext} needs a {@link Method}.
     * If you are implementing this and not proxying to a {@link PropertyDescriptor}
     * then you can probably return <code>null</code>.
     * We should probably refactor {@link TypeHintContext} to use {@link Property}
     * @return A setter method if one is available, or null otherwise
     */
    public Method getSetter();
}
