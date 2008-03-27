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
package uk.ltd.getahead.dwr.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ltd.getahead.dwr.AccessControl;
import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.Processor;
import uk.ltd.getahead.dwr.util.JavascriptUtil;
import uk.ltd.getahead.dwr.util.LocalUtil;

/**
 * Create some javascript interface code.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultInterfaceProcessor implements Processor
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Processor#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String pathinfo = req.getPathInfo();
        String servletpath = req.getServletPath();
        if (pathinfo == null)
        {
            pathinfo = req.getServletPath();
            servletpath = HtmlConstants.PATH_ROOT;
        }
        String scriptname = pathinfo;
        scriptname = LocalUtil.replace(scriptname, HtmlConstants.PATH_INTERFACE, HtmlConstants.BLANK);
        scriptname = LocalUtil.replace(scriptname, HtmlConstants.EXTENSION_JS, HtmlConstants.BLANK);
        Creator creator = creatorManager.getCreator(scriptname);

        //resp.setContentType("text/javascript");
        PrintWriter out = resp.getWriter();
        out.println();

        out.println("function " + scriptname + "() { }"); //$NON-NLS-1$ //$NON-NLS-2$

        String path = overridePath;
        if (path == null)
        {
            path = req.getContextPath() + servletpath;
        }
        out.println(scriptname + "._path = '" + path + "';"); //$NON-NLS-1$ //$NON-NLS-2$

        Method[] methods = creator.getType().getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            String methodName = method.getName();

            // We don't need to check accessControl.getReasonToNotExecute()
            // because the checks are made by the doExec method, but we do check
            // if we can display it
            String reason = accessControl.getReasonToNotDisplay(req, creator, scriptname, method);
            if (reason != null && !allowImpossibleTests)
            {
                continue;
            }

            // Is it on the list of banned names
            if (jsutil.isReservedWord(methodName))
            {
                continue;
            }

            out.print('\n');
            out.print(scriptname + '.' + methodName + " = function("); //$NON-NLS-1$
            Class[] paramTypes = method.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (!LocalUtil.isServletClass(paramTypes[j]))
                {
                    out.print("p" + j + ", "); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            out.println("callback) {"); //$NON-NLS-1$

            out.print("    DWREngine._execute(" + scriptname + "._path, '" + scriptname + "', '" + methodName + "\', "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (LocalUtil.isServletClass(paramTypes[j]))
                {
                    out.print("false, "); //$NON-NLS-1$
                }
                else
                {
                    out.print("p" + j + ", "); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            out.println("callback);"); //$NON-NLS-1$

            out.println('}');
        }

        out.flush();
    }

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param creatorManager The new DefaultConverterManager
     */
    public void setCreatorManager(CreatorManager creatorManager)
    {
        this.creatorManager = creatorManager;
    }

    /**
     * Accessor for the security manager
     * @param accessControl The accessControl to set.
     */
    public void setAccessControl(AccessControl accessControl)
    {
        this.accessControl = accessControl;
    }

    /**
     * Do we allow impossible tests for debug purposes
     * @param allowImpossibleTests The allowImpossibleTests to set.
     */
    public void setAllowImpossibleTests(boolean allowImpossibleTests)
    {
        this.allowImpossibleTests = allowImpossibleTests;
    }

    /**
     * If we need to override the default path
     * @param overridePath The new override path
     */
    public void setOverridePath(String overridePath)
    {
        this.overridePath = overridePath;
    }

    /**
     * If we need to override the default path
     */
    private String overridePath = null;

    /**
     * How we create new beans
     */
    protected CreatorManager creatorManager = null;

    /**
     * The security manager
     */
    protected AccessControl accessControl = null;

    /**
     * This helps us test that access rules are being followed
     */
    private boolean allowImpossibleTests = false;

    /**
     * The means by which we strip comments
     */
    private JavascriptUtil jsutil = new JavascriptUtil();
}
