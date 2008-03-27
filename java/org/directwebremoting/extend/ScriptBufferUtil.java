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
import java.util.Iterator;
import java.util.List;

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptBuffer.StringWrapper;

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
     * @throws MarshallException If an error happens during parameter marshalling
     */
    public static String createOutput(ScriptBuffer buffer, ConverterManager converterManager) throws MarshallException
    {
        OutboundContext context = new OutboundContext();
        List ovs = new ArrayList();

        // First convert everything
        for (Iterator it = buffer.getParts().iterator(); it.hasNext();)
        {
            Object element = it.next();
            if (!(element instanceof StringWrapper))
            {
                OutboundVariable ov = converterManager.convertOutbound(element, context);
                ovs.add(ov);
            }
            else
            {
                ovs.add(element);
            }
        }

        StringBuffer output = new StringBuffer();

        // First we look for the declaration code
        for (Iterator it = ovs.iterator(); it.hasNext();)
        {
            Object element = it.next();
            if (element instanceof OutboundVariable)
            {
                OutboundVariable ov = (OutboundVariable) element;
                output.append(ov.getDeclareCode());
            }
        }

        // Then we look for the construction code
        for (Iterator it = ovs.iterator(); it.hasNext();)
        {
            Object element = it.next();
            if (element instanceof OutboundVariable)
            {
                OutboundVariable ov = (OutboundVariable) element;
                output.append(ov.getBuildCode());
            }
        }

        // Then we output everything else
        for (Iterator it = ovs.iterator(); it.hasNext();)
        {
            Object element = it.next();
            if (element instanceof StringWrapper)
            {
                StringWrapper str = (StringWrapper) element;
                output.append(str.toString());
            }
            else
            {
                OutboundVariable ov = (OutboundVariable) element;
                output.append(ov.getAssignCode());
            }
        }

        String exported = output.toString();
        return exported;
    }
}
