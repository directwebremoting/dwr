package org.directwebremoting.guice.spring;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * A BeanFactory loader that loads a WebApplicationContext given a servlet context.
 * @author Tim Peierls
 */
public class WebApplicationContextLoader implements BeanFactoryLoader
{
    public WebApplicationContextLoader(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.guice.spring.BeanFactoryLoader#loadBeanFactory()
     */
    public BeanFactory loadBeanFactory()
    {
        new ContextLoader().initWebApplicationContext(servletContext);
        return WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

    private final ServletContext servletContext;
}
