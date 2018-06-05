package org.directwebremoting.extend;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;

/**
 * InboundContext is the context for set of inbound conversions.
 * Since a data set may be recursive parts of some data members may refer to
 * others so we need to keep track of who is converted for what.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class InboundContext
{
    /**
     * When we are sure we have finished parsing the input, we can begin to
     * fix all cross-references.
     * @throws ConversionException If cross-references don't add up
     */
    public void dereference() throws ConversionException
    {
        for (InboundVariable variable : variables.values())
        {
            variable.dereference();
        }
    }

    /**
     * Someone wants to tell us about a new conversion context.
     * @param context The current conversion context
     */
    public void pushContext(Property context)
    {
        typeHintStack.addFirst(context);
    }

    /**
     * Someone wants to tell us about a finished conversion context.
     */
    public void popContext()
    {
        typeHintStack.removeFirst();
    }

    /**
     * @return The method that we are currently converting data for
     */
    public Property getCurrentProperty()
    {
        return typeHintStack.getFirst();
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
        checkInboundVariable(callNum, key, cte);
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
     * @param has value been URL decoded?
     */
    public void createInboundVariable(int callNum, String key, String type, String value, boolean urlDecoded)
    {
        InboundVariable cte = new InboundVariable(this, key, type, value, urlDecoded);
        checkInboundVariable(callNum, key, cte);
    }

    /**
     * Create an inbound file variable.
     * Usually called by a query parser to setup a list of known variables.
     * This method also checks to see if the new variable is a parameter and if
     * it is it updates the count of parameters
     * @param callNum The call number to work on
     * @param key The name of the variable
     * @param type The javascript type of the variable
     * @param value The value of the file
     */
    public void createInboundVariable(int callNum, String key, String type, FormField value)
    {
        InboundVariable iv = new InboundVariable(this, key, type, value);
        checkInboundVariable(callNum, key, iv);
    }

    /**
     * Internal method to check the variable we just created does not already
     * exist, and to ensure that our count of inbound parameters is up to date
     * @param callNum The number of this call
     * @param key The name of the variable
     * @param iv The value to check
     */
    private void checkInboundVariable(int callNum, String key, InboundVariable iv)
    {
        Object old = variables.put(key, iv);
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
     * Method to allow entries to resolve references
     * @param name The name of the variable to lookup
     * @return The found variable
     */
    public InboundVariable getInboundVariable(String name)
    {
        return variables.get(name);
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
    public void addConverted(InboundVariable iv, Class<?> type, Object bean)
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
    public Object getConverted(InboundVariable iv, Class<?> type)
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
     * Count the arguments (including method and script params) for a given call
     * number.
     * @param callNum The Call number to count the parameters of
     * @return The parameter count for a given Call
     */
    public int getParameterCount(int callNum)
    {
        int count = 0;
        String prefix = ProtocolConstants.INBOUND_CALLNUM_PREFIX + callNum + ProtocolConstants.INBOUND_CALLNUM_SUFFIX + ProtocolConstants.INBOUND_KEY_PARAM;
        for (String key : variables.keySet())
        {
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

        InboundVariable found = variables.get(key);
        if (found != null)
        {
            return found;
        }

        return nullInboundVariable;
    }

    /**
     * This is very nasty - we need to create an array for varargs. The correct
     * solution is to have ArrayConverter (and MapConverter) part of dwrp and
     * not implementations of Converter. This would resolve a number of problems
     * however for now we will have to create a mock InboundVariable to make it
     * look to our ArrayConverter that the client passed in an array all along
     * @param callNum The call number to work on
     * @param destParamCount The number of parameters to the method that we are
     * calling, from which we can work out the size of the varargs array
     */
    public InboundVariable createArrayWrapper(int callNum, int destParamCount)
    {
        int inputParamCount = getParameterCount(callNum);
        int varArgArraySize = 1 + inputParamCount - destParamCount;

        InboundVariable[] members = new InboundVariable[varArgArraySize];
        int varArgsParamStartIndex = destParamCount - 1; // The varargs param has to be last
        for (int i = 0; i < varArgArraySize; i++)
        {
            members[i] = getParameter(callNum, varArgsParamStartIndex);
            varArgsParamStartIndex++;
        }

        return new InboundVariable(this, members);
    }

    /**
     * A debug method so people can get a list of all the variable names
     * @return an iterator over the known variable names
     */
    public Iterator<String> getInboundVariableNames()
    {
        return variables.keySet().iterator();
    }

    /**
     * A Class to use as a key in a map for conversion purposes.
     * A collection of an InboundVariable and a type
     */
    protected static class Conversion
    {
        /**
         * @param inboundVariable The new inboundVariable
         * @param type The new type
         */
        Conversion(InboundVariable inboundVariable, Class<?> type)
        {
            if (inboundVariable == null)
            {
                throw new NullPointerException("InboundVariable");
            }

            if (type == null)
            {
                throw new NullPointerException("Class type");
            }

            this.inboundVariable = inboundVariable;
            this.type = type;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj)
        {
            if (!(obj instanceof Conversion))
            {
                return false;
            }

            Conversion that = (Conversion) obj;

            return this.type.equals(that.type) && this.inboundVariable.equals(that.inboundVariable);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode()
        {
            return inboundVariable.hashCode() + type.hashCode();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return "Conversion[" + inboundVariable + "," + type.getName() + "]";
        }

        private final InboundVariable inboundVariable;

        private final Class<?> type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("InboundContext[");
        for (Map.Entry<String, InboundVariable> entry : variables.entrySet())
        {
            buffer.append(entry.getKey());
            buffer.append('=');
            buffer.append(entry.getValue());
            buffer.append(',');
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * A variable to use if we need to tell someone that we got nothing.
     */
    private final InboundVariable nullInboundVariable = new InboundVariable(this);

    /**
     * The stack of pushed conversion contexts.
     * i.e. What is the context of this type conversion.
     */
    private final LinkedList<Property> typeHintStack = new LinkedList<Property>();

    /**
     * How many params are there?.
     * To be more accurate, return one less than the highest numbered parameter
     * that we have come across.
     */
    private int paramCount = 0;

    /**
     * A map of all the inbound variables
     */
    private final Map<String, InboundVariable> variables = new HashMap<String, InboundVariable>();

    /**
     * A map of all the variables converted.
     */
    private final Map<Conversion, Object> converted = new HashMap<Conversion, Object>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(InboundContext.class);

}
