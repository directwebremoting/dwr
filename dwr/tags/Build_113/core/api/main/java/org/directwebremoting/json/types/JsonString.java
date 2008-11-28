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
package org.directwebremoting.json.types;

import java.util.Locale;

/**
 * The Json version of a String
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JsonString extends JsonValue
{
    /**
     * All JsonStrings wrap a Java string
     */
    public JsonString(String value)
    {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#getString()
     */
    @Override
    public String getString()
    {
        return value;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.json.JsonValue#toExternalRepresentation()
     */
    @Override
    public String toExternalRepresentation()
    {
        return "'" + escapeJavaScript(value) + "'";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getString();
    }

    /**
     * <p>Escapes the characters in a <code>String</code> using JavaScript String rules.</p>
     * <p>Escapes any values it finds into their JavaScript String form.
     * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters <code>'\\'</code> and
     * <code>'t'</code>.</p>
     *
     * <p>The only difference between Java strings and JavaScript strings
     * is that in JavaScript, a single quote must be escaped.</p>
     *
     * <p>Example:
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn\'t say, \"Stop!\"
     * </pre>
     * </p>
     *
     * @param str  String to escape values in, may be null
     * @return String with escaped values, <code>null</code> if null string input
     */
    protected static String escapeJavaScript(String str)
    {
        if (str == null)
        {
            return null;
        }

        StringBuffer writer = new StringBuffer(str.length() * 2);

        int sz = str.length();
        for (int i = 0; i < sz; i++)
        {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff)
            {
                writer.append("\\u");
                writer.append(hex(ch));
            }
            else if (ch > 0xff)
            {
                writer.append("\\u0");
                writer.append(hex(ch));
            }
            else if (ch > 0x7f)
            {
                writer.append("\\u00");
                writer.append(hex(ch));
            }
            else if (ch < 32)
            {
                switch (ch)
                {
                case '\b':
                    writer.append('\\');
                    writer.append('b');
                    break;
                case '\n':
                    writer.append('\\');
                    writer.append('n');
                    break;
                case '\t':
                    writer.append('\\');
                    writer.append('t');
                    break;
                case '\f':
                    writer.append('\\');
                    writer.append('f');
                    break;
                case '\r':
                    writer.append('\\');
                    writer.append('r');
                    break;
                default:
                    if (ch > 0xf)
                    {
                        writer.append("\\u00");
                        writer.append(hex(ch));
                    }
                    else
                    {
                        writer.append("\\u000");
                        writer.append(hex(ch));
                    }
                    break;
                }
            }
            else
            {
                switch (ch)
                {
                case '\'':
                    // If we wanted to escape for Java strings then we would
                    // not need this next line.
                    writer.append('\\');
                    writer.append('\'');
                    break;
                case '"':
                    writer.append('\\');
                    writer.append('"');
                    break;
                case '\\':
                    writer.append('\\');
                    writer.append('\\');
                    break;
                default:
                    writer.append(ch);
                    break;
                }
            }
        }

        return writer.toString();
    }

    /**
     * <p>Returns an upper case hexadecimal <code>String</code> for the given
     * character.</p>
     * @param ch The character to convert.
     * @return An upper case hexadecimal <code>String</code>
     */
    private static String hex(char ch)
    {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }

    /**
     * The string value that we wrap
     */
    private final String value;
}
