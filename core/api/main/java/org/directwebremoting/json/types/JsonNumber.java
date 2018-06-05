package org.directwebremoting.json.types;

/**
 * The Json version of a Number
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonNumber extends JsonValue
{
    /**
     * All JsonNumbers wrap something stored as a double
     */
    public JsonNumber(int value)
    {
        this.value = value;
    }

    /**
     * All JsonNumbers wrap something stored as a double
     */
    public JsonNumber(long value)
    {
        this.value = value;
    }

    /**
     * All JsonNumbers wrap something stored as a double
     */
    public JsonNumber(double value)
    {
        this.value = value;
    }

    /**
     * Parse the input string as a double
     */
    public JsonNumber(String text)
    {
        this.value = Double.parseDouble(text);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#getDouble()
     */
    @Override
    public double getDouble()
    {
        return value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#getLong()
     */
    @Override
    public long getLong()
    {
        return (long) value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#getInteger()
     */
    @Override
    public int getInteger()
    {
        return (int) value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#toExternalRepresentation()
     */
    @Override
    public String toExternalRepresentation()
    {
        return Double.toString(value);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return toExternalRepresentation();
    }

    /**
     * The string value that we wrap
     */
    private final double value;
}
