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
package org.directwebremoting.event;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

/**
 * Progress listener that stores results in the user session.
 * @author Jose Noheda
 */
public class SessionProgressListener implements ProgressListener, Serializable
{
    public SessionProgressListener(HttpSession session)
    {
        this.session = session;
    }

    /**
     * Accessor for the number of bytes read, as passed in by
     * {@link ProgressListener#update(long, long, int)}
     */
    public long getBytesRead()
    {
        return bytesRead;
    }

    /**
     * The total number of bytes, which are being read. May be -1, if this
     * number is unknown. Passed in by
     * {@link ProgressListener#update(long, long, int)}
     */
    public long getContentLength()
    {
        return contentLength;
    }

    /**
     * The number of the field, which is currently being read. (0 = no item so
     * far, 1 = first item is being read, ...)
     */
    public long getItem()
    {
        return item;
    }

    /* (non-Javadoc)
     * @see org.apache.commons.fileupload.ProgressListener#update(long, long, int)
     */
    public void update(long newBytesRead, long newContentLength, int items)
    {
        if (session.getAttribute(CANCEL_UPLOAD) != null)
        {
            this.bytesRead = this.contentLength + 1;
            throw new RuntimeException("User cancelled the upload.");
        }
        this.bytesRead = newBytesRead;
        this.contentLength = newContentLength;
        this.item = items;
    }

    /**
     * The session where the results are stored.
     */
    private volatile HttpSession session;

    /**
     * @see SessionProgressListener#getBytesRead()
     */
    private volatile long bytesRead = 0L;

    /**
     * @see #getContentLength()
     */
    private volatile long contentLength = 0L;

    /**
     * @see #getItem()
     */
    private volatile long item = 0L;

    /**
     * The attribute that indicates if the user wants to cancel the upload.
     */
    public static final String CANCEL_UPLOAD = "CANCEL_UPLOAD";
}
