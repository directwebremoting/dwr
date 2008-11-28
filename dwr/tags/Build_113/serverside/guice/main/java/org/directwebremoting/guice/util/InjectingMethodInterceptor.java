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

import com.google.inject.Key;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * A MethodInterceptor-decorator that looks up its target on each invocation.
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
