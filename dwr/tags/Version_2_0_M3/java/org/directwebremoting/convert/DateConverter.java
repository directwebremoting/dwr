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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.directwebremoting.Converter;
import org.directwebremoting.InboundContext;
import org.directwebremoting.InboundVariable;
import org.directwebremoting.MarshallException;
import org.directwebremoting.OutboundContext;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.dwrp.ConversionConstants;
import org.directwebremoting.util.Messages;

/**
 * An implementation of Converter for Dates.
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id: StringConverter.java,v 1.2 2004/11/04 15:54:07 joe_walker Exp $
 */
public class DateConverter extends BaseV20Converter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable, org.directwebremoting.InboundContext)
     */
    public Object convertInbound(Class paramType, InboundVariable iv, InboundContext inctx) throws MarshallException
    {
        String value = iv.getValue();

        // If the text is null then the whole bean is null
        if (value.trim().equals(ConversionConstants.INBOUND_NULL))
        {
            return null;
        }

        try
        {
            long millis = 0;
            if (value.length() > 0)
            {
                millis = Long.parseLong(value);
            }

            Date date = new Date(millis);
            if (paramType == Date.class)
            {
                return date;
            }
            else if (paramType == java.sql.Date.class)
            {
                return new java.sql.Date(date.getTime());
            }
            else if (paramType == Time.class)
            {
                return new Time(date.getTime());
            }
            else if (paramType == Timestamp.class)
            {
                return new Timestamp(date.getTime());
            }
            else if (paramType == Calendar.class)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal;
            }
            else
            {
                throw new MarshallException(Messages.getString("DateConverter.WrongType") + paramType.getName());
            }
        }
        catch (MarshallException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new MarshallException(Messages.getString("DateConverter.ErrorConverting", value, paramType.getName()), ex);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException
    {
        if (!(data instanceof Date))
        {
            throw new MarshallException(Messages.getString("DateConverter.TypeError", data.getClass()));
        }

        long millis;
        if (data instanceof Calendar)
        {
            Calendar cal = (Calendar) data;
            millis = cal.getTime().getTime();
        }
        else
        {
            Date date = (Date) data;
            millis = date.getTime();
        }

        return new OutboundVariable("", "new Date(" + millis + ")");
    }
}
