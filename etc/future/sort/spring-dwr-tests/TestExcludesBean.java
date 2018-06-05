package org.directwebremoting.spring;

/**
 * @author Brendan Grainger
 */
public class TestExcludesBean
{
    private String excludedProperty;

    private String notExcludedProperty;

    /**
     * @return the excludedProperty
     */
    public String getExcludedProperty()
    {
        return excludedProperty;
    }

    /**
     * @param excludedProperty the excludedProperty to set
     */
    public void setExcludedProperty(String excludedProperty)
    {
        this.excludedProperty = excludedProperty;
    }

    /**
     * @return the notExcludedProperty
     */
    public String getNotExcludedProperty()
    {
        return notExcludedProperty;
    }

    /**
     * @param notExcludedProperty the notExcludedProperty to set
     */
    public void setNotExcludedProperty(String notExcludedProperty)
    {
        this.notExcludedProperty = notExcludedProperty;
    }
  
}

