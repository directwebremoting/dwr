package org.directwebremoting.extend;

import org.directwebremoting.ui.Callback;

/**
 * TODO: can we make this work without a factory?
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface CallbackHelper
{
    /**
     * Used by the many proxy classes to record a callback for later calling
     * @param callback The callback that acts on the type
     * @param type The type of the data that we are recording
     * @return A key under which the data is stored
     */
    public <T> String saveCallback(Callback<T> callback, Class<T> type);
}
