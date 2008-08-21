package dwr;

import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ScannerListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent arg0) {
        try {
            ClasspathScanner scanner = new ClasspathScanner("org.hibernate.annotations.common.reflection.*");
            System.out.println("Found the following classes");
            Set<String> classes = scanner.getClasses();
            System.out.println(classes);
            System.out.println("Scan correct: " + (classes.size() == 12));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        return;
    }

}
