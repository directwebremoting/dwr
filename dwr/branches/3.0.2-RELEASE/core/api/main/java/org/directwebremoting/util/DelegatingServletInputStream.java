/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.directwebremoting.util;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

/**
 * Delegating implementation of ServletInputStream.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DelegatingServletInputStream extends ServletInputStream
{
    /**
     * Create a new DelegatingServletInputStream.
     * @param proxy the sourceStream InputStream
     */
    public DelegatingServletInputStream(InputStream proxy)
    {
        this.proxy = proxy;
    }

    /**
     * Accessor for the stream that we are proxying to
     * @return The stream we proxy to
     */
    public InputStream getTargetStream()
    {
        return proxy;
    }

    /**
     * @return The stream that we proxy to
     */
    public InputStream getSourceStream()
    {
        return proxy;
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException
    {
        return proxy.read();
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#close()
     */
    @Override
    public void close() throws IOException
    {
        super.close();
        proxy.close();
    }

    private final InputStream proxy;
}
