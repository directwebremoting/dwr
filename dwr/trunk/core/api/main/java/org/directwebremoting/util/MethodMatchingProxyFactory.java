/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Mike Wilson
 */
public class MethodMatchingProxyFactory
{
    public static <T> T createProxy(Class<T> interfaceClass, Class<?> backingClass, Object... backingArgs)
    {
        try
        {
            Class<?>[] backingArgsClasses = new Class<?>[backingArgs.length];
            for(int i=0; i<backingArgs.length; i++) {
                backingArgsClasses[i] = backingArgs[i].getClass();
            }
            Constructor<?> ctor = backingClass.getConstructor(backingArgsClasses);
            Object backingObject = ctor.newInstance(backingArgs);
            Object proxy = Proxy.newProxyInstance(FakeHttpServletRequestFactory.class.getClassLoader(), new Class[]{interfaceClass}, new MethodMatchingInvocationHandler(backingObject));
            return interfaceClass.cast(proxy);
        }
        catch (Exception ex)
        {
            if (ex instanceof RuntimeException) {
                RuntimeException rex = (RuntimeException) ex;
                throw rex;
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

    private static class MethodMatchingInvocationHandler implements InvocationHandler
    {
        private final Object backingObject;

        public MethodMatchingInvocationHandler(Object backingObject)
        {
            this.backingObject = backingObject;
        }

        public Object invoke(Object proxy, Method proxyMethod, Object[] args) throws Throwable
        {
            try {
                Method backingMethod = backingObject.getClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
                return backingMethod.invoke(backingObject, args);
            } catch(NoSuchMethodException ex) {
                throw new RuntimeException("Method '" + proxyMethod.getName() + "' not implemented in " + backingObject.getClass().getName() + ".");
            }
        }
    }
}

