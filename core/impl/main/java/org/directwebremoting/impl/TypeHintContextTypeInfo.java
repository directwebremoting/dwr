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
package org.directwebremoting.impl;

import java.lang.reflect.Method;

import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.ParameterProperty;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.extend.TypeInfo;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TypeHintContextTypeInfo implements TypeInfo
{
    /**
     * @param primaryType The top level type
     */
    public TypeHintContextTypeInfo(Class<?> primaryType, TypeHintContext icc)
    {
        this.primaryType = primaryType;
        this.icc = icc;
    }

    /**
     * @param primaryType The top level type
     * @param converterManager
     * @param method
     * @param i
     */
    public TypeHintContextTypeInfo(Class<?> primaryType, ConverterManager converterManager, Method method, int i)
    {
        this.primaryType = primaryType;
        Property property = new ParameterProperty(method, i);
        icc = new TypeHintContext(property);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.TypeInfo#getChildTypeInfo(int)
     */
    public TypeInfo getChildTypeInfo(ConverterManager converterManager, int i)
    {
        TypeHintContext subthc = icc.createChildContext(converterManager, i);
        Class<?> subtype = subthc.getExtraTypeInfo(converterManager);

        return new TypeHintContextTypeInfo(subtype, subthc);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.TypeInfo#getPrimaryType()
     */
    public Class<?> getPrimaryType()
    {
        return primaryType;
    }

    private final TypeHintContext icc;

    /**
     * The type about which we don't understand the any generic type info
     */
    private final Class<?> primaryType;
}
