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

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.Container;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.impl.SignatureParser;
import org.directwebremoting.util.LocalUtil;
import org.springframework.util.StringUtils;

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
        AccessControl accessControl = container.getBean(AccessControl.class);
        AjaxFilterManager ajaxFilterManager = container.getBean(AjaxFilterManager.class);
        ConverterManager converterManager = container.getBean(ConverterManager.class);
        CreatorManager creatorManager = container.getBean(CreatorManager.class);

        // Configure the creator types
        if (creatorTypes != null)
        {
            for (Entry<String, String> entry : creatorTypes.entrySet())
            {
                String typeName = entry.getKey();
                String className = entry.getValue();

                creatorManager.addCreatorType(typeName, className);
            }
        }

        // Configure the converter types
        if (converterTypes != null)
        {
            for (Entry<String, String> entry : converterTypes.entrySet())
            {
                String typeName = entry.getKey();
                String className = entry.getValue();

                converterManager.addConverterType(typeName, className);
            }
        }

        // Configure the creators
        if (creators != null)
        {
            try
            {
                for (Entry<String, CreatorConfig> entry : creators.entrySet())
                {
                    String scriptName = entry.getKey();
                    CreatorConfig creatorConfig = entry.getValue();

                    if (creatorConfig.getCreator() != null)
                    {
                        Creator creator = creatorConfig.getCreator();
                        creatorManager.addCreator(scriptName, creator);
                    }
                    else
                    {
                        String creatorName = creatorConfig.getCreatorType();
                        Map<String, String> params = creatorConfig.getParams();
                        creatorManager.addCreator(scriptName, creatorName, params);
                    }

                    for (String exclude : creatorConfig.getExcludes())
                    {
                        accessControl.addExcludeRule(scriptName, exclude);
                    }

                    for (String include : creatorConfig.getIncludes())
                    {
                        accessControl.addIncludeRule(scriptName, include);
                    }

                    Properties auth = creatorConfig.getAuth();
                    for (Entry<Object, Object> aentry : auth.entrySet())
                    {
                        String methodName = (String) aentry.getKey();
                        String role = (String) aentry.getValue();
                        accessControl.addRoleRestriction(scriptName, methodName, role);
                    }

                    List<?> filters = creatorConfig.getFilters();
                    for (Object obj : filters)
                    {
                        if (obj instanceof String)
                        {
                            String filterName = (String) obj;

                            AjaxFilter filter = LocalUtil.classNewInstance(filterName, filterName, AjaxFilter.class);
                            if (filter != null)
                            {
                                ajaxFilterManager.addAjaxFilter(filter, scriptName);
                            }
                        }
                        else if (obj instanceof AjaxFilter)
                        {
                            AjaxFilter filter = (AjaxFilter) obj;
                            ajaxFilterManager.addAjaxFilter(filter, scriptName);
                        }
                        else
                        {
                            throw new IllegalArgumentException("An invalid filter is added for script '" + scriptName + "'. It should either be the class name of the filter or an instantiated AjaxFilter, but was: '" + obj + "'.");
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                throw new IllegalArgumentException(ex);
            }
        }

        // Configure the converters
        if (converters != null)
        {
            try
            {
                for (Entry<String, ConverterConfig> entry : converters.entrySet())
                {
                    String match = entry.getKey();
                    ConverterConfig converterConfig = entry.getValue();

                    Map<String, String> params = converterConfig.getParams();
                    if (!converterConfig.getIncludes().isEmpty())
                    {
                        params.put("include", StringUtils.collectionToCommaDelimitedString(converterConfig.getIncludes()));
                    }

                    if (!converterConfig.getExcludes().isEmpty())
                    {
                        params.put("exclude", StringUtils.collectionToCommaDelimitedString(converterConfig.getExcludes()));
                    }

                    // params.put("force", Boolean.valueOf(converterConfig.isForce()));
                    if (StringUtils.hasText(converterConfig.getJavascriptClassName()))
                    {
                        params.put("javascript", converterConfig.getJavascriptClassName());
                    }

                    String type = converterConfig.getType();
                    if(type.startsWith("preconfigured"))
                    {
                        converterManager.addConverter(match, (Converter) container.getBean(type.substring(14)));
                    }
                    else
                    {
                        converterManager.addConverter(match, type, params);
                    }
                }
            }
            catch (Exception ex)
            {
                throw new IllegalArgumentException("An error occurred while configuring the converters.");
            }
        }

        // Configure the signatures
        if (StringUtils.hasText(signatures)) {
            SignatureParser sigp = new SignatureParser(converterManager, creatorManager);
            sigp.parse(signatures);
        }
    }

    /**
     * Setter for the map of Creator types
     * @param creatorTypes The new creator types map
     */
    public void setCreatorTypes(Map<String, String> creatorTypes)
    {
        this.creatorTypes = creatorTypes;
    }

    /**
     * Setter for the map of Converter types
     * @param converterTypes The new creator types map
     */
    public void setConverterTypes(Map<String, String> converterTypes)
    {
        this.converterTypes = converterTypes;
    }

    /**
     * Setter for the map of real Creators
     * @param creators The new creator map
     */
    public void setCreators(Map<String, CreatorConfig> creators)
    {
        this.creators = creators;
    }

    /**
     * Setter for the map of real Converter
     * @param converters The new creator map
     */
    public void setConverters(Map<String, ConverterConfig> converters)
    {
        this.converters = converters;
    }

    /**
     * @param signatures the signatures to set
     */
    public void setSignatures(String signatures)
    {
        this.signatures = signatures;
    }

    /**
     * @return the signatures
     */
    public String getSignatures()
    {
        return signatures;
    }

    /**
     * The map of Converter types
     */
    private Map<String, String> creatorTypes;

    /**
     * The map of Converter types
     */
    private Map<String, String> converterTypes;

    /**
     * The map of real Creators
     */
    private Map<String, CreatorConfig> creators;

    /**
     * The map of real Converter
     */
    private Map<String, ConverterConfig> converters;

    /**
     * The string of Signatures
     */
    private String signatures;
}