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
public class ObjectNonJsonOutboundVariable extends NonJsonNestedOutboundVariable implements MapOutboundVariable
{
    /**
     * Setup
     * @param outboundContext A collection of objects already converted and the results
     * @param aScriptClassName The object name or null for pure(ish) json
     */
    public ObjectNonJsonOutboundVariable(OutboundContext outboundContext, String aScriptClassName)
    {
        super(outboundContext);
        this.scriptClassName = aScriptClassName;

        isNamed = (scriptClassName != null && !"".equals(scriptClassName));
        if (isNamed)
        {
            forceOutline();
        }
    }

    /**
     * Generate an map declaration for a map of Outbound variables
     * @param children The map of the converted contents
     */
    public void setChildren(Map<String, OutboundVariable> children)
    {
        setChildren(children.values());
        this.childMap = children;
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
            setAssignCode(ObjectJsonOutboundVariable.createJsonString(childMap));
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#prepareBuildDeclareCodes()
     */
    public void prepareBuildDeclareCodes()
    {
        if (isOutline())
        {
            String declareCode;
            String variableName = getVariableName();
            if (!isNamed)
            {
                declareCode =  "var " + variableName + "={};";
            }
            else
            {
                declareCode = "var " + variableName + "=new " + scriptClassName + "();";
            }

            StringBuffer buildCode = new StringBuffer();
            for (Map.Entry<String, OutboundVariable> entry : childMap.entrySet())
            {
                String name = entry.getKey();
                OutboundVariable nested = entry.getValue();

                String nestedAssignCode = nested.getAssignCode();

                // The semi-compact syntax is only any good for simple names
                if (LocalUtil.isSimpleName(name))
                {
                    buildCode.append(variableName);
                    buildCode.append('.');
                    buildCode.append(name);
                    buildCode.append('=');
                    buildCode.append(nestedAssignCode);
                    buildCode.append(';');
                }
                else
                {
                    buildCode.append(variableName);
                    buildCode.append("['");
                    buildCode.append(name);
                    buildCode.append("']=");
                    buildCode.append(nestedAssignCode);
                    buildCode.append(';');
                }
            }
            buildCode.append("\r\n");

            setBaseDeclareCode(declareCode);
            setBaseBuildCode(buildCode.toString());
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
        return "ObjectNonJsonOutboundVariable:" + childMap;
    }

    /**
     * Are we named (or does {@link #scriptClassName} have some contents)
     */
    private boolean isNamed;

    /**
     * The contained variables
     */
    private Map<String, OutboundVariable> childMap;

    /**
     * The name of this typed class if there is one
     */
    private String scriptClassName;
}
