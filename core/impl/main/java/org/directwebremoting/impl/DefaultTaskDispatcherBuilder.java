package org.directwebremoting.impl;

import org.directwebremoting.extend.TaskDispatcher;
import org.directwebremoting.extend.TaskDispatcherFactory.TaskDispatcherBuilder;

/**
 * A Builder that creates {@link DefaultTaskDispatcher}s.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultTaskDispatcherBuilder extends DefaultBuilder<TaskDispatcher> implements TaskDispatcherBuilder
{
    /**
     * Initialize the DefaultBuilder with type of object to create
     */
    public DefaultTaskDispatcherBuilder()
    {
        super(DefaultTaskDispatcher.class);
    }
}
