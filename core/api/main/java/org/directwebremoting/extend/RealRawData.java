package org.directwebremoting.extend;

import org.directwebremoting.io.RawData;

/**
 * Sometimes DWR can't know at runtime the type of the inbound data, this class
 * allows us to defer conversion until we know more about the type we should
 * be converting to.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class RealRawData implements RawData
{
    /**
     * RawData is immutable. Setup the raw string data that we wrap
     * @param inboundContext The context variables that could be associated with this
     * @param inboundVariable The variable we are delaying marshalling
     */
    public RealRawData(InboundVariable inboundVariable, InboundContext inboundContext)
    {
        this.inboundVariable = inboundVariable;
        this.inboundContext = inboundContext;
    }

    /**
     * @return The context variables that could be associated with this
     */
    public InboundContext getInboundContext()
    {
        return inboundContext;
    }

    /**
     * @return The variable we are delaying marshalling
     */
    public InboundVariable getInboundVariable()
    {
        return inboundVariable;
    }

    /**
     * The context variables that could be associated with this
     */
    private final InboundContext inboundContext;

    /**
     * The variable we are delaying marshalling
     */
    private final InboundVariable inboundVariable;
}
