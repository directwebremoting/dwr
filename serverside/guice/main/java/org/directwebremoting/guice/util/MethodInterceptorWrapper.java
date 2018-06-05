package org.directwebremoting.guice.util;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * A way to wrap MethodInterceptors with extra functionality.
 * @author Tim Peierls [tim at peierls dot net]
 */
public interface MethodInterceptorWrapper
{
    MethodInterceptor wrap(MethodInterceptor methodInterceptor);
}
