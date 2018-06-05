package org.directwebremoting.servlet;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.util.LocalUtil;

/**
 * A handler for DTO class generation requests
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public abstract class BaseDtoHandler extends GeneratedJavaScriptHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.TemplateHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        if (!generateDtoClasses.matches(".*\\bdto\\b.*"))
        {
            return null;
        }

        String jsClassName = pathInfo;

        if (!jsClassName.startsWith(getBaseDtoPath()) || !jsClassName.endsWith(PathConstants.EXTENSION_JS))
        {
            return null;
        }

        jsClassName = jsClassName.substring(getBaseDtoPath().length());
        jsClassName = jsClassName.substring(0, jsClassName.length() - PathConstants.EXTENSION_JS.length());

        if (!LocalUtil.isValidMappedClassName(jsClassName))
        {
            log.debug("Throwing at request for class with name: '" + jsClassName + "'");
            throw new SecurityException("Illegal mapped class name.");
        }

        // Be flexible and let "/" separators in URL match both "." and "/" in
        // DTO script names
        if (jsClassName.contains("/"))
        {
            Pattern p = Pattern.compile(jsClassName.replaceAll("/", "[/\\.]"));
            String match = null;
            for(String convJsClassName : converterManager.getNamedConverterJavaScriptNames())
            {
                if (p.matcher(convJsClassName).matches())
                {
                    if (match == null)
                    {
                        match = convJsClassName;
                    }
                    else
                    {
                        throw new IllegalArgumentException("DTO script name '" + jsClassName + "' matches several converters.");
                    }
                }
            }
            if (match != null)
            {
                jsClassName = match;
            }
        }

        return generateDtoScript(contextPath, servletPath, jsClassName);
    }

    /**
     * Returns the base path used by this Handler
     * @return interface path
     */
    protected abstract String getBaseDtoPath();

    /**
     * Does the actual work of generating the dto script
     * @param contextPath TODO
     * @param servletPath TODO
     * @param jsClassName
     * @return dto script
     */
    public abstract String generateDtoScript(String contextPath, String servletPath, String jsClassName);

    /**
     * @param converterManager the converterManager to set
     */
    public void setConverterManager(ConverterManager converterManager)
    {
        this.converterManager = converterManager;
    }

    /**
     * Setter for the generator setting.
     * @param generateDtoClasses list of enabled places to generate DTO classes in
     */
    public void setGenerateDtoClasses(String generateDtoClasses)
    {
        this.generateDtoClasses = generateDtoClasses;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "(" + getBaseDtoPath() + ")";
    }

    /**
     * ConverterManager to query for DTO classes
     */
    protected ConverterManager converterManager;

    /**
     * List of enabled places to generate DTO classes in
     */
    protected String generateDtoClasses;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BaseDtoHandler.class);
}
