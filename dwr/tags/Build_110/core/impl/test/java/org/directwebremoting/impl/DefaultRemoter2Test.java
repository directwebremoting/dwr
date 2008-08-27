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
        TestEnvironment.configureFromClassResource(DwrConstants.PACKAGE + "/impl/dwr.xml");
        container = TestEnvironment.getContainer();
        remoter = container.getBean(Remoter.class);
    }

    @Test
    public void testGenerateInterfaceScript()
    {
        String script = remoter.generateInterfaceScript("JDate", true, "/path/to/dwr/servlet");
        assertTrue(script.contains("JDate.getTimezoneOffset"));
        assertTrue(script.contains("JDate._path = '/path/to/dwr/servlet';"));
        assertTrue(!script.contains("JDate.notify"));
    }

    private static Container container;

    private static Remoter remoter;
}
