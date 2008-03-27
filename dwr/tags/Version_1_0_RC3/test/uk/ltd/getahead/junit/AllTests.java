package uk.ltd.getahead.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class AllTests
{
    /**
     * @return test suite
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for uk.ltd.getahead.junit"); //$NON-NLS-1$
        //$JUnit-BEGIN$
        suite.addTestSuite(JavascriptUtilTest.class);
        //$JUnit-END$
        return suite;
    }
}
