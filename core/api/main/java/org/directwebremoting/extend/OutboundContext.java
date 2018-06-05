package org.directwebremoting.extend;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * We need to keep track of stuff while we are converting on the way out to
 * prevent recursion.
 * This class helps track the conversion process.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class OutboundContext
{
    /**
     * Contexts need to know if they are producing JSON output
     * @param jsonMode Are we producing JSON output?
     */
    public OutboundContext(boolean jsonMode)
    {
        this.jsonMode = jsonMode;
    }

    /**
     * Have we already converted an object?
     * @param object The object to check
     * @return How it was converted last time or null if we've not seen it before
     */
    public OutboundVariable get(Object object)
    {
        return map.get(object);
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
        String varName = OUTBOUND_VARIABLE_PREFIX + nextVarIndex;
        nextVarIndex++;

        return varName;
    }

    /**
     * @return Are we in JSON mode where everything is inline?
     */
    public boolean isJsonMode()
    {
        return jsonMode;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return map.toString();
    }

    /**
     * The prefix for outbound variable names the we generate
     */
    private static final String OUTBOUND_VARIABLE_PREFIX = "s";

    /**
     * The map of objects to how we converted them last time
     */
    private final Map<Object, OutboundVariable> map = new IdentityHashMap<Object, OutboundVariable>();

    /**
     * What index do we tack on the next variable name that we generate
     */
    private int nextVarIndex = 0;

    /**
     * Are we in JSON mode where everything is inline?
     */
    private boolean jsonMode = true;
}
