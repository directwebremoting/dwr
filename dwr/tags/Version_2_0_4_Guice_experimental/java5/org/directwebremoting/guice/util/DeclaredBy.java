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

import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

import java.lang.reflect.Method;

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
