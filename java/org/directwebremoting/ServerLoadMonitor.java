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
    int timeToNextPoll();

    /**
     * How long should we hold up a client request in case some more scripts
     * come in.
     * @return The time in millis to wait before closing an Ajax request
     */
    long timeWithinPoll();
}
