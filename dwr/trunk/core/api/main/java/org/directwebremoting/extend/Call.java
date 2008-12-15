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
import java.lang.reflect.Proxy;
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
    public Call(String callId, String scriptName, String methodName)
    {
        this.callId = callId;
        this.scriptName = scriptName;
        this.methodName = methodName;
    }

    public void setMarshallFailure(Throwable exception)
    {
        this.exception = exception;
        this.method = null;
        this.parameters = null;
    }

    /**
     * @return the exception
     */
    public Throwable getException()
    {
        return exception;
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
     * @return Returns the callId.
     */
    public String getCallId()
    {
        return callId;
    }

    /**
     * @return Returns the scriptName.
     */
    public String getScriptName()
    {
        return scriptName;
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
            method = allMethods.get(0);
            checkProxiedMethod(creatorManager);
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
            method = exactParamCountMatches.get(0);
            checkProxiedMethod(creatorManager);
            return;
        }

        // Lots of methods with the right name, but none with the right
        // parameter count. If we have exactly one varargs method, then we
        // try that, otherwise we bail.
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
            method = varargsMathods.get(0);
            checkProxiedMethod(creatorManager);
            return;
        }

        log.warn("Can't find single method to match " + creator.getType() + "." + methodName);
        log.warn("- DWR does not continue where there is ambiguity about which method to execute.");
        log.warn("- Input parameters: " + inputArgCount + ".Matching methods with param count match: " + exactParamCountMatches.size() + ". Number of matching varargs methods: " + varargsMathods.size());
        log.warn("- Potential matches include:");
        for (Method m : allMethods)
        {
            log.warn("  - " + m.toGenericString());
        }

        throw new IllegalArgumentException("Method not found. See logs for details");
    }

    /**
     * The main issue here happens with JDK proxies, that is, those based on
     * interfaces and is easily noticeable with Spring because it's designed to
     * generate proxies on the fly for many different purposes (security, tx,..)
     * For some unknown reasons but probably related to erasure, when a proxy is
     * created and it contains a method with at least one generic parameter,
     * that generic type information is lost. Those that rely on reflection to
     * detect that info at runtime (our case when detecting the matching method
     * for an incoming call) face a dead end. The solution involves detecting
     * the proxy interface and obtain the original class (which holds the
     * required information).
     * <p>Here comes the problematic area. In the case of Spring all proxies
     * implement the Advised interface which includes a method that returns the
     * target class (and so fulfills our need). Of course, this means that:
     * a) A Spring dependency appears and
     * b) The solution only applies to Spring contexts.
     * The first concern is solvable using Class.forName. The current fix does
     * not solve the second. Probably a better solution should be implemented
     * (for example, something that works under the AOP alliance umbrella).
     */
    private void checkProxiedMethod(CreatorManager creatorManager)
    {
        try
        {
            if (method != null && advisedClass != null && advisedClass.isAssignableFrom(method.getDeclaringClass()))
            {
                Creator creator = creatorManager.getCreator(scriptName, true);
                if (Proxy.isProxyClass(creator.getType()))
                {
                    Object target = creator.getInstance(); // Should be a singleton
                    Method targetClassMethod = target.getClass().getMethod("getTargetClass");
                    Class<?> targetClass = (Class<?>) targetClassMethod.invoke(target);
                    method = targetClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                }
            }
        }
        catch (Exception ex)
        {
            // Probably not in Spring context so no Advised proxies at all
        }
    }

    /**
     * Spring/AOP hack
     * @see #checkProxiedMethod(CreatorManager)
     */
    static
    {
        try
        {
            advisedClass = Class.forName("org.springframework.aop.framework.Advised");
        }
        catch (ClassNotFoundException ex)
        {
        }
    }

    /**
     * Spring/AOP hack
     * @see #checkProxiedMethod(CreatorManager)
     */
    private static Class<?> advisedClass;

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

    private final String callId;

    private final String scriptName;

    private final String methodName;

    private Method method = null;

    private Object[] parameters = null;

    private Throwable exception = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Call.class);
}
