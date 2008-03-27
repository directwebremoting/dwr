package uk.ltd.getahead.dwr.create;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.UrlResource;
import org.springframework.web.servlet.support.RequestContextUtils;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.ExecutionContext;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * A creator that relies on a spring bean factory
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SpringCreator extends AbstractCreator implements Creator
{
    /**
     * @return Returns the resourceName.
     */
    public String getResourceName()
    {
        return resourceName;
    }

    /**
     * @param resourceName The resourceName to set.
     */
    public void setResourceName(String resourceName)
    {
        this.resourceName = resourceName;
    }

    /**
     * @param session true if we should use a session
     * @deprecated Use scope instead of the session param
     */
    public void setSession(String session)
    {
        if (Boolean.valueOf(session).booleanValue())
        {
            setScope("session"); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.create.Creator#init(org.w3c.dom.Element)
     */
    public void setProperties(Map params) throws IllegalArgumentException
    {
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
                log.error("Failed to instansiate object to detect type.", ex); //$NON-NLS-1$
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
            Object reply = null;

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
                        log.info("Loading spring config via the classloader from " + url.toExternalForm()); //$NON-NLS-1$
                    }
                    else
                    {
                        url = ExecutionContext.get().getServletContext().getResource(resourceName);
                        if (url != null)
                        {
                            log.info("Loading spring config via servlet context from " + url.toExternalForm()); //$NON-NLS-1$
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
                        ExecutionContext execCtx = ExecutionContext.get();
                        ServletContext srvCtx = execCtx.getServletContext();
                        HttpServletRequest req = execCtx.getHttpServletRequest();

                        // factory = WebApplicationContextUtils.getWebApplicationContext(srvCtx);
                        factory = RequestContextUtils.getWebApplicationContext(req, srvCtx);
                    }
                }
            }

            if (factory == null)
            {
                throw new InstantiationException(Messages.getString("SpringCreator.MissingFactory", resourceName)); //$NON-NLS-1$
            }
            
            reply = factory.getBean(getJavascript());

            return reply;
        }
        catch (RuntimeException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            log.error("Error", ex); //$NON-NLS-1$
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

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(SpringCreator.class);

    /**
     * The Spring beans factory from which we get our beans
     */
    private static BeanFactory factory = null;

    /**
     * The cached type of bean that we are creating
     */
    private Class clazz = null;

    /**
     * An optional place to look for a beans.xml file
     */
    private String resourceName = null;

    /**
     * An array of locations to search through for a beans.xml file
     */
    private String[] configLocation = null;
}
