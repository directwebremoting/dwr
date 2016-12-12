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
package org.directwebremoting.json.types;

/**
 * The Json version of a Number
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonNumber extends JsonValue
{
    /**
     * All JsonNumbers wrap something stored as a double
     */
    public JsonNumber(int value)
    {
        this.value = value;
    }

    /**
     * All JsonNumbers wrap something stored as a double
     */
    public JsonNumber(long value)
    {
        this.value = value;
    }

    /**
     * All JsonNumbers wrap something stored as a double
     */
    public JsonNumber(double value)
    {
        this.value = value;
    }

    /**
     * Parse the input string as a double
     */
    public JsonNumber(String text)
    {
        this.value = Double.parseDouble(text);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#getDouble()
     */
    @Override
    public double getDouble()
    {
        return value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#getLong()
     */
    @Override
    public long getLong()
    {
        return (long) value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#getInteger()
     */
    @Override
    public int getInteger()
    {
        return (int) value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#toExternalRepresentation()
     */
    @Override
    public String toExternalRepresentation()
    {
        return Double.toString(value);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return toExternalRepresentation();
    }

    /**
     * The string value that we wrap
     */
    private final double value;
}
