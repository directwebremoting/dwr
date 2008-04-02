package org.directwebremoting.convert.mapped;

import org.directwebremoting.util.CompareUtil;

/**
 * An example that is mapped to a bean
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BeanEx
{
    public BeanEx()
    {
    }

    public BeanEx(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "BeanEx[" + name + "]";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        BeanEx that = (BeanEx) obj;

        if (!CompareUtil.equals(this.getName(), that.getName()))
        {
            return false;
        }

        return true;
    }
    
    private String name;
}
