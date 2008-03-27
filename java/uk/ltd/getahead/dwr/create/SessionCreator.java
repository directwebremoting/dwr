package uk.ltd.getahead.dwr.create;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.ExecutionContext;
import uk.ltd.getahead.dwr.Messages;

/**
 * A creator that records created objects in the users session.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SessionCreator implements Creator
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.create.Creator#init(org.w3c.dom.Element)
     */
    public void init(Element config) throws IllegalArgumentException
    {
        String classname = config.getAttribute("class"); //$NON-NLS-1$
        scriptname = config.getAttribute("javascript"); //$NON-NLS-1$

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
        Object reply = session.getAttribute(scriptname);

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
            
            session.setAttribute(scriptname, reply);
        }

        return reply;
    }

    /**
     * The class type that we generate by default
     */
    private Class clazz;

    /**
     * The javascript name for the class
     */
    private String scriptname;
}
