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

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.extend.FormField;
import org.directwebremoting.extend.ServerException;

/**
 * An interface to allow us to plug alternative FileUpload components into DWR,
 * and more specifically, to plug in a null implementation if file commons
 * file-upload.jar is not available on the classpath.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface FileUpload
{
    /**
     * Process an inbound request into a set of name/value pairs where the
     * values could be file uploads.
     * @param req The request to parse
     * @return A map of the fields in the input request
     * @throws ServerException If there is an {@link IOException} while reading the request
     */
    public Map<String, FormField> parseRequest(HttpServletRequest req) throws ServerException;
}
