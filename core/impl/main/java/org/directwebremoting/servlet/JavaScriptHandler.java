package org.directwebremoting.servlet;

import java.io.IOException;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Compressor;
import org.directwebremoting.util.MimeConstants;

/**
 * Once we know a resource is JavaScript, we can go about compressing it.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class JavaScriptHandler extends TemplateHandler
{
    /**
     * Setup the {@link JavaScriptHandler} defaults
     */
    public JavaScriptHandler()
    {
        setMimeType(MimeConstants.MIME_JS);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.CachingHandler#generate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public String generateCachableContent(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        String output = super.generateCachableContent(contextPath, servletPath, pathInfo);

        if (debug || compressor == null)
        {
            return output;
        }

        try
        {
            return compressor.compressJavaScript(output);
        }
        catch (Exception ex)
        {
            log.warn("Compression system (" + compressor.getClass().getSimpleName() +") failed to compress script", ex);
            return output;
        }
    }

    /**
     * Setter for the current JavaScript compression library
     * @param compressor The new compression library
     */
    public void setCompressor(Compressor compressor)
    {
        this.compressor = compressor;
    }

    /**
     * In debug mode we don't do compression at all
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * Are we compressing the script?
     */
    private Compressor compressor;

    /**
     * In debug mode, we skip script compression
     */
    protected boolean debug = false;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JavaScriptHandler.class);
}
