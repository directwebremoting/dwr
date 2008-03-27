package uk.ltd.getahead.dwr;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.ltd.getahead.dwr.util.Logger;

/**
 * Factory is like a mini-IoC container for DWR.
 * At least it is an IoC container by interface (check: no params that have
 * anything to do with DWR), but it is hard coded specifically for DWR. If we
 * want to make more of it we can later, but this is certainly not going to
 * become a full blown IoC container.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Factory
{
    /**
     * Set the class that should be used to implement the given interface
     * @param interfaceName The interface to implement
     * @param implName The new implementation
     */
    public void setImplementation(String interfaceName, String implName)
    {
        Class iface = null;
        Class impl = null;

        try
        {
            iface = Class.forName(interfaceName);
        }
        catch (ClassNotFoundException ex)
        {
            log.error("Class not found: " + interfaceName); //$NON-NLS-1$
            return;
        }

        try
        {
            impl = Class.forName(implName);
        }
        catch (ClassNotFoundException ex)
        {
            log.error("Class not found: " + implName); //$NON-NLS-1$
            return;
        }

        try
        {
            if (iface != AccessControl.class && iface != Configuration.class &&
                iface != ConverterManager.class && iface != CreatorManager.class && 
                iface != Processor.class && iface != ExecutionContext.class)
            {
                log.error("Factory does not manage: " + interfaceName); //$NON-NLS-1$
            }
            else
            {
                log.debug("Adding implementation of " + iface.getName() + ": " + impl.getName()); //$NON-NLS-1$ //$NON-NLS-2$
                beans.put(iface, impl.newInstance());
            }
        }
        catch (ClassCastException ex)
        {
            log.error("Can't cast: " + implName + " to " + interfaceName); //$NON-NLS-1$ //$NON-NLS-2$
        }
        catch (InstantiationException ex)
        {
            log.error("Failed to instansiate: " + implName, ex); //$NON-NLS-1$
        }
        catch (IllegalAccessException ex)
        {
            log.error("Construction denied for: " + implName); //$NON-NLS-1$
        }
    }

    /**
     * Called to indicate that we finished called setImplementation.
     * @see Factory#setImplementation(String, String)
     */
    public void configurationFinished()
    {
        // We try to autowire each bean in turn
        for (Iterator it = beans.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            // Class type = (Class) entry.getKey();
            Object ovalue = entry.getValue();

            Method[] methods = ovalue.getClass().getMethods();
            for (int i = 0; i < methods.length; i++)
            {
                Method method = methods[i];

                if (method.getName().startsWith("set") && //$NON-NLS-1$
                    method.getParameterTypes().length == 1)
                {
                    Class paramType = method.getParameterTypes()[0];

                    // We like to simply seach for the given parameter:
                    //   Object instance = beans.get(paramType);
                    // But that does not take inheritance into account
                    Object instance = null;
                    for (Iterator pit = beans.keySet().iterator(); instance == null && pit.hasNext();)
                    {
                        Class ptype = (Class) pit.next();
                        if (ptype.isAssignableFrom(paramType))
                        {
                            instance = beans.get(ptype);
                        }
                    }

                    // If we have a match
                    if (instance != null)
                    {
                        try
                        {
                            log.debug("Autowire: " + ovalue.getClass().getName() + "." + method.getName()); //$NON-NLS-1$ //$NON-NLS-2$
                            method.invoke(ovalue, new Object[] { instance });
                        }
                        catch (IllegalArgumentException ex)
                        {
                            log.error("Internal error", ex); //$NON-NLS-1$
                        }
                        catch (IllegalAccessException ex)
                        {
                            log.error("Permission issue invoking " + method.getName()); //$NON-NLS-1$
                        }
                        catch (InvocationTargetException ex)
                        {
                            log.error("Exception during auto-wire", ex.getTargetException()); //$NON-NLS-1$
                        }
                    }
                }
            }
        }
    }

    /**
     * Get an instance of a bean of a given type.
     * @param type The type to get an instance of
     * @return The object of the given type
     */
    public Object getBean(Class type)
    {
        Object reply = beans.get(type);
        if (reply == null)
        {
            throw new IllegalArgumentException("Factory can't find a " + type.getName()); //$NON-NLS-1$
        }

        return reply;
    }

    /**
     * The beans that we know of indexed by type
     */
    protected Map beans = new HashMap();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(Factory.class);
}
