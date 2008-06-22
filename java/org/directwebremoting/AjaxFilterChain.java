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

import java.lang.reflect.Method;

/**
 * An AjaxFilterChain is provided by DWR to an AjaxFilter to allow it to pass
 * the request on for invocation.
 * @see org.directwebremoting.AjaxFilter
 * @since DWR 2.0
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface AjaxFilterChain
{
    /**
     * Causes the next filter in the chain to be invoked, or if the calling
     * filter is the last filter in the chain, causes the resource at the end of
     * the chain to be invoked.
     * @param obj The object to execute the method on (i.e. 'this')
     * @param method The method to execute
     * @param params The parameters to the method call
     * @return The results of the method execution
     * @throws Exception When some processing goes wrong
     * @since DWR 2.0
     */
    public Object doFilter(Object obj, Method method, Object[] params) throws Exception;
}
