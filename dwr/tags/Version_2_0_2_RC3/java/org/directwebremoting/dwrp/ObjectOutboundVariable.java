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

import java.util.Iterator;
import java.util.Map;

import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.util.LocalUtil;

/**
 * An OutboundVariable that creates data from Maps.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ObjectOutboundVariable extends AbstractOutboundVariable implements OutboundVariable
{
    /**
     * Setup
     * @param outboundContext A collection of objects already converted and the results
     */
    public ObjectOutboundVariable(OutboundContext outboundContext)
    {
        super(outboundContext);
    }

    /**
     * Generate an map declaration for a map of Outbound variables
     * @param aOvs The map of the converted contents
     * @param aScriptClassName The object name or null for pure(ish) json
     */
    public void init(Map aOvs, String aScriptClassName)
    {
        this.ovs = aOvs;
        this.scriptClassName = aScriptClassName;

        isNamed = (scriptClassName != null && !scriptClassName.equals(""));
        if (isNamed)
        {
            forceInline(false);
        }

        setChildren(ovs.values());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.AbstractOutboundVariable#getNotInlineDefinition()
     */
    protected NotInlineDefinition getNotInlineDefinition()
    {
        String declareCode;
        if (!isNamed)
        {
            declareCode =  "var " + getVariableName() + "={};";
        }
        else
        {
            declareCode = "var " + getVariableName() + "=new " + scriptClassName + "();";
        }

        StringBuffer buildCode = new StringBuffer();
        for (Iterator it = ovs.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            OutboundVariable nested = (OutboundVariable) entry.getValue();

            String nestedAssignCode = nested.getAssignCode();
            String varName = getVariableName();

            // The semi-compact syntax is only any good for simple names
            // I dont think we need this check:  && !isRecursive()
            if (LocalUtil.isSimpleName(name))
            {
                buildCode.append(varName);
                buildCode.append('.');
                buildCode.append(name);
                buildCode.append('=');
                buildCode.append(nestedAssignCode);
                buildCode.append(';');
            }
            else
            {
                buildCode.append(varName);
                buildCode.append("['");
                buildCode.append(name);
                buildCode.append("']=");
                buildCode.append(nestedAssignCode);
                buildCode.append(';');
            }
        }
        buildCode.append("\r\n");

        return new NotInlineDefinition(declareCode, buildCode.toString());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.AbstractOutboundVariable#getInlineDefinition()
     */
    protected String getInlineDefinition()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append('{');

        boolean first = true;
        for (Iterator it = ovs.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            OutboundVariable nested = (OutboundVariable) entry.getValue();

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
    public String toString()
    {
        return "Object:" + toStringDefinitionHint() + ":" + ovs;
    }

    /**
     * Are we named (or does {@link #scriptClassName} have some contents)
     */
    private boolean isNamed;

    /**
     * The contained variables
     */
    private Map ovs;

    /**
     * The name of this typed class if there is one
     */
    private String scriptClassName;
}
