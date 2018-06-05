package org.directwebremoting.servlet;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.ModuleManager;
import org.directwebremoting.util.LocalUtil;

/**
 * Handler base class for interface generation requests
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BaseInterfaceHandler extends GeneratedJavaScriptHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.TemplateHandler#generateTemplate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected String generateTemplate(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        String scriptName = pathInfo;

        if (!scriptName.startsWith(getBaseInterfacePath()) || !scriptName.endsWith(PathConstants.EXTENSION_JS))
        {
            return null;
        }

        scriptName = scriptName.substring(getBaseInterfacePath().length());
        scriptName = scriptName.substring(0, scriptName.length() - PathConstants.EXTENSION_JS.length());

        if (!LocalUtil.isValidScriptName(scriptName))
        {
            log.debug("Throwing at request for script with name: '" + scriptName + "'");
            throw new SecurityException("Illegal script name.");
        }

        // Be flexible and let "/" separators in URL match both "." and "/" in
        // script names
        if (scriptName.contains("/"))
        {
            Pattern p = Pattern.compile(scriptName.replaceAll("/", "[/\\.]"));
            String match = null;
            for(String moduleName : moduleManager.getModuleNames(false))
            {
                if (p.matcher(moduleName).matches())
                {
                    if (match == null)
                    {
                        match = moduleName;
                    }
                    else
                    {
                        throw new IllegalArgumentException("Script name '" + scriptName + "' matches several modules.");
                    }
                }
            }
            if (match != null)
            {
                scriptName = match;
            }
        }

        // Generate script
        return generateInterfaceScript(contextPath, servletPath, scriptName);
    }

    /**
     * Returns the path used for interfaces by this Handler
     * @return interface path
     */
    protected abstract String getBaseInterfacePath();

    /**
     * Does the actual work of generating the interface script
     * @param contextPath
     * @param servletPath TODO
     * @param scriptName
     * @return interface script
     */
    public abstract String generateInterfaceScript(String contextPath, String servletPath, String scriptName);

    /**
     * Setter for the configured ModuleManager
     * @param moduleManager the ModuleManager
     */
    public void setModuleManager(ModuleManager moduleManager)
    {
        this.moduleManager = moduleManager;
    }

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
        return this.getClass().getSimpleName() + "(" + getBaseInterfacePath() + ")";
    }

    /**
     * Where we look up available script names
     */
    protected ModuleManager moduleManager = null;

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
    private static final Log log = LogFactory.getLog(BaseInterfaceHandler.class);
}
