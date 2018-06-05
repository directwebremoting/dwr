package org.directwebremoting.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AbstractCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.LocalUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

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

    /**
     * What sort of class do we create?
     * @param classname The name of the class
     */
    public void setClass(String classname)
    {
        try
        {
            this.clazz = LocalUtil.classForName(classname);
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException("Class not found: " + classname, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.create.Creator#init(org.w3c.dom.Element)
     */
    @Override
    public void setProperties(Map<String, String> params) throws IllegalArgumentException
    {
        List<String> locValues = new ArrayList<String>();

        for (Map.Entry<String, String> entry : params.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.startsWith("location"))
            {
                log.debug("Adding configLocation: " + value + " from parameter: " + key);
                locValues.add(value);
            }
        }

        configLocation = locValues.toArray(new String[locValues.size()]);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getType()
     */
    public Class<?> getType()
    {
        if (clazz == null)
        {
            try
            {
                clazz = getInstance().getClass();
            }
            catch (InstantiationException ex)
            {
                log.error("Failed to instansiate object to detect type.", ex);
                return Object.class;
            }
        }

        return clazz;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        try
        {
            if (overrideFactory != null)
            {
                return overrideFactory.getBean(beanName);
            }

            if (factory == null)
            {
                factory = getBeanFactory();
            }

            if (factory == null)
            {
                log.error("DWR can't find a spring config. See following info logs for solutions");
                log.info("- Option 1. In dwr.xml, <create creator='spring' ...> add <param name='location1' value='beans.xml'/> for each spring config file.");
                log.info("- Option 2. Use a spring org.springframework.web.context.ContextLoaderListener.");
                log.info("- Option 3. Call SpringCreator.setOverrideBeanFactory() from your web-app");
                throw new InstantiationException("DWR can't find a spring config. See the logs for solutions");
            }

            return factory.getBean(beanName);
        }
        catch (InstantiationException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new InstantiationException("Illegal Access to default constructor on " + clazz.getName() + " due to: " + ex);
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
            log.info("Spring BeanFactory via ClassPathXmlApplicationContext using " + configLocation.length + "configLocations.");
            return new ClassPathXmlApplicationContext(configLocation);
        }

        ServletContext srvCtx = ServerContextFactory.get().getServletContext();

        HttpServletRequest request = null;
        try
        {
            request = WebContextFactory.get().getHttpServletRequest();
        }
        catch (Exception ex)
        {
            // Probably on boot time
        }
        return request != null ? RequestContextUtils.getWebApplicationContext(request, srvCtx) : WebApplicationContextUtils.getWebApplicationContext(srvCtx);
    }

    /**
     * Set a web-app wide BeanFactory.
     * This method is misnamed (The parameter is a BeanFactory and not a
     * XmlBeanFactory)
     * @param factory The factory to set.
     * @deprecated This method is misnamed use setOverrideBeanFactory
     */
    @Deprecated
    public static void setXmlBeanFactory(BeanFactory factory)
    {
        overrideFactory = factory;
    }

    /**
     * Set a web-app wide BeanFactory.
     * @param factory The factory to set.
     */
    public static void setOverrideBeanFactory(BeanFactory factory)
    {
        overrideFactory = factory;
    }

    /**
     * The name of the spring bean we want to create
     */
    private String beanName = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(SpringCreator.class);

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
    private Class<?> clazz = null;

    /**
     * An array of locations to search through for a beans.xml file
     */
    private String[] configLocation = null;
}
