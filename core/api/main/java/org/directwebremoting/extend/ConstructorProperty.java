package org.directwebremoting.extend;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import org.directwebremoting.ConversionException;
import org.directwebremoting.util.LocalUtil;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConstructorProperty implements Property
{
    /**
     * @param constructor
     * @param parameterName
     * @param parameterNumber
     */
    public ConstructorProperty(Constructor<?> constructor, String parameterName, int parameterNumber)
    {
        this.constructor = constructor;
        this.parameterName = parameterName;
        this.parameterNumber = parameterNumber;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getName()
     */
    public String getName()
    {
        return parameterName;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getPropertyType()
     */
    public Class<?> getPropertyType()
    {
        Type[] types = constructor.getGenericParameterTypes();
        if (parameterNumber >= types.length)
        {
            throw new IllegalArgumentException("parameterNumber=" + parameterNumber + " is too big when method=" + constructor.getName() + " returns genericParameterTypes.length=" + types.length);
        }

        Type parameterType = types[parameterNumber];
        return LocalUtil.toClass(parameterType, toString());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#createChild(int)
     */
    public Property createChild(int nextNewParameterNumber)
    {
        Type[] types = constructor.getGenericParameterTypes();
        if (parameterNumber >= types.length)
        {
            throw new IllegalArgumentException("parameterNumber=" + parameterNumber + " is too big when method=" + constructor.getName() + " returns genericParameterTypes.length=" + types.length);
        }
        Type parameterType = types[parameterNumber];
        return new NestedProperty(this, constructor, parameterType, parameterNumber, nextNewParameterNumber);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) throws ConversionException
    {
        throw new UnsupportedOperationException("Can't get value from constructor parameter");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object bean, Object value) throws ConversionException
    {
        throw new UnsupportedOperationException("Can't set value to constructor parameter");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return constructor.hashCode() + parameterNumber;
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

        ConstructorProperty that = (ConstructorProperty) obj;

        if (!this.constructor.equals(that.constructor))
        {
            return false;
        }

        return this.parameterNumber == that.parameterNumber;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ConstructorProperty[ctor=" + constructor.getName() + ",p#=" + parameterNumber + "]";
    }

    private final Constructor<?> constructor;

    private final String parameterName;

    private final int parameterNumber;
}
