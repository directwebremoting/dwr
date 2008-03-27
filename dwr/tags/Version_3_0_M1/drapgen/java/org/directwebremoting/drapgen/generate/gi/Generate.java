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
package org.directwebremoting.drapgen.generate.gi;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.directwebremoting.drapgen.generate.xslt.ExtensionFunctions;
import org.directwebremoting.drapgen.loader.gi.GiLoader;
import org.directwebremoting.fsguide.FileSystemGuide;
import org.directwebremoting.fsguide.Visitor;
import org.directwebremoting.util.Logger;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Generate
{
    private static final String XML_BASE = "/Applications/TIBCO/tibco-gi-3.5-pro-src/dist/gi/api/xml/";
    private static final String GENERATED_BASE = "generated/gi/java/org/directwebremoting/proxy/";
    private static final String DOM_BASE = "generated/gi/dom/";
    private static final String TEMPLATES_BASE = "etc/drapgen/templates/";
    private static final String DEFAULT_TEMPLATE = "default.xslt";
    private static final String PREPROCESSOR = "etc/drapgen/preprocess/default.xslt";

    public static void main(String[] args) throws Exception
    {
        new Generate().generate();
    }

    public void generate() throws Exception
    {
        final GiProject registry = new GiProject();

        // Create a list of all the classes we need to generate
        log.info("Searching for XML files.");
        FileSystemGuide guide = new FileSystemGuide(new File(XML_BASE));
        guide.visit(new Visitor()
        {
            public void visitFile(File file)
            {
                if (file.getAbsolutePath().endsWith(".xml"))
                {
                    GiType code = new GiType(Generate.XML_BASE, file);
                    registry.add(code);
                }
                else
                {
                    log.info("Skipping: " + file.getAbsolutePath());
                }
            }

            public boolean visitDirectory(File directory)
            {
                return true;
            }
        });
        ExtensionFunctions.setJsClassloader(registry);

        // Clone the functions with multiple input parameter types for overloading
        log.info("Cloning for overloading.");
        for (GiType code : registry.getClasses())
        {
            code.cloneForOverloading(PREPROCESSOR);
        }
        
        // Work out which classes are super-classes and mixins
        log.info("Calculating super classes.");
        for (GiType code : registry.getClasses())
        {
            List<String> parents = code.getSuperClasses();
            for (String parent : parents)
            {
                registry.getClassByName(parent).setSuperClass(true);
            }
        }

        // Copying mixin functions because Java does not do MI
        log.info("Copying mixin functions.");
        ExecutorService exec = Executors.newFixedThreadPool(2);
        for (final GiType code : registry.getClasses())
        {
            exec.execute(new Runnable()
            {
                public void run()
                {
                    // Search the XML for lines like this from Button.xml:
                    //   <implements direct="1" id="implements:0" loaded="1" name="jsx3.gui.Form"/>
                    List<GiMethod> functions = code.getMixinFunctions();
                    for (GiMethod function : functions)
                    {
                        // replace the method element with the corresponding element from Form.
                        if (!"jsx3.lang.Object".equals(function.getDeclarationClassName()))
                        {
                            GiType implementingClass = registry.getClassByName(function.getDeclarationClassName());
                            GiMethod node = implementingClass.getImplementationDeclaration(function.getName());
                            if (node == null)
                            {
                                log.warn("- No implementation of: " + function.getDeclarationClassName() + "." + function.getName() + "() refered to by " + code.getClassName());
                            }
                            else
                            {
                                // log.info("- Promoting: " + mixinFunction.left + "." + mixinFunction.right + "() into " + code.getClassName());
                                code.replaceImplementation(node, function);
                            }
                        }
                    }
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        // Remove functions implemented by parent
        log.info("Trimming methods with identical implementation in parent.");
        exec = Executors.newFixedThreadPool(2);
        for (final GiType code : registry.getClasses())
        {
            exec.execute(new Runnable()
            {
                public void run()
                {
                    code.trimDuplicateMethods(registry);
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        // Remove documentation classes
        log.info("Pruning documentation classes.");
        for (Iterator<GiType> it = registry.getClasses().iterator(); it.hasNext();)
        {
            GiType code = it.next();
            if (code.getClassName().equals("index") ||
                code.getClassName().equals("inheritance") ||
                code.getClassName().equals("package-summary"))
            {
                it.remove();
            }
        }

        // Write the DOM out to disk
        log.info("Serializing " + registry.getClasses().size() + " classes.");
        registry.save(DOM_BASE);
        System.exit(0);

        // Transform them all
        log.info("Transforming " + registry.getClasses().size() + " classes.");
        for (GiType code : registry.getClasses())
        {
            code.transform(TEMPLATES_BASE, DEFAULT_TEMPLATE);
        }

        // Merge Inner Classes
        log.info("Merging Inner Classes");
        for (Iterator<GiType> it = registry.getClasses().iterator(); it.hasNext();)
        {
            GiType code = it.next();
            String parentName = "";

            int lastDot = code.getClassName().lastIndexOf('.');
            if (lastDot > 0)
            {
                parentName = code.getClassName().substring(0, lastDot);
            }

            GiType parent = registry.getClassByName(parentName);
            if (parent != null)
            {
                it.remove();
                parent.absorbClass(code);
            }
        }

        // Write them to disk
        log.info("Writing " + registry.getClasses().size() + " classes.");
        for (GiType code : registry.getClasses())
        {
            code.writeCode(GENERATED_BASE);
        }
    }

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(GiLoader.class);
}
