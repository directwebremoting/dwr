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
package org.directwebremoting.drapgen.ast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.Serializer;

import org.directwebremoting.util.LocalUtil;

import static org.directwebremoting.drapgen.ast.SerializationStrings.*;

/**
 * A class that represents a Java Class with methods to help us generate code
 * from it
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Type extends Element
{
    /**
     * All classes must know what project they are part of
     * @param project The container project
     */
    protected Type(Project project, String fullName)
    {
        this.project = project;

        int lastDot = fullName.lastIndexOf(".");
        if (lastDot > fullName.length() - 1)
        {
            throw new IllegalArgumentException("Illegal name: " + fullName);
        }

        if (lastDot < 1)
        {
            name = fullName;
            packageName = "";
        }
        else
        {
            name = fullName.substring(lastDot + 1);
            packageName = fullName.substring(0, lastDot);
        }
    }

    /**
     * @param project
     * @param file
     */
    protected Type(Project project, File file) throws IOException
    {
        this.project = project;
        load(file);
    }

    /**
     * Save an XML representation to the given directory
     * @param directory The parent for the file for this type
     * @throws IOException If the save process fails
     */
    public void save(File directory) throws IOException
    {
        directory.mkdirs();

        String filename = getFullName() + ".xml";
        File file = new File(directory, filename);
        FileOutputStream out = new FileOutputStream(file);
        Serializer serializer = new Serializer(out);
        serializer.setIndent(2);
        serializer.write(toXomDocument());
    }

    /**
     * Load our definition from the given file
     * @param file The file to load data from
     * @throws IOException If the load process fails
     */
    private void load(File file) throws IOException
    {
        try
        {
            Builder builder = new Builder();
            Document doc = builder.build(file);
            fromXomDocument(doc);
        }
        catch (ParsingException ex)
        {
            throw new IOException(ex.toString());
        }
    }

    /**
     * Create a XOM document from this
     * @return a Document representing this Type
     */
    private Document toXomDocument()
    {
        nu.xom.Element root = new nu.xom.Element(TYPE);
        root.addAttribute(new Attribute(PACKAGE, packageName));
        root.addAttribute(new Attribute(NAME, name));

        writeDocumentation(root);

        if (superClass != null)
        {
            root.addAttribute(new Attribute(EXTENDS, superClass.getName()));
        }

        String[] names = new String[interfaces.size()];
        int i = 0;
        for (Type interfce : interfaces)
        {
            names[i++] = interfce.getFullName();
        }
        String ifaceList = LocalUtil.join(names, ", ");

        root.addAttribute(new Attribute(IMPLEMENTS, ifaceList));

        for (Constructor constructor : constructors)
        {
            root.appendChild(constructor.toXomElement());
        }

        for (Method method : methods)
        {
            root.appendChild(method.toXomElement());
        }

        for (Field constant : constants)
        {
            root.appendChild(constant.toXomElement());
        }

        return new Document(root);
    }

    /**
     * Load this type with data from the given document
     * @param doc The document to load from
     */
    private void fromXomDocument(Document doc)
    {
        nu.xom.Element root = doc.getRootElement();
        packageName = root.getAttributeValue(PACKAGE);
        name = root.getAttributeValue(NAME);

        readDocumentation(root);

        String superClassName = root.getAttributeValue(EXTENDS);
        superClass = project.getType(superClassName);

        String ifaceList = root.getAttributeValue(IMPLEMENTS);
        interfaces.clear();
        for (String interfce : ifaceList.split(", "))
        {
            interfaces.add(project.getType(interfce));
        }

        Elements childElements = root.getChildElements(CONSTRUCTOR);
        constructors.clear();
        for (int i = 0; i < childElements.size(); i++)
        {
            Constructor constructor = new Constructor(this);
            constructor.fromXomDocument(childElements.get(i));
            constructors.add(constructor);
        }

        childElements = root.getChildElements(METHOD);
        methods.clear();
        for (int i = 0; i < childElements.size(); i++)
        {
            Method method = new Method(this);
            method.fromXomDocument(childElements.get(i));
            methods.add(method);
        }

        childElements = root.getChildElements(CONSTANT);
        constants.clear();
        for (int i = 0; i < childElements.size(); i++)
        {
            Field field = new Field(this);
            field.fromXomDocument(childElements.get(i));
            constants.add(field);
        }
    }

    /**
     * @param visitor
     */
    public void visit(Visitor visitor)
    {
        boolean dig = visitor.visitEnter(this);
        if (dig)
        {
            for (Field constant : constants)
            {
                visitor.visit(constant);
            }

            for (Constructor constructor : constructors)
            {
                visitor.visit(constructor);
            }

            for (Method method : methods)
            {
                visitor.visit(method);
            }

            visitor.visitLeave(this);
        }
    }

    /**
     * @return A new constructor
     */
    public Constructor createConstructor()
    {
        Constructor constructor = new Constructor(this);
        constructors.add(constructor);
        return constructor;
    }

    /**
     * @return A new constructor
     */
    public Method createMethod()
    {
        Method method = new Method(this);
        methods.add(method);
        return method;
    }

    /**
     * @return A new field
     */
    public Field createConstant()
    {
        Field constant = new Field(this);
        constants.add(constant);
        return constant;
    }

    /**
     * @param type The new interface to add
     */
    public void addInterface(Type type)
    {
        interfaces.add(type);
    }

    /**
     * @return the package name and class name separated by a '.'
     */
    public String getFullName()
    {
        if (packageName.length() == 0)
        {
            return name;
        }
        else
        {
            return packageName + "." + name;
        }
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the packageName
     */
    public String getPackageName()
    {
        return packageName;
    }

    /**
     * @return the superClass
     */
    public Type getSuperClass()
    {
        return superClass;
    }

    /**
     * @param superClass the superClass to set
     */
    public void setSuperClass(Type superClass)
    {
        this.superClass = superClass;
    }

    /**
     * @return the interfaces
     */
    public List<Type> getInterfaces()
    {
        return interfaces;
    }

    /**
     * @return the constructors
     */
    public List<Constructor> getConstructors()
    {
        return Collections.unmodifiableList(constructors);
    }

    /**
     * @return the methods
     */
    public List<Method> getMethods()
    {
        return Collections.unmodifiableList(methods);
    }

    /**
     * @return the constants
     */
    public List<Field> getConstants()
    {
        return Collections.unmodifiableList(constants);
    }

    /**
     * @return the project
     */
    public Project getProject()
    {
        return project;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return packageName.hashCode() + name.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        Type that = (Type) obj;

        if (!this.packageName.equals(that.packageName))
        {
            return false;
        }

        if (!this.name.equals(that.name))
        {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getFullName();
    }

    private Project project;

    private String name;

    private String packageName;

    private Type superClass;

    private List<Type> interfaces = new ArrayList<Type>();

    private List<Constructor> constructors = new ArrayList<Constructor>();

    private List<Method> methods = new ArrayList<Method>();

    private List<Field> constants = new ArrayList<Field>();
}
