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
package jsx3.net;

import org.directwebremoting.convert.BaseV20Converter;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class URIResolverConverter extends BaseV20Converter implements Converter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertInbound(java.lang.Class, org.directwebremoting.extend.InboundVariable, org.directwebremoting.extend.InboundContext)
     */
    public Object convertInbound(Class<?> paramType, InboundVariable data, InboundContext inctx) throws MarshallException
    {
        URIResolver reply = URIResolver.toURIResolver(paramType.toString());
        if (reply == null)
        {
            throw new MarshallException(paramType);
        }

        return reply;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Converter#convertOutbound(java.lang.Object, org.directwebremoting.extend.OutboundContext)
     */
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws MarshallException
    {
        URIResolver resolver = (URIResolver) data;
        return new NonNestedOutboundVariable(resolver.constant);
    }
}
