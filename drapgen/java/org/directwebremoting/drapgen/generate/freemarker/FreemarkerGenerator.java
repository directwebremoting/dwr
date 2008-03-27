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
package org.directwebremoting.drapgen.generate.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.directwebremoting.drapgen.ast.Project;
import org.directwebremoting.drapgen.ast.Type;
import org.directwebremoting.drapgen.generate.Generator;
import org.directwebremoting.util.LocalUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class FreemarkerGenerator implements Generator
{
    /* (non-Javadoc)
     * @see org.directwebremoting.drapgen.generate.Generator#setGeneratedBase(java.lang.String)
     */
    public void setGeneratedBase(File generatedBase)
    {
        this.generatedBase = generatedBase;
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.drapgen.generate.Generator#generate(org.directwebremoting.drapgen.ast.Project)
     */
    public void generate(Project project) throws IOException
    {
        Configuration cfg = new Configuration();
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setClassForTemplateLoading(this.getClass(), "");

        for (Type type : project.getTypes())
        {
            File directory = new File(generatedBase, type.getPackageName().replace('.', '/'));
            directory.mkdirs();

            Writer out = null;
            try
            {
                out = new FileWriter(new File(directory, type.getName() + ".java"));
                Template template = cfg.getTemplate("generate.ftl");

                Map<String, Object> root = new HashMap<String, Object>();
                root.put("type", type);
                root.put("project", project);
                template.process(root, out);
            }
            catch (TemplateException ex)
            {
                throw new RuntimeException(ex);
            }
            finally
            {
                LocalUtil.close(out);
            }
        }
    }

    private File generatedBase;
}
