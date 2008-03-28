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
package org.directwebremoting.convert;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.directwebremoting.OutboundContext;
import org.directwebremoting.OutboundVariable;
import org.directwebremoting.util.JavascriptUtil;
import org.directwebremoting.util.LocalUtil;

/**
 * A collection of utilities that are useful to more than one Converter
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ConverterUtil
{
    /**
     * Generate an array declaration for a list of Outbound variables
     * @param ov The OutboundVariable to declare
     * @param ovs The list of contents of this array
     */
    public static void addListInit(OutboundVariable ov, List ovs)
    {
        String varname = ov.getAssignCode();
        StringBuffer buffer = new StringBuffer();

        String init = getInitCodes(ovs);

        if (init.length() == 0)
        {
            // Declare ourselves so recurrsion works
            buffer.append("var "); //$NON-NLS-1$
            buffer.append(varname);
            buffer.append("=["); //$NON-NLS-1$

            // Declare the non-recursive parts to the list
            boolean first = true;
            for (int i = 0; i < ovs.size(); i++)
            {
                OutboundVariable nested = (OutboundVariable) ovs.get(i);

                if (!first)
                {
                    buffer.append(',');
                }

                if (nested.getAssignCode().equals(varname))
                {
                    // We'll fill it in later
                    buffer.append("null"); //$NON-NLS-1$
                }
                else
                {
                    ovs.set(i, null);
                    buffer.append(nested.getAssignCode());
                }

                first = false;
            }
            buffer.append("];"); //$NON-NLS-1$
        }
        else
        {
            // Declare ourselves so recurrsion works
            buffer.append("var "); //$NON-NLS-1$
            buffer.append(varname);
            buffer.append("=[];"); //$NON-NLS-1$

            // First we output all the init code
            buffer.append(init);

            // Declare the non-recursive parts to the list
            buffer.append(varname);
            buffer.append('=');
            buffer.append(varname);
            buffer.append(".concat(["); //$NON-NLS-1$
            boolean first = true;
            for (int i = 0; i < ovs.size(); i++)
            {
                OutboundVariable nested = (OutboundVariable) ovs.get(i);

                if (!first)
                {
                    buffer.append(',');
                }

                if (nested.getAssignCode().equals(varname))
                {
                    // We'll fill it in later
                    buffer.append("null"); //$NON-NLS-1$
                }
                else
                {
                    ovs.set(i, null);
                    buffer.append(nested.getAssignCode());
                }

                first = false;
            }
            buffer.append("]);"); //$NON-NLS-1$
        }

        // And now the recursive parts
        for (int i = 0; i < ovs.size(); i++)
        {
            OutboundVariable nested = (OutboundVariable) ovs.get(i);

            if (nested != null)
            {
                buffer.append(varname);
                buffer.append('[');
                buffer.append(i);
                buffer.append("]="); //$NON-NLS-1$
                buffer.append(nested.getAssignCode());
                buffer.append(';');
            }
        }
        buffer.append("\r\n"); //$NON-NLS-1$

        ov.setInitCode(buffer.toString());
    }

    /**
     * Generate an map declaration for a map of Outbound variables
     * @param ov The OutboundVariable to declare
     * @param ovs The map of the converted contents
     */
    public static void addMapInit(OutboundVariable ov, Map ovs)
    {
        String varname = ov.getAssignCode();
        StringBuffer buffer = new StringBuffer();

        String init = getInitCodes(ovs.values());

        // If there is no init code, there is no recursion so we can go into
        // compact JSON mode
        if (init.length() == 0)
        {
            // First loop through is for the stuff we can embed
            buffer.append("var "); //$NON-NLS-1$
            buffer.append(varname);
            buffer.append("={"); //$NON-NLS-1$

            // And now declare our stuff
            boolean first = true;
            for (Iterator it = ovs.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                String name = (String) entry.getKey();
                OutboundVariable nested = (OutboundVariable) entry.getValue();

                String assignCode = nested.getAssignCode();

                // The compact JSON style syntax is only any good for simple names
                // and when we are not recursive
                if (LocalUtil.isSimpleName(name) && !assignCode.equals(varname))
                {
                    if (!first)
                    {
                        buffer.append(',');
                    }

                    buffer.append(name);
                    buffer.append(':');
                    buffer.append(assignCode);

                    // we don't need to do this one the hard way
                    it.remove();
                    first = false;
                }
            }
            buffer.append("};"); //$NON-NLS-1$
        }
        else
        {
            // Declare ourselves so recursion works
            buffer.append("var "); //$NON-NLS-1$
            buffer.append(varname);
            buffer.append("={};"); //$NON-NLS-1$

            buffer.append(init);

            // And now declare our stuff
            for (Iterator it = ovs.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                String name = (String) entry.getKey();
                OutboundVariable nested = (OutboundVariable) entry.getValue();

                String assignCode = nested.getAssignCode();

                // The semi-compact syntax is only any good for simple names
                if (LocalUtil.isSimpleName(name) && !assignCode.equals(varname))
                {
                    buffer.append(varname);
                    buffer.append('.');
                    buffer.append(name);
                    buffer.append('=');
                    buffer.append(nested.getAssignCode());
                    buffer.append(';');

                    it.remove();
                }
            }
        }

        // The next loop through is for everything that will not embed
        for (Iterator it = ovs.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            OutboundVariable nested = (OutboundVariable) entry.getValue();

            buffer.append(varname);
            buffer.append("['"); //$NON-NLS-1$
            buffer.append(name);
            buffer.append("']="); //$NON-NLS-1$
            buffer.append(nested.getAssignCode());
            buffer.append(';');
        }

        buffer.append("\r\n"); //$NON-NLS-1$

        ov.setInitCode(buffer.toString());
    }

    /**
     * Grab all the init codes together
     * @param ovs The set of variables to marshall
     * @return An init string
     */
    private static String getInitCodes(Collection ovs)
    {
        StringBuffer init = new StringBuffer();

        // Make sure the nested things are declared
        for (Iterator it = ovs.iterator(); it.hasNext();)
        {
            OutboundVariable nested = (OutboundVariable) it.next();
            init.append(nested.getInitCode());
        }

        return init.toString();
    }

    /**
     * Generate a string declaration from a Java string. This includes deciding
     * to inline the string if it is too short, or splitting it up if it is too
     * long.
     * @param output The Java string to convert
     * @param outctx The conversion context.
     * @return The converted OutboundVariable
     */
    public static OutboundVariable addStringInit(String output, OutboundContext outctx)
    {
        String escaped = JavascriptUtil.escapeJavaScript(output);

        // For short strings inline them
        if (escaped.length() < INLINE_LENGTH)
        {
            return new OutboundVariable("", '\"' + escaped + '\"'); //$NON-NLS-1$
        }

        // For medium length strings do it in a separate variable
        if (escaped.length() < WRAP_LENGTH)
        {
            OutboundVariable ov = outctx.createOutboundVariable(output);
            String varname = ov.getAssignCode();
            ov.setInitCode("var " + varname + "=\"" + escaped + "\";\r\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return ov;
        }

        // For very long strings chop up the init into several parts
        OutboundVariable ov = outctx.createOutboundVariable(output);
        String varname = ov.getAssignCode();

        StringBuffer initBody = new StringBuffer();
        StringBuffer initEnd = new StringBuffer();

        initEnd.append("var "); //$NON-NLS-1$
        initEnd.append(varname);
        initEnd.append('=');

        int start = 0;
        while (start < escaped.length())
        {
            String tempvar = outctx.getNextVariableName();

            int end = start + WRAP_LENGTH;
            if (end >= escaped.length())
            {
                end = escaped.length();
            }

            // If the last char is a \ then there is a chance of breaking
            // an escape so we increment until the last char is not a \
            // There is a potential bug here where the input string contains
            // only escaped slashes (\\) in which case we will end up not
            // breaking the string. Since this whole thing is a workaround
            // and the solution is complex we are going to pass on it now.
            while (end < escaped.length() && escaped.charAt(end - 1) == '\\')
            {
                end++;
            }

            // If there is a '\' followed by a 'u' in the last 4 chars then
            // we've probably broken a unicode escape
            if (escaped.charAt(end - 5) == '\\' && escaped.charAt(end - 4) == 'u')
            {
                end += 1;
            }
            if (escaped.charAt(end - 4) == '\\' && escaped.charAt(end - 3) == 'u')
            {
                end += 2;
            }
            if (escaped.charAt(end - 3) == '\\' && escaped.charAt(end - 2) == 'u')
            {
                end += 3;
            }
            if (escaped.charAt(end - 2) == '\\' && escaped.charAt(end - 1) == 'u')
            {
                end += 4;
            }

            initBody.append("var "); //$NON-NLS-1$
            initBody.append(tempvar);
            initBody.append("=\""); //$NON-NLS-1$
            initBody.append(escaped.substring(start, end));
            initBody.append("\";\r\n"); //$NON-NLS-1$

            initEnd.append(tempvar);
            if (end < escaped.length())
            {
                initEnd.append('+');
            }

            start = end;
        }

        initEnd.append(";\r\n"); //$NON-NLS-1$
        initBody.append(initEnd.toString());

        ov.setInitCode(initBody.toString());
        return ov;
    }

    /**
     * The length at which we stop inlining strings
     */
    private static final int INLINE_LENGTH = 20;

    /**
     * Strings longer than this are chopped up into smaller strings
     */
    private static final int WRAP_LENGTH = 256;
}
