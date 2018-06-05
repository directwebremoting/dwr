package org.directwebremoting.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * A fake implementation of ServletConfig for cases (Like inside Spring) when
 * you don't have a real one, or when you want to modify the initParameters
 * provided by the real {@link ServletConfig}
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
    public FakeServletConfig(String name, ServletContext servletContext, Map<String, String> initParameters)
    {
        this.name = name;
        this.servletContext = servletContext;
        this.initParameters = (initParameters != null) ? initParameters : new HashMap<String, String>();
    }

    /**
     * Copy the values from another {@link ServletConfig} so we can modify them.
     */
    public FakeServletConfig(ServletConfig servletConfig)
    {
        this.name = servletConfig.getServletName();
        this.servletContext = servletConfig.getServletContext();
        this.initParameters = new HashMap<String, String>();

        for (String key : LocalUtil.iterableizer(servletConfig.getInitParameterNames(), String.class))
        {
            String value = servletConfig.getInitParameter(key);
            initParameters.put(key, value);
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

    /**
     * Modify an init parameter
     */
    public void setInitParameter(String name, String value)
    {
        initParameters.put(name, value);
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
    public Enumeration<String> getInitParameterNames()
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
    private final ServletContext servletContext;

    /**
     * Initialization parameters
     */
    private final Map<String, String> initParameters;
}
