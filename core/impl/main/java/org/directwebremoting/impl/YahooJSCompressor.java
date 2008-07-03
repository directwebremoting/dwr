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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.extend.Compressor;
import org.directwebremoting.util.Logger;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * JavaScript Compression Implementation using the YUI Compressor.
 * 
 * @author David Marginian [david at butterdev dot com]
 */
public class YahooJSCompressor implements Compressor
{
    /**
     * Constructor: YahooJSCompressor using default property values.
     */
    public YahooJSCompressor()
    {
        setCompressorParameters(null);
    }

    /**
     * Constructor: YahooJSCompressor using the property map passed in.
     * 
     * @param specifiedCompressorParameters
     */
    public YahooJSCompressor(Map<String, Object> specifiedCompressorParameters)
    {
        setCompressorParameters(specifiedCompressorParameters);
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.Compressor#compressJavaScript(java.lang.String)
     */
    public String compressJavaScript(String script) throws IOException
    {
        StringReader stringReader = new StringReader(script);
        JavaScriptCompressor yuiJavaScriptCompressor = new JavaScriptCompressor(stringReader, new YahooJSErrorReporter());
        StringWriter stringWriter = new StringWriter();
        yuiJavaScriptCompressor.compress(stringWriter, (Integer) compressorParameters.get(PARAMETER_LINEBREAK), (Boolean) compressorParameters.get(PARAMETER_MUNGE),
                (Boolean) compressorParameters.get(PARAMETER_VERBOSE), (Boolean) compressorParameters.get(PARAMETER_PRESERVE_ALL_SEMICOLONS),
                (Boolean) compressorParameters.get(PARAMETER_DISABLE_OPTIMIZATIONS));
        String compressedScript = stringWriter.toString();
        return compressedScript;
    }

    /**
     * Builds the compressor parameter map using the parameters specified or
     * the default parameters.  
     * 
     * @param configuredCompressorParameters - 
     *   Parameters specified by the applications configuration. We still need
     *   to validate these parameters and use defaults if all of the parameters 
     *   were not specified.
     */
    private void setCompressorParameters(Map<String, Object> configuredCompressorParameters)
    {
        String mungeString = null;
        String disableOptString = null;
        String preserveSemiString = null;
        String verboseString = null;
        String linebreakString = null;
        if (null != configuredCompressorParameters)
        {
            mungeString = (String) configuredCompressorParameters.get(PARAMETER_MUNGE);
            disableOptString = (String) configuredCompressorParameters.get(PARAMETER_DISABLE_OPTIMIZATIONS);
            preserveSemiString = (String) configuredCompressorParameters.get(PARAMETER_PRESERVE_ALL_SEMICOLONS);
            verboseString = (String) configuredCompressorParameters.get(PARAMETER_VERBOSE);
            linebreakString = (String) configuredCompressorParameters.get(PARAMETER_LINEBREAK);
        }
        // Place either the configured parameters or the default parameters into the compressorParameters Map
        compressorParameters.put(PARAMETER_MUNGE, (null != mungeString) ? Boolean.valueOf(mungeString) : DEFAULT_MUNGE);
        compressorParameters.put(PARAMETER_DISABLE_OPTIMIZATIONS, (null != disableOptString) ? Boolean.valueOf(disableOptString) : DEFAULT_DISABLE_OPTIMIZATIONS);
        compressorParameters.put(PARAMETER_PRESERVE_ALL_SEMICOLONS, (null != preserveSemiString) ? Boolean.valueOf(preserveSemiString) : DEFAULT_PRESERVE_ALL_SEMICOLONS);
        compressorParameters.put(PARAMETER_VERBOSE, (null != verboseString) ? Boolean.valueOf(verboseString) : DEFAULT_VERBOSE);
        compressorParameters.put(PARAMETER_LINEBREAK, (null != linebreakString) ? Integer.valueOf(linebreakString) : DEFAULT_LINEBREAK);
    }

    /**
     * ErrorReporter implementation required by the YUI Compressor compress.
     */
    protected static class YahooJSErrorReporter implements ErrorReporter
    {
        public void warning(String message, String sourceName, int line, String lineSource, int lineOffset)
        {
            if (line < 0)
            {
                log.warn(message);
            }
            else
            {
                log.error("\n" + line + ':' + lineOffset + ':' + message);
            }
        }

        public void error(String message, String sourceName, int line, String lineSource, int lineOffset)
        {
            if (line < 0)
            {
                log.error(message);
            }
            else
            {
                log.error(line + ':' + lineOffset + ':' + message);
            }
        }

        public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset)
        {
            error(message, sourceName, line, lineSource, lineOffset);
            return new EvaluatorException(message);
        }
    }

    protected static final Logger log = Logger.getLogger(YahooJSCompressor.class);

    private Map<String, Object> compressorParameters = new HashMap<String, Object>();

    // Fields representing the parameters that can be configured for this Compressor.    
    // Obfuscates the script
    private static final String PARAMETER_MUNGE = "munge";

    // Display informational messages and warnings.
    private static final String PARAMETER_VERBOSE = "verbose";

    // Some source control tools don't like it when files containing lines longer
    // than, say 8000 characters, are checked in. The linebreak option is used in
    // that case to split long lines after a specific column.
    private static final String PARAMETER_LINEBREAK = "linebreak";

    // Append a semi-colon at the end, even if unnecessary semi-colons are
    // supposed to be removed. This is especially useful when concatenating
    // several minified files (the absence of an ending semi-colon at the
    // end of one file may very likely cause a syntax error)
    private static final String PARAMETER_PRESERVE_ALL_SEMICOLONS = "preserveAllSemiColons";

    // Disable all the built-in micro optimizations.
    private static final String PARAMETER_DISABLE_OPTIMIZATIONS = "disableOptimizations";

    // Default values for the parameters that can be configured for this Compressor in case they
    // are not specified.
    private static final Boolean DEFAULT_MUNGE = Boolean.FALSE;

    private static final Boolean DEFAULT_VERBOSE = Boolean.FALSE;

    private static final Integer DEFAULT_LINEBREAK = 20000;

    private static final Boolean DEFAULT_PRESERVE_ALL_SEMICOLONS = Boolean.FALSE;

    private static final Boolean DEFAULT_DISABLE_OPTIMIZATIONS = Boolean.FALSE;
}
