package org.directwebremoting.convert;

import org.directwebremoting.convert.test.MyBeanImpl;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;
import org.directwebremoting.extend.Property;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BeanConverterTest
{
    private final BeanConverter converter = new BeanConverter();

    private ConverterManager manager;

    @Before
    public void setUp() throws Exception
    {
        manager = EasyMock.createMock(ConverterManager.class);
        converter.setConverterManager(manager);
    }

    @Test
    public void setImplementation() throws Exception
    {
        converter.setImplementation(MyBeanImpl.class.getName());
        assertEquals(MyBeanImpl.class, converter.getInstanceType());
    }

    @Test(expected = Exception.class)
    public void setWrongImplementation() throws Exception
    {
        converter.setImplementation("UnknownClass");
    }

    @Test
    public void convertInboundWithNull() throws Exception
    {
        InboundVariable var = new InboundVariable(null, null, null, "null");
        Object result = converter.convertInbound(null, var);
        assertNull(result);
    }

    @Test(expected = Exception.class)
    public void convertInboundWithInvalidArguments1() throws Exception
    {
        // test with missing map start in the variable value
        InboundVariable var = new InboundVariable(null, null, null, "value");
        converter.convertInbound(null, var);
    }

    @Test(expected = Exception.class)
    public void convertInboundWithInvalidArguments2() throws Exception
    {
        // test with missing map end in the variable value
        InboundVariable var = new InboundVariable(null, null, null, "{ value");
        converter.convertInbound(null, var);
    }

    @Ignore
    @Test
    public void convertInbound() throws Exception
    {
        // also test with an instance type
        InboundVariable var = new InboundVariable(null, null, "type", "{ property: bla }");
        converter.setInstanceType(MyBeanImpl.class);

        EasyMock.expect(manager.convertInbound(EasyMock.eq(String.class),
                EasyMock.isA(InboundVariable.class),
                EasyMock.isA(Property.class))).andReturn("bla");
        EasyMock.replay(manager);

        Object result = converter.convertInbound(Object.class, var);
        assertNotNull(result);
        assertTrue(result instanceof MyBeanImpl);
        MyBeanImpl bean = (MyBeanImpl) result;
        assertEquals("bla", bean.getProperty());

        EasyMock.verify(manager);
    }

    @Test(expected = Exception.class)
    public void convertInboundNullPointerException() throws Exception
    {
        converter.convertInbound(null, null);
    }

    @Test(expected = Exception.class)
    public void convertInboundNullPointerException2() throws Exception
    {
        InboundVariable var = new InboundVariable(null, null, null, (String) null);
        converter.convertInbound(null, var);
    }

    @Test(expected = Exception.class)
    public void convertInboundIllegalArgumentException() throws Exception
    {
        InboundVariable var = new InboundVariable(null, null, null, "value");
        converter.convertInbound(null, var);
    }

    @Test(expected = Exception.class)
    public void convertInboundMarshallException() throws Exception
    {
        InboundVariable var = new InboundVariable(null, null, null, "{ value }");
        converter.convertInbound(null, var);
    }

    @Test(expected = Exception.class)
    public void convertInboundMarshallException2() throws Exception
    {
        InboundVariable var = new InboundVariable(null, null, null, "{ value }");
        converter.convertInbound(Object.class, var);
    }

    @Test(expected = Exception.class)
    public void convertInboundMarshallException3() throws Exception
    {
        InboundVariable var = new InboundVariable(null, null, null, "{ value }");
        // TODO: this is an error due to a null pointer exception in hashcode of InboundVariable. This should be fixed!
        converter.convertInbound(Object.class, var);
    }

    @Test(expected = Exception.class)
    public void convertInboundMarshallException4() throws Exception
    {
        InboundVariable var = new InboundVariable(null, null, "type", "{ value }");
        converter.convertInbound(Object.class, var);
    }

    @Ignore
    @Test
    public void convertInboundExceptions() throws Exception
    {
        InboundVariable var = new InboundVariable(null, null, "type", "{ value: , }");
        Object result = converter.convertInbound(Object.class, var);
        assertNotNull(result);

        var = new InboundVariable(null, null, "type", "{ value: , }");
        result = converter.convertInbound(Object.class, var);
        assertNotNull(result);
    }

    @Ignore
    @Test
    public void convertOutbound() throws Exception
    {
        OutboundContext ctx = new OutboundContext(false);

        EasyMock.expect(manager.convertOutbound("bla", ctx)).andReturn(new NonNestedOutboundVariable(""));
        EasyMock.replay(manager);

        MyBeanImpl bean = new MyBeanImpl();
        bean.setProperty("bla");
        OutboundVariable result = converter.convertOutbound(bean, ctx);
        assertNotNull(result);
        assertEquals("s0", result.getAssignCode());
        assertTrue(result.getBuildCode().length() > 10);

        EasyMock.verify(manager);
    }

    @Test(expected = Exception.class)
    public void convertOutboundWithInclusionsAndExclusions() throws Exception
    {
        converter.setInclude("bla, getSomething");
        converter.setExclude("bla");
    }

    @Test(expected = Exception.class)
    public void convertOutboundWithExclusionsAndInclusions() throws Exception
    {
        converter.setExclude("bla, getSomething");
        converter.setInclude("bla");
    }

    @Ignore
    @Test
    public void convertOutboundWithInclusions() throws Exception
    {
        OutboundContext ctx = new OutboundContext(false);

        converter.setInclude("property, getSomething");

        EasyMock.expect(manager.convertOutbound("bla", ctx)).andReturn(new NonNestedOutboundVariable(""));
        EasyMock.replay(manager);

        MyBeanImpl bean = new MyBeanImpl();
        bean.setProperty("bla");
        OutboundVariable result = converter.convertOutbound(bean, ctx);
        assertNotNull(result);
        assertEquals("s0", result.getAssignCode());
        //assertTrue(result.getBuildCode().length() > 10);

        EasyMock.verify(manager);
    }

    @Ignore
    @Test
    public void convertOutboundWithExclusions() throws Exception
    {
        OutboundContext ctx = new OutboundContext(false);

        converter.setExclude("property, getSomething");

        EasyMock.replay(manager);

        MyBeanImpl bean = new MyBeanImpl();
        bean.setProperty("bla");
        OutboundVariable result = converter.convertOutbound(bean, ctx);
        assertNotNull(result);
        assertEquals("s0", result.getAssignCode());
        //assertEquals(result.getBuildCode(), "");

        EasyMock.verify(manager);
    }

    @Test(expected = Exception.class)
    public void convertOutboundNullPointerException() throws Exception
    {
        converter.convertOutbound(null, null);
    }

    @Test(expected = Exception.class)
    public void convertOutboundNullPointerException2() throws Exception
    {
        converter.convertOutbound(null, new OutboundContext(false));
    }

    @Ignore
    @Test
    public void convertOutboundExceptions() throws Exception
    {
        OutboundVariable result = converter.convertOutbound(new Object(), new OutboundContext(false));
        assertNotNull(result);
        assertEquals("s0", result.getAssignCode());
        //assertEquals(result.getBuildCode(), "");
    }
}
