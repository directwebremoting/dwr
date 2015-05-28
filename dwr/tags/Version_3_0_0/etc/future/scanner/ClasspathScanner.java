package org.directwebremoting.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ClasspathScanner {

    private String pkg;
    private boolean recur = false;

    protected String getPackage() {
        return pkg;
    }

    public ClasspathScanner(String pkg, boolean subpackages) {
        recur = subpackages;
        sanitizePackage(pkg);
    }

    public ClasspathScanner(String pkg) {
        sanitizePackage(pkg);
    }

    private void sanitizePackage(String pkgName) {
        if ((pkgName == null) || (pkgName.trim().length() == 0)) throw new IllegalArgumentException("Base package cannot be null");
        pkg = pkgName.replace('.', '/');
        if (pkg.endsWith("*")) pkg = pkg.substring(0, pkg.length() - 1);
        if (pkg.endsWith("/")) pkg = pkg.substring(0, pkg.length() - 1);
    }

    protected ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    protected boolean isJARPath(String path) {
        return (path.indexOf("!") > 0) & (path.indexOf(".jar") > 0);
    }

    protected void add(Set<String> classes, String className) {
        if ((className.startsWith(pkg)) && (className.endsWith(".class"))) {
            boolean add = recur ? true : className.substring(pkg.length() + 1).indexOf("/") < 0;
            if (add) classes.add(className.substring(0, className.length() - 6).replace('/', '.'));
        }
    }

    protected Set<String> getClassesFromJAR(String path) throws IOException {
        Set<String> classes = new HashSet<String>();
        String jarPath = path.substring(0, path.indexOf("!")).substring(path.indexOf(":") + 1);
        JarInputStream jarFile = new JarInputStream(new FileInputStream(jarPath));
        JarEntry jarEntry;
        do {
            jarEntry = jarFile.getNextJarEntry();
            if (jarEntry != null) add(classes, jarEntry.getName());
        } while (jarEntry != null);
        return classes;
    }

    protected Set<String> getClassesFromDirectory(String path) {
        Set<String> classes = new HashSet<String>();
        File directory = new File(path);
        if (directory.exists()) {
            for (String file : directory.list()) {
                File f = new File(directory, file);
                if (f.isFile()) add(classes, path.substring(path.indexOf(pkg)) + file);
                else if (recur) classes.addAll(getClassesFromDirectory(path + file + "/"));
            }
        }
        return classes;
    }

    protected String sanitizeURLForWindows(String path) {
        String tmp = path;
        if(tmp.indexOf("%20") > 0) tmp = tmp.replaceAll("%20", " "); // Encodes 
        if ((tmp.indexOf(":") >= 0) && (tmp.startsWith("/"))) tmp = tmp.substring(1); // Removes leading / in URLs like /c:/...
        return tmp;
    }

    protected String sanitizeURL(String path) {
        return sanitizeURLForWindows(path);
    }

    public Set<String> getClasses() throws IOException {
        Set<String> classes = new HashSet<String>();
        Enumeration<URL> resources = getClassLoader().getResources(pkg + "/");
        if (resources != null) {
            while (resources.hasMoreElements()) {
                String path = sanitizeURL(resources.nextElement().getFile());
                if ((path != null) && (path.trim().length() > 0))
                    classes.addAll(
                        isJARPath(path) ? getClassesFromJAR(path) : getClassesFromDirectory(path)
                    );
            }
        }
        return classes;
    }

}
