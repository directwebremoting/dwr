package org.directwebremoting.convert;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * An implementation of Converter for DOM objects.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JDOMConverter extends AbstractConverter
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

        String value = data.urlDecode();

        try
        {
            SAXBuilder builder = new SAXBuilder();

            // Protect us from hackers, see:
            // https://www.owasp.org/index.php/XML_External_Entity_%28XXE%29_Processing
            builder.setFeature("http://xml.org/sax/features/external-general-entities", false);
            builder.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            try {
                builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            } catch(Exception ex) {
                // XML parser doesn't have this setting, never mind
            }

            // Extra protection from external entity hacking
            builder.setEntityResolver(new EntityResolver()
            {
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
                {
                    return new InputSource(); // no lookup, just return empty
                }
            });

            Document doc = builder.build(new StringReader(value));

            if (paramType == Document.class)
            {
                return doc;
            }
            else if (paramType == Element.class)
            {
                return doc.getRootElement();
            }

            throw new ConversionException(paramType);
        }
        catch (ConversionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ConversionException(paramType, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        try
        {
            Format outformat = Format.getRawFormat();
            outformat.setEncoding("UTF-8");

            // Setup the destination
            String script;

            // Using XSLT to convert to a stream. Setup the source
            if (data instanceof Document)
            {
                StringWriter xml = new StringWriter();
                XMLOutputter writer = new XMLOutputter(outformat);
                writer.output((Document) data, xml);
                xml.flush();
                script = EnginePrivate.xmlStringToJavascriptDomDocument(xml.toString());
            }
            else if (data instanceof Element)
            {
                StringWriter xml = new StringWriter();
                XMLOutputter writer = new XMLOutputter(outformat);
                writer.output((Element) data, xml);
                xml.flush();
                script = EnginePrivate.xmlStringToJavascriptDomElement(xml.toString());
            }
            else
            {
                throw new ConversionException(data.getClass());
            }

            OutboundVariable ov = new NonNestedOutboundVariable(script);

            outctx.put(data, ov);

            return ov;
        }
        catch (ConversionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ConversionException(data.getClass(), ex);
        }
    }
}
