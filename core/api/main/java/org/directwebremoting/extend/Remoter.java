package org.directwebremoting.extend;

/**
 * The heart of DWR is a system to generate content from some requests.
 * This interface generates scripts and executes remote calls.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface Remoter
{
    /**
     * Generate some Javascript that forms an interface definition
     * @param scriptName The script to generate for
     * @param indent Indent string prepended to all generated text lines
     * @param assignVariable JavaScript identifier to generate an assignment of the interface to
     * @param contextServletPath request.contextPath + request.servletPath.
     * @return An interface javascript
     * @throws SecurityException
     */
    String generateInterfaceJavaScript(String scriptName, String indent, String assignVariable, String contextServletPath) throws SecurityException;

    /**
     * Generate JavaScript that forms a mapped DTO class
     * @param jsClassName The mapped JavaScript class name
     * @param indent Indent string prepended to all generated text lines
     * @param assignVariable JavaScript identifier to generate an assignment of the class to
     * @return JavaScript class definition
     * @throws SecurityException
     */
    String generateDtoJavaScript(String jsClassName, String indent, String assignVariable) throws SecurityException;

    /**
     * Generate JavaScript that sets up a DTO class's inheritance from its superclass
     * @param indent Indent string prepended to all generated text lines
     * @param classExpression The mapped JavaScript class name
     * @param superClassExpression The mapped JavaScript superclass name
     * @param delegateFunction Name of a callable JavaScript function that will create a prototype delegate
     * (f ex dojo.delegate, in other frameworks sometimes called clone, beget, etc)
     * @return JavaScript inheritance statement
     * @throws SecurityException
     */
    String generateDtoInheritanceJavaScript(String indent, String classExpression, String superClassExpression, String delegateFunction);

    /**
     * Execute a set of remote calls and generate set of reply data for later
     * conversion to whatever wire protocol we are using today.
     * @param calls The set of calls to execute
     * @return A set of reply data objects
     */
    Replies execute(Calls calls);

    /**
     * The path to the DWR servlet is probably just equal to request.contextPath
     * plus request.servletPath. However there are 2 ways to override this.
     * One is to provide an overridePath setting, and the other is to specify
     * useAbsolutePath=true, when the full URL up to the DWR servlet is used.
     * This method simply echos back the contextServletPath unless one of those
     * 2 settings are used in which case the modified value is returned.
     * @param contextServletPath request.contextPath + request.servletPath.
     * @return The path to the DWR servlet
     */
    String getPathToDwrServlet(String contextServletPath);
}
