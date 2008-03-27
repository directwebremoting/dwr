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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.ServerException;

/**
 * A default implementation of FileUpload for cases when Commons File-Upload is
 * not available. This implementation does not do file uploads, however it does
 * allow the system to carry on without classpath issues.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class UnsupportedFileUpload implements FileUpload
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.FileUpload#parseRequest(javax.servlet.http.HttpServletRequest, java.io.File)
     */
    public Map<String, FormField> parseRequest(HttpServletRequest req) throws ServerException
    {
        log.error("Commons File Upload jar file not found. Aborting request.");
        throw new UnsupportedOperationException("File uploads not supported");
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(UnsupportedFileUpload.class);
}
