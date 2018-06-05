package org.directwebremoting.impl;

import java.io.IOException;

import org.directwebremoting.extend.Compressor;

/**
 * An implementation of {@link Compressor} that does nothing.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class NullCompressor implements Compressor
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Compressor#compressJavaScript(java.lang.String, java.lang.String)
     */
    public String compressJavaScript(String script) throws IOException
    {
        return script;
    }
}
