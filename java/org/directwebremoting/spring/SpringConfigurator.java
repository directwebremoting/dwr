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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.directwebremoting.AccessControl;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterManager;
import org.directwebremoting.Configurator;
import org.directwebremoting.Container;
import org.directwebremoting.ConverterManager;
import org.directwebremoting.CreatorManager;
import org.directwebremoting.util.LocalUtil;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SpringConfigurator implements Configurator
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Configurator#configure(org.directwebremoting.Container)
     */
    public void configure(Container container)
    {
        AccessControl accessControl = (AccessControl) container.getBean(AccessControl.class.getName());
        AjaxFilterManager ajaxFilterManager = (AjaxFilterManager) container.getBean(AjaxFilterManager.class.getName());
        ConverterManager converterManager = (ConverterManager) container.getBean(ConverterManager.class.getName());
        CreatorManager creatorManager = (CreatorManager) container.getBean(CreatorManager.class.getName());

        // Configure the creator types
        if (creatorTypes != null)
        {
            for (Iterator it = creatorTypes.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                String typeName = (String) entry.getKey();
                String className = (String) entry.getValue();

                creatorManager.addCreatorType(typeName, className);
            }
        }

        // Configure the converter types
        if (converterTypes != null)
        {
            for (Iterator it = converterTypes.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                String typeName = (String) entry.getKey();
                String className = (String) entry.getValue();

                converterManager.addConverterType(typeName, className);
            }
        }

        // Configure the creators
        if (creators != null)
        {
            try
            {
                for (Iterator it = creators.entrySet().iterator(); it.hasNext();)
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    String scriptName = (String) entry.getKey();
                    CreatorConfig creatorConfig = (CreatorConfig) entry.getValue();

                    String creatorName = creatorConfig.getCreator();
                    Map params = creatorConfig.getParams();
                    creatorManager.addCreator(scriptName, creatorName, params);

                    List excludes = creatorConfig.getExcludes();
                    for (Iterator eit = excludes.iterator(); eit.hasNext();)
                    {
                        String exclude = (String) eit.next();
                        accessControl.addExcludeRule(scriptName, exclude);
                    }

                    List includes = creatorConfig.getIncludes();
                    for (Iterator iit = includes.iterator(); iit.hasNext();)
                    {
                        String include = (String) iit.next();
                        accessControl.addIncludeRule(scriptName, include);
                    }

                    Properties auth = creatorConfig.getAuth();
                    for (Iterator ait = auth.entrySet().iterator(); ait.hasNext();)
                    {
                        Map.Entry aentry = (Map.Entry) ait.next();
                        String methodName = (String) aentry.getKey();
                        String role = (String) aentry.getValue();
                        accessControl.addRoleRestriction(scriptName, methodName, role);
                    }

                    List filters = creatorConfig.getFilters();
                    for (Iterator fit = filters.iterator(); fit.hasNext();)
                    {
                        String filterName = (String) fit.next();

                        AjaxFilter filter = (AjaxFilter) LocalUtil.classNewInstance(filterName, filterName, AjaxFilter.class);
                        if (filter != null)
                        {
                            // LocalUtil.setParams(filter, createSettingMap(include), ignore);
                            ajaxFilterManager.addAjaxFilter(filter, scriptName);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                throw new IllegalArgumentException(ex.toString());
            }
        }

        // Configure the converters
        if (converters != null)
        {
            try
            {
                for (Iterator it = converters.entrySet().iterator(); it.hasNext();)
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    String match = (String) entry.getKey();
                    String converterName = (String) entry.getValue();

                    converterManager.addConverter(match, converterName, new HashMap());
                }
            }
            catch (Exception ex)
            {
                throw new IllegalArgumentException(ex.toString());
            }
        }
    }

    /**
     * Setter for the map of Creator types 
     * @param creatorTypes The new creator types map
     */
    public void setCreatorTypes(Map creatorTypes)
    {
        this.creatorTypes = creatorTypes;
    }

    /**
     * Setter for the map of Converter types 
     * @param converterTypes The new creator types map
     */
    public void setConverterTypes(Map converterTypes)
    {
        this.converterTypes = converterTypes;
    }

    /**
     * Setter for the map of real Creators 
     * @param creators The new creator map
     */
    public void setCreators(Map creators)
    {
        this.creators = creators;
    }

    /**
     * Setter for the map of real Converter 
     * @param converters The new creator map
     */
    public void setConverters(Map converters)
    {
        this.converters = converters;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "SpringConfigurator[" //$NON-NLS-1$
            + creatorTypes.size() + " creatorTypes, " //$NON-NLS-1$
            + converterTypes.size() + " converterTypes, " //$NON-NLS-1$
            + creators.size() + " creators, " //$NON-NLS-1$
            + converters.size() + " converters, ]"; //$NON-NLS-1$
    }

    /**
     * The map of Converter types
     */
    private Map creatorTypes;

    /**
     * The map of Converter types
     */
    private Map converterTypes;

    /**
     * The map of real Creators
     */
    private Map creators;

    /**
     * The map of real Converter
     */
    private Map converters;
}
