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

import org.directwebremoting.create.NewCreator;
import org.directwebremoting.extend.Creator;

/**
 * Make a class available for remote access.
 * @author Maik Schreiber <blizzy AT blizzy DOT de>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteProxy
{
    /**
     * Name of the JavaScript variable (default: the simple class name without package).
     */
    String name() default "";

    /**
     * Creator that creates instances of the class (default: &quot;{@code new}&quot; creator).
     */
    Class<? extends Creator> creator() default NewCreator.class;

    /**
     * Parameters for the creator.
     */
    Param[] creatorParams() default {};

    /**
     * Scope of the JavaScript variable (default: PAGE).
     */
    ScriptScope scope() default ScriptScope.PAGE;
}
