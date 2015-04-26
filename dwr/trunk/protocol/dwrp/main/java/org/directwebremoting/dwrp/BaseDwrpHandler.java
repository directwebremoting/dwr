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
import javax.servlet.http.HttpSession;

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
            log.error("GET is disallowed because it makes request forgery easier. See allowGetForSafariButMakeForgeryEasier setting for more details.");
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

        // It is only interesting to inspect requests that carry cookies, as CSRF
        // attacks are targeted at the state carried by other cookies
        if (request.getCookies() == null)
        {
            return;
        }

        // If there is a DWRSESSIONID cookie on the request then the
        // corresponding session value in the batch must match
        int dwrCookieCount = 0;
        int dwrCookieMatchCount = 0;
        for (Cookie cookie : request.getCookies())
        {
            if (cookie.getName().equals("DWRSESSIONID"))
            {
                dwrCookieCount++;
                if (cookie.getValue().equals(batch.getDwrSessionId()))
                {
                    dwrCookieMatchCount++;
                }
            }
        }

        // Issue error if no DWRSESSIONID cookie matches (probably an attack)
        if (dwrCookieCount > 0 && dwrCookieMatchCount == 0)
        {
            throwCsrfException();
        }

        // Issue warning in log if there were multiple and different cookies
        if (dwrCookieCount > 1 && dwrCookieMatchCount != dwrCookieCount)
        {
            log.warn("Multiple DWRSESSIONID cookies with different values in request.");
        }

        if (!extendedCrossDomainSessionSecurity)
        {
            return;
        }

        // Extended check: Issue error if there was no DWRSESSIONID cookie
        if (dwrCookieCount == 0)
        {
            throwCsrfException();
        }

        // Extended check: correlate and restrict DWRSESSIONID to owning session if any
        HttpSession sess = request.getSession(false);
        if (sess == null)
        {
            return;
        }
        String registeredDwrSessionId = updateRegisteredDwrSession(sess, batch);
        if (!registeredDwrSessionId.equals(batch.getDwrSessionId()))
        {
            throwCsrfException();
        }
    }

    private void throwCsrfException() throws SecurityException
    {
        log.error("A request has been denied as a potential CSRF attack. This security check is performed as DWR's crossDomainSessionSecurity setting is active. Read more in the DWR documentation.");
        throw new SecurityException("CSRF Security Error (see server log for details).");
    }

    /**
     * Update data we store for the CSRF protection
     * @param request The original browser's request
     * @param batch The data that we've parsed from the request body
     */
    protected void updateCsrfState(HttpServletRequest request, Batch batch)
    {
        if (!crossDomainSessionSecurity || !extendedCrossDomainSessionSecurity)
        {
            return;
        }

        if (request.getCookies() == null)
        {
            return;
        }

        HttpSession sess = request.getSession(false);
        if (sess == null)
        {
            return;
        }

        // Register DWRSESSIONID on the way out in case HttpSession was created/changed
        updateRegisteredDwrSession(sess, batch);
    }

    /**
     * Get registered DWRSESSIONID from session or register ourselves
     * @param sess
     * @param batch
     * @return DWRSESSIONID registered for this session
     */
    private String updateRegisteredDwrSession(HttpSession sess, Batch batch)
    {
        String registeredDwrSessionId = (String) sess.getAttribute(ATTRIBUTE_DWRSESSIONID);
        if (registeredDwrSessionId == null)
        {
            // Check again and update while locking out all other threads
            // (note that this is not a DCL anti-pattern as we are using an immutable
            // write-once design and getAttribute/setAttribute methods are also synchronized!)
            synchronized (this)
            {
                registeredDwrSessionId = (String) sess.getAttribute(ATTRIBUTE_DWRSESSIONID);
                if (registeredDwrSessionId == null)
                {
                    registeredDwrSessionId = batch.getDwrSessionId();
                    sess.setAttribute(ATTRIBUTE_DWRSESSIONID, registeredDwrSessionId);
                }
            }
        }
        return registeredDwrSessionId;
    }

    /**
     * Do we perform cross-domain session security checks?
     * @param crossDomainSessionSecurity the cross domain session security setting
     */
    public void setCrossDomainSessionSecurity(boolean crossDomainSessionSecurity)
    {
        this.crossDomainSessionSecurity = crossDomainSessionSecurity;
    }

    /**
     * Do we perform cross-domain session security checks?
     */
    private boolean crossDomainSessionSecurity = true;

    /**
     * Do we perform extended cross-domain session security checks?
     * @param value the cross domain session security setting
     */
    public void setExtendedCrossDomainSessionSecurity(boolean value)
    {
        this.extendedCrossDomainSessionSecurity = value;
    }

    /**
     * Do we perform extended cross-domain session security checks?
     */
    private boolean extendedCrossDomainSessionSecurity = true;

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
     * Session attribute for DWRSESSIONID
     */
    public static final String ATTRIBUTE_DWRSESSIONID = "org.directwebremoting.DWRSESSIONID";

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BaseDwrpHandler.class);
}
