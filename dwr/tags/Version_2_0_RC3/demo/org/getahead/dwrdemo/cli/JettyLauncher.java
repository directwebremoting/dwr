package org.getahead.dwrdemo.cli;

import org.directwebremoting.servlet.DwrServlet;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * JettyLauncher.
 */
public class JettyLauncher
{
    /**
     * Sets up and runs server.
     * @param args
     */
    public static void main(String[] args)
    {
        final Server server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.addConnector(connector);

        Context context = new Context(server, "/", Context.SESSIONS);

        ResourceHandler htmlHandler = new ResourceHandler();
        htmlHandler.setResourceBase("web");
        context.setHandler(htmlHandler);

        ServletHolder servletHolder = new ServletHolder(new DwrServlet());
        servletHolder.setInitParameter("activeReverseAjaxEnabled", "true");
        servletHolder.setInitParameter("debug", "true");
        context.addServlet(servletHolder, "/dwr/*");

        /*
        ContextHandler handler = new ContextHandler();
        handler.setContextPath("/");
        handler.setResourceBase("web");
        root.setHandler(handler);
        */

        try
        {
            // JettyShutdown.addShutdownHook(server);
            server.start();
            server.join();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
