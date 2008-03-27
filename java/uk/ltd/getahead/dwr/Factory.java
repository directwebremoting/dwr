package uk.ltd.getahead.dwr;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Factory
{
    /**
     * Prevent Instansiation
     */
    private Factory()
    {
    }

    /**
     * Singleton accessor for the Doorman
     * @return The new Doorman
     */
    public static Doorman getDoorman()
    {
        return doorman;
    }

    private static Doorman doorman = new Doorman();
}
