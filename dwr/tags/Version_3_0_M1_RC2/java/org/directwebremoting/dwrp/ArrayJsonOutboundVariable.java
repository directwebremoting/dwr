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

import java.util.Collection;

import org.directwebremoting.extend.OutboundVariable;

/**
 * An OutboundVariable that creates data from Collections.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ArrayJsonOutboundVariable extends JsonNestedOutboundVariable implements CollectionOutboundVariable
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.CollectionOutboundVariable#setChildren(java.util.List)
     */
    public void setChildren(Collection<OutboundVariable> children)
    {
        this.children = children;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#getAssignCode()
     */
    public String getAssignCode()
    {
        return createJsonString(children);
    }

    /**
     * JSON strings sometimes need to be created in a non JSON mode too, so
     * this method is shared between {@link ArrayJsonOutboundVariable} and it's
     * brother {@link ArrayNonJsonOutboundVariable}.
     * @return A JSON string that represents the set of {@link OutboundVariable}s
     */
    protected static String createJsonString(Collection<OutboundVariable> children)
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[");

        boolean first = true;
        for (OutboundVariable child : children)
        {
            if (!first)
            {
                buffer.append(',');
            }

            buffer.append(child.getAssignCode());

            first = false;
        }
        buffer.append("]");

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ArrayJsonOutboundVariable:" + children;
    }

    /**
     * The contained variables
     */
    private Collection<OutboundVariable> children = null;
}
