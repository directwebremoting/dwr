package org.directwebremoting.json.types;

/**
 * JsonValue allows you to hold any of the JSON types and to get a string
 * version without needing to know what type you have.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class JsonValue
{
    /**
     * i.e. How do we send this from one system to another?
     * @return A string representing the portable version of this data
     */
    public abstract String toExternalRepresentation();

    /**
     * Often when dealing with a {@link JsonValue} you know which subtype it is,
     * this allows you to assume it is a string and get the Java value quickly.
     * @return A String representing the {@link JsonNumber} implementing this
     * @throws ClassCastException if this is not a JsonString
     */
    public String getString()
    {
        throw new ClassCastException(this.getClass().getSimpleName() + " is not a " + JsonString.class.getSimpleName());
    }

    /**
     * Often when dealing with a {@link JsonValue} you know which subtype it is,
     * this allows you to assume it is a number and get the Java value quickly.
     * @return An int representing the {@link JsonNumber} implementing this
     * @throws ClassCastException if this is not a JsonNumber
     */
    public int getInteger()
    {
        throw new ClassCastException(this.getClass().getSimpleName() + " is not a " + JsonNumber.class.getSimpleName());
    }

    /**
     * Often when dealing with a {@link JsonValue} you know which subtype it is,
     * this allows you to assume it is a number and get the Java value quickly.
     * @return A long representing the {@link JsonNumber} implementing this
     * @throws ClassCastException if this is not a JsonNumber
     */
    public long getLong()
    {
        throw new ClassCastException(this.getClass().getSimpleName() + " is not a " + JsonNumber.class.getSimpleName());
    }

    /**
     * Often when dealing with a {@link JsonValue} you know which subtype it is,
     * this allows you to assume it is a number and get the Java value quickly.
     * @return A double representing the {@link JsonNumber} implementing this
     * @throws ClassCastException if this is not a JsonNumber
     */
    public double getDouble()
    {
        throw new ClassCastException(this.getClass().getSimpleName() + " is not a " + JsonNumber.class.getSimpleName());
    }

    /**
     * Often when dealing with a {@link JsonValue} you know which subtype it is,
     * this allows you to assume it is a boolean and get the Java value quickly.
     * @return A boolean representing the {@link JsonNumber} implementing this
     * @throws ClassCastException if this is not a JsonNumber
     */
    public boolean getBoolean()
    {
        throw new ClassCastException(this.getClass().getSimpleName() + " is not a " + JsonNumber.class.getSimpleName());
    }

    /**
     * Often when dealing with a {@link JsonValue} you know which subtype it is,
     * this allows you to assume it is a JsonArray and get it quickly
     * @return A cast of this to {@link JsonArray}
     * @throws ClassCastException if this is not a JsonArray
     */
    public JsonArray getJsonArray()
    {
        throw new ClassCastException(this.getClass().getSimpleName() + " is not a " + JsonArray.class.getSimpleName());
    }

    /**
     * Often when dealing with a {@link JsonValue} you know which subtype it is,
     * this allows you to assume it is a JsonObject and get it quickly
     * @return A cast of this to {@link JsonObject}
     * @throws ClassCastException if this is not a JsonObject
     */
    public JsonObject getJsonObject()
    {
        throw new ClassCastException(this.getClass().getSimpleName() + " is not a " + JsonObject.class.getSimpleName());
    }
}
