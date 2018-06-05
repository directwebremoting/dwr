package org.directwebremoting.extend;

import java.util.Collection;

/**
 * A helper for implementing OutboundVariable when you have children
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class NestedOutboundVariable implements OutboundVariable
{
    /**
     * OutboundVariable may need to create variables or deny outlining
     */
    protected NestedOutboundVariable(OutboundContext context)
    {
        this.context = context;
    }

    /**
     * We setup the children later than construction time so we can check for
     * recursive references.
     */
    public void setChildren(Collection<OutboundVariable> children)
    {
        this.children = children;
    }

    /**
     * The objects that we contain
     */
    public Collection<OutboundVariable> getChildren()
    {
        return children;
    }

    /**
     * @return The {@link #getDeclareCode()}s of all our children
     */
    protected String getChildDeclareCodes()
    {
        StringBuilder builder = new StringBuilder();

        for (OutboundVariable child : children)
        {
            builder.append(child.getDeclareCode());
        }

        return builder.toString();
    }

    /**
     * @return The {@link #getBuildCode()}s of all our children
     */
    protected String getChildBuildCodes()
    {
        StringBuilder builder = new StringBuilder();

        for (OutboundVariable child : children)
        {
            builder.append(child.getBuildCode());
        }

        return builder.toString();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.OutboundVariable#getReference()
     */
    public OutboundVariable getReferenceVariable()
    {
        if (reference == null)
        {
            reference = new NonNestedOutboundVariable(getVariableName());
        }

        referenced = true;
        return reference;
    }

    /**
     * If outline then we need a variable so we can be referred to
     */
    protected String getVariableName()
    {
        if (varName == null)
        {
            varName = context.getNextVariableName();
        }

        return varName;
    }

    /**
     * Is there something (like JSON mode) that forces us to inline or maybe we
     * are referred to by multiple other things
     */
    protected boolean isInline()
    {
        return context.isJsonMode() || !referenced;
    }

    /**
     * Are we in JSON mode, and therefore must quote property names?
     */
    public boolean isJsonMode()
    {
        return context.isJsonMode();
    }

    /**
     * Are there references to us (forcing outline)?
     */
    private boolean referenced = false;

    /**
     * When we create buildCode and declareCode, what do we need to add?
     */
    private Collection<OutboundVariable> children;

    /**
     * Does anything refer to us?
     */
    private OutboundVariable reference;

    /**
     * If we get recursive, this is the variable name we declare
     */
    private String varName;

    /**
     * The conversion context, from which we get variable names
     */
    private final OutboundContext context;
}
