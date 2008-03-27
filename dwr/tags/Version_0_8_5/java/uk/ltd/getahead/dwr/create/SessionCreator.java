package uk.ltd.getahead.dwr.create;

import javax.servlet.http.HttpSession;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.ExecutionContext;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.impl.AbstractCreator;

/**
 * A creator that records created objects in the users session.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SessionCreator extends AbstractCreator implements Creator
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
        HttpSession session = ExecutionContext.get().getSession();
        Object reply = session.getAttribute(getJavascript());

        if (reply == null)
        {
            try
            {
                reply = clazz.newInstance();
            }
            catch (IllegalAccessException ex)
            {
                throw new InstantiationException(Messages.getString("Creator.IllegalAccess")); //$NON-NLS-1$
            }
            
            session.setAttribute(getJavascript(), reply);
        }

        return reply;
    }

    /**
     * The class type that we generate by default
     */
    private Class clazz;
}
