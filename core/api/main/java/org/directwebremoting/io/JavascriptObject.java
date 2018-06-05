package org.directwebremoting.io;

/**
 * Represents a JavaScript object, passed in from a client for later use.
 * <p>A JavascriptObject is tied to a specific object in a specific browser
 * page. In this way the eval of a JavascriptObject is outside of the normal
 * execution scoping provided by {@link org.directwebremoting.Browser}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface JavascriptObject
{
    /**
     * Execute the function.
     * TODO: At some stage it would be good to allow the final parameter to be
     * a Callback....
     * @param params The data to pass to the server
     */
    public void execute(String methodName, Object... params);

    /**
     * Add a property to a JavaScript object. The type of the data must be
     * convertible by DWR.
     */
    public void set(String propertyName, Object data);

    /*
     * TODO: Add this in
     * Asynchronously fetch the value of some data from this object in the
     * browser.
     */
    //public <T> void get(String propertyName, Callback<T> callback);

    /**
     * A small amount of data is stored on the client to track the remotely.
     * accessible objects. To clear this data, the function needs to be cleared
     * on the server.
     */
    public void close();
}
