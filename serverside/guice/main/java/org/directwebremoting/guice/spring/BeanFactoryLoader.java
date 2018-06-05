package org.directwebremoting.guice.spring;

import org.springframework.beans.factory.BeanFactory;

/**
 * Knows how to load a concrete BeanFactory.
 * @author Tim Peierls
 */
public interface BeanFactoryLoader
{
    BeanFactory loadBeanFactory();
}
