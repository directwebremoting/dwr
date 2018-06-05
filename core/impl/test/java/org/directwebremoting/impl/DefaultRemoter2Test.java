package org.directwebremoting.impl;

import org.directwebremoting.Container;
import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.extend.Remoter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultRemoter2Test
{
    @BeforeClass
    public static void setup() throws Exception
    {
        TestEnvironment.engageThread();
        TestEnvironment.configureFromClassResource(DwrConstants.PACKAGE_PATH + "/impl/dwr.xml");
        container = TestEnvironment.getContainer();
        remoter = container.getBean(Remoter.class);
    }

    @Test
    public void testGenerateInterfaceScript()
    {
        String script = remoter.generateInterfaceJavaScript("JDate", "", "p", "/path/to/dwr/servlet");
        assertTrue(script.contains("p.getTimezoneOffset"));
        assertTrue(!script.contains("p.notify"));
    }

    private static Container container;

    private static Remoter remoter;
}
