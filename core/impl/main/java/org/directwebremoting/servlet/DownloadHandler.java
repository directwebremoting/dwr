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
            if (transfer.getSize() > 0)
            {
                response.setContentLength((int) transfer.getSize());
            }

            String defaultType = "attachment";
            String type = null;
            String suppliedType = request.getParameter("contentDispositionType");
            if (suppliedType != null && suppliedType.matches("^[-A-Za-z]+$"))
            {
                type = suppliedType;
            }

            String filename = transfer.getFilename();

            if (type != null || filename != null)
            {
                response.setHeader("Content-disposition",
                    (type != null ? type : defaultType)
                    + (filename != null ? "; filename=\"" + filename + "\"" : ""));
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
