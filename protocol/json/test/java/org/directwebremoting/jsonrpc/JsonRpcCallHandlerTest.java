package org.directwebremoting.jsonrpc;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.Container;
import org.directwebremoting.create.NewCreator;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.impl.TestEnvironment;
import org.directwebremoting.util.FakeHttpServletRequest;
import org.directwebremoting.util.FakeHttpServletRequestFactory;
import org.directwebremoting.util.FakeHttpServletResponse;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonRpcCallHandlerTest
{
    @BeforeClass
    public static void setup()
    {
        TestEnvironment.engageThread();
    }

    /**
     * Test method for {@link org.directwebremoting.jsonrpc.JsonRpcCallHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
     */
    @Test
    public void testHandle() throws Exception
    {
        Container container = TestEnvironment.getContainer();
        JsonRpcCallHandler handler = (JsonRpcCallHandler) container.getBean("url:/jsonrpc");
        handler.setJsonRpcEnabled(true);
        CreatorManager creatorManager = container.getBean(CreatorManager.class);

        NewCreator creator = new NewCreator();
        creator.setClassName("com.example.dwr.simple.Demo");
        creator.setJavascript("Demo");
        creatorManager.addCreator(creator);

        List<String> validTests = TestUtil.parseTestInput(getClass(), "validJsonRpc.txt");
        for (String test : validTests)
        {
            FakeHttpServletRequest request = FakeHttpServletRequestFactory.create();
            request.setContent(test);
            FakeHttpServletResponse response = new FakeHttpServletResponse();

            handler.handle(request, response);
            String output = new String(response.getContentAsByteArray());
            log.info(output);
        }
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(JsonRpcCallHandlerTest.class);
}
