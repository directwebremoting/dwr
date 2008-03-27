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
import java.util.Map;

/**
 * We need to keep track of stuff while we are converting on the way out to
 * prevent recurrsion.
 * This class helps track the conversion process.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class OutboundContext
{
    /**
     * Since map needs to have referencial equality rather than object equality
     * this constructor tries to use java.util.IdentityHashMap (>=1.4), and
     * failing that falls back on wrapper objects in a HashMap.
     */
    public OutboundContext()
    {
        // We can only assign to map once, so we use this as a staging post.
        Map assign;

        try
        {
            assign = (Map) Class.forName("java.util.IdentityHashMap").newInstance(); //$NON-NLS-1$
            referenceWrappers = false;
        }
        catch (Exception ex)
        {
            assign = new HashMap();
            referenceWrappers = true;
        }

        map = assign;
    }

    /**
     * Have we already converted an object?
     * @param object The object to check
     * @return How it was converted last time or null if we've not seen it before
     */
    public OutboundVariable get(Object object)
    {
        if (referenceWrappers)
        {
            object = new ReferenceWrapper(object);
        }

        return (OutboundVariable) map.get(object);
    }

    /**
     * @param object We have converted a new object, remember it
     * @param ss How the object was converted
     */
    public void put(Object object, OutboundVariable ss)
    {
        if (referenceWrappers)
        {
            object = new ReferenceWrapper(object);
        }

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
     * The prefix for outbound variable names the we generate
     */
    public static final String OUTBOUND_VARIABLE_PREFIX = "s"; //$NON-NLS-1$

    /**
     * The map of objects to how we converted them last time
     */
    private final Map map;

    /**
     * Tells if we are to wrap objects in the map to get referencial equality.
     */
    private boolean referenceWrappers;

    /**
     * What index do we tack on the next variable name that we generate
     */
    private int nextVarIndex = 0;

    /**
     * Wrapper class that makes sure that equals() and hashCode() uses
     * referencial equality on the wrapped object. This is used when
     * we can't have a IdentityHashMap in map.
     */
    private static class ReferenceWrapper
    {
        /**
         * @param object The object to wrap
         */
        private ReferenceWrapper(Object object)
        {
            this.object = object;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        public int hashCode()
        {
            return System.identityHashCode(object);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj)
        {
            if (!(obj instanceof ReferenceWrapper))
            {
                return false;
            }

            ReferenceWrapper that = (ReferenceWrapper) obj;
            return this.object == that.object;
        }

        /**
         * My wrapped object.
         */
        private Object object;
    }
}
