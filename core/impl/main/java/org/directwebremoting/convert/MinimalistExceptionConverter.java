package org.directwebremoting.convert;

import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.PlainProperty;
import org.directwebremoting.extend.Property;

/**
 * A special case of BeanConverter for use by default with {@link Throwable}s,
 * which only outputs a "broken" message.
 * <p>In other words, by default the user doesn't get to learn anything about
 * failures on the server.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class MinimalistExceptionConverter extends BeanConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BasicBeanConverter#getPropertyDescriptors(java.lang.Class, boolean, boolean)
     */
    @Override
    public Map<String, Property> getPropertyMapFromClass(Class<?> type, boolean readRequired, boolean writeRequired) throws ConversionException
    {
        Map<String, Property> descriptors = new HashMap<String, Property>();

        descriptors.put("message", new PlainProperty("message", "Error"));
        descriptors.put("javaClassName", new PlainProperty("javaClassName", "java.lang.Throwable"));

        return descriptors;
    }
}
