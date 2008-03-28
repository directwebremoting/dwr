package uk.ltd.getahead.dwr;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class OutboundContext
{
    /**
     * Create a ConversionContext which stores context information about a set
     * of conversions from Java to Javascript.
     */
    public OutboundContext()
    {
    }

    /**
     * Have we already converted an object?
     * @param object The object to check
     * @return How it was converted last time or null if we've not seen it before
     */
    public OutboundVariable get(Object object)
    {
        return (OutboundVariable) map.get(object);
    }

    /**
     * @param object We have converted a new object, remember it
     * @param ss How the object was converted
     */
    public void put(Object object, OutboundVariable ss)
    {
        map.put(object, ss);
    }

    /**
     * Create a new variable name to keep everything we declare separate
     * @return A new unique variable name
     */
    public String getNextVariableName()
    {
        String varName = "s" + nextVarIndex;
        nextVarIndex++;

        return varName;
    }

    /**
     * The map of objects to how we converted them last time
     */
    private Map map = new HashMap();

    /**
     * What index do we tack on the next variable name that we generate
     */
    private int nextVarIndex = 0;
}

