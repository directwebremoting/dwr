package org.directwebremoting.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.directwebremoting.extend.DwrConstants;
import org.directwebremoting.servlet.UtilHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class YahooJSCompressorTest
{
    String scriptToCompress = null;

    @Before
    public void setUp()
    {
        StringBuffer scriptBuffer = new StringBuffer();
        InputStream scriptStream = UtilHandler.class.getResourceAsStream(DwrConstants.PACKAGE + "/util.js");
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
    public void testCompression() throws IOException
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
