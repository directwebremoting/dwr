package uk.ltd.getahead.dwr.convert;

import java.lang.reflect.Constructor;

import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.lang.StringEscapeUtils;

/**
 * An implementation of Converter for anything with a string constructor.
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id: ConstructorConverter.java,v 1.2 2004/11/04 15:54:07 joe_walker Exp $
 */
public class ConstructorConverter implements Converter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#init(uk.ltd.getahead.dwr.Configuration)
     */
    public void init(ConverterManager config)
    {
    }
    
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertTo(java.lang.Class, uk.ltd.getahead.dwr.InboundVariable, java.util.Map)
     */
    public Object convertInbound(Class paramType, InboundVariable data, InboundContext inctx)
    {
        try
        {
            Constructor converter = paramType.getConstructor(new Class[] { String.class });
            return converter.newInstance(new Object[] { data.getValue() });
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException(ex.toString());
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertFrom(java.lang.Object, java.lang.String, java.util.Map)
     */
    public String convertOutbound(Object data, String varname, OutboundContext outctx)
    {
        return "var " + varname + " = \"" + StringEscapeUtils.escapeJavaScript(data.toString()) + "\";"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
