package org.directwebremoting.util;

/**
 * @author Mike Wilson
 */
public class FakeHttpServletRequestFactory
{
    public static FakeHttpServletRequest create()
    {
        return MethodMatchingProxyFactory.createProxy(FakeHttpServletRequest.class, fakeHttpServletRequestClass);
    }

    private static Class<?> fakeHttpServletRequestClass = null;

    static {
        fakeHttpServletRequestClass = FakeHttpServletRequestObject24.class;
    }
}

