package org.directwebremoting.impl.test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Bram Smeets
 */
public class TestCreatedObject
{
    /**
     * @param request
     * @param response
     * @param config
     * @param context
     * @param session
     */
    public void testMethodWithServletParameters(HttpServletRequest request, HttpServletResponse response, ServletConfig config, ServletContext context, HttpSession session)
    {
        Object ignore = request;
        ignore = response;
        ignore = config;
        ignore = context;
        ignore = session;
        session = (HttpSession) ignore;

        // do nothing
    }

    /**
     * A method with a reserved javascript word as name.
     */
    public void namespace()
    {
        // do nothing
    }
}
