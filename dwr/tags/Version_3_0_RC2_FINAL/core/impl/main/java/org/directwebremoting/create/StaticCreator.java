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
package org.directwebremoting.create;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.directwebremoting.extend.AbstractCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.LocalUtil;

/**
 * A creator that attempts to grab an instance of a static/singleton class.
 * @author David Marginian [david at butterdev dot com]
 */
public class StaticCreator extends AbstractCreator implements Creator
{
    /**
     * What sort of class do we create?
     * @param classname The name of the class
     */
    public void setClass(String classname)
    {
        try
        {
            clazz = LocalUtil.classForName(classname);
            if (getJavascript() == null)
            {
                setJavascript(clazz.getSimpleName());
            }
        }
        catch (ExceptionInInitializerError ex)
        {
            throw new IllegalArgumentException("Error loading class: " + classname, ex);
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException("Class not found: " + classname, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getType()
     */
    public Class<?> getType()
    {
        return clazz;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        try
        {
            Method getInstanceMethod = clazz.getDeclaredMethod(instanceMethodName, new Class[] {});
            return getInstanceMethod.invoke(null, new Object[] {});
        }
        catch (IllegalAccessException ex)
        {
            throw new InstantiationException("Illegal Access to default constructor on " + clazz.getName());
        }
        catch (NoSuchMethodException ex)
        {
            throw new InstantiationException("The getInstance method specified " + instanceMethodName + " does not exist for " + clazz.getName());
        }
        catch (InvocationTargetException ex)
        {
            throw new InstantiationException("An exception occurred while calling the getInstanceMethod " + instanceMethodName + " on " + clazz.getName());
        }
    }

    /**
     * Retrieves name of the method that will be called to retrieve the static instance of this class.
     * @return the getInstanceMethodName
     */
    public String getInstanceMethodName()
    {
        return instanceMethodName;
    }

    /**
     * Sets the name of the method that will be called to retrieve the static instance of this class.
     * @param the new getInstanceMethodName
     */
    public void setInstanceMethodName(String instanceMethodName)
    {
        this.instanceMethodName = instanceMethodName;
    }

    /**
     * Sets the class name to create.
     * @param className The name of the class to create
     */
    public void setClassName(String className)
    {
        setClass(className);
    }

    /**
     * Gets the name of the class to create.
     * @return The name of the class to create
     */
    public String getClassName()
    {
        return getType().getName();
    }

    /**
     * The type of the class that we are creating
     */
    private Class<?> clazz;

    /**
     * The name of the method that will be called to retrieve the static instance of this class.
     */
    private String instanceMethodName = "getInstance";
}
