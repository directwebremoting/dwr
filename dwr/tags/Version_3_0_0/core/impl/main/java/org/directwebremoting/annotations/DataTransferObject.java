/*
 * Copyright 2006 Maik Schreiber <blizzy AT blizzy DOT de>
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
package org.directwebremoting.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.extend.Converter;

/**
 * Convert a class to JavaScript and back.
 * @author Maik Schreiber <blizzy AT blizzy DOT de>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataTransferObject
{
    /**
     * Converter that converts instance of the class (default: bean converter).
     */
    Class<? extends Converter> converter() default BeanConverter.class;

    /**
     * Parameters for the converter.
     */
    Param[] params() default {};

    /**
     * Converter type
     * TODO: Just used by Spring configurator
     */
    String type() default "bean";

    /**
     * Javascript class mapping.
     * TODO: Just used by Spring configurator
     */
    String javascript() default "";

}
