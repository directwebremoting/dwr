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
package org.directwebremoting.util;

/**
 * Various utilities that help us work with Jetty Continuations
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ContinuationUtil
{
    /**
     * We shouldn't be catching Jetty RetryRequests so we rethrow them.
     * @param ex The exception to test for continuation-ness
     */
    public static void rethrowIfContinuation(Throwable ex)
    {
        // Allow Jetty RequestRetry exception to propogate to container!
        if ("org.mortbay.jetty.RetryRequest".equals(ex.getClass().getName())) //$NON-NLS-1$
        {
            throw (RuntimeException) ex;
        }
    }
}
