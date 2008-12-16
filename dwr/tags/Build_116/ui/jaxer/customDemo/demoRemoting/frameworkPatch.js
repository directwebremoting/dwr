
/**
 * This code should be migrated into the Jaxer framework classes
 */
Jaxer.dwr = {};

/**
 * This is the path to the DWR servlet. Generally this string will end 'dwr'.
 * <p>This property <strong>must</strong> be set before any calls to
 * <code>Jaxer.dwr.require()</code>.
 * <p>Example usage:
 * <code>Jaxer.dwr.pathToDwrServlet = "http://javaserver.intranet:8080/HOSTEDAPP/dwr";</code>
 */
Jaxer.dwr.pathToDwrServlet = null;

/**
 * Import Java resources into Jaxer.
 * Each resource is the name of a Java class prefixed with details about how it
 * is to be instantiated. In the initial release, only 'new' is supported
 * although it is envisaged that support for other creators like spring/guice
 * will be added shortly. There is a special value of 'util' that will give
 * access to the dwr.util classes
 * <p>Example usage:
 * <code>Jaxer.dwr.require("util", "new/java.io.File");</code>
 * @param {string} varargs list of resources to import
 */
Jaxer.dwr.requireUtil = function() {
  Jaxer.dwr._requireSingle("util");
}

/**
 * Import Java resources into Jaxer.
 * Each resource is the name of a Java class prefixed with details about how it
 * is to be instantiated. In the initial release, only 'new' is supported
 * although it is envisaged that support for other creators like spring/guice
 * will be added shortly.
 * <p>Example usage:
 * <code>Jaxer.dwr.configureAccess("java.io.File");</code>
 * @param {string} varargs list of resources to import
 */
Jaxer.dwr.configureAccess = function(className) {
  if (!Jaxer.dwr.pathToDwrServlet) {
    Jaxer.Log.error("Jaxer.dwr.pathToDwrServlet has not been set");
    return;
  }
  if (!Jaxer.dwr._haveEngine) {
    Jaxer.dwr._requireSingle("engine");
    Jaxer.dwr._haveEngine = true;
  }
  Jaxer.dwr._requireSingle("new/" + className);
}

/**
 * Internal function to import a single resource
 * @param {Object} required The short name of the resource to import
 */
Jaxer.dwr._requireSingle = function(required) {
  var url = Jaxer.dwr.pathToDwrServlet + "/" + required + ".js";
  Jaxer.Log.info("importing: " + url);
  Jaxer.load(url);
};
