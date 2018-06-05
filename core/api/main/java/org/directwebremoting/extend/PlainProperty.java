package org.directwebremoting.extend;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;

/**
 * An implementation of {@link Property} that simply uses stored values.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PlainProperty implements Property
{
    /**
     * @param name The property name
     * @param value The property value irrespective of the object that we read it on
     */
    public PlainProperty(String name, Object value)
    {
        this.name = name;
        this.value = value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getName()
     */
    public String getName()
    {
        return name;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getPropertyType()
     */
    public Class<?> getPropertyType()
    {
        return value.getClass();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#createChild(int)
     */
    public Property createChild(int newParameterNumber)
    {
        return new NestedProperty(this, null, null, 0, newParameterNumber);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) throws ConversionException
    {
        return value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object bean, Object value) throws ConversionException
    {
        log.warn("Attempt to setValue() on plain property.");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int hash = 141;
        hash += (name == null) ? 2886 : name.hashCode();
        return hash;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass())
        {
            return false;
        }

        PlainProperty that = (PlainProperty) obj;

        return this.name.equals(that.name);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "PlainProperty[name=" + name + "]";
    }

    /**
     * The name of this property
     */
    private final String name;

    /**
     * The property value irrespective of the object that we read it on
     */
    private final Object value;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(PlainProperty.class);
}
