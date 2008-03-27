package uk.ltd.getahead.dwr;

import java.io.BufferedReader;
import java.io.IOException;
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

import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * This class represents a query made by a client in terms of the data that is
 * passed in to be converted to Java objects.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class ExecuteQuery
{
    /**
     * Simple ctor
     * @param req The users request
     * @param creatorManager The way we get an object to call methods on
     * @param converterManager The way we convert javascript to java
     */
    public ExecuteQuery(HttpServletRequest req, CreatorManager creatorManager, ConverterManager converterManager)
    {
        this.creatorManager = creatorManager;
        this.converterManager = converterManager;
        this.req = req;

        if (req.getMethod().equals("GET")) //$NON-NLS-1$
        {
            parseParameters(parseGet());
        }
        else
        {
            parseParameters(parsePost());
        }
    }

    /**
     * Parse an HTTP POST request to fill out the scriptName, methodName and
     * paramList properties. This method should not fail unless it will not
     * be possible to return any sort of error to the user. Failure cases should
     * be handled by the <code>checkParams()</code> method.
     * @return The equivalent of HttpServletRequest.getParameterMap() for now
     */
    private Map parsePost()
    {
        Map paramMap = new HashMap();

        try
        {
            BufferedReader in = req.getReader();
            while (true)
            {
                String line = in.readLine();

                if (line == null)
                {
                    break;
                }

                // If there are any &s then this must be iframe post and all the
                // parameters have got dumped on one line.
                if (line.indexOf('&') != -1)
                {
                    StringTokenizer st = new StringTokenizer(line, "&"); //$NON-NLS-1$
                    while (st.hasMoreTokens())
                    {
                        String part = st.nextToken();
                        part = LocalUtil.decode(part);

                        log.debug("iframe POST line: " + part); //$NON-NLS-1$
                        parsePostLine(part, paramMap);
                    }
                }
                else
                {
                    log.debug("POST line: " + line); //$NON-NLS-1$
                    parsePostLine(line, paramMap);
                }
            }
        }
        catch (IOException ex)
        {
            delayed = ex;
        }

        return paramMap;
    }

    /**
     * @param line
     * @param paramMap
     */
    private void parsePostLine(String line, Map paramMap)
    {
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
     * @return Simply HttpServletRequest.getParameterMap() for now
     */
    private Map parseGet()
    {
        Map convertedMap = new HashMap();
        Map paramMap = req.getParameterMap();

        for (Iterator it = paramMap.keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            Object value = paramMap.get(key);
            
            if (value != null)
            {
                if (value instanceof String[])
                {
                    String[] array = (String[]) value;
                    convertedMap.put(key, array[0]);

                    if (log.isDebugEnabled())
                    {
                        for (int i = 0; i < array.length; i++)
                        {
                            log.debug("GET line[" + i + "]: " + key + '=' + value); //$NON-NLS-1$ //$NON-NLS-2$
                        }
                    }
                }
                else
                {
                    convertedMap.put(key, value);
                    log.debug("GET line: " + key + '=' + value); //$NON-NLS-1$
                }
            }
        }

        return convertedMap;
    }

    /**
     * Fish out the important parameters
     * @param paramMap The string/string map to convert
     */
    private void parseParameters(Map paramMap)
    {
        // XML mode is common to all calls
        xmlMode = Boolean.valueOf((String) paramMap.remove(ConversionConstants.INBOUND_KEY_XMLMODE)).booleanValue();

        // Work out haw many calls are in this packet
        String callStr = (String) paramMap.remove(ConversionConstants.INBOUND_CALL_COUNT);
        if (callStr == null)
        {
            // If there was no indication of how many inbound calls, then assume 0;
            callStr = "0"; //$NON-NLS-1$
        }
        int callCount = Integer.parseInt(callStr);
        calls = new Call[callCount];

        // Extract the ids, scriptnames and methodnames
        for (int callNum = 0; callNum < calls.length; callNum++)
        {
            Call call = new Call();
            calls[callNum] = call;

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
                    String[] split = splitInbound(data);

                    call.getInboundContext().createInboundVariable(callNum, key, split[INBOUND_INDEX_TYPE], split[INBOUND_INDEX_VALUE]);
                    it.remove();
                }
            }
        }
        
        if (paramMap.size() != 0)
        {
            log.warn("Entries left over in parameter map"); //$NON-NLS-1$
        }

        if (log.isDebugEnabled())
        {
            for (int callNum = 0; callNum < calls.length; callNum++)
            {
                Call call = calls[callNum];

                log.debug("Call[" + callNum + "]: " + call.getScriptName() + '.' + call.getMethodName() + "();"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

                for (int i = 0; i < call.getInboundContext().getParameterCount(); i++)
                {
                    log.debug("  Param: " + i + '=' + call.getInboundContext().getParameter(callNum, i)); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }

            // We can just use 0 because they are all shared
            for (Iterator it = calls[0].getInboundContext().getInboundVariableNames(); it.hasNext();)
            {
                String key = (String) it.next();
                InboundVariable value = calls[0].getInboundContext().getInboundVariable(key);
    
                log.debug("  Env: " + key + '=' + value.getRawData()); //$NON-NLS-1$
            }
        }
    }

    /**
     * Check (as far as we can) that the execute method will succeed.
     * @return The return from the method invocation
     * wrapping and we are unwrapping InvocationTargetException
     */
    public Call[] execute()
    {
        if (delayed != null)
        {
            for (int callNum = 0; callNum < calls.length; callNum++)
            {
                calls[callNum].setThrowable(delayed);
            }

            return calls;
        }

        // Since we are passing all the responses back in one script, there is
        // only one outbound context.
        OutboundContext converted = new OutboundContext();

        for (int callNum = 0; callNum < calls.length; callNum++)
        {
            Call call = calls[callNum];
            InboundContext inctx = call.getInboundContext();

            try
            {
                // Get a list of the available matching methods with the coerced
                // parameters that we will use to call it if we choose to use that
                // method.
                Creator creator = creatorManager.getCreator(call.getScriptName());

                // Which method are we using?
                Method method = findMethod(callNum);
                if (method == null)
                {
                    String name = call.getScriptName() + "." + call.getMethodName(); //$NON-NLS-1$
                    throw new IllegalArgumentException(Messages.getString("ExecuteQuery.UnknownMethod", name)); //$NON-NLS-1$
                }

                // Check this method is accessible
                String reason = Factory.getDoorman().getReasonToNotExecute(req, creator, call.getScriptName(), method);
                if (reason != null)
                {
                    log.error("Access denied: " + reason); //$NON-NLS-1$
                    throw new SecurityException(Messages.getString("ExecuteQuery.AccessDenied")); //$NON-NLS-1$
                }

                // Convert all the parameters to the correct types
                Object[] params = new Object[method.getParameterTypes().length];
                for (int j = 0; j < method.getParameterTypes().length; j++)
                {
                    Class paramType = method.getParameterTypes()[j];

                    InboundVariable param = inctx.getParameter(callNum, j);
                    inctx.pushContext(method, j);
                    params[j] = converterManager.convertInbound(paramType, param, inctx);
                    inctx.popContext(method, j);
                }

                // Get ourselves an object to execute a method on unless the
                // method is static
                Object object = null;
                if (!Modifier.isStatic(method.getModifiers()))
                {
                    String scope = creator.getScope();
                    ExecutionContext execCtx = ExecutionContext.get();

                    // Check the various scopes to see if it is there
                    if (scope.equals(Creator.APPLICATION))
                    {
                        object = execCtx.getServletContext().getAttribute(call.getScriptName());
                    }
                    else if (scope.equals(Creator.SESSION))
                    {
                        object = execCtx.getSession().getAttribute(call.getScriptName());
                    }
                    else if (scope.equals(Creator.REQUEST))
                    {
                        object = execCtx.getHttpServletRequest().getAttribute(call.getScriptName());
                    }
                    // Creator.PAGE scope means we create one every time anyway

                    // If we don't have an object the call the creator
                    if (object == null)
                    {
                        // Create an instance
                        object = creator.getInstance();
                    }

                    // We might need to remember it for next time
                    if (scope.equals(Creator.APPLICATION))
                    {
                        execCtx.getServletContext().setAttribute(call.getScriptName(), object);
                    }
                    else if (scope.equals(Creator.SESSION))
                    {
                        execCtx.getSession().setAttribute(call.getScriptName(), object);
                    }
                    else if (scope.equals(Creator.REQUEST))
                    {
                        execCtx.getHttpServletRequest().setAttribute(call.getScriptName(), object);
                    }
                    // Creator.PAGE scope means we create one every time anyway
                }

                // Execute
                log.info("Executing: " + method.toString()); //$NON-NLS-1$
                Object reply = method.invoke(object, params);

                OutboundVariable ov = converterManager.convertOutbound(reply, converted);
                call.setReply(ov);
            }
            catch (InvocationTargetException ex)
            {
                call.setThrowable(ex.getTargetException());
            }
            catch (Throwable ex)
            {
                call.setThrowable(ex);
            }
        }

        return calls;
    }

    /**
     * Some browsers (i.e. Konq at least) send the request with no data).
     * Normally we except later on, but that clogs up the log files, so in the
     * short term we allow detection of requests from 'broken' browsers.
     * @return Did we get anything from the browser at all
     */
    public boolean isFailingBrowser()
    {
        return calls.length == 0;
    }

    /**
     * @return Are we in XMLHttpRequest mode
     */
    public boolean isXmlMode()
    {
        return xmlMode;
    }

    /**
     * Find the method the best matches the method name and parameters
     * @param callNum The call number to work on
     * @return A matching method, or null if one was not found.
     */
    private Method findMethod(int callNum)
    {
        Call call = calls[callNum];

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
                    params:
                    for (int j = 0; j < methods[i].getParameterTypes().length; j++)
                    {
                        Class paramType = methods[i].getParameterTypes()[j];
                        if (!converterManager.isConvertable(paramType))
                        {
                            // Give up with this method and try the next
                            break methods;
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
     * splitInbound() returns the type info in this parameter
     */
    public static final int INBOUND_INDEX_TYPE = 0;

    /**
     * splitInbound() returns the value info in this parameter
     */
    public static final int INBOUND_INDEX_VALUE = 1;

    /**
     * The javascript outbound marshaller prefixes the toString value with a
     * colon and the original type information. This undoes that.
     * @param data The string to be split up
     * @return A string array containing the split data
     */
    public static String[] splitInbound(String data)
    {
        String[] reply = new String[2];

        int colon = data.indexOf(ConversionConstants.INBOUND_TYPE_SEPARATOR);
        if (colon != -1)
        {
            reply[INBOUND_INDEX_TYPE] = data.substring(0, colon);
            reply[INBOUND_INDEX_VALUE] = data.substring(colon + 1);
        }
        else
        {
            log.error("Missing : in conversion data (" + data + ')'); //$NON-NLS-1$
            reply[INBOUND_INDEX_TYPE] = ConversionConstants.TYPE_STRING;
            reply[INBOUND_INDEX_VALUE] = data;
        }

        return reply;
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(ExecuteQuery.class);

    private ConverterManager converterManager;
    private CreatorManager creatorManager;
    private HttpServletRequest req;

    private boolean xmlMode;
    private IOException delayed;
    private Call[] calls;
}
