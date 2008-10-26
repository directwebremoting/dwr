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
     * Find the method the best matches the method name and parameters.
     * <p>
     * This method used to be significantly more detailed in its matching
     * sequences, this simpler version is less defined in the order in which it
     * matches methods, but able to find more matches. If we discover that this
     * version creates problems, the old version was around up to revision 2317.
     */
    public void findMethod(CreatorManager creatorManager, ConverterManager converterManager, InboundContext inctx, int callNum)
    {
        if (scriptName == null)
        {
            throw new IllegalArgumentException("Missing class parameter");
        }

        if (methodName == null)
        {
            throw new IllegalArgumentException("Missing method parameter");
        }

        int inputArgCount = inctx.getParameterCount(callNum);

        // Get a mutable list of all methods on the type specified by the creator
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

        if (allMethods.isEmpty())
        {
            // Not even a name match
            log.warn("No method called '" + methodName + "' found in " + creator.getType());
            throw new IllegalArgumentException("Method name not found. See logs for details");
        }

        // Remove all the methods where we can't convert the parameters
        allMethodsLoop:
        for (Iterator<Method> it = allMethods.iterator(); it.hasNext();)
        {
            Method m = it.next();
            Class<?>[] methodParamTypes = m.getParameterTypes();

            // Remove non-varargs methods which declare less params than were passed
            if (!m.isVarArgs() && methodParamTypes.length < inputArgCount)
            {
                it.remove();
                continue allMethodsLoop;
            }

            // Remove methods where we can't convert the input
            for (int i = 0; i < methodParamTypes.length; i++)
            {
                Class<?> methodParamType = methodParamTypes[i];
                InboundVariable param = inctx.getParameter(callNum, i);
                Class<?> inputType = converterManager.getClientDeclaredType(param);

                // If we can't convert this parameter type, ignore the method
                if (inputType == null && !converterManager.isConvertable(methodParamType))
                {
                    it.remove();
                    continue allMethodsLoop;
                }

                // Remove methods which declare more non-nullable parameters than were passed
                if (inputArgCount <= i && methodParamType.isPrimitive())
                {
                    it.remove();
                    continue allMethodsLoop;
                }

                // Remove methods where the client passed a type and we can't use it.
                if (inputType != null && !methodParamType.isAssignableFrom(inputType))
                {
                    it.remove();
                    continue allMethodsLoop;
                }
            }
        }

        if (allMethods.isEmpty())
        {
            // Not even a name match
            log.warn("No methods called " + creator.getType() + "." + methodName + "' are applicable for the passed parameters.");
            throw new IllegalArgumentException("Method not found. See logs for details");
        }
        else if (allMethods.size() == 1)
        {
            setMethod(allMethods.get(0));
            return;
        }

        // If we have methods that exactly match the param count we use a
        // different matching algorithm, to when we don't
        List<Method> exactParamCountMatches = new ArrayList<Method>();
        for (Method m : allMethods)
        {
            if (!m.isVarArgs() && m.getParameterTypes().length == inputArgCount)
            {
                exactParamCountMatches.add(m);
            }
        }

        if (exactParamCountMatches.size() == 1)
        {
            // One method with the right number of params - use that
            setMethod(exactParamCountMatches.get(0));
        }
        else
        {
            // Lots of methods with the right name, but none with the right
            // parameter count. If we have exactly one varargs method, then we
            // use that, otherwise we bail.
            List<Method> varargsMathods = new ArrayList<Method>();
            for (Method m : allMethods)
            {
                if (m.isVarArgs())
                {
                    varargsMathods.add(m);
                }
            }

            if (varargsMathods.size() == 1)
            {
                setMethod(varargsMathods.get(0));
            }
            else
            {
                log.warn("Can't find method to match " + creator.getType() + "." + methodName);
                log.warn("- DWR does not execute where there is ambiguity. Matching methods with param count match: " + exactParamCountMatches.size() + ". Number of matching varargs methods: " + varargsMathods.size());
                throw new IllegalArgumentException("Method not found. See logs for details");
            }
        }


        if (allMethods.size() > 1)
        {
            log.warn("Warning: Multiple potential method matches. The selection process is undefined. Future versions of DWR (or your class) may select a different method. Potential matches include:");
            for (Method m : allMethods)
            {
                log.warn("- " + m.toGenericString());
            }
        }

        setMethod(allMethods.get(0));
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
