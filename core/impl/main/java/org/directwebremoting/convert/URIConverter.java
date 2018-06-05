package org.directwebremoting.convert;

import java.net.URI;
import java.net.URISyntaxException;

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
public class URIConverter extends AbstractConverter
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

        String uriString = data.urlDecode();
        try
        {
            return new URI(uriString);
        }
        catch (URISyntaxException ex)
        {
            log.warn("Failed to create URL from string '" + uriString + "'. Returning null");
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        URI uri = (URI) data;
        String escaped = JavascriptUtil.escapeJavaScript(uri.toString());
        return new NonNestedOutboundVariable('\"' + escaped + '\"');
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(URIConverter.class);
}
