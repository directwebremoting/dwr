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
package org.directwebremoting.impl;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.servlet.ServletContextListener;

import org.directwebremoting.extend.UninitializingBean;

/**
 * Just a standard ScheduledThreadPoolExecutor with a single default thread in
 * the pool (we're not doing heavy scheduling) that is also a
 * {@link ServletContextListener} so the {@link org.directwebremoting.Container}
 * can shut us down.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class AutoShutdownScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor implements UninitializingBean
{
    /**
     * This is generally used as an event timer, so we don't need more than one
     * thread running at a time.
     */
    public AutoShutdownScheduledThreadPoolExecutor()
    {
        super(1);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.UninitializingBean#contextDestroyed()
     */
    public void contextDestroyed()
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.UninitializingBean#servletDestroyed()
     */
    public void servletDestroyed()
    {
        this.shutdownNow();
    }
}
