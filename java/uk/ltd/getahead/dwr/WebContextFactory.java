package uk.ltd.getahead.dwr;

/**
 * Accessor for the current WebContext.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class WebContextFactory
{
    /**
     * Accessor for the current WebContext.
     * @return The current WebContext
     */
    public static WebContext get()
    {
        return builder.get();
    }

    /**
     * Internal method to allow us to get the WebContextBuilder from which we
     * will get WebContext objects
     * @param builder The factory object (from DWRServlet)
     */
    protected static void setWebContextBuilder(WebContextBuilder builder)
    {
        WebContextFactory.builder = builder;
    }

    /**
     * The WebContextBuilder from which we will get WebContext objects
     */
    private static WebContextBuilder builder;
}
