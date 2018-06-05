package org.directwebremoting.json.parse;

import org.directwebremoting.Container;
import org.directwebremoting.ServerContext;
import org.directwebremoting.extend.Builder;
import org.directwebremoting.extend.Factory;

/**
 * An accessor for the current JsonParser.
 * The nested {@link Builder} will only be of use to system implementors.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonParserFactory
{
    /**
     * Accessor for the current JsonParser.
     * @return The current JsonParser.
     */
    public static JsonParser get()
    {
        return factory.get();
    }

    /**
     * Accessor for the current JsonParser in more complex setups.
     * For some setups DWR may not be able to discover the correct environment
     * (i.e. ServerContext), so we need to tell it. This generally happens if
     * you have DWR configured twice in a single context. Unless you are writing
     * code that someone else will configure, it is probably safe to use the
     * simpler {@link #get()} method.
     * @param ctx The servlet context to allow us to bootstrap
     * @return The current JsonParser.
     */
    public static JsonParser get(ServerContext ctx)
    {
        return factory.get(ctx);
    }

    /**
     * Internal method to allow us to get the Builder from which we
     * will get JsonParser objects.
     * Do NOT call this method from outside of DWR.
     */
    public static JsonParser attach(Container container)
    {
        return factory.attach(container);
    }

    private static Factory<JsonParser> factory = Factory.create(JsonParserBuilder.class);

    /**
     * Hack to get around Generics not being implemented by erasure
     */
    public interface JsonParserBuilder extends Builder<JsonParser>
    {
    }
}
