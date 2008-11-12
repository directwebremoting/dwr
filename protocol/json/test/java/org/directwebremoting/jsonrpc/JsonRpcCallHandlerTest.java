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
package org.directwebremoting.jsonrpc;

import java.io.StringReader;
import java.util.List;

import org.directwebremoting.impl.TestEnvironment;
import org.directwebremoting.json.parse.JsonParser;
import org.directwebremoting.json.parse.JsonParserFactory;
import org.directwebremoting.jsonrpc.io.JsonRpcCalls;
import org.directwebremoting.jsonrpc.io.JsonRpcCallsJsonDecoder;
import org.directwebremoting.util.TestUtil;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonRpcCallHandlerTest
{
    @BeforeClass
    public static void setup()
    {
        TestEnvironment.engageThread();
    }

    /**
     * Test method for {@link org.directwebremoting.jsonrpc.JsonRpcCallHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
     */
    @Test
    public void testHandle() throws Exception
    {
        // JsonRpcCallHandler handler = TestEnvironment.getContainer().getBean(JsonRpcCallHandler.class);

        List<String> validTests = TestUtil.parseTestInput(getClass(), "validJsonRpc.txt");
        for (String test : validTests)
        {
            StringReader in = new StringReader(test);
            JsonParser parser = JsonParserFactory.get();
            JsonRpcCalls calls = (JsonRpcCalls) parser.parse(in, new JsonRpcCallsJsonDecoder());
        }
    }
}
