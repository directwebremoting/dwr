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
package org.directwebremoting.extend;

import org.directwebremoting.WebContext;

/**
 * An interface that extends WebContext with some functions that should not
 * be used by end users, but could be useful to system extenders.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface RealWebContext extends WebContext
{
    /**
     * Fill in the page details from the servlet request.
     * <p>This method should be used by anything that parses a batch, and then
     * allows either uses a {@link WebContext} or allows other things to use a
     * WebContext.
     * <p><strong>Caution<strong> Following this call, the passed
     * scriptSessionId may be wrong. scriptSessionIds can become invalid due to
     * server re-start, a timeout, or a back-button.
     * <p>It seems wrong to throw a security exception, because it could
     * be totally innocent. So this method will create a new script session and
     * insert a script into the script session so that the page becomes synced
     * with the new ID at the earliest possible opportunity.
     * @param sentPage The URL of the current page
     * @param sentScriptId The session id passed in by the browser
     * @param windowName Reverse Ajax uses window.name to avoid 2 connection limit
     */
    void checkPageInformation(String sentPage, String sentScriptId, String windowName);
}
