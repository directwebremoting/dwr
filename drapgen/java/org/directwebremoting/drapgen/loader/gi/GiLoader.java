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
package org.directwebremoting.drapgen.loader.gi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.XPathException;

import org.directwebremoting.drapgen.ast.Constructor;
import org.directwebremoting.drapgen.ast.Field;
import org.directwebremoting.drapgen.ast.Method;
import org.directwebremoting.drapgen.ast.Parameter;
import org.directwebremoting.drapgen.ast.Project;
import org.directwebremoting.drapgen.ast.Type;
import org.directwebremoting.drapgen.loader.Loader;
import org.directwebremoting.drapgen.util.XomHelper;
import org.directwebremoting.drapgen.util.XomHelper.ElementBlock;
import org.directwebremoting.fsguide.FileSystemGuide;
import org.directwebremoting.fsguide.Visitor;
import org.directwebremoting.util.Logger;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class GiLoader implements Loader
{
    /**
     * @param source Where we read the API XML source from
     */
    public GiLoader(File source) throws Exception
    {
        log.info("Loading GI API from: " + source.getCanonicalPath());
        load(source);
    }

    /**
     * @param source Where we read the API XML source from
     */
    protected void load(File source) throws Exception
    {
        loadGiClasses(source);

        applyPreProcessingHacks();
        copyMixinFunctions();
        trimDuplicateMethods();
        applyPostProcessingHacks();
    }

    /**
     * 
     */
    protected void applyPreProcessingHacks()
    {
        log.warn("jsx3.gui.Alerts.alert(): Type of parameter 'objParams' should be 'String'");
        changeAttributeValue("jsx3.gui.Alerts", "/interface/method[@name='alert']/param[@name='objParams']/type", "name", "String");

        log.warn("jsx3.gui.Alerts.prompt(): Type of parameter 'objParams' should be 'String'");
        changeAttributeValue("jsx3.gui.Alerts", "/interface/method[@name='prompt']/param[@name='objParams']/type", "name", "String");

        log.warn("jsx3.gui.Alerts.confirm(): Type of parameter 'objParams' should be 'String'");
        changeAttributeValue("jsx3.gui.Alerts", "/interface/method[@name='confirm']/param[@name='objParams']/type", "name", "String");

        log.warn("jsx3.chart.PlotChart.DEFAULT_MAX_POINT_RADIUS: Type of number can't be 'String' assuming 'int'");
        changeAttributeValue("jsx3.chart.PlotChart", "/class/field[@name='DEFAULT_MAX_POINT_RADIUS']/type", "name", "int");

        log.warn("jsx3.gui.Matrix.MINIMUM_COLUMN_WIDTH: Type of number can't be 'String' assuming 'int'");
        changeAttributeValue("jsx3.gui.Matrix", "/class/field[@name='MINIMUM_COLUMN_WIDTH']/type", "name", "int");
    }

    /**
     * 
     */
    protected void changeAttributeValue(String typename, String elementXPath, final String attributeName, final String newValue)
    {
        Document doc = types.get(typename);
        int changes = XomHelper.query(doc, elementXPath, new ElementBlock()
        {
            public void use(Element typeElement)
            {
                typeElement.addAttribute(new Attribute(attributeName, newValue));
            }
        });

        if (changes == 0)
        {
            log.warn("** Changing " + elementXPath + " from " + typename + " didn't result in any changes");
        }
    }

    /**
     * 
     */
    protected void applyPostProcessingHacks()
    {
        log.warn("jsx3.gui.TextBox.OVERFLOWSCROLL: Field is overridden from jsx3.gui.Block (with a different type), is this safe?");
        removeElement("jsx3.gui.TextBox", "/class/field[@name='OVERFLOWSCROLL']");

        log.warn("jsx3.gui.Block.OVERFLOWSCROLL: Field is overridden in jsx3.gui.TextBox (with a different type), is this safe?");
        removeElement("jsx3.gui.Block", "/class/field[@name='OVERFLOWSCROLL']");

        log.warn("jsx3.gui.Block.DEFAULTCLASSNAME: Field is overridden in several places, is this safe?");
        removeElement("jsx3.gui.Block", "/class/field[@name='DEFAULTCLASSNAME']");

        log.warn("jsx3.gui.Block.DEFAULTFONTSIZE: Field is overridden in jsx3.gui.WindowBar, is this safe?");
        removeElement("jsx3.gui.Block", "/class/field[@name='DEFAULTFONTSIZE']");

        log.warn("jsx3.chart.Chart.DEFAULT_FILLS: Ignoring, value has been trimmed to 1st line");
        removeElement("jsx3.chart.Chart", "/class/field[@name='DEFAULT_FILLS']");

        log.warn("jsx3.app.Model.META_FIELDS: Ignoring, value has been trimmed to 1st line");
        removeElement("jsx3.app.Model", "/class/field[@name='META_FIELDS']");

        log.warn("jsx3.gui.Sound.QUICKTIME: Ignoring, value has been trimmed to 1st line");
        removeElement("jsx3.gui.Sound", "/class/field[@name='QUICKTIME']");

        log.warn("jsx3.gui.Table.DEFAULT_CELL_VALUE_TEMPLATE: Ignoring, value has been trimmed to 1st line");
        removeElement("jsx3.gui.Table", "/class/field[@name='DEFAULT_CELL_VALUE_TEMPLATE']");

        // We don't warn for these because Drapgen is at fault
        removeElement("jsx3.chart.PointRenderer", "/interface/field[@name='CIRCLE']");
        removeElement("jsx3.chart.PointRenderer", "/interface/field[@name='CROSS']");
        removeElement("jsx3.chart.PointRenderer", "/interface/field[@name='DIAMOND']");
        removeElement("jsx3.chart.PointRenderer", "/interface/field[@name='BOX']");
        removeElement("jsx3.chart.PointRenderer", "/interface/field[@name='TRIANGLE']");

        log.warn("jsx3.gui.Stack.BORDER: ';' in value triggers end of line trimming");
        removeElement("jsx3.gui.Stack", "/class/field[@name='BORDER']");

        log.warn("jsx3.gui.WindowBar.DEFAULTBORDER: ';' in value triggers end of line trimming");
        removeElement("jsx3.gui.WindowBar", "/class/field[@name='DEFAULTBORDER']");

        log.warn("jsx3.gui.WindowBar.DEFAULTBORDERCAPTION: ';' in value triggers end of line trimming");
        removeElement("jsx3.gui.WindowBar", "/class/field[@name='DEFAULTBORDERCAPTION']");
    }

    /**
     * Utility to remove (an) element(s) by xpath selector from a type
     */
    protected void removeElement(String typename, String xpath)
    {
        Document doc = types.get(typename);
        int changes = XomHelper.query(doc, xpath, new ElementBlock()
        {
            public void use(Element element)
            {
                element.getParent().removeChild(element);
            }
        });

        if (changes == 0)
        {
            log.warn("** Removing " + xpath + " from " + typename + " didn't result in any changes");
        }
    }

    /**
     * Load the GI XML files into a map of loaded XOM documents
     * @param directory The source of GI XML files
     */
    protected void loadGiClasses(final File directory)
    {
        types = new HashMap<String, Document>();

        // Create a list of all the classes we need to generate
        FileSystemGuide guide = new FileSystemGuide(directory);
        guide.visit(new Visitor()
        {
            public void visitFile(File file)
            {
                try
                {
                    if (!file.getAbsolutePath().endsWith(".xml"))
                    {
                        log.info("Skipping: " + file.getAbsolutePath());
                        return;
                    }

                    String className = file.getAbsolutePath()
                        .substring(directory.getAbsolutePath().length() + 1)
                        .replaceFirst("\\.xml$", "")
                        .replace("/", ".");

                    Document doc = builder.build(file);

                    types.put(className, doc);
                }
                catch (Exception ex)
                {
                    throw new RuntimeException(ex);
                }
            }

            public boolean visitDirectory(File dir)
            {
                return true;
            }
        });
    }

    /**
     * Find the super class names listed in the given document.
     * Perhaps this should be cached somewhere???
     * @param doc The document to search in
     * @return The list of found super classes
     */
    protected List<String> getSuperClasses(Document doc)
    {
        List<String> superClasses = new ArrayList<String>();
        Nodes superClassNodes = doc.query("/class/superclass/@name");
        for (int i = 0; i < superClassNodes.size(); i++)
        {
            superClasses.add(superClassNodes.get(i).getValue());
        }
        return superClasses;
    }

    /**
     * Copying mixin functions because Java does not do MI
     * @throws InterruptedException If the threading breaks
     */
    protected void copyMixinFunctions() throws InterruptedException
    {
        ExecutorService exec = Executors.newFixedThreadPool(2);

        for (final Map.Entry<String, Document> entry : types.entrySet())
        {
            exec.execute(new Runnable()
            {
                public void run()
                {
                    String className = entry.getKey();
                    Document doc = entry.getValue();

                    // Search the XML for lines like this from Button.xml:
                    //   <implements direct="1" id="implements:0" loaded="1" name="jsx3.gui.Form"/>

                    // Find all the inherited functions, and trim the ones that are not
                    // from super-classes - leaving the mixins
                    Nodes mixinRefNodes = doc.query("/class/method[@inherited='1']");

                    for (int i = 0; i < mixinRefNodes.size(); i++)
                    {
                        Element mixinRefNode = (Element) mixinRefNodes.get(i);
                        String name = mixinRefNode.getAttributeValue("name");
                        if (name == null)
                        {
                            throw new NullPointerException("Missing name attribute from element: " + mixinRefNode);
                        }

                        // We might be implemented elsewhere
                        String mixinSource = mixinRefNode.getAttributeValue("source");
                        if (mixinSource == null)
                        {
                            mixinSource = className;
                        }

                        boolean isMixin = true;
                        for (String superClass : getSuperClasses(doc))
                        {
                            if (mixinSource.equals(superClass))
                            {
                                isMixin = false;
                            }
                        }

                        if (isMixin)
                        {
                            // replace the method element with the corresponding element from Form.
                            if (!"jsx3.lang.Object".equals(mixinSource))
                            {
                                Document mixinSourceDocument = types.get(mixinSource);
                                Nodes templateNodes = mixinSourceDocument.query("/interface/method[@name='" + name + "']");
                                if (templateNodes.size() != 1)
                                {
                                    log.warn("** Found " + templateNodes.size() + " methods called " + name + " in " + mixinSource + " as a result of reference from " + className);
                                }
                                else
                                {
                                    Element templateNode = (Element) templateNodes.get(0);
                                    Node replacementNode = templateNode.copy();
                                    mixinRefNode.getParent().replaceChild(mixinRefNode, replacementNode);
                                }
                            }
                        }
                    }
                }
            });
        }
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    /**
     * Remove functions implemented by parent
     */
    protected void trimDuplicateMethods()
    {
        SortedSet<String> keys = new TreeSet<String>(types.keySet());
        for (final String className : keys)
        {
            Document doc = types.get(className);

            // Get a list of super classes
            final List<Document> parents = new ArrayList<Document>();
            for (String superClassName : getSuperClasses(doc))
            {
                Document parent = types.get(superClassName);
                if (parent == null)
                {
                    throw new IllegalStateException("Unknown superclass: " + superClassName);
                }
                parents.add(parent);
            }

            // Find all the inherited functions, and trim the ones that are not
            // from super-classes - leaving the mixins
            XomHelper.query(doc, "/class/method[not(@inherited)]", new ElementBlock()
            {
                public void use(final Element inherited)
                {
                    final String inheritedName = inherited.getAttributeValue("name");

                    // Loop over signatures, checking for methods in super-classes
                    for (Document parent : parents)
                    {
                        try
                        {
                            XomHelper.query(parent, "/class/method[not(@inherited)]", new ElementBlock()
                            {
                                public void use(Element parentMethod)
                                {
                                    if (inherited.getParent() == null)
                                    {
                                        return;
                                    }

                                    String parentMethodName = parentMethod.getAttributeValue("name");
                                    if (inheritedName.equals(parentMethodName))
                                    {
                                        inherited.getParent().removeChild(inherited);
                                    }
                                }
                            });
                        }
                        catch (XPathException ex)
                        {
                            log.warn("** Failed XPath for '" + className + "': " + ex.getXPath());
                        }
                    }
                }
            });
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.drapgen.loader.Loader#loadToProject(org.directwebremoting.drapgen.ast.Project)
     */
    public void loadToProject(final Project project)
    {
        SortedSet<String> keys = new TreeSet<String>(types.keySet());
        for (String className : keys)
        {
            String rename = hasLocalVersion(className);
            if (rename != null)
            {
                log.debug(className + ": Skipping: has a local version - " + rename);
                continue;
            }

            if (skipOnInput(className))
            {
                log.debug(className + ": Skipping: marked skip on input");
                continue;
            }

            // Remove documentation classes like index.java, package-summary.java, etc
            if (className.endsWith("index") ||
                className.endsWith("inheritance") ||
                className.endsWith("package-summary"))
            {
                log.debug(className + ": Skipping: it's documentation");
                continue;
            }

            Element root = types.get(className).getRootElement();
            if (root.getAttributeValue("deprecated") != null)
            {
                deprecatedTypes.add(className);
                log.debug(className + ": Skipping: it's deprecated");
                continue;
            }

            final Type type = getType(project, className);
            String typeDocs = readDocumentation(root);
            type.setDocumentation(typeDocs);

            String superClassName = XomHelper.queryValue(root, "superclass[@direct='1']/@name");
            if (superClassName == null)
            {
                type.setSuperClass(getType(project, "jsx3.lang.Object"));
            }
            else
            {
                type.setSuperClass(getType(project, superClassName));
            }

            XomHelper.getChildElements(root, "implements", new ImplementsElementBlock(project, type));
            XomHelper.getChildElements(root, "constructor", new ConstructorElementBlock(project, type));
            XomHelper.getChildElements(root, "method", new MethodElementBlock(project, type));
            XomHelper.getChildElements(root, "field", new FieldElementBlock(project, type));
        }

        // We need to remove the classes that we don't want to generate
        Set<Type> toDelete = new HashSet<Type>();
        types:
        for (Type type : project.getTypes())
        {
            if (!type.getPackageName().startsWith("jsx3"))
            {
                toDelete.add(type);
                continue types;
            }
            for (String manualClassName : SKIP_ON_OUTPUT)
            {
                if (type.getFullName().equals(manualClassName))
                {
                    toDelete.add(type);
                    continue types;
                }
            }
        }
        for (Type type : toDelete)
        {
            project.remove(type);
        }
    }

    /**
     * Find all the constants
     */
    protected class FieldElementBlock extends ProjectElementBlock
    {
        protected FieldElementBlock(Project project, Type type)
        {
            super(project, type);
        }

        public void use(Element fieldElement)
        {
            if (fieldElement.getAttribute("deprecated") != null)
            {
                return;
            }

            String statc = fieldElement.getAttributeValue("static");
            if (statc == null || !statc.equals("1"))
            {
                return;
            }

            String access = fieldElement.getAttributeValue("access");
            if (access == null || !access.equals("public"))
            {
                return;
            }

            String name = fieldElement.getAttributeValue("name");

            Field field = type.createConstant();
            field.setDocumentation(stripToNull(fieldElement.getAttributeValue("text")));
            field.setDocumentation(readDocumentation(fieldElement));
            field.setName(name);
            field.setValue(fieldElement.getAttributeValue("value"));

            Elements typeElements = fieldElement.getChildElements("type");
            if (typeElements.size() == 0)
            {
                log.warn(type.getFullName() + "." + name + "(): Missing type element for constant. Using Object.");
                field.setType(getType(project, "java.lang.Object"));
            }
            else
            {
                if (typeElements.size() > 1)
                {
                    log.warn(type.getFullName() + "." + name + "(): Multiple type element for a constant!. Using first.");
                }

                Element typeElement = typeElements.get(0);
                String typeName = typeElement.getAttributeValue("name");
                field.setType(getType(project, typeName));
            }
        }
    }

    /**
     * Find all the methods that we're interested in
     */
    protected class MethodElementBlock extends ProjectElementBlock
    {
        protected MethodElementBlock(Project project, Type type)
        {
            super(project, type);
        }

        public void use(Element methodElement)
        {
            if (methodElement.getAttribute("deprecated") != null)
            {
                return;
            }

            if (methodElement.getAttribute("inherited") != null)
            {
                return;
            }

            String methodName = methodElement.getAttributeValue("name");
            String debugName = type.getFullName() + "." + methodName;

            String documentation = readDocumentation(methodElement);
            Parameter returnType = readGiReturnType(methodElement, project, debugName);

            // Read a set of param elements attached to a constructor or method

            Set<List<Parameter>> parameters = getGiParamList(methodElement, debugName, project);
            for (List<Parameter> parameter : parameters)
            {
                Method method = type.createMethod();
                method.setDocumentation(documentation);
                method.setName(methodName);
                method.setReturnType(returnType);
                method.setParameters(parameter);
            }
        }
    }

    /**
     * Find all the constructors that we're interested in
     */
    protected class ConstructorElementBlock extends ProjectElementBlock
    {
        protected ConstructorElementBlock(Project project, Type type)
        {
            super(project, type);
        }

        public void use(Element ctorElement)
        {
            if (ctorElement.getAttribute("deprecated") != null)
            {
                return;
            }

            // Read a set of param elements attached to a constructor or method

            String documentation = readDocumentation(ctorElement);
            Set<List<Parameter>> parameters = getGiParamList(ctorElement, type.getFullName() + ".ctor", project);
            for (List<Parameter> parameter : parameters)
            {
                Constructor constructor = type.createConstructor();
                constructor.setDocumentation(documentation);
                constructor.setParameters(parameter);
            }
        }
    }

    /**
     * Helper so we can implement ElementBlock easily
     */
    protected abstract class ProjectElementBlock implements ElementBlock
    {
        /**
         * @param project The project that we are writing to
         * @param type The type we are creating
         */
        protected ProjectElementBlock(Project project, Type type)
        {
            this.project = project;
            this.type = type;
        }

        /**
         * The project that we are writing to
         */
        protected final Project project;

        /**
         * The type we are creating
         */
        protected final Type type;
    }

    /**
     * Process an implements element
     */
    protected class ImplementsElementBlock extends ProjectElementBlock
    {
        protected ImplementsElementBlock(Project project, Type type)
        {
            super(project, type);
        }

        public void use(Element implementsElement)
        {
            String direct = implementsElement.getAttributeValue("direct");
            if (direct == null || !direct.equals("1"))
            {
                return;
            }

            String interfaceName = implementsElement.getAttributeValue("name");
            if (interfaceName == null)
            {
                log.warn(type.getFullName() + ": Skipping direct implements element with no name");
            }
            type.addInterface(getType(project, interfaceName));
        }
    }

    /**
     * We want to alter the names of some classes as we generate them
     * @param project The project that we use to lookup Types
     * @param className The name of the requested class
     * @return A Type representing the name altered type
     */
    protected Type getType(Project project, String className)
    {
        if (isDeprecated(className))
        {
            log.warn("** Request for deprecated type: " + className);
            return project.getType("jsx3.lang.Unavailable");
        }

        String name = checkAlternativeNames(className);

        return project.getType(name);
    }

    /**
     * @param className The name to check for deprecation
     * @return true if the type has been deprecated
     */
    protected boolean isDeprecated(String className)
    {
        for (String deprecatedType : deprecatedTypes)
        {
            if (className.equals(deprecatedType))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * We might need to use differing versions of class names
     */
    protected String checkAlternativeNames(String className)
    {
        for (String[] pairs : JSX3_RENAMES)
        {
            if (className.equals(pairs[0]))
            {
                return pairs[1];
            }
        }
        for (String[] pairs : HAS_LOCAL_VERSION)
        {
            if (className.equals(pairs[0]))
            {
                return pairs[1];
            }
        }
        return className;
    }

    /**
     * Does the given string have a local version?
     */
    protected String hasLocalVersion(String className)
    {
        for (String[] pairs : HAS_LOCAL_VERSION)
        {
            if (className.equals(pairs[0]))
            {
                return pairs[1];
            }
        }
        return null;
    }

    /**
     * Does the given string have a local version?
     */
    protected boolean skipOnInput(String className)
    {
        for (String skip : SKIP_ON_INPUT)
        {
            if (className.equals(skip))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * We might want to move jsx3 classes around the hierarchy, mostly this
     * is due to lack of current support for inner classes
     */
    private static final String[][] JSX3_RENAMES = new String[][]
    {
        { "jsx3.gui.Matrix.BlockMask", "jsx3.gui.matrix.BlockMask" },
        { "jsx3.gui.Matrix.Column", "jsx3.gui.matrix.Column" },
        { "jsx3.gui.Matrix.ColumnFormat", "jsx3.gui.matrix.ColumnFormat" },
        { "jsx3.gui.Matrix.EditMask", "jsx3.gui.matrix.EditMask" },
    };

    /**
     * JSX classes that we don't have a local version of, but we ignore anyway
     */
    private static final String[] SKIP_ON_INPUT = new String[]
    {
        "jsx3.app.Monitor",
        "jsx3.app.PropsBundle",
        "jsx3.lang.NativeError",
        "jsx3.lang.Package",
        "jsx3.net.URIResolver",
        "jsx3.util.Logger",
        "jsx3.util.Logger.AlertHandler",
        "jsx3.util.Logger.FormatHandler",
        "jsx3.util.Logger.Handler",
        "jsx3.util.Logger.Manager",
        "jsx3.util.Logger.MemoryHandler",
        "jsx3.util.Logger.Record",
        "jsx3.xml.XslDocument",
    };

    /**
     * Many JSX classes are not converted because there is a better local
     * version.
     */
    private static final String[][] HAS_LOCAL_VERSION = new String[][]
    {
        { "", "jsx3.lang.Object" },
        { "Object", "jsx3.lang.Object" },
        { "Date", "java.util.Date" },
        { "Iterator", "java.util.Iterator" },
        { "Math", "java.lang.Math" },
        { "RegExp", "java.util.regex.Pattern" },
        { "Number", "Integer" },
        { "String", "String" },
        { "Array", "Object[]" },
        { "Boolean", "Boolean" },
        { "Function", "org.directwebremoting.proxy.CodeBlock" },
        { "HTMLElement", "String" },
        { "HTMLDocument", "String" },
        { "VectorStroke", "String" },
        { "jsx3.Boolean", "Boolean" },
        { "jsx3.app.Locale", "java.util.Locale" },
        { "jsx3.app.Properties", "java.util.Properties" },
        { "jsx3.lang.Class", "Class" },
        { "jsx3.lang.ClassLoader", "ClassLoader" },
        { "jsx3.lang.Exception", "Exception" },
        { "jsx3.lang.IllegalArgumentException", "IllegalArgumentException" },
        { "jsx3.lang.Method", "java.lang.reflect.Method" },
        { "jsx3.net.URI", "java.net.URI" },
        { "jsx3.util.DateFormat", "java.util.DateFormat" },
        { "jsx3.util.Locale", "java.util.Locale" },
        { "jsx3.util.List", "java.util.List" },
        { "jsx3.util.Iterator", "java.util.Iterator" },
        { "jsx3.util.MessageFormat", "java.util.MessageFormat" },
        { "jsx3.util.NumberFormat", "java.util.NumberFormat" },
        { "jsx3.xml.CDF", "jsx3.xml.CdfDocument" },
        { "jsx3.xml.CDF.Document", "jsx3.xml.CdfDocument" },
        { "jsx3.xml.Document", "jsx3.xml.CdfDocument" },
        { "jsx3.xml.Entity", "jsx3.xml.Node" },
    };

    /**
     * Some JSX3 classes have been hand cranked, or should not be generated
     */
    private static final String[] SKIP_ON_OUTPUT = new String[]
    {
        "jsx3.lang.Object",
        "jsx3.lang.Unavailable",
        "jsx3.net.URIResolver",
        "jsx3.xml.CdfDocument",
        "jsx3.xml.Node",
    };

    /**
     * Read the text child element and set the drapgen AST element with it's
     * content
     * @param xomElement the XML element to read from
     */
    protected String readDocumentation(Element xomElement)
    {
        Element textElement = xomElement.getFirstChildElement("text");
        if (textElement != null)
        {
            return stripToNull(textElement.getValue());
        }
        else
        {
            return null;
        }
    }

    /**
     * Read a set of param elements attached to a constructor or method
     * @param element The XOM element to read from
     * @param debugName
     * @param project
     * @return A list of found parameters
     */
    protected Set<List<Parameter>> getGiParamList(Element element, final String debugName, final Project project)
    {
        // The types used by this method are somewhat funky. We first create a
        // list of parameters with the alternative types for each in a stack.
        // We then need to return a set where all the different combinations of
        // these alternatives are represented. We use Set<List<>> and
        // List<Stack<>> to help make it clearer what we are working on.

        // So GI has a list of parameters for this subroutine. Each parameter
        // has a set of types that it can be
        final List<Stack<Parameter>> parametersWithOptions = new ArrayList<Stack<Parameter>>();
        XomHelper.getChildElements(element, "param", new ElementBlock()
        {
            public void use(Element paramElement)
            {
                String documentation = stripToNull(paramElement.getAttributeValue("text"));
                String name = paramElement.getAttributeValue("name");

                Set<Type> alternatives = new HashSet<Type>();

                Elements typeElements = paramElement.getChildElements("type");
                if (typeElements.size() == 0)
                {
                    String typeName = "java.lang.Object";
                    if (name.startsWith("str"))
                    {
                        typeName = "String";
                    }
                    else if (name.startsWith("b"))
                    {
                        typeName = "boolean";
                    }
                    else if (name.startsWith("int"))
                    {
                        typeName = "int";
                    }
                    log.warn(debugName + "(): Missing type element for parameter '" + name + "'. Guessing at '" + typeName + "' from parameter name.");
                    alternatives.add(getType(project, typeName));
                }
                else
                {
                    for (int i = 0; i < typeElements.size(); i++)
                    {
                        Element typeElement = typeElements.get(i);
                        String typeName = typeElement.getAttributeValue("name");
                        if (typeName.equals("function"))
                        {
                            typeName = "Function";
                            log.warn(debugName + "(): Types of parameter '" + name + "' includes 'function', it probably should be 'Function'");
                        }
                        alternatives.add(getType(project, typeName));
                    }
                }

                Stack<Parameter> p = new Stack<Parameter>();
                for (Type alternative : alternatives)
                {
                    Parameter parameter = new Parameter(project);
                    parameter.setDocumentation(documentation);
                    parameter.setName(name);
                    parameter.setType(alternative);
                    p.add(parameter);
                }

                parametersWithOptions.add(p);
            }
        });

        Set<List<Parameter>> parameterCombinations = new HashSet<List<Parameter>>();

        if (parametersWithOptions.size() > 0)
        {
            recurseOverTypesInParameter(parametersWithOptions, 0, new LinkedList<Integer>(), parameterCombinations);
        }
        else
        {
            // There is one combination - that with no parameters
            parameterCombinations.add(new ArrayList<Parameter>());
        }

        return parameterCombinations;
    }

    /**
     * @param parametersWithOptions
     * @param paramNumber
     * @param combinations
     */
    private void recurseOverTypesInParameter(List<Stack<Parameter>> parametersWithOptions, int paramNumber, LinkedList<Integer> typeList, Set<List<Parameter>> combinations)
    {
        // We do *something* for each of the alternatives in the set of
        // parameters for this type
        Stack<Parameter> alternatives = parametersWithOptions.get(paramNumber);
        for (int i = 0; i < alternatives.size(); i++)
        {
            LinkedList<Integer> nextTypeList = new LinkedList<Integer>(typeList);
            nextTypeList.add(i);

            if (paramNumber == parametersWithOptions.size() - 1)
            {
                // If this is the last parameter then *something* is creating
                // an entry in the output set
                List<Parameter> combination = new ArrayList<Parameter>();
                for (int paramIndex = 0; paramIndex < nextTypeList.size(); paramIndex++)
                {
                    Integer selection = nextTypeList.get(paramIndex);
                    Parameter selected = parametersWithOptions.get(paramIndex).get(selection);
                    combination.add(selected);
                }
                combinations.add(combination);
            }
            else
            {
                // If this is not the last parameter then *something* is to call
                // ourself to loop over the options for the next parameter
                recurseOverTypesInParameter(parametersWithOptions, paramNumber + 1, nextTypeList, combinations);
            }
        }
    }

    /**
     * Read a return type element attached to a method.
     * For some reason calling method.toString as part of the log.warn messages
     * crashes the VM on mac-os. Why???
     * @param element The XOM element to read from
     */
    protected Parameter readGiReturnType(Element element, Project project, final String name)
    {
        Parameter returnType = new Parameter(project);

        //Project project = method.getParent().getProject();
        Elements returnElements = element.getChildElements("return");
        if (returnElements.size() == 0)
        {
            returnType.setType(getType(project, "void"));
            return returnType;
        }
        else
        {
            if (returnElements.size() > 1)
            {
                log.warn(name + "(): More than one return element. Using first.");
            }

            Element returnElement = returnElements.get(0);

            String documentation = returnElement.getAttributeValue("text");
            returnType.setDocumentation(stripToNull(documentation));

            Elements typeElements = returnElement.getChildElements("type");
            if (typeElements.size() == 0)
            {
                log.warn(name + "(): Missing return element. Using Object.");
                Type type = getType(project, "java.lang.Object");
                returnType.setType(type);
                return returnType;
            }
            else
            {
                if (typeElements.size() > 1)
                {
                    log.warn(name + "(): More than one type element in return. Using first. Are the semantics of this clear?");
                }

                Element typeElement = typeElements.get(0);
                String typeName = typeElement.getAttributeValue("name");
                Type type = getType(project, typeName);
                returnType.setType(type);
                return returnType;
            }
        }
    }

    /**
     * Utility for simplifying documentation
     * @param input The string to call {@link String#trim()} on before swapping
     * empty replies for null
     * @return A non blank, trimmed string, possibly null
     */
    public static String stripToNull(String input)
    {
        if (input == null)
        {
            return null;
        }
        String output = input.trim();
        if (output.length() == 0)
        {
            return null;
        }
        return output;
    }

    /**
     * What classes have been deprecated?
     */
    protected Set<String> deprecatedTypes = new HashSet<String>();

    /**
     * Our cache of loaded GI documents
     */
    protected Map<String, Document> types;

    /**
     * The XOM document builder
     */
    protected Builder builder = new Builder();

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(GiLoader.class);
}
