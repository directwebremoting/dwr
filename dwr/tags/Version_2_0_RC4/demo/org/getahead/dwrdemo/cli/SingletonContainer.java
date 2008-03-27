package org.getahead.dwrdemo.cli;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.directwebremoting.impl.ContainerUtil;
import org.directwebremoting.impl.DefaultContainer;
import org.directwebremoting.impl.DwrXmlConfigurator;
import org.directwebremoting.impl.StartupUtil;
import org.directwebremoting.util.FakeServletConfig;
import org.directwebremoting.util.FakeServletContext;

/**
 * An example of how to setup DWR to run from a command line
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SingletonContainer
{
    /**
     * @param args The command line arguments
     * @throws Exception If anything goes wrong
     */
    public static void main(String[] args) throws Exception
    {
        WebContextBuilder webContextBuilder = null;

        try
        {
            ServletContext servletContext = new FakeServletContext();
            ServletConfig servletConfig = new FakeServletConfig("dwr-test", servletContext);

            // Setup the DWR container
            DefaultContainer container = ContainerUtil.createDefaultContainer(servletConfig);
            ContainerUtil.setupDefaultContainer(container, servletConfig);

            webContextBuilder = StartupUtil.initWebContext(servletConfig, servletContext, container);
            StartupUtil.initServerContext(servletConfig, servletContext, container);
            ContainerUtil.prepareForWebContextFilter(servletContext, servletConfig, container, webContextBuilder, null);

            ContainerUtil.configureFromSystemDwrXml(container);

            DwrXmlConfigurator local = new DwrXmlConfigurator();
            local.setClassResourceName("/dwr.xml");
            local.configure(container);

            ContainerUtil.configureFromInitParams(container, servletConfig);

            ContainerUtil.publishContainer(container, servletConfig);
            webContextBuilder.set(null, null, servletConfig, servletContext, container);

            // Now we have a fully configured DWR we can use it's services
            /*
            Remoter remoter = (Remoter) container.getBean(Remoter.class.getName());
            String script = remoter.generateInterfaceScript("<ExportedClass>", "<Path>");
            System.out.println(script);
            */
        }
        finally
        {
            if (webContextBuilder != null)
            {
                webContextBuilder.unset();
            }
        }
    }
}
