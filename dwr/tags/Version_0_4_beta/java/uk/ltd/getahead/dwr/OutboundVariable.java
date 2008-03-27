package uk.ltd.getahead.dwr;

/**
 * A simple data container for 2 strings that comprise information about
 * how a Java object has been converted into Javascript
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class OutboundVariable
{
    /**
     * Default ctor that leaves blank (not null) members
     */
    public OutboundVariable()
    {
    }

    /**
     * Default ctor that leaves blank (not null) members
     * @param initCode the init script
     * @param assignCode the access for the inited code
     */
    public OutboundVariable(String initCode, String assignCode)
    {
        this.initCode = initCode;
        this.assignCode = assignCode;
    }

    /**
     * @param initCode The initCode to set.
     */
    public void setInitCode(String initCode)
    {
        this.initCode = initCode;
    }

    /**
     * @return Returns the initCode.
     */
    public String getInitCode()
    {
        return initCode;
    }

    /**
     * @param assignCode The assignCode to set.
     */
    public void setAssignCode(String assignCode)
    {
        this.assignCode = assignCode;
    }

    /**
     * @return Returns the assignCode.
     */
    public String getAssignCode()
    {
        return assignCode;
    }

    /**
     * The code to be executed to initialize any variables
     */
    private String initCode = ""; //$NON-NLS-1$

    /**
     * The code to be executed to get the value of the initialized data
     */
    private String assignCode = ""; //$NON-NLS-1$
}
