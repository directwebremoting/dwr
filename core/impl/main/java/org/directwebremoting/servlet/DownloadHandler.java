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
package org.directwebremoting.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.DownloadManager;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.io.FileTransfer;
import org.directwebremoting.io.OutputStreamLoader;
import org.directwebremoting.util.LocalUtil;

/**
 * A DownloadHandler is basically a FileServingServlet that integrates with
 * a DownloadManager to purge files from the system that have been downloaded.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DownloadHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String id = request.getPathInfo();

        if (!id.startsWith(downloadHandlerUrl))
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        id = id.substring(downloadHandlerUrl.length());

        FileTransfer transfer = downloadManager.getFileTransfer(id);
        if (transfer == null)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        else
        {
            String filename = transfer.getFilename();
            if (filename != null)
            {
                response.setHeader("Content-disposition", "attachment; filename=" + filename);
            }

            response.setContentType(transfer.getMimeType());

            OutputStreamLoader loader = null;
            try
            {
                loader = transfer.getOutputStreamLoader();
                loader.load(response.getOutputStream());
            }
            finally
            {
                LocalUtil.close(loader);
            }
        }
    }

    /**
     * @param downloadManager The new DownloadManager
     */
    public void setDownloadManager(DownloadManager downloadManager)
    {
        this.downloadManager = downloadManager;
    }

    /**
     * The URL part which we attach to the downloads.
     * @param downloadHandlerUrl The URL for this Handler.
     */
    public void setDownloadHandlerUrl(String downloadHandlerUrl)
    {
        this.downloadHandlerUrl = downloadHandlerUrl;
    }

    /**
     * The place we store files for later download
     */
    private DownloadManager downloadManager;

    /**
     * The URL part which we attach to the downloads.
     */
    protected String downloadHandlerUrl;
}
