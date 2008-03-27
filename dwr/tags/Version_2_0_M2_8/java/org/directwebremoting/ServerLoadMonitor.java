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
package org.directwebremoting;

/**
 * Polling or Comet style interactive web applications require something to
 * monitor high levels of server load to ensure that
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface ServerLoadMonitor
{
    /**
     * Controller for poll times.
     * @return How long should this client wait until it next polls?
     */
    int getTimeToNextPoll();

    /**
     * How long should we hold up a client request before starting a
     * response stream while waiting for some scripts come in.
     * @return The time in millis to wait before opening an Ajax poll response stream.
     */
    long getPreStreamWaitTime();

    /**
     * How long should we hold up a client request after starting 
     * a response stream while waiting for more scripts
     * come in.
     * @return The time in millis to wait before closing an Ajax poll response stream
     */
    long getPostStreamWaitTime();

    /**
     * A thread is about to begin a wait period.
     * This can be used by implementations to dynamically adjust the poll
     * timings. 
     */
    void threadWaitStarting();

    /**
     * A thread has just ended a wait period.
     * This can be used by implementations to dynamically adjust the poll
     * timings. 
     */
    void threadWaitEnding();
}
