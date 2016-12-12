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
package org.directwebremoting.datasync;

import org.directwebremoting.util.LocalUtil;

/**
 * An {@link AttributeValueExtractor} that simply calls {@link LocalUtil#getProperty}
 * to extract attribute values.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PojoAttributeValueExtractor implements AttributeValueExtractor
{
    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.AttributeValueExtractor#getValue(java.lang.Object, java.lang.String)
     */
    public Object getValue(Object bean, String property)
    {
        return LocalUtil.getProperty(bean, property, Object.class);
    }
}
