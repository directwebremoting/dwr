package org.directwebremoting.impl;

import org.directwebremoting.extend.CallbackHelper;
import org.directwebremoting.extend.CallbackHelperFactory.CallbackHelperBuilder;

/**
 * A Builder that creates {@link DefaultCallbackHelper}s.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultCallbackHelperBuilder extends DefaultBuilder<CallbackHelper> implements CallbackHelperBuilder
{
    /**
     * Initialize the DefaultBuilder with type of object to create
     */
    public DefaultCallbackHelperBuilder()
    {
        super(DefaultCallbackHelper.class);
    }
}
