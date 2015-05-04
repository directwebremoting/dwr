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

import java.util.List;

import org.directwebremoting.ScriptSession;

/**
 * RealScriptSession is the real interface that should be implemented in place
 * of ScriptSession. It includes methods required by the guts of DWR, that are
 * not needed by normal users.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Mike Wilson
 */
public interface RealScriptSession extends ScriptSession
{
    /**
     * While a Marshaller is processing a request it can register a
     * Sleeper with the ScriptSession to say - "tell me when there is new data"
     */
    void setSleeper(Sleeper sleeper);

    /**
     * Remove Sleeper.
     */
    void clearSleeper(Sleeper sleeper);

    /**
     * Get queued scripts starting from the supplied index.
     * @param fromScriptIndex start index
     * @return a Script instance containing the scripts
     */
    Scripts getScripts(long fromScriptIndex);

    /**
     * Confirms that the client has received all scripts up to and including
     * the supplied index so that script data may be purged.
     * @param scriptIndex last index that can be purged
     */
    void confirmScripts(long scriptIndex);

    /**
     * Called whenever a browser accesses this ScriptSession to ensure that the
     * session does not timeout before it should.
     */
    void updateLastAccessedTime();

    /**
     * Set a new page for the ScriptSession (in case of HTML pushState)
     */
    void setPage(String page);

    /**
     * Set a new HttpSession id for the ScriptSession
     */
    void setHttpSessionId(String httpSessionId);

    /**
     * Data class combining script list with offset.
     * @author Mike Wilson
     */
    public static interface Scripts
    {
        long getScriptIndexOffset();
        List<String> getScripts();
    }
}
