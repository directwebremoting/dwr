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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.directwebremoting.io.FileTransfer;
import org.directwebremoting.io.InputStreamFactory;
import org.directwebremoting.io.OutputStreamLoader;

/**
 * Used when the {@link FileTransfer} has a {@link OutputStreamLoader}, but what
 * it really wants is an {@link InputStreamFactory}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class OutputStreamLoaderInputStreamFactory implements InputStreamFactory
{
    /**
     * We need an OutputStreamLoader
     */
    public OutputStreamLoaderInputStreamFactory(OutputStreamLoader outputStreamLoader)
    {
        this.outputStreamLoader = outputStreamLoader;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.InputStreamFactory#getInputStream()
     */
    public InputStream getInputStream() throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        outputStreamLoader.load(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.InputStreamFactory#close()
     */
    public void close() throws IOException
    {
        outputStreamLoader = null;
    }

    /**
     * The object we are proxying to
     */
    private OutputStreamLoader outputStreamLoader;
}
