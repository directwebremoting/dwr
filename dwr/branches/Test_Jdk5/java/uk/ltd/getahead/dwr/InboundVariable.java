package uk.ltd.getahead.dwr;

/**
 * A simple struct to hold data about a single converted javascript variable.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class InboundVariable
{
    /**
     * Parsing ctor
     * @param context How we lookup references
     * @param type The type information from javascript
     * @param value The javascript variable converted to a string
     */
    public InboundVariable(InboundContext context, String type, String value)
    {
        this.context = context;
        this.type = type;
        this.value = value;
    }

    /**
     * @return Returns the lookup table.
     */
    public InboundContext getLookup()
    {
        return context;
    }

    /**
     * @return Returns the type.
     */
    public String getType()
    {
        return type;
    }

    /**
     * @return Returns the value.
     * @throws ConversionException If we can't follow a reference
     */
    public String getValue() throws ConversionException
    {
        String tempType = type;
        String tempValue = value;

        while (TYPE_REFERENCE.equals(tempType))
        {
            InboundVariable cd = context.getInboundVariable(tempValue);
            if (cd == null)
            {
                throw new ConversionException("Failed to find reference to " + tempValue);
            }

            tempType = cd.type;
            tempValue = cd.value;
        }

        return tempValue;
    }

    /**
     * @return Returns the type and value in one string.
     */
    public String getRawData()
    {
        return type + ":" + value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        if (TYPE_REFERENCE.equals(type))
        {
            try
            {
                return type + ":" + value + "=(" + getValue() + ")";
            }
            catch (ConversionException ex)
            {
                return type + ":" + value + "=(invalid)";
            }
        }
        else
        {
            return type + ":" + value;
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (!(obj instanceof InboundVariable))
        {
            return false;
        }

        InboundVariable that = (InboundVariable) obj;

        if (!this.type.equals(that.type))
        {
            return false;
        }

        if (!this.value.equals(that.value))
        {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return value.hashCode() + type.hashCode();
    }

    /**
     * The name for reference types
     */
    private static final String TYPE_REFERENCE = "reference";

    /**
     * How do be lookup references?
     */
    private InboundContext context;

    /**
     * The javascript declared variable type
     */
    private String type;

    /**
     * The javascript declared variable value
     */
    private String value;
}
