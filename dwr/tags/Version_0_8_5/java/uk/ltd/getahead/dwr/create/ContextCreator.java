package uk.ltd.getahead.dwr.create;

import uk.ltd.getahead.dwr.Creator;

/**
 * A creator that records created objects in the application-context.
 * @deprecated Use the new creator with a scope="application" attribute
 * @author Frank Nestel
 */
public class ContextCreator extends NewCreator implements Creator
{
    /**
     * Setup the default scope
     */
    public ContextCreator()
    {
        setScope(APPLICATION);
    }
}
