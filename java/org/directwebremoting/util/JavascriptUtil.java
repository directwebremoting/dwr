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
package org.directwebremoting.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Various Javascript code utilities.
 * The escape classes were taken from jakarta-commons-lang which in turn borrowed
 * from Turbine and other projects. The list of authors below is almost certainly
 * far too long, but I'm not sure who really wrote these methods.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Apache Jakarta Turbine
 * @author GenerationJavaCore library
 * @author Purple Technology
 * @author <a href="mailto:bayard@generationjava.com">Henri Yandell</a>
 * @author <a href="mailto:alex@purpletech.com">Alexander Day Chaffee</a>
 * @author <a href="mailto:cybertiger@cyberiantiger.org">Antony Riley</a>
 * @author Helge Tesgaard
 * @author <a href="sean@boohai.com">Sean Brown</a>
 * @author <a href="mailto:ggregory@seagullsw.com">Gary Gregory</a>
 * @author Phil Steitz
 * @author Pete Gieser
 */
public class JavascriptUtil
{
    /**
     * Flag for use in javascript compression: Remove single line comments.
     * For ease of use you may wish to use one of the LEVEL_* compression levels.
     * @noinspection PointlessBitwiseExpression
     */
    public static final int COMPRESS_STRIP_SL_COMMENTS = 1 << 0;

    /**
     * Flag for use in javascript compression: Remove multi line comments.
     * For ease of use you may wish to use one of the LEVEL_* compression levels.
     */
    public static final int COMPRESS_STRIP_ML_COMMENTS = 1 << 1;

    /**
     * Flag for use in javascript compression: Remove whitespace at the start and end of a line.
     * For ease of use you may wish to use one of the LEVEL_* compression levels.
     */
    public static final int COMPRESS_TRIM_LINES = 1 << 2;

    /**
     * Flag for use in javascript compression: Remove blank lines.
     * This option will make the javascript harder to debug because line number references
     * are likely be altered.
     * For ease of use you may wish to use one of the LEVEL_* compression levels.
     */
    public static final int COMPRESS_STRIP_BLANKLINES = 1 << 3;

    /**
     * Flag for use in javascript compression: Shrink variable names.
     * This option is currently un-implemented.
     * For ease of use you may wish to use one of the LEVEL_* compression levels.
     */
    public static final int COMPRESS_SHRINK_VARS = 1 << 4;

    /**
     * Flag for use in javascript compression: Remove all lines endings.
     * Warning: Javascript can add semi-colons in for you. If you make use of this feature
     * then removing newlines may well break.
     * For ease of use you may wish to use one of the LEVEL_* compression levels.
     */
    public static final int COMPRESS_REMOVE_NEWLINES = 1 << 5;

    /**
     * Compression level that leaves the source un-touched.
     */
    public static final int LEVEL_NONE = 0;

    /**
     * Basic compression that leaves the source fully debuggable.
     * This includes removing all comments and extraneous whitespace.
     */
    public static final int LEVEL_DEBUGGABLE = COMPRESS_STRIP_SL_COMMENTS | COMPRESS_STRIP_ML_COMMENTS | COMPRESS_TRIM_LINES;

    /**
     * Normal compression makes all changes that will work for generic javascript.
     * This adds variable name compression and blank line removal in addition to the
     * compressions done by LEVEL_DEBUGGABLE.
     */
    public static final int LEVEL_NORMAL = LEVEL_DEBUGGABLE | COMPRESS_STRIP_BLANKLINES | COMPRESS_SHRINK_VARS;

    /**
     * LEVEL_ULTRA performs additional compression that makes some assumptions about the
     * style of javascript.
     * Specifically it assumes that you are not using javascripts ability to infer where the ;
     * should go.
     */
    public static final int LEVEL_ULTRA = LEVEL_NORMAL | COMPRESS_REMOVE_NEWLINES;

