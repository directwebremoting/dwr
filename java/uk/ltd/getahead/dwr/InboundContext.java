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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

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
     * This method also check so see if the new variable is a parameter and if
     * it is it updates the count of parameters
     * @param callNum The call number to work on
     * @param key The name of the variable
     * @param type The javascript type of the variable
     * @param value The value of the variable
     */
    public void createInboundVariable(int callNum, String key, String type, String value)
    {
        InboundVariable cte = new InboundVariable(this, type, value);

        variables.put(key, cte);

        String paramPrefix = ConversionConstants.INBOUND_CALLNUM_PREFIX + callNum +
                             ConversionConstants.INBOUND_CALLNUM_SUFFIX +
                             ConversionConstants.INBOUND_KEY_PARAM;

        if (key.startsWith(paramPrefix))
        {
            int i = Integer.parseInt(key.substring(paramPrefix.length())) + 1;
            if (i > paramCount )
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
     * @param bean The converted version
     */
    public void addConverted(InboundVariable iv, Object bean)
    {
        converted.put(iv, bean);
    }

    /**
     * Check to see if the conversion has already been done
     * @param iv The inbound data to check
     * @return The converted data or null if it has not been converted
     */
    public Object getConverted(InboundVariable iv)
    {
        return converted.get(iv);
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
        String prefix = ConversionConstants.INBOUND_CALLNUM_PREFIX + callNum + ConversionConstants.INBOUND_CALLNUM_SUFFIX + ConversionConstants.INBOUND_KEY_PARAM;
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
        String key = ConversionConstants.INBOUND_CALLNUM_PREFIX + callNum +
                     ConversionConstants.INBOUND_CALLNUM_SUFFIX +
                     ConversionConstants.INBOUND_KEY_PARAM + index;

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
}
