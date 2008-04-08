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

import static org.junit.Assert.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The tests for the <code>DateConverter</code> class.
 * @see DateConverter
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DateConverterTest
{
    private DateConverter converter = new DateConverter();

    @Test
    public void convertOutbound() throws Exception
    {
        OutboundVariable result = converter.convertOutbound(new Date(1104534000000L), new OutboundContext(false));

        assertNotNull(result);
        assertEquals("new Date(1104534000000)", result.getAssignCode());
        assertEquals("", result.getBuildCode());
    }

    @Test(expected = MarshallException.class)
    public void convertOutboundFail() throws Exception
    {
        // try to convert a non-date object
        converter.convertOutbound("01-01-2005", new OutboundContext(false));
    }

    @Test
    public void testConvertInbound() throws Exception
    {
        InboundContext ctx = new InboundContext();
        InboundVariable iv = new InboundVariable(ctx, null, "type", "null");

        Object result = converter.convertInbound(Date.class, iv, ctx);
        assertNull(result);

        iv = new InboundVariable(ctx, null, "type", "1104534000000");
        result = converter.convertInbound(Date.class, iv, ctx);

        assertNotNull(result);
        assertTrue(result instanceof Date);
        assertEquals(new Date(1104534000000L), result);

        result = converter.convertInbound(java.sql.Date.class, iv, ctx);
        assertNotNull(result);
        assertTrue(result instanceof java.sql.Date);

        result = converter.convertInbound(Time.class, iv, ctx);
        assertNotNull(result);
        assertTrue(result instanceof Time);

        result = converter.convertInbound(Timestamp.class, iv, ctx);
        assertNotNull(result);
        assertTrue(result instanceof Timestamp);
    }

    @Ignore
    @Test(expected = MarshallException.class)
    public void testConvertInboundFail() throws Exception
    {
        InboundContext ctx = new InboundContext();
        InboundVariable iv = new InboundVariable(ctx, null, "type", "null");

        // try to convert a non-supported type
        converter.convertInbound(String.class, iv, ctx);
    }
}
