package uk.ltd.getahead.dwr.create;

import uk.ltd.getahead.dwr.Creator;

/**
 * A 'Creator' for static classes.
 * Since reflection uses a null destination object in Method.invoke for static
 * methods, this class just returns null for calls to getInstance().
 * @deprecated Use the NewCreator in place of this one
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class StaticCreator extends NewCreator implements Creator
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Creator#getInstance()
     */
    public Object getInstance() throws InstantiationException
    {
        return null;
    }
}
