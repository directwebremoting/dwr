package org.directwebremoting.extend;

import java.io.IOException;

import org.directwebremoting.io.FileTransfer;

/**
 * A DownloadManager allows you to inject files into the system and then
 * retrieve them via a servlet at some later date. Implementations of
 * DownloadManager are responsible for defining a purge policy.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface DownloadManager
{
    /**
     * Inject a file into the system for later download.
     * @param transfer The representation of the file to inject
     * @return A URL for how to allow download of this data at a later time
     * @throws IOException If there are problems reading from the {@link FileTransfer}
     */
    String addFileTransfer(FileTransfer transfer) throws IOException;

    /**
     * Retrieve a FileGenerator given the id that it was stored under
     * @param id The id to lookup
     * @return The matching FileGenerator or null if no match was found
     */
    FileTransfer getFileTransfer(String id) throws IOException;
}
