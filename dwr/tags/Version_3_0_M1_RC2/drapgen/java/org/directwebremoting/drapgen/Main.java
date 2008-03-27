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
package org.directwebremoting.drapgen;

import java.io.File;

import org.directwebremoting.drapgen.ast.Project;
import org.directwebremoting.drapgen.generate.Generator;
import org.directwebremoting.drapgen.generate.freemarker.FreemarkerGenerator;
import org.directwebremoting.drapgen.loader.Loader;
import org.directwebremoting.drapgen.loader.gi.GiLoader;
import org.directwebremoting.util.Logger;
import org.directwebremoting.util.LoggingOutput;
import org.directwebremoting.util.SystemOutLoggingOutput;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Main
{
    /**
     * 
     */
    public static void main(String[] args) throws Exception
    {
        Logger.setDefaultImplementation(SystemOutLoggingOutput.class);
        SystemOutLoggingOutput.setLevel(LoggingOutput.LEVEL_INFO);

        Loader loader = new GiLoader(new File("/Users/joe/Workspace/gi/dist/gi/api/xml/"));
        Project project = new Project();
        loader.loadToProject(project);

        project.save(new File("generated/gi/dom/"));

        Generator generator = new FreemarkerGenerator();
        generator.setGeneratedBase(new File("generated/gi/java/"));
        generator.generate(project);
    }
}
