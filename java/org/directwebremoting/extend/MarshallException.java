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

import org.directwebremoting.util.Messages;

/**
 * Something has gone wrong when we were doing some conversion.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class MarshallException extends Exception
{
    /**
     * Default ctor
     * @param paramType The type we were trying to marshall
     */
    public MarshallException(Class paramType)
    {
        super(Messages.getString("MarshallException.SimpleFailure", paramType.getName()));
        this.paramType = paramType;
    }

    /**
     * Construct a MarshallException with an exception and a destination type
     * @param paramType The type we were trying to marshall
     * @param ex error stack trace
     */
    public MarshallException(Class paramType, Throwable ex)
    {
        super(Messages.getString("MarshallException.FailureWithCause", paramType.getName(), ex.getMessage()));

        this.paramType = paramType;
        this.ex = ex;
    }

    /**
     * Construct a MarshallException with a description message and exception
     * @param paramType The type we were trying to marshall
     * @param message error description
     */
    public MarshallException(Class paramType, String message)
    {
        super(Messages.getString("MarshallException.FailureWithCause", paramType.getName(), message));

        this.paramType = paramType;
    }

    /**
     * @return The cause of this exception (if any)
     */
    public Throwable getCause()
    {
        return ex;
    }

    /**
     * Accessor for the type we are converting to/from
     * @return The type we are converting to/from
     */
    public Class getConversionType()
    {
        return paramType;
    }

    /**
     * The type we are converting to/from
     */
    private Class paramType;

    /**
     * Stored exception cause
     */
    private Throwable ex = null;
}
