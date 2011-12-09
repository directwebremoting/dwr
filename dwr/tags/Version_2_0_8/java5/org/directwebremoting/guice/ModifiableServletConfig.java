/*
 * Copyright 2007 Tim Peierls
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
package org.directwebremoting.guice;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import static java.util.Collections.synchronizedMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * Wraps an existing ServletConfig, allowing changes to be made to the existing initParams.
 * @author Tim Peierls [tim at peierls dot net]
 */
class ModifiableServletConfig implements ServletConfig 
{
    ModifiableServletConfig(ServletConfig servletConfig) 
    {
        this.servletConfig = servletConfig;
    }

    public String getInitParameter(String name) 
    {
        if (overrides.containsKey(name)) 
        {
            return overrides.get(name);
        } 
        else 
        {
            return servletConfig.getInitParameter(name);
        }
    }

    public Enumeration getInitParameterNames() 
    {
        Set<String> names = new HashSet<String>();
        Enumeration enumeration = servletConfig.getInitParameterNames();
        while (enumeration.hasMoreElements()) 
        {
            names.add(enumeration.nextElement().toString());
        }
        names.addAll(overrides.keySet());
        return toEnumeration(names.iterator());
    }

    public ServletContext getServletContext() 
    {
        return servletConfig.getServletContext();
    }

    public String getServletName() 
    {
        return servletConfig.getServletName();
    }

    public void setInitParameter(String name, String value) 
    {
        overrides.put(name, value);
    }

    private static <E> Enumeration<E> toEnumeration(final Iterator<E> iterator) 
    {
        return new Enumeration<E>() 
        {
            public boolean hasMoreElements() 
            {
                return iterator.hasNext();
            }
            
            public E nextElement() 
            {
                return iterator.next();
            }
        };
    }
    
    private final ServletConfig servletConfig;
    
    private final Map<String, String> overrides = synchronizedMap(new HashMap<String, String>());
}
