/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ltd.getahead.dwr.convert;

import java.io.StringReader;
import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import uk.ltd.getahead.dwr.ConversionException;
import uk.ltd.getahead.dwr.Converter;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.compat.BaseV10Converter;
import uk.ltd.getahead.dwr.util.JavascriptUtil;
import uk.ltd.getahead.dwr.util.LocalUtil;

/**
 * An implementation of Converter for DOM objects.
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id: StringConverter.java,v 1.2 2004/11/04 15:54:07 joe_walker Exp $
 */
public class JDOMConverter extends BaseV10Converter implements Converter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#init(uk.ltd.getahead.dwr.DefaultConfiguration)
     */
    public void setConverterManager(ConverterManager newConfig)
    {
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertInbound(java.lang.Class, java.util.List, uk.ltd.getahead.dwr.InboundVariable, uk.ltd.getahead.dwr.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws ConversionException
    {
        String value = LocalUtil.decode(iv.getValue());

        try
        {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(value));

            if (paramType == Document.class)
            {
                return doc;
            }
            else if (paramType == Element.class)
            {
                return doc.getRootElement();
            }

            throw new ConversionException(Messages.getString("DOMConverter.UnusableClass", paramType.getName())); //$NON-NLS-1$
        }
        catch (ConversionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ConversionException(ex);
        }
    }

    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Converter#convertOutbound(java.lang.Object, java.lang.String, uk.ltd.getahead.dwr.OutboundContext)
     */
    public String convertOutbound(Object data, String varname, OutboundContext outctx) throws ConversionException
    {
        try
        {
            Format outformat = Format.getCompactFormat();
            outformat.setEncoding("UTF-8"); //$NON-NLS-1$

            // Setup the destination
            StringWriter xml = new StringWriter();

            XMLOutputter writer = new XMLOutputter(outformat);

            // Using XSLT to convert to a stream. Setup the source
            if (data instanceof Document)
            {
                Document doc = (Document) data;
                writer.output(doc, xml);
            }
            else if (data instanceof Element)
            {
                Element ele = (Element) data;
                writer.output(ele, xml);
            }
            else
            {
                throw new ConversionException("Data is not a DOM Node"); //$NON-NLS-1$
            }

            xml.flush();

            StringBuffer buffer = new StringBuffer();
            buffer.append("var "); //$NON-NLS-1$
            buffer.append(varname);
            buffer.append("=DWREngine._unserializeDocument(\""); //$NON-NLS-1$
            buffer.append(jsutil.escapeJavaScript(xml.toString()));
            buffer.append("\");"); //$NON-NLS-1$

            return buffer.toString();
        }
        catch (ConversionException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ConversionException(ex);
        }
    }

    /**
     * The means by which we strip comments
     */
    private JavascriptUtil jsutil = new JavascriptUtil();
}
