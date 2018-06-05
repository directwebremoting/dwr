package org.directwebremoting.extend;

/**
 * Some constants to do with the heart of DWR.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @noinspection InterfaceNeverImplemented
 */
public interface DwrConstants
{
    /**
     * The package path because people need to load resources in this package.
     */
    public static final String PACKAGE_PATH = "/org/directwebremoting";

    /**
     * The package name.
     */
    public static final String PACKAGE_NAME = PACKAGE_PATH.replace('/', '.').substring(1); // obfuscated algorithm to avoid renames at repackaging (shading)

    /**
     * The system dwr.xml resource name
     */
    public static final String SYSTEM_DWR_XML_PATH = PACKAGE_PATH + "/dwr.xml";

    /**
     * The default set of entries into the container
     */
    public static final String SYSTEM_DEFAULT_PROPERTIES_PATH = PACKAGE_PATH + "/defaults.properties";

    /**
     * The default dwr.xml file path
     */
    public static final String USER_DWR_XML_PATH = "/WEB-INF/dwr.xml";
}
