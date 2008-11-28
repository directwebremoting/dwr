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
package org.directwebremoting.impl.test;

import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TestCreatedObject
{
    public void testMethodWithServletParameters(HttpServletRequest request, HttpServletResponse response, ServletConfig config, ServletContext context, HttpSession session)
    {
        Object ignore = request;
        ignore = response;
        ignore = config;
        ignore = context;
        ignore = session;
        session = (HttpSession) ignore;
    }

    public Set<Integer> testBeanSetParam(Set<Integer> param)
    {
        return param;
    }

    /**
     * A method with a reserved javascript word as name.
     */
    public void namespace()
    {
        // do nothing
    }
}
