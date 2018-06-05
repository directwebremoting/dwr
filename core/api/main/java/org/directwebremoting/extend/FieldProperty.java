package org.directwebremoting.extend;

import java.lang.reflect.Field;

import org.directwebremoting.ConversionException;

/**
 * An implementation of {@link Property} that proxies to a {@link Field}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FieldProperty implements Property
{
    /**
     * @param field The Field that we are proxying to
     */
    public FieldProperty(Field field)
    {
        this.field = field;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getName()
     */
    public String getName()
    {
        return field.getName();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getPropertyType()
     */
    public Class<?> getPropertyType()
    {
        return field.getType();
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
        try
        {
            return field.get(bean);
        }
        catch (Exception ex)
        {
            throw new ConversionException(bean.getClass(), ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object bean, Object value) throws ConversionException
    {
        try
        {
            field.set(bean, value);
        }
        catch (Exception ex)
        {
            throw new ConversionException(bean.getClass(), ex);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return field.hashCode();
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

        FieldProperty that = (FieldProperty) obj;

        return this.field.equals(that.field);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "FieldProperty[field=" + field.getName() + "]";
    }

    /**
     * The Field that we are proxying to
     */
    private final Field field;
}
