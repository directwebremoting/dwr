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

import java.util.Date;

import org.directwebremoting.ConversionException;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.ProtocolConstants;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

public class LocalDateTimeConverter extends AbstractConverter {


    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException {
        if (data.isNull()) {
            return null;
        }

        String value = data.getValue();

        // If the text is null then the whole bean is null
        if (value.trim().equals(ProtocolConstants.INBOUND_NULL)) {
            return null;
        }

        try {
            long millis = 0;
            if (value.length() > 0) {
                millis = Long.parseLong(value);
            }

            if (paramType == DateTime.class) {
                return new DateTime(millis);
            } else if (paramType == LocalDateTime.class) {
                return new LocalDateTime(new Date(millis));
            } else {
                throw new ConversionException(paramType);
            }
        }
        catch (ConversionException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ConversionException(paramType, ex);
        }
    }

    public OutboundVariable convertOutbound(Object data, OutboundContext outboundContext) throws ConversionException {
        long milliSeconds;

        if (data instanceof DateTime) {
            DateTime dateTime = (DateTime) data;
            milliSeconds = dateTime.getMillis();
        } else if (data instanceof LocalDateTime) {
            LocalDateTime date = (LocalDateTime) data;
            milliSeconds = date.toDateTime().toDate().getTime();
        } else {
            throw new ConversionException(data.getClass());
        }
        return new NonNestedOutboundVariable("new Date(" + milliSeconds + ")");
    }
}
