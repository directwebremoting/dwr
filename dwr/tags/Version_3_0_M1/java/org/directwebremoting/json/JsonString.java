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
package org.directwebremoting.json;

import org.directwebremoting.util.JavascriptUtil;

/**
 * The Json version of a String
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonString extends JsonValue
{
    /**
     * All JsonStrings wrap a Java string
     */
    public JsonString(String value)
    {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#getString()
     */
    @Override
    public String getString()
    {
        return value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#toExternalRepresentation()
     */
    @Override
    public String toExternalRepresentation()
    {
        return "'" + JavascriptUtil.escapeJavaScript(value) + "'";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getString();
    }

    /**
     * The string value that we wrap
     */
    private final String value;
}
