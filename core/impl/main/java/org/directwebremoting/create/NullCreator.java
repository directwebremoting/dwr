package org.directwebremoting.create;

import org.directwebremoting.extend.AbstractCreator;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.util.LocalUtil;

/**
 * A creator that simply uses the default constructor each time it is called.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class NullCreator extends AbstractCreator implements Creator
{
    /**
     * What sort of class do we create?
     * @param classname The name of the class
     */
    public void setClass(String classname)
    {
        try
        {
            this.clazz = LocalUtil.classForName(classname);
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
        throw new InstantiationException("The NullCreator was asked to create an object. It should only be used for static methods or with a pre-populated scope.");
    }

    private Class<?> clazz;
}
