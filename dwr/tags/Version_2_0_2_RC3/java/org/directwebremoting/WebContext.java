/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Class to enable us to access servlet parameters.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface WebContext extends ServerContext
{
    /**
     * Get the script session that represents the currently viewed page in the
     * same way that an HttpSession represents a cookie.
     * @return A browser object for this user
     */
    ScriptSession getScriptSession();

    /**
     * What is the URL of the current page.
     * This includes 'http://', up to (but not including) '?' or '#'
     * @return The URL of the current page
     */
    String getCurrentPage();

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

    /**
     * For system use only: This method allows the system to fill in the session
     * id and page id when they are discovered.
     * @param page The URL of the current page
     * @param scriptSessionId The session id passed in by the browser
     */
    void setCurrentPageInformation(String page, String scriptSessionId);
}
