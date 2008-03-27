package uk.ltd.getahead.dwr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.ltd.getahead.dwr.lang.StringEscapeUtils;
import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Logger;
import uk.ltd.getahead.dwr.util.ServletLoggingOutput;
import uk.ltd.getahead.dwr.util.SourceUtil;

/**
 * This is the main servlet that handles all the requests to DWR.
 * <p>It is on the large side because it can't use technologies like JSPs etc
 * since it all needs to be deployed in a single jar file, and while it might be
 * possible to integrate Velocity or similar I think simplicity is more
 * important, and there are only 2 real pages both script heavy in this servlet
 * anyway.</p>
 * <p>There are 5 things to do, in the order that you come across them:</p>
 * <ul>
 * <li>The index test page that points at the classes</li>
 * <li>The class test page that lets you execute methods</li>
 * <li>The interface javascript that uses the engine to send requests</li>
 * <li>The engine javascript to form the iframe request and process replies</li>
 * <li>The exec 'page' that executes the method and returns data to the iframe</li>
 * </ul>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DWRServlet extends HttpServlet
{
    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        // How much logging do we do?
        String logLevel = config.getInitParameter(INIT_LOGLEVEL);
        if (logLevel != null)
        {
            ServletLoggingOutput.setLevel(logLevel);
        }

        // Maybe we want to override the ExecutionContext
        // This code is un-documented, and experimental. We'll keep it if it
        // turns out to be useful.
        String execCxName = config.getInitParameter(INIT_EXEC_CONTEXT);
        if (execCxName != null)
        {
            try
            {
                ExecutionContext.setImplementation(execCxName);
            }
            catch (Exception ex)
            {
                log.fatal("Invalid executionContext parameter", ex); //$NON-NLS-1$
                throw new ServletException(Messages.getString("DWRServlet.ExecutionContextInit", execCxName, ex)); //$NON-NLS-1$
            }
        }

        // Setup the creator manager
        String creatorMgrName = config.getInitParameter(INIT_CREATOR);
        if (creatorMgrName == null)
        {
            creatorMgrName = INIT_DEFAULT_CREATOR;
        }

        try
        {
            Class creatorMgrClass = Class.forName(creatorMgrName);
            creatorManager = (CreatorManager) creatorMgrClass.newInstance();
        }
        catch (Exception ex)
        {
            log.fatal("Failed to create the creator manager", ex); //$NON-NLS-1$
            throw new ServletException(Messages.getString("DWRServlet.CreatorManagerInit", creatorMgrName, ex)); //$NON-NLS-1$
        }

        // Setup the converter manager
        String converterMgrName = config.getInitParameter(INIT_CONVERTER);
        if (converterMgrName == null)
        {
            converterMgrName = INIT_DEFAULT_CONVERTER;
        }

        try
        {
            Class converterMgrClass = Class.forName(converterMgrName);
            converterManager = (ConverterManager) converterMgrClass.newInstance();
        }
        catch (Exception ex)
        {
            log.fatal("Failed to create the converter manager", ex); //$NON-NLS-1$
            throw new ServletException(Messages.getString("DWRServlet.ConverterManagerInit", converterMgrName, ex)); //$NON-NLS-1$
        }

        // Are we in debug mode?
        String debugStr = config.getInitParameter(INIT_DEBUG);
        boolean debug = Boolean.valueOf(debugStr).booleanValue();
        creatorManager.setDebug(debug);

        configuration = new Configuration();
        configuration.setConverterManager(converterManager);
        configuration.setCreatorManager(creatorManager);

        // Load the system config file
        InputStream in = getClass().getResourceAsStream(FILE_DWR_XML);
        try
        {
            configuration.addConfig(in);
        }
        catch (Exception ex)
        {
            throw new ServletException(Messages.getString("DWRServlet.ConfigError", FILE_DWR_XML), ex); //$NON-NLS-1$
        }

        // Find all the init params
        Enumeration en = config.getInitParameterNames();

        boolean foundConfig = false;

        // Loop through the ones that do exist
        while (en.hasMoreElements())
        {
            String name = (String) en.nextElement();
            if (name.startsWith(INIT_CONFIG))
            {
                foundConfig = true;

                // if the init param starts with "config" then try to load it
                String configFile = config.getInitParameter(name);
                readFile(configFile);
            }
        }

        // If there are none then use the default name
        if (!foundConfig)
        {
            readFile(DEFAULT_DWR_XML);
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        doPost(req, resp);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        try
        {
            ExecutionContext.setExecutionContext(req, resp, getServletConfig(), getServletContext());
            ServletLoggingOutput.setExecutionContext(this);

            String pathinfo = req.getPathInfo();
            if (pathinfo == null || pathinfo.length() == 0 || pathinfo.equals(PATH_ROOT))
            {
                doIndex(req, resp);
            }
            else if (pathinfo != null && pathinfo.startsWith(PATH_TEST))
            {
                doTest(req, resp);
            }
            else if (pathinfo != null && pathinfo.equalsIgnoreCase('/' + FILE_ENGINE))
            {
                doFile(resp, FILE_ENGINE, MIME_JS);
            }
            else if (pathinfo != null && pathinfo.equalsIgnoreCase('/' + FILE_UTIL))
            {
                doFile(resp, FILE_UTIL, MIME_JS);
            }
            else if (pathinfo != null && pathinfo.equalsIgnoreCase('/' + FILE_JSCP))
            {
                doFile(resp, FILE_JSCP, MIME_JS);
            }
            else if (pathinfo != null && pathinfo.startsWith(PATH_INTERFACE))
            {
                doInterface(req, resp);
            }
            else if (pathinfo != null && pathinfo.startsWith(PATH_EXEC))
            {
                doExec(req, resp);
            }
            else
            {
                log.warn("Page not found. In debug/test mode try viewing /[WEB-APP]/dwr/"); //$NON-NLS-1$
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        finally
        {
            ExecutionContext.unset();
            ServletLoggingOutput.unsetExecutionContext();
        }
    }

    /**
     * Create a debug mode only index page to all the available classes
     * @param req The browsers request
     * @param resp The response channel
     * @throws IOException If writing to the output fails
     */
    protected void doIndex(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        if (!creatorManager.isDebug())
        {
            log.warn("Failed attempt to access index page outside of debug mode. Set the debug init-parameter to true to enable."); //$NON-NLS-1$
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        resp.setContentType(MIME_HTML);

        PrintWriter out = resp.getWriter();

        out.println("<html>"); //$NON-NLS-1$
        out.println("<head><title>DWR Test Index</title></head>"); //$NON-NLS-1$
        out.println("<body>"); //$NON-NLS-1$

        out.println("<h2>Classes known to DWR:</h2>"); //$NON-NLS-1$
        out.println("<ul>"); //$NON-NLS-1$
        for (Iterator it = creatorManager.getCreatorNames().iterator(); it.hasNext();)
        {
            String name = (String) it.next();
            Creator creator = creatorManager.getCreator(name);
            out.println("<li><a href='" + req.getContextPath() + req.getServletPath() + PATH_TEST + name + "'>" + name + "</a> (" + creator.getType().getName() + ")</li>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        }
        out.println("</ul>"); //$NON-NLS-1$

        out.println("<h2>Other Links</h2>"); //$NON-NLS-1$
        out.println("<ul>"); //$NON-NLS-1$
        out.println("<li>Up to <a href='" + req.getContextPath() + "/'>top level of web app</a>.</li>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("</ul>"); //$NON-NLS-1$

        out.println("</body></html>"); //$NON-NLS-1$
        out.flush();
    }

    /**
     * Create a debug mode only test page for a single available class
     * @param req The browsers request
     * @param resp The response channel
     * @throws IOException If writing to the output fails
     * @PMD:REVIEWED:ExcessiveMethodLength: by Joe on 12/02/05 16:18
     */
    protected void doTest(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        if (!creatorManager.isDebug())
        {
            log.warn("Failed attempt to access test pages outside of debug mode. Set the debug init-parameter to true to enable."); //$NON-NLS-1$
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String scriptname = req.getPathInfo();
        scriptname = LocalUtil.replace(scriptname, PATH_TEST, BLANK);
        scriptname = LocalUtil.replace(scriptname, PATH_ROOT, BLANK);

        Creator creator = creatorManager.getCreator(scriptname);

        Method[] methods = creator.getType().getMethods();

        resp.setContentType(MIME_HTML);
        PrintWriter out = resp.getWriter();

        String interfaceURL = req.getContextPath() + req.getServletPath() + PATH_INTERFACE + scriptname + EXTENSION_JS;
        String engineURL = req.getContextPath() + req.getServletPath() + '/' + FILE_ENGINE;
        String utilURL = req.getContextPath() + req.getServletPath() + '/' + FILE_UTIL;

        out.println("<html>"); //$NON-NLS-1$
        out.println("<head>"); //$NON-NLS-1$
        out.println("  <title>DWR Test</title>"); //$NON-NLS-1$
        out.println("  <script type='text/javascript' src='" + interfaceURL + "'></script>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("  <script type='text/javascript' src='" + engineURL + "'></script>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("  <script type='text/javascript' src='" + utilURL + "'></script>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("  <style>"); //$NON-NLS-1$
        out.println("    input.itext { font-size: smaller; background: #E4E4E4; border: 0; }"); //$NON-NLS-1$
        out.println("    input.ibutton { font-size: xx-small; border: 1px outset; margin: 0px; padding: 0px; }"); //$NON-NLS-1$
        out.println("    span.reply { background: #ffffdd; }"); //$NON-NLS-1$
        out.println("  </style>"); //$NON-NLS-1$
        out.println("</head>"); //$NON-NLS-1$
        out.println("<body>"); //$NON-NLS-1$
        out.println(BLANK);

        out.println("<h2>Methods For: " + scriptname + " (" + creator.getType().getName() + ")</h2>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        out.println("<p>To use this class in your javascript you will need the following script includes:</p>"); //$NON-NLS-1$
        out.println("<pre>"); //$NON-NLS-1$
        out.println("  &lt;script type='text/javascript' src='<a href='" + interfaceURL + "'>" + interfaceURL + "</a>'&gt;&lt;/script&gt;"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        out.println("  &lt;script type='text/javascript' src='<a href='" + engineURL + "'>" + engineURL + "</a>'&gt;&lt;/script&gt;"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        out.println("</pre>"); //$NON-NLS-1$

        out.println("<p>In addition there is an optional utility script:</p>"); //$NON-NLS-1$
        out.println("<pre>"); //$NON-NLS-1$
        out.println("  &lt;script type='text/javascript' src='<a href='" + utilURL + "'>" + utilURL + "</a>'&gt;&lt;/script&gt;"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        out.println("</pre>"); //$NON-NLS-1$

        out.println("<p>Replies from DWR are shown with a yellow background.<br/>"); //$NON-NLS-1$
        out.println("The inputs are evaluated as Javascript so strings must be quoted before execution.</p>"); //$NON-NLS-1$

        out.println("<p>There are " + methods.length + " declared methods:</p><ul>"); //$NON-NLS-1$ //$NON-NLS-2$

        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            String methodName = method.getName();

            String reason = Factory.getDoorman().getReasonToNotExecute(req, creator, scriptname, method);
            if (reason != null)
            {
                out.println(BLANK);
                out.println("<li style='color: #A88;'>  " + methodName + "() is not available: " + reason + "</li>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                if (!allowImpossibleTests)
                {
                    continue;
                }
            }

            // Is it on the list of banned names
            if (configuration.isReservedWord(methodName))
            {
                out.println(BLANK);
                out.println("<li style='color: #88A;'>" + methodName + "() is not available because it is a reserved word.</li>"); //$NON-NLS-1$ //$NON-NLS-2$
                continue;
            }

            out.println(BLANK);
            out.println("<li>"); //$NON-NLS-1$
            out.println("  " + methodName + '('); //$NON-NLS-1$

            Class[] paramTypes = method.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++)
            {
                Class paramType = paramTypes[j];

                // The special type that we handle transparently
                if (isAutoFilled(paramType))
                {
                    out.print("AUTO"); //$NON-NLS-1$
                }
                else
                {
                    String value = BLANK; //$NON-NLS-1$
                    if (paramType == String.class)
                    {
                        value = "\"\""; //$NON-NLS-1$
                    }
                    else if (paramType == Boolean.class || paramType == Boolean.TYPE)
                    {
                        value = "true"; //$NON-NLS-1$
                    }
                    else if (paramType == Integer.class || paramType == Integer.TYPE ||
                             paramType == Short.class || paramType == Short.TYPE ||
                             paramType == Long.class || paramType == Long.TYPE ||
                             paramType == Byte.class || paramType == Byte.TYPE)
                    {
                        value = "0"; //$NON-NLS-1$
                    }
                    else if (paramType == Float.class || paramType == Float.TYPE ||
                             paramType == Double.class || paramType == Double.TYPE)
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

                out.println(j == paramTypes.length - 1 ? BLANK : ", "); //$NON-NLS-1$
            }
            out.println("  );"); //$NON-NLS-1$

            String onclick = scriptname + '.' + methodName + "(reply" + i; //$NON-NLS-1$
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (!isAutoFilled(paramTypes[j]))
                {
                    onclick += ",eval(document.getElementById(\"p" + i + j + "\").value)"; //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            onclick += ");"; //$NON-NLS-1$

            out.print("  <input class='ibutton' type='button' onclick='" + onclick + "' value='Execute'  title='Calls " + scriptname + '.' + methodName + "(). View source for details.'/>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            out.println("  <script type='text/javascript'>"); //$NON-NLS-1$
            out.println("    var reply" + i + " = function(data)"); //$NON-NLS-1$ //$NON-NLS-2$
            out.println("    {"); //$NON-NLS-1$
            out.println("      DWRUtil.setValue('d" + i + "', DWRUtil.toDescriptiveString(data));"); //$NON-NLS-1$ //$NON-NLS-2$
            out.println("    }"); //$NON-NLS-1$
            out.println("  </script>"); //$NON-NLS-1$
            out.println("  <span id='d" + i + "' class='reply'></span>"); //$NON-NLS-1$ //$NON-NLS-2$

            // Print a warning if this method is overloaded
            boolean overridden = false;
            for (int j = 0; j < methods.length; j++)
            {
                if (j != i && methods[j].getName().equals(methodName))
                {
                    overridden = true;
                }
            }
            if (overridden)
            {
                out.println("<br/><span style='font-size: smaller; color: red;'>(Warning: overridden methods are not recommended. See <a href='#overriddenMethod'>below</a>)</span>"); //$NON-NLS-1$
            }

            // Print a warning if the method uses un-marshallable types
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (!converterManager.isConvertable(paramTypes[j]))
                {
                    out.println("<br/><span style='font-size: smaller; color: red;'>(Warning: " + paramTypes[j].getName() + " param not marshallable. See <a href='#paramNotMarshallable'>below</a>)</span>"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }

            if (!converterManager.isConvertable(method.getReturnType()))
            {
                out.println("<br/><span style='font-size: smaller; color: red;'>(Warning: " + method.getReturnType().getName() + " return type not marshallable. See <a href='#returnNotMarshallable'>below</a>)</span>"); //$NON-NLS-1$ //$NON-NLS-2$
            }

            out.println("</li>"); //$NON-NLS-1$
        }

        out.println(BLANK);
        out.println("</ul>"); //$NON-NLS-1$

        out.println("<h2>Other Links</h2>"); //$NON-NLS-1$
        out.println("<ul>"); //$NON-NLS-1$
        out.println("<li>Back to <a href='" + req.getContextPath() + req.getServletPath() + "'>class index</a>.</li>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("<li>Up to <a href='" + req.getContextPath() + "/'>top level of web app</a>.</li>"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println("</ul>"); //$NON-NLS-1$

        synchronized (scriptCache)
        {
            String output = (String) scriptCache.get(FILE_HELP);
            if (output == null)
            {
                StringBuffer buffer = new StringBuffer();

                InputStream raw = getClass().getResourceAsStream(FILE_HELP);
                if (raw == null)
                {
                    log.error(Messages.getString("DWRServlet.MissingHelp", FILE_HELP)); //$NON-NLS-1$
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

                scriptCache.put(FILE_HELP, output);
            }

            out.println(output);
        }

        out.println("</body></html>"); //$NON-NLS-1$
        out.flush();
    }

    /**
     * Create some javascript interface code
     * @param req The browsers request
     * @param resp The response channel
     * @throws IOException If writing to the output fails
     */
    protected void doInterface(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String scriptname = req.getPathInfo();
        scriptname = LocalUtil.replace(scriptname, PATH_INTERFACE, BLANK);
        scriptname = LocalUtil.replace(scriptname, EXTENSION_JS, BLANK);
        Creator creator = creatorManager.getCreator(scriptname);

        //resp.setContentType("text/javascript");
        PrintWriter out = resp.getWriter();
        out.println();

        out.println("function " + scriptname + "() { }"); //$NON-NLS-1$ //$NON-NLS-2$
        out.println();

        Method[] methods = creator.getType().getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            String methodName = method.getName();

            String reason = Factory.getDoorman().getReasonToNotExecute(req, creator, scriptname, method);
            if (reason != null && !allowImpossibleTests)
            {
                continue;
            }

            // Is it on the list of banned names
            if (configuration.isReservedWord(methodName))
            {
                continue;
            }

            if (i != 0)
            {
                out.print('\n');
            }
            out.print(scriptname + '.' + methodName + " = function("); //$NON-NLS-1$
            Class[] paramTypes = method.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (!isAutoFilled(paramTypes[j]))
                {
                    out.print("p" + j + ", "); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            out.println("callback)"); //$NON-NLS-1$
            out.println('{');

            String path = req.getContextPath() + req.getServletPath();

            out.print("    DWREngine._execute('" + path + "', '" + scriptname + "', '" + methodName + "\', "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (isAutoFilled(paramTypes[j]))
                {
                    out.print("false, "); //$NON-NLS-1$
                }
                else
                {
                    out.print("p" + j + ", "); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            out.println("callback);"); //$NON-NLS-1$

            out.println('}');
        }

        out.flush();
    }

    /**
     * Basically a file servlet component that does some <b>very limitted</b>
     * EL type processing on the file. See the source for the cheat.
     * @param resp The response channel
     * @param path The path to search for, process and output
     * @param mimeType The mime type to use for this output file
     * @throws IOException If writing to the output fails
     */
    protected void doFile(HttpServletResponse resp, String path, String mimeType) throws IOException
    {
        resp.setContentType(mimeType);

        synchronized (scriptCache)
        {
            String output = (String) scriptCache.get(path);
            if (output == null)
            {
                StringBuffer buffer = new StringBuffer();

                InputStream raw = getClass().getResourceAsStream(path);
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

                if (mimeType.equals(MIME_JS) && compress)
                {
                    output = SourceUtil.compress(output);
                }

                scriptCache.put(path, output);
            }

            PrintWriter out = resp.getWriter();
            out.println(output);
            out.flush();
        }
    }

    /**
     * Execute a remote Javascript request.
     * @param req The browsers request
     * @param resp The response channel
     * @throws IOException If an error occurs in writing the output
     */
    protected void doExec(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        ExecuteQuery eq = new ExecuteQuery(req, creatorManager, converterManager);

        if (eq.isFailingBrowser())
        {
            resp.setContentType(MIME_HTML);

            PrintWriter out = resp.getWriter();
            out.println("//<script type='text/javascript'>"); //$NON-NLS-1$
            out.println("alert('Your browser sent a request that could not be understood.\\nIf you understand how Javascript works in your browser, please help us fix the problem.\\nSee the mailing lists at http://www.getahead.ltd.uk/dwr/ for more information.');"); //$NON-NLS-1$
            out.println("//</script>"); //$NON-NLS-1$
            out.flush();
            return;
        }

        Call[] calls = eq.execute();

        for (int i = 0; i < calls.length; i++)
        {
            Call call = calls[i];
            if (call.getThrowable() != null)
            {
                log.warn("Erroring: id[" + call.getId() + "] message[" + call.getThrowable().getMessage() + ']', call.getThrowable()); //$NON-NLS-1$ //$NON-NLS-2$
            }
            else
            {
                log.debug("Returning: id[" + call.getId() + "] init[" + call.getReply().getInitCode() + "] assign[" + call.getReply().getAssignCode() + "] xml[" + eq.isXmlMode() + ']'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            }
        }

        // We build the answer up in a StringBuffer because that makes is easier
        // to debug, and because that's only what the compiler does anyway.
        StringBuffer buffer = new StringBuffer();

        // if we are in html (iframe mode) we need to direct script to the parent
        String prefix = eq.isXmlMode() ? "" : "window.parent."; //$NON-NLS-1$ //$NON-NLS-2$

        // iframe mode starts as HTML, so get into script mode
        if (!eq.isXmlMode())
        {
            buffer.append("<script type='text/javascript'>\n"); //$NON-NLS-1$
        }

        for (int i = 0; i < calls.length; i++)
        {
            Call call = calls[i];
            if (call.getThrowable() != null)
            {
                String output = StringEscapeUtils.escapeJavaScript(call.getThrowable().getMessage());

                buffer.append(prefix);
                buffer.append("DWREngine._handleError('"); //$NON-NLS-1$
                buffer.append(call.getId());
                buffer.append("', '"); //$NON-NLS-1$
                buffer.append(output);
                buffer.append("');\n"); //$NON-NLS-1$
            }
            else
            {
                buffer.append(call.getReply().getInitCode());
                buffer.append('\n');

                buffer.append(prefix);
                buffer.append("DWREngine._handleResponse('"); //$NON-NLS-1$
                buffer.append(call.getId());
                buffer.append("', "); //$NON-NLS-1$
                buffer.append(call.getReply().getAssignCode());
                buffer.append(");\n"); //$NON-NLS-1$
            }
        }

        // iframe mode needs to get out of script mode
        if (!eq.isXmlMode())
        {
            buffer.append("</script>\n"); //$NON-NLS-1$
        }

        String reply = buffer.toString();
        log.debug(reply);

        // LocalUtil.addNoCacheHeaders(resp);
        resp.setContentType(eq.isXmlMode() ? MIME_XML : MIME_HTML);
        PrintWriter out = resp.getWriter();
        out.print(reply);
        out.flush();
    }

    /**
     * Load a DWR config file.
     * @param configFile the config file to read
     * @throws ServletException If the extra checking of the config file fails
     */
    protected void readFile(String configFile) throws ServletException
    {
        try
        {
            InputStream in = getServletContext().getResourceAsStream(configFile);
            if (in == null)
            {
                throw new ServletException(Messages.getString("DWRServlet.MissingFile", configFile)); //$NON-NLS-1$
            }

            configuration.addConfig(in);
        }
        catch (Exception ex)
        {
            throw new ServletException(Messages.getString("DWRServlet.ConfigError", configFile), ex); //$NON-NLS-1$
        }
    }

    /**
     * Is this class one that we auto fill, so the user can ignore?
     * @param paramType The type to test
     * @return true if the type is a Servlet type
     */
    private boolean isAutoFilled(Class paramType)
    {
        return paramType == HttpServletRequest.class ||
               paramType == HttpServletResponse.class ||
               paramType == ServletConfig.class ||
               paramType == ServletContext.class ||
               paramType == HttpSession.class;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        log.error("Warning: DWRServlet has not been designed for use in collections."); //$NON-NLS-1$
        return super.hashCode();
    }

    protected static final String PATH_ROOT = "/"; //$NON-NLS-1$

    protected static final String PATH_EXEC = "/exec"; //$NON-NLS-1$

    protected static final String PATH_INTERFACE = "/interface/"; //$NON-NLS-1$

    protected static final String PATH_TEST = "/test/"; //$NON-NLS-1$

    protected static final String EXTENSION_JS = ".js"; //$NON-NLS-1$

    protected static final String FILE_UTIL = "util.js"; //$NON-NLS-1$

    protected static final String FILE_ENGINE = "engine.js"; //$NON-NLS-1$

    protected static final String FILE_JSCP = "jscp.js"; //$NON-NLS-1$

    protected static final String FILE_DWR_XML = "dwr.xml"; //$NON-NLS-1$

    protected static final String FILE_HELP = "help.html"; //$NON-NLS-1$

    protected static final String INIT_CONFIG = "config"; //$NON-NLS-1$

    protected static final String INIT_DEBUG = "debug"; //$NON-NLS-1$

    protected static final String INIT_LOGLEVEL = "logLevel"; //$NON-NLS-1$

    protected static final String INIT_EXEC_CONTEXT = "executionContext"; //$NON-NLS-1$

    protected static final String INIT_CONVERTER = "converterManager"; //$NON-NLS-1$

    protected static final String INIT_CREATOR = "creatorManager"; //$NON-NLS-1$

    protected static final String INIT_DEFAULT_CONVERTER = "uk.ltd.getahead.dwr.impl.DefaultConverterManager"; //$NON-NLS-1$

    protected static final String INIT_DEFAULT_CREATOR = "uk.ltd.getahead.dwr.impl.DefaultCreatorManager"; //$NON-NLS-1$

    /*
     * If we need to do more advanced char processing then we should consider
     * adding "; charset=utf-8" to the end of these 3 strings and altering the
     * marshalling to assume utf-8, which it currently does not.
     */
    protected static final String MIME_XML = "text/xml"; //$NON-NLS-1$

    protected static final String MIME_HTML = "text/html"; //$NON-NLS-1$

    protected static final String MIME_JS = "text/javascript"; //$NON-NLS-1$

    /**
     * Empty string
     */
    protected static final String BLANK = ""; //$NON-NLS-1$

    /**
     * This helps us test that access rules are being followed
     */
    private boolean allowImpossibleTests = false;

    /**
     * Do we retain comments and unneeded spaces in Javascript code?
     */
    private boolean compress = true;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(DWRServlet.class);

    /**
     * The default dwr.xml file path
     */
    protected static final String DEFAULT_DWR_XML = "/WEB-INF/dwr.xml"; //$NON-NLS-1$

    /**
     * We cache the script output for speed
     */
    protected final Map scriptCache = new HashMap();

    /**
     * The local configuration settings
     */
    protected Configuration configuration = null;

    /**
     * How we create new beans
     */
    protected CreatorManager creatorManager = null;

    /**
     * How we convert parameters
     */
    protected ConverterManager converterManager = null;
}
