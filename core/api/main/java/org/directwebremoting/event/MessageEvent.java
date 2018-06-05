package org.directwebremoting.event;

import java.util.EventObject;

import org.directwebremoting.Hub;

/**
 * An MessageEvent is fired to a set of {@link MessageListener}s by the DWR
 * {@link Hub}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface MessageEvent
{
    /**
     * @see EventObject#getSource()
     * @return the hub that processed this message
     */
    public Hub getHub();

    /**
     * We convert the data (if the message is from the client) as late as
     * possible so the message recipient can choose what type it should be.
     * @param <T> The type that we are converting to
     * @param asType The type that we are converting to
     * @return The data coerced into the required type
     */
    public <T> T getData(Class<T> asType);

    /**
     * WARNING: This method is for internal use only. It may well disappear at
     * some stage in the future
     * Sometimes we just want to get at whatever the data was originally without
     * any conversion.
     * @return The original data probably of type RawData
     */
    public Object getRawData();
}
