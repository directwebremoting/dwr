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
package org.directwebremoting.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.directwebremoting.extend.FileGenerator;
import org.directwebremoting.io.FileTransfer;

/**
 * An implementation of {@link FileGenerator} that uses a {@link FileTransfer}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FileTransferFileGenerator implements FileGenerator
{
    /**
     * @param fileTransfer The FileTransfer user object that we base ourselves on
     */
    public FileTransferFileGenerator(FileTransfer fileTransfer)
    {
        this.fileTransfer = fileTransfer;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.FileGenerator#generateFile(java.io.OutputStream)
     */
    public void generateFile(OutputStream out) throws IOException
    {
        InputStream in = fileTransfer.getInputStream();
        byte[] buffer = new byte[1024];

        while (true)
        {
            int length = in.read(buffer);

            if (length <= 0)
            {
                break;
            }

            out.write(buffer, 0, length);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.FileGenerator#getMimeType()
     */
    public String getMimeType()
    {
        return fileTransfer.getMimeType();
    }

    private FileTransfer fileTransfer;
}
