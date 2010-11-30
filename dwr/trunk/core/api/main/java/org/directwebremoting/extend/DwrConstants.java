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
     * The package name.
     */
    public static final String PACKAGE_NAME = "org.directwebremoting";

    /**
     * The package path because people need to load resources in this package.
     */
    public static final String PACKAGE_PATH = "/" + PACKAGE_NAME.replace('.', '/');

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
