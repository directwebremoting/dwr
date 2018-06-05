package org.directwebremoting.extend;

import java.io.IOException;
import java.io.Serializable;

/**
 * A Sleeper allows the request to halt and cease execution for some time,
 * while still allowing output.
 * <p>All implementations of Sleeper must be {@link Serializable} so we can
 * store Sleepers in the session and therefore have other connections wake them
 * up.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Mike Wilson
 */
public interface Sleeper extends Serializable
{
    /**
     * 'halt' the current execution in some way.
     * This method should be the last meaningful thing that is done in a
     * poll request to activate the Sleeper's background wait mechanism.
     * @param batchId The batchId for the poll
     * @param onClose The action to take when {@link #wakeUpToClose()} is called
     * @param disconnectedTime The waiting time to instruct the browser before the next poll
     */
    void enterSleep(String batchId, Runnable onClose, int disconnectedTime) throws IOException;

    /**
     * Wake up to handle new data that arrived in the associated ScriptSession.
     */
    void wakeUpForData();

    /**
     * Wake up to close down the Sleeper and free any resources held by it.
     * The previously supplied onClose callback will be executed.
     * @return the disconnectedTime that will be sent to the client
     */
    int wakeUpToClose();
}
