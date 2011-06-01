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
import java.util.Map;


/**
 * A {@link org.directwebremoting.extend.Handler} that allows some very simple
 * search and replace templating. The general recommended syntax is to use
 * ${search} as the string to be searched for, to allow future expansion to a
 * more EL-like syntax.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public abstract class TemplateHandler extends CachingHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.CachingHandler#generate(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public String generateCachableContent(String contextPath, String servletPath, String pathInfo) throws IOException
    {
        String template = generateTemplate(contextPath, servletPath, pathInfo);

        Map<String, String> replace = getSearchReplacePairs(contextPath, servletPath, pathInfo);
        if (replace != null)
        {
            for (Map.Entry<String, String> entry : replace.entrySet())
            {
                String search = entry.getKey();
                if (template.contains(search))
                {
                    template = template.replace(search, entry.getValue());
                }
            }
        }

        return template;
    }

    /**
     * Generate a template to undergo search and replace processing according to
     * the search and replace pairs from {@link #getSearchReplacePairs(String, String, String)}.
     * @param contextPath
     * @param servletPath
     * @param pathInfo
     * @return A template string containing ${} sections to be replaced
     */
    protected abstract String generateTemplate(String contextPath, String servletPath, String pathInfo) throws IOException;

    /**
     * Mostly when we send a file out, we don't change anything so the default
     * set of search and replaces is empty.
     * Engine.js can override this with strings to customize the output
     * @param contextPath
     * @param servletPath
     * @param pathInfo
     * @return a map of search (key) and replace (value) strings
     */
    protected Map<String, String> getSearchReplacePairs(String contextPath, String servletPath, String pathInfo)
    {
        return null;
    }
}
