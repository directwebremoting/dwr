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
import java.io.InputStream;
import java.io.OutputStream;

import org.directwebremoting.io.FileTransfer;
import org.directwebremoting.io.InputStreamFactory;
import org.directwebremoting.io.OutputStreamLoader;
import org.directwebremoting.util.CopyUtils;

/**
 * Used when the {@link FileTransfer} has a {@link InputStreamFactory}, but what
 * it really wants is an {@link OutputStreamLoader}
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class InputStreamFactoryOutputStreamLoader implements OutputStreamLoader
{
    /**
     * We need an InputStreamFactory
     */
    public InputStreamFactoryOutputStreamLoader(InputStreamFactory inputStreamFactory)
    {
        this.inputStreamFactory = inputStreamFactory;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.OutputStreamLoader#load(java.io.OutputStream)
     */
    public void load(OutputStream out) throws IOException
    {
        InputStream in = inputStreamFactory.getInputStream();
        CopyUtils.copy(in, out);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.io.OutputStreamLoader#close()
     */
    public void close() throws IOException
    {
        inputStreamFactory.close();
    }

    /**
     * The object we are proxying to
     */
    private final InputStreamFactory inputStreamFactory;
}
