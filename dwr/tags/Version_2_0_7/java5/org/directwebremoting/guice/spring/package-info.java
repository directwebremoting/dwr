/**
 * <p>
 *   This package provides support for using Spring beans in a DWR-Guice setting.
 *   This documentation assumes you already understand 
 *    <a href="http://code.google.com/p/google-guice/">Guice</a> concepts and
 *   some core <a href="http://springframework.org">Spring</a> concepts.
 * </p>
 * <p>
 *   To use this support, in the {@code configure} method of a concrete 
 *   extension of 
 *   {@link org.directwebremoting.guice.DwrGuiceServletContextListener DwrGuiceServletContextListener},
 *   call {@code install(new SpringModule(loader))}, where {@code loader}
 *   is an implementation of the 
 *   {@link org.directwebremoting.guice.spring.BeanFactoryLoader BeanFactoryLoader}
 *   interface. This package includes one such implementation, 
 *   {@link org.directwebremoting.guice.spring.WebApplicationContextLoader WebApplicationContextLoader}.
 * </p>
 * @author Tim Peierls [tim at peierls dot net]
 */
package org.directwebremoting.guice.spring;
