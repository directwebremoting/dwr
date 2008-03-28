package uk.ltd.getahead.dwr.create;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.ExecutionContext;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SessionCreator implements Creator
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.create.Creator#init(org.w3c.dom.Element)
     */
    public void init(Element config) throws IllegalArgumentException
    {
        String classname = config.getAttribute("class");
        scriptname = config.getAttribute("javascript");

        try
        {
            this.clazz = Class.forName(classname);
        }
        catch (ClassNotFoundException ex)
        {
            throw new IllegalArgumentException("Class not found: "+classname);
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
        HttpSession session = ExecutionContext.getExecutionContext().getSession();
        Object reply = session.getAttribute(scriptname);

        if (reply == null)
        {
            try
            {
                reply = clazz.newInstance();
            }
            catch (IllegalAccessException ex)
            {
                throw new InstantiationException("Illegal Access to default constructor");
            }
            
            session.setAttribute(scriptname, reply);
        }

        return reply;
    }

    /**
     * 
     */
    private Class clazz;

    /**
     * 
     */
    private String scriptname;
}
