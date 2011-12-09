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

/**
 * Polling or Comet style interactive web applications require something to
 * monitor high levels of server load to ensure that
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ServerLoadMonitor
{
    /**
     * If the server is not going to be streaming then we need to tell browsers
     * to just use XHR rather than anything fancier.
     * @return true if the server will be supporting streaming
     */
    boolean supportsStreaming();

    /**
     * Controller for poll times.
     * <p>TODO: We should probably get rid of this and leave it to PollHandler?
     * @return How long should this client wait until it next polls?
     */
    int getDisconnectedTime();

    /**
     * What's the longest time that we should wait before asking the client to
     * reconnect?
     * @return The maximum client connected time
     */
    long getConnectedTime();

    /**
     * A thread is about to begin a wait period.
     * This can be used by implementations to dynamically adjust the poll
     * timings.
     * @param controller An object that we can use to control the wait
     */
    void threadWaitStarting(WaitController controller);

    /**
     * A thread has just ended a wait period.
     * This can be used by implementations to dynamically adjust the poll
     * timings.
     * @param controller An object that we can use to control the wait
     */
    void threadWaitEnding(WaitController controller);

    /**
     * Kill all available long-poll requests
     */
    void shutdown();
}
