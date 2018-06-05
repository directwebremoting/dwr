package org.directwebremoting.guice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.servlet.DwrServlet;
import org.directwebremoting.util.FakeServletConfig;
import org.directwebremoting.util.LocalUtil;

import com.google.inject.Injector;
import com.google.inject.Key;

import static org.directwebremoting.guice.util.ContextCloseHandlers.*;

/**
 * An extension of the basic
 * {@link org.directwebremoting.servlet.DwrServlet DwrServlet}
 * that configures itself for dependency injection with Guice.
 * Must be used in conjunction with {@link DwrGuiceServletContextListener}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class DwrGuiceServlet extends DwrServlet
{
    /**
     * Copies DWR configuration values from the Guice bindings into
     * {@code servletConfig} to make these values accessible to the
     * standard DWR servlet configuration machinery.
     */
    @Override
    public void init(final ServletConfig servletConfig) throws ServletException
    {
        // Save this for later use by destroy.
        this.servletContext = servletConfig.getServletContext();

        // Set the current context thread-locally so our internal classes can
        // look up the Injector and use it in turn to look up further objects.
        try
        {
            DwrGuiceUtil.withServletContext(this.servletContext, new Callable<Void>()
            {
                public Void call() throws ServletException
                {
                    // Since ServletConfig is immutable, we use a modifiable
                    // decoration of the real servlet configuration and pass
                    // that to the init method of the superclass.
                    FakeServletConfig config = new FakeServletConfig(servletConfig);

                    // Apply settings configured at bind-time.
                    setInitParameters(config);

                    // Use our internal manager classes to replace and delegate to
                    // any user-specified or default implementations, after adding
                    // additional creators and converters registered at bind-time.
                    configureDelegatedTypes(config);

                    // Normal DwrServlet initialization happens here using the
                    // modified ServletConfig instead of the one we were passed.
                    DwrGuiceServlet.super.init(config);

                    // Objects with (non-global) application scope are initialized
                    // eagerly.
                    initApplicationScoped();

                    return null;
                }
            });
        }
        catch (ServletException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            // Can't happen:
            throw new AssertionError("unexpected exception: " + e);
        }
    }

    /**
     * Closes any {@code Closeable} application-scoped objects.
     * IO exceptions are collected but ignored.
     */
    @Override
    public void destroy()
    {
        ServletContext localContext = this.servletContext;
        this.servletContext = null;

        DwrGuiceUtil.withServletContext(localContext, new Runnable()
        {
            public void run()
            {
                // Closeable objects with (non-global) application scope are closed.
                List<Exception> exceptions = destroyApplicationScoped();

                DwrGuiceServlet.super.destroy();

                for (Exception ex : exceptions)
                {
                    log.warn("During servlet shutdown", ex);
                }
            }
        });
    }

    /**
     * Inject some values that might have been configured at bind-time.
     * Override web.xml <init-param> settings in each case that injection
     * is successful.
     */
    private void setInitParameters(FakeServletConfig config)
    {
        InjectedConfig cfg = new InjectedConfig(config);
        DwrGuiceUtil.getInjector().injectMembers(cfg);
        cfg.setParameters();
    }

    private void configureDelegatedTypes(FakeServletConfig config)
    {
        // Get the user-specified type names, if any, for CreatorManager
        // and ConverterManager and stash them (thread-locally) so that
        // InternalCreatorManager and InternalConverterManager can retrieve
        // them in their parameterless constructors.

        InternalCreatorManager.setTypeName(config.getInitParameter(INIT_CREATOR_MANAGER));
        InternalConverterManager.setTypeName(config.getInitParameter(INIT_CONVERTER_MANAGER));
        InternalAjaxFilterManager.setTypeName(config.getInitParameter(INIT_AJAX_FILTER_MANAGER));

        // Tell DWR to use our special delegating classes that know how to
        // create delegates of the appropriate type by looking at the type
        // names that we just stashed.

        config.setInitParameter(INIT_CREATOR_MANAGER, InternalCreatorManager.class.getName());
        config.setInitParameter(INIT_CONVERTER_MANAGER, InternalConverterManager.class.getName());
        config.setInitParameter(INIT_AJAX_FILTER_MANAGER, InternalAjaxFilterManager.class.getName());
    }


    private static void initApplicationScoped()
    {
        Injector injector = DwrGuiceUtil.getInjector();
        for (Key<?> key : DwrScopes.APPLICATION.getKeysInScope())
        {
            // Eagerly create application-scoped object.
            injector.getInstance(key);
        }
    }

    private static List<Exception> destroyApplicationScoped()
    {
        final List<Exception> exceptions = new ArrayList<Exception>();
        DwrScopes.APPLICATION.closeAll(newExceptionLoggingCloseableHandler(exceptions));
        return exceptions;
    }


    /**
     * Used to stash context for later use by destroy().
     */
    private volatile ServletContext servletContext;


    /**
     * The name DWR uses to look up a CreatorManager implementation class name
     */
    private static final String INIT_CREATOR_MANAGER = LocalUtil.originalDwrClassName(CreatorManager.class.getName());

    /**
     * The name DWR uses to look up a ConverterManager implementation class name
     */
    private static final String INIT_CONVERTER_MANAGER = LocalUtil.originalDwrClassName(ConverterManager.class.getName());

    /**
     * The name DWR uses to look up an AjaxFilterManager implementation class name
     */
    private static final String INIT_AJAX_FILTER_MANAGER = LocalUtil.originalDwrClassName(AjaxFilterManager.class.getName());

    /**
     * The log stream
     */
    private static final org.apache.commons.logging.Log log =
                         org.apache.commons.logging.LogFactory.getLog
                         (DwrGuiceServlet.class);
}
