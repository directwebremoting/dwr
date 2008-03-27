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
