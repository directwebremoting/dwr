package org.directwebremoting.impl;

import java.lang.reflect.Method;

import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.MethodDeclaration;
import org.directwebremoting.extend.ParameterProperty;
import org.directwebremoting.extend.Property;
import org.directwebremoting.impl.test.SignatureTestsObject;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Bram Smeets
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SignatureParserTest
{
    private SignatureParser parser;

    private ConverterManager converterManager;

    private CreatorManager creatorManager;

    @Before
    public void setUp() throws Exception
    {
        converterManager = EasyMock.createMock(ConverterManager.class);
        creatorManager = EasyMock.createMock(CreatorManager.class);
        parser = new SignatureParser(converterManager, creatorManager);
    }

    @Test
    public void testParseEmptyString() throws Exception
    {
        EasyMock.replay(converterManager);
        parser.parse("");
        EasyMock.verify(converterManager);
    }

    @Test
    public void testParse1() throws NoSuchMethodException
    {
        Method expectedMethod = SignatureTestsObject.class.getMethod("setLotteryResults", java.util.List.class);
        Property expectedProperty = new ParameterProperty(new MethodDeclaration(expectedMethod), 0);
        EasyMock.expect(converterManager.checkOverride(EasyMock.isA(Property.class))).andReturn(expectedProperty);
        converterManager.setOverrideProperty(EasyMock.isA(Property.class), EasyMock.isA(Property.class));
        EasyMock.replay(converterManager);
        parser.parse("import java.util.*;\n" + "  import org.directwebremoting.impl.test.SignatureTestsObject;\n"
            + "  public void SignatureTestsObject.setLotteryResults(List<Integer> nos);");
        EasyMock.verify(converterManager);
    }

    @Test
    public void parse2()
    {
        EasyMock.replay(converterManager);
        parser.parse("import java.util.List;\n" + "   SignatureTestsObject.setLotteryResults(List<Integer>);");
        EasyMock.verify(converterManager);
    }
}
