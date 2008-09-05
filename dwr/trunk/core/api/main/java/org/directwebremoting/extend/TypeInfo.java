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
 * Neither {@link Class} nor {@link java.lang.reflect.Type} really do it for
 * passing generic type information around. Type isn't guaranteed to work, and
 * Class can't tell you any thing about the types. TypeInfo allows us to
 * annotate the type information when we know more than the JVM.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface TypeInfo
{
    /**
     * The We'd like to just pass around a Class, but that doesn't hold generic
     * information, so this gets us the basic level of data.
     */
    Class<?> getPrimaryType();

    /**
     * Any type can have a number of generic type parameters. This method gets
     * access to the child TypeInfo objects, from which you can call
     * {@link #getPrimaryType()}. This array is zero based.
     * @param i The index of the generic type parameter.
     * @return A child TypeInfo object
     * @throws IllegalArgumentException if i<0 or i>=childCount
     */
    public TypeInfo getChildTypeInfo(ConverterManager converterManager, int i);
}
