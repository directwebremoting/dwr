package org.directwebremoting.extend;

/**
 * A simple data container for the strings that comprise information about how a
 * Java object has been converted into Javascript.
 * <p>
 * There are 3 steps to conversion:
 * <ul>
 * <li>First the {@link ConverterManager} creates a set of OutboundVariables
 * from the raw data.
 * <li>Second we reference count to see what needs to be outlined (i.e. not
 * done inline). This typically means that something is declared before it is
 * built.
 * <li>Finally we create the full output script.
 * </ul>
 * <p>
 * There are potentially 3 parts to a variable in Javascript. If the variable
 * is not something that can recurse then only the assignCode will contain data.
 * Otherwise all the parts will be filled out.
 * <ul>
 * <li>declareCode: To prevent problems with recursion, we declare everything
 * first. A declaration will look something like <code>var s0 = [];</code> or
 * <code>var s1 = {};</code>.</li>
 * <li>buildCode: Next we fill in the contents of the objects and arrays.</li>
 * <li>assignCode: This is an accessor for the name of the variable so we can
 * refer to anything that has been declared.</li>
 * </ul>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface OutboundVariable
{
    /**
     * Get a reference to this OutboundVariable.
     * During step 1 of the conversion process where we turn the raw objects
     * into OutboundVariables we may wish to refer to something that has already
     * been converted.
     * If <code>this</code> already is a reference then this method returns
     * <code>this</code>, or if not it creates one that does.
     * @return An OutboundVariable that refers to this one.
     */
    OutboundVariable getReferenceVariable();

    /**
     * A script to declare the variable so it can be referred to. This script
     * is guaranteed not to refer to anything that can recurse
     * @return Returns the declareCode
     */
    String getDeclareCode();

    /**
     * A script that completes the definition of this variable, and may contain
     * reference to other declared variables.
     * @return Returns the buildCode.
     */
    String getBuildCode();

    /**
     * A short script that can be used to refer to this variable
     * @return Returns the assignCode.
     */
    String getAssignCode();
}
