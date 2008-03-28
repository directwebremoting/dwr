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
public class ExecuteQuery
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

        if (req.getParameter("id") != null)
        {
            parseParameters(parseGet(req));
        }
        else
        {
            parseParameters(parsePost(req));
        }

        Log.debug("Exec: " + toString());
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

                int sep = line.indexOf(SEPARATOR);
                if (sep == -1)
                {
                    Log.warn("Missing separator in POST line: " + line);
                }
                else
                {
                    String key = line.substring(0, sep);
                    String value = line.substring(sep  + SEPARATOR.length());

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
        id = (String) parammap.remove(KEY_ID);
        className = (String) parammap.remove(KEY_CLASSNAME);
        methodName = (String) parammap.remove(KEY_METHODNAME);
        xmlMode = Boolean.valueOf((String) parammap.remove(KEY_XMLMODE)).booleanValue();

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

        int colon = data.indexOf(":");
        if (colon != -1)
        {
            reply[INBOUND_INDEX_TYPE] = data.substring(0, colon);
            reply[INBOUND_INDEX_VALUE] = data.substring(colon + 1);
        }
        else
        {
            Log.error("Missing : in conversion data");
            reply[INBOUND_INDEX_TYPE] = "string";
            reply[INBOUND_INDEX_VALUE] = data;
        }

        return reply;
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

        if (className == null)
        {
            throw new IllegalArgumentException("Missing class parameter");
        }

        if (methodName == null)
        {
            throw new IllegalArgumentException("Missing method parameter");
        }

        // Get a list of the available matching methods with the coerced
        // parameters that we will use to call it if we choose to use that
        // method.
        Creator creator = creatorManager.getCreator(className);

        Method[] methods = creator.getType().getMethods();

        List available = new ArrayList();
        List coercedList = new ArrayList();
        methods:
        for (int i = 0; i < methods.length; i++)
        {
            // Check method name and access
            if (methods[i].getName().equals(methodName))
            {
                // Check number of parameters
                if (methods[i].getParameterTypes().length == inctx.getParameterCount())
                {
                    Object[] coerced = new Object[inctx.getParameterCount()];

                    // Clear the previous conversion attempts (the param types
                    // will probably be different)
                    inctx.clearConverted();

                    // Check parameter types
                    params:
                    for (int j = 0; j < methods[i].getParameterTypes().length; j++)
                    {
                        Class paramType = methods[i].getParameterTypes()[j];
                        InboundVariable param = inctx.getParameter(j);
                        coerced[j] = converterManager.convertInbound(paramType, param, inctx);
                        if (coerced[j] == null)
                        {
                            // Give up with this method and try the next
                            break methods;
                        }
                    }

                    available.add(methods[i]);
                    coercedList.add(coerced);
                }
            }
        }

        // Pick a method to call
        if (available.size() > 1)
        {
            Log.warn("Warning multiple matching methods. Using first match.");
        }

        Method method = null;
        Object[] converted = null;

        // At the moment we are just going to take the first match, for a
        // later increment we might pack the best implementation
        if (!available.isEmpty())
        {
            method = (Method) available.get(0);
            converted = (Object[]) coercedList.get(0);
        }

        // Complain if there is nothing to call
        if (method == null)
        {
            throw new IllegalArgumentException("Missing method: " + toString());
        }

        try
        {
            // Create an instance
            Object object = creator.getInstance();

            // Execute
            Log.info("Executing: " + method.toString());
            return method.invoke(object, converted);
        }
        catch (InvocationTargetException ex)
        {
            throw ex.getTargetException();
        }
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
                allParams.append(", ");
            }
            allParams.append(inctx.getParameter(i));
        }

        StringBuffer allData = new StringBuffer();
        for (Iterator it = inctx.getInboundVariableNames(); it.hasNext();)
        {
            String key = (String) it.next();
            InboundVariable value = inctx.getInboundVariable(key);

            allData.append(key);
            allData.append("=");
            allData.append(value.getRawData());

            if (it.hasNext())
            {
                allData.append(", ");
            }
        }

        return "" + className + "." + methodName + "(" + allParams + "); with " + allData;
    }

    private static final String SEPARATOR = "=";
    private static final String KEY_METHODNAME = "methodname";
    private static final String KEY_CLASSNAME = "classname";
    private static final String KEY_ID = "id";
    private static final String KEY_XMLMODE = "xml";

    private ConverterManager converterManager;
    private CreatorManager creatorManager;

    private boolean xmlMode;
    private String id;
    private String className;
    private String methodName;
    private IOException delayed;
    private InboundContext inctx = new InboundContext();
}
