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

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.util.LocalUtil;

/**
 * An OutboundVariable that can not be recursive.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ErrorOutboundVariable extends NonNestedOutboundVariable
{
    /**
     * Default ctor that leaves blank members
     * @param errorMessage Some message for the developer to see.
     */
    public ErrorOutboundVariable(String errorMessage)
    {
        super(sanitizeErrorMessage(errorMessage));
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.OutboundVariable#getAssignCode()
     */
    public static String sanitizeErrorMessage(String errorMessage)
    {
        boolean debug = false;

        Object debugVal = WebContextFactory.get().getContainer().getBean("debug");
        if (debugVal != null)
        {
            debug = LocalUtil.simpleConvert(debugVal.toString(), Boolean.class);
        }

        if (debug)
        {
            return "null /* " + errorMessage.replace("*/", "* /") + " */";
        }
        else
        {
            return "null";
        }
    }
}
