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
package org.directwebremoting.fsguide.visitors;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.dwrp.BaseCallMarshaller;
import org.directwebremoting.fsguide.Visitor;

/**
 * A debugging visitor that simply prints out the files and directories that it
 * visits.
 */
public class SystemOutVisitor implements Visitor
{
    /* (non-Javadoc)
     * @see com.barclaycard.fsguide.Visitor#visitFile(java.io.File)
     */
    public void visitFile(File file)
    {
        log.info("File: " + file.getAbsolutePath());
    }

    /* (non-Javadoc)
     * @see com.barclaycard.fsguide.Visitor#visitDirectory(java.io.File)
     */
    public boolean visitDirectory(File directory)
    {
        log.info("Directory: " + directory.getAbsolutePath());
        return true;
    }

    /**
     * The log stream
     */
    protected static final Log log = LogFactory.getLog(BaseCallMarshaller.class);
}
