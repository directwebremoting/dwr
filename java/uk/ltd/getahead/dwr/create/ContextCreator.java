package uk.ltd.getahead.dwr.create;

import java.util.Map;

import javax.servlet.ServletContext;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.ExecutionContext;
import uk.ltd.getahead.dwr.Messages;

/**
 * A creator that records created objects in the application-context.
 * @author Frank Nestel
 */
public class ContextCreator implements Creator
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#init(java.util.Map)
     */
    public void init(Map params) throws IllegalArgumentException
    {
        String classname = (String) params.get("class"); //$NON-NLS-1$
        scriptname = (String) params.get("javascript"); //$NON-NLS-1$

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
        ServletContext context = ExecutionContext.get().getServletContext();
        Object reply = context.getAttribute(scriptname);

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
            
            context.setAttribute(scriptname, reply);
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
