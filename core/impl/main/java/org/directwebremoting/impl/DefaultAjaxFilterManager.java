package org.directwebremoting.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.extend.AjaxFilterManager;

/**
 * The default implementation of AjaxFilterManager
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultAjaxFilterManager implements AjaxFilterManager
{
    /* (non-Javadoc)
     * @see org.directwebremoting.AjaxFilterManager#getAjaxFilters(java.lang.String)
     */
    public List<AjaxFilter> getAjaxFilters(String scriptName)
    {
        if (ExportUtil.isSystemClass(scriptName))
        {
            return Collections.emptyList();
        }

        // PERFORMANCE: we could probably cache the results of these if we wanted to
        List<AjaxFilter> reply = new ArrayList<AjaxFilter>();

        reply.addAll(global);

        List<AjaxFilter> classBased = classBasedMap.get(scriptName);
        if (classBased != null)
        {
            reply.addAll(classBased);
        }

        return Collections.unmodifiableList(reply);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.AjaxFilterManager#addAjaxFilter(org.directwebremoting.AjaxFilter)
     */
    public void addAjaxFilter(AjaxFilter filter)
    {
        global.add(filter);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.AjaxFilterManager#addAjaxFilter(org.directwebremoting.AjaxFilter, java.lang.String)
     */
    public void addAjaxFilter(AjaxFilter filter, String scriptName)
    {
        List<AjaxFilter> classBased = classBasedMap.get(scriptName);
        if (classBased == null)
        {
            classBased = new ArrayList<AjaxFilter>();
            classBasedMap.put(scriptName, classBased);
        }

        classBased.add(filter);
    }

    /**
     * The list of all global filters
     */
    private List<AjaxFilter> global = new ArrayList<AjaxFilter>();

    /**
     * The map of lists of class based filters
     */
    private Map<String, List<AjaxFilter>> classBasedMap = new HashMap<String, List<AjaxFilter>>();
}
