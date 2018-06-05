package org.directwebremoting.guice.util;

import java.lang.reflect.Method;

import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

/**
 * For matching methods declared by a given interface or superclass.
 * @author Tim Peierls [tim at peierls dot net]
 */
public class DeclaredBy extends AbstractMatcher<Method>
{
    /**
     * Returns a matcher that matches methods in subclasses
     * of {@code cls} (or {@code cls} itself) with the same
     * name and parameter types as methods declared in {@code cls}.
     */
    public static Matcher<Method> declaredBy(Class<?> cls)
    {
        return new DeclaredBy(cls);
    }

    public boolean matches(Method method)
    {
        try
        {
            // Matches if the method is from a subclass of the given class
            // (or the class itself) and the given class declares a method
            // with the same name and parameter types.
            if (cls.isAssignableFrom(method.getDeclaringClass()))
            {
                // Return value of getDeclaredMethod is ignored. It throws
                // an exception if the method is not found.
                cls.getDeclaredMethod(method.getName(), method.getParameterTypes());

                return true;
            }
            // fall through
        }
        catch (NoSuchMethodException e)
        {
            // fall through
        }
        catch (SecurityException e)
        {
            // fall through
        }

        return false;
    }


    private DeclaredBy(Class<?> cls)
    {
        this.cls = cls;
    }

    private final Class<?> cls;
}
