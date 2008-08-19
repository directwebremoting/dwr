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
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface JsonDecoder<T>
{
    T getRoot();

    /**
     *
     */
    void beginObject();

    /**
     *
     */
    void endObject();

    /**
     *
     */
    void beginMember();

    /**
     *
     */
    void endMember();

    /**
     *
     */
    void beginArray();

    /**
     *
     */
    void endArray();

    /**
     * @param value
     */
    void addString(String value);

    /**
     * @param value
     */
    void addNumber(BigDecimal value);

    /**
     * @param value
     */
    void addDouble(double value);

    /**
     * @param value
     */
    void addLong(long value);

    /**
     * @param value
     */
    void addInt(int value);

    /**
     * @param value
     */
    void addBoolean(boolean value);

    /**
     *
     */
    void addNull();
}
