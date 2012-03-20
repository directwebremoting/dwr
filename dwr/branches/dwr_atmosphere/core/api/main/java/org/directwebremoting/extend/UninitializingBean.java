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

/**
 * Like {@link InitializingBean} except that this requests notification when
 * things are shutting down.
 * @see InitializingBean
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface UninitializingBean
{
    /**
     * Called when a {@link javax.servlet.ServletContext} is being destroyed.
     * DWR finds out about this destruction if (and only if) in web.xml there
     * is a {@link org.directwebremoting.servlet.DwrListener} registered.
     * This happens before the {@link javax.servlet.http.HttpServlet#destroy()}
     * is called.
     * <p>
     * <strong>If DwrListener is not registered, this will not happen</strong>
     * <p>
     * This method should only be used when we need to take action to enable the
     * servlet to stop cleanly. Typically this will be restricted to stopping
     * reverse ajax threads.
     */
    void contextDestroyed();

    /**
     * Called when {@link javax.servlet.http.HttpServlet#destroy()} is called.
     * This event is the preferred time to close resources that don't require
     * all connections to be closed. The servletDestroy method is far more
     * likely to be called.
     */
    void servletDestroyed();
}
