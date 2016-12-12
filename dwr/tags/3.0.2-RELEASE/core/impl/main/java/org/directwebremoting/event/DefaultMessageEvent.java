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
package org.directwebremoting.event;

import org.directwebremoting.ConversionException;
import org.directwebremoting.Hub;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.RealRawData;

/**
 * An MessageEvent is fired to a set of {@link MessageListener}s by the DWR
 * {@link Hub}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultMessageEvent implements MessageEvent
{
    /**
     * Constructor for use with server-side originated messages
     * @param hub The hub used to send the data
     * @param data The data to publish
     */
    public DefaultMessageEvent(Hub hub, Object data)
    {
        this.hub = hub;
        this.data = data;

        source = Source.SERVER;
    }

    /**
     * Constructor for use with client-side originated messages
     * @param hub The hub used to send the data
     * @param converterManager
     * @param rawData
     */
    public DefaultMessageEvent(Hub hub, ConverterManager converterManager, RealRawData rawData)
    {
        this.hub = hub;
        this.converterManager = converterManager;
        this.rawData = rawData;

        source = Source.INTERNET;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.event.MessageEvent#getHub()
     */
    public Hub getHub()
    {
        return hub;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.event.MessageEvent#getData(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(Class<T> asType)
    {
        if (source == Source.SERVER)
        {
            try
            {
                return (T) data;
            }
            catch (ClassCastException ex)
            {
                throw new ConversionException(asType, ex);
            }
        }
        else
        {
            return converterManager.convertInbound(asType, rawData);
        }
    }

    /**
     * WARNING: This method is for internal use only. It may well disappear at
     * some stage in the future
     * Sometimes we just want to get at whatever the data was originally without
     * any conversion.
     * @return The original data probably of type RawData
     */
    public Object getRawData()
    {
        if (source == Source.SERVER)
        {
            return data;
        }
        else
        {
            return rawData;
        }
    }

    /**
     * Did the data come from the web or from the server?
     */
    private enum Source
    {
        SERVER, INTERNET
    }

    private Hub hub;
    private Object data;
    private RealRawData rawData;
    private Source source;
    private ConverterManager converterManager;
}
