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
public class AbstractConfig
{
    /**
     * Gets the list of method names to include for this creator.
     * @return the list of method names to include
     * @see org.directwebremoting.extend.AccessControl#addIncludeRule(String, String)
     */
    public List<String> getIncludes()
    {
        return includes;
    }

    /**
     * Sets the list of method names to include for this creator.
     * @param includes the list of method names to include
     * @see org.directwebremoting.extend.AccessControl#addIncludeRule(String, String)
     */
    public void setIncludes(List<String> includes)
    {
        this.includes = includes;
    }

    /**
     * Gets the list of method names to exclude for this creator.
     * @return the list of method names to exclude
     * @see org.directwebremoting.extend.AccessControl#addExcludeRule(String, String)
     */
    public List<String> getExcludes()
    {
        return excludes;
    }

    /**
     * Sets the list of method names to exclude for this creator.
     * @param excludes the list of method names to exclude
     * @see org.directwebremoting.extend.AccessControl#addExcludeRule(String, String)
     */
    public void setExcludes(List<String> excludes)
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
    public Map<String, String> getParams()
    {
        return params;
    }

    /**
     * The set of key/value pairs to provide further configuration.<br>
     * Note that these params are only used when setting the creator type and not when setting the
     * creator directly.
     * @param params The params to set.
     */
    public void setParams(Map<String, String> params)
    {
        this.params = params;
    }

    /**
     * The list of method names to include for this creator.
     */
    private List<String> includes = new ArrayList<String>();

    /**
     * The list of method names to exclude for this creator.
     */
    private List<String> excludes = new ArrayList<String>();

    /**
     * The set of key/value pairs to provide further configuration
     */
    private Map<String, String> params = new HashMap<String, String>();
}
