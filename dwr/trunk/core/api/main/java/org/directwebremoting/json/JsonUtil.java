/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.json;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.json.impl.IgnoreJsonDecoder;
import org.directwebremoting.json.parse.JsonParseException;
import org.directwebremoting.json.parse.JsonParser;
import org.directwebremoting.json.parse.javacc.JavaccJsonParser;
import org.directwebremoting.json.simple.SimpleJsonDecoder;

/**
 * Various utilities to make parsing and reading JSON easier
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonUtil
{
    /**
     * Convert the input string into a set of basic types
     */
    public Map<String, Object> toSimpleTypes(String input) throws JsonParseException
    {
        log.info("testing: " + input);
        return toSimpleTypes(new StringReader(input));
    }

    /**
     * Convert the input document into a set of basic types
     */
    public Map<String, Object> toSimpleTypes(Reader reader) throws JsonParseException
    {
        JsonParser parser = new JavaccJsonParser();
        return parser.parse(reader, new SimpleJsonDecoder());
    }

    /**
     * Get a record of any errors in parsing the input string
     */
    public String getErrors(String input)
    {
        return getErrors(new StringReader(input));
    }

    /**
     * Get a record of any errors in parsing the input document
     */
    public String getErrors(Reader reader)
    {
        try
        {
            JsonParser parser = new JavaccJsonParser();
            parser.parse(reader, new IgnoreJsonDecoder());
            return null;
        }
        catch (JsonParseException ex)
        {
            return ex.toString();
        }
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

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JsonUtil.class);
}
