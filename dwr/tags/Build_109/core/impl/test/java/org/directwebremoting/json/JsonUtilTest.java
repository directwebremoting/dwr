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
package org.directwebremoting.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.impl.TestEnvironment;
import org.directwebremoting.json.parse.JsonParseException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonUtilTest
{
    @BeforeClass
    public static void setup()
    {
        TestEnvironment.engageThread();
    }

    /**
     * Test method for {@link org.directwebremoting.json.JsonUtil#getErrors}.
     */
    @Test
    public void testGetErrors() throws IOException
    {
        List<String> validTests = parseTestInput("validJson.txt");
        for (String test : validTests)
        {
            String errors = JsonUtil.getErrors(test);
            assertNull("Expecting no error for: " + test + " got " + errors, errors);
        }

        List<String> invalidTests = parseTestInput("invalidJson.txt");
        for (String test : invalidTests)
        {
            String errors = JsonUtil.getErrors(test);
            assertNotNull("Expecting error for: " + test, errors);
        }
    }

    /**
     * Test method for {@link org.directwebremoting.json.JsonUtil#toSimpleTypes}.
     */
    @Test
    public void testToSimpleTypes() throws JsonParseException
    {
        HashMap<String, Object> expected = new HashMap<String, Object>();

        Map<String, Object> reply = JsonUtil.toSimpleTypes("{  }");
        assertEquals(expected, reply);

        expected.put("b", Boolean.TRUE);
        reply = JsonUtil.toSimpleTypes("{ \"b\":true }");
        assertEquals(expected, reply);
        reply = JsonUtil.toSimpleTypes("{\"b\":true}");
        assertEquals(expected, reply);
        reply = JsonUtil.toSimpleTypes("{\n\"b\":\n\ntrue\n}\n");
        assertEquals(expected, reply);

        expected.put("b", Boolean.FALSE);
        reply = JsonUtil.toSimpleTypes("{ \"b\":false }");
        assertEquals(expected, reply);
        reply = JsonUtil.toSimpleTypes("{\"b\":false}");
        assertEquals(expected, reply);
        reply = JsonUtil.toSimpleTypes("{\n\"b\":\n\nfalse\n}\n");
        assertEquals(expected, reply);

        expected.put("b", "b");
        reply = JsonUtil.toSimpleTypes("{ \"b\":\"b\" }");
        assertEquals(expected, reply);

        expected.put("b", "{}");
        reply = JsonUtil.toSimpleTypes("{ \"b\":\"{}\" }");
        assertEquals(expected, reply);

        expected.put("b", ":");
        reply = JsonUtil.toSimpleTypes("{ \"b\":\":\" }");
        assertEquals(expected, reply);

        expected.put("b", "[]");
        reply = JsonUtil.toSimpleTypes("{ \"b\":\"[]\" }");
        assertEquals(expected, reply);

        expected.put("b", ",[]");
        reply = JsonUtil.toSimpleTypes("{ \"b\":\",[]\" }");
        assertEquals(expected, reply);

        expected.put("b", 1);
        reply = JsonUtil.toSimpleTypes("{ \"b\":1 }");
        assertEquals(expected, reply);

        expected.put("b", 2);
        reply = JsonUtil.toSimpleTypes("{ \"b\":2 }");
        assertEquals(expected, reply);

        expected.put("b", 0);
        reply = JsonUtil.toSimpleTypes("{ \"b\":0 }");
        assertEquals(expected, reply);

        expected.put("c", true);
        reply = JsonUtil.toSimpleTypes("{ \"b\":0, \"c\":true }");
        assertEquals(expected, reply);

        expected.put("c", null);
        reply = JsonUtil.toSimpleTypes("{ \"b\":0, \"c\":null }");
        assertEquals(expected, reply);

        expected.put("b", null);
        reply = JsonUtil.toSimpleTypes("{ \"b\":null, \"c\":null }");
        assertEquals(expected, reply);

        ArrayList<Object> child1 = new ArrayList<Object>();
        expected.put("c", child1);
        reply = JsonUtil.toSimpleTypes("{ \"b\":null, \"c\":[] }");
        assertEquals(expected, reply);

        child1.add(true);
        expected.put("c", child1);
        reply = JsonUtil.toSimpleTypes("{ \"b\":null, \"c\":[true] }");
        assertEquals(expected, reply);

        child1.add(false);
        child1.add(null);
        reply = JsonUtil.toSimpleTypes("{ \"b\":null, \"c\":[true,false, null] }");
        assertEquals(expected, reply);

        HashMap<String, Object> child2 = new HashMap<String, Object>();
        child1.add(child2);
        reply = JsonUtil.toSimpleTypes("{ \"b\":null, \"c\":[true,false, null, {}] }");
        assertEquals(expected, reply);

        child2.put("d", 1);
        reply = JsonUtil.toSimpleTypes("{ \"b\":null, \"c\":[true,false, null, { \"d\":1}] }");
        assertEquals(expected, reply);

        child2.put("d", null);
        reply = JsonUtil.toSimpleTypes("{ \"b\":null, \"c\":[true,false, null, { \"d\":null}] }");
        assertEquals(expected, reply);

        child2.put("e", new ArrayList<Object>());
        reply = JsonUtil.toSimpleTypes("{ \"b\":null, \"c\":[true,false, null, { \"d\":null, \"e\":[]}] }");
        assertEquals(expected, reply);

        expected.remove("b");
        reply = JsonUtil.toSimpleTypes("{ \"c\":[true,false, null, { \"d\":null, \"e\":[]}] }");
        assertEquals(expected, reply);

        child1.remove(0);
        reply = JsonUtil.toSimpleTypes("{ \"c\":[false, null, { \"d\":null, \"e\":[]}] }");
        assertEquals(expected, reply);

        child1.remove(0);
        child1.remove(0);
        reply = JsonUtil.toSimpleTypes("{ \"c\":[{ \"d\":null, \"e\":[]}] }");
        assertEquals(expected, reply);

        child2.remove("d");
        reply = JsonUtil.toSimpleTypes("{ \"c\":[{\"e\":[]}] }");
        assertEquals(expected, reply);
    }

    /**
     * Parse the list of tests to detect json test that we need to run
     * @param filename The name of a file in the same package
     * @return A list of json test, read from the file
     * @throws IOException
     */
    private List<String> parseTestInput(String filename) throws IOException
    {
        InputStream raw = getClass().getResourceAsStream(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(raw));
        List<String> tests = new ArrayList<String>();
        StringBuilder test = new StringBuilder();

        while (true)
        {
            String line = in.readLine();

            if (line == null)
            {
                break;
            }

            String trim = line.trim();

            if (trim.startsWith("#"))
            {
                continue;
            }

            if (trim.length() != 0)
            {
                test.append(line);
            }
            else
            {
                if (test.toString().trim().length() != 0)
                {
                    tests.add(test.toString());
                    test = new StringBuilder();
                }
            }
        }

        return tests;
    }
}
