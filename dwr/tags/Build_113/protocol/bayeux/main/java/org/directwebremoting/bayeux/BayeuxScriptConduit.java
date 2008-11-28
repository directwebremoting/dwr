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
package org.directwebremoting.bayeux;

import java.io.IOException;

import org.directwebremoting.ConversionException;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.ScriptBufferUtil;
import org.directwebremoting.extend.ScriptConduit;

/**
 * @author Greg Wilkins [gregw at webtide dot com]
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class BayeuxScriptConduit extends ScriptConduit
{
    /**
     * 
     */
    public BayeuxScriptConduit(ConverterManager converterManager, boolean jsonOutput)
    {
        super(RANK_FAST, true);

        this.converterManager = converterManager;
        this.jsonOutput = jsonOutput;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#addScript(org.directwebremoting.ScriptBuffer)
     */
    @Override
    public boolean addScript(ScriptBuffer script) throws IOException, ConversionException
    {
        builder.append(ScriptBufferUtil.createOutput(script, converterManager, jsonOutput));
        return true;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.extend.ScriptConduit#toString()
     */
    @Override
    public String toString()
    {
        return builder.toString();
    }

    /**
     * Are we outputting in JSON mode?
     */
    private boolean jsonOutput = false;

    private ConverterManager converterManager;

    private StringBuilder builder = new StringBuilder();
}
