package uk.ltd.getahead.dwr;

import org.w3c.dom.Element;

/**
 * A base class for all AllowedClasses
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Creator
{
    /**
     * Configuration is done via access to the DOM Element.
     * This is not at all ideal, but it will do for the moment.
     * @param config The configuration element
     * @throws IllegalArgumentException If the config data in the Element is invalid
     */
    public void init(Element config) throws IllegalArgumentException;

    /**
     * Accessor for the <code>java.lang.Class</code> that this Creator
     * allows access to.
     * @return The type of this allowed class
     */
    public Class getType();

    /**
     * Accessor for the/an instance of this Creator.
     * @return the instance to use
     * @throws InstantiationException If for some reason the object can not be created
     */
    public Object getInstance() throws InstantiationException;
}
