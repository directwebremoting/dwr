package org.directwebremoting.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * A proxy generator that allows mapping a backing class to an interface it doesn't implement as long as method signatures match.
 *
 * @author Mike Wilson
 */
public class MethodMatchingProxyFactory
{
    public static <T> T createProxy(Class<T> interfaceClass, Class<?> backingClass, Object... constructorArgs)
    {
        try
        {
            // Instantiate backing class object
            Object backingObject;
            if (constructorArgs.length == 0 && backingClass.getConstructors().length == 0) {
                // Default constructor
                backingObject = backingClass.newInstance();
            } else {
                // Look up explicit constructor
                Class<?>[] backingArgsClasses = new Class<?>[constructorArgs.length];
                for(int i=0; i<constructorArgs.length; i++) {
                    backingArgsClasses[i] = constructorArgs[i].getClass();
                }
                Constructor<?> ctor = backingClass.getConstructor(backingArgsClasses);
                backingObject = ctor.newInstance(constructorArgs);
            }

            // Wrap with a proxy conforming to the fronting interface
            Object proxy = Proxy.newProxyInstance(MethodMatchingProxyFactory.class.getClassLoader(), new Class[]{interfaceClass}, new MethodMatchingInvocationHandler(backingObject));
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

