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

import org.directwebremoting.ScriptSession;

/**
 * RealScriptSession is the real interface that should be implemented in place
 * of ScriptSession. It includes methods required by the guts of DWR, that are
 * not needed by normal users.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface RealScriptSession extends ScriptSession
{
    /**
     * While a Marshaller is processing a request it can register a
     * ScriptConduit with the ScriptSession to say - "pass scripts to me"
     * <p>
     * Several Marshallers may be active on the same page as a time and it
     * doesn't really matter which gets the script. So ScriptSession should
     * record all of the active ScriptConduits, but just pick one
     * @param conduit The new ScriptConduit
     * @see RealScriptSession#removeScriptConduit(ScriptConduit)
     */
    void addScriptConduit(ScriptConduit conduit);

    /**
     * Remove a ScriptConduit.
     * @param conduit The ScriptConduit to remove
     * @see RealScriptSession#addScriptConduit(ScriptConduit)
     */
    void removeScriptConduit(ScriptConduit conduit);

    /**
     * Allows for checking to see if there is data waiting to be returned
     * @return true if there are no waiting scripts
     */
    boolean hasWaitingScripts();

    /**
     * Accessor for the mutex used to synchronize access to
     * @return The mutex object used by the script session
     */
    Object getScriptLock();

    /**
     * Flush the session conduits
     */
    void flushConduits();
}
