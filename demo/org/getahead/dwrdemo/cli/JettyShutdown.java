package org.getahead.dwrdemo.cli;

import org.mortbay.jetty.Server;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class JettyShutdown implements Runnable
{
    /**
     * 
     */
    private final Server server;

    /**
     * @param server
     */
    public JettyShutdown(Server server)
    {
        this.server = server;
    }

    public void run()
    {
        try
        {
            server.stop();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Register a shutdown hook for a server
     * @param server The server to shutdown
     */
    public static void addShutdownHook(Server server)
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new JettyShutdown(server)));
    }
}