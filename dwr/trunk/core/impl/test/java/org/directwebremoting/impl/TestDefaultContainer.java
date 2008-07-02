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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.impl.DefaultContainer;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class TestDefaultContainer extends DefaultContainer
{
    /* (non-Javadoc)
     * @see org.directwebremoting.impl.DefaultContainer#setupFinished()
     */
    @Override
    public void setupFinished()
    {
        log.debug("TestDefaultContainer.setupFinished()");
        super.setupFinished();
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(TestDefaultContainer.class);
}
