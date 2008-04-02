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
package org.directwebremoting.dwrp;

import org.directwebremoting.convert.BeanConverter;
import org.directwebremoting.convert.StringConverter;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultConverterManagerTest
{
    private DefaultConverterManager manager = new DefaultConverterManager();

    @Test
    public void addConverterTypeFail()
    {
        int before = manager.converterTypes.size();

        manager.addConverterType(null, null);
        manager.addConverterType(null, Converter.class.getName());
        manager.addConverterType(null, BeanConverter.class.getName());

        int after = manager.converterTypes.size();
        Assert.assertEquals(before, after);
    }

    @Test(expected = NullPointerException.class)
    public void convertInboundFail() throws Exception
    {
        manager.convertInbound(null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void convertInboundFail2() throws Exception
    {
        manager.convertInbound(null, null, new InboundContext(), null);
    }

    @Test(expected = NullPointerException.class)
    public void convertInboundFail3() throws Exception
    {
        manager.convertInbound(String.class, null, new InboundContext(), null);
    }

    @Test(expected = NullPointerException.class)
    public void convertInboundFail4() throws Exception
    {
        manager.convertInbound(String.class, null, new InboundContext(), null);
    }

    @Test
    public void convertInbound5() throws Exception
    {
        manager.addConverter("java.lang.String", new StringConverter());

        InboundContext ctx = new InboundContext();
        InboundVariable var = new InboundVariable(ctx, "e_1", "string", "bla");
        manager.convertInbound(String.class, var, ctx, null);
    }

    @Test
    public void convertOutbound() throws Exception
    {
        manager.convertOutbound(null, null);
    }
}
