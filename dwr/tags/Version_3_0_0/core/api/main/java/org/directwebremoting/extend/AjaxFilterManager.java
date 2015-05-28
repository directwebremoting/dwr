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

import java.util.List;

import org.directwebremoting.AjaxFilter;

/**
 * A class that manages the various <code>AjaxFilter</code>s and what classes
 * they are registered against.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface AjaxFilterManager
{
    /**
     * Retrieve the global and class-based AjaxFilters for a given class.
     * In versions prior to 3.0 this method returned an Iterator AND which
     * included an ExecuteAjaxFilter (in last place). In version 3 this method
     * should return an unmodifiable list which does not contain any execute
     * functionality.
     * @param scriptName The script name to use to filter the class-based filters
     * @return An iterator over the available filters.
     * @since 3.0
     */
    List<AjaxFilter> getAjaxFilters(String scriptName);

    /**
     * Add a global AjaxFilter
     * @param filter The new global AjaxFilter
     */
    void addAjaxFilter(AjaxFilter filter);

    /**
     * Add a class based AjaxFilter
     * @param filter The new AjaxFilter
     * @param scriptName The script name to filter against
     */
    void addAjaxFilter(AjaxFilter filter, String scriptName);
}
