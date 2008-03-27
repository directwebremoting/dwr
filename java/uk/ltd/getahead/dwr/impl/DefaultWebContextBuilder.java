package uk.ltd.getahead.dwr.impl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ltd.getahead.dwr.Container;
import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextBuilder;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * A WebContextBuilder that creates DefaultWebContexts.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultWebContextBuilder implements WebContextBuilder
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContextBuilder#set(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletConfig, javax.servlet.ServletContext)
     */
    public void set(HttpServletRequest request, HttpServletResponse response, ServletConfig config, ServletContext context, Container container)
    {
        try
        {
            WebContext ec = new DefaultWebContext(request, response, config, context, container);
            user.set(ec);
        }
        catch (Exception ex)
        {
            log.fatal("Failed to create an ExecutionContext", ex); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContextBuilder#get()
     */
    public WebContext get()
    {
        return (WebContext) user.get();
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.WebContextBuilder#unset()
     */
    public void unset()
    {
        user.set(null);
    }

    /**
     * The storage of thread based data
     */
    private static ThreadLocal user = new ThreadLocal();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultWebContextBuilder.class);
}
