package org.directwebremoting.hibernate;

import java.beans.PropertyDescriptor;

import javax.servlet.ServletContext;

import org.directwebremoting.ConversionException;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.PropertyDescriptorProperty;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

/**
 * A {@link Property} that catches Hibernate exceptions.
 * This is useful for Hibernate 2 where lazy loading results in an exception
 * and you are unable to detect and prevent this.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class H3PropertyDescriptorProperty extends PropertyDescriptorProperty
{
    /**
     * Simple constructor
     * @param descriptor The PropertyDescriptor that we are proxying to
     */
    public H3PropertyDescriptorProperty(PropertyDescriptor descriptor)
    {
        super(descriptor);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.PropertyDescriptorProperty#getValue(java.lang.Object)
     */
    @Override
    public Object getValue(Object bean) throws ConversionException
    {
        if (!(bean instanceof HibernateProxy))
        {
            // This is not a hibernate dynamic proxy, just use it
            return super.getValue(bean);
        }
        else
        {
            // If the property is already initialized, use it
            boolean initialized = Hibernate.isPropertyInitialized(bean, descriptor.getName());
            if (initialized)
            {
                // This might be a lazy-collection so we need to double check
                Object reply = super.getValue(bean);
                initialized = Hibernate.isInitialized(reply);
            }

            if (initialized)
            {
                return super.getValue(bean);
            }
            else
            {
                // If the session bound to the property is live, use it
                HibernateProxy proxy = (HibernateProxy) bean;
                LazyInitializer initializer = proxy.getHibernateLazyInitializer();
                SessionImplementor implementor = initializer.getSession();
                if (implementor.isOpen())
                {
                    return super.getValue(bean);
                }

                // So the property needs database access, and the session is closed
                // We'll need to try get another session
                ServletContext context = WebContextFactory.get().getServletContext();
                Session session = H3SessionAjaxFilter.getCurrentSession(context);

                if (session != null)
                {
                    session.update(bean);
                    return super.getValue(bean);
                }

                return null;
            }
        }
    }
}
