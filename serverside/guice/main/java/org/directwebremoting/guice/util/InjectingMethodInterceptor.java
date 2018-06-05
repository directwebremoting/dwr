package org.directwebremoting.guice.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * A MethodInterceptor-decorator that looks up its target on each invocation.
 * @author Tim Peierls
 */
class InjectingMethodInterceptor implements MethodInterceptor
{
    InjectingMethodInterceptor(Class<? extends MethodInterceptor> interceptorClass)
    {
        this.key = Key.get(interceptorClass);
    }

    InjectingMethodInterceptor(Key<? extends MethodInterceptor> key)
    {
        this.key = key;
    }

    @Inject void injectInterceptor(Injector newInjector)
    {
        this.injector = newInjector;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        Injector inj = injector; // volatile read
        MethodInterceptor interceptor = inj != null ? inj.getInstance(key) : null;
        if (interceptor == null)
        {
            return invocation.proceed();
        }
        else
        {
            return interceptor.invoke(invocation);
        }
    }

    private final Key<? extends MethodInterceptor> key;
    private volatile Injector injector = null;
}
