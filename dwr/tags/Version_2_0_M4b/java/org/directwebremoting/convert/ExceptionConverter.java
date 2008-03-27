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

import java.util.Map;

import org.directwebremoting.extend.MarshallException;

/**
 * A special case of BeanConverter that doesn't convert StackTraces
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ExceptionConverter extends BeanConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BasicBeanConverter#getPropertyDescriptors(java.lang.Class, boolean, boolean)
     */
    public Map getPropertyMap(Class data, boolean readRequired, boolean writeRequired) throws MarshallException
    {
        Map descriptors = super.getPropertyMap(data, readRequired, writeRequired);
        descriptors.remove("stackTrace");
        descriptors.put("type", new PlainProperty("type", data.getClass().getName()));
        return descriptors;
    }
}