    /**
     * Compress the source code by removing java style comments and removing
     * leading and trailing spaces.
     * @param text The javascript (or java) program to compress
     * @param level The compression level - see LEVEL_* and COMPRESS_* constants.
     * @return The compressed version
     */
    public static String compress(String text, int level)
    {
        String reply = text;

        // First we strip multi line comments. I think this is important:
        if ((level & COMPRESS_STRIP_ML_COMMENTS) != 0)
        {
            reply = stripMultiLineComments(text);
        }

        if ((level & COMPRESS_STRIP_SL_COMMENTS) != 0)
        {
            reply = stripSingleLineComments(reply);
        }

        if ((level & COMPRESS_TRIM_LINES) != 0)
        {
            reply = trimLines(reply);
        }

        if ((level & COMPRESS_STRIP_BLANKLINES) != 0)
        {
            reply = stripBlankLines(reply);
        }

        if ((level & COMPRESS_SHRINK_VARS) != 0)
        {
            reply = shrinkVariableNames(reply);
        }

        if ((level & COMPRESS_REMOVE_NEWLINES) != 0)
        {
            reply = stripNewlines(reply);
        }

        return reply;
    }

    /**
     * Remove any leading or trailing spaces from a line of code.
     * This function could be improved by making it strip unnecessary double
     * spaces, but since we would need to leave double spaces inside strings
     * this is not simple and since the benefit is small, we'll leave it for now
     * @param text The javascript program to strip spaces from.
     * @return The stripped program
     */
    public static String trimLines(String text)
    {
        if (text == null)
        {
            return null;
        }

        try
        {
            StringBuffer output = new StringBuffer();

            // First we strip multi line comments. I think this is important:
            BufferedReader in = new BufferedReader(new StringReader(text));
            while (true)
            {
                String line = in.readLine();
                if (line == null)
                {
                    break;
                }

                output.append(line.trim());
                output.append('\n');
            }

            return output.toString();
        }
        catch (IOException ex)
        {
            log.error("IOExecption unexpected.", ex);
            throw new IllegalArgumentException("IOExecption unexpected.");
        }
    }

    /**
     * Remove all the single-line comments from a block of text
     * @param text The text to remove single-line comments from
     * @return The single-line comment free text
     */
    public static String stripSingleLineComments(String text)
    {
        if (text == null)
        {
            return null;
        }

        try
        {
            StringBuffer output = new StringBuffer();

            BufferedReader in = new BufferedReader(new StringReader(text));
            while (true)
            {
                String line = in.readLine();
                if (line == null)
                {
                    break;
                }

                // Skip @DWR comments
                if (line.indexOf(COMMENT_RETAIN) == -1)
                {
                    int cstart = line.indexOf(COMMENT_SL_START);
                    if (cstart >= 0)
                    {
                        line = line.substring(0, cstart);
                    }
                }

                output.append(line);
                output.append('\n');
            }

            return output.toString();
        }
        catch (IOException ex)
        {
            log.error("IOExecption unexpected.", ex);
            throw new IllegalArgumentException("IOExecption unexpected.");
        }
    }

    /**
     * Remove all the multi-line comments from a block of text
     * @param text The text to remove multi-line comments from
     * @return The multi-line comment free text
     */
    public static String stripMultiLineComments(String text)
    {
        if (text == null)
        {
            return null;
        }

        try
        {
            StringBuffer output = new StringBuffer();

            // Comment rules:
            /*/           This is still a comment
            /* /* */      // Comments do not nest
            // /* */      This is in a comment
            /* // */      // The second // is needed to make this a comment.

            // First we strip multi line comments. I think this is important:
            boolean inMultiLine = false;
            BufferedReader in = new BufferedReader(new StringReader(text));
            while (true)
            {
                String line = in.readLine();
                if (line == null)
                {
                    break;
                }

                if (!inMultiLine)
                {
                    // We are not in a multi-line comment, check for a start
                    int cstart = line.indexOf(COMMENT_ML_START);
                    if (cstart >= 0)
                    {
                        // This could be a MLC on one line ...
                        int cend = line.indexOf(COMMENT_ML_END, cstart + COMMENT_ML_START.length());
                        if (cend >= 0)
                        {
                            // A comment that starts and ends on one line
                            // BUG: you can have more than 1 multi-line comment on a line
                            line = line.substring(0, cstart) + SPACE + line.substring(cend + COMMENT_ML_END.length());
                        }
                        else
                        {
                            // A real multi-line comment
                            inMultiLine = true;
                            line = line.substring(0, cstart) + SPACE;
                        }
                    }
                    else
                    {
                        // We are not in a multi line comment and we havn't
                        // started one so we are going to ignore closing
                        // comments even if they exist.
                    }
                }
                else
                {
                    // We are in a multi-line comment, check for the end
                    int cend = line.indexOf(COMMENT_ML_END);
                    if (cend >= 0)
                    {
                        // End of comment
                        line = line.substring(cend + COMMENT_ML_END.length());
                        inMultiLine = false;
                    }
                    else
                    {
                        // The comment continues
                        line = SPACE;
                    }
                }

                output.append(line);
                output.append('\n');
            }

            return output.toString();
        }
        catch (IOException ex)
        {
            log.error("IOExecption unexpected.", ex);
            throw new IllegalArgumentException("IOExecption unexpected.");
        }
    }

