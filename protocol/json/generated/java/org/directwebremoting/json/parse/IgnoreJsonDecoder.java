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
package org.directwebremoting.json.parse;

import java.math.BigDecimal;

/**
 * A {@link JsonDecoder} that doesn't do anything, which is useful for
 * validations that don't need to get any data, just check it's validity.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class IgnoreJsonDecoder implements JsonDecoder<Void>
{
    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#getRoot()
     */
    public Void getRoot()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addBoolean(boolean)
     */
    public void addBoolean(boolean b)
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addDouble(double)
     */
    public void addDouble(double parseDouble)
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addLong(long)
     */
    public void addLong(long parseLong)
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addInt(int)
     */
    public void addInt(int parseInt)
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNull()
     */
    public void addNull()
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addNumber(java.math.BigDecimal)
     */
    public void addNumber(BigDecimal bigDecimal)
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#addString(java.lang.String)
     */
    public void addString(String string)
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginArray()
     */
    public void beginArray()
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginMember()
     */
    public void beginMember()
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#beginObject()
     */
    public void beginObject()
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endArray()
     */
    public void endArray()
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endMember()
     */
    public void endMember()
    {
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.parse.JsonDecoder#endObject()
     */
    public void endObject()
    {
    }
}
