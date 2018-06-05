package org.directwebremoting.extend;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.directwebremoting.util.LocalUtil;

/**
 * An immutable value object for method declaration information.
 * @author Mike Wilson [mikewse at g mail dot com]
 */
public class MethodDeclaration
{
    /**
     * Initializes the logical method declaration from a reflected Java method.
     * @param method
     */
    public MethodDeclaration(Method method)
    {
        this(method.getDeclaringClass().getName(), method.getName(), method.getGenericParameterTypes(), method.isVarArgs(), method.getGenericReturnType());
    }

    /**
     * Initializes the logical method declaration from primitive data.
     * @param moduleName
     * @param methodName
     * @param genericParameterTypes
     * @param varArgs
     * @param genericReturnType
     */
    public MethodDeclaration(String moduleName, String methodName, Type[] genericParameterTypes, boolean varArgs, Type genericReturnType)
    {
        this.moduleName = moduleName;
        this.methodName = methodName;
        this.genericParameterTypes = genericParameterTypes;
        this.varArgs = varArgs;
        this.genericReturnType = genericReturnType;

        // Interpolate raw parameter types
        parameterTypes = new Class<?>[genericParameterTypes.length];
        for (int i = 0; i < genericParameterTypes.length; i++) {
            parameterTypes[i] = LocalUtil.toClass(genericParameterTypes[i], toString());
        }

        // Interpolate raw return type
        returnType = LocalUtil.toClass(genericReturnType, toString());
    }

    public String getModuleName()
    {
        return moduleName;
    }

    public String getName()
    {
        return methodName;
    }

    public Class<?>[] getParameterTypes()
    {
        return parameterTypes;
    }

    public Type[] getGenericParameterTypes()
    {
        return genericParameterTypes;
    }

    public boolean isVarArgs()
    {
        return varArgs;
    }

    public Class<?> getReturnType()
    {
        return returnType;
    }

    public Type getGenericReturnType()
    {
        return genericReturnType;
    }

    @Override
    public boolean equals(Object obj)
    {
        MethodDeclaration other = (MethodDeclaration) obj;

        if (moduleName != other.moduleName || methodName != other.methodName || parameterTypes.length != other.parameterTypes.length || returnType != other.returnType)
        {
            return false;
        }

        for (int i = 0; i < parameterTypes.length; i++)
        {
            if (parameterTypes[i] != other.parameterTypes[i])
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return moduleName.hashCode() ^ methodName.hashCode();
    }

    /**
     * Make a nice string for showing what this method is.
     */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append(methodName + "(");
        boolean first = true;
        for(Type p : genericParameterTypes)
        {
            if (!first)
            {
                buf.append(", ");
            }
            buf.append(p.toString());
            first = false;
        }
        buf.append(")");
        return buf.toString();
    }

    private final String moduleName;
    private final String methodName;
    private final Class<?>[] parameterTypes;
    private final Type[] genericParameterTypes;
    private final boolean varArgs;
    private final Class<?> returnType;
    private final Type genericReturnType;
}

