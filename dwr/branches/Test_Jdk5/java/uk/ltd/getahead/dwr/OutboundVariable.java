package uk.ltd.getahead.dwr;

/**
 * A simple data container for 2 strings that comprise information about
 * how a Java object has been converted into Javascript
 */
public class OutboundVariable
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
     * The code to be executed to initialize any variables
     */
    public String initCode = "";

    /**
     * The code to be executed to get the value of the initialized data
     */
    public String assignCode = "";
}