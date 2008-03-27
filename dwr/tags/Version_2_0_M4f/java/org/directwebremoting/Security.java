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
package org.directwebremoting;

import org.directwebremoting.util.LocalUtil;

/**
 * Some simple replacement utilities to help people protect themselves from
 * XSS attacks.
 * <p>This class represents some simple filters which <b>may</b> protect from
 * simple attacks in low risk environments. There is no replacement for a full
 * security review which assesses the risks that you face.</p>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Security
{
    /**
     * Perform the following replacements:<ul>
     * <li>&amp; to &amp;amp;</li>
     * <li>&lt; to &amp;lt;</li>
     * <li>&gt; to &amp;gt;</li>
     * <li>&apos; to &amp;apos;</li>
     * <li>&quot; to &amp;quot;</li>
     * </ul>
     * These replacements are useful when the original sense is important, but
     * when we wish to reduce the risk of XSS attacks
     * @param original The string to perform entity replacement on
     * @return The original string with &amp;, &lt;, &gt;, ' and " escaped.
     */
    public static String addEntities(String original)
    {
        String reply = original;
        reply = LocalUtil.replace(reply, "&", "&amp;");
        reply = LocalUtil.replace(reply, "<", "&lt;");
        reply = LocalUtil.replace(reply, ">", "&gt;");
        reply = LocalUtil.replace(reply, "\'", "&apos;");
        reply = LocalUtil.replace(reply, "\"", "&quot;");
        return reply;
    }

    /**
     * Perform the following replacements:<ul>
     * <li>&amp;amp; to &amp;</li>
     * <li>&amp;lt; to &lt;</li>
     * <li>&amp;gt; to &gt;</li>
     * <li>&amp;apos; to &apos;</li>
     * <li>&amp;quot; to &quot;</li>
     * </ul>
     * These replacements are useful to reverse the effects of
     * {@link #addEntities(String)}.
     * @param original The string to perform entity replacement on
     * @return The original string with &amp;, &lt;, &gt;, ' and " replaced.
     */
    public static String removeEntities(String original)
    {
        String reply = original;
        reply = LocalUtil.replace(reply, "&amp;", "&");
        reply = LocalUtil.replace(reply, "&lt;", "<");
        reply = LocalUtil.replace(reply, "&gt;", ">");
        reply = LocalUtil.replace(reply, "&apos;", "\'");
        reply = LocalUtil.replace(reply, "&quot;", "\"");
        return reply;
    }

    /**
     * Perform the following replacements:<ul>
     * <li>&amp; to +</li>
     * <li>&lt; to \\u2039 (\u2039) (�)</li>
     * <li>&gt; to \\u203A (\u203A) (�)</li>
     * <li>&apos; to \\u2018 (\u2018) (�)</li>
     * <li>&quot; to \\u201C (\u201C) (�)</li>
     * </ul>
     * These replacements are useful when readibility is more important than
     * retaining the exact character string of the original.
     * @param original The string to perform entity replacement on
     * @return The original string with &amp;, &lt;, &gt;, ' and " escaped.
     */
    public static String replaceXmlCharacters(String original)
    {
        String reply = original;
        reply = LocalUtil.replace(reply, "&", "+");
        reply = LocalUtil.replace(reply, "<", "\u2039");
        reply = LocalUtil.replace(reply, ">", "\u203A");
        reply = LocalUtil.replace(reply, "\'", "\u2018");
        reply = LocalUtil.replace(reply, "\"", "\u201C");
        return reply;
    }

    /**
     * Return true iff the input string contains any of the characters that
     * are special to XML: &amp;, &lt;, &gt;, &apos; or &quot;
     * @param original The string to test for XML special characters
     * @return True if the characters are found, false otherwise
     */
    public static boolean containsXssRiskyCharacters(String original)
    {
        return (original.indexOf('&') != -1
            && original.indexOf('<') != -1
            && original.indexOf('>') != -1
            && original.indexOf('\'') != -1
            && original.indexOf('\"') != -1);
    }
}
