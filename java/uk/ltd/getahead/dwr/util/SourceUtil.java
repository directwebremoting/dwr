package uk.ltd.getahead.dwr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Various code utilities.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SourceUtil
{
    /**
     * Compress the source code by removing java style comments and removing
     * leading and trailing spaces.
     * @param text The javascript (or java) program to compress
     * @return The compressed version
     */
    public static String compress(String text)
    {
        String reply = stripComments(text);
        reply = stripSpaces(reply);
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
    public static String stripSpaces(String text)
    {
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
     * Remove all the single and multi-line comments from a block of text
     * @param text The text to remove comments from
     * @return The comment free text
     */
    public static String stripComments(String text)
    {
        String reply = SourceUtil.stripMultiLineComments(text);
        reply = SourceUtil.stripSingleLineComments(reply);
        return reply;
    }

    /**
     * Remove all the single-line comments from a block of text
     * @param text The text to remove single-line comments from
     * @return The single-line comment free text
     */
    public static String stripSingleLineComments(String text)
    {
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
    public static String stripMultiLineComments(String text)
    {
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

    private static final String SPACE = " "; //$NON-NLS-1$

    private static final String COMMENT_ML_START = "/*"; //$NON-NLS-1$

    private static final String COMMENT_ML_END = "*/"; //$NON-NLS-1$

    private static final String COMMENT_SL_START = "//"; //$NON-NLS-1$

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(SourceUtil.class);
}

