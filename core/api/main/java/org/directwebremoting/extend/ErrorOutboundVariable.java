package org.directwebremoting.extend;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.util.LocalUtil;

/**
 * An OutboundVariable that can not be recursive.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ErrorOutboundVariable extends NonNestedOutboundVariable
{
    /**
     * Default ctor that leaves blank members
     * @param errorMessage Some message for the developer to see.
     */
    public ErrorOutboundVariable(String errorMessage)
    {
        super(sanitizeErrorMessage(errorMessage));
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#getAssignCode()
     */
    public static String sanitizeErrorMessage(String errorMessage)
    {
        boolean debug = false;

        Object debugVal = WebContextFactory.get().getContainer().getBean("debug");
        if (debugVal != null)
        {
            debug = LocalUtil.simpleConvert(debugVal.toString(), Boolean.class);
        }

        if (debug)
        {
            return "null /* " + errorMessage.replace("*/", "* /") + " */";
        }
        else
        {
            return "null";
        }
    }
}
