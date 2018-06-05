package org.directwebremoting.json.parse.javacc;

import org.directwebremoting.impl.DefaultBuilder;
import org.directwebremoting.json.parse.JsonParser;
import org.directwebremoting.json.parse.JsonParserFactory.JsonParserBuilder;

/**
 * A Builder that creates {@link JavaccJsonParser}s.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JavaccJsonParserBuilder extends DefaultBuilder<JsonParser> implements JsonParserBuilder
{
    /**
     * Initialize the DefaultBuilder with type of object to create
     */
    public JavaccJsonParserBuilder()
    {
        super(JavaccJsonParser.class);
    }
}
