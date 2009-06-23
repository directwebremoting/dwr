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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.servlet.EngineHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class YahooJSCompressorTest
{
    String scriptToCompress = null;

    @Before
    public void setUp()
    {
        StringBuffer scriptBuffer = new StringBuffer();
        InputStream scriptStream = EngineHandler.class.getResourceAsStream(DwrConstants.PACKAGE + "/engine.js");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(scriptStream));
        try
        {
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                scriptBuffer.append(line + "\n");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            try
            {
                scriptStream.close();
                bufferedReader.close();
            }
            catch (Exception e1)
            {
            }
        }
        scriptToCompress = scriptBuffer.toString();
    }

    @After
    public void tearDown()
    {
        scriptToCompress = null;
    }

    @Test
    public void testCompression() throws Exception
    {
        YahooJSCompressor yahooJsCompressor = new YahooJSCompressor(null);
        String compressedScript = yahooJsCompressor.compressJavaScript(scriptToCompress);
        try
        {
            BufferedWriter out = new BufferedWriter(new StringWriter());
            out.write(compressedScript);
            out.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
