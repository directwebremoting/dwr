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

import org.directwebremoting.ConversionException;
import org.directwebremoting.convert.LocalDateTimeConverter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocalDateTimeConverterTest {

    private final LocalDateTimeConverter converter = new LocalDateTimeConverter();

    @Test
    public void convertOutbound() throws Exception {
        OutboundVariable result = converter.convertOutbound(new DateTime(1104534000000L), new OutboundContext(false));

        assertNotNull(result);
        assertEquals("new Date(1104534000000)", result.getAssignCode());
        assertEquals("", result.getBuildCode());

        result = converter.convertOutbound(new LocalDateTime(1104534000000L), new OutboundContext(false));

        assertNotNull(result);
        assertEquals("new Date(1104534000000)", result.getAssignCode());
        assertEquals("", result.getBuildCode());

    }

    @Test(expected = ConversionException.class)
    public void convertOutboundFail() throws Exception {
        // try to convert a non-LocalDateTime object
        converter.convertOutbound("01-01-2005", new OutboundContext(false));
    }

    @Test
    public void testConvertInbound() throws Exception {
        InboundContext ctx = new InboundContext();
        InboundVariable iv = new InboundVariable(ctx, null, "type", "null");

        Object result = converter.convertInbound(LocalDateTime.class, iv);
        assertNull(result);

        iv = new InboundVariable(ctx, null, "type", "1104534000000");
        result = converter.convertInbound(DateTime.class, iv);

        assertNotNull(result);
        assertTrue(result instanceof DateTime);
        assertEquals(new DateTime(1104534000000L), result);

        result = converter.convertInbound(LocalDateTime.class, iv);
        assertNotNull(result);
        assertTrue(result instanceof LocalDateTime);

    }

    @Test(expected = ConversionException.class)
    public void testConvertInboundFail() throws Exception {
        InboundContext ctx = new InboundContext();
        InboundVariable iv = new InboundVariable(ctx, null, "type", "null");

        // try to convert a non-supported type
        iv = new InboundVariable(ctx, null, "type", "1104534000000");
        converter.convertInbound(String.class, iv);

    }
}
