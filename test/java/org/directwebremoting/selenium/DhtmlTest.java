package org.directwebremoting.selenium;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.server.SeleniumServer;

import com.thoughtworks.selenium.SeleneseTestCase;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DhtmlTest extends SeleneseTestCase
{
    private static final String[] ONE_TO_FIVE = new String[] { "One", "Two", "Three", "Four", "Five" };
    private static final String[] EMPTY = new String[0];

    /* (non-Javadoc)
     * @see com.thoughtworks.selenium.SeleneseTestCase#setUp()
     */
    @Override
    public void setUp() throws Exception
    {
        // default max is 200k; zero is infinite
        System.setProperty("org.mortbay.http.HttpRequest.maxFormContentSize", "0");

        SeleniumServer server = new SeleniumServer();
        server.start();

        super.setUp();
    }

    /**
     * @throws Exception
     */
    public void testNew() throws Exception
    {
        selenium.open("http://localhost:8080/dwr-test/test/");
        selenium.click("link=dhtml.html");
        selenium.waitForPageToLoad("30000");

        verifyEquals(selenium.getSelectOptions("removeOptions"), ONE_TO_FIVE);
        selenium.click("//button[@onclick='testRemoveOptions();']");
        verifyEquals(selenium.getSelectOptions("removeOptions"), EMPTY);

        verifyEquals(selenium.getSelectOptions("testAddOptionsBasic"), EMPTY);
        selenium.click("//button[@onclick='testAddOptionsBasic();']");
        verifyEquals(selenium.getSelectOptions("testAddOptionsBasic"), ONE_TO_FIVE);

        verifyEquals(selenium.getSelectOptions("testAddOptionsObject1"), EMPTY);
        selenium.click("//button[@onclick='testAddOptionsObject1();']");
        verifyEquals(selenium.getSelectOptions("testAddOptionsObject1"), ONE_TO_FIVE);

        verifyEquals(selenium.getSelectOptions("testAddOptionsObject2"), EMPTY);
        selenium.click("//button[@onclick='testAddOptionsObject2();']");
        verifyEquals(selenium.getSelectOptions("testAddOptionsObject2"), ONE_TO_FIVE);
    }

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(DhtmlTest.class);
}
