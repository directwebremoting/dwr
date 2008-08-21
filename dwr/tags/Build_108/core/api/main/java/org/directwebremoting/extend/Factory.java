/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.extend;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory is not a user facing object, it is designed for system implementors.
 * Factory objects are generally use as helper classes by XFactory classes
 * whose methods reflect the methods of Factory, but which are static and
 * proxy the call to the contained Factory instance.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Factory<T>
{
    /**
     * Make it easy for XFactories to create a Factory instance.
     */
    public static <T> Factory<T> create()
    {
        return new Factory<T>();
    }

    /**
     * Accessor for the current object managed by this factory instance.
     */
    public T get()
    {
        if (builder == null)
        {
            log.warn("DWR has not been initialized properly");
            return null;
        }

        return builder.get();
    }

    /**
     * Accessor for the current object in more complex setups.
     * For some setups DWR may not be able to discover the correct environment
     * (i.e. ServletContext), so we need to tell it. This generally happens if
     * you have DWR configured twice in a single context. Unless you are writing
     * code that someone else will configure, it is probably safe to use the
     * simpler {@link #get()} method.
     * @param ctx The servlet context to allow us to bootstrap
     * @return The current JsonParser.
     */
    public T get(ServletContext ctx)
    {
        if (builder == null)
        {
            log.warn("DWR has not been initialized properly");
            return null;
        }

        return builder.get(ctx);
    }

    /**
     * Internal method to allow us to get the Builder from which we
     * will get JsonParser objects.
     * Do not call this method from outside of DWR.
     * @param builder The factory object (from DwrServlet)
     */
    public void setBuilder(Builder<T> builder)
    {
        this.builder = builder;
    }

    /**
     * The Builder from which we will get JsonParser objects
     */
    private Builder<T> builder = null;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Factory.class);
}
