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

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * A fake implementation of ServletConfig for cases (Like inside Spring) when
 * you don't have a real one.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FakeServletConfig implements ServletConfig
{
    /**
     * @param name The servlet name
     * @param servletContext The ServletContext
     */
    public FakeServletConfig(String name, ServletContext servletContext)
    {
        this(name, servletContext, null);
    }

    /**
     * @param name The servlet name
     * @param servletContext The ServletContext
     * @param initParameters Optional init parameters (can be null)
     */
    public FakeServletConfig(String name, ServletContext servletContext, Map initParameters)
    {
        this.name = name;
        this.servletContext = servletContext;
        this.initParameters = initParameters;

        if (this.initParameters == null)
        {
            this.initParameters = new HashMap();
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletConfig#getServletName()
     */
    public String getServletName()
    {
        return name;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletConfig#getServletContext()
     */
    public ServletContext getServletContext()
    {
        return servletContext;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletConfig#getInitParameter(java.lang.String)
     */
    public String getInitParameter(String paramName)
    {
        Object obj = initParameters.get(paramName);
        if (obj instanceof String)
        {
            return (String) obj;
        }
        else
        {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletConfig#getInitParameterNames()
     */
    public Enumeration getInitParameterNames()
    {
        return Collections.enumeration(initParameters.keySet());
    }

    /**
     * The servlet name
     */
    private final String name;

    /**
     * The servlet deployment information
     */
    private ServletContext servletContext;

    /**
     * Initialization parameters
     */
    private Map initParameters;
}
