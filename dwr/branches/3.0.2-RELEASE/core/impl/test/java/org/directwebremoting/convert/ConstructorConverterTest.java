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
