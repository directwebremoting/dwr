package org.directwebremoting.convert;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

/**
 * A converter that magics up HTTP objects
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ServletConverter extends AbstractConverter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data)
    {
        WebContext webcx = WebContextFactory.get();

        if (HttpServletRequest.class.isAssignableFrom(paramType))
        {
            return webcx.getHttpServletRequest();
        }

        if (HttpServletResponse.class.isAssignableFrom(paramType))
        {
            return webcx.getHttpServletResponse();
        }

        if (ServletConfig.class.isAssignableFrom(paramType))
        {
            return webcx.getServletConfig();
        }

        if (ServletContext.class.isAssignableFrom(paramType))
        {
            return webcx.getServletContext();
        }

        if (HttpSession.class.isAssignableFrom(paramType))
        {
            return webcx.getSession(true);
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx)
    {
        return new NonNestedOutboundVariable("null");
    }
}
