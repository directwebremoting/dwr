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
package org.directwebremoting.spring;

import java.util.List;
import java.util.Properties;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.Creator;

/**
 * The configuration for a creator. <br>
 * You can either specify the creator directly or specify one of the build in creator types,
 * for instance "new".
 *
 * It allows the specification of the following optional configuration parameters:
 * <ul>
 *  <li>includes - the list of method names to include</li>
 *  <li>excludes - the list of method names to exclude</li>
 *  <li>auth - the <code>Properties</code> object containing method names and corresponding
 *      required role</li>
 *  <li>filters - the list of filter objects</li>
 * </ul>
 *
 * @see org.directwebremoting.AccessControl#addIncludeRule(String, String)
 * @see org.directwebremoting.AccessControl#addExcludeRule(String, String)
 * @see org.directwebremoting.AccessControl#addRoleRestriction(String, String, String)
 * @see org.directwebremoting.AjaxFilter
 * @see org.directwebremoting.AjaxFilterManager#addAjaxFilter(org.directwebremoting.AjaxFilter, String)
 *
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CreatorConfig
{
    /**
     * The creator type that will be used to create new objects for remoting
     * @return Returns the creator type.
     */
    public String getCreatorType()
    {
        return creatorType;
    }

    /**
     * The creator that will be used to create new objects for remoting
     * @param creatorType The creator type to set.
     */
    public void setCreatorType(String creatorType)
    {
        this.creatorType = creatorType;
    }

    /**
     * The creator that will be used to create new objects for remoting
     * @return Returns the creator.
     */
    public Creator getCreator()
    {
        return creator;
    }

    /**
     * The creator type that will be used to create new objects for remoting
     * @param creator The creator to set.
     */
    public void setCreator(Creator creator)
    {
        this.creator = creator;
    }

    /**
     * Gets the list of method names to include for this creator.
     * @return the list of method names to include
     * @see org.directwebremoting.AccessControl#addIncludeRule(String, String)
     */
    public List getIncludes()
    {
        return includes;
    }

    /**
     * Sets the list of method names to include for this creator.
     * @param includes the list of method names to include
     * @see org.directwebremoting.AccessControl#addIncludeRule(String, String)
     */
    public void setIncludes(List includes)
    {
        this.includes = includes;
    }

    /**
     * Gets the list of method names to exclude for this creator.
     * @return the list of method names to exclude
     * @see org.directwebremoting.AccessControl#addExcludeRule(String, String)
     */
    public List getExcludes()
    {
        return excludes;
    }

    /**
     * Sets the list of method names to exclude for this creator.
     * @param excludes the list of method names to exclude
     * @see org.directwebremoting.AccessControl#addExcludeRule(String, String)
     */
    public void setExcludes(List excludes)
    {
        this.excludes = excludes;
    }

    /**
     * Sets the authentication parameters for this creator.
     * @return the map containing the method name and the corrosponding required role
     * @see org.directwebremoting.AccessControl#addRoleRestriction(String, String, String)
     */
    public Properties getAuth()
    {
        return auth;
    }

    /**
     * Sets the authentication parameters for this creator.
     * @param auth the map containing the method name and the corrosponding required role
     * @see org.directwebremoting.AccessControl#addRoleRestriction(String, String, String)
     */
    public void setAuth(Properties auth)
    {
        this.auth = auth;
    }

    /**
     * Gets the list of all filters for this creator.
     * @return the list containing all filters
     * @see org.directwebremoting.AjaxFilter
     * @see org.directwebremoting.AjaxFilterManager#addAjaxFilter(org.directwebremoting.AjaxFilter, String)
     */
    public List getFilters()
    {
        return filters;
    }

    /**
     * Sets the list of all filters for this creator.
     * @param filters the list containing all filters
     * @see org.directwebremoting.AjaxFilter
     * @see org.directwebremoting.AjaxFilterManager#addAjaxFilter(org.directwebremoting.AjaxFilter, String)
     */
    public void setFilters(List filters)
    {
        this.filters = filters;
    }

    /**
     * Convenience method for adding an authentication rule.
     * @param method the method to add the authentication rule
     * @param role the role to add the authentication constraint for
     * @throws IllegalArgumentException in case the specified argument is null
     */
    public void addAuth(String method, String role)
    {
        auth.setProperty(method, role);
    }

    /**
     * Convenience method for adding a filter.
     * @param filter the filter to add for this creator
     * @throws IllegalArgumentException in case the specified argument is null
     */
    public void addFilter(AjaxFilter filter)
    {
        filters.add(filter);
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
     * The creator type to use
     */
    private String creatorType;

    /**
     * The creator to use
     */
    private Creator creator;

    /**
     * The list of method names to include for this creator.
     */
    private List includes = new ArrayList();

    /**
     * The list of method names to exclude for this creator.
     */
    private List excludes = new ArrayList();

    /**
     * The properties containing the method name and the corrosponding required role.
     */
    private Properties auth = new Properties();

    /**
     * The list of filter objects for this creator.
     */
    private List filters = new ArrayList();

    /**
     * The set of key/value pairs to provide further configuration
     */
    private Map params = new HashMap();
}
