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
import java.io.Serializable;

/**
 * A Sleeper allows the request to halt and cease execution for some time,
 * while still allowing output.
 * <p>All implementations of Sleeper must be {@link Serializable} so we can
 * store Sleepers in the session and therefore have other connections wake them
 * up.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Mike Wilson
 */
public interface Sleeper extends Serializable
{
    /**
     * 'halt' the current execution in some way.
     * This method should be the last meaningful thing that is done in a
     * poll request to activate the Sleeper's background wait mechanism.
     * @param onClose The action to take when {@link #wakeUpToClose()} is called
     * @param disconnectedTime The waiting time to instruct the browser before the next poll
     */
    void enterSleep(Runnable onClose, int disconnectedTime) throws IOException;

    /**
     * Wake up to handle new data that arrived in the associated ScriptSession.
     */
    void wakeUpForData();

    /**
     * Wake up to close down the Sleeper and free any resources held by it.
     * The previously supplied onClose callback will be executed.
     */
    void wakeUpToClose();
}
