package org.directwebremoting.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.impl.TestEnvironment;
import org.directwebremoting.json.parse.JsonParseException;
import org.directwebremoting.util.TestUtil;
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
        List<String> validTests = TestUtil.parseTestInput(getClass(), "validJson.txt");
        for (String test : validTests)
        {
            String errors = JsonUtil.getErrors(test);
            assertNull("Expecting no error for: " + test + " got " + errors, errors);
        }

        List<String> invalidTests = TestUtil.parseTestInput(getClass(), "invalidJson.txt");
        for (String test : invalidTests)
        {
            String errors = JsonUtil.getErrors(test);
            assertNotNull("Expecting error for: " + test, errors);
        }
    }

    /**
     * Test method for {@link org.directwebremoting.json.JsonUtil#toSimpleObject}.
     */
    @Test
    public void testToSimpleObject() throws JsonParseException
    {
        HashMap<String, Object> expected = new HashMap<String, Object>();

        Map<String, Object> reply = JsonUtil.toSimpleObject("{  }");
        assertEquals(expected, reply);

        expected.put("b", Boolean.TRUE);
        reply = JsonUtil.toSimpleObject("{ \"b\":true }");
        assertEquals(expected, reply);
        reply = JsonUtil.toSimpleObject("{\"b\":true}");
        assertEquals(expected, reply);
        reply = JsonUtil.toSimpleObject("{\n\"b\":\n\ntrue\n}\n");
        assertEquals(expected, reply);

        expected.put("b", Boolean.FALSE);
        reply = JsonUtil.toSimpleObject("{ \"b\":false }");
        assertEquals(expected, reply);
        reply = JsonUtil.toSimpleObject("{\"b\":false}");
        assertEquals(expected, reply);
        reply = JsonUtil.toSimpleObject("{\n\"b\":\n\nfalse\n}\n");
        assertEquals(expected, reply);

        expected.put("b", "b");
        reply = JsonUtil.toSimpleObject("{ \"b\":\"b\" }");
        assertEquals(expected, reply);

        expected.put("b", "{}");
        reply = JsonUtil.toSimpleObject("{ \"b\":\"{}\" }");
        assertEquals(expected, reply);

        expected.put("b", ":");
        reply = JsonUtil.toSimpleObject("{ \"b\":\":\" }");
        assertEquals(expected, reply);

        expected.put("b", "[]");
        reply = JsonUtil.toSimpleObject("{ \"b\":\"[]\" }");
        assertEquals(expected, reply);

        expected.put("b", ",[]");
        reply = JsonUtil.toSimpleObject("{ \"b\":\",[]\" }");
        assertEquals(expected, reply);

        expected.put("b", 1);
        reply = JsonUtil.toSimpleObject("{ \"b\":1 }");
        assertEquals(expected, reply);

        expected.put("b", 2);
        reply = JsonUtil.toSimpleObject("{ \"b\":2 }");
        assertEquals(expected, reply);

        expected.put("b", 0);
        reply = JsonUtil.toSimpleObject("{ \"b\":0 }");
        assertEquals(expected, reply);

        expected.put("c", true);
        reply = JsonUtil.toSimpleObject("{ \"b\":0, \"c\":true }");
        assertEquals(expected, reply);

        expected.put("c", null);
        reply = JsonUtil.toSimpleObject("{ \"b\":0, \"c\":null }");
        assertEquals(expected, reply);

        expected.put("b", null);
        reply = JsonUtil.toSimpleObject("{ \"b\":null, \"c\":null }");
        assertEquals(expected, reply);

        ArrayList<Object> child1 = new ArrayList<Object>();
        expected.put("c", child1);
        reply = JsonUtil.toSimpleObject("{ \"b\":null, \"c\":[] }");
        assertEquals(expected, reply);

        child1.add(true);
        expected.put("c", child1);
        reply = JsonUtil.toSimpleObject("{ \"b\":null, \"c\":[true] }");
        assertEquals(expected, reply);

        child1.add(false);
        child1.add(null);
        reply = JsonUtil.toSimpleObject("{ \"b\":null, \"c\":[true,false, null] }");
        assertEquals(expected, reply);

        HashMap<String, Object> child2 = new HashMap<String, Object>();
        child1.add(child2);
        reply = JsonUtil.toSimpleObject("{ \"b\":null, \"c\":[true,false, null, {}] }");
        assertEquals(expected, reply);

        child2.put("d", 1);
        reply = JsonUtil.toSimpleObject("{ \"b\":null, \"c\":[true,false, null, { \"d\":1}] }");
        assertEquals(expected, reply);

        child2.put("d", null);
        reply = JsonUtil.toSimpleObject("{ \"b\":null, \"c\":[true,false, null, { \"d\":null}] }");
        assertEquals(expected, reply);

        child2.put("e", new ArrayList<Object>());
        reply = JsonUtil.toSimpleObject("{ \"b\":null, \"c\":[true,false, null, { \"d\":null, \"e\":[]}] }");
        assertEquals(expected, reply);

        expected.remove("b");
        reply = JsonUtil.toSimpleObject("{ \"c\":[true,false, null, { \"d\":null, \"e\":[]}] }");
        assertEquals(expected, reply);

        child1.remove(0);
        reply = JsonUtil.toSimpleObject("{ \"c\":[false, null, { \"d\":null, \"e\":[]}] }");
        assertEquals(expected, reply);

        child1.remove(0);
        child1.remove(0);
        reply = JsonUtil.toSimpleObject("{ \"c\":[{ \"d\":null, \"e\":[]}] }");
        assertEquals(expected, reply);

        child2.remove("d");
        reply = JsonUtil.toSimpleObject("{ \"c\":[{\"e\":[]}] }");
        assertEquals(expected, reply);
    }

    /**
     * Test method for {@link org.directwebremoting.json.JsonUtil#toSimpleObject}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testToReflectedTypes() throws JsonParseException
    {
        List list = JsonUtil.toReflectedTypes(ArrayList.class, "[ \"1\", \"2\", \"3\" ]");
        assertEquals(Arrays.asList(new String[] { "1", "2", "3" }), list);
    }
}
