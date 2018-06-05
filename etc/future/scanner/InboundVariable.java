package org.directwebremoting.extend;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.json.InvalidJsonException;
import org.directwebremoting.json.JsonArray;
import org.directwebremoting.json.JsonBoolean;
import org.directwebremoting.json.JsonNull;
import org.directwebremoting.json.JsonNumber;
import org.directwebremoting.json.JsonObject;
import org.directwebremoting.json.JsonString;
import org.directwebremoting.json.JsonValue;

/**
 * A simple struct to hold data about a single converted javascript variable.
 * An inbound variable will have either a value or a fileValue but not both. 
 * If file is <code>true</code> fileValue will be populated, otherwise value
 * will be populated.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class InboundVariable
{
    /**
     * Parsing ctor
     * @param context How we lookup references
     * @param key The name of the variable that this was transfered as
     * @param type The type information from javascript
     * @param value The javascript variable converted to a string
     */
    public InboundVariable(InboundContext context, String key, String type, String value)
    {
        this(context, key, type, new FormField(value));
    }

    /**
     * Parsing ctor
     * @param context How we lookup references
     * @param key The name of the variable that this was transfered as
     * @param type The type information from javascript
     * @param fileValue The javascript variable converted to a FormField
     */
    public InboundVariable(InboundContext context, String key, String type, FormField fileValue)
    {
        this.context = context;
        this.type = type;
        this.formField = fileValue;
        this.key = key;
    }

    /**
     * Accessor of the context of the variable: the other related variables
     */
    public InboundContext getContext()
    {
        return context;
    }

    /**
     * Attempt to de-reference an inbound variable.
     * We try de-referencing as soon as possible (why? there is a good reason
     * for it, it fixes some bug, but I can't remember what right now) However
     * the referenced variable may not exist yet, so the de-referencing may
     * fail, requiring us to have another go later.
     * @throws ConversionException If cross-references don't add up
     */
    public void dereference() throws ConversionException
    {
        int maxDepth = 0;

        while (ProtocolConstants.TYPE_REFERENCE.equals(type))
        {
            InboundVariable cd = context.getInboundVariable(formField.getString());
            if (cd == null)
            {
                throw new ConversionException(getClass(), "Found reference to variable named '" + formField.getString() + "', but no variable of that name could be found.");
            }

            type = cd.type;
            formField = cd.getFormField();

            // For some reason we used to leave this until the loop finished
            // and then only set it if the key was null. I think this logic
            // may have been broken by named objects
            key = cd.key;

            maxDepth++;
            if (maxDepth > 20)
            {
                throw new ConversionException(getClass(), "Max depth exceeded when dereferencing " + formField.getString());
            }
        }

        // For references without an explicit variable name, we use the
        // name of the thing they point at
        // if (key == null)
        // {
        //     key = formField.getString();
        // }
    }

    /**
     * @return Returns the lookup table.
     */
    public InboundContext getLookup()
    {
        return context;
    }

    /**
     * If we are using object parameters that have specified types then the
     * {@link ConverterManager} will need to get to know what the required type
     * is.
     * @return The requested object type, or null if one was not specified
     */
    public String getNamedObjectType()
    {
        if (type.startsWith("Object_"))
        {
            return type.substring("Object_".length());
        }
        else
        {
            return null;
        }
    }

    public enum OnJsonParseError
    {
        /**
         * If there is anything about the {@link InboundVariable} that can not
         * be represented in 100% pure JSON, then throw
         */
        Throw,

        /**
         * If there is anything about the {@link InboundVariable} that can not
         * be represented in 100% pure JSON, then insert null and carry on
         */
        Skip,
    }

    /**
     * Convert the set of {@link InboundVariable}s to JSON
     * @return This object in JSON
     * @throws InvalidJsonException If this can't be represented as JSON
     */
    public JsonValue getJsonValue(OnJsonParseError onError) throws InvalidJsonException
    {
        return getJsonValue(onError, 0);
    }

    /**
     * Convert the set of {@link InboundVariable}s to JSON
     * @return This object in JSON
     * @throws InvalidJsonException If this can't be represented as JSON
     */
    private JsonValue getJsonValue(OnJsonParseError onError, int currentDepth) throws InvalidJsonException
    {
        if (currentDepth > 50)
        {
            throw new InvalidJsonException("JSON structure too deeply nested. Is it recursive?");
        }

        String value = getValue();

        if ("boolean".equalsIgnoreCase(type))
        {
            return new JsonBoolean(Boolean.parseBoolean(value));
        }
        else if ("number".equalsIgnoreCase(type))
        {
            return new JsonNumber(Double.parseDouble(value));
        }
        else if ("string".equalsIgnoreCase(type))
        {
            return new JsonString(value);
        }
        else if ("date".equalsIgnoreCase(type))
        {
            switch (onError)
            {
            case Throw:
                throw new InvalidJsonException("Can't use date in JSON");
            case Skip:
                return new JsonNull();
            }
        }
        else if ("xml".equalsIgnoreCase(type))
        {
            switch (onError)
            {
            case Throw:
                throw new InvalidJsonException("Can't use XML in JSON");
            case Skip:
                return new JsonNull();
            }
        }
        else if ("array".equalsIgnoreCase(type))
        {
            JsonArray array = new JsonArray();

            // If the text is null then the whole bean is null
            if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
            {
                return new JsonNull();
            }

            if (!value.startsWith(ProtocolConstants.INBOUND_ARRAY_START) || !value.endsWith(ProtocolConstants.INBOUND_ARRAY_END))
            {
                log.warn("Expected collection. Passed: " + value);
                throw new InvalidJsonException("Data conversion error. See logs for more details.");
            }

            value = value.substring(1, value.length() - 1);
            StringTokenizer st = new StringTokenizer(value, ProtocolConstants.INBOUND_ARRAY_SEPARATOR);
            int size = st.countTokens();
            for (int i = 0; i < size; i++)
            {
                String token = st.nextToken();

                String[] split = ConvertUtil.splitInbound(token);
                String splitValue = split[ConvertUtil.INBOUND_INDEX_VALUE];

                InboundVariable nested = context.getInboundVariable(splitValue);
                array.add(nested.getJsonValue(onError, currentDepth + 1));
            }

            return array;
        }
        else if (type.startsWith("Object_"))
        {
            JsonObject object = new JsonObject();

            // If the text is null then the whole bean is null
            if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
            {
                return new JsonNull();
            }

            if (!value.startsWith(ProtocolConstants.INBOUND_MAP_START) || !value.endsWith(ProtocolConstants.INBOUND_MAP_END))
            {
                log.warn("Expected object. Passed: " + value);
                throw new InvalidJsonException("Data conversion error. See logs for more details.");
            }

            value = value.substring(1, value.length() - 1);

            // Loop through the property declarations
            StringTokenizer st = new StringTokenizer(value, ",");
            int size = st.countTokens();
            for (int i = 0; i < size; i++)
            {
                String token = st.nextToken();
                if (token.trim().length() == 0)
                {
                    continue;
                }

                int colonpos = token.indexOf(ProtocolConstants.INBOUND_MAP_ENTRY);
                if (colonpos == -1)
                {
                    throw new InvalidJsonException("Missing separator: " + ProtocolConstants.INBOUND_MAP_ENTRY);
                }

                // Convert the value part of the token by splitting it into the
                // type and value (as passed in by Javascript)
                String valStr = token.substring(colonpos + 1).trim();
                String[] splitIv = ConvertUtil.splitInbound(valStr);
                String splitIvValue = splitIv[ConvertUtil.INBOUND_INDEX_VALUE];

                String keyStr = token.substring(0, colonpos).trim();

                InboundVariable nested = context.getInboundVariable(splitIvValue);
                object.put(keyStr, nested.getJsonValue(onError, currentDepth + 1));
            }

            return object;
        }

        log.warn("Data type: " + type + " is not one that InboundVariable understands");
        throw new InvalidJsonException("Unknown data type");
    }

    /**
     * Was this type null on the way in
     * @return true if the javascript variable was null or undefined.
     */
    public boolean isNull()
    {
        return type.equals(ProtocolConstants.INBOUND_NULL);
    }

    /**
     * @return Returns the value.
     */
    public String getValue()
    {
        return formField.getString();
    }

    /**
     * @return Returns the file value
     */
    public FormField getFormField()
    {
        return formField;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return type + ProtocolConstants.INBOUND_TYPE_SEPARATOR + formField.getString(); 
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof InboundVariable))
        {
            return false;
        }

        InboundVariable that = (InboundVariable) obj;

        if (!this.type.equals(that.type))
        {
            return false;
        }

        if (!this.formField.equals(that.formField))
        {
            return false;
        }

        if (this.key == null || that.key == null)
        {
            return false;
        }

        return true; // this.key.equals(that.key);
    }

    public String getType() {
        return type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return formField.hashCode() + type.hashCode();
    }

    /**
     * How do be lookup references?
     */
    private InboundContext context;

    /**
     * The variable name
     */
    private String key;

    /**
     * The javascript declared variable type
     */
    private String type;

    /**
     * The javascript declared file value
     */
    private FormField formField;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(InboundVariable.class);
}
