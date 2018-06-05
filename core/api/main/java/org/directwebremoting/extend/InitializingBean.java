package org.directwebremoting.extend;

import org.directwebremoting.Container;

/**
 * Interface to be implemented by beans that need to react once all their
 * properties have been set by a {@link Container}: for example, to perform
 * custom initialization.
 * <p>This is similar in concept to the Spring
 * {@link org.springframework.beans.factory.InitializingBean}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface InitializingBean
{
    /**
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set
     * @param container The container that is doing the initialization
     */
    void afterContainerSetup(Container container);
}
