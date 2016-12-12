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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An {@link OutboundVariable} that we know to be unable to recurse
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class NonNestedOutboundVariable implements OutboundVariable
{
    /**
     * Create a new NonNestedOutboundVariable
     */
    public NonNestedOutboundVariable(String assignCode)
    {
        this.assignCode = assignCode;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#incrementReferenceCount()
     */
    public void incrementReferenceCount()
    {
        referenceCount++;

        if (referenceCount > 1)
        {
            log.warn("Creating JsonString from multiply referenced ArrayJsonOutboundVariable. Recurrsion expected.");
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getDeclareCode()
     */
    public String getDeclareCode()
    {
        return "";
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getBuildCode()
     */
    public String getBuildCode()
    {
        return "";
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getAssignCode()
     */
    public String getAssignCode()
    {
        return assignCode;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getReference()
     */
    public OutboundVariable getReferenceVariable()
    {
        return this;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "NonNestedOutboundVariable(" + getAssignCode() + ")";
    }

    /**
     * The variable that we refer to
     */
    private final String assignCode;

    /**
     * By how many objects are we referred to?
     */
    private int referenceCount = 0;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(NonNestedOutboundVariable.class);
}
