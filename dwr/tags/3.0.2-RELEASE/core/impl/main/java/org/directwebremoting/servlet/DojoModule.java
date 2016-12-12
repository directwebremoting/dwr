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
 * A class that abstracts the generation of Dojo script modules.
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public class DojoModule extends BaseModule
{
    /**
     * Constructor.
     * @param contextPath used for variable expansion in baseModulePaths
     * @param servletPath used for variable expansion in baseModulePaths
     * @param baseModulePath baseModulePath for module itself (dojo.provide)
     * @param moduleName module name for module itself (dojo.provide)
     */
    public DojoModule(String contextPath, String servletPath, String baseModulePath, String moduleName)
    {
        super(contextPath, servletPath, ".");
        provide = expandModulePath(baseModulePath, moduleName);
    }

    /**
     * Add a module that this module is dependent upon.
     * @param baseModulePath the module root that should be added to
     * the module name (may be empty, may be absolute or relative and
     * may contain path variables ${contextPath} and ${servletPath})
     * @param moduleName the module name, f ex a.b
     */
    public void addRequire(String baseModulePath, String moduleName)
    {
        requires.add(expandModulePath(baseModulePath, moduleName));
    }

    /**
     * Generates the module text
     */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();

        // Provide
        buf.append("dojo.provide(\"" + provide + "\");\n");

        // Requires
        for(String require : requires)
        {
            buf.append("dojo.require(\"" + require + "\");\n");
        }

        buf.append("\n");

        buf.append(contentBuf.toString());

        return buf.toString();
    }

    /**
     * This module's name for dojo.provide.
     */
    private final String provide;

    /**
     * List of dependency module names.
     */
    private final List<String> requires = new ArrayList<String>();
}
