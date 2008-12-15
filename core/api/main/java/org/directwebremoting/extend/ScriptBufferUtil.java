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
package org.directwebremoting.extend;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.util.Loggers;

/**
 * A simple utility class to extract a {@link String} from a {@link ScriptBuffer}.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ScriptBufferUtil
{
    /**
     * Ensure we can't be created
     */
    private ScriptBufferUtil()
    {
    }

    /**
     * Return a string ready for output.
     * @param buffer The source of the script data
     * @param converterManager How we convert script variable to Javascript
     * @return Some Javascript to be eval()ed by a browser.
     * @throws ConversionException If an error happens during parameter marshalling
     */
    public static String createOutput(ScriptBuffer buffer, ConverterManager converterManager) throws ConversionException
    {
        return createOutput(buffer, converterManager, false);
    }

    /**
     * Return a string ready for output.
     * @param script The source of the script data
     * @param converterManager How we convert script variable to Javascript
     * @param jsonOutput Are we doing strict JSON output?
     * @return Some Javascript to be eval()ed by a browser.
     * @throws ConversionException If an error happens during parameter marshalling
     */
    public static String createOutput(ScriptBuffer script, ConverterManager converterManager, boolean jsonOutput) throws ConversionException
    {
        OutboundContext context = new OutboundContext(jsonOutput);
        List<OutboundVariable> scriptParts = new ArrayList<OutboundVariable>();
        boolean outboundError = false;

        // First convert everything into OutboundVariables
        for (Object part : script.getParts())
        {
            OutboundVariable ov = converterManager.convertOutbound(part, context);
            if (ov instanceof ErrorOutboundVariable)
            {
                outboundError = true;
            }
            scriptParts.add(ov);
        }

        StringBuilder buffer = new StringBuilder();

        // First we look for the declaration code
        for (OutboundVariable ov : scriptParts)
        {
            buffer.append(ov.getDeclareCode());
        }

        // Then we look for the construction code
        for (OutboundVariable ov : scriptParts)
        {
            buffer.append(ov.getBuildCode());
        }

        // Then we output everything else
        for (OutboundVariable ov : scriptParts)
        {
            String assignCode = ov.getAssignCode();
            if (assignCode == null)
            {
                throw new NullPointerException();
            }
            buffer.append(assignCode);
        }

        // Real JSON must be wrapped in { }
        String output = buffer.toString();
        if (jsonOutput && !output.startsWith("{"))
        {
            if (outboundError)
            {
                output = output.replaceFirst("null", "{}");
                output = "{\"error\":" + output + "}";
            }
            else
            {
                output = "{ \"reply\":" + output + "}";
            }
        }

        Loggers.SCRIPTS.debug(output);
        return output;
    }
}
