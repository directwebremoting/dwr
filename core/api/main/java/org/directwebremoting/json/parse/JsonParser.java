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
