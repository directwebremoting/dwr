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

/**
 * Accessor for the current WebContext.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class WebContextFactory
{
    /**
     * Accessor for the current WebContext.
     * @return The current WebContext or null if the current thread was not
     * started by DWR.
     */
    public static WebContext get()
    {
        if (builder == null)
        {
            return null;
        }

        return builder.get();
    }

    /**
     * Internal method to allow us to get the WebContextBuilder from which we
     * will get WebContext objects
     * @param builder The factory object (from DWRServlet)
     */
    protected static void setWebContextBuilder(WebContextBuilder builder)
    {
        WebContextFactory.builder = builder;
    }

    /**
     * The WebContextBuilder from which we will get WebContext objects
     */
    private static WebContextBuilder builder;
}
