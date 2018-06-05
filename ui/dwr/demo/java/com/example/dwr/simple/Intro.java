package com.example.dwr.simple;

import java.io.IOException;

import javax.servlet.ServletException;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.util.VersionUtil;

/**
 * Used by the default webapp landing page to check basic functionality
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Intro
{
    /**
     * A simple test that the DWR is working. Used by the front page.
     * @return The text of the insert.html page
     * @throws IOException From {@link WebContext#forwardToString(String)}
     * @throws ServletException From {@link WebContext#forwardToString(String)}
     */
    public String[] getInsert() throws ServletException, IOException
    {
        return new String[]
        {
            WebContextFactory.get().forwardToString("/insert.html"),
            VersionUtil.getLabel(),
        };
    }
}
