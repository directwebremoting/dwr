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
package org.directwebremoting.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.directwebremoting.AccessControl;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.AjaxFilterManager;
import org.directwebremoting.Call;
import org.directwebremoting.Calls;
import org.directwebremoting.Creator;
import org.directwebremoting.CreatorManager;
import org.directwebremoting.Remoter;
import org.directwebremoting.Replies;
import org.directwebremoting.Reply;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.util.ContinuationUtil;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;

/**
 * In implementation of Remoter that delegates requests to a set of Modules
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultRemoter implements Remoter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Remoter#generateInterfaceScript(java.lang.String, java.lang.String)
     */
    public String generateInterfaceScript(String scriptName, String path) throws SecurityException
    {
        String actualPath = path;
        if (overridePath != null)
        {
            actualPath = overridePath;
        }

        Creator creator = creatorManager.getCreator(scriptName);
        StringBuffer buffer = new StringBuffer();

        buffer.append('\n');
        buffer.append("// Provide a default path to DWREngine\n"); //$NON-NLS-1$
        buffer.append("if (DWREngine == null) { var DWREngine = {}; }\n"); //$NON-NLS-1$
        buffer.append("DWREngine._defaultPath = '" + actualPath + "';\n"); //$NON-NLS-1$ //$NON-NLS-2$
        buffer.append('\n');
        buffer.append("function " + scriptName + "() { }\n"); //$NON-NLS-1$ //$NON-NLS-2$
        buffer.append(scriptName + "._path = '" + actualPath + "';\n"); //$NON-NLS-1$ //$NON-NLS-2$

        Method[] methods = creator.getType().getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            String methodName = method.getName();

            // We don't need to check accessControl.getReasonToNotExecute()
            // because the checks are made by the doExec method, but we do check
            // if we can display it
            String reason = accessControl.getReasonToNotDisplay(creator, scriptName, method);
            if (reason != null && !allowImpossibleTests)
            {
                continue;
            }

            // Is it on the list of banned names
            if (JavascriptUtil.isReservedWord(methodName))
            {
                continue;
            }

            // Check to see if the creator is reloadable
            // If it is, then do not cache the generated Javascript
            String script;
            if (!creator.isCacheable())
            {
                script = getMethodJS(scriptName, method);
            }
            else
            {
                String key = scriptName + "." + method.getName(); //$NON-NLS-1$

                // For optimal performance we might synchronize on methodCache however
                // since performance isn't a big issue we are prepared to cope with
                // the off chance that getMethodJS() may be run more than once.
                script = (String) methodCache.get(key);
                if (script == null)
                {
                    script = getMethodJS(scriptName, method);
                    methodCache.put(key, script);
                }
            }

            buffer.append(script);
        }

        return buffer.toString();
    }

    /**
     * Generates Javascript for a given Java method
     * @param scriptName  Name of the Javascript file, sans ".js" suffix
     * @param method Target method
     * @return Javascript implementing the DWR call for the target method
     */
    private String getMethodJS(String scriptName, Method method)
    {
        StringBuffer buffer = new StringBuffer();

        String methodName = method.getName();
        buffer.append(scriptName + '.' + methodName + " = function("); //$NON-NLS-1$
        Class[] paramTypes = method.getParameterTypes();
        for (int j = 0; j < paramTypes.length; j++)
        {
            if (!LocalUtil.isServletClass(paramTypes[j]))
            {
                buffer.append("p" + j + ", "); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        buffer.append("callback) {\n"); //$NON-NLS-1$

        buffer.append("  DWREngine._execute(" + scriptName + "._path, '" + scriptName + "', '" + methodName + "\', "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        for (int j = 0; j < paramTypes.length; j++)
        {
            if (LocalUtil.isServletClass(paramTypes[j]))
            {
                buffer.append("false, "); //$NON-NLS-1$
            }
            else
            {
                buffer.append("p" + j + ", "); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        buffer.append("callback);\n"); //$NON-NLS-1$
        buffer.append("}\n"); //$NON-NLS-1$

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Remoter#execute(org.directwebremoting.Calls)
     */
    public Replies execute(Calls calls)
    {
        Replies replies = new Replies();

        for (int callNum = 0; callNum < calls.getCallCount(); callNum++)
        {
            Call call = calls.getCall(callNum);
            Reply reply = execute(call);
            replies.addReply(reply);
        }

        return replies;
    }

    /**
     * Execute a single call object
     * @param call The call to execute
     * @return A Reply to the Call
     */
    private Reply execute(Call call)
    {
        try
        {
            Method method = call.getMethod();
            if (method == null || call.getException() != null)
            {
                return new Reply(call.getId(), null, call.getException());
            }

            // Get a list of the available matching methods with the coerced
            // parameters that we will use to call it if we choose to use that
            // method.
            Creator creator = creatorManager.getCreator(call.getScriptName());

            // Get ourselves an object to execute a method on unless the
            // method is static
            Object object = null;
            String scope = creator.getScope();
            boolean create = false;

            if (!Modifier.isStatic(method.getModifiers()))
            {
                WebContext webcx = WebContextFactory.get();

                // Check the various scopes to see if it is there
                if (scope.equals(Creator.APPLICATION))
                {
                    object = webcx.getServletContext().getAttribute(call.getScriptName());
                }
                else if (scope.equals(Creator.SESSION))
                {
                    object = webcx.getSession().getAttribute(call.getScriptName());
                }
                else if (scope.equals(Creator.SCRIPT))
                {
                    object = webcx.getScriptSession().getAttribute(call.getScriptName());
                }
                else if (scope.equals(Creator.REQUEST))
                {
                    object = webcx.getHttpServletRequest().getAttribute(call.getScriptName());
                }
                // Creator.PAGE scope means we create one every time anyway

                // If we don't have an object the call the creator
                if (object == null)
                {
                    create = true;
                    object = creator.getInstance();
                }

                // Remember it for next time
                if (create)
                {
                    if (scope.equals(Creator.APPLICATION))
                    {
                        webcx.getServletContext().setAttribute(call.getScriptName(), object);
                    }
                    else if (scope.equals(Creator.SESSION))
                    {
                        webcx.getSession().setAttribute(call.getScriptName(), object);
                    }
                    else if (scope.equals(Creator.SCRIPT))
                    {
                        webcx.getScriptSession().setAttribute(call.getScriptName(), object);
                    }
                    else if (scope.equals(Creator.REQUEST))
                    {
                        webcx.getHttpServletRequest().setAttribute(call.getScriptName(), object);
                    }
                    // Creator.PAGE scope means we create one every time anyway
                }
            }

            // Some debug
            log.info("Exec: " + call.getScriptName() + "." + call.getMethodName() + "()"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            if (log.isDebugEnabled())
            {
                StringBuffer buffer = new StringBuffer();

                if (create)
                {
                    buffer.append("--Object created, "); //$NON-NLS-1$
                    if (!scope.equals(Creator.PAGE))
                    {
                        buffer.append(" stored in "); //$NON-NLS-1$
                        buffer.append(scope);
                    }
                    else
                    {
                        buffer.append(" not stored"); //$NON-NLS-1$
                    }
                }
                else
                {
                    buffer.append("--Object found in "); //$NON-NLS-1$
                    buffer.append(scope);
                }
                buffer.append(". "); //$NON-NLS-1$

                // It would be good to debug the params but it's not easy
                //buffer.append("Call params ("); //$NON-NLS-1$
                //for (int j = 0; j < inctx.getParameterCount(callNum); j++)
                //{
                //    if (j != 0)
                //    {
                //        buffer.append(", "); //$NON-NLS-1$
                //    }
                //    InboundVariable param = inctx.getParameter(callNum, j);
                //    buffer.append(param.toString());
                //}
                //buffer.append(") "); //$NON-NLS-1$

                buffer.append("id="); //$NON-NLS-1$
                buffer.append(call.getId());

                log.debug(buffer.toString());
            }

            // Execute the filter chain method.toString()
            final Iterator it = ajaxFilterManager.getAjaxFilters(call.getScriptName());
            AjaxFilterChain chain = new AjaxFilterChain()
            {
                public Object doFilter(Object obj, Method meth, Object[] p) throws Exception
                {
                    AjaxFilter next = (AjaxFilter) it.next();
                    return next.doFilter(obj, meth, p, this);
                }
            };
            Object reply = chain.doFilter(object, method, call.getParameters());
            return new Reply(call.getId(), reply);
        }
        catch (InvocationTargetException ex)
        {
            // Allow Jetty RequestRetry exception to propogate to container
            ContinuationUtil.rethrowIfContinuation(ex.getTargetException());

            log.warn("Method execution failed: ", ex.getTargetException()); //$NON-NLS-1$
            return new Reply(call.getId(), null, ex.getTargetException());
        }
        catch (Exception ex)
        {
            log.warn("Method execution failed: ", ex); //$NON-NLS-1$
            return new Reply(call.getId(), null, ex);
        }
    }

    /**
     * Accessor for the CreatorManager that we configure
     * @param creatorManager The new ConverterManager
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
     * Accessor for the AjaxFilterManager
     * @param ajaxFilterManager The AjaxFilterManager to set.
     */
    public void setAjaxFilterManager(AjaxFilterManager ajaxFilterManager)
    {
        this.ajaxFilterManager = ajaxFilterManager;
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
     * Do we allow impossible tests for debug purposes
     * @param allowImpossibleTests The allowImpossibleTests to set.
     */
    public void setAllowImpossibleTests(boolean allowImpossibleTests)
    {
        this.allowImpossibleTests = allowImpossibleTests;
    }

    /**
     * What AjaxFilters apply to which Ajax calls?
     */
    private AjaxFilterManager ajaxFilterManager = null;

    /**
     * How we create new beans
     */
    protected CreatorManager creatorManager = null;

    /**
     * The security manager
     */
    protected AccessControl accessControl = null;

    /**
     * If we need to override the default path
     */
    private String overridePath = null;

    /**
     * This helps us test that access rules are being followed
     */
    private boolean allowImpossibleTests = false;

    /**
     * Generated Javascript cache
     */
    private Map methodCache = new HashMap();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultRemoter.class);
}
