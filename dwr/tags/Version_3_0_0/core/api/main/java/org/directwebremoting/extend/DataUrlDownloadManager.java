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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.directwebremoting.io.FileTransfer;
import org.directwebremoting.io.OutputStreamLoader;
import org.directwebremoting.util.Base64;
import org.directwebremoting.util.LocalUtil;

/**
 * A download manager that works my returning a data: URL so the data is
 * sent directly without waiting in some store.
 * <p>This method has the benefit that it works in a clustered environment
 * and is quite simple. However it does not work in IE.
 * @author Jose Noheda [jose dot noheda at gmail dot com]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DataUrlDownloadManager implements DownloadManager
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.DownloadManager#addFile(org.directwebremoting.extend.DownloadManager.FileGenerator)
     */
    public String addFileTransfer(FileTransfer transfer) throws IOException
    {
        OutputStreamLoader loader = null;
        ByteArrayOutputStream out = null;
        try
        {
            out = new ByteArrayOutputStream();
            String mimeType = transfer.getMimeType();
            loader = transfer.getOutputStreamLoader();
            loader.load(out);
            String base64data = new String(Base64.encodeBase64(out.toByteArray()));
            return "'data:" + mimeType + ";base64," + base64data + "'";
        }
        finally
        {
            LocalUtil.close(loader);
            LocalUtil.close(out);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.DownloadManager#getFile(java.lang.String)
     */
    public FileTransfer getFileTransfer(String id)
    {
        throw new UnsupportedOperationException("data: URLs should not be touched with http:");
    }
}
