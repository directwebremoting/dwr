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

import java.util.Map;

/**
 * A base class for all AllowedClasses
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Creator
{
    /**
     * DefaultConfiguration is done via access to the DOM Element.
     * This is not at all ideal, but it will do for the moment.
     * @param params The map of paramters to configure the creator
     * @throws IllegalArgumentException If the config data in the Element is invalid
     */
    void setProperties(Map params) throws IllegalArgumentException;

    /**
     * Accessor for the <code>java.lang.Class</code> that this Creator
     * allows access to.
     * @return The type of this allowed class
     */
    Class getType();

    /**
     * Accessor for the/an instance of this Creator.
     * @return the instance to use
     * @throws InstantiationException If for some reason the object can not be created
     */
    Object getInstance() throws InstantiationException;

    /**
     * Each Creator creates objects with a given scope.
     * @return How long do we hold onto instances created by this Creator
     */
    String getScope();

    /**
     * Is the class behind the Creator likely to change over time?
     * @return Returns the reloadable variable
     */
    boolean isCacheable();

    /**
     * How is this creator refered to in Javascript land?
     * @return A Javascript identifier
     */
    String getJavascript();

    /**
     * Application scope: named reference remains available in the
     * ServletContext until it is reclaimed.
     */
    static final String APPLICATION = "application";

    /**
     * Session scope (only valid if this page participates in a session): the
     * named reference remains available from the HttpSession (if any)
     * associated with the Servlet until the HttpSession is invalidated.
     */
    static final String SESSION = "session";

    /**
     * Script scope (tied to a id recorded in Javascript): the named reference
     * remains available while the script variable remains stored in the
     * browser.
     */
    static final String SCRIPT = "script";

    /**
     * Request scope: the named reference remains available from the
     * ServletRequest associated with the Servlet until the current request is
     * completed. This scope type is almost identical to {@link #PAGE} scope
     * and it is recommended to use {@link #PAGE} scope in place of this scope.
     * Use of {@value #REQUEST} may be deprecated in the future.
     */
    static final String REQUEST = "request";

    /**
     * Page scope: (this is the default) the named reference remains available
     * in this PageContext until the return from the current Servlet.service()
     * invocation.
     */
    static final String PAGE = "page";
}
