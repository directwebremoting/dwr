package org.directwebremoting.extend;

/**
 * A WaitController allows the {@link ServerLoadMonitor} to know what is waiting
 * and enables it to tell them to stop waiting.
 * TODO: Change the name of this to something that indicates it's about shutdown
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface WaitController
{
    /**
     * Stop waiting - the server wants to shutdown
     */
    void shutdown();

    /**
     * @return Has {@link #shutdown()} been called?
     */
    boolean isShutdown();
}
