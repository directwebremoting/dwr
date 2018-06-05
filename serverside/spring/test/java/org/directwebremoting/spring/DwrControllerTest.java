package org.directwebremoting.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DwrControllerTest //extends AbstractDependencyInjectionSpringContextTests
{
    private DwrController controller;

    //@Override
    protected String[] getConfigLocations()
    {
        return new String[] { "/org/directwebremoting/spring/spring-configuration.xml" };
    }

    //@Override
    protected void onSetUp() throws Exception
    {
        //controller = (DwrController) applicationContext.getBean("dwrController");
    }

    /**
     * Subclasses can invoke this to get a context key for the given location.
     * This doesn't affect the applicationContext instance variable in this class.
     * Dependency Injection cannot be applied from such contexts.
     */
    //@Override
    protected ConfigurableApplicationContext loadContextLocations(String[] locations)
    {
        log.info("Loading my own config for: " + StringUtils.arrayToCommaDelimitedString(locations));

        XmlWebApplicationContext ctx = new XmlWebApplicationContext();
        ctx.setConfigLocations(locations);
        ctx.setServletContext(new MockServletContext());
        ctx.refresh();
        return ctx;
    }

    @Ignore
    @Test
    public void handleRequestInternal() throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        try
        {
            controller.handleRequestInternal(request, response);
        }
        catch (Exception e)
        {
            // TODO: what is this? SHould not be /WEB-INF/dwr.xml?
        }
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DwrControllerTest.class);
}
