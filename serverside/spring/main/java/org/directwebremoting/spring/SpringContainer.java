package org.directwebremoting.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.extend.ContainerConfigurationException;
import org.directwebremoting.extend.UninitializingBean;
import org.directwebremoting.impl.DefaultContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.ClassUtils;

/**
 * A <code>Container</code> implementation that looks up all beans from the
 * configuration specified in a Spring context.
 * It loads the configuration from a Spring web application context.
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SpringContainer extends DefaultContainer implements Container, BeanFactoryAware, UninitializingBean
{
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.DefaultContainer#addParameter(java.lang.String, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addParameter(String askFor, Object valueParam) throws ContainerConfigurationException
    {
        try
        {
            Class<?> clz = LocalClassUtils.forName(askFor, ClassUtils.getDefaultClassLoader());

            Map<String, Object> beansOfType = (Map<String, Object>) ((ListableBeanFactory) beanFactory).getBeansOfType(clz);

            if (beansOfType.isEmpty())
            {
                super.addParameter(askFor, valueParam);
            }
            else if (beansOfType.size() > 1)
            {
                // TODO: handle multiple declarations
                throw new ContainerConfigurationException("multiple beans of type '" + clz.getName() + "' were found in the spring configuration");
            }
            else
            {
                beans.put(askFor, beansOfType.values().iterator().next());
            }
        }
        catch (ClassNotFoundException ex)
        {
            super.addParameter(askFor, valueParam);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.DefaultContainer#getBean(java.lang.String)
     */
    @Override
    public Object getBean(String id)
    {
        Object reply;
        try
        {
            reply = beanFactory.getBean(id);
        }
        catch (BeansException ex)
        {
            // Spring throws on not-found, we return null.
            reply = super.getBean(id);
        }
        catch (IllegalStateException ex)
        {
            // Spring contexts can get shutdown and throw. It's not ideal to
            // hide spring context beans, but we don't have much choice.
            return super.getBean(id);
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.DefaultContainer#getBeanNames()
     */
    @Override
    public Collection<String> getBeanNames()
    {
        List<String> names = new ArrayList<String>();

        // Snarf the beans from Spring
        if (beanFactory instanceof ListableBeanFactory)
        {
            try
            {
                ListableBeanFactory listable = (ListableBeanFactory) beanFactory;
                names.addAll(Arrays.asList(listable.getBeanDefinitionNames()));
            }
            catch (IllegalStateException ex)
            {
                log.warn("List of beanNames does not include Spring beans since the BeanFactory was closed when we tried to read it.");
            }
        }
        else
        {
            log.warn("List of beanNames does not include Spring beans since your BeanFactory is not a ListableBeanFactory.");
        }

        // And append the DWR ones
        names.addAll(super.getBeanNames());

        return Collections.unmodifiableCollection(names);
    }

    /**
     * Avoids initialization of lazy-init beans in Spring context.
     */
    @Override
    protected void callInitializingBeans()
    {
        callInitializingBeans(super.getBeanNames());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.impl.DefaultContainer#destroy()
     */
    @Override
    public void destroy()
    {
        // This override prevents us from trying to poke around in the Spring
        // BeanFactory which gets Spring all confused
        // We're being destroyed, we shouldn't be touching Spring beans
        super.destroy(super.getBeanNames());
    }

    /**
     * The Spring BeanFactory that we read from
     */
    protected BeanFactory beanFactory;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(SpringContainer.class);
}
