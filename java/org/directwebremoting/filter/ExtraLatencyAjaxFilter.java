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
package org.directwebremoting.filter;

import java.lang.reflect.Method;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;

/**
 * An example filter that delays responding to a query by a customizable time
 * to simulate internet latency. Half of the delay is inserted before and half
 * after the invocation.
 * <p>The default delay is 100 milliseconds.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ExtraLatencyAjaxFilter implements AjaxFilter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.AjaxFilter#doFilter(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], org.directwebremoting.AjaxFilterChain)
     */
    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception
    {
        synchronized (this)
        {
            try
            {
                wait(delay/2);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }

        Object reply = chain.doFilter(obj, method, params);

        synchronized (this)
        {
            try
            {
                wait(delay/2);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }

        return reply;
    }

    /**
     * @return Returns the delay in milliseconds.
     */
    public long getDelay()
    {
        return delay;
    }

    /**
     * @param delay The delay to set.
     */
    public void setDelay(long delay)
    {
        this.delay = delay;
    }

    private long delay = 100;
}
