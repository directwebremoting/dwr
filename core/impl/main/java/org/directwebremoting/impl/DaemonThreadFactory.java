package org.directwebremoting.impl;

import java.util.concurrent.ThreadFactory;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DaemonThreadFactory implements ThreadFactory
{
    public Thread newThread(Runnable r)
    {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    }
}


