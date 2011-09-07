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
 * Some constants to do with the heart of DWR.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @noinspection InterfaceNeverImplemented
 */
public interface DwrConstants
{
    /**
     * The package name because people need to load resources in this package.
     */
    public static final String PACKAGE = "/org/directwebremoting";

    /**
     * The system dwr.xml resource name
     */
    public static final String FILE_DWR_XML = PACKAGE + "/dwr.xml";

    /**
     * The default dwr.xml file path
     */
    public static final String DEFAULT_DWR_XML = "/WEB-INF/dwr.xml";

    /**
     * The string that we use to make scripts fail eval() without processing
     */
    public static final String SCRIPT_TAG_PROTECTION = "throw 'allowScriptTagRemoting is false.';";
}
