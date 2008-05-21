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

/**
 * A Sleeper allows the request to halt and cease execution for some time,
 * while still allowing output.
 * <p>There are 3 envisaged implementations</p>
 * <ul>
 * <li>Old servlet stacks where the only option is to call
 * {@link Object#wait(long)} and {@link Object#notify()} to resume.</li>
 * <li>Jetty, where an Ajax Continuation causes an exception</li>
 * <li>Newer async servlets where the implementation will probably continue
 * with the servlet engine able to detect that it should not complete the
 * request.</li>
 * </ul>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
interface Sleeper
{
    /**
     * 'halt' the current execution in some way.
     * This method should be the last meaningful thing that is done on a
     * request, and work that needs to be done before completion should be
     * done in a {@link Runnable} so the system can schedule it at an
     * appropriate time.
     * @param onAwakening The action to take when {@link #wakeUp()} is called
     */
    void goToSleep(Runnable onAwakening);

    /**
     * This method should attempt to resume the execution.
     * It is possible that this method will be called more than once at the
     * same time so Sleepers should be prepared take steps to be woken only
     * once.
     */
    void wakeUp();
}
