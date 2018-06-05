package org.directwebremoting.extend;

import java.util.Collection;

/**
 * The heart of DWR is a system to generate content from some requests.
 * This interface generates scripts and executes remote calls.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface DebugPageGenerator
{
    /**
     * Generate some HTML that represents an index page
     * @param root The prefix common to all DWR URLs. Usually contextPath+servletPath
     * @return An index page in HTML
     * @throws SecurityException If the pages are not accessible
     */
    String generateIndexPage(String root) throws SecurityException;

    /**
     * Generate some HTML that represents a test page for a given script
     * @param root The prefix common to all DWR URLs. Usually contextPath+servletPath
     * @param scriptName The script to generate for
     * @return A test page in HTML
     * @throws SecurityException If the pages are not accessible
     */
    String generateTestPage(String root, String scriptName) throws SecurityException;

    /**
     * For a given remoted class, generate a URL that will retrieve the
     * Javascript interface
     * @param root The prefix common to all DWR URLs. Usually contextPath+servletPath
     * @param scriptName The script to generate for
     * @return A URL that points at the given scriptName
     * @deprecated Please tell the DWR users mailing list users@dwr.dev.java.net if you use this
     */
    @Deprecated
    String generateInterfaceUrl(String root, String scriptName);

    /**
     * Create a url that links to the engine.js file
     * @param root The prefix common to all DWR URLs. Usually contextPath+servletPath
     * @return A URL that points at the central engine Javascript file
     * @deprecated Please tell the DWR users mailing list users@dwr.dev.java.net if you use this
     */
    @Deprecated
    String generateEngineUrl(String root);

    /**
     * Create a url that links to one of the library files
     * @param root The prefix common to all DWR URLs. Usually contextPath+servletPath
     * @param library The name of a library as returned by {@link DebugPageGenerator#getAvailableLibraries()}
     * @return A URL that points at the given library
     * @deprecated Please tell the DWR users mailing list users@dwr.dev.java.net if you use this
     */
    @Deprecated
    String generateLibraryUrl(String root, String library);

    /**
     * @return A list of the available libraries.
     * @deprecated Please tell the DWR users mailing list users@dwr.dev.java.net if you use this
     */
    @Deprecated
    Collection<String> getAvailableLibraries();
}
