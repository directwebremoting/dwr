/*
 * Copyright 2007 Ahmed Hashim
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

import java.lang.reflect.Method;

import org.directwebremoting.extend.AbstractCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.LocalUtil;

/**
 * A {@link Creator} that uses an instance method to create singletons.
 * <p>By default this creator uses a static method with the signature:
 * <code>SomeClass.getInstance()</code> to create new instances. The name of
 * the singleton constructor method can be customized using the
 * <code>getInstance</code> parameter.
 * @author Ahmed Hashim [hashim at egjug dot org]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SingletonCreator extends AbstractCreator implements Creator
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
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException("Class not found: " + classname, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        try
        {
            Method method = clazz.getMethod(factoryMethod);
            return method.invoke(new SingletonCreator());
        }
        catch (Exception ex)
        {
            throw new InstantiationException(ex.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Creator#getType()
     */
    public Class<?> getType()
    {
        return clazz;
    }
    
    /**
     * @return the factoryMethod function name
     * */
    public String getFactoryMethod()
    {
        return factoryMethod;
    }
    
    /**
     * @param functionToCall the name of the factory function. 
     * */
    public void setFactoryMethod(String functionToCall)
    {
        this.factoryMethod = functionToCall;
    }

    /**
     * The function which will return an instance from the object, the common 
     * function name used in singleton class is 'getInstance', this will be the
     * default value. 
     * */
    private String factoryMethod = "getInstance";

    /**
     * The type of the object that we create
     */
    private Class<?> clazz;
}
