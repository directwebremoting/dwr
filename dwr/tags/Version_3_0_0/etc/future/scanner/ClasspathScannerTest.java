package dwr;

import java.io.IOException;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClasspathScannerTest {
    private ClasspathScanner scanner;

    @Test
    public void getPackage() {
        scanner = new ClasspathScanner("org.hibernate.*");
        assertEquals("Package was sanitized", "org/hibernate", scanner.getPackage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getClasses() throws IOException {
        scanner = new ClasspathScanner("org.hibernate.annotations.common.*");
        Set<String> classes = scanner.getClasses();
        assertTrue(classes.size() == 2);
        assertTrue(classes.contains("org.hibernate.annotations.common.AssertionFailure"));
        assertTrue(classes.contains("org.hibernate.annotations.common.TestDirectoryScan"));
        scanner = new ClasspathScanner("org.hibernate.annotations.common.*", true);
        classes = scanner.getClasses();
        assertTrue(classes.size() == 61);
        assertTrue(classes.contains("org.hibernate.annotations.common.other.another.AnotherTestDirectoryScan"));
        scanner = new ClasspathScanner("x.y.z", true);
        assertTrue(scanner.getClasses().size() == 0);
        scanner = new ClasspathScanner("");
    }

}
