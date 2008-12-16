/*
 * Copyright 2006 Maik Schreiber <blizzy AT blizzy DOT de>
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
package org.directwebremoting.annotations;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.Container;
import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.create.NewCreator;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.Configurator;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
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
        Class<? extends Creator> creator = createAnn.creator();
        String creatorClass = creator.getName();
        Map<String, String> creatorParams = getParamsMap(createAnn.creatorParams());
        ScriptScope scope = createAnn.scope();

        CreatorManager creatorManager = container.getBean(CreatorManager.class);
        String creatorName = creatorClass.replace(".", "_");
        creatorManager.addCreatorType(creatorName, creatorClass);

        Map<String, String> params = new HashMap<String, String>();
        if (NewCreator.class.isAssignableFrom(creator))
        {
            params.put("class", clazz.getName());
        }
        params.putAll(creatorParams);
        params.put("scope", scope.getValue());

        String name = createAnn.name();
        if (name == null || name.length() == 0)
        {
            name = clazz.getSimpleName();
        }

        try
        {
            Loggers.STARTUP.debug("Adding class " + clazz.getName() + " as " + name);
            creatorManager.addCreator(name, creatorName, params);
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
                accessControl.addIncludeRule(name, method.getName());

                Auth authAnn = method.getAnnotation(Auth.class);
                if (authAnn != null)
                {
                    for (String role : authAnn.role())
                    {
                        accessControl.addRoleRestriction(name, method.getName(), role);
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
                processFilter(filter, name, container);
            }
        }
        // process single filter for convenience
        else
        {
            Filter filterAnn = clazz.getAnnotation(Filter.class);
            if (filterAnn != null)
            {
                processFilter(filterAnn, name, container);
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

        ConverterManager converterManager = container.getBean(ConverterManager.class);
        String converterName = converterClass.replace(".", "_");
        converterManager.addConverterType(converterName, converterClass);

        if (BeanConverter.class.isAssignableFrom(converter))
        {
            StringBuilder properties = new StringBuilder();
            Set<Field> fields = new HashSet<Field>();
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            fields.addAll(Arrays.asList(clazz.getFields()));
            for (Field field : fields)
            {
                if (field.getAnnotation(RemoteProperty.class) != null)
                {
                    properties.append(',').append(field.getName());
                }
            }

            for (Method method : clazz.getMethods())
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
