package org.directwebremoting.impl;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class LegacyCompressorTest
{
    @Test
    public void testCompress()
    {
        assertEquals(LegacyCompressor.compress(" aaa \n aa \n", LegacyCompressor.COMPRESS_TRIM_LINES), "aaa\naa\n");
        assertEquals(LegacyCompressor.compress("//aaa//bbbb\nxx //aaa\n", LegacyCompressor.COMPRESS_STRIP_SL_COMMENTS), "\nxx \n");
        assertEquals(LegacyCompressor.compress("a/*\n*/a\n", LegacyCompressor.COMPRESS_STRIP_ML_COMMENTS), "a \na\n");
        assertEquals(LegacyCompressor.compress("a\n\na\n", LegacyCompressor.COMPRESS_STRIP_BLANKLINES), "a\na\n");
        assertEquals(LegacyCompressor.compress("a\nb\n", LegacyCompressor.COMPRESS_REMOVE_NEWLINES), "a b \n");
    }

    @Test
    public void testStripSpaces()
    {
        assertEquals(LegacyCompressor.trimLines("\n"), "\n");
        assertEquals(LegacyCompressor.trimLines(null), null);

        assertEquals(LegacyCompressor.trimLines("a a\n"), "a a\n");
        assertEquals(LegacyCompressor.trimLines("aaa\n"), "aaa\n");
        assertEquals(LegacyCompressor.trimLines(" a a \n"), "a a\n");
        assertEquals(LegacyCompressor.trimLines(" aaa \n"), "aaa\n");

        assertEquals(LegacyCompressor.trimLines(" aaa \n aa \n"), "aaa\naa\n");
    }

    @Test
    public void testStripSingleLineComments()
    {
        assertEquals(LegacyCompressor.stripSingleLineComments("\n"), "\n");
        assertEquals(LegacyCompressor.stripSingleLineComments(null), null);

        assertEquals(LegacyCompressor.stripSingleLineComments("a\n"), "a\n");
        assertEquals(LegacyCompressor.stripSingleLineComments("/a\n"), "/a\n");
        assertEquals(LegacyCompressor.stripSingleLineComments("/a/a\n"), "/a/a\n");
        assertEquals(LegacyCompressor.stripSingleLineComments("/ /\n"), "/ /\n");

        assertEquals(LegacyCompressor.stripSingleLineComments("//\n"), "\n");
        assertEquals(LegacyCompressor.stripSingleLineComments("//aaa\n"), "\n");
        assertEquals(LegacyCompressor.stripSingleLineComments("//aaa\naaa\n"), "\naaa\n");
        assertEquals(LegacyCompressor.stripSingleLineComments("//aaa//bbbb\naaa\n"), "\naaa\n");
        assertEquals(LegacyCompressor.stripSingleLineComments("//aaa//bbbb\nxx //aaa\n"), "\nxx \n");
    }

    @Test
    public void testStripMultiLineComments()
    {
        assertEquals(LegacyCompressor.stripMultiLineComments("\n"), "\n");
        assertEquals(LegacyCompressor.stripMultiLineComments(null), null);

        assertEquals(LegacyCompressor.stripMultiLineComments("a\n"), "a\n");
        assertEquals(LegacyCompressor.stripMultiLineComments("/a\n"), "/a\n");
        assertEquals(LegacyCompressor.stripMultiLineComments("/a*a\n"), "/a*a\n");
        assertEquals(LegacyCompressor.stripMultiLineComments("/ */ *\n"), "/ */ *\n");

        assertEquals(LegacyCompressor.stripMultiLineComments("/**/\n"), " \n");
        assertEquals(LegacyCompressor.stripMultiLineComments("/***/\n"), " \n");
        assertEquals(LegacyCompressor.stripMultiLineComments("/*a*/\n"), " \n");
        assertEquals(LegacyCompressor.stripMultiLineComments("a/**/a\n"), "a a\n");
        assertEquals(LegacyCompressor.stripMultiLineComments("a/*\n*/a\n"), "a \na\n");
    }

    @Test
    public void testStripBlankLines()
    {
        assertEquals(LegacyCompressor.stripBlankLines("\n"), "\n");
        assertEquals(LegacyCompressor.stripBlankLines("\n\n"), "\n");
        assertEquals(LegacyCompressor.stripBlankLines(null), null);

        assertEquals(LegacyCompressor.stripBlankLines("a\n"), "a\n");
        assertEquals(LegacyCompressor.stripBlankLines("a\na\n"), "a\na\n");
        assertEquals(LegacyCompressor.stripBlankLines(" \n \n"), "\n");
        assertEquals(LegacyCompressor.stripBlankLines(" \n\t\n#\n"), "#\n");

        assertEquals(LegacyCompressor.stripBlankLines("\n\n\n"), "\n");
        assertEquals(LegacyCompressor.stripBlankLines("a\n\na\n"), "a\na\n");
    }

    @Test
    public void testStripNewlines()
    {
        assertEquals(LegacyCompressor.stripNewlines(""), "\n");
        assertEquals(LegacyCompressor.stripNewlines("\n"), " \n");
        assertEquals(LegacyCompressor.stripNewlines(null), null);

        assertEquals(LegacyCompressor.stripNewlines("a\nb\n"), "a b \n");
    }
    
    @Test
    public void testHttp() throws IOException {
        String http = "path.indexOf(\"http://\") == 0\n";
        String https = "path.indexOf(\"https://\") == 0\n";
        
        LegacyCompressor compressor = new LegacyCompressor();
        
        assertEquals(http, compressor.compressJavaScript(http));
        assertEquals(https, compressor.compressJavaScript(https));
    }
}
