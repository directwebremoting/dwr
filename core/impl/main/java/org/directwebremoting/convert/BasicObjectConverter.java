package org.directwebremoting.convert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.ConstructorProperty;
import org.directwebremoting.extend.ConvertUtil;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NamedConverter;
import org.directwebremoting.extend.ObjectOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Pair;

/**
 * BasicObjectConverter is a parent to {@link BeanConverter} and
 * {@link ObjectConverter} an provides support for include and exclude lists,
 * and instanceTypes.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BasicObjectConverter implements NamedConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        if (data.isNull())
        {
            return null;
        }

        String value = data.getValue();
        if (value == null)
        {
            throw new NullPointerException(data.toString());
        }

        // If the text is null then the whole bean is null
        if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
        {
            return null;
        }

        if (!value.startsWith(ProtocolConstants.INBOUND_MAP_START) || !value.endsWith(ProtocolConstants.INBOUND_MAP_END))
        {
            log.warn("Expected object while converting data for " + paramType.getName() + " in " + data.getContext().getCurrentProperty() + ". Passed: " + value);
            throw new ConversionException(paramType, "Data conversion error. See logs for more details.");
        }

        value = value.substring(1, value.length() - 1);

        try
        {
            // Loop through the properties passed in
            Map<String, String> tokens = extractInboundTokens(paramType, value);

            if (paramsString == null)
            {
                return createUsingSetterInjection(paramType, data, tokens);
            }
            else
            {
                return createUsingConstructorInjection(paramType, data, tokens);
            }
        }
        catch (InvocationTargetException ex)
        {
            throw new ConversionException(paramType, ex.getTargetException());
        }
        catch (ConversionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ConversionException(paramType, ex);
        }
    }

    /**
     * We're using constructor injection, create and populate a bean using
     * a constructor
     */
    private Object createUsingConstructorInjection(Class<?> paramType, InboundVariable data, Map<String, String> tokens) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        Class<?> type;
        if (instanceType != null)
        {
            type = instanceType;
        }
        else
        {
            type = paramType;
        }

        // Find a constructor to match
        List<Class<?>> parameterTypes = new ArrayList<Class<?>>();
        for (Pair<Class<?>, String> parameter : parameters)
        {
            parameterTypes.add(parameter.left);
        }
        Class<?>[] paramTypeArray = parameterTypes.toArray(new Class<?>[parameterTypes.size()]);

        Constructor<?> constructor;
        try
        {
            constructor = type.getConstructor(paramTypeArray);
        }
        catch (NoSuchMethodException ex)
        {
            log.error("Can't find a constructor for " + type.getName() + " with params " + parameterTypes);
            throw ex;
        }

        List<Object> arguments = new ArrayList<Object>();

        int paramNum = 0;
        for (Pair<Class<?>, String> parameter : parameters)
        {
            String argument = tokens.get(parameter.right);
            ConstructorProperty property = new ConstructorProperty(constructor, parameter.right, paramNum);
            Object output = convert(argument, parameter.left, data.getContext(), property);
            arguments.add(output);
            paramNum++;
        }

        // log.debug("Using constructor injection for: " + constructor);

        Object[] argArray = arguments.toArray(new Object[arguments.size()]);

        try
        {
            return constructor.newInstance(argArray);
        }
        catch (InstantiationException ex)
        {
            log.error("Error building using constructor " + constructor.getName() + " with arguments " + argArray);
            throw ex;
        }
        catch (IllegalAccessException ex)
        {
            log.error("Error building using constructor " + constructor.getName() + " with arguments " + argArray);
            throw ex;
        }
        catch (InvocationTargetException ex)
        {
            log.error("Error building using constructor " + constructor.getName() + " with arguments " + argArray);
            throw ex;
        }
    }

    /**
     * We're using setter injection, create and populate a bean using property
     * setters
     */
    private Object createUsingSetterInjection(Class<?> paramType, InboundVariable data, Map<String, String> tokens) throws InstantiationException, IllegalAccessException
    {
        Object bean;
        if (instanceType != null)
        {
            bean = instanceType.newInstance();
        }
        else
        {
            bean = createParameterInstance(paramType);
        }

        // We should put the new object into the working map in case it
        // is referenced later nested down in the conversion process.
        if (instanceType != null)
        {
            data.getContext().addConverted(data, instanceType, bean);
        }
        else
        {
            data.getContext().addConverted(data, paramType, bean);
        }

        Map<String, Property> properties = getPropertyMapFromObject(bean, false, true);

        for (Entry<String, String> entry : tokens.entrySet())
        {
            String key = entry.getKey();

            // TODO: We don't URL decode method names we probably should. This is $dwr
            // TODO: We should probably have stripped these out already
            if (key.startsWith("%24dwr"))
            {
                continue;
            }

            Property property = properties.get(key);
            if (property == null)
            {
                log.warn("Missing setter: " + paramType.getName() + ".set" + Character.toTitleCase(key.charAt(0)) + key.substring(1) + "() to match javascript property: " + key + ". Check include/exclude rules and overloaded methods.");
                continue;
            }

            Object output = convert(entry.getValue(), property.getPropertyType(), data.getContext(), property);
            property.setValue(bean, output);
        }
        return bean;
    }

    /**
     * Extension point for subclasses to instantiate parameter classes.
     *
     * @param paramType The type of parameter that needs to be instantiated
     * @return An instance of <code>paramType</code>
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected Object createParameterInstance(Class<?> paramType) throws InstantiationException, IllegalAccessException
    {
        return paramType.newInstance();
    }

    /**
     * We have inbound data and a type that we need to convert it into, this
     * method performs the conversion.
     */
    protected Object convert(String val, Class<?> propType, InboundContext inboundContext, Property property)
    {
        String[] split = ConvertUtil.splitInbound(val);
        String splitValue = split[ConvertUtil.INBOUND_INDEX_VALUE];
        String splitType = split[ConvertUtil.INBOUND_INDEX_TYPE];

        InboundVariable nested = new InboundVariable(inboundContext, null, splitType, splitValue);
        nested.dereference();
        Property incc = createTypeHintContext(inboundContext, property);

        return converterManager.convertInbound(propType, nested, incc);
    }

    /**
     * {@link #convertInbound} needs to create a {@link Property} for the
     * {@link Property} it is converting so that the type guessing system can do
     * its work.
     * <p>The method of generating a {@link Property} is different for
     * the {@link BeanConverter} and the {@link ObjectConverter}.
     * @param inctx The parent context
     * @param property The property being converted
     * @return The new TypeHintContext
     */
    protected abstract Property createTypeHintContext(InboundContext inctx, Property property);

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        // Where we collect out converted children
        Map<String, OutboundVariable> ovs = new TreeMap<String, OutboundVariable>();

        // We need to do this before collecting the children to save recursion
        ObjectOutboundVariable ov = new ObjectOutboundVariable(outctx, data.getClass(), getJavascript());
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
        catch (ConversionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ConversionException(data.getClass(), ex);
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
            throw new IllegalArgumentException("Can't have both inclusions and exclusions for a Converter");
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
            throw new IllegalArgumentException("Can't have both inclusions and exclusions for a Converter");
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
     * @throws ConversionException If the marshalling fails
     */
    protected static Map<String, String> extractInboundTokens(Class<?> paramType, String value) throws ConversionException
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
                throw new ConversionException(paramType, "Missing " + ProtocolConstants.INBOUND_MAP_ENTRY + " in object description: " + token);
            }

            String key = token.substring(0, colonpos).trim();
            String val = token.substring(colonpos + 1).trim();
            tokens.put(key, val);
        }

        return tokens;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#setConverterManager(org.directwebremoting.extend.ConverterManager)
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
     * To forward marshalling requests
     */
    protected ConverterManager converterManager = null;

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

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#getJavascriptSuperClass()
     */
    public String getJavascriptSuperClass()
    {
        return javascriptSuperClass;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.NamedConverter#setJavascriptSuperClass(java.lang.String)
     */
    public void setJavascriptSuperClass(String javascriptSuperClass)
    {
        this.javascriptSuperClass = javascriptSuperClass;
    }

    /**
     * The javascript class name that will appear as superclass
     * for the converted objects
     */
    protected String javascriptSuperClass = null;

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

    /**
     * A type that allows us to fulfill an interface or subtype requirement
     */
    protected Class<?> instanceType = null;

    /**
     * Set the parameters to be used for constructor injection
     * @param paramsString a comma separated list of type/name pairs.
     */
    public void setConstructor(String paramsString)
    {
        this.paramsString = paramsString;

        // Convert a paramString into a list of parameters
        StringTokenizer st = new StringTokenizer(paramsString, ",");
        while (st.hasMoreTokens())
        {
            String paramString = st.nextToken().trim();
            String[] paramParts = paramString.split(" ");

            if (paramParts.length != 2)
            {
                log.error("Parameter list includes parameter '" + paramString + "' that can't be parsed into a [type] and [name]");
                throw new IllegalArgumentException("Badly formatted constructor parameter list. See log console for details.");
            }

            String typeName = paramParts[0];

            try
            {
                Class<?> type = LocalUtil.classForName(typeName);
                Pair<Class<?>, String> parameter = new Pair<Class<?>, String>(type, paramParts[1]);
                parameters.add(parameter);
            }
            catch (ClassNotFoundException ex)
            {
                if (typeName.contains("."))
                {
                    log.error("Parameter list includes unknown type '" + typeName + "'");
                    throw new IllegalArgumentException("Unknown type in constructor parameter list. See log console for details.");
                }
                else
                {
                    try
                    {
                        Class<?> type = LocalUtil.classForName("java.lang." + typeName);
                        Pair<Class<?>, String> parameter = new Pair<Class<?>, String>(type, paramParts[1]);
                        parameters.add(parameter);
                    }
                    catch (ClassNotFoundException ex2)
                    {
                        log.error("Parameter list includes unknown type '" + typeName + "' (also tried java.lang." + typeName + ")");
                        throw new IllegalArgumentException("Unknown type in constructor parameter list. See log console for details.");
                    }
                }
            }
        }
    }

    /**
     * Returns an immutable List of the inclusions.
     *
     * @return
     */
    public List<String> getInclusions() {
        return Collections.unmodifiableList(this.inclusions);
    }

    /**
     * Stored temporarily until the {@link #instanceType} is known so we can
     * parse for constructor injection
     */
    protected String paramsString;

    /**
     * The constructor to use if we are doing constructor injection
     */
    protected Map<Class<?>, Constructor<?>> constructorCache = new HashMap<Class<?>, Constructor<?>>();

    /**
     * If we are doing constructor injection, this is the type list
     */
    protected final List<Pair<Class<?>, String>> parameters = new ArrayList<Pair<Class<?>,String>>();

    /**
     * The list of excluded properties
     */
    protected List<String> exclusions = null;

    /**
     * The list of included properties
     */
    protected List<String> inclusions = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BasicObjectConverter.class);
}
