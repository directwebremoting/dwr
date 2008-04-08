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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.ConvertUtil;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.JsonModeMarshallException;
import org.directwebremoting.extend.MapOutboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.NamedConverter;
import org.directwebremoting.extend.ObjectJsonOutboundVariable;
import org.directwebremoting.extend.ObjectNonJsonOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.extend.TypeHintContext;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;

/**
 * BasicObjectConverter is a parent to {@link BeanConverter} and
 * {@link ObjectConverter} an provides support for include and exclude lists,
 * and instanceTypes.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BasicObjectConverter extends BaseV20Converter implements NamedConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data, InboundContext inctx) throws MarshallException
    {
        String value = data.getValue();

        // If the text is null then the whole bean is null
        if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
        {
            return null;
        }

        if (!value.startsWith(ProtocolConstants.INBOUND_MAP_START))
        {
            throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
        }

        if (!value.endsWith(ProtocolConstants.INBOUND_MAP_END))
        {
            throw new MarshallException(paramType, Messages.getString("BeanConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
        }

        value = value.substring(1, value.length() - 1);

        try
        {
            Object bean;
            if (instanceType != null)
            {
                bean = instanceType.newInstance();
            }
            else
            {
                bean = paramType.newInstance();
            }

            // We should put the new object into the working map in case it
            // is referenced later nested down in the conversion process.
            if (instanceType != null)
            {
                inctx.addConverted(data, instanceType, bean);
            }
            else
            {
                inctx.addConverted(data, paramType, bean);
            }

            Map<String, Property> properties = getPropertyMapFromObject(bean, false, true);

            // Loop through the properties passed in
            Map<String, String> tokens = extractInboundTokens(paramType, value);
            for (Entry<String, String> entry : tokens.entrySet())
            {
                String key = entry.getKey();
                String val = entry.getValue();

                Property property = properties.get(key);
                if (property == null)
                {
                    log.warn("Missing java bean property to match javascript property: " + key + ". For causes see debug level logs:");

                    log.debug("- The javascript may be refer to a property that does not exist");
                    log.debug("- You may be missing the correct setter: set" + Character.toTitleCase(key.charAt(0)) + key.substring(1) + "()");
                    log.debug("- The property may be excluded using include or exclude rules.");

                    StringBuffer all = new StringBuffer();
                    for (Iterator<String> pit = properties.keySet().iterator(); pit.hasNext();)
                    {
                        all.append(pit.next());
                        if (pit.hasNext())
                        {
                            all.append(',');
                        }
                    }
                    log.debug("Fields exist for (" + all + ").");
                    continue;
                }

                Class<?> propType = property.getPropertyType();

                String[] split = ConvertUtil.splitInbound(val);
                String splitValue = split[ConvertUtil.INBOUND_INDEX_VALUE];
                String splitType = split[ConvertUtil.INBOUND_INDEX_TYPE];

                InboundVariable nested = new InboundVariable(data.getLookup(), null, splitType, splitValue);
                nested.dereference();
                TypeHintContext incc = createTypeHintContext(inctx, property);

                Object output = converterManager.convertInbound(propType, nested, inctx, incc);
                property.setValue(bean, output);
            }

            return bean;
        }
        catch (MarshallException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new MarshallException(paramType, ex);
        }
    }

    /**
     * {@link #convertInbound(Class, InboundVariable, InboundContext)} needs to
     * create a {@link TypeHintContext} for the {@link Property} it is
     * converting so that the type guessing system can do its work.
     * <p>The method of generating a {@link TypeHintContext} is different for
     * the {@link BeanConverter} and the {@link ObjectConverter}.
     * @param inctx The parent context
     * @param property The property being converted
     * @return The new TypeHintContext
     */
    protected abstract TypeHintContext createTypeHintContext(InboundContext inctx, Property property);

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException
    {
        // Where we collect out converted children
        Map<String, OutboundVariable> ovs = new TreeMap<String, OutboundVariable>();

        // We need to do this before collecting the children to save recursion
        MapOutboundVariable ov;
        if (outctx.isJsonMode())
        {
            if (javascript != null)
            {
                throw new JsonModeMarshallException(data.getClass(), "Can't used named Javascript objects in JSON mode");
            }

            ov = new ObjectJsonOutboundVariable();
        }
        else
        {
            ov = new ObjectNonJsonOutboundVariable(outctx, getJavascript());
        }
        outctx.put(data, ov);

        try
        {
            Map<String, Property> properties = getPropertyMapFromObject(data, true, false);
            for (Entry<String, Property> entry : properties.entrySet())
            {
                String name = entry.getKey();
                Property property = entry.getValue();

                Object value = property.getValue(data);
                OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);

                ovs.put(name, nested);
            }
        }
        catch (MarshallException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new MarshallException(data.getClass(), ex);
        }

        ov.setChildren(ovs);

        return ov;
    }

    /**
     * Set a list of properties excluded from conversion
     * @param excludes The space or comma separated list of properties to exclude
     */
    public void setExclude(String excludes)
    {
        if (inclusions != null)
        {
            throw new IllegalArgumentException(Messages.getString("BeanConverter.OnlyIncludeOrExclude"));
        }

        exclusions = new ArrayList<String>();

        String toSplit = excludes.replace(",", " ");
        StringTokenizer st = new StringTokenizer(toSplit);
        while (st.hasMoreTokens())
        {
            String rule = st.nextToken();
            if (rule.startsWith("get"))
            {
                log.warn("Exclusions are based on property names and not method names. '" + rule + "' starts with 'get' so it looks like a method name and not a property name.");
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
            throw new IllegalArgumentException(Messages.getString("BeanConverter.OnlyIncludeOrExclude"));
        }

        inclusions = new ArrayList<String>();

        String toSplit = includes.replace(",", " ");
        StringTokenizer st = new StringTokenizer(toSplit);
        while (st.hasMoreTokens())
        {
            String rule = st.nextToken();
            if (rule.startsWith("get"))
            {
                log.warn("Inclusions are based on property names and not method names. '" + rule + "' starts with 'get' so it looks like a method name and not a property name.");
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

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.NamedConverter#getInstanceType()
     */
    public Class<?> getInstanceType()
    {
        return instanceType;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.NamedConverter#setInstanceType(java.lang.Class)
     */
    public void setInstanceType(Class<?> instanceType)
    {
        this.instanceType = instanceType;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BaseV20Converter#setConverterManager(org.directwebremoting.ConverterManager)
     */
    @Override
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
    protected boolean isAllowedByIncludeExcludeRules(String property)
    {
        if (exclusions != null)
        {
            // Check each exclusions and return false if we get a match
            for (String exclusion : exclusions)
            {
                if (property.equals(exclusion))
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
            for (String inclusion : inclusions)
            {
                if (property.equals(inclusion))
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
     * Loop over all the inputs and extract a Map of key:value pairs
     * @param paramType The type we are converting to
     * @param value The input string
     * @return A Map of the tokens in the string
     * @throws MarshallException If the marshalling fails
     */
    protected static Map<String, String> extractInboundTokens(Class<?> paramType, String value) throws MarshallException
    {
        Map<String, String> tokens = new HashMap<String, String>();
        StringTokenizer st = new StringTokenizer(value, ProtocolConstants.INBOUND_MAP_SEPARATOR);
        int size = st.countTokens();

        for (int i = 0; i < size; i++)
        {
            String token = st.nextToken();
            if (token.trim().length() == 0)
            {
                continue;
            }

            int colonpos = token.indexOf(ProtocolConstants.INBOUND_MAP_ENTRY);
            if (colonpos == -1)
            {
                throw new MarshallException(paramType, Messages.getString("BeanConverter.MissingSeparator", ProtocolConstants.INBOUND_MAP_ENTRY, token));
            }

            String key = token.substring(0, colonpos).trim();
            String val = token.substring(colonpos + 1).trim();
            tokens.put(key, val);
        }

        return tokens;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.NamedConverter#getJavascript()
     */
    public String getJavascript()
    {
        return javascript;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.convert.NamedConverter#setJavascript(java.lang.String)
     */
    public void setJavascript(String javascript)
    {
        this.javascript = javascript;
    }

    /**
     * The javascript class name for the converted objects
     */
    protected String javascript = null;

    /**
     * The list of excluded properties
     */
    protected List<String> exclusions = null;

    /**
     * The list of included properties
     */
    protected List<String> inclusions = null;

    /**
     * A type that allows us to fulfill an interface or subtype requirement
     */
    protected Class<?> instanceType = null;

    /**
     * To forward marshalling requests
     */
    protected ConverterManager converterManager = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BasicObjectConverter.class);
}
