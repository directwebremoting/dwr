package org.directwebremoting.convert;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.util.JavascriptUtil;

/**
 * An implementation of Converter for Strings.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class URLConverter extends AbstractConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        if (data.isNull())
        {
            return null;
        }

        String urlString = data.urlDecode();
        try
        {
            return new URL(urlString);
        }
        catch (MalformedURLException ex)
        {
            log.warn("Failed to create URL from string '" + urlString + "'. Returning null");
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        URL url = (URL) data;
        String escaped = JavascriptUtil.escapeJavaScript(url.toExternalForm());
        return new NonNestedOutboundVariable('\"' + escaped + '\"');
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(URLConverter.class);
}
