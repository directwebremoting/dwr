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

        // If there is a DWRSESSIONID cookie on the request then the
        // corresponding session value in the batch must match
        if (request.getCookies() != null)
        {
            for (Cookie cookie : request.getCookies())
            {
                if (cookie.getName().equals("DWRSESSIONID"))
                {
                    if (cookie.getValue().equals(batch.getDwrSessionId()))
                    {
                        // All is well, we can exit from the cookie test loop
                        break;
                    }
                    else
                    {
                        // Values don't match which is probably an attack
                        log.error("A request has been denied as a potential CSRF attack. This security check is performed as DWR's crossDomainSessionSecurity setting is active. Read more in the DWR documentation.");
                        throw new SecurityException("CSRF Security Error (see server log for details).");
                    }
                }
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
     * The log stream
     */
    private static final Log log = LogFactory.getLog(BaseDwrpHandler.class);
}
