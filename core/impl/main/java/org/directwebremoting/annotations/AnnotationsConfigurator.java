package org.directwebremoting.annotations;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.Container;
import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.util.ClasspathScanner;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Loggers;

/**
 * A Configurator that works off Annotations.
 * @author Maik Schreiber [blizzy AT blizzy DOT de]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class AnnotationsConfigurator implements Configurator
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Configurator#configure(org.directwebremoting.Container)
     */
    public void configure(Container container)
    {
        for (Class<?> clazz : getClasses(container))
        {
            try
            {
                processClass(clazz, container);
            }
            catch (Exception ex)
            {
                Loggers.STARTUP.error("Failed to process class: " + clazz.getName(), ex);
            }
        }
    }

    /**
     * Allow subclasses to override the default way we find out which classes
     * have DWR annotations for us to work with
     * @param container Commonly we get configuration information from here
     * @return A set of classes with DWR annotations
     */
    protected Set<Class<?>> getClasses(Container container)
    {
        Set<Class<?>> classes = new HashSet<Class<?>>();

        Object data = container.getBean("classes");
        if (data != null)
        {
            if (data instanceof String)
            {
                String classesStr = (String) data;
                for (String element : classesStr.split(","))
                {
                    element = element.trim();
                    if (element.length() == 0)
                    {
                        continue;
                    }

                    if (element.endsWith(".*") || element.endsWith(".**")) {
                        try {
                            boolean recursive = element.endsWith(".**");
                            String packageName = element.substring(0, element.length() - 2 - (recursive ? 1 : 0));
                            Set<String> classesInPackage = new ClasspathScanner(packageName, recursive).getClasses();
                            for (String className : classesInPackage) {
                                Class<?> clazz = LocalUtil.classForName(className);
                                classes.add(clazz);
                            }
                        }
                        catch(Exception ex)
                        {
                            Loggers.STARTUP.error("Failed to process package: " + element, ex);
                            continue;
                        }
                    } else {
                        try
                        {
                            Class<?> clazz = LocalUtil.classForName(element);
                            classes.add(clazz);
                        }
                        catch (Exception ex)
                        {
                            Loggers.STARTUP.error("Failed to process class: " + element, ex);
                        }
                    }
                }
            }
            else
            {
                try
                {
                    classes.add(data.getClass());
                }
                catch (Exception ex)
                {
                    Loggers.STARTUP.error("Failed to process class: " + data.getClass().getName(), ex);
                }
            }
        }

        return classes;
    }

    /**
     * Process the annotations on a given class
     * @param clazz The class to search for annotations
     * @param container The IoC container to configure
     * @throws IllegalAccessException If annotation processing fails
     * @throws InstantiationException If annotation processing fails
     */
    protected void processClass(Class<?> clazz, Container container) throws InstantiationException, IllegalAccessException
    {
        RemoteProxy createAnn = clazz.getAnnotation(RemoteProxy.class);
        if (createAnn != null)
        {
            processCreate(clazz, createAnn, container);
        }

        DataTransferObject convertAnn = clazz.getAnnotation(DataTransferObject.class);
        if (convertAnn != null)
        {
            processConvert(clazz, convertAnn, container);
        }

        GlobalFilter globalFilterAnn = clazz.getAnnotation(GlobalFilter.class);
        if (globalFilterAnn != null)
        {
            processGlobalFilter(clazz, globalFilterAnn, container);
        }
    }

    /**
     * Process the @RemoteProxy annotation on a given class
     * @param clazz The class annotated with @RemoteProxy
     * @param createAnn The annotation
     * @param container The IoC container to configure
     */
    protected void processCreate(Class<?> clazz, RemoteProxy createAnn, Container container)
    {
        Class<? extends Creator> creatorClass = createAnn.creator();
        String creatorClassName = creatorClass.getName();

        CreatorManager creatorManager = container.getBean(CreatorManager.class);

        // Add a phony creator type just so we are able to do addCreator with the params collection later
        String creatorName = creatorClassName.replace(".", "_");
        creatorManager.addCreatorType(creatorName, creatorClassName);

        // Set up the params map from specific attributes and parameter attribute annotations
        Map<String, String> params = new HashMap<String, String>();
        params.putAll(getParamsMap(createAnn.creatorParams()));
        if (createAnn.name() != null && !createAnn.name().equals(""))
        {
            params.put("javascript", createAnn.name());
        }
        params.put("scope", createAnn.scope().getValue());

        // Add default class (remoted class)
        if (params.get("class") == null)
        {
            params.put("class", clazz.getName());
        }

        // Add default scriptName
        String scriptName = params.get("javascript");
        if (scriptName == null || scriptName.equals(""))
        {
            scriptName = clazz.getSimpleName();
            params.put("javascript", scriptName);
        }

        try
        {
            Loggers.STARTUP.debug("Adding class " + clazz.getName() + " as " + scriptName);
            creatorManager.addCreator(creatorName, params);
        }
        catch (Exception ex)
        {
            Loggers.STARTUP.error("Failed to add class as Creator: " + clazz.getName(), ex);
        }

        AccessControl accessControl = container.getBean(AccessControl.class);
        for (Method method : clazz.getMethods())
        {
            if (method.getAnnotation(RemoteMethod.class) != null)
            {
                accessControl.addIncludeRule(scriptName, method.getName());

                Auth authAnn = method.getAnnotation(Auth.class);
                if (authAnn != null)
                {
                    for (String role : authAnn.role())
                    {
                        accessControl.addRoleRestriction(scriptName, method.getName(), role);
                    }
                }
            }
        }

        Filters filtersAnn = clazz.getAnnotation(Filters.class);
        if (filtersAnn != null)
        {
            Filter[] fs = filtersAnn.value();
            for (Filter filter : fs)
            {
                processFilter(filter, scriptName, container);
            }
        }
        // process single filter for convenience
        else
        {
            Filter filterAnn = clazz.getAnnotation(Filter.class);
            if (filterAnn != null)
            {
                processFilter(filterAnn, scriptName, container);
            }
        }
    }

    /**
     * Process the @Filter annotation
     * @param filterAnn The filter annotation
     * @param name The Javascript name of the class to filter
     * @param container The IoC container to configure
     */
    protected void processFilter(Filter filterAnn, String name, Container container)
    {
        Map<String, String> filterParams = getParamsMap(filterAnn.params());
        AjaxFilter filter = LocalUtil.classNewInstance(name, filterAnn.type().getName(), AjaxFilter.class);
        if (filter != null)
        {
            LocalUtil.setParams(filter, filterParams, null);
            AjaxFilterManager filterManager = container.getBean(AjaxFilterManager.class);
            filterManager.addAjaxFilter(filter, name);
        }
    }

    /**
     * Process the @DataTransferObject annotation on a given class
     * @param clazz The class annotated with @DataTransferObject
     * @param convertAnn The annotation
     * @param container The IoC container to configure
     * @throws InstantiationException If there are problems instantiating the Converter
     * @throws IllegalAccessException If there are problems instantiating the Converter
     */
    protected void processConvert(Class<?> clazz, DataTransferObject convertAnn, Container container) throws InstantiationException, IllegalAccessException
    {
        Class<? extends Converter> converter = convertAnn.converter();
        String converterClass = converter.getName();
        Map<String, String> params = getParamsMap(convertAnn.params());
        if (LocalUtil.hasText(convertAnn.javascript()))
        {
            params.put("javascript", convertAnn.javascript());
        }

        ConverterManager converterManager = container.getBean(ConverterManager.class);
        String converterName = converterClass.replace(".", "_");
        converterManager.addConverterType(converterName, converterClass);

        if (BeanConverter.class.isAssignableFrom(converter))
        {
            StringBuilder properties = new StringBuilder();
            Class<?> superClazz = clazz;
            while (superClazz != null && superClazz != Object.class)
            {
                for (Field field : superClazz.getDeclaredFields())
                {
                    if (field.getAnnotation(RemoteProperty.class) != null)
                    {
                        properties.append(',').append(field.getName());
                    }
                }

                for (Method method : superClazz.getMethods())
                {
                    if (method.getAnnotation(RemoteProperty.class) != null)
                    {
                        String name = method.getName();
                        if (name.startsWith(METHOD_PREFIX_GET) || name.startsWith(METHOD_PREFIX_IS))
                        {
                            if (name.startsWith(METHOD_PREFIX_GET))
                            {
                                name = name.substring(3);
                            }
                            else
                            {
                                name = name.substring(2);
                            }
                            name = Introspector.decapitalize(name);
                            properties.append(',').append(name);
                        }
                    }
                }
                superClazz = superClazz.getSuperclass();
            }

            if (properties.length() > 0)
            {
                properties.deleteCharAt(0);
                params.put("include", properties.toString());
            }
        }

        converterManager.addConverter(clazz.getName(), converterName, params);
    }

    /**
     * Global Filters apply to all classes
     * @param clazz The class to use as a filter
     * @param globalFilterAnn The filter annotation
     * @param container The IoC container to configure
     * @throws InstantiationException In case we can't create the given clazz
     * @throws IllegalAccessException In case we can't create the given clazz
     */
    protected void processGlobalFilter(Class<?> clazz, GlobalFilter globalFilterAnn, Container container) throws InstantiationException, IllegalAccessException
    {
        if (!AjaxFilter.class.isAssignableFrom(clazz))
        {
            throw new IllegalArgumentException(clazz.getName() + " is not an AjaxFilter implementation");
        }

        Map<String, String> filterParams = getParamsMap(globalFilterAnn.params());
        AjaxFilter filter = (AjaxFilter) clazz.newInstance();
        if (filter != null)
        {
            LocalUtil.setParams(filter, filterParams, null);
            AjaxFilterManager filterManager = container.getBean(AjaxFilterManager.class);
            filterManager.addAjaxFilter(filter);
        }
    }

    /**
     * Utility to turn a Param array into a Map<String, String>.
     * @param params The params array from annotations
     * @return A Map<String, String>
     */
    protected Map<String, String> getParamsMap(Param[] params)
    {
        // TODO: Should we move this code into Param? Is that even possible?
        Map<String, String> result = new HashMap<String, String>();
        if (params != null)
        {
            for (Param param : params)
            {
                result.put(param.name(), param.value());
            }
        }
        return result;
    }

    /**
     * The getter prefix for boolean variables
     */
    private static final String METHOD_PREFIX_IS = "is";

    /**
     * The getter prefix for non-boolean variables
     */
    private static final String METHOD_PREFIX_GET = "get";
}
