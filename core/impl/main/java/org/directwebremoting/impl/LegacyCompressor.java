package org.directwebremoting.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.extend.Compressor;
import org.directwebremoting.util.LocalUtil;

/**
 * An implementation of {@link Compressor} that does nothing.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class LegacyCompressor implements Compressor
{
    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Compressor#compressJavaScript(java.lang.String, java.lang.String)
     */
    public String compressJavaScript(String script) throws IOException
    {
        return compress(script, compressionLevel);
    }

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

        BufferedReader in = null;
        try
        {
            StringBuffer output = new StringBuffer();

            // First we strip multi line comments. I think this is important:
            in = new BufferedReader(new StringReader(text));
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
        finally
        {
            LocalUtil.close(in);
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

        BufferedReader in = null;
        try
        {
            StringBuffer output = new StringBuffer();

            in = new BufferedReader(new StringReader(text));
            while (true)
            {
                String line = in.readLine();
                if (line == null)
                {
                    break;
                }

                if (line.contains(COMMENT_SL_START))
                {
                    // Skip @DWR comments
                    if (!line.contains(COMMENT_RETAIN) && !line.matches(".*https?://.*"))
                    {
                        int cstart = line.indexOf(COMMENT_SL_START);
                        if (cstart >= 0)
                        {
                            line = line.substring(0, cstart);
                        }
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
        finally
        {
            LocalUtil.close(in);
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

        BufferedReader in = null;
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
            in = new BufferedReader(new StringReader(text));
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
                            line = line.substring(0, cstart) + " " + line.substring(cend + COMMENT_ML_END.length());
                        }
                        else
                        {
                            // A real multi-line comment
                            inMultiLine = true;
                            line = line.substring(0, cstart) + " ";
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
                        line = " ";
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
        finally
        {
            LocalUtil.close(in);
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

        BufferedReader in = null;
        try
        {
            StringBuffer output = new StringBuffer();

            in = new BufferedReader(new StringReader(text));
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
        finally
        {
            LocalUtil.close(in);
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

        BufferedReader in = null;
        try
        {
            StringBuffer output = new StringBuffer();

            in = new BufferedReader(new StringReader(text));
            while (true)
            {
                String line = in.readLine();
                if (line == null)
                {
                    break;
                }

                output.append(line);
                output.append(" ");
            }
            output.append('\n');

            return output.toString();
        }
        catch (IOException ex)
        {
            log.error("IOExecption unexpected.", ex);
            throw new IllegalArgumentException("IOExecption unexpected.");
        }
        finally
        {
            LocalUtil.close(in);
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
     * @param compressionLevel The compressionLevel to set.
     */
    public void setCompressionLevel(int compressionLevel)
    {
        this.compressionLevel = compressionLevel;
    }

    /**
     * How much do we compression javascript by?
     */
    protected int compressionLevel = LEVEL_DEBUGGABLE;

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
     * Specifically it assumes that you are not using JavaScript's ability to infer where the ;
     * should go.
     */
    public static final int LEVEL_ULTRA = LEVEL_NORMAL | COMPRESS_REMOVE_NEWLINES;

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
    private static final Log log = LogFactory.getLog(LegacyCompressor.class);
}
