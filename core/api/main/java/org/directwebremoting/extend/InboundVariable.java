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

import org.directwebremoting.ConversionException;

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
    private final InboundContext context;

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
}
