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

/**
 * Something has gone wrong when we were doing some conversion.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ServerException extends Exception
{
    /**
     * Default ctor
     */
    public ServerException()
    {
    }

    /**
     * Construct a MarshallException with a description message and exception
     * @param message error description
     */
    public ServerException(String message)
    {
        super(message);
    }

    /**
     * Construct a MarshallException with a description message and exception
     * @param message error description
     * @param ex error stack trace
     */
    public ServerException(String message, Throwable ex)
    {
        super(message);
        this.ex = ex;
    }

    /**
     * Construct a MarshallException with an exception
     * @param ex error stack trace
     */
    public ServerException(Throwable ex)
    {
        super(ex.getMessage());
        this.ex = ex;
    }

    /**
     * @return The cause of this exception (if any)
     */
    public Throwable getCause()
    {
        return ex;
    }

    /**
     * Stored exception cause
     */
    private Throwable ex = null;
}
