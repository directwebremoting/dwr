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

import java.util.Collection;

/**
 * A helper class for people that want to implement {@link OutboundVariable}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class NonJsonNestedOutboundVariable implements OutboundVariable
{
    /**
     * @param outboundContext the OutboundContext to set
     */
    protected NonJsonNestedOutboundVariable(OutboundContext outboundContext)
    {
        this.outboundContext = outboundContext;
    }

    /**
     * We might want to force us into predefined mode.
     */
    protected void forceOutline()
    {
        // if we are in JSON mode then we are not allowed to be non-inline.
        if (outboundContext.isJsonMode())
        {
            throw new JsonModeMarshallException(NonJsonNestedOutboundVariable.class, "Outline in JSON mode is illegal");
        }

        this.outline = true;
    }

    /**
     * @return the outline
     */
    public boolean isOutline()
    {
        return outline;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getDeclareCode()
     */
    public String getDeclareCode()
    {
        if (declareCode == null || children == null)
        {
            throw new NullPointerException();
        }

        return declareCode + getChildDeclareCodes();
    }

    /**
     * @param declareCode the declareCode to set
     */
    public void setBaseDeclareCode(String declareCode)
    {
        this.declareCode = declareCode;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getBuildCode()
     */
    public String getBuildCode()
    {
        if (declareCode == null || children == null)
        {
            throw new NullPointerException();
        }

        return buildCode + getChildBuildCodes();
    }

    /**
     * @param buildCode the buildCode to set
     */
    public void setBaseBuildCode(String buildCode)
    {
        this.buildCode = buildCode;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getAssignCode()
     */
    public String getAssignCode()
    {
        return assignCode;
    }

    /**
     * @param assignCode the assignCode to set
     */
    public void setAssignCode(String assignCode)
    {
        this.assignCode = assignCode;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getReference()
     */
    public OutboundVariable getReferenceVariable()
    {
        if (reference == null)
        {
            reference = new NonNestedOutboundVariable(getVariableName());
            forceOutline();
        }

        return reference;
    }

    /**
     * In JSON mode we need to recursively prepare assign codes because the
     * order in which they are created matters.
     */
    protected void prepareChildAssignCodes()
    {
        for (OutboundVariable child : children)
        {
            child.prepareAssignCode();
        }
    }

    /**
     * Grab all the build codes together
     * @return A build string
     */
    private String getChildBuildCodes()
    {
        if (children == null)
        {
            return "";
        }

        StringBuffer buffer = new StringBuffer();

        // Make sure the nested things are declared
        for (OutboundVariable nested : children)
        {
            buffer.append(nested.getBuildCode());
        }

        return buffer.toString();
    }

    /**
     * Grab all the declare codes together
     * @return A declare string
     */
    private String getChildDeclareCodes()
    {
        if (children == null)
        {
            return "";
        }

        StringBuffer buffer = new StringBuffer();

        // Make sure the nested things are declared
        for (OutboundVariable nested : children)
        {
            buffer.append(nested.getDeclareCode());
        }

        return buffer.toString();
    }

    /**
     * @return the varName
     */
    protected String getVariableName()
    {
        if (varName == null)
        {
            varName = outboundContext.getNextVariableName();
        }

        return varName;
    }

    /**
     * When we create buildCode and declareCode, what do we need to add?
     * @param children The list of dependent {@link OutboundVariable}s
     */
    public void setChildren(Collection<OutboundVariable> children)
    {
        this.children = children;
    }

    /**
     * Does anything refer to us?
     */
    private OutboundVariable reference;

    /**
     * Are we known to be recursive
     */
    private boolean outline = false;

    /**
     * The code to be executed to do basic initialization
     */
    private String declareCode;

    /**
     * The code to be executed to setup the data structure
     */
    private String buildCode;

    /**
     * The code to use to refer to this data structure
     */
    private String assignCode;

    /**
     * If we get recursive, this is the variable name we declare
     */
    private String varName;

    /**
     * When we create buildCode and declareCode, what do we need to add?
     */
    protected Collection<OutboundVariable> children;

    /**
     * The conversion context
     */
    private OutboundContext outboundContext;
}
