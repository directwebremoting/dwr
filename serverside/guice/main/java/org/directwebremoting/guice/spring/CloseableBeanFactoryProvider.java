package org.directwebremoting.guice.spring;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;

import com.google.inject.Provider;

/**
 * Lazily creates a singleton BeanFactory and, when {@code close()} is
 * called, destroys it if it exists and is a {@code DisposableBean}.
 * @author Tim Peierls
 */
class CloseableBeanFactoryProvider implements Closeable, Provider<BeanFactory>
{
    CloseableBeanFactoryProvider(BeanFactoryLoader loader)
    {
        this.loader = loader;
    }

    /* (non-Javadoc)
     * @see com.google.inject.Provider#get()
     */
    public synchronized BeanFactory get()
    {
        if (beanFactory == null)
        {
            beanFactory = loader.loadBeanFactory();
        }
        return beanFactory;
    }

    /* (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    public synchronized void close() throws IOException
    {
        if (beanFactory != null && beanFactory instanceof DisposableBean)
        {
            try
            {
                ((DisposableBean) beanFactory).destroy();
                log.info("Destroyed BeanFactory from Guice provider.");
            }
            catch (IOException e)
            {
                log.info("Caught IO exception destroying BeanFactory: " + e);
                throw e;
            }
            catch (Exception e)
            {
                log.info("Unexpected exception while destroying BeanFactory: " + e);
                throw new RuntimeException(e);
            }
        }
    }

    private final BeanFactoryLoader loader;

    /* @GuardedBy("this") */ private BeanFactory beanFactory;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(CloseableBeanFactoryProvider.class);
}
