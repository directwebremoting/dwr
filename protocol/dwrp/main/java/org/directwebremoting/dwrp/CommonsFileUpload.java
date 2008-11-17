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
package org.directwebremoting.dwrp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.directwebremoting.event.SessionProgressListener;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.ServerException;
import org.directwebremoting.extend.SimpleInputStreamFactory;
import org.directwebremoting.io.InputStreamFactory;


/**
 * An implementation of {@link FileUpload} that uses Apache Commons FileUpload.
 * This class with fail to classload if commons-fileupload.jar is not present
 * on the classpath.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CommonsFileUpload implements FileUpload
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.FileUpload#isMultipartContent(javax.servlet.http.HttpServletRequest)
     */
    public boolean isMultipartContent(HttpServletRequest req)
    {
        return ServletFileUpload.isMultipartContent(req);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.FileUpload#parseRequest(javax.servlet.http.HttpServletRequest)
     */
    @SuppressWarnings("unchecked")
    public Map<String, FormField> parseRequest(HttpServletRequest req) throws ServerException
    {
        File location = new File(System.getProperty("java.io.tmpdir"));
        DiskFileItemFactory itemFactory = new DiskFileItemFactory(DEFAULT_SIZE_THRESHOLD, location);
        ServletFileUpload fileUploader = new ServletFileUpload(itemFactory);

        HttpSession session = req.getSession(false);
        if (session != null)
        {
            fileUploader.setProgressListener(new SessionProgressListener(session));
            session.setAttribute(SessionProgressListener.CANCEL_UPLOAD, null);
            session.setAttribute(PROGRESS_LISTENER, fileUploader.getProgressListener());
        }

        try
        {
            Map<String, FormField> map = new HashMap<String, FormField>();
            List<FileItem> fileItems = fileUploader.parseRequest(req);
            for (final FileItem fileItem : fileItems)
            {
                FormField formField;
                if (fileItem.isFormField())
                {
                    formField = new FormField(fileItem.getString());
                }
                else
                {
                    InputStreamFactory inFactory = new SimpleInputStreamFactory(fileItem.getInputStream());
                    formField = new FormField(fileItem.getName(), fileItem.getContentType(), fileItem.getSize(), inFactory);
                }
                map.put(fileItem.getFieldName(), formField);
            }

            return map;
        }
        catch (IOException ex)
        {
            throw new ServerException("Input read failed", ex);
        }
        catch (FileUploadException ex)
        {
            throw new ServerException("Input read failed", ex);
        }
    }

    /**
     * The name of the attribute that stores the progress of an upload in a session.
     */
    public static final String PROGRESS_LISTENER = "PROGRESS_LISTENER";

    /**
     * The threshold, in bytes, below which items will be retained in memory and above which they will be stored as a file
     */
    private static final int DEFAULT_SIZE_THRESHOLD = 256 * 1024;
}
