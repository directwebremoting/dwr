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
package org.directwebremoting.extend;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.dwrp.ParseUtil;
import org.directwebremoting.dwrp.ProtocolConstants;
import org.directwebremoting.json.InvalidJsonException;
import org.directwebremoting.json.JsonArray;
import org.directwebremoting.json.JsonBoolean;
import org.directwebremoting.json.JsonHack;
import org.directwebremoting.json.JsonNull;
import org.directwebremoting.json.JsonNumber;
import org.directwebremoting.json.JsonObject;
import org.directwebremoting.json.JsonString;
import org.directwebremoting.json.JsonValue;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;

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
     * Attempt to de-reference an inbound variable.
     * We try de-referencing as soon as possible (why? there is a good reason
     * for it, it fixes some bug, but I can't remember what right now) However
     * the referenced variable may not exist yet, so the de-referencing may
     * fail, requiring us to have another go later.
     * @throws MarshallException If cross-references don't add up
     */
    public void dereference() throws MarshallException
    {
        int maxDepth = 0;

        while (ProtocolConstants.TYPE_REFERENCE.equals(type))
        {
            InboundVariable cd = context.getInboundVariable(formField.getString());
            if (cd == null)
            {
                throw new MarshallException(getClass(), Messages.getString("InboundVariable.MissingVariable", formField.getString()));
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
                throw new MarshallException(getClass(), "Max depth exceeded when dereferencing " + formField.getString());
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

        /**
         * If there is anything about the {@link InboundVariable} that can not
         * be represented in 100% pure JSON, then find some hack to do the best
         * we can and carry on. This option may produce invalid JSON
         */
        Hack,
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

        if (type.equalsIgnoreCase("boolean"))
        {
            return new JsonBoolean(Boolean.parseBoolean(value));
        }
        else if (type.equalsIgnoreCase("number"))
        {
            return new JsonNumber(Double.parseDouble(value));
        }
        else if (type.equalsIgnoreCase("string"))
        {
            return new JsonString(value);
        }
        else if (type.equalsIgnoreCase("date"))
        {
            switch (onError)
            {
            case Throw:
                throw new InvalidJsonException("Can't use date in JSON");
            case Skip:
                return new JsonNull();
            case Hack:
                return new JsonHack("new Date(" + value + ")");
            }
        }
        else if (type.equalsIgnoreCase("xml"))
        {
            switch (onError)
            {
            case Throw:
                throw new InvalidJsonException("Can't use XML in JSON");
            case Skip:
                return new JsonNull();
            case Hack:
                return new JsonHack(EnginePrivate.xmlStringToJavascriptDom(value));
            }
        }
        else if (type.equalsIgnoreCase("array"))
        {
            JsonArray array = new JsonArray();

            // If the text is null then the whole bean is null
            if (value.trim().equals(ProtocolConstants.INBOUND_NULL))
            {
                return new JsonNull();
            }

            if (!value.startsWith(ProtocolConstants.INBOUND_ARRAY_START))
            {
                throw new InvalidJsonException(Messages.getString("CollectionConverter.FormatError", ProtocolConstants.INBOUND_ARRAY_START));
            }

            if (!value.endsWith(ProtocolConstants.INBOUND_ARRAY_END))
            {
                throw new InvalidJsonException(Messages.getString("CollectionConverter.FormatError", ProtocolConstants.INBOUND_ARRAY_END));
            }

            value = value.substring(1, value.length() - 1);
            StringTokenizer st = new StringTokenizer(value, ProtocolConstants.INBOUND_ARRAY_SEPARATOR);
            int size = st.countTokens();
            for (int i = 0; i < size; i++)
            {
                String token = st.nextToken();

                String[] split = ParseUtil.splitInbound(token);
                String splitValue = split[LocalUtil.INBOUND_INDEX_VALUE];

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

            if (!value.startsWith(ProtocolConstants.INBOUND_MAP_START))
            {
                throw new InvalidJsonException(Messages.getString("MapConverter.FormatError", ProtocolConstants.INBOUND_MAP_START));
            }

            if (!value.endsWith(ProtocolConstants.INBOUND_MAP_END))
            {
                throw new InvalidJsonException(Messages.getString("MapConverter.FormatError", ProtocolConstants.INBOUND_MAP_END));
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
                    throw new InvalidJsonException(Messages.getString("MapConverter.MissingSeparator", ProtocolConstants.INBOUND_MAP_ENTRY, token));
                }

                // Convert the value part of the token by splitting it into the
                // type and value (as passed in by Javascript)
                String valStr = token.substring(colonpos + 1).trim();
                String[] splitIv = ParseUtil.splitInbound(valStr);
                String splitIvValue = splitIv[LocalUtil.INBOUND_INDEX_VALUE];

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
