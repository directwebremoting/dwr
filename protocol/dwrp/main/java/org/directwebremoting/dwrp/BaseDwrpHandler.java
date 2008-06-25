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
package org.directwebremoting.dwrp;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Handler;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class BaseDwrpHandler implements Handler
{
    /**
     * Complain if GET is disallowed
     * @param batch
     */
    protected void checkGetAllowed(Batch batch)
    {
        if (!allowGetForSafariButMakeForgeryEasier && batch.isGet())
        {
            log.error("GET is disallowed because it makes request forgery easier. See http://getahead.org/dwr/security/allowGetForSafariButMakeForgeryEasier for more details.");
            throw new SecurityException("GET Disallowed");
        }
    }

    /**
     * Check that this request is not subject to a CSRF attack
     * @param request The original browser's request
     * @param batch The data that we've parsed from the request body
     */
    protected void checkNotCsrfAttack(HttpServletRequest request, Batch batch)
    {
        if (!crossDomainSessionSecurity)
        {
            return;
        }

        // A check to see that this isn't a csrf attack
        // http://en.wikipedia.org/wiki/Cross-site_request_forgery
        // http://www.tux.org/~peterw/csrf.txt
        if (request.isRequestedSessionIdValid() && request.isRequestedSessionIdFromCookie())
        {
            String headerSessionId = request.getRequestedSessionId();
            if (headerSessionId.length() > 0)
            {
                String bodySessionId = batch.getHttpSessionId();

                // Normal case; if same session cookie is supplied by DWR and
                // in HTTP header then all is ok
                if (headerSessionId.equals(bodySessionId))
                {
                    return;
                }

                // Weblogic adds creation time to the end of the incoming
                // session cookie string (even for request.getRequestedSessionId()).
                // Use the raw cookie instead
                for (Cookie cookie : request.getCookies())
                {
                    if (cookie.getName().equals(sessionCookieName) && cookie.getValue().equals(bodySessionId))
                    {
                        return;
                    }
                }

                // Otherwise error
                log.error("A request has been denied as a potential CSRF attack.");
                throw new SecurityException("CSRF Security Error");
            }
        }
    }

    /**
     * To we perform cross-domain session security checks?
     * @param crossDomainSessionSecurity the cross domain session security setting
     */
    public void setCrossDomainSessionSecurity(boolean crossDomainSessionSecurity)
    {
        this.crossDomainSessionSecurity = crossDomainSessionSecurity;
    }

    /**
     * To we perform cross-domain session security checks?
     */
    private boolean crossDomainSessionSecurity = true;

    /**
     * @param allowGetForSafariButMakeForgeryEasier Do we reduce security to help Safari
     */
    public void setAllowGetForSafariButMakeForgeryEasier(boolean allowGetForSafariButMakeForgeryEasier)
    {
        this.allowGetForSafariButMakeForgeryEasier = allowGetForSafariButMakeForgeryEasier;
    }

    /**
     * By default we disable GET, but this hinders old Safaris
     */
    private boolean allowGetForSafariButMakeForgeryEasier = false;

    /**
     * Alter the session cookie name from the default JSESSIONID.
     * @param sessionCookieName the sessionCookieName to set
     */
    public void setSessionCookieName(String sessionCookieName)
    {
        this.sessionCookieName = sessionCookieName;
    }

    /**
     * The session cookie name
     */
    private String sessionCookieName = "JSESSIONID";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BaseDwrpHandler.class);
}
