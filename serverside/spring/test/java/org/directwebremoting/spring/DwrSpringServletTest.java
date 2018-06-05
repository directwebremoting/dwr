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
