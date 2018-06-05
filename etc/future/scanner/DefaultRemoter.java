package org.directwebremoting.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.directwebremoting.filter.LogAjaxFilter;
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
    public String generateInterfaceScript(String scriptName, String contextServletPath) throws SecurityException
    {
        StringBuilder buffer = new StringBuilder();

        buffer.append(createParameterDefinitions(scriptName));
        buffer.append(EnginePrivate.getEngineInitScript());
        buffer.append(createClassDefinition(scriptName));
        buffer.append(createPathDefinition(scriptName, contextServletPath));
        buffer.append(createMethodDefinitions(scriptName));

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
     * @param path The default path to the DWR servlet
     */
    protected String createPathDefinition(String scriptName, String path)
    {
        return scriptName + "._path = '" + getPathToDwrServlet(path) + "';\n\n";
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Remoter#getPathToDwrServlet(java.lang.String)
     */
    public String getPathToDwrServlet(String contextServletPath)
    {
        String actualPath = contextServletPath;
        if (overridePath != null)
        {
            actualPath = overridePath;
        }

        if (useAbsolutePath)
        {
            HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();

            StringBuffer absolutePath = new StringBuffer(48);

            String scheme = request.getScheme();
            int port = request.getServerPort();

            absolutePath.append(scheme);
            absolutePath.append("://");
            absolutePath.append(request.getServerName());

            if (port > 0 && 
                (("http".equalsIgnoreCase(scheme) && port != 80) ||
                 ("https".equalsIgnoreCase(scheme) && port != 443)))
            {
                absolutePath.append(':');
                absolutePath.append(port);
            }
            
            absolutePath.append(request.getContextPath());
            absolutePath.append(request.getServletPath());

            actualPath = absolutePath.toString();
        }

        return actualPath;
    }

    /**
     * Create a list of method definitions for the given creator.
     * @param fullCreatorName To allow AccessControl to allow/deny requests
     */
    protected String createMethodDefinitions(String fullCreatorName)
    {
        Creator creator = creatorManager.getCreator(fullCreatorName, false);
        String scriptName = creator.getJavascript();

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

    private StringBuilder createParameterDefinition(String clazz, String jsClassName, NamedConverter boConv)
    {
        Class<?> mappedType;
        StringBuilder paramBuffer = new StringBuilder();
        try
        {
            mappedType = LocalUtil.classForName(clazz);
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException(ex.getMessage());
        }

        if (!mappedType.isInterface())
        {
            String[] parts = jsClassName.split("\\.");
            if (parts.length == 1)
            {
                paramBuffer.append("\nif (typeof this['").append(jsClassName).append("'] != 'function') {\n").append("  function ").append(jsClassName).append("() {\n");
            }
            else
            {
                for (int i = 0; i < parts.length - 1; i++)
                {
                    paramBuffer.append("\nif (typeof ");
                    for (int j = 0; j <= i; j++)
                        paramBuffer.append(parts[j]).append(".");

                    paramBuffer.deleteCharAt(paramBuffer.length() - 1).append(" == \"undefined\") ");

                    if (i == 0)
                        paramBuffer.append("var ");

                    for (int j = 0; j <= i; j++)
                        paramBuffer.append(parts[j]).append(".");

                    paramBuffer.deleteCharAt(paramBuffer.length() - 1).append(" = {};\n");
                }

                paramBuffer.append("\nif (typeof ").append(jsClassName).append(" != 'function') {\n  ").append(jsClassName).append(" = function ").append(parts[parts.length - 1]).append(" () {\n");
            }

            Map<String, Property> properties = boConv.getPropertyMapFromClass(mappedType, true, true);
            for (Entry<String, Property> entry : properties.entrySet())
            {
                String name = entry.getKey();
                Property property = entry.getValue();
                Class<?> propType = property.getPropertyType();
                paramBuffer.append("    this.").append(name).append(" = ");
                if (propType.isArray() || List.class.isAssignableFrom(propType))
                    paramBuffer.append("[];\n");
                else if (propType == boolean.class)
                    paramBuffer.append("false;\n");
                else if (propType.isPrimitive())
                    paramBuffer.append("0;\n");
                else
                    paramBuffer.append("null;\n");
            }
            paramBuffer.append("  }\n").append("}\n");
        }
        return paramBuffer;
    }

    /**
     * Output the class definitions for all the converted objects.
     * An optimization for this class might be to only generate class
     * definitions for classes used as parameters in the class that we are
     * currently generating a proxy for.
     * <p>Currently the <code>scriptName</code> parameter is not used, we just
     * generate the class definitions for all types, however conceptually, it
     * should be used
     * @param scriptName The script for which we are generating parameter classes
     */
    protected String createParameterDefinitions(String scriptName) {
        StringBuilder buffer = new StringBuilder();
        for (String match : converterManager.getConverterMatchStrings()) {
            try {
                Converter conv = converterManager.getConverterByMatchString(match);
                if (conv instanceof NamedConverter) {
                    NamedConverter boConv = (NamedConverter) conv;
                    String jsClassName = boConv.getJavascript();
                    if (jsClassName != null && !"".equals(jsClassName)) {
                        if (!match.contains("*")) {
                            if ("*".equals(jsClassName)) jsClassName = match.substring(match.lastIndexOf('.') + 1);
                            if ("**".equals(jsClassName)) jsClassName = match;
                            buffer.append(createParameterDefinition(match, jsClassName, boConv));
                        } else {
                            ClasspathScanner scanner = new ClasspathScanner(match);
                            for (String clazz : scanner.getClasses()) {
                                buffer.append(createParameterDefinition(clazz, "*".equals(jsClassName) ? clazz.substring(clazz.lastIndexOf('.') + 1) : clazz, boConv));
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                log.warn("Failed to create parameter declaration for " + match, ex);
                buffer.append("// Missing parameter declaration for ").append(match).append(". See the server logs for details.");
            }
        }
        return buffer.append('\n').toString();
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
        Class<?>[] paramTypes = method.getParameterTypes();

        // Create the sdoc comment
        buffer.append("/**\n");
        for (int j = 0; j < paramTypes.length; j++)
        {
            if (!LocalUtil.isServletClass(paramTypes[j]))
            {
                buffer.append(" * @param {" + paramTypes[j] + "} p" + j + " a param\n");
            }
        }
        buffer.append(" * @param {function|Object} callback callback function or options object\n");
        buffer.append(" */\n");

        // Create the function definition
        buffer.append(scriptName + '.' + methodName + " = function(");
        for (int j = 0; j < paramTypes.length; j++)
        {
            if (!LocalUtil.isServletClass(paramTypes[j]))
            {
                buffer.append("p" + j + ", ");
            }
        }
        buffer.append("callback) {\n");

        // The method body calls into engine.js
        String executeFunctionName = EnginePrivate.getExecuteFunctionName();
        buffer.append("  return " + executeFunctionName + "(" + scriptName + "._path, '" + scriptName + "', '" + methodName + "\', ");
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
        // We set this up here because if something goes wrong we want to know
        // if there are any LogAjaxFilter implementations to provide any logging
        List<AjaxFilter> filters = ajaxFilterManager.getAjaxFilters(call.getScriptName());

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
            Creator creator = creatorManager.getCreator(call.getScriptName(), true);

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
                buffer.append("id=");
                buffer.append(call.getCallId());

                log.debug(buffer.toString());
            }

            // Execute the filter chain method.toString()
            final Iterator<AjaxFilter> it = filters.iterator();
            AjaxFilterChain chain = new AjaxFilterChain()
            {
                public Object doFilter(Object obj, Method meth, Object[] params) throws Exception
                {
                    if (it.hasNext())
                    {
                        AjaxFilter next = it.next();
                        return next.doFilter(obj, meth, params, this);
                    }
                    else
                    {
                        return meth.invoke(obj, params);
                    }
                }
            };
            Object reply = chain.doFilter(object, method, call.getParameters());
            return new Reply(call.getCallId(), reply);
        }
        catch (SecurityException ex)
        {
            if (!filtersIncludeLogging(filters))
            {
                log.warn("Security Exception: " + ex.getMessage());
            }

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
            debugException(filters, ex.getTargetException());
            return new Reply(call.getCallId(), null, ex.getTargetException());
        }
        catch (Exception ex)
        {
            // Allow Jetty RequestRetry exception to propagate to container
            Continuation.rethrowIfContinuation(ex);
            debugException(filters, ex);
            return new Reply(call.getCallId(), null, ex);
        }
    }

    /**
     * Do logging output if there are no logging filters and add a note of
     * explanation the first time
     * @param filters The configured filters
     * @param ex The exception saying what broke
     */
    private void debugException(List<AjaxFilter> filters, Throwable ex)
    {
        if (debug && !filtersIncludeLogging(filters))
        {
            if (!givenAuditLogHint)
            {
                log.debug("No logging filters defined. Minimal execption logging. For more detail add <filter class='org.directwebremoting.filter.AuditLogAjaxFilter'/> to dwr.xml");
                givenAuditLogHint = true;
            }
            log.debug("Method execution failed: ", ex);
        }
    }

    /**
     * A quick check to see if we are already doing some form of logging
     * @param filters The list of configured filters
     * @return true if we are logging
     */
    private boolean filtersIncludeLogging(List<AjaxFilter> filters)
    {
        for (AjaxFilter element : filters)
        {
            if (element instanceof LogAjaxFilter)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * By default we use a relative path to the DWR servlet which can help if
     * there are several routes to the servlet. However it can be a pain if
     * the DWR engine is running on a different port from the web-server.
     * However this is a minority case so this is not officially supported.
     * @param useAbsolutePath Does DWR generate an absolute _path property
     */
    public void setUseAbsolutePath(boolean useAbsolutePath)
    {
        this.useAbsolutePath = useAbsolutePath;
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
     * Have we given the hint about {@link org.directwebremoting.filter.AuditLogAjaxFilter}
     */
    protected boolean givenAuditLogHint = false;

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
     * @see #setUseAbsolutePath(boolean)
     */
    protected boolean useAbsolutePath = false;

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
