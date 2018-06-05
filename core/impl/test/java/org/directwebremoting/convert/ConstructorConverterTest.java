package org.directwebremoting.convert;

import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The tests for the <code>ConstructorConverter</code> class.
 * @see ConstructorConverter
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConstructorConverterTest
{
    private ConstructorConverter converter = new ConstructorConverter();

    @Test
    public void convertInbound()
    {
        InboundContext ctx = new InboundContext();
        InboundVariable iv = new InboundVariable(ctx, null, "type", "value");

        Object result = converter.convertInbound(String.class, iv);

        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("value", result);
    }

    @Test
    public void convertOutbound()
    {
        OutboundContext ctx = new OutboundContext(false);

        OutboundVariable result = converter.convertOutbound("value", ctx);

        assertNotNull(result);
        assertEquals("'value'", result.getAssignCode());
        assertEquals("", result.getBuildCode());
    }

    @Test
    public void setConverterManager()
    {
        converter.setConverterManager(null);
    }
}
