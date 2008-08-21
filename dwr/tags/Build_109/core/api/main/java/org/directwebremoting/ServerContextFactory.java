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
package org.directwebremoting;

import javax.servlet.ServletContext;

import org.directwebremoting.extend.Builder;
import org.directwebremoting.extend.Factory;

/**
 * Accessor for the current ServerContext.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ServerContextFactory
{
    /**
     * Accessor for the current ServerContext.
     * @return The current ServerContext.
     */
    public static ServerContext get()
    {
        return factory.get();
    }

    /**
     * Accessor for the current ServerContext in more complex setups.
     * For some setups DWR may not be able to discover the correct environment
     * (i.e. ServletContext), so we need to tell it. This generally happens if
     * you have DWR configured twice in a single context. Unless you are writing
     * code that someone else will configure, it is probably safe to use the
     * simpler {@link #get()} method.
     * @param ctx The servlet context to allow us to bootstrap
     * @return The current ServerContext.
     */
    public static ServerContext get(ServletContext ctx)
    {
        return factory.get(ctx);
    }

    /**
     * Internal method to allow us to get the Builder from which we
     * will get ServerContext objects.
     * Do not call this method from outside of DWR.
     * @param builder The factory object (from DwrServlet)
     */
    public static void setBuilder(Builder<ServerContext> builder)
    {
        factory.setBuilder(builder);
    }

    /**
     * The factory helper class
     */
    private static Factory<ServerContext> factory = Factory.create();

    /**
     * Hack to get around Generics not being implemented by erasure
     */
    public interface ServerContextBuilder extends Builder<ServerContext>
    {
    }
}
