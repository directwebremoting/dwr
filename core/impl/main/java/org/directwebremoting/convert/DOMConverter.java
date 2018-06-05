package org.directwebremoting.convert;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.EnginePrivate;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * An implementation of Converter for DOM objects.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DOMConverter extends AbstractConverter
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
            if (buildFactory == null)
            {
                buildFactory = DocumentBuilderFactory.newInstance();

                // Protect us from hackers, see:
                // https://www.owasp.org/index.php/XML_External_Entity_%28XXE%29_Processing
                buildFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
                buildFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
                try {
                    buildFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                } catch(Exception ex) {
                    // XML parser doesn't have this setting, never mind
                }
            }

            DocumentBuilder builder = buildFactory.newDocumentBuilder();

            // Extra protection from external entity hacking
            builder.setEntityResolver(new EntityResolver()
            {
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
                {
                    return new InputSource(); // no lookup, just return empty
                }
            });

            InputSource is = new InputSource(new StringReader(value));
            Document doc = builder.parse(is);

            if (paramType == Document.class)
            {
                return doc;
            }
            else if (paramType == Element.class)
            {
                return doc.getDocumentElement();
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
            Transformer transformer = xslFact.newTransformer();

            // Using XSLT to convert to a stream. Setup the source
            Source source;
            if (data instanceof Node)
            {
                Node node = (Node) data;
                source = new DOMSource(node);
            }
            else
            {
                throw new ConversionException(data.getClass());
            }

            // Setup the destination
            StringWriter xml = new StringWriter();
            StreamResult result = new StreamResult(xml);

            transformer.transform(source, result);

            xml.flush();

            String script;
            if (data instanceof Element)
            {
                script = EnginePrivate.xmlStringToJavascriptDomElement(xml.toString());
            }
            else
            {
                script = EnginePrivate.xmlStringToJavascriptDomDocument(xml.toString());
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

    /**
     * How we create new transformers
     */
    private final TransformerFactory xslFact = TransformerFactory.newInstance();

    /**
     * How we create new documents
     */
    private DocumentBuilderFactory buildFactory = null;
}
