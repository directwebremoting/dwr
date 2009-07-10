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
        private int prim;

        public Bean(String aux)
        {
            this.aux = aux;
        }

        public String getAux()
        {
            return aux;
        }

        public void setAux(String aux)
        {
            this.aux = aux;
        }

        public int getPrim()
        {
            return prim;
        }

        public void setPrim(int prim)
        {
            this.prim = prim;
        }

        @Override
        public String toString()
        {
            return aux;
        }
    }

    private final Bean zero = new Bean("0");

    private final Bean one = new Bean("1");

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
    public void testIsWrapper()
    {
        assertFalse(LocalUtil.isWrapper(null));
        assertFalse(LocalUtil.isWrapper("me"));
        assertTrue(LocalUtil.isWrapper(new Float(2.0)));
    }

    @Test
    public void testSetProperty() throws Exception
    {
        Bean bean = new Bean("whatever");
        LocalUtil.setProperty(bean, "aux", "new");
        assertEquals("new", bean.getAux());
        LocalUtil.setProperty(bean, "prim", 2);
        assertTrue(bean.getPrim() == 2);
    }

}
