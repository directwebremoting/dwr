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
 * Used by {@link JsonParser} to allow the parse process to be mostly stateless,
 * and to abstract the process of creating objects. It is very likely that
 * {@link org.directwebremoting.json.parse.impl.AbstractJsonDecoder} will be
 * an easier start point than this.
 * TODO: Some naming tidy-up and thought around when the various addNumber methods are used
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface JsonDecoder<T>
{
    /**
     * When the parse is finished, this gives access to the result
     */
    T getRoot() throws JsonParseException;

    /**
     * We have encountered a {. The first thing to happen inside an object will
     * be an {@link #addString(String)} so we do need to remember that we are
     * inside an object.
     * <p>
     * In the future we might change the {@link #addString(String)} call to be
     * addPropertyName() or similar.
     */
    void beginObject() throws JsonParseException;

    /**
     * We have encountered a }
     */
    void endObject() throws JsonParseException;

    /**
     * We have encountered a :. This happens directly after an
     * {@link #addString(String)} and which happens after {@link #beginObject()}
     * Following this one of the add methods will be called for the data behind
     * the property, followed by {@link #endMember()}
     */
    void beginMember() throws JsonParseException;

    /**
     * @see #beginMember()
     */
    void endMember() throws JsonParseException;

    /**
     * We have encountered a [. What follows is a series of addX() calls and
     * then an {@link #endArray()}.
     */
    void beginArray() throws JsonParseException;

    /**
     * We have encountered a ].
     * @see #beginArray()
     */
    void endArray() throws JsonParseException;

    /**
     * Add a string member. See the note on {@link #beginObject()}
     */
    void addString(String value) throws JsonParseException;

    /**
     * Add an integer member. This method will always be used for numbers unless
     * the number will not fit into an integer.
     * @see #addLong(long)
     */
    void addInt(int value) throws JsonParseException;

    /**
     * Add an long member. After {@link #addInt(int)} this method will be used
     * for numbers unless the number will not fit into an integer.
     * @see #addDouble(double)
     */
    void addLong(long value) throws JsonParseException;

    /**
     * Add a double member. After {@link #addLong(long)} this method will be
     * used for numbers unless the number will not fit into a double.
     * @see #addNumber(BigDecimal)
     */
    void addDouble(double value) throws JsonParseException;

    /**
     * Add a BigDecimal member. After {@link #addDouble(double)} this method
     * will be used for numbers.
     */
    void addNumber(BigDecimal value) throws JsonParseException;

    /**
     * Add a boolean member
     */
    void addBoolean(boolean value) throws JsonParseException;

    /**
     * Add a null member
     */
    void addNull() throws JsonParseException;
}
