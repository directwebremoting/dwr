package org.directwebremoting.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.directwebremoting.Container;
import org.directwebremoting.extend.ContainerConfigurationException;
import org.directwebremoting.extend.UninitializingBean;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Loggers;

/**
 * DefaultContainer is like a mini-IoC container for DWR.
 * At least it is an IoC container by interface (check: no params that have
 * anything to do with DWR), but it is hard coded specifically for DWR. If we
 * want to make more of it we can later, but this is certainly not going to
 * become a full blown IoC container.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultContainer extends AbstractContainer implements Container
{
    /**
     * Set the class that should be used to implement the given interface
     * @param base The interface to implement
     * @param bean The new implementation
     * @throws ContainerConfigurationException If the specified beans could not be used
     */
    public <T> void addBean(Class<T> base, T bean)
    {
        addParameter(LocalUtil.originalDwrClassName(base.getName()), bean);
    }

    /**
     * Set the class that should be used to implement the given interface
     * @param base The interface to implement
     * @param implementation The new implementation
     * @throws ContainerConfigurationException If the specified beans could not be used
     */
    public <T> void addImplementation(Class<T> base, Class<? extends T> implementation)
    {
        addParameter(LocalUtil.originalDwrClassName(base.getName()), implementation.getName());
    }

    /**
     * Set the class that should be used to implement the given interface
     * @param base The interface to implement
     * @param implementation The new implementation
     * @throws ContainerConfigurationException If the specified beans could not be used
     */
    public <T> void addImplementationOption(Class<T> base, Class<? extends T> implementation)
    {
        Object existingOptions = beans.get(base.getName());
        if (existingOptions == null)
        {
            beans.put(LocalUtil.originalDwrClassName(base.getName()), implementation.getName());
        }
        else
        {
            beans.put(LocalUtil.originalDwrClassName(base.getName()), existingOptions + " " + implementation.getName());
        }
    }

    /**
     * Add a parameter to the container as a possibility for injection
     * @param askFor The key that will be looked up
     * @param valueParam The value to be returned from the key lookup
     * @throws ContainerConfigurationException If the specified beans could not be used
     */
    public void addParameter(String askFor, Object valueParam) throws ContainerConfigurationException
    {
        Object value = valueParam;

        // Maybe the value is a classname that needs instantiating
        if (value instanceof String)
        {
            try
            {
                Class<?> impl = LocalUtil.classForName((String) value);
                value = impl.newInstance();
            }
            catch (ClassNotFoundException ex)
            {
                // it's not a classname, leave it
            }
            catch (InstantiationException ex)
            {
                throw new ContainerConfigurationException("Unable to instantiate " + value);
            }
            catch (IllegalAccessException ex)
            {
                throw new ContainerConfigurationException("Unable to access " + value);
            }
        }

        // If we have an instantiated value object and askFor is an interface
        // then we can check that one implements the other
        if (!(value instanceof String))
        {
            try
            {
                Class<?> iface = LocalUtil.classForName(askFor);
                if (!iface.isAssignableFrom(value.getClass()))
                {
                    Loggers.STARTUP.error("Can't cast: " + value + " to " + askFor);
                }
            }
            catch (ClassNotFoundException ex)
            {
                // it's not a classname, leave it
            }
        }

        if (Loggers.STARTUP.isDebugEnabled())
        {
            if (value instanceof String)
            {
                Loggers.STARTUP.debug("- value: " + askFor + " = " + value);
            }
            else
            {
                Loggers.STARTUP.debug("- impl:  " + askFor + " = " + value.getClass().getName());
            }
        }

        beans.put(askFor, value);
    }

    /**
     * Retrieve a previously set parameter
     * @param name The parameter name to retrieve
     * @return The value of the specified parameter, or null if one is not set
     */
    public String getParameter(String name)
    {
        Object value = beans.get(name);
        return (value == null) ? null : value.toString();
    }

    /**
     * Called to indicate that we finished adding parameters.
     * The thread safety of a large part of DWR depends on this function only
     * being called from {@link Servlet#init(javax.servlet.ServletConfig)},
     * where all the setup is done, and where we depend on the undocumented
     * feature of all servlet containers that they complete the init process
     * of a Servlet before they begin servicing requests.
     * @see DefaultContainer#addParameter(String, Object)
     * @noinspection UnnecessaryLabelOnContinueStatement
     */
    public void setupFinished()
    {
        // We try to autowire each bean in turn
        for (Object bean : beans.values())
        {
            initializeBean(bean);
        }

        callInitializingBeans();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Container#newInstance(java.lang.Class)
     */
    public <T> T newInstance(Class<T> type) throws InstantiationException, IllegalAccessException
    {
        T t = type.newInstance();
        initializeBean(t);
        return t;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Container#initializeBean(java.lang.Object)
     */
    public void initializeBean(Object bean)
    {
        // HACK: It wouldn't be a good idea to start injecting into objects
        // created by the app-server. Currently this is just ServletContext
        // and ServletConfig. If it is others in the future then we'll need a
        // better way of marking objects that should not be injected into.
        // It's worth remembering that the Container is itself in the container
        // so there is a vague risk of recursion here if we're not careful

        if (bean instanceof ServletContext || bean instanceof ServletConfig)
        {
            Loggers.STARTUP.debug("- skipping injecting into: " + bean.getClass().getName());
            return;
        }

        if (!(bean instanceof String))
        {
            Loggers.STARTUP.debug("- autowire: " + bean.getClass().getName());

            Method[] methods = bean.getClass().getMethods();
            methods:
            for (Method setter : methods)
            {
                if (setter.getName().startsWith("set") &&
                        setter.getName().length() > 3 &&
                        setter.getParameterTypes().length == 1)
                {
                    String name = Character.toLowerCase(setter.getName().charAt(3)) + setter.getName().substring(4);
                    Class<?> propertyType = setter.getParameterTypes()[0];

                    // First we try auto-wire by name
                    Object setting = beans.get(name);
                    if (setting != null)
                    {
                        if (propertyType.isAssignableFrom(setting.getClass()))
                        {
                            Loggers.STARTUP.debug("  - by name: " + name + " = " + setting);
                            invoke(setter, bean, setting);

                            continue methods;
                        }
                        else if (setting.getClass() == String.class)
                        {
                            try
                            {
                                Object value = LocalUtil.simpleConvert((String) setting, propertyType);

                                Loggers.STARTUP.debug("  - by name: " + name + " = " + value);
                                invoke(setter, bean, value);
                            }
                            catch (IllegalArgumentException ex)
                            {
                                // Ignore - this was a speculative convert anyway
                            }

                            continue methods;
                        }
                    }

                    // Next we try autowire-by-type
                    Object value = beans.get(LocalUtil.originalDwrClassName(propertyType.getName()));
                    if (value != null)
                    {
                        Loggers.STARTUP.debug("  - by type: " + name + " = " + value.getClass().getName());
                        invoke(setter, bean, value);

                        continue methods;
                    }

                    Loggers.STARTUP.debug("  - no properties for: " + name);
                }
            }
        }
    }

    /**
     * A helper to do the reflection.
     * This helper throws away all exceptions, preferring to log.
     * @param setter The method to invoke
     * @param bean The object to invoke the method on
     * @param value The value to assign to the property using the setter method
     */
    private static void invoke(Method setter, Object bean, Object value)
    {
        try
        {
            setter.invoke(bean, value);
        }
        catch (InvocationTargetException ex)
        {
            Loggers.STARTUP.error("  - Exception during auto-wire: ", ex.getTargetException());
        }
        catch (Exception ex)
        {
            Loggers.STARTUP.error("  - Error calling setter: " + setter, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Container#getBean(java.lang.String)
     */
    public Object getBean(String id)
    {
        return beans.get(id);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Container#getBeanNames()
     */
    public Collection<String> getBeanNames()
    {
        return Collections.unmodifiableCollection(beans.keySet());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Container#destroy()
     */
    public synchronized void destroy()
    {
        if (isDestroyed)
        {
            return;
        }

        isDestroyed = true;
        destroy(getBeanNames());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Container#destroy()
     */
    public void destroy(Collection<String> beanNames)
    {
        Loggers.STARTUP.debug("Destroy for container: " + getClass().getSimpleName());
        for (String beanName : beanNames)
        {
            Object bean = getBean(beanName);
            if (bean instanceof UninitializingBean && !(bean instanceof Container))
            {
                UninitializingBean scl = (UninitializingBean) bean;
                Loggers.STARTUP.debug("- For contained bean: " + beanName);
                scl.destroy();
            }
        }
    }

    /**
     * The beans that we know of indexed by type
     */
    protected Map<String, Object> beans = new TreeMap<String, Object>();

    /**
     * Keep track of whether we have already been destroyed
     */
    boolean isDestroyed = false;
}
