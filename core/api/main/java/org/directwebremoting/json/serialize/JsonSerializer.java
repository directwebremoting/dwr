package org.directwebremoting.json.serialize;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface JsonSerializer
{
    /**
     * @param data
     * @param out
     */
    void toJson(Object data, Writer out) throws IOException;
}
