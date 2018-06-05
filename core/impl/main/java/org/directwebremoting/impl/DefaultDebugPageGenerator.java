package org.directwebremoting.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.DebugPageGenerator;
import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.extend.MethodDeclaration;
import org.directwebremoting.extend.Module;
import org.directwebremoting.extend.ModuleManager;
import org.directwebremoting.servlet.PathConstants;
import org.directwebremoting.util.CopyUtils;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;

/**
 * A default implementation of TestPageGenerator
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultDebugPageGenerator implements DebugPageGenerator
{
    /* (non-Javadoc)
     * @see org.directwebremoting.DebugPageGenerator#generateIndexPage(java.lang.String)
     */
    public String generateIndexPage(final String root) throws SecurityException
    {
        if (!debug)
        {
            log.warn("Failed attempt to access test pages outside of debug mode. Set the debug init-parameter to true to enable.");
            throw new SecurityException("Access to debug pages is denied.");
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append("<html>\n");
        buffer.append("<head><title>DWR Test Index</title></head>\n");
        buffer.append("<body>\n");

        buffer.append("<h2>Modules known to DWR:</h2>\n");
        buffer.append("<ul>\n");
        for (String name : moduleManager.getModuleNames(false))
        {
            Module module = moduleManager.getModule(name, false);

            buffer.append("<li><a href='");
            buffer.append(root);
            buffer.append(testHandlerUrl);
            buffer.append(name);
            buffer.append("'>");
            buffer.append(name);
            buffer.append("</a> (");
            buffer.append(module.toString());
            buffer.append(")</li>\n");
        }
        buffer.append("</ul>\n");

        buffer.append("</body></html>\n");

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.DebugPageGenerator#generateTestPage(java.lang.String, java.lang.String)
     */
    public String generateTestPage(final String root, final String scriptName) throws SecurityException
    {
        if (!debug)
        {
            log.warn("Failed attempt to access test pages outside of debug mode. Set the debug init-parameter to true to enable.");
            throw new SecurityException("Access to debug pages is denied.");
        }

        String interfaceURL = root + interfaceHandlerUrl + scriptName + PathConstants.EXTENSION_JS;
        String engineURL = root + engineHandlerUrl;
        String utilURL = root + utilHandlerUrl;

        String proxyInterfaceURL = PATH_UP + interfaceHandlerUrl + scriptName + PathConstants.EXTENSION_JS;
        String proxyEngineURL = PATH_UP + engineHandlerUrl;
        String proxyUtilURL = PATH_UP + utilHandlerUrl;
        int slashPos = -1;
        while((slashPos = scriptName.indexOf('/', slashPos + 1)) != -1)
        {
            proxyInterfaceURL = PATH_UP + "/" + proxyInterfaceURL;
            proxyEngineURL = PATH_UP + "/" + proxyEngineURL;
            proxyUtilURL = PATH_UP + "/" + proxyUtilURL;
        }

        Module module = moduleManager.getModule(scriptName, true);
        MethodDeclaration[] methods = module.getMethods();
        StringBuffer buffer = new StringBuffer();

        buffer.append("<html>\n");
        buffer.append("<head>\n");
        buffer.append("  <title>DWR Test</title>\n");
        buffer.append("  <!-- These paths use .. so that they still work behind a path mapping proxy. The fully qualified version is more cut and paste friendly. -->\n");
        buffer.append("  <script type='text/javascript' src='" + proxyEngineURL + "'></script>\n");
        buffer.append("  <script type='text/javascript' src='" + proxyUtilURL + "'></script>\n");
        buffer.append("  <script type='text/javascript' src='" + proxyInterfaceURL + "'></script>\n");
        buffer.append("  <script type='text/javascript'>\n");
        buffer.append("  function objectEval(text)\n");
        buffer.append("  {\n");
        buffer.append("    // eval() breaks when we use it to get an object using the { a:42, b:'x' }\n");
        buffer.append("    // syntax because it thinks that { and } surround a block and not an object\n");
        buffer.append("    // So we wrap it in an array and extract the first element to get around\n");
        buffer.append("    // this.\n");
        buffer.append("    // This code is only needed for interpreting the parameter input fields,\n");
        buffer.append("    // so you can ignore this for normal use.\n");
        buffer.append("    // The regex = [start of line][whitespace]{[stuff]}[whitespace][end of line]\n");
        buffer.append("    text = text.replace(/\\n/g, ' ');\n");
        buffer.append("    text = text.replace(/\\r/g, ' ');\n");
        buffer.append("    if (text.match(/^\\s*\\{.*\\}\\s*$/))\n");
        buffer.append("    {\n");
        buffer.append("      text = '[' + text + '][0]';\n");
        buffer.append("    }\n");
        buffer.append("    return eval(text);\n");
        buffer.append("  }\n");
        buffer.append("  </script>\n");
        buffer.append("  <style>\n");
        buffer.append("    input.itext { font-size: smaller; background: #E4E4E4; border: 0; }\n");
        buffer.append("    input.ibutton { font-size: xx-small; border: 1px outset; margin: 0px; padding: 0px; }\n");
        buffer.append("    span.reply { background: #ffffdd; white-space: pre; }\n");
        buffer.append("    span.warning { font-size: smaller; color: red; }\n");
        buffer.append("  </style>\n");
        buffer.append("</head>\n");
        buffer.append("<body onload='dwr.util.useLoadingMessage()'>\n");

        buffer.append("<h2>Methods For: " + scriptName + " (" + module.toString() + ")</h2>\n");
        buffer.append("<p>To use this class in your javascript you will need the following script includes:</p>\n");
        buffer.append("<pre>\n");
        buffer.append("  &lt;script type='text/javascript' src='<a href='" + engineURL + "'>" + engineURL + "</a>'&gt;&lt;/script&gt;\n");
        buffer.append("  &lt;script type='text/javascript' src='<a href='" + interfaceURL + "'>" + interfaceURL + "</a>'&gt;&lt;/script&gt;\n");
        buffer.append("</pre>\n");

        buffer.append("<p>In addition there is an optional utility script:</p>\n");
        buffer.append("<pre>\n");
        buffer.append("  &lt;script type='text/javascript' src='<a href='" + utilURL + "'>" + utilURL + "</a>'&gt;&lt;/script&gt;\n");
        buffer.append("</pre>\n");

        buffer.append("<p>Replies from DWR are shown with a yellow background if they are simple or in an alert box otherwise.<br/>\n");
        buffer.append("The inputs are evaluated as Javascript so strings must be quoted before execution.</p>\n");

        for (int i = 0; i < methods.length; i++)
        {
            MethodDeclaration method = methods[i];
            String methodName = method.getName();

            // Is it on the list of banned names
            if (JavascriptUtil.isReservedWord(methodName))
            {
                buffer.append("<li style='color: #88A;'>" + methodName + "() is not available because it is a reserved word.</li>\n");
                continue;
            }

            buffer.append("<li>\n");
            buffer.append("  " + methodName + '(');

            Class<?>[] paramTypes = method.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++)
            {
                Class<?> paramType = paramTypes[j];

                // The special type that we handle transparently
                if (LocalUtil.isServletClass(paramType))
                {
                    buffer.append("AUTO");
                }
                else
                {
                    String value = "";
                    if (paramType == String.class)
                    {
                        value = "\"\"";
                    }
                    else if (paramType == Boolean.class || paramType == Boolean.TYPE)
                    {
                        value = "true";
                    }
                    else if (paramType == Integer.class || paramType == Integer.TYPE || paramType == Short.class || paramType == Short.TYPE
                        || paramType == Long.class || paramType == Long.TYPE || paramType == Byte.class || paramType == Byte.TYPE)
                    {
                        value = "0";
                    }
                    else if (paramType == Float.class || paramType == Float.TYPE || paramType == Double.class || paramType == Double.TYPE)
                    {
                        value = "0.0";
                    }
                    else if (paramType.isArray() || Collection.class.isAssignableFrom(paramType))
                    {
                        value = "[]";
                    }
                    else if (Map.class.isAssignableFrom(paramType))
                    {
                        value = "{}";
                    }

                    buffer.append("    <input class='itext' type='text' size='10' value='" + value + "' id='p" + i + j + "' title='Will be converted to: " + paramType.getName() + "'/>");
                }

                buffer.append(j == paramTypes.length - 1 ? "" : ", \n");
            }
            buffer.append("  );\n");

            String onclick = (LocalUtil.isJavaIdentifierWithPackage(scriptName) ? scriptName : "dwr.engine._getObject(\"" + scriptName + "\")") + "." + methodName + "(";
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (!LocalUtil.isServletClass(paramTypes[j]))
                {
                    onclick += "objectEval($(\"p" + i + j + "\").value), ";
                }
            }
            onclick += "reply" + i + ");";

            buffer.append("  <input class='ibutton' type='button' onclick='" + onclick + "' value='Execute'  title='Calls " + scriptName + '.' + methodName + "(). View source for details.'/>\n");

            buffer.append("  <script type='text/javascript'>\n");
            buffer.append("    var reply" + i + " = function(data)\n");
            buffer.append("    {\n");
            buffer.append("      if (data != null && typeof data == 'object') alert(dwr.util.toDescriptiveString(data, 2));\n");
            buffer.append("      else dwr.util.setValue('d" + i + "', dwr.util.toDescriptiveString(data, 1));\n");
            buffer.append("    }\n");
            buffer.append("  </script>\n");
            buffer.append("  <span id='d" + i + "' class='reply'></span>\n");

            // Print a warning if this method is overloaded
            boolean overloaded = false;
            for (int j = 0; j < methods.length; j++)
            {
                if (j != i && methods[j].getName().equals(methodName))
                {
                    overloaded = true;
                }
            }
            if (overloaded)
            {
                buffer.append("<br/><span class='warning'>(Warning: overloaded methods are not recommended. See <a href='#overloadedMethod'>below</a>)</span>\n");
            }

            // Print a warning if the method uses un-marshallable types
            for (Class<?> paramType1 : paramTypes)
            {
                if (!converterManager.isConvertable(paramType1))
                {
                    buffer.append("<br/><span class='warning'>(Warning: No Converter for " + paramType1.getName() + ". See <a href='#missingConverter'>below</a>)</span>\n");
                }
            }

            if (!converterManager.isConvertable(method.getReturnType()))
            {
                buffer.append("<br/><span class='warning'>(Warning: No Converter for " + method.getReturnType().getName() + ". See <a href='#missingConverter'>below</a>)</span>\n");
            }

            // See also the call to getReasonToNotExecute() above
            try
            {
                accessControl.assertGeneralDisplayable(scriptName, method);
            }
            catch (SecurityException ex)
            {
                buffer.append("<br/><span class='warning'>(Warning: " + methodName + "() is excluded: " + ex.getMessage() + ". See <a href='#excludedMethod'>below</a>)</span>\n");
            }

            // We don't need to call assertExecutionIsPossible() because those
            // checks should be done by assertIsDisplayable() above
            // accessControl.assertExecutionIsPossible(creator, scriptName, method);

            buffer.append("</li>\n");
        }

        buffer.append("</ul>\n");

        buffer.append("<h2>Other Links</h2>\n");
        buffer.append("<ul>\n");
        buffer.append("<li>Back to <a href='" + root + "/'>module index</a>.</li>\n");
        buffer.append("</ul>\n");

        synchronized (scriptCache)
        {
            String output = scriptCache.get(PathConstants.FILE_HELP);
            if (output == null)
            {
                InputStream raw = LocalUtil.getInternalResourceAsStream(DwrConstants.PACKAGE_PATH + PathConstants.FILE_HELP);
                if (raw == null)
                {
                    log.error("Missing file " + PathConstants.FILE_HELP + ". Check the dwr.jar file was built to include html files.");
                    output = "<p>Failed to read help text from resource file. Check dwr.jar is built to include html files.</p>";
                }
                else
                {
                    BufferedReader in = new BufferedReader(new InputStreamReader(raw));
                    try
                    {
                        StringWriter writer = new StringWriter();
                        CopyUtils.copy(in, writer);
                        output = writer.toString();
                    }
                    catch (IOException ex)
                    {
                        log.error("Failed to read help text from resource file.", ex);
                        output = "Failed to read help text from resource file.";
                    }
                }

                scriptCache.put(PathConstants.FILE_HELP, output);
            }

            buffer.append(output);
        }

        buffer.append("</body></html>\n");

        return buffer.toString();
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.DebugPageGenerator#generateInterfaceUrl(java.lang.String, java.lang.String)
     */
    @Deprecated
    public String generateInterfaceUrl(String root, String scriptName)
    {
        return root + interfaceHandlerUrl + scriptName + PathConstants.EXTENSION_JS;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.DebugPageGenerator#generateEngineUrl(java.lang.String)
     */
    @Deprecated
    public String generateEngineUrl(String root)
    {
        return root + engineHandlerUrl;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.DebugPageGenerator#generateLibraryUrl(java.lang.String, java.lang.String)
     */
    @Deprecated
    public String generateLibraryUrl(String root, String library)
    {
        return root + library;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.DebugPageGenerator#getAvailableLibraries()
     */
    @Deprecated
    public Collection<String> getAvailableLibraries()
    {
        if (availableLibraries == null)
        {
            availableLibraries = Collections.unmodifiableCollection(Arrays.asList(utilHandlerUrl));
        }

        return availableLibraries;
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
     * Accessor for the ModuleManager that we configure
     * @param moduleManager
     */
    public void setModuleManager(ModuleManager moduleManager)
    {
        this.moduleManager = moduleManager;
    }

    /**
     * Accessor for the security manager
     * @param accessControl The accessControl to set.
     */
    public void setAccessControl(AccessControl accessControl)
    {
        this.accessControl = accessControl;
    }

    /**
     * @param engineHandlerUrl the engineHandlerUrl to set
     */
    public void setEngineHandlerUrl(String engineHandlerUrl)
    {
        this.engineHandlerUrl = engineHandlerUrl;
    }

    /**
     * @param utilHandlerUrl the utilHandlerUrl to set
     */
    public void setUtilHandlerUrl(String utilHandlerUrl)
    {
        this.utilHandlerUrl = utilHandlerUrl;
    }

    /**
     * @param testHandlerUrl the testHandlerUrl to set
     */
    public void setTestHandlerUrl(String testHandlerUrl)
    {
        this.testHandlerUrl = testHandlerUrl;
    }

    /**
     * Setter for the URL that this handler available on
     * @param interfaceHandlerUrl the interfaceHandlerUrl to set
     */
    public void setInterfaceHandlerUrl(String interfaceHandlerUrl)
    {
        this.interfaceHandlerUrl = interfaceHandlerUrl;
    }

    /**
     * Setter for debug enabling
     * @param debug
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * The URL for the {@link org.directwebremoting.servlet.EngineHandler}
     */
    protected String engineHandlerUrl;

    /**
     * The URL for the {@link org.directwebremoting.ui.servlet.UtilHandler}
     */
    protected String utilHandlerUrl;

    /**
     * The URL for the {@link org.directwebremoting.servlet.TestHandler}
     */
    protected String testHandlerUrl;

    /**
     * What URL is this handler available on?
     */
    protected String interfaceHandlerUrl;

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;

    /**
     * How we create new beans
     */
    protected ModuleManager moduleManager = null;

    /**
     * The security manager
     */
    protected AccessControl accessControl = null;

    /**
     * We cache the script output for speed
     */
    protected final Map<String, String> scriptCache = new HashMap<String, String>();

    /**
     * For getAvailableLibraries() - just a RO Collection that currently returns
     * only util.js, but may be expanded in the future.
     */
    private Collection<String> availableLibraries = null;

    /**
     * Debug setting
     */
    private boolean debug = false;

    /**
     * 2 dots
     */
    private static final String PATH_UP = "..";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultDebugPageGenerator.class);
}
