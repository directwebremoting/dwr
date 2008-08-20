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
package org.directwebremoting.impl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.directwebremoting.Container;
import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory.ServerContextBuilder;

/**
 * A ServerContextBuilder that creates DefaultServerContexts.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultServerContextBuilder extends DefaultBuilder<ServerContext> implements ServerContextBuilder
{
    /**
     * Initialize the DefaultBuilder with type of object to create
     */
    public DefaultServerContextBuilder()
    {
        super(DefaultServerContext.class, ServletConfig.class, ServletContext.class, Container.class);
    }
}
