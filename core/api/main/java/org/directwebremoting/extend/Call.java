package org.directwebremoting.extend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.util.LocalUtil;

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
        this.methodDeclaration = null;
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
    public MethodDeclaration getMethodDeclaration()
    {
        return methodDeclaration;
    }

    /**
     * @param methodDeclaration the method to set
     */
    public void setMethodDeclaration(MethodDeclaration methodDeclaration)
    {
        this.methodDeclaration = methodDeclaration;
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
    public void findMethod(ModuleManager moduleManager, ConverterManager converterManager, InboundContext inctx, int callNum)
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
        Module module = moduleManager.getModule(scriptName, true);
        List<MethodDeclaration> allMethods = new ArrayList<MethodDeclaration>();
        allMethods.addAll(Arrays.asList(module.getMethods()));

        // Remove all methods that don't have a matching name
        for (Iterator<MethodDeclaration> it = allMethods.iterator(); it.hasNext();)
        {
            if (!it.next().getName().equals(methodName))
            {
                it.remove();
            }
        }

        if (allMethods.isEmpty())
        {
            // Not even a name match
            log.warn("No method called '" + methodName + "' found in " + module.toString());
            throw new IllegalArgumentException("Method name not found. See logs for details");
        }

        // Remove all the methods where we can't convert the parameters
        allMethodsLoop:
        for (Iterator<MethodDeclaration> it = allMethods.iterator(); it.hasNext();)
        {
            MethodDeclaration m = it.next();
            Class<?>[] methodParamTypes = m.getParameterTypes();

            // Remove non-varargs methods which declare less params than were passed
            if (!m.isVarArgs() && methodParamTypes.length < inputArgCount)
            {
                it.remove();
                continue allMethodsLoop;
            }

            // Remove methods where we can't convert the input
            int argIndex = 0;
            for (int paramIndex = 0; paramIndex < methodParamTypes.length; paramIndex++)
            {
                Class<?> methodParamType = methodParamTypes[paramIndex];
                if (LocalUtil.isServletClass(methodParamType))
                {
                    continue;
                }

                InboundVariable param = inctx.getParameter(callNum, argIndex);
                Class<?> inputType = converterManager.getClientDeclaredType(param);

                // If we can't convert this parameter type, ignore the method
                if (inputType == null && !converterManager.isConvertable(methodParamType))
                {
                    it.remove();
                    continue allMethodsLoop;
                }

                // Remove methods which declare more non-nullable parameters than were passed
                if (inputArgCount <= argIndex && methodParamType.isPrimitive())
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

                argIndex++;
            }
        }

        // Added to increase our ability to call overloaded methods accurately!
        // Is the inbound JavaScript type assignable to methodParamType?
        // We are limited to what JavaScript gives us (number, date, boolean, etc.)
        // We only want to performn this if there are multiple methods with the same name (hack for now).
        if (allMethods.size() > 1) {
            allMethodsLoop2:
            for (Iterator<MethodDeclaration> it = allMethods.iterator(); it.hasNext();)
            {
                MethodDeclaration m = it.next();
                Class<?>[] methodParamTypes = m.getParameterTypes();

                // Remove methods where we can't convert the input
                for (int i = 0; i < methodParamTypes.length; i++)
                {
                    Class<?> methodParamType = methodParamTypes[i];
                    InboundVariable param = inctx.getParameter(callNum, i);
                    String javaScriptType = param.getType();
                    // If this method takes a vararg, the JavaScript type being passed is not
                    // an array, and this is the var argument we need to use the component type of the argument.
                    // Otherwise this method will be removed because javaScriptType (not array) and methodParamType
                    // (Array - this is the vararg) won't match.
                    if (m.isVarArgs() && !"array".equals(javaScriptType) && i == methodParamTypes.length - 1)
                    {
                        methodParamType=methodParamType.getComponentType();
                    }
                    if (!LocalUtil.isJavaScriptTypeAssignableTo(javaScriptType, methodParamType))
                    {
                        it.remove();
                        continue allMethodsLoop2;
                    }
                }
            }
        }

        if (allMethods.isEmpty())
        {
            // Not even a name match
            log.warn("No methods called '" + methodName + "' in " + module.toString() + " are applicable for the passed parameters.");
            throw new IllegalArgumentException("Method not found. See logs for details");
        }
        else if (allMethods.size() == 1)
        {
            methodDeclaration = allMethods.get(0);
            return;
        }

        // If we have methods that exactly match the param count we use a
        // different matching algorithm, to when we don't
        List<MethodDeclaration> exactParamCountMatches = new ArrayList<MethodDeclaration>();
        for (MethodDeclaration m : allMethods)
        {
            if (!m.isVarArgs() && m.getParameterTypes().length == inputArgCount)
            {
                exactParamCountMatches.add(m);
            }
        }

        if (exactParamCountMatches.size() == 1)
        {
            // One method with the right number of params - use that
            methodDeclaration = exactParamCountMatches.get(0);
            return;
        }
        else if (exactParamCountMatches.size() == 2)
        {
            // Two methods with same name, the correct number of parameters and convertible inputs.
            // Hope it's due to generics (DWR-343). Still check
            int choose = -1;
            boolean compatible = true;
            Class<?>[] params1 = exactParamCountMatches.get(0).getParameterTypes();
            Class<?>[] params2 = exactParamCountMatches.get(1).getParameterTypes();
            for (int i = 0; i < params1.length; i++)
            {
                if (compatible && !params1[i].equals(params2[i]))
                {
                    if (choose < 0)
                    {
                        choose = params1[i].isAssignableFrom(params2[i]) ? 1 : 0;
                    }
                    compatible &= ((choose == 1) || params2[i].isAssignableFrom(params1[i]));
                }
            }
            if (compatible && (choose >= 0))
            {
                methodDeclaration = exactParamCountMatches.get(choose); // Select the most concrete of the two
                return;
            }
        }

        // Lots of methods with the right name, but none with the right
        // parameter count. If we have exactly one varargs method, then we
        // try that, otherwise we bail.
        List<MethodDeclaration> varargsMethods = new ArrayList<MethodDeclaration>();
        for (MethodDeclaration m : allMethods)
        {
            if (m.isVarArgs())
            {
                varargsMethods.add(m);
            }
        }

        if (varargsMethods.size() == 1)
        {
            methodDeclaration = varargsMethods.get(0);
            return;
        }

        log.warn("Can't find single method to match method '" + methodName + "' in " + module.toString());
        log.warn("- DWR does not continue where there is ambiguity about which method to execute.");
        log.warn("- Input parameters: " + inputArgCount + ".Matching methods with param count match: " + exactParamCountMatches.size() + ". Number of matching varargs methods: " + varargsMethods.size());
        log.warn("- Potential matches include:");
        for (MethodDeclaration m : allMethods)
        {
            log.warn("  - " + m.toString());
        }

        throw new IllegalArgumentException("Method not found. See logs for details");
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

    private final String callId;

    private final String scriptName;

    private final String methodName;

    private MethodDeclaration methodDeclaration = null;

    private Object[] parameters = null;

    private Throwable exception = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Call.class);

}
