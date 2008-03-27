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
        Server server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.addConnector(connector);

        Context htmlContext = new Context(server, "/", Context.SESSIONS);
        ResourceHandler htmlHandler = new ResourceHandler();
        htmlHandler.setResourceBase("web");
        htmlContext.setHandler(htmlHandler);

        Context servletContext = new Context(server, "/", Context.SESSIONS);
        ServletHolder holder = new ServletHolder(new DwrServlet());
        holder.setInitParameter("activeReverseAjaxEnabled", "true");
        holder.setInitParameter("debug", "true");
        servletContext.addServlet(holder, "/dwr/*");
        servletContext.setResourceBase("web");

        try
        {
            JettyShutdown.addShutdownHook(server);
            server.start();
            server.join();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
