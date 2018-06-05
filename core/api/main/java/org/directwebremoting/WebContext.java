package org.directwebremoting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Class to enable us to access servlet parameters.
 * WebContext is only available from a DWR thread. If you need to access web
 * data from a non-DWR thread, use the superclass, {@link ServerContext}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface WebContext extends ServerContext
{
    /**
     * Get the script session that represents the currently viewed page in the
     * same way that an HttpSession represents a cookie.
     * <p>If the DWR thread that gave rise to this {@link WebContext} is as a
     * result of a JSON call, this method will throw an UnsupportedOperationException
     * @return A browser object for this user
     * @throws UnsupportedOperationException If this is part of a JSON call
     */
    ScriptSession getScriptSession() throws UnsupportedOperationException;

    /**
     * Returns the partial-URL of the page from which the current thread
     * originated.
     * This string starts from (and includes) the application's context path,
     * and therefore excludes the scheme, host, and port of the URL.
     * If the URL has a query string (starting with and including a '?'
     * character) and/or an in-page location (starting with and including a '#'
     * character), these portions will be excluded. If the URL has a session
     * identifier, i.e. via an application server's URL-rewriting behavior, this
     * may not necessarily be excluded. e.g. Tomcat's default is to use
     * ";jsessionid=123...".
     * NOTE: this means that DWR will consider a session-appended URL to be
     * distinct from a non-session-appended URL.
     * @see org.directwebremoting.extend.PageNormalizer for details on how pages
     * are converted from external form to this form.
     * @throws UnsupportedOperationException If this is part of a JSON call
     */
    String getCurrentPage() throws UnsupportedOperationException;

    /**
     * Returns the current session associated with this request, or if the
     * request does not have a session, creates one.
     * @return Returns the http session.
     * @see HttpServletRequest#getSession()
     */
    HttpSession getSession();

    /**
     * Returns the current HttpSession associated with this request or, if
     * there is no current session and create is true, returns a new session.
     * If create is false and the request has no valid HttpSession, this method
     * returns null.
     * @param create false to return null if there's no current session
     * @return the session associated with this request
     * @see HttpServletRequest#getSession(boolean)
     */
    HttpSession getSession(boolean create);

    /**
     * Accessor for the http request information.
     * @return Returns the request.
     */
    HttpServletRequest getHttpServletRequest();

    /**
     * Accessor for the http response bean.
     * <p>You can't use this request to directly reply to the response or to add
     * headers or cookies.
     * @return Returns the response.
     */
    HttpServletResponse getHttpServletResponse();

    /**
     * An attribute used by {@link WebContext#forwardToString(String)} to inform
     * anyone that wants to know that this is a request from DWR.
     */
    public static final String ATTRIBUTE_DWR = "org.directwebremoting";

    /**
     * Forward a request to a given URL and catch the data written to it.
     * It is possible to distinguish requests that arrive normally and requests
     * that come from a DWR forwardToString() by the presence of a request
     * attribute. Before the request is forwarded, DWR will call:
     * <pre>
     * request.setAttribute(WebContext.ATTRIBUTE_DWR, Boolean.TRUE);
     * </pre>
     * @param url The URL to forward to
     * @return The text that results from forwarding to the given URL
     * @throws IOException if the target resource throws this exception
     * @throws ServletException if the target resource throws this exception
     * @throws IllegalStateException if the response was already committed
     */
    String forwardToString(String url) throws ServletException, IOException;
}
