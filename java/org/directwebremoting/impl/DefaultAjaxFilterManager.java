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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
    public Iterator getAjaxFilters(String scriptname)
    {
        // PERFORMANCE: we could probably cache the results of these if we wanted to
        List reply = new ArrayList();

        reply.addAll(global);

        List classBased = (List) classBasedMap.get(scriptname);
        if (classBased != null)
        {
            reply.addAll(classBased);
        }

        reply.add(executor);

        return Collections.unmodifiableList(reply).iterator();
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
    public void addAjaxFilter(AjaxFilter filter, String scriptname)
    {
        List classBased = (List) classBasedMap.get(scriptname);
        if (classBased == null)
        {
            classBased = new ArrayList();
            classBasedMap.put(scriptname, classBased);
        }

        classBased.add(filter);
    }

    /**
     * The base filter that actually does the execution
     */
    private AjaxFilter executor = new ExecuteAjaxFilter();

    /**
     * The list of all global filters
     */
    private List global = new ArrayList();

    /**
     * The map of lists of class based filters
     */
    private Map classBasedMap = new HashMap();
}
