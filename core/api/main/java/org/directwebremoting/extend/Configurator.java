package org.directwebremoting.extend;

import org.directwebremoting.Container;

/**
 * Provides a way to add bits of configuration to the system.
 * Predominantly created for Spring, however I can see that other systems and
 * particularly IoC containers may be glad of a system like this.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Configurator
{
    /**
     * Do the Configuration actions
     * @param container The object that contains the system objects to configure
     */
    void configure(Container container);
}
