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
     * Accessor for the DefaultConverterManager that we configure
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
