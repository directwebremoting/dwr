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

import org.directwebremoting.util.LocalUtil;

/**
 * A base class for script modules.
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class BaseModule
{
    /**
     * Constructor. Path parameters are supplied for expanding path variables in
     * module paths.
     * @param contextPath
     * @param servletPath
     * @param separator
     */
    public BaseModule(String contextPath, String servletPath, String separator)
    {
        this.contextPath = contextPath.replace("/", separator);
        this.servletPath = servletPath.replace("/", separator);
        this.separator = separator;
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
     * Expands ${contextPath} and ${servletPath} variables in the baseModulePath.
     * @param baseModulePath
     * @return module path
     */
    public String expandModulePath(String baseModulePath)
    {
        // Replace variables in baseModulePath according to this scheme
        // (assuming / as separator):
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
        // - remove double separators
        // - remove leading separator if not absolute path
        // - remove trailing separator (but leave leading separator)
        while(expandedBaseModulePath.indexOf(separator + separator) > 0)
        {
            expandedBaseModulePath.replaceAll(separator + separator, separator);
        }
        if (!baseModulePath.startsWith(separator) && expandedBaseModulePath.startsWith(separator))
        {
            expandedBaseModulePath = expandedBaseModulePath.substring(separator.length());
        }
        if (expandedBaseModulePath.endsWith(separator) && expandedBaseModulePath.length() > separator.length())
        {
            expandedBaseModulePath = expandedBaseModulePath.substring(0, expandedBaseModulePath.length() - separator.length());
        }

        return expandedBaseModulePath;
    }

    /**
     * Mounts a moduleName onto a baseModulePath and at the same time expanding
     * ${contextPath} and ${servletPath} variables in the baseModulePath. The
     * module path segments in the resulting module name will be separated with
     * the configured separator (typically / or .).
     * @param baseModulePath
     * @param moduleName
     * @return module name rooted on baseModulePath
     */
    public String expandModulePath(String baseModulePath, String moduleName)
    {
        String expandedModulePath = expandModulePath(baseModulePath);
        String expandedModuleName = moduleName.replaceAll("\\.", separator);
        String sep = "";
        if (LocalUtil.hasLength(expandedModulePath) && LocalUtil.hasLength(expandedModuleName))
        {
            sep = separator;
        }
        return expandedModulePath + sep + expandedModuleName;
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
     * The separator used between module name segments (typically / or .).
     */
    private final String separator;

    /**
     * Module content (script body).
     */
    protected final StringBuilder contentBuf = new StringBuilder();
}
