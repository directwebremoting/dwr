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
import java.util.ArrayList;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.create.NewCreator;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.AjaxFilterManager;
import org.directwebremoting.extend.Calls;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.Marshaller;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.Replies;
import org.directwebremoting.impl.test.TestCreatedObject;
import org.directwebremoting.impl.test.TestWebContextFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultRemoterTest
{
    private DefaultRemoter defaultRemoter = new DefaultRemoter();

    private CreatorManager creatorManager;

    private AccessControl accessControl;

    private ConverterManager converterManager;

    private AjaxFilterManager ajaxFilterManager;

    private MockHttpServletRequest request;

    private Marshaller marshaller;

    @Before
    public void setUp() throws Exception
    {
        creatorManager = createMock(CreatorManager.class);
        defaultRemoter.setCreatorManager(creatorManager);

        accessControl = createMock(AccessControl.class);
        defaultRemoter.setAccessControl(accessControl);

        marshaller = createMock(Marshaller.class);

        ajaxFilterManager = createMock(AjaxFilterManager.class);
        defaultRemoter.setAjaxFilterManager(ajaxFilterManager);

        request = new MockHttpServletRequest();
    }

    /**
     * @throws Exception
     */
    public void testHandle() throws Exception
    {
        request.setPathInfo("/exec/dataManager.doTest");
        request.setMethod("POST");
        request.setContent(("callCount=2\n" + "c0-id=1\nc0-scriptName=creatorName\nc0-methodName=toString\n"
            + "c1-id=2\nc1-scriptName=creatorName\nc1-methodName=hashCode").getBytes());

        creatorManager.getCreator("creatorName");
        NewCreator creator = new NewCreator();
        creator.setClass(TestCreatedObject.class.getName());
        expectLastCall().andReturn(creator).times(4);

        accessControl.assertExecutionIsPossible(eq(creator), eq("creatorName"), isA(Method.class));
        expectLastCall().andReturn(null).times(2);

        expect(converterManager.convertOutbound(isA(Object.class), isA(OutboundContext.class)));
        expectLastCall().andReturn(new NonNestedOutboundVariable("")).times(2);

        // TODO: this should not be necessary!
        ArrayList<AjaxFilter> filters = new ArrayList<AjaxFilter>();
        filters.add(new AjaxFilter()
        {
            public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception
            {
                return new Object();
            }
        });
        filters.add(new AjaxFilter()
        {
            public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception
            {
                return new Object();
            }
        });
        expect(ajaxFilterManager.getAjaxFilters("creatorName")).andReturn(filters.iterator()).atLeastOnce();

        replay(creatorManager);
        replay(accessControl);
        replay(converterManager);
        replay(ajaxFilterManager);

        DefaultWebContextBuilder builder = new DefaultWebContextBuilder();
        builder.set(request, null, null, null, null);
        TestWebContextFactory.setWebContextBuilder(builder);

        MockHttpServletResponse response = new MockHttpServletResponse();
        Calls calls = marshaller.marshallInbound(request, response);
        Replies replies = defaultRemoter.execute(calls);
        marshaller.marshallOutbound(replies, request, response);

        String result = response.getContentAsString();
        verify(creatorManager);
        verify(accessControl);
        verify(converterManager);
        verify(ajaxFilterManager);

        assertNotNull(result);
        assertTrue(result.indexOf("<script type='text/javascript'>") != -1);
        assertTrue(result.indexOf("window.parent.DWREngine._handleResponse('1',") != -1);
        assertTrue(result.indexOf("window.parent.DWREngine._handleResponse('2',") != -1);
        assertTrue(result.indexOf("</script>") != -1);
    }

    @Ignore
    @Test
    public void handle2() throws Exception
    {
        request.setPathInfo("/interface/creatorName.js");

        creatorManager.getCreator("creatorName");
        NewCreator creator = new NewCreator();
        creator.setClass(TestCreatedObject.class.getName());
        expectLastCall().andReturn(creator);

        // expect 9 method calls to 'getReasonToNotDisplay' for each method on Object
        accessControl.assertIsDisplayable(creator, "creatorName", isA(Method.class));
        expectLastCall().andReturn(null).times(11);

        replay(creatorManager);
        replay(accessControl);

        String result = defaultRemoter.generateInterfaceScript("creatorName", "/path");

        verify(creatorManager);
        verify(accessControl);

        // check the response
        assertTrue(result.indexOf("function creatorName() { }") != -1);
        assertTrue(result.indexOf("creatorName.hashCode = function(") != -1);
        assertTrue(result.indexOf("creatorName.getClass = function(") != -1);
        assertTrue(result.indexOf("creatorName.wait = function(") != -1);
        assertTrue(result.indexOf("creatorName.wait = function(") != -1);
        assertTrue(result.indexOf("creatorName.wait = function(") != -1);
        assertTrue(result.indexOf("creatorName.equals = function(") != -1);
        assertTrue(result.indexOf("creatorName.notify = function(") != -1);
        assertTrue(result.indexOf("creatorName.notifyAll = function(") != -1);
        assertTrue(result.indexOf("creatorName.toString = function(") != -1);
        assertTrue(result.indexOf("creatorName.testMethodWithServletParameters = function(") != -1);

        // make sure no entry is generated for the reserved javascript word 'namespace'
        assertFalse(result.indexOf("creatorName.namespace = function(") != -1);
    }

    @Ignore
    @Test
    public void handleWithoutInterface() throws Exception
    {
        request.setMethod("GET");

        creatorManager.getCreator("");
        expectLastCall().andThrow(new SecurityException());

        replay(creatorManager);
        replay(accessControl);

        try
        {
            Calls calls = marshaller.marshallInbound(request, null);
            defaultRemoter.execute(calls);
            fail("a security exception was expected");
        }
        catch (SecurityException e)
        {
            // do nothing, was expected
        }

        verify(creatorManager);
        verify(accessControl);
    }

    @Ignore
    @Test
    public void handleWithReasonsNotToDisplay() throws Exception
    {
        request.setMethod("GET");
        
        // make sure not to allow an impossible test
        defaultRemoter.setAllowImpossibleTests(false);

        request.setPathInfo("/interface/creatorName.js");

        creatorManager.getCreator("creatorName");
        NewCreator creator = new NewCreator();
        creator.setClass(TestCreatedObject.class.getName());
        expectLastCall().andReturn(creator);

        // expect 9 method calls to 'getReasonToNotDisplay' for each method on Object
        accessControl.assertIsDisplayable(eq(creator), eq("creatorName"), isA(Method.class));
        expectLastCall().andReturn("myReason").times(11);

        replay(creatorManager);
        replay(accessControl);

        MockHttpServletResponse response = new MockHttpServletResponse();
        Calls calls = marshaller.marshallInbound(request, response);
        Replies replies = defaultRemoter.execute(calls);
        marshaller.marshallOutbound(replies, request, response);

        verify(creatorManager);
        verify(accessControl);

        // check the response
        String result = response.getContentAsString();
        assertTrue(result.indexOf("function creatorName() { }") != -1);
        assertFalse(result.indexOf("creatorName.hashCode = function(callback)") != -1);
        assertFalse(result.indexOf("creatorName.getClass = function(callback)") != -1);
        assertFalse(result.indexOf("creatorName.wait = function(callback)") != -1);
        assertFalse(result.indexOf("creatorName.wait = function(p0, p1, callback)") != -1);
        assertFalse(result.indexOf("creatorName.wait = function(p0, callback)") != -1);
        assertFalse(result.indexOf("creatorName.equals = function(p0, callback)") != -1);
        assertFalse(result.indexOf("creatorName.notify = function(callback)") != -1);
        assertFalse(result.indexOf("creatorName.notifyAll = function(callback)") != -1);
        assertFalse(result.indexOf("creatorName.toString = function(callback)") != -1);
        assertFalse(result.indexOf("creatorName.testMethodWithServletParameters = function(callback)") != -1);
        assertFalse(result.indexOf("creatorName.namespace = function(callback)") != -1);
    }
}
