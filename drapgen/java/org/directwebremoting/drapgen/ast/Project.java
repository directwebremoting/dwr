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
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.directwebremoting.drapgen.loader.gi.GiLoader;
import org.directwebremoting.util.Logger;

/**
 * A Project is a collection of {@link Type}s that we will convert to reverse
 * ajax proxies.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Project
{
    /**
     * Save the types in the current project into XML files in the given
     * directory.
     * This helps us separate the process of creating the AST (from the
     * Javascript) from the process of generating the Java from the AST
     * @param directory The place to write to
     * @throws IOException If the save process fails
     */
    public void save(File directory) throws IOException
    {
        SortedSet<String> keys = new TreeSet<String>(types.keySet());
        for (String className : keys)
        {
            Type type = types.get(className);
            type.save(directory);
        }
    }

    /**
     * Load the types in the given directory into this Project.
     * This method does *not* call clear before it loads Types
     * @param directory The place to read from
     * @throws IOException If the read fails.
     */
    public void load(File directory) throws IOException
    {
        for (File file : directory.listFiles())
        {
            if (file.getName().endsWith(".xml"))
            {
                Type type = new Type(this, file);
                add(type);
            }
        }
    }

    /**
     * Allow users to dig into the code in a project
     * @param visitor The object to pass around the code in a project
     */
    public void visit(Visitor visitor)
    {
        boolean dig = visitor.visitEnter(this);
        if (dig)
        {
            for (Type type : types.values())
            {
                type.visit(visitor);
            }
            visitor.visitLeave(this);
        }
    }

    /**
     * Useful if we have a classname that we know must be defined somewhere,
     * but might not have been defined yet.
     * @param className The potentially existing class name
     * @return A newly created or existing Type
     */
    public Type getType(String className)
    {
        Type type = types.get(className);

        if (className.indexOf(" ") != -1)
        {
            log.warn("Creating class called: " + className);
        }

        if (type == null)
        {
            type = new Type(this, className);
            add(type);
        }

        return type;
    }

    /**
     * Is the given (full) class name one that another class claims as it's
     * ancestor?
     * @param name The class name to search for
     * @return true iff the class has children
     */
    public boolean isSuperClass(String name)
    {
        if (superClasses == null)
        {
            superClasses = new HashSet<String>();
            for (Type type : types.values())
            {
                Type superClass = type.getSuperClass();
                if (superClass != null)
                {
                    superClasses.add(superClass.getFullName());
                }

                for (Type iface : type.getInterfaces())
                {
                    superClasses.add(iface.getFullName());
                }
            }
        }

        return superClasses.contains(name);
    }

    /**
     * @return An class name with native types replaced by Object types
     */
    public String asObject(String maybeNative)
    {
        if (maybeNative.equals("int"))
        {
            return "Integer";
        }
        else if (maybeNative.equals("char"))
        {
            return "Character";
        }
        else if (maybeNative.equals("boolean"))
        {
            return "Boolean";
        }
        else if (maybeNative.equals("long"))
        {
            return "Long";
        }
        else if (maybeNative.equals("float"))
        {
            return "Float";
        }
        else if (maybeNative.equals("double"))
        {
            return "Double";
        }
        else if (maybeNative.equals("short"))
        {
            return "Short";
        }
        else if (maybeNative.equals("byte"))
        {
            return "Byte";
        }
        return maybeNative;
    }

    /**
     * @see java.util.Set#clear()
     */
    public void clear()
    {
        types.clear();
    }

    /**
     * @see java.util.Set#contains(java.lang.Object)
     */
    public boolean contains(String className)
    {
        return types.containsKey(className);
    }

    /**
     * @see java.util.Set#isEmpty()
     */
    public boolean isEmpty()
    {
        return types.isEmpty();
    }

    /**
     * @return Read-only collection of all our types
     */
    public Collection<Type> getTypes()
    {
        return Collections.unmodifiableCollection(types.values());
    }

    /**
     * @see java.util.Set#remove(java.lang.Object)
     */
    public void remove(Type type)
    {
        types.remove(type.getFullName());
    }

    /**
     * @see java.util.Set#size()
     */
    public int size()
    {
        return types.size();
    }

    /**
     * @see java.util.Set#add(java.lang.Object)
     */
    private void add(Type type)
    {
        types.put(type.getFullName(), type);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o)
    {
        return types.equals(o);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return types.hashCode();
    }

    /**
     * The store of the types we are about to generate from
     */
    private Map<String, Type> types = new HashMap<String, Type>();

    /**
     * We cache the names of the super classes that we've found
     */
    private Set<String> superClasses;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(GiLoader.class);
}
