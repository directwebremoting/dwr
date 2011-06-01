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
package org.directwebremoting.servlet;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that abstracts the generation of CommonJS AMD script modules.
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class CommonJsAmdModule
{
    /**
     * Constructor. Path parameters are supplied for expanding path variables in
     * module paths.
     * @param contextPath
     * @param servletPath
     */
    public CommonJsAmdModule(String contextPath, String servletPath)
    {
        this.contextPath = contextPath;
        this.servletPath = servletPath;
    }

    /**
     * Add a module that this module is dependent upon.
     * @param baseModulePath the module root that should be added to
     * the module name (may be empty, may be absolute or relative and 
     * may contain path variables ${contextPath} and ${servletPath})
     * @param moduleName the module name, f ex a/b (dots a.b will be 
     * converted to slashes)
     * @param parameterName the name of the dependency's parameter in
     * the script function
     */
    public void addDependency(String baseModulePath, String moduleName, String parameterName)
    {
        //
        // Dependency module path
        //

        // Replace variables in baseModulePath according to this scheme:
        //   baseModulePath         servletPath        result
        //   ${servletPath}         ""                 ""
        //   ${servletPath}         /dwr               dwr
        //   /${servletPath}        ""                 /
        //   /${servletPath}        /dwr               /dwr
        //   ${servletPath}/abc     ""                 abc
        //   ${servletPath}/abc     /dwr               dwr/abc
        //   /${servletPath}/abc    ""                 /abc
        //   /${servletPath}/abc    /dwr               /dwr/abc
        //   abc/${servletPath}     ""                 abc
        //   abc/${servletPath}     /dwr               abc/dwr
        //   /abc/${servletPath}    ""                 /abc
        //   /abc/${servletPath}    /dwr               /abc/dwr

        // Expand variables in baseModulePath
        String expandedBaseModulePath = baseModulePath.replace("${contextPath}", contextPath).replace("${servletPath}", servletPath);

        // Clean up the result:
        // - remove double slashes
        // - remove leading slash if not absolute path
        // - remove trailing slash
        while(expandedBaseModulePath.indexOf("//") > 0)
        {
            expandedBaseModulePath.replaceAll("//", "/");
        }
        if (!baseModulePath.startsWith("/") && expandedBaseModulePath.startsWith("/"))
        {
            expandedBaseModulePath = expandedBaseModulePath.substring(1);
        }
        if (expandedBaseModulePath.endsWith("/"))
        {
            expandedBaseModulePath = expandedBaseModulePath.substring(0, expandedBaseModulePath.length() - 1);
        }

        // Add module name rooted on the base path
        String fullModulePath = (expandedBaseModulePath.length() > 0 ? expandedBaseModulePath + "/" : "")
            + moduleName.replaceAll("\\.", "/");
        dependencies.add(fullModulePath);

        //
        // Parameter
        //

        parameters.add(parameterName);
    }

    /**
     * Adds the actual module content (script body).
     * @param content
     */
    public void addContent(String content)
    {
        this.contentBuf.append(content);
    }

    /**
     * Generates the module text
     */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        boolean first;

        buf.append("define(");

        // Dependency array
        if (dependencies.size() > 0)
        {
            buf.append("[");
            first = true;
            for(String modulePath : dependencies)
            {
                if (!first)
                {
                    buf.append(", ");
                }
                buf.append("\"" + modulePath + "\"");
                first = false;
            }
            buf.append("], ");
        }

        // Function
        buf.append("function(");
        first = true;
        for(String parameter : parameters)
        {
            if (!first)
            {
                buf.append(", ");
            }
            buf.append(parameter);
            first = false;
        }
        buf.append(") {\n");

        buf.append(contentBuf.toString());

        buf.append("});\n");

        return buf.toString();
    }

    /**
     * The supplied context path used for variable expansion.
     */
    private final String contextPath;
    
    /**
     * The supplied servlet path used for variable expansion.
     */
    private final String servletPath;
    
    /**
     * List of dependency module names.
     */
    private final List<String> dependencies = new ArrayList<String>();
    
    /**
     * List of function parameter names.
     */
    private final List<String> parameters = new ArrayList<String>();
    
    /**
     * Module content (script body).
     */
    private final StringBuilder contentBuf = new StringBuilder();
}
