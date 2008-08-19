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

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.json.parse.DebuggingJsonDecoder;
import org.directwebremoting.json.parse.IgnoreJsonDecoder;
import org.directwebremoting.json.parse.JsonParseException;
import org.directwebremoting.json.parse.JsonParser;
import org.directwebremoting.json.parse.SimpleTypesJsonDecoder;
import org.directwebremoting.json.parse.javacc.JavaccJsonParser;

/**
 * Various utilities to make parsing and reading JSON easier
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonUtil
{
    /**
     * Convert the input string into a set of basic types
     */
    public Map<String, Object> toSimpleTypes(String input) throws JsonParseException
    {
        log.info("testing: " + input);
        return toSimpleTypes(new StringReader(input));
    }

    /**
     * Convert the input document into a set of basic types
     */
    public Map<String, Object> toSimpleTypes(Reader reader) throws JsonParseException
    {
        JsonParser parser = new JavaccJsonParser();
        return parser.parse(reader, new DebuggingJsonDecoder<Map<String, Object>>(new SimpleTypesJsonDecoder()));
    }

    /**
     * Get a record of any errors in parsing the input string
     */
    public String getErrors(String input)
    {
        return getErrors(new StringReader(input));
    }

    /**
     * Get a record of any errors in parsing the input document
     */
    public String getErrors(Reader reader)
    {
        try
        {
            JsonParser parser = new JavaccJsonParser();
            parser.parse(reader, new IgnoreJsonDecoder());
            return null;
        }
        catch (JsonParseException ex)
        {
            return ex.toString();
        }
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JsonUtil.class);
}
