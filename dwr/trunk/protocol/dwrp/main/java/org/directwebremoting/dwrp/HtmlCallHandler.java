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
package org.directwebremoting.dwrp;

import java.io.PrintWriter;

import org.directwebremoting.extend.ScriptConduit;

/**
 * A Handler standard DWR calls whose replies are HTML wrapped.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class HtmlCallHandler extends BaseCallHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.BaseCallHandler#createScriptConduit(java.io.PrintWriter, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    protected ScriptConduit createScriptConduit(PrintWriter out, String instanceId, String batchId, String documentDomain)
    {
        return new HtmlScriptConduit(out, instanceId, batchId, documentDomain);
    }
}
