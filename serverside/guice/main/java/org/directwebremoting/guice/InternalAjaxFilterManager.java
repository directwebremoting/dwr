package org.directwebremoting.guice;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.impl.DefaultAjaxFilterManager;
import org.directwebremoting.util.LocalUtil;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;

import static org.directwebremoting.guice.DwrGuiceUtil.*;

/**
 * Extends an existing ajax filter manager with an injected list of ajax filters
 * specified at Guice bind-time. Only to be used in conjunction with
 * {@link DwrGuiceServlet}.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class InternalAjaxFilterManager implements AjaxFilterManager
{
    /**
     * Retrieves an underlying ajaxFilter manager from thread-local state
     * to which this class delegates {@link AjaxFilterManager} calls.
     */
    public InternalAjaxFilterManager()
    {
        this.ajaxFilterManager = getAjaxFilterManager();
        addAjaxFilters();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.AjaxFilterManager#getAjaxFilters(java.lang.String)
     */
    public List<AjaxFilter> getAjaxFilters(String scriptname)
    {
        return ajaxFilterManager.getAjaxFilters(scriptname);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.AjaxFilterManager#addAjaxFilter(org.directwebremoting.AjaxFilter)
     */
    public void addAjaxFilter(AjaxFilter filter)
    {
        ajaxFilterManager.addAjaxFilter(filter);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.AjaxFilterManager#addAjaxFilter(org.directwebremoting.AjaxFilter, java.lang.String)
     */
    public void addAjaxFilter(AjaxFilter filter, String scriptname)
    {
        ajaxFilterManager.addAjaxFilter(filter, scriptname);
    }

    private final AjaxFilterManager ajaxFilterManager;

    private void addAjaxFilters()
    {
        Injector injector = getInjector();
        for (Key<?> key : injector.getBindings().keySet())
        {
            Class<?> atype = key.getAnnotationType();
            if (atype != null && Filtering.class.isAssignableFrom(atype))
            {
                String scriptName = Filtering.class.cast(key.getAnnotation()).value();
                @SuppressWarnings("unchecked")
                Provider<AjaxFilter> provider = injector.getProvider((Key<AjaxFilter>) key);
                if ("".equals(scriptName))
                {
                    addAjaxFilter(new InternalAjaxFilter(provider));
                }
                else
                {
                    addAjaxFilter(new InternalAjaxFilter(provider), scriptName);
                }
            }
        }
    }


    /**
     * Stores a type name in a thread-local variable for later retrieval by
     * {@code getAjaxFilterManager}.
     */
    static void setTypeName(String name)
    {
        typeName.set(name);
    }

    private static AjaxFilterManager getAjaxFilterManager()
    {
        String name = typeName.get();
        try
        {
            @SuppressWarnings("unchecked")
            Class<? extends AjaxFilterManager> cls = (Class<? extends AjaxFilterManager>) LocalUtil.classForName(name);
            return cls.newInstance();
        }
        catch (Exception e)
        {
            if (name != null && !"".equals(name))
            {
                log.warn("Couldn't make AjaxFilterManager from type: " + name);
            }
            return new DefaultAjaxFilterManager();
        }
    }


    /**
     * Place to stash a type name for retrieval in same thread.
     */
    private static final ThreadLocal<String> typeName = new ThreadLocal<String>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(InternalAjaxFilterManager.class);
}
