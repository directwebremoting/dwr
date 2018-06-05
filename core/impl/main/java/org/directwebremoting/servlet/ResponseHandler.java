package org.directwebremoting.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.Handler;

/**
 * Decorating handler for configuring response headers such as caching and expiry.
 * The UrlProcessor automatically maps and applies these from configuration.
 * @author Mike Wilson
 */
public interface ResponseHandler
{
    /**
     * Decorate a response by setting headers and other meta-data.
     * @param handler
     * @param request
     * @param response
     */
    void handle(Handler handler, HttpServletRequest request, HttpServletResponse response);
}

