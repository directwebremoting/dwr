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

/**
 * A special case of BeanConverter that doesn't convert StackTraces
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ExceptionConverter extends BeanConverter
{
    /* (non-Javadoc)
     * @see org.directwebremoting.convert.BeanConverter#isAllowed(java.lang.String)
     */
    protected boolean isAllowed(String property)
    {
        // We never send stack traces to the client
        if ("stackTrace".equals(property)) //$NON-NLS-1$
        {
            return false;
        }

        return super.isAllowed(property);
    }
}
