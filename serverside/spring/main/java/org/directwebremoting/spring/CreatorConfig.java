package org.directwebremoting.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.extend.Creator;

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
 * @see org.directwebremoting.extend.AccessControl#addIncludeRule(String, String)
 * @see org.directwebremoting.extend.AccessControl#addExcludeRule(String, String)
 * @see org.directwebremoting.extend.AccessControl#addRoleRestriction(String, String, String)
 * @see org.directwebremoting.AjaxFilter
 * @see org.directwebremoting.extend.AjaxFilterManager#addAjaxFilter(org.directwebremoting.AjaxFilter, String)
 *
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CreatorConfig extends AbstractConfig
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
     * Sets the authentication parameters for this creator.
     * @return the map containing the method name and the corrosponding required role
     * @see org.directwebremoting.extend.AccessControl#addRoleRestriction(String, String, String)
     */
    public Map<String, List<String>> getAuth()
    {
        return auth;
    }

    /**
     * Sets the authentication parameters for this creator.
     * @param auth the map containing the method name and the corresponding required role
     * @see org.directwebremoting.extend.AccessControl#addRoleRestriction(String, String, String)
     */
    public void setAuth(Map<String, List<String>> auth)
    {
        this.auth = auth;
    }

    /**
     * Gets the list of all filters for this creator.
     * @return the list containing all filters
     * @see org.directwebremoting.AjaxFilter
     * @see org.directwebremoting.extend.AjaxFilterManager#addAjaxFilter(org.directwebremoting.AjaxFilter, String)
     */
    public List<?> getFilters()
    {
        return filters;
    }

    /**
     * Sets the list of all filters for this creator.
     * @param filters the list containing all filters
     * @see org.directwebremoting.AjaxFilter
     * @see org.directwebremoting.extend.AjaxFilterManager#addAjaxFilter(org.directwebremoting.AjaxFilter, String)
     */
    public void setFilters(List<Object> filters)
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
        if (auth.get(method) == null)
        {
            auth.put(method, new ArrayList<String>());
        }
        auth.get(method).add(role);
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
     * The creator type to use
     */
    private String creatorType;

    /**
     * The creator to use
     */
    private Creator creator;

    /**
     * The properties containing the method name and the corresponding required role.
     */
    private Map<String, List<String>> auth = new HashMap<String, List<String>>();

    /**
     * The list of filter objects for this creator.
     */
    private List<Object> filters = new ArrayList<Object>();

}
