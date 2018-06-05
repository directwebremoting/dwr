package org.directwebremoting.util;

import javax.servlet.ServletContext;

/**
 * @author Mike Wilson
 */
public class FakeServletContextFactory
{
    public static ServletContext create(Object... args)
    {
        return MethodMatchingProxyFactory.createProxy(ServletContext.class, fakeServletContextClass, args);
    }

    private static Class<?> fakeServletContextClass = null;

    static {
        fakeServletContextClass = FakeServletContextObject24.class;
    }
}

