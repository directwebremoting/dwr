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
package uk.ltd.getahead.dwr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
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
    public String compress(String text, int level)
    {
        String reply = text;

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
    public String trimLines(String text)
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
            log.error("IOExecption unexpected.", ex); //$NON-NLS-1$
            throw new IllegalArgumentException("IOExecption unexpected."); //$NON-NLS-1$
        }
    }

    /**
     * Remove all the single-line comments from a block of text
     * @param text The text to remove single-line comments from
     * @return The single-line comment free text
     */
    public String stripSingleLineComments(String text)
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

                int cstart = line.indexOf(COMMENT_SL_START);
                if (cstart >= 0)
                {
                    line = line.substring(0, cstart);
                }

                output.append(line);
                output.append('\n');
            }

            return output.toString();
        }
        catch (IOException ex)
        {
            log.error("IOExecption unexpected.", ex); //$NON-NLS-1$
            throw new IllegalArgumentException("IOExecption unexpected."); //$NON-NLS-1$
        }
    }

    /**
     * Remove all the multi-line comments from a block of text
     * @param text The text to remove multi-line comments from
     * @return The multi-line comment free text
     */
    public String stripMultiLineComments(String text)
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
            log.error("IOExecption unexpected.", ex); //$NON-NLS-1$
            throw new IllegalArgumentException("IOExecption unexpected."); //$NON-NLS-1$
        }
    }

    /**
     * Remove all blank lines from a string.
     * A blank line is defined to be a line where the only characters are whitespace.
     * We always ensure that the line contains a newline at the end.
     * @param text The string to strip blank lines from
     * @return The blank line stripped reply
     */
    public String stripBlankLines(String text)
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
            log.error("IOExecption unexpected.", ex); //$NON-NLS-1$
            throw new IllegalArgumentException("IOExecption unexpected."); //$NON-NLS-1$
        }
    }

    /**
     * Remove all newline characters from a string.
     * @param text The string to strip newline characters from
     * @return The stripped reply
     */
    public String stripNewlines(String text)
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
            log.error("IOExecption unexpected.", ex); //$NON-NLS-1$
            throw new IllegalArgumentException("IOExecption unexpected."); //$NON-NLS-1$
        }
    }

    /**
     * Shrink variable names to a minimum.
     * @param text The javascript program to shrink the variable names in.
     * @return The shrunk version of the javascript program.
     */
    public String shrinkVariableNames(String text)
    {
        if (text == null)
        {
            return null;
        }

        throw new UnsupportedOperationException("Variable name shrinking is not supported"); //$NON-NLS-1$
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
    public String escapeJavaScript(String str) {
        if (str == null) {
            return null;
        }

        StringBuffer writer = new StringBuffer(str.length() * 2);
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
        
            // handle unicode
            if (ch > 0xfff) {
                writer.append("\\u" + hex(ch)); //$NON-NLS-1$
            } else if (ch > 0xff) {
                writer.append("\\u0" + hex(ch)); //$NON-NLS-1$
            } else if (ch > 0x7f) {
                writer.append("\\u00" + hex(ch)); //$NON-NLS-1$
            } else if (ch < 32) {
                switch (ch) {
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
                    default :
                        if (ch > 0xf) {
                            writer.append("\\u00" + hex(ch)); //$NON-NLS-1$
                        } else {
                            writer.append("\\u000" + hex(ch)); //$NON-NLS-1$
                        }
                        break;
                }
            } else {
                switch (ch) {
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
                    default :
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
     *
     * @param ch The character to convert.
     * @return An upper case hexadecimal <code>String</code>
     */
    private String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }

    /**
     * <p>Unescapes any JavaScript literals found in the <code>String</code>.</p>
     * <p>For example, it will turn a sequence of <code>'\'</code> and <code>'n'</code>
     * into a newline character, unless the <code>'\'</code> is preceded by another
     * <code>'\'</code>.</p>
     * @param str  the <code>String</code> to unescape, may be null
     * @return A new unescaped <code>String</code>, <code>null</code> if null string input
     */
    public String unescapeJavaScript(String str) {
        if (str == null) {
            return null;
        }

        StringBuffer writer = new StringBuffer(str.length());
        int sz = str.length();
        StringBuffer unicode = new StringBuffer(4);
        boolean hadSlash = false;
        boolean inUnicode = false;
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (inUnicode) {
                // if in unicode, then we're reading unicode
                // values in somehow
                unicode.append(ch);
                if (unicode.length() == 4) {
                    // unicode now contains the four hex digits
                    // which represents our unicode chacater
                    try {
                        int value = Integer.parseInt(unicode.toString(), 16);
                        writer.append((char) value);
                        unicode.setLength(0);
                        inUnicode = false;
                        hadSlash = false;
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException("Unable to parse unicode value: " + unicode + " cause: " + nfe); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
                continue;
            }
            if (hadSlash) {
                // handle an escaped value
                hadSlash = false;
                switch (ch) {
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
                    default :
                        writer.append(ch);
                        break;
                }
                continue;
            } else if (ch == '\\') {
                hadSlash = true;
                continue;
            }
            writer.append(ch);
        }
        if (hadSlash) {
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
    public boolean isReservedWord(String name)
    {
        return reserved.contains(name);
    }

    /**
     * The array of javascript reserved words
     */
    private static final String[] RESERVED_ARRAY = new String[]
    {
        // Reserved and used at ECMAScript 4
        "as", //$NON-NLS-1$
        "break", //$NON-NLS-1$
        "case", //$NON-NLS-1$
        "catch", //$NON-NLS-1$
        "class", //$NON-NLS-1$
        "const", //$NON-NLS-1$
        "continue", //$NON-NLS-1$
        "default", //$NON-NLS-1$
        "delete", //$NON-NLS-1$
        "do", //$NON-NLS-1$
        "else", //$NON-NLS-1$
        "export", //$NON-NLS-1$
        "extends", //$NON-NLS-1$
        "false", //$NON-NLS-1$
        "finally", //$NON-NLS-1$
        "for", //$NON-NLS-1$
        "function", //$NON-NLS-1$
        "if", //$NON-NLS-1$
        "import", //$NON-NLS-1$
        "in", //$NON-NLS-1$
        "instanceof", //$NON-NLS-1$
        "is", //$NON-NLS-1$
        "namespace", //$NON-NLS-1$
        "new", //$NON-NLS-1$
        "null", //$NON-NLS-1$
        "package", //$NON-NLS-1$
        "private", //$NON-NLS-1$
        "public", //$NON-NLS-1$
        "return", //$NON-NLS-1$
        "super", //$NON-NLS-1$
        "switch", //$NON-NLS-1$
        "this", //$NON-NLS-1$
        "throw", //$NON-NLS-1$
        "true", //$NON-NLS-1$
        "try", //$NON-NLS-1$
        "typeof", //$NON-NLS-1$
        "use", //$NON-NLS-1$
        "var", //$NON-NLS-1$
        "void", //$NON-NLS-1$
        "while", //$NON-NLS-1$
        "with", //$NON-NLS-1$
        // Reserved for future use at ECMAScript 4
        "abstract", //$NON-NLS-1$
        "debugger", //$NON-NLS-1$
        "enum", //$NON-NLS-1$
        "goto", //$NON-NLS-1$
        "implements", //$NON-NLS-1$
        "interface", //$NON-NLS-1$
        "native", //$NON-NLS-1$
        "protected", //$NON-NLS-1$
        "synchronized", //$NON-NLS-1$
        "throws", //$NON-NLS-1$
        "transient", //$NON-NLS-1$
        "volatile", //$NON-NLS-1$
        // Reserved in ECMAScript 3, unreserved at 4 best to avoid anyway
        "boolean", //$NON-NLS-1$
        "byte", //$NON-NLS-1$
        "char", //$NON-NLS-1$
        "double", //$NON-NLS-1$
        "final", //$NON-NLS-1$
        "float", //$NON-NLS-1$
        "int", //$NON-NLS-1$
        "long", //$NON-NLS-1$
        "short", //$NON-NLS-1$
        "static", //$NON-NLS-1$

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

    private static final String SPACE = " "; //$NON-NLS-1$

    private static final String COMMENT_ML_START = "/*"; //$NON-NLS-1$

    private static final String COMMENT_ML_END = "*/"; //$NON-NLS-1$

    private static final String COMMENT_SL_START = "//"; //$NON-NLS-1$

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(JavascriptUtil.class);
}

