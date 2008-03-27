package uk.ltd.getahead.dwr.convert;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.ExecutionContext;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.OutboundContext;

/**
 * A converter that magics up HTTP objects
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id: StringConverter.java,v 1.2 2004/11/04 15:54:07 joe_walker Exp $
 */
public class ServletConverter implements Converter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#init(uk.ltd.getahead.dwr.DefaultConfiguration)
     */
    public void setConverterManager(ConverterManager config)
    {
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertInbound(java.lang.Class, java.util.List, uk.ltd.getahead.dwr.InboundVariable, uk.ltd.getahead.dwr.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx)
    {
        if (HttpServletRequest.class.isAssignableFrom(paramType))
        {
            return ExecutionContext.get().getHttpServletRequest();
        }

        if (HttpServletResponse.class.isAssignableFrom(paramType))
        {
            return ExecutionContext.get().getHttpServletResponse();
        }

        if (ServletConfig.class.isAssignableFrom(paramType))
        {
            return ExecutionContext.get().getServletConfig();
        }

        if (ServletContext.class.isAssignableFrom(paramType))
        {
            return ExecutionContext.get().getServletContext();
        }

        if (HttpSession.class.isAssignableFrom(paramType))
        {
            return ExecutionContext.get().getSession(true);
        }

        return null;
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertOutbound(java.lang.Object, java.lang.String, uk.ltd.getahead.dwr.OutboundContext)
     */
    public String convertOutbound(Object data, String varname, OutboundContext outctx)
    {
        return "var " + varname + " = undefined;";  //$NON-NLS-1$ //$NON-NLS-2$
    }
}
