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

/**
 * An OutboundVariable that declares a JavaScript array
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ArrayOutboundVariable extends NestedOutboundVariable
{
    /**
     * Constructor
     */
    public ArrayOutboundVariable(OutboundContext context)
    {
        super(context);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#getDeclareCode()
     */
    public String getDeclareCode()
    {
        if (isInline())
        {
            return getChildDeclareCodes();
        }
        else
        {
            return getChildDeclareCodes() + "var " + getVariableName() + "=[];";
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#getBuildCode()
     */
    public String getBuildCode()
    {
        if (isInline())
        {
            return getChildBuildCodes();
        }
        else
        {
            StringBuffer buffer = new StringBuffer(getChildBuildCodes());

            int i = 0;
            String variableName = getVariableName();
            for (OutboundVariable child : getChildren())
            {
                if (child != null)
                {
                    buffer.append(variableName);
                    buffer.append('[');
                    buffer.append(i);
                    buffer.append("]=");
                    buffer.append(child.getAssignCode());
                    buffer.append(';');
                }

                i++;
            }
            buffer.append("\r\n");

            return buffer.toString();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#getAssignCode()
     */
    public String getAssignCode()
    {
        if (isInline())
        {
            StringBuffer buffer = new StringBuffer();

            buffer.append("[");

            boolean first = true;
            for (OutboundVariable child : getChildren())
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
        else
        {
            return getVariableName();
        }
    }
}
