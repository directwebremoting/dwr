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

/**
 * The simplest implementation of {@link TypeInfo} that knows only about the
 * direct type and can only guess at {@link String} for parameterized types.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SimpleTypeInfo implements TypeInfo
{
    /**
     * @param primaryType The top level type
     */
    private SimpleTypeInfo(Class<?> primaryType)
    {
        this.primaryType = primaryType;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.TypeInfo#getChildTypeInfo(int)
     */
    public TypeInfo getChildTypeInfo(int i)
    {
        return STRING_TYPE_INFO;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.TypeInfo#getPrimaryType()
     */
    public Class<?> getPrimaryType()
    {
        return primaryType;
    }

    /**
     * The type about which we don't understand the any generic type info
     */
    private Class<?> primaryType;

    /**
     * A static TypeInfo when we just need to make something up about strings
     */
    private static final TypeInfo STRING_TYPE_INFO = new ReflectionTypeInfo(String.class);
}
