package uk.ltd.getahead.dwr.create;

import org.w3c.dom.Element;

import uk.ltd.getahead.dwr.Creator;

/**
 * A creator that simply uses the default constructor each time it is called.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class NewCreator implements Creator
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.create.Creator#init(java.lang.String, org.w3c.dom.Element)
     */
    public void init(Element config) throws IllegalArgumentException
    {
        String classname = config.getAttribute("class");

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
        try
        {
            return clazz.newInstance();
        }
        catch (IllegalAccessException ex)
        {
            throw new InstantiationException("Illegal Access to default constructor");
        }
    }

    private Class clazz;
}
