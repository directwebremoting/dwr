package org.directwebremoting.extend;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Call is a POJO to encapsulate the information required to make a single java
 * call, including the result of the call (either returned data or exception).
 * Either the Method and Parameters should be filled in to allow a call to be
 * made or, the exception should be filled in indicating that things have gone
 * wrong already.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Call
{
    /**
     * @return the exception
     */
    public Throwable getException()
    {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(Throwable exception)
    {
        this.exception = exception;
    }

    /**
     * @return the method
     */
    public Method getMethod()
    {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(Method method)
    {
        this.method = method;
    }

    /**
     * @return the parameters
     */
    public Object[] getParameters()
    {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Object[] parameters)
    {
        this.parameters = parameters;
    }

    /**
     * @param callId The callId to set.
     */
    public void setCallId(String callId)
    {
        this.callId = callId;
    }

    /**
     * @return Returns the callId.
     */
    public String getCallId()
    {
        return callId;
    }

    /**
     * @param scriptName The scriptName to set.
     */
    public void setScriptName(String scriptName)
    {
        this.scriptName = scriptName;
    }

    /**
     * @return Returns the scriptName.
     */
    public String getScriptName()
    {
        return scriptName;
    }

    /**
     * @param methodName The methodName to set.
     */
    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    /**
     * @return Returns the methodName.
     */
    public String getMethodName()
    {
        return methodName;
    }

    /**
     * Find the method the best matches the method name and parameters
     */
    public void findMethod(CreatorManager creatorManager, ConverterManager converterManager, InboundContext inboundContext)
    {
        Class<?> type = creatorManager.getCreator(scriptName, true).getType();
        List<Method> validMethods = new ArrayList<Method>();
        for (Method m : type.getMethods())
        {
            if (methodName.equals(m.getName()) && (m.getParameterTypes().length == inboundContext.getParameterCount()))
            {
                validMethods.add(m);
            }
        }

        if (validMethods.isEmpty())
        {
            if (log.isWarnEnabled())
            {
                log.warn("No method called '" + methodName + "' with the correct number of params [" + inboundContext.getParameterCount() + "] found in " + type);
            }

            throw new IllegalArgumentException("Valid method not found. See logs for details");            
        }

        setMethod(validMethods.get(0));

        for (Method m : validMethods)
        {
            if (parameterTypesCorrect(converterManager, inboundContext, m))
            {
                setMethod(m);
            }
        }
    }

    /**
     * @param converterManager
     * @param inctx
     * @param methodOption
     */
    private boolean parameterTypesCorrect(ConverterManager converterManager, InboundContext inctx, Method methodOption)
    {
        for (int i = 0; i < methodOption.getParameterTypes().length; i++)
        {
            Class<?> type = methodOption.getParameterTypes()[i];
            Object inboundParam = getParameters()[i];
            if (inboundParam == null)
            {
                if (!converterManager.isConvertable(type))
                {
                    return false;
                }
            }
            else 
            {
                if (!type.isInstance(inboundParam))
                {
                    return false;
                }
            }
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        try
        {
            return scriptName + "." + methodName + "(...)";
        }
        catch (Exception ex)
        {
            return "Call(undefined)";
        }
    }

    private String callId = null;

    private String scriptName = null;

    private String methodName = null;

    private Method method = null;

    private Object[] parameters = null;

    private Throwable exception = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Call.class);
}
