/*
 * Copyright 2008 author or authors
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
package org.directwebremoting.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for utility class.
 *
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class LocalUtilTest
{

    class Bean
    {
        private String aux;

        public Bean(String aux)
        {
            this.aux = aux;
        }

        @Override
        public String toString()
        {
            return aux;
        }
    }

    private Bean zero = new Bean("0");

    private Bean one = new Bean("1");

    @Test
    public void testJoin()
    {
        assertNull(LocalUtil.join(null, null));
        assertEquals(LocalUtil.join(new String[] { "hello", "world" }, null), "helloworld");
        assertEquals(LocalUtil.join(new String[] { "hello", "world" }, ""), "helloworld");
        assertEquals(LocalUtil.join(new String[] { "hello", "world" }, "."), "hello.world");
        assertEquals(LocalUtil.join(new String[] { "hello", "world" }, ".."), "hello..world");
        assertEquals(LocalUtil.join(new Object[] { zero, one }, "-"), "0-1");
    }

    @Test
    public void testHasText()
    {
        assertFalse(LocalUtil.hasText(""));
        assertFalse(LocalUtil.hasText(null));
        assertFalse(LocalUtil.hasText("   "));
        assertFalse(LocalUtil.hasText(new String()));
        assertTrue(LocalUtil.hasText("hello"));
    }

    @Test
    public void testIsJavaIdentifier()
    {
    }

    @Test
    public void testIsLetterOrDigitOrUnderline()
    {
    }

    @Test
    public void testIsEquivalent()
    {
    }

    @Test
    public void testEquals()
    {
    }

    @Test
    public void testShrink()
    {
    }

    @Test
    public void testGetNonPrimitiveType()
    {
    }

    @Test
    public void testAddNoCacheHeaders()
    {
    }

    @Test
    public void testIsServletClass()
    {
    }

    @Test
    public void testDebugRequest()
    {
    }

    @Test
    public void testIterableizer()
    {
    }

    @Test
    public void testDecode()
    {
    }

    @Test
    public void testSetParams()
    {
    }

    @Test
    public void testSetProperty() throws Exception
    {
    }

    @Test
    public void testGetPropertyType()
    {
    }

    @Test
    public void testIsTypeSimplyConvertable()
    {
    }

    @Test
    public void testSimpleConvert()
    {
    }

    @Test
    public void testIsSimpleName()
    {
    }

    @Test
    public void testClassForName_String() throws Exception
    {
    }

    @Test
    public void testInvoke()
    {
    }

    @Test
    public void testClassForName_3args()
    {
    }

    @Test
    public void testClassNewInstance()
    {
    }

    @Test
    public void testClose()
    {
    }

    @Test
    public void testGetAllSuperclasses()
    {
    }

    @Test
    public void testGetAllFields()
    {
    }

    @Test
    public void testGetSystemClassloadTime()
    {
    }

    @Test
    public void testGetProperty() throws Exception
    {
    }
}
