/**
 * 
 */
package com.example.dwr.stress;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Setting
{
    public Setting(String name)
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

    public List<String> getClassNames()
    {
        return classNames;
    }

    public void setClassNames(List<String> classNames)
    {
        this.classNames = classNames;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public boolean isWritable()
    {
        return writable;
    }

    public void setWritable(boolean writable)
    {
        this.writable = writable;
    }

    private String name;

    private List<String> classNames = new ArrayList<String>();

    private Object value;

    private boolean writable = true;
}
