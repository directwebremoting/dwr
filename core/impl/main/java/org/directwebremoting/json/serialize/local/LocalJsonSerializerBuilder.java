package org.directwebremoting.json.serialize.local;

import org.directwebremoting.impl.DefaultBuilder;
import org.directwebremoting.json.serialize.JsonSerializer;
import org.directwebremoting.json.serialize.JsonSerializerFactory.JsonSerializerBuilder;

/**
 * A Builder that creates {@link LocalJsonSerializer}s.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class LocalJsonSerializerBuilder extends DefaultBuilder<JsonSerializer> implements JsonSerializerBuilder
{
    /**
     * Initialize the DefaultBuilder with type of object to create
     */
    public LocalJsonSerializerBuilder()
    {
        super(LocalJsonSerializer.class);
    }
}
