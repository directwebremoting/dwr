package org.directwebremoting.filter;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.spring.SpringCreator;
import org.directwebremoting.util.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Wrap the current call in a transaction.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SpringTransactionAjaxFilter implements AjaxFilter
{
    /**
     * @return Returns the isolationLevel.
     */
    public String getIsolationLevel()
    {
        return isolationLevel;
    }

    /**
     * @param isolationLevel The isolationLevel to set.
     */
    public void setIsolationLevel(String isolationLevel)
    {
        this.isolationLevel = isolationLevel;
    }

    /**
     * @return Returns the propagationBehavior.
     */
    public String getPropagationBehavior()
    {
        return propagationBehavior;
    }

    /**
     * @param propagationBehavior The propagationBehavior to set.
     */
    public void setPropagationBehavior(String propagationBehavior)
    {
        this.propagationBehavior = propagationBehavior;
    }

    /**
     * @return Returns the readOnly.
     */
    public boolean isReadOnly()
    {
        return readOnly;
    }

    /**
     * @param readOnly The readOnly to set.
     */
    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    /**
     * @return Returns the timeout.
     */
    public int getTimeout()
    {
        return timeout;
    }

    /**
     * @param timeout The timeout to set.
     */
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    /**
     * @return Returns the transactionName.
     */
    public String getTransactionName()
    {
        return transactionName;
    }

    /**
     * @param transactionName The transactionName to set.
     */
    public void setTransactionName(String transactionName)
    {
        this.transactionName = transactionName;
    }

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
     * @see org.directwebremoting.AjaxFilter#doFilter(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], org.directwebremoting.AjaxFilterChain)
     */
    public Object doFilter(final Object object, final Method method, final Object[] params, final AjaxFilterChain chain) throws Exception
    {
        if (tt == null)
        {
            tt = getTransactionTemplate();
        }

        try
        {
            return tt.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus status)
                {
                    try
                    {
                        return chain.doFilter(object, method, params);
                    }
                    catch (RuntimeException ex)
                    {
                        throw ex;
                    }
                    catch (Exception ex)
                    {
                        throw new TransactionSystemException("Transaction Failure", ex); //$NON-NLS-1$
                    }
                }
            });
        }
        catch (TransactionSystemException ex)
        {
            throw (Exception) ex.getCause();
        }
    }

    private TransactionTemplate getTransactionTemplate() throws InstantiationException
    {
        try
        {
            if (factory == null)
            {
                factory = getBeanFactory();
            }

            if (factory == null)
            {
                log.error("DWR can't find a spring config. See following info logs for solutions"); //$NON-NLS-1$
                log.info("- Option 1. In dwr.xml, <filter class='...TransactionBoundaryAjaxFilter'/> add <param name='location1' value='beans.xml'/> for each spring config file."); //$NON-NLS-1$
                log.info("- Option 2. Use a spring org.springframework.web.context.ContextLoaderListener."); //$NON-NLS-1$
            }

            if (beanName == null)
            {
                log.error("Missing beanName parameter from SpringTransactionAjaxFilter. beanName should specify a spring bean that implements TransactionTemplate."); //$NON-NLS-1$
            }

            TransactionTemplate reply = (TransactionTemplate) factory.getBean(beanName);

            if (isolationLevel != null)
            {
                reply.setIsolationLevelName(isolationLevel);
            }

            if (transactionName != null)
            {
                reply.setName(transactionName);
            }

            if (propagationBehavior != null)
            {
                reply.setPropagationBehaviorName(propagationBehavior);
            }

            // This assumes that the default is not read-only (which is the
            // sensible default). We check first to save making the call when
            // we don't need to. i.e. by default we leave spring to how it is
            // set up by default
            if (readOnly)
            {
                reply.setReadOnly(true);
            }

            if (timeout != -1)
            {
                reply.setTimeout(timeout );
            }

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

        ServletContext srvCtx = WebContextFactory.get().getServletContext();
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();

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
     * Spring transaction setting
     */
    private String isolationLevel;

    /**
     * Spring transaction setting
     */
    private String transactionName;

    /**
     * Spring transaction setting
     */
    private String propagationBehavior;

    /**
     * Spring transaction setting
     */
    private boolean readOnly;

    /**
     * Spring transaction setting
     */
    private int timeout = -1;

    /**
     * The name of the spring bean we want to create
     */
    private String beanName = null;

    /**
     * The Spring beans factory from which we get our beans.
     */
    private BeanFactory factory = null;

    /**
     * An array of locations to search through for a beans.xml file
     */
    private String[] configLocation = null;

    /**
     * The Spring transaction interface
     */
    private TransactionTemplate tt;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(SpringCreator.class);
}
