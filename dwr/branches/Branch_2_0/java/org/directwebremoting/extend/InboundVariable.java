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

import org.directwebremoting.dwrp.ProtocolConstants;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

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
        this.type = type;
        this.value = value;
        this.key = key;
        this.dereferenced = attemptDereference();
    }

    /**
     * Attempt to de-reference an inbound variable.
     * We try de-referencing as soon as possible (why? there is a good reason
     * for it, it fixes some bug, but I can't remember what right now) However
     * the referenced variable may not exist yet, so the de-referencing may
     * fail, requiring us to have another go later.
     * @return Did the dereferencing succeed?
     */
    private boolean attemptDereference()
    {
        int maxDepth = 0;

        if (ProtocolConstants.TYPE_REFERENCE.equals(type))
        {
            while (ProtocolConstants.TYPE_REFERENCE.equals(type))
            {
                InboundVariable cd = context.getInboundVariable(value);
                if (cd == null)
                {
                    return false;
                }

                key = value; // Let the variable name follow the dereferenced value
                type = cd.type;
                value = cd.value;

                maxDepth++;
                if (maxDepth > 20)
                {
                    throw new IllegalStateException("Max depth exceeded when dereferencing " + value);
                }
            }

            // For references without an explicit variable name, we use the
            // name of the thing they point at
            if (key == null)
            {
                key = value;
            }
        }

        return true;
    }

    /**
     * Call <code>attemptDereference()</code>, and complain if it fails.
     * The assumption is that when we call this it really should succeed.
     */
    private void forceDereference()
    {
        if (!dereferenced)
        {
            dereferenced = attemptDereference();
            if (!dereferenced)
            {
                log.error(Messages.getString("InboundVariable.MissingVariable", value));
            }
        }
    }

    /**
     * @return Returns the lookup table.
     */
    public InboundContext getLookup()
    {
        forceDereference();
        return context;
    }

    /**
     * @return Returns the type.
     */
    public String getType()
    {
        forceDereference();
        return type;
    }

    /**
     * Was this type null on the way in
     * @return true if the javascript variable was null or undefined.
     */
    public boolean isNull()
    {
        forceDereference();
        return type.equals(ProtocolConstants.INBOUND_NULL);
    }

    /**
     * @return Returns the value.
     */
    public String getValue()
    {
        forceDereference();
        return value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        forceDereference();
        return type + ProtocolConstants.INBOUND_TYPE_SEPARATOR + value;
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

        forceDereference();

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

        return this.key.equals(that.key);
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
    private String key;

    /**
     * The javascript declared variable type
     */
    private String type;

    /**
     * The javascript declared variable value
     */
    private String value;

    /**
     * Has this variable been successfully de-referenced
     */
    private boolean dereferenced;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(InboundVariable.class);
}
