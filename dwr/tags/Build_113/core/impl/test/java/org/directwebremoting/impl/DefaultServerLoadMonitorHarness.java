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
import org.directwebremoting.extend.WaitController;

/**
 * @author Joe Walker [joe at getahead dot org]
 */
public class DefaultServerLoadMonitorHarness
{
    public static void main(String[] args)
    {
        DefaultServerLoadMonitorHarness test = new DefaultServerLoadMonitorHarness();
        test.exec();
    }

    public void exec()
    {
        dslm.setMaxWaitingThreads(100);
        dslm.setMaxHitsPerSecond(100);

        List<Client> clients = new ArrayList<Client>();

        //*
        for (int i = 0; i < 300; i++)
        {
            Client client = new Client(clients.size());
            client.start();
            clients.add(client);
        }
        //*/

        try
        {
            while (true)
            {
                Thread.sleep(5000);
                Client client = new Client(clients.size());
                client.start();
                clients.add(client);

                float hitsPerSecond = (float) dslm.hitMonitor.getHitsInLastPeriod() / DefaultServerLoadMonitor.SECONDS_MONITORED;

                log.debug("------------------------------------");
                log.debug("Num. of clients: " + clients.size());
                log.debug("Hits per second: " + hitsPerSecond);
                log.debug("Waiting threads: " + dslm.waitingThreads);
                log.debug("Disconnect time: " + dslm.disconnectedTime);
                log.debug("Connected time:  " + dslm.connectedTime);
            }
        }
        catch (InterruptedException ex)
        {
            log.warn("Interrupted: ", ex);
        }
    }

    private class Client extends Thread
    {
        Client(int id)
        {
            setName("Client:" + id);
        }

        @Override
        public void run()
        {
            try
            {
                Thread.sleep((int) (Math.random() * 60000));
                WaitController waitController = new CustomWaitController();

                while (true)
                {
                    dslm.threadWaitStarting(waitController);
                    long connectedTime = dslm.getConnectedTime();
                    waitWithCatch(connectedTime);
                    dslm.threadWaitEnding(waitController);
                    int timeToNextPoll = dslm.getDisconnectedTime();
                    waitWithCatch(timeToNextPoll);
                }
            }
            catch (Exception ex)
            {
                log.warn("", ex);
            }
            finally
            {
                log.warn("Client dying");
            }
        }

        private void waitWithCatch(long waitTime)
        {
            if (waitTime == 0)
            {
                waitTime = waitTime + 1;
            }

            try
            {
                synchronized (lock)
                {
                    lock.wait(waitTime);
                }
            }
            catch (InterruptedException ex)
            {
                interrupt();
                System.out.print('x');
                System.out.flush();
            }
        }

        protected class CustomWaitController implements WaitController
        {
            public void shutdown()
            {
                synchronized (lock)
                {
                    lock.notifyAll();
                }
            }

            public boolean isShutdown()
            {
                return false;
            }
        }

        protected final Object lock = new Object();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DefaultServerLoadMonitorHarness.class);

    protected DefaultServerLoadMonitor dslm = new DefaultServerLoadMonitor();
}
