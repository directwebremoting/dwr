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

import java.util.Map;

import org.directwebremoting.util.LocalUtil;

/**
 * An OutboundVariable that creates data from Maps.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ObjectJsonOutboundVariable extends JsonNestedOutboundVariable implements MapOutboundVariable
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.CollectionOutboundVariable#setChildren(java.lang.Object)
     */
    public void setChildren(Map<String, OutboundVariable> children)
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
     * this method is shared between {@link ObjectJsonOutboundVariable} and it's
     * brother {@link ObjectNonJsonOutboundVariable}.
     * @return A JSON string that represents the set of {@link OutboundVariable}s
     */
    protected static String createJsonString(Map<String, OutboundVariable> childList)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append('{');

        boolean first = true;
        for (Map.Entry<String, OutboundVariable> entry : childList.entrySet())
        {
            String name = entry.getKey();
            OutboundVariable nested = entry.getValue();

            String innerAssignCode = nested.getAssignCode();

            if (!first)
            {
                buffer.append(',');
            }

            // The compact JSON style syntax is only any good for simple names
            // and when we are not recursive
            if (LocalUtil.isSimpleName(name))
            {
                buffer.append(name);
                buffer.append(':');
                buffer.append(innerAssignCode);
            }
            else
            {
                buffer.append('\'');
                buffer.append(name);
                buffer.append("\':");
                buffer.append(innerAssignCode);
            }

            // we don't need to do this one the hard way
            first = false;
        }
        buffer.append('}');

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ObjectJsonOutboundVariable:" + children;
    }

    /**
     * The contained variables
     */
    private Map<String, OutboundVariable> children;
}
