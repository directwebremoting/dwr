package org.directwebremoting.impl;

import org.directwebremoting.Hub;
import org.directwebremoting.HubFactory.HubBuilder;

/**
 * A Builder that creates DefaultHubs.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultHubBuilder extends DefaultBuilder<Hub> implements HubBuilder
{
    /**
     * Initialize the DefaultBuilder with type of object to create
     */
    public DefaultHubBuilder()
    {
        super(DefaultHub.class);
    }
}