    /**
     * Remove all blank lines from a string.
     * A blank line is defined to be a line where the only characters are whitespace.
     * We always ensure that the line contains a newline at the end.
     * @param text The string to strip blank lines from
     * @return The blank line stripped reply
     */
    public static String stripBlankLines(String text)
    {
        if (text == null)
        {
            return null;
        }

        try
        {
            StringBuffer output = new StringBuffer();

            BufferedReader in = new BufferedReader(new StringReader(text));
            boolean doneOneLine = false;
            while (true)
            {
                String line = in.readLine();
                if (line == null)
                {
                    break;
                }

                if (line.trim().length() > 0)
                {
                    output.append(line);
                    output.append('\n');
                    doneOneLine = true;
                }
            }

            if (!doneOneLine)
            {
                output.append('\n');
            }

            return output.toString();
        }
        catch (IOException ex)
        {
            log.error("IOExecption unexpected.", ex);
            throw new IllegalArgumentException("IOExecption unexpected.");
        }
    }

    /**
     * Remove all newline characters from a string.
     * @param text The string to strip newline characters from
     * @return The stripped reply
     */
    public static String stripNewlines(String text)
    {
        if (text == null)
        {
            return null;
        }

        try
        {
            StringBuffer output = new StringBuffer();

            BufferedReader in = new BufferedReader(new StringReader(text));
            while (true)
            {
                String line = in.readLine();
                if (line == null)
                {
                    break;
                }

                output.append(line);
                output.append(SPACE);
            }
            output.append('\n');

            return output.toString();
        }
        catch (IOException ex)
        {
            log.error("IOExecption unexpected.", ex);
            throw new IllegalArgumentException("IOExecption unexpected.");
        }
    }

