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
package jsx3.xml;

import org.directwebremoting.convert.BaseV20Converter;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.util.JavascriptUtil;

/**
 * An implementation of Converter for DOM objects.
 * @author Joe Walker [joe at eireneh dot com]
 */
public class CdfDocumentConverter extends BaseV20Converter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable iv, InboundContext inctx) throws MarshallException
    {
        // String value = LocalUtil.decode(iv.getValue());

        try
        {
            throw new MarshallException(paramType);
        }
        catch (MarshallException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new MarshallException(paramType, ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException
    {
        try
        {
            // Using XSLT to convert to a stream. Setup the source
            if (!(data instanceof CdfDocument))
            {
                throw new MarshallException(data.getClass());
            }

            CdfDocument document = (CdfDocument) data;
            String xmlout = JavascriptUtil.escapeJavaScript(document.toXml());

            String script = "dwr.gi._loadXml(\"" + xmlout + "\")";
            OutboundVariable ov = new NonNestedOutboundVariable(script);

            outctx.put(data, ov);

            return ov;
        }
        catch (MarshallException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new MarshallException(data.getClass(), ex);
        }
    }
}
