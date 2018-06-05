package com.example.dwr.simple;

import java.io.IOException;

import javax.servlet.ServletException;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Some simple text demos
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Demo
{
    /**
     * Return a server side string to display on the client in real time
     * @param name The name of person to say hello to
     * @return A demo string
     */
    public String sayHello(String name)
    {
        return "Hello, " + name;
    }

    /**
     * Fetch a resource using forwardToString()
     * @return a demo HTML page
     * @throws ServletException If the servlet engine breaks
     * @throws IOException If the servlet engine breaks
     */
    public String getInclude() throws ServletException, IOException
    {
        WebContext wctx = WebContextFactory.get();
        return wctx.forwardToString("/simple/forward.html");
    }
}
