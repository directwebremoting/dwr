package org.directwebremoting.convert.mapped;

import org.directwebremoting.util.CompareUtil;

/**
 * An example that is mapped to the object converter
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ObjectEx
{
    public ObjectEx()
    {
    }

    public ObjectEx(String name)
    {
        this.name = name;
    }

    public String name;

    private String hidden;

    @Override
    public String toString()
    {
        return "ObjectEx[" + name + "]";
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

        ObjectEx that = (ObjectEx) obj;

        if (!CompareUtil.equals(this.name, that.name))
        {
            return false;
        }

        return true;
    }

    void shutupStupidCompiler() { System.getProperty(name, hidden); }
}
