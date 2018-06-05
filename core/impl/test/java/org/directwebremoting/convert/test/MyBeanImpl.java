package org.directwebremoting.convert.test;

/**
 * @author Bram Smeets
 */
public class MyBeanImpl
{
    private String property;

    private String nonReadableProperty;

    /**
     * @return a string
     */
    public String getProperty()
    {
        return property;
    }

    /**
     * @param property
     */
    public void setProperty(String property)
    {
        this.property = property;
    }

    /**
     * @param nonReadableProperty
     */
    public void setNonReadableProperty(String nonReadableProperty)
    {
        this.nonReadableProperty = nonReadableProperty;
    }

    /**
     * This just shuts lint up
     */
    protected void ignore()
    {
        String ignore = nonReadableProperty;
        nonReadableProperty = ignore;
    }
}
