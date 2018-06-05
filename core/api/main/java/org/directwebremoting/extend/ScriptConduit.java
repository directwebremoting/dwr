package org.directwebremoting.extend;

import java.io.IOException;

/**
 * This interface hides the decoration taking place on different poll types.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Mike Wilson
 */
public interface ScriptConduit
{
    /**
     * What mime type should we send to the browser for this data?
     * @return A mime-type
     */
    String getOutboundMimeType();

    /**
     * Called when we are initially setting up the stream.
     * <p>This method is always called exactly once in the lifetime of a
     * conduit.
     */
    void beginStreamAndChunk() throws IOException;

    /**
     * Called before a each set of scripts that are to be sent.
     */
    void beginChunk() throws IOException;

    /**
     * Write a script to remote side.
     * @param script The script to write
     */
    void sendScript(String script) throws IOException;

    /**
     * Called after each set of scripts when they have been sent.
     */
    void endChunk() throws IOException;

    /**
     * Called when we are shutting the stream down.
     */
    void endStreamAndChunk() throws IOException;
}
