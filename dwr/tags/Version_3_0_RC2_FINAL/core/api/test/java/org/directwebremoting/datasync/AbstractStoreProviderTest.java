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
package org.directwebremoting.datasync;

import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractStoreProviderTest
{

    @Test
    public void testTestPattern()
    {
        assertFalse("Null entry", AbstractStoreProvider.testPattern("joe", null, false));
        assertTrue("Equal values", AbstractStoreProvider.testPattern("joe", "joe", false));
        assertTrue("Infer character", AbstractStoreProvider.testPattern("jo?", "joe", false));
        assertTrue("Infer character with trailing exp", AbstractStoreProvider.testPattern("jo?y", "joey", false));
        assertTrue("Infer two characters", AbstractStoreProvider.testPattern("jo??", "joey", false));
        assertTrue("Infer two characters with trailing exp", AbstractStoreProvider.testPattern("jo??!", "joey!", false));
        assertFalse("Infer character but wrong expression", AbstractStoreProvider.testPattern("jo?", "jae", false));
        assertFalse("Infer character but wrong expression and trailing exp", AbstractStoreProvider.testPattern("jo?y", "joe", false));
        assertFalse("Infer two characters but wrong expression and trailing exp", AbstractStoreProvider.testPattern("jo??y", "joeeu", false));
        assertTrue("Infer any", AbstractStoreProvider.testPattern("jo*", "joey", false));
        assertTrue("Infer chars and any", AbstractStoreProvider.testPattern("ye? jo?y*", "yes joey!", false));
        assertTrue("Equal ignore case", AbstractStoreProvider.testPattern("joe", "JOE", true));
        assertTrue("Equal alternate ignore case", AbstractStoreProvider.testPattern("joe", "JoE", true));
        assertTrue("Equal unicode ignore case", AbstractStoreProvider.testPattern("joé", "JOÉ", true));
    }

}

