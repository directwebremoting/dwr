package org.directwebremoting.spring;

/**
 * @author Brendan Grainger
 */
public class TestIncludesBean
{
    private String includedProperty;
    private String notIncludedProperty;
    
    /**
     * Method to be included in testing
     * @return value of included property
     */
    public String getIncludedProperty()
    {
        return includedProperty;
    }

    /**
     * Method to be included in testing
     * @param stringProperty of included property
     */
    public void setIncludedProperty(String stringProperty)
    {
        this.includedProperty = stringProperty;
    }

    /**
     * @return the notIncludedProperty
     */
    public String getNotIncludedProperty()
    {
        return notIncludedProperty;
    }

    /**
     * @param notIncludedProperty the notIncludedProperty to set
     */
    public void setNotIncludedProperty(String notIncludedProperty)
    {
        this.notIncludedProperty = notIncludedProperty;
    }
  
}

