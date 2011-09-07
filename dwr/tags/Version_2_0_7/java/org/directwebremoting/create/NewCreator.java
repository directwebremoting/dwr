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

import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

/**
 * A creator that simply uses the default constructor each time it is called.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class NewCreator extends AbstractCreator implements Creator
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
        catch (ExceptionInInitializerError ex)
        {
            log.warn("Class load error", ex);
            throw new IllegalArgumentException(Messages.getString("Creator.ClassLoadError", classname));
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException(Messages.getString("Creator.ClassNotFound", classname));
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Creator#getType()
     */
    public Class getType()
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
            return clazz.newInstance();
        }
        catch (IllegalAccessException ex)
        {
            // JDK5: We should really be passing the exception on
            throw new InstantiationException(Messages.getString("Creator.IllegalAccess"));
        }
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
     * The log stream
     */
    private static final Logger log = Logger.getLogger(NewCreator.class);

    /**
     * The type of the class that we are creating
     */
    private Class clazz;
}
