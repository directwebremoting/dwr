package org.directwebremoting.impl;

import java.util.ArrayList;

import org.directwebremoting.create.NewCreator;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.Module;
import org.directwebremoting.extend.ModuleManager;
import org.directwebremoting.impl.test.TestCreatedObject;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DebugPageGeneratorTest
{
    private final DefaultDebugPageGenerator debugPageGenerator = new DefaultDebugPageGenerator();

    private ModuleManager moduleManager;

    private AccessControl accessControl;

    private ConverterManager converterManager;

    @Before
    public void setUp() throws Exception
    {
        moduleManager = EasyMock.createMock(ModuleManager.class);
        debugPageGenerator.setModuleManager(moduleManager);

        accessControl = EasyMock.createMock(AccessControl.class);
        debugPageGenerator.setAccessControl(accessControl);

        converterManager = EasyMock.createMock(ConverterManager.class);
        debugPageGenerator.setConverterManager(converterManager);
    }

    @Test
    public void handleInNonDebug() throws Exception
    {
        debugPageGenerator.setDebug(false);

        EasyMock.replay(moduleManager);
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

        EasyMock.verify(moduleManager);
        EasyMock.verify(accessControl);
        EasyMock.verify(converterManager);

        assertNull(response);
        // assertTrue(new String(response.getBody()).indexOf("Test Pages") != -1);
    }

    @Ignore
    @Test
    public void handle() throws Exception
    {
        // TODO: fix test
        /*
        debugPageGenerator.setDebug(false);

        moduleManager.getCreator("creatorName", false);
        NewCreator creator = new NewCreator();
        creator.setClass(TestCreatedObject.class.getName());
        EasyMock.expectLastCall().andReturn(creator);

        accessControl.assertIsDisplayable(EasyMock.eq(creator), EasyMock.eq("creatorName"), EasyMock.isA(Method.class));
        EasyMock.expectLastCall().andReturn(null).times(11);

        converterManager.isConvertable((Class<?>) EasyMock.anyObject());
        EasyMock.expectLastCall().andReturn(Boolean.TRUE).times(19);

        accessControl.assertExecutionIsPossible(EasyMock.eq(creator), EasyMock.eq("creatorName"), EasyMock.isA(Method.class));
        EasyMock.expectLastCall().andReturn(null).times(10);

        EasyMock.replay(moduleManager);
        EasyMock.replay(accessControl);
        EasyMock.replay(converterManager);

        String result = debugPageGenerator.generateTestPage("", "creatorName");

        EasyMock.verify(moduleManager);
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
        */
    }

    @Test
    public void handleWithoutDebug() throws Exception
    {
        debugPageGenerator.setDebug(false);

        EasyMock.replay(moduleManager);

        try
        {
            debugPageGenerator.generateIndexPage("root");
            fail("Missing SecurityException");
        }
        catch (SecurityException ex)
        {
        }

        EasyMock.verify(moduleManager);
    }

    @Test
    public void generateIndexPage() throws Exception
    {
        debugPageGenerator.setDebug(true);

        ArrayList<String> names = new ArrayList<String>();
        names.add("creatorName");
        EasyMock.expect(moduleManager.getModuleNames(false)).andReturn(names);

        NewCreator creator = new NewCreator();
        creator.setClass(TestCreatedObject.class.getName());
        Module module = new CreatorModule(creator, null, null, false, null, true);
        EasyMock.expect(moduleManager.getModule("creatorName", false)).andReturn(module);

        EasyMock.replay(moduleManager);

        String result = debugPageGenerator.generateIndexPage("root");

        EasyMock.verify(moduleManager);

        assertNotNull(result);
        assertTrue(result.indexOf("creatorName") != -1);
        assertTrue(result.indexOf(TestCreatedObject.class.getName()) != -1);
    }
}
