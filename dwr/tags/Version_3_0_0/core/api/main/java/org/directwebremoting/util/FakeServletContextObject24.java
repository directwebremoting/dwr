/*
 * Copyright 2002-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.directwebremoting.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Backing class for Servlet 2.4 fake ServletContext.
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
class FakeServletContextObject24 // Note: does not implement interface as we are mapping versions in runtime
{
    /**
     * Create a new FakeServletContext, using no base path and a
     * DefaultResourceLoader (i.e. the classpath root as WAR root).
     */
    public FakeServletContextObject24()
    {
        this("");
    }

    /**
     * Create a new FakeServletContext, using a DefaultResourceLoader.
     * @param resourceBasePath the WAR root directory (should not end with a slash)
     */
    public FakeServletContextObject24(String resourceBasePath)
    {
        this.resourceBasePath = (resourceBasePath != null ? resourceBasePath : "");

        // Use JVM temp dir as ServletContext temp dir.
        String tempDir = System.getProperty("java.io.tmpdir");
        if (tempDir != null)
        {
            attributes.put("javax.servlet.context.tempdir", new File(tempDir));
        }
    }

    /**
     * Build a full resource location for the given path,
     * prepending the resource base path of this FakeServletContext.
     * @param path the path as specified
     * @return the full resource path
     */
    protected String getResourceLocation(String path)
    {
        String output = path;
        if (!output.startsWith("/"))
        {
            output = "/" + output;
        }
        output = resourceBasePath + output;

        return output;
    }

    public ServletContext getContext(String name)
    {
        throw new UnsupportedOperationException("getContext");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getMajorVersion()
     */
    public int getMajorVersion()
    {
        return 2;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getMinorVersion()
     */
    public int getMinorVersion()
    {
        return 4;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
     */
    public String getMimeType(String filePath)
    {
        throw new UnsupportedOperationException("getMimeType");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    public Set<String> getResourcePaths(String path)
    {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getResource(java.lang.String)
     */
    public URL getResource(String path) throws MalformedURLException
    {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream(String path)
    {
        try
        {
            return new FileInputStream(resourceBasePath + path);
        }
        catch (FileNotFoundException ex)
        {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String path)
    {
        if (!path.startsWith("/"))
        {
            throw new IllegalArgumentException("RequestDispatcher path at ServletContext level must start with '/'");
        }
        return new FakeRequestDispatcher(path);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
     */
    public RequestDispatcher getNamedDispatcher(String path)
    {
        throw new UnsupportedOperationException("getNamedDispatcher");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getServlet(java.lang.String)
     */
    @Deprecated
    public Servlet getServlet(String name)
    {
        throw new UnsupportedOperationException("getServlet");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getServlets()
     */
    @Deprecated
    public Enumeration<Servlet> getServlets()
    {
        throw new UnsupportedOperationException("getServlets");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getServletNames()
     */
    @Deprecated
    public Enumeration<String> getServletNames()
    {
        throw new UnsupportedOperationException("getServletNames");
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.String)
     */
    public void log(String message)
    {
        log.info(message);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.Exception, java.lang.String)
     */
    @Deprecated
    public void log(Exception ex, String message)
    {
        log.warn(message, ex);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
     */
    public void log(String message, Throwable ex)
    {
        log.warn(message, ex);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     */
    public String getRealPath(String path)
    {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getServerInfo()
     */
    public String getServerInfo()
    {
        return "FakeServletContext";
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
     */
    public String getInitParameter(String name)
    {
        return initParameters.get(name);
    }

    /**
     * Add an init parameter to the list that we hand out
     * @param name The name of the new init parameter
     * @param value The value of the new init parameter
     */
    public void addInitParameter(String name, String value)
    {
        initParameters.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getInitParameterNames()
     */
    public Enumeration<String> getInitParameterNames()
    {
        return Collections.enumeration(initParameters.keySet());
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getAttributeNames()
     */
    public Enumeration<String> getAttributeNames()
    {
        return Collections.enumeration(attributes.keySet());
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String name, Object value)
    {
        if (value != null)
        {
            attributes.put(name, value);
        }
        else
        {
            attributes.remove(name);
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name)
    {
        attributes.remove(name);
    }

    /**
     * Accessor for the servlet context name. Normally read-only, but read
     * write in this fake context
     * @param servletContextName The new servlet context name
     */
    public void setServletContextName(String servletContextName)
    {
        this.servletContextName = servletContextName;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    public String getServletContextName()
    {
        return servletContextName;
    }

    /**
     * See Servlet API version 2.5
     * @return The context path for this servlet
     */
    public String getContextPath()
    {
        throw new UnsupportedOperationException("getContextPath");
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(FakeServletContextObject24.class);

    /**
     * The resource path to allow us to fetch resources from the disk
     */
    private final String resourceBasePath;

    /**
     * The init parameters to this servlet
     */
    private final Map<String, String> initParameters = new HashMap<String, String>();

    /**
     * The servlet level attributes
     */
    private final Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * The servlet context name
     */
    private String servletContextName = "FakeServletContext";
}
