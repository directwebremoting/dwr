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

import org.directwebremoting.Converter;
import org.directwebremoting.InboundContext;
import org.directwebremoting.InboundVariable;
import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundContext;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;

/**
 * Converter for all primitive types
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PrimitiveConverter extends BaseV20Converter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws MarshallException
    {
        String value = iv.getValue();
        value = LocalUtil.decode(value.trim());

        try
        {
            return LocalUtil.simpleConvert(value, paramType);
        }
        catch (NumberFormatException ex)
        {
            throw new MarshallException(Messages.getString("PrimitiveConverter.FormatError", value, paramType.getName()), ex);
        }
        catch (IllegalArgumentException ex)
        {
            throw new MarshallException(Messages.getString("PrimitiveConverter.TypeNotPrimitive", paramType.getName()), ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object object, OutboundContext outctx)
    {
        Class paramType = object.getClass();

        if (object.equals(Boolean.TRUE))
        {
            return new OutboundVariable("", "true");
        }
        else if (object.equals(Boolean.FALSE))
        {
            return new OutboundVariable("", "false");
        }
        else if (paramType == Character.class)
        {
            // Treat characters as strings
            return new OutboundVariable("", '\"' + JavascriptUtil.escapeJavaScript(object.toString()) + '\"');
        }
        else
        {
            // We just use the default toString for all numbers
            return new OutboundVariable("", object.toString());
        }
    }
}
