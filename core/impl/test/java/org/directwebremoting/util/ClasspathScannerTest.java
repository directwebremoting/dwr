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
        assertTrue(classes.size() <= 25);
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
