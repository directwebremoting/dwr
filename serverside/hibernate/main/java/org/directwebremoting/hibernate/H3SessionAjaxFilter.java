package org.directwebremoting.hibernate;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.WebContextFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * An {@link AjaxFilter} that uses DWR Hibernate support classes to do a
 * {@link Session#beginTransaction()} before passing the control on to the chain
 * and a {@link Transaction#commit()} after.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class H3SessionAjaxFilter implements AjaxFilter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.AjaxFilter#doFilter(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], org.directwebremoting.AjaxFilterChain)
     */
    public Object doFilter(Object object, Method method, Object[] params, AjaxFilterChain chain) throws Exception
    {
        ServletContext context = WebContextFactory.get().getServletContext();
        SessionFactory sessionFactory = (SessionFactory) context.getAttribute(ATTRIBUTE_SESSION);

        Transaction transaction = null;
        if (sessionFactory != null)
        {
            Session session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();
        }
        else
        {
            log.error("SessionFactory not initialized for this web application. Use: H3SessionAjaxFilter.setSessionFactory(servletContext, sessionFactory);");
        }

        Object reply = chain.doFilter(object, method, params);

        if (transaction != null)
        {
            transaction.commit();
        }

        return reply;
    }

    /**
     * Assigns a {@link SessionFactory} to a {@link ServletContext} so DWR knows
     * how to get hold of a {@link org.hibernate.Session}.
     * @param context The Servlet environment to store the ServletContext in
     * @param sessionFactory The Hibernate session factory to register
     */
    public static void setSessionFactory(ServletContext context, SessionFactory sessionFactory)
    {
        context.setAttribute(ATTRIBUTE_SESSION, sessionFactory);
    }

    /**
     * Get access to a Session, given the {@link SessionFactory} linked in
     * {@link #setSessionFactory(ServletContext, SessionFactory)}
     * @param context The webapp to link the calls together
     * @return A Session from the {@link SessionFactory} or null if
     * {@link #setSessionFactory(ServletContext, SessionFactory)} has not been
     * called for this {@link ServletContext}
     */
    public static Session getCurrentSession(ServletContext context)
    {
        SessionFactory sessionFactory = (SessionFactory) context.getAttribute(ATTRIBUTE_SESSION);
        if (sessionFactory == null)
        {
            return null;
        }

        return sessionFactory.getCurrentSession();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(H3SessionAjaxFilter.class);

    /**
     * Under what name do we store the session factory?
     */
    protected static final String ATTRIBUTE_SESSION = "org.directwebremoting.hibernate.SessionFactory";
}
