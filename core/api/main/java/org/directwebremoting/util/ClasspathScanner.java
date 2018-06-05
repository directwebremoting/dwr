package org.directwebremoting.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ClasspathScanner enables you to find the set of classes that match some
 * package name. The search can be recursive.
 * @author Jose Noheda [jose.noheda at gmail dot com]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ClasspathScanner
{

    /**
     * Attempt to find all classes in the VM
     */
    public ClasspathScanner()
    {
        this(null, true);
    }

    /**
     * Non recursively find classes within the given package
     * @param packageName package name specified with dot separators
     */
    public ClasspathScanner(String packageName)
    {
        this(packageName, false);
    }

    /**
     * Find classes within the given package (optionally recursively)
     * @param packageName package name specified with dot separator
     * @param recursive True to dig into sub-packages
     */
    public ClasspathScanner(String packageName, boolean recursive)
    {
        this.recursive = recursive;

        if (packageName == null)
        {
            packageName = "";
        }

        packageName = packageName.replace('.', '/');

        if (packageName.endsWith("*"))
        {
            packageName = packageName.substring(0, packageName.length() - 1);
        }

        if (packageName.endsWith("/"))
        {
            packageName = packageName.substring(0, packageName.length() - 1);
        }

        this.packageName = packageName;
    }

    /**
     * Get the list of classes available to the classloader
     */
    public Set<String> getClasses()
    {
        Set<String> classes = new HashSet<String>();

        try
        {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String match = packageName;
            if (match == "/")
            {
                match = "";
            }

            Enumeration<URL> resources = classLoader.getResources(match + "/");
            while (resources.hasMoreElements())
            {
                URL resource = resources.nextElement();
                String path = sanitizePath(resource.getFile());
                if ((path == null) || (path.trim().length() <= 0))
                {
                    continue;
                }
                if ("vfszip".equals(resource.getProtocol())) {
                    classes.addAll(getClassesFromVFS(path));
                } else if (isJARPath(path))
                {
                    classes.addAll(getClassesFromJAR(path));
                }
                else
                {
                    classes.addAll(getClassesFromDirectory(path));
                }
            }
        }
        catch (IOException ex)
        {
            log.warn("Failed to find any resources from classloader");
        }

        return classes;
    }

    /**
     * Is this path pointing at a JAR file?
     */
    protected boolean isJARPath(String path)
    {
        return (path.indexOf("!") > 0) & (path.indexOf(".jar") > 0);
    }

    /**
     * Extract the classes from a JAR file
     */
    protected Set<String> getClassesFromJAR(String path) throws IOException
    {
        Set<String> classes = new HashSet<String>();
        String jarPath = path.substring(0, path.indexOf("!")).substring(path.indexOf(":") + 1);
        JarInputStream jarFile = new JarInputStream(new FileInputStream(jarPath));
        try
        {
            while (true)
            {
                JarEntry jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null)
                {
                    break;
                }
                addIfMatches(classes, jarEntry.getName());
            }
        }
        finally
        {
            if (null != jarFile)
            {
                jarFile.close();
            }
        }
        return classes;
    }

    /**
     * Extract the classes from a set of classes in the file system
     */
    protected Set<String> getClassesFromDirectory(String path)
    {
        Set<String> classes = new HashSet<String>();
        File directory = new File(path);
        if (directory.exists())
        {
            for (String file : directory.list())
            {
                File f = new File(directory, file);
                if (f.isFile())
                {
                    addIfMatches(classes, path.substring(path.indexOf(packageName)) + file);
                }
                else if (recursive)
                {
                    classes.addAll(getClassesFromDirectory(path + file + "/"));
                }
            }
        }
        return classes;
    }

    /**
     * Extract the classes from a set of classes within a JBoss 5 vfszip protocol URL
     */
    protected Set<String> getClassesFromVFS(String path) throws IOException
    {
        Set<String> classes = new HashSet<String>();
        Pattern vfsPattern = Pattern.compile("(.*\\.[wej]ar)(/.*)");
        Matcher vfsMatcher = vfsPattern.matcher(path);
        if (vfsMatcher.matches()) {
            JarInputStream jarFile = new JarInputStream(new FileInputStream(vfsMatcher.group(1)));
            try
            {
                Pattern vfsEntryPattern;
                if (recursive) {
                    vfsEntryPattern = Pattern.compile(".*(" + packageName + ".*/)([^/]+)\\.class");
                } else {
                    vfsEntryPattern = Pattern.compile(".*(" + packageName + "/)([^/]+)\\.class");
                }
                while (true)
                {
                    JarEntry jarEntry = jarFile.getNextJarEntry();
                    if (jarEntry == null)
                    {
                        break;
                    }
                    Matcher vfsEntryMatcher = vfsEntryPattern.matcher(jarEntry.getName());
                    if (vfsEntryMatcher.matches()) {
                        classes.add(vfsEntryMatcher.group(1).replace("/", ".") + vfsEntryMatcher.group(2));
                    }
                }
            }
            finally
            {
                if (null != jarFile)
                {
                    jarFile.close();
                }
            }
        }
        return classes;
    }

    /**
     * Check to see that the given file is a class in the right package and add
     * it to the given collection
     */
    protected void addIfMatches(Set<String> classes, String className)
    {
        if ((className.startsWith(packageName)) && (className.endsWith(".class")))
        {
            boolean add = recursive ? true : className.substring(packageName.length() + 1).indexOf("/") < 0;
            if (add)
            {
                classes.add(className.substring(0, className.length() - 6).replace('/', '.'));
            }
        }
    }

    /**
     * Paths need cleaning up, especially in windows
     */
    protected String sanitizePath(String path)
    {
        String tmp = path;
        if (tmp.indexOf("%20") > 0)
        {
            // TODO: maybe we should do full URL decoding here?
            tmp = tmp.replaceAll("%20", " ");
        }

        if ((tmp.indexOf(":") >= 0) && (tmp.startsWith("/")))
        {
            // Remove leading / in URLs like /c:/...
            tmp = tmp.substring(1);
        }

        return tmp;
    }

    /**
     * The package name we are finding classes in
     */
    private final String packageName;

    /**
     * Are we digging recursively?
     */
    private final boolean recursive;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(ClasspathScanner.class);
}
