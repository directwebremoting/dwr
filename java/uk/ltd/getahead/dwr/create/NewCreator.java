package uk.ltd.getahead.dwr.create;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.impl.AbstractCreator;

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
            this.clazz = Class.forName(classname);
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException(Messages.getString("Creator.ClassNotFound", classname)); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getType()
     */
    public Class getType()
    {
        return clazz;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        try
        {
            return clazz.newInstance();
        }
        catch (IllegalAccessException ex)
        {
            throw new InstantiationException(Messages.getString("Creator.IllegalAccess")); //$NON-NLS-1$
        }
    }

    private Class clazz;
}
