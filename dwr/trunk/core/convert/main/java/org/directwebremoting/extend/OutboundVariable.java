/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.extend;

/**
 * A simple data container for 2 strings that comprise information about how a
 * Java object has been converted into Javascript.
 * <p>There are potentially 3 parts to a variable in Javascript. If the variable
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

    /**
     * Get a reference to this OutboundVariable.
     * If <code>this</code> already is a reference then this method returns
     * <code>this</code>, or if not it creates one that does.
     * @return An OutboundVariable that refers to this one.
     */
    OutboundVariable getReferenceVariable();

    /**
     * Things work out if they are doubly referenced during the conversion
     * process, and can't be sure how to create output until that phase is done.
     * This method creates the assign code such that other variables can
     * refer to us when creating build and declare codes
     */
    void prepareAssignCode();

    /**
     * Create build and declare codes.
     * @see #prepareAssignCode()
     */
    void prepareBuildDeclareCodes();
}
