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

