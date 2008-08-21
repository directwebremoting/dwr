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
import java.io.OutputStream;

/**
 * A way to convert byte arrays to files so they can be written using a
 * FileServingServlet or similar.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ByteArrayFileGenerator extends AbstractFileGenerator
{
    /**
     * Setup the image to convert
     * @param buffer the data to stream
     * @param mimeType The mime type to convert the image into
     */
    public ByteArrayFileGenerator(byte[] buffer, String filename, String mimeType)
    {
        super(filename, mimeType);
        this.buffer = buffer;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.FileGenerator#generateFile(java.io.OutputStream)
     */
    public void generateFile(OutputStream out) throws IOException
    {
        out.write(buffer);
    }

    /**
     * The data we wish to squirt to the browser.
     */
    private final byte[] buffer;
}
