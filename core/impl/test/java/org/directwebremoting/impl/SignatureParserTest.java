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

import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.CreatorManager;
import org.directwebremoting.extend.Property;
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
    public void testParse1()
    {
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
