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

import java.io.IOException;

import org.directwebremoting.ScriptBuffer;
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
     * If this ScriptSession currently has a connected {@link ScriptConduit}
     * and this conduit accepts and claims to be able to publish the script
     * then publish and return true, otherwise return false.
     * Add a script to the list waiting for remote execution.
     * The version automatically wraps the string in a ClientScript object.
     * @param script The script to execute
     */
    boolean addScriptImmediately(ScriptBuffer script);

    /**
     * While a Marshaller is processing a request it can register a
     * ScriptConduit with the ScriptSession to say - "pass scripts to me"
     * <p>
     * Several Marshallers may be active on the same page as a time and it
     * doesn't really matter which gets the script. So ScriptSession should
     * record all of the active ScriptConduits, but just pick one
     * @param conduit The new ScriptConduit
     * @throws IOException If the write to the output fails
     * @see RealScriptSession#removeScriptConduit(ScriptConduit)
     */
    void addScriptConduit(ScriptConduit conduit) throws IOException;

    /**
     * Remove a ScriptConduit.
     * @param conduit The ScriptConduit to remove
     * @see RealScriptSession#addScriptConduit(ScriptConduit)
     */
    void removeScriptConduit(ScriptConduit conduit);

    /**
     * We might need to send a script directly to a conduit without adding the
     * conduit to the "open" list and then removing it directly.
     * @param conduit The conduit to write to
     * @throws IOException If writing fails
     */
    void writeScripts(ScriptConduit conduit) throws IOException;

    /**
     * Allows for checking to see if there is data waiting to be returned
     * @return true if there are no waiting scripts
     */
    boolean hasWaitingScripts();

    /**
     * Called whenever a browser accesses this ScriptSession to ensure that the
     * session does not timeout before it should.
     */
    void updateLastAccessedTime();

    /**
     * If the global parameter avoid2ConnectionLimitWithWindowName == true then
     * we need to keep a track of the names of the windows that connect to us
     * @param windowName The new name for the window that spawned this Session
     */
    void setWindowName(String windowName);

    /**
     * Accessor for the name attached to this window
     */
    String getWindowName();

    /**
     * Are there any persistent {@link ScriptConduit}s currently connected to
     * this session?
     * @return The number of current persistent connections.
     */
    int countPersistentConnections();
}
