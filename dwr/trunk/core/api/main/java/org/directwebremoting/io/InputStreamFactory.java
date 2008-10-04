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
import java.io.InputStream;

/**
 * This class wraps the getting of an input stream in a closure to avoid
 * issues with not closing the stream. An input stream will only be retrieved
 * when it is needed.
 * If you wish to implement this when you already have an {@link InputStream}
 * (i.e. you don't care about delayed creation) then you should use
 * {@link SimpleInputStreamFactory}.
 * @see SimpleInputStreamFactory
 * @author Lance Semmens [uklance at gmail dot com]
 */
public interface InputStreamFactory
{
    /**
     * Gets an input stream
     * @return the input stream
     */
    public InputStream getInputStream() throws IOException;

    /**
     * Whether or not this resource has been read from, it is not longer required.
     * Must be called by whatever calls {@link #getInputStream()} when it has
     * finished reading from the stream, or when it has decided that it never
     * will call {@link #getInputStream()}
     */
    public void close() throws IOException;
}
