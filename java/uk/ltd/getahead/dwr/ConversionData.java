package uk.ltd.getahead.dwr;

import java.util.Map;

import uk.ltd.getahead.dwr.util.Log;

/**
 * A simple struct to hold data about a single converted javascript variable.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConversionData
{
    /**
     * Parsing ctor
     * @param lookup How we lookup references
     * @param data The value passed from javascript
     */
    public ConversionData(Map lookup, String data)
    {
        this.lookup = lookup;

        int colon = data.indexOf(":");
        if (colon != -1)
        {
            type = data.substring(0, colon);
            value = data.substring(colon + 1);
        }
        else
        {
            Log.error("Missing : in conversion data");
            type = "string";
            value = data;
        }
    }

    /**
     * Simple ctor
     * @param lookup How we lookup references
     * @param type The javascript type
     * @param value The javascript value
     */
    public ConversionData(Map lookup, String type, String value)
    {
        this.lookup = lookup;
        this.type = type;
        this.value = value;
    }

    /**
     * @return Returns the lookup table.
     */
    public Map getLookup()
    {
        return lookup;
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
            ConversionData cd = (ConversionData) lookup.get(tempValue);
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
        if (!(obj instanceof ConversionData))
        {
            return false;
        }

        ConversionData that = (ConversionData) obj;

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
    private Map lookup;

    /**
     * The javascript declared variable type
     */
    private String type;

    /**
     * The javascript declared variable value
     */
    private String value;
}
