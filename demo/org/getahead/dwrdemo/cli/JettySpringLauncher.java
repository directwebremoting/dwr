package org.getahead.dwrdemo.cli;

import org.directwebremoting.spring.DwrSpringServlet;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * JettyLauncher.
 */
public class JettySpringLauncher
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

        ResourceHandler handler = new ResourceHandler();
        handler.setResourceBase("web");
        context.setHandler(handler);

        ServletHolder holder = new ServletHolder(new DwrSpringServlet());
        holder.setInitParameter("activeReverseAjaxEnabled", "true");
        holder.setInitParameter("debug", "true");

        GenericWebApplicationContext webapp = new GenericWebApplicationContext();
        webapp.setParent(new ClassPathXmlApplicationContext("org/getahead/dwrdemo/cli/spring.xml"));

        context.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webapp);
        context.addServlet(holder, "/dwr/*");

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
