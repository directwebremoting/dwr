package uk.ltd.getahead.dwr.create;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;

import org.w3c.dom.Element;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.ExecutionContext;
import uk.ltd.getahead.dwr.util.Log;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SpringCreator implements Creator
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.create.Creator#init(org.w3c.dom.Element)
     */
    public void init(Element config) throws IllegalArgumentException
    {
        this.beanName = config.getAttribute("beanName");
        this.resourceName = config.getAttribute("resourceName");
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
                ex.printStackTrace();
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
                if (resourceName != null)
                {
                    URL url = getClass().getClassLoader().getResource(resourceName);
                    if (url != null)
                    {
                        Log.info("Loading spring config via the classloader from " + url.toExternalForm());
                    }
                    else
                    {
                        url = ExecutionContext.getExecutionContext().getServletContext().getResource(resourceName);
                        if (url != null)
                        {
                            Log.info("Loading spring config via servlet context from " + url.toExternalForm());
                        }
                        else
                        {
                            throw new InstantiationException("Failed to find spring configuration file using resourceName=" + resourceName);
                        }
                    }

                    // Could use url.openStream() but then we can do relative url
                    // traversal.
                    Class cUrlResource = Class.forName("org.springframework.core.io.UrlResource");
                    Class cResource = Class.forName("org.springframework.core.io.Resource");
                    Class cXmlBeanFactory = Class.forName("org.springframework.beans.factory.xml.XmlBeanFactory");

                    Constructor ctorUrlResource = cUrlResource.getConstructor(new Class[] { URL.class });
                    Object resource = ctorUrlResource.newInstance(new Object[] { url });

                    Constructor factctor = cXmlBeanFactory.getConstructor(new Class[] { cResource });
                    factory = factctor.newInstance(new Object[] { resource });
                }
                else
                {
                    throw new InstantiationException("No spring XmlBeanFactory set. Either call uk.ltd.getahead.dwr.create.SpringCreator.setXmlBeanFactory() with a beanFactory or ");
                }
            }

            Method creator = factory.getClass().getMethod("getBean", new Class[] { String.class });
            Object reply = creator.invoke(factory, new Object[] { beanName });
            return reply;
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new InstantiationException(ex.toString());
        }
    }

    /**
     * @param factory The factory to set.
     */
    public static void setXmlBeanFactory(Object factory)
    {
        SpringCreator.factory = factory;
    }

    private static Object factory = null;
    private Class clazz;
    private String beanName;
    private String resourceName;
}
