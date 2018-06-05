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
