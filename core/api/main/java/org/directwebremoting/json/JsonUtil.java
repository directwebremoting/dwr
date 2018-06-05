package org.directwebremoting.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.directwebremoting.json.parse.JsonParseException;
import org.directwebremoting.json.parse.JsonParser;
import org.directwebremoting.json.parse.JsonParserFactory;
import org.directwebremoting.json.parse.impl.IgnoreJsonDecoder;
import org.directwebremoting.json.parse.impl.ReflectionJsonDecoder;
import org.directwebremoting.json.parse.impl.SimpleJsonDecoder;
import org.directwebremoting.json.serialize.JsonSerializer;
import org.directwebremoting.json.serialize.JsonSerializerFactory;

/**
 * Various utilities to make parsing and reading JSON easier
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonUtil
{
    /**
     * Get a record of any errors in parsing the input string
     */
    public static String getErrors(String input)
    {
        return getErrors(new StringReader(input));
    }

    /**
     * Get a record of any errors in parsing the input document
     */
    public static String getErrors(Reader reader)
    {
        try
        {
            JsonParser parser = JsonParserFactory.get();
            parser.parse(reader, new IgnoreJsonDecoder());
            return null;
        }
        catch (JsonParseException ex)
        {
            return ex.toString();
        }
    }

    /**
     * Convert the input string into a set of basic types
     */
    public static Map<String, Object> toSimpleObject(String input) throws JsonParseException
    {
        return toSimpleObject(new StringReader(input));
    }

    /**
     * Convert the input document into a set of basic types
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toSimpleObject(Reader reader) throws JsonParseException
    {
        JsonParser parser = JsonParserFactory.get();
        return (Map<String, Object>) parser.parse(reader, new SimpleJsonDecoder());
    }

    /**
     * Convert the input string into a set of basic types
     */
    public static List<Object> toSimpleArray(String input) throws JsonParseException
    {
        return toSimpleArray(new StringReader(input));
    }

    /**
     * Convert the input document into a set of basic types
     */
    @SuppressWarnings("unchecked")
    public static List<Object> toSimpleArray(Reader reader) throws JsonParseException
    {
        JsonParser parser = JsonParserFactory.get();
        return (List<Object>) parser.parse(reader, new SimpleJsonDecoder());
    }

    /**
     * Convert the input string into a set of basic types
     */
    public static <T> T toReflectedTypes(Class<T> marshallInto, String input) throws JsonParseException
    {
        return toReflectedTypes(marshallInto, new StringReader(input));
    }

    /**
     * Convert the input document into a set of basic types
     */
    @SuppressWarnings("unchecked")
    public static <T> T toReflectedTypes(Class<T> marshallInto, Reader reader) throws JsonParseException
    {
        JsonParser parser = JsonParserFactory.get();
        return (T) parser.parse(reader, new ReflectionJsonDecoder(marshallInto));
    }

    /**
     * Convert arbitrary convertible data into a JSON string and write it out
     * to the given Writer
     */
    public static void toJson(Object data, Writer out) throws IOException
    {
        JsonSerializer serializer = JsonSerializerFactory.get();
        serializer.toJson(data, out);
    }

    /**
     * Convert arbitrary convertible data into a JSON string.
     */
    public static String toJson(Object data) throws IOException
    {
        StringWriter out = new StringWriter();
        toJson(data, out);
        return out.toString();
    }

    /*
     * Convert the set of {@link InboundVariable}s to JSON
     * @return This object in JSON
     * @throws InvalidJsonException If this can't be represented as JSON
     *
    public JsonValue getJsonValue(OnJsonParseError onError) throws InvalidJsonException
    {
        return getJsonValue(onError, 0);
    }

    /*
     * Convert the set of {@link InboundVariable}s to JSON
     * @return This object in JSON
     * @throws InvalidJsonException If this can't be represented as JSON
     *
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
    */
}
