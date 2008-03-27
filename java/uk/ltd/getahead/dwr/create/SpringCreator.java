package uk.ltd.getahead.dwr.create;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.ExecutionContext;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * A creator that relies on a spring bean factory.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SpringCreator extends AbstractCreator implements Creator
{
    /**
     * @return Returns the beanName.
     */
    public String getBeanName()
    {
        return beanName;
    }

    /**
     * @param beanName The beanName to set.
     */
    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
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
                log.debug("Adding configLocation: " + value + " from parameter: " + key); //$NON-NLS-1$ //$NON-NLS-2$
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
            if (overrideFactory != null)
            {
                Object reply = overrideFactory.getBean(beanName);
                return reply;
            }

            if (factory == null)
            {
                factory = getBeanFactory();
            }

            if (factory == null)
            {
                log.error("DWR can't find a spring config. See following info logs for solutions"); //$NON-NLS-1$
                log.info("- Option 1. In dwr.xml, <create creator='spring' ...> add <param name='location1' value='beans.xml'/> for each spring config file."); //$NON-NLS-1$
                log.info("- Option 2. Use a spring org.springframework.web.context.ContextLoaderListener."); //$NON-NLS-1$
                log.info("- Option 3. Call SpringCreator.setOverrideBeanFactory() from your web-app"); //$NON-NLS-1$
            }

            Object reply = factory.getBean(beanName);
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
     * @return A found BeanFactory configuration
     */
    private BeanFactory getBeanFactory()
    {
        // If someone has set a resource name then we need to load that.
        if (configLocation != null && configLocation.length > 0)
        {
            log.info("Spring BeanFactory via ClassPathXmlApplicationContext using " + configLocation.length + "configLocations."); //$NON-NLS-1$ //$NON-NLS-2$
            return new ClassPathXmlApplicationContext(configLocation);
        }

        ServletContext srvCtx = ExecutionContext.get().getServletContext();
        HttpServletRequest request = ExecutionContext.get().getHttpServletRequest();

        if (request != null)
        {
            return RequestContextUtils.getWebApplicationContext(request, srvCtx);
        }
        else
        {
            return WebApplicationContextUtils.getWebApplicationContext(srvCtx);
        }
    }

    /**
     * Set a web-app wide BeanFactory.
     * This method is misnamed (The parameter is a BeanFactory and not a
     * XmlBeanFactory)
     * @param factory The factory to set.
     * @deprecated This method is misnamed use setOverrideBeanFactory
     */
    public static void setXmlBeanFactory(BeanFactory factory)
    {
        SpringCreator.overrideFactory = factory;
    }

    /**
     * Set a web-app wide BeanFactory.
     * @param factory The factory to set.
     */
    public static void setOverrideBeanFactory(BeanFactory factory)
    {
        SpringCreator.overrideFactory = factory;
    }

    /**
     * The name of the spring bean we want to create
     */
    private String beanName = null;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(SpringCreator.class);

    /**
     * The Spring beans factory from which we get our beans.
     */
    private BeanFactory factory = null;

    /**
     * An web-app wide BeanFactory that we can use to override any calculated
     * ones.
     */
    private static BeanFactory overrideFactory = null;

    /**
     * The cached type of bean that we are creating
     */
    private Class clazz = null;

    /**
     * An array of locations to search through for a beans.xml file
     */
    private String[] configLocation = null;
}
