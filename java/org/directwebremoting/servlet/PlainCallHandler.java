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
package org.directwebremoting.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.dwrp.PlainCallMarshaller;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.Handler;
import org.directwebremoting.extend.Remoter;
import org.directwebremoting.extend.Replies;

/**
 * A Handler standard DWR calls whose replies are NOT HTML wrapped.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class PlainCallHandler implements Handler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.Handler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Calls calls = null;

        try
        {
            calls = plainCallMarshaller.marshallInbound(request, response);
        }
        catch (Exception ex)
        {
            plainCallMarshaller.marshallException(request, response, ex);
            return;
        }

        Replies replies = remoter.execute(calls);
        plainCallMarshaller.marshallOutbound(replies, request, response);
    }

    /**
     * Setter for the Plain Javascript Marshaller
     * @param marshaller The new marshaller
     */
    public void setPlainCallMarshaller(PlainCallMarshaller marshaller)
    {
        this.plainCallMarshaller = marshaller;
    }

    /**
     * Setter for the remoter
     * @param remoter
     */
    public void setRemoter(Remoter remoter)
    {
        this.remoter = remoter;
    }

    /**
     * The 'HTML Javascript' method by which objects are marshalled
     */
    protected PlainCallMarshaller plainCallMarshaller = null;

    /**
     * The bean to execute remote requests and generate interfaces
     */
    protected Remoter remoter = null;
}
