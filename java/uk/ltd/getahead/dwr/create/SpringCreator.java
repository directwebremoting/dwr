package uk.ltd.getahead.dwr.create;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.UrlResource;
import org.springframework.web.context.support.WebApplicationContextUtils;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.ExecutionContext;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.util.Log;

/**
 * A creator that relies on a spring bean factory
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SpringCreator implements Creator
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.create.Creator#init(org.w3c.dom.Element)
     */
    public void init(Map params) throws IllegalArgumentException
    {
        this.beanName = (String) params.get("beanName"); //$NON-NLS-1$
        this.resourceName = (String) params.get("resourceName"); //$NON-NLS-1$

        List locValues = new ArrayList();

        for (Iterator it = params.keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            String value = (String) params.get(key);
            if (key.startsWith("location")) //$NON-NLS-1$
            {
                locValues.add(value);
            }
        }

        configLocation = (String[]) locValues.toArray(new String[locValues.size()]);
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getType()
     */
    public Class getType()
    {
        if (clazz == null)
        {
            try
            {
                clazz = getInstance().getClass();
            }
            catch (InstantiationException ex)
            {
                Log.error("Failed to instansiate object to detect type.", ex); //$NON-NLS-1$
                return Object.class;
            }
        }

        return clazz;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        try
        {
            if (factory == null)
            {
                // If someone has set a resource name then we need to load that.
                if (configLocation != null && configLocation.length > 0)
                {
                    factory = new ClassPathXmlApplicationContext(configLocation);
                }
                else if (resourceName != null)
                {
                    URL url = getClass().getClassLoader().getResource(resourceName);
                    if (url != null)
                    {
                        Log.info("Loading spring config via the classloader from " + url.toExternalForm()); //$NON-NLS-1$
                    }
                    else
                    {
                        url = ExecutionContext.get().getServletContext().getResource(resourceName);
                        if (url != null)
                        {
                            Log.info("Loading spring config via servlet context from " + url.toExternalForm()); //$NON-NLS-1$
                        }
                        else
                        {
                            throw new InstantiationException(Messages.getString("SpringCreator.ResourceNameInvalid", resourceName)); //$NON-NLS-1$
                        }
                    }

                    UrlResource resource = new UrlResource(url);
                    factory = new XmlBeanFactory(resource);
                }
                else
                {
                    if (factory == null)
                    {
                        ServletContext cx = ExecutionContext.get().getServletContext();
                        factory = WebApplicationContextUtils.getWebApplicationContext(cx);
                    }
                }
            }

            return factory.getBean(beanName);
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            Log.error("Error", ex); //$NON-NLS-1$
            throw new InstantiationException(ex.toString());
        }
    }

    /**
     * @param factory The factory to set.
     */
    public static void setXmlBeanFactory(BeanFactory factory)
    {
        SpringCreator.factory = factory;
    }

    private static BeanFactory factory = null;

    private Class clazz = null;

    private String beanName = null;

    private String resourceName = null;

    private String[] configLocation = null;
}
