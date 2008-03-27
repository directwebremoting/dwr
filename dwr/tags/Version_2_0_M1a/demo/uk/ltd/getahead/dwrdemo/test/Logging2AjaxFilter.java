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
package uk.ltd.getahead.dwrdemo.test;

import java.lang.reflect.Method;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.util.Logger;


/**
 * An example filter that does some logging of Ajax calls
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Logging2AjaxFilter implements AjaxFilter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.AjaxFilter#doFilter(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], uk.ltd.getahead.dwr.AjaxFilterChain)
     */
    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception
    {
        log.debug("L2: About to execute: " + method.getName() + "() on " + obj); //$NON-NLS-1$ //$NON-NLS-2$
        Object reply = chain.doFilter(obj, method, params);
        log.debug("L2: - Executed: " + method.getName() + "() giving " + reply); //$NON-NLS-1$ //$NON-NLS-2$
        return reply;
    }
    
    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(Logging2AjaxFilter.class);
}
