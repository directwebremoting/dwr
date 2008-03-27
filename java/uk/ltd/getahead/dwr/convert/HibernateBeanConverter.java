package uk.ltd.getahead.dwr.convert;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.OutboundVariable;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * BeanConverter that works with Hibernate3 to get BeanInfo.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HibernateBeanConverter extends BeanConverter
{
    /**
     * Simple ctor
     * @throws ClassNotFoundException If Hibernate can not be configured
     */
    public HibernateBeanConverter() throws ClassNotFoundException
    {
        try
        {
            hibernate = Class.forName(CLASS_HIBERNATE3);
            log.info("Found Hibernate3 class: " + hibernate.getName()); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            try
            {
                hibernate = Class.forName(CLASS_HIBERNATE2);
                log.info("Found Hibernate2 class: " + hibernate.getName()); //$NON-NLS-1$
            }
            catch (Exception ex2)
            {
                throw new ClassNotFoundException(Messages.getString("HibernateBeanConverter.MissingClass")); //$NON-NLS-1$
            }
        }

        try
        {
            getClass = hibernate.getMethod("getClass", new Class[] { Object.class }); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            throw new ClassNotFoundException(Messages.getString("HibernateBeanConverter.MissingGetClass")); //$NON-NLS-1$
        }

        try
        {
            isPropertyInitialized = hibernate.getMethod("isPropertyInitialized", new Class[] { Object.class, String.class }); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
            log.info("Hibernate.isPropertyInitialized() is not available in Hibernate2 so initialization checks will not take place"); //$NON-NLS-1$
        }
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
        try
        {
            Class clazz = (Class) getClass.invoke(null, new Object[] { bean });
            BeanInfo info = Introspector.getBeanInfo(clazz);
            return info;
        }
        catch (IntrospectionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            log.error("Logic Error", ex); //$NON-NLS-1$
            throw new IntrospectionException(ex.getMessage());
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
                    // We don't marshall getClass()
                    if (name.equals("class")) //$NON-NLS-1$
                    {
                        continue;
                    }

                    // We dont marshall things we can't read
                    Method getter = descriptor.getReadMethod();
                    if (getter == null)
                    {
                        continue;
                    }

                    // We don't marshall un-initialized properties for Hibernate3
                    if (isPropertyInitialized != null)
                    {
                        Object reply = isPropertyInitialized.invoke(null, new Object[] { data, name });
                        boolean inited = ((Boolean) reply).booleanValue();
                        if (!inited)
                        {
                            continue;
                        }
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

    private static final String CLASS_HIBERNATE2 = "net.sf.hibernate.Hibernate"; //$NON-NLS-1$

    private static final String CLASS_HIBERNATE3 = "org.hibernate.Hibernate"; //$NON-NLS-1$

    private Class hibernate;

    private Method getClass;
    
    private Method isPropertyInitialized;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(HibernateBeanConverter.class);
}
