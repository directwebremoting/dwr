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
package org.directwebremoting.convert;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.directwebremoting.Converter;
import org.directwebremoting.InboundContext;
import org.directwebremoting.InboundVariable;
import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundContext;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * An implementation of Converter for DOM objects.
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id: StringConverter.java,v 1.2 2004/11/04 15:54:07 joe_walker Exp $
 */
public class DOMConverter extends BaseV20Converter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws MarshallException
    {
        String value = LocalUtil.decode(iv.getValue());

        try
        {
            if (buildFactory == null)
            {
                buildFactory = DocumentBuilderFactory.newInstance();
            }

            DocumentBuilder builder = buildFactory.newDocumentBuilder();

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

            throw new MarshallException(Messages.getString("DOMConverter.UnusableClass", paramType.getName()));
        }
        catch (MarshallException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new MarshallException(ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException
    {
        OutboundVariable ov = outctx.createOutboundVariable(data);
        String varname = ov.getAssignCode();

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
                throw new MarshallException("Data is not a DOM Node");
            }

            // Setup the destination
            StringWriter xml = new StringWriter();
            StreamResult result = new StreamResult(xml);

            transformer.transform(source, result);

            xml.flush();

            StringBuffer buffer = new StringBuffer();
            buffer.append("var ");
            buffer.append(varname);
            buffer.append("=DWREngine._unserializeDocument(\"");
            buffer.append(JavascriptUtil.escapeJavaScript(xml.toString()));
            buffer.append("\");");

            ov.setInitCode(buffer.toString());
            return ov;
        }
        catch (MarshallException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new MarshallException(ex);
        }
    }

    /**
     * How we create new transformers
     */
    private TransformerFactory xslFact = TransformerFactory.newInstance();

    /**
     * How we create new documents
     */
    private DocumentBuilderFactory buildFactory = null;
}
