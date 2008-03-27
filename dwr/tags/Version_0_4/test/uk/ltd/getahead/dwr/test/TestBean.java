package uk.ltd.getahead.dwr.test;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TestBean
{
    /**
     * 
     */
    public TestBean()
    {
    }

    /**
     * @param string
     * @param integer
     * @param testBean
     */
    public TestBean(int integer, String string, TestBean testBean)
    {
        this.string = string;
        this.integer = integer;
        this.testBean = testBean;
    }

    /**
     * @return Returns the integer.
     */
    public int getInteger()
    {
        return integer;
    }

    /**
     * @param integer The integer to set.
     */
    public void setInteger(int integer)
    {
        this.integer = integer;
    }

    /**
     * @return Returns the string.
     */
    public String getString()
    {
        return string;
    }

    /**
     * @param string The string to set.
     */
    public void setString(String string)
    {
        this.string = string;
    }

    /**
     * @return Returns the testBean.
     */
    public TestBean getTestBean()
    {
        return testBean;
    }

    /**
     * @param testBean The testBean to set.
     */
    public void setTestBean(TestBean testBean)
    {
        this.testBean = testBean;
    }

    private String string = "Default initial value"; //$NON-NLS-1$
    private int integer = 42;
    private TestBean testBean = null;
}

