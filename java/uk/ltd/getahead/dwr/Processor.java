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
package uk.ltd.getahead.dwr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler is a bit like a servlet except that we don't need to ask users to
 * put stuff into WEB-INF/web.xml we can configure it ourselves. We also don't
 * distinguish between GET and POST at a method level.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Processor
{
    /**
     * Handle a request, the equivalent of doGet and doPost
     * @param req The HTTP request parameters
     * @param resp The HTTP response data
     * @throws IOException If i/o fails
     * @throws ServletException Other failures
     */
    void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
