package org.directwebremoting.datasync;

import org.directwebremoting.util.LocalUtil;

/**
 * An {@link AttributeValueExtractor} that simply calls {@link LocalUtil#getProperty}
 * to extract attribute values.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PojoAttributeValueExtractor implements AttributeValueExtractor
{
    /* (non-Javadoc)
     * @see org.directwebremoting.datasync.AttributeValueExtractor#getValue(java.lang.Object, java.lang.String)
     */
    public Object getValue(Object bean, String property)
    {
        return LocalUtil.getProperty(bean, property, Object.class);
    }
}
