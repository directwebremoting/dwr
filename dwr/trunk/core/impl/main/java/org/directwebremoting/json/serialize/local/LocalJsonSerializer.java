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
package org.directwebremoting.json.serialize.local;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.json.serialize.JsonSerializer;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class LocalJsonSerializer implements JsonSerializer
{
    /* (non-Javadoc)
     * @see org.directwebremoting.json.serialize.JsonSerializer#toJson(java.lang.Object, java.io.PrintWriter)
     */
    public void toJson(Object data, Writer out) throws IOException
    {
        // Get the output stream and setup the mime type
        try
        {
            ScriptBuffer buffer = new ScriptBuffer();
            buffer.appendData(data);

            String output = ScriptBufferUtil.createOutput(buffer, converterManager, true);
            out.write(output);
        }
        catch (ConversionException ex)
        {
            log.warn("--ConversionException: class=" + ex.getConversionType().getName(), ex);

            ScriptBuffer buffer = new ScriptBuffer();
            buffer.appendData(ex);

            try
            {
                String output = ScriptBufferUtil.createOutput(buffer, converterManager, true);
                out.write(output);
            }
            catch (ConversionException ex1)
            {
                log.error("--Nested ConversionException: Is there an exception handler registered? class=" + ex.getConversionType().getName(), ex);
            }
        }
    }

    /**
     * Accessor for the DefaultCreatorManager that we configure
     * @param converterManager The new DefaultConverterManager
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(LocalJsonSerializer.class);
}
