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
package org.directwebremoting.impl;

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
}
