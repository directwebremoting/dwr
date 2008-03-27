package org.getahead.dwrdemo.gidemo;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.util.Logger;

/**
 * A generator of random objects to push to GI
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Publisher implements Runnable
{
    /**
     * Create a new publish thread and start it
     */
    public Publisher()
    {
        WebContext webContext = WebContextFactory.get();
        ServletContext servletContext = webContext.getServletContext();

        serverContext = ServerContextFactory.get(servletContext);

        new Thread(this).start();
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        try
        {
            while (!Thread.currentThread().isInterrupted())
            {
                Collection sessions = serverContext.getScriptSessionsByPage("/dwr/gi/index.html");
                ScriptProxy proxy = new ScriptProxy(sessions);

                Corporation corp = corporations.getNextChangedCorporation();
                proxy.addFunctionCall("OpenAjax.publish", "gidemo", "corporation", corp);
            }

            log.info("Publisher: Stopping server-side thread");
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * The set of corporations that we manage
     */
    private Corporations corporations = new Corporations();

    /**
     * We use DWRs ServerContext to find users of a given page
     */
    private ServerContext serverContext;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(Publisher.class);

    /*
        Object bean = webContext.getContainer().getBean(PageNormalizer.class.getName());
        if (bean instanceof DefaultPageNormalizer)
        {
            DefaultPageNormalizer normalizer = (DefaultPageNormalizer) bean;
            normalizer.setServletContext(servletContext);
        }
     */
}
