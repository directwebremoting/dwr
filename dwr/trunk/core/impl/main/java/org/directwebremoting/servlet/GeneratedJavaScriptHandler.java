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
package org.directwebremoting.servlet;

import org.directwebremoting.extend.Remoter;
import org.directwebremoting.util.LocalUtil;

/**
 * A base class for all generated JavaScript files
 * @author Mike Wilson [mikewse at hotmail dot com]
 */
public abstract class GeneratedJavaScriptHandler extends JavaScriptHandler
{
    /* (non-Javadoc)
     * @see org.directwebremoting.servlet.CachingHandler#getLastModifiedTime()
     */
    @Override
    protected long getLastModifiedTime()
    {
        return LocalUtil.getSystemClassloadTime();
    }

    /**
     * Setter for the remoter
     * @param remoter The new remoter
     */
    public void setRemoter(Remoter remoter)
    {
        this.remoter = remoter;
    }

    /**
     * The bean to execute remote requests and generate interfaces
     */
    protected Remoter remoter = null;
}
