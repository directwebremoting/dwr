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
import java.util.Iterator;

import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

/**
 * A helper class for people that want to implement {@link OutboundVariable}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractOutboundVariable implements OutboundVariable
{
    /**
     * @param outboundContext the OutboundContext to set
     */
    protected AbstractOutboundVariable(OutboundContext outboundContext)
    {
        this.outboundContext = outboundContext;
    }

    /**
     * We might want to force us into predefined mode.
     * @param inlineStatus The inline status to force
     */
    protected void forceInline(boolean inlineStatus)
    {
        setInline(inlineStatus);
        forcedInlineStatus = true;
    }

    /**
     * @param children the dependent children of this variable
     */
    protected void setChildren(Collection children)
    {
        this.children = children;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getDeclareCode()
     */
    public String getDeclareCode()
    {
        if (!calculated)
        {
            calculate();
        }

        if (inline)
        {
            return getChildDeclareCodes();
        }
        else
        {
            return notInlineDefinition.declareCode + getChildDeclareCodes();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getBuildCode()
     */
    public String getBuildCode()
    {
        if (!calculated)
        {
            calculate();
        }

        if (inline)
        {
            return getChildBuildCodes();
        }
        else
        {
            return notInlineDefinition.buildCode + getChildBuildCodes();
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getAssignCode()
     */
    public String getAssignCode()
    {
        if (calculated)
        {
            if (inline)
            {
                return assignCode;
            }
            else
            {
                return varName;
            }
        }
        else
        {
            // Someone is asking our name before we are calculated? This is
            // likely to be because we are nested. calling calculate() is not
            // possible because there might not be the data to make that work
            // so we name ourselves, and return that.
            if (forcedInlineStatus)
            {
                if (inline)
                {
                    return getInlineDefinition();
                }
                else
                {
                    return getVariableName();
                }
            }
            else
            {
                // log.debug("Moving outline. Do we need to? " + this);
                setInline(false);
                return getVariableName();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getReference()
     */
    public OutboundVariable getReferenceVariable()
    {
        if (reference == null)
        {
            reference = new ReferenceOutboundVariable(getVariableName());
            if (forcedInlineStatus && inline)
            {
                throw new IllegalStateException("Ignoring request to inline on reference for: " + this);
            }
            else
            {
                setInline(false);
            }
        }

        return reference;
    }

    /**
     * Called at the last moment as the outputs are being read when we are
     * sure if we are a reference or not.
     */
    private void calculate()
    {
        if (inline)
        {
            assignCode = getInlineDefinition();
        }
        else
        {
            notInlineDefinition = getNotInlineDefinition();
        }

        calculated = true;
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
        for (Iterator it = children.iterator(); it.hasNext();)
        {
            OutboundVariable nested = (OutboundVariable) it.next();
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
        for (Iterator it = children.iterator(); it.hasNext();)
        {
            OutboundVariable nested = (OutboundVariable) it.next();
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
     * Define the definition we should use if we are being used not inline
     * @return an out of line definition
     */
    protected abstract NotInlineDefinition getNotInlineDefinition();

    /**
     * Define the definition we should use if we are being used inline
     * @return an inline definition
     */
    protected abstract String getInlineDefinition();

    /**
     * A helper struct to pass a build code and define code together
     */
    protected class NotInlineDefinition
    {
        protected NotInlineDefinition(String declareCode, String buildCode)
        {
            this.declareCode = declareCode;
            this.buildCode = buildCode;
        }

        /**
         * The code to be executed to do basic initialization
         */
        String declareCode;

        /**
         * The code to be executed to setup the data structure
         */
        String buildCode;
    }

    /**
     * A helper to children get have definition info in {@link #toString()}
     * @return For children to use in {@link #toString()}
     */
    protected String toStringDefinitionHint()
    {
        if (inline)
        {
            return "inline";
        }
        else
        {
            if (varName != null)
            {
                return varName;
            }
            else
            {
                return "?";
            }
        }
    }

    /**
     * @param isInline The new inline status
     */
    private void setInline(boolean isInline)
    {
        if (calculated)
        {
            throw new IllegalStateException("Attempt to change inline status after calculation");
        }

        this.inline = isInline;
    }

    /**
     * Does anything refer to us?
     */
    private OutboundVariable reference;

    /**
     * Are we known to be recursive
     */
    private boolean inline = true;

    /**
     * Have we forced an inline/outline status?
     */
    private boolean forcedInlineStatus = false;

    /**
     * Has calculate been run?
     */
    private boolean calculated = false;

    /**
     * The init code for the non-inline case
     */
    private NotInlineDefinition notInlineDefinition;

    /**
     * The code to be executed to get the value of the initialized data
     */
    private String assignCode;

    /**
     * If we get recursive, this is the variable name we declare
     */
    private String varName;

    /**
     * The conversion context
     */
    private OutboundContext outboundContext;

    /**
     * The OutboundVariables that we depend on
     */
    private Collection children;
}
