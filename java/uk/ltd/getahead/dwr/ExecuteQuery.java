package uk.ltd.getahead.dwr;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import uk.ltd.getahead.dwr.util.Log;

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

        if (req.getParameter(ConversionConstants.INBOUND_KEY_ID) != null)
        {
            parseParameters(parseGet(req));
        }
        else
        {
            parseParameters(parsePost(req));
        }

        Log.debug("Exec: " + toString()); //$NON-NLS-1$
    }

    /**
     * Parse an HTTP POST request to fill out the className, methodName and
     * paramList properties. This method should not fail unless it will not
     * be possible to return any sort of error to the user. Failure cases should
     * be handled by the <code>checkParams()</code> method.
     * @param req The incoming request
     * @return The equivalent of HttpServletRequest.getParameterMap() for now
     */
    private Map parsePost(HttpServletRequest req)
    {
        Map parammap = new HashMap();

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

                int sep = line.indexOf(ConversionConstants.INBOUND_DECL_SEPARATOR);
                if (sep == -1)
                {
                    Log.warn("Missing separator in POST line: " + line); //$NON-NLS-1$
                }
                else
                {
                    String key = line.substring(0, sep);
                    String value = line.substring(sep  + ConversionConstants.INBOUND_DECL_SEPARATOR.length());

                    parammap.put(key, value);
                }
            }
        }
        catch (IOException ex)
        {
            delayed = ex;
        }

        return parammap;
    }

    /**
     * Parse an HTTP GET request to fill out the className, methodName and
     * paramList properties. This method should not fail unless it will not
     * be possible to return any sort of error to the user. Failure cases should
     * be handled by the <code>checkParams()</code> method.
     * @param req The incoming request
     * @return Simply HttpServletRequest.getParameterMap() for now
     */
    private Map parseGet(HttpServletRequest req)
    {
        return req.getParameterMap();
    }

    /**
     * Fish out the important parameters
     * @param parammap The string/string map to convert
     */
    private void parseParameters(Map parammap)
    {
        // The special values
        id = (String) parammap.remove(ConversionConstants.INBOUND_KEY_ID);
        className = (String) parammap.remove(ConversionConstants.INBOUND_KEY_CLASSNAME);
        methodName = (String) parammap.remove(ConversionConstants.INBOUND_KEY_METHODNAME);
        xmlMode = Boolean.valueOf((String) parammap.remove(ConversionConstants.INBOUND_KEY_XMLMODE)).booleanValue();

        // Look through the params and convert to InboundVariable.
        for (Iterator it = parammap.keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            String data = (String) parammap.get(key);
            String[] split = splitInbound(data);

            inctx.createInboundVariable(key, split[INBOUND_INDEX_TYPE], split[INBOUND_INDEX_VALUE]);
        }
    }

    /**
     * Check (as far as we can) that the execute method will succeed.
     * @return The return from the method invocation
     * @throws Throwable Almost anything could go wrong. We're not exception
     * wrapping and we are unwrapping InvocationTargetException
     */
    public Object execute() throws Throwable
    {
        if (delayed != null)
        {
            throw delayed;
        }

        // Get a list of the available matching methods with the coerced
        // parameters that we will use to call it if we choose to use that
        // method.
        Creator creator = creatorManager.getCreator(className);

        // Which method are we using?
        Method method = findMethod();
        if (method == null)
        {
            throw new IllegalArgumentException(Messages.getString("ExecuteQuery.UnknownMethod", toString())); //$NON-NLS-1$
        }

        // Convert all the parameters to the correct types
        Object[] converted = new Object[method.getParameterTypes().length];
        for (int j = 0; j < method.getParameterTypes().length; j++)
        {
            Class paramType = method.getParameterTypes()[j];
            InboundVariable param = inctx.getParameter(j);
            converted[j] = converterManager.convertInbound(paramType, param, inctx);
        }

        try
        {
            // Create an instance
            Object object = creator.getInstance();

            // Execute
            Log.info("Executing: " + method.toString()); //$NON-NLS-1$
            return method.invoke(object, converted);
        }
        catch (InvocationTargetException ex)
        {
            throw ex.getTargetException();
        }
    }

    /**
     * Some browsers (i.e. Konq at least) send the request with no data).
     * Normally we except later on, but that clogs up the log files, so in the
     * short term we allow detection of requests from 'broken' browsers.
     * @return Did we get anything from the browser at all
     */
    public boolean isFailingBrowser()
    {
        return id == null && methodName == null && className == null;
    }

    /**
     * @return Are we in XMLHttpRequest mode
     */
    public boolean isXmlMode()
    {
        return xmlMode;
    }

    /**
     * @return The id of this query
     */
    public String getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer allParams = new StringBuffer();
        for (int i = 0; i < inctx.getParameterCount(); i++)
        {
            if (i != 0)
            {
                allParams.append(',');
            }
            allParams.append(inctx.getParameter(i));
        }

        StringBuffer allData = new StringBuffer();
        for (Iterator it = inctx.getInboundVariableNames(); it.hasNext();)
        {
            String key = (String) it.next();
            InboundVariable value = inctx.getInboundVariable(key);

            allData.append(key);
            allData.append('=');
            allData.append(value.getRawData());

            if (it.hasNext())
            {
                allData.append(',');
            }
        }

        return className + '.' + methodName + '(' + allParams + "); with " + allData; //$NON-NLS-1$
    }

    /**
     * Find the method the best matches the method name and parameters
     * @return A matching method, or null if one was not found.
     */
    private Method findMethod()
    {
        if (className == null)
        {
            throw new IllegalArgumentException(Messages.getString("ExecuteQuery.MissingClassParam")); //$NON-NLS-1$
        }

        if (methodName == null)
        {
            throw new IllegalArgumentException(Messages.getString("ExecuteQuery.MissingMethodParam")); //$NON-NLS-1$
        }

        Creator creator = creatorManager.getCreator(className);
        Method[] methods = creator.getType().getMethods();
        List available = new ArrayList();

        methods:
        for (int i = 0; i < methods.length; i++)
        {
            // Check method name and access
            if (methods[i].getName().equals(methodName))
            {
                // Check number of parameters
                if (methods[i].getParameterTypes().length == inctx.getParameterCount())
                {
                    // Clear the previous conversion attempts (the param types
                    // will probably be different)
                    inctx.clearConverted();

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
            Log.warn("Warning multiple matching methods. Using first match."); //$NON-NLS-1$
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
            Log.error("Missing : in conversion data"); //$NON-NLS-1$
            reply[INBOUND_INDEX_TYPE] = ConversionConstants.TYPE_STRING;
            reply[INBOUND_INDEX_VALUE] = data;
        }

        return reply;
    }

    private ConverterManager converterManager;
    private CreatorManager creatorManager;

    private boolean xmlMode;
    private String id;
    private String className;
    private String methodName;
    private IOException delayed;
    private InboundContext inctx = new InboundContext();
}
