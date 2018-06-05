package org.directwebremoting.guice;

import java.lang.reflect.Method;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;

import com.google.inject.Provider;

/**
 * Specialized Ajax filter implementation that uses a Provider to
 * look up instances to delegate to. This class is used by
 * {@link InternalAjaxFilterManager}.
 * @author Tim Peierls [tim at peierls dot net]
 */
class InternalAjaxFilter implements AjaxFilter
{
    InternalAjaxFilter(Provider<AjaxFilter> provider)
    {
        this.provider = provider;
    }

    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain)
        throws Exception
    {
        return provider.get().doFilter(obj, method, params, chain);
    }

    private final Provider<AjaxFilter> provider;
}
