package uk.ltd.getahead.dwr.convert;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * Convert a Javascript associative array into a JavaBean
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BeanConverter implements Converter
{
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
            BeanInfo info = getBeanInfo(bean);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            Map props = new HashMap();
            for (int i = 0; i < descriptors.length; i++)
            {
                String key = descriptors[i].getName();
                props.put(key, descriptors[i]);
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

                PropertyDescriptor descriptor = (PropertyDescriptor) props.get(key);
                Method setter = descriptor.getWriteMethod();
                if (setter == null)
                {
                    log.warn("No visible setter for " + key); //$NON-NLS-1$
                    StringBuffer all = new StringBuffer();
                    for (Iterator it = props.keySet().iterator(); it.hasNext();)
                    {
                        all.append(it.next());
                        if (it.hasNext())
                        {
                            all.append(',');
                        }
                    }
                    log.warn("Setters exist for (" + all + ')'); //$NON-NLS-1$
                }
                else
                {
                    Class propType = descriptor.getPropertyType();

                    String[] split = LocalUtil.splitInbound(val);
                    InboundVariable nested = new InboundVariable(iv.getLookup(), split[LocalUtil.INBOUND_INDEX_TYPE], split[LocalUtil.INBOUND_INDEX_VALUE]);

                    inctx.pushContext(setter, 0);
                    Object output = config.convertInbound(propType, nested, inctx);
                    inctx.popContext(setter, 0);
                    setter.invoke(bean, new Object[] { output });
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
        buffer.append(" = new Object();"); //$NON-NLS-1$

        try
        {
            BeanInfo info = getBeanInfo(data);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; i++)
            {
                PropertyDescriptor descriptor = descriptors[i];
                String name = descriptor.getName();

                try
                {
                    // We don't marshall things we can't read
                    Method getter = descriptor.getReadMethod();
                    if (getter == null)
                    {
                        continue;
                    }

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

                    Object value = getter.invoke(data, new Object[0]);
                    OutboundVariable nested = getConverterManager().convertOutbound(value, outctx);

                    // Make sure the nested thing is declared
                    buffer.append(nested.getInitCode());

                    // And now declare our stuff
                    buffer.append(varname);
                    buffer.append('.');
                    buffer.append(name);
                    buffer.append(" = "); //$NON-NLS-1$
                    buffer.append(nested.getAssignCode());
                    buffer.append(';');
                }
                catch (Exception ex)
                {
                    log.warn("Failed to convert " + name, ex); //$NON-NLS-1$
                }
            }
        }
        catch (IntrospectionException ex)
        {
            throw new ConversionException(ex);
        }

        return buffer.toString();
    }

    /**
     * HibernateBeanConverter (and maybe others) may want to provide alternate
     * versions of bean.getClass()
     * @param bean The class to find bean info from
     * @return BeanInfo for the given class
     * @throws IntrospectionException
     */
    protected BeanInfo getBeanInfo(Object bean) throws IntrospectionException
    {
        BeanInfo info = Introspector.getBeanInfo(bean.getClass());
        return info;
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
    private static final Logger log = Logger.getLogger(BeanConverter.class);

    /**
     * To forward marshalling requests
     */
    private ConverterManager config = null;
}
