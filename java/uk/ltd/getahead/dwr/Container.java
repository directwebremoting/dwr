package uk.ltd.getahead.dwr;

/**
 * A very basic IoC container
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Container
{
    /**
     * Get an instance of a bean of a given type.
     * @param id The type to get an instance of
     * @return The object of the given type
     */
    Object getBean(String id);
}

