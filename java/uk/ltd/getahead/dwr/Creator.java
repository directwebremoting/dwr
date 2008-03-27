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
package uk.ltd.getahead.dwr;

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
     * Application scope: named reference remains available in the
     * ServletContext until it is reclaimed. 
     */
    static final String APPLICATION = "application"; //$NON-NLS-1$

    /**
     * Session scope (only valid if this page participates in a session): the
     * named reference remains available from the HttpSession (if any)
     * associated with the Servlet until the HttpSession is invalidated. 
     */
    static final String SESSION = "session"; //$NON-NLS-1$

    /**
     * <b>This type of scope is not currently supported by DWR</b>
     * Request scope: the named reference remains available from the
     * ServletRequest associated with the Servlet until the current request is
     * completed.
     */
    static final String REQUEST = "request"; //$NON-NLS-1$

    /**
     * Page scope: (this is the default) the named reference remains available
     * in this PageContext until the return from the current Servlet.service()
     * invocation.
     */
    static final String PAGE = "page"; //$NON-NLS-1$
}
