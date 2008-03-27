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
package uk.ltd.getahead.dwr.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ltd.getahead.dwr.AbstractDWRServlet;
import uk.ltd.getahead.dwr.AccessControl;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.Creator;
import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.Messages;
import uk.ltd.getahead.dwr.Processor;
import uk.ltd.getahead.dwr.util.JavascriptUtil;
import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Logger;

/**
 * Create a debug mode only test page for a single available class.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultTestProcessor implements Processor
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.Processor#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        if (!creatorManager.isDebug())
        {
            log.warn("Failed attempt to access test pages outside of debug mode. Set the debug init-parameter to true to enable."); //$NON-NLS-1$
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String pathInfo = req.getPathInfo();
        String servletPath = req.getServletPath();
        if (pathInfo == null)
        {
            pathInfo = req.getServletPath();
            servletPath = HtmlConstants.PATH_ROOT;
        }
        String scriptName = pathInfo;

        scriptName = LocalUtil.replace(scriptName, HtmlConstants.PATH_TEST, HtmlConstants.BLANK);
        scriptName = LocalUtil.replace(scriptName, HtmlConstants.PATH_ROOT, HtmlConstants.BLANK);

        Creator creator = creatorManager.getCreator(scriptName);

        Method[] methods = creator.getType().getMethods();

        resp.setContentType(HtmlConstants.MIME_HTML);
        PrintWriter out = resp.getWriter();

        String interfaceURL = req.getContextPath() + servletPath + HtmlConstants.PATH_INTERFACE + scriptName + HtmlConstants.EXTENSION_JS;
        String engineURL = req.getContextPath() + servletPath + HtmlConstants.FILE_ENGINE;
        String utilURL = req.getContextPath() + servletPath + HtmlConstants.FILE_UTIL;

        String proxyInterfaceURL = HtmlConstants.PATH_UP + HtmlConstants.PATH_INTERFACE + scriptName + HtmlConstants.EXTENSION_JS;
        String proxyEngineURL = HtmlConstants.PATH_UP + HtmlConstants.FILE_ENGINE;
        String proxyUtilURL = HtmlConstants.PATH_UP + HtmlConstants.FILE_UTIL;

        out.println("<html>"); //$NON-NLS-1$
        out.println("<head>"); //$NON-NLS-1$
        out.println("  <title>DWR Test</title>"); //$NON-NLS-1$
        out.println("  <!-- These paths use .. so that they still work behind a path mapping proxy. The fully qualified version is more cut and paste friendly. -->"); //$NON-NLS-1$
        out.println("  <script type='text/javascript' src='" + proxyInterfaceURL + "'></script>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("  <script type='text/javascript' src='" + proxyEngineURL + "'></script>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("  <script type='text/javascript' src='" + proxyUtilURL + "'></script>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("  <script type='text/javascript'>"); //$NON-NLS-1$
        out.println("  function objectEval(text)"); //$NON-NLS-1$
        out.println("  {"); //$NON-NLS-1$
        out.println("    // eval() breaks when we use it to get an object using the { a:42, b:'x' }"); //$NON-NLS-1$
        out.println("    // syntax because it thinks that { and } surround a block and not an object"); //$NON-NLS-1$;
        out.println("    // So we wrap it in an array and extract the first element to get around"); //$NON-NLS-1$
        out.println("    // this."); //$NON-NLS-1$
        out.println("    // The regex = [start of line][whitespace]{[stuff]}[whitespace][end of line]"); //$NON-NLS-1$
        out.println("    text = text.replace(/\\n/g, ' ');"); //$NON-NLS-1$
        out.println("    text = text.replace(/\\r/g, ' ');"); //$NON-NLS-1$
        out.println("    if (text.match(/^\\s*\\{.*\\}\\s*$/))"); //$NON-NLS-1$
        out.println("    {"); //$NON-NLS-1$
        out.println("      text = '[' + text + '][0]';"); //$NON-NLS-1$
        out.println("    }"); //$NON-NLS-1$
        out.println("    return eval(text);"); //$NON-NLS-1$
        out.println("  }"); //$NON-NLS-1$
        out.println("  </script>"); //$NON-NLS-1$
        out.println("  <style>"); //$NON-NLS-1$
        out.println("    input.itext { font-size: smaller; background: #E4E4E4; border: 0; }"); //$NON-NLS-1$
        out.println("    input.ibutton { font-size: xx-small; border: 1px outset; margin: 0px; padding: 0px; }"); //$NON-NLS-1$
        out.println("    span.reply { background: #ffffdd; white-space: pre; }"); //$NON-NLS-1$
        out.println("    span.warning { font-size: smaller; color: red; }"); //$NON-NLS-1$
        out.println("  </style>"); //$NON-NLS-1$
        out.println("</head>"); //$NON-NLS-1$
        out.println("<body onload='DWRUtil.useLoadingMessage()'>"); //$NON-NLS-1$
        out.println(HtmlConstants.BLANK);

        out.println("<h2>Methods For: " + scriptName + " (" + creator.getType().getName() + ")</h2>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        out.println("<p>To use this class in your javascript you will need the following script includes:</p>"); //$NON-NLS-1$
        out.println("<pre>"); //$NON-NLS-1$
        out.println("  &lt;script type='text/javascript' src='<a href='" + interfaceURL + "'>" + interfaceURL + "</a>'&gt;&lt;/script&gt;"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        out.println("  &lt;script type='text/javascript' src='<a href='" + engineURL + "'>" + engineURL + "</a>'&gt;&lt;/script&gt;"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        out.println("</pre>"); //$NON-NLS-1$

        out.println("<p>In addition there is an optional utility script:</p>"); //$NON-NLS-1$
        out.println("<pre>"); //$NON-NLS-1$
        out.println("  &lt;script type='text/javascript' src='<a href='" + utilURL + "'>" + utilURL + "</a>'&gt;&lt;/script&gt;"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        out.println("</pre>"); //$NON-NLS-1$

        out.println("<p>Replies from DWR are shown with a yellow background if they are simple or in an alert box otherwise.<br/>"); //$NON-NLS-1$
        out.println("The inputs are evaluated as Javascript so strings must be quoted before execution.</p>"); //$NON-NLS-1$

        out.println("<p>There are " + methods.length + " declared methods:</p><ul>"); //$NON-NLS-1$ //$NON-NLS-2$

        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            String methodName = method.getName();

            // Is it on the list of banned names
            if (jsutil.isReservedWord(methodName))
            {
                out.println(HtmlConstants.BLANK);
                out.println("<li style='color: #88A;'>" + methodName + "() is not available because it is a reserved word.</li>"); //$NON-NLS-1$ //$NON-NLS-2$
                continue;
            }

            out.println(HtmlConstants.BLANK);
            out.println("<li>"); //$NON-NLS-1$
            out.println("  " + methodName + '('); //$NON-NLS-1$

            Class[] paramTypes = method.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++)
            {
                Class paramType = paramTypes[j];

                // The special type that we handle transparently
                if (LocalUtil.isServletClass(paramType))
                {
                    out.print("AUTO"); //$NON-NLS-1$
                }
                else
                {
                    String value = HtmlConstants.BLANK;
                    if (paramType == String.class)
                    {
                        value = "\"\""; //$NON-NLS-1$
                    }
                    else if (paramType == Boolean.class || paramType == Boolean.TYPE)
                    {
                        value = "true"; //$NON-NLS-1$
                    }
                    else if (paramType == Integer.class || paramType == Integer.TYPE || paramType == Short.class || paramType == Short.TYPE
                        || paramType == Long.class || paramType == Long.TYPE || paramType == Byte.class || paramType == Byte.TYPE)
                    {
                        value = "0"; //$NON-NLS-1$
                    }
                    else if (paramType == Float.class || paramType == Float.TYPE || paramType == Double.class || paramType == Double.TYPE)
                    {
                        value = "0.0"; //$NON-NLS-1$
                    }
                    else if (paramType.isArray() || Collection.class.isAssignableFrom(paramType))
                    {
                        value = "[]"; //$NON-NLS-1$
                    }
                    else if (Map.class.isAssignableFrom(paramType))
                    {
                        value = "{}"; //$NON-NLS-1$
                    }

                    out.print("    <input class='itext' type='text' size='10' value='" + value + "' id='p" + i + j + "' title='Will be converted to: " + paramType.getName() + "'/>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                }

                out.println(j == paramTypes.length - 1 ? HtmlConstants.BLANK : ", "); //$NON-NLS-1$
            }
            out.println("  );"); //$NON-NLS-1$

            String onclick = scriptName + '.' + methodName + "("; //$NON-NLS-1$
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (!LocalUtil.isServletClass(paramTypes[j]))
                {
                    onclick += "objectEval($(\"p" + i + j + "\").value), "; //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            onclick += "reply" + i + ");"; //$NON-NLS-1$ //$NON-NLS-2$

            out.println("  <input class='ibutton' type='button' onclick='" + onclick + "' value='Execute'  title='Calls " + scriptName + '.' + methodName + "(). View source for details.'/>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            out.println("  <script type='text/javascript'>"); //$NON-NLS-1$
            out.println("    var reply" + i + " = function(data)"); //$NON-NLS-1$ //$NON-NLS-2$
            out.println("    {"); //$NON-NLS-1$
            out.println("      if (data != null && typeof data == 'object') alert(DWRUtil.toDescriptiveString(data, 2));"); //$NON-NLS-1$
            out.println("      else DWRUtil.setValue('d" + i + "', DWRUtil.toDescriptiveString(data, 1));"); //$NON-NLS-1$ //$NON-NLS-2$
            out.println("    }"); //$NON-NLS-1$
            out.println("  </script>"); //$NON-NLS-1$
            out.println("  <span id='d" + i + "' class='reply'></span>"); //$NON-NLS-1$ //$NON-NLS-2$

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
                out.println("<br/><span class='warning'>(Warning: overloaded methods are not recommended. See <a href='#overloadedMethod'>below</a> for more including notes on false positives)</span>"); //$NON-NLS-1$
            }

            // Print a warning if the method uses un-marshallable types
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (!converterManager.isConvertable(paramTypes[j]))
                {
                    out.println("<br/><span class='warning'>(Warning: No Converter for " + paramTypes[j].getName() + ". See <a href='#missingConverter'>below</a>)</span>"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }

            if (!converterManager.isConvertable(method.getReturnType()))
            {
                out.println("<br/><span class='warning'>(Warning: No Converter for " + method.getReturnType().getName() + ". See <a href='#missingConverter'>below</a>)</span>"); //$NON-NLS-1$ //$NON-NLS-2$
            }

            // Just warn if the code can't be displayed
            String warning = accessControl.getReasonToNotDisplay(req, creator, scriptName, method);
            if (warning != null)
            {
                out.println("<br/><span class='warning'>(Warning: Security restructions in place: " + warning + ".)</span>"); //$NON-NLS-1$ //$NON-NLS-2$
            }

            out.println("</li>"); //$NON-NLS-1$
        }

        out.println(HtmlConstants.BLANK);
        out.println("</ul>"); //$NON-NLS-1$

        out.println("<h2>Other Links</h2>"); //$NON-NLS-1$
        out.println("<ul>"); //$NON-NLS-1$
        out.println("<li>Back to <a href='" + req.getContextPath() + servletPath + "/'>class index</a>.</li>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("<li>Up to <a href='" + req.getContextPath() + "/'>top level of web app</a>.</li>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("</ul>"); //$NON-NLS-1$

        synchronized (scriptCache)
        {
            String output = (String) scriptCache.get(HtmlConstants.FILE_HELP);
            if (output == null)
            {
                StringBuffer buffer = new StringBuffer();

                InputStream raw = getClass().getResourceAsStream(AbstractDWRServlet.PACKAGE + HtmlConstants.FILE_HELP);
                if (raw == null)
                {
                    log.error(Messages.getString("DefaultProcessor.MissingHelp", HtmlConstants.FILE_HELP)); //$NON-NLS-1$
                    output = "<p>Failed to read help text from resource file. Check dwr.jar is built to include html files.</p>"; //$NON-NLS-1$
                }
                else
                {
                    BufferedReader in = new BufferedReader(new InputStreamReader(raw));
                    while (true)
                    {
                        String line = in.readLine();
                        if (line == null)
                        {
                            break;
                        }

                        buffer.append(line);
                        buffer.append('\n');
                    }

                    output = buffer.toString();
                }

                scriptCache.put(HtmlConstants.FILE_HELP, output);
            }

            out.println(output);
        }

        out.println("</body></html>"); //$NON-NLS-1$
        out.flush();
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
     * We cache the script output for speed
     */
    protected final Map scriptCache = new HashMap();

    /**
     * The means by which we strip comments
     */
    private JavascriptUtil jsutil = new JavascriptUtil();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DefaultTestProcessor.class);
}
