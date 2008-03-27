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
package org.directwebremoting.dwrp;

import java.util.List;

import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

/**
 * An OutboundVariable that creates data from Collections.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ArrayOutboundVariable extends AbstractOutboundVariable implements OutboundVariable
{
    /**
     * Setup
     * @param outboundContext A collection of objects already converted and the results
     */
    public ArrayOutboundVariable(OutboundContext outboundContext)
    {
        super(outboundContext);
    }

    /**
     * Generate an array declaration for a list of Outbound variables
     * @param aOvs The list of contents of this array
     */
    public void init(List aOvs)
    {
        this.ovs = aOvs;
        setChildren(ovs);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.AbstractOutboundVariable#getNotInlineDefinition()
     */
    protected NotInlineDefinition getNotInlineDefinition()
    {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < ovs.size(); i++)
        {
            OutboundVariable nested = (OutboundVariable) ovs.get(i);
            String varName = getVariableName();

            if (nested != null)
            {
                buffer.append(varName);
                buffer.append('[');
                buffer.append(i);
                buffer.append("]=");
                buffer.append(nested.getAssignCode());
                buffer.append(';');
            }
        }
        buffer.append("\r\n");

        return new NotInlineDefinition("var " + getVariableName() + "=[];", buffer.toString());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.AbstractOutboundVariable#getInlineDefinition()
     */
    protected String getInlineDefinition()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[");

        boolean first = true;
        for (int i = 0; i < ovs.size(); i++)
        {
            OutboundVariable ov = (OutboundVariable) ovs.get(i);

            if (!first)
            {
                buffer.append(',');
            }

            buffer.append(ov.getAssignCode());

            first = false;
        }
        buffer.append("]");

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "Array:" + toStringDefinitionHint() + ":" + ovs;
    }

    /**
     * The contained variables
     */
    private List ovs;
}
