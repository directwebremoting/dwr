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
