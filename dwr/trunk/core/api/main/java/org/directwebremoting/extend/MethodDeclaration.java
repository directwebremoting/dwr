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
import java.lang.reflect.Type;

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
        this(method.getDeclaringClass().getName(), method.getName(), method.getParameterTypes(), method.getGenericParameterTypes(), method.isVarArgs(), method.getReturnType(), method.getGenericReturnType());
    }

    /**
     * Initializes the logical method declaration from primitive data.
     * @param moduleName
     * @param methodName
     * @param parameterTypes
     * @param genericParameterTypes
     * @param varArgs
     * @param returnType
     * @param genericReturnType
     */
    public MethodDeclaration(String moduleName, String methodName, Class<?>[] parameterTypes, Type[] genericParameterTypes, boolean varArgs, Class<?> returnType, Type genericReturnType)
    {
        this.moduleName = moduleName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.genericParameterTypes = genericParameterTypes;
        this.varArgs = varArgs;
        this.returnType = returnType;
        this.genericReturnType = genericReturnType;
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
        for(Class<?> p : parameterTypes)
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

