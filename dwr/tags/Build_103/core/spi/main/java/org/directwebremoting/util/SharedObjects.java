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
package org.directwebremoting.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * DWR has some singleton objects (well 1 to be exact) that is JDK defined
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SharedObjects
{
    /**
     * Each {@link ScheduledThreadPoolExecutor} used by an application uses a
     * thread, so DWR tries to use only one.
     * @return DWR's shared ScheduledThreadPoolExecutor
     */
    public static ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor()
    {
        return timer;
    }

    /**
     * The cron system
     */
    private static ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1);
}
