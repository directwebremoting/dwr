package org.directwebremoting.hibernate;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Setup an in-process HSQLDB instance
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DatabaseInitServletContextListener implements ServletContextListener
{
    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent ev)
    {
        Database.init();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent ev)
    {
    }
}
