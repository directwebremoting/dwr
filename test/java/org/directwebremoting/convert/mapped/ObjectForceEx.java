package org.directwebremoting.convert.mapped;

import org.directwebremoting.util.CompareUtil;

/**
 * An example that is mapped to the object converter, which requires use of the
 * force parameter to read the name member
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ObjectForceEx
{
    public ObjectForceEx()
    {
    }

    public ObjectForceEx(String name)
    {
        this.name = name;
    }

    private String name;

    @Override
    public String toString()
    {
        return "ObjectForceEx[" + name + "]";
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

        ObjectForceEx that = (ObjectForceEx) obj;

        if (!CompareUtil.equals(this.name, that.name))
        {
            return false;
        }

        return true;
    }

    void shutupStupidCompiler() { System.getProperty(name, name); }
}
