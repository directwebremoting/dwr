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

import org.directwebremoting.extend.FileGenerator;

/**
 * A helper to aid implementing {@link FileGenerator} that takes care of the
 * filename and mimeType fields
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class AbstractFileGenerator implements FileGenerator
{
    /**
     * Setup the filename and mimeType
     */
    public AbstractFileGenerator(String filename, String mimeType)
    {
        this.filename = filename;
        this.mimeType = mimeType;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.FileGenerator#getFilename()
     */
    public String getFilename()
    {
        return filename;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.FileGenerator#getMimeType()
     */
    public String getMimeType()
    {
        return mimeType;
    }

    /**
     * Sometimes the filename is user visible when downloading
     */
    private String filename;

    /**
     * The mime type in case the browser needs to do something with the file
     */
    private String mimeType;
}
