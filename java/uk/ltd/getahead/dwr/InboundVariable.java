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
package uk.ltd.getahead.dwr;

import uk.ltd.getahead.dwr.util.Logger;

/**
 * A simple struct to hold data about a single converted javascript variable.
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
        this.context = context;

        if (ConversionConstants.TYPE_REFERENCE.equals(type))
        {
            String tempType = type;
            String tempValue = value;

            while (ConversionConstants.TYPE_REFERENCE.equals(tempType))
            {
                InboundVariable cd = context.getInboundVariable(tempValue);
                if (cd == null)
                {
                    log.error(Messages.getString("InboundVariable.MissingVariable", tempValue)); //$NON-NLS-1$
                    break;
                }

                tempType = cd.type;
                tempValue = cd.value;
            }

            this.type = tempType;
            this.value = tempValue;

            // For references without an explicit variable name, we use the
            // name of the thing they point at
            if (key == null)
            {
                this.key = value;
            }
            else
            {
                this.key = key;
            }
        }
        else
        {
            this.type = type;
            this.value = value;
            this.key = key;
        }
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
     * Was this type null on the way in
     * @return true if the javascript variable was null or undefined.
     */
    public boolean isNull()
    {
        return type.equals(ConversionConstants.INBOUND_NULL);
    }

    /**
     * @return Returns the value.
     */
    public String getValue()
    {
        return value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return type + ConversionConstants.INBOUND_TYPE_SEPARATOR + value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

        if (!this.value.equals(that.value))
        {
            return false;
        }

        if (this.key == null || that.key == null)
        {
            return false;
        }

        if (!this.key.equals(that.key))
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
     * How do be lookup references?
     */
    private InboundContext context;

    /**
     * The variable name
     */
    private final String key;

    /**
     * The javascript declared variable type
     */
    private final String type;

    /**
     * The javascript declared variable value
     */
    private final String value;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(InboundVariable.class);
}
