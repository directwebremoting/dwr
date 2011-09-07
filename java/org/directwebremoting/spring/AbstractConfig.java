/*
 * Copyright 2005-2006 Joe Walker
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

package org.directwebremoting.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The abstract config to use to configure parts of DWR in Spring. <br>
 *
 * @see org.directwebremoting.extend.AccessControl#addIncludeRule(String, String)
 * @see org.directwebremoting.extend.AccessControl#addExcludeRule(String, String)
 *
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Brendan Grainger
 */
abstract class AbstractConfig
{

    /**
     * Gets the list of method names to include for this creator.
     * @return the list of method names to include
     * @see org.directwebremoting.extend.AccessControl#addIncludeRule(String, String)
     */
    public List getIncludes()
    {
        return includes;
    }

    /**
     * Sets the list of method names to include for this creator.
     * @param includes the list of method names to include
     * @see org.directwebremoting.extend.AccessControl#addIncludeRule(String, String)
     */
    public void setIncludes(List includes)
    {
        this.includes = includes;
    }

    /**
     * Gets the list of method names to exclude for this creator.
     * @return the list of method names to exclude
     * @see org.directwebremoting.extend.AccessControl#addExcludeRule(String, String)
     */
    public List getExcludes()
    {
        return excludes;
    }

    /**
     * Sets the list of method names to exclude for this creator.
     * @param excludes the list of method names to exclude
     * @see org.directwebremoting.extend.AccessControl#addExcludeRule(String, String)
     */
    public void setExcludes(List excludes)
    {
        this.excludes = excludes;
    }

    /**
     * Convenience method for adding an include rule.
     * @param method the method to add the include rule for
     * @throws IllegalArgumentException in case the specified argument is null
     */
    public void addInclude(String method)
    {
        includes.add(method);
    }

    /**
     * Convenience method for adding an exclude rule.
     * @param method the method to add the exclude rule
     * @throws IllegalArgumentException in case the specified argument is null
     */
    public void addExclude(String method)
    {
        excludes.add(method);
    }

    /**
     * The set of key/value pairs to provide further configuration.<br>
     * Note that these params are only used when setting the creator type and not when setting the
     * creator directly.
     * @return Returns the params.
     */
    public Map getParams()
    {
        return params;
    }

    /**
     * The set of key/value pairs to provide further configuration.<br>
     * Note that these params are only used when setting the creator type and not when setting the
     * creator directly.
     * @param params The params to set.
     */
    public void setParams(Map params)
    {
        this.params = params;
    }

    /**
     * The list of method names to include for this creator.
     */
    private List includes = new ArrayList();

    /**
     * The list of method names to exclude for this creator.
     */
    private List excludes = new ArrayList();

    /**
     * The set of key/value pairs to provide further configuration
     */
    private Map params = new HashMap();
}
