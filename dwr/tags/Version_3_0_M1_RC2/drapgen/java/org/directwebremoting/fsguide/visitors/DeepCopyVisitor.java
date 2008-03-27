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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.fsguide.Visitor;
import org.directwebremoting.util.LocalUtil;

/**
 * 
 */
public class DeepCopyVisitor implements Visitor
{
    /**
     * 
     */
    public DeepCopyVisitor(File origRoot, File destRoot)
    {
        this.origRoot = origRoot;
        this.destRoot = destRoot;
    }

    /* (non-Javadoc)
     * @see com.barclaycard.fsguide.Visitor#visitFile(java.io.File)
     */
    public void visitFile(File inputFile)
    {
        File destFile = getDestFile(inputFile);

        BufferedReader in = null;
        PrintWriter out = null;

        try
        {
            in = new BufferedReader(new FileReader(inputFile));
            out = new PrintWriter(new FileWriter(destFile));

            while (true)
            {
                String line = in.readLine();

                if (line == null)
                {
                    break;
                }

                out.println(map(line));
            }

            log.info("Writing to file: " + destFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
        finally
        {
            LocalUtil.close(in);
            LocalUtil.close(out);
        }
    }

    /* (non-Javadoc)
     * @see com.barclaycard.fsguide.Visitor#visitDirectory(java.io.File)
     */
    public boolean visitDirectory(File file)
    {
        File destFile = getDestFile(file);

        destFile.mkdirs();
        log.info("Created dir:     " + destFile.getAbsolutePath());

        return true;
    }

    /**
     * 
     */
    private File getDestFile(File file)
    {
        String currentPath = file.getAbsolutePath();
        String origPath = origRoot.getAbsolutePath();

        if (!currentPath.startsWith(origPath))
        {
            throw new IllegalArgumentException("'" + currentPath + "' is not rooted in '" + origPath + "'");
        }

        String suffix = currentPath.substring(origPath.length());
        String prefix = destRoot.getAbsolutePath();
        String full = prefix + suffix;
        full = map(full);

        File destFile = new File(full);
        return destFile;
    }

    /**
     * 
     */
    protected String map(String name)
    {
        return name;
    }

    private File origRoot;

    private File destRoot;

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(DeepCopyVisitor.class);
}
