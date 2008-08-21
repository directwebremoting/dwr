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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JsonModeMarshallException is a hint to the conversion process that
 * we are trying to convert in JSON mode, but we've discovered recursive data.
 * TODO: This should probably be called JsonConversionException or similar
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonModeMarshallException extends RuntimeException
{
    /**
     * Default ctor
     * @param paramType The type we were trying to marshall
     */
    public JsonModeMarshallException(Class<?> paramType)
    {
        super("Error marshalling type. See the logs for more details.");
        log.warn("Failure to marshall type: " + paramType.getName());

        this.paramType = paramType;
    }

    /**
     * Construct a JsonModeMarshallException with an exception and a destination type
     * @param paramType The type we were trying to marshall
     * @param ex error stack trace
     */
    public JsonModeMarshallException(Class<?> paramType, Throwable ex)
    {
        super("Error marshalling type. See the logs for more details.", ex);
        log.warn("Failure to marshall type: " + paramType.getName() + ". Due to: " + ex);

        this.paramType = paramType;
    }

    /**
     * Construct a JsonModeMarshallException with a description message and exception
     * @param paramType The type we were trying to marshall
     * @param message error description
     */
    public JsonModeMarshallException(Class<?> paramType, String message)
    {
        super(message);
        log.warn("Failure to marshall type: " + paramType.getName());

        this.paramType = paramType;
    }

    /**
     * Accessor for the type we are converting to/from
     * @return The type we are converting to/from
     */
    public Class<?> getConversionType()
    {
        return paramType;
    }

    /**
     * The type we are converting to/from
     */
    private Class<?> paramType;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JsonModeMarshallException.class);
}
