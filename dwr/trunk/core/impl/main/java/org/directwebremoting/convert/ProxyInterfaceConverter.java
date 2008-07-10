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

import java.lang.reflect.Proxy;

import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.extend.AbstractConverter;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.DefaultJavascriptObject;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ProxyInterfaceConverter extends AbstractConverter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertInbound(java.lang.Class, org.directwebremoting.extend.InboundVariable)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException
    {
        ScriptSession session = WebContextFactory.get().getScriptSession();
        String id = data.getValue();

        DefaultJavascriptObject object = new DefaultJavascriptObject(session, id);

        return Proxy.newProxyInstance(paramType.getClassLoader(), new Class[] { paramType }, object);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertOutbound(java.lang.Object, org.directwebremoting.extend.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException
    {
        throw new ConversionException(data.getClass(), "Interfaces can not be passed to a browser");
    }
}
