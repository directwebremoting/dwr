package org.directwebremoting.impl;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.extend.InitializingBean;
import org.directwebremoting.util.LocalUtil;

/**
 * An implementation of some of the simpler methods from {@link Container}
 * This class has nothing whatsoever to do with
 * {@link org.directwebremoting.extend.ContainerAbstraction}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractContainer implements Container
{
    /**
     * Used after setup to call {@link InitializingBean#afterContainerSetup(Container)}
     * on all the contained beans.
     */
    protected void callInitializingBeans()
    {
        callInitializingBeans(getBeanNames());
    }

    /**
     * Call {@link InitializingBean#afterContainerSetup(Container)} on the named
     * beans
     * @param beanNames The beans to setup.
     */
    protected void callInitializingBeans(Collection<String> beanNames)
    {
        for (String name : beanNames)
        {
            Object bean = getBean(name);
            if (bean instanceof InitializingBean)
            {
                InitializingBean startMeUp = (InitializingBean) bean;
                startMeUp.afterContainerSetup(this);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Container#getBean(java.lang.Class)
     */
    public <T> T getBean(Class<T> type)
    {
        Object bean = getBean(LocalUtil.originalDwrClassName(type.getName()));
        try
        {
            return type.cast(bean);
        }
        catch (ClassCastException ex)
        {
            log.error("ClassCastException: Asked for implementation of " + type.getName() + " but the container has a type " + bean.getClass().getName());
            log.error("  - calling toString() on returned bean gives: " + bean);
            throw ex;
        }
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(AbstractContainer.class);
}
