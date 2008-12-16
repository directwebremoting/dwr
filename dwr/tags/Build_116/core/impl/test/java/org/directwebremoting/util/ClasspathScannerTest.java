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
package org.directwebremoting.util;

import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClasspathScannerTest
{
    @Test
    public void getClasses()
    {
        ClasspathScanner scanner = new ClasspathScanner("org.directwebremoting.util.*");
        Set<String> classes = scanner.getClasses();
        assertTrue(classes.size() > 10);
        assertTrue(classes.contains(this.getClass().getName()));
        assertTrue(classes.contains(ClasspathScanner.class.getName()));

        scanner = new ClasspathScanner("org.directwebremoting");
        classes = scanner.getClasses();
        assertTrue(classes.size() > 5);
        assertTrue(classes.size() < 20);
        assertTrue(classes.contains(org.directwebremoting.ScriptSession.class.getName()));
        assertFalse(classes.contains(this.getClass().getName()));
        assertFalse(classes.contains(ClasspathScanner.class.getName()));

        scanner = new ClasspathScanner("org.directwebremoting", true);
        classes = scanner.getClasses();
        assertTrue(classes.size() > 100);
        assertTrue(classes.contains(org.directwebremoting.ScriptSession.class.getName()));
        assertTrue(classes.contains(this.getClass().getName()));
        assertTrue(classes.contains(ClasspathScanner.class.getName()));

        scanner = new ClasspathScanner("x.y.z", true);
        assertTrue(scanner.getClasses().size() == 0);

        /*
        scanner = new ClasspathScanner();
        classes = scanner.getClasses();
        assertTrue("Expecting at least 500 classes in global scan, found " + classes.size(), classes.size() > 500);
        assertTrue(classes.contains(org.directwebremoting.ScriptSession.class.getName()));
        assertTrue(classes.contains(this.getClass().getName()));
        assertTrue(classes.contains(ClasspathScanner.class.getName()));
        */
    }
}
