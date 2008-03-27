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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import uk.ltd.getahead.dwr.AccessControl;
import uk.ltd.getahead.dwr.Call;
import uk.ltd.getahead.dwr.Calls;
import uk.ltd.getahead.dwr.ConversionConstants;
import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.TypeHintContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.OutboundVariable;
import uk.ltd.getahead.dwr.WebContext;
import uk.ltd.getahead.dwr.WebContextFactory;
import uk.ltd.getahead.dwr.util.JavascriptUtil;
import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * This class represents a query made by a client in terms of the data that is
 * passed in to be converted to Java objects.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ExecuteQuery
{
    /**
     * Simple ctor
     * @param creatorManager The way we get an object to call methods on
     * @param converterManager The way we convert javascript to java
     * @param accessControl The security manager
     */
    public ExecuteQuery(CreatorManager creatorManager, ConverterManager converterManager, AccessControl accessControl)
    {
        this.creatorManager = creatorManager;
        this.converterManager = converterManager;
        this.accessControl = accessControl;
    }

    /**
     * Check (as far as we can) that the execute method will succeed.
     * @param req The original browser's request
     * @return The call details the methods we are calling
     * @throws IOException If reading from the request body stream fails
     */
    public Calls execute(HttpServletRequest req) throws IOException
    {
        Calls calls = null;

        if (req.getMethod().equals("GET")) //$NON-NLS-1$
        {
            calls = parseParameters(parseGet(req));
        }
        else
        {
            calls = parseParameters(parsePost(req));
        }

        // Since we are passing all the responses back in one script, there is
        // only one outbound context.
        OutboundContext converted = new OutboundContext();

        for (int callNum = 0; callNum < calls.getCallCount(); callNum++)
        {
            Call call = calls.getCall(callNum);
            InboundContext inctx = call.getInboundContext();

            try
            {
                // Get a list of the available matching methods with the coerced
                // parameters that we will use to call it if we choose to use that
                // method.
                Creator creator = creatorManager.getCreator(call.getScriptName());

                // Which method are we using?
                Method method = findMethod(call);
                if (method == null)
                {
                    String name = call.getScriptName() + '.' + call.getMethodName();
                    throw new IllegalArgumentException(Messages.getString("ExecuteQuery.UnknownMethod", name)); //$NON-NLS-1$
                }

                // Check this method is accessible
                String reason = accessControl.getReasonToNotExecute(req, creator, call.getScriptName(), method);
                if (reason != null)
                {
                    log.error("Access denied: " + reason); //$NON-NLS-1$
                    log.error("  From: "  + req.getRemoteAddr() + " asking for: " + req.getRequestURI()); //$NON-NLS-1$ //$NON-NLS-2$
                    throw new SecurityException(Messages.getString("ExecuteQuery.AccessDenied")); //$NON-NLS-1$
                }

                // Convert all the parameters to the correct types
                Object[] params = new Object[method.getParameterTypes().length];
                for (int j = 0; j < method.getParameterTypes().length; j++)
                {
                    try
                    {
                        Class paramType = method.getParameterTypes()[j];
                        InboundVariable param = inctx.getParameter(callNum, j);
                        TypeHintContext incc = new TypeHintContext(method, j);

                        params[j] = converterManager.convertInbound(paramType, param, inctx, incc);
                    }
                    catch (ConversionException ex)
                    {
                        throw new ConversionException(Messages.getString("ExecuteQuery.ConversionError", call.getScriptName(), call.getMethodName(), ex.getMessage()), ex); //$NON-NLS-1$
                    }
                }

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

                    // We might need to remember it for next time
                    if (scope.equals(Creator.APPLICATION))
                    {
                        webcx.getServletContext().setAttribute(call.getScriptName(), object);
                    }
                    else if (scope.equals(Creator.SESSION))
                    {
                        webcx.getSession().setAttribute(call.getScriptName(), object);
                    }
                    else if (scope.equals(Creator.REQUEST))
                    {
                        webcx.getHttpServletRequest().setAttribute(call.getScriptName(), object);
                    }
                    // Creator.PAGE scope means we create one every time anyway
                }

                // Some debug
                log.info("Exec[" + callNum + "]: " + call.getScriptName() + "." + call.getMethodName() + "()"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
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

                    buffer.append(". Call params ("); //$NON-NLS-1$
                    for (int j = 0; j < inctx.getParameterCount(callNum); j++)
                    {
                        if (j != 0)
                        {
                            buffer.append(", "); //$NON-NLS-1$
                        }
                        InboundVariable param = inctx.getParameter(callNum, j);
                        buffer.append(param.toString());
                    }
                    buffer.append(") id="); //$NON-NLS-1$
                    buffer.append(call.getId());

                    buffer.append(". Using ("); //$NON-NLS-1$
                    buffer.append(calls.isXhrMode() ? "XHR" : "IFrame"); //$NON-NLS-1$ //$NON-NLS-2$
                    buffer.append(","); //$NON-NLS-1$
                    buffer.append(req.getMethod());
                    buffer.append(")"); //$NON-NLS-1$

                    log.debug(buffer.toString());
                }

                // Execute
                Object reply = method.invoke(object, params);

                OutboundVariable ov = converterManager.convertOutbound(reply, converted);
                call.setReply(ov);
            }
            catch (InvocationTargetException ex)
            {
                log.warn("Method execution failed: ", ex.getTargetException()); //$NON-NLS-1$
                call.setThrowable(convertException(converted, ex.getTargetException()));
            }
            catch (Throwable ex)
            {
                log.warn("Method execution failed: ", ex); //$NON-NLS-1$
                call.setThrowable(convertException(converted, ex));
            }
        }

        return calls;
    }

    /**
     * Parse an HTTP POST request to fill out the scriptName, methodName and
     * paramList properties. This method should not fail unless it will not
     * be possible to return any sort of error to the user. Failure cases should
     * be handled by the <code>checkParams()</code> method.
     * @param req The original browser's request
     * @return The equivalent of HttpServletRequest.getParameterMap() for now
     * @throws IOException If reading from the request body stream fails
     */
    private Map parsePost(HttpServletRequest req) throws IOException
    {
        Map paramMap = new HashMap();

        // I've had reports of data loss in Tomcat 5.0 that relate to this bug
        //   http://issues.apache.org/bugzilla/show_bug.cgi?id=27447
        // See mails to users@dwr.dev.java.net:
        //   Subject: "Tomcat 5.x read-ahead problem"
        //   From: CAKALIC, JAMES P [AG-Contractor/1000]
        // It would be more normal to do the following:
        // BufferedReader in = req.getReader();
        BufferedReader in = new BufferedReader(new InputStreamReader(req.getInputStream()));

        if (in == null)
        {
            // it is not a post message
            throw new RuntimeException(Messages.getString("ExecuteQuery.ErrorNullPost")); //$NON-NLS-1$
        }

        int lines = 0;
        while (true)
        {
            String line = in.readLine();

            if (line == null)
            {
                break;
            }

            lines++;

            if (line.indexOf('&') != -1)
            {
                // If there are any &s then this must be iframe post and all the
                // parameters have got dumped on one line, split with &
                log.debug("Using iframe POST mode"); //$NON-NLS-1$
                StringTokenizer st = new StringTokenizer(line, "&"); //$NON-NLS-1$
                while (st.hasMoreTokens())
                {
                    String part = st.nextToken();
                    part = LocalUtil.decode(part);

                    parsePostLine(part, paramMap);
                }
            }
            else
            {
                // Horay, this is a normal one!
                parsePostLine(line, paramMap);
            }
        }

        // If there is only 1 param then this must be a broken Safari. All
        // the parameters have got dumped on one line split with \n
        // See: http://bugzilla.opendarwin.org/show_bug.cgi?id=3565
        //      https://dwr.dev.java.net/issues/show_bug.cgi?id=93
        //      http://jira.atlassian.com/browse/JRA-8354
        //      http://developer.apple.com/internet/safari/uamatrix.html
        if (paramMap.size() == 1)
        {
            // This looks like a broken Mac where the line endings are confused
            log.debug("Using Broken Safari POST mode"); //$NON-NLS-1$

            // Iterators insist that we call hasNext() before we next()
            Iterator it = paramMap.keySet().iterator();
            if (!it.hasNext())
            {
                throw new IllegalStateException("No entries in non empty map!"); //$NON-NLS-1$
            }

            // So get the first
            String key = (String) it.next();
            String value = (String) paramMap.remove(key);
            String line = key + ConversionConstants.INBOUND_DECL_SEPARATOR + value;

            StringTokenizer st = new StringTokenizer(line, "\n"); //$NON-NLS-1$
            while (st.hasMoreTokens())
            {
                String part = st.nextToken();
                part = LocalUtil.decode(part);

                parsePostLine(part, paramMap);
            }
        }

        return paramMap;
    }

    /**
     * Sort out a single line in a POST request
     * @param line The line to parse
     * @param paramMap The map to add parsed parameters to
     */
    private void parsePostLine(String line, Map paramMap)
    {
        if (line.length() == 0)
        {
            return;
        }

        int sep = line.indexOf(ConversionConstants.INBOUND_DECL_SEPARATOR);
        if (sep == -1)
        {
            log.warn("Missing separator in POST line: " + line); //$NON-NLS-1$
        }
        else
        {
            String key = line.substring(0, sep);
            String value = line.substring(sep  + ConversionConstants.INBOUND_DECL_SEPARATOR.length());

            paramMap.put(key, value);
        }
    }

    /**
     * Parse an HTTP GET request to fill out the scriptName, methodName and
     * paramList properties. This method should not fail unless it will not
     * be possible to return any sort of error to the user. Failure cases should
     * be handled by the <code>checkParams()</code> method.
     * @param req The original browser's request
     * @return Simply HttpServletRequest.getParameterMap() for now
     * @throws IOException If the parsing fails
     */
    private Map parseGet(HttpServletRequest req) throws IOException
    {
        Map convertedMap = new HashMap();
        Map paramMap = req.getParameterMap();

        for (Iterator it = paramMap.keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            String[] array = (String[]) paramMap.get(key);

            if (array.length == 1)
            {
                convertedMap.put(key, array[0]);
            }
            else
            {
                throw new IOException(Messages.getString("ExecuteQuery.MultiValues", key)); //$NON-NLS-1$
            }
        }

        return convertedMap;
    }

    /**
     * Fish out the important parameters
     * @param paramMap The string/string map to convert
     * @return The call details the methods we are calling
     * @throws IOException If the parsing of input parameter fails
     */
    private Calls parseParameters(Map paramMap) throws IOException
    {
        Calls calls = new Calls();

        // XML mode is common to all calls
        calls.setXhrMode(Boolean.valueOf((String) paramMap.remove(ConversionConstants.INBOUND_KEY_XMLMODE)).booleanValue());

        // Work out how many calls are in this packet
        String callStr = (String) paramMap.remove(ConversionConstants.INBOUND_CALL_COUNT);
        int callCount;
        try
        {
            callCount = Integer.parseInt(callStr);
        }
        catch (NumberFormatException ex)
        {
            throw new IOException(Messages.getString("ExecuteQuery.BadCallCount", callStr)); //$NON-NLS-1$
        }

        // Extract the ids, scriptnames and methodnames
        for (int callNum = 0; callNum < callCount; callNum++)
        {
            Call call = new Call();
            calls.addCall(call);

            String prefix = ConversionConstants.INBOUND_CALLNUM_PREFIX + callNum + ConversionConstants.INBOUND_CALLNUM_SUFFIX;

            // The special values
            call.setId((String) paramMap.remove(prefix + ConversionConstants.INBOUND_KEY_ID));
            call.setScriptName((String) paramMap.remove(prefix + ConversionConstants.INBOUND_KEY_SCRIPTNAME));
            call.setMethodName((String) paramMap.remove(prefix + ConversionConstants.INBOUND_KEY_METHODNAME));

            // Look for parameters to this method
            for (Iterator it = paramMap.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();

                if (key.startsWith(prefix))
                {
                    String data = (String) entry.getValue();
                    String[] split = LocalUtil.splitInbound(data);

                    String value = split[LocalUtil.INBOUND_INDEX_VALUE];
                    String type = split[LocalUtil.INBOUND_INDEX_TYPE];
                    call.getInboundContext().createInboundVariable(callNum, key, type, value);
                    it.remove();
                }
            }
        }

        if (paramMap.size() != 0)
        {
            paramMap.remove(ConversionConstants.BROKEN_SAFARI2);
            if (paramMap.size() != 0)
            {
                log.warn("Entries left over in parameter map"); //$NON-NLS-1$
            }
        }

        return calls;
    }

    /**
     * Find the method the best matches the method name and parameters
     * @param call The function call we are going to make
     * @return A matching method, or null if one was not found.
     */
    private Method findMethod(Call call)
    {
        if (call.getScriptName() == null)
        {
            throw new IllegalArgumentException(Messages.getString("ExecuteQuery.MissingClassParam")); //$NON-NLS-1$
        }

        if (call.getMethodName() == null)
        {
            throw new IllegalArgumentException(Messages.getString("ExecuteQuery.MissingMethodParam")); //$NON-NLS-1$
        }

        Creator creator = creatorManager.getCreator(call.getScriptName());
        Method[] methods = creator.getType().getMethods();
        List available = new ArrayList();

        methods:
        for (int i = 0; i < methods.length; i++)
        {
            // Check method name and access
            if (methods[i].getName().equals(call.getMethodName()))
            {
                // Check number of parameters
                if (methods[i].getParameterTypes().length == call.getInboundContext().getParameterCount())
                {
                    // Clear the previous conversion attempts (the param types
                    // will probably be different)
                    call.getInboundContext().clearConverted();

                    // Check parameter types
                    for (int j = 0; j < methods[i].getParameterTypes().length; j++)
                    {
                        Class paramType = methods[i].getParameterTypes()[j];
                        if (!converterManager.isConvertable(paramType))
                        {
                            // Give up with this method and try the next
                            continue methods;
                        }
                    }

                    available.add(methods[i]);
                }
            }
        }

        // Pick a method to call
        if (available.size() > 1)
        {
            log.warn("Warning multiple matching methods. Using first match."); //$NON-NLS-1$
        }

        // At the moment we are just going to take the first match, for a
        // later increment we might pack the best implementation
        if (available.isEmpty())
        {
            return null;
        }

        return (Method) available.get(0);
    }

    /**
     * Convert an exception into an outbound variable
     * @param converted The conversion context
     * @param th The exception to be converted
     * @return A new outbound exception
     */
    private OutboundVariable convertException(OutboundContext converted, Throwable th)
    {
        try
        {
            if (converterManager.isConvertable(th.getClass()))
            {
                return converterManager.convertOutbound(th, converted);
            }
        }
        catch (ConversionException ex)
        {
            log.warn("Exception while converting. Exception to be converted: " + th, ex); //$NON-NLS-1$
        }

        // So we will have to create one for ourselves
        OutboundVariable ov = new OutboundVariable();
        String varName = converted.getNextVariableName();
        ov.setAssignCode(varName);
        ov.setInitCode("var " + varName + " = \"" + jsutil.escapeJavaScript(th.getMessage()) + "\";"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        return ov;
    }

    /**
     * The means by which we strip comments
     */
    private JavascriptUtil jsutil = new JavascriptUtil();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ExecuteQuery.class);

    /**
     * The converter manager that decides how parameters are converted
     */
    private ConverterManager converterManager = null;

    /**
     * The DefaultCreatorManager to which we delegate creation of new objects.
     */
    private CreatorManager creatorManager = null;

    /**
     * The security manager
     */
    private AccessControl accessControl = null;
}
