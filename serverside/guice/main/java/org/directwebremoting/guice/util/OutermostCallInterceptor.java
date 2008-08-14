/*
 * Copyright 2008 Tim Peierls
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.guice.util;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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
