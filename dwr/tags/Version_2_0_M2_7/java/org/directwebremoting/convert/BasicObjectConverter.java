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
package org.directwebremoting.convert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.directwebremoting.Converter;
import org.directwebremoting.ConverterManager;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

/**
 * BasicObjectConverter is a parent to [Bean|Object]Converter an provides
 * support for include and exclude lists, and instanceTypes.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BasicObjectConverter extends BaseV20Converter implements Converter
{
    /**
     * Set a list of properties excluded from conversion
     * @param excludes The space or comma separated list of properties to exclude
     */
    public void setExclude(String excludes)
    {
        if (inclusions != null)
        {
            throw new IllegalArgumentException(Messages.getString("BeanConverter.OnlyIncludeOrExclude")); //$NON-NLS-1$
        }

        exclusions = new ArrayList();

        String toSplit = LocalUtil.replace(excludes, ",", " "); //$NON-NLS-1$ //$NON-NLS-2$
        StringTokenizer st = new StringTokenizer(toSplit);
        while (st.hasMoreTokens())
        {
            String rule = st.nextToken();
            if (rule.startsWith("get")) //$NON-NLS-1$
            {
                log.warn("Exclusions are based on property names and not method names. '" + rule + "' starts with 'get' so it looks like a method name and not a property name."); //$NON-NLS-1$ //$NON-NLS-2$
            }

            exclusions.add(rule);
        }
    }

    /**
     * Set a list of properties included from conversion
     * @param includes The space or comma separated list of properties to exclude
     */
    public void setInclude(String includes)
    {
        if (exclusions != null)
        {
            throw new IllegalArgumentException(Messages.getString("BeanConverter.OnlyIncludeOrExclude")); //$NON-NLS-1$
        }

        inclusions = new ArrayList();

        String toSplit = LocalUtil.replace(includes, ",", " "); //$NON-NLS-1$ //$NON-NLS-2$
        StringTokenizer st = new StringTokenizer(toSplit);
        while (st.hasMoreTokens())
        {
            String rule = st.nextToken();
            if (rule.startsWith("get")) //$NON-NLS-1$
            {
                log.warn("Inclusions are based on property names and not method names. '" + rule + "' starts with 'get' so it looks like a method name and not a property name."); //$NON-NLS-1$ //$NON-NLS-2$
            }

            inclusions.add(rule);
        }
    }

    /**
     * @param name The class name to use as an implementation of the converted bean
     * @throws ClassNotFoundException If the given class can not be found
     */
    public void setImplementation(String name) throws ClassNotFoundException
    {
        setInstanceType(LocalUtil.classForName(name));
    }

    /**
     * @return Returns the instanceType.
     */
    public Class getInstanceType()
    {
        return instanceType;
    }

    /**
     * @param instanceType The instanceType to set.
     */
    public void setInstanceType(Class instanceType)
    {
        this.instanceType = instanceType;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BaseV20Converter#setConverterManager(org.directwebremoting.ConverterManager)
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * Accessor for the current ConverterManager
     * @return the current ConverterManager
     */
    public ConverterManager getConverterManager()
    {
        return converterManager;
    }

    /**
     * Check with the access rules to see if we are allowed to convert a property
     * @param property The property to test
     * @return true if the property may be marshalled
     */
    protected boolean isAllowed(String property)
    {
        if (exclusions != null)
        {
            // Check each exclusions and return false if we get a match
            for (Iterator it = exclusions.iterator(); it.hasNext();)
            {
                String test = (String) it.next();
                if (property.equals(test))
                {
                    return false;
                }
            }

            // So we passed all the exclusions. The setters enforce mutual
            // exclusion between exclusions and inclusions so we don't need to
            // 'return true' here, we can carry on. This has the advantage that
            // we can relax the mutual exclusion at some stage.
        }

        if (inclusions != null)
        {
            // Check each inclusion and return true if we get a match
            for (Iterator it = inclusions.iterator(); it.hasNext();)
            {
                String test = (String) it.next();
                if (property.equals(test))
                {
                    return true;
                }
            }

            // Since we are white-listing with inclusions and there was not
            // match, this property is not allowed.
            return false;
        }

        // default to allow if there are no inclusions or exclusions
        return true;
    }


    /**
     * The list of excluded properties
     */
    protected List exclusions = null;

    /**
     * The list of included properties
     */
    protected List inclusions = null;

    /**
     * A type that allows us to fulfill an interface or subtype requirement
     */
    protected Class instanceType = null;

    /**
     * To forward marshalling requests
     */
    protected ConverterManager converterManager = null;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(BasicObjectConverter.class);
}
