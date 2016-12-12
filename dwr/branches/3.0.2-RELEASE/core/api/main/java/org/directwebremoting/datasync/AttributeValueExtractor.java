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

/**
 * There are many ways to get a property from things, the most obvious would
 * be reflection or introspection or using {@link java.util.Map#get}. This
 * interface allows us to abstract the options.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface AttributeValueExtractor
{
    /**
     * Get an attribute/<code>property</code> from a <code>bean</code>.
     * @param bean The object to read an attribute/property from.
     * @param property The attribute/property to read.
     * @return The value of the returned object or null if it could not be
     * found.
     */
    public Object getValue(Object bean, String property);
}
