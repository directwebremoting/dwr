package org.directwebremoting.servlet;

import java.io.IOException;

import org.directwebremoting.extend.ConverterManager;

/**
 * Base class handler for dtoall generation requests
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public abstract class BaseDtoAllHandler extends GeneratedJavaScriptHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.TemplateHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        if (!generateDtoClasses.matches(".*\\bdtoall\\b.*"))
        {
            return null;
        }

        return generateDtoAllScript(contextPath, servletPath);
    }

    /**
     * Generates the full dtoall script by decorating the DTO classes returned by the Remoter.
     * @param contextPath
     * @param servletPath
     * @return string containing dtoall script
     */
    public abstract String generateDtoAllScript(String contextPath, String servletPath);

    /**
     * Setter for the generator setting.
     * @param generateDtoClasses list of enabled places to generate DTO classes in
     */
    public void setGenerateDtoClasses(String generateDtoClasses)
    {
        this.generateDtoClasses = generateDtoClasses;
    }

    /**
     * @param converterManager the converterManager to set
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * List of enabled places to generate DTO classes in
     */
    protected String generateDtoClasses;

    /**
     * ConverterManager to query for DTO classes
     */
    protected ConverterManager converterManager;
}
