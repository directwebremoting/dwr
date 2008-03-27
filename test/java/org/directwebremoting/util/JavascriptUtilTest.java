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

import static org.junit.Assert.*;
import org.directwebremoting.util.JavascriptUtil;
import org.junit.Test;

/**
 * JUnit
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JavascriptUtilTest
{
    @Test
    public void testCompress()
    {
        assertEquals(JavascriptUtil.compress(" aaa \n aa \n", JavascriptUtil.COMPRESS_TRIM_LINES), "aaa\naa\n");
        assertEquals(JavascriptUtil.compress("//aaa//bbbb\nxx //aaa\n", JavascriptUtil.COMPRESS_STRIP_SL_COMMENTS), "\nxx \n");
        assertEquals(JavascriptUtil.compress("a/*\n*/a\n", JavascriptUtil.COMPRESS_STRIP_ML_COMMENTS), "a \na\n");
        assertEquals(JavascriptUtil.compress("a\n\na\n", JavascriptUtil.COMPRESS_STRIP_BLANKLINES), "a\na\n");
        assertEquals(JavascriptUtil.compress("a\nb\n", JavascriptUtil.COMPRESS_REMOVE_NEWLINES), "a b \n");
    }

    @Test
    public void testStripSpaces()
    {
        assertEquals(JavascriptUtil.trimLines("\n"), "\n");
        assertEquals(JavascriptUtil.trimLines(null), null);

        assertEquals(JavascriptUtil.trimLines("a a\n"), "a a\n");
        assertEquals(JavascriptUtil.trimLines("aaa\n"), "aaa\n");
        assertEquals(JavascriptUtil.trimLines(" a a \n"), "a a\n");
        assertEquals(JavascriptUtil.trimLines(" aaa \n"), "aaa\n");

        assertEquals(JavascriptUtil.trimLines(" aaa \n aa \n"), "aaa\naa\n");
    }

    @Test
    public void testStripSingleLineComments()
    {
        assertEquals(JavascriptUtil.stripSingleLineComments("\n"), "\n");
        assertEquals(JavascriptUtil.stripSingleLineComments(null), null);

        assertEquals(JavascriptUtil.stripSingleLineComments("a\n"), "a\n");
        assertEquals(JavascriptUtil.stripSingleLineComments("/a\n"), "/a\n");
        assertEquals(JavascriptUtil.stripSingleLineComments("/a/a\n"), "/a/a\n");
        assertEquals(JavascriptUtil.stripSingleLineComments("/ /\n"), "/ /\n");

        assertEquals(JavascriptUtil.stripSingleLineComments("//\n"), "\n");
        assertEquals(JavascriptUtil.stripSingleLineComments("//aaa\n"), "\n");
        assertEquals(JavascriptUtil.stripSingleLineComments("//aaa\naaa\n"), "\naaa\n");
        assertEquals(JavascriptUtil.stripSingleLineComments("//aaa//bbbb\naaa\n"), "\naaa\n");
        assertEquals(JavascriptUtil.stripSingleLineComments("//aaa//bbbb\nxx //aaa\n"), "\nxx \n");
    }

    @Test
    public void testStripMultiLineComments()
    {
        assertEquals(JavascriptUtil.stripMultiLineComments("\n"), "\n");
        assertEquals(JavascriptUtil.stripMultiLineComments(null), null);

        assertEquals(JavascriptUtil.stripMultiLineComments("a\n"), "a\n");
        assertEquals(JavascriptUtil.stripMultiLineComments("/a\n"), "/a\n");
        assertEquals(JavascriptUtil.stripMultiLineComments("/a*a\n"), "/a*a\n");
        assertEquals(JavascriptUtil.stripMultiLineComments("/ */ *\n"), "/ */ *\n");

        assertEquals(JavascriptUtil.stripMultiLineComments("/**/\n"), " \n");
        assertEquals(JavascriptUtil.stripMultiLineComments("/***/\n"), " \n");
        assertEquals(JavascriptUtil.stripMultiLineComments("/*a*/\n"), " \n");
        assertEquals(JavascriptUtil.stripMultiLineComments("a/**/a\n"), "a a\n");
        assertEquals(JavascriptUtil.stripMultiLineComments("a/*\n*/a\n"), "a \na\n");
    }

    @Test
    public void testStripBlankLines()
    {
        assertEquals(JavascriptUtil.stripBlankLines("\n"), "\n");
        assertEquals(JavascriptUtil.stripBlankLines("\n\n"), "\n");
        assertEquals(JavascriptUtil.stripBlankLines(null), null);

        assertEquals(JavascriptUtil.stripBlankLines("a\n"), "a\n");
        assertEquals(JavascriptUtil.stripBlankLines("a\na\n"), "a\na\n");
        assertEquals(JavascriptUtil.stripBlankLines(" \n \n"), "\n");
        assertEquals(JavascriptUtil.stripBlankLines(" \n\t\n#\n"), "#\n");

        assertEquals(JavascriptUtil.stripBlankLines("\n\n\n"), "\n");
        assertEquals(JavascriptUtil.stripBlankLines("a\n\na\n"), "a\na\n");
    }

    @Test
    public void testStripNewlines()
    {
        assertEquals(JavascriptUtil.stripNewlines(""), "\n");
        assertEquals(JavascriptUtil.stripNewlines("\n"), " \n");
        assertEquals(JavascriptUtil.stripNewlines(null), null);

        assertEquals(JavascriptUtil.stripNewlines("a\nb\n"), "a b \n");
    }
}
