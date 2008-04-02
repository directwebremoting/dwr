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

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.directwebremoting.create.NewCreator;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.impl.test.TestCreatedObject;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DebugPageGeneratorTest
{
    private DefaultDebugPageGenerator debugPageGenerator = new DefaultDebugPageGenerator();

    private CreatorManager creatorManager;

    private AccessControl accessControl;

    private ConverterManager converterManager;

    @Before
    public void setUp() throws Exception
    {
        creatorManager = EasyMock.createMock(CreatorManager.class);
        debugPageGenerator.setCreatorManager(creatorManager);

        accessControl = EasyMock.createMock(AccessControl.class);
        debugPageGenerator.setAccessControl(accessControl);

        converterManager = EasyMock.createMock(ConverterManager.class);
        debugPageGenerator.setConverterManager(converterManager);
    }

    @Test
    public void handleInNonDebug() throws Exception
    {
        creatorManager.isDebug();
        EasyMock.expectLastCall().andReturn(Boolean.FALSE);

        EasyMock.replay(creatorManager);
        EasyMock.replay(accessControl);
        EasyMock.replay(converterManager);

        String response = null;
        try
        {
            response = debugPageGenerator.generateIndexPage("/");
            fail("a security exception was expected");
        }
        catch (SecurityException e)
        {
            // do nothing, was expected
        }

        EasyMock.verify(creatorManager);
        EasyMock.verify(accessControl);
        EasyMock.verify(converterManager);

        assertNull(response);
        // assertTrue(new String(response.getBody()).indexOf("Test Pages") != -1);
    }

    @Ignore
    @Test
    public void handle() throws Exception
    {
        creatorManager.isDebug();
        EasyMock.expectLastCall().andReturn(Boolean.TRUE);

        creatorManager.getCreator("creatorName", false);
        NewCreator creator = new NewCreator();
        creator.setClass(TestCreatedObject.class.getName());
        EasyMock.expectLastCall().andReturn(creator);

        accessControl.assertIsDisplayable(EasyMock.eq(creator), EasyMock.eq("creatorName"), EasyMock.isA(Method.class));
        EasyMock.expectLastCall().andReturn(null).times(11);

        converterManager.isConvertable((Class<?>) EasyMock.anyObject());
        EasyMock.expectLastCall().andReturn(Boolean.TRUE).times(19);

        accessControl.assertExecutionIsPossible(EasyMock.eq(creator), EasyMock.eq("creatorName"), EasyMock.isA(Method.class));
        EasyMock.expectLastCall().andReturn(null).times(10);

        EasyMock.replay(creatorManager);
        EasyMock.replay(accessControl);
        EasyMock.replay(converterManager);

        String result = debugPageGenerator.generateTestPage("", "creatorName");

        EasyMock.verify(creatorManager);
        EasyMock.verify(accessControl);
        EasyMock.verify(converterManager);

        assertNotNull(result);
        assertTrue(result.indexOf("testMethodWithServletParameters(") != -1);
        assertTrue(result.indexOf("hashCode(") != -1);
        assertTrue(result.indexOf("getClass(") != -1);
        assertTrue(result.indexOf("wait(") != -1);
        assertTrue(result.indexOf("equals(") != -1);
        assertTrue(result.indexOf("notify(") != -1);
        assertTrue(result.indexOf("notifyAll(") != -1);
        assertTrue(result.indexOf("toString(") != -1);
    }

    @Test
    public void handleWithoutDebug() throws Exception
    {
        creatorManager.isDebug();
        EasyMock.expectLastCall().andReturn(Boolean.FALSE);

        EasyMock.replay(creatorManager);

        try
        {
            debugPageGenerator.generateIndexPage("root");
            fail("Missing SecurityException");
        }
        catch (SecurityException ex)
        {
        }

        EasyMock.verify(creatorManager);
    }

    @Test
    public void generateIndexPage() throws Exception
    {
        creatorManager.isDebug();
        EasyMock.expectLastCall().andReturn(Boolean.TRUE);

        creatorManager.getCreatorNames(false);
        ArrayList<String> names = new ArrayList<String>();
        names.add("creatorName");
        EasyMock.expectLastCall().andReturn(names);

        creatorManager.getCreator("creatorName", false);
        NewCreator creator = new NewCreator();
        creator.setClass(TestCreatedObject.class.getName());
        EasyMock.expectLastCall().andReturn(creator);

        EasyMock.replay(creatorManager);

        String result = debugPageGenerator.generateIndexPage("root");

        EasyMock.verify(creatorManager);

        assertNotNull(result);
        assertTrue(result.indexOf("creatorName") != -1);
        assertTrue(result.indexOf(TestCreatedObject.class.getName()) != -1);
    }
}
