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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.NamedConverter;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.extend.Replies;
import org.directwebremoting.extend.Reply;
import org.directwebremoting.util.Continuation;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;

/**
 * In implementation of Remoter that delegates requests to a set of Modules
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Mike Wilson
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

        StringBuilder buffer = new StringBuilder();

        buffer.append(createParameterDefinitions());
        buffer.append(EnginePrivate.getEngineInitScript());
        buffer.append(createClassDefinition(scriptName));
        buffer.append(createPathDefinition(scriptName, actualPath));
        buffer.append(createMethodDefinitions(creator, scriptName));

        return buffer.toString();
    }

    /**
     * Create a class definition string.
     * This is similar to {@link EnginePrivate#getEngineInitScript()} except
     * that it creates scripts for a specific class not for dwr.engine
     * @see EnginePrivate#getEngineInitScript()
     * @param scriptName
     */
    protected String createClassDefinition(String scriptName)
    {
        return "if (typeof this['" + scriptName + "'] == 'undefined') this." + scriptName + " = {};\n\n";
    }

    /**
     * Create a _path member to point at DWR
     * @param scriptName The class that we are creating a member for
     * @param actualPath The default path to the DWR servlet
     */
    protected String createPathDefinition(String scriptName, String actualPath)
    {
        return scriptName + "._path = '" + actualPath + "';\n\n";
    }

    /**
     * Create a list of method definitions for the given creator.
     * @param creator The source of method definitions
     * @param scriptName To allow AccessControl to allow/deny requests
     */
    protected String createMethodDefinitions(Creator creator, String scriptName)
    {
        StringBuilder buffer = new StringBuilder();

        Method[] methods = creator.getType().getMethods();
        for (Method method : methods)
        {
            String methodName = method.getName();

            // We don't need to check accessControl.getReasonToNotExecute()
            // because the checks are made by the execute() method, but we do
            // check if we can display it
            try
            {
                accessControl.assertIsDisplayable(creator, scriptName, method);
            }
            catch (SecurityException ex)
            {
                if (!allowImpossibleTests)
                {
                    continue;
                }
            }

            // Is it on the list of banned names
            if (JavascriptUtil.isReservedWord(methodName))
            {
                continue;
            }

            // Check to see if the creator is reloadable
            // If it is, then do not cache the generated Javascript
            // See the notes on creator.isCacheable().
            String script;
            if (!creator.isCacheable())
            {
                script = getMethodJS(scriptName, method);
            }
            else
            {
                String key = scriptName + "." + method.getName();

                // For optimal performance we might use the Memoizer pattern
                // JCiP#108 however performance isn't a big issue and we are
                // prepared to cope with getMethodJS() being run more than once.
                script = methodCache.get(key);
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
     * Output the class definitions for all the converted objects.
     * An optimization for this class might be to only generate class
     * definitions for classes used as parameters in the class that we are
     * currently generating a proxy for
     */
    protected String createParameterDefinitions()
    {
        StringBuilder buffer = new StringBuilder();

        for (String match : converterManager.getConverterMatchStrings())
        {
            try
            {
                StringBuilder paramBuffer = new StringBuilder();

                Converter conv = converterManager.getConverterByMatchString(match);
                // We will only generate JavaScript classes for compound objects/beans
                if (conv instanceof NamedConverter)
                {
                    NamedConverter boConv = (NamedConverter) conv;
                    String jsClassName = boConv.getJavascript();

                    // We need a configured JavaScript class name
                    if (jsClassName != null && !"".equals(jsClassName))
                    {
                        // Wildcard match strings are currently not supported
                        if (!match.contains("*"))
                        {
                            paramBuffer.append('\n');

                            // output: if (typeof this['<class>'] != 'function') { function <class>() {
                            paramBuffer.append("if (typeof this['" + jsClassName + "'] != 'function') {\n");
                            paramBuffer.append("  function " + jsClassName + "() {\n");

                            // output: this.<property> = <init-value>;
                            Class<?> mappedType;
                            try
                            {
                                mappedType = LocalUtil.classForName(match);
                            }
                            catch (ClassNotFoundException ex)
                            {
                                throw new IllegalArgumentException(ex.getMessage());
                            }

                            Map<String, Property> properties = boConv.getPropertyMapFromClass(mappedType, true, true);
                            for (Entry<String, Property> entry : properties.entrySet())
                            {
                                String name = entry.getKey();
                                Property property = entry.getValue();
                                Class<?> propType = property.getPropertyType();

                                // Property name
                                paramBuffer.append("    this." + name + " = ");

                                // Default property values
                                if (propType.isArray())
                                {
                                    paramBuffer.append("[]");
                                }
                                else if (propType == boolean.class)
                                {
                                    paramBuffer.append("false");
                                }
                                else if (propType.isPrimitive())
                                {
                                    paramBuffer.append("0");
                                }
                                else
                                {
                                    paramBuffer.append("null");
                                }

                                paramBuffer.append(";\n");
                            }

                            paramBuffer.append("  }\n");
                            paramBuffer.append("}\n");
                        }
                    }
                }

                buffer.append(paramBuffer.toString());
            }
            catch (Exception ex)
            {
                log.warn("Failed to create parameter declaration for " + match, ex);
                buffer.append("// Missing parameter declaration for " + match + ". See the server logs for details.");
            }
        }
        buffer.append('\n');

        return buffer.toString();
    }

    /**
     * Generates Javascript for a given Java method
     * @param scriptName  Name of the Javascript file, without ".js" suffix
     * @param method Target method
     * @return Javascript implementing the DWR call for the target method
     */
    protected String getMethodJS(String scriptName, Method method)
    {
        StringBuffer buffer = new StringBuffer();

        String methodName = method.getName();
        buffer.append(scriptName + '.' + methodName + " = function(");
        Class<?>[] paramTypes = method.getParameterTypes();
        for (int j = 0; j < paramTypes.length; j++)
        {
            if (!LocalUtil.isServletClass(paramTypes[j]))
            {
                buffer.append("p" + j + ", ");
            }
        }

        buffer.append("callback) {\n");

        String executeFunctionName = EnginePrivate.getExecuteFunctionName();
        buffer.append("  " + executeFunctionName + "(" + scriptName + "._path, '" + scriptName + "', '" + methodName + "\', ");
        for (int j = 0; j < paramTypes.length; j++)
        {
            if (LocalUtil.isServletClass(paramTypes[j]))
            {
                buffer.append("false, ");
            }
            else
            {
                buffer.append("p" + j + ", ");
            }
        }

        buffer.append("callback);\n");
        buffer.append("};\n\n");

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Remoter#execute(org.directwebremoting.Calls)
     */
    public Replies execute(Calls calls)
    {
        Replies replies = new Replies(calls.getBatchId());

        int callCount = calls.getCallCount();
        if (callCount > maxCallCount)
        {
            log.error("Call count for batch exceeds maxCallCount. Add an init-param of maxCallCount to increase this limit");
            throw new SecurityException("Call count for batch is too high");
        }

        for (Call call : calls)
        {
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
    public Reply execute(Call call)
    {
        try
        {
            Method method = call.getMethod();
            if (method == null || call.getException() != null)
            {
                return new Reply(call.getCallId(), null, call.getException());
            }

            // Get a list of the available matching methods with the coerced
            // parameters that we will use to call it if we choose to use that
            // method.
            Creator creator = creatorManager.getCreator(call.getScriptName());

            // We don't need to check accessControl.getReasonToNotExecute()
            // because the checks are made by the doExec method, but we do check
            // if we can display it
            accessControl.assertExecutionIsPossible(creator, call.getScriptName(), method);

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
                        // This might also be done at application startup by
                        // DefaultCreatorManager.addCreator(String, Creator)
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
            if (log.isDebugEnabled())
            {
                StringBuffer buffer = new StringBuffer();
                buffer.append("Exec: ")
                      .append(call.getScriptName())
                      .append(".")
                      .append(call.getMethodName())
                      .append("()");

                if (create)
                {
                    buffer.append(" Object created, ");
                    if (!scope.equals(Creator.PAGE))
                    {
                        buffer.append(" stored in ");
                        buffer.append(scope);
                    }
                    else
                    {
                        buffer.append(" not stored");
                    }
                }
                else
                {
                    buffer.append(" Object found in ");
                    buffer.append(scope);
                }
                buffer.append(". ");

                // It would be good to debug the params but it's not easy
                //buffer.append("Call params (");
                //for (int j = 0; j < inctx.getParameterCount(callNum); j++)
                //{
                //    if (j != 0)
                //    {
                //        buffer.append(", ");
                //    }
                //    InboundVariable param = inctx.getParameter(callNum, j);
                //    buffer.append(param.toString());
                //}
                //buffer.append(") ");

                buffer.append("id=");
                buffer.append(call.getCallId());

                log.debug(buffer.toString());
            }

            // Execute the filter chain method.toString()
            final Iterator<AjaxFilter> it = ajaxFilterManager.getAjaxFilters(call.getScriptName());
            AjaxFilterChain chain = new AjaxFilterChain()
            {
                public Object doFilter(Object obj, Method meth, Object[] p) throws Exception
                {
                    AjaxFilter next = it.next();
                    return next.doFilter(obj, meth, p, this);
                }
            };
            Object reply = chain.doFilter(object, method, call.getParameters());
            return new Reply(call.getCallId(), reply);
        }
        catch (SecurityException ex)
        {
            log.debug("Security Exception: ", ex);

            // If we are in live mode, then we don't even say what went wrong
            if (debug)
            {
                return new Reply(call.getCallId(), null, ex);
            }
            else
            {
                return new Reply(call.getCallId(), null, new SecurityException());
            }
        }
        catch (InvocationTargetException ex)
        {
            // Allow Jetty RequestRetry exception to propagate to container
            Continuation.rethrowIfContinuation(ex);

            log.debug("Method execution failed: ", ex.getTargetException());
            return new Reply(call.getCallId(), null, ex.getTargetException());
        }
        catch (Exception ex)
        {
            // Allow Jetty RequestRetry exception to propagate to container
            Continuation.rethrowIfContinuation(ex);

            log.debug("Method execution failed: ", ex);
            return new Reply(call.getCallId(), null, ex);
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
     * Accessor for the ConverterManager that we configure
     * @param converterManager The new ConverterManager
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
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
     * To prevent a DoS attack we limit the max number of calls that can be
     * made in a batch
     * @param maxCallCount the maxCallCount to set
     */
    public void setMaxCallCount(int maxCallCount)
    {
        this.maxCallCount = maxCallCount;
    }

    /**
     * Set the debug status
     * @param debug The new debug setting
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * Are we in debug-mode and therefore more helpful at the expense of security?
     */
    private boolean debug = false;

    /**
     * What AjaxFilters apply to which Ajax calls?
     */
    private AjaxFilterManager ajaxFilterManager = null;

    /**
     * How we create new beans
     */
    protected CreatorManager creatorManager = null;

    /**
     * How we convert beans - or in this case create client side classes
     */
    protected ConverterManager converterManager = null;

    /**
     * The security manager
     */
    protected AccessControl accessControl = null;

    /**
     * If we need to override the default path
     */
    protected String overridePath = null;

    /**
     * This helps us test that access rules are being followed
     */
    protected boolean allowImpossibleTests = false;

    /**
     * To prevent a DoS attack we limit the max number of calls that can be
     * made in a batch
     */
    protected int maxCallCount = 20;

    /**
     * Generated Javascript cache
     */
    protected Map<String, String> methodCache = Collections.synchronizedMap(new HashMap<String, String>());

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultRemoter.class);
}
