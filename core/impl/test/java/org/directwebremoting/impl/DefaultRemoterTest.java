package org.directwebremoting.impl;

import org.junit.Ignore;
import org.junit.Test;


/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultRemoterTest
{
    // TODO: this whole test file needs updating
    @Test
    @Ignore
    public void dummyTestMethodToAvoidJunitComplaintsAboutNoMethods()
    {

    }
/*
    private final DefaultRemoter defaultRemoter = new DefaultRemoter();

    private CreatorManager creatorManager;

    private AccessControl accessControl;

    private ConverterManager converterManager;

    private AjaxFilterManager ajaxFilterManager;

    private FakeHttpServletRequest request;

    @Before
    public void setUp() throws Exception
    {
        creatorManager = createMock(CreatorManager.class);
        defaultRemoter.setCreatorManager(creatorManager);

        accessControl = createMock(AccessControl.class);
        defaultRemoter.setAccessControl(accessControl);

        ajaxFilterManager = createMock(AjaxFilterManager.class);
        defaultRemoter.setAjaxFilterManager(ajaxFilterManager);

        request = new FakeHttpServletRequest();
    }

    public void testHandle() throws Exception
    {
        request.setPathInfo("/exec/dataManager.doTest");
        request.setMethod("POST");
        request.setContent(("callCount=2\n" + "c0-id=1\nc0-scriptName=creatorName\nc0-methodName=toString\n"
            + "c1-id=2\nc1-scriptName=creatorName\nc1-methodName=hashCode").getBytes());

        creatorManager.getCreator("creatorName", false);
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
        expect(ajaxFilterManager.getAjaxFilters("creatorName")).andReturn(filters).atLeastOnce();

        replay(creatorManager);
        replay(accessControl);
        replay(converterManager);
        replay(ajaxFilterManager);

        DefaultWebContextBuilder builder = new DefaultWebContextBuilder();
        builder.engageThread(null, request, null);
        //TestWebContextFactory.setWebContextBuilder(builder);

        FakeHttpServletResponse response = new FakeHttpServletResponse();
        //Calls calls = marshaller.marshallInbound(request, response);
        //Replies replies = defaultRemoter.execute(calls);
        //marshaller.marshallOutbound(replies, request, response);

        String result = response.getContentAsString();
        verify(creatorManager);
        verify(accessControl);
        verify(converterManager);
        verify(ajaxFilterManager);

        assertNotNull(result);
        assertNotSame(result.indexOf("<script type='text/javascript'>"), -1);
        assertNotSame(result.indexOf("window.parent.DWREngine._handleResponse('1',"), -1);
        assertNotSame(result.indexOf("window.parent.DWREngine._handleResponse('2',"), -1);
        assertNotSame(result.indexOf("</script>"), -1);
    }

    @Ignore
    @Test
    public void handle2() throws Exception
    {
        request.setPathInfo("/interface/creatorName.js");

        creatorManager.getCreator("creatorName", false);
        NewCreator creator = new NewCreator();
        creator.setClass(TestCreatedObject.class.getName());
        expectLastCall().andReturn(creator);

        // expect 9 method calls to 'getReasonToNotDisplay' for each method on Object
        accessControl.assertIsDisplayable(creator, "creatorName", isA(Method.class));
        expectLastCall().andReturn(null).times(11);

        replay(creatorManager);
        replay(accessControl);

        String result = defaultRemoter.generateInterfaceScript("creatorName", true, "/path");

        verify(creatorManager);
        verify(accessControl);

        // check the response
        Assert.assertNotSame(result.indexOf("function creatorName() { }"), -1);
        Assert.assertNotSame(result.indexOf("creatorName.hashCode = function("), -1);
        Assert.assertNotSame(result.indexOf("creatorName.getClass = function("), -1);
        Assert.assertNotSame(result.indexOf("creatorName.wait = function("), -1);
        Assert.assertNotSame(result.indexOf("creatorName.wait = function("), -1);
        Assert.assertNotSame(result.indexOf("creatorName.wait = function("), -1);
        Assert.assertNotSame(result.indexOf("creatorName.equals = function("), -1);
        Assert.assertNotSame(result.indexOf("creatorName.notify = function("), -1);
        Assert.assertNotSame(result.indexOf("creatorName.notifyAll = function("), -1);
        Assert.assertNotSame(result.indexOf("creatorName.toString = function("), -1);
        Assert.assertNotSame(result.indexOf("creatorName.testMethodWithServletParameters = function("), -1);

        // make sure no entry is generated for the reserved javascript word 'namespace'
        Assert.assertSame(result.indexOf("creatorName.namespace = function("), -1);
    }

    @Ignore
    @Test
    public void handleWithoutInterface() throws Exception
    {
        request.setMethod("GET");

        creatorManager.getCreator("", false);
        expectLastCall().andThrow(new SecurityException());

        replay(creatorManager);
        replay(accessControl);

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

        creatorManager.getCreator("creatorName", false);
        NewCreator creator = new NewCreator();
        creator.setClass(TestCreatedObject.class.getName());
        expectLastCall().andReturn(creator);

        // expect 9 method calls to 'getReasonToNotDisplay' for each method on Object
        accessControl.assertIsDisplayable(eq(creator), eq("creatorName"), isA(Method.class));
        expectLastCall().andReturn("myReason").times(11);

        replay(creatorManager);
        replay(accessControl);

        FakeHttpServletResponse response = new FakeHttpServletResponse();
        //Calls calls = marshaller.marshallInbound(request, response);
        //Replies replies = defaultRemoter.execute(calls);
        //marshaller.marshallOutbound(replies, request, response);

        verify(creatorManager);
        verify(accessControl);

        // check the response
        String result = response.getContentAsString();
        Assert.assertNotSame(result.indexOf("function creatorName() { }"), -1);
        Assert.assertSame(result.indexOf("creatorName.hashCode = function(callback)"), -1);
        Assert.assertSame(result.indexOf("creatorName.getClass = function(callback)"), -1);
        Assert.assertSame(result.indexOf("creatorName.wait = function(callback)"), -1);
        Assert.assertSame(result.indexOf("creatorName.wait = function(p0, p1, callback)"), -1);
        Assert.assertSame(result.indexOf("creatorName.wait = function(p0, callback)"), -1);
        Assert.assertSame(result.indexOf("creatorName.equals = function(p0, callback)"), -1);
        Assert.assertSame(result.indexOf("creatorName.notify = function(callback)"), -1);
        Assert.assertSame(result.indexOf("creatorName.notifyAll = function(callback)"), -1);
        Assert.assertSame(result.indexOf("creatorName.toString = function(callback)"), -1);
        Assert.assertSame(result.indexOf("creatorName.testMethodWithServletParameters = function(callback)"), -1);
        Assert.assertSame(result.indexOf("creatorName.namespace = function(callback)"), -1);
    }
*/
}
