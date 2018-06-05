package org.directwebremoting.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.util.FakeHttpServletRequestFactory;
import org.directwebremoting.util.FakeHttpServletResponse;
import org.directwebremoting.util.FakeServletConfig;
import org.directwebremoting.util.FakeServletContextFactory;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrServletTest
{
    @Test
    @Ignore // TODO: need to adjust dirs (which WEB-INF dir is supposed to be used here???)
    public void doGet() throws Exception
    {
        DwrServlet servlet = new DwrServlet();

        ServletContext servletContext = FakeServletContextFactory.create("test/web");
        FakeServletConfig config = new FakeServletConfig("dwr-invoker", servletContext);
        servlet.init(config);

        HttpServletRequest request = FakeHttpServletRequestFactory.create();
        HttpServletResponse response = new FakeHttpServletResponse();
        servlet.doPost(request, response);
    }
}
