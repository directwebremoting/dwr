package uk.ltd.getahead.dwr.create;

import org.w3c.dom.Element;

import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.Messages;

/**
 * A 'Creator' for static classes.
 * Since reflection uses a null destination object in Method.invoke for static
 * methods, this class just returns null for calls to getInstance().
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class StaticCreator implements Creator
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.create.Creator#init(java.lang.String, org.w3c.dom.Element)
     */
    public void init(Element config) throws IllegalArgumentException
    {
        String classname = config.getAttribute("class"); //$NON-NLS-1$

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
        return null;
    }

    private Class clazz;
}
