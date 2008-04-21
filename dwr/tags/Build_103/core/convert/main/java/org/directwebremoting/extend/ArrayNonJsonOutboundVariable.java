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
 * An OutboundVariable that creates data from Collections.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ArrayNonJsonOutboundVariable extends NonJsonNestedOutboundVariable implements CollectionOutboundVariable
{
    /**
     * Setup
     * @param outboundContext A collection of objects already converted and the results
     */
    public ArrayNonJsonOutboundVariable(OutboundContext outboundContext)
    {
        super(outboundContext);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#prepareAssignCode()
     */
    public void prepareAssignCode()
    {
        if (isOutline())
        {
            setAssignCode(getVariableName());
        }
        else
        {
            prepareChildAssignCodes();
            setAssignCode(ArrayJsonOutboundVariable.createJsonString(children));
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#prepareBuildDeclareCodes()
     */
    public void prepareBuildDeclareCodes()
    {
        if (isOutline())
        {
            StringBuffer buffer = new StringBuffer();

            int i = 0;
            String variableName = getVariableName();
            for (OutboundVariable child : children)
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

            setBaseDeclareCode("var " + variableName + "=[];");
            setBaseBuildCode(buffer.toString());
        }
        else
        {
            setBaseDeclareCode("");
            setBaseBuildCode("");
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ArrayNonJsonOutboundVariable:" + children;
    }
}
