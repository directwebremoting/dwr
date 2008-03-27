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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.directwebremoting.dwrp.ProtocolConstants;
import org.directwebremoting.util.Logger;

/**
 * InboundContext is the context for set of inbound conversions.
 * Since a data set may be recurrsive parts of some data members may refer to
 * others so we need to keep track of who is converted for what.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class InboundContext
{
    /**
     * Someone wants to tell us about a new conversion context.
     * @param context The current conversion context
     */
    public void pushContext(TypeHintContext context)
    {
        contexts.addFirst(context);
    }

    /**
     * Someone wants to tell us about a finished conversion context.
     */
    public void popContext()
    {
        contexts.removeFirst();
    }

    /**
     * @return The method that we are currently converting data for
     */
    public TypeHintContext getCurrentTypeHintContext()
    {
        return (TypeHintContext) contexts.getFirst();
    }

    /**
     * Create an inbound variable.
     * Usually called by a query parser to setup a list of known variables.
     * This method also checks to see if the new variable is a parameter and if
     * it is it updates the count of parameters
     * @param callNum The call number to work on
     * @param key The name of the variable
     * @param type The javascript type of the variable
     * @param value The value of the variable
     */
    public void createInboundVariable(int callNum, String key, String type, String value)
    {
        InboundVariable cte = new InboundVariable(this, key, type, value);

        Object old = variables.put(key, cte);
        if (old != null)
        {
            log.warn("Duplicate variable called: " + key);
        }

        String paramPrefix = ProtocolConstants.INBOUND_CALLNUM_PREFIX + callNum +
                             ProtocolConstants.INBOUND_CALLNUM_SUFFIX +
                             ProtocolConstants.INBOUND_KEY_PARAM;

        if (key.startsWith(paramPrefix))
        {
            int i = Integer.parseInt(key.substring(paramPrefix.length())) + 1;
            if (i > paramCount)
            {
                paramCount = i;
            }
        }
    }

    /**
     * Internal method to allow entries to resolve references
     * @param name The name of the variable to lookup
     * @return The found variable
     */
    public InboundVariable getInboundVariable(String name)
    {
        return (InboundVariable) variables.get(name);
    }

    /**
     * Clear the list of converted objects.
     * If the conversion attempt for a given method failed, we may want to try
     * another so we will need to ditch the list of converted objects because
     * the next method could well have different parameter types.
     */
    public void clearConverted()
    {
        converted.clear();
    }

    /**
     * Add to the (temporary) list of converted objects
     * @param iv The converted object
     * @param type The type that we converted the object to
     * @param bean The converted version
     */
    public void addConverted(InboundVariable iv, Class type, Object bean)
    {
        Conversion conversion = new Conversion(iv, type);
        Object old = converted.put(conversion, bean);
        if (old != null)
        {
            log.warn("Duplicate variable conversion called: " + conversion);
        }
    }

    /**
     * Check to see if the conversion has already been done
     * @param iv The inbound data to check
     * @param type The type that we want the object converted to
     * @return The converted data or null if it has not been converted
     */
    public Object getConverted(InboundVariable iv, Class type)
    {
        Conversion conversion = new Conversion(iv, type);
        return converted.get(conversion);
    }

    /**
     * How many parameters are there?
     * @return The parameter count
     */
    public int getParameterCount()
    {
        return paramCount;
    }

    /**
     * This is a bit of a hack, needed for debug purposes - it counts the
     * parameters (including method and script params) for a given call number
     * @param callNum The Call number to count the parameters of
     * @return The parameter count for a given Call
     */
    public int getParameterCount(int callNum)
    {
        int count = 0;
        String prefix = ProtocolConstants.INBOUND_CALLNUM_PREFIX + callNum + ProtocolConstants.INBOUND_CALLNUM_SUFFIX + ProtocolConstants.INBOUND_KEY_PARAM;
        for (Iterator it = variables.keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            if (key.startsWith(prefix))
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Get a parameter by index
     * @param callNum The call number to work on
     * @param index The parameter index
     * @return The found parameter
     */
    public InboundVariable getParameter(int callNum, int index)
    {
        String key = ProtocolConstants.INBOUND_CALLNUM_PREFIX + callNum +
                     ProtocolConstants.INBOUND_CALLNUM_SUFFIX +
                     ProtocolConstants.INBOUND_KEY_PARAM + index;

        return (InboundVariable) variables.get(key);
    }

    /**
     * A debug method so people can get a list of all the variable names
     * @return an iterator over the known variable names
     */
    public Iterator getInboundVariableNames()
    {
        return variables.keySet().iterator();
    }

    /**
     * A Class to use as a key in a map for conversion purposes
     */
    protected static class Conversion
    {
        /**
         * @param inboundVariable
         * @param type
         */
        Conversion(InboundVariable inboundVariable, Class type)
        {
            this.inboundVariable = inboundVariable;
            this.type = type;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj)
        {
            if (!(obj instanceof Conversion))
            {
                return false;
            }

            Conversion that = (Conversion) obj;

            if (!this.type.equals(that.type))
            {
                return false;
            }

            return this.inboundVariable.equals(that.inboundVariable);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        public int hashCode()
        {
            return inboundVariable.hashCode() + type.hashCode();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return "Conversion[" + inboundVariable + "," + type.getName() + "]";
        }

        private InboundVariable inboundVariable;

        private Class type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("InboundContext[");
        for (Iterator it = variables.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            buffer.append(entry.getKey());
            buffer.append('=');
            buffer.append(entry.getValue());
            buffer.append(',');
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * The stack of pushed conversion contexts.
     * i.e. What is the context of this type conversion.
     */
    private LinkedList contexts = new LinkedList();

    /**
     * How many params are there?.
     * To be more accurate, return one less than the highest numbered parameter
     * that we have come across.
     */
    private int paramCount = 0;

    /**
     * A map of all the inbound variables
     */
    private final Map variables = new HashMap();

    /**
     * A map of all the variables converted.
     */
    private final Map converted = new HashMap();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(InboundContext.class);
}
