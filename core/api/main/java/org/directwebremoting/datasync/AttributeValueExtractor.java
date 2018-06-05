package org.directwebremoting.datasync;

/**
 * There are many ways to get a property from things, the most obvious would
 * be reflection or introspection or using {@link java.util.Map#get}. This
 * interface allows us to abstract the options.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface AttributeValueExtractor
{
    /**
     * Get an attribute/<code>property</code> from a <code>bean</code>.
     * @param bean The object to read an attribute/property from.
     * @param property The attribute/property to read.
     * @return The value of the returned object or null if it could not be
     * found.
     */
    public Object getValue(Object bean, String property);
}
