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
     * Things work out if they are doubly referenced during the conversion
     * process, and can't be sure how to create output until that phase is done.
     * This method declares that we are done conversion, and now is a good time
     * to calculate how to generate output
     */
    public void prepareForOutput()
    {
        for (OutboundVariable variable : map.values())
        {
            variable.prepareAssignCode();
        }

        for (OutboundVariable variable : map.values())
        {
            variable.prepareBuildDeclareCodes();
        }
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
