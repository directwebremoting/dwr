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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Marshaller is responsible for all the on-the-wire communication between
 * DWR on the server and the HTTP channel. engine.js does the corresponding
 * work on the Javascript side.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Marshaller
{
    /**
     * Marshall an incomming HttpRequest into an abstract Calls POJO that
     * defines the functions that we need to call.
     * @param request The incoming Http request
     * @param response An Ajax response, XML, JSON, Javascript, etc.
     * @return Data specifying the methods to call
     * @throws IOException If the connection breaks
     * @throws ServerException If an error occurs during parsing
     */
    Calls marshallInbound(HttpServletRequest request, HttpServletResponse response) throws IOException, ServerException;

    /**
     * Marshall the return values from executing this batch of requests.
     * @param replies The objects to convert into a reply
     * @param request The incoming Http request
     * @param response An Ajax response, XML, JSON, Javascript, etc.
     * @throws IOException If the connection breaks
     */
    void marshallOutbound(Replies replies, HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * Try to find a batchId to send to the client so it knows what broke
     * @param request The incoming Http request
     * @param response An Ajax response, XML, JSON, Javascript, etc.
     * @param ex The exception that we wish to propogate to the client
     * @throws IOException If writing to the output stream fails
     */
    void marshallException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException;

    /**
     * Check if we can coerce the given type
     * @param paramType The type to check
     * @return true iff <code>paramType</code> is coercable
     */
    boolean isConvertable(Class paramType);
}
