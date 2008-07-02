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
package org.directwebremoting.spring;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

/**
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrSpringServletTest
{
    @Ignore
    @Test
    public void doPost() throws Exception
    {
        DwrSpringServlet servlet = new DwrSpringServlet();

        MockServletConfig config = new MockServletConfig(new MockServletContext());
        servlet.setIncludeDefaultConfig(false);
        servlet.init(config);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doPost(request, response);
    }

    @Ignore
    @Test
    public void mockPost() throws Exception
    {
        /*
        BeanFactory factory = EasyMock.createMock(BeanFactory.class);

        UrlProcessor processor = new UrlProcessor();
        WebContextBuilder builder = EasyMock.createMock(WebContextBuilder.class);

        EasyMock.expect(factory.getBean(UrlProcessor.class.getName())).andReturn(processor);
        EasyMock.expect(factory.getBean(WebContextBuilder.class.getName())).andReturn(builder);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        processor.handle(request, response);

        EasyMock.replay(factory);

        EasyMock.verify(factory);
        */
    }
}
