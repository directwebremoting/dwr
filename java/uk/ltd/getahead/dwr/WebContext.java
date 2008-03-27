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
package uk.ltd.getahead.dwr;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Class to enable us to access servlet parameters.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface WebContext
{
    /**
     * Get a browser object that we can use to identify this user across
     * separate requests.
     * @return A browser object for this user
     */
    Browser getBrowser();

    /**
     * Accessor for the IoC container.
     * @return The IoC container that created the interface implementations.
     */
    Container getContainer();

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
     * Accessor for the servlet config.
     * @return Returns the config.
     */
    ServletConfig getServletConfig();

    /**
     * Returns the ServletContext to which this session belongs.
     * @return The servlet context information.
     */
    ServletContext getServletContext();

    /**
     * Accessor for the http request information.
     * @return Returns the request.
     */
    HttpServletRequest getHttpServletRequest();

    /**
     * Accessor for the http response bean.
     * @return Returns the response.
     */
    HttpServletResponse getHttpServletResponse();

    /**
     * Forward a request to a given URL and catch the data written to it
     * @param url The URL to forward to
     * @return The text that results from forwarding to the given URL
     * @throws IOException if the target resource throws this exception
     * @throws ServletException if the target resource throws this exception
     * @throws IllegalStateException if the response was already committed
     */
    String forwardToString(String url) throws ServletException, IOException;

    /**
     * Fish the version number out of the dwr.properties file.
     * @return The current version number.
     */
    String getVersion();
}
