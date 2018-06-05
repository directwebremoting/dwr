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
