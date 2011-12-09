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
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public interface OutboundVariable
{
    /**
     * @return Returns the buildCode.
     */
    String getBuildCode();

    /**
     * @return Returns the declareCode
     */
    String getDeclareCode();

    /**
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
}
