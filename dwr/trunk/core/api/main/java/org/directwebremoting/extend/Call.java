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
    public void findMethod(CreatorManager creatorManager, ConverterManager converterManager, InboundContext inctx)
    {
        if (scriptName == null)
        {
            throw new IllegalArgumentException("Missing class parameter");
        }

        if (methodName == null)
        {
            throw new IllegalArgumentException("Missing method parameter");
        }

        // Get a list of the available matching methods with the coerced
        // parameters that we will use to call it if we choose to use
        // that method.
        Creator creator = creatorManager.getCreator(scriptName, true);
        List<Method> allMethods = new ArrayList<Method>();
        allMethods.addAll(Arrays.asList(creator.getType().getMethods()));

        // Remove all methods that don't have a matching name
        for (Iterator<Method> it = allMethods.iterator(); it.hasNext();)
        {
            if (!it.next().getName().equals(methodName))
            {
                it.remove();
            }
        }

        if (allMethods.size() == 0)
        {
            // Not even a name match
            log.warn("No method called '" + methodName + "' found in " + creator.getType());
            throw new IllegalArgumentException("Method name not found. See logs for details");            
        }

        if (allMethods.size() == 1)
        {
            // Check the params of the single name match
            Method methodOption = allMethods.get(0);
            if (!parameterTypesCorrect(converterManager, inctx, methodOption))
            {
                throw new IllegalArgumentException("Parameter not convertible. See logs for details");
            }
            method = methodOption;
        }
        else
        {
            // First weed out the methods with the wrong number of parameters
            for (Iterator<Method> it = allMethods.iterator(); it.hasNext();)
            {
                // Check number of parameters
                Method methodOption = it.next();
                if (methodOption.getParameterTypes().length != inctx.getParameterCount())
                {
                    it.remove();
                }
            }

            if (allMethods.size() == 0)
            {
                // None have the right number of parameters
                log.warn("Multiple methods called '" + methodName + "' found. But none had " + inctx.getParameterCount() + " parameters.");
                throw new IllegalArgumentException("Method not found. See logs for details");
            }

            if (allMethods.size() == 1)
            {
                // Only one match, check and use if we can
                Method methodOption = allMethods.get(0);
                if (!parameterTypesCorrect(converterManager, inctx, methodOption))
                {
                    throw new IllegalArgumentException("Parameter not convertible. See logs for details");
                }

                method = methodOption;
            }
            else
            {
                // Many available weed out the ones with params that don't match
                for (Iterator<Method> it = allMethods.iterator(); it.hasNext();)
                {
                    Method methodOption = it.next();
                    if (!parameterTypesCorrect(converterManager, inctx, methodOption))
                    {
                        it.remove();
                    }
                }

                if (allMethods.size() == 0)
                {
                    // None have the right parameters
                    log.warn("Multiple methods called '" + methodName + "' found. But none had all parameters convertible.");
                    throw new IllegalArgumentException("Method not found. See logs for details");
                }

                if (allMethods.size() > 1)
                {
                    // Randomly use the first, but warn
                    log.warn("Warning multiple matching methods. Using first match.");
                }

                method = allMethods.get(0);                
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
        // Only one option - we're checking not selecting
        // Check parameter types
        for (int i = 0; i < methodOption.getParameterTypes().length; i++)
        {
            Class<?> paramType = methodOption.getParameterTypes()[i];
            if (!converterManager.isConvertable(paramType))
            {
                log.warn(scriptName + '.' + methodName + "(), parameter #" + i + " is not convertable.");
                log.warn("You might need to add a <convert converter='bean' match='" + paramType.getCanonicalName() + "'/> to dwr.xml");
                log.warn("See: http://getahead.org/dwr/server/dwrxml/converters/bean for more info.");
                return false;
            }

            if (i > inctx.getParameterCount() && paramType.isPrimitive())
            {
                log.warn(scriptName + '.' + methodName + "(), parameter #" + i + " is not primitive, and no data supplied.");
                return false;
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
