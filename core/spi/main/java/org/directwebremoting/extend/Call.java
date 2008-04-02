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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.jsonp.JsonCallException;
import org.directwebremoting.util.Messages;

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
            throw new IllegalArgumentException(Messages.getString("JsonCallMarshaller.MissingClassParam"));
        }

        if (methodName == null)
        {
            throw new IllegalArgumentException(Messages.getString("JsonCallMarshaller.MissingMethodParam"));
        }

        // Get a list of the available matching methods with the coerced
        // parameters that we will use to call it if we choose to use
        // that method.
        Creator creator = creatorManager.getCreator(scriptName, true);
        List<Method> available = new ArrayList<Method>();

        methods:
        for (Method methodOptions : creator.getType().getMethods())
        {
            // Check method name and access
            if (methodOptions.getName().equals(methodName))
            {
                // Check number of parameters
                if (methodOptions.getParameterTypes().length == inctx.getParameterCount())
                {
                    // Clear the previous conversion attempts (the param types
                    // will probably be different)
                    inctx.clearConverted();

                    // Check parameter types
                    for (int j = 0; j < methodOptions.getParameterTypes().length; j++)
                    {
                        Class<?> paramType = methodOptions.getParameterTypes()[j];
                        if (!converterManager.isConvertable(paramType))
                        {
                            // Give up with this method and try the next
                            continue methods;
                        }
                    }

                    available.add(methodOptions);
                }
            }
        }

        // Pick a method to call
        if (available.size() > 1)
        {
            log.warn("Warning multiple matching methods. Using first match.");
        }

        if (available.isEmpty())
        {
            String name = scriptName + '.' + methodName;
            String error = Messages.getString("JsonCallMarshaller.UnknownMethod", name);
            log.warn("Marshalling exception: " + error);

            throw new JsonCallException("No available method. See logs for more details.");
        }

        // At the moment we are just going to take the first match, for a
        // later increment we might pick the best implementation
        method = available.get(0);
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
