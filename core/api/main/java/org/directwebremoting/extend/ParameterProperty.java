package org.directwebremoting.extend;

import java.lang.reflect.Type;

import org.directwebremoting.ConversionException;
import org.directwebremoting.util.LocalUtil;

/**
 * An implementation of Property that gets type information from the 'n'th
 * parameter to a method
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ParameterProperty implements Property
{
    public ParameterProperty(MethodDeclaration method, int parameterNumber)
    {
        this.method = method;
        this.parameterNumber = parameterNumber;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getName()
     */
    public String getName()
    {
        return "parameter" + parameterNumber;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getPropertyType()
     */
    public Class<?> getPropertyType()
    {
        Type[] types = method.getGenericParameterTypes();
        if (parameterNumber >= types.length)
        {
            throw new IllegalArgumentException("parameterNumber=" + parameterNumber + " is too big when method=" + method.getName() + " returns genericParameterTypes.length=" + types.length);
        }

        Type parameterType = types[parameterNumber];
        return LocalUtil.toClass(parameterType, toString());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) throws ConversionException
    {
        throw new UnsupportedOperationException("Can't get value from method parameter");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object bean, Object value) throws ConversionException
    {
        throw new UnsupportedOperationException("Can't set value to method parameter");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#createChild(int)
     */
    public Property createChild(int aNewParameterNumber)
    {
        Type[] types = method.getGenericParameterTypes();
        if (parameterNumber >= types.length)
        {
            throw new IllegalArgumentException("parameterNumber=" + parameterNumber + " is too big when method=" + method.getName() + " returns genericParameterTypes.length=" + types.length);
        }
        Type parameterType = types[parameterNumber];
        return new NestedProperty(this, method, parameterType, parameterNumber, aNewParameterNumber);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return method.hashCode() + parameterNumber;
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

        ParameterProperty that = (ParameterProperty) obj;

        if (!this.method.equals(that.method))
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
        return "ParameterProperty[method=" + method.getName() + ",p#=" + parameterNumber + "]";
    }

    private final MethodDeclaration method;

    private final int parameterNumber;
}
