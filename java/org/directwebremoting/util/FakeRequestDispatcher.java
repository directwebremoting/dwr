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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Mock implementation of the RequestDispatcher interface.
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FakeRequestDispatcher implements RequestDispatcher
{
    /**
     * Create a new FakeRequestDispatcher for the given URL.
     * @param url the URL to dispatch to.
     */
    public FakeRequestDispatcher(String url)
    {
        this.url = url;
    }

    /* (non-Javadoc)
     * @see javax.servlet.RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    public void forward(ServletRequest request, ServletResponse response)
    {
        if (response.isCommitted())
        {
            throw new IllegalStateException("Cannot perform forward - response is already committed");
        }

        if (!(response instanceof FakeHttpServletResponse))
        {
            throw new IllegalArgumentException("FakeRequestDispatcher requires FakeHttpServletResponse");
        }

        ((FakeHttpServletResponse) response).setForwardedUrl(this.url);

        if (log.isDebugEnabled())
        {
            log.debug("FakeRequestDispatcher: forwarding to URL [" + this.url + "]");
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.RequestDispatcher#include(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    public void include(ServletRequest request, ServletResponse response)
    {
        if (!(response instanceof FakeHttpServletResponse))
        {
            throw new IllegalArgumentException("FakeRequestDispatcher requires FakeHttpServletResponse");
        }

        ((FakeHttpServletResponse) response).setIncludedUrl(this.url);

        if (log.isDebugEnabled())
        {
            log.debug("FakeRequestDispatcher: including URL [" + this.url + "]");
        }
    }

    private final String url;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(FakeRequestDispatcher.class);
}
