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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.extend.ServerLoadMonitor;
import org.directwebremoting.extend.WaitController;

/**
 * A base implementation of {@link ServerLoadMonitor} that implements waiting
 * functionallity, mostly to provide {@link #shutdown()}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractServerLoadMonitor implements ServerLoadMonitor
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ServerLoadMonitor#threadWaitStarting(org.directwebremoting.extend.WaitController)
     */
    public void threadWaitStarting(WaitController controller)
    {
        synchronized (waitControllers)
        {
            waitControllers.add(controller);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ServerLoadMonitor#threadWaitEnding(org.directwebremoting.extend.WaitController)
     */
    public void threadWaitEnding(WaitController controller)
    {
        synchronized (waitControllers)
        {
            waitControllers.remove(controller);
        }
    }

    /**
     * If there are too many WaitControllers waiting then we can kill one off at
     * random.
     * @param count How many {@link WaitController}s do we shutdown?
     */
    public void shutdownRandomWaitControllers(int count)
    {
        synchronized (waitControllers)
        {
            for (int i = 0; i < count && !waitControllers.isEmpty(); i++)
            {
                waitControllers.get(0).shutdown();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ServerLoadMonitor#shutdown()
     */
    public void shutdown()
    {
        if (shutdownCalled)
        {
            return;
        }

        synchronized (waitControllers)
        {
            List<WaitController> copy = new ArrayList<WaitController>();
            copy.addAll(waitControllers);

            for (WaitController controller : copy)
            {
                controller.shutdown();
            }

            log.debug(" - shutdown on: " + this);
            shutdownCalled = true;
        }
    }

    /**
     * Have we been shutdown already?
     */
    private boolean shutdownCalled = false;

    /**
     * The known wait controllers
     */
    protected final List<WaitController> waitControllers = new ArrayList<WaitController>();

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(AbstractServerLoadMonitor.class);
}
