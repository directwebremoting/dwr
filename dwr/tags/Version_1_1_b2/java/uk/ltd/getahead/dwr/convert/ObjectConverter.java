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
package uk.ltd.getahead.dwr.convert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import uk.ltd.getahead.dwr.ConversionConstants;
import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.OutboundVariable;
import uk.ltd.getahead.dwr.compat.BaseV10Converter;
import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * Convert a Javascript associative array into a JavaBean
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ObjectConverter extends BaseV10Converter implements Converter
{
    /**
     * Do we force accessibility for private fields
     * @param force "true|false" to set the force accessibility flag
     */
    public void setForce(String force)
    {
        this.force = Boolean.valueOf(force).booleanValue();
    }

    /**
     * @param excludes
     */
    public void setExclude(String excludes)
    {
        if (inclusions != null)
        {
            throw new IllegalArgumentException(Messages.getString("HibernateBeanConverter.OnlyIncludeOrExclude")); //$NON-NLS-1$
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
     * @param includes
     */
    public void setInclude(String includes)
    {
        if (exclusions != null)
        {
            throw new IllegalArgumentException(Messages.getString("HibernateBeanConverter.OnlyIncludeOrExclude")); //$NON-NLS-1$
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
        setInstanceType(Class.forName(name));
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
     * @see uk.ltd.getahead.dwr.Converter#init(uk.ltd.getahead.dwr.DefaultConfiguration)
     */
    public void setConverterManager(ConverterManager newConfig)
    {
        this.config = newConfig;
    }

    /**
     * Accessor for the current ConverterManager
     * @return the current ConverterManager
     */
    public ConverterManager getConverterManager()
    {
        return config;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertInbound(java.lang.Class, java.util.List, uk.ltd.getahead.dwr.InboundVariable, uk.ltd.getahead.dwr.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws ConversionException
    {
        String value = iv.getValue();

        // If the text is null then the whole bean is null
        if (value.trim().equals(ConversionConstants.INBOUND_NULL))
        {
            return null;
        }

        if (!value.startsWith(ConversionConstants.INBOUND_MAP_START))
        {
            throw new IllegalArgumentException(Messages.getString("BeanConverter.MissingOpener", ConversionConstants.INBOUND_MAP_START)); //$NON-NLS-1$
        }

        if (!value.endsWith(ConversionConstants.INBOUND_MAP_END))
        {
            throw new IllegalArgumentException(Messages.getString("BeanConverter.MissingCloser", ConversionConstants.INBOUND_MAP_START)); //$NON-NLS-1$
        }

        value = value.substring(1, value.length() - 1);

        try
        {
            Object bean = null;
            if (instanceType != null)
            {
                bean = instanceType.newInstance();
            }
            else
            {
                bean = paramType.newInstance();
            }

            // We know what we are converting to, so we create a map of property
            // names against PropertyDescriptors to speed lookup later
            Field[] fields = getAllFields(bean);
            Map props = new HashMap();
            for (int i = 0; i < fields.length; i++)
            {
                String key = fields[i].getName();
                props.put(key, fields[i]);
            }

            // We should put the new object into the working map in case it
            // is referenced later nested down in the conversion process.
            inctx.addConverted(iv, bean);

            // Loop through the property declarations
            StringTokenizer st = new StringTokenizer(value, ConversionConstants.INBOUND_MAP_SEPARATOR);
            int size = st.countTokens();
            for (int i = 0; i < size; i++)
            {
                String token = st.nextToken();
                if (token.trim().length() == 0)
                {
                    continue;
                }

                int colonpos = token.indexOf(ConversionConstants.INBOUND_MAP_ENTRY);
                if (colonpos == -1)
                {
                    throw new ConversionException(Messages.getString("BeanConverter.MissingSeparator", ConversionConstants.INBOUND_MAP_ENTRY, token)); //$NON-NLS-1$
                }

                String key = token.substring(0, colonpos).trim();
                String val = token.substring(colonpos + 1).trim();

                Field field = (Field) props.get(key);
                if (field == null)
                {
                    log.warn("No field for " + key); //$NON-NLS-1$
                    StringBuffer all = new StringBuffer();
                    for (Iterator it = props.keySet().iterator(); it.hasNext();)
                    {
                        all.append(it.next());
                        if (it.hasNext())
                        {
                            all.append(',');
                        }
                    }
                    log.warn("Fields exist for (" + all + ")."); //$NON-NLS-1$ //$NON-NLS-2$
                }
                else
                {
                    Class propType = field.getType();

                    String[] split = LocalUtil.splitInbound(val);
                    InboundVariable nested = new InboundVariable(iv.getLookup(), split[LocalUtil.INBOUND_INDEX_TYPE], split[LocalUtil.INBOUND_INDEX_VALUE]);

                    if (!field.isAccessible())
                    {
                        if (force)
                        {
                            field.setAccessible(true);
                        }
                        else
                        {
                            log.debug("Field: " + field.getName() + " is not accessible. use <param name='force' value='true'/>"); //$NON-NLS-1$ //$NON-NLS-2$
                            continue;
                        }
                    }

                    //inctx.pushContext(setter, 0);
                    Object output = config.convertInbound(propType, nested, inctx);
                    //inctx.popContext(setter, 0);
                    field.set(bean, new Object[] { output });
                }
            }

            return bean;
        }
        catch (ConversionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ConversionException(ex);
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertOutbound(java.lang.Object, java.lang.String, uk.ltd.getahead.dwr.OutboundContext)
     */
    public String convertOutbound(Object data, String varname, OutboundContext outctx) throws ConversionException
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("var "); //$NON-NLS-1$
        buffer.append(varname);
        buffer.append("={};"); //$NON-NLS-1$

        try
        {
            Field[] fields = getAllFields(data);
            for (int i = 0; i < fields.length; i++)
            {
                Field field = fields[i];
                String name = field.getName();

                try
                {
                    // We don't marshall getClass()
                    if (name.equals("class")) //$NON-NLS-1$
                    {
                        continue;
                    }

                    // Access rules mean we might not want to do this one
                    if (!isAllowed(name))
                    {
                        log.debug("Skipping marshalling " + name + " due to include/exclude rules"); //$NON-NLS-1$ //$NON-NLS-2$
                        continue;
                    }

                    if (!isAvailable(data, name))
                    {
                        log.debug("Skipping marshalling " + name + " due to availability rules"); //$NON-NLS-1$ //$NON-NLS-2$
                        continue;
                    }

                    if (!field.isAccessible())
                    {
                        if (force)
                        {
                            field.setAccessible(true);
                        }
                        else
                        {
                            log.debug("Field: " + field.getName() + " is not accessible. use <param name='force' value='true'/>"); //$NON-NLS-1$ //$NON-NLS-2$
                            continue;
                        }
                    }

                    Object value = field.get(data);
                    OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);

                    // Make sure the nested thing is declared
                    buffer.append(nested.getInitCode());

                    // And now declare our stuff
                    buffer.append(varname);
                    buffer.append('.');
                    buffer.append(name);
                    buffer.append('=');
                    buffer.append(nested.getAssignCode());
                    buffer.append(';');
                }
                catch (Exception ex)
                {
                    log.warn("Failed to convert " + name, ex); //$NON-NLS-1$
                }
            }
        }
        catch (Exception ex)
        {
            throw new ConversionException(ex);
        }

        return buffer.toString();
    }

    /**
     * Get the fields from a bean. You can't use <code>class.getFields()</code>
     * in place of this because it only gives you accessible fields, and
     * although <code>class.getDeclaredFields()</code> does give in-accessible
     * fields is doesn't walk up the tree.
     * @param bean The class to find bean info from
     * @return An array of all the fields for the given object
     */
    protected Field[] getAllFields(Object bean)
    {
        Set allFields = new HashSet();

        Class clazz = bean.getClass();

        while (clazz != Object.class)
        {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++)
            {
                allFields.add(fields[i]);
            }

            clazz = clazz.getSuperclass();
        }

        return (Field[]) allFields.toArray(new Field[allFields.size()]);
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
     * Some child converters (like Hibernate at least) need to check that a
     * property should be marshalled. This allows them to veto a marshal
     * @param data The object to check on
     * @param property The property of the <code>data</code> object
     * @return true if we should continue and marshall it.
     */
    public boolean isAvailable(Object data, String property)
    {
        // This just shuts the eclipse lint up
        if (false) { data = property; property = (String) data; }

        return true;
    }

    /**
     * Do we force accessibillity for hidden fields
     */
    private boolean force;

    /**
     * The list of excluded properties
     */
    private List exclusions = null;

    /**
     * The list of included properties
     */
    private List inclusions = null;

    /**
     * A type that allows us to fulfill an interface or subtype requirement
     */
    private Class instanceType = null;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ObjectConverter.class);

    /**
     * To forward marshalling requests
     */
    private ConverterManager config = null;
}
