package uk.ltd.getahead.dwr;

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
            if (iface == CreatorManager.class)
            {
                creatorManager = (CreatorManager) impl.newInstance();
            }
            else if (iface == ConverterManager.class)
            {
                converterManager = (ConverterManager) impl.newInstance();
            }
            else if (iface == AccessControl.class)
            {
                accessControl = (AccessControl) impl.newInstance();
            }
            else if (iface == DWRProcessor.class)
            {
                processor = (DWRProcessor) impl.newInstance();
            }
            else if (iface == ExecutionContext.class)
            {
                executionContext = (ExecutionContext) impl.newInstance();
            }
            else
            {
                log.error("Factory does not manage: " + interfaceName); //$NON-NLS-1$
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
        // So supply defaults for any implementations that have not been
        // configured
        try
        {
            if (creatorManager == null)
            {
                setImplementation(CreatorManager.class.getName(), CreatorManager.class.getName());
            }

            if (converterManager == null)
            {
                setImplementation(ConverterManager.class.getName(), ConverterManager.class.getName());
            }

            if (processor == null)
            {
                setImplementation(DWRProcessor.class.getName(), DWRProcessor.class.getName());
            }

            if (executionContext == null)
            {
                setImplementation(ExecutionContext.class.getName(), ExecutionContext.class.getName());
            }

            if (accessControl == null)
            {
                setImplementation(AccessControl.class.getName(), AccessControl.class.getName());
            }

            if (configuration == null)
            {
                setImplementation(Configuration.class.getName(), Configuration.class.getName());
            }
        }
        catch (Exception ex)
        {
            log.fatal("Internal error setting defaults", ex); //$NON-NLS-1$
            throw new IllegalStateException(ex.getMessage());
        }

        // And wire them together
        ExecutionContext.setImplementation(executionContext.getClass());

        processor.setConverterManager(converterManager);
        processor.setCreatorManager(creatorManager);
        processor.setAccessControl(accessControl);

        configuration.setConverterManager(converterManager);
        configuration.setCreatorManager(creatorManager);
        configuration.setAccessControl(accessControl);
    }

    /**
     * Get an instance of a bean of a given type.
     * @param type The type to get an instance of
     * @return The object of the given type
     */
    public Object getBean(Class type)
    {
        if (type == CreatorManager.class)
        {
            return creatorManager;
        }
        else if (type == ConverterManager.class)
        {
            return converterManager;
        }
        else if (type == AccessControl.class)
        {
            return accessControl;
        }
        else if (type == DWRProcessor.class)
        {
            return processor;
        }
        else if (type == ExecutionContext.class)
        {
            return executionContext;
        }
        else if (type == Configuration.class)
        {
            return configuration;
        }

        throw new IllegalArgumentException("Factory can't create a " + type.getName()); //$NON-NLS-1$
    }

    /**
     * How we create new beans
     */
    protected CreatorManager creatorManager = null;

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * The security manager
     */
    protected AccessControl accessControl = null;

    /**
     * The thing that actually does the work
     */
    private DWRProcessor processor = null;

    /**
     * Container for the HTTP objects for deep thread access
     */
    private ExecutionContext executionContext = null;

    /**
     * The dwr.xml parser
     */
    private Configuration configuration = new Configuration();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(Factory.class);
}
