package org.directwebremoting.extend;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.directwebremoting.ConversionException;
import org.directwebremoting.util.LocalUtil;

/**
 * An implementation of {@link Property} that proxies to a {@link PropertyDescriptor}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PropertyDescriptorProperty implements Property
{
    /**
     * @param descriptor The PropertyDescriptor that we are proxying to
     */
    public PropertyDescriptorProperty(PropertyDescriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getName()
     */
    public String getName()
    {
        return descriptor.getName();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getPropertyType()
     */
    public Class<?> getPropertyType()
    {
        Method readMethod = descriptor.getReadMethod();
        Type[] types = (null != readMethod) ? readMethod.getGenericParameterTypes() : null;
        if (null == types || types.length == 0)
        {
            return descriptor.getPropertyType();
        }
        Type parameterType = types[0];
        return LocalUtil.toClass(parameterType, toString());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#getValue(java.lang.Object)
     */
    public Object getValue(Object bean) throws ConversionException
    {
        try
        {
            return descriptor.getReadMethod().invoke(bean);
        }
        catch (InvocationTargetException ex)
        {
            throw new ConversionException(bean.getClass(), ex.getTargetException());
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
            Method setter = LocalUtil.getWriteMethod(bean.getClass(), descriptor);
            setter.invoke(bean, value);
        }
        catch (InvocationTargetException ex)
        {
            throw new ConversionException(bean.getClass(), ex.getTargetException());
        }
        catch (Exception ex)
        {
            throw new ConversionException(bean.getClass(), ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Property#createChild(int)
     */
    public Property createChild(int newParameterNumber) throws ConversionException
    {
        Method method = descriptor.getReadMethod();
        if (method == null)
        {
            throw new ConversionException(descriptor.getPropertyType(), "Property \"" + descriptor.getDisplayName() + "\" of type " + descriptor.getPropertyType().getName() + " has no read method (getter).");
        }
        // Type[] types = method.getGenericParameterTypes();
        // if (types.length == 0)
        // {
        //     return new NestedProperty(this, method, null, 0, newParameterNumber);
        // }
        // return new NestedProperty(this, method, types[0], 0, newParameterNumber);

        return new NestedProperty(
            this, method, method.getGenericReturnType(), 0, newParameterNumber);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return descriptor.getReadMethod().hashCode();
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

        PropertyDescriptorProperty that = (PropertyDescriptorProperty) obj;

        if (!this.descriptor.getReadMethod().equals(that.descriptor.getReadMethod()))
        {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "PropertyDescriptorProperty[" + descriptor.getName() + "=" + descriptor.getPropertyType() + "]";
    }

    /**
     * The PropertyDescriptor that we are proxying to
     */
    protected PropertyDescriptor descriptor;

}
