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
package org.directwebremoting.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.DebugPageGenerator;
import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.servlet.EngineHandler;
import org.directwebremoting.servlet.PathConstants;
import org.directwebremoting.servlet.UtilHandler;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.Messages;

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
        if (!creatorManager.isDebug())
        {
            log.warn("Failed attempt to access test pages outside of debug mode. Set the debug init-parameter to true to enable.");
            throw new SecurityException(Messages.getString("DefaultDebugPageGenerator.AccessDenied"));
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append("<html>\n");
        buffer.append("<head><title>DWR Test Index</title></head>\n");
        buffer.append("<body>\n");

        buffer.append("<h2>Classes known to DWR:</h2>\n");
        buffer.append("<ul>\n");
        for (Iterator it = creatorManager.getCreatorNames().iterator(); it.hasNext();)
        {
            String name = (String) it.next();
            Creator creator = creatorManager.getCreator(name);

            buffer.append("<li><a href='");
            buffer.append(root);
            buffer.append(testHandlerUrl);
            buffer.append(name);
            buffer.append("'>");
            buffer.append(name);
            buffer.append("</a> (");
            buffer.append(creator.getType().getName());
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
        if (!creatorManager.isDebug())
        {
            log.warn("Failed attempt to access test pages outside of debug mode. Set the debug init-parameter to true to enable.");
            throw new SecurityException(Messages.getString("DefaultAccessControl.AccessDenied"));
        }

        String interfaceURL = root + interfaceHandlerUrl + scriptName + PathConstants.EXTENSION_JS;
        String engineURL = root + engineHandlerUrl;
        String utilURL = root + utilHandlerUrl;

        String proxyInterfaceURL = PATH_UP + interfaceHandlerUrl + scriptName + PathConstants.EXTENSION_JS;
        String proxyEngineURL = PATH_UP + engineHandlerUrl;
        String proxyUtilURL = PATH_UP + utilHandlerUrl;

        Creator creator = creatorManager.getCreator(scriptName);
        Method[] methods = creator.getType().getMethods();
        StringBuffer buffer = new StringBuffer();

        buffer.append("<html>\n");
        buffer.append("<head>\n");
        buffer.append("  <title>DWR Test</title>\n");
        buffer.append("  <!-- These paths use .. so that they still work behind a path mapping proxy. The fully qualified version is more cut and paste friendly. -->\n");
        buffer.append("  <script type='text/javascript' src='" + proxyInterfaceURL + "'></script>\n");
        buffer.append("  <script type='text/javascript' src='" + proxyEngineURL + "'></script>\n");
        buffer.append("  <script type='text/javascript' src='" + proxyUtilURL + "'></script>\n");
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
        buffer.append(BLANK);

        buffer.append("<h2>Methods For: " + scriptName + " (" + creator.getType().getName() + ")</h2>\n");
        buffer.append("<p>To use this class in your javascript you will need the following script includes:</p>\n");
        buffer.append("<pre>\n");
        buffer.append("  &lt;script type='text/javascript' src='<a href='" + interfaceURL + "'>" + interfaceURL + "</a>'&gt;&lt;/script&gt;\n");
        buffer.append("  &lt;script type='text/javascript' src='<a href='" + engineURL + "'>" + engineURL + "</a>'&gt;&lt;/script&gt;\n");
        buffer.append("</pre>\n");

        buffer.append("<p>In addition there is an optional utility script:</p>\n");
        buffer.append("<pre>\n");
        buffer.append("  &lt;script type='text/javascript' src='<a href='" + utilURL + "'>" + utilURL + "</a>'&gt;&lt;/script&gt;\n");
        buffer.append("</pre>\n");

        buffer.append("<p>Replies from DWR are shown with a yellow background if they are simple or in an alert box otherwise.<br/>\n");
        buffer.append("The inputs are evaluated as Javascript so strings must be quoted before execution.</p>\n");

        buffer.append("<p>There are " + methods.length + " declared methods:</p><ul>\n");

        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            String methodName = method.getName();

            // See also the call to getReasonToNotExecute() above
            try
            {
                accessControl.assertIsDisplayable(creator, scriptName, method);
            }
            catch (SecurityException ex)
            {
                buffer.append(BLANK);
                buffer.append("<li style='color: #A88;'>  " + methodName + "() is not available: " + ex.getMessage() + "</li>\n");
                if (!allowImpossibleTests)
                {
                    continue;
                }
            }

            // Is it on the list of banned names
            if (JavascriptUtil.isReservedWord(methodName))
            {
                buffer.append(BLANK);
                buffer.append("<li style='color: #88A;'>" + methodName + "() is not available because it is a reserved word.</li>\n");
                continue;
            }

            buffer.append(BLANK);
            buffer.append("<li>\n");
            buffer.append("  " + methodName + '(');

            Class[] paramTypes = method.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++)
            {
                Class paramType = paramTypes[j];

                // The special type that we handle transparently
                if (LocalUtil.isServletClass(paramType))
                {
                    buffer.append("AUTO");
                }
                else
                {
                    String value = BLANK;
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

                buffer.append(j == paramTypes.length - 1 ? BLANK : ", \n");
            }
            buffer.append("  );\n");

            String onclick = scriptName + '.' + methodName + "(";
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
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (!converterManager.isConvertable(paramTypes[j]))
                {
                    buffer.append("<br/><span class='warning'>(Warning: No Converter for " + paramTypes[j].getName() + ". See <a href='#missingConverter'>below</a>)</span>\n");
                }
            }

            if (!converterManager.isConvertable(method.getReturnType()))
            {
                buffer.append("<br/><span class='warning'>(Warning: No Converter for " + method.getReturnType().getName() + ". See <a href='#missingConverter'>below</a>)</span>\n");
            }

            // See also the call to getReasonToNotDisplay() above
            try
            {
                accessControl.assertExecutionIsPossible(creator, scriptName, method);
            }
            catch (SecurityException ex)
            {
                buffer.append("<br/><span class='warning'>(Security restructions in place: " + ex.getMessage() + ".)</span>\n");
            }
            
            buffer.append("</li>\n");
        }

        buffer.append(BLANK);
        buffer.append("</ul>\n");

        buffer.append("<h2>Other Links</h2>\n");
        buffer.append("<ul>\n");
        buffer.append("<li>Back to <a href='" + root + "/'>class index</a>.</li>\n");
        buffer.append("</ul>\n");

        synchronized (scriptCache)
        {
            String output = (String) scriptCache.get(PathConstants.FILE_HELP);
            if (output == null)
            {
                InputStream raw = getClass().getResourceAsStream(DwrConstants.PACKAGE + PathConstants.FILE_HELP);
                if (raw == null)
                {
                    log.error(Messages.getString("DefaultProcessor.MissingHelp", PathConstants.FILE_HELP));
                    output = "<p>Failed to read help text from resource file. Check dwr.jar is built to include html files.</p>";
                }
                else
                {
                    StringBuffer fileBuffer = new StringBuffer();

                    BufferedReader in = new BufferedReader(new InputStreamReader(raw));
                    while (true)
                    {
                        try
                        {
                            String line = in.readLine();
                            if (line == null)
                            {
                                break;
                            }

                            fileBuffer.append(line);
                            fileBuffer.append('\n');
                        }
                        catch (IOException ex)
                        {
                            fileBuffer.append(ex.toString());
                            fileBuffer.append('\n');
                            break;
                        }
                    }

                    output = fileBuffer.toString();
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
    public String generateInterfaceUrl(String root, String scriptName)
    {
        return root + interfaceHandlerUrl + scriptName + PathConstants.EXTENSION_JS;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.DebugPageGenerator#generateEngineUrl(java.lang.String)
     */
    public String generateEngineUrl(String root)
    {
        return root + engineHandlerUrl;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.DebugPageGenerator#generateLibraryUrl(java.lang.String, java.lang.String)
     */
    public String generateLibraryUrl(String root, String library)
    {
        return root + library;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.DebugPageGenerator#getAvailableLibraries()
     */
    public Collection getAvailableLibraries()
    {
        if (availableLibraries == null)
        {
            availableLibraries = Collections.unmodifiableCollection(Arrays.asList(new String[] { utilHandlerUrl }));
        }

        return availableLibraries;
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
     * Accessor for the DefaultCreatorManager that we configure
     * @param creatorManager The new DefaultConverterManager
     */
    public void setCreatorManager(CreatorManager creatorManager)
    {
        this.creatorManager = creatorManager;
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
     * Do we allow impossible tests for debug purposes
     * @param allowImpossibleTests The allowImpossibleTests to set.
     */
    public void setAllowImpossibleTests(boolean allowImpossibleTests)
    {
        this.allowImpossibleTests = allowImpossibleTests;
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
     * The URL for the {@link EngineHandler}
     */
    protected String engineHandlerUrl;

    /**
     * The URL for the {@link UtilHandler}
     */
    protected String utilHandlerUrl;

    /**
     * The URL for the {@link UtilHandler}
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
    protected CreatorManager creatorManager = null;

    /**
     * The security manager
     */
    protected AccessControl accessControl = null;

    /**
     * This helps us test that access rules are being followed
     */
    private boolean allowImpossibleTests = false;

    /**
     * We cache the script output for speed
     */
    protected final Map scriptCache = new HashMap();

    /**
     * For getAvailableLibraries() - just a RO Collection that currently returns
     * only util.js, but may be expanded in the future.
     */
    private Collection availableLibraries = null;

    /**
     * 2 dots
     */
    private static final String PATH_UP = "..";

    /**
     * Empty string
     */
    public static final String BLANK = "";

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultDebugPageGenerator.class);
}
