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

import java.lang.reflect.Method;

import org.directwebremoting.extend.Creator;
import org.directwebremoting.extend.MethodDeclaration;
import org.directwebremoting.util.FakeHttpServletRequest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultAccessControlTest
{
    private final DefaultAccessControl accessControl = new DefaultAccessControl();

    private FakeHttpServletRequest request;

    @Before
    public void setUp()
    {
        request = new FakeHttpServletRequest();
    }

    @Test(expected = SecurityException.class)
    public void testReasonToNotDisplayDwrObject() throws Exception
    {
        accessControl.assertMethodDisplayable(Class.forName("org.directwebremoting.impl.DefaultRemoter"), getMethod());
    }

    @Test
    public void testReasonToNotDisplay() throws Exception
    {
        accessControl.assertMethodDisplayable(Class.forName("java.lang.Object"), getMethod());
    }

    @Test(expected = SecurityException.class)
    public void testReasonToNotDisplayWithNonPublicMethod() throws Exception
    {
        accessControl.assertMethodDisplayable(Class.forName("java.lang.Object"), getPrivateMethod());
    }

    @Test(expected = SecurityException.class)
    public void testReasonToNotDisplayWithNonExecutableMethod() throws Exception
    {
        accessControl.addExcludeRule("scriptName", "someMethod");
        accessControl.assertGeneralDisplayable("scriptName", new MethodDeclaration(getMethod()));
    }

    @Test(expected = SecurityException.class)
    public void testReasonToNotDisplayWithMethodWithDwrParameter() throws Exception
    {
        accessControl.assertGeneralDisplayable("scriptName", new MethodDeclaration(getMethodWithDwrParameter()));
    }

    @Test(expected = SecurityException.class)
    public void testReasonToNotDisplayWithObjectMethod() throws Exception
    {
        accessControl.assertMethodDisplayable(Class.forName("java.lang.Object"), getHashCodeMethod());
    }

    @Ignore
    @Test
    public void testReasonToNotExecute() throws Exception
    {
        //WebContextBuilder builder = new DefaultWebContextBuilder();
        //builder.engageThread(null, new FakeHttpServletRequest(), new FakeHttpServletResponse());
        //WebContextFactory.setBuilder(builder);

        try
        {
            accessControl.assertMethodExecutionIsPossible(DefaultAccessControl.class, getMethod());
            fail();
        }
        catch (SecurityException ex)
        {
            assertNotNull(ex.getMessage());
        }

        accessControl.addRoleRestriction("className", "someMethod", "someRole");
        accessControl.addRoleRestriction("className", "someMethod", "someOtherRole");

        try
        {
            accessControl.assertMethodExecutionIsPossible(DefaultAccessControl.class, getMethod());
            fail();
        }
        catch (SecurityException ex)
        {
            assertNotNull(ex.getMessage());
        }

        request.addUserRole("someRole");

        try
        {
            accessControl.assertMethodExecutionIsPossible(DefaultAccessControl.class, getMethod());
            fail();
        }
        catch (SecurityException ex)
        {
            assertNotNull(ex.getMessage());
        }
    }

    /**
     *
     */
    public void someMethod()
    {
        // do nothing
    }

    /**
     * @param someString
     * @param creator
     */
    public void someMethodWithDwrParameter(String someString, Creator creator)
    {
        Object ignore = someString;
        ignore =  creator;
        creator = (Creator) ignore;

        // do nothing
    }

    /**
     *
     */
    private void somePrivateMethod()
    {
        // do nothing
    }

    private Method getMethod() throws NoSuchMethodException
    {
        return getClass().getMethod("someMethod", new Class[0]);
    }

    private Method getMethodWithDwrParameter() throws NoSuchMethodException
    {
        return getClass().getMethod("someMethodWithDwrParameter", new Class[]
        {
            String.class, Creator.class
        });
    }

    private Method getPrivateMethod() throws NoSuchMethodException
    {
        return getClass().getDeclaredMethod("somePrivateMethod", new Class[0]);
    }

    private Method getHashCodeMethod() throws NoSuchMethodException
    {
        return getClass().getMethod("hashCode", new Class[0]);
    }

    /**
     * Shuts lint up
     */
    protected void ignore()
    {
        somePrivateMethod();
    }
}
