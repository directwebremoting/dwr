package uk.ltd.getahead.dwr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.SAXException;

import uk.ltd.getahead.dwr.lang.StringEscapeUtils;
import uk.ltd.getahead.dwr.util.LocalUtil;
import uk.ltd.getahead.dwr.util.Log;

/**
 * This is the main servlet that handles all the requests to DWR.
 * <p>It is on the large side because it can't use technologies like JSPs etc
 * since it all needs to be deployed in a single jar file, and while it might be
 * possible to integrate Velocity or similar I think simplicity is more
 * important, and there are only 2 real pages both script heavy in this servlet
 * anyway.</p>
 * <p>There are 5 things to do, in the order that you come across them:
 * <li>The index test page that points at the classes</li>
 * <li>The class test page that lets you execute methods</li>
 * <li>The interface javascript that uses the engine to send requests</li>
 * <li>The engine javascript to form the iframe request and process replies</li>
 * <li>The exec 'page' that executes the method and returns data to the iframe</li>
 * </p>
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
        String logLevel = config.getInitParameter("logLevel");
        if (logLevel != null)
        {
            Log.setLevel(logLevel);
        }

        // Are we in debug mode?
        String debugStr = config.getInitParameter("debug");
        boolean debug = Boolean.valueOf(debugStr).booleanValue();

        configuration = new Configuration(debug);

        // Load the system config file
        try
        {
            InputStream in = getClass().getResourceAsStream("dwr.xml");
            configuration.addConfig(in);
        }
        catch (SAXException ex)
        {
            Log.fatal("Failed to parse dwr.xml", ex);
            throw new ServletException("Parse error reading from dwr.xml", ex);
        }

        // Find all the init params
        Enumeration en = config.getInitParameterNames();

        boolean foundConfig = false;

        // Loop through the ones that do exist
        while (en.hasMoreElements())
        {
            String name = (String) en.nextElement();
            if (name.startsWith("config"))
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

        converterManager = configuration.getConverterManager();
        creatorManager = configuration.getCreatorManager();
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
            ExecutionContext.setExecutionContext(req, resp, getServletConfig());
            Log.setExecutionContext(this);
    
            String pathinfo = req.getPathInfo();
            if (pathinfo == null || pathinfo.length() == 0 || pathinfo.equals("/"))
            {
                doIndex(req, resp);
            }
            else if (pathinfo != null && pathinfo.startsWith("/test/"))
            {
                doTest(req, resp);
            }
            else if (pathinfo != null && pathinfo.equalsIgnoreCase("/engine.js"))
            {
                doFile(req, resp, "engine.js");
            }
            else if (pathinfo != null && pathinfo.equalsIgnoreCase("/util.js"))
            {
                doFile(req, resp, "util.js");
            }
            else if (pathinfo != null && pathinfo.startsWith("/interface/"))
            {
                doInterface(req, resp);
            }
            else if (pathinfo != null && pathinfo.startsWith("/exec"))
            {
                doExec(req, resp);
            }
            else
            {
                Log.warn("Page not found. In debug/test mode try viewing /[WEB-APP]/dwr/");
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        finally
        {
            ExecutionContext.unset();
            Log.unsetExecutionContext();
        }
    }

    /**
     * Create an debug mode only index page to all the available classes
     * @param req The browsers request
     * @param resp The response channel
     * @throws IOException If writing to the output fails
     */
    private void doIndex(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        if (!configuration.isDebugMode())
        {
            Log.warn("Failed attempt to access index page outside of debug mode. Set the debug init-parameter to true to enable.");
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<head><title>DWR Test Index</title></head>");
        out.println("<body>");
        out.println("<h2>Classes known to DWR:</h2>");

        for (Iterator it = creatorManager.getCreatorNames().iterator(); it.hasNext();)
        {
            String name = (String) it.next();
            Creator creator = creatorManager.getCreator(name);
            out.println("<li><a href='" + req.getContextPath() + req.getServletPath() + "/test/" + name + "'>" + name + "</a> (" + creator.getType().getName() + ")</li>");
        }

        out.println("</body></html>");
        out.flush();
    }

    /**
     * Create a debug mode only test page for a single available class
     * @param req The browsers request
     * @param resp The response channel
     * @throws IOException If writing to the output fails
     */
    private void doTest(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        if (!configuration.isDebugMode())
        {
            Log.warn("Failed attempt to access test pages outside of debug mode. Set the debug init-parameter to true to enable.");
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String scriptname = req.getPathInfo();
        scriptname = LocalUtil.replace(scriptname, "/test/", "");
        scriptname = LocalUtil.replace(scriptname, "/", "");

        Creator creator = creatorManager.getCreator(scriptname);

        Method[] methods = creator.getType().getDeclaredMethods();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String interfaceURL = req.getContextPath() + req.getServletPath() + "/interface/" + scriptname + ".js";
        String engineURL = req.getContextPath() + req.getServletPath() + "/engine.js";

        out.println("<html>");
        out.println("<head>");
        out.println("  <title>DWR Test</title>");
        out.println("  <script type='text/javascript' src='" + interfaceURL + "'></script>");
        out.println("  <script type='text/javascript' src='" + engineURL + "'></script>");
        out.println("  <style>");
        out.println("    input.itext { font-size: smaller; background: #E4E4E4; border: 0; }");
        out.println("    input.ibutton { font-size: xx-small; border: 1px outset; margin: 0px; padding: 0px; }");
        out.println("    span.reply { background: #ffffdd; }");
        out.println("  </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("");
        out.println("<h2>Methods For: " + scriptname + " (" + creator.getType().getName() + ")</h2>");
        out.println("<p>Replies from DWR are shown with a yellow background.<br/>There are " + methods.length + " declared methods:<ul>");

        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];

            // Is it public
            if (!Modifier.isPublic(method.getModifiers()))
            {
                out.println("");
                out.println("<li style='color: #888;'>  " + method.getName() + "() is not available because it is not public.</li>");
                continue;
            }

            // Is it on the list of banned names
            if (configuration.isReservedWord(method.getName()))
            {
                out.println("");
                out.println("<li style='color: #888;'>" + method.getName() + "() is not available because it is a reserved word.</li>");
                continue;
            }

            out.println("");
            out.println("<li>");
            out.println("  "+method.getName()+"(");

            Class[] paramTypes = method.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++)
            {
                out.print("    <input class='itext' type='text' size='10' id='p"+i+j+"' title='input id=p"+i+j+"\r\nData will be converted to: "+paramTypes[j].getName()+"'/>");
                out.println(j == paramTypes.length - 1 ? "" : ", ");
            }
            out.println("  );");

            String onclick = scriptname+"."+method.getName()+"(reply"+i;
            for (int j = 0; j < paramTypes.length; j++)
            {
                onclick += ",document.getElementById(\"p"+i+j+"\").value";
            }
            onclick += ");";

            out.print("  <input class='ibutton' type='button' onclick='" + onclick + "' value='Execute'  title='onclick=" + onclick + "\r\nWhere reply" + i + " is a callback function (view source for more).'/>");

            out.println("  <script type='text/javascript'>");
            out.println("    var reply" + i + " = function(data)");
            out.println("    {");
            out.println("      document.getElementById('d" + i + "').innerHTML = data;");
            out.println("    }");
            out.println("  </script>");
            out.println("  <span id='d" + i + "' class='reply'></span>");

            // Print a warning if this method is overloaded
            boolean overridden = false;
            for (int j = 0; j < methods.length; j++)
            {
                if (j != i && methods[j].getName().equals(method.getName()))
                {
                    overridden = true;
                }
            }
            if (overridden)
            {
                out.println("<span style='font-size: smaller; color: red;'>(Warning: overridden methods are not recommended)</span>");
            }

            // Print a warning if the method uses un-marshallable types
            for (int j = 0; j < paramTypes.length; j++)
            {
                if (!converterManager.isConvertable(paramTypes[j]))
                {
                    out.println("<span style='font-size: smaller; color: red;'>(Warning: " + paramTypes[j].getName() + " param not marshallable)</span>");
                }
            }

            if (!converterManager.isConvertable(method.getReturnType()))
            {
                out.println("<span style='font-size: smaller; color: red;'>(Warning: " + method.getReturnType().getName() + " return type not marshallable)</span>");
            }

            out.println("</li>");
        }

        out.println("");
        out.println("</ul></p>");

        out.println("<h2>Other Links</h2>");
        out.println("<ul>");
        out.println("<li>Back to <a href='" + req.getContextPath() + req.getServletPath() + "'>class index</a>.</li>");
        out.println("<li>The Javascript <a href='" + interfaceURL + "'>interface</a> definition corresponding to your Java classes</li>");
        out.println("<li>The Javascript <a href='" + engineURL + "'>engine</a> Marshalls calls to the server. Used by the interface script. This is the same for all classes.</li>");
        out.println("</ul>");

        out.println("</body></html>");
        out.flush();
    }

    /**
     * Create some javascript interface code
     * @param req The browsers request
     * @param resp The response channel
     * @throws IOException If writing to the output fails
     */
    private void doInterface(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        String pathinfo = req.getPathInfo();
        pathinfo = LocalUtil.replace(pathinfo, "/interface/", "");
        pathinfo = LocalUtil.replace(pathinfo, ".js", "");
        Creator creator = creatorManager.getCreator(pathinfo);

        //resp.setContentType("text/javascript");
        PrintWriter out = resp.getWriter();
        out.println();
        out.println("// Methods for: "+creator.getType().getName());

        out.println();
        out.println(pathinfo+" = new Object();");

        Method[] methods = creator.getType().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];

            // Is it public
            if (!Modifier.isPublic(method.getModifiers()))
            {
                continue;
            }

            // Is it on the list of banned names
            if (configuration.isReservedWord(method.getName()))
            {
                continue;
            }

            if (i != 0)
            {
                out.print("\n");
            }
            out.print(pathinfo+"."+method.getName()+" = function(callback");
            Class[] paramTypes = method.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++)
            {
                out.print(", p"+j);
            }
            out.println(")");
            out.println("{");

            out.print("    dwrExecute(callback, '"+pathinfo+"', '"+method.getName()+"'");
            for (int j = 0; j < paramTypes.length; j++)
            {
                out.print(", p"+j);
            }
            out.println(");");

            out.println("}");
        }

        out.flush();
    }

    /**
     * Basically a file servlet component that does some <b>very limitted</b>
     * EL type processing on the file.
     * @param req The browsers request
     * @param resp The response channel
     * @param path The path to search for, process and output
     * @throws IOException If writing to the output fails
     */
    private void doFile(HttpServletRequest req, HttpServletResponse resp, String path) throws IOException
    {
        resp.setContentType("text/javascript");

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

                line = LocalUtil.replace(line, "${request.contextPath}", req.getContextPath());
                line = LocalUtil.replace(line, "${request.servletPath}", req.getServletPath());

                buffer.append(line);
                buffer.append("\n");
            }

            output = buffer.toString();
            scriptCache.put(path, output);
        }

        PrintWriter out = resp.getWriter();
        out.println(output);
        out.flush();
    }

    /**
     * Execute a remote Javascript request. 
     * @param req The browsers request
     * @param resp The response channel
     */
    private void doExec(HttpServletRequest req, HttpServletResponse resp)
    {
        ExecuteQuery eq = new ExecuteQuery(req, creatorManager, converterManager);

        try
        {
            Object reply = eq.execute();
            ScriptSetup ss = converterManager.convertFrom(reply);

            PrintWriter out = resp.getWriter();

            Log.info("Returning: id[" + eq.getId() + "] init[" + ss.initCode + "] assign[" + ss.assignCode + "] xml[" + eq.isXmlMode() + "]");

            LocalUtil.addNoCacheHeaders(resp);

            if (eq.isXmlMode())
            {
                resp.setContentType("text/xml");

                out.println(ss.initCode);
                out.println("var reply = " + ss.assignCode + ";");
                out.println("dwrHandleResponse(\"" + eq.getId() + "\", reply);");
            }
            else
            {
                resp.setContentType("text/html");

                out.println("<script type='text/javascript'>");
                out.println(ss.initCode);
                out.println("var reply = " + ss.assignCode + ";");
                out.println("window.parent.dwrHandleResponse(\"" + eq.getId() + "\", reply);");
                out.println("</script>");
            }

            out.flush();
        }
        catch (Throwable ex)
        {
            Log.warn("Erroring: id[" + eq.getId() + "] message[" + ex.getMessage() + "]", ex);

            try
            {
                PrintWriter out = resp.getWriter();
                String output = StringEscapeUtils.escapeJavaScript(ex.getMessage());

                if (eq.isXmlMode())
                {
                    resp.setContentType("text/xml");

                    out.println("dwrHandleError(\"" + eq.getId() + "\", \"" + output + "\");");
                }
                else
                {
                    resp.setContentType("text/html");

                    out.println("<script type='text/javascript'>");
                    out.println("window.parent.dwrHandleError(\"" + eq.getId() + "\", '" + output + "')");
                    out.println("</script>");
                }

                out.flush();
            }
            catch (IOException ex2)
            {
                Log.error("IO error: " + eq.getId(), ex2);
                return;
            }
        }
    }

    /**
     * Load a DWR config file.
     * @param configFile the config file to read
     * @throws ServletException If the extra checking of the config file fails
     */
    private void readFile(String configFile) throws ServletException
    {
        try
        {
            InputStream in = getServletContext().getResourceAsStream(configFile);
            if (in != null)
            {
                configuration.addConfig(in);
            }
            else
            {
                throw new ServletException("Could not find dwr config file at: " + configFile);
            }
        }
        catch (SAXException ex)
        {
            throw new ServletException(ex.getMessage());
        }
    }

    /**
     * The default dwr.xml file path
     */
    private static final String DEFAULT_DWR_XML = "WEB-INF/dwr.xml";

    /**
     * We cache the script output for speed
     */
    private Map scriptCache = new HashMap();

    /**
     * The local configuration settings
     */
    private Configuration configuration = null;

    /**
     * How we create new beans
     */
    private CreatorManager creatorManager = null;

    /**
     * How we convert parameters
     */
    private ConverterManager converterManager = null;
}
