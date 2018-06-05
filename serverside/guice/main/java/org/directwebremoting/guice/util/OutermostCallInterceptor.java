package org.directwebremoting.guice.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Provides static decorator method for restricting interception to
 * the outermost of several nested matching join points. Also provides
 * corresponding MethodInterceptor wrapper method.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class OutermostCallInterceptor implements MethodInterceptor
{
    /**
     * For passing to bindInterceptor to wrap injected MethodInterceptors.
     */
    public static MethodInterceptorWrapper outermostCallWrapper()
    {
        return new MethodInterceptorWrapper()
        {
            public MethodInterceptor wrap(MethodInterceptor methodInterceptor)
            {
                return outermostCall(methodInterceptor);
            }
        };
    }

    /**
     * Decorates a MethodInterceptor so that only the outermost invocation
     * using that interceptor will be intercepted and nested invocations will
     * be ignored.
     */
    public static MethodInterceptor outermostCall(MethodInterceptor interceptor)
    {
        return new OutermostCallInterceptor(interceptor);
    }

    @Inject void injectInterceptor(Injector injector)
    {
        injector.injectMembers(interceptor);
    }

    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        int savedCount = count.get();
        count.set(savedCount + 1);
        try
        {
            if (count.get() > 1)
            {
                return invocation.proceed();
            }
            else
            {
                return interceptor.invoke(invocation);
            }
        }
        finally
        {
            count.set(savedCount);
        }
    }

    private OutermostCallInterceptor(MethodInterceptor interceptor)
    {
        this.interceptor = interceptor;
    }

    private final MethodInterceptor interceptor;

    private final ThreadLocal<Integer> count = new ThreadLocal<Integer>()
    {
        @Override
        protected Integer initialValue()
        {
            return 0;
        }
    };
}
