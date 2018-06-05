package org.directwebremoting.extend;

import java.util.Map;

import org.directwebremoting.ConversionException;

/**
 * Additions to Converter that allow objects to have names that are exposed to
 * the browser
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface NamedConverter extends Converter
{
    /**
     * Get a map of property names to implementations of {@link Property}.
     * <p>HibernateBeanConverter (and maybe others) may want to provide
     * alternate versions of bean.getClass(), and we may wish to fake or hide
     * properties in some cases.
     * <p>This implementation is preferred above the alternate:
     * {@link #getPropertyMapFromClass(Class, boolean, boolean)} because it
     * potentially retains important extra type information.
     * @param example The object to find bean info from
     * @param readRequired The properties returned must be readable
     * @param writeRequired The properties returned must be writable
     * @return An array of PropertyDescriptors describing the beans properties
     * @see #getPropertyMapFromClass(Class, boolean, boolean)
     * @throws ConversionException If the introspection fails
     */
    Map<String, Property> getPropertyMapFromObject(Object example, boolean readRequired, boolean writeRequired) throws ConversionException;

    /**
     * Get a map of property names to implementations of {@link Property}.
     * <p>HibernateBeanConverter (and maybe others) may want to provide
     * alternate versions of bean.getClass(), and we may wish to fake or hide
     * properties in some cases.
     * <p>If you have a real object to investigate then it is probably better
     * to call {@link #getPropertyMapFromObject(Object, boolean, boolean)}
     * because that version can take into account extra runtime type info.
     * @param type The class to find bean info from
     * @param readRequired The properties returned must be readable
     * @param writeRequired The properties returned must be writable
     * @return An array of PropertyDescriptors describing the beans properties
     * @see #getPropertyMapFromObject(Object, boolean, boolean)
     * @throws ConversionException If the introspection fails
     */
    Map<String, Property> getPropertyMapFromClass(Class<?> type, boolean readRequired, boolean writeRequired) throws ConversionException;

    /**
     * @return Returns the instanceType.
     */
    Class<?> getInstanceType();

    /**
     * @param instanceType The instanceType to set.
     */
    void setInstanceType(Class<?> instanceType);

    /**
     * Accessor for the javascript class name for mapped converted objects.
     * @return The Javascript classname
     */
    String getJavascript();

    /**
     * Accessor for the javascript class name for mapped converted objects.
     * @param javascript The Javascript classname
     */
    void setJavascript(String javascript);

    /**
     * Accessor for the javascript class name that will appear as superclass
     * for mapped converted objects.
     * @return The Javascript classname
     */
    String getJavascriptSuperClass();

    /**
     * Setter for the javascript class name that will appear as superclass
     * for mapped converted objects.
     * @param javascriptSuperClass The Javascript classname
     */
    void setJavascriptSuperClass(String javascriptSuperClass);
}
