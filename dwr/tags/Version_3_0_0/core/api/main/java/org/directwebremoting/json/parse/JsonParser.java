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

import java.io.Reader;

/**
 * Parse some JSON input and produce some objects that represent the input.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface JsonParser
{
    /**
     * Walk along the json <code>input</code> calling methods on
     * <code>decoder</code> as we discover new tokens in the input.
     * @param input The json data source
     * @param decoder The decoder to turn parse events into a data tree.
     * @return The object constructed by the {@link JsonDecoder}.
     * @throws JsonParseException If the input is not valid.
     */
    Object parse(Reader input, JsonDecoder decoder) throws JsonParseException;
}
