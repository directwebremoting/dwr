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
package jsx3.net;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class URIResolver
{
    /**
     * {jsx3.net.URIResolver} The default URI resolver. This resolver can
     * resolve any of the absolute URI formats supported by the system. Other
     * absolute URIs and all relative URIs are unmodified. The absolute URI
     * formats are:
     * <ul>
     *   <li><code>JSX/...</code> &#8211;</li>
     *   <li><code>JSXAPPS/...</code> &#8211;</li>
     *   <li><code>GI_Builder/...</code> &#8211;</li>
     *   <li><code>jsx:/...</code> &#8211;</li>
     *   <li><code>jsxapp://appPath/...</code> &#8211;</li>
     *   <li><code>jsxaddin://addinKey/...</code> &#8211;</li>
     *   <li><code>jsxuser:/...</code> &#8211;</li>
     * </ul>
     */
    public static final URIResolver DEFAULT = new URIResolver("jsx3.net.URIResolver.DEFAULT");

    /** 
     * {jsx3.net.URIResolver} Resolves URIs according to the default resolver
     * except that all relative URIs are resolved relative to the
     * <code>JSX/</code> directory. This resolver resolves the following URIs to
     * the same value:
     * <ul>
     *   <li><code>JSX/file.xml</code></li>
     *   <li><code>jsx:/file.xml</code></li>
     *   <li><code>file.xml</code></li>
     * </ul>
     */
    public static final URIResolver JSX = new URIResolver("jsx3.net.URIResolver.JSX");
    
    /** 
     * {jsx3.net.URIResolver} Resolves URIs according to the default resolver
     * except that all relative URIs are resolved relative to the user directory
     * (or <code>JSXAPPS/../</code>). This resolver resolves the following URIs
     * to the same value:
     * <ul>
     *   <li><code>JSXAPPS/../file.xml</code></li>
     *   <li><code>jsxuser:/file.xml</code></li>
     *   <li><code>file.xml</code></li>
     * </ul>
     */
    public static final URIResolver USER = new URIResolver("jsx3.net.URIResolver.USER");

    /**
     * @param name
     */
    protected static URIResolver toURIResolver(String name)
    {
        if (name.equals(URIResolver.DEFAULT.constant))
        {
            return DEFAULT;
        }

        if (name.equals(URIResolver.JSX.constant))
        {
            return JSX;
        }

        if (name.equals(URIResolver.USER.constant))
        {
            return USER;
        }

        return null;
    }

    /**
     * @param constant
     */
    public URIResolver(String constant)
    {
        this.constant = constant;
    }

    /**
     * The string to send to Javascript
     */
    protected final String constant;
}
