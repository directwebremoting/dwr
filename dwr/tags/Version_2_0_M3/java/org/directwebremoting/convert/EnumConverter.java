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

import java.lang.reflect.Method;

import org.directwebremoting.Converter;
import org.directwebremoting.InboundContext;
import org.directwebremoting.InboundVariable;
import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundContext;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Messages;

/**
 * Converter for all primitive types
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class EnumConverter extends BaseV20Converter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws MarshallException
    {
        String value = LocalUtil.decode(iv.getValue());

        Object[] values;
        try
        {
            Method getter = paramType.getMethod("values", new Class[0]);
            values = (Object[]) getter.invoke(paramType, null);
        }
        catch (NoSuchMethodException ex)
        {
            // We would like to have done: if (!paramType.isEnum())
            // But this catch block has the same effect
            throw new MarshallException(Messages.getString("EnumConverter.TypeNotEnum", paramType));
        }
        catch (Exception ex)
        {
            throw new MarshallException(Messages.getString("EnumConverter.ReflectionError", paramType), ex);
        }

        for (int i = 0; i < values.length; i++)
        {
            Object en = values[i];
            if (value.equals(en.toString()))
            {
                return en;
            }
        }

        throw new MarshallException(Messages.getString("EnumConverter.NoMatch", value, paramType));
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object object, OutboundContext outctx)
    {
        return new OutboundVariable("", '\'' + object.toString() + '\'');
    }
}
