package org.directwebremoting.create;

import org.directwebremoting.extend.AbstractCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.LocalUtil;

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
            return clazz.newInstance();
        }
        catch (IllegalAccessException ex)
        {
            throw new InstantiationException("Illegal Access to default constructor on " + clazz.getName());
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
     * The type of the class that we are creating
     */
    private Class<?> clazz;
}
