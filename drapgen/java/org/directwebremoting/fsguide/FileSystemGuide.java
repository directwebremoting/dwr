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
package org.directwebremoting.fsguide;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FileSystemGuide
{
    /**
     * @param root
     */
    public FileSystemGuide(File root)
    {
        this.root = root;
    }

    /**
     * @param visitor
     */
    public void visit(Visitor visitor)
    {
        visitChild(root, visitor);
    }

    /**
     * @param directory
     * @param visitor
     */
    private void visitDirectory(File directory, Visitor visitor)
    {
        File[] files = directory.listFiles();

        // Sort the list of files
        Arrays.sort(files, new Comparator<File>()
        {
            public int compare(File o1, File o2)
            {
                return o1.getName().compareTo(o2.getName());
            }
        });

        // Visit all the files and directories
        for (File child : files)
        {
            visitChild(child, visitor);
        }
    }

    /**
     * @param child
     * @param visitor
     */
    private void visitChild(File child, Visitor visitor)
    {
        if (child.isDirectory())
        {
            if (visitor.visitDirectory(child))
            {
                visitDirectory(child, visitor);
            }
        }
        else
        {
            visitor.visitFile(child);
        }
    }

    private File root;
}
