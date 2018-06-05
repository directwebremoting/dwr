package org.directwebremoting;

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
     * when we wish to reduce the risk of XSS attacks.
     * @param original The string to perform entity replacement on
     * @return The original string with &amp;, &lt;, &gt;, ' and " escaped.
     * @see #unescapeHtml(String)
     */
    public static String escapeHtml(String original)
    {
        String reply = original;
        reply = reply.replace("&", "&amp;");
        reply = reply.replace("<", "&lt;");
        reply = reply.replace(">", "&gt;");
        reply = reply.replace("\'", "&apos;");
        reply = reply.replace("\"", "&quot;");
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
     * {@link #escapeHtml(String)}.
     * @param original The string to perform entity replacement on
     * @return The original string with &amp;, &lt;, &gt;, ' and " replaced.
     * @see #escapeHtml(String)
     */
    public static String unescapeHtml(String original)
    {
        String reply = original;
        reply = reply.replace("&amp;", "&");
        reply = reply.replace("&lt;", "<");
        reply = reply.replace("&gt;", ">");
        reply = reply.replace("&apos;", "\'");
        reply = reply.replace("&quot;", "\"");
        return reply;
    }

    /**
     * Perform the following replacements:<ul>
     * <li>&amp; to +</li>
     * <li>&lt; to \\u2039 (\u2039)</li>
     * <li>&gt; to \\u203A (\u203A)</li>
     * <li>&apos; to \\u2018 (\u2018)</li>
     * <li>&quot; to \\u201C (\u201C)</li>
     * </ul>
     * These replacements are useful when readability is more important than
     * retaining the exact character string of the original.
     * @param original The string to perform entity replacement on
     * @return The original string with &amp;, &lt;, &gt;, ' and " escaped.
     */
    public static String replaceXmlCharacters(String original)
    {
        String reply = original;
        reply = reply.replace("&", "+");
        reply = reply.replace("<", "\u2039");
        reply = reply.replace(">", "\u203A");
        reply = reply.replace("\'", "\u2018");
        reply = reply.replace("\"", "\u201C");
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
            || original.indexOf('<') != -1
            || original.indexOf('>') != -1
            || original.indexOf('\'') != -1
            || original.indexOf('\"') != -1);
    }
}