    /**
     * Shrink variable names to a minimum.
     * @param text The javascript program to shrink the variable names in.
     * @return The shrunk version of the javascript program.
     */
    public static String shrinkVariableNames(String text)
    {
        if (text == null)
        {
            return null;
        }

        throw new UnsupportedOperationException("Variable name shrinking is not supported");
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
    public static String escapeJavaScript(String str)
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
     * <p>Unescapes any JavaScript literals found in the <code>String</code>.</p>
     * <p>For example, it will turn a sequence of <code>'\'</code> and <code>'n'</code>
     * into a newline character, unless the <code>'\'</code> is preceded by another
     * <code>'\'</code>.</p>
     * @param str  the <code>String</code> to unescape, may be null
     * @return A new unescaped <code>String</code>, <code>null</code> if null string input
     */
    public static String unescapeJavaScript(String str)
    {
        if (str == null)
        {
            return null;
        }

        StringBuffer writer = new StringBuffer(str.length());
        int sz = str.length();
        StringBuffer unicode = new StringBuffer(4);
        boolean hadSlash = false;
        boolean inUnicode = false;

        for (int i = 0; i < sz; i++)
        {
            char ch = str.charAt(i);
            if (inUnicode)
            {
                // if in unicode, then we're reading unicode
                // values in somehow
                unicode.append(ch);
                if (unicode.length() == 4)
                {
                    // unicode now contains the four hex digits
                    // which represents our unicode chacater
                    try
                    {
                        int value = Integer.parseInt(unicode.toString(), 16);
                        writer.append((char) value);
                        unicode.setLength(0);
                        inUnicode = false;
                        hadSlash = false;
                    }
                    catch (NumberFormatException nfe)
                    {
                        throw new IllegalArgumentException("Unable to parse unicode value: " + unicode + " cause: " + nfe);
                    }
                }
                continue;
            }

            if (hadSlash)
            {
                // handle an escaped value
                hadSlash = false;
                switch (ch)
                {
                case '\\':
                    writer.append('\\');
                    break;
                case '\'':
                    writer.append('\'');
                    break;
                case '\"':
                    writer.append('"');
                    break;
                case 'r':
                    writer.append('\r');
                    break;
                case 'f':
                    writer.append('\f');
                    break;
                case 't':
                    writer.append('\t');
                    break;
                case 'n':
                    writer.append('\n');
                    break;
                case 'b':
                    writer.append('\b');
                    break;
                case 'u':
                    // uh-oh, we're in unicode country....
                    inUnicode = true;
                    break;
                default:
                    writer.append(ch);
                    break;
                }
                continue;
            }
            else if (ch == '\\')
            {
                hadSlash = true;
                continue;
            }
            writer.append(ch);
        }

        if (hadSlash)
        {
            // then we're in the weird case of a \ at the end of the
            // string, let's output it anyway.
            writer.append('\\');
        }

        return writer.toString();
    }

    /**
     * Check to see if the given word is reserved or a bad idea in any known
     * version of JavaScript.
     * @param name The word to check
     * @return false if the word is not reserved
     */
    public static boolean isReservedWord(String name)
    {
        return reserved.contains(name);
    }

    /**
     * The array of javascript reserved words
     */
    private static final String[] RESERVED_ARRAY = new String[]
    {
        // Reserved and used at ECMAScript 4
        "as",
        "break",
        "case",
        "catch",
        "class",
        "const",
        "continue",
        "default",
        "delete",
        "do",
        "else",
        "export",
        "extends",
        "false",
        "finally",
        "for",
        "function",
        "if",
        "import",
        "in",
        "instanceof",
        "is",
        "namespace",
        "new",
        "null",
        "package",
        "private",
        "public",
        "return",
        "super",
        "switch",
        "this",
        "throw",
        "true",
        "try",
        "typeof",
        "use",
        "var",
        "void",
        "while",
        "with",
        // Reserved for future use at ECMAScript 4
        "abstract",
        "debugger",
        "enum",
        "goto",
        "implements",
        "interface",
        "native",
        "protected",
        "synchronized",
        "throws",
        "transient",
        "volatile",
        // Reserved in ECMAScript 3, unreserved at 4 best to avoid anyway
        "boolean",
        "byte",
        "char",
        "double",
        "final",
        "float",
        "int",
        "long",
        "short",
        "static",

        // I have seen the folowing list as 'best avoided for function names'
        // but it seems way to all encompassing, so I'm not going to include it
        /*
        "alert", "anchor", "area", "arguments", "array", "assign", "blur",
        "boolean", "button", "callee", "caller", "captureevents", "checkbox",
        "clearinterval", "cleartimeout", "close", "closed", "confirm",
        "constructor", "date", "defaultstatus", "document", "element", "escape",
        "eval", "fileupload", "find", "focus", "form", "frame", "frames",
        "getclass", "hidden", "history", "home", "image", "infinity",
        "innerheight", "isfinite", "innerwidth", "isnan", "java", "javaarray",
        "javaclass", "javaobject", "javapackage", "length", "link", "location",
        "locationbar", "math", "menubar", "mimetype", "moveby", "moveto",
        "name", "nan", "navigate", "navigator", "netscape", "number", "object",
        "onblur", "onerror", "onfocus", "onload", "onunload", "open", "opener",
        "option", "outerheight", "outerwidth", "packages", "pagexoffset",
        "pageyoffset", "parent", "parsefloat", "parseint", "password",
        "personalbar", "plugin", "print", "prompt", "prototype", "radio", "ref",
        "regexp", "releaseevents", "reset", "resizeby", "resizeto",
        "routeevent", "scroll", "scrollbars", "scrollby", "scrollto", "select",
        "self", "setinterval", "settimeout", "status", "statusbar", "stop",
        "string", "submit", "sun", "taint",  "text", "textarea", "toolbar",
        "top", "tostring", "unescape", "untaint", "unwatch", "valueof", "watch",
        "window",
        */
    };

    private static SortedSet reserved = new TreeSet();

    /**
     * For easy access ...
     */
    static
    {
        // The Javascript reserved words array so we don't generate illegal javascript
        reserved.addAll(Arrays.asList(RESERVED_ARRAY));
    }

    private static final String SPACE = " ";

    /**
     * How does a multi line comment start?
     */
    private static final String COMMENT_ML_START = "/*";

    /**
     * How does a multi line comment end?
     */
    private static final String COMMENT_ML_END = "*/";

    /**
     * How does a single line comment start?
     */
    private static final String COMMENT_SL_START = "//";

    /**
     * Sometimes we need to retain the comment because it has special meaning
     */
    private static final String COMMENT_RETAIN = "#DWR";

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(JavascriptUtil.class);
}
