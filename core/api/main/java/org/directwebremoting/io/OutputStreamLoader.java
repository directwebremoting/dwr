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
package org.directwebremoting.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Sometimes you might have a way to write to an {@link OutputStream} and
 * don't want to create a temporary in memory buffer to hold the data before
 * it is squirted to the browser.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface OutputStreamLoader
{
    /**
     * Write all the data to the given output stream.
     */
    public void load(OutputStream out) throws IOException;

    /**
     * This method indicates that whether or not this resource has been read,
     * it is not longer required.
     * Must be called by whatever calls {@link #load(OutputStream)} when it has
     * finished reading from the stream, or when it has decided that it never
     * will call {@link #load(OutputStream)}.
     */
    public void close() throws IOException;
}
