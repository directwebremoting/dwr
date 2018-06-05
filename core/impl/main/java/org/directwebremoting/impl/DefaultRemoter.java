package org.directwebremoting.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.Call;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.MethodDeclaration;
import org.directwebremoting.extend.Module;
import org.directwebremoting.extend.ModuleManager;
import org.directwebremoting.extend.NamedConverter;
import org.directwebremoting.extend.Property;
import org.directwebremoting.extend.RealScriptSession;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.extend.Replies;
import org.directwebremoting.extend.Reply;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Loggers;

/**
 * An implementation of Remoter that delegates requests to a set of Modules
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Mike Wilson [mikewse at g mail dot com]
 */
public class DefaultRemoter implements Remoter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Remoter#generateInterfaceScript(java.lang.String, java.lang.String, java.lang.String)
     */
    public String generateInterfaceJavaScript(String scriptName, String indent, String assignVariable, String contextServletPath) throws SecurityException
    {
        // The desired output should follow this scheme (not wrapped by any
        // "if already defined clauses" as this is not used by all module
        // systems):
        // ( ) <indent><assignVariable> = {};
        // ( )
        // ( ) <indent>/**
        // ( ) <indent> * @param {<type>} p1 a param
        // ( ) <indent> * ...
        // ( ) <indent> * @param {function|Object} callback callback function or options object
        // ( ) <indent> */
        // ( ) <indent><assignVariable>.<methodName> = function(p1, ..., callback) {
        // ( ) <indent>  return dwr.engine._execute(<assignVariable>._path, <scriptName>, <methodName>, arguments);
        // ( ) <indent>};
        // ( )
        // ( ) ...

        StringBuilder buffer = new StringBuilder();

        buffer.append(indent + assignVariable + " = {};\n");

        Module module = moduleManager.getModule(scriptName, false);

        MethodDeclaration[] methods = module.getMethods();
        for (MethodDeclaration method : methods)
        {
            String methodName = method.getName();

            // We don't need to check accessControl.getReasonToNotExecute()
            // because the checks are made by the execute() method, but we do
            // check if we can display it
            try
            {
                accessControl.assertGeneralDisplayable(scriptName, method);
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

            Class<?>[] paramTypes = method.getParameterTypes();

            // Create the sdoc comment
            buffer.append("\n");
            buffer.append(indent + "/**\n");
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (!LocalUtil.isServletClass(paramTypes[j]))
                {
                    buffer.append(indent + " * @param {");
                    buffer.append(paramTypes[j]);
                    buffer.append("} p");
                    buffer.append(j);
                    buffer.append(" a param\n");
                }
            }
            buffer.append(indent + " * @param {function|Object} callback callback function or options object\n");
            buffer.append(indent + " */\n");

            // Create the function definition
            buffer.append(indent + assignVariable + "." + methodName + " = function(");
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (!LocalUtil.isServletClass(paramTypes[j]))
                {
                    buffer.append("p");
                    buffer.append(j);
                    buffer.append(", ");
                }
            }
            buffer.append("callback) {\n");

            // The method body calls into engine.js
            buffer.append(indent + "  return ");
            buffer.append(EnginePrivate.getExecuteFunctionName());
            buffer.append("(");
            buffer.append(assignVariable);
            buffer.append("._path, '");
            buffer.append(scriptName);
            buffer.append("', '");
            buffer.append(methodName);
            buffer.append("\', arguments);\n");
            buffer.append(indent + "};\n");
        }

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Remoter#generateDtoScript(java.lang.String, java.lang.String, java.lang.String)
     */
    public String generateDtoJavaScript(String jsClassName, String indent, String assignVariable) throws SecurityException
    {
        NamedConverter namedConv = converterManager.getNamedConverter(jsClassName);
        if (namedConv != null)
        {
            // The desired output should follow this scheme (not wrapped by any
            // "if already defined clauses" as this is not used by all module
            // systems):
            // (1) <indent><assignVariable> = function() {
            // (2) <indent>  this.myProp = <initial value>;
            // (2) <indent>  ...
            // (2) <indent>}
            // (3) <indent><assignVariable>.$dwrClassName = 'pkg.MyData';
            // (4) <indent><assignVariable>.$dwrClassMembers = {};
            // (5) <indent><assignVariable>.$dwrClassMembers.myProp = {};
            // (6) <indent><assignVariable>.createFromMap = dwr.engine._createFromMap;
            StringBuilder buf = new StringBuilder();

            // Generate (1): <indent><assignVariable> = function() {
            buf.append(indent + assignVariable + " = function() {\n");

            // Generate (2): <indent>  this.myProp = <initial value>;
            Map<String, Property> properties = namedConv.getPropertyMapFromClass(namedConv.getInstanceType(), true, true);
            for (Entry<String, Property> entry : properties.entrySet())
            {
                String name = entry.getKey();
                Property property = entry.getValue();
                Class<?> propType = property.getPropertyType();

                // Property name
                buf.append(indent + "  this.");
                buf.append(name);
                buf.append(" = ");

                // Default property values
                if (propType.isArray())
                {
                    buf.append("[]");
                }
                else if (propType == boolean.class)
                {
                    buf.append("false");
                }
                else if (propType.isPrimitive())
                {
                    buf.append("0");
                }
                else
                {
                    buf.append("null");
                }

                buf.append(";\n");
            }

            // Generate (2): <indent>}
            buf.append(indent + "}\n");

            // Generate (3): <indent><assignVariable>.$dwrClassName = 'pkg.MyData';
            buf.append(indent);
            buf.append(assignVariable);
            buf.append(".$dwrClassName = '");
            buf.append(jsClassName);
            buf.append("';\n");

            // Generate (4): <indent><assignVariable>.$dwrClassMembers = {};
            buf.append(indent + assignVariable);
            buf.append(".$dwrClassMembers = {};\n");

            // Generate (5): <indent><assignVariable>.$dwrClassMembers.myProp = {};
            for (Entry<String, Property> entry : properties.entrySet())
            {
                String name = entry.getKey();
                buf.append(indent);
                buf.append(assignVariable);
                buf.append(".$dwrClassMembers.");
                buf.append(name);
                buf.append(" = {};\n");
            }

            // Generate (6): <indent><assignVariable>.createFromMap = dwr.engine._createFromMap;
            buf.append(indent);
            buf.append(assignVariable);
            buf.append(".createFromMap = dwr.engine._createFromMap;\n");

            return buf.toString();
        }
        else
        {
            log.warn("Failed to create class definition for JS class " + jsClassName + " because it was not found.");
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Remoter#generateDtoInheritanceScript(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public String generateDtoInheritanceJavaScript(String indent, String classExpression, String superClassExpression, String delegateFunction)
    {
        // The desired output is something like this (not wrapped by any
        // "if already defined clauses" as this is not needed on all module
        // systems):
        //   <indent><classExpression>.prototype = <delegateFunction>(<superClassExpression>.prototype);
        //   <indent><classExpression>.prototype.constructor = <classExpression>;
        StringBuilder buf = new StringBuilder();
        buf.append(indent + classExpression);
        buf.append(".prototype = " + delegateFunction + "(");
        buf.append(superClassExpression);
        buf.append(".prototype);\n");
        buf.append(indent + classExpression);
        buf.append(".prototype.constructor = ");
        buf.append(classExpression);
        buf.append(";\n");
        return buf.toString();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Remoter#getPathToDwrServlet(java.lang.String)
     */
    public String getPathToDwrServlet(String contextServletPath)
    {
        String actualPath = contextServletPath;

        if (useAbsolutePath)
        {
            HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
            actualPath = LocalUtil.getFullUrlToDwrServlet(request);
        }

        return actualPath;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Remoter#execute(org.directwebremoting.Calls)
     */
    public Replies execute(Calls calls)
    {
        Replies replies = new Replies(calls);

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
            Module module = moduleManager.getModule(call.getScriptName(), true);

            MethodDeclaration method = call.getMethodDeclaration();

            // Do we already have an error?
            if (method == null || call.getException() != null)
            {
                return new Reply(call.getCallId(), null, call.getException());
            }

            // We don't need to check accessControl.getReasonToNotExecute()
            // because the checks are made by the doExec method, but we do check
            // if we can display it
            accessControl.assertGeneralExecutionIsPossible(call.getScriptName(), method);

            // Log the call details if the accessLogLevel is call.
            if (AccessLogLevel.getValue(this.accessLogLevel, debug).hierarchy() == 0)
            {
                StringBuffer buffer = new StringBuffer();
                buffer.append("Exec: ")
                      .append(call.getScriptName())
                      .append(".")
                      .append(call.getMethodDeclaration().toString());

                buffer.append(", ");
                buffer.append("id=");
                buffer.append(call.getCallId());

                Loggers.ACCESS.info(buffer.toString());
            }

            Object reply = module.executeMethod(method, call.getParameters());

            return new Reply(call.getCallId(), reply);
        }
        catch (SecurityException ex)
        {
            writeExceptionToAccessLog(ex);
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
            writeExceptionToAccessLog(ex.getTargetException());
            return new Reply(call.getCallId(), null, ex.getTargetException());
        }
        catch (Exception ex)
        {
            writeExceptionToAccessLog(ex);
            return new Reply(call.getCallId(), null, ex);
        }
        finally
        {
            // Update ScriptSession's httpSessionId in case session was created/invalidated while calling into user code
            WebContext webCtx = WebContextFactory.get();
            RealScriptSession scriptSession = (RealScriptSession) webCtx.getScriptSession();
            HttpSession httpSession = webCtx.getSession(false);
            String httpSessionId = (httpSession != null ? httpSession.getId() : null);
            // Null-safe string comparison
            if (scriptSession != null && !String.valueOf(httpSessionId).equals(String.valueOf(scriptSession.getHttpSessionId())))
            {
                // Note: we can do DCL as involved methods are synchronized
                synchronized (scriptSession)
                {
                    if (!String.valueOf(httpSessionId).equals(String.valueOf(scriptSession.getHttpSessionId())))
                    {
                        scriptSession.setHttpSessionId(httpSessionId);
                    }
                }
            }
        }
    }

    /**
     * Writes exceptions to the log based on the accessLogLevel init-param. Options are:
     * 1) exception (checked) - default for debug.
     * 2) runtimeexception (unchecked).
     * 3) error - default for production.
     * 4) off.
     * @param ex The exception saying what broke
     */
    private void writeExceptionToAccessLog(Throwable ex)
    {
        // This call is null safe and will always return an AccessLogLevel.
        AccessLogLevel accessLogLevelEnum = AccessLogLevel.getValue(this.accessLogLevel, debug);

        if (accessLogLevelEnum.hierarchy() <= 1 && ex instanceof Exception)
        {
            Loggers.ACCESS.info("Method execution failed: ", ex);
        }
        else if (accessLogLevelEnum.hierarchy() <= 2 && ex instanceof RuntimeException)
        {
            Loggers.ACCESS.info("Method execution failed: ", ex);
        }
        else if (accessLogLevelEnum.hierarchy() <= 3 && ex instanceof Error)
        {
            Loggers.ACCESS.info("Method execution failed: ", ex);
        }
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
     * Accessor for the ModuleManager that we configure
     * @param moduleManager The new ModuleManager
     */
    public void setModuleManager(ModuleManager moduleManager)
    {
        this.moduleManager = moduleManager;
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
     * When and what should we log? Options are (specified in the DWR servlet's init-params):
     * 1) call (start of call + successful return values).
     * 2) exception (checked) - default for debug.
     * 3) runtimeexception (unchecked).
     * 4) error - default for production.
     * 5) off.
     */
    public void setAccessLogLevel(String accessLogLevel)
    {
        this.accessLogLevel = accessLogLevel;
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
     * How we create new beans
     */
    protected ModuleManager moduleManager = null;

    /**
     * How we convert beans - or in this case create client side classes
     */
    protected ConverterManager converterManager = null;

    /**
     * The security manager
     */
    protected AccessControl accessControl = null;

    /**
     * When and what should we log? Options are (specified in the DWR servlet's init-params):
     * 1) call (start of call + successful return values).
     * 2) exception (checked) - default for debug.
     * 3) runtimeexception (unchecked).
     * 4) error - default for production.
     * 5) off.
     */
    protected String accessLogLevel = null;

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
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultRemoter.class);
}
