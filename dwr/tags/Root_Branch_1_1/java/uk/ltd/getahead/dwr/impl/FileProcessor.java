package uk.ltd.getahead.dwr.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import uk.ltd.getahead.dwr.DWRServlet;
import uk.ltd.getahead.dwr.util.JavascriptUtil;

/**
 * A file servlet component that does some very limitted.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FileProcessor
{
    /**
     * Basically a file servlet component that does some <b>very limitted</b>
     * EL type processing on the file. See the source for the cheat.
     * @param resp The response channel
     * @param path The path to search for, process and output
     * @param mimeType The mime type to use for this output file
     * @throws IOException If writing to the output fails
     */
    protected void doFile(HttpServletResponse resp, String path, String mimeType) throws IOException
    {
        resp.setContentType(mimeType);

        String output = null;

        synchronized (scriptCache)
        {
            output = (String) scriptCache.get(path);
            if (output == null)
            {
                StringBuffer buffer = new StringBuffer();

                String resource = DWRServlet.PACKAGE + path;
                InputStream raw = getClass().getResourceAsStream(resource);
                if (raw == null)
                {
                    throw new IOException("Failed to find resource: " + resource); //$NON-NLS-1$
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(raw));
                while (true)
                {
                    String line = in.readLine();
                    if (line == null)
                    {
                        break;
                    }

                    buffer.append(line);
                    buffer.append('\n');
                }

                output = buffer.toString();

                if (mimeType.equals(HtmlConstants.MIME_JS) && scriptCompressed)
                {
                    output = jsutil.compress(output, compressionLevel);
                }

                scriptCache.put(path, output);
            }
        }

        PrintWriter out = resp.getWriter();
        out.println(output);
        out.flush();
    }

    /**
     * To what level do we compress scripts?
     * @param scriptCompressed The scriptCompressed to set.
     */
    public void setScriptCompressed(boolean scriptCompressed)
    {
        this.scriptCompressed = scriptCompressed;
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
    private int compressionLevel = JavascriptUtil.LEVEL_DEBUGGABLE;

    /**
     * Do we retain comments and unneeded spaces in Javascript code?
     */
    private boolean scriptCompressed = true;

    /**
     * We cache the script output for speed
     */
    private final Map scriptCache = new HashMap();

    /**
     * The means by which we strip comments
     */
    private JavascriptUtil jsutil = new JavascriptUtil();
}
